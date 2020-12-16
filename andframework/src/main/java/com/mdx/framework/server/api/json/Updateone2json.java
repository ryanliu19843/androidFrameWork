package com.mdx.framework.server.api.json;

import com.mdx.framework.cache.DataStoreCacheManage;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.config.ApiConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.UpdateOne;
import com.mdx.framework.server.api.impl.ApiRead;
import com.mdx.framework.server.api.protobuf.IntenetRead2Protobuf;
import com.mdx.framework.server.api.protobuf.Son2protobuf;
import com.mdx.framework.utility.Device;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class Updateone2json implements ApiRead {
    public JsonData mBuild;

    public Updateone2json() {
    }

    /**
     * 初始化接口参数<BR>
     * 初始化接口调用参数
     *
     * @param params
     * @param data
     * @see com.mdx.framework.server.api.UpdateOne#initObj(Object, Object)
     */

    @Override
    public Object initObj(UpdateOne updateone) {

        String id = updateone.getId();
        String url = updateone.getUrl();
        Object params = updateone.params;
        Object data = updateone.data;


        String[][] autoapiparams = ApiConfig.getAutoApiInitParams(id, id, url, params, data);
        MLog.D(MLog.SYS_RUN, "api json:", id, url, autoapiparams, params, updateone.getPageParams(), data);

        ArrayList<String[]> mr = new ArrayList<String[]>();
        boolean haspage = updateone.haspage();
        if (autoapiparams != null) {
            for (int i = 0; i < autoapiparams.length; i++) {
                String[] sts = autoapiparams[i];
                haspage = addValue(mr, sts, updateone) || haspage;
            }
        }
        if (params instanceof String[][]) {
            String[][] strs = (String[][]) params;
            for (int i = 0; i < strs.length; i++) {
                String[] sts = strs[i];
                haspage = addValue(mr, sts, updateone) || haspage;
            }
        }
        if (data != null && data instanceof String[][]) {
            String[][] datas = (String[][]) data;
            for (String[] sts : datas) {
                if (sts.length > 1) {
                    haspage = addValue(mr, sts, updateone) || haspage;
                }
            }
        }
        updateone.setPostdata(mr);
        if (updateone.isHasPageParams()) {
            if (updateone.getPageParams() != null) {
                for (int i = 0; i < updateone.getPageParams().length; i++) {
                    String[] sts = updateone.getPageParams()[i];
                    haspage = addValue(mr, sts, updateone);
                }
            }
        }
        setFileMd5(updateone);
        if (haspage) {
            {
                mr.add(new String[]{updateone.getPageName(), updateone.getPage() + ""});
            }
            {
                mr.add(new String[]{updateone.getPageSizeName(), updateone.getPageSize() + ""});
            }
        }
        return mr;
    }

    private boolean addValue(ArrayList<String[]> mr, String[] sts, UpdateOne updateone) {
        boolean retn = false;
        if (sts.length > 1) {
            if (sts[0] == null || sts[1] == null) {
            } else {
                if (!sts[0].equals(updateone.getPageName())) {
                    mr.add(sts);
                } else {
                    try {
                        updateone.setPage(Long.valueOf(sts[1]));
                        retn = true;
                    } catch (Exception e) {
                        mr.add(sts);
                    }
                }
                updateone.getMap().put(sts[0], (String) updateone.fitValue(sts[1]));
            }
        }
        return retn;
    }

    private void setFileMd5(UpdateOne updateone) {
        String str = UUID.randomUUID().toString();
        if (updateone.getPostdata() != null && updateone.getPostdata() instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<String[]> pub = (ArrayList<String[]>) updateone.getPostdata();

            ComparatorDevice cd = new ComparatorDevice();
            Collections.sort(pub, cd);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                bos.write((updateone.getUrl() + "__").getBytes());
                for (String[] st : pub) {
                    bos.write((st[0] + "=" + st[1] + "&").getBytes());
                }
                updateone.setMd5str(Md5.md5(bos.toByteArray()) + "_" + updateone.getPage() + "_" + updateone.getPageSize());
            } catch (Exception e) {
                updateone.setSaveAble(false);
                updateone.setMd5str(str);
            }
        } else {
            updateone.setSaveAble(false);
            updateone.setMd5str(str);
        }
    }

    /**
     * 创建接口映射<BR>
     * 通过类名创建接口映射
     *
     * @param classname
     * @see com.mdx.framework.server.api.UpdateOne#createRequest(String)
     */

    @Override
    public void createRequest(UpdateOne updateone, String classname) {
        updateone.mbuild = getRequest(classname);
    }

    /**
     * 根据设置获取接口数据<BR>
     * 根据接口配置调用接口数据并返回接口son
     *
     * @return
     * @see com.mdx.framework.server.api.UpdateOne#getSon()
     */


    @Override
    public Son readSon(UpdateOne updateone) {
        Son son = null, cacheson = null;
        int network = Device.getNetWorkSpeed();
        if (updateone.isSaveAble()) {
            byte[] bytes = DataStoreCacheManage.readByte(updateone.getMd5str(), network == 0 ? System.currentTimeMillis() : updateone.getCacheTime());
            if (bytes != null) {
                synchronized (this) {
                    cacheson = new Son2Json(new String(bytes), updateone);
                    cacheson.isCacheSon = true;
                }
                if (cacheson.getError() == 0 && (updateone.userCache() || network == 0)) {
                    return cacheson;
                }
            }
        }

        @SuppressWarnings("unchecked")
        ArrayList<String[]> list = ((ArrayList<String[]>) updateone.getPostdata());
        String[][] params = new String[list.size()][];
        list.toArray(params);

        IntenetRead2Json ir = new IntenetRead2Json(updateone.mEncode, updateone.mUpencode);
        updateone.setServerIntermit(ir);
        try {
            String value = "";
            if (updateone.getType() % 100 / 10 == 1) {
                value = ir.get(updateone.getUrl(), params);
            } else {
                value = ir.post(updateone.getUrl(), params);
            }
            synchronized (this) {
                son = new Son2Json(value, updateone);
                if (updateone.canSave(son.getError())) {
                    DataStoreCacheManage.save(updateone.getMd5str(), value.getBytes());
                }
            }
        } catch (MException e) {
            son = new Son(e.getCode(), e.getMessage(), updateone);
            son.mParams = updateone.getMap();
        }
        if (cacheson != null) {
            son.cacheSon = cacheson;
        }
        return son;
    }

    @Override
    public void saveBuild(UpdateOne updateone, Object obj) {
        if (obj == null) {
            return;
        }
        DataStoreCacheManage.save(updateone.getMd5str(), obj.toString().getBytes());
    }

    @Override
    public Son readBuild(UpdateOne updateone) {
        byte[] bytes = DataStoreCacheManage.readByte(updateone.getMd5str(), System.currentTimeMillis());
        if (bytes == null) {
            return new Son(9090, "storenone", updateone);
        }
        return new Son2Json(new String(bytes), updateone);
    }

    private JsonData getRequest(String clas) {
        if (clas == null || clas.length() == 0) {
            return null;
        } else {
            if (clas.startsWith(".")) {
                clas = ApiConfig.getPackage() + clas;
            }
            Class<?> cls;
            try {
                try {
                    cls = Class.forName(clas);
                    Object obj = cls.newInstance();
                    if (obj instanceof JsonData) {
                        return (JsonData) obj;
                    } else {
                        throw new IllegalAccessError("class error,pls check");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IllegalAccessError("Api error,pls check initFrame");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalAccessError("Api error,pls check initFrame");
            }
        }
    }

    public static class ComparatorDevice implements Comparator<String[]> {

        @Override
        public int compare(String[] lhs, String[] rhs) {
            return lhs[0].compareTo(rhs[0]);
        }
    }

}

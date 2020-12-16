package com.mdx.framework.server.api.protobuf;

//import com.google.protobuf.GeneratedMessage;
import com.mdx.framework.cache.DataStoreCacheManage;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.data.Method;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.config.ApiConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.UpdateOne;
import com.mdx.framework.server.api.base.Msg_Post;
import com.mdx.framework.server.api.base.Msg_Request;
import com.mdx.framework.server.api.base.Msg_Retn;
import com.mdx.framework.server.api.impl.ApiRead;
import com.mdx.framework.utility.Device;
import com.squareup.wire.Message;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import okio.ByteString;

public class UpdateOne2Protobuf implements ApiRead {

    public UpdateOne2Protobuf() {
    }


    @SuppressWarnings("rawtypes")
    @Override
    // //String id,String url,Object params, Object data,
    public Object initObj(UpdateOne upo) {

        ArrayList<UpdateOne> ups = new ArrayList<UpdateOne>();
        ups.add(upo);
        ups.addAll(upo.updateones);
        Msg_Request mreq = null;
        for (UpdateOne updateone : ups) {
            Msg_Request mr = null;
            boolean haspage = updateone.haspage();

            String id = updateone.getId();
            String url = updateone.getUrl();
            Object params = updateone.params;
            Object data = updateone.data;
            String[][] autoapiparams = ApiConfig.getAutoApiInitParams(id, id, url, params, data);

            StringBuffer sb = new StringBuffer();
            for (String[] strs : autoapiparams) {
                for (int i = 0; i < strs.length; i++) {
                    sb.append(strs[i]);
                    if (i != strs.length - 1) {
                        sb.append("=");
                    }
                }
                sb.append("&");
            }

            if (updateone.initObj && updateone.autoapiparamsStr.equals(sb.toString()) && updateone.getPostdata() != null) {
                mr = (Msg_Request) updateone.getPostdata();
                if (mr.requests != null) {
                    mr.requests.clear();
                }
            } else {
                if (!updateone.autoapiparamsStr.equals(sb.toString())) {
                    updateone.autoapiparamsStr = sb.toString();
                }
                mr = new Msg_Request();
                MLog.D(MLog.SYS_RUN, "api protobuf:", id, url, autoapiparams, params, updateone.getPageParams(), data);
                mr.posts = new ArrayList<Msg_Post>();
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
                    if (data != null) {
                        if (data instanceof Message || data instanceof Message.Builder) {
                            Message pub = null;
                            if (data instanceof Message.Builder) {
                                pub = ((Message.Builder) data).build();
                            } else {
                                pub = (Message) data;
                            }
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            try {
                                Method.protobufSeralize(pub, bos);
                                mr.requestMessage = ByteString.of(bos.toByteArray());
                            } catch (Exception e) {
                                throw new IllegalStateException("Error Data to post!");
                            }
                        }/* else if (data instanceof GeneratedMessage.Builder) {
                            GeneratedMessage.Builder<?> pub = (GeneratedMessage.Builder<?>) data;
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            try {
                                Method.protobufSeralize(pub, bos);
                                mr.requestMessage = ByteString.of(bos.toByteArray());
                            } catch (Exception e) {
                                throw new IllegalStateException("Error Data to post!");
                            }
                        } */else {
                            throw new IllegalStateException("Error Data to post!");
                        }
                    }
                } else {
                    if (params instanceof Message || params instanceof Message.Builder) {
                        Message pub = null;
                        if (params instanceof Message.Builder) {
                            pub = ((Message.Builder) params).build();
                        } else {
                            pub = (Message) params;
                        }
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            Method.protobufSeralize(pub, bos);
                            mr.requestMessage = ByteString.of(bos.toByteArray());
                        } catch (Exception e) {
                            throw new IllegalStateException("Error Data to post!");
                        }
                    } /*else if (params instanceof GeneratedMessage.Builder) {
                        GeneratedMessage.Builder<?> pub = (GeneratedMessage.Builder<?>) params;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            Method.protobufSeralize(pub, bos);
                            mr.requestMessage = ByteString.of(bos.toByteArray());
                        } catch (Exception e) {
                            throw new IllegalStateException("Error Data to post!");
                        }
                    } */else if (params != null) {
                        throw new IllegalStateException("Error Data to post!");
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
                        Msg_Post mrr = new Msg_Post();
                        mrr.name = updateone.getPageName();
                        mrr.value = updateone.getPage() + "";
                        mr.posts.add(mrr);
                    }
                    {
                        Msg_Post mrr = new Msg_Post();
                        mrr.name = updateone.getPageSizeName();
                        mrr.value = updateone.getPageSize() + "";
                        mr.posts.add(mrr);
                    }
                }
                mr.url = updateone.mMethodUrl;
            }

            if (mreq == null) {
                mreq = mr;
                mreq.requests.clear();
            } else {
                mreq.requests.add(mr);
            }
        }
        return mreq;
    }

    private boolean addValue(Msg_Request mr, String[] sts, UpdateOne updateone) {
        boolean retn = false;
        if (sts.length > 1) {
            if (sts[0] == null || sts[1] == null) {
            } else {
                if (!sts[0].equals(updateone.getPageName())) {
                    Msg_Post mrr = new Msg_Post();
                    mrr.name = sts[0];
                    mrr.value = (String) updateone.fitValue(sts[1]);
                    mr.posts.add(mrr);
                } else {
                    try {
                        updateone.setPage(Long.valueOf(sts[1]));
                        retn = true;
                    } catch (Exception e) {
                        Msg_Post mrr = new Msg_Post();
                        mrr.name = sts[0];
                        mrr.value = sts[1];
                        mr.posts.add(mrr);
                    }
                }
                updateone.getMap().put(sts[0], sts[1]);
            }
        }
        return retn;
    }

    @SuppressWarnings("rawtypes")
    private void setFileMd5(UpdateOne updateone) {
        String str = UUID.randomUUID().toString();
        if (updateone.getPostdata() != null) {
            Object pub = null;
            if (updateone.getPostdata() instanceof Message) {
                pub = (Message) updateone.getPostdata();
            } else if (updateone.getPostdata() instanceof Message.Builder) {
                pub = ((Message.Builder) updateone.getPostdata()).build();
            }/* else if (updateone.getPostdata() instanceof GeneratedMessage.Builder) {
                pub = (GeneratedMessage.Builder<?>) updateone.getPostdata();
            }*/
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                bos.write((updateone.getUrl() + "__").getBytes());
                Method.protobufSeralize(pub, bos);
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


    @Override
    public void createRequest(UpdateOne updateOne, String classname) {
        updateOne.mbuild = getRequest(classname);
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
                    cacheson = new Son2protobuf(bytes, updateone);
                    cacheson.isCacheSon = true;
                }
                if (cacheson.getError() == 0 && (updateone.userCache() || network == 0)) {
                    return cacheson;
                }
            }
        }
        Object build = updateone.getPostdata();
        IntenetRead2Protobuf ir = new IntenetRead2Protobuf();
        updateone.setServerIntermit(ir);
        try {
            byte[] bytes = ir.post(updateone.getUrl(), build);
            synchronized (this) {
                son = new Son2protobuf(bytes, updateone);
                if (updateone.canSave(son.getError())) {
                    DataStoreCacheManage.save(updateone.getMd5str(), bytes);
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

    @SuppressWarnings("rawtypes")
    @Override
    public void saveBuild(UpdateOne updateone, Object obj) {

        if (obj instanceof Message.Builder) {
            try {
                Msg_Retn.Builder retn = new Msg_Retn.Builder();
                retn.errorCode = 0;
                retn.errorMsg = "";
                Message.Builder builder = (Message.Builder) obj;
                retn.retnMessage = ByteString.of(Method.protobufSeralize(builder));
                DataStoreCacheManage.save(updateone.getMd5str(), Method.protobufSeralizeDes(retn));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } /*else if (obj instanceof GeneratedMessage.Builder) {
            try {
                Msg_Retn.Builder retn = new Msg_Retn.Builder();
                retn.errorCode = 0;
                retn.errorMsg = "";
                GeneratedMessage.Builder<?> builder = (GeneratedMessage.Builder<?>) obj;
                retn.retnMessage = ByteString.of(Method.protobufSeralize(builder));
                DataStoreCacheManage.save(updateone.getMd5str(), Method.protobufSeralizeDes(retn));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public Son readBuild(UpdateOne updateone) {
        byte[] bytes = DataStoreCacheManage.readByte(updateone.getMd5str(), System.currentTimeMillis());
        if (bytes == null) {
            return new Son(9090, "storenone", updateone);
        }
        Son son = new Son2protobuf(bytes, updateone);
        return son;
    }

    private Object getRequest(String clas) {
        if (clas == null || clas.length() == 0) {
            return null;
        } else {
            if (clas.startsWith(".")) {
                clas = ApiConfig.getPackage() + clas;
            }
            Class<?> cls;
            try {
                // cls = Class.forName(clas + "$Builder");
                cls = Class.forName(clas);
                try {
                    Object obj = cls.newInstance();
                    if (obj instanceof Message) {
                        return obj;
                    } else {
                        throw new IllegalAccessError("Api error,pls check initFrame");
                    }
                } catch (Exception e) {
                    cls = Class.forName(clas);
                    java.lang.reflect.Method method = cls.getMethod("newBuilder");
                    Object obj = method.invoke(cls);
                    /*if (obj instanceof GeneratedMessage.Builder) {
                        return obj;
                    } else {
                        throw new IllegalAccessError("Api error,pls check initFrame");
                    }*/
                    throw new IllegalAccessError("Api error,pls check initFrame");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalAccessError("Api error,pls check initFrame");
            }
        }
    }
}

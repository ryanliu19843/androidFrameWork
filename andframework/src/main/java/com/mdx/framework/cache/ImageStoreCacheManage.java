package com.mdx.framework.cache;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.Util;
import com.mdx.framework.utility.tools.NetStatistics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageStoreCacheManage {
    public static String FILEDATASTR="TEMP_FILES_IMG";
    public static FileTempManager FILEMANAGER = new FileTempManager(FILEDATASTR,"MaxImageSize", new FileTempManager.OnDelete() {
        
        @Override
        public void onDelete(String str) {
        }
    });
    
    public static Map<String, String> removeMap = new HashMap<String, String>();
    
    public final static String FILEHOUZ = ".pngtemp";
    
    public final static String path = "image";
    
    /**
     * 查找缓存是否存在
     * 
     * @author ryan
     * @Title: check
     * @Description: TODO
     * @param url
     * @param w
     * @param h
     * @return
     * @throws
     */
    public static File check(String url, float w, float h) {
        String fn = getFilename(url, false, w, h);
        String filename = getFilename(url, false, w, h);
        if (removeMap.containsKey(fn) || removeMap.containsValue(filename)) {
            return null;
        }
        File file = Util.getDPath(Frame.CONTEXT, path);
        if (file != null && file.isDirectory()) {
            File f = Util.getPath(Frame.CONTEXT, path+"/"+Md5.mD5(url), filename);
            
            if (f.exists()) {
                return f;
            } else {
                File retn=getBig(url, w, h);
                return retn;
            }
        }
        return null;
    }
    
    /**
     * 获取比较大的图片
     * 
     * @author ryan
     * @Title: getBig
     * @Description: TODO
     * @param url
     * @param w
     * @param h
     * @return
     * @throws
     */
    public static File getBig(String url, float w, float h) {
        String fn = getFilename(url, true, w, h);
        File fdir = Util.getDPath(Frame.CONTEXT,  path+"/"+Md5.mD5(url));
        String fm = fn;
        int jind = 0, xind = 0, dind = 0;
        float mw = 0, mh = 0;
        
        File ft = Util.getPath(Frame.CONTEXT, path+"/"+Md5.mD5(url), fn + FILEHOUZ);
        if (ft.exists()) {
            return ft;
        }
        
        if (fdir.isDirectory()) {
            File[] fs = fdir.listFiles();
            for (File f : fs) {
                fm = f.getName();
                if (fm.startsWith(fn)) {
                    jind = fm.indexOf('_');
                    xind = fm.indexOf('x', jind);
                    dind = fm.lastIndexOf(".");
                    if (jind > 0 && fm.indexOf("x", jind) >= 0) {
                        mw = Float.valueOf(fm.substring(jind + 1, xind));
                        mh = Float.valueOf(fm.substring(xind + 1, dind));
                        if ((mw >= w && mh >= h) || (mw == 0 && mh == 0)) {
                            return f;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 删除某缓存组
     * 
     * @author ryan
     * @Title: delete
     * @Description: TODO
     * @param url
     * @throws
     */
    public static void delete(String url) {
        String fn = getFilename(url, true, 0, 0);
        synchronized (fn) {
            removeMap.put(fn, fn);
            File fdir = Util.getDPath(Frame.CONTEXT, path);
            if (fdir.isDirectory()) {
                File[] fs = fdir.listFiles();
                for (File f : fs) {
                    if (f.getName().startsWith(fn)) {
                        FILEMANAGER.deleteFile(f.getName(), false);
                        FILEMANAGER.save();
                        f.delete();
                    }
                }
            }
            removeMap.remove(fn);
        }
    }
    
    /**
     * 删除某个缓存
     * 
     * @author ryan
     * @Title: delete
     * @Description: TODO
     * @param url
     * @param w
     * @param h
     * @throws
     */
    public static void delete(String url, float w, float h) {
        String fn = getFilename(url, false, w, h);
        synchronized (fn) {
            removeMap.put(fn, fn);
            File file = Util.getPath(Frame.CONTEXT, path+"/"+Md5.mD5(url), fn);
            FILEMANAGER.deleteFile(file.getName(), false);
            FILEMANAGER.save();
            file.delete();
            if(file.getParentFile().list().length==0){
                file.delete();
            }
            removeMap.remove(fn);
        }
    }
    
    /**
     * 获取文件缓存的名称
     * 
     * @author ryan
     * @Title: getFilename
     * @Description: TODO
     * @param url
     * @param bol
     * @param w
     * @param h
     * @return
     * @throws
     */
    private static String getFilename(String url, boolean bol, float w, float h) {
        String filemd5 = url, sizestr = "_" + (int)w + "x" + (int)h;
        try {
            filemd5 = Md5.md5(filemd5);
            if (bol) {
                return filemd5;
            }
        }
        catch (Exception e1) {}
        return filemd5 + sizestr + FILEHOUZ;
    }
    
    /**
     * 保存文件
     * 
     * @author ryan
     * @Title: save
     * @Description: TODO
     * @param url
     * @param bytes
     * @param w
     * @param h
     * @throws
     */
    public static void save(String url, byte[] bytes, float w, float h) {
        try {
            String lock = "";
            String fn = getFilename(url, true, w, h);
            if (removeMap.containsKey(fn)) {
                lock = removeMap.get(fn);
            }
            String filename = getFilename(url, false, w, h);
            if (removeMap.containsKey(filename)) {
                lock = removeMap.get(filename);
            }
            synchronized (lock) {
                File file = Util.getPath(Frame.CONTEXT, path+"/"+Md5.mD5(url), filename);
                if (file != null) {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                }
                FILEMANAGER.saveFile(filename, file, System.currentTimeMillis());
                FILEMANAGER.checkFile();
                FILEMANAGER.save();
            }
        }
        catch (Exception e) {
            MLog.D(MLog.SYS_RUN,e);
        }
    }
    
    /**
     * 读取图片
     * 
     * @author ryan
     * @Title: read
     * @Description: TODO
     * @param url
     * @return
     * @throws
     */
    
    public static byte[] read(String url, float w, float h) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            File file = check(url, w, h);
            if (file == null) {
                return null;
            }
            FILEMANAGER.setTime(file.getName());
            FileInputStream is = new FileInputStream(file);
            byte[] bt = new byte[1024];
            int ind = 0;
            while ((ind = is.read(bt)) >= 0) {
                out.write(bt, 0, ind);
            }
            is.close();
            if (ToolsConfig.checkStatistics(ToolsConfig.TAG_IMAGE_STORE_CACHE)) {
                MLog.D(MLog.SYS_RUN,ToolsConfig.TAG_IMAGE_STORE_CACHE + ":" + url);
                NetStatistics.addStatistics(ToolsConfig.TAG_IMAGE_STORE_CACHE, url.length(), out.size());
            }
            return out.toByteArray();
        }
        catch (Exception e) {
            MLog.D(MLog.SYS_RUN,e);
            return null;
        }
    }
}

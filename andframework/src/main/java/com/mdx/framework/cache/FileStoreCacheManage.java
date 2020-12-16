package com.mdx.framework.cache;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.Util;
import com.mdx.framework.utility.tools.NetStatistics;

public class FileStoreCacheManage {

    public static String FILEDATASTR = "TEMP_FILES_FILE";
    public static FileTempManager FILEMANAGER = new FileTempManager(FILEDATASTR, "MaxFileSize", new FileTempManager.OnDelete() {

        @Override
        public void onDelete(String str) {
        }
    });

    public static Map<String, String> removeMap = new HashMap<String, String>();

    public final static String FILEHOUZ = ".filetemp";

    public final static String path = "file";

    /**
     * 查找缓存是否存在
     *
     * @param url
     * @param w
     * @param h
     * @return
     * @throws
     * @author ryan
     * @Title: check
     * @Description: TODO
     */
    public static File check(String url, String lastName) {
        String fn = getFilename(url, false);
        String filename;
        if (lastName == null) {
            filename = getFilename(url, true);
        } else {
            filename = getFilename(url, false) + "." + lastName;
        }
        if (removeMap.containsKey(fn) || removeMap.containsValue(filename)) {
            return null;
        }
        File file = Util.getDPath(Frame.CONTEXT, path);
        if (file != null && file.isDirectory()) {
            File f = Util.getPath(Frame.CONTEXT, path, filename);
            if (f.exists()) {
                return f;
            }
            f = Util.getPath(Frame.CONTEXT, path, fn);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    /**
     * 删除某缓存组
     *
     * @param url
     * @throws
     * @author ryan
     * @Title: delete
     * @Description: TODO
     */
    public static void delete(String url) {
        String fn = getFilename(url, true);
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
     * 获取文件缓存的名称
     *
     * @param url
     * @param bol
     * @param w
     * @param h
     * @return
     * @throws
     * @author ryan
     * @Title: getFilename
     * @Description: TODO
     */
    private static String getFilename(String url, boolean bol) {
        String filemd5 = url;
        try {
            filemd5 = Md5.md5(filemd5);
            if (bol) {
                return filemd5;
            }
        } catch (Exception e1) {
        }
        return filemd5 + FILEHOUZ;
    }

    /**
     * 保存文件
     *
     * @param url
     * @param bytes
     * @param w
     * @param h
     * @throws
     * @author ryan
     * @Title: save
     * @Description: TODO
     */
    public static File save(String url, byte[] bytes) {
        try {
            String lock = "";
            String fn = getFilename(url, true);
            if (removeMap.containsKey(fn)) {
                lock = removeMap.get(fn);
            }
            String filename = getFilename(url, false);
            if (removeMap.containsKey(filename)) {
                lock = removeMap.get(filename);
            }
            synchronized (lock) {
                File file = Util.getPath(Frame.CONTEXT, path, filename);
                if (file != null) {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                }
                FILEMANAGER.saveFile(filename, file, System.currentTimeMillis());
                FILEMANAGER.checkFile();
                FILEMANAGER.save();
                return file;
            }
        } catch (Exception e) {
            MLog.D(MLog.SYS_RUN, e);
        }
        return null;
    }

    public static File getOutputFile(String url, String lastName) {
        try {
            String lock = "";
            String fn = getFilename(url, false);
            if (removeMap.containsKey(fn)) {
                lock = removeMap.get(fn);
            }
            String filename = getFilename(url, false);
            if (removeMap.containsKey(filename)) {
                lock = removeMap.get(filename);
            }
            synchronized (lock) {
                File file;
                if (!TextUtils.isEmpty(lastName)) {
                    file = Util.getPath(Frame.CONTEXT, path, filename + "." + lastName);
                } else {
                    file = Util.getPath(Frame.CONTEXT, path, filename);
                }
                FILEMANAGER.saveFile(filename, file, System.currentTimeMillis());
                FILEMANAGER.checkFile();
                FILEMANAGER.save();
                return file;
            }
        } catch (Exception e) {
            MLog.D(MLog.SYS_RUN, e);
        }
        return null;
    }

    /**
     * 读取图片
     *
     * @param url
     * @return
     * @throws
     * @author ryan
     * @Title: read
     * @Description: TODO
     */
    public static File read(String url, String lastname) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            File file = check(url, lastname);
            if (file == null) {
                return null;
            }
            FILEMANAGER.setTime(file.getName());
            if (ToolsConfig.checkStatistics(ToolsConfig.TAG_File_STORE_CACHE)) {
                MLog.D(MLog.SYS_RUN, ToolsConfig.TAG_File_STORE_CACHE + ":" + url);
                NetStatistics.addStatistics(ToolsConfig.TAG_File_STORE_CACHE, url.length(), out.size());
            }
            return file;
        } catch (Exception e) {
            MLog.D(MLog.SYS_RUN, e);
            return null;
        }
    }
}

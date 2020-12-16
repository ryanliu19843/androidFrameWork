package com.mdx.framework.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import com.mdx.framework.Frame;
import com.mdx.framework.commons.data.Serialize;
import com.mdx.framework.utility.Util;

public class DataStoreCacheManage {
    public static String FILEDATASTR = "TEMP_FILES_DATA";
    
    public static FileTempManager FILEMANAGER = new FileTempManager(FILEDATASTR, "MaxDataSize",new FileTempManager.OnDelete() {
        
        @Override
        public void onDelete(String str) {
        }
    });
    
    public final static String path = "data";
    
    public static File check(final String filemd5, final long time) {
        File file = Util.getDPath(Frame.CONTEXT, path+"/"+filemd5);
        if (file != null && file.isDirectory()) {
            String[] list = file.list(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".temp") && filename.startsWith(filemd5);
                }
            });
            String read = null;
            long newess = time;
            for (String str : list) {
                String mtime = str.substring(str.indexOf("-") + 1, str.length() - ".temp".length());
                Long temptime = Long.parseLong(mtime);
                long gap = System.currentTimeMillis() - temptime;
                if (gap < newess) {
                    if (read != null) {
                        new File(file.getPath() + "/" + read).delete();
                    }
                    read = str;
                    newess = gap;
                }
            }
            if (read == null) {
                return null;
            }
            return new File(read);
        }
        return null;
    }
    
    public static void delete(String filemd5) {
        final String f5 = filemd5;
        File file = Util.getDPath(Frame.CONTEXT, path+"/"+filemd5);
        if (file.isDirectory()) {
            File[] files = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.startsWith(f5);
                }
            });
            for (File f : files) {
                FILEMANAGER.deleteFile(f.getName(), false);
                FILEMANAGER.save();
//                MLog.D(MLog.CACHE_LOAD+".DATADEL",f.getPath());
                f.delete();
            }
            if(file.list().length==0){
//                MLog.D(MLog.CACHE_LOAD+".DATADELDIR",file.getPath());
                file.delete();
            }
        }
    }
    
    public static void save(String filemd5, byte[] bytes) {
        try {
            delete(filemd5);
            String filename = filemd5 + "-" + Calendar.getInstance().getTimeInMillis() + ".temp";
            final File file = Util.getPath(Frame.CONTEXT, path+"/"+filemd5, filename);
            if (file != null) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();
            }
//            MLog.D(MLog.CACHE_LOAD+".DATASAVE",file.getPath());
            FILEMANAGER.saveFile(filename, file, System.currentTimeMillis());
            FILEMANAGER.checkFile();
            FILEMANAGER.save();
        }
        catch (Exception e) {
//            MLog.D(MLog.SYS_RUN,e);
        }
        
    }
    
    public static byte[] readByte(final String filemd5, final long time) {
        try {
            ByteArrayOutputStream baof = new ByteArrayOutputStream();
            byte[] bt = new byte[1024];
            File file = check(filemd5, time);
            if (file == null) {
                return null;
            }
            FILEMANAGER.setTime(file.getName());
            InputStream is = new FileInputStream(Util.getDPath(Frame.CONTEXT, path+"/"+filemd5) + "/" + file);
            int i = 0;
            while ((i = is.read(bt)) >= 0) {
                baof.write(bt, 0, i);
            }
            baof.flush();
            baof.close();
            is.close();
            return baof.toByteArray();
        }
        catch (Exception e) {
//            MLog.D(MLog.SYS_RUN,e);
            return null;
        }
    }
    
    public static Object read(final String filemd5, final long time) {
        try {
            File file = check(filemd5, time);
            if (file == null) {
                return null;
            }
            FILEMANAGER.setTime(file.getName());
            InputStream is = new FileInputStream(Util.getDPath(Frame.CONTEXT, path+"/"+filemd5) + "/" + file);
            Serialize serialize = new Serialize();
//            MLog.D(MLog.CACHE_LOAD+".DATAREAD",file.getPath());
            return serialize.unSerialize(is);
        }
        catch (Exception e) {
//            MLog.D(MLog.SYS_RUN,e);
            return null;
        }
    }
    
    public static void save(String filemd5, Serializable obj) {
        String filename = filemd5 + "-" + Calendar.getInstance().getTimeInMillis() + ".temp";
        final File file = Util.getPath(Frame.CONTEXT, path+"/"+filemd5, filename);
        if (file != null) {
            try {
                delete(filemd5);
                Serialize serialize = new Serialize();
                FileOutputStream fos = new FileOutputStream(file);
                serialize.serialize(obj, fos);
            }
            catch (Exception e) {
//                MLog.D(MLog.SYS_RUN,e);
            }
        }
        FILEMANAGER.saveFile(filename, file, System.currentTimeMillis());
        FILEMANAGER.checkFile();
        FILEMANAGER.save();
    }
}

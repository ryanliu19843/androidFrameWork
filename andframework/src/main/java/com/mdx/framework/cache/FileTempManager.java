/*
 * 文件名: FileTempManager.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: liulu 创建时间:2014-10-30
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.config.BaseConfig;
import com.mdx.framework.server.api.base.Msg_TempFile;
import com.mdx.framework.server.api.base.Msg_TempFiles;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.MAsyncTask;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author liulu
 * @version [2014-10-30 下午1:20:01]
 */

public class FileTempManager {
    ThreadPool tp = new ThreadPool(1);
    
    public Msg_TempFiles TEMPFILES =new Msg_TempFiles();
    
    private ComparatorDevice cd = new ComparatorDevice();
    
    private String FileTempName;
    
    private String LOCKSTR = "";
    
    public OnDelete onDelete;
    
    public String MaxTempSize = "";

    public FileTempManager(String data, String matem, OnDelete onDelete) {
        FileTempName = data;
        this.onDelete = onDelete;
        this.MaxTempSize = matem;
        TEMPFILES.tempSize=0L;
        TEMPFILES.files=new ArrayList<Msg_TempFile>();
    }
    
    public synchronized void saveFile(String id, File file, long time) {
        if (!file.exists()) {
            return;
        }
        Msg_TempFile.Builder tf =new Msg_TempFile.Builder();
        tf.filename=id;
        try {
            tf.filepath=file.getCanonicalPath();
        }
        catch (IOException e) {}
        long filesize = file.length();
        tf.filesize=filesize;
        tf.createtime=time;
        tf.tempTime=time;
        TEMPFILES.files.add(tf.build());
        TEMPFILES.tempSize=TEMPFILES.tempSize + filesize;
    }
    
    public synchronized void checkFile() {
        if (ParamsManager.getIntValue(MaxTempSize) == 0) {
            return;
        }
        long nowsize = TEMPFILES.tempSize;
        List<Msg_TempFile> list = TEMPFILES.files;
        Collections.sort(list, cd);
        
        while (ParamsManager.getIntValue(MaxTempSize) > nowsize) {
            Msg_TempFile retn = list.get(0);
            deleteFile(retn.filename, true);
        }
    }
    
    public synchronized void clear(final Runnable runable) {
        
        new MAsyncTask<String, String, String>() {
            
            @Override
            protected String doInBackground(String... params) {
                synchronized (LOCKSTR) {
                    long time = System.currentTimeMillis();
                    while (TEMPFILES.files.size() > 0) {
                        try {
                            String filepath = TEMPFILES.files.get(0).filename;
                            deleteFile(filepath, true);
                            if (System.currentTimeMillis() - time > 100) {
                                progressUpdate(filepath);
                            }
                            try {
                                Thread.sleep(10);
                            }
                            catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                    }
                    TEMPFILES.tempSize=0L;
                    progressUpdate("");
                    save();
                }
                return null;
            }
            
            public void onProgressUpdate(String... values) {
                if (runable != null) {
                    runable.run();
                }
            }
        }.execute("");
    }
    
    public synchronized void setTime(String url) {
        for (int i = 0; i < TEMPFILES.files.size(); i++) {
            Msg_TempFile tep = TEMPFILES.files.get(i);
            if (tep.filename.equals(url)) {
                tep.tempTime=(System.currentTimeMillis());
                return;
            }
        }
    }
    
    public long getSize() {
        return TEMPFILES.tempSize;
    }
    
    public static class ComparatorDevice implements Comparator<Msg_TempFile> {
        
        @Override
        public int compare(Msg_TempFile lhs, Msg_TempFile rhs) {
            if (lhs.createtime > rhs.createtime) {
                return 1;
            }
            if (lhs.createtime < rhs.createtime) {
                return -1;
            }
            return 0;
        }
    }
    
    public synchronized void deleteFile(String url, boolean bol) {
        for (int i = 0; i < TEMPFILES.files.size(); i++) {
            Msg_TempFile tep = TEMPFILES.files.get(i);
            long filesize = 0;
            if (tep.filename.equals(url)) {
                File file = new File(tep.filepath);
                filesize = file.length();
                TEMPFILES.files.remove(i);
                if (bol) {
                    onDelete.onDelete(url);
                    if (file.exists()) {
                        file.delete();
                        if (file.getParentFile().list().length == 0) {
                            file.getParentFile().delete();
                        }
                    }
                }
                TEMPFILES.tempSize=TEMPFILES.files.size() - filesize;
                return;
            }
        }
    }
    
    public interface OnDelete {
        public void onDelete(String str);
    }
    
    public synchronized void save() {
        tp.execute(new PRunable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                BroadCast.PostBroad(new BIntent(BaseConfig.getTempPath(), "", null, 99, null));
                Helper.saveBuilder(FileTempName, TEMPFILES);
                tp.getWatrun().clear();
            }
        });
    }
}

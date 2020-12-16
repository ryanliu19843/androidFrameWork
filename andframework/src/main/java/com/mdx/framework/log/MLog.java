package com.mdx.framework.log;

import android.text.TextUtils;
import android.util.Log;

import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.config.LogConfig;
import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.server.LogUpdateService;
import com.mdx.framework.server.api.base.Msg_Post;
import com.mdx.framework.server.api.base.Msg_Request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class MLog {
    public static ThreadPool ThreadPool = new ThreadPool(1);

    public static final String LOGTOOLS_LOGSHOW = "logtools_logshow";

    public final static String MLOG_TAG = "frame";

    public final static String MLOG_TAG_LOAD = "frameload";

    public final static String SYS_ERR = "system.err";

    public final static String SYS_RUN = "system.run";

    public final static String NWORK_LOAD = "network.load";

    public final static String CACHE_LOAD = "cache.load";

    public final static String FILE_LOAD = "file.load";

    public static StringBuffer logCache = new StringBuffer();

    public static void D(String msg) {
        D(SYS_RUN, msg);
    }

    public static void D(String showType, String msg) {
        cachelog(showType, msg);
        if (LogConfig.isShowLog()) {
            Log.d(MLOG_TAG, msg);
        }
        if (ToolsConfig.isLogToolsShow()) {
            BroadCast.PostBroad(new BIntent(LOGTOOLS_LOGSHOW + "." + showType, "", null, 0, msg));
        }
    }

    public static void D(Throwable msg) {
        D(SYS_RUN, msg);
    }

    public static void D(String showType, Throwable msg) {
        cachelog(showType, null, null, msg);
        if (LogConfig.isShowLog()) {
            msg.printStackTrace();
        }
        if (ToolsConfig.isLogToolsShow()) {
            BroadCast.PostBroad(new BIntent(LOGTOOLS_LOGSHOW + "." + showType, "", null, 0, msg));
        }
    }

    public static void D(String tag, String id, String url, Object oparams, Object aparams, Object pageparam, Object obj) {
        D(SYS_RUN, tag, id, url, oparams, aparams, pageparam, obj);
    }

    public static void D(String showType, String tag, String id, String url, Object oparams, Object aparams,
                         Object pageparam, Object obj) {
        if (LogConfig.isShowLog()) {
            StringBuffer sb = new StringBuffer();
            sb.append(tag);
            sb.append(id);
            sb.append(": ");
            sb.append(url);
            String[][] params = null;
            if (oparams instanceof String[][]) {
                params = (String[][]) oparams;
            }
            boolean haswh = false;
            if (params != null) {
                for (String[] param : params) {
                    if (url.indexOf("?") < 0 && !haswh) {
                        sb.append("?");
                        haswh = true;
                    } else {
                        sb.append("&");
                    }
                    if (param.length >= 2 && param[1] != null) {
                        sb.append(param[0]);
                        sb.append("=");
                        sb.append(param[1]);
                    }
                }
            }
            if (aparams instanceof String[][]) {
                params = (String[][]) aparams;
            } else {
                params = null;
            }
            if (params != null) {
                for (String[] param : params) {
                    if (url.indexOf("?") < 0 && !haswh) {
                        sb.append("?");
                        haswh = true;
                    } else {
                        sb.append("&");
                    }
                    if (param.length >= 2 && param[1] != null) {
                        sb.append(param[0]);
                        sb.append("=");
                        sb.append(param[1]);
                    }
                }
            }
            if (pageparam instanceof String[][]) {
                params = (String[][]) pageparam;
            } else {
                params = null;
            }
            if (params != null) {
                for (String[] param : params) {
                    if (url.indexOf("?") < 0 && !haswh) {
                        sb.append("?");
                        haswh = true;
                    } else {
                        sb.append("&");
                    }
                    if (param.length >= 2 && param[1] != null) {
                        sb.append(param[0]);
                        sb.append("=");
                        sb.append(param[1]);
                    }
                }
            }
            if (obj != null) {
                sb.append("&obj=object");
            }
            sb.append("&debug=1");
            Log.d(MLOG_TAG_LOAD, sb.toString());
            cachelog(showType, sb.toString());
        }
    }

    public static void E(String msg) {
        E(SYS_RUN, msg);
    }

    public static void E(String showType, String msg) {
        cachelog(showType, msg);
        if (LogConfig.isShowLog()) {
            Log.e(MLOG_TAG, msg);
        }

        if (ToolsConfig.isLogToolsShow()) {
            BroadCast.PostBroad(new BIntent(LOGTOOLS_LOGSHOW + "." + showType, "", null, 0, msg));
        }
    }

    public static void E(Throwable msg) {
        E(SYS_RUN, null, null, msg);
    }

    public static void E(String start, String end, Throwable msg) {
        E(SYS_RUN, start, end, msg);
    }

    public static void E(String showType, Throwable msg) {
        E(showType, null, null, msg);
    }

    public static void E(String showType, String start, String end, Throwable msg) {
        cachelog(showType, start, end, msg);
        if (LogConfig.isShowLog()) {
            for (StackTraceElement ste : msg.getStackTrace()) {
                Log.e(MLOG_TAG, ste.toString());
            }
            msg.printStackTrace();
        }
        if (ToolsConfig.isLogToolsShow()) {
            BroadCast.PostBroad(new BIntent(LOGTOOLS_LOGSHOW + "." + showType, "", null, 0, msg));
        }
    }

    public static void I(String msg) {
        I(SYS_RUN, msg);
    }

    public static void I(String showType, String msg) {
        cachelog(showType, msg);
        if (LogConfig.isShowLog()) {
            Log.i(MLOG_TAG, msg);
        }
        if (ToolsConfig.isLogToolsShow()) {
            BroadCast.PostBroad(new BIntent(LOGTOOLS_LOGSHOW + "." + showType, "", null, 0, msg));
        }
    }

    public static void p(Throwable e) {
        p(SYS_RUN, e);
    }

    public static void p(String showType, Throwable e) {
        cachelog(showType, null, null, e);
        if (LogConfig.isShowLog()) {
            e.printStackTrace();
        }
        if (ToolsConfig.isLogToolsShow()) {
            BroadCast.PostBroad(new BIntent(LOGTOOLS_LOGSHOW + "." + showType, "", null, 0, e.getMessage()));
        }
    }

    public static void D(String showType, String tag, String url, Object build) {
        if (LogConfig.isShowLog()) {
            StringBuffer sb = new StringBuffer();
            sb.append(tag);
            if (tag != null && !tag.endsWith(":")) {
                sb.append(":");
            }
            sb.append(" ");
            sb.append(url);
            if (build instanceof Msg_Request) {
                Msg_Request b = (Msg_Request) build;
                boolean haswh = false;
                for (Msg_Post post : b.posts) {
                    if (url.indexOf("?") < 0 && !haswh) {
                        sb.append("?");
                        haswh = true;
                    } else {
                        sb.append("&");
                    }
                    sb.append(post.name);
                    sb.append("=");
                    sb.append(post.value);
                }
                if (b.requestMessage != null && b.requestMessage.size() > 0) {
                    sb.append("&obj=object");
                }
                sb.append("&debug=1");
            }
            Log.d(MLOG_TAG_LOAD + "." + showType, sb.toString());
            cachelog(showType, sb.toString());
        }
    }


    public static void cachelog(String logtype, final String start, final String end, final Throwable msg) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        if (!TextUtils.isEmpty(start)) {
            pw.println(start);
        }
        msg.printStackTrace(pw);
        if (!TextUtils.isEmpty(end)) {
            pw.println(end);
        }
        String logmsg = sw.toString();
        cachelog(logtype, logmsg);
    }

    public static void cachelog(String logtype, final String log) {
        logCache.append(getNowtime()).append(":").append(logtype).append(" ").append(log).append("\n");
        if (logCache.length() > 10240) {
            writenow();
        }
    }

    public static void writenow() {
        String cachedlog = logCache.toString();
        writelogtofile(cachedlog);
        logCache.setLength(0);
    }

    public static void writelogtofile(final String log) {
        ThreadPool.execute(new PRunable() {

            @Override
            public void run() {
                synchronized (Frame.LOGFILE) {
                    FileOutputStream fout = null;
                    try {
                        fout = new FileOutputStream(Frame.LOGFILE, true);
                        fout.write(log.getBytes());
                    } catch (Exception e) {

                    } finally {
                        try {
                            fout.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        });
    }

    public static String readLog() {
        synchronized (Frame.LOGFILE) {
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            if (Frame.LOGFILE.exists()) {
                byte[] bt = new byte[1024];
                FileInputStream fin;
                try {
                    fin = new FileInputStream(Frame.LOGFILE);
                } catch (FileNotFoundException e1) {
                    return null;
                }
                int ind = 0;
                try {
                    while ((ind = fin.read(bt)) >= 0) {
                        fos.write(bt, 0, ind);
                    }
                    return new String(fos.toByteArray());
                } catch (Exception e) {

                } finally {
                    try {
                        fos.close();
                    } catch (Exception e) {
                    }
                    try {
                        fin.close();
                    } catch (Exception e) {
                    }
                }
            }
            return null;
        }
    }

    private static String getNowtime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String created = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"//从0计算
                + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + ":" + calendar.get(Calendar.MILLISECOND);
        return created;
    }

    public static boolean initLodfile() {
        synchronized (Frame.LOGFILE) {
            File backfile = new File(Frame.LOGFILE.getPath() + ".bak");
            if (backfile.exists() && Frame.LOGFILE.exists()) {
                backfile.delete();
            }
            Frame.LOGFILE.renameTo(backfile);
        }
        return true;
    }
}

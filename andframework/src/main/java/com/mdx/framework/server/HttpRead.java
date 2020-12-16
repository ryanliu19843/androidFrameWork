package com.mdx.framework.server;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mdx.framework.commons.CanIntermit;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.Util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
public abstract class HttpRead<T> implements CanIntermit {
    protected HttpURLConnection httpURLConnection;
    protected CookieManager cookieManager = new CookieManager();
    protected boolean stop = false;
    protected String statisticsTag = "NETWORK";

    protected int timeOut = 60000, rtimeOut = 180000;
    protected String urlEncode = "UTF-8";
    public int index = 1;
    public static String agent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36";
    public static String Connection = "Keep-Alive";
    public static boolean useCaches = false;

    public T get(String url, String[][] params) throws MException {
        init(url, params);
        url = getFullUrl(url, params);
        MLog.D(MLog.NWORK_LOAD, "get", "", url, null, null, null, null);
        T retn = null;
        try {
            CookieHandler.setDefault(cookieManager);
            HttpURLConnection.setFollowRedirects(true);

            URL httpurl = new URL(url);
            httpURLConnection = (HttpURLConnection) httpurl.openConnection();
            httpURLConnection.setConnectTimeout(timeOut);
            httpURLConnection.setReadTimeout(rtimeOut);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept-Charset", urlEncode);
            if (agent.length() > 0) {
                httpURLConnection.setRequestProperty("User-Agent", agent);
            }
            httpURLConnection.setRequestProperty("Connection", Connection);

            httpURLConnection.setDoInput(true); // 允许输入流，即允许下载
            httpURLConnection.setUseCaches(useCaches); // 不使用缓冲

            httpURLConnection.connect();
            statisticsHttp(httpURLConnection);
            retn = disread(httpURLConnection, url, params);
        } catch (MException e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw e;
        } catch (Exception e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw new MException(98, e);
        } finally {
            intermit();
        }
        httpURLConnection = null;
        return retn;
    }

    public void setHead(HttpURLConnection connection, HashMap<String, String> headmap) {
        if (headmap != null) {
            for (String key : headmap.keySet()) {
                String val = headmap.get(key);
                connection.setRequestProperty(key, val);
            }
        }
    }

    public T post(String url, String[][] params) throws MException {
        init(url, params);
        MLog.D(MLog.NWORK_LOAD, "post", "", url, params, null, null, null);
        T retn = null;
        try {
            URL httpurl = new URL(url);

            httpURLConnection = (HttpURLConnection) httpurl.openConnection();
            httpURLConnection.setConnectTimeout(timeOut);
            httpURLConnection.setReadTimeout(rtimeOut);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            if (agent.length() > 0) {
                httpURLConnection.setRequestProperty("User-Agent", agent);
            }
            if (Connection.length() > 0) {
                httpURLConnection.setRequestProperty("Connection", Connection);
            }
            httpURLConnection.setDoInput(true); // 允许输入流，即允许下载
            httpURLConnection.setDoOutput(true); // 允许输出流，即允许上传
            httpURLConnection.setUseCaches(useCaches); // 不使用缓冲
            httpURLConnection.connect();
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());

            StringBuffer sb = new StringBuffer();
            String start = "";
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    String[] param = params[i];
                    if (param.length > 1) {
                        if (param[0] == null || param[0].length() == 0) {
                            continue;
                        }
                        if (param[1] == null) {
                            param[1] = "";
                        }
                        sb.append(start);
                        start = "&";
                        try {
                            sb.append(param[0] + "=" + URLEncoder.encode(param[1], urlEncode));
                        } catch (UnsupportedEncodingException e) {
                            sb.append(param[0] + "=" + param[1]);
                        }
                    }
                }
            }
            out.write(sb.toString().getBytes(urlEncode));
            out.flush();
            out.close();
            statisticsHttp(httpURLConnection);
            retn = disread(httpURLConnection, url, params);
        } catch (MException e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw e;
        } catch (Exception e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw new MException(98, e);
        } finally {
            intermit();
        }
        httpURLConnection = null;
        return retn;
    }

    public T post(String url, String[][] params, Object obj) throws MException {
        if (obj == null) {
            return post(url, params);
        }
        init(url, params);
        MLog.D(MLog.NWORK_LOAD, "post", "", url, params, null, null, null);
        T retn = null;
        try {
            URL httpurl = new URL(url);

            httpURLConnection = (HttpURLConnection) httpurl.openConnection();
            httpURLConnection.setConnectTimeout(timeOut);
            httpURLConnection.setReadTimeout(rtimeOut);
            httpURLConnection.setRequestMethod("POST");
            String devid = "A9" + UUID.randomUUID().toString().replaceAll("-", "");
            httpURLConnection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + devid);
            if (agent.length() > 0) {
                httpURLConnection.setRequestProperty("User-Agent", agent);
            }
            if (Connection.length() > 0) {
                httpURLConnection.setRequestProperty("Connection", Connection);
            }
            httpURLConnection.setDoInput(true); // 允许输入流，即允许下载
            httpURLConnection.setDoOutput(true); // 允许输出流，即允许上传
            httpURLConnection.setUseCaches(useCaches); // 不使用缓冲
            httpURLConnection.connect();
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            setEntity(out, params, obj, devid);
            out.flush();
            out.close();
            statisticsHttp(httpURLConnection);
            retn = disread(httpURLConnection, url, params);
        } catch (MException e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw e;
        } catch (Exception e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw new MException(98,e);
        } finally {
            intermit();
        }
        httpURLConnection = null;
        return retn;
    }

    public T post(String url, String[][] params, HashMap<String, String> headmap) throws MException {
        init(url, params);
        MLog.D(MLog.NWORK_LOAD, "post", "", url, params, null, null, null);
        T retn = null;
        try {
            URL httpurl = new URL(url);

            httpURLConnection = (HttpURLConnection) httpurl.openConnection();
            httpURLConnection.setConnectTimeout(timeOut);
            httpURLConnection.setReadTimeout(rtimeOut);
            httpURLConnection.setRequestMethod("POST");
            setHead(httpURLConnection, headmap);
            httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            if (agent.length() > 0) {
                httpURLConnection.setRequestProperty("User-Agent", agent);
            }
            if (Connection.length() > 0) {
                httpURLConnection.setRequestProperty("Connection", Connection);
            }
            httpURLConnection.setDoInput(true); // 允许输入流，即允许下载
            httpURLConnection.setDoOutput(true); // 允许输出流，即允许上传
            httpURLConnection.setUseCaches(useCaches); // 不使用缓冲
            httpURLConnection.connect();
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());

            StringBuffer sb = new StringBuffer();
            String start = "";
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    String[] param = params[i];
                    if (param.length > 1) {
                        if (param[0] == null || param[0].length() == 0) {
                            continue;
                        }
                        if (param[1] == null) {
                            param[1] = "";
                        }
                        sb.append(start);
                        start = "&";
                        try {
                            sb.append(param[0] + "=" + URLEncoder.encode(param[1], urlEncode));
                        } catch (UnsupportedEncodingException e) {
                            sb.append(param[0] + "=" + param[1]);
                        }
                    }
                }
            }
            out.write(sb.toString().getBytes(urlEncode));
            out.flush();
            out.close();
            statisticsHttp(httpURLConnection);
            retn = disread(httpURLConnection, url, params);
        } catch (MException e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw e;
        } catch (Exception e) {
            MLog.D(MLog.NWORK_LOAD, e);
            throw new MException(98);
        } finally {
            intermit();
        }
        httpURLConnection = null;
        return retn;
    }


    protected void statisticsResponse(HttpURLConnection http) {
        // if (ToolsConfig.isStatistics()) {
        // try {
        // long length = 0;
        // for (String h : http.getHeaderFields().keySet()) {
        // if (h != null) {
        // length += h.length() + 1;
        // }
        // List<String> list = http.getHeaderFields().get(h);
        // for (String s : list) {
        // length += s.length() + 1;
        // }
        // }
        // length += http.getContentLength();
        // NetStatistics.addStatistics(statisticsTag, 0, length);
        // } catch (Exception e) {
        // }
        // }
    }

    protected void statisticsHttp(HttpURLConnection http) {
        // if (ToolsConfig.isStatistics()) {
        // try {
        // long length = http.getURL().toString().length();
        // for (String h : http.getRequestProperties().keySet()) {
        // if (h != null) {
        // length += h.length() + 1;
        // }
        // List<String> list = http.getRequestProperties().get(h);
        // for (String s : list) {
        // length += s.length() + 1;
        // }
        // }
        // NetStatistics.addStatistics(statisticsTag, length, 0);
        // } catch (Exception e) {
        // }
        // }
    }

    public T read(HttpURLConnection httpConnection, String url, String[][] params) throws Exception {
        return null;
    }


    public T disread(HttpURLConnection httpConnection, String url, String[][] params) throws Exception {
        int delaytime = ParamsManager.getIntValue("server_delayed");
        if (delaytime > 0) {
            Util.sleep(delaytime);
        }
        return read(httpConnection, url, params);
    }

    protected T init(String url, String[][] params) {
        T retn = onInit(url, params);
        return retn;
    }

    public void intermit() {
        try {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            stop = true;
        } catch (Exception e) {
            MLog.D(MLog.NWORK_LOAD + ".INTERMIT", e);
        }
    }

    protected T onInit(String url, String[][] params) {
        return null;
    }

    public String getFullUrl(String url, String[][] params) {
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        String start = "?";
        if (params != null && params.length > 0) {
            if (url.indexOf("?") < 0) {
                start = "?";
            } else {
                start = "&";
            }
            for (int i = 0; i < params.length; i++) {
                String[] param = params[i];
                if (param.length > 1) {
                    if (param[0] == null || param[0].length() == 0) {
                        continue;
                    }
                    if (param[1] == null) {
                        param[1] = "";
                    }
                    sb.append(start);
                    start = "&";
                    try {
                        sb.append(param[0] + "=" + URLEncoder.encode(param[1], urlEncode));
                    } catch (UnsupportedEncodingException e) {
                        sb.append(param[0] + "=" + param[1]);
                    }
                }
            }
        }
        return sb.toString();
    }

    private void setEntity(OutputStream out, String[][] params, Object object, String id) throws Exception {
        if (object instanceof List<?>) {
            for (Object o : (List<?>) object) {
                addContentBody(out, o, id);
            }
        } else {
            addContentBody(out, object, id);
        }

        if (params != null) {
            for (String[] param : params) {
                if (param.length >= 2) {
                    if (param[0] != null && param[0].length() > 0) {
                        addContentBody(out, new FilePar(param[0], URLEncoder.encode(param[1], urlEncode)), id);
                    }
                }
            }
        }
        out.write(new byte[]{'-', '-'});
        out.write(id.getBytes(urlEncode));
        out.write(new byte[]{'-', '-', '\r', '\n'});
    }

    private void addContentBody(OutputStream out, Object object, String id) throws Exception {
        FilePar fp = null;
        if (object instanceof FilePar) {
            fp = (FilePar) object;
        }
        if (object instanceof File) {
            fp = new FilePar("file" + index, (File) object);
            index++;
        } else if (object instanceof byte[]) {
            fp = new FilePar("file" + index, (byte[]) object);
            index++;
        }
        fp.writeto(out, urlEncode, id);
    }
}

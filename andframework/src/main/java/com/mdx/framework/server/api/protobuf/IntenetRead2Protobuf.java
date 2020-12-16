package com.mdx.framework.server.api.protobuf;

import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.commons.data.Method;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.HttpRead;
import com.mdx.framework.utility.Util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IntenetRead2Protobuf extends HttpRead<byte[]> {

	public IntenetRead2Protobuf() {
	}

	public byte[] post(String url, Object build) throws MException {
		byte[] retn = null;
		this.init(url, null);
		MLog.D(MLog.SYS_RUN, "Intenet Protobuf:", url, build);
		try {

			URL httpurl = new URL(url);

			httpURLConnection = (HttpURLConnection) httpurl.openConnection();
			httpURLConnection.setConnectTimeout(timeOut);
			httpURLConnection.setReadTimeout(rtimeOut);
			httpURLConnection.setRequestMethod("POST");
			if (agent.length() > 0) {
				httpURLConnection.setRequestProperty("User-Agent", agent);
			}
			if (Connection.length() > 0) {
				httpURLConnection.setRequestProperty("Connection", Connection);
			}
			httpURLConnection.addRequestProperty("Content-Type", "application/octet-stream");
			httpURLConnection.setDoInput(true); // 允许输入流，即允许下载
			httpURLConnection.setDoOutput(true); // 允许输出流，即允许上传
			httpURLConnection.setUseCaches(useCaches); // 不使用缓冲
			httpURLConnection.connect();
			OutputStream out = httpURLConnection.getOutputStream();

			ByteArrayOutputStream bytout = new ByteArrayOutputStream();
			Method.protobufSeralizeDes(build, bytout);
			out.write(bytout.toByteArray());
			out.flush();
			out.close();
			statisticsHttp(httpURLConnection);
			retn = disread(httpURLConnection, url);
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


	public byte[] disread(HttpURLConnection httpConnection, String url) throws Exception {
		int delaytime = ParamsManager.getIntValue("server_delayed");
		if (delaytime > 0) {
			Util.sleep(delaytime);
		}
		return read(httpConnection, url);
	}

	public byte[] read(HttpURLConnection response, String url) throws MException {
		try {
			int code = response.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				ByteArrayOutputStream fos = new ByteArrayOutputStream();
				byte[] bt = new byte[1024];
				InputStream in = response.getInputStream();
				int ind = 0;
				while ((ind = in.read(bt)) >= 0 && !this.stop) {
					fos.write(bt, 0, ind);
				}
				fos.flush();
				fos.close();
				in.close();
				if (ind != -1 || this.stop) {
					throw new MException(97);
				}
				return fos.toByteArray();
			} else {
				throw new MException(98, "http error " + code);
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e) {
			throw new MException(98, e);
		}
	}

}

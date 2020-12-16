package com.mdx.framework.server.file.read;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import com.mdx.framework.cache.FileStoreCacheManage;
import com.mdx.framework.commons.MException;
import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.server.HttpRead;
import com.mdx.framework.server.file.FileLoad;

/**
 * 
 * @author ryan
 * 
 */
public class HttpFileRead extends HttpRead<File> {
	private Object obj;

	public boolean mUseCache;

	public int loadtype;
	public String lastname=null;

	/**
	 * 用get方法读取图片
	 * 
	 * @param url
	 *            读取url
	 * @param obj
	 *            参数名称
	 * @param width
	 *            控件宽度
	 * @param height
	 *            控件高度
	 * @param params
	 *            传递参数
	 * @return
	 * @throws MException
	 */
	public File get(String url, Object obj, int loadtype, boolean useCache, String[][] params) throws MException {
		this.statisticsTag = ToolsConfig.TAG_IMAGE_NET_DOWN;
		this.obj = obj;
		this.mUseCache = useCache;
		this.loadtype = loadtype;
		FileLoad.postBroadcast(obj, 0, 2048);
		return super.get(url, params);
	}

	/**
	 * 用get方法读取图片
	 * 
	 * @param url
	 *            读取url
	 * @param obj
	 *            参数名称
	 * @param width
	 *            控件宽度
	 * @param height
	 *            控件高度
	 * @param params
	 *            传递参数
	 * @return
	 * @throws MException
	 */
	public File post(String url, Object obj, int loadtype, boolean useCache, String[][] params) throws MException {
		this.statisticsTag = ToolsConfig.TAG_IMAGE_NET_DOWN;
		this.obj = obj;
		this.mUseCache = useCache;
		FileLoad.postBroadcast(obj, 0, 2048);
		return super.post(url, params);
	}

	@Override
	public File read(HttpURLConnection response, String url, String[][] params) throws MException {
		try {
			int code = response.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				long fileSize = response.getContentLength();
				if (fileSize <= 0) {
					fileSize = 2048;
				}
				FileLoad.postBroadcast(obj, 512, fileSize);
				File file = FileStoreCacheManage.getOutputFile(obj.toString(), "downloadtemp");
				if (file.exists()) {
					file.delete();
				}
				OutputStream fos = new FileOutputStream(file);
				int nowsize = 0;
				byte[] bt = new byte[1024];
				InputStream in = response.getInputStream();
				int ind = 0;
				while ((ind = in.read(bt)) >= 0 && !this.stop) {
					nowsize += ind;
					fos.write(bt, 0, ind);
					FileLoad.postBroadcast(obj, nowsize, fileSize);
				}
				FileLoad.postBroadcast(obj, nowsize, fileSize);
				fos.flush();
				fos.close();
				in.close();
				fos.close();
				File rfile = FileStoreCacheManage.getOutputFile(obj.toString(), lastname);
				file.renameTo(rfile);
				if (ind != -1 || this.stop) {
					throw new MException(97);
				}
				FileLoad.postBroadcast(obj, fileSize, fileSize);
				this.statisticsResponse(response);
				return rfile;
			} else {
				throw new MException(98);
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e) {
			throw new MException(98, e);
		}
	}
}

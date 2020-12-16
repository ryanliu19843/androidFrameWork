package com.mdx.framework.server.file;

import java.io.File;
import java.util.LinkedHashMap;
import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.cache.FileStoreCacheManage;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.file.impl.FileBase;
import com.mdx.framework.server.file.impl.FileRead;
import com.mdx.framework.server.file.read.FileReadFactory;
import com.mdx.framework.utility.Util;

public class FileLoad {
	private ThreadPool threadpool = new ThreadPool(3);

	protected LinkedHashMap<String, Trun> FileHttpReadMap = new LinkedHashMap<String, Trun>();

	public FileLoad() {
	}

	/**
	 * 加载图片
	 * 
	 * @author ryan
	 * @Title: loadDrawable
	 * @Description: TODO
	 * @param view
	 * @param reload
	 * @return
	 * @throws
	 */
	public synchronized void loadFile(FileBase fileBase) {
		if (FileHttpReadMap.containsKey(fileBase.getObj())) {
			Trun tr = FileHttpReadMap.get(fileBase.getObj());
			fileBase.setLoadid(tr.hashCode() + "");
			tr.set(fileBase);
			return;
		}
		Trun run = new Trun(fileBase);
		fileBase.setLoadid(run.hashCode() + "");
		synchronized (FileHttpReadMap) {
			FileHttpReadMap.put((String) fileBase.getObj(), run);
		}
		threadpool.execute(run);
	}

	/**
	 * 
	 * 图片加载线程<BR>
	 * 功能加载线程
	 * 
	 * @author ryan
	 * @version [2013-12-3 下午3:55:19]
	 */
	private class Trun extends PRunable {
		private Object mObject;

		private FileBase mFileBase;

		private FileRead mCanRead;

		private int mLoadType;

		private boolean mUseCache = false;

		private boolean mReload = false;

		public Trun(FileBase fileBase) {
			this.mObject = fileBase.getObj();
			this.mFileBase = fileBase;
			this.mLoadType = fileBase.getLoadType();
			this.mUseCache = fileBase.isCache();
			this.mReload = fileBase.isReload();
		}

		public void set(FileBase fileBase) {
			synchronized (this) {
				this.mFileBase = fileBase;
				this.mLoadType = fileBase.getLoadType();
				this.mUseCache = fileBase.isCache();
				this.mReload = fileBase.isReload();
			}
		}

		private File loadFileFromUrl() throws MException {
			if (FileStoreCacheManage.check(mObject.toString(), null) != null && mUseCache && !mReload) {
				postBroadcast(mObject, 0, 100);
				File file = FileStoreCacheManage.read(mObject.toString(), null);
				postBroadcast(mObject, 100, 100);
				return file;
			}
			return mCanRead.loadFileFromUrl();
		}

		public void onIntermit() {
			mCanRead.intermit();
		}

		public void run() {
			try {
				// MLog.D("run:"+mObject.toString()+" "+threadpool.runingSize()+"/"+threadpool.size());
				synchronized (Frame.INITLOCK) {// 等待配置完成
				}
				// MLog.D("load:"+mObject.toString()+" "+threadpool.runingSize()+"/"+threadpool.size());
				if (this.stoped) {
					// MLog.D("stop:"+mObject.toString()+" "+threadpool.runingSize()+"/"+threadpool.size());
					return;
				}
				synchronized (this) {
					this.mCanRead = createRead(mObject, mLoadType, mUseCache);
				}
				postBroadcast(mObject, 0, 100);
				File file = loadFileFromUrl();
				postBroadcast(mObject, 100, 100);
				if (this.stoped) {
					return;
				}
				synchronized (this) {
					if (mFileBase == null) {
						return;
					}
				}
				if (mReload) {
					FileStoreCacheManage.delete((String) mObject);
				}
				if (file != null && file.exists()) {
					synchronized (this) {
						mFileBase.loaded(file, this.hashCode() + "");
					}
				}
				synchronized (FileHttpReadMap) {
					FileHttpReadMap.remove(mObject);
				}
			} catch (Exception e) {
				MLog.D(MLog.SYS_RUN, e);
			}
		}
	}

	protected FileRead createRead(Object object, int imageType, boolean useCache) {
		String url = getUrl(object.toString());
		return FileReadFactory.createFileRead(url, object, imageType, useCache);
	}

	private String getUrl(String url) {
		if (Util.isFullUrl(url)) {
			return url;
		} else if (Util.isFileUrl(url)) {
			return url;
		} else if (Util.isJarUrl(url)) {
			return url;
		} else if (Util.isAssets(url)) {
			return url;
		} else {
			String configurl = ParamsManager.readUrlParam(url, "file_url");
			return configurl;
		}
	}

	public static long lastproadcast = 0;

	public static void postBroadcast(Object obj, long size, long length) {
		if ((System.currentTimeMillis() - lastproadcast) > 500 || size == length) {
			BIntent bi = new BIntent(BroadCast.BROADLIST_FILEDOWNLOAD, obj, null, 0, null);
			bi.size = size;
			bi.lenth = length;
			bi.type = 101;
			BroadCast.PostBroad(bi);
			lastproadcast = System.currentTimeMillis();
		}

	}
}

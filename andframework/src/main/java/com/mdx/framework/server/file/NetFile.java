package com.mdx.framework.server.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.data.Method;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.server.file.FileDwonRead.ProgressListener;
import com.mdx.framework.utility.AppManager;
import com.mdx.framework.utility.Util;

import android.content.Context;

public class NetFile {
	public ProgressListener pgl = null;
	public DFile file = new DFile();
	public final static int DOWNLOADING = 1, WAIT = 0, DOWNLOADINGEND = 2;
	public static final String download = "/download";
	public PRunable prun;

	public NetFile(String name, String fileurl, String softid, String softpackage, String softVersion, String icon, ProgressListener pgl) {
		this.file.name = name;
		this.file.downloadUrl = fileurl;
		this.file.url = fileurl;
		this.file.filepath = getFileName(fileurl);
		this.file.softId = softid;
		this.file.softVersion = softVersion;
		this.file.softpackage = softpackage;
		this.pgl = pgl;
		this.file.icon = icon;
	}

	public NetFile(String name, String fileurl, String softid, String softpackage, String softVersion, String packageid, String phone, String tel, long time, String icon, ProgressListener pgl) {
		this.file.name = name;
		this.file.downloadUrl = fileurl;
		this.file.url = fileurl;
		this.file.filepath = getFileName(fileurl);
		this.file.softId = softid;
		this.file.softVersion = softVersion;
		this.file.softpackage = softpackage;
		this.pgl = pgl;
		this.file.phone = phone;
		this.file.tel = tel;
		this.file.time = time;
		this.file.packageid = packageid;
		this.file.icon = icon;
	}

	public static String getFileName(String url) {
		return Md5.mD5(url);
	}

	public NetFile(File file) throws Exception {
		this.file = DFile.read(file);
	}

	public void stop() {
		file.stop = true;
		if (prun != null) {
			prun.intermit();
		}
		prun = null;
	}

	public void setPrun(PRunable prun) {
		this.prun = prun;
	}

	public void store() {
		String filename = this.file.filepath;
		File file = Util.getPath(Frame.CONTEXT, download, filename + ".down");// Temp.getDpath(filename + ".down", download);
		try {
			this.file.write(file);
		} catch (Exception e) {
			File fie = Util.getPath(Frame.CONTEXT, download, filename + ".tmp");// Temp.getDpath(filename + ".tmp", download);
			if (fie.exists()) {
				fie.delete();
			}
		}
	}

	public File getDown() {
		return Util.getPath(Frame.CONTEXT, download, this.file.filepath + ".down");
	}

	public File getTmp() {
		return Util.getPath(Frame.CONTEXT, download, this.file.filepath + ".tmp");
	}

	public File getApk() {
		return Util.getPath(Frame.CONTEXT, download, this.file.filepath + ".apk");
	}

	public void install(Context context) {
		String path = Util.getPath(Frame.CONTEXT, download, this.file.filepath + ".apk").getPath();
		AppManager.install(context, path);
	}

	public void unInstall(Context context) {
		AppManager.deleteApp(context, this.file.softpackage);
	}

	public boolean delDown() {
		File file = getDown();
		if (file != null && file.exists()) {
			return file.delete();
		}
		return false;
	}

	public boolean delTmp() {
		File file = getTmp();
		if (file != null && file.exists()) {
			return file.delete();
		}
		return false;
	}

	public boolean delApk() {
		File file = getApk();
		if (file != null && file.exists()) {
			return file.delete();
		}
		return false;
	}

	public void nstop() {
		this.file.stop = false;
	}

	public void reset() {
		this.file.downstate = 0;
	}

	public static class DFile implements Serializable {
		public static final long serialVersionUID = 1L;
		public String downloadUrl = "";
		public String icon = "";
		public String name = "";
		public String filepath = "";
		public String softId = "";
		public String softVersion = "";
		public String downloadEnd = "";
		public long length = 0;
		public long nlength = 0;
		public String softpackage = "";
		public int state = -1;
		public int downstate = 0;
		public boolean stop = false;
		public String phone;
		public String tel;
		public long time;
		public String url;
		public String packageid;
		public Object tag;

		public void write(File file) throws Exception {
			FileOutputStream out = new FileOutputStream(file);
			Method.serializeZip(this, out);
		}

		public static DFile read(File file) throws Exception {
			Object obj = Method.unserializeZip(file);
			DFile retn = (DFile) obj;
			File fils = new File(file.getPath().replace(".down", ".tmp"));
			retn.nlength = fils.length();
			return retn;
		}
	}
}

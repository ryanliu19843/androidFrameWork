package com.mdx.framework.server.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.config.BaseConfig;
import com.mdx.framework.utility.AppManager;
import com.mdx.framework.utility.Util;

import android.content.Context;
import android.content.Intent;

public class DownloadFile {
	private ThreadPool threadpool = new ThreadPool();
	public ArrayList<NetFile> FILEDOWNLOADSLIST = new ArrayList<NetFile>();
	public static final String RECEIVER_START = "com.wjwl.apkfactory.frame.DOWNLOAD_FILE_START", RECEIVER_STOP = "com.wjwl.apkfactory.frame.DOWNLOAD_FILE_STOP",
			RECEIVER_DOWN = "com.wjwl.apkfactory.frame.DOWNLOAD_FILE_DOWN", RECEIVER_END = "com.wjwl.apkfactory.frame.DOWNLOAD_FILE_END";
	private PostRevicer thread = null;
	private boolean hasReceiver = true;
	private Context context;

	public DownloadFile(Context context, boolean hasReceiver, int thread) {
		this.context = context;
		this.hasReceiver = hasReceiver;
		threadpool.setMaxThreadSize(thread);
	}

	public DownloadFile() {
	}

	public boolean isHasReceiver() {
		return hasReceiver;
	}

	public void setHasReceiver(boolean hasReceiver) {
		this.hasReceiver = hasReceiver;
	}

	public void setMaxThread(int threads) {
		threadpool.setMaxThreadSize(threads);
	}

	public ArrayList<NetFile> getUnDownload() {
		ArrayList<NetFile> retn = new ArrayList<NetFile>();
		for (int i = 0; i < FILEDOWNLOADSLIST.size(); i++) {
			if (FILEDOWNLOADSLIST.get(i).file.state == NetFile.WAIT) {
				retn.add(FILEDOWNLOADSLIST.get(i));
			}
		}
		return retn;
	}

	public ArrayList<NetFile> getDownloaded() {
		ArrayList<NetFile> retn = new ArrayList<NetFile>();
		for (int i = 0; i < FILEDOWNLOADSLIST.size(); i++) {
			if (FILEDOWNLOADSLIST.get(i).file.state == NetFile.DOWNLOADINGEND) {
				retn.add(FILEDOWNLOADSLIST.get(i));
			}
		}
		return retn;
	}

	public void remove(NetFile netfile) {
		delete(netfile);
		deleteFile(netfile);
	}

	public void delete(NetFile netfile) {
		if (FILEDOWNLOADSLIST.contains(netfile)) {
			FILEDOWNLOADSLIST.remove(netfile);
			netfile.file.state = (-1);
		}
	}

	public void download(NetFile netfile) {
		netfile.reset();
		NetFile net = add(netfile);
		downloadfile(net, 0);
	}

	public NetFile add(NetFile netfile) {
		NetFile net = get(netfile.file.downloadUrl);
		if (net == null) {
			FILEDOWNLOADSLIST.add(netfile);
			netfile.file.state = 0;
			net = netfile;
			net.store();
		} else {
			net.pgl = netfile.pgl;
		}
		return net;
	}

	private void downloadfile(NetFile netfile, int type) {
		netfile.nstop();
		if (netfile.file.state == type) {
			Download down = new Download(netfile);
			netfile.setPrun(down);
			threadpool.submit(down);
		}
		if (FILEDOWNLOADSLIST.size() > 0 && thread == null && hasReceiver) {
			thread = new PostRevicer();
			thread.start();
		}
	}

	public void deleteFile(NetFile netfile) {
		netfile.delApk();
		netfile.delDown();
		netfile.delTmp();
	}

	public void installSoft(Context context, String url) {
		String filename = NetFile.getFileName(url);
		File file = Util.getPath(Frame.CONTEXT, NetFile.download, filename + ".apk");// Temp.getDpath(, NetFile.download);
		if (file.exists()) {
			AppManager.install(context, file.getPath());
		}
	}

	public void downloadAll() {
		ArrayList<NetFile> list = getUnDownload();
		for (int i = 0; i < getUnDownload().size(); i++) {
			NetFile netfile = list.get(i);
			downloadfile(netfile, 0);
		}
	}

	public boolean checkFile(NetFile netfile) {
		if (netfile == null || netfile.file == null) {
			return false;
		}
		File file = netfile.getApk();
		if (file == null) {
			return false;
		}
		return file.exists();
	}

	public void read(File file) {
		try {
			NetFile netfile = new NetFile(file);
			NetFile net = add(netfile);
			downloadfile(net, 1);
		} catch (Exception e) {
			file.delete();
		}
	}

	public void init() {
		File file = Util.getDPath(Frame.CONTEXT, NetFile.download);
		if (file != null && file.isDirectory()) {
			String[] list = file.list(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".down");
				}
			});
			for (String filename : list) {
				read(new File(file.getPath() + "/" + filename));
			}
		}
	}

	public String[] getFiles(final String end) {
		File file = Util.getDPath(Frame.CONTEXT, NetFile.download);
		if (file != null && file.isDirectory()) {
			String[] list = file.list(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith(end);
				}
			});
			return list;
		}
		return null;
	}

	public NetFile get(String url) {
		for (int i = 0; i < FILEDOWNLOADSLIST.size(); i++) {
			if (FILEDOWNLOADSLIST.get(i).file.downloadUrl.equals(url)) {
				return FILEDOWNLOADSLIST.get(i);
			}
		}
		return null;
	}

	public class PostRevicer extends Thread {
		public void run() {
			while (FILEDOWNLOADSLIST.size() > 0 && hasReceiver) {
				ArrayList<NetFile.DFile> list = new ArrayList<NetFile.DFile>();
				for (NetFile netfile : FILEDOWNLOADSLIST) {
					list.add(netfile.file);
				}
				postReveicer("file", list, RECEIVER_DOWN);
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			thread = null;
		}
	}

	public class Download extends PRunable {
		private NetFile netfile = null;

		public Download(NetFile netfile) {
			this.netfile = netfile;
			netfile.file.state = NetFile.DOWNLOADING;
			postReveicer("file", netfile.file, RECEIVER_START);
		}

		public void run() {
			File file = netfile.getTmp();
			if (file == null) {
				return;
			}
			FileDwonRead fd = (FileDwonRead) this.addIntermit(new FileDwonRead(netfile, file));
			int retn = 1;
			try {
				String durl = BaseConfig.getUpdateUri();
				boolean isget = false;
				if (durl != null && durl.startsWith("get/")) {
					isget = true;
					durl = durl.substring(4);
				}

				if (durl == null || durl.length() == 0) {
					if (isget) {
						retn = fd.get(netfile.file.downloadUrl, null);
					} else {
						retn = fd.post(netfile.file.downloadUrl, null);
					}
				} else {
					if (netfile.file.downloadUrl.indexOf("http://") >= 0 || netfile.file.downloadUrl.indexOf("https://") >= 0) {
						if (isget) {
							retn = fd.get(netfile.file.downloadUrl, null);
						} else {
							retn = fd.post(netfile.file.downloadUrl, null);
						}
					} else {
						if (isget) {
							retn = fd.get(durl, new String[][] { { "d", netfile.file.downloadUrl } });
						} else {
							retn = fd.post(durl, new String[][] { { "d", netfile.file.downloadUrl } });
						}
					}
				}
			} catch (MException e) {
				netfile.file.downstate = 3;
			}
			netfile.file.state = NetFile.WAIT;
			FILEDOWNLOADSLIST.remove(netfile);
			netfile.file.state = NetFile.DOWNLOADINGEND;
			if (retn == 0) {
				netfile.delDown();
				netfile.file.downstate = (4);
				postReveicer("file", netfile.file, RECEIVER_END);
			} else {
				postReveicer("file", netfile.file, RECEIVER_STOP);
				netfile.delApk();
				netfile.delDown();
				netfile.delTmp();
			}
		}

		public void onIntermit() {
			FILEDOWNLOADSLIST.remove(netfile);
			netfile.file.downstate = (3);
			postReveicer("file", netfile.file, RECEIVER_STOP);
			netfile.delApk();
			netfile.delDown();
			netfile.delTmp();
		}
	}

	private void postReveicer(String name, Serializable obj, String action) {
		if (hasReceiver && context != null) {
			Intent intent = new Intent();
			intent.setAction(action);
			intent.putExtra(name, obj);
			context.sendBroadcast(intent);
		}
	}

}

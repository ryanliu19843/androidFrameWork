package com.mdx.framework.server.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import com.mdx.framework.commons.MException;
import com.mdx.framework.server.HttpRead;

public class FileDwonRead extends HttpRead<Integer> {
	private NetFile netfile;
	private File outfile;

	public FileDwonRead(NetFile netFile, File outputFile) {
		this.netfile = netFile;
		this.outfile = outputFile;
	}

	@Override
	public Integer read(HttpURLConnection response, String url, String[][] params) throws MException {
		int result = 0;
		try {
			int code = response.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_PARTIAL) {
				long fileSize = response.getContentLength();
				long nowsize = netfile.file.nlength;
				netfile.file.downstate = 1;
				boolean type = true;
				netfile.file.length = fileSize;
				if (code == HttpURLConnection.HTTP_OK) {
					type = false;
					nowsize = 0;
				}
				InputStream is = response.getInputStream();
				FileOutputStream fos = new FileOutputStream(outfile, type);
				byte[] bt = new byte[1024 * 512];
				int i = 0;
				boolean isbreak = false;
				long time = System.currentTimeMillis(), stime = 0;
				while ((i = is.read(bt)) >= 0 && !this.stop) {
					if (netfile.file.stop) {
						isbreak = true;
						netfile.store();
						netfile.file.downstate = 3;
						break;
					}
					fos.write(bt, 0, i);
					nowsize += i;
					netfile.file.nlength = nowsize;
					stime = System.currentTimeMillis();
					if (stime - time > 100) {
						time = stime;
						if (netfile.pgl != null) {
							try {
								netfile.pgl.onProgress(nowsize, fileSize, 1);
							} catch (Exception e) {
							}
						}
					}
				}
				fos.flush();
				fos.close();
				is.close();
				if (i != -1 || this.stop) {
					throw new MException(97);
				}
				if (netfile.pgl != null) {
					if (!isbreak) {

						try {
							netfile.pgl.onProgress(nowsize, fileSize, 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (!isbreak) {
					netfile.file.downstate = 2;
					outfile.renameTo(new File(outfile.getPath().replace(".tmp", ".apk")));
					netfile.file.downstate = 4;
				} else {
					result = 1;
					netfile.file.downstate = 3;
				}
				if (netfile.pgl != null) {
					if (!isbreak) {
						try {
							netfile.pgl.onProgress(nowsize, fileSize, 4);
							netfile.pgl=null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							netfile.pgl.onProgress(nowsize, fileSize, 3);
							netfile.pgl=null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				result = 1;
			}

		} catch (MException e) {
			throw e;
		} catch (Exception e) {
			throw new MException(98, e);
		}
		return result;
	}

	@Override
	protected Integer onInit(String url, String[][] params) {
		if (netfile.pgl != null) {
			netfile.pgl.onProgress(0, netfile.file.length, 0);
		}
		return null;
	}

	public static interface ProgressListener extends Serializable {
		public void onProgress(long now, long all, int type);
	}
}

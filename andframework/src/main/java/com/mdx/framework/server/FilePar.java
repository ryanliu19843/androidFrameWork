/*
 * 文件名: FilePar.java
 * 版    权：  Copyright Huawei Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2013-12-12
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2013-12-12 上午10:24:31]
 */
public class FilePar {
	public String paramid;
	public String contexttype;
	public String disposition;
	public Object object;

	public FilePar() {
	}

	public FilePar(String name, Object obj) {
		this.paramid = name;
		this.object = obj;
	}

	public FilePar(String name, String contexttype, Object obj) {
		this.paramid = name;
		this.object = obj;
		this.contexttype = contexttype;
	}

	public void writeto(OutputStream out, String encode, String id) throws Exception {
		out.write(new byte[] { '-', '-' });
		out.write(id.getBytes(encode));
		out.write(new byte[] { '\r', '\n' });
		out.flush();
		if (object instanceof File) {
			File file = (File) object;
			disposition = "Content-Disposition: form-data; name=\"" + paramid + "\"; filename=\"" + file.getName() + "\"";
			contexttype = "Content-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\n";
			out.write(disposition.getBytes(encode));
			out.write(new byte[] { '\r', '\n' });
			out.write(contexttype.getBytes(encode));
			out.write(new byte[] { '\r', '\n' });
			out.flush();
			byte[] bt = new byte[1024];
			InputStream in = new FileInputStream(file);
			int ind = 0;
			while ((ind = in.read(bt)) >= 0) {
				out.write(bt, 0, ind);
			}
			in.close();
			out.flush();
		} else if (object instanceof byte[]) {
			disposition = "Content-Disposition: form-data; name=\"" + paramid + "\"; filename=\"bytesup\"";
			contexttype = "Content-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\n";
			out.write(disposition.getBytes(encode));
			out.write(new byte[] { '\r', '\n' });
			out.write(contexttype.getBytes(encode));
			out.write(new byte[] { '\r', '\n' });
			out.flush();
			out.write((byte[]) object);
			out.flush();

		} else if (object instanceof String) {
			disposition = "Content-Disposition: form-data; name=\"" + paramid + "\"";
			contexttype = "Content-Type: text/plain; charset=" + encode + "\r\nContent-Transfer-Encoding: 8bit\r\n";
			out.write(disposition.getBytes(encode));
			out.write(new byte[] { '\r', '\n' });
			out.write(contexttype.getBytes(encode));
			out.write(new byte[] { '\r', '\n' });
			out.flush();
			out.write(((String) object).getBytes(encode));
			out.write(new byte[] { '\r', '\n' });
			out.flush();
		}
	}
}

package com.mdx.framework.server.api.json;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Locale;
import android.text.TextUtils;
import com.mdx.framework.commons.MException;
import com.mdx.framework.server.HttpRead;

public class IntenetRead2Json extends HttpRead<String> {
	private String encode = "UTF-8";

	public IntenetRead2Json(String encode, String upcode) {

		if (!TextUtils.isEmpty(upcode)) {
			this.urlEncode = upcode.toUpperCase(Locale.ENGLISH);
		}
		if (!TextUtils.isEmpty(encode)) {
			this.encode = encode;
		}
	}

	public String read(HttpURLConnection response, String url, String[][] params) throws MException {
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
				byte[] bytes = fos.toByteArray();
				String encode = "UTF-8";
				if (TextUtils.isEmpty(encode)) {
					encode = this.encode.toUpperCase(Locale.ENGLISH);
				}
				String json;
				if (encode != null && encode.length() > 0) {
					json = new String(bytes, encode);
				} else {
					json = new String(bytes);
				}
				return json;
			} else {
				throw new MException(98, "http error " + code);
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e) {
			throw new MException(98, e);
		}
	};

}

package com.mdx.framework.server.image;

import android.content.ContentResolver;
import android.graphics.Bitmap;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.image.impl.ImageRead;
import com.mdx.framework.utility.BitmapRead;
import com.mdx.framework.utility.Util;

import java.util.HashMap;

public class MediaImageLoad implements ImageRead {

	private String mUrl;

	private Object mObj;

	private int mImageType;

	private boolean stoped;

	private String path;

	private HashMap<String, String> mParamsMap;

	public MediaImageLoad() {
		stoped = false;
		path = null;
		mParamsMap = new HashMap<String, String>();
	}

	public Object loadImageFromUrl(int width, int height) throws MException {
		MLog.D(MLog.SYS_RUN, "load image form media:"+path);
		int loadtype;
		ContentResolver resolver;
		mParamsMap.put(ParamsManager.PARAM_VIEW_HEIGHT, String.valueOf(height));
		mParamsMap.put(ParamsManager.PARAM_VIEW_WIDTH, String.valueOf(width));
		path = ParamsManager.getString(mUrl, mParamsMap);
		loadtype = 1;
		try {
			if (Util.isMedia(path)) {
				path = path.substring("MEDIA:".length());
				loadtype = 1;
			} else {
				path = path.substring("MINMEDIA:".length());
				loadtype = 3;
			}
			resolver = Frame.CONTEXT.getContentResolver();
			Bitmap bit;
			bit = android.provider.MediaStore.Images.Thumbnails.getThumbnail(resolver, Long.parseLong(path), loadtype, null);
			if (bit.getWidth() > width * 2) {
				bit = BitmapRead.decodeBitmapSize(bit, width, height);
			}
			if (stoped) {
				bit.recycle();
				return null;
			}
			return bit;
		} catch (Exception e) {
			MLog.D("system.run", (new StringBuilder("readimgerror:")).append(path).toString());
		}
		return null;
	}

	public void createRead(String url, Object object, int imageType, boolean useCache) {
		mParamsMap.clear();
		mUrl = url;
		mObj = object;
		mImageType = imageType;
		mParamsMap.put(ParamsManager.PARAM_DATA_OBJ, mObj.toString());
		mParamsMap.put("imageType", String.valueOf(imageType));
		mParamsMap.put("reload", String.valueOf(mImageType));
	}

	public void intermit() {
		stoped = true;
		if (path == null)
			return;
		else
			return;
	}

	public boolean needCache() {
		return false;
	}
}

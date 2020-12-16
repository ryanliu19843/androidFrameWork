package com.mdx.framework.widget;

import java.io.File;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import com.mdx.framework.Frame;
import com.mdx.framework.server.file.FileLoad;
import com.mdx.framework.server.file.impl.FileBase;

public class MFileView extends Button implements FileBase {

	protected FileLoad fileload = Frame.FILELOAD;

	protected Object obj = null;

	protected String mLoadingId = "";

	protected boolean loading = false, loaded = false;
	public OnFileLoaded onFileLoaded;
	public File mfile = null;

	public MFileView(Context context) {
		super(context);
	}

	public MFileView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MFileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setObj(Object obj) {
		setObj(obj, true);
	}

	private void setLoading(boolean bol) {
		loading = bol;
	}

	public void setObj(Object obj, boolean bol) {
		setLoading(false);
		if (obj != null && obj.toString().length() > 0) {
			setLoaded(false);
			if (obj.equals(this.obj)) {
				return;
			} else {
				mfile = null;
				this.obj = obj;
			}
		} else {
			mfile = null;
			this.obj = null;
		}
		toFileload();
		invalidate();
	}

	private void toFileload() {
		synchronized (this) {
			if (obj != null) {
				fileload.loadFile(this);
			}
		}
	}

	private void setLoaded(boolean bol) {
		loaded = bol;
	}

	@Override
	public Object getObj() {
		return obj;
	}

	@Override
	public boolean isCache() {
		return true;
	}

	@Override
	public boolean isReload() {
		return false;
	}

	@Override
	public int getLoadType() {
		return 0;
	}

	@Override
	public boolean isChange() {
		return false;
	}

	@Override
	public void loaded(final File file, final String loadingid) {
		post(new Runnable() {

			@Override
			public void run() {
				if (mLoadingId != null && mLoadingId.equals(loadingid)) {
					if (onFileLoaded != null) {
						mfile = file;
						onFileLoaded.onFileLoaded(mfile);
					}
				}
			}
		});
	}

	public void setOnFileLoaded(OnFileLoaded onFileLoaded) {
		this.onFileLoaded = onFileLoaded;
	}

	public interface OnFileLoaded {
		public void onFileLoaded(File file);
	}

	@Override
	public void setLoadid(String id) {
		this.mLoadingId = id;
	}

}

/*
 * 文件名: MAsynTask.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: Administrator 创建时间:2014-11-21
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.utility;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author Administrator
 * @version [2014-11-21 下午2:12:36]
 */
public abstract class MAsyncTask<Params, Progress, Result> {

	public void execute(@SuppressWarnings("unchecked") final Params... params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				final Result r = doInBackground(params);
				Util.post(new Runnable() {

					@Override
					public void run() {
						finish(r);
					}
				});
			}
		}).start();
	}

	protected abstract Result doInBackground(@SuppressWarnings("unchecked") Params... params);

	public void finish(Result result) {
	}

	public void progressUpdate(@SuppressWarnings("unchecked") final Progress... values) {
		Util.post(new Runnable() {

			@Override
			public void run() {
				onProgressUpdate(values);
			}
		});
	}

	public void onProgressUpdate(@SuppressWarnings("unchecked") Progress... values) {
	}
}

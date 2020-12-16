package com.mdx.framework.cache;

import com.mdx.framework.utility.CanLoad;

public class ObjectCache extends CacheItem<Object, CanLoad,String> {

	public ObjectCache(Object item) {
		super(item);
		this.init();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	protected long calcMemSize() {
		return 2000;
	}

	@Override
	public void destory() {
	}

}

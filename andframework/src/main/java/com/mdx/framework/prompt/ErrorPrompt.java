package com.mdx.framework.prompt;

import com.mdx.framework.server.api.ErrorMsg;

public interface ErrorPrompt {
	
	public void setMsg(ErrorMsg em);
	
	public void show();
	
	public void dismiss();
}

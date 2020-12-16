package com.mdx.framework.commons;

public class MException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private int code=0;

	public MException(int errorcode,String msg){
		super(msg);
		code=errorcode;
	}
	
	
	public MException(Exception e){
		super(e);
	}
	
	/**
	 * error 0 正确  81没有sd卡  99未知错误  98 网络错误  97程序编写错误 96数据解析错误 90授权错误
	 * @param errorcode
	 */
	public MException(int errorcode){
		code=errorcode;
	}
	
	public MException(int errorcode,Exception e){
	    super(e);
        code=errorcode;
    }


	public int getCode() {
		return code;
	}	
}

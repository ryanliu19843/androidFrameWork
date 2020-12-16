package com.mdx.framework.utility.handle;

import android.os.Handler;
import android.os.Message;

import com.mdx.framework.log.MLog;

public class MHandler extends Handler{
	public String id;
	public HandleMsgLisnener msglisnener;
	public int staus=0;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
//	/**
//	 * 调用loadData
//	 * @param type
//	 * @param obj
//	 */
//	public void send(int type,Object obj){
//		Message message=new Message();
//		message.what=200;
//		message.arg1=type;
//		message.obj=obj;
//		this.sendMessageL(message);
//	}
	
	/**
	 * 直接调用disposMsg
	 * @param type
	 * @param obj
	 */
	public void sent(int type,Object obj){
		Message message=new Message();
		message.what=201;
		message.arg1=type;
		message.obj=obj;
		this.sendMessageL(message);
	}
	
	/**
	 * 直接调用disposMsg
	 * @param obj
	 */
	public void sent(Object obj){
		Message message=new Message();
		message.what=201;
		message.arg1=0;
		message.obj=obj;
		this.sendMessageL(message);
	}
	
	/**
	 * 更新调用loaddate
	 * @param typs
	 */
	public void reload(int[] typs){
		Message message=new Message();
		message.what=100;
		message.obj=typs;
		this.sendMessageL(message);
	}
	
	public boolean sendMessageL(Message msg){
		MLog.D(MLog.SYS_RUN,this.getId()+" "+msg.arg1);
		return super.sendMessage(msg);
	}
	
	public void reload(){
		reload(null);
	}
	
	public void close(){
		this.sendEmptyMessage(0);
	}
	
	public interface HandleMsgLisnener {
		public void onMessage(Message msg);
	}
	
	public void setMsglisnener(HandleMsgLisnener msglisnener) {
		this.msglisnener = msglisnener;
	}
	
	@Override
	public synchronized void handleMessage(Message msg) {
		if(msglisnener!=null){
			msglisnener.onMessage(msg);
		}
	}
}

package com.mdx.framework.utility.application;

public class MContact {
	private long contactId=0;
	private String name="";
	private long photoId=0;
	private String phone="";
	private String pinyin="";
	
	public boolean search(String search){
		if(search==null || search.length()==0){
			return true;
		}
		if((pinyin+name+phone).indexOf(search)>=0){
			return true;
		}
		return false;
	}

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		try {
			pinyin=getpinyin(name);
		} catch (Exception e) {
		}
	}
	
	
	private String getpinyin(String str){
		return null;
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}

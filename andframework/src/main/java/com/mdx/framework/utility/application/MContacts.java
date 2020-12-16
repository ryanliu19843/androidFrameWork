package com.mdx.framework.utility.application;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;

public class MContacts {
	
	
	public List<MContact> getContact(Context ctx,OnContactAddListener oncont) {
		long contactid,photoid;
		String name;
		List<MContact> retn=new ArrayList<MContact>();
		Cursor cur = ctx.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			int photo = cur.getColumnIndex(ContactsContract.Contacts.PHOTO_ID);
			do {
				contactid= cur.getLong(idColumn);
				name = cur.getString(displayNameColumn);
				photoid = cur.getLong(photo);
				int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					Cursor phones = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactid, null, null);
					if (phones.moveToFirst()) {
						do {
							String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							MContact cont=new MContact();
							cont.setContactId(contactid);
							cont.setName(name);
							cont.setPhotoId(photoid);
							cont.setPhone(phoneNumber);
							retn.add(cont);
							if(oncont!=null){
								try {
									oncont.onAdd(cont);
								} catch (Exception e) {
									oncont=null;
								}
							}
						} while (phones.moveToNext());
						phones.close();
					}
				}
				
			} while (cur.moveToNext());
		}
		cur.close();
		return retn;
	}
	
	public static interface OnContactAddListener{
		public void onAdd(MContact cont) throws Exception;
	}
	
	public Drawable getPhoto(Context ctx,MContact cont){
		ContentResolver resolver = ctx.getContentResolver();
		Long photoid =cont.getPhotoId();
		if (photoid > 0) {
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cont.getContactId());
			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
			return Drawable.createFromStream(input, "photo"+photoid);
		} else {
		}	
		return null;
	}
}

package com.mdx.framework.utility.application;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;


public class App {

	public static List<AppInfo> getAppList(Context context){
		List<PackageInfo> packages =context.getPackageManager().getInstalledPackages(0);
		List<AppInfo> list=new ArrayList<AppInfo>();
		for(PackageInfo packinfo:packages){
			AppInfo apk=new AppInfo();
			ApplicationInfo app=packinfo.applicationInfo;
			if((app.flags&ApplicationInfo.FLAG_SYSTEM)==0){
				apk.setName(app.loadLabel(context.getPackageManager()).toString());
				apk.setPackage(packinfo.packageName);
				apk.setVersion(packinfo.versionCode);
				apk.setVersionName(packinfo.versionName);
				if(!apk.getPackage().startsWith("com.wjwl.apkfactory")){
					list.add(apk);
				}
			}
		}
		return list;
	}
	
	public static Drawable getIcon(Context context,String packageName) throws NameNotFoundException{
		PackageInfo pack=context.getPackageManager().getPackageInfo(packageName, 0);
		ApplicationInfo app=pack.applicationInfo;
		return app.loadIcon(context.getPackageManager());
	}
	
	public static AppInfo getApp(Context context,String packageName) throws NameNotFoundException{
		PackageInfo pack=context.getPackageManager().getPackageInfo(packageName, 0);
		AppInfo apk=new AppInfo();
		ApplicationInfo app=pack.applicationInfo;
		apk.setName(app.loadLabel(context.getPackageManager()).toString());
		apk.setPackage(pack.packageName);
		apk.setVersion(pack.versionCode);
		apk.setVersionName(pack.versionName);
		return apk;
	}
	
	public static void install(Context context,String path){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://"+ path),"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static void open(Context context,String packag){
		Intent intent=context.getPackageManager().getLaunchIntentForPackage(packag);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static void deleteApp(Context context,String packag){
		Uri packageURI = Uri.parse("package:"+packag);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);   
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(uninstallIntent);
	}
	
	public static List<MContact> getContacts(Context context){
		return getContacts(context,null);
	}
	
	public static List<MContact> getContacts(Context context,MContacts.OnContactAddListener onadd){
		MContacts conts=new MContacts();
		return conts.getContact(context,onadd);
	}
	
	public static Drawable getContantPhoto(Context context,MContact contact){
		MContacts conts=new MContacts();
		return conts.getPhoto(context, contact);
	}
}

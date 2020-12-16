package com.mdx.framework.server;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.mdx.framework.activity.taskactivity.ActRestart;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.config.BaseConfig;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.base.ApiUpdateApi;
import com.mdx.framework.server.api.base.Msg_Key;
import com.mdx.framework.server.api.base.Msg_Update;
import com.mdx.framework.utility.AppManager;

import java.util.Locale;


/**
 * Created by ryan on 2017/5/23.
 * 从服务器获取参数
 */



public class ServerParamsInit {
    public final static String SYNCPARAMS_INTERVAL="syncparams_interval";
    public final static String SYNCPARAMS_CACHETIME="syncparams_cachetime";

    public Context context;

    public void DataLoad(Context context) {
        this.DataLoad(context,-1);
    }

    public static long lastgettime=0;
    public static long syncparamsinterval=-1;
    public static long synccachetime=-1;

    public void DataLoad(Context context,long delaytime){
        if(syncparamsinterval<0) {
            syncparamsinterval = ParamsManager.getLongValue(SYNCPARAMS_INTERVAL);
        }
        if(synccachetime<0){
            synccachetime=ParamsManager.getLongValue(SYNCPARAMS_CACHETIME);
        }
        if(System.currentTimeMillis()-lastgettime<syncparamsinterval){
            return;
        }
        lastgettime=System.currentTimeMillis();
        int version = 0;
        this.context=context;
        String appid = "default";
        try {
            version = AppManager.getApp(context, context.getPackageName()).versionCode;
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appid = appInfo.metaData.getString("UDOWS_APPKEY").trim();
        } catch (Exception e) {
        }
        ApiUpdateApi apiupdate = new ApiUpdateApi();
        if(delaytime>0) {
            apiupdate.setCatcheTime(delaytime);
        }
        if(synccachetime>=0) {
            apiupdate.setCatcheTime(synccachetime);
        }
        apiupdate.setSonId("0");
        SharedPreferences sup = context.getSharedPreferences("default_update_param", Context.MODE_PRIVATE);
        String source = sup.getInt("updatesource", -2) == -2 ? null : (sup.getInt("updatesource", -2) + "");
        apiupdate.load("APP_UPDATE_SELF", context, this, "disposeMessage", context.getPackageName(), version, "android", appid, source, null);
    }


    public void disposeMessage(Son son) throws Exception {
        if (son.getError() == 0) {
            Msg_Update bu = (Msg_Update) son.getBuild();
            SharedPreferences sup = context.getSharedPreferences("default_update_param", Context.MODE_PRIVATE);
            if (bu.sourceUPdate == 1) {  //是否更新来源，如果更新来源则更新味服务器最新的版本
                SharedPreferences.Editor editor = sup.edit();
                editor.putInt("updatesource", bu.source);
                editor.commit();
            } else {
                if (sup.getInt("updatesource", -2) == -2) {  //是否存在source，如果存在则不操作，如果不存在则更新到服务器的
                    SharedPreferences.Editor editor = sup.edit();
                    //更新版本  内测，测试，正式
                    editor.putInt("updatesource", bu.source);
                    editor.commit();
                }
            }

            boolean isneedrestard=false;

            //判断是否需要更新参数
            if (son.getSonId().equals("0")) {
                SharedPreferences sp = context.getSharedPreferences("default_param", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                for (Msg_Key msg : bu.keys) {
                    // serverurl对应baseconfig的uri
                    if(msg.code.toLowerCase().equals("serverurl") && !msg.value.toLowerCase().equals(BaseConfig.getUri().toLowerCase())){
                        isneedrestard=true;
                    }
                    //对应baseconfig的url
                    if(BaseConfig.getNUri(msg.code)!=null && !msg.value.toLowerCase().equals(BaseConfig.getNUri(msg.code).toLowerCase())){
                        isneedrestard=true;
                    }
                    if(msg.code.toLowerCase().equals(SYNCPARAMS_INTERVAL)){
                        try {
                            syncparamsinterval = Long.valueOf(msg.value);
                        }catch (Exception e){

                        }
                    }
                    if(msg.code.toLowerCase().equals(SYNCPARAMS_CACHETIME)){
                        try {
                            synccachetime = Long.valueOf(msg.value);
                        }catch (Exception e){

                        }
                    }
                    //保存
                    editor.putString(msg.code, msg.value);
                    ParamsManager.put(msg.code.toLowerCase(Locale.ENGLISH), msg.value);
                }
                editor.commit();
            }
            if(isneedrestard){
                Intent intent=new Intent(context, ActRestart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        }
    }
}

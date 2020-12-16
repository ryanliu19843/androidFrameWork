## Add project specific ProGuard rules here.
## By default, the flags in this file are appended to flags specified
## in D:\exploiture\android\sdk/tools/proguard/proguard-android.txt
## You can edit the include path and order by changing the proguardFiles
## directive in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## Add any project specific keep options here:
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
#
#
#-keep class * extends android.app.Activity
#-keep class * extends android.app.Application
#-keep class * extends android.app.Service
#-keep class * extends com.squareup.wire.Message
#-keep public class * extends android.support.v4.app.Fragment
#-keep public class * implements com.mdx.framework.prompt.Fragment { *; }
#-keep public class * implements com.mdx.framework.prompt.Prompt { *; }
#-keep public class android.os.IHardwareService$Stub
#-keep public class android.os.SystemProperties
#-keep class **$Builder extends com.squareup.wire.Message$Builder { *; }
#-keep class com.squareup.** { *; }
#-keep class org.apache.** { *; }
#-keep class android.net.TrafficStats
#-keep class * extends android.databinding.ViewDataBinding { *; }
#-keep class * extends com.mdx.framework.server.api.json.JsonData { *; }
#
#-keep class * extends com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold{
#       public static ** getView(...);
#}
#
#-keepnames class * extends com.squareup.wire.Message {
#       public <fields>;
#       public <methods>;
#}
#-keepclassmembers class *{
#  public void *(com.mdx.framework.server.api.Son);
#  public void *(...,com.mdx.framework.server.api.Son);
#  public ** disposeMsg(...);
#  public ** setActionBar(...);
#  public Boolean onOptionsItemSelected(...);
#  public ** init(...);
#  public ** setFlashlightEnabled(...);
#  public ** setDisplayOrientation(...);
#  public ** getUidRxBytes(...);
#}
################################ 添加排除混淆类  #################################
#-keep class com.taobao.wireless.**{*;}
#-keep class com.alibaba.** {*;}
################################################################################
#-dontwarn org.apache.**
#-dontwarn com.squareup.**
#-dontwarn okio.Okio
################################ 百度   ##########################################
#-keep class com.baidu.** {*;}
#-keep class vi.com.** {*;}
#-dontwarn com.baidu.**
################################################个推#################################
#-dontwarn com.igexin.**
#-keep class com.igexin.** { *; }
#-keep class org.json.** { *; }
###################################排除警告#############################################
#-dontwarn com.alibaba.**
#-dontwarn com.taobao.**
#-dontwarn com.unionpay.**
#-dontwarn demo.Pinyin4jAppletDemo**
#-dontwarn com.mdx.framework.**
#-dontwarn com.udows.udowsmap.**
###############################################################################
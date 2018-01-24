# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudioProjects\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5  				#指定代码的压缩级别 0 - 7
-dontusemixedcaseclassnames  		#是否使用大小写混合
-dontskipnonpubliclibraryclasses  	#如果应用程序引入的有jar包，并且想混淆jar包里面的class
-dontpreverify  					#混淆时是否做预校验（可去掉加快混淆速度）
-verbose 							#混淆时是否记录日志（混淆后生产映射文件 map 类名 -> 转化后类名的映射
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  #混淆采用的算法

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference


  #保持 native 的方法不去混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

  #保持自定义控件类不被混淆，指定格式的构造方法不去混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

  #保持指定规则的方法不被混淆（Android layout 布局文件中为控件配置的onClick方法不能混淆）
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);

}

  #保持自定义控件指定规则的方法不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

  #保持枚举 enum 不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

 #保持 Parcelable 不被混淆（aidl文件不能去混淆）
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

 #需要序列化和反序列化的类不能被混淆（注：Java反射用到的类也不能被混淆）
-keepnames class * implements java.io.Serializable

  #保护实现接口Serializable的类中，指定规则的类成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
 #过滤泛型（不写可能会出现类型转换错误，一般情况把这个加上就是了）
-keepattributes Signature

 #假如项目中有用到注解，应加入这行配置
-keepattributes *Annotation*

 #保持R文件不被混淆，否则，你的反射是获取不到资源id的
-keep class **.R$* { *; }

#保护WebView对HTML页面的API不被混淆
-keep class **.Webview2JsInterface { *; }

#如果你的项目中用到了webview的复杂操作 ，最好加入
-keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
     public boolean *(android.webkit.WebView,java.lang.String);
}

#如果你的项目中用到了webview的复杂操作 ，最好加入
-keepclassmembers class * extends android.webkit.WebChromeClient {
     public void *(android.webkit.WebView,java.lang.String);
}

#转换JSON的JavaBean，类成员名称保护，使其不被混淆
-keepclassmembernames class com.cgv.cn.movie.common.bean.** { *; }
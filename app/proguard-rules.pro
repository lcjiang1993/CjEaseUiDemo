# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#****************baiduMap****************************
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#****************baiduMap****************************

#环信
-keep class org.xmlpull.** {*;}
-keep class com.hyphenate.** {*;}
-keep class com.hyphenate.chat.** {*;}
-dontwarn  com.hyphenate.**
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
#2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}
-keep class com.superrtc.** { *; }

#华为混淆
-keep class com.huawei.hms.update.** {
*;
}
-dontwarn com.huawei.hms.update.**

# OrmLite uses reflection
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keepattributes *Annotation*
-keepclassmembers class com.package.bo.** { *; }
-keepclassmembers class * {
@com.j256.ormlite.field.DatabaseField *;
}
-keepattributes Signature
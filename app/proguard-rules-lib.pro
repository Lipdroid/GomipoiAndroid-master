# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Kurosuke/Library/Android/sdk/tools/proguard/proguard-android.txt
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

-dontwarn com.facebook.**
-dontwarn com.google.**
-dontwarn net.nend.**

# ------------------------------
# Google関連
# ------------------------------
-keep public class com.google.** {
   public *;
}
-keepclassmembers class com.google.** {
   public *;
}

# ------------------------------
# nend
# ------------------------------
-keep class net.nend.android.** { *; }
-keep public class com.google.ads.mediation.* { public *; }
-keep class com.facebook.**{*;}
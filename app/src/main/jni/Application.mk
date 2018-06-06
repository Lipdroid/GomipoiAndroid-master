# Build対象のABIを指定 (all指定って大丈夫なん？...;)
#APP_ABI := all
APP_ABI := armeabi armeabi-v7a mips x86

# アプリケーションのminimum SDK versionを指定
APP_PLATFORM := android-15

# APP_OPTIM := release OR debug
APP_OPTIM := release

APP_STL += gnustl_static
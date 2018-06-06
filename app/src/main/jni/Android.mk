LOCAL_PATH := $(call my-dir)

# ------------------------------
# libPng
# ------------------------------
include $(CLEAR_VARS)

LOCAL_MODULE := libpng16
LOCAL_SRC_FILES :=\
	commonAPI/lib/libpng/png.c \
	commonAPI/lib/libpng/pngerror.c \
	commonAPI/lib/libpng/pngget.c \
	commonAPI/lib/libpng/pngmem.c \
	commonAPI/lib/libpng/pngpread.c \
	commonAPI/lib/libpng/pngread.c \
	commonAPI/lib/libpng/pngrio.c \
	commonAPI/lib/libpng/pngrtran.c \
	commonAPI/lib/libpng/pngrutil.c \
	commonAPI/lib/libpng/pngset.c \
	commonAPI/lib/libpng/pngtest.c \
	commonAPI/lib/libpng/pngtrans.c \
	commonAPI/lib/libpng/pngwio.c \
	commonAPI/lib/libpng/pngwrite.c \
	commonAPI/lib/libpng/pngwtran.c \
	commonAPI/lib/libpng/pngwutil.c

# 64bitマシンも32bit互換モードで動作させる
# (libPNG(1.16.x)が64bitに対応していない＞＜)
LOCAL_MULTILIB := 32

include $(BUILD_STATIC_LIBRARY)

# ------------------------------
# AppModule
# ------------------------------
include $(CLEAR_VARS)

# ラムダ式を使うためにはC++11が必要
# LOCAL_CFLAGS += -std=c++11
LOCAL_CPP_FEATURES += exceptions

LOCAL_CPPFLAGS := \
	-DIS_ANDROID=1\
	-DPICOJSON_USE_LOCALE=0\
	-std=c++11\

LOCAL_MODULE    := AppModule
LOCAL_SRC_FILES :=\
	PreferenceBridge.cpp \
	PurchaseBridge.cpp \
	billing/DeveloperPayLoad.c \
	billing/md5.c \
	commonAPI/JniBridge.cpp \
	commonAPI/lib/utility/PngUtils.cpp \
	commonAPI/lib/utility/TimeUtils.cpp \
	commonAPI/lib/utility/GlUtils.cpp \
	commonAPI/lib/utility/RandomUtils.cpp \
	commonAPI/lib/callback/AppManagerCallback.cpp \
	commonAPI/lib/callback/PausingCallback.cpp \
	commonAPI/lib/manager/AppManagerBase.cpp \
	commonAPI/lib/manager/ShaderManager.cpp \
	commonAPI/lib/manager/event/ClickEventManager.cpp \
	commonAPI/lib/manager/event/TrackingEventManager.cpp \
	commonAPI/lib/model/parts/PartsBase.cpp \
	commonAPI/lib/model/animation/AnimationBase.cpp \
	commonAPI/lib/model/animation/general/ScaleAnimation.cpp \
	commonAPI/lib/model/animation/general/RotateAnimation.cpp \
	commonAPI/lib/model/animation/general/FadeAnimation.cpp \
	commonAPI/lib/model/animation/general/TranslateAnimation.cpp \
	commonAPI/lib/model/animation/general/TranslateBlendAnimation.cpp \
	commonAPI/lib/model/animation/general/FrameAnimation.cpp \
	commonAPI/lib/model/animation/general/CurveAnimation.cpp \
	commonAPI/lib/model/animation/general/PathAnimation.cpp \
	commonAPI/lib/model/animation/general/MultipleAnimationSet.cpp \
	commonAPI/lib/model/animation/general/OrderAnimationSet.cpp \
	commonAPI/app/manager/AppManager.cpp \
	commonAPI/app/manager/GameManager.cpp \
	commonAPI/app/manager/PlayerManager.cpp \
	commonAPI/app/model/animation/PoikoMoveAnimation.cpp \
	commonAPI/app/model/animation/PoikoSweepAnimation.cpp \
	commonAPI/app/model/animation/PoikoFallAnimation.cpp \
	commonAPI/app/model/missions/SecretMission.cpp \
	commonAPI/app/model/parts/PoikoParts.cpp \
	commonAPI/app/model/parts/HolePartsBase.cpp \
	commonAPI/app/model/parts/GarbagePartsBase.cpp \
	commonAPI/app/model/parts/GemParts.cpp \
	commonAPI/app/model/data/SwipeItemData.cpp \
	commonAPI/app/model/data/GarbageData.cpp \
	commonAPI/app/model/data/GemData.cpp \
	commonAPI/app/utility/JsonUtils.cpp \
	commonAPI/app/utility/GarbageUtils.cpp \
	commonAPI/app/model/parts/AutoBroomParts.cpp \
	commonAPI/app/model/animation/AutoBroomSweepAnimation.cpp \

# 必要な外部参照を追加する
LOCAL_LDLIBS := -llog
LOCAL_LDLIBS += -lGLESv2
LOCAL_LDLIBS += -landroid
LOCAL_LDLIBS += -lz
LOCAL_LDLIBS += -latomic

# 64bitマシンも32bit互換モードで動作させる
# (libPNG(1.16.x)が64bitに対応していない＞＜)
LOCAL_MULTILIB := 32

# libPNGをstaticLibraryとして、追加する
LOCAL_C_INCLUDES += $(LOCAL_PATH)/libpng
LOCAL_STATIC_LIBRARIES := libpng16

include $(BUILD_SHARED_LIBRARY)

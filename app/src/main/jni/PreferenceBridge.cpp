//
//
//
#include <jni.h>
#include <string.h>

#include "commonAPI/lib/utility/android_log.h"

#define BASIC_USER_NAME "mix"
#define BASIC_PASSWORD "D6utVxKw"
#define PREF_KEY_ENABLED_SE "PrefKey1"
#define PREF_KEY_ENABLED_BGM "PrefKey2"
#define PREF_KEY_USER_ID "PrefKey3"
#define PREF_KEY_USER_PASSWORD "PrefKey4"
#define PREF_KEY_PICPOI_DATE "PrefKey5"
#define PREF_KEY_PICPOI_COUNT "PrefKey6"
#define PREF_KEY_BONUS_START_TIME "PrefKey7"
#define PREF_KEY_ADD_GARBAGE_START_TIME "PrefKey8"
#define PREF_KEY_HERO_DRINK_APPEAR_TIME "PrefKey9"
#define PREF_KEY_Z_DRINK_START_TIME "PrefKey10"
#define PREF_KEY_DROP_START_TIME "PrefKey11"


#define APPLICATION_ID 1002

// ------------------------------
// JNI Function Define
// ------------------------------
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyEnabledSe(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyEnabledBgm(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetBasicUsername(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetBasicPassword(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyUserId(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyUserPassword(JNIEnv* env, jclass clazz);

JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyPicPoiDate(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyPicPoiCount(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyBonusStartTime(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyAddGarbageStartTime(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyHeroDrinkAppearTime(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyZDrinkStartTime(JNIEnv* env, jclass clazz);
JNIEXPORT jstring JNICALL Java_app_preference_PJniBridge_nativeGetKeyDropStartTime(JNIEnv* env, jclass clazz);

JNIEXPORT jint JNICALL Java_app_preference_PJniBridge_nativeGetApplicationId(JNIEnv* env, jclass clazz);

#ifdef __cplusplus
}
#endif

// ------------------------------
// Jni Called
// ------------------------------
JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyEnabledSe(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_ENABLED_SE);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyEnabledBgm(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_ENABLED_BGM);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetBasicUsername(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(BASIC_USER_NAME);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetBasicPassword(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(BASIC_PASSWORD);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyUserId(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_USER_ID);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyUserPassword(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_USER_PASSWORD);
}

JNIEXPORT jint JNICALL
Java_app_preference_PJniBridge_nativeGetApplicationId(JNIEnv* env, jclass clazz)
{
    return APPLICATION_ID;
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyPicPoiDate(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_PICPOI_DATE);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyPicPoiCount(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_PICPOI_COUNT);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyBonusStartTime(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_BONUS_START_TIME);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyAddGarbageStartTime(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_ADD_GARBAGE_START_TIME);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyHeroDrinkAppearTime(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_HERO_DRINK_APPEAR_TIME);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyZDrinkStartTime(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_Z_DRINK_START_TIME);
}

JNIEXPORT jstring JNICALL
Java_app_preference_PJniBridge_nativeGetKeyDropStartTime(JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(PREF_KEY_DROP_START_TIME);
}
//
//
//
#include <jni.h>
#include <stdio.h>
#include <malloc.h>
#include <string.h>

#include "billing/DeveloperPayLoad.h"

#include "commonAPI/lib/utility/android_log.h"

// key取得用サイズ
#define SIZE_MAX_STORE_KEY 1024
#define SIZE_MD5_BUFFER 4096

typedef enum
{
    StoreItem_unknown = 0,
    StoreItem_gem10 = 1,
    StoreItem_gem50 = 2,
    StoreItem_gem100 = 3,
    StoreItem_gem300 = 4,
    StoreItem_gem500 = 5,
    StoreItem_gem1000 = 6,
} StoreItem;

static const char* productId1 = "gb_jwl_10";
static const char* productId2 = "gb_jwl_50";
static const char* productId3 = "gb_jwl_100";
static const char* productId4 = "gb_jwl_300";
static const char* productId5 = "gb_jwl_500";
static const char* productId6 = "gb_jwl_1000";

// ------------------------------
// JNI Function Define
// ------------------------------
#ifdef __cplusplus
extern "C" {
#endif

static const char* publicKey1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmzh9aEkEmUNYe8cEHC/OgSaj6GMA19Oa3DwspLnffs2x2qUg7JQ1KEc6Wba9uEogV5FumPdgjJjtvVQ9r";
static const char* publicKey2 = "NtE0PeRdjdFvf04nDMPWgWPybBDO+CFQ4M1m1X4G/S81MVoYW1PQjQhngueM8YMdvxQj+onXgk7lQFkNXS/6S/4xVN9CDwbW+6R83LVo/jReLKNFnmyYk0RTtdFc0";
static const char* publicKey3 = "34WpiATcSm8bl9y6DvLOSVByaQSwWrmB+938+qfqTS666cimNx6AGChAAaewWWxo0oeZuqApmjLjkc+dPQv2pHFTStnipMBCcTlldx5qzHrDHkQq9NrKyYkxHIJrv";
static const char* publicKey4 = "xSmCT2H7CiQIDAQAB";

static const char* productSolt = "alcGomipoi2016";

JNIEXPORT jbyteArray JNICALL Java_app_billing_PurchaseManager_getPurchaseItem(JNIEnv *env, jobject thiz);
JNIEXPORT jbyteArray JNICALL Java_app_billing_PurchaseManager_getStoreItem(JNIEnv *env,jobject thiz, jint type);
JNIEXPORT jint JNICALL Java_app_billing_PurchaseManager_getStoreItemValue(JNIEnv *env,jobject thiz, jstring jstring);
JNIEXPORT jbyteArray JNICALL Java_app_billing_PurchaseManager_createPayload(JNIEnv *env, jobject thiz, jstring jstring);
JNIEXPORT jboolean JNICALL Java_app_billing_PurchaseManager_verifyPayload(JNIEnv *env,jobject thiz,jstring receivedPayload, jstring skuString);
JNIEXPORT void JNICALL Java_app_billing_PurchaseManager_decrypt(JNIEnv *env, jobject thiz, jbyteArray srcBytes);
JNIEXPORT void JNICALL Java_app_billing_PurchaseManager_encryptBuffer(JNIEnv *env, jobject thiz, jbyteArray srcBytes);

#ifdef __cplusplus
}
#endif

// ------------------------------
// Jni Called
// ------------------------------
JNIEXPORT jbyteArray JNICALL
Java_app_billing_PurchaseManager_getPurchaseItem(JNIEnv *env, jobject thiz)
{
    char key[SIZE_MAX_STORE_KEY];
    int length = 0;

    memset(key,0,SIZE_MAX_STORE_KEY);

    strcat(key,publicKey1);
    strcat(key,publicKey2);
    strcat(key,publicKey3);
    strcat(key,publicKey4);

    length = strlen(key);

    jbyteArray jbyteArray = env->NewByteArray(length);
    if (jbyteArray == NULL)
    {
        return NULL;
    }

    jbyte *jbyte = env->GetByteArrayElements(jbyteArray,NULL);

    if (jbyte == NULL)
    {
        return NULL;
    }

    for (int i = 0; i < length; i++)
    {
        jbyte[i] = key[i];
    }

    env->ReleaseByteArrayElements(jbyteArray,jbyte,0);

    return jbyteArray;
}

/**
 * ストアの課金アイテムIDを取得する
 */
JNIEXPORT jbyteArray JNICALL
Java_app_billing_PurchaseManager_getStoreItem(JNIEnv *env,jobject thiz, jint type)
{
    const char *selectedId;

    switch(type)
    {
        case StoreItem_gem10:
            selectedId = productId1;
            break;

        case StoreItem_gem50:
            selectedId = productId2;
            break;

        case StoreItem_gem100:
            selectedId = productId3;
            break;

        case StoreItem_gem300:
            selectedId = productId4;
            break;

        case StoreItem_gem500:
            selectedId = productId5;
            break;

        case StoreItem_gem1000:
            selectedId = productId6;
            break;

        case StoreItem_unknown:
        default:
            return NULL;
    }

    int length = strlen(selectedId);

    jbyteArray jbyteArray = env->NewByteArray(length);
    if (jbyteArray == NULL)
    {
        return NULL;
    }

    jbyte *jbyte = env->GetByteArrayElements(jbyteArray,NULL);
    if (jbyte == NULL)
    {
        return NULL;
    }

    for (int i = 0; i < length; i++)
    {
        jbyte[i] = selectedId[i];
    }

    env->ReleaseByteArrayElements(jbyteArray,jbyte,0);

    return jbyteArray;
}

/**
 * ストアのskuから対応する数字を渡す
 */
JNIEXPORT jint JNICALL
Java_app_billing_PurchaseManager_getStoreItemValue(JNIEnv *env,jobject thiz, jstring jstring)
{
    if (jstring == NULL)
    {
        return 0;
    }

    const char *itemId = env->GetStringUTFChars(jstring, NULL);

    if (itemId == NULL)
    {
        env->ReleaseStringUTFChars(jstring, itemId);
        return 0;
    }

    StoreItem selectedItem = StoreItem_unknown;

    if (strcmp(productId1, itemId) == 0)
    {
        selectedItem = StoreItem_gem10;
    }
    else if (strcmp(productId2, itemId) == 0)
    {
        selectedItem = StoreItem_gem50;
    }
    else if (strcmp(productId3, itemId) == 0)
    {
        selectedItem = StoreItem_gem100;
    }
    else if (strcmp(productId4, itemId) == 0)
    {
        selectedItem = StoreItem_gem300;
    }
    else if (strcmp(productId5, itemId) == 0)
    {
        selectedItem = StoreItem_gem500;
    }
    else if (strcmp(productId6, itemId) == 0)
    {
        selectedItem = StoreItem_gem1000;
    }

    env->ReleaseStringUTFChars(jstring, itemId);

    return selectedItem;
}

JNIEXPORT jbyteArray JNICALL
Java_app_billing_PurchaseManager_createPayload(JNIEnv *env, jobject thiz, jstring jstring)
{
    if (jstring == NULL)
    {
        return NULL;
    }

    char *payLoad = (char *) malloc(sizeof(char) * SIZE_MD5_BUFFER);
    memset(payLoad, 0, sizeof(char) * SIZE_MD5_BUFFER);

    const char *productId = env->GetStringUTFChars(jstring, NULL);
    // サーバに送信するペイロード作成
    createDeveloperPayLoad(payLoad, productId, productSolt);
    env->ReleaseStringUTFChars(jstring, productId);

    int length = strlen(payLoad);

    jbyteArray jbyteArray = env->NewByteArray(length);
    if (jbyteArray == NULL)
    {
        return NULL;
    }

    jbyte *jbyte = env->GetByteArrayElements(jbyteArray, NULL);
    if (jbyte == NULL)
    {
        return NULL;
    }

    for (int i = 0; i < length; i++)
    {
        jbyte[i] = payLoad[i];
    }

    free(payLoad);
    env->ReleaseByteArrayElements(jbyteArray, jbyte, 0);

    return jbyteArray;
}

/**
 * google サーバから受け取ったpayloadが正しいかチェックする
 * jstring:md5化された文字列
 */JNIEXPORT jboolean JNICALL
Java_app_billing_PurchaseManager_verifyPayload(JNIEnv *env,jobject thiz,jstring receivedPayload, jstring skuString)
{
    if (receivedPayload == NULL)
    {
        return JNI_FALSE;
    }

    jboolean result = JNI_FALSE;

    const char *payload = env->GetStringUTFChars(receivedPayload, NULL);
    if (payload == NULL)
    {
        env->ReleaseStringUTFChars(receivedPayload, payload);
        return JNI_FALSE;
    }

    // skuからpayload作成
    char *createdPayload = (char *) malloc(sizeof(char) * SIZE_MD5_BUFFER);
    memset(createdPayload, 0, sizeof(char) * SIZE_MD5_BUFFER);

    const char *sku = env->GetStringUTFChars(skuString, NULL);

    if (sku == NULL)
    {
        env->ReleaseStringUTFChars(skuString, sku);
        env->ReleaseStringUTFChars(receivedPayload, payload);
        return JNI_FALSE;
    }

    createDeveloperPayLoad(createdPayload, sku, productSolt);
    // 正しくなければエラー
    if (strcmp(payload, createdPayload) == 0)
    {
        result = JNI_TRUE;
    }

    // release sku
    env->ReleaseStringUTFChars(skuString, sku);

    // release received payload
    env->ReleaseStringUTFChars(receivedPayload, payload);

    return result;
}

JNIEXPORT void JNICALL
Java_app_billing_PurchaseManager_decrypt(JNIEnv *env, jobject thiz, jbyteArray srcBytes)
{
    jboolean isCopy;
    int nSize;

    jbyte *srcArray = env->GetByteArrayElements(srcBytes, &isCopy);

    nSize = env->GetArrayLength(srcBytes);

    for (int i = 0; i < nSize; i++)
    {
        srcArray[i] ^= 0xff;
    }

    env->ReleaseByteArrayElements(srcBytes, srcArray, 0);
}

/**
 * カード図鑑画像隠蔽化
 * 実際はdecryptと同じ処理
 */
JNIEXPORT void JNICALL
Java_app_billing_PurchaseManager_encryptBuffer(JNIEnv *env, jobject thiz, jbyteArray srcBytes)
{
    jboolean isCopy;
    int nSize;

    jbyte *srcArray = env->GetByteArrayElements(srcBytes, &isCopy);

    nSize = env->GetArrayLength(srcBytes);

    for (int i = 0; i < nSize; i++)
    {
        srcArray[i] ^= 0xff;
    }

    env->ReleaseByteArrayElements(srcBytes, srcArray, 0);
}
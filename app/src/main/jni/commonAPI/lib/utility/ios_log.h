//
//
//
#ifndef TEST_IOS_LOG_H
#define TEST_IOS_LOG_H

// ------------------------------
// Log
// ------------------------------
#define WRITABLE 0
#define LOG_TAG    "DEBUG_LOG"

#if WRITABLE
#define LOGD(...)
#define LOGI(...)
#define LOGW(...)
#define LOGE(...)
#else
#define LOGD(...)
#define LOGI(...)
#define LOGW(...)
#define LOGE(...)
#endif

#endif //TEST_IOS_LOG_H

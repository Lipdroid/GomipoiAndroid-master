//
// 一時停止のコールバック一覧クラス
//
#include "../const/parameter.h"

#ifndef TEST_PAUSINGCALLBACK_H
#define TEST_PAUSINGCALLBACK_H


class PausingCallback {

// ------------------------------
// Member
// ------------------------------
public:
    tCallback_isPauseing  mIsPausing;
    tCallback_isSuspending  mIsSuspending;

// ------------------------------
// Accesser
// ------------------------------
public:
    bool isAppPausing();

};


#endif //TEST_PAUSINGCALLBACK_H

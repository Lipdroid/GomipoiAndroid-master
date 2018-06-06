//
// Partsのコールバック一覧クラス
//
#include "../const/parameter.h"
#include "PausingCallback.h"

#ifndef TEST_PARTSCALLBACK_H
#define TEST_PARTSCALLBACK_H

class PartsCallback
{

// ------------------------------
// Member
// ------------------------------
public:
    tCallback_onPartsEvent mOnPartsEvent;
    PausingCallback *mPausing;

};

#endif //TEST_PARTSCALLBACK_H

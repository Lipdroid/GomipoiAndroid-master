//
// AppManagerのコールバック一覧クラス
//
#include "../const/parameter.h"
#include "PartsCallback.h"

#ifndef TEST_APPMANAGERCALLBACK_H
#define TEST_APPMANAGERCALLBACK_H

class AppManagerCallback {

// ------------------------------
// Member
// ------------------------------
public:
    PartsCallback               *mParts;

    tCallback_isLockedEvent     mIsLockedEvent;
    tCallback_isLoading         mIsLoading;
    PausingCallback             *mPausing;
    tCallback_onLoadingEnd      mOnLoadingEnd;
    tCallback_onPartsClicked    mOnPartsClicked;
    tCallback_onPartsEvent      mOnPartsEvent;

// ------------------------------
// Constructor
// ------------------------------
    AppManagerCallback()
    {
        mParts = 0;
        mIsLockedEvent = 0;
        mIsLoading = 0;
        mPausing = 0;
        mOnLoadingEnd = 0;
        mOnPartsClicked = 0;
        mOnPartsEvent = 0;
    }

// ------------------------------
// Destructor
// ------------------------------
    virtual ~AppManagerCallback()
    {
        if (mParts != 0)
        {
            delete mParts;
            mParts = NULL;
        }

        if (mPausing != 0)
        {
            delete  mPausing;
            mPausing = NULL;
        }

        mIsLockedEvent = 0;
        mIsLoading = 0;
        mOnLoadingEnd = 0;
        mOnPartsClicked = 0;
        mOnPartsEvent = 0;
    }

// ------------------------------
// Accesser
// ------------------------------
    PartsCallback *getPartsCallback();

};


#endif //TEST_APPMANAGERCALLBACK_H

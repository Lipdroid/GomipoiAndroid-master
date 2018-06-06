//
// AppManagerのコールバック一覧クラス
//
#include "AppManagerCallback.h"

PartsCallback*
AppManagerCallback::getPartsCallback()
{
    if (mParts != 0)
    {
        return mParts;
    }

    mParts = new PartsCallback();
    mParts->mOnPartsEvent = mOnPartsEvent;
    mParts->mPausing = mPausing;
    return mParts;
}
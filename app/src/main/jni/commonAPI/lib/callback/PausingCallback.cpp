//
// 一時停止のコールバック一覧クラス
//
#include "PausingCallback.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * アプリがPause中かを返す
 */
bool
PausingCallback::isAppPausing()
{
    if (mIsPausing == 0 && mIsSuspending == 0)
    {
        return false;
    }
    else if (mIsPausing != 0 && mIsSuspending != 0)
    {
        return mIsPausing() || mIsSuspending();
    }
    else if (mIsPausing != 0)
    {
        return mIsPausing();
    }
    return mIsSuspending();
}
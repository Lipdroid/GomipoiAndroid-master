//
// なぞるアクション管理クラス
//
#include "TrackingEventManager.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * スクリーンのサイズをセットする
 */
void
TrackingEventManager::setScreenSize(double width, double height)
{
    mScreenWidth = width;
    mScreenHeight = height;
}

/**
 * touchMove時の処理
 */
void
TrackingEventManager::onTouchedMove(float ptX, float ptY)
{
    // 各パーツの当たり判定
    int i;
    for (i = mPartsList.size() - 1; i >= 0; i--)
    {
        if (mPartsList[i] == 0)
        {
            continue;
        }

        // 非表示中 or Trackingしないパーツなら無視
        if (!mPartsList[i]->isVisible() || !mPartsList[i]->isTrackable())
        {
            continue;
        }

        if (mPartsList[i]->isTouched(ptX, ptY, mScreenWidth, mScreenHeight))
        {
            // Tracking中でないパーツをタッチした時の処理
            if (!mPartsList[i]->isTracking())
            {
                mPartsList[i]->onTracking();
            }
        }
        else
        {
            // Tracking中のパーツのTrackingが終了した時の処理
            if (mPartsList[i]->isTracking())
            {
                mPartsList[i]->onTrackingEnd();
            }
        }
    }
}

/**
 * touchUp時の処理
 */
void
TrackingEventManager::onTouchedUp()
{
    int i;
    for (i = mPartsList.size() - 1; i >= 0; i--)
    {
        if (mPartsList[i] == 0)
        {
            continue;
        }

        // 非表示中 or Trackingしないパーツなら無視
        if (!mPartsList[i]->isVisible() || !mPartsList[i]->isTrackable())
        {
            continue;
        }

        // Tracking中のパーツのTrackingが終了した時の処理
        if (mPartsList[i]->isTracking())
        {
            mPartsList[i]->onTrackingEnd();
        }
    }
}
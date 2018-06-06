//
// クリックイベント管理クラス
//
#include "ClickEventManager.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * 画面サイズをセットする
 */
void
ClickEventManager::setScreenSize(double width, double height)
{
    mScreenWidth = width;
    mScreenHeight = height;
}

/**
 * コールバックをセットする
 */
void
ClickEventManager::setClickCallback(tCallback_onPartsClicked callback)
{
    mClickedFunc = callback;
}

/**
 * TouchDown時の処理
 */
bool
ClickEventManager::onTouchedDown(float ptX, float ptY)
{
    mTargetPartsId = 0;

    // 各パーツの当たり判定
    int i;
    for (i = mPartsList.size() - 1; i >= 0; i--)
    {
        if (!mPartsList[i]->isVisible() || !mPartsList[i]->isClickable())
        {
            // 非表示のパーツ or クリックイベントを取得しないパーツは無視
            continue;
        }

        if (mPartsList[i]->isTouched(ptX, ptY, mScreenWidth, mScreenHeight))
        {
            mTargetPartsId = mPartsList[i]->getPartsId();
            break;
        }
    }

    // パーツにタッチダウンしていた場合は、タッチダウン処理を行う
    if (mTargetPartsId > 0)
    {
        getParts(mTargetPartsId)->onPressed();
        return true;
    }

    return false;
}

/**
 * touchMove時の処理
 */
bool
ClickEventManager::onTouchedMove(float ptX, float ptY)
{
    if (mTargetPartsId == 0)
    {
        return false;
    }

    if (!(getParts(mTargetPartsId)->isTouched(ptX, ptY, mScreenWidth, mScreenHeight)))
    {
        // 範囲外に外れた場合の処理
        getParts(mTargetPartsId)->onPressedEnd();
        mTargetPartsId = 0;
        return true;
    }

    return true;
}

/**
 * touchUp時の処理
 */
bool
ClickEventManager::onTouchedUp()
{
    if (mTargetPartsId == 0)
    {
        return false;
    }

    // クリック処理
    getParts(mTargetPartsId)->onPressedEnd();
    if (mClickedFunc != 0)
    {
        mClickedFunc(mTargetPartsId);
    }

    mTargetPartsId = 0;
    return true;
}


// ------------------------------
// Function
// ------------------------------
/**
 * PartsIDからPartsを取得する
 */
PartsBase *
ClickEventManager::getParts(int id)
{
    int i;
    for (i = 0; i < mPartsList.size(); i++)
    {
        if (mPartsList[i]->getPartsId() == id)
        {
            return mPartsList[i];
        }
    }
    return 0;
}
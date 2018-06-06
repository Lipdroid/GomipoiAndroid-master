//
//  SwipeItemData.cpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/07.
//
//

#include "SwipeItemData.h"

// ------------------------------
// Accessor
// ------------------------------
/**
 * 残りのライフを返す
 */
int
SwipeItemData::getRestLife()
{
    return mRestLife;
}

/**
 * 残りのライフをセットする
 */
void
SwipeItemData::setRestLife(int restLife)
{
    mRestLife = restLife;
}

/**
 * 初期左位置を返す
 */
double
SwipeItemData::getInitLeft()
{
    return mInitLeft;
}

/**
 * 初期上位置を返す
 */
double
SwipeItemData::getInitTop()
{
    return mInitTop;
}

/**
 * Swipeされるアイテムかどうかを返す
 */
bool
SwipeItemData::isSwipeTarget()
{
    return mIsSwipeTarget;
}

/**
 * Swipeされるアイテムかどうかをセットする
 */
void
SwipeItemData::setSwipeTarget(bool isSwipeTarget)
{
    mIsSwipeTarget = isSwipeTarget;
}

/**
 * isFirstを返す
 */
bool
SwipeItemData::isFirst()
{
    return mIsFirst;
}

/**
 * 現在の表示Leftを返す
 */
double
SwipeItemData::getCurrentLeft()
{
    int maxLife = getMaxLife();
    double canX = 286.5 - getWidth() / 2.0;
    return mInitLeft + ((canX - mInitLeft) * (double)(maxLife - mRestLife) / (double)maxLife);
}

/**
 * 現在の表示Topを返す
 */
double
SwipeItemData::getCurrentTop()
{
    int maxLife = getMaxLife();
    double canY = 138.5 - getHeight() / 2.0;
    return mInitTop + ((canY - mInitTop) * (double)(maxLife - mRestLife) / (double)maxLife);
}

/**
 * ライフの最大値を返す
 */
int
SwipeItemData::getMaxLife()
{
    return 1;
}

/**
 * ボーナスを返す
 */
int
SwipeItemData::getBonus()
{
    return 1;
}

/**
 * 幅を返す
 */
double
SwipeItemData::getWidth()
{
    return 0.0;
}

/**
 * 高さを返す
 */
double
SwipeItemData::getHeight()
{
    return 0.0;
}

/**
 * ゴミが表示完了状態(残りライフが0)になっているかを返す
 */
bool
SwipeItemData::isCompleted()
{
    return (mRestLife <= 0);
}

/**
 * Sweepの対象かをチェックする
 */
void
SwipeItemData::checkHit(double ptX, double ptY, double screenWidth, double screenHeight)
{
    double middleX = getCurrentLeft() + getWidth() / 2.0;
    double middleY = getCurrentTop() + getHeight() / 2.0;
    double dist = sqrt(pow(ptX - middleX, 2.0) + pow(ptY - middleY, 2.0));
    mIsSwipeTarget = (dist <= RATIO(screenWidth, screenHeight) * SWIPE_TARGET_DIST);
}

/**
 * Sweep時の処理を行う
 */
void
SwipeItemData::onSweep(int broomType, double startTouchX, double startTouchY, double screenWidth, double screenHeight)
{
    checkHit(startTouchX, startTouchY, screenWidth, screenHeight);
    if (!mIsSwipeTarget)
    {
        return;
    }
    
    int addValue = -1;
    switch (broomType)
    {
        case eBroomType_Gold:
        {
            addValue = -5;
            break;
        }
            
        case eBroomType_Silver:
        {
            addValue = -2;
            break;
        }
    }
    
    // 残りライフを減らす
    mRestLife += addValue;
    
    if (mRestLife < 0)
    {
        mRestLife = 0;
    }
}

//
//
//
#include "HolePartsBase.h"

// ------------------------------
// Override
// ------------------------------
void
HolePartsBase::initParts(PartsCallback *callback)
{
    PartsBase::initParts(callback);
}

void
HolePartsBase::onPressed()
{
    
}

void
HolePartsBase::onPressedEnd()
{
    
}

// ------------------------------
// Accesser
// ------------------------------
/**
 * 穴が表示されているか否か。アニメーション開始のため
 */
bool
HolePartsBase::hasAppeared()
{
    return mHasAppeared;
}

/**
 * 穴が表示状態をリセットする。アニメーション開始のため
 */
void
HolePartsBase::resetAppeared()
{
    mHasAppeared = false;
}

/**
 * タッチの対象になるかを判定する
 */
void
HolePartsBase::checkHitTouch(double ptX, double ptY, double screenWidth, double screenHeight)
{
}

void
HolePartsBase::startAppearAnimation(tCallback_onCompletedAnim onCompleted)
{
    mHasAppeared = true;
    mOnCompletedStartAnimation = onCompleted;
    
    startAnimation(eAnimId_Hole_ScaleSet,
                   [&](int animationId)
                   {
                       if (mOnCompletedStartAnimation != NULL) {
                           mOnCompletedStartAnimation(animationId);
                           mOnCompletedStartAnimation = NULL;
                       }
                   });
}
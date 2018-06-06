//
//
//
#include "ScaleAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
ScaleAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    if (mMaxAnimationTime == 0 || !mIsAnimating)
    {
        // isKeepAfterScaleの場合は、ここで処理
        if (mIsOncePlay && mAfterOffsetTime < 0)
        {
            // 反映
            vertexts[0] *= mToX;
            vertexts[1] *= mToY;
            vertexts[3] *= mToX;
            vertexts[4] *= mToY;
            vertexts[6] *= mToX;
            vertexts[7] *= mToY;
            vertexts[9] *= mToX;
            vertexts[10] *= mToY;
        }
        return;
    }

    mIsOncePlay = true;

    //
    // パラメーターを設定
    // ------------------------------
    float currentScaleX;
    float currentScaleY;

    //
    // 前OffsetTime時
    // ------------------------------
    if (getCurrentAnimationTime() < 0)
    {
        currentScaleX = mFromX;
        currentScaleY = mFromY;
    }

    //
    // 後OffsetTime時
    // ------------------------------
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        currentScaleX = mToX;
        currentScaleY = mToY;
    }

    //
    // アニメーション時
    // ------------------------------
    else
    {
        double interpolator = makeStraightInterpolator();
        currentScaleX = mFromX + (mToX - mFromX) * interpolator;
        currentScaleY = mFromY + (mToY - mFromY) * interpolator;
    }

    // 反映
    vertexts[0] *= currentScaleX;
    vertexts[1] *= currentScaleY;
    vertexts[3] *= currentScaleX;
    vertexts[4] *= currentScaleY;
    vertexts[6] *= currentScaleX;
    vertexts[7] *= currentScaleY;
    vertexts[9] *= currentScaleX;
    vertexts[10] *= currentScaleY;
}


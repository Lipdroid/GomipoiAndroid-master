//
//
//
#include "FadeAnimation.h"

// ------------------------------
// Override
// ------------------------------
double
FadeAnimation::getCurrentAlpha()
{
    if (mMaxAnimationTime == 0 || !mIsAnimating)
    {
        // isKeepAfterScaleの場合は、ここで処理
        if (mIsOncePlay && mAfterOffsetTime < 0)
        {
            return mTo;
        }

        return mFrom;
    }

    mIsOncePlay = true;

    //
    // BeforeOffset時
    // ------------------------------
    if (getCurrentAnimationTime() < 0)
    {
        return mFrom;
    }

    //
    // AfterOffset時
    // ------------------------------
    if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        return mTo;
    }

    //
    // アニメーション時
    // ------------------------------
    double interpolator = makeStraightInterpolator();
    double value = mFrom + ((mTo - mFrom) * interpolator);
    if (value < 0)
    {
        value += 1.0;
    }

    return value;
}
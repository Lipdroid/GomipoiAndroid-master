//
//
//
#include "TranslateAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
TranslateAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    if (mMaxAnimationTime == 0 || !mIsAnimating)
    {
        // isKeepAfterScaleの場合は、ここで処理
        if (mIsOncePlay && mAfterOffsetTime < 0)
        {
            double ratio = RATIO(screenWidth, screenHeight);

            double toX = mToX * 2.0;
            double toY = mToY * 2.0;

            double offsetX = toX * ratio / screenWidth;
            double offsetY = toY * ratio / screenHeight;

            // 反映
            vertexts[0] += offsetX;
            vertexts[1] += offsetY;
            vertexts[3] += offsetX;
            vertexts[4] += offsetY;
            vertexts[6] += offsetX;
            vertexts[7] += offsetY;
            vertexts[9] += offsetX;
            vertexts[10] += offsetY;
            return;
        }

        return;
    }

    mIsOncePlay = true;

    double ratio = RATIO(screenWidth, screenHeight);

    // 座標変換の帳尻合わせ
    double fromX = mFromX * 2.0;
    double toX = mToX * 2.0;
    double fromY = mFromY * 2.0;
    double toY = mToY * 2.0;

    double offsetX;
    double offsetY;

    //
    // 前OffsetTime時
    // ------------------------------
    if (getCurrentAnimationTime() < 0)
    {
        offsetX = fromX * ratio / screenWidth;
        offsetY = fromY * ratio / screenHeight;
    }

    //
    // 後OffsetTime時
    // ------------------------------
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        offsetX = toX * ratio / screenWidth;
        offsetY = toY * ratio / screenHeight;
    }

    //
    // アニメーション時
    // ------------------------------
    else
    {
        double interpolator = 0.0;
        if (mInterpolatorType == eInterpolator_Curve)
        {
            interpolator = makeCurveInterpolator();
        }
        else
        {
            interpolator = makeStraightInterpolator();
        }
        offsetX = (fromX + (toX - fromX) * interpolator) * ratio / screenWidth;
        offsetY = (fromY + (toY - fromY) * interpolator) * ratio / screenHeight;
    }

    // 反映
    vertexts[0] += offsetX;
    vertexts[1] += offsetY;
    vertexts[3] += offsetX;
    vertexts[4] += offsetY;
    vertexts[6] += offsetX;
    vertexts[7] += offsetY;
    vertexts[9] += offsetX;
    vertexts[10] += offsetY;
}

// ------------------------------
// Accesser
// ------------------------------
void
TranslateAnimation::setPosition(double fromX, double fromY, double toX, double toY)
{
    mFromX = fromX;
    mFromY = fromY;
    mToX = toX;
    mToY = toY;
}
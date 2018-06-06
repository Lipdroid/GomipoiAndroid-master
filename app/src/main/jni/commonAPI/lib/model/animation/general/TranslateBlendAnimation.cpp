//
//  TranslationBlendAnimation.c
//  Gomibako
//
//  Created by Tan Herve on 2016/09/01.
//
//

#include "TranslateBlendAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
TranslateBlendAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    if (mMaxAnimationTime == 0 || !mIsAnimating)
    {
        // isKeepAfterScaleの場合は、ここで処理
        double ratio = RATIO(screenWidth, screenHeight);
        
        if (mIsOncePlay && mAfterOffsetTime < 0)
        {
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
            
            mOffsetX = offsetX - mOriginalOffsetX * ratio / screenWidth;
            mOffsetY = offsetY - mOriginalOffsetY * ratio / screenHeight;
            return;
        }
        else {
            mOffsetX = - mOriginalOffsetX * ratio / screenWidth;
            mOffsetY = - mOriginalOffsetY * ratio / screenHeight;
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
    
    mOffsetX = offsetX - mOriginalOffsetX * ratio / screenWidth;
    mOffsetY = offsetY - mOriginalOffsetY * ratio / screenHeight;
}

PartsBase*
TranslateBlendAnimation::getBlendParts()
{
    return mBlendParts;
}

GLuint
TranslateBlendAnimation::getBlendTextureId()
{
    if (mBlendParts == NULL)
        return 0;
    else
    {
        return mBlendParts->getTextureId(0);
    }
}

GLfloat
TranslateBlendAnimation::getBlendOffsetX()
{
    if (mBlendParts == NULL)
        return 0.0;
    else
    {
        return mOffsetX;
    }
}

/**
 * ブレンド用のyオフセット. デフォルトでブレンドしないので、0を返す
 */
GLfloat
TranslateBlendAnimation::getBlendOffsetY()
{
    if (mBlendParts == NULL)
        return 0.0;
    else
    {
        return mOffsetY;
    }
}

// ------------------------------
// Accesser
// ------------------------------
void
TranslateBlendAnimation::setPosition(double fromX, double fromY, double toX, double toY)
{
    mFromX = fromX;
    mFromY = fromY;
    mToX = toX;
    mToY = toY;
}

void
TranslateBlendAnimation::setOriginalOffset(double offsetX, double offsetY)
{
    mOriginalOffsetX = offsetX;
    mOriginalOffsetY = offsetY;
}

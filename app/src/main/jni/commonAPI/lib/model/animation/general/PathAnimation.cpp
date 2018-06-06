//
//  PathAnimation.cpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/15.
//
//

#include "PathAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
PathAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    if (mMaxAnimationTime == 0 || !mIsAnimating)
    {
        // isKeepAfterScaleの場合は、ここで処理
        if (mIsOncePlay && mAfterOffsetTime < 0)
        {
            double ratio = RATIO(screenWidth, screenHeight);
            
            double toX = mPath[mDataLength - 1]->x * 2.0;
            double toY = mPath[mDataLength - 1]->y * 2.0;
            
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
    
    //interpolatorを取得
    double interpolator = 0.0;
    
    //
    // 前OffsetTime時
    // ------------------------------
    if (getCurrentAnimationTime() < 0)
    {
        interpolator = 0.0;
    }
    
    //
    // 後OffsetTime時
    // ------------------------------
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        interpolator = 1.0;
    }
    
    //
    // アニメーション時
    // ------------------------------
    else
    {
        interpolator = makeStraightInterpolator();
    }
    
    PathData* previousData = mPath[0];
    PathData* nextData = mPath[mDataLength - 1];
    
    double totalTime = mPath[mDataLength - 1]->time;
    int i;
    for (i = 0; i < mDataLength; i++)
    {
        if ((mPath[i]->time / totalTime) >= interpolator)
        {
            nextData = mPath[i];
            break;
        }
        else
        {
            previousData = mPath[i];
        }
    }
    
    double intervalInterpolator = interpolator - (previousData->time / totalTime);
    double interval = (nextData->time / totalTime) - (previousData->time / totalTime);
    
    if (interval == 0)
    {
        interpolator = 1.0;
    }
    else
    {
        interpolator = intervalInterpolator / interval;
    }
    
    // 座標変換の帳尻合わせ
    double fromX = previousData->x * 2.0;
    double toX = nextData->x * 2.0;
    double fromY = - previousData->y * 2.0;
    double toY = - nextData->y * 2.0;
    
    double offsetX = (fromX + (toX - fromX) * interpolator) * ratio / screenWidth;
    double offsetY = (fromY + (toY - fromY) * interpolator) * ratio / screenHeight;
    
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

double
PathAnimation::getCurrentLeftOffset()
{
    //interpolatorを取得
    double interpolator = 0.0;
    if (getCurrentAnimationTime() < 0)
    {
        interpolator = 0.0;
    }
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        interpolator = 1.0;
    }
    else
    {
        interpolator = makeStraightInterpolator();
    }

    PathData* previousData = mPath[0];
    PathData* nextData = mPath[mDataLength - 1];
    
    double totalTime = mPath[mDataLength - 1]->time;
    int i;
    for (i = 0; i < mDataLength; i++)
    {
        if ((mPath[i]->time / totalTime) >= interpolator)
        {
            nextData = mPath[i];
            break;
        }
        else
        {
            previousData = mPath[i];
        }
    }
    
    double intervalInterpolator = interpolator - (previousData->time / totalTime);
    double interval = (nextData->time / totalTime) - (previousData->time / totalTime);
    
    if (interval == 0)
    {
        interpolator = 1.0;
    }
    else
    {
        interpolator = intervalInterpolator / interval;
    }
    
    return previousData->x + (nextData->x - previousData->x) * interpolator;
}

double
PathAnimation::getCurrentTopOffset()
{
    //interpolatorを取得
    double interpolator = 0.0;
    if (getCurrentAnimationTime() < 0)
    {
        interpolator = 0.0;
    }
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        interpolator = 1.0;
    }
    else
    {
        interpolator = makeStraightInterpolator();
    }
    
    PathData* previousData = mPath[0];
    PathData* nextData = mPath[mDataLength - 1];
    
    double totalTime = mPath[mDataLength - 1]->time;
    int i;
    for (i = 0; i < mDataLength; i++)
    {
        if ((mPath[i]->time / totalTime) >= interpolator)
        {
            nextData = mPath[i];
            break;
        }
        else
        {
            previousData = mPath[i];
        }
    }
    
    double intervalInterpolator = interpolator - (previousData->time / totalTime);
    double interval = (nextData->time / totalTime) - (previousData->time / totalTime);
    
    if (interval == 0)
    {
        interpolator = 1.0;
    }
    else
    {
        interpolator = intervalInterpolator / interval;
    }
    
    return previousData->y + (nextData->y - previousData->y) * interpolator;
}

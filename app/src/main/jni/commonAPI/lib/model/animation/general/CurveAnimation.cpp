//
//
//
#include "CurveAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
CurveAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    double interpolator = 0.0;
    
    if (mMaxAnimationTime == 0 || !mIsAnimating)
    {
        // isKeepAfterScaleの場合は、ここで処理
        if (mIsOncePlay && mAfterOffsetTime < 0)
        {
            interpolator = 1.0;
        }
        else return;
    }

    mIsOncePlay = true;
    
    if (interpolator == 0.0)
    {
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
            if (mInterpolatorType == eInterpolator_Curve)
            {
                interpolator = makeCurveInterpolator();
            }
            else
            {
                interpolator = makeStraightInterpolator();
            }
        }
    }
    
    double currentAngle = mStartDegree + (mEndDegree - mStartDegree) * interpolator;
    currentAngle *= M_PI / 180.0; //radianに変換
    
    //軌道の角度
    double orbitCos = cos(currentAngle);
    double orbitSin = sin(currentAngle);
    
    //回転の角度
    double rotateAngle = currentAngle + (mInitialRotation - mStartDegree) * M_PI / 180.0;
    double rotateCos = cos(rotateAngle);
    double rotateSin = sin(rotateAngle);
    
    //回転処理
    vertexts[0] *= screenWidth;
    vertexts[3] *= screenWidth;
    vertexts[6] *= screenWidth;
    vertexts[9] *= screenWidth;
    vertexts[1] *= screenHeight;
    vertexts[4] *= screenHeight;
    vertexts[7] *= screenHeight;
    vertexts[10] *= screenHeight;
    
    int i;
    double x, y;
    for (i = 0; i < 4; i++)
    {
        x = vertexts[3 * i];
        y = vertexts[3 * i + 1];
        vertexts[3 * i]         = (x * rotateCos - y * rotateSin) / screenWidth;
        vertexts[3 * i + 1]     = (x * rotateSin + y * rotateCos) / screenHeight;
    }
    
    //移動処理
    double distance = sqrt((mStartX - mCenterX) * (mStartX - mCenterX) + (mStartY - mCenterY) * (mStartY - mCenterY));
    double currentX = mCenterX + distance * orbitCos;
    double currentY = mCenterY + distance * orbitSin;
    
    double ratio = RATIO(screenWidth, screenHeight);
    
    double offsetX = currentX - mStartX;
    double offsetY = currentY - mStartY;
    
    offsetX *= 2.0 * ratio / screenWidth;
    offsetY *= 2.0 * ratio / screenHeight;
    
    vertexts[0] += offsetX;
    vertexts[1] += offsetY;
    vertexts[3] += offsetX;
    vertexts[4] += offsetY;
    vertexts[6] += offsetX;
    vertexts[7] += offsetY;
    vertexts[9] += offsetX;
    vertexts[10] += offsetY;
}

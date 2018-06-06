//
//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_CURVEANIMATION_H
#define JANPOI_CURVEANIMATION_H

/**
 * 曲線のアニメーション
 */
class CurveAnimation : public AnimationBase {
    
// ------------------------------
// Member
// ------------------------------
private:
    double mStartX;             //パーツの初期X座標
    double mStartY;             //パーツの初期Y座標
    double mCenterX;            //曲線の軌道のセンターのX座標
    double mCenterY;            //曲線の軌道のセンターのY座標
    double mStartDegree;        //軌道の初期角度
    double mEndDegree;          //軌道の最終角度
    double mInitialRotation;    //パーツの初期回転

// ------------------------------
// Constructor
// ------------------------------
public:
    CurveAnimation(int animationId, double startX, double startY, double centerX, double centerY, double startDegree, double endDegree, double initialRotation, double duration) : AnimationBase(animationId)
    {
        mStartX = startX;
        mStartY = startY;
        mCenterX = centerX;
        mCenterY = centerY;
        mStartDegree = startDegree;
        mEndDegree = endDegree;
        mInitialRotation = initialRotation;
        mMaxAnimationTime = duration;
        mInterpolatorType = eInterpolator_Curve;
    }

    virtual ~CurveAnimation()
    {
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);

};


#endif //JANPOI_CURVEANIMATION_H

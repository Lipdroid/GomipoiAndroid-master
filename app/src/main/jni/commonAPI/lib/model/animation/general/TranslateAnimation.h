//
//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_TRANSLATEANIMATION_H
#define JANPOI_TRANSLATEANIMATION_H


class TranslateAnimation : public AnimationBase {

// ------------------------------
// Member
// ------------------------------
private:
    double mFromX;
    double mToX;
    double mFromY;
    double mToY;

// ------------------------------
// Constructor
// ------------------------------
public:
    TranslateAnimation(int animationId, double fromX, double fromY, double toX, double toY, double duration) : AnimationBase(animationId)
    {
        mFromX = fromX;
        mFromY = fromY;
        mToX = toX;
        mToY = toY;
        mMaxAnimationTime = duration;
        mInterpolatorType = eInterpolator_Curve;
    }

    virtual ~TranslateAnimation()
    {
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);

// ------------------------------
// Accesser
// ------------------------------
public:
    void setPosition(double fromX, double fromY, double toX, double toY);

};


#endif //JANPOI_TRANSLATEANIMATION_H

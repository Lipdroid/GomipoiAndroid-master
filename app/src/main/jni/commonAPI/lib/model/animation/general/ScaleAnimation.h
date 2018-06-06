//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_SCALEANIMATION_H
#define JANPOI_SCALEANIMATION_H


class ScaleAnimation : public AnimationBase {

// ------------------------------
// Member
// ------------------------------
private:
    double mFromX;
    double mFromY;
    double mToX;
    double mToY;

// ------------------------------
// Constructor
// ------------------------------
public:
    ScaleAnimation(int animationId, double from, double to,
                   double duration) : AnimationBase(animationId)
    {
        mFromX = from;
        mFromY = from;
        mToX = to;
        mToY = to;
        mMaxAnimationTime = duration;
    }

    ScaleAnimation(int animationId, double fromX, double fromY,
                   double toX, double toY, double duration) : AnimationBase(animationId)
    {
        mFromX = fromX;
        mFromY = fromY;
        mToX = toX;
        mToY = toY;
        mMaxAnimationTime = duration;
    }

    virtual ~ScaleAnimation()
    {
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);

};


#endif //JANPOI_SCALEANIMATION_H

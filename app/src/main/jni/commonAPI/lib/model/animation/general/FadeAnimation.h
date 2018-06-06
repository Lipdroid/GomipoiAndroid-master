//
//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_FADEANIMATION_H
#define JANPOI_FADEANIMATION_H


class FadeAnimation : public AnimationBase {

// ------------------------------
// Member
// ------------------------------
private:
    double mFrom;
    double mTo;

// ------------------------------
// Constructor / Destructor
// ------------------------------
public:
    FadeAnimation(int animationId, double from, double to, double duration) : AnimationBase(animationId)
    {
        mFrom = from;
        mTo = to;
        mMaxAnimationTime = duration;
    }


    virtual ~FadeAnimation()
    {
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual double getCurrentAlpha();

};


#endif //JANPOI_FADEANIMATION_H

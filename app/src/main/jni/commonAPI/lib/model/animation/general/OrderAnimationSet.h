//
//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_ORDERANIMATIONSET_H
#define JANPOI_ORDERANIMATIONSET_H


class OrderAnimationSet : public AnimationBase {

// ------------------------------
// Member
// ------------------------------
private:
    std::vector<AnimationBase *> mAnimationList;
    int mCurrentAnimationIndex;
    tCallback_onCompletedAnim mOnCompletedAnim;

// ------------------------------
// Constructor
// ------------------------------
public:
    OrderAnimationSet(int animationId) : AnimationBase(animationId)
    {
    }

    virtual ~OrderAnimationSet()
    {
        for (AnimationBase *animation : mAnimationList)
        {
            if (animation != 0)
            {
                delete animation;
                animation = 0;
            }
        }
        mAnimationList.clear();
        mOnCompletedAnim = 0;
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual void start(tCallback_onCompletedAnim onCompletedAnimation);
    virtual void stop();
    virtual void pause();
    virtual void pauseEnd();
    virtual void checkState();
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);
    virtual double getCurrentAlpha();
    virtual void setIsPausingCallback(PausingCallback *callback);
    virtual double getTotalTime();
    
    virtual double getCurrentLeftOffset();
    virtual double getCurrentTopOffset();

// ------------------------------
// Accesser
// ------------------------------
public:
    void addAnimation(AnimationBase *animation);

// ------------------------------
// Function
// ------------------------------
private:
    AnimationBase *getCurrentAnimation();

};


#endif //JANPOI_ORDERANIMATIONSET_H


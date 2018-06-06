//
//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_ANIMATIONSET_H
#define JANPOI_ANIMATIONSET_H


class MultipleAnimationSet : public AnimationBase {

// ------------------------------
// Member
// ------------------------------
private:
    std::vector<AnimationBase *> mAnimationList;

// ------------------------------
// Constructor
// ------------------------------
public:
    MultipleAnimationSet(int animationId) : AnimationBase(animationId)
    {
    }

    virtual ~MultipleAnimationSet()
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
    virtual double getCurrentLeftOffset();
    virtual double getCurrentTopOffset();

// ------------------------------
// Accesser
// ------------------------------
public:
    void addAnimation(AnimationBase *animation);
    AnimationBase * getAnimation(int animationId);



};


#endif //JANPOI_ANIMATIONSET_H
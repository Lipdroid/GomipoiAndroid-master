//
//
//
#include "../../../lib/model/animation/AnimationBase.h"
#include "../../parameter/app_parameter.h"

#include "../../../lib/model/animation/general/OrderAnimationSet.h"
#include "../../../lib/model/animation/general/TranslateAnimation.h"
#include "../../../lib/model/animation/general/FrameAnimation.h"

#ifndef GOMIBAKO_SVN_POIKOSWEEPANIMATION_H
#define GOMIBAKO_SVN_POIKOSWEEPANIMATION_H


class PoikoSweepAnimation : public AnimationBase
{
// ------------------------------
// Define
// ------------------------------
private:

// ------------------------------
// Member
// ------------------------------
private:

    OrderAnimationSet *mOrderAnimationSet;
    FrameAnimation *mFrameAnimation;

    tCallback_onChangeTexture  mOnChangeTexture;

// ------------------------------
// Constructor
// ------------------------------
public:
    PoikoSweepAnimation(tCallback_onChangeTexture onChangeTexture) : AnimationBase(eAnimId_Poiko_Sweep_Set)
    {
        mOnChangeTexture = onChangeTexture;

        std::vector<int> list;
        list.push_back(ePoikoTexture_Sweep1);
        list.push_back(ePoikoTexture_Sweep2);
        list.push_back(ePoikoTexture_Sweep3);

        TranslateAnimation *translateAnimation;

        mOrderAnimationSet = new OrderAnimationSet(0);
        translateAnimation = new TranslateAnimation(eAnimId_Poiko_Sweep_Translate1, 0, 0, -10, 0, 100);
        mOrderAnimationSet->addAnimation(translateAnimation);
        translateAnimation = new TranslateAnimation(eAnimId_Poiko_Sweep_Translate2, -10, 0, 10, 0, 200);
        mOrderAnimationSet->addAnimation(translateAnimation);
        translateAnimation = new TranslateAnimation(eAnimId_Poiko_Sweep_Translate3, 10, 0, -10, 0, 200);
        mOrderAnimationSet->addAnimation(translateAnimation);
        translateAnimation = new TranslateAnimation(eAnimId_Poiko_Sweep_Translate2, -10, 0, 10, 0, 200);
        mOrderAnimationSet->addAnimation(translateAnimation);
        translateAnimation = new TranslateAnimation(eAnimId_Poiko_Sweep_Translate3, 10, 0, 0, 0, 100);
        mOrderAnimationSet->addAnimation(translateAnimation);

        mFrameAnimation = new FrameAnimation(eAnimId_Poiko_Sweep_Frame, list, 50);
        mFrameAnimation->setLooping(true);

//        setLooping(true);
    }

// ------------------------------
// Destructor
// ------------------------------
    virtual ~PoikoSweepAnimation()
    {
        if (mOrderAnimationSet != 0)
        {
            delete mOrderAnimationSet;
            mOrderAnimationSet = 0;
        }

        if (mFrameAnimation != 0)
        {
            delete mFrameAnimation;
            mFrameAnimation = 0;
        }
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

};


#endif //GOMIBAKO_SVN_POIKOSWEEPANIMATION_H

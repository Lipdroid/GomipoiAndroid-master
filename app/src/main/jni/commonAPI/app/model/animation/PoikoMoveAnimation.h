//
//
//
#include "../../../lib/model/animation/AnimationBase.h"
#include "../../parameter/app_parameter.h"

#include "../../../lib/model/animation/general/TranslateAnimation.h"
#include "../../../lib/model/animation/general/FrameAnimation.h"

#ifndef GOMIBAKO_SVN_POIKOMOVEANIMATION_H
#define GOMIBAKO_SVN_POIKOMOVEANIMATION_H

class PoikoMoveAnimation : public AnimationBase
{
// ------------------------------
// Define
// ------------------------------
private:

// ------------------------------
// Member
// ------------------------------
private:
    TranslateAnimation *mTranslateAnimation;
    FrameAnimation *mFrameAnimation;

    tCallback_onChangeTexture  mOnChangeTexture;

// ------------------------------
// Constructor
// ------------------------------
public:
    PoikoMoveAnimation(int animationId, tCallback_onChangeTexture onChangeTexture) : AnimationBase(animationId)
    {
        mOnChangeTexture = onChangeTexture;

        std::vector<int> list;
        list.push_back(ePoikoTexture_Move1);
        list.push_back(ePoikoTexture_Move2);

        mTranslateAnimation = new TranslateAnimation(eAnimId_Poiko_Move_Translate, 0, 0, 0, 0, 500);
        mFrameAnimation = new FrameAnimation(eAnimId_Poiko_Move_Frame, list, 50);
        mFrameAnimation->setLooping(true);
    }

// ------------------------------
// Destructor
// ------------------------------
    virtual ~PoikoMoveAnimation()
    {
        if (mTranslateAnimation != 0)
        {
            delete mTranslateAnimation;
            mTranslateAnimation = 0;
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

// ------------------------------
// Accesser
// ------------------------------
public:
    void setNewOffset(double left, double top);

// ------------------------------
// Function
// ------------------------------
private:

};




#endif //GOMIBAKO_SVN_POIKOMOVEANIMATION_H

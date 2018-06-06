//
//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_FRAMEANIMATION_H
#define JANPOI_FRAMEANIMATION_H


class FrameAnimation : public AnimationBase {

// ------------------------------
// Member
// ------------------------------
private:
    double mTmpBeforeTime;
    double mTmpAfterTime;
    int mCurrentTexture;
    std::vector <int> mTextureList;
    tCallback_onChangeTexture mOnChangeTexture;

// ------------------------------
// Constructor
// ------------------------------
public:
    FrameAnimation(int animationId, std::vector <int> textureList, double duration) : AnimationBase(animationId) {
        mTmpBeforeTime = 0.0;
        mTmpAfterTime = 0.0;
        mCurrentTexture = 0;
        mTextureList = textureList;
        mMaxAnimationTime = duration;
        mOnChangeTexture = NULL;
    }

    virtual ~FrameAnimation()
    {
    }

// ------------------------------
// Override
// ------------------------------
    virtual void start(tCallback_onCompletedAnim onCompletedAnimation);
    virtual void checkState();
    virtual void setBeforeOffsetTime(double offsetTime);
    virtual void setAffterOffsetTime(double offsetTime);

// ------------------------------
// Accesser
// ------------------------------
    void start(tCallback_onCompletedAnim onCompletedAnimation, tCallback_onChangeTexture onChangeTexture);
    void setOnChangeTextureCallback(tCallback_onChangeTexture callback);

// ------------------------------
// Function
// ------------------------------
private:
    void startFrameAnim(tCallback_onCompletedAnim onCompletedAnimation);

};

#endif //JANPOI_FRAMEANIMATION_H



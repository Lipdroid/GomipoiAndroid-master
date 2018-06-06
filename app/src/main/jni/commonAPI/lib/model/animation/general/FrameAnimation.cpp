//
//
//
#include "FrameAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
FrameAnimation::start(tCallback_onCompletedAnim onCompletedAnimation)
{
    start(onCompletedAnimation, mOnChangeTexture);
}

void
FrameAnimation::checkState()
{
    if (!mIsAnimating)
    {
        return;
    }

    if (getCurrentAnimationTime() - mAfterOffsetTime > mMaxAnimationTime)
    {
        mCurrentTexture += 1;
        if (mCurrentTexture < 0 || mCurrentTexture >= mTextureList.size())
        {
            if (mIsLoopingPlay)
            {
                // ループアニメーション
                start(mCompletedFunc, mOnChangeTexture);
                return;
            }

            stop();

            if (mCompletedFunc != 0)
            {
                // アニメーションの終了通知
                mCompletedFunc(mAnimationId);
            }

            return;
        }

        startFrameAnim(mCompletedFunc);

    }
}

void
FrameAnimation::setBeforeOffsetTime(double offsetTime)
{
    mTmpBeforeTime = offsetTime;
}

void
FrameAnimation::setAffterOffsetTime(double offsetTime)
{
    mTmpAfterTime = offsetTime;
}

// ------------------------------
// Accesser
// ------------------------------
void
FrameAnimation::start(tCallback_onCompletedAnim onCompletedAnimation, tCallback_onChangeTexture onChangeTexture)
{
    mCurrentTexture = 0;
    mOnChangeTexture = onChangeTexture;
    startFrameAnim(onCompletedAnimation);
}

// ------------------------------
// Function
// ------------------------------
void
FrameAnimation::setOnChangeTextureCallback(tCallback_onChangeTexture callback)
{
    mOnChangeTexture = callback;
}

void
FrameAnimation::startFrameAnim(tCallback_onCompletedAnim onCompletedAnimation)
{
    mBeforeOffsetTime = 0.0;
    mAfterOffsetTime = 0.0;

    if (mCurrentTexture == 0)
    {
        mBeforeOffsetTime = mTmpBeforeTime;
    }

    if (mCurrentTexture == mTextureList.size() - 1)
    {
        mAfterOffsetTime = mTmpAfterTime;
    }

    if (mCurrentTexture < 0 || mCurrentTexture >= mTextureList.size())
    {
        if (onCompletedAnimation != 0)
        {
            onCompletedAnimation(getAnimationId());
        }
        return;
    }

    if (mOnChangeTexture != 0)
    {
        mOnChangeTexture(mTextureList[mCurrentTexture]);
    }

    AnimationBase::start(onCompletedAnimation);
}
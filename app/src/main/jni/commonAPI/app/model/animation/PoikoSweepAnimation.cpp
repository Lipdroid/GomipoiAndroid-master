//
//
//
#include "PoikoSweepAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
PoikoSweepAnimation::start(tCallback_onCompletedAnim onCompletedAnimation)
{
    if (mOrderAnimationSet != 0)
    {
        mMaxAnimationTime = mOrderAnimationSet->getTotalTime();
    }

    AnimationBase::start(onCompletedAnimation);

    // Start処理
    if (mOrderAnimationSet != 0)
    {
        mOrderAnimationSet->start(0);
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->start(
                0,
                mOnChangeTexture);
    }
}

void
PoikoSweepAnimation::stop()
{
    if (mOrderAnimationSet != 0)
    {
        mOrderAnimationSet->stop();
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->stop();
    }

    AnimationBase::stop();
}

void
PoikoSweepAnimation::pause()
{
    if (mOrderAnimationSet != 0)
    {
        mOrderAnimationSet->pause();
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->pause();
    }

    AnimationBase::pause();
}

void
PoikoSweepAnimation::pauseEnd()
{
    if (mOrderAnimationSet != 0)
    {
        mOrderAnimationSet->pauseEnd();
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->pauseEnd();
    }

    AnimationBase::pauseEnd();
}

void
PoikoSweepAnimation::checkState()
{
    if (mOrderAnimationSet != 0)
    {
        mOrderAnimationSet->checkState();
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->checkState();
    }

    AnimationBase::checkState();
}

void
PoikoSweepAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    if (mOrderAnimationSet != 0)
    {
        mOrderAnimationSet->flushAnimation(screenWidth, screenHeight, vertexts);
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->flushAnimation(screenWidth, screenHeight, vertexts);
    }
}

double
PoikoSweepAnimation::getCurrentAlpha()
{
    double alpha = 1.0;

    return alpha;
}

void
PoikoSweepAnimation::setIsPausingCallback(PausingCallback *callback)
{
    if (mOrderAnimationSet != 0)
    {
        mOrderAnimationSet->setIsPausingCallback(callback);
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->setIsPausingCallback(callback);
    }

    AnimationBase::setIsPausingCallback(callback);
}

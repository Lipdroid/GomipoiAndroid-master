//
//
//
#include "PoikoMoveAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
PoikoMoveAnimation::start(tCallback_onCompletedAnim onCompletedAnimation)
{
    if (mTranslateAnimation != 0)
    {
        mMaxAnimationTime = mTranslateAnimation->getTotalTime();
    }

    AnimationBase::start(onCompletedAnimation);

    // Start処理
    if (mTranslateAnimation != 0)
    {
        mTranslateAnimation->start(0);
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->start(
                0,
                mOnChangeTexture);
    }
}

void
PoikoMoveAnimation::stop()
{
    if (mTranslateAnimation != 0)
    {
        mTranslateAnimation->stop();
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->stop();
    }

    AnimationBase::stop();
}

void
PoikoMoveAnimation::pause()
{
    if (mTranslateAnimation != 0)
    {
        mTranslateAnimation->pause();
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->pause();
    }

    AnimationBase::pause();
}

void
PoikoMoveAnimation::pauseEnd()
{
    if (mTranslateAnimation != 0)
    {
        mTranslateAnimation->pauseEnd();
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->pauseEnd();
    }

    AnimationBase::pauseEnd();
}

void
PoikoMoveAnimation::checkState()
{
    if (mFrameAnimation != 0)
    {
        mFrameAnimation->checkState();
    }

    AnimationBase::checkState();
}

void
PoikoMoveAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    if (mTranslateAnimation != 0)
    {
        mTranslateAnimation->flushAnimation(screenWidth, screenHeight, vertexts);
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->flushAnimation(screenWidth, screenHeight, vertexts);
    }
}

double
PoikoMoveAnimation::getCurrentAlpha()
{
    double alpha = 1.0;

    return alpha;
}

void
PoikoMoveAnimation::setIsPausingCallback(PausingCallback *callback)
{
    if (mTranslateAnimation != 0)
    {
        mTranslateAnimation->setIsPausingCallback(callback);
    }

    if (mFrameAnimation != 0)
    {
        mFrameAnimation->setIsPausingCallback(callback);
    }

    AnimationBase::setIsPausingCallback(callback);
}

// ------------------------------
// Accesser
// ------------------------------
void
PoikoMoveAnimation::setNewOffset(double left, double top)
{
    if (mTranslateAnimation != 0)
    {
        mTranslateAnimation->setPosition(0, 0, left, top);
    }
}

//
//
//
#include "OrderAnimationSet.h"

// ------------------------------
// Override
// ------------------------------
void
OrderAnimationSet::start(tCallback_onCompletedAnim onCompletedAnimation)
{
    if (mAnimationList.size() == 0)
    {
        return;
    }

    // Totalの時間を算出
//    mMaxAnimationTime = 0.0;
//    for (AnimationBase *animation : mAnimationList)
//    {
//        if (animation == 0)
//        {
//            continue;
//        }
//        mMaxAnimationTime += animation->getTotalTime();
//    }
    getTotalTime();

    AnimationBase::start(onCompletedAnimation);

    // Start処理
    mOnCompletedAnim = [&](int animationId)
    {
        mCurrentAnimationIndex += 1;
        if (mCurrentAnimationIndex >= mAnimationList.size())
        {
            if (!mIsLoopingPlay) {
                stop();
                if (mCompletedFunc != 0)
                {
                    mCompletedFunc(getAnimationId());
                }
                return;
            }

            mCurrentAnimationIndex = 0;
        }

        AnimationBase *animation = getCurrentAnimation();
        if (animation == 0)
        {
            stop();
            if (mCompletedFunc != 0)
            {
                mCompletedFunc(getAnimationId());
            }
            return;
        }

        animation->start(mOnCompletedAnim);
    };

    mCurrentAnimationIndex = 0;
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->start(mOnCompletedAnim);
    }
}

void
OrderAnimationSet::stop()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->stop();
    }

    AnimationBase::stop();
}

void
OrderAnimationSet::pause()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->pause();
    }

    AnimationBase::pause();
}

void
OrderAnimationSet::pauseEnd()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->pauseEnd();
    }

    AnimationBase::pauseEnd();
}

void
OrderAnimationSet::checkState()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->checkState();
    }
}

void
OrderAnimationSet::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->flushAnimation(screenWidth, screenHeight, vertexts);
    }
}

double
OrderAnimationSet::getCurrentAlpha()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getCurrentAlpha();
    }
    return 1.0;
}

void
OrderAnimationSet::setIsPausingCallback(PausingCallback *callback)
{
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        animation->setIsPausingCallback(callback);
    }

    AnimationBase::setIsPausingCallback(callback);
}

double
OrderAnimationSet::getTotalTime() {
    mMaxAnimationTime = 0.0;
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        mMaxAnimationTime += animation->getTotalTime();
    }
    return AnimationBase::getTotalTime();
}

// ------------------------------
// Accesser
// ------------------------------
void
OrderAnimationSet::addAnimation(AnimationBase *animation)
{
    if (animation == 0)
    {
        return;
    }
    mAnimationList.push_back(animation);
}

double
OrderAnimationSet::getCurrentLeftOffset()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getCurrentLeftOffset();
    }
    return 0.0;
}

double
OrderAnimationSet::getCurrentTopOffset()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getCurrentTopOffset();
    }
    return 0.0;
}

// ------------------------------
// Function
// ------------------------------
AnimationBase*
OrderAnimationSet::getCurrentAnimation()
{
    if (mCurrentAnimationIndex < 0 || mCurrentAnimationIndex >= mAnimationList.size())
    {
        return 0;
    }
    return mAnimationList[mCurrentAnimationIndex];
}

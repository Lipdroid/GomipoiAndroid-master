//
//
//
#include "MultipleAnimationSet.h"

// ------------------------------
// Override
// ------------------------------
void
MultipleAnimationSet::start(tCallback_onCompletedAnim onCompletedAnimation)
{
    // Total時間が長いアニメを検出
    double maxTime = 0.0;
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }

        if (maxTime < animation->getTotalTime())
        {
            maxTime = animation->getTotalTime();
        }
    }

    mMaxAnimationTime = maxTime;

    AnimationBase::start(onCompletedAnimation);

    // Start処理
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }

        animation->start(0);
    }
}

void
MultipleAnimationSet::stop()
{
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        animation->stop();
    }

    AnimationBase::stop();
}

void
MultipleAnimationSet::pause()
{
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        animation->pause();
    }
    AnimationBase::pause();
}

void
MultipleAnimationSet::pauseEnd()
{
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        animation->pauseEnd();
    }
    AnimationBase::pauseEnd();
}

void
MultipleAnimationSet::checkState()
{
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0 || !animation->isAnimating())
        {
            continue;
        }

        animation->checkState();
    }

    AnimationBase::checkState();
}

void
MultipleAnimationSet::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        animation->flushAnimation(screenWidth, screenHeight, vertexts);
    }
}

double
MultipleAnimationSet::getCurrentAlpha()
{
    double alpha = 1.0;
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }

        if (alpha > animation->getCurrentAlpha())
        {
            alpha = animation->getCurrentAlpha();
        }
    }

    return alpha;
}

void
MultipleAnimationSet::setIsPausingCallback(PausingCallback *callback)
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
MultipleAnimationSet::getCurrentLeftOffset()
{
    double offset = 0.0;
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        
        offset += animation->getCurrentLeftOffset();
    }
    
    return offset;
}

double
MultipleAnimationSet::getCurrentTopOffset()
{
    double offset = 0.0;
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation == 0)
        {
            continue;
        }
        
        offset += animation->getCurrentTopOffset();
    }
    
    return offset;
}

// ------------------------------
// Accesser
// ------------------------------
void
MultipleAnimationSet::addAnimation(AnimationBase *animation)
{
    if (animation == 0)
    {
        return;
    }
    mAnimationList.push_back(animation);
}

AnimationBase *
MultipleAnimationSet::getAnimation(int animationId) {
    for (AnimationBase *animation : mAnimationList)
    {
        if (animation != 0)
        {
            if (animation->getAnimationId() == animationId) {
                return animation;
            }
        }
    }
    
    return NULL;
}
//
//  GemParts.cpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/08.
//
//

#include "GemParts.h"

// ------------------------------
// Override
// ------------------------------
void
GemParts::initParts(PartsCallback *callback)
{
    PartsBase::initParts(callback);
    
    if (mGemData != 0 && mGemData->isFirst())
    {
        startAnimation(eAnimId_Garbage_Appear);
    }
}

// ------------------------------
// Accesser
// ------------------------------
bool
GemParts::isCompleted()
{
    if (mGemData == 0)
    {
        return true;
    }
    return mGemData->isCompleted();
}

bool
GemParts::isLastAnimation(int broomType)
{
    if (mGemData == 0)
    {
        return false;
    }
    
    int restChangeCount = 1;
    switch (broomType)
    {
        case eBroomType_Gold:
        {
            restChangeCount = 5;
            break;
        }
        
        case eBroomType_Silver:
        {
            restChangeCount = 2;
            break;
        }
    }
    
    return (mGemData->getRestLife() <= restChangeCount);
}

/**
 * 宝石のBottom値を返す
 */
double
GemParts::getBottom()
{
    return getTopOffset() + getHeight();
}

/**
 * スワイプの対象になるかを判定する
 */
void
GemParts::checkHitSwipe(double ptX, double ptY, double screenWidth, double screenHeight)
{
    if (mGemData == 0)
    {
        return;
    }
    mGemData->checkHit(ptX, ptY, screenWidth, screenHeight);
}

/**
 * スワイプの対象かを返す
 */
bool
GemParts::isSwipeTarget()
{
    if (mGemData == 0)
    {
        return false;
    }
    return mGemData->isSwipeTarget();
}

/**
 * スワイプの対象から外す
 */
void
GemParts::resetSwipe()
{
    if (mGemData == 0)
    {
        return;
    }
    mGemData->setSwipeTarget(false);
}

void
GemParts::startMoveAnimation(int broomType, tCallback_onCompletedGarbageAnim onCompleted)
{
    int maxLife = 1;
    int restLife = 0;
    double oldLeft = 0;
    double oldTop = 0;
    if (mGemData != 0)
    {
        maxLife = mGemData->getMaxLife();
        restLife = mGemData->getRestLife();
        oldLeft = mGemData->getInitLeft();
        oldTop = mGemData->getInitTop();
    }
    
    if (maxLife == 0)
    {
        maxLife = 1;
    }
    
    int addValue = -1;
    switch (broomType)
    {
        case eBroomType_Gold:
        {
            addValue = -5;
            break;
        }
            
        case eBroomType_Silver:
        {
            addValue = -2;
            break;
        }
    }
    restLife += addValue;
    
    if (restLife < 0)
    {
        restLife = 0;
    }
    
    double canX = 286.5 - getWidth() / 2.0;
    double canY = 138.5 - getHeight() / 2.0;
    double newLeft = oldLeft + ((canX - oldLeft) * (double)(maxLife - restLife) / (double)maxLife);
    double newTop = oldTop + ((canY - oldTop) * (double)(maxLife - restLife) / (double)maxLife);
    
    AnimationBase * animation = getAnimation(eAnimId_Garbage_Move_Set);
    if (animation != 0)
    {
        MultipleAnimationSet *animationSet = (MultipleAnimationSet *) animation;
        AnimationBase * translate = animationSet->getAnimation(eAnimId_Garbage_Move_Translate);
        if (translate != NULL) {
            ((TranslateAnimation *) translate)->setPosition(0, 0, newLeft - getLeftOffset(), getTopOffset() - newTop);
        }
    }
    
    startAnimation(
                   eAnimId_Garbage_Move_Set,
                   [&, onCompleted, newLeft, newTop, restLife](int animationId)
                   {
                       setOffset(newLeft, newTop);
                       
                       if (mGemData != 0)
                       {
                           mGemData->setRestLife(restLife);
                       }
                       
                       int bonus = 0;
                       if (isCompleted())
                       {
                           if (mGemData != 0)
                           {
                               bonus = mGemData->getBonus();
                           }
                           hide();
                       }
                       
                       if (onCompleted != 0)
                       {
                           onCompleted(bonus);
                       }
                   });
    
}

int
GemParts::getDefaultBonus()
{
    if (mGemData == 0)
    {
        return 0;
    }
    return mGemData->getBonus();
}

/**
 * ゴミパーツを作成する
 */
GemParts*
GemParts::makeGemParts()
{
    return makeGemParts(GemData::makeGemData());
}

/**
 * ゴミパーツを作成する
 */
GemParts*
GemParts::makeGemParts(GemData* data)
{
    GemParts *gem = new GemParts(data);
    gem->addTexture(GemParts::makeTexture(data));
    
    // 飛んでくアニメーション
    TranslateAnimation *translateAnimation = new TranslateAnimation(eAnimId_Garbage_Move_Translate, 0, 0, 0, 0, 500);
    RotateAnimation *rotateAnimation = new RotateAnimation(eAnimId_Garbage_Move_Rotate, 0, 360, 250, true);
    rotateAnimation->setLooping(true);
    MultipleAnimationSet *animationSet = new MultipleAnimationSet(eAnimId_Garbage_Move_Set);
    animationSet->addAnimation(rotateAnimation);
    animationSet->addAnimation(translateAnimation);
    gem->addAnimation(animationSet);
    
    FadeAnimation *fadeAnimation = new FadeAnimation(eAnimId_Garbage_Appear, 0, 1, 300);
    fadeAnimation->setKeepAfter(true);
    gem->addAnimation(fadeAnimation);
    
    return gem;
}

// ------------------------------
// Function
// ------------------------------
/**
 * ゴミIDに対応するTextuerを作成する
 */
PartsTexture*
GemParts::makeTexture(GemData* data)
{
    return new PartsTexture("gem.png", 29, 29);
}

//
//
//
#ifndef GOMIBAKO_SVN_HOLEPARTSBASE_H
#define GOMIBAKO_SVN_HOLEPARTSBASE_H

#include "../../../lib/model/parts/PartsBase.h"
#include "../../parameter/app_parameter.h"

#include "../../../lib/model/animation/general/MultipleAnimationSet.h"
#include "../../../lib/model/animation/general/OrderAnimationSet.h"
#include "../../../lib/model/animation/general/ScaleAnimation.h"
#include "../../../lib/model/animation/general/FadeAnimation.h"

class HolePartsBase : public PartsBase {

// ------------------------------
// Member
// ------------------------------
private:
    bool mHasAppeared;
    
    tCallback_onCompletedAnim mOnCompletedStartAnimation;

// ------------------------------
// Constructor
// ------------------------------
public:
    HolePartsBase() : PartsBase(ePartsID_hole, 102.0, 320.0, 116.0, 46.0)
    {
        mHasAppeared = false;
        
        //フェイドインのアニメーション
        FadeAnimation *fadeAnimation = new FadeAnimation(eAnimId_Hole_Appear, 0, 1, 300);
        fadeAnimation->setKeepAfter(true);
        ScaleAnimation *halfAnimation = new ScaleAnimation(eAnimId_Hole_HalfScale, 0.5, 0.5, 300);
        halfAnimation->setKeepAfter(true);
        
        MultipleAnimationSet *animationSet = new MultipleAnimationSet(eAnimId_Hole_AppearSet);
        animationSet->addAnimation(fadeAnimation);
        animationSet->addAnimation(halfAnimation);
        
        //拡大アニメーション
        ScaleAnimation *scaleAnimation = new ScaleAnimation(eAnimId_Hole_Scale, 0.5, 1.0, 700);
        scaleAnimation->setKeepAfter(true);
        
        OrderAnimationSet *orderAnimation = new OrderAnimationSet(eAnimId_Hole_ScaleSet);
        orderAnimation->addAnimation(animationSet);
        orderAnimation->addAnimation(scaleAnimation);
        
        addAnimation(orderAnimation);
    }

// ------------------------------
// Destructor
// ------------------------------
    virtual ~HolePartsBase()
    {
        mOnCompletedStartAnimation = NULL;
    }

// ------------------------------
// Override
// ------------------------------
    virtual void initParts(PartsCallback *callback);
    virtual void onPressed();
    virtual void onPressedEnd();

// ------------------------------
// Accesser
// ------------------------------
    bool hasAppeared();
    void resetAppeared();
    
    void checkHitTouch(double ptX, double ptY, double screenWidth, double screenHeight);
    void startAppearAnimation(tCallback_onCompletedAnim onCompleted);

// ------------------------------
// Function
// ------------------------------
private:
};

#endif //GOMIBAKO_SVN_HOLEPARTSBASE_H


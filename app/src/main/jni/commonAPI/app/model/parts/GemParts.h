//
//  GemParts.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/08.
//
//

#ifndef GemParts_h
#define GemParts_h

#include "../../../lib/model/parts/PartsBase.h"
#include "../../parameter/app_parameter.h"

#include "../data/GemData.h"

#include "../../../lib/model/animation/general/MultipleAnimationSet.h"
#include "../../../lib/model/animation/general/RotateAnimation.h"
#include "../../../lib/model/animation/general/TranslateAnimation.h"
#include "../../../lib/model/animation/general/FadeAnimation.h"

class GemParts : public PartsBase {

// ------------------------------
// Member
// ------------------------------
private:
    GemData *mGemData;
    
// ------------------------------
// Constructor
// ------------------------------
public:
    GemParts(GemData *data) : PartsBase(
                                        0,
                                        data->getCurrentLeft(),
                                        data->getCurrentTop(),
                                        data->getWidth(),
                                        data->getHeight())
    {
        mGemData = data;
        data->setSwipeTarget(false);
    }
    
    // ------------------------------
    // Destructor
    // ------------------------------
    virtual ~GemParts()
    {
        if (mGemData != 0)
        {
            delete mGemData;
            mGemData = 0;
        }
        
    }
    
    // ------------------------------
    // Override
    // ------------------------------
    virtual void initParts(PartsCallback *callback);
    
    // ------------------------------
    // Accesser
    // ------------------------------
public:
    bool isCompleted();
    bool isLastAnimation(int broomType);
    double getBottom();
    void checkHitSwipe(double ptX, double ptY, double screenWidth, double screenHeight);
    bool isSwipeTarget();
    void resetSwipe();
    void startMoveAnimation(int broomType, tCallback_onCompletedGarbageAnim onCompleted);
    int getDefaultBonus();
    
    static GemParts *makeGemParts();
    static GemParts *makeGemParts(GemData *data);
    
    // ------------------------------
    // Function
    // ------------------------------
private:
    static PartsTexture *makeTexture(GemData* data);
    
};

#endif /* GemParts_h */

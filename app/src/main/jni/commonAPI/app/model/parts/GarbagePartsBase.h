//
//
//
#include "../../../lib/model/parts/PartsBase.h"
#include "../../parameter/app_parameter.h"

#include "../data/GarbageData.h"

#include "../../../lib/model/animation/general/MultipleAnimationSet.h"
#include "../../../lib/model/animation/general/RotateAnimation.h"
#include "../../../lib/model/animation/general/TranslateAnimation.h"
#include "../../../lib/model/animation/general/FadeAnimation.h"

#ifndef GOMIBAKO_SVN_GARBAGEPARTSBASE_H
#define GOMIBAKO_SVN_GARBAGEPARTSBASE_H

class GarbagePartsBase : public PartsBase {

// ------------------------------
// Member
// ------------------------------
private:
    GarbageData *mGarbageData;
    int mSpriteSheet;

// ------------------------------
// Constructor
// ------------------------------
public:
    GarbagePartsBase(GarbageData *data) : PartsBase(
            0,
            data->getCurrentLeft(),
            data->getCurrentTop(),
            data->getWidth(),
            data->getHeight())
    {
        mGarbageData = data;
        data->setSwipeTarget(false);
        mSpriteSheet = 0;
    }

// ------------------------------
// Destructor
// ------------------------------
    virtual ~GarbagePartsBase()
    {
        if (mGarbageData != NULL)
        {
            delete mGarbageData;
            mGarbageData = NULL;
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
    int getSpriteSheet();
    bool isSp();

    static GarbagePartsBase *makeGarbageParts(int garbageId);
    static GarbagePartsBase *makeGarbageParts(GarbageData *data);

// ------------------------------
// Function
// ------------------------------
private:
    static PartsTexture* makeTexture(GarbageData* data, int* outSpriteSheet);

};

#endif //GOMIBAKO_SVN_GARBAGEPARTSBASE_H


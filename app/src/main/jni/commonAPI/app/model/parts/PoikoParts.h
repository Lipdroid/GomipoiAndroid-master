//
//
//
#include "../../../lib/model/parts/PartsBase.h"
#include "../../parameter/app_parameter.h"

#ifndef GOMIBAKO_SVN_POIKOPARTS_H
#define GOMIBAKO_SVN_POIKOPARTS_H


enum
{
    ePoiko_Normal1 = 0,
    ePoiko_Normal2,
    ePoiko_Move1,
    ePoiko_Move2,
    ePoiko_Sweep1,
    ePoiko_Sweep2,
    ePoiko_Sweep3,
    ePoiko_Glanced,
    ePoiko_Silver_Normal1,
    ePoiko_Silver_Normal2,
    ePoiko_Silver_Move1,
    ePoiko_Silver_Move2,
    ePoiko_Silver_Sweep1,
    ePoiko_Silver_Sweep2,
    ePoiko_Silver_Sweep3,
    ePoiko_Silver_Glanced,
    ePoiko_Gold_Normal1,
    ePoiko_Gold_Normal2,
    ePoiko_Gold_Move1,
    ePoiko_Gold_Move2,
    ePoiko_Gold_Sweep1,
    ePoiko_Gold_Sweep2,
    ePoiko_Gold_Sweep3,
    ePoiko_Gold_Glanced,
    ePoiko_Hero_Normal1,
    ePoiko_Hero_Normal2,
    ePoiko_Hero_Move1,
    ePoiko_Hero_Move2,
    ePoiko_Hero_Sweep1,
    ePoiko_Hero_Sweep2,
    ePoiko_Hero_Sweep3,
};

class PoikoParts : public PartsBase {


// ------------------------------
// Member
// ------------------------------
private:
    int mCurrentState;
    int mCurrentCharacter;

// ------------------------------
// Constructor
// ------------------------------
public:
    PoikoParts(double x, double y, double width, double height) : PartsBase(ePartsID_poiko, x, y,
                                                                            width, height)
    {
        mCurrentState = ePoiko_Status_Normal;
        mCurrentCharacter = eCharacter_Poiko;
    }

    virtual ~PoikoParts()
    {
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual void setTexturePosition(int position);


// ------------------------------
// Accesser
// ------------------------------
public:
    void setBroomType(int type);
    void setCharacterType(int type);
    void onBonusTime();

// ------------------------------
// Function
// ------------------------------
private:
    int getCurrentTexturePosition(int texturePosition);
    int getRandomNumber();

};


#endif //GOMIBAKO_SVN_POIKOPARTS_H


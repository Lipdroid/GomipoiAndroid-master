//
//
//
#include "../../../lib/model/parts/PartsBase.h"
#include "../../parameter/app_parameter.h"

#ifndef GOMIBAKO_SVN_AUTOBROOMPARTS_H
#define GOMIBAKO_SVN_AUTOBROOMPARTS_H


enum
{
    eAutoBroom_Normal1 = 0,
    eAutoBroom_Normal2,
    eAutoBroom_Move1,
    eAutoBroom_Move2,
    eAutoBroom_Sweep1,
    eAutoBroom_Sweep2,
    eAutoBroom_Sweep3,
    eAutoBroom_Glanced,
};


class AutoBroomParts : public PartsBase {


// ------------------------------
// Member
// ------------------------------
private:
    bool mIsOwned;

// ------------------------------
// Constructor
// ------------------------------
public:
    AutoBroomParts(double x, double y, double width, double height) : PartsBase(ePartsID_auto_broom, x, y,
                                                                            width, height)
    {
        mIsOwned = false;
        this->hide();
    }

    virtual ~AutoBroomParts()
    {
    }

// ------------------------------
// Override
// ------------------------------
public:


// ------------------------------
// Accesser
// ------------------------------
public:
    void setOwned(bool value);
    
// ------------------------------
// Function
// ------------------------------
private:
    int getRandomNumber();

};


#endif //GOMIBAKO_SVN_AUTOBROOMPARTS_H


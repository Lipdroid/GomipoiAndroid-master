//
// クリックイベント管理クラス
//
#include "../../const/parameter.h"
#include "../../model/parts/PartsBase.h"

#ifndef TEST_CLICKEVENTMANAGER_H
#define TEST_CLICKEVENTMANAGER_H

class ClickEventManager {

// ------------------------------
// Member
// ------------------------------
protected:
    int mTargetPartsId;
    std::vector<PartsBase *> mPartsList;
    double mScreenWidth;
    double mScreenHeight;
    tCallback_onPartsClicked mClickedFunc;

// ------------------------------
// Constructor
// ------------------------------
public:
    ClickEventManager(std::vector<PartsBase *> partsList)
    {
        int i;
        for (i = 0; i < partsList.size(); i++)
        {
            mPartsList.push_back(partsList[i]);
        }
    }

// ------------------------------
// Accesser
// ------------------------------
public:
    void setScreenSize(double width, double height);
    void setClickCallback(tCallback_onPartsClicked callback);
    bool onTouchedDown(float ptX, float ptY);
    bool onTouchedMove(float ptX, float ptY);
    bool onTouchedUp();

// ------------------------------
// Function
// ------------------------------
protected:
    PartsBase *getParts(int id);

};


#endif //TEST_CLICKEVENTMANAGER_H

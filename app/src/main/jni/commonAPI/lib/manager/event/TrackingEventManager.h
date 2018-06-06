//
// なぞるアクション管理クラス
//
#include "../../const/parameter.h"
#include "../../model/parts/PartsBase.h"

#ifndef TEST_TRACKINGEVENTMANAGER_H
#define TEST_TRACKINGEVENTMANAGER_H


class TrackingEventManager {

// ------------------------------
// Member
// ------------------------------
private:
    std::vector<PartsBase *> mPartsList;
    double mScreenWidth;
    double mScreenHeight;

// ------------------------------
// Constructor
// ------------------------------
public:
    TrackingEventManager(std::vector<PartsBase *> partsList)
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
    void onTouchedMove(float ptX, float ptY);
    void onTouchedUp();
};


#endif //TEST_TRACKINGEVENTMANAGER_H

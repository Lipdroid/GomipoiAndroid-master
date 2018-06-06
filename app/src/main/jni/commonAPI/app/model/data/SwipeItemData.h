//
//  SwipeItemData.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/07.
//
//

#ifndef SwipeItemData_h
#define SwipeItemData_h

#include "../../parameter/app_parameter.h"

#define SWIPE_TARGET_DIST 20.0

class SwipeItemData {
    
// ------------------------------
// Member
// ------------------------------
protected:
    int mRestLife;
    double mInitLeft;
    double mInitTop;
    bool mIsSwipeTarget;
    bool mIsFirst;
    
// ------------------------------
// Constructor
// ------------------------------
public:
    SwipeItemData(int restLife, double left, double top, bool isFirst)
    {
        mRestLife = restLife;
        mInitLeft = left;
        mInitTop = top;
        mIsFirst = isFirst;
    }
    
// ------------------------------
// Destructor
// ------------------------------
public:
    virtual ~SwipeItemData()
    {
    }
    
// ------------------------------
// Accessor
// ------------------------------
public:
    int getRestLife();
    void setRestLife(int restLife);
    double getInitLeft();
    double getInitTop();
    bool isSwipeTarget();
    void setSwipeTarget(bool isSwipeTarget);
    bool isFirst();
    
    double getCurrentLeft();
    double getCurrentTop();
    virtual double getWidth();
    virtual double getHeight();
    virtual int getMaxLife();
    virtual int getBonus();
    bool isCompleted();
    void checkHit(double ptX, double ptY, double screenWidth, double screenHeight);
    void onSweep(int broomType, double startTouchX, double startTouchY, double screenWidth, double screenHeight);
    
};

#endif /* SwipeItemData_h */

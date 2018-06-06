//
//
//

#ifndef GOMIBAKO_SVN_GARBAGEDATA_H
#define GOMIBAKO_SVN_GARBAGEDATA_H

#include "SwipeItemData.h"

class GarbageData : public SwipeItemData {

// ------------------------------
// Member
// ------------------------------
public:
    int mGarbageId;

// ------------------------------
// Constructor
// ------------------------------
public:
    GarbageData(int id, int restLife, double left, double top) : SwipeItemData(restLife, left, top, false)
    {
        mGarbageId = id;
    }

    GarbageData(int id, int restLife, double left, double top, bool isFirst) : SwipeItemData(restLife, left, top, isFirst)
    {
        mGarbageId = id;
    }

// ------------------------------
// Destructor
// ------------------------------
public:
    virtual ~GarbageData()
    {
    }

// ------------------------------
// Accessor
// ------------------------------
public:
    GarbageData *clone();

    virtual double getWidth();
    virtual double getHeight();
    virtual int getMaxLife();
    virtual int getBonus();
    bool isSp();
    
    void changeGarbageId(int garbageId);

    static GarbageData *makeGarbageData(int garbageId);

// ------------------------------
// Function
// ------------------------------
private:
    static double getGarbageWidth(int garbageId);
    static double getGarbageHeight(int garbageId);
    static int getMaxLife(int garbageId);
    static int getBonus(int garbageId);

};


#endif //GOMIBAKO_SVN_GARBAGEDATA_H

//
//  GemData.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/07.
//
//

#ifndef GemData_h
#define GemData_h

#include "SwipeItemData.h"

class GemData : public SwipeItemData {
    
// ------------------------------
// Member
// ------------------------------
public:
    
// ------------------------------
// Constructor
// ------------------------------
public:
    GemData(int restLife, double left, double top) : SwipeItemData(restLife, left, top, false)
    {
    }
    
    GemData(int restLife, double left, double top, bool isFirst) : SwipeItemData(restLife, left, top, isFirst)
    {
    }
    
// ------------------------------
// Destructor
// ------------------------------
public:
    virtual ~GemData()
    {
    }
    
// ------------------------------
// Accesser
// ------------------------------
public:
    GemData *clone();
    
    virtual double getWidth();
    virtual double getHeight();
    virtual int getMaxLife();
    virtual int getBonus();
    
    static GemData *makeGemData();
    
};

#endif /* GemData_h */

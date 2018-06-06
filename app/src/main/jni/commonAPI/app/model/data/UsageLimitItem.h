//
//  UsageLimitItems.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/26.
//
//

#ifndef UsageLimitItem_h
#define UsageLimitItem_h

#include <string>

class UsageLimitItem
{
    // ------------------------------
    // Member
    // ------------------------------
public:
    std::string mItemCode;
    int mCount;
    
    // ------------------------------
    // Constructor
    // ------------------------------
    UsageLimitItem(const std::string itemCode, int count)
    {
        mItemCode = itemCode;
        mCount = count;
    }
    
    // ------------------------------
    // Destructor
    // ------------------------------
    virtual ~UsageLimitItem()
    {
    }
};

#endif /* UsageLimitItem_h */

//
//
//
#include <string>

#ifndef GOMIBAKO_SVN_ITEM_H
#define GOMIBAKO_SVN_ITEM_H


class Item
{
    // ------------------------------
    // Member
    // ------------------------------
public:
    std::string mItemCode;
    int mOwnCount;
    bool mItemUsing;
    double mUsedAt;

    // ------------------------------
    // Constructor
    // ------------------------------
    Item(const std::string itemCode, int ownCount, bool itemUsing, double usedAt)
    {
        mItemCode = itemCode;
        mOwnCount = ownCount;
        mItemUsing = itemUsing;
        mUsedAt = usedAt;
    }

    // ------------------------------
    // Destructor
    // ------------------------------
    virtual ~Item()
    {
    }
};


#endif //GOMIBAKO_SVN_ITEM_H

//
//
//
#include <string>

#ifndef GOMIBAKO_SVN_USINGITEM_H
#define GOMIBAKO_SVN_USINGITEM_H

class UsingItem
{
// ------------------------------
// Member
// ------------------------------
public:
    std::string item_code;
    int use_count;
    double used_at;

// ------------------------------
// Constructor
// ------------------------------
    UsingItem(const std::string item_code, int use_count, double used_at)
    {
        this->item_code = item_code;
        this->use_count = use_count;
        this->used_at = used_at;
    }

// ------------------------------
// Destructor
// ------------------------------
    virtual ~UsingItem()
    {
    }

};

#endif //GOMIBAKO_SVN_USINGITEM_H

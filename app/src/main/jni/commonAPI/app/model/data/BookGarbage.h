//
//
//
#include <string>

#ifndef GOMIBAKO_SVN_BOOKGARBAGE_H
#define GOMIBAKO_SVN_BOOKGARBAGE_H

class BookGarbage
{
// ------------------------------
// Member
// ------------------------------
public:
    std::string garbage_code;
    bool checked;

// ------------------------------
// Constructor
// ------------------------------
    BookGarbage(std::string garbage_code, bool checked)
    {
        this->garbage_code = garbage_code;
        this->checked = checked;
    }

    virtual ~BookGarbage()
    {
    }

};

#endif //GOMIBAKO_SVN_BOOKGARBAGE_H

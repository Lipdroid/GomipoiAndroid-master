//
//
//
#include "../parameter/app_parameter.h"

#ifndef GOMIBAKO_SVN_GARBAGEUTILS_H
#define GOMIBAKO_SVN_GARBAGEUTILS_H


class GarbageUtils {

public:
    static int getGarbageId(std::string garbageCode);
    static std::string getGarbageCode(int garbageId);

};


#endif //GOMIBAKO_SVN_GARBAGEUTILS_H

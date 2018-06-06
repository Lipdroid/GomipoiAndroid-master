//
//
//
#include "../parameter/app_parameter.h"
#include "../../picojson/picojson.h"
#include "../model/data/GarbageData.h"
#include "../model/data/UsageLimitItem.h"

#include <time.h>

#ifndef GOMIBAKO_SVN_JSONUTILS_H
#define GOMIBAKO_SVN_JSONUTILS_H


class JsonUtils {

// ------------------------------
// Accesser
// ------------------------------
public:
    static std::string makeGarbageJson(int garbageId, int restCount, double initLeft, double initTop);
    static std::string makeHeroDrinkJson(double left, double top);
    static void parseGarbageJson(std::string json, std::vector<GarbageData *> *list);
    static void parseUserAppSelfResponse(std::string json, int *jewel_count, int *point, int *total_point);
    static void parseGomipoiGarbageOwnResponse(std::string json, std::vector<std::string> *list);
    static void parseGomipoiGameLoadResponse(std::string json, int *placeType, int *level, double *gomiGauge, int *experiencePoint, int *currentLevelRequiredPoint, int *nextLevelRequiredPoint,
                                            std::vector<UsingItem *> *list, std::string *garbagesJson, std::vector<UsageLimitItem *> *usageLimitItems, double *serverTime, int *jirokichi_jewel_count, int *friendCount, int *playingCharaType);
    static void parseGomipoiItemOwnResponse(std::string json, std::vector<Item *> *list, double *serverTime);
    static void parseGomipoiBookOwnResponse(std::string json, std::vector<BookGarbage *> *garbagesList, std::vector<int> *received_book_bonusesList);
    static void parseGomipoiGarbagesSynthesesResponse(std::string json, std::string *garbageCode);
    static void parseGomipoiGameSaveResponse(std::string json, int *newLevel, int *jirokichiJewelCount, int *experiencePoint, int *currentLevelRequiredPoint, int *nextLevelRequiredPoint);
    static void parseGomipoiBookReceiveBonuses(std::string json, std::vector<int> *pages);

    // DEBUG
    static std::string makeUserInfoJson(int userLevel, std::string garbages);

private:
    static double getMillisecondsFromDateText(char* scanf_format, std::string dateText);

};


#endif //GOMIBAKO_SVN_JSONUTILS_H

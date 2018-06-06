//
//
//
#include "JsonUtils.h"

#include <iostream>
#include <array>


// ------------------------------
// Accesser
// ------------------------------
std::string
JsonUtils::makeGarbageJson(int garbageId, int restCount, double initLeft, double initTop)
{
    std::ostringstream stream;
    stream.precision(15);
    stream << "{";
    stream << "\"garbage_code\":" << garbageId << ",";
    stream << "\"position\":{";
    stream << "\"top\":" << std::fixed << initTop << ",";
    stream << "\"left\":" << std::fixed << initLeft;
    stream << "},";
    stream << "\"swipe_left\":" << restCount;
    stream << "}";
    return stream.str();
}

std::string
JsonUtils::makeHeroDrinkJson(double left, double top)
{
    std::ostringstream stream;
    stream.precision(15);
    stream << "{";
    stream << "\"garbage_code\":" << -1 << ",";
    stream << "\"position\":{";
    stream << "\"top\":" << std::fixed << top << ",";
    stream << "\"left\":" << std::fixed << left;
    stream << "},";
    stream << "\"swipe_left\":" << -1;
    stream << "}";
    return stream.str();
}

void
JsonUtils::parseGarbageJson(std::string json, std::vector<GarbageData *>* list)
{
    std::string jsonText = json;

//    LOGI("json = %s", jsonText.c_str());

    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, jsonText);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::array array = v.get<picojson::array>();
    for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
    {
        int garbageCode = eGarbageId_Dust;
        int restCount = 0;
        double left = 0;
        double top = 0;

        picojson::object& garbageData = it2->get<picojson::object>();
        if (!garbageData["garbage_code"].is<picojson::null>())
        {
            garbageCode = garbageData["garbage_code"].get<double>();
        }

        if (!garbageData["swipe_left"].is<picojson::null>())
        {
            restCount = garbageData["swipe_left"].get<double>();
        }

        if (!garbageData["position"].is<picojson::null>())
        {
            picojson::object& position = garbageData["position"].get<picojson::object>();

            if (!position["left"].is<picojson::null>())
            {
                left = position["left"].get<double>();
            }

            if (!position["top"].is<picojson::null>())
            {
                top = position["top"].get<double>();
            }
        }

        list->push_back(new GarbageData(garbageCode, restCount, left, top));
    }

    return;
}

/**
 * /user_apps/self のレスポンスを解析する
 */
void
JsonUtils::parseUserAppSelfResponse(std::string json, int *jewel_count, int *point, int *total_point)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();

    if (!response["user_app"].is<picojson::null>())
    {
        picojson::object& user_app = response["user_app"].get<picojson::object>();
        if (!user_app["point"].is<picojson::null>())
        {
            *point = user_app["point"].get<double>();
        }

        if (!user_app["total_point"].is<picojson::null>())
        {
            *total_point = user_app["total_point"].get<double>();
        }

        if (!user_app["jewel_count"].is<picojson::null>())
        {
            *jewel_count = user_app["jewel_count"].get<double>();
        }
    }


}

/**
 * /gomipoi_garbages/own のレスポンスを解析して、Managerに渡す
 */
void
JsonUtils::parseGomipoiGarbageOwnResponse(std::string json, std::vector <std::string> *list)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();

    if (!response["garbages"].is<picojson::null>())
    {
        picojson::array array  = response["garbages"].get<picojson::array>();
        for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
        {
            picojson::object& garbages = it2->get<picojson::object>();

            std::string garbage_code;
            if (!garbages["garbage_code"].is<picojson::null>())
            {
                garbage_code = garbages["garbage_code"].get<std::string>();
            }

            list->push_back(garbage_code);
        }
    }

}

/**
 * /gomipoi_games/load のレスポンスを解析して、Managerに渡す
 */
void
JsonUtils::parseGomipoiGameLoadResponse(std::string json, int *placeType, int *level, double *gomiGauge, int *experiencePoint, int *currentLevelRequiredPoint, int *nextLevelRequiredPoint,
        std::vector<UsingItem *> *list, std::string *garbagesJson, std::vector<UsageLimitItem *> *usageLimitItems, double *serverTime, int *jirokichi_jewel_count, int *friendCount, int *playingCharaType)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();
    
    if (!response["place_type"].is<picojson::null>())
    {
        *placeType = response["place_type"].get<double>();
    }
    
    if (!response["user"].is<picojson::null>())
    {
        picojson::object& user = response["user"].get<picojson::object>();
        if (!user["friend_count"].is<picojson::null>())
        {
            *friendCount = user["friend_count"].get<double>();
        }
    }

    if (!response["gomipoi_user"].is<picojson::null>())
    {
        picojson::object& gomipoi_user = response["gomipoi_user"].get<picojson::object>();

        if (!gomipoi_user["level"].is<picojson::null>())
        {
            *level = gomipoi_user["level"].get<double>();
        }

        if (!gomipoi_user["gomi_gauge"].is<picojson::null>())
        {
            *gomiGauge = gomipoi_user["gomi_gauge"].get<double>();
        }
        
        if (!gomipoi_user["experience_point"].is<picojson::null>())
        {
            *experiencePoint = gomipoi_user["experience_point"].get<double>();
        }
        
        if (!gomipoi_user["current_level_required_point"].is<picojson::null>())
        {
            *currentLevelRequiredPoint = gomipoi_user["current_level_required_point"].get<double>();
        }
        
        if (!gomipoi_user["next_level_required_point"].is<picojson::null>())
        {
            *nextLevelRequiredPoint = gomipoi_user["next_level_required_point"].get<double>();
        }
        
        if (!gomipoi_user["playing_chara_type"].is<picojson::null>())
        {
            *playingCharaType = gomipoi_user["playing_chara_type"].get<double>();
        }
        
        
    }

    if (!response["using_items"].is<picojson::null>())
    {
        picojson::array array  = response["using_items"].get<picojson::array>();
        for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
        {
            picojson::object& using_items = it2->get<picojson::object>();

            std::string item_code;
            int use_count = 0;
            double usedAt = 0.0;

            if (!using_items["item_code"].is<picojson::null>())
            {
                item_code = using_items["item_code"].get<std::string>();
            }

            if (!using_items["use_count"].is<picojson::null>())
            {
                use_count = using_items["use_count"].get<double>();
            }

            if (!using_items["used_at"].is<picojson::null>())
            {
                std::string usedAtStr = using_items["used_at"].get<std::string>();
                char scanf_format[] = "%4d-%2d-%2dT%2d:%2d:%2dZ";
                usedAt = getMillisecondsFromDateText(scanf_format, usedAtStr);
            }

            list->push_back(new UsingItem(item_code, use_count, usedAt));
        }
    }
    
    if (!response["usage_limit_items"].is<picojson::null>())
    {
        picojson::array array  = response["usage_limit_items"].get<picojson::array>();
        for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
        {
            picojson::object& using_items = it2->get<picojson::object>();
            
            std::string item_code;
            int count = 0;
            
            if (!using_items["item_code"].is<picojson::null>())
            {
                item_code = using_items["item_code"].get<std::string>();
            }
            
            if (!using_items["count"].is<picojson::null>())
            {
                count = using_items["count"].get<double>();
            }
            
            usageLimitItems->push_back(new UsageLimitItem(item_code, count));
        }
    }

    if (!response["garbages"].is<picojson::null>())
    {
        *garbagesJson = response["garbages"].get<std::string>();
    }

    if (!response["server_date"].is<picojson::null>())
    {
        std::string serverDateStr = response["server_date"].get<std::string>();
        char scanf_format[] = "%4d-%2d-%2dT%2d:%2d:%2d+00:00";
        *serverTime = getMillisecondsFromDateText(scanf_format, serverDateStr);
    }
    
    if (!response["jirokichi_bonus"].is<picojson::null>())
    {
        picojson::object& jirokichi_bonus = response["jirokichi_bonus"].get<picojson::object>();
        
        if (!jirokichi_bonus["jewel_count"].is<picojson::null>())
        {
            *jirokichi_jewel_count = jirokichi_bonus["jewel_count"].get<double>();
        }
    }
}

/**
 * /gomipoi_items/own のレスポンスを解析して、Managerに渡す
 */
void
JsonUtils::parseGomipoiItemOwnResponse(std::string json, std::vector<Item *> *list, double *serverTime)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();

    if (!response["items"].is<picojson::null>())
    {
        picojson::array array  = response["items"].get<picojson::array>();
        for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
        {
            picojson::object& items = it2->get<picojson::object>();

            std::string item_code;
            int own_count = 0;
            int item_using = 0;
            double usedAt = 0.0;

            if (!items["item_code"].is<picojson::null>())
            {
                item_code = items["item_code"].get<std::string>();
            }

            if (!items["own_count"].is<picojson::null>())
            {
                own_count = items["own_count"].get<double>();
            }

            if (!items["item_using"].is<picojson::null>())
            {
                item_using = items["item_using"].get<double>();
            }

            if (!items["used_at"].is<picojson::null>())
            {
                std::string usedAtStr = items["used_at"].get<std::string>();
                char scanf_format[] = "%4d-%2d-%2dT%2d:%2d:%2dZ";
                usedAt = getMillisecondsFromDateText(scanf_format, usedAtStr);
            }

            list->push_back(new Item(item_code, own_count, (item_using != 0), usedAt));
        }
    }

    if (!response["server_date"].is<picojson::null>())
    {
        std::string serverDateStr = response["server_date"].get<std::string>();
        char scanf_format[] = "%4d-%2d-%2dT%2d:%2d:%2d+00:00";
        *serverTime = getMillisecondsFromDateText(scanf_format, serverDateStr);
    }
}

/**
 * /gomipoi_items/own のレスポンスを解析して、Managerに渡す
 */
void
JsonUtils::parseGomipoiBookOwnResponse(std::string json, std::vector<BookGarbage *> *garbagesList,
        std::vector<int> *received_book_bonusesList)
//void
//JsonUtils::parseGomipoiBookOwnResponse(std::string json, std::vector<BookGarbage *> *garbagesList)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();

    if (!response["garbages"].is<picojson::null>())
    {
        picojson::array array  = response["garbages"].get<picojson::array>();
        for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
        {
            picojson::object& garbages = it2->get<picojson::object>();

            std::string garbage_code;
            int checked;

            if (!garbages["garbage_code"].is<picojson::null>())
            {
                garbage_code = garbages["garbage_code"].get<std::string>();
            }

            if (!garbages["checked"].is<picojson::null>())
            {
                checked = garbages["checked"].get<double>();
            }

            garbagesList->push_back(new BookGarbage(garbage_code, (checked == 1)));
        }
    }

    if (!response["received_book_bonuses"].is<picojson::null>())
    {
        picojson::array array  = response["received_book_bonuses"].get<picojson::array>();
        for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
        {
            picojson::object& received_book_bonuses = it2->get<picojson::object>();

            int received_page = 0;
            if (!received_book_bonuses["received_page"].is<picojson::null>())
            {
                received_page = received_book_bonuses["received_page"].get<double>();
            }

            received_book_bonusesList->push_back(received_page);
        }
    }
}

/**
 * /gomipoi_garbages/syntheses のレスポンスを解析して、Managerに渡す
 */
void
JsonUtils::parseGomipoiGarbagesSynthesesResponse(std::string json, std::string *garbageCode)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();

    if (!response["garbage_code"].is<picojson::null>())
    {
        *garbageCode = response["garbage_code"].get<std::string>();
    }
}

/**
 * /gomipoi_games/save のレスポンスを解析して、Managerに渡す
 */
void
JsonUtils::parseGomipoiGameSaveResponse(std::string json, int *newLevel, int *jirokichiJewelCount, int *experiencePoint, int *currentLevelRequiredPoint, int *nextLevelRequiredPoint)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();

    *newLevel = 0;
    if (!response["new_level"].is<picojson::null>() && !response["new_level"].is<std::string>())
    {
        *newLevel = response["new_level"].get<double>();
    }
    
    if (!response["jirokichi_bonus"].is<picojson::null>())
    {
        picojson::object& jirokichi_bonus = response["jirokichi_bonus"].get<picojson::object>();
        
        if (!jirokichi_bonus["jewel_count"].is<picojson::null>())
        {
            *jirokichiJewelCount = jirokichi_bonus["jewel_count"].get<double>();
        }
    }
    
    if (!response["experience_point"].is<picojson::null>())
    {
        *experiencePoint = response["experience_point"].get<double>();
    }
    
    if (!response["current_level_required_point"].is<picojson::null>())
    {
        *currentLevelRequiredPoint = response["current_level_required_point"].get<double>();
    }
    
    if (!response["next_level_required_point"].is<picojson::null>())
    {
        *nextLevelRequiredPoint = response["next_level_required_point"].get<double>();
    }
}

void
JsonUtils::parseGomipoiBookReceiveBonuses(std::string json, std::vector<int> *pages)
{
    if (json.length() == 0)
    {
        return;
    }

    picojson::value v;
    std::string err = picojson::parse(v, json);
    if (!err.empty())
    {
        LOGE("error:%s)", err.c_str());
        return;
    }

    picojson::object& response = v.get<picojson::object>();

    if (!response["already_received_pages"].is<picojson::null>())
    {
        picojson::array array  = response["already_received_pages"].get<picojson::array>();
        for (picojson::array::iterator it2 = array.begin(); it2 != array.end(); it2++)
        {
            picojson::object& already_received_pages = it2->get<picojson::object>();

            int received_page = 0;
            if (!already_received_pages["received_page"].is<picojson::null>())
            {
                received_page = already_received_pages["received_page"].get<double>();
            }

            pages->push_back(received_page);
        }
    }
}

// DEBUG用
std::string
JsonUtils::makeUserInfoJson(int userLevel, std::string garbages)
{
    std::ostringstream stream;
    stream.precision(15);
    stream << "{";
    stream << "\"gomipoi_user\":{";
    stream << "\"level\":" << userLevel << ",";
    stream << "},";
    stream << "\"using_items\":{";
    stream << "\"item_code\":" << 0 << ",";
    stream << "\"use_count\":" << 0 << ",";
    stream << "\"used_at\":" << 0 << ",";
    stream << "},";
    stream << "\"garbages\":{";
    stream << garbages
    << ",";
    stream << "\"server_date\":" << "time" << ",";
    stream << "}";
    return stream.str();
}

double
JsonUtils::getMillisecondsFromDateText(char* scanf_format, std::string dateText)
{
    int yyyy, MM, dd, HH, mm, ss;
    sscanf(dateText.c_str(), scanf_format, &yyyy, &MM, &dd, &HH, &mm, &ss);
    tm ttm = tm();
    ttm.tm_year = yyyy - 1900; // Year since 1900
    ttm.tm_mon = MM - 1; // Month since January
    ttm.tm_mday = dd; // Day of the month [1-31]
    ttm.tm_hour = HH; // Hour of the day [00-23]
    ttm.tm_min = mm;
    ttm.tm_sec = ss;
    time_t time = mktime(&ttm);
    auto time_point = std::chrono::system_clock::from_time_t(time).time_since_epoch();
    return (double)std::chrono::duration_cast<std::chrono::milliseconds>(time_point).count();
}

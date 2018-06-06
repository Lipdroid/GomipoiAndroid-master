//
//
//
#include "PlayerManager.h"
#include "../model/missions/SecretMission.h"

// ------------------------------
// Getter / Setter
// ------------------------------
/**
 * 宝石数を返す
 */
int
PlayerManager::getGem()
{
    return mGem;
}

/**
 * ポイント数を返す
 */
int
PlayerManager::getPoint()
{
    return mPoint;
}

/**
 * 獲得したポイントの総数を返す
 */
int
PlayerManager::getTotalPoint()
{
    return mTotalPoint;
}

/**
 * ジロキチの隠れ家のフラグを返す
 */
bool
PlayerManager::canEnterUnderground()
{
    return mUndergroundJewelCount > 0;
}

void
PlayerManager::setUndergroundJewelCount(int jewelCount)
{
#if TEST_JIROKICHI
    mUndergroundJewelCount = RandomUtils::getRandomNumber(20) + 10;//jewelCount;
#else
    mUndergroundJewelCount = jewelCount;
#endif
}

int
PlayerManager::getUndergroundJewelCount()
{
    return mUndergroundJewelCount;
}

/**
 * 占有量を返す
 */
double
PlayerManager::getFullness()
{
    return mFullness;
}

/**
 * ゴミ箱のタイプを返す
 */
int
PlayerManager::getGarbageCanType()
{
    return mGarbageCanType;
}

int
PlayerManager::getGarbageCanXlUsedCount()
{
    return mGarbageCanXlUsedCount;
}

/**
 * タイマーの開始時刻を返す
 */
double
PlayerManager::getTimerStartTime()
{
    if (mTimerStartTime == 0.0 && getFullness() > 0)
    {
        mTimerStartTime = getCurrentTime();
    }
    return mTimerStartTime;
}

// ------------------------------
// Accesser
// ------------------------------
/**
 * /user_apps/self のレスポンスをセットする
 */
void
PlayerManager::setUserAppsSelfResponse(int jewel, int point, int totalPoint)
{
    setGem(jewel);
    setPoint(point);
    setTotalPoint(totalPoint);

    if (mOnChangedGem != 0)
    {
        mOnChangedGem(getGem());
    }

    if (mOnChangedPoint != 0)
    {
        mOnChangedPoint(getPoint());
    }
}

/**
 * /gomipoi_games/load のレスポンスをセットする
 */
void
PlayerManager::setGomipoiGameLoadResponse(int level, double serverTime, int experiencePoint, int currentLevelRequiredPoint, int nextLevelRequiredPoint)
{
    setLevel(level);
    setServerTime(serverTime);
    setExperiencePoint(experiencePoint);
    setCurrentLevelRequiredPoint(currentLevelRequiredPoint);
    setNextLevelRequiredPoint(nextLevelRequiredPoint);
}

/**
 * 宝石数の変更が可能かを返す
 */
int
PlayerManager::isEnabledChangeGem(int addValue)
{
    return makeDefaultCheckResponse(getGem() + addValue, 0);
}

/**
 * ポイント数の変更が可能かを返す
 */
int
PlayerManager::isEnabledChangePoint(int addValue)
{
    return makeDefaultCheckResponse(getPoint() + addValue, MAX_POINT);
}

/**
 * 占有量の変更が可能かを返す
 */
int
PlayerManager::isEnabledChangeFullness(int addValue)
{
    return makeDefaultCheckResponse(getFullness() + addValue, getMaxCapacity());
}

/**
 * 宝石数を変更する
 */
void
PlayerManager::changeGem(int addValue)
{
    setGem(getGem() + addValue);

    if (mOnChangedGem != 0)
    {
        mOnChangedGem(getGem());
    }
}

/**
 * ポイント数を変更する
 */
void
PlayerManager::changePoint(int addValue)
{
    setPoint(getPoint() + addValue);
    if (mOnChangedPoint != 0)
    {
        mOnChangedPoint(getPoint());
    }
}

/**
 * 占有量を変更する
 */
void
PlayerManager::changeFullness(double addValue)
{
    bool isNeedSetStart = (getFullness() == 0);

    setCapacity(getFullness() + addValue, getGarbageCanType());

    if (mOnChangedFullness != 0)
    {
        mOnChangedFullness(getFullnessRate());
    }

    if (isNeedSetStart)
    {
        setTimerStartTime(getCurrentTime());
    }
}

/**
 * ゴミ箱のタイプを変更する
 */
void
PlayerManager::changeGarbageCanType(int type, int stage)
{
    setCapacity(getFullness(), type);
    
    if (type == eGarbageCanType_XL)
    {
        if (stage == eStage_PoikoRoom)
        {
            setGarbageCanXlUsedCount_Room(0);
        }
        else
        {
            setGarbageCanXlUsedCount(0);
        }
    }

    if (mOnChangedFullness != 0)
    {
        mOnChangedFullness(getFullnessRate());
    }
}

/**
 * レベルを返す
 */
int
PlayerManager::getLevel()
{
    return mLevel;
}

/**
 * 経験値を返す
 */
int
PlayerManager::getExperiencePoint()
{
    return mExperiencePoint;
}

/**
 * 現レベルの必要経験値を返す
 */
int
PlayerManager::getCurrentLevelRequiredPoint()
{
    return mCurrentLevelRequiredPoint;
}

/**
 * 次レベルの必要経験値を返す
 */
int
PlayerManager::getNextLevelRequiredPoint()
{
    return mNextLevelRequiredPoint;
}

/**
 * 最高レベルを返す
 */
int
PlayerManager::getMaxLevel()
{
    return MAX_LEVEL;
}

/**
 * サーバーの時間を返す
 */
double
PlayerManager::getServerTime()
{
    return mServerTime;
}

/**
 * ゴミ箱の占有率を返す
 */
double
PlayerManager::getFullnessRate()
{
    if (getMaxCapacity() == 0)
    {
        return 0;
    }
    return (double)getFullness() / (double)getMaxCapacity();
}

/**
 * 「業者に電話」アイテム使用時に呼ばれる
 */
void
PlayerManager::onUsedTelephoneItem()
{
    setCapacity(0, getGarbageCanType());
    if (mOnChangedFullness != 0)
    {
        mOnChangedFullness(getFullnessRate());
    }
}

/**
 * 所持アイテムのリストをセットする
 */
void
PlayerManager::setOwnItem(std::vector<Item *> items)
{
    mOwnItems = items;
}

/**
 * ひみつの使用カウント数のリストをセットする
 */
void
PlayerManager::setUsageLimitItems(std::vector<UsageLimitItem *> items)
{
    mUsageLimitItems = items;
}

/**
 * 取得済みのゴミリストをセットする
 */
void
PlayerManager::setBookGarbages(std::vector<BookGarbage *> garbages)
{
    mBookGarbages = garbages;
}

/**
 * ボーナス取得済みのページリストをセットする
 */
void
PlayerManager::setReceivedBonusPages(std::vector<int> pages)
{
    mReceivedBonusPage = pages;
}

/**
 * 取得できるボーナスが存在するかを返す
 */
std::vector<int>
PlayerManager::getPageBonus()
{
    std::vector<int> completedPages;

    // ページが埋まっているかをチェック
    int startGarbageIndex = 0;
    int garbageIndex = 0;
    bool isCompletedPage = false;
    
    int garbagePerPage = 6;
    int maxPage = (GARBAGE_COUNT - 1) / garbagePerPage + 1;
    int page = 0;
    for (page = 0; page < maxPage; page++)
    {
        startGarbageIndex = page * garbagePerPage;
        isCompletedPage = true;
        for (garbageIndex = startGarbageIndex; garbageIndex < startGarbageIndex + garbagePerPage; garbageIndex++)
        {
            if (garbageIndex >= GARBAGE_COUNT)
            {
                break;
            }

            int garbageId = bookGarbages[garbageIndex];
            
            if (!isIncludeBookGarbage(garbageId))
            {
                isCompletedPage = false;
                break;
            }

        }

        if (isCompletedPage)
        {
            completedPages.push_back(page + 1);
        }
    }

    std::vector<int> result;
    if (completedPages.size() <= 0)
    {
        return result;
    }

    int i;
    for (i = 0; i < completedPages.size(); i++)
    {
        if (find(mReceivedBonusPage.begin(), mReceivedBonusPage.end(), completedPages[i]) == mReceivedBonusPage.end())
        {
            result.push_back(completedPages[i]);
        }
    }
    return result;
}

/**
 * ページボーナス受け取り時の処理を行う
 */
void
PlayerManager::onReceivedPageBonus(std::vector<int> receivedPage)
{
    mReceivedBonusPage = receivedPage;
}

/**
 * アイテムコードからアイテムの所持数を返す
 */
int
PlayerManager::getItemOwnCount(std::string item_code)
{
    int i;
    for (i = 0; i < mOwnItems.size(); i++)
    {
        Item *item = mOwnItems[i];
        if (item != 0 && item->mItemCode == item_code)
        {
            return item->mOwnCount;
        }
    }
    return 0;
}

/**
 * アイテムコードからアイテムが使用中かを返す
 */
bool
PlayerManager::isItemOwnUsing(std::string item_code)
{
    // キャラクターだけは特別扱い
    if (item_code == ITEM_CODE_POIKO || item_code == ITEM_CODE_OTON || item_code == ITEM_CODE_KOTATSU)
    {
        return ((item_code == ITEM_CODE_POIKO && mCharacterType == eCharacter_Poiko) ||
                (item_code == ITEM_CODE_OTON && mCharacterType == eCharacter_Oton) ||
                (item_code == ITEM_CODE_KOTATSU && mCharacterType == eCharacter_Kotatsu));
    }
    
    int i;
    for (i = 0; i < mOwnItems.size(); i++)
    {
        Item *item = mOwnItems[i];
        if (item != 0 && item->mItemCode == item_code)
        {
            return item->mItemUsing;
        }
    }
    return false;
}

bool
PlayerManager::isLimitItemUsage(std::string item_code)
{
    int i;
    for (i = 0; i < mUsageLimitItems.size(); i++)
    {
        UsageLimitItem *item = mUsageLimitItems[i];
        if (item != 0 && item->mItemCode == item_code)
        {
            return true;
        }
    }
    return false;
}

void
PlayerManager::onUsedZDrink()
{
    mUsageLimitItems.push_back(new UsageLimitItem(ITEM_CODE_Z_DRINK, 1));
}

void
PlayerManager::onUsedDrop()
{
    mUsageLimitItems.push_back(new UsageLimitItem(ITEM_CODE_DROP, 1));
}

/**
 * ひみつが使用可能か否かを返す
 */
bool
PlayerManager::isValidMission(int missionType)
{
    std::string useItemCode = SecretMission::missionIdToItemCode(missionType);
    
    bool isValid = false;
    for (Item* item : mOwnItems) {
        if (item->mItemCode == useItemCode) {
            isValid = true;
            break;
        }
    }
    
    if (isValid) {
        for (UsageLimitItem* item : mUsageLimitItems) {
            if (item->mItemCode == useItemCode && item->mCount >= 1) {
                isValid = false;
                break;
            }
        }
        
        return isValid;
    }
    
    return false;
}

/**
 * ひみつを使用済に設定する
 */
void
PlayerManager::consumeMission(int missionType)
{
    std::string useItemCode = SecretMission::missionIdToItemCode(missionType);
    
    bool isUpdated = false;
    for (UsageLimitItem* item : mUsageLimitItems) {
        if (item->mItemCode == useItemCode) {
            item->mCount++;
            isUpdated = true;
            break;
        }
    }
    
    if (!isUpdated) {
        mUsageLimitItems.push_back(new UsageLimitItem(useItemCode, 1));
    }
}

/**
 * ゴミコードからロックが外れているかを返す
 */
bool
PlayerManager::isUnlocked(std::string garbage_code)
{
    int i;
    for (i = 0; i < mBookGarbages.size(); i++)
    {
        BookGarbage *garbage = mBookGarbages[i];
        if (garbage != 0 && garbage->garbage_code == garbage_code)
        {
            return true;
        }
    }
    return false;
}

/**
 * ゴミコードからNewのゴミかを返す
 */
bool
PlayerManager::isNew(std::string garbage_code)
{
    int i;
    for (i = 0; i < mBookGarbages.size(); i++)
    {
        BookGarbage *garbage = mBookGarbages[i];
        if (garbage != 0 && garbage->garbage_code == garbage_code)
        {
            return !(garbage->checked);
        }
    }
    return false;
}

bool
PlayerManager::isRareGarbage(std::string garbage_code)
{
    std::vector<int> vectorList = getRareGarbages();
    int garbageId = GarbageUtils::getGarbageId(garbage_code);
    int i;
    for (i = 0; i < vectorList.size(); i++)
    {
        int rareGarbageId = vectorList[i];
        if (garbageId == rareGarbageId)
        {
            return true;
        }
    }
    
    return false;
}

// ------------------------------
// Function
// ------------------------------
/**
 * 宝石数をセットする
 */
void
PlayerManager::setGem(int gem)
{
    if (gem < 0)
    {
        gem = 0;
    }
    mGem = gem;
}

/**
 * ポイント数をセットする
 */
void
PlayerManager::setPoint(int point)
{
    if (point < 0)
    {
        point = 0;
    }
    else if (point > MAX_POINT)
    {
        point = MAX_POINT;
    }
    mPoint = point;
}

/**
 * 獲得したポイントの総数をセットする
 */
void
PlayerManager::setTotalPoint(int totalPoint)
{
    if (totalPoint < 0)
    {
        totalPoint = 0;
    }
    mTotalPoint = totalPoint;
}

/**
 * 現在のレベルをセットする
 */
void
PlayerManager::setLevel(int level)
{
    if (level < 0)
    {
        level = 0;
    }
    else if (level > MAX_LEVEL)
    {
        level = MAX_LEVEL;
    }
    mLevel = level;
}

/**
 * 現在の経験値をセットする
*/
void
PlayerManager::setExperiencePoint(int experiencePoint)
{
    if (experiencePoint < 0) {
        experiencePoint = 0;
    }
    mExperiencePoint = experiencePoint;
}

/**
 * 現レベルの必要経験値をセットする
 */
void
PlayerManager::setCurrentLevelRequiredPoint(int currentLevelRequiredPoint)
{
    if (currentLevelRequiredPoint < 0) {
        currentLevelRequiredPoint = 0;
    }
    mCurrentLevelRequiredPoint = currentLevelRequiredPoint;
}

/**
 * 次レベルの必要経験値をセットする
 */
void
PlayerManager::setNextLevelRequiredPoint(int nextLevelRequiredPoint)
{
    if (nextLevelRequiredPoint < 0) {
        nextLevelRequiredPoint = 0;
    }
    mNextLevelRequiredPoint = nextLevelRequiredPoint;
}

/**
 * サーバー時間をセットする
 */
void
PlayerManager::setServerTime(double time)
{
    if (time < 0)
    {
        time = 0;
    }
    mServerTime = time;
    mServerStartTime = TimeUtils::getTime();
}

double
PlayerManager::getCurrentTime()
{
    double diff = TimeUtils::getTime() - mServerStartTime;
    return getServerTime() + diff;
}

/**
 * ゴミ箱の占有量とタイプをセットする
 */
void
PlayerManager::setCapacity(double fullness, int garbageCanType)
{
    if (garbageCanType < eGarbageCanType_Normal || garbageCanType > eGarbageCanType_XL)
    {
        garbageCanType = eGarbageCanType_Normal;
    }
    mGarbageCanType = garbageCanType;
    
    if (fullness < 0)
    {
        fullness = 0;
    }
    else if (fullness > getMaxCapacity())
    {
        fullness = (double)getMaxCapacity();
    }
    mFullness = fullness;

    setTimerStartTime(getCurrentTime());

    if (mOnChangedFullness != 0)
    {
        mOnChangedFullness(getFullnessRate());
    }
}

void
PlayerManager::setGarbageCanXlUsedCount(int count)
{
    mGarbageCanXlUsedCount = count;
}

void
PlayerManager::setGarbageCanXlUsedCount_Room(int count)
{
    mGarbageCanXlUsedCount_Room = count;
}

bool
PlayerManager::addGarbageCanXlUsedCount(int addValue, int stage)
{
    if (getGarbageCanType() != eGarbageCanType_XL)
    {
        return false;
    }
    
    if (stage == eStage_PoikoRoom)
    {
        mGarbageCanXlUsedCount_Room += addValue;
        return (mGarbageCanXlUsedCount_Room >= GARBAGE_CAN_XL_BROKEN_CAPACITY);
    }

    mGarbageCanXlUsedCount += addValue;
    return (mGarbageCanXlUsedCount >= GARBAGE_CAN_XL_BROKEN_CAPACITY);
}

/**
 * タイマーの開始時刻をセットする
 */
void
PlayerManager::setTimerStartTime(double startTime)
{
    if (startTime < 0)
    {
        startTime = 0;
    }

    if (getFullness() <= 0)
    {
        startTime = 0.0;
    }

    mTimerStartTime = startTime;
}

// ゴミの最大容量の調整
/**
 * ゴミ箱の最大容量を返す
 */
int
PlayerManager::getMaxCapacity()
{
    int multipleRate = 1;
    switch (mGarbageCanType)
    {
        case eGarbageCanType_Big:
            return 90;

        case eGarbageCanType_Huge:
            return 200;
            
        case eGarbageCanType_XL:
            return 300;
    }

    return DEFAULT_MAX_CAPACITY;
}

/**
 * 汎用的なチェック処理のレスポンスを作成する
 */
int
PlayerManager::makeDefaultCheckResponse(int value, int maxValue)
{
    if (value < 0)
    {
        return eChangeCeckCode_Low;
    }

    if (maxValue > 0 && value > maxValue)
    {
        return eChangeCeckCode_High;
    }

    return eChangeCeckCode_OK;
}

/**
 * BookGarbagesにgarbageIdが含まれているかを返す
 */
bool
PlayerManager::isIncludeBookGarbage(int garbageId)
{
    int i;
    for (i = 0; i < mBookGarbages.size(); i++)
    {
        BookGarbage *garbage = mBookGarbages[i];
        if (garbage != 0 && garbage->garbage_code == GarbageUtils::getGarbageCode(garbageId))
        {
            return true;
        }
    }
    return false;
}

/**
 * BookGarbagesにgarbageIdが含まれているかを返す
 */
std::vector<int>
PlayerManager::getBookGarbages()
{
    return bookGarbages;
}

std::vector<int>
PlayerManager::getRareGarbages()
{
    return rareGarbages;
}

void
PlayerManager::setCharacter(int type)
{
    mCharacterType = type;
}

int
PlayerManager::getCharacter()
{
    return mCharacterType;
}

void
PlayerManager::setFriendCount(int count)
{
    mFriendCount = count;
}

int
PlayerManager::getFriendCount()
{
    return mFriendCount;
}

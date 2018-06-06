//
//
//
#include "GameManager.h"
#include "../model/missions/SecretMission.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * 現在のステージを返す
 */
int
GameManager::getCurrentStage()
{
    return mCurrentStage;
}

/**
 * 現在のステージをセットする
 */
void
GameManager::setCurrentStage(int stage)
{
    mCurrentStage = stage;
}

/**
 * 現在のステージをのひみつかどうかを返す
 */
bool
GameManager::isCurrentSecret(int secretMissionId)
{
    //ジロキチの隠れ家にいたら、ひみつが全部無効
    if (isUndergroundStage())
        return false;
    
    switch (secretMissionId) {
        case eSecretMission_DefaultTrashCan:
            return mCurrentStage == eStage_Default;
            
        case eSecretMission_DefaultTV:
            return mCurrentStage == eStage_Default;
            
        case eSecretMission_DefaultRocket:
            return mCurrentStage == eStage_Default;
            
        case eSecretMission_PoikoHeart:
            return mCurrentStage == eStage_PoikoRoom;
            
        case eSecretMission_PoikoLamp:
            return mCurrentStage == eStage_PoikoRoom;
            
        case eSecretMission_PoikoMouseHole:
            return mCurrentStage == eStage_PoikoRoom;
            
        default:
            return false;
    }
}

/**
 * 封印使用の開始時刻を返す
 */
double
GameManager::getSealStartTime()
{
    return mSealStartTime;
}

double
GameManager::getBatteryStartTime()
{
    return mBatteryStartTime;
}

/**
 * ボーナスタイムの開始時刻を返す
 */
double
GameManager::getBonusStartTime()
{
    return mBonusStartTime;
}

/**
 * 残りボーナスタイムを返す
 */
double
GameManager::getRemainingBonusTime()
{
    if (mGetPlayerManager() != NULL)
        return BONUS_INTERVAL - (mGetPlayerManager()->getCurrentTime() - mBonusStartTime);
    else return -1;
}

/**
 * Zドリンクによるボーナスタイムの開始時刻を返す
 */
double
GameManager::getZDrinkStartTime()
{
    return mZDrinkStartTime;
}

/**
 * Zドリンクの残りボーナスタイムを返す
 */
double
GameManager::getRemainingZDrinkTime()
{
    if (mGetPlayerManager() != NULL)
        return BONUS_INTERVAL - (mGetPlayerManager()->getCurrentTime() - mZDrinkStartTime);
    else return -1;
}

/**
 * 変身ドロップの開始時刻を返す
 */
double
GameManager::getDropStartTime()
{
    return mDropStartTime;
}

/**
 * 変身ドロップの残り時間を返す
 */
double
GameManager::getRemainingDropTime()
{
    if (mGetPlayerManager() != NULL)
        return BONUS_INTERVAL - (mGetPlayerManager()->getCurrentTime() - mDropStartTime);
    else return -1;
}

/**
 * ゴミ追加の開始時刻を返す
 */
double
GameManager::getAddGarbageStartTime()
{
    return mAddGarbageStartTime[getCurrentStage()];
}

/**
 * 現在の箒のタイプを返す
 */
int
GameManager::getBroomType()
{
    return mBroomType;
}

/**
 * 現在の箒の残り使用数を返す
 */
int
GameManager::getBroomSweepCount()
{
    return mBroomSweepCount;
}

/**
 * タイマー処理を行う
 */
void
GameManager::onCheckTime(int isForegroundFirst)
{
    if (!mIsActive())
    {
        return;
    }

    double currentTime = mGetPlayerManager()->getCurrentTime();

    // ゴミ箱の内容量チェック
    if (mGetPlayerManager != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0 && playerManager->getFullness() > 0 && playerManager->getTimerStartTime() > 0.0)
        {
            double dif = currentTime - playerManager->getTimerStartTime();
            if (dif > 0)
            {
                int maxCapacity = playerManager->getMaxCapacity();
                double consuptionCount = maxCapacity * CONSUMPTION_RATE;
                
                if (isBonusTime() || isZDrinkTime())
                {
                    int count = (int)(dif / CONSUMPTION_INTERVAL_FOR_BONUS);
                    double addValue =  count * consuptionCount;
                    double restTime = dif - ((double) count * CONSUMPTION_INTERVAL_FOR_BONUS);
                    if (addValue >= 1 && playerManager->isEnabledChangeFullness(-addValue) != eChangeCeckCode_High)
                    {
                        onSaveData(0, -addValue, false, false, false);

                        // ゴミ箱の占有率と次回用の開始時刻を変更する
                        playerManager->changeFullness(-addValue);
                        playerManager->setTimerStartTime(currentTime - restTime);
                    }
                }
                else {
                    int count = (int)(dif / CONSUMPTION_INTERVAL);
                    double addValue = count * consuptionCount;
                    double restTime = dif - ((double) count * CONSUMPTION_INTERVAL);
                    if (addValue >= 1 && playerManager->isEnabledChangeFullness(-addValue) != eChangeCeckCode_High)
                    {
                        // ゴミ箱の占有率と次回用の開始時刻を変更する
                        playerManager->changeFullness(-addValue);
                        playerManager->setTimerStartTime(currentTime - restTime);
                    }
                }
            }
            else
            {
                playerManager->setTimerStartTime(currentTime);
            }
        }
    }

    // スタックしていたSaveDataを送信する
    if (mSaveDataWaitStartTime > 0.0 && mSaveDataQueue.size() > 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        double dif = currentTime - mSaveDataWaitStartTime;
        if (dif < 0)
        {
            mSaveDataWaitStartTime = playerManager->getCurrentTime();
        }
        else if (dif >= SAVE_DATA_INTERVAL)
        {
            if (mRequestSaveData != 0)
            {
                mRequestSaveData();
            }

            if (mSaveDataQueue.size() > 0)
            {
                mSaveDataWaitStartTime = playerManager->getCurrentTime();
            }
            else
            {
                mSaveDataWaitStartTime = 0.0;
            }
        }

    }

    // 封印の継続時間チェック
    if (isSealing()
        && (currentTime - mSealStartTime) >= SEAL_INTERVAL)
    {
        finishedSeal();
    }
    
    if (isUsedBattery()
        && (currentTime - mBatteryStartTime) >= BATTERY_INTERVAL)
    {
        finishedBattery();
    }

    // ドリンク表示の継続時間チェック
    if (isShowHeroDrink())
    {
        if ((currentTime - getHeroDrinkAppearTime()) >= HERO_DRINK_APPEAR_INTERVAL)
        {
            finishedHeroDrinkAppear();
        }
        else if (currentTime - getHeroDrinkAppearTime() < 0)
        {
            finishedHeroDrinkAppear();
        }
    }

    // ボーナスタイムの継続時間チェック
    if (isBonusTime())
    {
        if((currentTime - mBonusStartTime) >= BONUS_INTERVAL)
        {
            finishedBonusTime();
        }
        else if (currentTime - mBonusStartTime < 0)
        {
            finishedBonusTime();
        }
    }
    
    // Zドリンクによるボーナスタイムの継続時間チェック
    if (isZDrinkTime())
    {
        if((currentTime - mZDrinkStartTime) >= BONUS_INTERVAL)
        {
            finishedZDrinkTime();
        }
        else if (currentTime - mZDrinkStartTime < 0)
        {
            finishedZDrinkTime();
        }
    }
    // 変身ドロップの継続時間チェック
    if (isDropTime())
    {
        if((currentTime - mDropStartTime) >= DROP_INTERVAL)
        {
            finishedDropTime();
        }
        else if (currentTime - mDropStartTime < 0)
        {
            finishedDropTime();
        }
    }

    //穴の出現チェック
    if (canShowHole())
    {
        if (getAddHoleStartTime() <= 0.0)
        {
            setAddHoleStartTime(currentTime);
            addHole();
        }
    }
    
    // ゴミの発生をチェック
    if (getAddGarbageStartTime() <= 0.0)
    {
        setAddGarbageStartTime(currentTime);
    }
    
    if (currentTime - getAddGarbageStartTime() < 0)
    {
        setAddGarbageStartTime(currentTime);
    }
    
    double addGarbageInterval = isBonusTime() || isZDrinkTime() ? ADD_GARBAGE_INTERVAL_FOR_BONUS : ADD_GARBAGE_INTERVAL;

    if (!isShowHole() && !isUndergroundStage() &&
        getSwipeState() == eSwipeState_None &&
        (currentTime - getAddGarbageStartTime()) >= addGarbageInterval)
    {
        int count = (int)((currentTime - getAddGarbageStartTime()) / addGarbageInterval);
        double rest = (currentTime - getAddGarbageStartTime()) - addGarbageInterval * (double)count;

        // ゴミの発生を通知し、次回用の開始時刻を変更する
        addGarbage(count, isForegroundFirst);
        setAddGarbageStartTime(currentTime - rest);
    }
    
}

/**
 * ローカル保存のデータをセットする
 */
void
GameManager::setInitGameInfo(double bonusStartTime, double addGarbageStartTime, double heroDrinkAppearTime, double zDrinkStartTime, double dropStartTime)
{
    setBonusStartTime(bonusStartTime);
    setAddGarbageStartTime(addGarbageStartTime);
    setHeroDrinkAppearTime(heroDrinkAppearTime);
    setZDrinkStartTime(zDrinkStartTime);
    setDropStartTime(dropStartTime);
}

/**
 * /gomipoi_games/load のレスポンスをセットする
 */
void
GameManager::setGomipoiGameLoadResponse(std::string garbages)
{
    int i;
    if (mGarbageList.size() > 0)
    {
        for (i = (int)mGarbageList.size() - 1; i >= 0; i--)
        {
            if (mGarbageList[i] != 0)
            {
                delete mGarbageList[i];
                mGarbageList[i] = 0;
            }
            mGarbageList.erase(mGarbageList.begin() + i);
        }
    }
    
    mSwipeState = eSwipeState_OnFinished;

    JsonUtils::parseGarbageJson(garbages, &mGarbageList);
    if (mGarbageList.size() > 0 && mGarbageList[mGarbageList.size() - 1]->mGarbageId == -1)
    {
        GarbageData *drink = mGarbageList[mGarbageList.size() - 1];
        if (drink != 0)
        {
            mHeroDrinkLeft = drink->getInitLeft();
            mHeroDrinkTop = drink->getInitTop();
            mGarbageList.erase(mGarbageList.begin() + (mGarbageList.size() - 1));
        }
    }
}

/**
 * 封印アイテムの使用中かを返す
 */
bool
GameManager::isSealing()
{
    return mSealStartTime > 0.0;
}

bool
GameManager::isUsedBattery()
{
    return mBatteryStartTime > 0.0;
}

/**
 * ボーナスタイム中かを返す
 */
bool
GameManager::isBonusTime()
{
    return mBonusStartTime > 0.0;
}

/**
 * Zドリンクによるボーナスタイム中かを返す
 */
bool
GameManager::isZDrinkTime()
{
    return mZDrinkStartTime > 0.0;
}

/**
 * 変身ドロップ使用中かを返す
 */
bool
GameManager::isDropTime()
{
    return mDropStartTime > 0.0;
}

/**
 * 封印アイテムの使用時に呼ばれる
 */
bool
GameManager::onUsedSealItem()
{
    if (isSealing())
    {
        return false;
    }

    startSeal();
    return true;
}

bool
GameManager::onUsedBattery()
{
    if (isUsedBattery())
    {
        return false;
    }
    
    startBattery();
    return true;
}

/**
 * 箒が変更された時に呼ばれる
 */
void
GameManager::onChangeBroomType(int type)
{
    // 変更がなければ、無視
    if (type == getBroomType()) {
        return;
    }

    switch (type)
    {
        case eBroomType_Silver:
        {
            setBroom(eBroomType_Silver, 0);
            break;
        }

        case eBroomType_Gold:
        {
            setBroom(eBroomType_Gold, 0);
            break;
        }

        default:
        {
            setBroom(eBroomType_Normal, 0);
            break;
        }
    }
}

/**
 * 母親イベントを発生させるかをチェックする
 */
bool
GameManager::onCheckMother()
{
    // 封印シール使用中、もしくはボーナスタイム中は発生させない
    if (isSealing() || isBonusTime() || isZDrinkTime() || isUsedBattery())
    {
        return false;
    }
    
    // ゴミがなければ発生させない
    if (mGarbageList.size() <= 0)
    {
        return false;
    }

    int random = RandomUtils::getRandomNumber(MOTHER_RATE - 1);
    bool result = (random == 0);
    if (result)
    {
        PlayerManager *player = mGetPlayerManager();
        setAddGarbageStartTime(player->getCurrentTime());

        // ゴミを全削除
        int i;
        if (mGarbageList.size() > 0)
        {
            for (i = (int)mGarbageList.size() - 1; i >= 0; i--)
            {
                if (mGarbageList[i] != 0)
                {
                    delete mGarbageList[i];
                    mGarbageList[i] = 0;
                }
                mGarbageList.erase(mGarbageList.begin() + i);
            }
        }
    }

    return result;
}

/**
 * ヒーロードリンクの表示開始時刻を返す
 */
double
GameManager::getHeroDrinkAppearTime()
{
    if (mHeroDrinkAppearTime < 0.0)
    {
        mHeroDrinkAppearTime = 0.0;
    }
    return mHeroDrinkAppearTime;
}

/**
 * ヒーロードリンクが表示されているかを返す
 */
bool
GameManager::isShowHeroDrink()
{
    return (getHeroDrinkAppearTime() != 0.0 && getHeroDrinkLeft() > 0 && getHeroDrinkTop() > 0);
}

/**
 * ヒーロードリンクを表示するLeftOffsetを返す
 */
double
GameManager::getHeroDrinkLeft()
{
    return mHeroDrinkLeft;
}

/**
 * ヒーロードリンクを表示するTopOffsetを返す
 */
double
GameManager::getHeroDrinkTop()
{
    return mHeroDrinkTop;
}

/**
 * ボーナスタイムアイテムの使用時に呼ばれる
 */
bool
GameManager::onUsedBonusTimeItem()
{
    // ヒーロードリンク、Zドリンク、変身ドロップとの併用は不可
    if (isBonusTime() || isZDrinkTime() || isDropTime())
    {
        return false;
    }

    finishedHeroDrinkAppear();
    startBonusTime();
    return true;
}

/**
 * Zドリンクによるボーナスタイムアイテムの使用時に呼ばれる
 */
bool
GameManager::onUsedZDrinkItem()
{
    // ヒーロードリンク、Zドリンク、変身ドロップとの併用は不可
    if (isBonusTime() || isZDrinkTime() || isDropTime())
    {
        return false;
    }
    
    startZDrinkTime();
    return true;
}

/**
 * 変身ドロップ使用時に呼ばれる
 */
bool
GameManager::onUsedDropItem()
{
    // ヒーロードリンク、Zドリンク、変身ドロップとの併用は不可
    if (isBonusTime() || isZDrinkTime() || isDropTime())
    {
        return false;
    }
    
    // 既に出ているゴミの種類を変更
    transformGarbageByDrop();
    
    startDropTime();
    return true;
}

/**
 * 穴出現の開始時刻を返す
 */
double
GameManager::getAddHoleStartTime()
{
    return mAddHoleStartTime;
}

/**
 * 穴の表示状態を返す
 */
bool 
GameManager::isShowHole()
{
#if TEST_JIROKICHI
    return (mShowHoleStartTime >= 0.0 && getAddHoleStartTime() != 0.0 && getHoleLeft() > 0 && getHoleTop() > 0 && !mIsUndergroundStage);
#else
    PlayerManager *playerManager = mGetPlayerManager();
    if (playerManager != NULL) {
        return (getAddHoleStartTime() != 0.0 && getHoleLeft() > 0 && getHoleTop() > 0 && !mIsUndergroundStage && playerManager->canEnterUnderground());
    }
    else return false;
#endif
}

/**
 * 穴の左座標
 */
double 
GameManager::getHoleLeft()
{
    return mHoleLeft;
}

/**
 * 穴の上座標
 */
double 
GameManager::getHoleTop()
{
    return mHoleTop;
}

/**
 * ジロキチの隠れ家に入ったかどうかを返す
 */
bool
GameManager::isUndergroundStage()
{
    return mIsUndergroundStage;
}

/**
 * ジロキチの隠れ家に入ったかどうかをセットする
 */
void
GameManager::setUndergroundStage(bool isUndergroundStage)
{
    mIsUndergroundStage = isUndergroundStage;
    
    if (!isUndergroundStage) {
        //宝石をリセットする
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0)
        {
            playerManager->setUndergroundJewelCount(0);
        }
    }
    
#if TEST_JIROKICHI
    if (isUndergroundStage) {
        mShowHoleStartTime = -2;
    }
#endif
}

/**
 * ジロキチの隠れ家ステージをクリアしたかどうかを返す
 */
bool
GameManager::checkUndergroundStageEnd()
{
    return mGemList.size() == 0;
}

/**
 * 途中に隠れ家から出ると、通常のステージに戻す
 */
void
GameManager::resetUndergroundStage()
{
    mIsUndergroundStage = false;
    int i;
    for (i = (int) mGemList.size() - 1; i >= 0; i--)
    {
        GemData *data = mGemList[i];
        mGemList.erase(mGemList.begin() + i);
        delete data;
    }
    mGemList.clear();
}

/**
 * ジロキチの隠れ家の宝石を生成する
 */
void
GameManager::makeGemList()
{
    int i;
    for (i = (int) mGemList.size() - 1; i >= 0; i--)
    {
        GemData *data = mGemList[i];
        mGemList.erase(mGemList.begin() + i);
        delete data;
    }
    mGemList.clear();
    
    int gemCount = 0;
    if (mGetPlayerManager != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0)
        {
            gemCount = playerManager->getUndergroundJewelCount();
        }
    }
    
    for (i = 0; i < gemCount; i++)
    {
        mGemList.push_back(GemData::makeGemData());
    }
    
    // 遠近法にならって、リストの並び替え
    std::sort(
              mGemList.begin(),
              mGemList.end(),
              [&](GemData *left, GemData *right)
              {
                  if (left == 0 || left == NULL
                      || right == 0 || right == NULL)
                  {
                      return true;
                  }
                  
                  double leftBottom = left->getCurrentTop() + left->getHeight();
                  double rightBottom = right->getCurrentTop() + right->getHeight();
                  
                  return leftBottom < rightBottom;
              });
    
    if (mRequestRefreshGarbage != 0)
    {
        LOGI("#10 : eSwipeState_OnRefreshed");
        changeSwipeState(eSwipeState_OnRefreshed);
        mRequestRefreshGarbage();
    }
}

/**
 * ジロキチの隠れ家の宝石を返す
 */
std::vector<GemData *>
GameManager::getGemList()
{
    std::vector<GemData *> tmpList = mGemList;
    std::vector<GemData *> list;
    
    int i;
    for (i = 0; i < tmpList.size(); i++)
    {
        list.push_back(tmpList[i]->clone());
    }
    return list;
}

void
GameManager::setGemList(std::vector<GemData *> list)
{
    mGemList.clear();
    
    int size = (int) list.size();
    int i;
    for (i = 0; i < size; i++) {
        GemData* data = list.at(i);
        mGemList.push_back(data);
    }
}

/**
 * ゴミ情報のJson文字列を返す
 */
std::string
GameManager::getGarbagesJson()
{
    std::ostringstream stream;
    stream.precision(15);
    stream << "[";

    std::vector<GarbageData *> list = getGarbageList();
    int i;
    for (i = 0; i < list.size(); i++)
    {
        GarbageData *garbage = list[i];
        if (garbage == 0)
        {
            continue;
        }

        stream << JsonUtils::makeGarbageJson(garbage->mGarbageId, garbage->getRestLife(), garbage->getInitLeft(), garbage->getInitTop());
        if (i != list.size() - 1)
        {
            stream << ",";
        }
    }

    if (isShowHeroDrink())
    {
        stream << ",";
        stream << JsonUtils::makeHeroDrinkJson(mHeroDrinkLeft, mHeroDrinkTop);
    }

    stream << "]";
    return stream.str();
}

/**
 * GarbageDataのリストをcloneしてから返す
 * (同時操作を防ぐためにcloneする)
 */
std::vector<GarbageData *>
GameManager::getGarbageList()
{
    std::vector<GarbageData *> tmpList = mGarbageList;
    std::vector<GarbageData *> list;

    int i;
    for (i = 0; i < tmpList.size(); i++)
    {
        list.push_back(tmpList[i]->clone());
    }
    return list;
}

/**
 * 変身ドロップによって、既にあるゴミのリストを変身する
 */
void
GameManager::transformGarbageByDrop()
{
    std::vector<GarbageData *> tmpList = mGarbageList;
    std::vector<GarbageData *> list;
    
    int i;
    for (i = 0; i < tmpList.size(); i++)
    {
        GarbageData *garbage = tmpList[i]->clone();
        garbage->changeGarbageId(getMaxRareGarbageId());
        list.push_back(garbage);
    }
    
    // ゴミを全削除してから、入れ直す
    if (mGarbageList.size() > 0)
    {
        for (i = (int)mGarbageList.size() - 1; i >= 0; i--)
        {
            if (mGarbageList[i] != 0)
            {
                delete mGarbageList[i];
                mGarbageList[i] = 0;
            }
            mGarbageList.erase(mGarbageList.begin() + i);
        }
    }
    for (i = 0; i < list.size(); i++)
    {
        mGarbageList.push_back(list[i]->clone());
    }
}

/**
 * スワイプイベントの状態を変更する
 */
void
GameManager::changeSwipeState(int state)
{
    if (state == eSwipeState_OnFinished)
    {
        // 箒が壊れていないかチェック
        bool isBrokenBroom = false;
        if (getBroomType() != eBroomType_Normal)
        {
            int newSweepCount = getBroomSweepCount() + 1;
            if (newSweepCount >= (getBroomType() == eBroomType_Gold
                                  ? BROOM_MAX_SWEEP_COUNT_GOLD
                                  : BROOM_MAX_SWEEP_COUNT_SILVER))
            {
                isBrokenBroom = true;
                // コールバック
                if (mBrokenBroom != 0)
                {
                    mBrokenBroom(getBroomType());
                }
                setBroom(eBroomType_Normal, 0);

                LOGI("mSwipeState#1 = %d", state);
            }
            else
            {
                setBroom(getBroomType(), newSweepCount);
            }
        }
        
        // ゴミ箱XLが壊れていないかをチェック
        if (mGetPlayerManager()->getGarbageCanType() == eGarbageCanType_XL
            && mGetPlayerManager()->addGarbageCanXlUsedCount(0, getCurrentStage()))
        {
            // コールバック
            if (mBrokenGarbageCanXl != 0)
            {
                mBrokenGarbageCanXl();
            }
            mGetPlayerManager()->setCapacity(mGetPlayerManager()->getFullness(), eGarbageCanType_Huge);
        }
    }

    LOGI("mSwipeState#2 = %d", state);
    mSwipeState = state;
}

/**
 * サーバーにデータを保存する
 */
void
GameManager::onSaveData(int totalPoint, int completeCount, bool isSweep, bool isBrokenBroom, bool isGarbageCanBroken)
{
    mSaveDataQueue.push_back(new SaveData(totalPoint, completeCount, isSweep ? 1 : 0, isBrokenBroom, isGarbageCanBroken));
    if (mSaveDataWaitStartTime == 0.0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        mSaveDataWaitStartTime = playerManager->getCurrentTime();
    }
}

/**
 * セーブデータのリストを返す
 */
std::vector<SaveData *>
GameManager::getSaveData()
{
    std::vector<SaveData *> retList = std::vector<SaveData *>();
    int i;
    for (i = mSaveDataQueue.size() - 1; i >= 0; i--)
    {
        retList.push_back(mSaveDataQueue[i]->cloneData());

        delete mSaveDataQueue[i];
        mSaveDataQueue.erase(mSaveDataQueue.begin() + i);
    }
    mSaveDataWaitStartTime = 0.0;
    return retList;
}

/**
 * 新規発見のゴミリストのカンマ区切り文字列を返す
 */
std::string
GameManager::getNewFoundGarbages()
{
    if (mNewFoundGarbages.size() == 0)
    {
        return "";
    }

    std::string idListText;
    int i;
    int startIndex = (int) mNewFoundGarbages.size() - 1;
    for (i = startIndex; i >= 0; i--)
    {
        if (i < startIndex)
        {
            idListText += ",";
        }
        idListText += GarbageUtils::getGarbageCode(mNewFoundGarbages[i]);

        // 消す
        mNewFoundGarbages.erase(mNewFoundGarbages.begin() + i);
    }

    return idListText;
}

/**
 * 現在のスワイプイベントの状態を返す
 */
int
GameManager::getSwipeState()
{
    return mSwipeState;
}

/**
 * 箒を掃いた時に得られる情報をアニメーションより先回りして算出する
 */
void
GameManager::onSweep(double startTouchX, double startTouchY, double screenWidth, double screenHeight, bool isAuto)
{
    // データの更新
    int totalBonus = 0;
    int comboCount = 0;
    int targetCount = 0;
    int exclusionCount = 0;
    int broomType = isAuto ? eBroomType_Normal : getBroomType();

    if (isUndergroundStage())
    {
        int i;
        for (i = (int)mGemList.size() - 1; i >= 0; i--)
        {
            GemData *gem = mGemList[i];
            
            if (gem == 0)
            {
                continue;
            }
            
            if (gem->isCompleted())
            {
                if (mGemList[i] != 0)
                {
                    delete mGemList[i];
                    mGemList[i] = 0;
                }
                mGemList.erase(mGemList.begin() + i);
                continue;
            }
            
            mGemList[i]->checkHit(startTouchX, startTouchY, screenWidth, screenHeight);
            if (!mGemList[i]->isSwipeTarget())
            {
                continue;
            }
            
            // 容量を考慮する
            if (mGetPlayerManager != 0 && mGetPlayerManager() != 0)
            {
                int restChangeCount = 1;
                switch (isAuto ? eBroomType_Normal : getBroomType())
                {
                    case eBroomType_Gold: {
                        restChangeCount = 5;
                        break;
                    }
                        
                    case eBroomType_Silver: {
                        restChangeCount = 2;
                        break;
                    }
                }
                
                bool isComplete = (gem->getRestLife() <= restChangeCount);
                if (isComplete)
                {
                    comboCount += 1;
                }
            }
            
            targetCount += 1;
            gem->onSweep(broomType, startTouchX, startTouchY, screenWidth, screenHeight);
            if (gem->isCompleted())
            {
                totalBonus += gem->getBonus();
                
                if (mGemList[i] != 0)
                {
                    delete mGemList[i];
                    mGemList[i] = 0;
                }
                mGemList.erase(mGemList.begin() + i);
                continue;
            }
            
        }
        
        // 遠近法にならって、リストの並び替え
        std::sort(
                  mGemList.begin(),
                  mGemList.end(),
                  [&](GemData *left, GemData *right)
                  {
                      if (left == 0 || left == NULL
                          || right == 0 || right == NULL)
                      {
                          return true;
                      }
                      
                      double leftBottom = left->getCurrentTop() + left->getHeight();
                      double rightBottom = right->getCurrentTop() + right->getHeight();
                      
                      return leftBottom < rightBottom;
                  });
        
        // 箒の判定
        bool isBrokenBroom = false;
        if ((isAuto ? eBroomType_Normal : getBroomType()) != eBroomType_Normal && targetCount > 0)
        {
            int newSweepCount = getBroomSweepCount() + 1;
            if (newSweepCount >= (getBroomType() == eBroomType_Gold
                                  ? BROOM_MAX_SWEEP_COUNT_GOLD
                                  : BROOM_MAX_SWEEP_COUNT_SILVER))
            {
                isBrokenBroom = true;
            }
        }
        
        // Save
//        onSaveData(totalBonus, comboCount, targetCount > 0, isBrokenBroom);
    }
    else
    {
        std::vector<int> garbageIdList;
        
        int i;
        for (i = (int)mGarbageList.size() - 1; i >= 0; i--)
        {
            GarbageData *garbage = mGarbageList[i];
            
            if (garbage == 0)
            {
                continue;
            }
            
            if (garbage->isCompleted())
            {
                if (find(garbageIdList.begin(),garbageIdList.end(),garbage->mGarbageId) == garbageIdList.end())
                {
                    garbageIdList.push_back(garbage->mGarbageId);
                }
                
                if (mGarbageList[i] != 0)
                {
                    delete mGarbageList[i];
                    mGarbageList[i] = 0;
                }
                mGarbageList.erase(mGarbageList.begin() + i);
                continue;
            }
            
            mGarbageList[i]->checkHit(startTouchX, startTouchY, screenWidth, screenHeight);
            if (!mGarbageList[i]->isSwipeTarget())
            {
                continue;
            }
            
            // 容量を考慮する
            if (mGetPlayerManager != 0 && mGetPlayerManager() != 0)
            {
                PlayerManager *playerManager = mGetPlayerManager();
                int restChangeCount = 1;
                switch (isAuto ? eBroomType_Normal : getBroomType())
                {
                    case eBroomType_Gold: {
                        restChangeCount = 5;
                        break;
                    }
                        
                    case eBroomType_Silver: {
                        restChangeCount = 2;
                        break;
                    }
                }
                
                bool isComplete = (garbage->getRestLife() <= restChangeCount);
                if (isComplete)
                {
                    comboCount += 1;
                    if (playerManager->isEnabledChangeFullness(comboCount) != eChangeCeckCode_OK)
                    {
                        exclusionCount += 1;
                        comboCount -= 1;
                        continue;
                    }
                }
            }
            
            targetCount += 1;
            garbage->onSweep(broomType, startTouchX, startTouchY, screenWidth, screenHeight);
            if (garbage->isCompleted())
            {
                if (find(garbageIdList.begin(),garbageIdList.end(),garbage->mGarbageId) == garbageIdList.end())
                {
                    garbageIdList.push_back(garbage->mGarbageId);
                }
                
                totalBonus += getBonus(garbage->getBonus(), isAuto, garbage->isSp());
                
                if (mGarbageList[i] != 0)
                {
                    delete mGarbageList[i];
                    mGarbageList[i] = 0;
                }
                mGarbageList.erase(mGarbageList.begin() + i);
                continue;
            }
            
        }
        
        // 遠近法にならって、リストの並び替え
        std::sort(
                  mGarbageList.begin(),
                  mGarbageList.end(),
                  [&](GarbageData *left, GarbageData *right)
                  {
                      if (left == 0 || left == NULL
                          || right == 0 || right == NULL)
                      {
                          return true;
                      }
                      
                      int leftSp = left->isSp() ? 1 : 0;
                      int rightSp = right->isSp() ? 1 : 0;
                      if (leftSp != rightSp)
                      {
                          return leftSp < rightSp;
                      }
                      
                      double leftBottom = left->getCurrentTop() + left->getHeight();
                      double rightBottom = right->getCurrentTop() + right->getHeight();
                      
                      return leftBottom < rightBottom;
                  });
        
        
        // ゴミ箱満タン判定
        if (exclusionCount > 0)
        {
            // AppManagerに通知
            if (mExistExclusionGarbage != 0)
            {
                mExistExclusionGarbage();
            }
        }
        
        // コンボボーナス判定
        if (comboCount >= 10)
        {
            totalBonus += (int)(comboCount / 10) * 10;
        }
        
        // 箒の判定
        bool isBrokenBroom = false;
        if ((isAuto ? eBroomType_Normal : getBroomType()) != eBroomType_Normal && targetCount > 0)
        {
            int newSweepCount = getBroomSweepCount() + 1;
            if (newSweepCount >= (getBroomType() == eBroomType_Gold
                                  ? BROOM_MAX_SWEEP_COUNT_GOLD
                                  : BROOM_MAX_SWEEP_COUNT_SILVER))
            {
                isBrokenBroom = true;
            }
        }
        
        // 新しいゴミを取得した判定
        PlayerManager *playerManager = mGetPlayerManager();
        for (i = 0; i < garbageIdList.size(); i++)
        {
            if (playerManager->isUnlocked(GarbageUtils::getGarbageCode(garbageIdList[i])))
            {
                continue;
            }
            
            // 重複チェック
            if (find(mNewFoundGarbages.begin(),mNewFoundGarbages.end(),garbageIdList[i]) == mNewFoundGarbages.end())
            {
                mNewFoundGarbages.push_back(garbageIdList[i]);
            }
        }
        
        // ゴミ箱XLの判定
        bool isBrokenGarbageCanXl = playerManager->addGarbageCanXlUsedCount(comboCount, getCurrentStage());
        
        // Save
        onSaveData(totalBonus, comboCount, !isAuto && targetCount > 0, isBrokenBroom, isBrokenGarbageCanXl);
    }
    
}

/**
 * 取得するボーナス(ポイント値)を返す
 */
int
GameManager::getBonus(int defValue, bool isAuto, bool isSp)
{
    if (isBonusTime())
    {
        return defValue * 3;
    }
    if (isAuto && !isSp)
    {
        return (int)round((double)defValue * 1.5);
    }
    return defValue;
}

/**
 * 合成済みゴミリストをリセットする
 */
void
GameManager::resetSynthesisSuccessList()
{
    mSynthesisSuccessList.clear();
}

/**
 * 合成済みゴミリストにゴミを追加する
 */
void
GameManager::addSucceedSynthesisId(int garbageId)
{
    if (find(mSynthesisSuccessList.begin(),mSynthesisSuccessList.end(),garbageId) == mSynthesisSuccessList.end())
    {
        mSynthesisSuccessList.push_back(garbageId);
    }
}

/**
 * ログアウト処理
 */
void
GameManager::logout()
{
    finishedSeal();
    finishedBattery();
}

GarbageData *
GameManager::getBottomGarbage()
{
    std::vector<GarbageData *> tmpList = mGarbageList;

    int i;
    double maxTop = 0;
    int maxIndex = -1;
    for (i = 0; i < tmpList.size(); i++)
    {
        if (tmpList[i]->getRestLife() == 1
            && mGetPlayerManager()->isEnabledChangeFullness(1) != eChangeCeckCode_OK)
        {
            continue;
        }
        
        double top = tmpList[i]->getCurrentTop();
        if (top > maxTop)
        {
            maxTop = top;
            maxIndex = i;
        }
    }
    
    if (maxIndex < 0)
    {
        return 0;
    }
    return tmpList[maxIndex]->clone();
}

void
GameManager::setIsOwnedAutoBroom(bool isOwned)
{
    mIsOwnedAutoBroom = isOwned;
}

void
GameManager::onOwnedAutoBroom()
{
    mIsOwnedAutoBroom = true;
}

bool
GameManager::isOwnedAutoBroom()
{
    return mIsOwnedAutoBroom;
}

double
GameManager::getBatteryGage()
{
    if (!isUsedBattery())
    {
        return 0.0;
    }
    
    double currentTime = mGetPlayerManager()->getCurrentTime();
    double startTime = getBatteryStartTime();
    double rate = (currentTime - startTime) / BATTERY_INTERVAL;
    return 25.0 * (1.0 - rate);
}

// ------------------------------
// Function
// ------------------------------
/**
 * 封印使用の開始時刻をセットする
 */
void
GameManager::setSealStartTime(double value)
{
    if (value < 0)
    {
        value = 0;
    }
    mSealStartTime = value;
}

void
GameManager::setBatteryStartTime(double value)
{
    if (value < 0)
    {
        value = 0;
    }
    mBatteryStartTime = value;
}

/**
 * ボーナスタイム使用の開始時刻をセットする
 */
void
GameManager::setBonusStartTime(double value)
{
    if (value < 0)
    {
        value = 0;
    }
    mBonusStartTime = value;
}

/**
 * Zドリンクによるボーナスタイム使用の開始時刻をセットする
 */
void
GameManager::setZDrinkStartTime(double value)
{
    if (value < 0)
    {
        value = 0;
    }
    mZDrinkStartTime = value;
}

/**
 * 変身ドロップの開始時刻をセットする
 */
void
GameManager::setDropStartTime(double value)
{
    if (value < 0)
    {
        value = 0;
    }
    mDropStartTime = value;
}

/**
 * ゴミ追加の開始時刻をセットする
 */
void
GameManager::setAddGarbageStartTime(double value)
{
    if (value < 0)
    {
        value = 0;
    }
    
    int i;
    for (i = 0; i < ROOM_COUNT; i++) {
        if (mAddGarbageStartTime[i] <= 0) {
            mAddGarbageStartTime[i] = value;
        }
    }
    mAddGarbageStartTime[getCurrentStage()] = value;
}

/**
 * 穴出現のアニメーション開始
 */
void 
GameManager::setAddHoleStartTime(double value)
{
    if (value < 0.0)
    {
        value = 0.0;
    }
    mAddHoleStartTime = value;
}

/**
 * 箒関連のデータをセットする
 */
void
GameManager::setBroom(int type, int sweepCount)
{
    if (type <= eBroomType_Normal
        || type > eBroomType_Gold
        || sweepCount >= (type == eBroomType_Gold
                          ? BROOM_MAX_SWEEP_COUNT_GOLD
                          : BROOM_MAX_SWEEP_COUNT_SILVER))
    {
        mBroomType = eBroomType_Normal;
        mBroomSweepCount = 0;
        return;
    }

    mBroomType = type;
    mBroomSweepCount = sweepCount;
}

/**
 * 封印の効果を開始する
 */
void
GameManager::startSeal()
{
    mSealStartTime = mGetPlayerManager()->getCurrentTime();
}

/**
 * 封印の効果を終了する
 */
void
GameManager::finishedSeal()
{
    mSealStartTime = 0.0;
}

void
GameManager::startBattery()
{
    mBatteryStartTime = mGetPlayerManager()->getCurrentTime();
}

void
GameManager::finishedBattery()
{
    mBatteryStartTime = 0.0;
}

/**
 * ボーナスタイムの効果を開始する
 */
void
GameManager::startBonusTime()
{
    if (mGetPlayerManager != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0)
        {
            onSaveData(0, -playerManager->getFullness(), false, false, false);

            playerManager->setCapacity(0, playerManager->getGarbageCanType());
            mBonusStartTime = playerManager->getCurrentTime();
        }
    }
}

/**
 * ボーナスタイムの効果を終了する
 */
void
GameManager::finishedBonusTime()
{
    mBonusStartTime = 0.0;
}

/**
 * Zドリンクによるボーナスタイムの効果を開始する
 */
void
GameManager::startZDrinkTime()
{
    if (mGetPlayerManager != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0)
        {
            onSaveData(0, -playerManager->getFullness(), false, false, false);
            
            playerManager->setCapacity(0, playerManager->getGarbageCanType());
            mZDrinkStartTime = playerManager->getCurrentTime();
        }
    }
}

/**
 * Zドリンクによるボーナスタイムの効果を終了する
 */
void
GameManager::finishedZDrinkTime()
{
    mZDrinkStartTime = 0.0;
}

/**
 * 変身ドロップの効果を開始する
 */
void
GameManager::startDropTime()
{
    PlayerManager *playerManager = mGetPlayerManager();
    if (playerManager != 0)
    {
        mDropStartTime = playerManager->getCurrentTime();
    }
}

/**
 * 変身ドロップの効果を終了する
 */
void
GameManager::finishedDropTime()
{
    mDropStartTime = 0.0;
}

/**
 * ゴミを追加する
 */
void
GameManager::addGarbage(int count, int isForegroundFirst)
{
    LOGI("仕様変更対応 - 1 - count:%d, isForegroundFirst:%d", count, isForegroundFirst);

    // ヒーロードリンクの出現チェック
    if (!isShowHeroDrink() &&
        !isBonusTime() &&
        !isZDrinkTime() &&
        !isDropTime())
    {
        if (RandomUtils::getRandomNumber(HERO_DRINK_RATE - 1) == 0)
        {
            startHeroDrinkAppear();
        }
    }

    int maxGarbage = getGarbageMaxValue();

    if (mGarbageList.size() >= maxGarbage)
    {
//        LOGI("仕様変更対応 - 2");
//
//        // 庭以外で電動ほうき使用中なら全てをポイントに変換
//        if (isForegroundFirst == 1 && getCurrentStage() != eStage_Garden && isUsedBattery())
//        {
//            LOGI("仕様変更対応 - 3");
//
//            int level = 0;
//            if (mGetPlayerManager != 0)
//            {
//                PlayerManager *playerManager = mGetPlayerManager();
//                if (playerManager != 0)
//                {
//                    level = playerManager->getLevel();
//                }
//            }
//            
//            int totalBonus = 0;
//            int dropGarbageId = getMaxRareGarbageId();
//            int i;
//            for (i = 0; i < count; i++)
//            {
//                // 変身ドロップ
//                if (isDropTime())
//                {
//                    totalBonus += GarbageData::makeGarbageData(dropGarbageId)->getBonus();
//                    continue;
//                }
//                
//#if TEST_RANDOM_GARBAGE
//                int garbageId = RandomUtils::getRandomNumber(126 - 1) + 1;
//#else
//                // 合成に成功していたら、とりあえず1%の確率で出現させる
//                if (mSynthesisSuccessList.size() > 0)
//                {
//                    if (RandomUtils::getRandomNumber(SYNTHESIS_GARBAGE_APPEAR_RATE - 1) == 0)
//                    {
//                        int index = RandomUtils::getRandomNumber((int)mSynthesisSuccessList.size() - 1);
//                        int garbageId = mSynthesisSuccessList[index];
//                        totalBonus += GarbageData::makeGarbageData(garbageId)->getBonus();
//                        continue;
//                    }
//                }
//                
//                int garbageId = eGarbageId_Dust;
//                if (level > 0)
//                {
//                    if (level > MAX_LEVEL)
//                    {
//                        level = MAX_LEVEL;
//                    }
//                    // TODO 乱数調整
//                    garbageId = RandomUtils::getRandomNumber(level - 1) + 1;
//                    
//                    //途中の合成アイテムスキップ
//                    if (garbageId > eGarbageId_PetitEnvelope) {
//                        garbageId += eGarbageId_Chest - eGarbageId_PetitEnvelope;
//                    }
//                }
//#endif
//                
//                totalBonus += GarbageData::makeGarbageData(garbageId)->getBonus();
//            }
//
//            LOGI("仕様変更対応 - 4 - bonus:%d", totalBonus);
//
//            mGetPlayerManager()->changePoint(totalBonus);
//            onSaveData(totalBonus, 0, false, false, false);
//        }
        return;
    }

    int level = 0;
    if (mGetPlayerManager != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0)
        {
            level = playerManager->getLevel();
        }
    }

//    int autoBroomBonus = 0;
    int dropGarbageId = getMaxRareGarbageId();
    int i;
    for (i = 0; i < (mGarbageList.size() + count >= maxGarbage ? maxGarbage - mGarbageList.size() : count); i++)
//    for (i = 0; i < count; i++)
    {
        if (mGarbageList.size() + i > maxGarbage)
        {
//            LOGI("仕様変更対応 - 5");
//
//            // 庭以外で電動ほうき使用中なら全てをポイントに変換
//            if (isForegroundFirst == 1 && getCurrentStage() != eStage_Garden && isUsedBattery())
//            {
//                LOGI("仕様変更対応 - 6");
//
//                int bonus = 0;
//                
//                // 変身ドロップ
//                if (isDropTime())
//                {
//                    bonus = GarbageData::makeGarbageData(dropGarbageId)->getBonus();
//                    autoBroomBonus += bonus;
//                    continue;
//                }
//                
//#if TEST_RANDOM_GARBAGE
//                int garbageId = RandomUtils::getRandomNumber(126 - 1) + 1;
//#else
//                // 合成に成功していたら、とりあえず1%の確率で出現させる
//                if (mSynthesisSuccessList.size() > 0)
//                {
//                    if (RandomUtils::getRandomNumber(SYNTHESIS_GARBAGE_APPEAR_RATE - 1) == 0)
//                    {
//                        int index = RandomUtils::getRandomNumber((int)mSynthesisSuccessList.size() - 1);
//                        int garbageId = mSynthesisSuccessList[index];
//                        bonus = GarbageData::makeGarbageData(garbageId)->getBonus();
//                        autoBroomBonus += bonus;
//                        continue;
//                    }
//                }
//                
//                int garbageId = eGarbageId_Dust;
//                if (level > 0)
//                {
//                    if (level > MAX_LEVEL)
//                    {
//                        level = MAX_LEVEL;
//                    }
//                    // TODO 乱数調整
//                    garbageId = RandomUtils::getRandomNumber(level - 1) + 1;
//                    
//                    //途中の合成アイテムスキップ
//                    if (garbageId > eGarbageId_PetitEnvelope) {
//                        garbageId += eGarbageId_Chest - eGarbageId_PetitEnvelope;
//                    }
//                }
//#endif
//                bonus = GarbageData::makeGarbageData(garbageId)->getBonus();
//                autoBroomBonus += bonus;
//            }
            
            continue;
        }
        
        // 変身ドロップ
        if (isDropTime())
        {
            mGarbageList.push_back(GarbageData::makeGarbageData(dropGarbageId));
            continue;
        }
        
        // SPゴミは1/10000の確率で出現させる
        int friendCount = mGetPlayerManager()->getFriendCount();
        if (friendCount >= 1)
        {
            if (RandomUtils::getRandomNumber(SP_GARBAGE_APPEAR_RATE - 1) == 0)
            {
                int garbageId = RandomUtils::getRandomNumber((friendCount > 9 ? 9 : friendCount) - 1) + 127;
                mGarbageList.push_back(GarbageData::makeGarbageData(garbageId));
                continue;
            }
        }

        
#if TEST_RANDOM_GARBAGE
        int garbageId = RandomUtils::getRandomNumber(126 - 1) + 1;
#else
        // 合成に成功していたら、とりあえず1%の確率で出現させる
        if (mSynthesisSuccessList.size() > 0)
        {
            if (RandomUtils::getRandomNumber(SYNTHESIS_GARBAGE_APPEAR_RATE - 1) == 0)
            {
                int index = RandomUtils::getRandomNumber((int)mSynthesisSuccessList.size() - 1);
                int garbageId = mSynthesisSuccessList[index];
                mGarbageList.push_back(GarbageData::makeGarbageData(garbageId));
                continue;
            }
        }

        int garbageId = eGarbageId_Dust;
        if (level > 0)
        {
            if (level > MAX_LEVEL)
            {
                level = MAX_LEVEL;
            }
            // TODO 乱数調整
            garbageId = RandomUtils::getRandomNumber(level - 1) + 1;
            
            //途中の合成アイテムスキップ
            if (garbageId > eGarbageId_PetitEnvelope) {
                garbageId += eGarbageId_Chest - eGarbageId_PetitEnvelope;
            }
        }
#endif
        
        mGarbageList.push_back(GarbageData::makeGarbageData(garbageId));
    }

    // 遠近法にならって、リストの並び替え
    std::sort(
            mGarbageList.begin(),
            mGarbageList.end(),
            [&](GarbageData *left, GarbageData *right)
            {
                if (left == 0 || left == NULL
                    || right == 0 || right == NULL)
                {
                    return true;
                }

                int leftSp = left->isSp() ? 1 : 0;
                int rightSp = right->isSp() ? 1 : 0;
                if (leftSp != rightSp)
                {
                    return leftSp < rightSp;
                }
                
                double leftBottom = left->getCurrentTop() + left->getHeight();
                double rightBottom = right->getCurrentTop() + right->getHeight();
                return leftBottom < rightBottom;
            });

//    LOGI("仕様変更対応 - 7");
//    if (autoBroomBonus > 0)
//    {
//        LOGI("仕様変更対応 - 8 - bonus:%d", autoBroomBonus);
//        mGetPlayerManager()->changePoint(autoBroomBonus);
//        onSaveData(autoBroomBonus, 0, false, false, false);
//    }

    if (mRequestRefreshGarbage != 0)
    {
        LOGI("#10 : eSwipeState_OnRefreshed");
        changeSwipeState(eSwipeState_OnRefreshed);
        mRequestRefreshGarbage();
    }
}

/**
 * ヒーロードリンク表示開始時刻をセットする
 */
void
GameManager::setHeroDrinkAppearTime(double value)
{
    if (value < 0.0)
    {
        value = 0.0;
    }
    mHeroDrinkAppearTime = value;
}

/**
 * ヒーロードリンクの表示を開始する
 */
void
GameManager::startHeroDrinkAppear()
{
    mHeroDrinkLeft = (double)RandomUtils::getRandomNumber(320);
    mHeroDrinkTop = (double)RandomUtils::getRandomNumber(278) + 190.0;
    double width = 46;
    double height = 75;
    if (mHeroDrinkLeft + width > STANDARD_WIDTH)
    {
        mHeroDrinkLeft = STANDARD_WIDTH - width - (double)RandomUtils::getRandomNumber(10);
    }
    if (mHeroDrinkTop + height > STANDARD_HEIGHT - 120.0)
    {
        mHeroDrinkTop = STANDARD_HEIGHT - 120.0 - height - (double)RandomUtils::getRandomNumber(10);
    }
    setHeroDrinkAppearTime(mGetPlayerManager()->getCurrentTime());
}

/**
 * ヒーロードリンクの表示を終了する
 */
void
GameManager::finishedHeroDrinkAppear()
{
    setHeroDrinkAppearTime(0.0);
    mHeroDrinkLeft = 0.0;
    mHeroDrinkTop = 0.0;
}

int
GameManager::getGarbageMaxValue()
{
    if (mGetPlayerManager != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0)
        {
            if (playerManager->getGarbageCanType() == eGarbageCanType_Big)
            {
                return GARBAGE_MAX_COUNT_BIG;
            }

            if (playerManager->getGarbageCanType() == eGarbageCanType_Huge)
            {
                return GARBAGE_MAX_COUNT_HUGE;
            }
            
            if (playerManager->getGarbageCanType() == eGarbageCanType_XL)
            {
                return GARBAGE_MAX_COUNT_HUGE;
            }
        }
    }
    return GARBAGE_MAX_COUNT_DEFAULT;
}

//-----------------------------------------------
// 穴の出現（ジロキチの隠れ家）
//-----------------------------------------------
/**
 * 穴の出現条件チェック　達成したレベル：サーバ側で管理される　部屋のごみなし
 */
 bool 
 GameManager::canShowHole()
 {
#if TEST_JIROKICHI
     //テスト用のコード
     double currentTime = TimeUtils::getTime();
     if (mShowHoleStartTime == -1.0) {
         mShowHoleStartTime = currentTime + 10000.0;
         return false;
     }
     if (currentTime >= mShowHoleStartTime) {
         if (mShowHoleStartTime >= 0) {
             return true;
         }
         else return false;
     }
     return false;
#endif
     
     // ボーナスタイムのとき、隠れ家にすでにいるとき、ゴミがまだ残っているとき
     if (isBonusTime() || isZDrinkTime() || isUndergroundStage() || mGarbageList.size() > 0)
         return false;
     
     PlayerManager *playerManager = mGetPlayerManager();
     if (playerManager != 0)
     {
         return playerManager->canEnterUnderground();
     }
     
     return false;
 }

 void 
 GameManager::addHole()
 {
    mHoleLeft = (double) RandomUtils::getRandomNumber(320);
    mHoleTop = (double) RandomUtils::getRandomNumber(278) + 190.0;
    double width = 140.0;
    double height = 56.0;

    if (mHoleLeft + width > STANDARD_WIDTH)
    {
        mHoleLeft = STANDARD_WIDTH - width - (double)RandomUtils::getRandomNumber(10);
    }
    if (mHoleTop + height > STANDARD_HEIGHT - 120.0)
    {
        mHoleTop = STANDARD_HEIGHT - 120.0 - height - (double)RandomUtils::getRandomNumber(10);
    }
 }

int GameManager::getMaxRareGarbageId()
{
    int maxGarbageId = eGarbageId_Dust;
    int maxGarbagePoint = 0;
    
    // 通常ゴミの中で最大のポイントを持つものを選定
    int i = 0;
    int level = 0;
    if (mGetPlayerManager != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager != 0)
        {
            level = playerManager->getLevel();
        }
    }
    if (level > 0)
    {
        if (level > MAX_LEVEL)
        {
            level = MAX_LEVEL;
        }
        for (i = 1; i <= level; i++)
        {
            int garbageId = i;
            //途中の合成アイテムスキップ
            if (garbageId > eGarbageId_PetitEnvelope) {
                garbageId += eGarbageId_Chest - eGarbageId_PetitEnvelope;
            }
            GarbageData *data = GarbageData::makeGarbageData(garbageId);
            int point = data->getBonus();
            if (maxGarbagePoint <= point)
            {
                maxGarbageId = garbageId;
                maxGarbagePoint = point;
            }
            data = 0;
        }
    }
    
    // 合成ゴミの中で最大のポイントを持つものを選定
    if (mSynthesisSuccessList.size() > 0)
    {
        for (i = 0; i < mSynthesisSuccessList.size(); i++)
        {
            int garbageId = mSynthesisSuccessList[i];
            GarbageData *data = GarbageData::makeGarbageData(garbageId);
            int point = data->getBonus();
            if (maxGarbagePoint <= point)
            {
                maxGarbageId = garbageId;
                maxGarbagePoint = point;
            }
            data = 0;
        }
    }

    return maxGarbageId;
}

//
//
//
#include "../parameter/app_parameter.h"
#include "../../lib/utility/TimeUtils.h"
#include "../utility/JsonUtils.h"
#include "../utility/GarbageUtils.h"

#include "../model/data/GarbageData.h"
#include "../model/data/GemData.h"
#include "../model/data/SaveData.h"

#include "PlayerManager.h"

#ifndef GOMIBAKO_GAMEMANAGER_H
#define GOMIBAKO_GAMEMANAGER_H

#define SP_APPEAR_TEST_FLAG 0 // 0:本番(1/10,000) 1:テスト(1/20)

#define SEAL_INTERVAL (1000.0 * 60.0 * 60.0 * 24.0)     // 封印シールの継続時間 : 24時間
#define BONUS_INTERVAL (1000.0 * 60.0)                  // ボーナスタイムの継続時間 : 1分
#define HERO_DRINK_APPEAR_INTERVAL (1000.0 * 30.0)      // ヒーロードリンク表示の継続時間 : 30秒
#define GARBAGE_MAX_COUNT_DEFAULT 50                    // ゴミの部屋に表示される最大数(普通) : 50
#define GARBAGE_MAX_COUNT_BIG 100                       // ゴミの部屋に表示される最大数(大きい) : 100
#define GARBAGE_MAX_COUNT_HUGE 150                      // ゴミの部屋に表示される最大数(巨大な) : 150
#define ADD_GARBAGE_INTERVAL (1000.0 * 10.0)            // ゴミが増える間隔 : 10.0秒
#define ADD_GARBAGE_INTERVAL_FOR_BONUS (1200.0)         // ゴミが増える間隔 : 1.2秒 (1分でMaxの50に達する計算)
#define ADD_HOLE_TIME (1000.0)                          // ゴミが増える間隔 : 1.2秒 (1分でMaxの50に達する計算)
#define BROOM_MAX_SWEEP_COUNT_SILVER 300                // 銀の箒の使用回数 : 300回
#define BROOM_MAX_SWEEP_COUNT_GOLD 500                  // 金の箒の使用回数 : 500回
#define MOTHER_RATE 10                                  // お母さんイベント発生の確率 : 1/10
#define HERO_DRINK_RATE 100                             // ヒーロードリンク出現の確率 : 1/100
#define SYNTHESIS_GARBAGE_APPEAR_RATE 100               // 合成で手に入れたゴミが出現する確率 : 1/100
#define SP_GARBAGE_APPEAR_RATE (SP_APPEAR_TEST_FLAG == 1 ? 20 : 10000)
#define SAVE_DATA_INTERVAL (1000.0 * 10.0)              // セーブデータを送信する間隔 : 10秒
#define CONSUMPTION_RATE 0.01                           // 容量回復の割合
#define DROP_INTERVAL (1000.0 * 60.0)                   // 変身ドロップの継続時間 : 1分
#define BATTERY_INTERVAL (1000.0 * 60.0 * 60.0 * 24.0)     // 電池の継続時間 : 24時間
#define AUTOBROOM_WAITING_INTERVAL 100                 // 電動ほうきの待機時間: 0.1秒

//
// Enum
// ------------------------------
enum
{
    eSwipeState_None = 0,
    eSwipeState_Swiping,
    eSwipeState_DoAnimation,
    eSwipeState_OnFinished,
    eSwipeState_OnRefreshed,
    eSwipeState_OnFinishedAutoClean,
};

//
// Callback
// ------------------------------
typedef std::function<PlayerManager*()>    tCallback_getPlayerManager;
typedef std::function<void()>      tCallback_requestRefreshGarbage;
typedef std::function<void()>      tCallback_requestRefreshGem;
typedef std::function<void(int)>      tCallback_brokenBroom;
typedef std::function<void()> tCallback_reqestSaveData;
typedef std::function<bool()> tCallback_isActive;
typedef std::function<void()> tCallback_existExclusionGarbage;
typedef std::function<void()>      tCallback_brokenGarbageCanXl;

class GameManager {

// ------------------------------
// Member
// ------------------------------
private:
    int mCurrentStage;
    
    // 開始時刻
    double mSealStartTime;
    double mBonusStartTime;
    double mZDrinkStartTime;
    double mDropStartTime;
    double mBatteryStartTime;
    
    double* mAddGarbageStartTime;
    double mSaveDataWaitStartTime;

    // セーブデータ
    std::vector<SaveData *> mSaveDataQueue;
    std::vector<int> mNewFoundGarbages;
    
    // 箒
    int mBroomType;
    int mBroomSweepCount;

    // スワイプイベント
    int mSwipeState;

    // サーバーデータ
    std::vector<GarbageData *> mGarbageList;
    std::vector<GemData *> mGemList;
    std::vector<int> mSynthesisSuccessList;

    // ヒーロードリンク
    double mHeroDrinkAppearTime;
    double mHeroDrinkLeft;
    double mHeroDrinkTop;

    // 穴
    double mAddHoleStartTime;
    double mHoleLeft;
    double mHoleTop;
    
    // ジロキチの隠れ家
    bool mIsUndergroundStage;
    
    // 自動ほうき
    bool mIsOwnedAutoBroom;

    // コールバック
    tCallback_getPlayerManager mGetPlayerManager;
    tCallback_requestRefreshGarbage mRequestRefreshGarbage;
    tCallback_requestRefreshGem mRequestRefreshGem;
    tCallback_brokenBroom mBrokenBroom;
    tCallback_reqestSaveData mRequestSaveData;
    tCallback_isActive mIsActive;
    tCallback_existExclusionGarbage mExistExclusionGarbage;
    tCallback_brokenGarbageCanXl mBrokenGarbageCanXl;
    
#if TEST_JIROKICHI
    //テスト用
public:
    double mShowHoleStartTime;
#endif

// ------------------------------
// Constructor
// ------------------------------
public:
    GameManager(
                tCallback_getPlayerManager getPlayerManager,
                tCallback_requestRefreshGarbage requestRefreshGarbage,
                tCallback_brokenBroom brokenBroom,
                tCallback_reqestSaveData requestSaveData,
                tCallback_isActive isActive,
                tCallback_existExclusionGarbage existExclusionGarbage,
                tCallback_brokenGarbageCanXl brokenGarbageCanXl)
    {
        mCurrentStage = eStage_Default;
        
        mIsActive = isActive;
        mGetPlayerManager = getPlayerManager;
        mRequestRefreshGarbage = requestRefreshGarbage;
        mBrokenBroom = brokenBroom;
        mRequestSaveData = requestSaveData;
        mExistExclusionGarbage = existExclusionGarbage;
        mBrokenGarbageCanXl = brokenGarbageCanXl;

        mSwipeState = eSwipeState_None;

        mSealStartTime = 0.0;
        mBonusStartTime = 0.0;
        mZDrinkStartTime = 0.0;
        mDropStartTime = 0.0;
        mBatteryStartTime = 0.0;
        mAddGarbageStartTime = new double[ROOM_COUNT];
        int i;
        for (i = 0; i < ROOM_COUNT; i++) {
            mAddGarbageStartTime[i] = 0.0;
        }
        
        mAddHoleStartTime = 0.0;
        mBroomType = eBroomType_Normal;
        mBroomSweepCount = 0;
        mHeroDrinkAppearTime = 0.0;
        mHeroDrinkLeft = 0.0;
        mHeroDrinkTop = 0.0;
        
        mIsUndergroundStage = false;
        
        mIsOwnedAutoBroom = false;
        
#if TEST_JIROKICHI
        mShowHoleStartTime = -1.0;
#endif
    }

    virtual ~GameManager()
    {
        mGetPlayerManager = 0;
        mRequestRefreshGarbage = 0;

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
        
        if (mGemList.size() > 0)
        {
            for (i = (int)mGemList.size() - 1; i >= 0; i--)
            {
                if (mGemList[i] != 0)
                {
                    delete mGemList[i];
                    mGemList[i] = 0;
                }
                mGemList.erase(mGemList.begin() + i);
            }
        }
        
        delete mAddGarbageStartTime;

    }

// ------------------------------
// Accesser
// ------------------------------
public:
    // ステージ管理
    int getCurrentStage();
    void setCurrentStage(int stage);
    bool isCurrentSecret(int secretMissionId);
    
    // 開始時刻
    double getSealStartTime();
    double getBonusStartTime();
    double getRemainingBonusTime();
    double getAddGarbageStartTime();
    double getZDrinkStartTime();
    double getRemainingZDrinkTime();
    double getDropStartTime();
    double getRemainingDropTime();
    double getBatteryStartTime();

    // 箒
    void setBroom(int type, int sweepCount);
    int getBroomType();
    int getBroomSweepCount();

    // タイマー処理
    void onCheckTime(int isForegroundFirst);

    // データセット
    void setInitGameInfo(double bonusStartTime, double addGarbageStartTime, double heroDrinkAppearTime, double zDrinkStartTime, double dropStartTime);
    void setGomipoiGameLoadResponse(std::string garbages);
    void resetSynthesisSuccessList();
    void addSucceedSynthesisId(int garbageId);

    // アイテム処理
    bool isSealing();
    bool isBonusTime();
    bool isZDrinkTime();
    bool isDropTime();
    bool onUsedSealItem();
    void onChangeBroomType(int type);
    void setSealStartTime(double value);
    bool isUsedBattery();
    bool onUsedBattery();
    void setBatteryStartTime(double value);

    // イベント処理
    bool onCheckMother();

    // ヒーロードリンク
    double getHeroDrinkAppearTime();
    bool isShowHeroDrink();
    double getHeroDrinkLeft();
    double getHeroDrinkTop();
    bool onUsedBonusTimeItem();
    
    // Zドリンク
    bool onUsedZDrinkItem();
    
    // 変身ドロップ
    bool onUsedDropItem();

    // 穴
    double getAddHoleStartTime();
    bool isShowHole();
    double getHoleLeft();
    double getHoleTop();
    
    // ジロキチの隠れ家
    bool isUndergroundStage();
    void setUndergroundStage(bool isUndergroundStage);
    bool checkUndergroundStageEnd();
    void resetUndergroundStage();
    void setAddHoleStartTime(double value);
    
    // 宝石
    void makeGemList();
    std::vector<GemData *> getGemList();
    void setGemList(std::vector<GemData *> list);

    // ゴミデータ
    std::string getGarbagesJson();
    std::vector<GarbageData *> getGarbageList();
    void transformGarbageByDrop();

    // スワイプイベント処理
    void changeSwipeState(int state);
    int getSwipeState();
    void onSweep(double startTouchX, double startTouchY, double screenWidth, double screenHeight, bool isAuto);
    int getBonus(int defValue, bool isAuto, bool isSp);

    // データセーブ関連処理
    void onSaveData(int totalPoint, int completeCount, bool isSweep, bool isBrokenBroom, bool isGarbageCanBroken);
    std::vector<SaveData *> getSaveData();
    std::string getNewFoundGarbages();

    void logout();
    
    GarbageData *getBottomGarbage();
    
    void setIsOwnedAutoBroom(bool isOwned);
    void onOwnedAutoBroom();
    bool isOwnedAutoBroom();
    
    double getBatteryGage();

// ------------------------------
// Function
// ------------------------------
private:
    void setBonusStartTime(double value);
    void setZDrinkStartTime(double value);
    void setAddGarbageStartTime(double value);
    void setHeroDrinkAppearTime(double value);
    void setDropStartTime(double value);

    void startSeal();
    void finishedSeal();
    void startBonusTime();
    void finishedBonusTime();
    void startZDrinkTime();
    void finishedZDrinkTime();
    void startDropTime();
    void finishedDropTime();
    void addGarbage(int count, int isForegroundFirst);
    void startBattery();
    void finishedBattery();

    void startHeroDrinkAppear();
    void finishedHeroDrinkAppear();

    int getGarbageMaxValue();

    //穴：ジロキチの隠れ家
    bool canShowHole();
    void addHole();
    
    int getMaxRareGarbageId();

};


#endif //GOMIBAKO_GAMEMANAGER_H

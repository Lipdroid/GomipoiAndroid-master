//
// JNIの窓口クラス
//
#include "app/parameter/app_parameter.h"

#include "app/manager/AppManager.h"
#include "lib/callback/AppManagerCallback.h"

#include "app/manager/PlayerManager.h"
#include "app/manager/GameManager.h"

#include "app/utility/JsonUtils.h"
#include "app/utility/GarbageUtils.h"
#include "app/model/data/SaveData.h"
#include "app/model/data/UsageLimitItem.h"

// ------------------------------
// Common Member
// ------------------------------
AppManager *appManager = 0;
AppManagerCallback *mCallback = 0;

PlayerManager *mPlayerManager = 0;
GameManager *mGameManager = 0;

bool mIsPausingFlag = false;
bool mIsSuspendingFlag = false;
bool mIsGettingData = false;

// ------------------------------
// Common Function
// ------------------------------
//
// Lib
// ------------------------------
bool nativeCallIsLockedEvent();
bool nativeCallIsLoading();
void nativeCallOnLoadingEnd();
void nativeCallOnPartsClicked(int partsId);
bool nativeCallInterceptOnPartsClicked(int partsId);
void nativeCallOnPartsEvent(int eventId);

//
// App
// ------------------------------
void nativeCallOnChangedGem(int gem);
void nativeCallOnChangedPoint(int point);
void nativeCallOnChangedFullness(double fullness);
void nativeCallOnChangedLevel(int level);
void nativeCallOnBrokenBroom(int broomType);
void nativeCallOnBrokenGarbageCanXl();
//void nativeCallOnFoundGarbage(std::string foundIdList);
void nativeCallOnGetPoint(int point);
void nativeCallOnGetGem(int gem);
void nativeCallOnGetComboBonus(int comboCount, int point);
void nativeCallOnClearMission(int missionId);
void nativeCallOnUndergroundGemGot(int gem);
void nativeCallOnSucceededSyntheses(int garbageId);
void nativeCallOnEnterUnderground(bool enterJirokichi);
void nativeCallOnChangeStage(int stageId);
void nativeCallOnRemainingBonusTime(int remainingSeconds);

// データ保存
void nativeCallOnSaveOtherInfo();
void nativeCallOnSaveGameInfo(int placeType, int addPoint, int completedCount, int sweepCount, bool isBrokenBroom, int nextStage, bool isBrokenGarbageCan);

bool
isGameActive()
{
    bool isExistAppManager = appManager != 0;
    return isExistAppManager && !mIsSuspendingFlag && !mIsGettingData;
}

/**
 * X座標をデバイス座標からOpenGL座標に変換する
 */
float
convertDeviceXToGLX(float value)
{
    if (appManager == 0)
    {
        return -100.0f;
    }

    float screenWidth = appManager->getScreenWidth();

    // まだスクリーンサイズが取れていない(Viewが完成していない)場合は、イベントを無視
    if (screenWidth <= 0)
    {
        // OpenGL座標は -1.0 〜 1.0 なので、違いを出すためにエラー時は -100 を返す
        return -100.0f;
    }

    float middleWidth = screenWidth / 2.0f;
    return ((value - middleWidth) * 2.0f)/ screenWidth;
}

/**
 * Y座標をデバイス座標からOpenGL座標に変換する
 */
float
convertDeviceYToGLY(float value)
{
    if (appManager == 0)
    {
        return -100.0f;
    }

    float screenHeight = appManager->getScreenHeight();

    // まだスクリーンサイズが取れていない(Viewが完成していない)場合は、イベントを無視
    if (screenHeight <= 0)
    {
        // OpenGL座標は -1.0 〜 1.0 なので、違いを出すためにエラー時は -100 を返す
        return -100.0f;
    }

    float middleHeight = screenHeight / 2.0f;
    return -((value - middleHeight) * 2.0f) / screenHeight;
}

/**
 * 親ViewがCreateされた時に呼ばれる
 */
void
commonOnParentCreated()
{
    mIsPausingFlag = false;
    mIsSuspendingFlag = false;

    // コールバック生成
    mCallback = new AppManagerCallback();
    mCallback->mOnPartsClicked = [&](int partsId)
    {
        // tCallback_onPartsClicked
        nativeCallOnPartsClicked(partsId);
    };

    mCallback->mOnPartsEvent = [&](int eventId)
    {
        // tCallback_onPartsEvent
        nativeCallOnPartsEvent(eventId);
    };

    mCallback->mIsLockedEvent = [&]()
    {
        // tCallback_isLockedEvent
        return nativeCallIsLockedEvent();
    };

    mCallback->mOnLoadingEnd = [&]()
    {
        nativeCallOnLoadingEnd();
    };

    mCallback->mIsLoading = [&]()
    {
        return nativeCallIsLoading();
    };

    mCallback->mPausing = new PausingCallback();
    mCallback->mPausing->mIsPausing = [&]()
    {
        return mIsPausingFlag;
    };

    mCallback->mPausing->mIsSuspending = [&]()
    {
        return mIsSuspendingFlag;
    };

    appManager = new AppManager(
            [&]()
            {
                return mGameManager;
            },
            [&]()
            {
                return mPlayerManager;
            },
            [&](int point)
            {
                nativeCallOnGetPoint(point);
            },
            [&](int gem)
            {
                nativeCallOnGetGem(gem);
            },
            [&](int comboCount, int point)
            {
                nativeCallOnGetComboBonus(comboCount, point);
            },
            [&](int missionId)
            {
                nativeCallOnClearMission(missionId);
            },
            [&](int undergroundGem)
            {
                nativeCallOnUndergroundGemGot(undergroundGem);
            },
            [&](bool enterJirokichi)
            {
                nativeCallOnEnterUnderground(enterJirokichi);
            },
            [&](int stageId)
            {
                nativeCallOnChangeStage(stageId);
            },
            [&](int remainingSeconds)
            {
                nativeCallOnRemainingBonusTime(remainingSeconds);
            });
    appManager->setCallback(mCallback);
}

/**
 * 親ViewがDestroyされる時に呼ばれる
 */
void
commonOnParentDestroy()
{
    if (appManager != 0)
    {
        delete appManager;
        appManager = 0;
    }

    if (mCallback != 0)
    {
        delete mCallback;
        mCallback = 0;
    }
}

/**
 * SurfaceViewがCreateされる時に呼ばれる
 */
void
commonOnSurfaceCreated()
{
    if (appManager == 0)
    {
        return;
    }
    appManager->onSurfaceCreated();
}

/**
 * SurfaceViewのサイズに変更があった時に呼ばれる
 */
void
commonOnSurfaceChanged(int width, int height)
{
    if (appManager == 0)
    {
        return;
    }
    appManager->onSurfaceChanged(width, height);
}

/**
 * Renderする時に呼ばれる
 */
void
commonOnDrawFrame()
{
    if (appManager == 0)
    {
        return;
    }
    appManager->onDrawFrame();
}

/**
 * ViewをTouchDownした時に呼ばれる
 */
void
commonOnTouchDown(float ptX, float ptY)
{
    // Androidの座標 -> OpenGLの座標に変換する
    float touchX = convertDeviceXToGLX(ptX);
    float touchY = convertDeviceYToGLY(ptY);
    if (touchX == -100 || touchY == -100)
    {
        return;
    }

    if (appManager == 0)
    {
        return;
    }
    appManager->onTouchDown(touchX, touchY);
}

/**
 * ViewをTouchMoveした時に呼ばれる
 */
void
commonOnTouchMove(float ptX, float ptY)
{
    // Androidの座標 -> OpenGLの座標に変換する
    float touchX = convertDeviceXToGLX(ptX);
    float touchY = convertDeviceYToGLY(ptY);
    if (touchX == -100 || touchY == -100)
    {
        return;
    }

    if (appManager == 0)
    {
        return;
    }
    appManager->onTouchMove(touchX, touchY);
}

/**
 * ViewをTouchUpした時に呼ばれる
 */
void
commonOnTouchUp()
{
    if (appManager == 0)
    {
        return;
    }
    appManager->onTouchUp();
}

/**
 * 一時停止中かを返す
 */
bool
commonIsPausing()
{
    return mIsPausingFlag;
}

/**
 * 一時停止が終了した時に呼ばれる
 */
void
commonPauseEnd()
{
    mIsPausingFlag = false;

    if (appManager == 0)
    {
        return;
    }
    appManager->pauseEnd();
}

/**
 * 一時停止が開始した時に呼ばれる
 */
void
commonPause()
{
    mIsPausingFlag = true;

    if (appManager == 0)
    {
        return;
    }
    appManager->pause();
}

/**
 * サスペンド中かを返す
 */
bool
commonIsSuspending()
{
    return mIsSuspendingFlag;
}

/**
 * サスペンド復帰時に呼ばれる
 */
void
commonForeground()
{
    mIsSuspendingFlag = false;

    if (appManager == 0)
    {
        return;
    }
    appManager->foreground();
}

/**
 * ゲームデータ保存処理。部屋を移動する場合、nextStageを指定する。移動しない場合、nextStageを-1にする
 */
void
commonSaveData(int nextStage)
{
    if (mGameManager != 0)
    {
        std::vector<SaveData *> saveDatas = mGameManager->getSaveData();
        if (saveDatas.size() > 0)
        {
            int addPoint = 0;
            int completeCount = 0;
            int sweepCount = 0;
            bool isBroken = false;
            bool isGarbageCanBroken = false;
            
            int i;
            for (i = 0; i < saveDatas.size(); i++)
            {
                SaveData *saveData = saveDatas[i];
                if (saveData != 0)
                {
                    addPoint += saveData->add_point;
                    completeCount += saveData->put_in_garbage_count;
                    sweepCount += saveData->broom_use_count;
                    if (!isBroken)
                    {
                        isBroken = saveData->broom_broken;
                    }
                    if (!isGarbageCanBroken)
                    {
                        isGarbageCanBroken = saveData->garbage_can_broken;
                    }
                }
            }
            nativeCallOnSaveGameInfo(mGameManager->getCurrentStage(), addPoint, completeCount, sweepCount, isBroken, nextStage, isGarbageCanBroken);
        }
        else
        {
            nativeCallOnSaveGameInfo(mGameManager->getCurrentStage(), 0, 0, 0, false, nextStage, false);
        }
        
        nativeCallOnSaveOtherInfo();
    }
}

/**
 * サスペンド時に呼ばれる
 */
void
commonBackground()
{
    if (mGameManager != 0)
    {
        if (appManager != NULL) {
            appManager->setPausedJirokichi(mGameManager->isUndergroundStage());
            appManager->setPausedJirokichiGem(mGameManager->getGemList());
        }
        mGameManager->resetUndergroundStage();
    }
    
    commonSaveData(-1);

    // iOSはバックグラウンドに入ったときbackgroundを呼んだ後にpauseを呼ぶ
    // フォアグラウンドに戻ってきたときはforegroundを呼ばず、pauseEndを呼ぶ
#if IS_ANDROID
    mIsSuspendingFlag = true;

    if (appManager == 0)
    {
        return;
    }
    appManager->background();
#endif
}

//
// App
// ------------------------------
/**
 * アプリケーションがフォアグラウンドに出てきた時に呼ばれる
 */
void
commonForegroundApplication()
{
    // PlayerManagerを作成
    if (mPlayerManager != 0)
    {
        delete mPlayerManager;
        mPlayerManager = 0;
    }
    mPlayerManager = new PlayerManager(
            [&](int gem)
            {
                nativeCallOnChangedGem(gem);
            },
            [&](int point)
            {
                nativeCallOnChangedPoint(point);
            },
            [&](double fullness)
            {
                nativeCallOnChangedFullness(fullness);

                if (appManager != 0)
                {
                    appManager->onChangedFullness(fullness);
                }
            });

    // GameManagerを作成
    if (mGameManager != 0)
    {
        delete mGameManager;
        mGameManager = 0;
    }
    mGameManager = new GameManager(
            [&]()
            {
                return mPlayerManager;
            },
            [&]()
            {
                if (appManager != 0)
                {
                    appManager->refreshGarbage();
                }
            },
            [&](int broomType)
            {
                nativeCallOnBrokenBroom(broomType);
            },
            [&]()
            {
                if (mGameManager != 0)
                {
                    std::vector<SaveData *> saveDatas = mGameManager->getSaveData();
                    if (saveDatas.size() > 0)
                    {
                        int addPoint = 0;
                        int completeCount = 0;
                        int sweepCount = 0;
                        bool isBroken = false;
                        bool isGarbageCanBroken = false;

                        int i;
                        for (i = 0; i < saveDatas.size(); i++)
                        {
                            SaveData *saveData = saveDatas[i];
                            if (saveData != 0)
                            {
                                addPoint += saveData->add_point;
                                completeCount += saveData->put_in_garbage_count;
                                sweepCount += saveData->broom_use_count;
                                if (!isBroken)
                                {
                                    isBroken = saveData->broom_broken;
                                }
                                if (!isGarbageCanBroken)
                                {
                                    isGarbageCanBroken = saveData->garbage_can_broken;
                                }
                            }
                        }
                        nativeCallOnSaveGameInfo(mGameManager->getCurrentStage(), addPoint, completeCount, sweepCount, isBroken, -1, isGarbageCanBroken);
                        nativeCallOnSaveOtherInfo();
                    }
                }
            },
            [&]()
            {
                return isGameActive();
            },
            [&]()
            {
                if (appManager != 0)
                {
                    appManager->showFullGarbages();
                }
            },
            [&]()
            {
                nativeCallOnBrokenGarbageCanXl();
            });
    
    if (appManager != NULL) {
        mPlayerManager->setCapacity(mPlayerManager->getFullness(), appManager->getPausedGarbageCan());
        mGameManager->setCurrentStage(appManager->getPausedStage());
        mGameManager->setUndergroundStage(appManager->getPausedJirokichi());
        mGameManager->setGemList(appManager->getPausedJirokichiGem());
        
        appManager->setPausedJirokichi(false);
        appManager->clearPausedJirokichiGem();
        
        mGameManager->getGemList();
    }
}

/**
 * アプリケーションがバックグラウンドに入った時に呼ばれる
 */
void
commonBackgroundApplication()
{
    if (mPlayerManager != 0)
    {
        if (appManager != NULL) {
            appManager->setPausedGarbageCan(mPlayerManager->getGarbageCanType());
        }
        delete mPlayerManager;
        mPlayerManager = 0;
    }

    if (mGameManager != 0)
    {
        if (appManager != NULL) {
            appManager->setPausedStage(mGameManager->getCurrentStage());
        }
        delete mGameManager;
        mGameManager = 0;
    }
}

/**
 * (サーバー連携によって変更の可能性大)
 * ローカルに保存していたデータをセットする
 */
void
commonSetInitData(double bonesStart, double addGarbageStart, double heroDrinkAppearTime, double zDrinkStartTime, double dropStartTime)
{
    if (mGameManager != 0)
    {
        mGameManager->setInitGameInfo(bonesStart, addGarbageStart, heroDrinkAppearTime, zDrinkStartTime, dropStartTime);
    }
}

/**
 * タイマー処理を行う
 * (1秒ごとに呼ぶ)
 */
void
commonOnCheckTime(int isForegroundFirst)
{
    if (mGameManager != 0)
    {
        mGameManager->onCheckTime(isForegroundFirst);
    }
}

/**
 * 所持宝石数を返す
 */
int
commonGetGem()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }
    return mPlayerManager->getGem();
}

/**
 * 所持ポイント数を返す
 */
int
commonGetPoint()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }
    return mPlayerManager->getPoint();
}

/**
 * レベルを返す
 */
int
commonGetLevel()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }

    return mPlayerManager->getLevel();
}

/**
 * 最高レベルを返す
 */
int
commonGetMaxLevel()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }

    return mPlayerManager->getMaxLevel();
}

/**
 * ゴミ箱の占有率を返す
 */
double
commonGetFullness()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }
    return mPlayerManager->getFullnessRate();
}

/**
 * 経験値を返す
 */
int
commonGetExperiencePoint()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }
    
    return mPlayerManager->getExperiencePoint();
}

/**
 * 現レベルの必要経験値を返す
 */
int
commonGetCurrentLevelRequiredPoint()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }
    
    return mPlayerManager->getCurrentLevelRequiredPoint();
}

/**
 * 次レベルの必要経験値を返す
 */
int
commonGetNextLevelRequiredPoint()
{
    if (mPlayerManager == 0)
    {
        return 0;
    }
    
    return mPlayerManager->getNextLevelRequiredPoint();
}
 
/**
 * 宝石数を増減する
 */
void
commonAddGem(int addValue)
{
    if (mPlayerManager != 0)
    {
        mPlayerManager->changeGem(addValue);
    }
}

/**
 * ポイント数を増減する
 */
void
commonAddPoint(int addValue)
{
    if (mPlayerManager != 0)
    {
        mPlayerManager->changePoint(addValue);
    }
}

/**
 * 現在の箒のタイプを返す
 */
int
commonGetCurrentBroomType()
{
    if (mGameManager != 0)
    {
        return mGameManager->getBroomType();
    }
    return eBroomType_Normal;
}

void
commonChangeCharacter(int type)
{
    if (mPlayerManager != 0)
    {
        mPlayerManager->setCharacter(type);
    }
}

/**
 * 箒のタイプを変更する
 */
void
commonChangeBroomType(int type)
{
    if (mGameManager != 0)
    {
        mGameManager->onChangeBroomType(type);
    }
}

/**
 * 現在のゴミ箱のタイプを返す
 */
int
commonGetCurrentGarbageCanType()
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->getGarbageCanType();
    }
    return eGarbageCanType_Normal;
}

/**
 * ゴミ箱のタイプを変更する
 */
void
commonChangeGarbageCanType(int type)
{
    if (mPlayerManager != 0)
    {
        mPlayerManager->changeGarbageCanType(type, mGameManager->getCurrentStage());
    }
}

/**
 * ステージを変更する
 */
void
commonChangeStage(int stage)
{
    if (appManager != 0)
    {
        appManager->startSwitchStage(stage, NULL);
    }
}

/**
 * 現在どのステージにいるか取得する
 */
int
commonGetCurrentStage()
{
    if (mGameManager != 0)
    {
        return mGameManager->getCurrentStage();
    }
    
    return eStage_Default;
}

/**
 * 封印シースを使用する
 */
void
commonUseSeal()
{
    if (mGameManager != 0)
    {
        mGameManager->onUsedSealItem();
    }
}

/**
 * 業者に電話を使用する
 */
void
commonUseTelephone()
{
    if (appManager != 0)
    {
        appManager->usedTelephoneItem();
    }
}

/**
 * /user_apps/self のレスポンスを解析して、Managerに渡す
 */
void
commonOnReceivedUserAppSelfResponse(std::string json)
{
//    LOGI("UserAppSelfResponse : %s", json.c_str());

    int jewel_count = 0;
    int point = 0;
    int total_point = 0;
    JsonUtils::parseUserAppSelfResponse(json, &jewel_count, &point, &total_point);

    if (mPlayerManager != 0)
    {
        mPlayerManager->setUserAppsSelfResponse(jewel_count, point, total_point);
    }
}

/**
 * /gomipoi_garbages/own のレスポンスを解析して、Managerに渡す
 */
void
commonOnReceivedGomipoiGarbageOwnResponse(std::string json)
{
//    LOGI("GomipoiGarbageOwnResponse : %s", json.c_str());

    std::vector<std::string> garbageOwnList;
    JsonUtils::parseGomipoiGarbageOwnResponse(json, &garbageOwnList);

    if (mGameManager != 0)
    {
        mGameManager->resetSynthesisSuccessList();
        int i;
        for (i = 0; i < garbageOwnList.size(); i++)
        {
            mGameManager->addSucceedSynthesisId(GarbageUtils::getGarbageId(garbageOwnList[i]));
        }
    }
}

/**
 * /gomipoi_games/load のレスポンスを解析して、Managerに渡す
 */
void
commonOnReceivedGomipoiGameLoadResponse(std::string json)
{
//    LOGI("GomipoiGameLoadResponse : %s", json.c_str());

    int level = 0;
    double gomi_gauge = 0.0;
    int experience_point = 0;
    int current_level_required_point = 0;
    int next_level_required_point = 0;
    std::vector<UsingItem *> usingItemList;
    std::vector<UsageLimitItem *> usageLimitItemList;
    std::string garbagesJson;
    double serverTime = 0.0;
    int jirokichi_jewel_count = 0;
    int server_place_type = 1;
    int placeType = eStage_Default;
    
    int friendCount = 0;
    int playingCharaType = 1;
    
    JsonUtils::parseGomipoiGameLoadResponse(json, &server_place_type, &level, &gomi_gauge, &experience_point, &current_level_required_point, &next_level_required_point, &usingItemList, &garbagesJson, &usageLimitItemList, &serverTime, &jirokichi_jewel_count, &friendCount, &playingCharaType);

    switch (server_place_type) {
        case 1:
            placeType = eStage_Default;
            break;
            
        case 2:
            placeType = eStage_PoikoRoom;
            break;
            
        case 3:
            placeType = eStage_Garden;
            break;
            
        default:
            break;
    }
    
    if (mPlayerManager != 0)
    {
        mPlayerManager->setGomipoiGameLoadResponse(level, serverTime, experience_point, current_level_required_point, next_level_required_point);
        mPlayerManager->setUsageLimitItems(usageLimitItemList);
        mPlayerManager->setFriendCount(friendCount);
        
        // キャラクターをセット
        switch (playingCharaType) {
            case 1:
                if (mPlayerManager != 0)
                {
                    mPlayerManager->setCharacter(eCharacter_Poiko);
                }
                break;
                
            case 2:
                if (mPlayerManager != 0)
                {
                    mPlayerManager->setCharacter(eCharacter_Oton);
                }
                break;
                
            case 3:
                if (mPlayerManager != 0)
                {
                    mPlayerManager->setCharacter(eCharacter_Kotatsu);
                }
                break;
                
            default:
                break;
        }

    }

    if (mGameManager != 0)
    {
        mGameManager->setIsOwnedAutoBroom(false);
    }
    
    bool isUsedDefaultBroom = true;
    int i;
    for (i = 0; i < usingItemList.size(); i++)
    {
        UsingItem *item = usingItemList[i];
        if (item != 0)
        {
            if (item->item_code == ITEM_CODE_BRROM_NORMAL)
            {
            }
            else if (item->item_code == ITEM_CODE_BRROM_SILVER)
            {
                if (mGameManager != 0)
                {
                    mGameManager->setBroom(eBroomType_Silver, item->use_count);
                }
                isUsedDefaultBroom = false;
            }
            else if (item->item_code == ITEM_CODE_BRROM_GOLD)
            {
                if (mGameManager != 0)
                {
                    mGameManager->setBroom(eBroomType_Gold, item->use_count);
                }
                isUsedDefaultBroom = false;
            }
            else if (item->item_code == ITEM_CODE_SEAL)
            {
                if (mGameManager != 0)
                {
                    mGameManager->setSealStartTime(item->used_at);
                }
            }
            else if (item->item_code == ITEM_CODE_TELEPHONE)
            {
                if (mPlayerManager != 0)
                {
                    mPlayerManager->onUsedTelephoneItem();
                }
            }
            
            else if (item->item_code == ITEM_CODE_AUTO_BRROM && placeType != eStage_Garden)
            {
                if (mGameManager != 0)
                {
                    mGameManager->onOwnedAutoBroom();
                }
            }
            else if (item->item_code == ITEM_CODE_BATTERY && placeType != eStage_Garden)
            {
                if (mGameManager != 0)
                {
                    mGameManager->setBatteryStartTime(item->used_at);
                }
            }
        }
    }

    if (isUsedDefaultBroom)
    {
        if (mGameManager != 0)
        {
            mGameManager->setBroom(eBroomType_Normal, 0);
        }
    }
    
    if (mPlayerManager != 0)
    {
        mPlayerManager->setUndergroundJewelCount(jirokichi_jewel_count);
    }
    
    tCallback_stageLayoutTransition callback = [&, gomi_gauge, placeType, garbagesJson, usingItemList]() {
        if (mGameManager != 0)
        {
            mGameManager->setGomipoiGameLoadResponse(garbagesJson);
        }
        
        bool isUsedDefaultGarbageCan = true;
        int i;
        for (i = 0; i < usingItemList.size(); i++)
        {
            UsingItem *item = usingItemList[i];
            if (item != 0)
            {
                if (placeType == eStage_Default) {
                    if (item->item_code == ITEM_CODE_GARBAGE_CAN_NORMAL)
                    {
                    }
                    else if (item->item_code == ITEM_CODE_GARBAGE_CAN_BIG)
                    {
                        if (mPlayerManager != 0)
                        {
                            mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_Big);
                        }
                        isUsedDefaultGarbageCan = false;
                    }
                    else if (item->item_code == ITEM_CODE_GARBAGE_CAN_HUGE)
                    {
                        if (mPlayerManager != 0)
                        {
                            mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_Huge);
                        }
                        isUsedDefaultGarbageCan = false;
                    }
                    else if (item->item_code == ITEM_CODE_GARBAGE_CAN_XL)
                    {
                        if (mPlayerManager != 0)
                        {
                            mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_XL);
                            mPlayerManager->setGarbageCanXlUsedCount(item->use_count);
                        }
                        isUsedDefaultGarbageCan = false;
                    }
                }
                else if (placeType == eStage_PoikoRoom) {
                    if (item->item_code == ITEM_CODE_GARBAGE_CAN_NORMAL_2)
                    {
                    }
                    else if (item->item_code == ITEM_CODE_GARBAGE_CAN_BIG_2)
                    {
                        if (mPlayerManager != 0)
                        {
                            mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_Big);
                        }
                        isUsedDefaultGarbageCan = false;
                    }
                    else if (item->item_code == ITEM_CODE_GARBAGE_CAN_HUGE_2)
                    {
                        if (mPlayerManager != 0)
                        {
                            mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_Huge);
                        }
                        isUsedDefaultGarbageCan = false;
                    }
                    else if (item->item_code == ITEM_CODE_GARBAGE_CAN_XL_ROOM)
                    {
                        if (mPlayerManager != 0)
                        {
                            mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_XL);
                            mPlayerManager->setGarbageCanXlUsedCount_Room(item->use_count);
                        }
                        isUsedDefaultGarbageCan = false;
                    }
                }
                else if (placeType == eStage_Garden) {
                    if (item->item_code == ITEM_CODE_GARDEN_GARBAGE_CAN)
                    {
                        if (mPlayerManager != 0)
                        {
                            mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_Big);
                        }
                        isUsedDefaultGarbageCan = false;
                    }
                }
            }
        }
        
        if (isUsedDefaultGarbageCan)
        {
            if (mPlayerManager != 0)
            {
                mPlayerManager->setCapacity(gomi_gauge, eGarbageCanType_Normal);
            }
        }
    };
    
    if (appManager != 0) {
        bool shouldSwitchStage = true;
#if !TEST_JIROKICHI
        if (jirokichi_jewel_count > 0) {
#endif
            if (mGameManager != 0 && mGameManager->isUndergroundStage()) {
                mGameManager->setCurrentStage(placeType);
                callback();
                shouldSwitchStage = false;
            }
#if !TEST_JIROKICHI
        }
        else {
            if (mGameManager != 0 && mGameManager->isUndergroundStage()) {
                mGameManager->setCurrentStage(placeType);
                callback();
                shouldSwitchStage = false;
                mGameManager->resetUndergroundStage();
                appManager->goToNormalStage();
            }
        }
#endif
        
        if (shouldSwitchStage) {
            appManager->startSwitchStage(placeType, callback);
        }
    }
    else {
        if (mGameManager != 0)
        {
            mGameManager->setCurrentStage(placeType);
            callback();
        }
    }
    
}

/**
 * /gomipoi_items/own のレスポンスを解析して、Managerに渡す
 */
void
commonOnReceivedGomipoiItemOwnResponse(std::string json)
{
//    LOGI("GomipoiItemOwnResponse : %s", json.c_str());

    std::vector<Item *> itemList;
    double serverTime = 0.0;
    JsonUtils::parseGomipoiItemOwnResponse(json, &itemList, &serverTime);

    if (mPlayerManager != 0)
    {
        mPlayerManager->setOwnItem(itemList);
    }
}

/**
 * アイテムの所持数を返す
 */
int
commonGetItemOwnCount(std::string item_code)
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->getItemOwnCount(item_code);
    }
    return 0;
}

/**
 * アイテムが使用中かを返す
 */
bool
commonIsItemUsing(std::string item_code)
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->isItemOwnUsing(item_code);
    }
    return false;
}

bool
commonIsLimitItemUsing(std::string item_code)
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->isLimitItemUsage(item_code);
    }
    return false;
}

/**
 * gomipoi_books/own のレスポンスを解析して、Managerに渡す
 */
void
commonOnReceivedGomipoiBookOwnResponse(std::string json)
{
//    LOGI("GomipoiBookOwnResponse : %s", json.c_str());

    std::vector<BookGarbage *> garbages;
    std::vector<int> received_book_bonuses;
    JsonUtils::parseGomipoiBookOwnResponse(json, &garbages, &received_book_bonuses);

    if (mPlayerManager != 0)
    {
        mPlayerManager->setBookGarbages(garbages);
        mPlayerManager->setReceivedBonusPages(received_book_bonuses);
    }
}

/**
 * gomipoi_garbages/syntheses のレスポンスを解析して、Managerに戻す
 */
void
commonOnReceivedGomipoiGarbageSynthesesResponse(std::string json)
{
//    LOGI("GomipoiGarbageSynthesesResponse : %s", json.c_str());

    std::string garbage_code;
    JsonUtils::parseGomipoiGarbagesSynthesesResponse(json, &garbage_code);

    int garbageId = GarbageUtils::getGarbageId(garbage_code);
    if (garbageId > 0)
    {
        if (mGameManager != 0)
        {
            mGameManager->addSucceedSynthesisId(garbageId);
        }

        // 成功通知
        nativeCallOnSucceededSyntheses(garbageId);
    }
}

/**
 * gomipoi_games/save のレスポンスを解析して、Managerに戻す
 */
void
commonOnReceivedGomipoiGameSaveResponse(std::string json)
{
//    LOGI("GomipoiGameSaveResponse : %s", json.c_str());

    int new_level = 0;
    int jirokichi_jewel_count = 0;
    int experience_point = 0;
    int current_level_required_point = 0;
    int next_level_required_point = 0;
    JsonUtils::parseGomipoiGameSaveResponse(json, &new_level, &jirokichi_jewel_count, &experience_point, &current_level_required_point, &next_level_required_point);

    if (new_level > 0)
    {
        if (mPlayerManager != 0)
        {
            mPlayerManager->setLevel(new_level);
        }

        nativeCallOnChangedLevel(new_level);
    }
    
    if (mPlayerManager != 0)
    {
        mPlayerManager->setExperiencePoint(experience_point);
        mPlayerManager->setCurrentLevelRequiredPoint(current_level_required_point);
        mPlayerManager->setNextLevelRequiredPoint(next_level_required_point);
        mPlayerManager->setUndergroundJewelCount(jirokichi_jewel_count);
    }
}

/**
 * 図鑑情報（GarbageIdの配列）を返す
 */
std::vector<int>
commonGetBookGarbages()
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->getBookGarbages();
    }
    return std::vector<int>();
}

/**
 * 図鑑のロックが解除されているかを返す
 */
bool
commonIsUnlockBook(std::string garbage_code)
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->isUnlocked(garbage_code);
    }
    return false;
}

/**
 * 図鑑のNewがついているかを返す
 */
bool
commonIsNewBook(std::string garbage_code)
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->isNew(garbage_code);
    }
    return false;
}

/**
 * 合成でできたレアなゴミかどうか返す
 */
bool
commonIsRareGarbage(std::string garbage_code)
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->isRareGarbage(garbage_code);
    }
    return false;
}

/**
 * ボーナスを取得可能なページ番号のリストを返す
 */
std::vector<int>
commonGetPageBonus()
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->getPageBonus();
    }
    return std::vector<int>();
}

/**
 * ページボーナス取得時の処理を行う
 */
void
commonOnReceivedBookReceiveBonusesRespnse(std::string json)
{
//    LOGI("BookReceiveBonusesRespnse : %s", json.c_str());

    std::vector<int> receivedPages;
    JsonUtils::parseGomipoiBookReceiveBonuses(json, &receivedPages);
    if (mPlayerManager != 0)
    {
        mPlayerManager->onReceivedPageBonus(receivedPages);
    }

}

/**
 * データ取得の開始時に呼ばれる(タイマー処理等をストップする)
 */
void
commonOnStartGetData()
{
    mIsGettingData = true;
}

/**
 * データ取得の完了時に呼ばれる(タイマー処理等を開始する)
 */
void
commonOnFinishedGetData()
{
    mIsGettingData = false;

    if (isGameActive())
    {
        if (appManager != 0)
        {
            appManager->checkMother();
            appManager->startAutoBroomWaitingAnimation();
        }
    }
}

/**
 * 新規発見ゴミのゴミコードのカンマ区切り文字列を返す
 */
std::string
commonGetNewFoundGarbages()
{
    if (mGameManager == 0)
    {
        return "";
    }
    return mGameManager->getNewFoundGarbages();
}

void
commonLogout()
{
    if (mGameManager != 0)
    {
        mGameManager->logout();
    }
}

std::string
commonGetCurrentDate()
{
    if (mPlayerManager != 0)
    {
        double currentTime = mPlayerManager->getCurrentTime();

        char timestamp[20] = "";
        time_t secs = (currentTime / 1000) + (60 * 60 * 9);
        tm *ptm = localtime(&secs);
        size_t len = strftime(timestamp, 20, "%Y-%m-%d", ptm);

        return std::string(timestamp);
    }

    return "";
}

void
commonOnUsedZDrink()
{
    if (mGameManager != 0)
    {
        mGameManager->onUsedZDrinkItem();
    }
    if (mPlayerManager != 0)
    {
        mPlayerManager->onUsedZDrink();
    }
}

void
commonOnUsedDrop()
{
    if (mGameManager != 0)
    {
        mGameManager->onUsedDropItem();
    }
    if (mPlayerManager != 0)
    {
        mPlayerManager->onUsedDrop();
    }
    
    // 変身ドロップでリストを変えたので、更新
    if (appManager != 0)
    {
        if (mGameManager != 0)
        {
            mGameManager->changeSwipeState(eSwipeState_OnRefreshed);
        }
        appManager->refreshGarbage();
    }
}

void
commonOnUsedAutoBroom()
{
    if (mGameManager != 0)
    {
        mGameManager->onOwnedAutoBroom();
    }
    if (mGameManager != 0)
    {
        mGameManager->onUsedBattery();
    }
    if (appManager != 0)
    {
        appManager->refreshAutoBroom();
    }
}

void
commonOnUsedBattery()
{
    if (mGameManager != 0)
    {
        mGameManager->onUsedBattery();
    }
    if (appManager != 0)
    {
        appManager->refreshAutoBroom();
    }
}

int
commonGetFriendCount()
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->getFriendCount();
    }
    return 0;
}

bool
commonIsUsedZDrink()
{
    if (mGameManager != 0)
    {
        return mGameManager->isZDrinkTime();
    }
    return false;
}

bool
commonIsUsedDrop()
{
    if (mGameManager != 0)
    {
        return mGameManager->isDropTime();
    }
    return false;
}

bool
commonIsBonusTime()
{
    if (mGameManager != 0)
    {
        return mGameManager->isBonusTime();
    }
    return false;
}

double
commonGetCurrentTime()
{
    if (mPlayerManager != 0)
    {
        return mPlayerManager->getCurrentTime();
    }
    return 0;
}

// iOS側で使用
int
commonGetGarbageId(std::string garbageCode)
{
    return GarbageUtils::getGarbageId(garbageCode);
}

// iOS側で使用
std::string
commonGetGarbageCode(int garbage_id)
{
    return GarbageUtils::getGarbageCode(garbage_id);
}

// iOS側で使用
int
commonGetGarbageBonus(int garbage_id)
{
    return GarbageData::makeGarbageData(garbage_id)->getBonus();
}

// iOS側で使用
std::string
commonMissionIdToItemCode(int mission_id)
{
    return SecretMission::missionIdToItemCode(mission_id);
}

bool
nativeCallInterceptOnPartsClicked(int partsId)
{
    if (partsId == ePartsID_hero_drink)
    {
        if (appManager != 0)
        {
            appManager->usedHeroDrink();
        }
        
        if (mGameManager != 0)
        {
            mGameManager->onUsedBonusTimeItem();
        }
        return true;
    }
    
    if (partsId == ePartsID_hole) {
        if (appManager != 0)
        {
            appManager->enteringHole();
        }
        return true;
    }
    
    return false;
}

#if IS_ANDROID

// TODO Android特有の処理

#include <jni.h>

// ------------------------------
// JNI Function Define
// ------------------------------
#ifdef __cplusplus
extern "C" {
#endif

//
// Lib
// ------------------------------
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnParentCreated(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnParentDestroy(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnSurfaceCreated(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnSurfaceChanged(JNIEnv* env, jobject thiz, jint width, jint height);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnDrawFrame(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnTouchDown(JNIEnv* env, jobject thiz, jfloat ptX, jfloat ptY);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnTouchMove(JNIEnv* env, jobject thiz, jfloat ptX, jfloat ptY);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeOnTouchUp(JNIEnv* env, jobject thiz);
JNIEXPORT jboolean JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeIsPausing(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativePauseEnd(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativePause(JNIEnv* env, jobject thiz);
JNIEXPORT jboolean JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeIsSuspending(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeForeground(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_lib_opengl_jni_GLJniBridgeBase_nativeBackground(JNIEnv* env, jobject thiz);

//
// App
// ------------------------------
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeForegroundApplication(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeBackgroundApplication(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnCheckTime(JNIEnv* env, jobject thiz, jint isForegroundFirst);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetGem(JNIEnv* env, jobject thiz);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetPoint(JNIEnv* env, jobject thiz);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetLevel(JNIEnv* env, jobject thiz);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetMaxLevel(JNIEnv* env, jobject thiz);
JNIEXPORT jdouble JNICALL Java_app_jni_JniBridge_nativeGetFullness(JNIEnv* env, jobject thiz);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetExperiencePoint(JNIEnv* env, jobject thiz);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetCurrentLevelRequiredPoint(JNIEnv* env, jobject thiz);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetNextLevelRequiredPoint(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeSetInitData(JNIEnv* env, jobject thiz, jdouble bonesStart, jdouble addGarbageStart, jdouble heroDrinkAppearTime, jdouble zDrinkStartTime, jdouble dropStartTime);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeAddGem(JNIEnv* env, jobject thiz, jint addValue);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeAddPoint(JNIEnv* env, jobject thiz, jint addValue);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetCurrentBroomType(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeChangeCharacter(JNIEnv* env, jobject thiz, jint type);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeChangeBroomType(JNIEnv* env, jobject thiz, jint type);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetCurrentGarbageCanType(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeChangeGarbageCanType(JNIEnv* env, jobject thiz, jint type);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeChangeStage(JNIEnv* env, jobject thiz, jint stage);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeSaveData(JNIEnv* env, jobject thiz, jint nextStage);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetCurrentStage(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeUseSeal(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeUseTelephone(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedUserAppsSelfResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedGomipoiGarbageOwnResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedGomipoiGameLoadResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedGomipoiItemOwnResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetItemOwnCount(JNIEnv* env, jobject thiz, jstring item_code);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsItemUsing(JNIEnv* env, jobject thiz, jstring item_code);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsLimitItemUsing(JNIEnv* env, jobject thiz, jstring item_code);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedGomipoiBookOwnResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedGomipoiGarbageSynthesesResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedGomipoiGameSaveResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsUnlockBook(JNIEnv* env, jobject thiz, jstring garbage_code);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsNewBook(JNIEnv* env, jobject thiz, jstring garbage_code);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsRareGarbage(JNIEnv* env, jobject thiz, jstring garbage_code);
JNIEXPORT jintArray JNICALL Java_app_jni_JniBridge_nativeGetPageBonus(JNIEnv* env, jobject thiz);
JNIEXPORT jintArray JNICALL Java_app_jni_JniBridge_nativeGetBookGarbages(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnReceivedBookReceiveBonusesResponse(JNIEnv* env, jobject thiz, jstring json);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnStartGetData(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnFinishedGetData(JNIEnv* env, jobject thiz);
JNIEXPORT jint JNICALL Java_app_jni_JniBridge_nativeGetGarbageBonus(JNIEnv* env, jobject thiz, jint garbage_id);
JNIEXPORT jstring JNICALL Java_app_jni_JniBridge_nativeGetNewFoundGarbages(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeLogout(JNIEnv* env, jobject thiz);
JNIEXPORT jstring JNICALL Java_app_jni_JniBridge_nativeGetCurrentDate(JNIEnv* env, jobject thiz);

JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnUsedZDrink(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnUsedDrop(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnUsedAutoBroom(JNIEnv* env, jobject thiz);
JNIEXPORT void JNICALL Java_app_jni_JniBridge_nativeOnUsedBattery(JNIEnv* env, jobject thiz);
JNIEXPORT int JNICALL Java_app_jni_JniBridge_nativeGetFriendCount(JNIEnv* env, jobject thiz);
    
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsUsedZDrink(JNIEnv* env, jobject thiz);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsUsedDrop(JNIEnv* env, jobject thiz);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsBonusTime(JNIEnv* env, jobject thiz);
JNIEXPORT jdouble JNICALL Java_app_jni_JniBridge_nativeGetCurrentTime(JNIEnv* env, jobject thiz);
JNIEXPORT jboolean JNICALL Java_app_jni_JniBridge_nativeIsGameActive(JNIEnv* env, jobject thiz);

#ifdef __cplusplus
}
#endif

// ------------------------------
// Member
// ------------------------------
JavaVM* mJavaVM;
jobject mThiz;

// ------------------------------
// Function
// ------------------------------
/**
 * Jniロード時に呼ばれる
 */
int
JNI_OnLoad(JavaVM *vm, void *reserved)
{
    mJavaVM = vm;
    return JNI_VERSION_1_6;
}

// ------------------------------
// Java Call
// ------------------------------
//
// Lib
// ------------------------------
/**
 * JNIEnvを取得する
 */
JNIEnv *
getJNIEnv()
{
    if (mJavaVM == 0 || mJavaVM == NULL)
    {
        return NULL;
    }

    JNIEnv *env;
    int ret = mJavaVM->GetEnv((void **)&env, JNI_VERSION_1_6);
    if(ret < 0)
    {
        //取得失敗
        LOGE("Failed to get JNIEnv");
        return  NULL;
    }

    return  env;
}

/**
 * [Java連携]
 * JavaからAssetManagerを受け取る
 */
void
setAssetManager()
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getAssetManagerMethodID = env->GetMethodID(
            activityClass,
            "getAssetManager",
            "()Landroid/content/res/AssetManager;");
    if (getAssetManagerMethodID == 0)
    {
        LOGE("Function getAssetManager() not found.");
        return;
    }
    jobject objAsset = env->CallObjectMethod(mThiz, getAssetManagerMethodID);
    appManager->setAssetManager(env, objAsset);
}

/**
 * [Java連携]
 * イベントロック中かを返す
 */
bool
nativeCallIsLockedEvent()
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return false;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "isLockedEvent",
            "()Z");

    if (getMethodID == 0)
    {
        LOGE("Function isLockedEvent() not found.");
        return false;
    }

    return env->CallBooleanMethod(mThiz, getMethodID);
}

// CommonAPI側で制御するので、コメントアウト
///**
// * [Java連携]
// * 一時停止中かを返す
// */
//bool
//nativeCallIsPausing()
//{
//    JNIEnv *env = getJNIEnv();
//    if (env == NULL || mThiz == 0)
//    {
//        LOGE("Env is Null");
//        return false;
//    }
//
//    jclass activityClass = env->GetObjectClass(mThiz);
//    jmethodID getMethodID = env->GetMethodID(
//            activityClass,
//            "isPausing",
//            "()Z");
//
//    if (getMethodID == 0)
//    {
//        LOGE("Function isPausing() not found.");
//        return false;
//    }
//
//    bool result = env->CallBooleanMethod(mThiz, getMethodID);
//
//    return result;
//}

///**
// * [Java連携]
// * サスペンド中かを返す
// */
//bool
//nativeCallIsSuspending()
//{
//    JNIEnv *env = getJNIEnv();
//    if (env == NULL || mThiz == 0)
//    {
//        LOGE("Env is Null");
//        return false;
//    }
//
//    jclass activityClass = env->GetObjectClass(mThiz);
//    jmethodID getMethodID = env->GetMethodID(
//            activityClass,
//            "isSuspending",
//            "()Z");
//
//    if (getMethodID == 0)
//    {
//        LOGE("Function isSuspending() not found.");
//        return false;
//    }
//
//    return env->CallBooleanMethod(mThiz, getMethodID);
//}

/**
 * [Java連携]
 * Loading中かを返す
 */
bool
nativeCallIsLoading()
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return false;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "isLoading",
            "()Z");

    if (getMethodID == 0)
    {
        LOGE("Function isLoading() not found.");
        return false;
    }

    return env->CallBooleanMethod(mThiz, getMethodID);
}

/**
 * [Java連携]
 * ローティングの終了を通知する
 */
void
nativeCallOnLoadingEnd()
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onLoadingEnd",
            "()V");
    if (getMethodID == 0)
    {
        LOGE("Function onLoadingEnd() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID);
}

/**
 * [Java連携]
 * パーツのクリックイベントを伝える
 */
void
nativeCallOnPartsClicked(int partsId)
{
    if (nativeCallInterceptOnPartsClicked(partsId))
    {
        return;
    }

    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onPartsClicked",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onPartsClicked() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID, partsId);
}

/**
 * [Java連携]
 * パーツのイベントを伝える
 */
void
nativeCallOnPartsEvent(int eventId)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onPartsEvent",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onPartsEvent() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID, eventId);
}

//
// App
// ------------------------------
/**
 * [Java連携]
 * 所持宝石数の変更を伝える
 */
void
nativeCallOnChangedGem(int gem)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onChangedGem",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onSaveGameData() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID, gem);
}

/**
 * [Java連携]
 * 所持ポイント数の変更を伝える
 */
void
nativeCallOnChangedPoint(int point)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onChangedPoint",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onChangedPoint() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID, point);
}

/**
 * [Java連携]
 * ゴミ箱の占有率の変更を伝える
 */
void
nativeCallOnChangedFullness(double fullness)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onChangedFullness",
            "(D)V");
    if (getMethodID == 0)
    {
        LOGE("Function onChangedFullness() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID, fullness);
}

/**
 * [Java連携]
 * レベルの変更を伝える
 */
void
nativeCallOnChangedLevel(int level)
{
    // レベルアップ表示
    if (appManager != 0)
    {
        appManager->levelup();
    }

    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onChangedLevel",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onChangedLevel() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID, level);

}

/**
 * [Java連携]
 * 箒が壊れたことを伝える
 */
void
nativeCallOnBrokenBroom(int broomType)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onBrokenBroom",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onBrokenBroom() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID, broomType);
}

/**
 * [Java連携]
 */
void
nativeCallOnBrokenGarbageCanXl()
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onBrokenGarbageCanXl",
            "()V");
    if (getMethodID == 0)
    {
        LOGE("Function onBrokenBroom() not found.");
        return;
    }
    env->CallVoidMethod(mThiz, getMethodID);
}

/**
 * [Java連携]
 * ポイントを取得したことを伝える
 * (+〇〇Pを表示する時に呼ばれる)
 */
void
nativeCallOnGetPoint(int point)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onGetPoint",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onGetPoint() not found.");
        return;
    }
    env->CallVoidMethod(
        mThiz,
        getMethodID,
        point);
}

/**
 * [Java連携]
 * ポイントを取得したことを伝える
 * (+〇〇Pを表示する時に呼ばれる)
 */
void
nativeCallOnGetGem(int gem)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }
    
    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
                                             activityClass,
                                             "onGetGem",
                                             "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onGetGem() not found.");
        return;
    }
    env->CallVoidMethod(
                        mThiz,
                        getMethodID,
                        gem);
}

/**
 * [Java連携]
 * コンボボーナスを取得したことを伝える
 * (コンボボーナスを表示する時に呼ばれる)
 */
void
nativeCallOnGetComboBonus(int comboCount, int point)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onGetComboBonus",
            "(II)V");
    if (getMethodID == 0)
    {
        LOGE("Function onGetComboBonus() not found.");
        return;
    }
    env->CallVoidMethod(
        mThiz,
        getMethodID,
        comboCount,
        point);
}

/**
 * [Java連携]
 * ひみつのミッションをクリアしたことを伝える
 */
void
nativeCallOnClearMission(int missionId)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }
    
    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
                                             activityClass,
                                             "onClearMission",
                                             "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onClearMission() not found.");
        return;
    }
    env->CallVoidMethod(
                        mThiz,
                        getMethodID,
                        missionId);
}

/**
 * [Java連携]
 * ジロキチの隠れ家の宝石を全てとったことを伝える
 */
void
nativeCallOnUndergroundGemGot(int gem)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }
    
    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
                                             activityClass,
                                             "onUndergroundGemGot",
                                             "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onUndergroundGemGot() not found.");
        return;
    }
    env->CallVoidMethod(
                        mThiz,
                        getMethodID,
                        gem);
}

/**
 * [Java連携]
 * 合成に成功した時に呼ばれる
 */
void
nativeCallOnSucceededSyntheses(int garbageId)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onSucceededSyntheses",
            "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onSucceededSyntheses() not found.");
        return;
    }
    env->CallVoidMethod(
        mThiz,
        getMethodID,
        garbageId);
}

void nativeCallOnEnterUnderground(bool enterJirokichi)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }
    
    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
                                             activityClass,
                                             "onEnterUnderground",
                                             "(Z)V");
    if (getMethodID == 0)
    {
        LOGE("Function onEnterUnderground() not found.");
        return;
    }
    env->CallVoidMethod(
                        mThiz,
                        getMethodID,
                        enterJirokichi);
}

void nativeCallOnChangeStage(int stageId)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }
    
    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
                                             activityClass,
                                             "onChangeStage",
                                             "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onChangeStage() not found.");
        return;
    }
    env->CallVoidMethod(
                        mThiz,
                        getMethodID,
                        stageId);
}

void nativeCallOnRemainingBonusTime(int remainingSeconds)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }
    
    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
                                             activityClass,
                                             "onRemainingBonusTime",
                                             "(I)V");
    if (getMethodID == 0)
    {
        LOGE("Function onRemainingBonusTime() not found.");
        return;
    }
    env->CallVoidMethod(
                        mThiz,
                        getMethodID,
                        remainingSeconds);
}

/**
 * [Java連携]
 * 情報をPreferenceに保存する
 */
void
nativeCallOnSaveOtherInfo()
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onSaveOtherInfo",
            "(DDDDD)V");
    if (getMethodID == 0)
    {
        LOGE("Function onSaveOtherInfo() not found.");
        return;
    }
    env->CallVoidMethod(
            mThiz,
            getMethodID,
            mGameManager->getBonusStartTime(),
            mGameManager->getAddGarbageStartTime(),
            mGameManager->getHeroDrinkAppearTime(),
            mGameManager->getZDrinkStartTime(),
            mGameManager->getDropStartTime()
            );
}

/**
 * [Java連携]
 * ゲーム情報を保存する
 */
void
nativeCallOnSaveGameInfo(int placeType, int addPoint, int completedCount, int sweepCount, bool isBrokenBroom, int nextStage, bool isGarbageCanBroken)
{
    JNIEnv *env = getJNIEnv();
    if (env == NULL || mThiz == 0)
    {
        LOGE("Env is Null");
        return;
    }

    jclass activityClass = env->GetObjectClass(mThiz);
    jmethodID getMethodID = env->GetMethodID(
            activityClass,
            "onSaveGameData",
            "(IIILjava/lang/String;IIII)V");
    if (getMethodID == 0)
    {
        LOGE("Function onSaveGameData() not found.");
        return;
    }

//    LOGI("[saveData] point = %d, complete = %d, sweepCount = %d, isBroken = %d", addPoint, completedCount, sweepCount, isBrokenBroom ? 1 : 0);
    env->CallVoidMethod(
        mThiz,
        getMethodID,
        placeType,
        addPoint,
        completedCount,
        env->NewStringUTF(mGameManager->getGarbagesJson().c_str()),
        sweepCount,
        (isBrokenBroom ? 1 : 0),
        nextStage,
        (isGarbageCanBroken ? 1 : 0));
}

// ------------------------------
// Jni Called
// ------------------------------
//
// Lib
// ------------------------------
JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnParentCreated(JNIEnv* env, jobject thiz)
{
    // 初期化
//    mThiz = env->NewGlobalRef(thiz);
    commonOnParentCreated();
    setAssetManager();
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnParentDestroy(JNIEnv* env, jobject thiz)
{
    commonOnParentDestroy();
//    mThiz = 0;
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnSurfaceCreated(JNIEnv* env, jobject thiz)
{
    commonOnSurfaceCreated();
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnSurfaceChanged(JNIEnv* env, jobject thiz,
                                                                   jint width, jint height)
{
    commonOnSurfaceChanged(width, height);
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnDrawFrame(JNIEnv* env, jobject thiz)
{
    commonOnDrawFrame();
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnTouchDown(JNIEnv* env, jobject thiz,
        jfloat ptX, jfloat ptY)
{
    commonOnTouchDown(ptX, ptY);
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnTouchMove(JNIEnv* env, jobject thiz,
        jfloat ptX, jfloat ptY)
{
    commonOnTouchMove(ptX, ptY);
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeOnTouchUp(JNIEnv* env, jobject thiz)
{
    commonOnTouchUp();
}

JNIEXPORT jboolean JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeIsPausing(JNIEnv* env, jobject thiz)
{
    return commonIsPausing();
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativePauseEnd(JNIEnv* env, jobject thiz)
{
    commonPauseEnd();
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativePause(JNIEnv* env, jobject thiz)
{
    commonPause();
}

JNIEXPORT jboolean JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeIsSuspending(JNIEnv* env, jobject thiz)
{
    return commonIsSuspending();
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeForeground(JNIEnv* env, jobject thiz)
{
    commonForeground();
}

JNIEXPORT void JNICALL
Java_lib_opengl_jni_GLJniBridgeBase_nativeBackground(JNIEnv* env, jobject thiz)
{
    commonBackground();
}

//
// App
// ------------------------------
/**
 * アプリケーションがバックグランドから出てきた時に呼ぶ
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeForegroundApplication(JNIEnv* env, jobject thiz)
{
    mThiz = env->NewGlobalRef(thiz);
    commonForegroundApplication();
}

/**
 * アプリケーションがバックグラウンドに入った時に呼ぶ
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeBackgroundApplication(JNIEnv* env, jobject thiz)
{
    commonBackgroundApplication();
    env->DeleteGlobalRef(mThiz);
    mThiz = 0;
}

/**
 * タイマー処理(一定の間隔でこの関数を呼ぶ)
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnCheckTime(JNIEnv* env, jobject thiz, jint isForegroundFirst)
{
    commonOnCheckTime(isForegroundFirst);
}

/**
 * 宝石数を返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetGem(JNIEnv* env, jobject thiz)
{
    return commonGetGem();
}

/**
 * ポイント数を返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetPoint(JNIEnv* env, jobject thiz)
{
    return commonGetPoint();
}

/**
 * レベルを返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetLevel(JNIEnv* env, jobject thiz)
{
    return commonGetLevel();
}

/**
 * 最高レベルを返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetMaxLevel(JNIEnv* env, jobject thiz)
{
    return commonGetMaxLevel();
}

/**
 * 容量の率を返す
 */
JNIEXPORT jdouble JNICALL
Java_app_jni_JniBridge_nativeGetFullness(JNIEnv* env, jobject thiz)
{
    return commonGetFullness();
}

/**
 * 経験値を返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetExperiencePoint(JNIEnv* env, jobject thiz)
{
    return commonGetExperiencePoint();
}

/**
 * 現レベルの必要経験値を返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetCurrentLevelRequiredPoint(JNIEnv* env, jobject thiz)
{
    return commonGetCurrentLevelRequiredPoint();
}

/**
 * 次レベルの必要経験値を返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetNextLevelRequiredPoint(JNIEnv* env, jobject thiz)
{
    return commonGetNextLevelRequiredPoint();
}

/**
 * ユーザーデータをセットする
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeSetInitData(JNIEnv* env, jobject thiz, jdouble bonesStart,
        jdouble addGarbageStart, jdouble heroDrinkAppearTime, jdouble zDrinkStartTime, jdouble dropStartTime)
{
    commonSetInitData(bonesStart, addGarbageStart, heroDrinkAppearTime, zDrinkStartTime, dropStartTime);
}

/**
 * 宝石数を追加する
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeAddGem(JNIEnv* env, jobject thiz, jint addValue)
{
    commonAddGem(addValue);
}

/**
 * ポイント数を追加する
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeAddPoint(JNIEnv* env, jobject thiz, jint addValue)
{
    commonAddPoint(addValue);
}

/**
 * 箒のタイプを返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetCurrentBroomType(JNIEnv* env, jobject thiz)
{
    return commonGetCurrentBroomType();
}

/**
 * キャラクターのタイプを変更する
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeChangeCharacter(JNIEnv* env, jobject thiz, jint type)
{
    return commonChangeCharacter(type);
}

/**
 * 箒のタイプを変更する
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeChangeBroomType(JNIEnv* env, jobject thiz, jint type)
{
    return commonChangeBroomType(type);
}

/**
 * ゴミ箱のタイプを返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetCurrentGarbageCanType(JNIEnv* env, jobject thiz)
{
    return commonGetCurrentGarbageCanType();
}

/**
 * ゴミ箱のタイプを変更する
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeChangeGarbageCanType(JNIEnv* env, jobject thiz, jint type)
{
    return commonChangeGarbageCanType(type);
}

/**
 * ステージを変更する
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeChangeStage(JNIEnv* env, jobject thiz, jint stage)
{
    return commonChangeStage(stage);
}

/**
 * データを保存する
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeSaveData(JNIEnv* env, jobject thiz, jint nextStage)
{
    return commonSaveData(nextStage);
}

/**
 * ステージを変更する
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetCurrentStage(JNIEnv* env, jobject thiz)
{
    return commonGetCurrentStage();
}

/**
 * 封印シール使用時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeUseSeal(JNIEnv* env, jobject thiz)
{
    commonUseSeal();
}

/**
 * 電話アイテム使用時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeUseTelephone(JNIEnv* env, jobject thiz)
{
    commonUseTelephone();
}

/**
 * /user_apps/self のレスポンスを受け取った時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedUserAppsSelfResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedUserAppSelfResponse(string);
}

/**
 * /gomipoi_garbages/own のレスポンスを受け取った時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedGomipoiGarbageOwnResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedGomipoiGarbageOwnResponse(string);
}

/**
 * /gomipoi_games/load のレスポンスを受け取った時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedGomipoiGameLoadResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedGomipoiGameLoadResponse(string);
}

/**
 * /gomipoi_items/own のレスポンスを受け取った時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedGomipoiItemOwnResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedGomipoiItemOwnResponse(string);
}

/**
 * アイテムコードに対応するアイテムの所持数を返す
 */
JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetItemOwnCount(JNIEnv* env, jobject thiz, jstring item_code)
{
    const char* str = env->GetStringUTFChars(item_code, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(item_code, str);

    return commonGetItemOwnCount(string);
}

/**
 * アイテムコードに対応するアイテムが現在使用中かを返す
 */
JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsItemUsing(JNIEnv* env, jobject thiz, jstring item_code)
{
    const char* str = env->GetStringUTFChars(item_code, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(item_code, str);

    return commonIsItemUsing(string);
}

JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsLimitItemUsing(JNIEnv* env, jobject thiz, jstring item_code)
{
    const char* str = env->GetStringUTFChars(item_code, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(item_code, str);

    return commonIsLimitItemUsing(string);
}

/**
 * gomipoi_books/own　のレスポンスを受け取った時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedGomipoiBookOwnResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedGomipoiBookOwnResponse(string);
}

/**
 * gomipoi_garbages/syntheses　のレスポンスを受け取った時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedGomipoiGarbageSynthesesResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedGomipoiGarbageSynthesesResponse(string);
}

/**
 * gomipoi_games/save　のレスポンスを受け取った時に呼ばれる
 */
JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedGomipoiGameSaveResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedGomipoiGameSaveResponse(string);
}

/**
 * ゴミコードに対応するゴミが既に取得済みかを返す
 */
JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsUnlockBook(JNIEnv* env, jobject thiz, jstring garbage_code)
{
    const char* str = env->GetStringUTFChars(garbage_code, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(garbage_code, str);

    return commonIsUnlockBook(string);
}

/**
 * ゴミコードに対応するゴミがNewかを返す
 */
JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsNewBook(JNIEnv* env, jobject thiz, jstring garbage_code)
{
    const char* str = env->GetStringUTFChars(garbage_code, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(garbage_code, str);

    return commonIsNewBook(string);
}

JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsRareGarbage(JNIEnv* env, jobject thiz, jstring garbage_code)
{
    const char* str = env->GetStringUTFChars(garbage_code, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(garbage_code, str);
    
    return commonIsRareGarbage(string);
}

/**
 * 受け取り可能なページボーナスのページNo.の一覧を返す
 */
JNIEXPORT jintArray JNICALL
Java_app_jni_JniBridge_nativeGetPageBonus(JNIEnv* env, jobject thiz)
{
    // 判定処理
    std::vector<int> result = commonGetPageBonus();

    // std::vector -> jintArray
    jintArray returnArray = env->NewIntArray(result.size());
    jint* b = env->GetIntArrayElements(returnArray, 0);
    int i;
    for(i = 0; i < result.size(); i++){
        b[i] = (jint)result[i];
    }
    env->ReleaseIntArrayElements(returnArray, b, 0);

    return returnArray;

}

/**
 * 図鑑情報（GarbageIdの配列）を返す
 */
JNIEXPORT jintArray JNICALL
Java_app_jni_JniBridge_nativeGetBookGarbages(JNIEnv* env, jobject thiz)
{
    // 判定処理
    std::vector<int> result = commonGetBookGarbages();
    
    // std::vector -> jintArray
    jintArray returnArray = env->NewIntArray(result.size());
    jint* b = env->GetIntArrayElements(returnArray, 0);
    int i;
    for(i = 0; i < result.size(); i++){
        b[i] = (jint)result[i];
    }
    env->ReleaseIntArrayElements(returnArray, b, 0);
    
    return returnArray;
    
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnReceivedBookReceiveBonusesResponse(JNIEnv* env, jobject thiz, jstring json)
{
    const char* str = env->GetStringUTFChars(json, NULL);
    std::string string = str;
    env->ReleaseStringUTFChars(json, str);

    commonOnReceivedBookReceiveBonusesRespnse(string);
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnStartGetData(JNIEnv* env, jobject thiz)
{
    commonOnStartGetData();
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnFinishedGetData(JNIEnv* env, jobject thiz)
{
    commonOnFinishedGetData();
}

JNIEXPORT jint JNICALL
Java_app_jni_JniBridge_nativeGetGarbageBonus(JNIEnv* env, jobject thiz, jint garbage_id)
{
    return commonGetGarbageBonus((int) garbage_id);
}

JNIEXPORT jstring JNICALL
Java_app_jni_JniBridge_nativeGetNewFoundGarbages(JNIEnv* env, jobject thiz)
{
    return env->NewStringUTF(commonGetNewFoundGarbages().c_str());
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeLogout(JNIEnv* env, jobject thiz)
{
    commonLogout();
}

JNIEXPORT jstring JNICALL
Java_app_jni_JniBridge_nativeGetCurrentDate(JNIEnv* env, jobject thiz)
{
    return env->NewStringUTF(commonGetCurrentDate().c_str());
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnUsedZDrink(JNIEnv* env, jobject thiz)
{
    commonOnUsedZDrink();
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnUsedDrop(JNIEnv* env, jobject thiz)
{
    commonOnUsedDrop();
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnUsedAutoBroom(JNIEnv* env, jobject thiz)
{
    commonOnUsedAutoBroom();
}

JNIEXPORT void JNICALL
Java_app_jni_JniBridge_nativeOnUsedBattery(JNIEnv* env, jobject thiz)
{
    commonOnUsedBattery();
}

JNIEXPORT int JNICALL
Java_app_jni_JniBridge_nativeGetFriendCount(JNIEnv* env, jobject thiz)
{
    commonGetFriendCount();
}

JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsUsedZDrink(JNIEnv* env, jobject thiz)
{
    return commonIsUsedZDrink();
}

JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsUsedDrop(JNIEnv* env, jobject thiz)
{
    return commonIsUsedDrop();
}

JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsBonusTime(JNIEnv* env, jobject thiz)
{
    return commonIsBonusTime();
}

JNIEXPORT jdouble JNICALL
Java_app_jni_JniBridge_nativeGetCurrentTime(JNIEnv* env, jobject thiz)
{
    return commonGetCurrentTime();
}

JNIEXPORT jboolean JNICALL
Java_app_jni_JniBridge_nativeIsGameActive(JNIEnv* env, jobject thiz)
{
    return (isGameActive() && mGameManager != 0 && mGameManager->getSwipeState() == eSwipeState_None);
}

#else

#import <Foundation/Foundation.h>
#import "JniBridge.h"

void onPartsClicked(int partsId);
void onPartsEvent(int eventId);

// TODO iOS特有の処理

// ------------------------------
// C++ -> Objective-C Called
// ------------------------------
//
// Lib
// ------------------------------
/**
 * [Native連携]
 * イベントロック中かを返す
 */
bool
nativeCallIsLockedEvent()
{
    // TODO nativeの処理を呼ぶ
    return false;
}

///**
// * [Native連携]
// * 一時停止中かを返す
// */
//bool
//nativeCallIsPausing()
//{
//    // TODO nativeの処理を呼ぶ
//    return false;
//}
//
///**
// * [Native連携]
// * サスペンド中かを返す
// */
//bool
//nativeCallIsSuspending()
//{
//    // TODO nativeの処理を呼ぶ
//    return false;
//}

/**
 * [Native連携]
 * Loading中かを返す
 */
bool
nativeCallIsLoading()
{
    // TODO nativeの処理を呼ぶ
    return false;
}

/**
 * [Native連携]
 * ローティングの終了を通知する
 */
void
nativeCallOnLoadingEnd()
{
    // TODO nativeの処理を呼ぶ
}

/**
 * [Native連携]
 * パーツのクリックイベントを伝える
 */
void
nativeCallOnPartsClicked(int partsId)
{
    nativeCallInterceptOnPartsClicked(partsId);
    
	onPartsClicked(partsId);
}

/**
 * [Native連携]
 * パーツのイベントを伝える
 */
void
nativeCallOnPartsEvent(int eventId)
{
	onPartsEvent(eventId);
}

//
// App
// ------------------------------
/**
 * [Native連携]
 * 所持宝石数の変更を伝える
 */
void
nativeCallOnChangedGem(int gem)
{
    [JniBridge onChangedGem:(NSInteger)gem];
}

/**
 * [Native連携]
 * 所持ポイント数の変更を伝える
 */
void
nativeCallOnChangedPoint(int point)
{
    [JniBridge onChangedPoint:(NSInteger)point];
}

/**
 * [Native連携]
 * ゴミ箱の占有率の変更を伝える
 */
void
nativeCallOnChangedFullness(double fullness)
{
    [JniBridge onChangedFullness:fullness];
}

/**
 * [Native連携]
 * レベルの変更を伝える
 */
void
nativeCallOnChangedLevel(int level)
{
    // フォアグラウンド時にデータ保存した場合はレベルアップ表示をさせる
    if (![JniBridge gameFragmentDidEnterBackground])
    {
        if (appManager != 0)
        {
            appManager->levelup();
        }
    }
    
    [JniBridge onChangedLevel:(NSInteger)level];
}

/**
 * [Native連携]
 * 箒が壊れたことを伝える
 */
void
nativeCallOnBrokenBroom(int broomType)
{
    [JniBridge onBrokenBroom:(NSInteger)broomType];
}

/**
 * [Native連携]
 * ゴミ箱XLが壊れたことを伝える
 */
void
nativeCallOnBrokenGarbageCanXl()
{
    [JniBridge onBrokenGarbageCanXl];
}

/**
 * [Native連携]
 * ポイントを取得したことを伝える
 * (+〇〇Pを表示する時に呼ばれる)
 */
void
nativeCallOnGetPoint(int point)
{
    [JniBridge onGetPoint:(NSInteger)point];
}

/**
 * [Native連携]
 * ポイントを取得したことを伝える
 * (+〇〇Getを表示する時に呼ばれる)
 */
void
nativeCallOnGetGem(int gem)
{
    [JniBridge onGetGem:(NSInteger)gem];
}

/**
 * [Native連携]
 * コンボボーナスを取得したことを伝える
 * (コンボボーナスを表示する時に呼ばれる)
 */
void
nativeCallOnGetComboBonus(int comboCount, int point)
{
    [JniBridge onGetComboBonus:(NSInteger)comboCount point:(NSInteger)point];
}

/**
 * [Native連携]
 * ひみつのミッションをクリアしたことを伝える
 */
void
nativeCallOnClearMission(int missionId)
{
    [JniBridge onClearMission:(NSInteger)missionId];
}

/**
 * [Native連携]
 * ジロキチの隠れ家の宝石を全てとったことを伝える
 */
void
nativeCallOnUndergroundGemGot(int gem)
{
    [JniBridge onUndergroundGemGot:(NSInteger)gem];
}

void
nativeCallOnSucceededSyntheses(int garbageId)
{
    [JniBridge onSucceededSyntheses:(NSInteger)garbageId];
}

void
nativeCallOnEnterUnderground(bool enterJirokichi)
{
    [JniBridge onEnterUnderground:enterJirokichi];
}

void
nativeCallOnChangeStage(int stageId)
{
    [JniBridge onChangeStage:(NSInteger)stageId];
}

void nativeCallOnRemainingBonusTime(int remainingSeconds)
{
    [JniBridge onRemainingBonusTime:(NSInteger)remainingSeconds];
}

/**
 * [Native連携]
 * (サーバー連携によって変更する可能性大)
 * 情報をPreferenceに保存する
 */
void
nativeCallOnSaveOtherInfo()
{
    [JniBridge onSaveOtherInfoWithBbonusStartTime:mGameManager->getBonusStartTime()
                              addGarbageStartTime:mGameManager->getAddGarbageStartTime()
                              heroDrinkAppearTime:mGameManager->getHeroDrinkAppearTime()
                                  zDrinkStartTime:mGameManager->getZDrinkStartTime()
                                    dropStartTime:mGameManager->getDropStartTime()];
}

/**
 * [Native連携]
 * (サーバー連携によって変更する可能性大)
 * ゲーム情報を保存する
 */
void
nativeCallOnSaveGameInfo(int placeType, int addPoint, int completedCount, int sweepCount, bool isBrokenBroom, int nextStage, bool isGarbageCanBroken)
{
    [JniBridge onSaveGameInfoWithPlaceType:(NSInteger)placeType
                                  addPoint:(NSInteger)addPoint
                            completedCount:(NSInteger)completedCount
                                  garbages:[NSString stringWithUTF8String:mGameManager->getGarbagesJson().c_str()]
                                sweepCount:sweepCount
                             isBrokenBroom:isBrokenBroom
                                 nextStage:(NSInteger)nextStage
                        isGarbageCanBroken:isGarbageCanBroken];
}

// ------------------------------
// Objective-C -> C++ Called
// ------------------------------
//
// Lib
// ------------------------------
void
onParentCreated()
{
    commonOnParentCreated();
}

void
onParentDestroy()
{
    commonOnParentDestroy();
}

void
onSurfaceCreated()
{
    commonOnSurfaceCreated();
}

void
onSurfaceChanged(int width, int height)
{
    commonOnSurfaceChanged(width, height);
}

void
onDrawFrame()
{
    commonOnDrawFrame();
}

void
onTouchDown(float ptX, float ptY)
{
    commonOnTouchDown(ptX, ptY);
}

void
onTouchMove(float ptX, float ptY)
{
    commonOnTouchMove(ptX, ptY);
}

void
onTouchUp()
{
    commonOnTouchUp();
}

bool
isPausing()
{
    return commonIsPausing();
}

void
pauseEnd()
{
    commonPauseEnd();
}

// 関数名がpauseだと関数重複のエラーが出るので、関数名を変更
void
pauseStart()
{
    commonPause();
}

bool
isSuspending()
{
    return commonIsSuspending();
}

void
foreground()
{
    commonForeground();
}

void
background()
{
    commonBackground();
}

////
//// ゴミ箱で追加
//// ------------------------------
//bool isPausing()
//{
//    return commonIsPausing();
//}
//
//bool isSuspending()
//{
//    return commonIsSuspending();
//}

//
// App
// ------------------------------
void
foregroundApplication()
{
    commonForegroundApplication();
}

void
backgroundApplication()
{
    commonBackgroundApplication();
}

void
onCheckTime(int isForegroundFirst)
{
    commonOnCheckTime(isForegroundFirst);
}

int
getGem()
{
    return commonGetGem();
}

int
getPoint()
{
    return commonGetPoint();
}

int
getLevel()
{
    return commonGetLevel();
}

int
getMaxLevel()
{
    return commonGetMaxLevel();
}

double
getFullness()
{
    return commonGetFullness();
}

int
getExperiencePoint()
{
    return commonGetExperiencePoint();
}

int
getCurrentLevelRequiredPoint()
{
    return commonGetCurrentLevelRequiredPoint();
}

int
getNextLevelRequiredPoint()
{
    return commonGetNextLevelRequiredPoint();
}

void
setInitData(double bonusStart, double addGarbageStart, double heroDrinkAppearTime, double zDrinkStartTime, double dropStartTime)
{
    return commonSetInitData(bonusStart, addGarbageStart, heroDrinkAppearTime, zDrinkStartTime, dropStartTime);
}

void
addGem(int addValue)
{
    commonAddGem(addValue);
}

void
AddPoint(int addValue)
{
    commonAddPoint(addValue);
}

int
getCurrentBroomType()
{
    return commonGetCurrentBroomType();
}

void
changeCharacter(int type)
{
    commonChangeCharacter(type);
}

void
changeBroomType(int type)
{
    commonChangeBroomType(type);
}

int
getCurrentGarbageCanType()
{
    return commonGetCurrentGarbageCanType();
}

void
changeGarbageCanType(int type)
{
    commonChangeGarbageCanType(type);
}

void
changeStage(int stage)
{
    commonChangeStage(stage);
}

void
saveData(int nextStage)
{
    commonSaveData(nextStage);
}

int
getCurrentStage()
{
    return commonGetCurrentStage();
}

void
useSeal()
{
    commonUseSeal();
}

void
useTelephone()
{
    commonUseTelephone();
}

void
onReceivedUserAppsSelfResponse(std::string json)
{
    commonOnReceivedUserAppSelfResponse(json);
}

void
onReceivedGomipoiGarbageOwnResponse(std::string json)
{
    commonOnReceivedGomipoiGarbageOwnResponse(json);
}

void
onReceivedGomipoiGameLoadResponse(std::string json)
{
    commonOnReceivedGomipoiGameLoadResponse(json);
}

void
onReceivedGomipoiItemOwnResponse(std::string json)
{
    commonOnReceivedGomipoiItemOwnResponse(json);
}

int
getItemOwnCount(std::string item_code)
{
    return commonGetItemOwnCount(item_code);
}

bool
isItemUsing(std::string item_code)
{
    return commonIsItemUsing(item_code);
}

bool
isLimitItemUsing(std::string item_code)
{
    return commonIsLimitItemUsing(item_code);
}

void
onReceivedGomipoiBookOwnResponse(std::string json)
{
    commonOnReceivedGomipoiBookOwnResponse(json);
}

void
onReceivedGomipoiGarbageSynthesesResponse(std::string json)
{
    commonOnReceivedGomipoiGarbageSynthesesResponse(json);
}

void
onReceivedGomipoiGameSaveResponse(std::string json)
{
    commonOnReceivedGomipoiGameSaveResponse(json);
}

bool
isUnlockBook(std::string garbage_code)
{
    return commonIsUnlockBook(garbage_code);
}

bool
isNewBook(std::string garbage_code)
{
    return commonIsNewBook(garbage_code);
}

bool
isRareGarbage(std::string garbage_code)
{
    return commonIsRareGarbage(garbage_code);
}

std::vector<int>
getPageBonus()
{
    return commonGetPageBonus();
}

void
onReceivedBookReceiveBonusesResponse(std::string json)
{
    commonOnReceivedBookReceiveBonusesRespnse(json);
}

void onStartGetData()
{
    commonOnStartGetData();
}

void onFinishedGetData()
{
    commonOnFinishedGetData();
}

int
getGarbageId(std::string garbageCode)
{
    return commonGetGarbageId(garbageCode);
}

std::string
getGarbageCode(int garbage_id)
{
    return commonGetGarbageCode(garbage_id);
}

int
getGarbageBonus(int garbage_id)
{
    return commonGetGarbageBonus(garbage_id);
}

std::string
getNewFoundGarbages()
{
    return commonGetNewFoundGarbages();
}

std::vector<int>
getBookGarbages()
{
    return commonGetBookGarbages();
}

void
logout()
{
    commonLogout();
}

std::string
getCurrntDate()
{
    return commonGetCurrentDate();
}

void
onUsedZDrink()
{
    commonOnUsedZDrink();
}

void
onUsedDrop()
{
    commonOnUsedDrop();
}

std::string
missionIdToItemCode(int mission_id)
{
    return commonMissionIdToItemCode(mission_id);
}

void
onUsedAutoBroom()
{
    commonOnUsedAutoBroom();
}

void
onUsedBattery()
{
    commonOnUsedBattery();
}

int
getFriendCount()
{
    return commonGetFriendCount();
}

bool
isUsedZDrink()
{
    return commonIsUsedZDrink();
}

bool
isUsedDrop()
{
    return commonIsUsedDrop();
}

bool
isBonusTime()
{
    return commonIsBonusTime();
}

double
getCurrentTime()
{
    return commonGetCurrentTime();
}

#endif



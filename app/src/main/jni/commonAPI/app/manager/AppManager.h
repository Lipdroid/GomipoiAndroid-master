//
//
//
#include <random>

#include "../parameter/app_parameter.h"
#include "../../lib/manager/AppManagerBase.h"
#include "../../lib/model/animation/general/RotateAnimation.h"
#include "../../lib/model/animation/general/ScaleAnimation.h"
#include "../../lib/model/animation/general/FadeAnimation.h"
#include "../../lib/model/animation/general/TranslateAnimation.h"
#include "../../lib/model/animation/general/FrameAnimation.h"
#include "../../lib/model/animation/general/OrderAnimationSet.h"
#include "../../lib/model/animation/general/MultipleAnimationSet.h"
#include "../model/animation/PoikoMoveAnimation.h"
#include "../model/animation/PoikoSweepAnimation.h"
#include "../model/animation/PoikoFallAnimation.h"
#include "../model/animation/PoikoFallOutAnimation.h"
#include "../model/animation/AutoBroomSweepAnimation.h"
#include "../model/parts/PoikoParts.h"
#include "../model/parts/GarbagePartsBase.h"
#include "../model/parts/GemParts.h"
#include "../model/parts/HolePartsBase.h"
#include "../model/parts/AutoBroomParts.h"

#include "GameManager.h"
#include "PlayerManager.h"
#include "../model/missions/SecretMission.h"

#ifndef TEST_APPMANAGER_H
#define TEST_APPMANAGER_H

#define BATTERY_GAGE_FRAME_DIFF_X 35.0
#define BATTERY_GAGE_FRAME_DIFF_Y 48.0

//
// Callback
// ------------------------------
typedef std::function<GameManager*()>   tCallback_getGameManager;
typedef std::function<void(int)>        tCallback_getPoint;
typedef std::function<void(int)>        tCallback_getGem;
typedef std::function<void(int, int)>   tCallback_getComboBonus;
typedef std::function<void(int)>        tCallback_clearSecretMission;
typedef std::function<void(int)>        tCallback_undergroundGemGot;
typedef std::function<void()>           tCallback_stageLayoutTransition;
typedef std::function<void(bool)>       tCallback_enterUnderGround;
typedef std::function<void(int)>        tCallback_changeStage;
typedef std::function<void(int)>        tCallback_remainingBonusTime;


class AppManager : public AppManagerBase {

// ------------------------------
// Enum
// ------------------------------
    enum
    {
        eBackground_Room = 0,
        eBackground_PoikoRoom,
        eBackground_Garden,
        eBackground_BonusTime,
        eBackground_SecretRoom
    };
    
    enum
    {
        eGarbageCan_Normal = 0,
        eGarbageCan_Silver,
        eGarbageCan_Gold,
        eGarbageCan_PoikoNormal,
        eGarbageCan_PoikoSilver,
        eGarbageCan_PoikoGold,
        
        eGarbageCan_Xl,
        eGarbageCan_Garden,        
        eGarbageCan_SecretRoom,
    };

// ------------------------------
// Member
// ------------------------------
private:
    int mGarbageTargetCount;
    int mGarbageFinishCount;
    int mComboCount;
    
    std::vector<SecretMission*> mSecretMissions;
    
    int mGemTargetCount;
    int mGemFinishCount;

    bool mIsGetAnimating;
    bool mIsMotherAnimation;
    bool mInStageTransition; //ステージ切り替え中、タッチイベントを無視するので、このフラグで判定する
    bool mInWaitingForStageTransition; //ステージ切り替え中、タッチイベントを無視するので、このフラグで判定する
    double mCurrentRemainingBonusTime;

    float mLastTouchX;
    float mLastTouchY;
    float mStartTouchX;
    float mStartTouchY;

    double mAutoLastTouchX;
    double mAutoLastTouchY;
    double mAutoStartTouchX;
    double mAutoStartTouchY;
    
    int mPausedStageId;
    int mPausedGarbageCanType;
    bool mPausedJirokichi;
    std::vector<GemData *> mPausedJirokichiGem;
    
    double mAutoBroomWaitingStartTime;

    tCallback_onCompletedAnim mOnCompletedPoikoWaitingAnimation;
    tCallback_onCompletedAnim mOnCompletedAutoBroomWaitingAnimation;
    tCallback_getGameManager mGetGameManager;
    tCallback_getPlayerManager mGetPlayerManager;
    tCallback_getPoint mGetPoint;
    tCallback_getGem mGetGem;
    tCallback_getComboBonus mGetComboBonus;
    tCallback_clearSecretMission mClearMissionCallback;
    tCallback_undergroundGemGot mUndergroundGemGotCallback;
    tCallback_enterUnderGround mEnterUndergroundCallback;
    tCallback_changeStage mChangeStageCallback;
    tCallback_remainingBonusTime mBonusTimeCallback;

    PoikoMoveAnimation *mPoikoMoveAnimation;
    PoikoMoveAnimation *mAutoBroomMoveAnimation;
    TranslateAnimation *mGageFrameAnimation;
    TranslateAnimation *mGageLeftAnimation;
    TranslateAnimation *mGageMiddleAnimation;
    TranslateAnimation *mGageRightAnimation;

    std::vector<GarbagePartsBase *> mGarbageList;
    std::vector<GemParts *> mGemList;
    std::vector<PartsBase *> mSortedPartsList;

    PartsBase *mDummyGarbage;
    PartsBase *mDummyGarbage2;
    PartsBase *mDummyGarbage3;
    PartsBase *mDummyGem;
    PartsBase *mBackground;
    PartsBase *mGarbageCan;
    PartsBase *mSeal;
    PoikoParts *mPoiko;
    PartsBase *mHeroDrink;
    PartsBase *mSerifTelephone;
    PartsBase *mSerifMother;
    PartsBase *mHeroDrinkPlusLight;
    PartsBase *mTextHeroDrink;
    PartsBase *mTextGet;
    PartsBase *mTextBonusTime;
    PartsBase *mTextLevelUpPlusDescript;
    PartsBase *mPoikoFace;
    PartsBase *mPoikoEffect;
    PartsBase *mSerifFullGarbages;
    PartsBase *mTextFull;
    HolePartsBase *mHole;
    PartsBase *mHoleMask;
    PartsBase *mUndergroundTitle;
    PartsBase *mScreen; //ステージ切り替えで使用する
    AutoBroomParts *mAutoBroom;
    PartsBase *mGageFrame;
    PartsBase *mGageLeft;
    PartsBase *mGageMiddle;
    PartsBase *mGageRight;
    
    //部屋のひみつのトリガーパーツ
    std::vector<PartsBase *> mTapList;
    PartsBase *mSecretTapEffect;
    PartsBase *mSecretTrashCan;
    PartsBase *mSecretTrashCanGem;
    PartsBase *mSecretTV;
    PartsBase *mSecretTVScreen;
    PartsBase *mSecretRocket;
    PartsBase *mSecretFlyingRocket;
    PartsBase *mSecretHeart1;
    PartsBase *mSecretHeart2;
    PartsBase *mSecretHeart3;
    PartsBase *mSecretShowHeart1;
    PartsBase *mSecretShowHeart2;
    PartsBase *mSecretShowHeart3;
    PartsBase *mSecretLamp;
    PartsBase *mSecretLightLamp;
    PartsBase *mSecretMouseHole;
    PartsBase *mSecretMouse;

// ------------------------------
// Constructor
// ------------------------------
public:
    AppManager(tCallback_getGameManager getGameFunc,
               tCallback_getPlayerManager getPlayerFunc,
               tCallback_getPoint getPoint,
               tCallback_getGem getGem,
               tCallback_getComboBonus getComboBonus,
               tCallback_clearSecretMission clearMissionCallback,
               tCallback_undergroundGemGot undergroundGemGotCallback,
               tCallback_enterUnderGround enterUndergroundCallback,
               tCallback_changeStage changeStageCallback,
               tCallback_remainingBonusTime bonusTimeCallback) : AppManagerBase()
    {
        mGetGameManager = getGameFunc;
        mGetPlayerManager = getPlayerFunc;
        mGetPoint = getPoint;
        mGetGem = getGem;
        mGetComboBonus = getComboBonus;
        mClearMissionCallback = clearMissionCallback;
        mUndergroundGemGotCallback = undergroundGemGotCallback;
        mEnterUndergroundCallback = enterUndergroundCallback;
        mChangeStageCallback = changeStageCallback;
        mBonusTimeCallback = bonusTimeCallback;

        mIsGetAnimating = false;
        mIsMotherAnimation = false;

        mLastTouchX = 0.0;
        mLastTouchY = 0.0;
        mStartTouchX = 0.0;
        mStartTouchY = 0.0;
        mAutoLastTouchX = 0.0;
        mAutoLastTouchY = 0.0;
        mAutoStartTouchX = 0.0;
        mAutoStartTouchY = 0.0;
        mInStageTransition = false;
        mInWaitingForStageTransition = false;
        mCurrentRemainingBonusTime = -1.0;
        
        mAutoBroomWaitingStartTime = 0.0;
        
        GameManager *gameManager = mGetGameManager();

        mOnCompletedPoikoWaitingAnimation = [&](int animationId)
        {
            if (mPoiko == 0)
            {
                return;
            }

            if (animationId != eAnimId_Poiko_Normal)
            {
                mPoiko->startFrameAnimation(eAnimId_Poiko_Normal, mOnCompletedPoikoWaitingAnimation);
                return;
            }

            std::random_device rnd;                             // 非決定的な乱数生成器を生成
            std::mt19937 mt(rnd());                             //  メルセンヌ・ツイスタの32ビット版、引数は初期シード値
            std::uniform_int_distribution<> rand(0, 5);         // [0, 5] 範囲の一様乱数
            if (rand(mt) == 0)
            {
                mPoiko->startFrameAnimation(eAnimId_Poiko_Glanced, mOnCompletedPoikoWaitingAnimation);
            }
            else
            {
                mPoiko->startFrameAnimation(eAnimId_Poiko_Normal, mOnCompletedPoikoWaitingAnimation);
            }
        };
        
        mOnCompletedAutoBroomWaitingAnimation = [&](int animationId)
        {
            if (mAutoBroom == 0)
            {
                return;
            }
            
            mGageFrame->show();
            
            if (animationId != eAnimId_AutoBroom_Normal)
            {
                mAutoBroom->startFrameAnimation(eAnimId_AutoBroom_Normal, mOnCompletedAutoBroomWaitingAnimation);
                return;
            }
            
            GameManager *gameManager = mGetGameManager();
            PlayerManager *playerManager = mGetPlayerManager();
            if (gameManager == 0 || playerManager == 0)
            {
                mAutoBroom->startFrameAnimation(eAnimId_AutoBroom_Normal, mOnCompletedAutoBroomWaitingAnimation);
                return;
            }
            
            if (!gameManager->isUsedBattery())
            {
                if (mGageFrame != 0)
                {
                    mGageFrame->hide();
                }
                if (mGageLeft != 0)
                {
                    mGageLeft->hide();
                }
                if (mGageMiddle != 0)
                {
                    mGageMiddle->hide();
                }
                if (mGageRight != 0)
                {
                    mGageRight->hide();
                }
                
                if (mAutoBroom->getCurrentLeft() != 81.0
                    || mAutoBroom->getCurrentTop() != 57.5)
                {
                    double oldLeft = mAutoBroom->getLeftOffset();
                    double oldTop = mAutoBroom->getTopOffset();
                    double newLeft = 81.0;
                    double newTop = 57.5;
                    
                    // ぽい子の移動アニメーション
                    if (mAutoBroomMoveAnimation != 0)
                    {
                        mAutoBroomMoveAnimation->setNewOffset(newLeft - oldLeft, -(newTop - oldTop));
                    }
                    mAutoBroom->startAnimation(
                                               eAnimId_AutoBroom_Move_Set,
                                               [&, newLeft, newTop](int animationId)
                                               {
                                                   mAutoBroom->setOffset(81.0, 57.5);
                                                   mAutoBroom->setOwned(true);
                                               });
                }
                else
                {
                    mAutoBroom->setOwned(true);
                }
                
                mAutoBroomWaitingStartTime = 0.0;
                return;
            }

            double gage = gameManager->getBatteryGage();
            if (gage > 22.0)
            {
                double leftW = 3.0;
                double rightW = 3.0;
                double middleW = gage - (leftW + rightW) + 2.0;
                
                double leftX = mGageFrame->getLeftOffset() + 2.5;
                double middleX = leftX + leftW - 1.0;
                double rightX = leftX + leftW + (gage - (leftW + rightW));
                
                double top = mGageFrame->getTopOffset();
                double height = mGageFrame->getHeight();
                
                if (mGageLeft != 0)
                {
                    mGageLeft->setRect(leftX,
                                       top,
                                       leftW,
                                       height);
                    mGageLeft->show();
                }
                if (mGageMiddle != 0)
                {
                    mGageMiddle->setRect(middleX,
                                         top,
                                         middleW,
                                         height);
                    mGageMiddle->show();
                }
                if (mGageRight != 0)
                {
                    mGageRight->setRect(rightX,
                                        top,
                                        rightW,
                                        height);
                    mGageRight->show();
                }
            }
            else if (gage > 3.0)
            {
                double leftW = 3.0;
                double middleW = gage - leftW + 1.0;
                
                double leftX = mGageFrame->getLeftOffset() + 2.5;
                double middleX = leftX + leftW - 1.0;
                
                double top = mGageFrame->getTopOffset();
                double height = mGageFrame->getHeight();
                
                if (mGageLeft != 0)
                {
                    mGageLeft->setRect(leftX,
                                       top,
                                       leftW,
                                       height);
                    mGageLeft->show();
                }
                if (mGageMiddle != 0)
                {
                    mGageMiddle->setRect(middleX,
                                         top,
                                         middleW,
                                         height);
                    mGageMiddle->show();
                }
                if (mGageRight != 0)
                {
                    mGageRight->hide();
                }
            }
            else
            {
                double leftW = gage;
                double leftX = mGageFrame->getLeftOffset() + 2.5;
                
                double top = mGageFrame->getTopOffset();
                double height = mGageFrame->getHeight();
                
                if (mGageLeft != 0)
                {
                    mGageLeft->setRect(leftX,
                                       top,
                                       leftW,
                                       height);
                    mGageLeft->show();
                }
                if (mGageMiddle != 0)
                {
                    mGageMiddle->hide();
                }
                if (mGageRight != 0)
                {
                    mGageRight->hide();
                }
            }
            
            if (mAutoBroomWaitingStartTime <= 0.0)
            {
                mAutoBroomWaitingStartTime = playerManager->getCurrentTime();
            }
            GarbageData *bottomGarbage = gameManager->getBottomGarbage();
            if (bottomGarbage != 0
                && mCallback != 0
                && !mCallback->mIsLockedEvent()
                && gameManager->getSwipeState() == eSwipeState_None
                && (playerManager->getCurrentTime() - mAutoBroomWaitingStartTime >= AUTOBROOM_WAITING_INTERVAL))
            {
                startAutoBroomSweepAnimation(bottomGarbage);
                mAutoBroomWaitingStartTime = 0.0;
            }
            else
            {
                std::random_device rnd;                             // 非決定的な乱数生成器を生成
                std::mt19937 mt(rnd());                             //  メルセンヌ・ツイスタの32ビット版、引数は初期シード値
                std::uniform_int_distribution<> rand(0, 3);         // [0, 3] 範囲の一様乱数
                if (rand(mt) == 0)
                {
                    mAutoBroom->startFrameAnimation(eAnimId_AutoBroom_Glanced, mOnCompletedAutoBroomWaitingAnimation);
                }
                else
                {
                    mAutoBroom->startFrameAnimation(eAnimId_AutoBroom_Normal, mOnCompletedAutoBroomWaitingAnimation);
                }
            }
        };

        // PartsListを生成する
        ScaleAnimation *scaleAnimation;
        FadeAnimation *fadeAnimation;
        RotateAnimation *rotateAnimation;
        TranslateAnimation *translateAnimation;
        FrameAnimation *frameAnimation;
        OrderAnimationSet *orderAnimationSet;
        MultipleAnimationSet *multipleAnimationSet;

        //
        // ePartsID_dummy
        // (ユーザーには見せないが、ゴミ画像のテクスチャーID取得の為に必要)
        // ------------------------------
        mDummyGarbage = new PartsBase(ePartsID_dummy, 0, 0, 59, 44);
        mDummyGarbage->addTexture(new PartsTexture("garbage.png", 340, 2030, 1, 1, 1, 1, false));
        mDummyGarbage->hide();
        mPartsList.push_back(mDummyGarbage);
        
        //
        // ePartsID_dummy2
        // (ユーザーには見せないが、ゴミ画像のテクスチャーID取得の為に必要)
        // ------------------------------
        mDummyGarbage2 = new PartsBase(ePartsID_dummy2, 0, 0, 59, 44);
        mDummyGarbage2->addTexture(new PartsTexture("garbage2.png", 512, 1680, 1, 1, 1, 1, false));
        mDummyGarbage2->hide();
        mPartsList.push_back(mDummyGarbage2);
        
        //
        // ePartsID_dummy3
        // (ユーザーには見せないが、ゴミ画像のテクスチャーID取得の為に必要)
        // ------------------------------
        mDummyGarbage3 = new PartsBase(ePartsID_dummy3, 0, 0, 59, 44);
        mDummyGarbage3->addTexture(new PartsTexture("garbage3.png", 652, 512, 1, 1, 1, 1, false));
        mDummyGarbage3->hide();
        mPartsList.push_back(mDummyGarbage3);
        
        //
        // ePartsID_dummyGem
        // (宝石画像のテクスチャーID取得の為に必要)
        // ------------------------------
        mDummyGem = new PartsBase(ePartsID_dummyGem, 0, 0, 29, 29);
        mDummyGem->addTexture(new PartsTexture("gem.png", 29, 29));
        mDummyGem->hide();
        mPartsList.push_back(mDummyGem);

        //
        // ePartsID_roomBackground
        // ------------------------------
        mBackground = new PartsBase(ePartsID_roomBackground, 0, 0, 320, 568);
        
        // 通常
        mBackground->addTexture(new PartsTexture("room_background.png", 320, 568));
        mBackground->addTexture(new PartsTexture("room_background_poiko.png", 320, 568));
        mBackground->addTexture(new PartsTexture("room_background_garden.png", 320, 568));
        // ボーナスタイム
        mBackground->addTexture(new PartsTexture("bonus_time_background.png", 320, 568));
        // ジロキチの隠れ家
        mBackground->addTexture(new PartsTexture("room_background_jirokichi.png", 320, 568));
        
        mPartsList.push_back(mBackground);
        
        //
        // ePartsID_secretLightLamp
        // ------------------------------
        mSecretLightLamp = new PartsBase(ePartsID_secretLightLamp, 142, 59.5, 42, 49);
        mSecretLightLamp->addTexture(new PartsTexture("secret_light.png", 42, 49));
        mSecretLightLamp->hide();
        mPartsList.push_back(mSecretLightLamp);
        
        //
        // ePartsID_secretTVScreen
        // ------------------------------
        mSecretTVScreen = new PartsBase(ePartsID_secretTVScreen, 177, 91, 71, 58);
        mSecretTVScreen->addTexture(new PartsTexture("tv_screen_janpoi.png", 71, 58));
        mSecretTVScreen->hide();
        mPartsList.push_back(mSecretTVScreen);
        
        //
        // ePartsID_secretFlyingRocket
        // ------------------------------
        mSecretFlyingRocket = new PartsBase(ePartsID_secretFlyingRocket, 47, 102, 54, 95);
        mSecretFlyingRocket->addTexture(new PartsTexture("secret_rocket_1.png", 54, 95));
        mSecretFlyingRocket->addTexture(new PartsTexture("secret_rocket_2.png", 54, 95));
        mSecretFlyingRocket->hide();
        mPartsList.push_back(mSecretFlyingRocket);
        
        //
        // ePartsID_secretMouse
        // ------------------------------
        mSecretMouse = new PartsBase(ePartsID_secretMouse, 76.5, 145, 66.5, 31.5);
        mSecretMouse->addTexture(new PartsTexture("secret_rat_1.png", 66.5, 31.5));
        mSecretMouse->addTexture(new PartsTexture("secret_rat_2.png", 66.5, 31.5));
        mSecretMouse->hide();
        mSortedPartsList.push_back(mSecretMouse);

        //
        // ePartsID_garbageCan
        // ------------------------------
        mGarbageCan = new PartsBase(ePartsID_garbageCan, 254, 96, 65, 85);
        
        // 通常
        mGarbageCan->addTexture(new PartsTexture("can_normal.png", 65, 85));
        mGarbageCan->addTexture(new PartsTexture("can_silver.png", 65, 85));
        mGarbageCan->addTexture(new PartsTexture("can_gold.png", 65, 85));
        mGarbageCan->addTexture(new PartsTexture("can_normal_poiko.png", 65, 85));
        mGarbageCan->addTexture(new PartsTexture("can_silver_poiko.png", 65, 85));
        mGarbageCan->addTexture(new PartsTexture("can_gold_poiko.png", 65, 85));
        
        mGarbageCan->addTexture(new PartsTexture("can_xl.png", 65, 85));
        mGarbageCan->addTexture(new PartsTexture("can_garden.png", 65, 85));
        // ジロキチの隠れ家
        mGarbageCan->addTexture(new PartsTexture("can_normal_jirokichi.png", 65, 85));
        
        mGarbageCan->setTexturePosition(mGetPlayerManager()->getGarbageCanType() - 1 + mGetGameManager()->getCurrentStage() * 3);
        mPartsList.push_back(mGarbageCan);
        
        //
        // ePartsID_secretTrashCanGem
        // ------------------------------
        mSecretTrashCanGem = new PartsBase(ePartsID_secretTrashCanGem, 272.5, 181, 28, 26);
        mSecretTrashCanGem->addTexture(new PartsTexture("secret_gem_1.png", 28, 26));
        mSecretTrashCanGem->addTexture(new PartsTexture("secret_gem_2.png", 28, 26));
        mSecretTrashCanGem->hide();
        mPartsList.push_back(mSecretTrashCanGem);

        //
        // ePartsID_seal
        // ------------------------------
        mSeal = new PartsBase(ePartsID_seal, 27, 90, 50, 50);
        mSeal->addTexture(new PartsTexture("seal.png", 50, 50));
        mSeal->hide();
        mPartsList.push_back(mSeal);
        
        //
        // ePartsID_secretTrashCan
        // ------------------------------
        mSecretTrashCan = new PartsBase(ePartsID_secretTrashCan, 254, 96, 65, 85);
        mPartsList.push_back(mSecretTrashCan);
        
        //
        // ePartsID_secretTV
        // ------------------------------
        mSecretTV = new PartsBase(ePartsID_secretTV, 176, 87, 78, 87);
        mPartsList.push_back(mSecretTV);
        
        //
        // ePartsID_secretRocket
        // ------------------------------
        mSecretRocket = new PartsBase(ePartsID_secretRocket, 59, 141, 28, 21);
        mPartsList.push_back(mSecretRocket);
        
        //
        // ePartsID_secretHeart1
        // ------------------------------
        mSecretHeart1 = new PartsBase(ePartsID_secretHeart1, 239, 411, 65, 47);
        mPartsList.push_back(mSecretHeart1);
        
        //
        // ePartsID_secretHeart2
        // ------------------------------
        mSecretHeart2 = new PartsBase(ePartsID_secretHeart2, 5, 118, 30, 26);
        mPartsList.push_back(mSecretHeart2);
        
        //
        // ePartsID_secretHeart3
        // ------------------------------
        mSecretHeart3 = new PartsBase(ePartsID_secretHeart3, 298, 75, 22, 24);
        mPartsList.push_back(mSecretHeart3);
        
        //
        // ePartsID_secretLamp
        // ------------------------------
        mSecretLamp = new PartsBase(ePartsID_secretLamp, 146, 68, 34, 36);
        mPartsList.push_back(mSecretLamp);
        
        //
        // ePartsID_secretMouseHole
        // ------------------------------
        mSecretMouseHole = new PartsBase(ePartsID_secretMouseHole, 96, 142, 30, 26);
        mPartsList.push_back(mSecretMouseHole);
        
        //
        // ePartsID_hole
        // ------------------------------
        mHole = new HolePartsBase();
        mHole->addTexture(new PartsTexture("hole.png", mHole->getWidth(), mHole->getHeight()));
        mHole->hide();
        mHole->setClickable(true);
        
        mPartsList.push_back(mHole);
        
        //
        // ePartsID_holeMask Hole mask（穴に落ちるときのマスク）
        // ------------------------------
        mHoleMask = new PartsBase(ePartsID_holeMask, 100.0, 239.0, 120.0, 254.0);
        mHoleMask->addTexture(new PartsTexture("hole_mask.png", mHoleMask->getWidth(), mHoleMask->getHeight()));
        mHoleMask->hide();
        
        mPartsList.push_back(mHoleMask);

        //
        // ePartsID_hero_drink
        // ------------------------------
        mHeroDrink = new PartsBase(ePartsID_hero_drink, 0, 0, 46, 75);
        mHeroDrink->addTexture(new PartsTexture("hero_drink.png", 46, 75));
        mHeroDrink->hide();
        mHeroDrink->setClickable(true);
        mPartsList.push_back(mHeroDrink);

        //
        // ePartsID_textFull
        // ------------------------------
        mTextFull = new PartsBase(ePartsID_textFull, 260, 136, 52, 32);
        mTextFull->addTexture(new PartsTexture("text_full.png", 52, 32));
        mTextFull->hide();

        // アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_TextFull_Set);
        fadeAnimation = new FadeAnimation(eAnimId_TextFull_Appear, 0.5, 1, 500);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_TextFull_Disappear, 1, 0.5, 500);
        orderAnimationSet->addAnimation(fadeAnimation);
        orderAnimationSet->setLooping(true);
        mTextFull->addAnimation(orderAnimationSet);

        mPartsList.push_back(mTextFull);
        
        //
        // ePartsID_secretShowHeart1
        // ------------------------------
        mSecretShowHeart1 = new PartsBase(ePartsID_secretShowHeart1, 242, 406, 60, 55);
        mSecretShowHeart1->addTexture(new PartsTexture("secret_heart_1.png", 60, 55));
        mSecretShowHeart1->addTexture(new PartsTexture("secret_heart_2.png", 60, 55));
        mSecretShowHeart1->hide();
        mPartsList.push_back(mSecretShowHeart1);
        
        //
        // ePartsID_secretShowHeart2
        // ------------------------------
        mSecretShowHeart2 = new PartsBase(ePartsID_secretShowHeart2, -9, 104, 60, 55);
        mSecretShowHeart2->addTexture(new PartsTexture("secret_heart_1.png", 60, 55));
        mSecretShowHeart2->addTexture(new PartsTexture("secret_heart_2.png", 60, 55));
        mSecretShowHeart2->hide();
        mPartsList.push_back(mSecretShowHeart2);
        
        //
        // ePartsID_secretShowHeart3
        // ------------------------------
        mSecretShowHeart3 = new PartsBase(ePartsID_secretShowHeart3, 270, 58, 60, 55);
        mSecretShowHeart3->addTexture(new PartsTexture("secret_heart_1.png", 60, 55));
        mSecretShowHeart3->addTexture(new PartsTexture("secret_heart_2.png", 60, 55));
        mSecretShowHeart3->hide();
        mPartsList.push_back(mSecretShowHeart3);
        
        //
        // ePartsID_auto_broom
        // ------------------------------
        mAutoBroom = new AutoBroomParts(81.0, 57.5, 120.0, 127.0);
        mAutoBroom->addTexture(new PartsTexture("autobroom_off.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_normal_1.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_normal_2.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_move_1.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_move_2.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_sweep_1.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_sweep_2.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_sweep_3.png", 120.0, 127.0));
        mAutoBroom->addTexture(new PartsTexture("autobroom_glanced.png", 120.0, 127.0));
        
        // 通常アニメーション
        std::vector<int> autobroom_textureList_normal;
        autobroom_textureList_normal.push_back(eAutoBroomTexture_Normal2);
        autobroom_textureList_normal.push_back(eAutoBroomTexture_Normal1);
        frameAnimation = new FrameAnimation(eAnimId_AutoBroom_Normal, autobroom_textureList_normal, 200);
        mAutoBroom->addAnimation(frameAnimation);
        
        // 振り向きアニメーション
        std::vector<int> autobroom_textureList_glanced;
        autobroom_textureList_glanced.push_back(eAutoBroomTexture_Glanced);
        autobroom_textureList_glanced.push_back(eAutoBroomTexture_Normal1);
        frameAnimation = new FrameAnimation(eAnimId_AutoBroom_Glanced, autobroom_textureList_glanced, 200);
        frameAnimation->setBeforeOffsetTime(300);
        mAutoBroom->addAnimation(frameAnimation);
        
        // 移動アニメーション
        mAutoBroomMoveAnimation = new PoikoMoveAnimation(
                                                         eAnimId_AutoBroom_Move_Set,
                                                         [&](int texturePosition)
                                                         {
                                                             mAutoBroom->setTexturePosition(texturePosition);
                                                         });
        mAutoBroom->addAnimation(mAutoBroomMoveAnimation);
        
        // 掃除アニメーション
        mAutoBroom->addAnimation(new AutoBroomSweepAnimation(
                                                             [&](int texturePosition)
                                                             {
                                                                 mAutoBroom->setTexturePosition(texturePosition);
                                                             }));
        
        if (gameManager != 0)
        {
            mAutoBroom->setOwned(gameManager->isOwnedAutoBroom());
        }
        
        mPartsList.push_back(mAutoBroom);

        //
        // ePartsID_battery_gage_frame
        // ------------------------------
        mGageFrame = new PartsBase(ePartsID_battery_gage_frame,
                                   mAutoBroom->getLeftOffset() + BATTERY_GAGE_FRAME_DIFF_X,
                                   mAutoBroom->getTopOffset() + BATTERY_GAGE_FRAME_DIFF_Y,
                                   30.0,
                                   10.0);
        mGageFrame->addTexture(new PartsTexture("battery_gage_frame.png", 30.0, 10.0));
        if (gameManager != 0 && gameManager->isUsedBattery())
        {
            mGageFrame->show();
        }
        else
        {
            mGageFrame->hide();
        }
        
        mGageFrameAnimation = new TranslateAnimation(eAnimId_BatteryGage_Move, 0, 0, 0, 0, 500);
        mGageFrame->addAnimation(mGageFrameAnimation);
        
        mPartsList.push_back(mGageFrame);
        
        //
        // ePartsID_battery_gage_left
        // ------------------------------
        mGageLeft = new PartsBase(ePartsID_battery_gage_left,
                                  0,
                                  0,
                                  3.0,
                                  10.0);
        mGageLeft->addTexture(new PartsTexture("battery_gage_left.png", 3.0, 10.0));
        mGageLeft->hide();
        
        mGageLeftAnimation = new TranslateAnimation(eAnimId_BatteryGageLeft_Move, 0, 0, 0, 0, 500);
        mGageLeft->addAnimation(mGageLeftAnimation);
        
        mPartsList.push_back(mGageLeft);
        
        //
        // ePartsID_battery_gage_middle
        // ------------------------------
        mGageMiddle = new PartsBase(ePartsID_battery_gage_middle,
                                    0,
                                    0,
                                    1.0,
                                    10.0);
        mGageMiddle->addTexture(new PartsTexture("battery_gage_middle.png", 1.0, 10.0));
        mGageMiddle->hide();
        
        mGageMiddleAnimation = new TranslateAnimation(eAnimId_BatteryGageMiddle_Move, 0, 0, 0, 0, 500);
        mGageMiddle->addAnimation(mGageMiddleAnimation);
        
        mPartsList.push_back(mGageMiddle);
        
        //
        // ePartsID_battery_gage_right
        // ------------------------------
        mGageRight = new PartsBase(ePartsID_battery_gage_right,
                                   0,
                                   0,
                                   3.0,
                                   10.0);
        mGageRight->addTexture(new PartsTexture("battery_gage_right.png", 3.0, 10.0));
        mGageRight->hide();
        
        mGageRightAnimation = new TranslateAnimation(eAnimId_BatteryGageRight_Move, 0, 0, 0, 0, 500);
        mGageRight->addAnimation(mGageRightAnimation);
        
        mPartsList.push_back(mGageRight);
        
        //
        // ePartsID_secretTapEffect
        // ------------------------------
        mSecretTapEffect = new PartsBase(ePartsID_secretTapEffect, 0, 0, 45, 45);
        mSecretTapEffect->addTexture(new PartsTexture("tap_ballon.png", 45, 45));
        mSecretTapEffect->hide();
        
        mPartsList.push_back(mSecretTapEffect);

        
        //
        // ePartsID_poiko
        // ------------------------------
        mPoiko = new PoikoParts(150, 350, 120, 127);
        // ポイ子テクスチャー
        mPoiko->addTexture(new PartsTexture("poiko_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_silver_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poiko_gold_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poihero_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poihero_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poihero_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poihero_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poihero_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poihero_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("poihero_sweep_3.png", 120, 127));
        // おとんテクスチャー
        mPoiko->addTexture(new PartsTexture("oton_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_silver_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("oton_gold_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("otonhero_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("otonhero_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("otonhero_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("otonhero_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("otonhero_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("otonhero_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("otonhero_sweep_3.png", 120, 127));
        // こたつテクスチャー
        mPoiko->addTexture(new PartsTexture("kotatsu_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_silver_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_sweep_3.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("kotatsu_gold_glanced.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("nekohero_normal_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("nekohero_normal_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("nekohero_move_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("nekohero_move_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("nekohero_sweep_1.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("nekohero_sweep_2.png", 120, 127));
        mPoiko->addTexture(new PartsTexture("nekohero_sweep_3.png", 120, 127));
        mPoiko->setBroomType(mGetGameManager()->getBroomType());
        mPoiko->setCharacterType(mGetPlayerManager()->getCharacter());

        // 通常アニメーション
        std::vector<int> textureList_normal;
        textureList_normal.push_back(ePoikoTexture_Normal2);
        textureList_normal.push_back(ePoikoTexture_Normal1);
        frameAnimation = new FrameAnimation(eAnimId_Poiko_Normal, textureList_normal, 200);
        mPoiko->addAnimation(frameAnimation);

        // 振り向きアニメーション
        std::vector<int> textureList_glanced;
        textureList_glanced.push_back(ePoikoTexture_Glanced);
        textureList_glanced.push_back(ePoikoTexture_Normal1);
        frameAnimation = new FrameAnimation(eAnimId_Poiko_Glanced, textureList_glanced, 200);
        frameAnimation->setBeforeOffsetTime(300);
        mPoiko->addAnimation(frameAnimation);

        // 移動アニメーション
        mPoikoMoveAnimation = new PoikoMoveAnimation(
                eAnimId_Poiko_Move_Set,
                [&](int texturePosition)
                {
                    mPoiko->setTexturePosition(texturePosition);
                });
        mPoiko->addAnimation(mPoikoMoveAnimation);

        // 掃除アニメーション
        mPoiko->addAnimation(new PoikoSweepAnimation(
                [&](int texturePosition)
                {
                    mPoiko->setTexturePosition(texturePosition);
                }));
        
        // 穴に落ちるアニメーション
        mPoiko->addAnimation(new PoikoFallAnimation(0.0, 0.0, 0.0, 0.0, mHoleMask));
        
        mPoiko->addAnimation(new PoikoFallOutAnimation(0.0, 239.0 + 127.0, 0.0, 0.0));
        
        // リストに追加
        mPartsList.push_back(mPoiko);

        //
        // ePartsID_poiko_effect
        // ------------------------------
        mPoikoEffect = new PartsBase(ePartsID_poiko_effect, 144.0, 332.0, 52.0, 43.0);
        mPoikoEffect->addTexture(new PartsTexture("poiko_effect_lose.png", 52.0, 43.0));
        mPoikoEffect->hide();
        mPartsList.push_back(mPoikoEffect);

        //
        // ePartsID_poiko_face
        // ------------------------------
        mPoikoFace = new PartsBase(ePartsID_poiko_face, 150.0, 343.0, 100.0, 75.0);
        mPoikoFace->addTexture(new PartsTexture("poiko_face_lose.png", 100.0, 75.0));
        mPoikoFace->addTexture(new PartsTexture("oton_face_lose.png", 100.0, 75.0));
        mPoikoFace->addTexture(new PartsTexture("kotatsu_face_lose.png", 100.0, 75.0));
        mPoikoFace->hide();
        // でかい顔を消すまでの待機(Animationでするのは変だな。。。)
        scaleAnimation = new ScaleAnimation(eAnimId_PoikoFaceWait, 1.0, 1.0, 3000.0);
        mPoikoFace->addAnimation(scaleAnimation);
        mPartsList.push_back(mPoikoFace);

        //
        // ePartsID_serif_telephone
        // ------------------------------
        mSerifTelephone = new PartsBase(ePartsID_serif_telephone, 0, 95, 320, 78);
        mSerifTelephone->addTexture(new PartsTexture("serif_telephone.png", 320, 78));
        mSerifTelephone->hide();
        // 表示アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_Serif_Show_Set);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Appear, 0, 1, 300);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Disappear, 1, 0, 300);
        fadeAnimation->setBeforeOffsetTime(1500);
        orderAnimationSet->addAnimation(fadeAnimation);
        mSerifTelephone->addAnimation(orderAnimationSet);
        mPartsList.push_back(mSerifTelephone);

        //
        // ePartsID_serif_mother
        // ------------------------------
        mSerifMother = new PartsBase(ePartsID_serif_mother, 0, 102, 320, 102);
        mSerifMother->addTexture(new PartsTexture("serif_mother.png", 320, 102));
        mSerifMother->hide();
        // 表示アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_Serif_Show_Set);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Appear, 0, 1, 300);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Disappear, 1, 0, 300);
        fadeAnimation->setBeforeOffsetTime(1500);
        orderAnimationSet->addAnimation(fadeAnimation);
        mSerifMother->addAnimation(orderAnimationSet);
        mPartsList.push_back(mSerifMother);

        //
        // ePartsID_serif_full_garbages
        // ------------------------------
        mSerifFullGarbages = new PartsBase(ePartsID_serif_full_garbages, 0, 100, 320, 75);
        mSerifFullGarbages->addTexture(new PartsTexture("serif_full_garbages.png", 320, 75));
        mSerifFullGarbages->hide();
        // 表示アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_Serif_Show_Set);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Appear, 0, 1, 150);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Disappear, 1, 0, 150);
        fadeAnimation->setBeforeOffsetTime(1000);
        orderAnimationSet->addAnimation(fadeAnimation);
        mSerifFullGarbages->addAnimation(orderAnimationSet);
        mPartsList.push_back(mSerifFullGarbages);

        //
        // ePartsID_hero_drink_plush_light
        // ------------------------------
        mHeroDrinkPlusLight = new PartsBase(ePartsID_hero_drink_plush_light, 62.5, 140, 195, 195);
        mHeroDrinkPlusLight->addTexture(new PartsTexture("hero_drink_plus_light.png", 195, 195));
        mHeroDrinkPlusLight->hide();
        // 表示アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_Serif_Show_Set);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Appear, 0, 1, 300);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Disappear, 1, 0, 300);
        fadeAnimation->setBeforeOffsetTime(1500);
        orderAnimationSet->addAnimation(fadeAnimation);
        mHeroDrinkPlusLight->addAnimation(orderAnimationSet);
        mPartsList.push_back(mHeroDrinkPlusLight);

        //
        // ePartsID_text_hero_drink
        // ------------------------------
        mTextHeroDrink = new PartsBase(ePartsID_text_hero_drink, 0, 130, 320, 38);
        mTextHeroDrink->addTexture(new PartsTexture("text_hero_drink.png", 320, 38));
        mTextHeroDrink->hide();
        // 表示アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_Serif_Show_Set);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Appear, 0, 1, 300);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Disappear, 1, 0, 300);
        fadeAnimation->setBeforeOffsetTime(1500);
        orderAnimationSet->addAnimation(fadeAnimation);
        mTextHeroDrink->addAnimation(orderAnimationSet);
        mPartsList.push_back(mTextHeroDrink);

        //
        // ePartsID_text_get
        // ------------------------------
        mTextGet = new PartsBase(ePartsID_text_get, 0, 300, 320, 70);
        mTextGet->addTexture(new PartsTexture("text_get.png", 320, 70));
        mTextGet->hide();
        // 表示アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_Serif_Show_Set);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Appear, 0, 1, 300);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Disappear, 1, 0, 300);
        fadeAnimation->setBeforeOffsetTime(1500);
        orderAnimationSet->addAnimation(fadeAnimation);
        mTextGet->addAnimation(orderAnimationSet);
        mPartsList.push_back(mTextGet);

        //
        // ePartsID_text_bonus_time
        // ------------------------------
        mTextBonusTime = new PartsBase(ePartsID_text_bonus_time, 0, 90, 320, 84);
        mTextBonusTime->addTexture(new PartsTexture("text_bonus_time.png", 320, 84));
        mTextBonusTime->hide();
        // 消えるアニメーション
        fadeAnimation = new FadeAnimation(eAnimId_BonusTime_Disappear, 1.0, 0.0, 300.0);
        fadeAnimation->setBeforeOffsetTime(2000.0);
        fadeAnimation->setKeepAfter(true);
        mTextBonusTime->addAnimation(fadeAnimation);
        mPartsList.push_back(mTextBonusTime);

        //
        // ePartsID_text_level_up_plus_desript
        // ------------------------------
        mTextLevelUpPlusDescript = new PartsBase(ePartsID_text_level_up_plus_desript, 0, 90, 320, 68);
        mTextLevelUpPlusDescript->addTexture(new PartsTexture("text_level_up_plus_descript.png", 320, 68));
        mTextLevelUpPlusDescript->hide();
        // 表示アニメーション
        orderAnimationSet = new OrderAnimationSet(eAnimId_Serif_Show_Set);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Appear, 0, 1, 300);
        orderAnimationSet->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Serif_Show_Disappear, 1, 0, 300);
        fadeAnimation->setBeforeOffsetTime(1500);
        orderAnimationSet->addAnimation(fadeAnimation);
        mTextLevelUpPlusDescript->addAnimation(orderAnimationSet);
        mPartsList.push_back(mTextLevelUpPlusDescript);
        
        //
        // ePartsID_undergroundTitle
        // ------------------------------
        mUndergroundTitle = new PartsBase(ePartsID_text_bonus_time, 0, 90, 320, 41);
        mUndergroundTitle->addTexture(new PartsTexture("text_jirokichi.png", 320, 41));
        mUndergroundTitle->hide();
        // 消えるアニメーション
        fadeAnimation = new FadeAnimation(eAnimId_Underground_Title_Disppear, 1.0, 0.0, 300.0);
        fadeAnimation->setKeepAfter(true);
        fadeAnimation->setBeforeOffsetTime(2000.0);
        mUndergroundTitle->addAnimation(fadeAnimation);
        mPartsList.push_back(mUndergroundTitle);
        
        //
        // ePartsID_screen
        // ------------------------------
        mScreen = new PartsBase(ePartsID_screen, 0, 0, 320, 568);
        mScreen->addTexture(new PartsTexture("screen.png", 2, 2));
        mScreen->hide();
        
        fadeAnimation = new FadeAnimation(eAnimId_Screen_Appear, 0, 1, 700);
        fadeAnimation->setKeepAfter(true);
        mScreen->addAnimation(fadeAnimation);
        fadeAnimation = new FadeAnimation(eAnimId_Screen_Disppear, 1, 0, 700);
        mScreen->addAnimation(fadeAnimation);
        mPartsList.push_back(mScreen);
        
        
        // 部屋のひみつの準備
        initMissions();

        // GameManagerで管理しているゴミ情報を取得してGarbagePartsを作成する
        if (gameManager != 0)
        {
            std::vector<GarbageData *> list = gameManager->getGarbageList();
            int i;
            for (i = 0; i < list.size(); i++)
            {
                if (list[i] == 0)
                {
                    continue;
                }

                GarbagePartsBase *garbage = GarbagePartsBase::makeGarbageParts(list[i]);
                if (mCallback != 0)
                {
                    garbage->initParts(mCallback->getPartsCallback());
                }
               mGarbageList.push_back(garbage);
            }
        }
        
        std::sort(
                  mSortedPartsList.begin(),
                  mSortedPartsList.end(),
                  [&](PartsBase *left, PartsBase *right)
                  {
                      if (left == 0 || left == NULL
                          || right == 0 || right == NULL)
                      {
                          return true;
                      }
                      
                      double leftBottom = left->getTopOffset() + left->getHeight();
                      double rightBottom = right->getTopOffset() + right->getHeight();
                      
                      return leftBottom < rightBottom;
                  });

        // イベント管理クラスの作成
        mClickManager = new ClickEventManager(mPartsList);
        mTrackingEventManager = new TrackingEventManager(mPartsList);
        
        switchStageInternal(gameManager->getCurrentStage());
        mPausedStageId = gameManager->getCurrentStage();
        
        if (mGetPlayerManager()) {
            mPausedGarbageCanType = mGetPlayerManager()->getGarbageCanType();
        }
        mPausedJirokichi = false;
        
        gameManager->setAddHoleStartTime(0);
#if TEST_JIROKICHI
        gameManager->mShowHoleStartTime = -1;
#endif
    }

    virtual ~AppManager()
    {
        for (GarbagePartsBase* garbage : mGarbageList)
        {
            if (garbage != NULL)
                delete garbage;
        }
        mGarbageList.clear();
        
        for (GemParts* gem : mGemList)
        {
            if (gem != NULL)
                delete gem;
        }
        mGemList.clear();
        
        for (SecretMission* mission : mSecretMissions)
        {
            if (mission != NULL)
                delete mission;
        }
        mSecretMissions.clear();
        
        for (PartsBase* parts : mSortedPartsList)
        {
            if (parts != NULL)
                delete parts;
        }
        mSortedPartsList.clear();
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual void foreground();
    virtual void setCallback(AppManagerCallback *callback);
    virtual std::vector<PartsBase*> getAdditionalTexture();
    virtual void onTouchDown(float ptX, float ptY);
    virtual void onTouchMove(float ptX, float ptY);
    virtual void onTouchUp();

protected:
    virtual void onDraw();

// ------------------------------
// Accesser
// ------------------------------
public:
    void refreshGarbage();
    void refreshGem();
    void levelup();
    void usedTelephoneItem();
    void usedHeroDrink();
    void enteringHole();
    void checkMother();
    void showFullGarbages();
    void onChangedFullness(double rate);
    // ステージ管理
    void startSwitchStage(int newStage, tCallback_stageLayoutTransition callback);
    void endSwitchStage(int newStage);
    
    void setPausedStage(int stageId);
    int getPausedStage();
    void setPausedGarbageCan(int garbageType);
    int getPausedGarbageCan();
    void setPausedJirokichi(bool jirokichi);
    bool getPausedJirokichi();
    void setPausedJirokichiGem(std::vector<GemData *> jirokichiGem);
    void clearPausedJirokichiGem();
    std::vector<GemData *> getPausedJirokichiGem();
    
    void refreshAutoBroom();
    void startAutoBroomWaitingAnimation();

// ------------------------------
// Function
// ------------------------------
private:
    void startPoikoWaitingAnimation();
    void startAutoBroomSweepAnimation(GarbageData *bottomGarbage);
    void startPoikoFallInAnimation();
    void startPoikoFallOutAnimation();
    void checkNeedRefreshGarbageData();
    void checkNeedRefreshGemData();
    void onDrawGarbage();
    void onDrawGem();
    void onDrawTap();
    void checkFinishedAllGarbageAnimation(bool isAutoClean);
    void checkFinishedAllGemAnimation(bool isAutoClean);
    
    // ジロキチの隠れ家
    void goToUndergroundStage();
    
    // ステージ管理
    void switchStageInternal(int newStage);
    
    // 部屋のひみつ
    void initMissions();
    void onMissionClear(int missionId);
    
    // タップエフェクト
    void addTapEffect(double offsetX, double offsetY);
    
    void hideSecret(int stage);
    
    void autoClean();
    
public:
    void goToNormalStage();

};


#endif //TEST_APPMANAGER_H


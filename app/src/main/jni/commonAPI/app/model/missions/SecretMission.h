//
//  SecretMission.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/13.
//
//

#ifndef SecretMission_h
#define SecretMission_h

#include <stdlib.h>
#include <stdio.h>
#include "../../../lib/model/parts/PartsBase.h"
#include "../../parameter/app_parameter.h"
#include "../../../lib/model/animation/general/ScaleAnimation.h"
#include "../../../lib/model/animation/general/TranslateAnimation.h"
#include "../../../lib/model/animation/general/OrderAnimationSet.h"
#include "../../../lib/model/animation/general/MultipleAnimationSet.h"
#include "../../../lib/model/animation/general/FrameAnimation.h"
#include "../../../lib/model/animation/general/CurveAnimation.h"
#include "../../../lib/model/animation/general/FadeAnimation.h"
#include "../../../lib/model/animation/general/PathAnimation.h"

typedef std::function<void(int)> tCallback_missionClear;

//ひみつのミッションをクリアする条件：現在タップしかないが、今後違うアクションが出る可能性がある
enum {
    eTriggerType_None = 0,
    eTriggerType_Tap,
};

/**
 * 部屋のひみつのデータクラス
 */
class SecretMission {
    
// ------------------------------
// Member
// ------------------------------
private:
    tCallback_missionClear mClearCallback;
    
    int mMissionId;
    bool mIsClear;                          //ミッション達成済みフラグ
    int mBonus;                             //報酬（宝石の数）
    bool mIsActive;
    
    //ターゲット
    int mTargetCount;
    int* mTriggerTypes;
    std::vector<PartsBase*> mTargetParts;
    bool* mValidTargets;
    
    //クリアアクション
    std::vector<PartsBase*> mClearParts;
    std::vector<int>        mClearPreAnimationIds;  //宝石取得前のアニメーション
    std::vector<int>        mClearPostAnimationIds; //宝石取得後のアニメーション
    
    //TriggerTypeタップの場合
    int* mTapCount;                         //タップ回数
    int* mCurrentTapCount;                  //タップ回数
    int mLastTapIndex;                      //最後にタップされたパーツのインデックス
    double mLastTapTime;                    //最後にタップした時間
    bool mUseTapEffect;
    
// ------------------------------
// Constructor
// ------------------------------
public:
    SecretMission(int missionId, std::vector<PartsBase*> targetParts,
                  std::vector<PartsBase*> clearParts, tCallback_missionClear callback)
    {
        mClearCallback = callback;
        
        mMissionId = missionId;
        mIsClear = false;
        mTargetParts = targetParts;
        mClearParts = clearParts;
        mLastTapIndex = -1;
        mLastTapTime = -1.0;
        
        switch (missionId) {
            case eSecretMission_DefaultTrashCan:
            {
                mBonus = 1;
                mTargetCount = 1;
                
                mTriggerTypes = new int[mTargetCount];
                mTriggerTypes[0] = eTriggerType_Tap;
                
                mTapCount = new int[mTargetCount];
                mTapCount[0] = 10;
                
                mCurrentTapCount = new int[mTargetCount];
                mCurrentTapCount[0] = 0;
                
                mValidTargets = new bool[mTargetCount];
                mValidTargets[0] = true;
                
                OrderAnimationSet* animationSet = new OrderAnimationSet(eAnimId_secretGemSet);
                
                TranslateAnimation* translateAnimation = new TranslateAnimation(eAnimId_secretGemDown, 0, 111, 0, 0, 250);
                translateAnimation->setBeforeOffsetTime(1000);
                animationSet->addAnimation(translateAnimation);
                
                std::vector<int> textureIds = std::vector<int>();
                textureIds.push_back(0);
                textureIds.push_back(1);
                FrameAnimation* frameAnimation = new FrameAnimation(eAnimId_secretGemChange, textureIds, 1);
                animationSet->addAnimation(frameAnimation);
                
                CurveAnimation* curveAnimation = new CurveAnimation(eAnimId_secretGemOut, mClearParts[0]->getLeftOffset(), mClearParts[0]->getTopOffset(), 352.5, 101, 135, 90, -45, 120);
                animationSet->addAnimation(curveAnimation);
                
                mClearParts[0]->addAnimation(animationSet);
                
                mClearPreAnimationIds.push_back(eAnimId_secretGemSet);
                mClearPostAnimationIds.push_back(0);
                
                mUseTapEffect = true;
            }
                break;
                
            case eSecretMission_DefaultTV:
            {
                mBonus = 1;
                mTargetCount = 1;
                
                mTriggerTypes = new int[mTargetCount];
                mTriggerTypes[0] = eTriggerType_Tap;
                
                mTapCount = new int[mTargetCount];
                mTapCount[0] = 10;
                
                mCurrentTapCount = new int[mTargetCount];
                mCurrentTapCount[0] = 0;
                
                mValidTargets = new bool[mTargetCount];
                mValidTargets[0] = true;
                
                //Preアニメーション
                ScaleAnimation* scaleAnimation = new ScaleAnimation(eAnimId_secret_TVScale, 0, 1, 100);
                scaleAnimation->setKeepAfter(true);
                mClearParts[0]->addAnimation(scaleAnimation);
                
                mClearPreAnimationIds.push_back(eAnimId_secret_TVScale);
                
                //Postアニメーション
                OrderAnimationSet* animationSet = new OrderAnimationSet(eAnimId_secret_TVFadeSet);
                
                scaleAnimation = new ScaleAnimation(eAnimId_secret_TVKeep, 1, 1, 30000);
                animationSet->addAnimation(scaleAnimation);
                
                FadeAnimation* fadeAnimation = new FadeAnimation(eAnimId_secret_TVFade, 1, 0, 300);
                animationSet->addAnimation(fadeAnimation);
                
                mClearParts[0]->addAnimation(animationSet);
                mClearPostAnimationIds.push_back(eAnimId_secret_TVFadeSet);
                
                mUseTapEffect = true;
            }
                break;
                
            case eSecretMission_DefaultRocket:
            {
                mBonus = 1;
                mTargetCount = 1;
                
                mTriggerTypes = new int[mTargetCount];
                mTriggerTypes[0] = eTriggerType_Tap;
                
                mTapCount = new int[mTargetCount];
                mTapCount[0] = 3;
                
                mCurrentTapCount = new int[mTargetCount];
                mCurrentTapCount[0] = 0;
                
                mValidTargets = new bool[mTargetCount];
                mValidTargets[0] = true;
                
                //Preアニメーション
                MultipleAnimationSet* animationSet = new MultipleAnimationSet(eAnimId_secret_RocketSet);
                
                std::vector<int> textureIds = std::vector<int>();
                textureIds.push_back(0);
                textureIds.push_back(1);
                FrameAnimation* frameAnimation = new FrameAnimation(eAnimId_secret_RocketFrame, textureIds, 30);
                frameAnimation->setLooping(true);
                frameAnimation->setBeforeOffsetTime(1000);
                animationSet->addAnimation(frameAnimation);
                frameAnimation->setOnChangeTextureCallback([&](int texturePosition)
                                                           {
                                                               mClearParts[0]->setTexturePosition(texturePosition);
                                                           });
                
                CurveAnimation* curveAnimation = new CurveAnimation(eAnimId_secret_RocketCurve, mClearParts[0]->getLeftOffset(), mClearParts[0]->getTopOffset(), 393, -143, 150, 90, -30, 700);
                curveAnimation->setBeforeOffsetTime(1000);
                animationSet->addAnimation(curveAnimation);
                mClearParts[0]->addAnimation(animationSet);
                
                mClearPreAnimationIds.push_back(eAnimId_secret_RocketSet);
                
                //Postアニメーション
                mClearPostAnimationIds.push_back(0);
                
                mUseTapEffect = true;
            }
                break;
                
            case eSecretMission_PoikoHeart:
            {
                mBonus = 1;
                mTargetCount = 3;
                
                mTriggerTypes = new int[mTargetCount];
                mTriggerTypes[0] = eTriggerType_Tap;
                mTriggerTypes[1] = eTriggerType_Tap;
                mTriggerTypes[2] = eTriggerType_Tap;
                
                mTapCount = new int[mTargetCount];
                mTapCount[0] = 1;
                mTapCount[1] = 1;
                mTapCount[2] = 1;
                
                mCurrentTapCount = new int[mTargetCount];
                mCurrentTapCount[0] = 0;
                mCurrentTapCount[1] = 0;
                mCurrentTapCount[2] = 0;
                
                mValidTargets = new bool[mTargetCount];
                mValidTargets[0] = true;
                mValidTargets[1] = true;
                mValidTargets[2] = true;
                
                int i;
                for (i = 0; i < mTargetCount; i++)
                {
                    //Preアニメーション
                    OrderAnimationSet* animationSet = new OrderAnimationSet(eAnimId_secret_HeartSet);
                    
                    FadeAnimation* fadeAnimation = new FadeAnimation(eAnimId_secret_HeartFadeIn, 0, 1, 600);
                    fadeAnimation->setKeepAfter(true);
                    animationSet->addAnimation(fadeAnimation);
                    
                    std::vector<int> textureIds1 = std::vector<int>();
                    textureIds1.push_back(0);
                    textureIds1.push_back(1);
                    FrameAnimation* frameAnimation = new FrameAnimation(eAnimId_secret_HeartFrame, textureIds1, 50);
                    animationSet->addAnimation(frameAnimation);
                    frameAnimation->setOnChangeTextureCallback([&, i](int texturePosition)
                                                               {
                                                                   mClearParts[i]->setTexturePosition(texturePosition);
                                                               });
                    
                    MultipleAnimationSet* multipleAnimation = new MultipleAnimationSet(eAnimId_secret_HeartFadeSet);
                    
                    ScaleAnimation* scaleAnimation = new ScaleAnimation(eAnimId_secret_HeartScale, 1, 1.1, 600);
                    multipleAnimation->addAnimation(scaleAnimation);
                    
                    fadeAnimation = new FadeAnimation(eAnimId_secret_HeartFadeOut, 1, 0, 600);
                    multipleAnimation->addAnimation(fadeAnimation);
                    
                    animationSet->addAnimation(multipleAnimation);
                    mClearParts[i]->addAnimation(animationSet);
                    
                    mClearPreAnimationIds.push_back(eAnimId_secret_HeartSet);
                    
                    //Postアニメーション
                    mClearPostAnimationIds.push_back(0);
                }
                
                mUseTapEffect = false;
            }
                break;
                
            case eSecretMission_PoikoLamp:
            {
                mBonus = 1;
                mTargetCount = 1;
                
                mTriggerTypes = new int[mTargetCount];
                mTriggerTypes[0] = eTriggerType_Tap;
                
                mTapCount = new int[mTargetCount];
                mTapCount[0] = 10;
                
                mCurrentTapCount = new int[mTargetCount];
                mCurrentTapCount[0] = 0;
                
                mValidTargets = new bool[mTargetCount];
                mValidTargets[0] = true;
                
                //Preアニメーション
                ScaleAnimation* scaleAnimation = new ScaleAnimation(eAnimId_secret_LampStill, 1, 1, 500);
                scaleAnimation->setKeepAfter(true);
                mClearParts[0]->addAnimation(scaleAnimation);
                
                mClearPreAnimationIds.push_back(eAnimId_secret_LampStill);
                
                //Postアニメーション
                FadeAnimation* fadeAnimation = new FadeAnimation(eAnimId_secret_LampFadeOut, 1, 0, 300);
                fadeAnimation->setBeforeOffsetTime(30000);
                mClearParts[0]->addAnimation(fadeAnimation);
                
                mClearPostAnimationIds.push_back(eAnimId_secret_LampFadeOut);
                
                mUseTapEffect = true;
            }
                break;
                
            case eSecretMission_PoikoMouseHole:
            {
                mBonus = 1;
                mTargetCount = 1;
                
                mTriggerTypes = new int[mTargetCount];
                mTriggerTypes[0] = eTriggerType_Tap;
                
                mTapCount = new int[mTargetCount];
                mTapCount[0] = 10;
                
                mCurrentTapCount = new int[mTargetCount];
                mCurrentTapCount[0] = 0;
                
                mValidTargets = new bool[mTargetCount];
                mValidTargets[0] = true;
                
                //Preアニメーション
                MultipleAnimationSet* animationSet = new MultipleAnimationSet(eAnimId_secret_MouseSet);
                
                std::vector<int> textureIds = std::vector<int>();
                textureIds.push_back(0);
                textureIds.push_back(1);
                FrameAnimation* frameAnimation = new FrameAnimation(eAnimId_secret_MouseFrame, textureIds, 50);
                frameAnimation->setLooping(true);
                frameAnimation->setBeforeOffsetTime(1000);
                animationSet->addAnimation(frameAnimation);
                frameAnimation->setOnChangeTextureCallback([&](int texturePosition)
                                                           {
                                                               mClearParts[0]->setTexturePosition(texturePosition);
                                                           });
                
                double pathX[] = {0, 8, 14.5, 15, 29, 37.5, 37, 40, 64, 73.5, 87.5, 104, 116, 138, 154.5, 169, 180, 191.5, 204, 215, 226.5, 237, 245.5};
                double pathY[] = {0, 5, 14, 21.5, 26, 29, 38, 50, 53.5, 54, 52.5, 51.5, 48, 45, 45, 45.5, 47, 51, 55, 57, 60, 61.5, 69};
                double time[] = {0, 9.43, 20.54, 28.05, 42.76, 51.77, 60.79, 73.15, 97.41, 106.92, 121, 137.53, 150.03, 172.24, 188.74, 203.24, 214.35, 226.52, 239.65, 250.83, 262.71, 273.32, 284.65};
                int dataCount = 23;
                PathAnimation* pathAnimation = new PathAnimation(eAnimId_secret_MousePath, pathX, pathY, time, dataCount, 800);
                pathAnimation->setBeforeOffsetTime(1000);
                animationSet->addAnimation(pathAnimation);
                
                mClearParts[0]->addAnimation(animationSet);
                
                mClearPreAnimationIds.push_back(eAnimId_secret_MouseSet);
                
                //Postアニメーション
                mClearPostAnimationIds.push_back(0);
                
                mUseTapEffect = true;
            }
                break;
                
            default:
                break;
        }
    }
    
// ------------------------------
// Destructor
// ------------------------------
public:
    virtual ~SecretMission()
    {
        delete mTriggerTypes;
        delete mTapCount;
        delete mCurrentTapCount;
        delete mValidTargets;
        
        mTargetParts.clear();
        mClearParts.clear();
    }

// ------------------------------
// Accessor
// ------------------------------
public:
    int getMissionId();
    
// ------------------------------
// Function
// ------------------------------
public:
    void makeActive(bool active);
    int checkHit(double ptX, double ptY, double screenWidth, double screenHeight, double time);
    bool useTapEffect();
    
private:
    void checkClear(int position);
    
// ------------------------------
// Class function
// ------------------------------
public:
    static std::string missionIdToItemCode(int missionId);
    
};

#endif /* SecretMission_h */

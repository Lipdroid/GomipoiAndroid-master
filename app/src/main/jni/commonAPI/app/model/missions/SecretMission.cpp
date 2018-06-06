//
//  SecretMission.cpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/13.
//
//

#include "SecretMission.h"

#define TAP_MAX_INTERVAL 1000 //ミリ秒単位、連続タップのMAX間隔

int
SecretMission::getMissionId()
{
    return mMissionId;
}

void
SecretMission::makeActive(bool active)
{
    mIsActive = active;
    
    if (!active)
    {
        for (PartsBase* parts : mClearParts)
        {
            //アニメーションがあれば、中断しvisibilityをオフにする
            parts->stopAnimation();
            parts->hide();
        }
    }
}

int
SecretMission::checkHit(double ptX, double ptY, double screenWidth, double screenHeight, double time)
{
    if (!mIsActive)
    {
        return false;
    }
    
    int i;
    for (i = 0; i < mTargetCount; i++) {
        PartsBase* parts = mTargetParts[i];
        
        //タップできるパーツの判定
        if (mValidTargets[i] && mTriggerTypes[i] == eTriggerType_Tap) {
            
            //タップのあたり判定
            if (ptX >= parts->getLeftOffset() && ptX < (parts->getLeftOffset() + parts->getWidth()) &&
                ptY >= parts->getTopOffset() && ptY < (parts->getTopOffset() + parts->getHeight()))
            {
                //前回タップされたパーツが違うであれば、連続タップ回数をリセットする
                //タップの間が長すぎたら、リセットする
                if (mLastTapIndex != i || mLastTapTime < 0.0 || time - mLastTapTime > TAP_MAX_INTERVAL)
                {
                    mCurrentTapCount[i] = 0;
                }
                
                //時間を記録
                mLastTapIndex = i;
                mLastTapTime = time;
                mCurrentTapCount[i] = mCurrentTapCount[i] + 1;
                
                //連続タップ回数達成のチェック
                if (mCurrentTapCount[i] >= mTapCount[i])
                {
                    //達成したら、パーツ
                    mValidTargets[i] = false;
                    mCurrentTapCount[i] = 0;
                    
                    //アニメーション開始
                    mClearParts[i]->setTexturePosition(0);
                    mClearParts[i]->show();
                    mClearParts[i]->startAnimation(mClearPreAnimationIds[i], [&, i](int animationId)
                                                   {
                                                       checkClear(i);
                                                   });
                }
                
                return eTriggerType_Tap;
            }
        }
    }
    
    return eTriggerType_None;
}

void
SecretMission::checkClear(int position)
{
    if (mClearPostAnimationIds[position] != 0)
    {
        mClearParts[position]->startAnimation(mClearPostAnimationIds[position], [&, position](int animationId)
                                              {
                                                  mClearParts[position]->hide();
                                              });
    }
    else
    {
        mClearParts[position]->hide();
    }
    
    bool allClear = true;
    int i;
    for (i = 0; i < mTargetCount; i++)
    {
        if (mValidTargets[i])
        {
            //まだ押されてないパーツがあれば、ミッション完了の通知を送らない
            allClear = false;
            break;
        }
    }
    
    if (allClear)
    {
        if (mClearCallback != NULL)
        {
            mClearCallback(mMissionId);
        }
    }
}

bool
SecretMission::useTapEffect()
{
    return mUseTapEffect;
}

std::string
SecretMission::missionIdToItemCode(int missionId)
{
    switch (missionId) {
        case eSecretMission_DefaultTrashCan:
            return ITEM_CODE_PLACE_SECRET_1;
            break;
            
        case eSecretMission_DefaultTV:
            return ITEM_CODE_PLACE_SECRET_2;
            break;
            
        case eSecretMission_DefaultRocket:
            return ITEM_CODE_PLACE_SECRET_3;
            break;
            
        case eSecretMission_PoikoHeart:
            return ITEM_CODE_PLACE_SECRET_4;
            break;
            
        case eSecretMission_PoikoLamp:
            return ITEM_CODE_PLACE_SECRET_5;
            break;
            
        case eSecretMission_PoikoMouseHole:
            return ITEM_CODE_PLACE_SECRET_6;
            break;
            
        default:
            return ITEM_CODE_PLACE_SECRET_1;
    }
}


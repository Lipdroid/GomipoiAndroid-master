//
//
//
#include "AppManager.h"

// ------------------------------
// Override
// ------------------------------
void
AppManager::foreground()
{
    AppManagerBase::foreground();
//    checkMother();
}

void
AppManager::setCallback(AppManagerCallback *callback)
{
    AppManagerBase::setCallback(callback);

    // ゴミパーツを作成
    int i;
    for (i = 0; i < mGarbageList.size(); i++)
    {
        if (mGarbageList[i] == 0)
        {
            continue;
        }
        mGarbageList[i]->initParts(callback->getPartsCallback());
    }
    
    for (PartsBase* parts : mSortedPartsList)
    {
        if (parts == NULL)
        {
            continue;
        }
        
        if (mCallback != 0)
        {
            parts->initParts(mCallback->getPartsCallback());
        }
    }

    startPoikoWaitingAnimation();
    startAutoBroomWaitingAnimation();
    checkMother();
}

std::vector<PartsBase*>
AppManager::getAdditionalTexture()
{
    return mSortedPartsList;
}

void
AppManager::onTouchDown(float ptX, float ptY)
{
    if (mCallback == 0 || mCallback->mIsLockedEvent())
    {
        return;
    }

    // クリックアクション中なら以降の処理を無視
    if (mClickManager != 0
        && mClickManager->onTouchedDown(ptX, ptY))
    {
        return;
    }

    if (mGetGameManager == 0)
    {
        return;
    }

    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }

    // なんらかのスワイプイベント中なら、以降の処理を無視
    if (gameManager->getSwipeState() != eSwipeState_None)
    {
        return;
    }
    
    //ステージ切り替え中なら、以降の処理を無視
    if (mInStageTransition)
    {
        return;
    }

    if (mSerifMother->isVisible() || mSerifTelephone->isVisible() || mPoikoFace->isVisible())
    {
        return;
    }

    // スワイプイベント判定に必要なデータを格納
    ptX = (ptX + 1) * mScreenWidth / 2.0;
    ptX = ptX * STANDARD_WIDTH / mScreenWidth;
    ptY = (1 - ptY) * mScreenHeight / 2.0;
    ptY = ptY * STANDARD_HEIGHT / mScreenHeight;
    
    mStartTouchX = ptX;
    mStartTouchY = ptY;
    mLastTouchX = ptX;
    mLastTouchY = ptY;

    // スワイプの可能範囲
    if (ptX < 0 || ptX > 320 || ptY < 170 || ptY > 463)
    {
        return;
    }

    LOGI("#1 : eSwipeState_Swiping");
    gameManager->changeSwipeState(eSwipeState_Swiping);
}

void
AppManager::onTouchMove(float ptX, float ptY)
{
    if (mCallback == 0 || mCallback->mIsLockedEvent())
    {
        return;
    }

    // クリックアクション中なら以降の処理は無視
    if (mClickManager != 0
        && mClickManager->onTouchedMove(ptX, ptY))
    {
        return;
    }

    if (mGetGameManager == 0)
    {
        return;
    }

    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }

    // 必要なデータを格納
    ptX = (ptX + 1) * mScreenWidth / 2.0;
    ptX = ptX * STANDARD_WIDTH / mScreenWidth;
    ptY = (1 - ptY) * mScreenHeight / 2.0;
    ptY = ptY * STANDARD_HEIGHT / mScreenHeight;
    mLastTouchX = ptX;
    mLastTouchY = ptY;
    
    // スワイプイベント中でなければ、以降の処理は無視
    if (gameManager->getSwipeState() != eSwipeState_Swiping)
    {
        return;
    }
}

void
AppManager::onTouchUp()
{
    if (mCallback == 0 || mCallback->mIsLockedEvent())
    {
        return;
    }

    // クリックアクション中なら以降の処理は無視
    if (mClickManager != 0
        && mClickManager->onTouchedUp())
    {
        return;
    }

    if (mGetGameManager == 0)
    {
        return;
    }

    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }
    
    //ボーナスタイムやジロキチの隠れ家の場合、ひみつのパーツをタップできないようにする
    if (!gameManager->isBonusTime() &&
        !gameManager->isZDrinkTime() &&
        !gameManager->isDropTime() &&
        !gameManager->isUndergroundStage())
    {
        // 部屋のひみつのヒット判定を先に行う
        PlayerManager *playerManager = mGetPlayerManager();
        for (SecretMission* mission : mSecretMissions)
        {
            if (playerManager->isValidMission(mission->getMissionId()) &&
                mission->checkHit(mStartTouchX, mStartTouchY, mScreenWidth, mScreenHeight, playerManager->getCurrentTime()) == eTriggerType_Tap)
            {
                //タップアニメーションを行う
                if (mSecretTapEffect != NULL && mission->useTapEffect())
                {
                    addTapEffect(mStartTouchX, mStartTouchY);
                }
                
                //ヒットすれば、ぽい子の移動などを行わない
                if (mGetGameManager != 0)
                {
                    GameManager *gameManager = mGetGameManager();
                    if (gameManager != 0)
                    {
                        LOGI("#5 : eSwipeState_None");
                        gameManager->changeSwipeState(eSwipeState_None);
                    }
                }
                return;
            }
        }
    }

    //スワイプイベント中でなければ、以降の処理は無視
    if (gameManager->getSwipeState() != eSwipeState_Swiping)
    {
        return;
    }

    // スワイプ判定
    mGarbageTargetCount = 0;
    mGarbageFinishCount = 0;
    mComboCount = 0;
    mGemTargetCount = 0;
    mGemFinishCount = 0;

    LOGI("#2 : eSwipeState_DoAnimation");
    gameManager->changeSwipeState(eSwipeState_DoAnimation);

    if (mPoiko == 0)
    {
        // データが不正なら、スワイプ処理のキャンセル
        int i;
        for (i = 0; i < mGarbageList.size(); i++)
        {
            if (mGarbageList[i] == 0)
            {
                continue;
            }
            mGarbageList[i]->resetSwipe();
        }

        LOGI("#3 : eSwipeState_None");
        gameManager->changeSwipeState(eSwipeState_None);

        // 待機アニメーションに変更
        startPoikoWaitingAnimation();
        return;
    }

    double oldLeft = mPoiko->getLeftOffset();
    double oldTop = mPoiko->getTopOffset();
    double newLeft = mStartTouchX - mPoiko->getWidth() / 2.0 + 9.0;
    double newTop = mStartTouchY - mPoiko->getHeight() + 22;

    // ぽい子の移動アニメーション
    if (mPoikoMoveAnimation != 0)
    {
        mPoikoMoveAnimation->setNewOffset(newLeft - oldLeft, -(newTop - oldTop));
    }
    mPoiko->startAnimation(
            eAnimId_Poiko_Move_Set,
            [&, newLeft, newTop](int animationId)
            {
                // ぽい子のOffsetを移動先の座標にセット
                mPoiko->setOffset(newLeft, newTop);
                
                GameManager *gameManager = mGetGameManager();
                if (gameManager == 0)
                {
                    return;
                }
                
                if (gameManager->isUndergroundStage())
                {
                    // 宝石がなければ、待機アニメーション
                    if (mGemList.size() <= 0)
                    {
                        GameManager *gameManager = mGetGameManager();
                        if (gameManager != 0)
                        {
                            gameManager->changeSwipeState(eSwipeState_None);
                        }
                        startPoikoWaitingAnimation();
                        return;
                    }
                    
                    mGetGameManager()->onSweep(mStartTouchX, mStartTouchY, mScreenWidth, mScreenHeight, false);
                    
                    // 宝石のアニメーション
                    std::vector<GemParts*> tmpGemList = mGemList;
                    int i;
                    for (i = (int)tmpGemList.size() - 1; i >= 0; i--)
                    {
                        if (tmpGemList[i] == 0 || tmpGemList[i]->isCompleted())
                        {
                            continue;
                        }
                        
                        // アニメーションの対象でなければ無視
                        tmpGemList[i]->checkHitSwipe(mStartTouchX, mStartTouchY, mScreenWidth, mScreenHeight);
                        if (!tmpGemList[i]->isSwipeTarget())
                        {
                            continue;
                        }
                        
                        // 容量を考慮する
                        if (mGetPlayerManager() != 0)
                        {
                            int broomType = (mGetGameManager == 0 || mGetGameManager() == 0)
                            ? eBroomType_Normal : mGetGameManager()->getBroomType();
                            bool isComplete = (tmpGemList[i]->isLastAnimation(broomType));
                            if (isComplete)
                            {
                                mComboCount += 1;
                            }
                        }
                        
                        // 対象宝石数のカウンターを増やす
                        mGemTargetCount += 1;
                        
                        // アニメーション開始
                        int gotBonus = tmpGemList[i]->getDefaultBonus();
                        tmpGemList[i]->startMoveAnimation(
                                                              mGetGameManager()->getBroomType(),
                                                              [&, i, tmpGemList, gotBonus](int bonus)
                                                              {
                                                                  // ポイント変換に到達したゴミの場合の処理
                                                                  if (tmpGemList[i]->isCompleted() && mGetPlayerManager != 0)
                                                                  {
                                                                      // ツボの上の〇〇Getを表示
                                                                      if (mGetGem != 0)
                                                                      {
                                                                          mGetGem(gotBonus);
                                                                      }
                                                                      
                                                                      
                                                                      // API成功後に宝石数を反映させるため、以下の処理を行わない
                                                                      // 宝石箱の占有率と所持ポイント数を変更
//                                                                      PlayerManager *player = mGetPlayerManager();
//                                                                      if (player != 0)
//                                                                      {
//                                                                          player->changeGem(gotBonus);
//                                                                      }
                                                                  }
                                                                  
                                                                  // スワイプイベントの終了判定
                                                                  checkFinishedAllGemAnimation(false);
                                                              });
                    }
                    
                    if (mGemTargetCount == 0)
                    {
                        // 対象ゴミがなければ、待機アニメーション
                        if (mGetGameManager != 0)
                        {
                            GameManager *gameManager = mGetGameManager();
                            if (gameManager != 0)
                            {
                                LOGI("#5 : eSwipeState_None");
                                gameManager->changeSwipeState(eSwipeState_None);
                            }
                        }
                        startPoikoWaitingAnimation();
                    }
                    else
                    {
                        if (mCallback != 0 && mCallback->mOnPartsEvent != 0)
                        {
                            mCallback->mOnPartsEvent(ePartsEventId_playBroomSe);
                        }
                        
                        // 対象ゴミがあれば、掃除アニメーション
                        mPoiko->startAnimation(
                                               eAnimId_Poiko_Sweep_Set,
                                               [&](int animationId)
                                               {
                                                   // 掃除が終わったら、待機アニメーション
                                                   startPoikoWaitingAnimation();
                                               });
                    }
                }
                else
                {
                    // ゴミがなければ、待機アニメーション
                    if (mGarbageList.size() <= 0)
                    {
                        GameManager *gameManager = mGetGameManager();
                        if (gameManager != 0)
                        {
                            gameManager->changeSwipeState(eSwipeState_None);
                        }
                        startPoikoWaitingAnimation();
                        return;
                    }
                    
                    mGetGameManager()->onSweep(mStartTouchX, mStartTouchY, mScreenWidth, mScreenHeight, false);
                    
                    // ゴミのアニメーション
                    std::vector<GarbagePartsBase*> tmpGarbageList = mGarbageList;
                    int i;
                    for (i = (int)tmpGarbageList.size() - 1; i >= 0; i--)
                    {
                        if (tmpGarbageList[i] == 0 || tmpGarbageList[i]->isCompleted())
                        {
                            continue;
                        }
                        
                        // アニメーションの対象でなければ無視
                        tmpGarbageList[i]->checkHitSwipe(mStartTouchX, mStartTouchY, mScreenWidth, mScreenHeight);
                        if (!tmpGarbageList[i]->isSwipeTarget())
                        {
                            continue;
                        }
                        
                        // 容量を考慮する
                        if (mGetPlayerManager != 0 && mGetPlayerManager() != 0)
                        {
                            PlayerManager *playerManager = mGetPlayerManager();
                            int broomType = (mGetGameManager == 0 || mGetGameManager() == 0)
                            ? eBroomType_Normal : mGetGameManager()->getBroomType();
                            bool isComplete = (tmpGarbageList[i]->isLastAnimation(broomType));
                            if (isComplete)
                            {
                                mComboCount += 1;
                                if (playerManager->isEnabledChangeFullness(mComboCount) != eChangeCeckCode_OK)
                                {
                                    mComboCount -= 1;
                                    tmpGarbageList[i]->resetSwipe();
                                    continue;
                                }
                            }
                        }
                        
                        // 対象ゴミ数のカウンターを増やす
                        mGarbageTargetCount += 1;
                        
                        // アニメーション開始
                        int gotBonus = mGetGameManager()->getBonus(tmpGarbageList[i]->getDefaultBonus(), false, tmpGarbageList[i]->isSp());
                        tmpGarbageList[i]->startMoveAnimation(
                                                              mGetGameManager()->getBroomType(),
                                                              [&, i, tmpGarbageList, gotBonus](int bonus)
                                                              {
                                                                  // ポイント変換に到達したゴミの場合の処理
                                                                  if (tmpGarbageList[i]->isCompleted() && mGetPlayerManager != 0)
                                                                  {
                                                                      // ゴミ箱の上の〇〇Pを表示
                                                                      if (mGetPoint != 0)
                                                                      {
                                                                          mGetPoint(gotBonus);
                                                                      }
                                                                      
                                                                      // ゴミ箱の占有率と所持ポイント数を変更
                                                                      PlayerManager *player = mGetPlayerManager();
                                                                      if (player != 0)
                                                                      {
                                                                          player->changePoint(gotBonus);
                                                                          player->changeFullness(1);
                                                                      }
                                                                  }
                                                                  
                                                                  // スワイプイベントの終了判定
                                                                  checkFinishedAllGarbageAnimation(false);
                                                              });
                    }
                    
                    if (mGarbageTargetCount == 0)
                    {
                        // 対象ゴミがなければ、待機アニメーション
                        if (mGetGameManager != 0)
                        {
                            GameManager *gameManager = mGetGameManager();
                            if (gameManager != 0)
                            {
                                LOGI("#5 : eSwipeState_None");
                                gameManager->changeSwipeState(eSwipeState_None);
                            }
                        }
                        startPoikoWaitingAnimation();
                    }
                    else
                    {
                        if (mCallback != 0 && mCallback->mOnPartsEvent != 0)
                        {
                            mCallback->mOnPartsEvent(ePartsEventId_playBroomSe);
                        }
                        
                        // 対象ゴミがあれば、掃除アニメーション
                        mPoiko->startAnimation(
                                               eAnimId_Poiko_Sweep_Set,
                                               [&](int animationId)
                                               {
                                                   // 掃除が終わったら、待機アニメーション
                                                   startPoikoWaitingAnimation();
                                               });
                    }
                }

            });
}

void
AppManager::onDraw()
{
    if (mGetGameManager != 0 && mGetGameManager() != 0)
    {
        GameManager *gameManager = mGetGameManager();

        // ゴミ箱のタイプによって表示するテクスチャを変更
        if (mGetGameManager()->getCurrentStage() == eStage_Garden)
        {
            mGarbageCan->setTexturePosition(eGarbageCan_Garden);
        }
        else if (mGetPlayerManager()->getGarbageCanType() == eGarbageCanType_XL)
        {
            mGarbageCan->setTexturePosition(eGarbageCan_Xl);
        }
        else
        {
//            mGarbageCan->setTexturePosition(mGetPlayerManager()->getGarbageCanType() - 1);
            mGarbageCan->setTexturePosition(mGetPlayerManager()->getGarbageCanType() - 1 + mGetGameManager()->getCurrentStage() * 3);
        }

        // ヒーロドリンク
        if (mHeroDrink != 0)
        {
            if (gameManager->isShowHeroDrink() && !gameManager->isUndergroundStage())
            {
                mHeroDrink->setOffset(gameManager->getHeroDrinkLeft(), gameManager->getHeroDrinkTop());
                mHeroDrink->show();
            }
            else
            {
                mHeroDrink->hide();
            }
        }

        // 穴
        if (mHole != 0)
        {
            if (gameManager->isShowHole())
            {
                if (!mHole->hasAppeared())
                {
                    mHole->startAppearAnimation([&](int animationId)
                                                {
                                                });
                }
                mHole->show();
            }
            else
            {
                mHole->hide();
            }
        }

        // ボーナスタイム
        if (!mIsGetAnimating && gameManager->isBonusTime())
        {
            if (mBackground != 0)
            {
                mBackground->setTexturePosition(eBackground_BonusTime);
            }

            if (mTextBonusTime != 0 && mTextBonusTime->getCurrentAnimation() == 0)
            {
                mTextBonusTime->startAnimation(
                        eAnimId_BonusTime_Disappear,
                        [&](int animationId)
                        {
                            mTextBonusTime->hide();
                        });
                mTextBonusTime->show();
            }

            if (mPoiko != 0)
            {
                mPoiko->setCharacterType(mGetPlayerManager()->getCharacter());
                mPoiko->onBonusTime();
            }

            if (mSeal != 0)
            {
                mSeal->hide();
            }

            if (gameManager->isUndergroundStage())
            {
                if (mGarbageCan != 0)
                {
                    mGarbageCan->setTexturePosition(eGarbageCan_SecretRoom);
                }
            }
            else
            {
                if (mGarbageCan != 0)
                {
                    if (mGetGameManager()->getCurrentStage() == eStage_Garden)
                    {
                        mGarbageCan->setTexturePosition(eGarbageCan_Garden);
                    }
                    else if (mGetPlayerManager()->getGarbageCanType() == eGarbageCanType_XL)
                    {
                        mGarbageCan->setTexturePosition(eGarbageCan_Xl);
                    }
                    else
                    {
                        mGarbageCan->setTexturePosition(mGetPlayerManager()->getGarbageCanType() - 1 + mGetGameManager()->getCurrentStage() * 3);
                    }
                }
            }
            
            double currentRemainingTime = gameManager->getRemainingBonusTime();
            if (!mTextBonusTime->isVisible() &&
                (mCurrentRemainingBonusTime < 0.0 || (int) mCurrentRemainingBonusTime % 100 != (int) currentRemainingTime % 100)) {
                
                if (mBonusTimeCallback != NULL) {
                    int remainingSeconds = (int) (currentRemainingTime - 1.0) / 1000 + 1;
                    mBonusTimeCallback(remainingSeconds);
                }
            }

            mCurrentRemainingBonusTime = currentRemainingTime;
            if (mCurrentRemainingBonusTime < 0.0) {
                mCurrentRemainingBonusTime = 0.0;
            }
        }
        // Zドリンクによるボーナスタイム
        else if (!mIsGetAnimating && gameManager->isZDrinkTime())
        {
//            if (mTextBonusTime != 0 && mTextBonusTime->getCurrentAnimation() == 0)
//            {
//                mTextBonusTime->startAnimation(
//                                               eAnimId_BonusTime_Disappear,
//                                               [&](int animationId)
//                                               {
//                                                   mTextBonusTime->hide();
//                                               });
//                mTextBonusTime->show();
//            }
            
            if (mPoiko != 0)
            {
                mPoiko->setCharacterType(mGetPlayerManager()->getCharacter());
//                mPoiko->onBonusTime();
            }
            
            double currentRemainingTime = gameManager->getRemainingZDrinkTime();
            if (!mTextBonusTime->isVisible() &&
                (mCurrentRemainingBonusTime < 0.0 || (int) mCurrentRemainingBonusTime % 100 != (int) currentRemainingTime % 100)) {
                
                if (mBonusTimeCallback != NULL) {
                    int remainingSeconds = (int) (currentRemainingTime - 1.0) / 1000 + 1;
                    mBonusTimeCallback(remainingSeconds);
                }
            }

            if (gameManager->isUndergroundStage())
            {
                if (mCurrentRemainingBonusTime >= 0.0) {
                    if (mBonusTimeCallback != NULL) {
                        mBonusTimeCallback(-1);
                    }
                }
                mCurrentRemainingBonusTime = -1;

                if (mSeal != 0)
                {
                    mSeal->hide();
                }

                if (mBackground != 0)
                {
                    mBackground->setTexturePosition(eBackground_SecretRoom);
                }

                if (mGarbageCan != 0)
                {
                    mGarbageCan->setTexturePosition(eGarbageCan_SecretRoom);
                }
            }
            else
            {
                if (mSeal != 0)
                {
                    mSeal->hide();
                }

                if (mBackground != 0)
                {
                    mBackground->setTexturePosition(eBackground_BonusTime);
                }

                if (mGarbageCan != 0)
                {
                    if (mGetGameManager()->getCurrentStage() == eStage_Garden)
                    {
                        mGarbageCan->setTexturePosition(eGarbageCan_Garden);
                    }
                    else if (mGetPlayerManager()->getGarbageCanType() == eGarbageCanType_XL)
                    {
                        mGarbageCan->setTexturePosition(eGarbageCan_Xl);
                    }
                    else
                    {
                        mGarbageCan->setTexturePosition(mGetPlayerManager()->getGarbageCanType() - 1 + mGetGameManager()->getCurrentStage() * 3);
                    }
                }
            }
            
            mCurrentRemainingBonusTime = currentRemainingTime;
            if (mCurrentRemainingBonusTime < 0.0) {
                mCurrentRemainingBonusTime = 0.0;
            }
        }
        // 変身ドロップ
        else if (gameManager->isDropTime())
        {
            double currentRemainingTime = gameManager->getRemainingDropTime();
            if (!mTextBonusTime->isVisible() &&
                (mCurrentRemainingBonusTime < 0.0 || (int) mCurrentRemainingBonusTime % 100 != (int) currentRemainingTime % 100)) {
                
                if (mBonusTimeCallback != NULL) {
                    int remainingSeconds = (int) (currentRemainingTime - 1.0) / 1000 + 1;
                    mBonusTimeCallback(remainingSeconds);
                }
            }
            
            if (gameManager->isUndergroundStage())
            {
                if (mCurrentRemainingBonusTime >= 0.0) {
                    if (mBonusTimeCallback != NULL) {
                        mBonusTimeCallback(-1);
                    }
                }
                mCurrentRemainingBonusTime = -1;
                
                if (mSeal != 0)
                {
                    mSeal->hide();
                }
                
                if (mBackground != 0)
                {
                    mBackground->setTexturePosition(eBackground_SecretRoom);
                }
                
                if (mGarbageCan != 0)
                {
                    mGarbageCan->setTexturePosition(eGarbageCan_SecretRoom);
                }
            }
            else
            {
                if (mBackground != 0)
                {
                    mBackground->setTexturePosition(gameManager->getCurrentStage());
                }
                
                // 封印シールの表示状態を変更
                if (mSeal != 0)
                {
                    if (gameManager->isSealing() && !gameManager->isUndergroundStage())
                    {
                        mSeal->show();
                    }
                    else
                    {
                        mSeal->hide();
                    }
                }
                
                if (mGarbageCan != 0)
                {
                    if (mGetGameManager()->getCurrentStage() == eStage_Garden)
                    {
                        mGarbageCan->setTexturePosition(eGarbageCan_Garden);
                    }
                    else if (mGetPlayerManager()->getGarbageCanType() == eGarbageCanType_XL)
                    {
                        mGarbageCan->setTexturePosition(eGarbageCan_Xl);
                    }
                    else
                    {
                        mGarbageCan->setTexturePosition(mGetPlayerManager()->getGarbageCanType() - 1 + mGetGameManager()->getCurrentStage() * 3);
                    }
                }
            }
            
            mCurrentRemainingBonusTime = currentRemainingTime;
            if (mCurrentRemainingBonusTime < 0.0) {
                mCurrentRemainingBonusTime = 0.0;
            }

        }
        else if (gameManager->isUndergroundStage())
        {
            if (mCurrentRemainingBonusTime >= 0.0) {
                if (mBonusTimeCallback != NULL) {
                    mBonusTimeCallback(-1);
                }
            }
            mCurrentRemainingBonusTime = -1;
            
            if (mSeal != 0)
            {
                mSeal->hide();
            }
            
            if (mBackground != 0)
            {
                mBackground->setTexturePosition(eBackground_SecretRoom);
            }
            
            if (mGarbageCan != 0)
            {
                mGarbageCan->setTexturePosition(eGarbageCan_SecretRoom);
            }
        }
        else
        {
            if (mCurrentRemainingBonusTime >= 0.0) {
                if (mBonusTimeCallback != NULL) {
                    mBonusTimeCallback(-1);
                }
            }
            mCurrentRemainingBonusTime = -1;
            
            if (mBackground != 0)
            {
                mBackground->setTexturePosition(gameManager->getCurrentStage());
            }
            
            // 庭のゴミ箱テクスチャとXLゴミ箱
            if (mGarbageCan != 0)
            {
                if (mGetGameManager()->getCurrentStage() == eStage_Garden)
                {
                    mGarbageCan->setTexturePosition(eGarbageCan_Garden);
                }
                else if (mGetPlayerManager()->getGarbageCanType() == eGarbageCanType_XL)
                {
                    mGarbageCan->setTexturePosition(eGarbageCan_Xl);
                }
                else
                {
                    mGarbageCan->setTexturePosition(mGetPlayerManager()->getGarbageCanType() - 1 + mGetGameManager()->getCurrentStage() * 3);
                }
            }

            if (mTextBonusTime != 0)
            {
                mTextBonusTime->stopAnimation();
                mTextBonusTime->hide();
            }

            // 箒のタイプによって表示するテクスチャを変更
            if (mPoiko != 0)
            {
                mPoiko->setCharacterType(mGetPlayerManager()->getCharacter());
                mPoiko->setBroomType(gameManager->getBroomType());
            }

            // 封印シールの表示状態を変更
            if (mSeal != 0)
            {
                if (gameManager->isSealing() && !gameManager->isUndergroundStage())
                {
                    mSeal->show();
                }
                else
                {
                    mSeal->hide();
                }
            }
        }


    }

    if (mGetGameManager != 0 && mGetGameManager() != 0)
    {
        GameManager *gameManager = mGetGameManager();
        
        int i;
        for (i = 0; i < mPartsList.size(); i++)
        {
            if (mPartsList[i] == 0)
            {
                continue;
            }
            
            // ヒーロードリンクより先にゴミを描画する
            if (mPartsList[i]->getPartsId() == ePartsID_hero_drink)
            {
                if (gameManager->isUndergroundStage())
                {
                    onDrawGem();
                }
                else
                {
                    onDrawGarbage();
                }
                
                onDrawTap();
            }
            
            if (!mPartsList[i]->isVisible())
            {
                continue;
            }
            
            // Vertextsを作成
            GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
            mPartsList[i]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);
            
            // Texcoordを作成
            GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
            mPartsList[i]->getCurrentTexcoord(texcoord);
            
            // 描画処理
            drawShader(mPartsList[i], vertexts, texcoord);
            
            // 再生中のAnimationがあれば、状態を確認する
            if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
            {
                AnimationBase *animation = mPartsList[i]->getCurrentAnimation();
                if (animation != 0)
                {
                    animation->checkState();
                }
            }
        }
    }
    
}

// ------------------------------
// Accesser
// ------------------------------
/**
 * 描画するゴミを最新のデータに更新する
 */
void
AppManager::refreshGarbage()
{
    if (mGetGameManager == 0)
    {
        return;
    }

    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }

    // スワイプイベント中は更新しない
    if (gameManager->getSwipeState() != eSwipeState_OnRefreshed)
    {
        return;
    }

    // 現在のリストはクリア
#if !IS_ANDROID
    for (GarbagePartsBase* garbage : mGarbageList)
    {
        if (garbage != NULL)
            delete garbage;
    }
#endif
    mGarbageList.clear();

    // 新しいリストを取得し、GarbagePartsを作成する
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
        if (mDummyGarbage != 0)
        {
            if (garbage->getSpriteSheet() == 0) {
                garbage->setTextureId(0, mDummyGarbage->getCurrentTextureId());
            }
            else if (garbage->getSpriteSheet() == 1) {
                garbage->setTextureId(0, mDummyGarbage2->getCurrentTextureId());
            }
            else
            {
                garbage->setTextureId(0, mDummyGarbage3->getCurrentTextureId());
            }
        }
        mGarbageList.push_back(garbage);
    }

    LOGI("#6 : eSwipeState_None");
    gameManager->changeSwipeState(eSwipeState_None);
}

/**
 * 描画するゴミを最新のデータに更新する
 */
void
AppManager::refreshGem()
{
    if (mGetGameManager == 0)
    {
        return;
    }
    
    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }
    
    // スワイプイベント中は更新しない
    if (gameManager->getSwipeState() != eSwipeState_OnRefreshed && gameManager->getSwipeState() != eSwipeState_None)
    {
        return;
    }
    
    // 現在のリストはクリア
#if !IS_ANDROID
    for (GemParts* gem : mGemList)
    {
        if (gem != NULL)
            delete gem;
    }
#endif
    mGemList.clear();
    
    // 新しいリストを取得し、GarbagePartsを作成する
    std::vector<GemData *> list = gameManager->getGemList();
    int i;
    for (i = 0; i < list.size(); i++)
    {
        if (list[i] == 0)
        {
            continue;
        }
        
        GemParts *gem = GemParts::makeGemParts(list[i]);
        if (mCallback != 0)
        {
            gem->initParts(mCallback->getPartsCallback());
        }
        if (mDummyGem != 0)
        {
            gem->setTextureId(0, mDummyGem->getCurrentTextureId());
        }
        
        mGemList.push_back(gem);
    }
    
    LOGI("#6 : eSwipeState_None");
    gameManager->changeSwipeState(eSwipeState_None);
}

/**
 * レベルアップ時の処理
 */
void
AppManager::levelup()
{
    if (mCallback != 0 && mCallback->mOnPartsEvent != 0)
    {
        mCallback->mOnPartsEvent(ePartsEventId_levelUp);
    }

    // レベルアップ文言をアニメーション表示
    if (mTextLevelUpPlusDescript != 0)
    {
        mTextLevelUpPlusDescript->startAnimation(
                eAnimId_Serif_Show_Set,
                [&](int animationId)
                {
                    mTextLevelUpPlusDescript->hide();
                });
        mTextLevelUpPlusDescript->show();
    }
}

/**
 * 業者に電話使用時の処理
 */
void
AppManager::usedTelephoneItem()
{
    // 業者のセリフをアニメーション表示
    if (mSerifTelephone != 0)
    {
        mSerifTelephone->startAnimation(
                eAnimId_Serif_Show_Set,
                [&](int animationId)
                {
                    mSerifTelephone->hide();
                    if (mGetPlayerManager != 0)
                    {
                        mGetPlayerManager()->onUsedTelephoneItem();
                    }
                });
        mSerifTelephone->show();
    }
}

/**
 * ヒーロードリンく使用の処理を行う
 */
void
AppManager::usedHeroDrink()
{
    if (mGetGameManager()->isZDrinkTime() || mGetGameManager()->isDropTime())
    {
        return;
    }
    
    mIsGetAnimating = true;

    if (mHeroDrinkPlusLight != 0)
    {
        mHeroDrinkPlusLight->startAnimation(
                eAnimId_Serif_Show_Set,
                [&](int animationId)
                {
                    mHeroDrinkPlusLight->stopAnimation();
                    mHeroDrinkPlusLight->hide();
                });
        mHeroDrinkPlusLight->show();
    }

    if (mTextGet != 0)
    {
        mTextGet->startAnimation(
                eAnimId_Serif_Show_Set,
                [&](int animationId)
                {
                    mTextGet->stopAnimation();
                    mTextGet->hide();
                });
        mTextGet->show();
    }

    if (mTextHeroDrink != 0)
    {
        mTextHeroDrink->startAnimation(
                eAnimId_Serif_Show_Set,
                [&](int animationId)
                {
                    mTextHeroDrink->stopAnimation();
                    mTextHeroDrink->hide();
                    mIsGetAnimating = false;
                });
        mTextHeroDrink->show();
    }
}

/**
 * 穴への移動処理 + 穴に落ちる処理
 */
void
AppManager::enteringHole()
{
    //すでに切り替え中なら、何もしない
    if (mInStageTransition || mIsMotherAnimation || mIsGetAnimating)
        return;
    
    if (mPoiko != 0 || mHoleMask != 0)
    {
        double oldLeft = mPoiko->getLeftOffset();
        double oldTop = mPoiko->getTopOffset();
        
        //ぽい子を穴の上に移動させる
        double newLeft = mHoleMask->getLeftOffset() + 10.0;
        double newTop = mHoleMask->getTopOffset();
        
        tCallback_onCompletedAnim completion = [&, newLeft, newTop](int animationId)
        {
            // ぽい子のOffsetを移動先の座標にセット
            mPoiko->setOffset(newLeft, newTop);
            
            // 待機アニメーション
            GameManager *gameManager = mGetGameManager();
            if (gameManager != 0)
            {
                LOGI("#4 : eSwipeState_None");
                gameManager->changeSwipeState(eSwipeState_None);
            }
            startPoikoWaitingAnimation();
            
            startPoikoFallInAnimation();
        };
        
        //ぽい子が違う場所にいたら、移動させる
        if (oldLeft != newLeft || oldTop != newTop)
        {
            if (mPoikoMoveAnimation != 0)
            {
                mPoikoMoveAnimation->setNewOffset(newLeft - oldLeft, -(newTop - oldTop));
            }
            
            mPoiko->startAnimation(eAnimId_Poiko_Move_Set, completion);
        }
        else
        {
            //同じbあしょであれば、そのまま次のアニメーションに移す
            completion(eAnimId_Poiko_Move_Set);
        }
    }
}

/**
 * ゴミ箱がいっぱいなことを伝える
 */
void
AppManager::showFullGarbages()
{
    // セリフを表示する
    if (mSerifFullGarbages != 0 && !mSerifFullGarbages->isVisible())
    {
        // Java, Objective-Cに伝える
        if (mCallback != 0 && mCallback->mOnPartsEvent != 0)
        {
            mCallback->mOnPartsEvent(ePartsEventId_fullGarbages);
        }

        mSerifFullGarbages->startAnimation(
                eAnimId_Serif_Show_Set,
                [&](int animationId)
                {
                    mSerifFullGarbages->hide();
                });
        mSerifFullGarbages->show();
    }
}

/**
 * ゴミ箱の容量に変更のあった時の処理
 */
void
AppManager::onChangedFullness(double rate)
{
    if (mTextFull == 0)
    {
        return;
    }

    if (rate >= 1.0 && !mTextFull->isVisible())
    {
        mTextFull->show();
        mTextFull->startAnimation(eAnimId_TextFull_Set);
        return;
    }

    if (rate < 1.0 && mTextFull->isVisible())
    {
        mTextFull->stopAnimation();
        mTextFull->hide();
        return;
    }

}

/**
 * ステージ切り替えの処理（前半）
 */
void
AppManager::startSwitchStage(int newStage, tCallback_stageLayoutTransition callback)
{
    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        if (callback != NULL) {
            callback();
        }
        return;
    }
    
    // 同じなら何もしない
    if (mInStageTransition || gameManager->getCurrentStage() == newStage || newStage < 0) {
        if (callback != NULL) {
            callback();
        }
        return;
    }
    
    mInStageTransition = true;
    if (mAutoBroom != 0)
    {
        mAutoBroom->stopAnimation();
    }
    if (mGageFrame != 0)
    {
        mGageFrame->stopAnimation();
    }
    if (mGageLeft != 0)
    {
        mGageLeft->stopAnimation();
    }
    if (mGageMiddle != 0)
    {
        mGageMiddle->stopAnimation();
    }
    if (mGageRight != 0)
    {
        mGageRight->stopAnimation();
    }

    mScreen->startAnimation(eAnimId_Screen_Appear,
                            [&, newStage, callback](int animationId)
                            {
                                if (callback != NULL) {
                                    callback();
                                }
                                endSwitchStage(newStage);
                                checkMother();
                            });
    mScreen->show();
}

/**
 * ステージ切り替えの処理（後半）
 */
void
AppManager::endSwitchStage(int newStage)
{
    switchStageInternal(newStage);
    
    mScreen->startAnimation(eAnimId_Screen_Disppear,
                            [&](int animationId)
                            {
                                mInStageTransition = false;
                                mScreen->hide();

                                GameManager *gameManager = mGetGameManager();
                                if (gameManager != 0)
                                {
                                    gameManager->changeSwipeState(eSwipeState_None);
                                }
                                startAutoBroomWaitingAnimation();
                            });
}

void
AppManager::setPausedStage(int stageId)
{
    mPausedStageId = stageId;
}

int
AppManager::getPausedStage()
{
    return mPausedStageId;
}

void
AppManager::setPausedGarbageCan(int garbageType)
{
    mPausedGarbageCanType = garbageType;
}

int
AppManager::getPausedGarbageCan()
{
    return mPausedGarbageCanType;
}

void
AppManager::setPausedJirokichi(bool jirokichi)
{
    mPausedJirokichi = jirokichi;
}

bool
AppManager::getPausedJirokichi()
{
    return mPausedJirokichi;
}

void
AppManager::setPausedJirokichiGem(std::vector<GemData *> jirokichiGem)
{
    mPausedJirokichiGem = jirokichiGem;
}

void
AppManager::clearPausedJirokichiGem()
{
    mPausedJirokichiGem.clear();
}

std::vector<GemData *>
AppManager::getPausedJirokichiGem()
{
    return mPausedJirokichiGem;
}

void
AppManager::refreshAutoBroom()
{
    if (mAutoBroom != 0)
    {
        GameManager *gameManager = mGetGameManager();
        if (gameManager != 0)
        {
            mAutoBroom->setOwned(gameManager->isOwnedAutoBroom());
            if (gameManager->getCurrentStage() == eStage_Garden)
            {
                mAutoBroom->hide();
            }

            if (gameManager->getCurrentStage() != eStage_Garden && gameManager->isUsedBattery())
            {
                if (mGageFrame != 0)
                {
                    mGageFrame->show();
                }
                startAutoBroomSweepAnimation(gameManager->getBottomGarbage());
            }
            else
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
            }
        }
    }
}

// ------------------------------
// Function
// ------------------------------
/**
 * ぽい子の待機アニメーション開始
 */
void
AppManager::startPoikoWaitingAnimation()
{
    if (mPoiko != 0)
    {
        mPoiko->startFrameAnimation(eAnimId_Poiko_Normal, mOnCompletedPoikoWaitingAnimation);
        mPoiko->show();
    }
}

void
AppManager::startAutoBroomWaitingAnimation()
{
    GameManager *gameManager = mGetGameManager();
    if (gameManager != 0 && gameManager->isUndergroundStage())
    {
        if (mAutoBroom != 0)
        {
            mAutoBroom->hide();
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
        if (mGageFrame != 0)
        {
            mGageFrame->hide();
        }
    }
    else if (gameManager != 0 && gameManager->isOwnedAutoBroom() && gameManager->isUsedBattery()) {
        mAutoBroom->show();
        mAutoBroom->startFrameAnimation(eAnimId_AutoBroom_Normal, mOnCompletedAutoBroomWaitingAnimation);
    }
    else if (!gameManager->isOwnedAutoBroom())
    {
        if (mAutoBroom != 0)
        {
            mAutoBroom->hide();
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
        if (mGageFrame != 0)
        {
            mGageFrame->hide();
        }
    }
    else
    {
        mAutoBroom->setTexturePosition(eAutoBroomTexture_Off);
        mAutoBroom->show();
    }
}

void
AppManager::startAutoBroomSweepAnimation(GarbageData *bottomGarbage)
{
    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        GameManager *gameManager = mGetGameManager();
        if (gameManager != 0)
        {
            gameManager->changeSwipeState(eSwipeState_None);
        }
        startAutoBroomWaitingAnimation();
        return;
    }

    if (bottomGarbage == 0)
    {
        GameManager *gameManager = mGetGameManager();
        if (gameManager != 0)
        {
            gameManager->changeSwipeState(eSwipeState_None);
        }
        startAutoBroomWaitingAnimation();
        return;
    }

    mAutoStartTouchX = bottomGarbage->getCurrentLeft() + bottomGarbage->getWidth() / 2.0f;
    mAutoStartTouchY = bottomGarbage->getCurrentTop() + bottomGarbage->getHeight() / 2.0f;
    mAutoLastTouchX = bottomGarbage->getCurrentLeft() + bottomGarbage->getWidth() / 2.0f;
    mAutoLastTouchY = bottomGarbage->getCurrentTop() + bottomGarbage->getHeight() / 2.0f;
    gameManager->changeSwipeState(eSwipeState_Swiping);
    autoClean();

}

void
AppManager::autoClean()
{
//    if (mCallback == 0 || mCallback->mIsLockedEvent())
//    {
//        return;
//    }
//
//    // クリックアクション中なら以降の処理は無視
//    if (mClickManager != 0
//        && mClickManager->onTouchedUp())
//    {
//        return;
//    }

    if (mGetGameManager == 0)
    {
        return;
    }

    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }

    //ボーナスタイムやジロキチの隠れ家の場合、ひみつのパーツをタップできないようにする
//    if (!gameManager->isBonusTime() &&
//        !gameManager->isZDrinkTime() &&
//        !gameManager->isDropTime() &&
//        !gameManager->isUndergroundStage())
//    {
//        // 部屋のひみつのヒット判定を先に行う
//        PlayerManager *playerManager = mGetPlayerManager();
//        for (SecretMission* mission : mSecretMissions)
//        {
//            if (playerManager->isValidMission(mission->getMissionId()) &&
//                mission->checkHit(mAutoStartTouchX, mAutoStartTouchY, mScreenWidth, mScreenHeight, playerManager->getCurrentTime()) == eTriggerType_Tap)
//            {
//                //タップアニメーションを行う
//                if (mSecretTapEffect != NULL && mission->useTapEffect())
//                {
//                    addTapEffect(mAutoStartTouchX, mAutoStartTouchY);
//                }
//
//                //ヒットすれば、ぽい子の移動などを行わない
//                if (mGetGameManager != 0)
//                {
//                    GameManager *gameManager = mGetGameManager();
//                    if (gameManager != 0)
//                    {
//                        LOGI("#5 : eSwipeState_None");
//                        gameManager->changeSwipeState(eSwipeState_None);
//                    }
//                }
//                return;
//            }
//        }
//    }

    //スワイプイベント中でなければ、以降の処理は無視
    if (gameManager->getSwipeState() != eSwipeState_Swiping)
    {
        return;
    }

    // スワイプ判定
    mGarbageTargetCount = 0;
    mGarbageFinishCount = 0;
    mComboCount = 0;
    mGemTargetCount = 0;
    mGemFinishCount = 0;

    LOGI("#2 : eSwipeState_DoAnimation");
    gameManager->changeSwipeState(eSwipeState_DoAnimation);

    if (mAutoBroom == 0)
    {
        // データが不正なら、スワイプ処理のキャンセル
        int i;
        for (i = 0; i < mGarbageList.size(); i++)
        {
            if (mGarbageList[i] == 0)
            {
                continue;
            }
            mGarbageList[i]->resetSwipe();
        }

        LOGI("#3 : eSwipeState_None");
        gameManager->changeSwipeState(eSwipeState_None);
        startAutoBroomWaitingAnimation();
        return;
    }

    double oldLeft = mAutoBroom->getLeftOffset();
    double oldTop = mAutoBroom->getTopOffset();
    double newLeft = mAutoStartTouchX - mAutoBroom->getWidth() / 2.0 + 9.0;
    double newTop = mAutoStartTouchY - mAutoBroom->getHeight() + 22;

    // ぽい子の移動アニメーション
    if (mAutoBroomMoveAnimation != 0)
    {
        mAutoBroomMoveAnimation->setNewOffset(newLeft - oldLeft, -(newTop - oldTop));
    }
    LOGI("AutoBroomMove");
    mAutoBroom->startAnimation(
                           eAnimId_AutoBroom_Move_Set,
                           [&, newLeft, newTop](int animationId)
                           {
                               // ぽい子のOffsetを移動先の座標にセット
                               mAutoBroom->setOffset(newLeft, newTop);

                               GameManager *gameManager = mGetGameManager();
                               if (gameManager == 0)
                               {
                                   return;
                               }

                               if (gameManager->isUndergroundStage())
                               {
                                   // 宝石がなければ、待機アニメーション
                                   if (mGemList.size() <= 0)
                                   {
                                       GameManager *gameManager = mGetGameManager();
                                       if (gameManager != 0)
                                       {
                                           gameManager->changeSwipeState(eSwipeState_None);
                                       }
                                       startAutoBroomWaitingAnimation();
                                       return;
                                   }

                                   mGetGameManager()->onSweep(mAutoStartTouchX, mAutoStartTouchY, mScreenWidth, mScreenHeight, true);

                                   // 宝石のアニメーション
                                   std::vector<GemParts*> tmpGemList = mGemList;
                                   int i;
                                   for (i = (int)tmpGemList.size() - 1; i >= 0; i--)
                                   {
                                       if (tmpGemList[i] == 0 || tmpGemList[i]->isCompleted())
                                       {
                                           continue;
                                       }

                                       // アニメーションの対象でなければ無視
                                       tmpGemList[i]->checkHitSwipe(mAutoStartTouchX, mAutoStartTouchY, mScreenWidth, mScreenHeight);
                                       if (!tmpGemList[i]->isSwipeTarget())
                                       {
                                           continue;
                                       }

                                       // 容量を考慮する
                                       if (mGetPlayerManager() != 0)
                                       {
                                           int broomType = eBroomType_Normal;
                                           bool isComplete = (tmpGemList[i]->isLastAnimation(broomType));
                                           if (isComplete)
                                           {
                                               mComboCount += 1;
                                           }
                                       }

                                       // 対象宝石数のカウンターを増やす
                                       mGemTargetCount += 1;

                                       // アニメーション開始
                                       int gotBonus = tmpGemList[i]->getDefaultBonus();
                                       tmpGemList[i]->startMoveAnimation(
                                                                         mGetGameManager()->getBroomType(),
                                                                         [&, i, tmpGemList, gotBonus](int bonus)
                                                                         {
                                                                             // ポイント変換に到達したゴミの場合の処理
                                                                             if (tmpGemList[i]->isCompleted() && mGetPlayerManager != 0)
                                                                             {
                                                                                 // ツボの上の〇〇Getを表示
                                                                                 if (mGetGem != 0)
                                                                                 {
                                                                                     mGetGem(gotBonus);
                                                                                 }


                                                                                 // API成功後に宝石数を反映させるため、以下の処理を行わない
                                                                                 // 宝石箱の占有率と所持ポイント数を変更
                                                                                 //                                                                      PlayerManager *player = mGetPlayerManager();
                                                                                 //                                                                      if (player != 0)
                                                                                 //                                                                      {
                                                                                 //                                                                          player->changeGem(gotBonus);
                                                                                 //                                                                      }
                                                                             }

                                                                             // スワイプイベントの終了判定
                                                                             checkFinishedAllGemAnimation(true);
                                                                         });
                                   }

                                   if (mGemTargetCount == 0)
                                   {
                                       // 対象ゴミがなければ、待機アニメーション
                                       if (mGetGameManager != 0)
                                       {
                                           GameManager *gameManager = mGetGameManager();
                                           if (gameManager != 0)
                                           {
                                               LOGI("#5 : eSwipeState_None");
                                               gameManager->changeSwipeState(eSwipeState_None);
                                           }
                                       }
                                       startAutoBroomWaitingAnimation();
                                   }
                                   else
                                   {
                                       if (mCallback != 0 && mCallback->mOnPartsEvent != 0)
                                       {
                                           mCallback->mOnPartsEvent(ePartsEventId_playBroomSe);
                                       }

                                       // 対象ゴミがあれば、掃除アニメーション
                                       mAutoBroom->startAnimation(
                                                              eAnimId_AutoBroom_Sweep_Set,
                                                              [&](int animationId)
                                                              {
                                                                  // 掃除が終わったら、待機アニメーション
                                                                  GameManager *gameManager = mGetGameManager();
                                                                  if (gameManager != 0)
                                                                  {
                                                                      gameManager->changeSwipeState(eSwipeState_None);
                                                                  }
                                                                  startAutoBroomWaitingAnimation();
                                                              });
                                   }
                               }
                               else
                               {
                                   // ゴミがなければ、待機アニメーション
                                   if (mGarbageList.size() <= 0)
                                   {
                                       GameManager *gameManager = mGetGameManager();
                                       if (gameManager != 0)
                                       {
                                           gameManager->changeSwipeState(eSwipeState_None);
                                       }
                                       startAutoBroomWaitingAnimation();
                                       return;
                                   }

                                   mGetGameManager()->onSweep(mAutoStartTouchX, mAutoStartTouchY, mScreenWidth, mScreenHeight, true);

                                   // ゴミのアニメーション
                                   std::vector<GarbagePartsBase*> tmpGarbageList = mGarbageList;
                                   int i;
                                   for (i = (int)tmpGarbageList.size() - 1; i >= 0; i--)
                                   {
                                       if (tmpGarbageList[i] == 0 || tmpGarbageList[i]->isCompleted())
                                       {
                                           continue;
                                       }

                                       // アニメーションの対象でなければ無視
                                       tmpGarbageList[i]->checkHitSwipe(mAutoStartTouchX, mAutoStartTouchY, mScreenWidth, mScreenHeight);
                                       if (!tmpGarbageList[i]->isSwipeTarget())
                                       {
                                           continue;
                                       }

                                       // 容量を考慮する
                                       if (mGetPlayerManager != 0 && mGetPlayerManager() != 0)
                                       {
                                           PlayerManager *playerManager = mGetPlayerManager();
                                           int broomType = eBroomType_Normal;
                                           bool isComplete = (tmpGarbageList[i]->isLastAnimation(broomType));
                                           if (isComplete)
                                           {
                                               mComboCount += 1;
                                               if (playerManager->isEnabledChangeFullness(mComboCount) != eChangeCeckCode_OK)
                                               {
                                                   mComboCount -= 1;
                                                   tmpGarbageList[i]->resetSwipe();
                                                   continue;
                                               }
                                           }
                                       }

                                       // 対象ゴミ数のカウンターを増やす
                                       mGarbageTargetCount += 1;

                                       // アニメーション開始
                                       int gotBonus = mGetGameManager()->getBonus(tmpGarbageList[i]->getDefaultBonus(), true, tmpGarbageList[i]->isSp());
                                       tmpGarbageList[i]->startMoveAnimation(
                                                                             eBroomType_Normal,
                                                                             [&, i, tmpGarbageList, gotBonus](int bonus)
                                                                             {
                                                                                 // ポイント変換に到達したゴミの場合の処理
                                                                                 if (tmpGarbageList[i]->isCompleted() && mGetPlayerManager != 0)
                                                                                 {
                                                                                     // ゴミ箱の上の〇〇Pを表示
                                                                                     if (mGetPoint != 0)
                                                                                     {
                                                                                         mGetPoint(gotBonus);
                                                                                     }

                                                                                     // ゴミ箱の占有率と所持ポイント数を変更
                                                                                     PlayerManager *player = mGetPlayerManager();
                                                                                     if (player != 0)
                                                                                     {
                                                                                         player->changePoint(gotBonus);
                                                                                         player->changeFullness(1);
                                                                                     }
                                                                                 }

                                                                                 // スワイプイベントの終了判定
                                                                                 checkFinishedAllGarbageAnimation(true);
                                                                             });
                                   }

                                   if (mGarbageTargetCount == 0)
                                   {
                                       // 対象ゴミがなければ、待機アニメーション
                                       if (mGetGameManager != 0)
                                       {
                                           GameManager *gameManager = mGetGameManager();
                                           if (gameManager != 0)
                                           {
                                               LOGI("#5 : eSwipeState_None");
                                               gameManager->changeSwipeState(eSwipeState_None);
                                           }
                                       }
                                       startAutoBroomWaitingAnimation();
                                   }
                                   else
                                   {
                                       if (mCallback != 0 && mCallback->mOnPartsEvent != 0)
                                       {
                                           mCallback->mOnPartsEvent(ePartsEventId_playBroomSe);
                                       }

                                       // 対象ゴミがあれば、掃除アニメーション
                                       mAutoBroom->startAnimation(
                                                              eAnimId_AutoBroom_Sweep_Set,
                                                              [&](int animationId)
                                                              {
                                                                  // 掃除が終わったら、待機アニメーション
                                                                  GameManager *gameManager = mGetGameManager();
                                                                  if (gameManager != 0)
                                                                  {
                                                                      gameManager->changeSwipeState(eSwipeState_None);
                                                                  }
                                                                  startAutoBroomWaitingAnimation();
                                                              });
                                   }
                               }

                           });

    // ゲージもついて動く
    if (mGageFrameAnimation != 0)
    {
        mGageFrameAnimation->setPosition(0,
                                         0,
                                         newLeft - oldLeft,
                                         -(newTop - oldTop));
    }
    if (mGageLeftAnimation != 0)
    {
        mGageLeftAnimation->setPosition(0,
                                        0,
                                        newLeft - oldLeft,
                                        -(newTop - oldTop));
    }
    double middleDifX = 0.0;
    if (mGageMiddleAnimation != 0)
    {
        middleDifX = mGageMiddle->getLeftOffset() - mGageLeft->getLeftOffset();
        mGageMiddleAnimation->setPosition(0,
                                        0,
                                        newLeft - oldLeft,
                                        -(newTop - oldTop));
    }
    double rightDifX = 0.0;
    if (mGageRightAnimation != 0)
    {
        rightDifX = mGageRight->getLeftOffset() - mGageLeft->getLeftOffset();
        mGageRightAnimation->setPosition(0,
                                          0,
                                          newLeft - oldLeft,
                                          -(newTop - oldTop));
    }

    double frameX = newLeft + BATTERY_GAGE_FRAME_DIFF_X;
    double frameY = newTop + BATTERY_GAGE_FRAME_DIFF_Y;
    mGageFrame->startAnimation(eAnimId_BatteryGage_Move,
                               [&, frameX, frameY](int animationId)
                               {
                                   mGageFrame->setOffset(frameX,
                                                         frameY);
                               });

    double leftX = frameX + 2.5;
    double leftY = frameY;
    if (mGageLeft != 0)
    {
        mGageLeft->startAnimation(eAnimId_BatteryGageLeft_Move,
                                   [&, leftX, leftY](int animationId)
                                   {
                                       mGageLeft->setOffset(leftX,
                                                             leftY);
                                   });
    }

    double middleX = leftX + middleDifX;
    double middleY = frameY;
    if (mGageMiddle != 0)
    {
        mGageMiddle->startAnimation(eAnimId_BatteryGageMiddle_Move,
                                  [&, middleX, middleY](int animationId)
                                  {
                                      mGageMiddle->setOffset(middleX,
                                                            middleY);
                                  });
    }

    double rightX = leftX + rightDifX;
    double rightY = frameY;
    if (mGageRight != 0)
    {
        mGageRight->startAnimation(eAnimId_BatteryGageRight_Move,
                                    [&, rightX, rightY](int animationId)
                                    {
                                        mGageRight->setOffset(rightX,
                                                               rightY);
                                    });
    }
    
}


/**
 * ぽい子が穴に落ちるアニメーション開始
 */
void
AppManager::startPoikoFallInAnimation()
{
    if (mPoiko != 0)
    {
        PoikoFallAnimation *animation = (PoikoFallAnimation *) mPoiko->getAnimation(eAnimId_Hole_FallIn);
        
        if (animation != NULL)
        {
            double newTopDiff = - mPoiko->getHeight();
            
            animation->setPosition(0.0, 0.0, 0.0, newTopDiff);
        }
        
        mInStageTransition = true;
        
        mPoiko->startAnimation(eAnimId_Hole_FallIn,
                               [&](int animationId)
                               {
                                   mPoiko->hide();
                                   
                                   //アニメーション終わったら、フェイドアウトする
                                   mScreen->startAnimation(eAnimId_Screen_Appear,
                                                           [&](int animationId)
                                                           {
                                                               goToUndergroundStage();
                                                           });
                                   mScreen->show();
                               });
        mPoiko->show();
    }
}

/**
 * ぽい子が穴から落ちるアニメーション開始
 */
void
AppManager::startPoikoFallOutAnimation()
{
    if (mPoiko != 0)
    {
        PoikoFallAnimation *animation = (PoikoFallAnimation *) mPoiko->getAnimation(eAnimId_Hole_FallIn);
        
        if (animation != NULL)
        {
            double newTopDiff = - mPoiko->getHeight();
            
            animation->setPosition(0.0, 0.0, 0.0, newTopDiff);
        }
        
        mPoiko->startAnimation(eAnimId_Hole_FallOut,
                               [&](int animationId)
                               {
                                   if (mUndergroundTitle != 0)
                                   {
                                       mUndergroundTitle->startAnimation(eAnimId_Underground_Title_Disppear,
                                                                         [&](int animationId)
                                                                         {
                                                                             mUndergroundTitle->hide();
                                                                         });
                                       mUndergroundTitle->show();
                                   }
                                   
                                   mInStageTransition = false;
                                   startPoikoWaitingAnimation();
                                   startAutoBroomWaitingAnimation();
                               });
        mPoiko->show();
    }
}

/**
 * 隠れ家への切り替え
 */
void
AppManager::goToUndergroundStage()
{
    GameManager *gameManager = mGetGameManager();
    if (gameManager != 0)
    {
        gameManager->setUndergroundStage(true);
        gameManager->makeGemList();
        
        for (SecretMission* mission : mSecretMissions)
        {
            //新しいステージのひみつかどうかをチェック
            bool isCurrentSecret = gameManager->isCurrentSecret(mission->getMissionId());
            mission->makeActive(isCurrentSecret);
        }
    }
    
    if (mEnterUndergroundCallback != NULL) {
        mEnterUndergroundCallback(true);
    }
    
    hideSecret(gameManager->getCurrentStage());
    refreshGem();
    mUndergroundTitle->show();
    if (mTextFull != 0) {
        if (mTextFull->isVisible()) {
            mTextFull->stopAnimation();
            mTextFull->hide();
        }
    }

    mScreen->startAnimation(eAnimId_Screen_Disppear,
                            [&](int animationId)
                            {
                                mScreen->hide();

                                startPoikoFallOutAnimation();
                            });
}

void
AppManager::hideSecret(int stage)
{
    switch (stage) {
        case eStage_Default:
            if (mSecretTrashCanGem != NULL) {
                mSecretTrashCanGem->hide();
            }
            if (mSecretTVScreen != NULL) {
                mSecretTVScreen->hide();
            }
            if (mSecretFlyingRocket != NULL) {
                mSecretFlyingRocket->hide();
            }
            break;
            
        case eStage_PoikoRoom:
            if (mSecretShowHeart1 != NULL) {
                mSecretShowHeart1->hide();
            }
            if (mSecretShowHeart2 != NULL) {
                mSecretShowHeart2->hide();
            }
            if (mSecretShowHeart3 != NULL) {
                mSecretShowHeart3->hide();
            }
            if (mSecretLightLamp != NULL) {
                mSecretLightLamp->hide();
            }
            if (mSecretMouse != NULL) {
                mSecretMouse->hide();
            }
            break;
            
        case eStage_Garden:
            break;
            
        default:
            break;
    }
}

/**
 * 通常のステージへの切り替え
 */
void
AppManager::goToNormalStage()
{
    mInStageTransition = true;
    
    mScreen->startAnimation(eAnimId_Screen_Appear,
                            [&](int animationId)
                            {
                                GameManager *gameManager = mGetGameManager();
                                if (gameManager != 0)
                                {
                                    gameManager->setUndergroundStage(false);
                                    mHole->resetAppeared();
                                    
                                    for (SecretMission* mission : mSecretMissions)
                                    {
                                        //新しいステージのひみつかどうかをチェック
                                        bool isCurrentSecret = gameManager->isCurrentSecret(mission->getMissionId());
                                        mission->makeActive(isCurrentSecret);
                                    }
                                }
                                
                                PlayerManager *playerManager = mGetPlayerManager();
                                if (playerManager != 0) {
                                    double rate = playerManager->getFullnessRate();
                                    onChangedFullness(rate);
                                }
                                
                                if (mEnterUndergroundCallback != NULL) {
                                    mEnterUndergroundCallback(false);
                                }
                                
                                mScreen->startAnimation(eAnimId_Screen_Disppear,
                                                        [&](int animationId)
                                                        {
                                                            mScreen->hide();
                                                            
                                                            startPoikoWaitingAnimation();
                                                            startAutoBroomWaitingAnimation();
                                                            mInStageTransition = false;
                                                        });
                            });
    mScreen->show();
    
    if (mUndergroundGemGotCallback != NULL)
    {
        int gemCount = 0;
        if (mGetPlayerManager != 0 && mGetPlayerManager() != 0)
        {
            gemCount = mGetPlayerManager()->getUndergroundJewelCount();
        }
        mUndergroundGemGotCallback(gemCount);
    }
}

/**
 * ステージ切り替えの処理
 */
void
AppManager::switchStageInternal(int newStage)
{
    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }
    
    mHole->resetAppeared();
    hideSecret(gameManager->getCurrentStage());
    
    gameManager->setCurrentStage(newStage);
    
    for (SecretMission* mission : mSecretMissions)
    {
        //新しいステージのひみつかどうかをチェック
        bool isCurrentSecret = gameManager->isCurrentSecret(mission->getMissionId());
        mission->makeActive(isCurrentSecret);
    }
    
    if (mChangeStageCallback != NULL) {
        mChangeStageCallback(newStage);
    }
}

/**
 * ひみつの初期化
 */
void
AppManager::initMissions()
{
    tCallback_missionClear missionClearCalback = [&](int missionId) {
        onMissionClear(missionId);
    };
    
    //お茶の間：ゴミ箱
    std::vector<PartsBase*> secretTargetParts1 = std::vector<PartsBase*>();
    std::vector<PartsBase*> secretClearParts1 = std::vector<PartsBase*>();
    
    secretTargetParts1.push_back(mSecretTrashCan);
    secretClearParts1.push_back(mSecretTrashCanGem);
    
    SecretMission* mission1 = new SecretMission(eSecretMission_DefaultTrashCan,
                                                secretTargetParts1,
                                                secretClearParts1,
                                                missionClearCalback);
    mSecretMissions.push_back(mission1);
    
    //お茶の間：テレビ
    std::vector<PartsBase*> secretTargetParts2 = std::vector<PartsBase*>();
    std::vector<PartsBase*> secretClearParts2 = std::vector<PartsBase*>();
    
    secretTargetParts2.push_back(mSecretTV);
    secretClearParts2.push_back(mSecretTVScreen);
    
    SecretMission* mission2 = new SecretMission(eSecretMission_DefaultTV,
                                                secretTargetParts2,
                                                secretClearParts2,
                                                missionClearCalback);
    mSecretMissions.push_back(mission2);
    
    //お茶の間：宇宙ロケット
    std::vector<PartsBase*> secretTargetParts3 = std::vector<PartsBase*>();
    std::vector<PartsBase*> secretClearParts3 = std::vector<PartsBase*>();
    
    secretTargetParts3.push_back(mSecretRocket);
    secretClearParts3.push_back(mSecretFlyingRocket);
    
    SecretMission* mission3 = new SecretMission(eSecretMission_DefaultRocket,
                                                secretTargetParts3,
                                                secretClearParts3,
                                                missionClearCalback);
    mSecretMissions.push_back(mission3);
    
    //ぽい子の部屋：ハート
    std::vector<PartsBase*> secretTargetParts4 = std::vector<PartsBase*>();
    std::vector<PartsBase*> secretClearParts4 = std::vector<PartsBase*>();
    
    secretTargetParts4.push_back(mSecretHeart1);
    secretTargetParts4.push_back(mSecretHeart2);
    secretTargetParts4.push_back(mSecretHeart3);
    secretClearParts4.push_back(mSecretShowHeart1);
    secretClearParts4.push_back(mSecretShowHeart2);
    secretClearParts4.push_back(mSecretShowHeart3);
    
    SecretMission* mission4 = new SecretMission(eSecretMission_PoikoHeart,
                                                secretTargetParts4,
                                                secretClearParts4,
                                                missionClearCalback);
    mSecretMissions.push_back(mission4);
    
    //ぽい子の部屋：ランプ
    std::vector<PartsBase*> secretTargetParts5 = std::vector<PartsBase*>();
    std::vector<PartsBase*> secretClearParts5 = std::vector<PartsBase*>();
    
    secretTargetParts5.push_back(mSecretLamp);
    secretClearParts5.push_back(mSecretLightLamp);
    
    SecretMission* mission5 = new SecretMission(eSecretMission_PoikoLamp,
                                                secretTargetParts5,
                                                secretClearParts5,
                                                missionClearCalback);
    mSecretMissions.push_back(mission5);
    
    //ぽい子の部屋：ネズミ穴
    std::vector<PartsBase*> secretTargetParts6 = std::vector<PartsBase*>();
    std::vector<PartsBase*> secretClearParts6 = std::vector<PartsBase*>();
    
    secretTargetParts6.push_back(mSecretMouseHole);
    secretClearParts6.push_back(mSecretMouse);
    
    SecretMission* mission6 = new SecretMission(eSecretMission_PoikoMouseHole,
                                                secretTargetParts6,
                                                secretClearParts6,
                                                missionClearCalback);
    mSecretMissions.push_back(mission6);
}

/**
 * ひみつのミッションをクリアしたコールバック
 */
void
AppManager::onMissionClear(int missionId)
{
    if (mGetPlayerManager != 0 && mGetPlayerManager() != 0)
    {
        PlayerManager *playerManager = mGetPlayerManager();
        if (playerManager->isValidMission(missionId)) {
            playerManager->consumeMission(missionId);
        }
    }
    
    if (mClearMissionCallback != NULL)
    {
        mClearMissionCallback(missionId);
    }
}

/**
 * タップエフェクトを追加する
 */
void
AppManager::addTapEffect(double offsetX, double offsetY)
{
    if (mSecretTapEffect == NULL)
        return;
    
    double tapWidth = mSecretTapEffect->getWidth();
    double tapHeight = mSecretTapEffect->getHeight();
    
    PartsBase* tap = new PartsBase(ePartsID_secretTapEffect,
                                   offsetX - tapWidth / 2.0, offsetY - tapHeight / 2.0,
                                   45, 45);
    tap->addTexture(new PartsTexture("tap_ballon.png", 45, 45));
    
    FadeAnimation* fadeAnimation = new FadeAnimation(eAnimId_secretTap, 1, 0, 50);
    fadeAnimation->setBeforeOffsetTime(100);
    fadeAnimation->setKeepAfter(true);
    tap->addAnimation(fadeAnimation);
    mTapList.push_back(tap);
   
    if (mCallback != 0)
    {
        tap->initParts(mCallback->getPartsCallback());
    }
    if (mDummyGem != 0)
    {
        tap->setTextureId(0, mSecretTapEffect->getCurrentTextureId());
    }
    tap->show();
    
    tap->startAnimation(eAnimId_secretTap, [&, tap](int animationId)
                        {
                            tap->hide();
                        });
}

/**
 * ゴミアニメーションが終了しているか(最新のデータを取得する必要があるか)をチェックする
 */
void
AppManager::checkNeedRefreshGarbageData()
{
    if (mGetGameManager == 0)
    {
        return;
    }

    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }

    // 待機中や、掃除中などの動きのない場合は更新する必要はないので、抜ける
    if (gameManager->getSwipeState() != eSwipeState_OnFinished && gameManager->getSwipeState() != eSwipeState_OnFinishedAutoClean)
    {
        return;
    }

    // スワイプイベントを修了して、ゴミリストを最新のものに更新
    LOGI("#7 : eSwipeState_OnRefreshed");
    gameManager->changeSwipeState(eSwipeState_OnRefreshed);
    refreshGarbage();
}

/**
 * 宝石アニメーションが終了しているか(最新のデータを取得する必要があるか)をチェックする
 */
void
AppManager::checkNeedRefreshGemData()
{
    if (mGetGameManager == 0)
    {
        return;
    }
    
    GameManager *gameManager = mGetGameManager();
    if (gameManager == 0)
    {
        return;
    }
    
    // 待機中や、掃除中などの動きのない場合は更新する必要はないので、抜ける
    if (gameManager->getSwipeState() != eSwipeState_OnFinished && gameManager->getSwipeState() != eSwipeState_OnFinishedAutoClean)
    {
        return;
    }
    
    // スワイプイベントを修了して、ゴミリストを最新のものに更新
    LOGI("#7 : eSwipeState_OnRefreshed");
    gameManager->changeSwipeState(eSwipeState_OnRefreshed);
    refreshGem();
}

/**
 * ゴミパーツを描画する
 */
void
AppManager::onDrawGarbage()
{
    // アニメーションが終了しているか(最新のデータを取得する必要があるか)をチェックする
    checkNeedRefreshGarbageData();

    int i;
    // DummyPartsからTextureIDをセット
    if (mGarbageList.size() > 0 &&
        mGarbageList[0]->getCurrentTextureId() == 0 &&
        mDummyGarbage->getCurrentTextureId() != 0)
    {
        for (i = 0; i < mGarbageList.size(); i++)
        {
            if (mGarbageList[i]->getSpriteSheet() == 0) {
                mGarbageList[i]->setTextureId(0, mDummyGarbage->getCurrentTextureId());
            }
            else if (mGarbageList[i]->getSpriteSheet() == 1) {
                mGarbageList[i]->setTextureId(0, mDummyGarbage2->getCurrentTextureId());
            }
            else if (mGarbageList[i]->getSpriteSheet() == 2) {
                mGarbageList[i]->setTextureId(0, mDummyGarbage3->getCurrentTextureId());
            }
        }
    }

    // ゴミパーツの描画
    int sortedListPosition = 0;
    for (i = 0; i < mGarbageList.size(); i++)
    {
        if (mGarbageList[i] == 0 || !mGarbageList[i]->isVisible())
        {
            continue;
        }
        
        //sortedPartsListの描画
        while (sortedListPosition < (int) mSortedPartsList.size() &&
               (mSortedPartsList[sortedListPosition] == 0 || !mSortedPartsList[sortedListPosition]->isVisible()))
        {
            sortedListPosition++;
        }
        
        while (sortedListPosition < (int) mSortedPartsList.size())
        {
            double currentY = mGarbageList[i]->getBottom();
            double partsY = mSortedPartsList[sortedListPosition]->getCurrentTop() + mSortedPartsList[sortedListPosition]->getHeight();
            
            if (partsY <= currentY)
            {
                // Vertextsを作成
                GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
                mSortedPartsList[sortedListPosition]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);
                
                // Texcoordを作成
                GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
                mSortedPartsList[sortedListPosition]->getCurrentTexcoord(texcoord);
                
                // 描画処理
                drawShader(mSortedPartsList[sortedListPosition], vertexts, texcoord);
                
                // 再生中のAnimationがあれば、状態を確認する
                if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
                {
                    AnimationBase *animation = mSortedPartsList[sortedListPosition]->getCurrentAnimation();
                    if (animation != 0)
                    {
                        animation->checkState();
                    }
                }
                
                sortedListPosition++;
            }
            else {
                break;
            }
        }

        // Vertextsを作成
        GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
        mGarbageList[i]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);

        // Texcoordを作成
        GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
        mGarbageList[i]->getCurrentTexcoord(texcoord);

        // 描画処理
        drawShader(mGarbageList[i], vertexts, texcoord);
        
        // 再生中のAnimationがあれば、状態を確認する
        if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
        {
            AnimationBase *animation = mGarbageList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->checkState();
            }
        }
    }
    
    //残りのsortedPartsListの描画
    while (sortedListPosition < (int) mSortedPartsList.size() &&
           (mSortedPartsList[sortedListPosition] == 0 || !mSortedPartsList[sortedListPosition]->isVisible()))
    {
        sortedListPosition++;
    }
    
    while (sortedListPosition < (int) mSortedPartsList.size())
    {
        // Vertextsを作成
        GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
        mSortedPartsList[sortedListPosition]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);
        
        // Texcoordを作成
        GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
        mSortedPartsList[sortedListPosition]->getCurrentTexcoord(texcoord);
        
        // 描画処理
        drawShader(mSortedPartsList[sortedListPosition], vertexts, texcoord);
        
        // 再生中のAnimationがあれば、状態を確認する
        if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
        {
            AnimationBase *animation = mSortedPartsList[sortedListPosition]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->checkState();
            }
        }
        
        sortedListPosition++;
    }

}

/**
 * 宝石パーツを描画する
 */
void
AppManager::onDrawGem()
{
    // アニメーションが終了しているか(最新のデータを取得する必要があるか)をチェックする
    checkNeedRefreshGemData();
    
    int i;
    if (mGemList.size() > 0 && mGemList[0]->getCurrentTextureId() == 0 && mDummyGem->getCurrentTextureId() != 0)
    {
        for (i = 0; i < mGemList.size(); i++)
        {
            mGemList[i]->setTextureId(0, mDummyGem->getCurrentTextureId());
        }
    }
    
    // 宝石パーツの描画
    int sortedListPosition = 0;
    for (i = 0; i < mGemList.size(); i++)
    {
        if (mGemList[i] == 0 || !mGemList[i]->isVisible())
        {
            continue;
        }
        
        //sortedPartsListの描画
        while (sortedListPosition < (int) mSortedPartsList.size() &&
               (mSortedPartsList[sortedListPosition] == 0 || !mSortedPartsList[sortedListPosition]->isVisible()))
        {
            sortedListPosition++;
        }
        
        while (sortedListPosition < (int) mSortedPartsList.size())
        {
            double currentY = mGarbageList[i]->getBottom();
            double partsY = mSortedPartsList[sortedListPosition]->getCurrentTop() + mSortedPartsList[sortedListPosition]->getHeight();
            
            if (partsY <= currentY)
            {
                // Vertextsを作成
                GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
                mSortedPartsList[sortedListPosition]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);
                
                // Texcoordを作成
                GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
                mSortedPartsList[sortedListPosition]->getCurrentTexcoord(texcoord);
                
                // 描画処理
                drawShader(mSortedPartsList[sortedListPosition], vertexts, texcoord);
                
                // 再生中のAnimationがあれば、状態を確認する
                if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
                {
                    AnimationBase *animation = mSortedPartsList[sortedListPosition]->getCurrentAnimation();
                    if (animation != 0)
                    {
                        animation->checkState();
                    }
                }
                
                sortedListPosition++;
            }
            else {
                break;
            }
        }
        
        // Vertextsを作成
        GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
        mGemList[i]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);
        
        // Texcoordを作成
        GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
        mGemList[i]->getCurrentTexcoord(texcoord);
        
        // 描画処理
        drawShader(mGemList[i], vertexts, texcoord);
        
        // 再生中のAnimationがあれば、状態を確認する
        if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
        {
            AnimationBase *animation = mGemList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->checkState();
            }
        }
    }
    
    //残りのsortedPartsListの描画
    while (sortedListPosition < (int) mSortedPartsList.size() &&
           (mSortedPartsList[sortedListPosition] == 0 || !mSortedPartsList[sortedListPosition]->isVisible()))
    {
        sortedListPosition++;
    }
    
    while (sortedListPosition < (int) mSortedPartsList.size())
    {
        // Vertextsを作成
        GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
        mSortedPartsList[sortedListPosition]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);
        
        // Texcoordを作成
        GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
        mSortedPartsList[sortedListPosition]->getCurrentTexcoord(texcoord);
        
        // 描画処理
        drawShader(mSortedPartsList[sortedListPosition], vertexts, texcoord);
        
        // 再生中のAnimationがあれば、状態を確認する
        if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
        {
            AnimationBase *animation = mSortedPartsList[sortedListPosition]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->checkState();
            }
        }
        
        sortedListPosition++;
    }
    
}

/**
 * タップパーツを描画する
 */
void
AppManager::onDrawTap()
{
    // アニメーションが終了しているか(最新のデータを取得する必要があるか)をチェックする
    int i;
    while (i < (int) mTapList.size())
    {
        PartsBase* tap = mTapList[i];
        
        if (!tap->isVisible())
        {
            mTapList.erase(mTapList.begin() + i);
            delete tap;
        }
        else i++;
    }
    
    
    // DummyPartsからTextureIDをセット
    if (mTapList.size() > 0 && mTapList[0]->getCurrentTextureId() == 0 && mSecretTapEffect->getCurrentTextureId() != 0)
    {
        for (i = 0; i < mTapList.size(); i++)
        {
            mTapList[i]->setTextureId(0, mSecretTapEffect->getCurrentTextureId());
        }
    }
    
    // タップパーツの描画
    for (i = 0; i < mTapList.size(); i++)
    {
        if (mTapList[i] == 0 || !mTapList[i]->isVisible())
        {
            continue;
        }
        
        // Vertextsを作成
        GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
        mTapList[i]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);
        
        // Texcoordを作成
        GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
        mTapList[i]->getCurrentTexcoord(texcoord);
        
        // 描画処理
        drawShader(mTapList[i], vertexts, texcoord);
        
        // 再生中のAnimationがあれば、状態を確認する
        if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
        {
            AnimationBase *animation = mTapList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->checkState();
            }
        }
    }
    
}

/**
 * 全てのゴミアニメーションが終了したかをチェックし、終了していたら、終了処理を行う
 */
void
AppManager::checkFinishedAllGarbageAnimation(bool isAutoClean)
{
    // 全てのゴミアニメーションが終了しているかをチェック
    mGarbageFinishCount += 1;
    if (mGarbageFinishCount < mGarbageTargetCount)
    {
        return;
    }

    if (mGetGameManager == 0)
    {
        return;
    }

    // コンボボーナス
    if (mComboCount >= 10)
    {
        int bonusPoint = (int)(mComboCount / 10) * 10;

        if (mGetPlayerManager != 0 && mGetPlayerManager() != 0)
        {
            mGetPlayerManager()->changePoint(bonusPoint);
        }

        if (mGetComboBonus != 0)
        {
            mGetComboBonus(mComboCount, bonusPoint);
        }
    }

    // 終了処理
    GameManager *gameManager = mGetGameManager();
    if (gameManager != 0)
    {
        LOGI("#8 : eSwipeState_OnFinished");
        gameManager->changeSwipeState(isAutoClean ? eSwipeState_OnFinishedAutoClean : eSwipeState_OnFinished);
    }
}

/**
 * 全ての宝石アニメーションが終了したかをチェックし、終了していたら、終了処理を行う
 */
void
AppManager::checkFinishedAllGemAnimation(bool isAutoClean)
{
    // 全ての宝石アニメーションが終了しているかをチェック
    mGemFinishCount += 1;
    if (mGemFinishCount < mGemTargetCount)
    {
        return;
    }
    
    if (mGetGameManager == 0)
    {
        return;
    }
    
    // 終了処理
    GameManager *gameManager = mGetGameManager();
    if (gameManager != 0)
    {
        LOGI("#8 : eSwipeState_OnFinished");
        gameManager->changeSwipeState(isAutoClean ? eSwipeState_OnFinishedAutoClean : eSwipeState_OnFinished);
        
        if (gameManager->checkUndergroundStageEnd())
        {
            goToNormalStage();
        }
    }
}

/**
 * 母親イベントを発生させるかをチェックする
 */
void
AppManager::checkMother()
{
    if (mGetGameManager == 0 || mGetPlayerManager == 0)
    {
        return;
    }

    if (!mGetGameManager()->onCheckMother() ||
        mGetGameManager()->isUndergroundStage() ||
        mGetGameManager()->isUsedBattery())
    {
        return;
    }

    LOGI("#9 : eSwipeState_OnRefreshed");
    mGetGameManager()->changeSwipeState(eSwipeState_OnRefreshed);
    refreshGarbage();

    // 母親のセリフを表示
    if (mSerifMother != 0)
    {
        mSerifMother->startAnimation(
                eAnimId_Serif_Show_Set,
                [&](int animationId)
                {
                    mSerifMother->hide();
                });
        mSerifMother->show();
    }

    if (mPoiko == 0)
    {
        return;
    }

    // 待機アニメーションは停止しとく
    mPoiko->stopAnimation();

    // ぽい子の大きな顔を表示
    if (mPoikoFace != 0)
    {
        int texturePosition = ePoikoFaceTexture_Poiko;
        switch (mGetPlayerManager()->getCharacter())
        {
            case eCharacter_Oton:
                texturePosition = ePoikoFaceTexture_Oton;
                break;
            case eCharacter_Kotatsu:
                texturePosition = ePoikoFaceTexture_Kotatsu;
                break;
        }
        mPoikoFace->setTexturePosition(texturePosition);
        mPoikoFace->setOffset(mPoiko->getLeftOffset(), mPoiko->getTopOffset() - 7.0);
        
        mIsMotherAnimation = true;
        
        mPoikoFace->startAnimation(
                eAnimId_PoikoFaceWait,
                [&](int animId)
                {
                    mIsMotherAnimation = false;
                    
                    // ぽい子関連のパーツを非表示
                    mPoikoFace->hide();
                    if (mPoikoEffect != 0)
                    {
                        mPoikoEffect->hide();
                    }

                    // 待機アニメーション再開
                    startPoikoWaitingAnimation();
                });
        mPoikoFace->show();
    }

    // ぽい子のエフェクトを表示
    if (mPoikoEffect != 0)
    {
        mPoikoEffect->setOffset(mPoiko->getLeftOffset() - 6.0, mPoiko->getTopOffset() - 18.0);
        mPoikoEffect->show();
    }
}

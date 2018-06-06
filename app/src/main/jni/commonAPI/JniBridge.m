//
//  JniBridge.m
//  JanPoi
//
//  Created by 藤原 克明 on 2016/02/23.
//  Copyright © 2016年 alc. All rights reserved.
//

#import "JniBridge.h"
#import "GameView.h"
#import "SoundManager.h"
#import "PreferenceManager.h"
#import "GBNotificationDef.h"
#import "GBUserAppManager.h"
#import "GBGameManager.h"
#import "GBSynthesisSuccessVC.h"
#import "UIUtility.h"
#import "GameFragment.h"
#import "GBBookGarbageData.h"

#include <string>
#include "app_parameter.h"

extern void     foregroundApplication();
extern void     backgroundApplication();
extern void     setInitData(double bonusStart, double addGarbageStart, double heroDrinkAppearTime, double zDrinkStartTime, double dropStartTime);
extern void     onCheckTime(int isForegroundFirst);
extern int      getGem();
extern int      getPoint();
extern int      getLevel();
extern int      getMaxLevel();
extern double   getFullness();
extern int      getExperiencePoint();
extern int      getCurrentLevelRequiredPoint();
extern int      getNextLevelRequiredPoint();
extern void     addGem(int addValue);
extern int      getCurrentBroomType();
extern void     changeCharacter(int type);
extern void     changeBroomType(int type);
extern int      getCurrentGarbageCanType();
extern void     changeGarbageCanType(int type);
extern void     changeStage(int stage);
extern void     saveData(int nextStage);
extern int      getCurrentStage();
extern void     useSeal();
extern void     useTelephone();
extern void     AddPoint(int addValue);
extern void     onReceivedUserAppsSelfResponse(std::string json);
extern void     onReceivedGomipoiGarbageOwnResponse(std::string json);
extern void     onReceivedGomipoiGameLoadResponse(std::string json);
extern void     onReceivedGomipoiItemOwnResponse(std::string json);
extern int      getItemOwnCount(std::string item_code);
extern bool     isItemUsing(std::string item_code);
extern void     onReceivedGomipoiBookOwnResponse(std::string json);
extern void     onReceivedGomipoiGarbageSynthesesResponse(std::string json);
extern void     onReceivedGomipoiGameSaveResponse(std::string json);
extern bool     isUnlockBook(std::string garbage_code);
extern bool     isNewBook(std::string garbage_code);
extern std::vector<int> getPageBonus();
extern void     onReceivedBookReceiveBonusesResponse(std::string json);
extern void     onStartGetData();
extern void     onFinishedGetData();
extern int      getGarbageId(std::string garbageCode);
extern std::string getGarbageCode(int garbage_id);
extern int      getGarbageBonus(int garbage_id);
extern std::string getNewFoundGarbages();
extern void     logout();
extern std::string  getCurrntDate();
extern void     onUsedZDrink();
extern void     onUsedDrop();
extern std::vector<int> getBookGarbages();
extern bool     isRareGarbage(std::string garbage_code);
extern std::string  missionIdToItemCode(int mission_id);
extern void     onUsedAutoBroom();
extern void     onUsedBattery();
extern int      getFriendCount();
extern bool     isUsedZDrink();
extern bool     isUsedDrop();
extern bool     isBonusTime();
extern double   getCurrentTime();
extern bool     isGameActive();
extern bool     isLimitItemUsing(std::string item_code);

@implementation JniBridge

/**
 * アプリ起動時に呼ぶ
 */
+ (void)foregroundApplication
{
    foregroundApplication();

    setInitData([PreferenceManager sharedManager].bonusStartTime,
                [PreferenceManager sharedManager].addGarbageStartTime,
                [PreferenceManager sharedManager].heroDrinkAppearTime,
                [PreferenceManager sharedManager].zDrinkStartTime,
                [PreferenceManager sharedManager].dropStartTime);
}

/**
 * アプリ終了時に呼ぶ
 */
+ (void)backgroundApplication
{
    backgroundApplication();
}

/**
  * [Native連携]
 * (サーバー連携によって変更する可能性大)
 * 情報をPreferenceに保存する
 */
+ (void)onSaveOtherInfoWithBbonusStartTime:(double)bonusStartTime addGarbageStartTime:(double)addGarbageStartTime heroDrinkAppearTime:(double)heroDrinkAppearTime zDrinkStartTime:(double)zDrinkStartTime dropStartTime:(double)dropStartTime
{
    [PreferenceManager sharedManager].bonusStartTime = bonusStartTime;
    [PreferenceManager sharedManager].addGarbageStartTime = addGarbageStartTime;
    [PreferenceManager sharedManager].heroDrinkAppearTime = heroDrinkAppearTime;
    [PreferenceManager sharedManager].zDrinkStartTime = zDrinkStartTime;
    [PreferenceManager sharedManager].dropStartTime = dropStartTime;
}

/**
 * [Native連携]
 * (サーバー連携によって変更する可能性大)
 * ゲーム情報を保存する
 */
+ (void)onSaveGameInfoWithPlaceType:(NSInteger)placeType addPoint:(NSInteger)addPoint completedCount:(NSInteger)completedCount garbages:(NSString *)garbages sweepCount:(NSInteger)sweepCount isBrokenBroom:(BOOL)isBrokenBroom nextStage:(NSInteger)nextStage isGarbageCanBroken:(BOOL)isGarbageCanBroken
{
    BOOL isRetry = YES;
    if ([JniBridge gameFragmentDidEnterBackground])
    {
        isRetry = NO;
    }
    
    // データ保存
    [[GBGameManager sharedManager] saveGameDataWithAppPlaceType:placeType
                                                       addPoint:addPoint
                                              putInGarbageCount:completedCount
                                                       garbages:garbages
                                                  broomUseCount:sweepCount
                                                    broomBroken:isBrokenBroom
                                               garbageCanBroken:isGarbageCanBroken
                                                      nextStage:nextStage
                                                        isRetry:isRetry
                                                        handler:^(BOOL isSuccess)
    {
        if (isSuccess)
        {
            // 新しく発見したゴミ
            NSString *newFoundGarbages = [JniBridge getNewFoundGarbages];
            if (newFoundGarbages && newFoundGarbages.length > 0)
            {
                NSMutableArray *garbages = [NSMutableArray array];
                NSArray *foundIdList = [newFoundGarbages componentsSeparatedByString:@","];
                for (NSString *garbageCode in foundIdList)
                {
                    NSDictionary *info = @{@"garbage_code":garbageCode,@"found_type":@(0)};
                    [garbages addObject:info];
                }
                
                // 新規発見のゴミがあればサーバへ送信
                [[GBGameManager sharedManager] foundGarbageWithGarbages:garbages useIndicator:NO handler:^(BOOL isSuccess)
                {
                    if (isSuccess)
                    {
                        // 図鑑情報取得
                        [GBBookGarbageData garbageOwnListWithUseIndicator:NO handler:nil];
                    }
                }];
            }
            
            if (nextStage != -1) {
                [[GBGameManager sharedManager] movePlaceWithAppPlaceType:nextStage handler:nil];
            }
        }
        
    }];
}

/**
 * [Native連携]
 * 所持宝石数の変更を伝える
 */
+ (void)onChangedGem:(NSInteger)gem
{
    [GBUserAppManager sharedManager].playerGem = gem;

    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnChangedGem
                                                        object:nil
                                                      userInfo:@{dNotificationInfoGemKey:@(gem)}];
}

/**
 * [Native連携]
 * 所持ポイント数の変更を伝える
 */
+ (void)onChangedPoint:(NSInteger)point
{
    [GBUserAppManager sharedManager].playerPoint = point;
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnChangedPoint
                                                        object:nil
                                                      userInfo:@{dNotificationInfoPointKey:@(point)}];
}

/**
 * [Native連携]
 * ゴミ箱の占有率の変更を伝える
 */
+ (void)onChangedFullness:(double)fullness
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnChangedFullness
                                                        object:nil
                                                      userInfo:@{dNotificationInfoFullnessKey:@(fullness)}];
}

/**
 * [Native連携]
 * レベルの変更を伝える
 */
+ (void)onChangedLevel:(NSInteger)level
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnChangedLevel
                                                        object:nil
                                                      userInfo:@{dNotificationInfoLevelKey:@(level)}];
}

/**
 * [Native連携]
 * 箒が壊れたことを伝える
 */
+ (void)onBrokenBroom:(NSInteger)broomType
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnBrokenBroom
                                                        object:nil
                                                      userInfo:@{dNotificationInfoBroomTypeKey:@(broomType)}];
}

+ (void) onBrokenGarbageCanXl
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnBrokenGarbageCanXl
                                                        object:nil
                                                      userInfo:nil];
}

+ (void)onGetPoint:(NSInteger)point
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnShowPoint
                                                        object:nil
                                                      userInfo:@{dNotificationInfoPointKey:@(point)}];
}

+ (void)onGetGem:(NSInteger)gem
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnShowGem
                                                        object:nil
                                                      userInfo:@{dNotificationInfoGemKey:@(gem)}];
}

+ (void)onGetComboBonus:(NSInteger)comboCount point:(NSInteger)point
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnShowComboBonus
                                                        object:nil
                                                      userInfo:@{dNotificationInfoComboCountKey:@(comboCount),
                                                                 dNotificationInfoPointKey:@(point)}];
}

+ (void)onClearMission:(NSInteger)missionId
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnClearMission
                                                        object:nil
                                                      userInfo:@{dNotificationInfoMissionIdKey:@(missionId)}];
}

+ (void)onUndergroundGemGot:(NSInteger)gem
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnUndergroundGemGot
                                                        object:nil
                                                      userInfo:@{dNotificationInfoUndergroundGemKey:@(gem)}];
}

+ (void)onSucceededSyntheses:(NSInteger)garbageId
{
    NSString *garbageCode = [JniBridge getGarbageCode:garbageId];
    [GBSynthesisSuccessVC onShowVC:[UIUtility currentVC] garbageCode:garbageCode];
}

+ (void)onEnterUnderground:(BOOL)enterJirokichi
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnEnterUnderground
                                                        object:nil
                                                      userInfo:@{dJniBridgeNotificationOnEnterUndergroundKey:@(enterJirokichi)}];
}

+ (void)onChangeStage:(NSInteger)stageId
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnChangeStage
                                                        object:nil
                                                      userInfo:@{dJniBridgeNotificationOnChangeStageKey:@(stageId)}];
}

+ (void)onRemainingBonusTime:(NSInteger)remainingSeconds
{
    [[NSNotificationCenter defaultCenter] postNotificationName:dJniBridgeNotificationOnRemainingBonusTime
                                                        object:nil
                                                      userInfo:@{dJniBridgeNotificationOnRemainingBonusTimeKey:@(remainingSeconds)}];
}

+ (void)onCheckTime:(BOOL)isForegroundFirst
{
    onCheckTime(isForegroundFirst ? 1 : 0);
}

+ (NSInteger)getGem
{
    return (NSInteger)getGem();
}

+ (NSInteger)getPoint
{
    return (NSInteger)getPoint();
}

+ (NSInteger)getLevel
{
    return (NSInteger)getLevel();
}

+ (NSInteger)getMaxLevel
{
    return (NSInteger)getMaxLevel();
}

+ (double)getFullness
{
    return getFullness();
}

+ (NSInteger)getExperiencePoint
{
    return (NSInteger)getExperiencePoint();
}

+ (NSInteger)getCurrentLevelRequiredPoint
{
    return (NSInteger)getCurrentLevelRequiredPoint();
}

+ (NSInteger)getNextLevelRequiredPoint
{
    return (NSInteger)getNextLevelRequiredPoint();
}

+ (void)addGem:(NSInteger)addValue
{
    addGem((int)addValue);
}

+ (void)addPoint:(NSInteger)addValue
{
    AddPoint((int)addValue);
}

+ (NSInteger)getCurrentBroomType
{
    return (NSInteger)getCurrentBroomType();
}

+ (void)changeCharacter:(NSInteger)type
{
    changeCharacter((int)type);
}

+ (void)changeBroomType:(NSInteger)type
{
    changeBroomType((int)type);
}

+ (NSInteger)getCurrentGarbageCanType
{
    return (NSInteger)getCurrentGarbageCanType();
}

+ (void)changeGarbageCanType:(NSInteger)type
{
    changeGarbageCanType((int)type);
}

+ (void)changeStage:(NSInteger)stage
{
    changeStage((int)stage);
}

+ (void)saveData:(NSInteger)nextStage
{
    saveData((int)nextStage);
}

+ (NSInteger)getCurrentStage
{
    return (NSInteger)getCurrentStage();
}

+ (void)useSeal
{
    useSeal();
}

+ (void)useTelephone
{
    useTelephone();
}

+ (void)onReceivedUserAppsSelfResponse:(NSString *)json
{
    onReceivedUserAppsSelfResponse([json UTF8String]);
}

+ (void)onReceivedGomipoiGarbageOwnResponse:(NSString *)json
{
    onReceivedGomipoiGarbageOwnResponse([json UTF8String]);
}

+ (void)onReceivedGomipoiGameLoadResponse:(NSString *)json
{
    onReceivedGomipoiGameLoadResponse([json UTF8String]);
}

+ (void)onReceivedGomipoiItemOwnResponse:(NSString *)json
{
    onReceivedGomipoiItemOwnResponse([json UTF8String]);
}

+ (NSInteger)getItemOwnCount:(NSString *)itemCode
{
    return (NSInteger)getItemOwnCount([itemCode UTF8String]);
}

+ (BOOL)isItemUsing:(NSString *)itemCode
{
    return (BOOL)isItemUsing([itemCode UTF8String]);
}

+ (void)onReceivedGomipoiBookOwnResponse:(NSString *)json
{
    onReceivedGomipoiBookOwnResponse([json UTF8String]);
}

+ (void)onReceivedGomipoiGarbageSynthesesResponse:(NSString *)json
{
    onReceivedGomipoiGarbageSynthesesResponse([json UTF8String]);
}

+ (void)onReceivedGomipoiGameSaveResponse:(NSString *)json
{
    onReceivedGomipoiGameSaveResponse([json UTF8String]);
}

+ (BOOL)isUnlockBook:(NSString *)garbageCode
{
    return (BOOL)isUnlockBook([garbageCode UTF8String]);
}

+ (BOOL)isNewBook:(NSString *)garbageCode
{
    return (BOOL)isNewBook([garbageCode UTF8String]);
}

+ (BOOL)isRareGarbage:(NSString *)garbageCode
{
    return (BOOL)isRareGarbage([garbageCode UTF8String]);
}

+ (NSArray *)getPageBonus
{
    std::vector<int> vectorList = getPageBonus();
    NSMutableArray *list = [NSMutableArray array];
    for (int i=0; i<vectorList.size(); i++)
    {
        [list addObject:@(vectorList[i])];
    }
    
    return list;
}

+ (void)onReceivedBookReceiveBonusesResponse:(NSString *)json
{
    onReceivedBookReceiveBonusesResponse([json UTF8String]);
}

+ (void)onStartGetData
{
    onStartGetData();
}

+ (void)onFinishedGetData
{
    onFinishedGetData();
}

+ (NSInteger)getGarbageId:(NSString *)garbageCode
{
    return (NSInteger)getGarbageId([garbageCode UTF8String]);
}

+ (NSString *)getGarbageCode:(NSInteger)garbageId
{
    std::string garbageCode = getGarbageCode((int)garbageId);
    return [NSString stringWithUTF8String:garbageCode.c_str()];
}

+ (NSInteger)getGarbagePoint:(NSString *)garbageCode
{
    int garbageId = (int)[JniBridge getGarbageId:garbageCode];
    return (NSInteger)getGarbageBonus(garbageId);
}

+ (BOOL)gameFragmentDidEnterBackground
{
    // GameaFragmentがバックグラウンドにいるかどうか
    UINavigationController *rootVC = (UINavigationController *)[[[UIApplication sharedApplication] delegate] window].rootViewController;
    if ([rootVC.viewControllers.lastObject isKindOfClass:[GameFragment class]] &&
        [[GameFragment sharedFragment] didEnterBackground])
    {
        return YES;
    }
    
    return NO;
}

+ (NSString *)getNewFoundGarbages
{
    std::string newGarbages = getNewFoundGarbages();
    return [NSString stringWithUTF8String:newGarbages.c_str()];
}

+ (void)logout
{
    logout();
}

+ (NSString *)getCurrntDate
{
    return [NSString stringWithUTF8String:getCurrntDate().c_str()];
}

+ (void)onUsedZDrink
{
    onUsedZDrink();
}

+ (void)onUsedDrop
{
    onUsedDrop();
}

+ (NSArray *)getBookGarbages
{
    std::vector<int> vectorList = getBookGarbages();
    NSMutableArray *list = [NSMutableArray array];
    for (int i=0; i<vectorList.size(); i++)
    {
        [list addObject:@(vectorList[i])];
    }
    
    return list;
}

+ (NSString *)missionIdToItemCode:(NSInteger)missionId
{
    return [NSString stringWithUTF8String:missionIdToItemCode((int)missionId).c_str()];
}

+ (void)onUsedAutoBroom
{
    onUsedAutoBroom();
}

+ (void)onUsedBattery
{
    onUsedBattery();
}

+ (int)getFriendCount
{
    return getFriendCount();
}

+ (BOOL) isUsedZDrink
{
    return isUsedZDrink();
}

+ (BOOL) isUsedDrop
{
    return isUsedDrop();
}

+ (BOOL) isBonusTime
{
    return isBonusTime();
}

+ (double) getCurrentTime
{
    return getCurrentTime();
}

+ (BOOL)isGameActive
{
    return isGameActive();
}

+ (BOOL) isLimitItemUsing:(NSString *)itemCode
{
    return isLimitItemUsing([itemCode UTF8String]);
}

@end

/**
 * パーツがクリックされた時に呼ばれる
 * @param partsId [int]
 */
void onPartsClicked(int partsId)
{
	if (![GameView isActive]) {
		return;
	}
	
	if ([GameView isLockedEvent]) {
		return;
	}
	[GameView lockEvent];
	
	switch (partsId)
    {
		default:
            break;
    }
}

/**
 * パーツのイベント時に呼ばれる
 * @param eventId [int]
 */
void onPartsEvent(int eventId)
{
	if (![GameView isActive]) {
		return;
	}
	
	switch (eventId)
    {
        case ePartsEventId_playBroomSe:
        {
            [[SoundManager sharedManager] playSe:SoundEffectType_Broom];
        }
            break;
            
        case ePartsEventId_levelUp:
        {
            [[SoundManager sharedManager] playSe:SoundEffectType_LevelUp];
        }
            break;
            
        case ePartsEventId_unknown:
        default:
            break;
    }
}

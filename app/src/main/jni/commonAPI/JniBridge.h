//
//  JniBridge.h
//  JanPoi
//
//  Created by 藤原 克明 on 2016/02/23.
//  Copyright © 2016年 alc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JniBridge : NSObject

+ (void)foregroundApplication;
+ (void)backgroundApplication;

+ (void)onSaveOtherInfoWithBbonusStartTime:(double)bonusStartTime addGarbageStartTime:(double)addGarbageStartTime heroDrinkAppearTime:(double)heroDrinkAppearTime zDrinkStartTime:(double)zDrinkStartTime dropStartTime:(double)dropStartTime;
+ (void)onSaveGameInfoWithPlaceType:(NSInteger)placeType addPoint:(NSInteger)addPoint completedCount:(NSInteger)completedCount garbages:(NSString *)garbages sweepCount:(NSInteger)sweepCount isBrokenBroom:(BOOL)isBrokenBroom nextStage:(NSInteger)nextStage isGarbageCanBroken:(BOOL)isGarbageCanBroken;
+ (void)onChangedGem:(NSInteger)gem;
+ (void)onChangedPoint:(NSInteger)point;
+ (void)onChangedFullness:(double)fullness;
+ (void)onChangedLevel:(NSInteger)level;
+ (void)onBrokenBroom:(NSInteger)broomType;
+ (void)onBrokenGarbageCanXl;
+ (void)onGetPoint:(NSInteger)point;
+ (void)onGetGem:(NSInteger)gem;
+ (void)onGetComboBonus:(NSInteger)comboCount point:(NSInteger)point;
+ (void)onClearMission:(NSInteger)missionId;
+ (void)onUndergroundGemGot:(NSInteger)gem;
+ (void)onSucceededSyntheses:(NSInteger)garbageId;
+ (void)onEnterUnderground:(BOOL)enterJirokichi;
+ (void)onChangeStage:(NSInteger)stageId;
+ (void)onRemainingBonusTime:(NSInteger)remainingSeconds;

+ (void)onCheckTime:(BOOL)isForegroundFirst;
+ (NSInteger)getGem;
+ (NSInteger)getPoint;
+ (NSInteger)getLevel;
+ (NSInteger)getMaxLevel;
+ (double)getFullness;
+ (NSInteger)getExperiencePoint;
+ (NSInteger)getCurrentLevelRequiredPoint;
+ (NSInteger)getNextLevelRequiredPoint;
+ (void)addGem:(NSInteger)addValue;
+ (void)addPoint:(NSInteger)addValue;
+ (NSInteger)getCurrentBroomType;
+ (void)changeCharacter:(NSInteger)type;
+ (void)changeBroomType:(NSInteger)type;
+ (NSInteger)getCurrentGarbageCanType;
+ (void)changeGarbageCanType:(NSInteger)type;
+ (void)changeStage:(NSInteger)stage;
+ (void)saveData:(NSInteger)nextStage;
+ (NSInteger)getCurrentStage;
+ (void)useSeal;
+ (void)useTelephone;
+ (void)onReceivedUserAppsSelfResponse:(NSString *)json;
+ (void)onReceivedGomipoiGarbageOwnResponse:(NSString *)json;
+ (void)onReceivedGomipoiGameLoadResponse:(NSString *)json;
+ (void)onReceivedGomipoiItemOwnResponse:(NSString *)json;
+ (NSInteger)getItemOwnCount:(NSString *)itemCode;
+ (BOOL)isItemUsing:(NSString *)itemCode;
+ (void)onReceivedGomipoiBookOwnResponse:(NSString *)json;
+ (void)onReceivedGomipoiGarbageSynthesesResponse:(NSString *)json;
+ (void)onReceivedGomipoiGameSaveResponse:(NSString *)json;
+ (BOOL)isUnlockBook:(NSString *)garbageCode;
+ (BOOL)isNewBook:(NSString *)garbageCode;
+ (BOOL)isRareGarbage:(NSString *)garbageCode;
+ (NSArray *)getPageBonus;
+ (void)onReceivedBookReceiveBonusesResponse:(NSString *)json;
+ (void)onStartGetData;
+ (void)onFinishedGetData;
+ (NSInteger)getGarbageId:(NSString *)garbageCode;
+ (NSString *)getGarbageCode:(NSInteger)garbageId;
+ (NSInteger)getGarbagePoint:(NSString *)garbageCode;
+ (void)logout;
+ (NSString *)getCurrntDate;
+ (void)onUsedZDrink;
+ (void)onUsedDrop;
+ (NSArray *)getBookGarbages;
+ (NSString *)missionIdToItemCode:(NSInteger)missionId;
+ (void)onUsedAutoBroom;
+ (void)onUsedBattery;
+ (int)getFriendCount;
+ (BOOL)isUsedZDrink;
+ (BOOL)isUsedDrop;
+ (BOOL)isBonusTime;
+ (double)getCurrentTime;
+ (BOOL)isGameActive;

+ (BOOL)gameFragmentDidEnterBackground;

+ (BOOL)isLimitItemUsing:(NSString *)itemCode;

@end

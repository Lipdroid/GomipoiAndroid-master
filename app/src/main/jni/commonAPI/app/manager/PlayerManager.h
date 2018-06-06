//
//
//
#include "../parameter/app_parameter.h"
#include "../../lib/utility/TimeUtils.h"
#include "../utility/GarbageUtils.h"
#include "../model/data/UsageLimitItem.h"

#ifndef GOMIBAKO_PLAYERMANAGER_H
#define GOMIBAKO_PLAYERMANAGER_H

#define DEFAULT_MAX_CAPACITY 50                 // 標準のゴミ箱の最大容量 : 50
#define MAX_POINT 99999999                      // ポイントの最大値 : 99,999,999
#define CONSUMPTION_INTERVAL (60.0 * 1000.0)    // ゴミ箱の容量回復 : 1分
#define CONSUMPTION_INTERVAL_FOR_BONUS 1500.0    // ゴミ箱の容量回復 : 1.5秒 (1分で40)
#define GARBAGE_CAN_XL_BROKEN_CAPACITY 1500

//
// Callback
// ------------------------------
typedef std::function<void(int)>      tCallback_onChangedGem;
typedef std::function<void(int)>      tCallback_onChangedPoint;
typedef std::function<void(double)>   tCallback_onChangedFullness;

const std::vector<int> bookGarbages = {eGarbageId_Dust,eGarbageId_Paper,eGarbageId_SmallGarbageBag,eGarbageId_MiddleGarbageBag,eGarbageId_LargeGarbageBag,eGarbageId_PaperAirplane,eGarbageId_EmptyCan,eGarbageId_TangerinePeel,eGarbageId_CaterpillarFungus,eGarbageId_G,eGarbageId_Helmet,eGarbageId_Nope,eGarbageId_SecondhandBook,eGarbageId_BananaPeel,eGarbageId_RatPis,eGarbageId_AppleCore,eGarbageId_UnfortunatelyAnswerSheet,eGarbageId_EmptyBottle,eGarbageId_Scorpion,eGarbageId_Bowl,eGarbageId_ShepherdsPurse,eGarbageId_Pencil,eGarbageId_MiraculousAnswerSheet,eGarbageId_Socks,eGarbageId_FishBone,eGarbageId_GaoGao,eGarbageId_Stone,eGarbageId_Record,eGarbageId_AdultBook,eGarbageId_LastYearText,eGarbageId_JellyfinshMushroom,eGarbageId_Pin,eGarbageId_Snail,eGarbageId_Slime,eGarbageId_BrokenHeart,eGarbageId_LaughterBag,eGarbageId_Parasite,eGarbageId_HeartMushroom,eGarbageId_HotWaterBottle,eGarbageId_CardboardBox,eGarbageId_TreeOfStrangeBeans,eGarbageId_Crystal,eGarbageId_Umbrella,eGarbageId_PanaeolusPapilionaceus,eGarbageId_IronManNo3,eGarbageId_UFO,eGarbageId_SmallMedal,eGarbageId_Spider,eGarbageId_Batch,eGarbageId_MarsPassport,eGarbageId_No1Arm,eGarbageId_PoisonedApple,eGarbageId_Haunted,eGarbageId_BambooShoot,eGarbageId_Wig,eGarbageId_LittleUncle,eGarbageId_LuckyBag,eGarbageId_KingG,eGarbageId_PandoraCan,eGarbageId_Cane,eGarbageId_GodOfPoverty,eGarbageId_LuckyStone,eGarbageId_PetitEnvelope,eGarbageId_Chest,
    //------------------------------------------------------
    //第2弾
    //------------------------------------------------------
    // 出現率1%
    eGarbageId_65,                          // Lv.51 小言1（勉強しなさい）
    eGarbageId_66,                          // Lv.52 曲げたスプーン
    eGarbageId_67,                          // Lv.53 ラヂオ体操カード
    eGarbageId_68,                          // Lv.54 スイッチ
    eGarbageId_69,                          // Lv.55 パンプキン
    eGarbageId_115,                         // (合成)メカガオガオ<-eGarbageId_Chest + eGarbageId_66 + eGarbageId_69

    eGarbageId_70,                          // Lv.56 心霊写真
    eGarbageId_71,                          // Lv.57 おねしょパンツ
    eGarbageId_72,                          // Lv.58 火星人（タコ）
    eGarbageId_73,                          // Lv.59 小言2（早く寝なさい）
    eGarbageId_74,                          // Lv.60 OIWAの皿
    eGarbageId_116,                         // (合成)マンドラゴラ<-eGarbageId_GodOfPoverty + eGarbageId_70 + eGarbageId_74
    eGarbageId_75,                          // Lv.61 口裂け女マスク
    eGarbageId_76,                          // Lv.62 下向き矢印
    eGarbageId_77,                          // Lv.63 大根足
    eGarbageId_78,                          // Lv.64 あぶりだし
    eGarbageId_117,                         // (合成)小言3（おバカ！）<-eGarbageId_65 + eGarbageId_73 + eGarbageId_78
    eGarbageId_79,                          // Lv.65 ネコの小判
    eGarbageId_80,                          // Lv.66 まっくろこげ肉
    eGarbageId_81,                          // Lv.67 人面魚
    eGarbageId_118,                         // (合成)不幸の手紙<-eGarbageId_71 + eGarbageId_77 + eGarbageId_81
    eGarbageId_82,                          // Lv.68 月の石
    eGarbageId_83,                          // Lv.69 ラフレシア
    eGarbageId_84,                          // Lv.70 覆面マスク
    eGarbageId_85,                          // Lv.71 真っ白い粉
    eGarbageId_86,                          // Lv.72 おサルのこしかけ
    eGarbageId_87,                          // Lv.73 メタボスネーク（ツチノコ）
    eGarbageId_119,                         // (合成)蛇足<-eGarbageId_79 + eGarbageId_117 + eGarbageId_87
    eGarbageId_88,                          // Lv.74 開いたパンドラ缶
    eGarbageId_89,                          // Lv.75 顔パック
    // 出現率0.5%
    eGarbageId_90,                          // Lv.76 殿様カツラ
    eGarbageId_91,                          // Lv.77 ラブレター
    eGarbageId_120,                         // (合成)トロルの鼻くそ<-eGarbageId_115 + eGarbageId_83 + eGarbageId_91
    eGarbageId_92,                          // Lv.78 反陽子爆弾
    eGarbageId_93,                          // Lv.79 ヌートリア
    eGarbageId_94,                          // Lv.80 8号のリモコン
    eGarbageId_95,                          // Lv.81 預言の書
    eGarbageId_121,                         // (合成)変身手鏡<-eGarbageId_81 + eGarbageId_90 + eGarbageId_95
    eGarbageId_96,                          // Lv.82 ガラスの長靴
    eGarbageId_97,                          // Lv.83 有名なキノコ
    eGarbageId_98,                          // Lv.84 スズメの涙
    eGarbageId_99,                          // Lv.85 サイボーグG
    eGarbageId_122,                         // (合成)メタルG<-eGarbageId_88 + eGarbageId_94 + eGarbageId_99
    eGarbageId_100,                         // Lv.86 偽ヒーロドリンク
    eGarbageId_101,                         // Lv.87 モノクロ写真
    eGarbageId_102,                         // Lv.88 リッププラント
    eGarbageId_123,                         // (合成)ケセランパサラン<-eGarbageId_116 + eGarbageId_121 + eGarbageId_102
    eGarbageId_103,                         // Lv.89 看板
    eGarbageId_104,                         // Lv.90 壊れたレディオ
    eGarbageId_105,                         // Lv.91 オニの金棒
    eGarbageId_124,                         // (合成)賢者の石<-eGarbageId_120 + eGarbageId_98 + eGarbageId_105
    eGarbageId_106,                         // Lv.92 バクスイ
    eGarbageId_107,                         // Lv.93 通信簿
    eGarbageId_108,                         // Lv.94 デズラ装置
    eGarbageId_109,                         // Lv.95 魔女の薬
    eGarbageId_110,                         // Lv.96 招かない猫
    eGarbageId_125,                         // (合成)玉手箱<-eGarbageId_124 + eGarbageId_108 + eGarbageId_110
    eGarbageId_111,                         // Lv.97 きつね火
    eGarbageId_112,                         // Lv.98 ポルターガイスト
    eGarbageId_113,                         // Lv.99 カミナリオヤジ
    eGarbageId_126,
    eGarbageId_114,                         // Lv.100 かあちゃんのでべそ
};

const std::vector<int> rareGarbages = {eGarbageId_Slime,eGarbageId_Scorpion,eGarbageId_UFO,eGarbageId_GaoGao,eGarbageId_CaterpillarFungus,eGarbageId_Parasite,eGarbageId_MarsPassport,eGarbageId_AdultBook,eGarbageId_LittleUncle,eGarbageId_Haunted,eGarbageId_TreeOfStrangeBeans,eGarbageId_MiraculousAnswerSheet,eGarbageId_GodOfPoverty,eGarbageId_Chest,
    // 出現率0.5%
    eGarbageId_115,                         // (合成)メカガオガオ<-eGarbageId_Chest + eGarbageId_66 + eGarbageId_69
    eGarbageId_116,                         // (合成)マンドラゴラ<-eGarbageId_GodOfPoverty + eGarbageId_70 + eGarbageId_74
    eGarbageId_117,                         // (合成)小言3（おバカ！）<-eGarbageId_65 + eGarbageId_73 + eGarbageId_78
    eGarbageId_118,                         // (合成)不幸の手紙<-eGarbageId_71 + eGarbageId_77 + eGarbageId_81
    eGarbageId_119,                         // (合成)蛇足<-eGarbageId_79 + eGarbageId_117 + eGarbageId_87
    eGarbageId_120,                         // (合成)トロルの鼻くそ<-eGarbageId_115 + eGarbageId_83 + eGarbageId_91
    eGarbageId_121,                         // (合成)変身手鏡<-eGarbageId_81 + eGarbageId_90 + eGarbageId_95
    eGarbageId_122,                         // (合成)メタルG<-eGarbageId_88 + eGarbageId_94 + eGarbageId_99
    eGarbageId_123,                         // (合成)ケセランパサラン<-eGarbageId_116 + eGarbageId_121 + eGarbageId_102
    eGarbageId_124,                         // (合成)賢者の石<-eGarbageId_120 + eGarbageId_98 + eGarbageId_105
    eGarbageId_125,                         // (合成)玉手箱<-eGarbageId_124 + eGarbageId_108 + eGarbageId_110
    eGarbageId_126
};

const std::vector<int> spGarbages = {
    eGarbageId_127,
    eGarbageId_128,
    eGarbageId_129,
    eGarbageId_130,
    eGarbageId_131,
    eGarbageId_132,
    eGarbageId_133,
    eGarbageId_134,
    eGarbageId_135,
};

class PlayerManager {

// ------------------------------
// Define
// ------------------------------
private:

// ------------------------------
// Member
// ------------------------------
private:
    int mGem;           // 所持宝石数
    int mPoint;         // 所持ポイント数
    int mTotalPoint;    // 獲得したポイントの総数
    
    bool mCanEnterUnderground; //ジロキチの隠れ家に入れるがどうかのフラグ
    int mUndergroundJewelCount;

    int mLevel;         // レベル
    int mExperiencePoint;   // 経験値
    int mCurrentLevelRequiredPoint; // 現レベルの必要経験値
    int mNextLevelRequiredPoint;    // 次レベルの必要経験値
    double mServerTime; // サーバー時間
    double mServerStartTime;

    std::vector<Item *> mOwnItems;
    std::vector<UsageLimitItem *> mUsageLimitItems;
    std::vector<BookGarbage *> mBookGarbages;
    std::vector<int> mReceivedBonusPage;

    double mFullness;      // ゴミ箱の占有量
    int mGarbageCanType;   // ゴミ箱のタイプ
    double mTimerStartTime; // 占有量を減らすタイマーの開始時間
    int mGarbageCanXlUsedCount;
    int mGarbageCanXlUsedCount_Room;

    tCallback_onChangedGem mOnChangedGem;
    tCallback_onChangedPoint mOnChangedPoint;
    tCallback_onChangedFullness mOnChangedFullness;
    
    // キャラクター
    int mCharacterType;
    
    int mFriendCount;
    
// ------------------------------
// Constructor
// ------------------------------
public:
    PlayerManager(
            tCallback_onChangedGem onChangedGem,
            tCallback_onChangedPoint onChangedPoint,
            tCallback_onChangedFullness onChangedFullness)
    {
        mOnChangedGem = onChangedGem;
        mOnChangedPoint = onChangedPoint;
        mOnChangedFullness = onChangedFullness;

        mGem = 0;
        mPoint = 0;
        mTotalPoint = 0;
        mCanEnterUnderground = false;
        mLevel = 0;
        mExperiencePoint = 0;
        mCurrentLevelRequiredPoint = 0;
        mNextLevelRequiredPoint = 0;
        mServerTime = 0.0;
        mServerStartTime = 0.0;
        mFullness = 0;
        mGarbageCanType = 0;
        mGarbageCanXlUsedCount = 0;
        mGarbageCanXlUsedCount_Room = 0;
        mTimerStartTime = 0.0;
        mCharacterType = eCharacter_Poiko;
        mFriendCount = 0;
    }

    virtual ~PlayerManager()
    {
        mOnChangedGem = 0;
        mOnChangedPoint = 0;
        mOnChangedFullness = 0;
    }

// ------------------------------
// Getter / Setter
// ------------------------------
public:
    int getGem();
    int getPoint();
    int getTotalPoint();
    bool canEnterUnderground();
    void setUndergroundJewelCount(int jewelCount);
    int getUndergroundJewelCount();
    double getFullness();
    int getGarbageCanType();
    int getGarbageCanXlUsedCount();
    double getTimerStartTime();
    void setTimerStartTime(double startTime);

// ------------------------------
// Accesser
// ------------------------------
public:
    void setUserAppsSelfResponse(int jewel, int point, int totalPoint);
    void setGomipoiGameLoadResponse(int level, double serverTime, int experiencePoint, int currentLevelRequiredPoint, int nextLevelRequiredPoint);
    int isEnabledChangeGem(int addValue);
    int isEnabledChangePoint(int addValue);
    int isEnabledChangeFullness(int addValue);

    void changeGem(int addValue);
    void changePoint(int addValue);
    void changeFullness(double addValue);
    void changeGarbageCanType(int type, int stage);

    double getFullnessRate();

    int getLevel();
    int getExperiencePoint();
    int getCurrentLevelRequiredPoint();
    int getNextLevelRequiredPoint();
    void setLevel(int level);
    void setExperiencePoint(int experiencePoint);
    void setCurrentLevelRequiredPoint(int currentLevelRequiredPoint);
    void setNextLevelRequiredPoint(int nextLevelRequiredPoint);
    int getMaxLevel();
    double getServerTime();
    void setServerTime(double time);
    double getCurrentTime();

    void setCapacity(double fullness, int garbageCanType);
    void setGarbageCanXlUsedCount(int count);
    void setGarbageCanXlUsedCount_Room(int count);
    bool addGarbageCanXlUsedCount(int addValue, int stage);

    void setOwnItem(std::vector<Item *> items);
    void setUsageLimitItems(std::vector<UsageLimitItem *> items);
    void setBookGarbages(std::vector<BookGarbage *> garbages);
    void setReceivedBonusPages(std::vector<int> pages);
    std::vector<int> getPageBonus();
    void onReceivedPageBonus(std::vector<int> receivedPage);

    int getItemOwnCount(std::string item_code);
    bool isItemOwnUsing(std::string item_code);
    bool isValidMission(int missionType);
    void consumeMission(int missionType);
    
    void onUsedTelephoneItem();

    bool isUnlocked(std::string garbage_code);
    bool isNew(std::string garbage_code);
    bool isRareGarbage(std::string garbage_code);

    bool isIncludeBookGarbage(int garbageId);
    std::vector<int> getBookGarbages();
    
    int getMaxCapacity();
    
    // キャラクター
    void setCharacter(int type);
    int getCharacter();
    
    void setFriendCount(int count);
    int getFriendCount();
    
    bool isLimitItemUsage(std::string item_code);
    void onUsedZDrink();
    void onUsedDrop();

// ------------------------------
// Function
// ------------------------------
private:
    void setGem(int gem);
    void setPoint(int point);
    void setTotalPoint(int totalPoint);
    int makeDefaultCheckResponse(int value, int maxValue);
    std::vector<int> getRareGarbages();

};

#endif //GOMIBAKO_PLAYERMANAGER_H

//
//
//
#include "../../lib/const/parameter.h"
#include "../model/data/Item.h"
#include "../model/data/UsingItem.h"
#include "../model/data/BookGarbage.h"

#ifndef TEST_APP_PARAMETER_H
#define TEST_APP_PARAMETER_H

#define TEST_JIROKICHI 0
#define TEST_RANDOM_GARBAGE 0

//
// Define
// ------------------------------
//#define SWIPE_DIST 20.0
#define SWIPE_DIST 0.0

#define MAX_LEVEL 100                            // レベルのMax値 : 50

#define ITEM_CODE_BRROM_NORMAL "item_broom_default"
#define ITEM_CODE_BRROM_SILVER "item_broom_silver"
#define ITEM_CODE_BRROM_GOLD "item_broom_gold"
#define ITEM_CODE_GARBAGE_CAN_NORMAL "item_garbage_can_default"
#define ITEM_CODE_GARBAGE_CAN_BIG "item_garbage_can_large"
#define ITEM_CODE_GARBAGE_CAN_HUGE "item_garbage_can_huge"
#define ITEM_CODE_SCROLL_1 "item_garbage_recipe_single"
#define ITEM_CODE_SCROLL_2 "item_garbage_recipe_double"
#define ITEM_CODE_SCROLL_3 "item_garbage_recipe_triple"
#define ITEM_CODE_SEAL "item_sticker"
#define ITEM_CODE_TELEPHONE "item_clear_garbage_can"
#define ITEM_CODE_PLACE_SECRET_1 "item_place_secret_1"
#define ITEM_CODE_PLACE_SECRET_2 "item_place_secret_2"
#define ITEM_CODE_PLACE_SECRET_3 "item_place_secret_3"
#define ITEM_CODE_PLACE_SECRET_4 "item_place_secret_4"
#define ITEM_CODE_PLACE_SECRET_5 "item_place_secret_5"
#define ITEM_CODE_PLACE_SECRET_6 "item_place_secret_6"
#define ITEM_CODE_PLACE_KEY_2 "item_place_key_2"
#define ITEM_CODE_SCROLL_4 "item_garbage_recipe_4"
#define ITEM_CODE_SCROLL_5 "item_garbage_recipe_5"
#define ITEM_CODE_SCROLL_6 "item_garbage_recipe_6"
#define ITEM_CODE_GARBAGE_CAN_NORMAL_2 "item_garbage_can_default_2"
#define ITEM_CODE_GARBAGE_CAN_BIG_2 "item_garbage_can_large_2"
#define ITEM_CODE_GARBAGE_CAN_HUGE_2 "item_garbage_can_huge_2"

#define ITEM_CODE_GARDEN_KEY "item_25"
#define ITEM_CODE_GARDEN_GARBAGE_CAN "item_26"
#define ITEM_CODE_Z_DRINK "item_29"
#define ITEM_CODE_DROP "item_30"
#define ITEM_CODE_AUTO_BRROM "item_31"
#define ITEM_CODE_BATTERY "item_32"
#define ITEM_CODE_POIKO "item_33"
#define ITEM_CODE_OTON "item_34"
#define ITEM_CODE_KOTATSU "item_35"
#define ITEM_CODE_GARBAGE_CAN_XL "item_36"
#define ITEM_CODE_GARBAGE_CAN_XL_ROOM "item_37"


#define ITEM_SET_CODE_BRROM_SILVER "item_set_broom_silver"
#define ITEM_SET_CODE_BRROM_GOLD "item_set_broom_gold"
#define ITEM_SET_CODE_GARBAGE_CAN_BIG "item_set_garbage_can_large"
#define ITEM_SET_CODE_GARBAGE_CAN_HUGE "item_set_garbage_can_huge"
#define ITEM_SET_CODE_SCROLL_1 "item_set_garbage_recipe_single"
#define ITEM_SET_CODE_SCROLL_2 "item_set_garbage_recipe_double"
#define ITEM_SET_CODE_SCROLL_3 "item_set_garbage_recipe_triple"
#define ITEM_SET_CODE_SEAL "item_set_sticker"
#define ITEM_SET_CODE_TELEPHONE "item_set_clear_garbage_can"
#define ITEM_SET_CODE_PLACE_SECRET_1 "item_set_place_secret_1"
#define ITEM_SET_CODE_PLACE_SECRET_2 "item_set_place_secret_2"
#define ITEM_SET_CODE_PLACE_SECRET_3 "item_set_place_secret_3"
#define ITEM_SET_CODE_PLACE_SECRET_4 "item_set_place_secret_4"
#define ITEM_SET_CODE_PLACE_SECRET_5 "item_set_place_secret_5"
#define ITEM_SET_CODE_PLACE_SECRET_6 "item_set_place_secret_6"
#define ITEM_SET_CODE_PLACE_KEY_2 "item_set_place_key_2"
#define ITEM_SET_CODE_SCROLL_4 "item_set_garbage_recipe_4"
#define ITEM_SET_CODE_SCROLL_5 "item_set_garbage_recipe_5"
#define ITEM_SET_CODE_SCROLL_6 "item_set_garbage_recipe_6"
#define ITEM_SET_CODE_GARBAGE_CAN_BIG_2 "item_set_garbage_can_large_2"
#define ITEM_SET_CODE_GARBAGE_CAN_HUGE_2 "item_set_garbage_can_huge_2"

#define ITEM_SET_CODE_Z_DRINK "item_set_22"
#define ITEM_SET_CODE_DROP "item_set_23"
#define ITEM_SET_CODE_AUTO_BRROM "item_set_24"
#define ITEM_SET_CODE_BATTERY "item_set_25"
#define ITEM_SET_CODE_OTON "item_set_26"
#define ITEM_SET_CODE_KOTATSU "item_set_27"
#define ITEM_SET_CODE_GARBAGE_CAN_XL "item_set_28"
#define ITEM_SET_CODE_GARBAGE_CAN_XL_ROOM "item_set_29"


#define GARBAGE_CODE_1 "gomipoi_garbage_dust"
#define GARBAGE_CODE_2 "gomipoi_garbage_dust_paper"
#define GARBAGE_CODE_3 "gomipoi_garbage_bag_small"
#define GARBAGE_CODE_4 "gomipoi_garbage_bag_middle"
#define GARBAGE_CODE_5 "gomipoi_garbage_bag_large"
#define GARBAGE_CODE_6 "gomipoi_garbage_paper_airplane"
#define GARBAGE_CODE_7 "gomipoi_garbage_empty_can"
#define GARBAGE_CODE_8 "gomipoi_garbage_orange_peel"
#define GARBAGE_CODE_9 "gomipoi_garbage_g"
#define GARBAGE_CODE_10 "gomipoi_garbage_kabuto"
#define GARBAGE_CODE_11 "gomipoi_garbage_failure"
#define GARBAGE_CODE_12 "gomipoi_garbage_secondhand_book"
#define GARBAGE_CODE_13 "gomipoi_garbage_banana_peel"
#define GARBAGE_CODE_14 "gomipoi_garbage_rat_pis"
#define GARBAGE_CODE_15 "gomipoi_garbage_apple_wick"
#define GARBAGE_CODE_16 "gomipoi_garbage_0point_examination_paper"
#define GARBAGE_CODE_17 "gomipoi_garbage_empty_bottle"
#define GARBAGE_CODE_18 "gomipoi_garbage_empty_bowl"
#define GARBAGE_CODE_19 "gomipoi_garbage_grass"
#define GARBAGE_CODE_20 "gomipoi_garbage_pencil_leftover"
#define GARBAGE_CODE_21 "gomipoi_garbage_socks"
#define GARBAGE_CODE_22 "gomipoi_garbage_fishbone"
#define GARBAGE_CODE_23 "gomipoi_garbage_stone"
#define GARBAGE_CODE_24 "gomipoi_garbage_record"
#define GARBAGE_CODE_25 "gomipoi_garbage_last_year_textbook"
#define GARBAGE_CODE_26 "gomipoi_garbage_jelly_mushroom"
#define GARBAGE_CODE_27 "gomipoi_garbage_pin"
#define GARBAGE_CODE_28 "gomipoi_garbage_maimai"
#define GARBAGE_CODE_29 "gomipoi_garbage_broken_heart"
#define GARBAGE_CODE_30 "gomipoi_garbage_waraibukuro"
#define GARBAGE_CODE_31 "gomipoi_garbage_heart_mushroom"
#define GARBAGE_CODE_32 "gomipoi_garbage_hottie"
#define GARBAGE_CODE_33 "gomipoi_garbage_cardboard"
#define GARBAGE_CODE_34 "gomipoi_garbage_crystal_ball"
#define GARBAGE_CODE_35 "gomipoi_garbage_umbrella"
#define GARBAGE_CODE_36 "gomipoi_garbage_laugh_mushroom"
#define GARBAGE_CODE_37 "gomipoi_garbage_iron_man_no_3"
#define GARBAGE_CODE_38 "gomipoi_garbage_minimum_medal"
#define GARBAGE_CODE_39 "gomipoi_garbage_spider"
#define GARBAGE_CODE_40 "gomipoi_garbage_badge"
#define GARBAGE_CODE_41 "gomipoi_garbage_arm_no_1"
#define GARBAGE_CODE_42 "gomipoi_garbage_poisoned_apple"
#define GARBAGE_CODE_43 "gomipoi_garbage_bamboo_shoot"
#define GARBAGE_CODE_44 "gomipoi_garbage_wig"
#define GARBAGE_CODE_45 "gomipoi_garbage_empty_lucky_bag"
#define GARBAGE_CODE_46 "gomipoi_garbage_g_king"
#define GARBAGE_CODE_47 "gomipoi_garbage_pandora_canning"
#define GARBAGE_CODE_48 "gomipoi_garbage_witch_wand"
#define GARBAGE_CODE_49 "gomipoi_garbage_lucky_stone"
#define GARBAGE_CODE_50 "gomipoi_garbage_red_pocket"
#define GARBAGE_CODE_51 "gomipoi_garbage_slime"
#define GARBAGE_CODE_52 "gomipoi_garbage_scorpion"
#define GARBAGE_CODE_53 "gomipoi_garbage_ufo"
#define GARBAGE_CODE_54 "gomipoi_garbage_gaogao"
#define GARBAGE_CODE_55 "gomipoi_garbage_plant_worm"
#define GARBAGE_CODE_56 "gomipoi_garbage_parasite"
#define GARBAGE_CODE_57 "gomipoi_garbage_mars_of_passport"
#define GARBAGE_CODE_58 "gomipoi_garbage_otonano_ehon"
#define GARBAGE_CODE_59 "gomipoi_garbage_ossan"
#define GARBAGE_CODE_60 "gomipoi_garbage_ghost"
#define GARBAGE_CODE_61 "gomipoi_garbage_tree_of_strange_beans"
#define GARBAGE_CODE_62 "gomipoi_garbage_miracle_examination_paper"
#define GARBAGE_CODE_63 "gomipoi_garbage_god_of_poverty"
#define GARBAGE_CODE_64 "gomipoi_garbage_treasure_chest"
#define GARBAGE_CODE_65 "gomipoi_garbage_65"
#define GARBAGE_CODE_66 "gomipoi_garbage_66"
#define GARBAGE_CODE_67 "gomipoi_garbage_67"
#define GARBAGE_CODE_68 "gomipoi_garbage_68"
#define GARBAGE_CODE_69 "gomipoi_garbage_69"
#define GARBAGE_CODE_70 "gomipoi_garbage_70"
#define GARBAGE_CODE_71 "gomipoi_garbage_71"
#define GARBAGE_CODE_72 "gomipoi_garbage_72"
#define GARBAGE_CODE_73 "gomipoi_garbage_73"
#define GARBAGE_CODE_74 "gomipoi_garbage_74"
#define GARBAGE_CODE_75 "gomipoi_garbage_75"
#define GARBAGE_CODE_76 "gomipoi_garbage_76"
#define GARBAGE_CODE_77 "gomipoi_garbage_77"
#define GARBAGE_CODE_78 "gomipoi_garbage_78"
#define GARBAGE_CODE_79 "gomipoi_garbage_79"
#define GARBAGE_CODE_80 "gomipoi_garbage_80"
#define GARBAGE_CODE_81 "gomipoi_garbage_81"
#define GARBAGE_CODE_82 "gomipoi_garbage_82"
#define GARBAGE_CODE_83 "gomipoi_garbage_83"
#define GARBAGE_CODE_84 "gomipoi_garbage_84"
#define GARBAGE_CODE_85 "gomipoi_garbage_85"
#define GARBAGE_CODE_86 "gomipoi_garbage_86"
#define GARBAGE_CODE_87 "gomipoi_garbage_87"
#define GARBAGE_CODE_88 "gomipoi_garbage_88"
#define GARBAGE_CODE_89 "gomipoi_garbage_89"
#define GARBAGE_CODE_90 "gomipoi_garbage_90"
#define GARBAGE_CODE_91 "gomipoi_garbage_91"
#define GARBAGE_CODE_92 "gomipoi_garbage_92"
#define GARBAGE_CODE_93 "gomipoi_garbage_93"
#define GARBAGE_CODE_94 "gomipoi_garbage_94"
#define GARBAGE_CODE_95 "gomipoi_garbage_95"
#define GARBAGE_CODE_96 "gomipoi_garbage_96"
#define GARBAGE_CODE_97 "gomipoi_garbage_97"
#define GARBAGE_CODE_98 "gomipoi_garbage_98"
#define GARBAGE_CODE_99 "gomipoi_garbage_99"
#define GARBAGE_CODE_100 "gomipoi_garbage_100"
#define GARBAGE_CODE_101 "gomipoi_garbage_101"
#define GARBAGE_CODE_102 "gomipoi_garbage_102"
#define GARBAGE_CODE_103 "gomipoi_garbage_103"
#define GARBAGE_CODE_104 "gomipoi_garbage_104"
#define GARBAGE_CODE_105 "gomipoi_garbage_105"
#define GARBAGE_CODE_106 "gomipoi_garbage_106"
#define GARBAGE_CODE_107 "gomipoi_garbage_107"
#define GARBAGE_CODE_108 "gomipoi_garbage_108"
#define GARBAGE_CODE_109 "gomipoi_garbage_109"
#define GARBAGE_CODE_110 "gomipoi_garbage_110"
#define GARBAGE_CODE_111 "gomipoi_garbage_111"
#define GARBAGE_CODE_112 "gomipoi_garbage_112"
#define GARBAGE_CODE_113 "gomipoi_garbage_113"
#define GARBAGE_CODE_114 "gomipoi_garbage_114"
#define GARBAGE_CODE_115 "gomipoi_garbage_115"
#define GARBAGE_CODE_116 "gomipoi_garbage_116"
#define GARBAGE_CODE_117 "gomipoi_garbage_117"
#define GARBAGE_CODE_118 "gomipoi_garbage_118"
#define GARBAGE_CODE_119 "gomipoi_garbage_119"
#define GARBAGE_CODE_120 "gomipoi_garbage_120"
#define GARBAGE_CODE_121 "gomipoi_garbage_121"
#define GARBAGE_CODE_122 "gomipoi_garbage_122"
#define GARBAGE_CODE_123 "gomipoi_garbage_123"
#define GARBAGE_CODE_124 "gomipoi_garbage_124"
#define GARBAGE_CODE_125 "gomipoi_garbage_125"
#define GARBAGE_CODE_126 "gomipoi_garbage_126"
#define GARBAGE_CODE_127 "gomipoi_garbage_127"
#define GARBAGE_CODE_128 "gomipoi_garbage_128"
#define GARBAGE_CODE_129 "gomipoi_garbage_129"
#define GARBAGE_CODE_130 "gomipoi_garbage_130"
#define GARBAGE_CODE_131 "gomipoi_garbage_131"
#define GARBAGE_CODE_132 "gomipoi_garbage_132"
#define GARBAGE_CODE_133 "gomipoi_garbage_133"
#define GARBAGE_CODE_134 "gomipoi_garbage_134"
#define GARBAGE_CODE_135 "gomipoi_garbage_135"

//
// Callback
// ------------------------------
typedef std::function<void(int)>        tCallback_onCompletedGarbageAnim;

//
// ChangeCeckCode
// ------------------------------
enum
{
    eChangeCeckCode_OK = 0,
    eChangeCeckCode_Low,
    eChangeCeckCode_High,
    eChangeCeckCode_LevelUp,
};

//
// GarbageCanType
// ------------------------------
enum
{
    eGarbageCanType_Unknown = 0,
    eGarbageCanType_Normal,
    eGarbageCanType_Big,
    eGarbageCanType_Huge,
    eGarbageCanType_XL,
};

//
// BroomType
// ------------------------------
enum
{
    eBroomType_Unknown = 0,
    eBroomType_Normal,
    eBroomType_Silver,
    eBroomType_Gold,
};

//
// CharacterState
// ------------------------------
enum
{
    eCharacter_Poiko = 0,
    eCharacter_Oton = 31,
    eCharacter_Kotatsu = 62,
};

//
// PoikoState
// ------------------------------
enum
{
    ePoiko_Status_Normal = 0,
    ePoiko_Status_Silver,
    ePoiko_Status_Gold,
    ePoiko_Status_Hero,
};

//
// PoikoTexture
// ------------------------------
enum
{
    ePoikoTexture_Normal1 = 0,
    ePoikoTexture_Normal2,
    ePoikoTexture_Move1,
    ePoikoTexture_Move2,
    ePoikoTexture_Sweep1,
    ePoikoTexture_Sweep2,
    ePoikoTexture_Sweep3,
    ePoikoTexture_Glanced,
};

enum
{
    ePoikoFaceTexture_Poiko = 0,
    ePoikoFaceTexture_Oton,
    ePoikoFaceTexture_Kotatsu,
};

//
// AutoBroomTexture
// ------------------------------
enum
{
    eAutoBroomTexture_Off = 0,
    eAutoBroomTexture_Normal1 = 1,
    eAutoBroomTexture_Normal2,
    eAutoBroomTexture_Move1,
    eAutoBroomTexture_Move2,
    eAutoBroomTexture_Sweep1,
    eAutoBroomTexture_Sweep2,
    eAutoBroomTexture_Sweep3,
    eAutoBroomTexture_Glanced,
};

//
// ステージ
// ------------------------------
#define ROOM_COUNT 3
enum
{
    eStage_Default = 0,
    eStage_PoikoRoom,
    eStage_Garden,
};

//
// 部屋のひみつ
// ------------------------------
enum {
    eSecretMission_DefaultTrashCan = 0,
    eSecretMission_DefaultTV,
    eSecretMission_DefaultRocket,
    eSecretMission_PoikoHeart,
    eSecretMission_PoikoLamp,
    eSecretMission_PoikoMouseHole,
};

//
// GarbageID
// ------------------------------
#define GARBAGE_COUNT 126
enum
{
    eGarbageId_Unknown = 0,
    //------------------------------------------------------
    //第1弾
    //------------------------------------------------------
    // 出現率30%
    eGarbageId_Dust,                        // Lv.1 埃
    eGarbageId_Paper,                       // Lv.2 ちり紙
    eGarbageId_SmallGarbageBag,             // Lv.3 ゴミ袋(小)
    eGarbageId_MiddleGarbageBag,            // Lv.4 ゴミ袋(中)
    eGarbageId_LargeGarbageBag,             // Lv.5 ゴミ袋(大)
    // 出現率25%
    eGarbageId_PaperAirplane,               // Lv.6 紙飛行機
    eGarbageId_EmptyCan,                    // Lv.7 空き缶
    eGarbageId_TangerinePeel,               // Lv.8 みかんの皮
    eGarbageId_G,                           // Lv.9 G
//    eGarbageId_PaperBag,                    // Lv.10 紙袋
    eGarbageId_Helmet,                    // Lv.10 紙カブト
    // 出現率10%
    eGarbageId_Nope,                        // Lv.11 スカ
    eGarbageId_SecondhandBook,              // Lv.12 古本
    eGarbageId_BananaPeel,                  // Lv.13 バナナの皮
    eGarbageId_RatPis,                      // Lv.14 ネズミ小僧
    eGarbageId_AppleCore,                   // Lv.15 リンゴの芯
    // 出現率10%
    eGarbageId_UnfortunatelyAnswerSheet,    // Lv.16 0点の回答
    eGarbageId_EmptyBottle,                 // Lv.17 空き瓶
    eGarbageId_Bowl,                        // Lv.18 どんぶり
    eGarbageId_ShepherdsPurse,              // Lv.19 ペンペン草
    eGarbageId_Pencil,                      // Lv.20 鉛筆の最後
    // 出現率8%
    eGarbageId_Socks,                       // Lv.21 片足靴下
//    eGarbageId_Block,                       // Lv.22 ボロック
    eGarbageId_FishBone,                    // Lv.22 魚の骨
    eGarbageId_Stone,                       // Lv.23 石
    eGarbageId_Record,                      // Lv.24 レコード
    eGarbageId_LastYearText,                // Lv.25 去年の教科書
    // 出現率5%
    eGarbageId_JellyfinshMushroom,          // Lv.26 くらげきのこ
    eGarbageId_Pin,                         // Lv.27 ピン
    eGarbageId_Snail,                       // Lv.28 マイマイ
    eGarbageId_BrokenHeart,                 // Lv.29 壊れたハート
    eGarbageId_LaughterBag,                 // Lv.30 笑い袋
    // 出現率 4%
    eGarbageId_HeartMushroom,               // Lv.31 ハートきのこ
//    eGarbageId_OnePiece,                    // Lv.32 1ピース
    eGarbageId_HotWaterBottle,              // Lv.32 湯たんぽ
    eGarbageId_CardboardBox,                // Lv.33 ダン箱
    eGarbageId_Crystal,                     // Lv.34 水晶玉
//    eGarbageId_TelephoneCard,               // Lv.35 折れたテレカ
    eGarbageId_Umbrella,                    // Lv.35 傘
    // 出現率3%
    eGarbageId_PanaeolusPapilionaceus,      // Lv.36 笑い茸
    eGarbageId_IronManNo3,                  // Lv.37 鉄人3号
    eGarbageId_SmallMedal,                  // Lv.38 ちっこいメダル
    eGarbageId_Spider,                      // Lv.39 スパイダー
    eGarbageId_Batch,                       // Lv.40 防衛軍バッチ
    // 出現率2%
    eGarbageId_No1Arm,                      // Lv.41 1号の腕
    eGarbageId_PoisonedApple,               // Lv.42 毒林檎
//    eGarbageId_Horsetail,                   // Lv.43 つくしんぼ
    eGarbageId_BambooShoot,                 // Lv.43 筍
    eGarbageId_Wig,                         // Lv.44 親父のカツラ
    eGarbageId_LuckyBag,                    // Lv.45 福袋の袋
    // 出現率1%
    eGarbageId_KingG,                       // Lv.46 キングG
    eGarbageId_PandoraCan,                  // Lv.47 パンドラの缶詰
    eGarbageId_Cane,                        // Lv.48 魔法使いの杖
    eGarbageId_LuckyStone,                  // Lv.49 ラッキーストーン
    eGarbageId_PetitEnvelope,               // Lv.50 お年玉の袋
    // 出現率1%
    eGarbageId_Slime,                       // (合成)スライム<-eGarbageId_BananaPeel + eGarbageId_Snail + eGarbageId_Horsetail
    eGarbageId_Scorpion,                    // (合成)サソリ<-eGarbageId_RatPis + eGarbageId_JellyfinshMushroom + eGarbageId_Spider
    eGarbageId_UFO,                         // (合成)UFO<-eGarbageId_PaperAirplane + eGarbageId_Stone + eGarbageId_IronManNo3
    eGarbageId_GaoGao,                      // (合成)ガオガオ<-eGarbageId_Bowl + eGarbageId_BrokenHeart + eGarbageId_No1Arm
    eGarbageId_CaterpillarFungus,           // (合成)冬虫夏草<-eGarbageId_G + eGarbageId_Pin + eGarbageId_PanaeolusPapilionaceus
    eGarbageId_Parasite,                    // (合成)パラサイト<-eGarbageId_ShepherdsPurse + eGarbageId_LaughterBag + eGarbageId_PoisonedApple
    eGarbageId_MarsPassport,                // (合成)火星パスポート<-eGarbageId_UnfortunatelyAnswerSheet + eGarbageId_LastYearText + eGarbageId_Batch
    eGarbageId_AdultBook,                   // (合成)大人の絵本<-eGarbageId_SecondhandBook + eGarbageId_Record + eGarbageId_OnePiece
    // 出現率0.5%
    eGarbageId_LittleUncle,                 // (合成)小さなおっさん<-eGarbageId_Slime + eGarbageId_Scorpion + eGarbageId_Wig
    eGarbageId_Haunted,                     // (合成)おばけ<-eGarbageId_UFO + eGarbageId_GaoGao + eGarbageId_PandoraCan
    eGarbageId_TreeOfStrangeBeans,          // (合成)不思議な豆の木<-eGarbageId_CaterpillarFungus + eGarbageId_Parasite + eGarbageId_LuckyBag
    eGarbageId_MiraculousAnswerSheet,       // (合成)奇跡の回答<-eGarbageId_MarsPassport + eGarbageId_AdultBook + eGarbageId_LuckyStone
    eGarbageId_GodOfPoverty,                // (合成)貧乏神<-eGarbageId_LittleUncle + eGarbageId_Haunted + eGarbageId_Cane
    eGarbageId_Chest,                       // (合成)宝箱<-eGarbageId_TreeOfStrangeBeans + eGarbageId_MiraculousAnswerSheet + eGarbageId_PetitEnvelope
    
    //------------------------------------------------------
    //第2弾
    //------------------------------------------------------
    // 出現率1%
    eGarbageId_65,                          // Lv.51 小言1（勉強しなさい）
    eGarbageId_66,                          // Lv.52 曲げたスプーン
    eGarbageId_67,                          // Lv.53 ラヂオ体操カード
    eGarbageId_68,                          // Lv.54 スイッチ
    eGarbageId_69,                          // Lv.55 パンプキン
    eGarbageId_70,                          // Lv.56 心霊写真
    eGarbageId_71,                          // Lv.57 おねしょパンツ
    eGarbageId_72,                          // Lv.58 火星人（タコ）
    eGarbageId_73,                          // Lv.59 小言2（早く寝なさい）
    eGarbageId_74,                          // Lv.60 OIWAの皿
    eGarbageId_75,                          // Lv.61 口裂け女マスク
    eGarbageId_76,                          // Lv.62 下向き矢印
    eGarbageId_77,                          // Lv.63 大根足
    eGarbageId_78,                          // Lv.64 あぶりだし
    eGarbageId_79,                          // Lv.65 ネコの小判
    eGarbageId_80,                          // Lv.66 まっくろこげ肉
    eGarbageId_81,                          // Lv.67 人面魚
    eGarbageId_82,                          // Lv.68 月の石
    eGarbageId_83,                          // Lv.69 ラフレシア
    eGarbageId_84,                          // Lv.70 覆面マスク
    eGarbageId_85,                          // Lv.71 真っ白い粉
    eGarbageId_86,                          // Lv.72 おサルのこしかけ
    eGarbageId_87,                          // Lv.73 メタボスネーク（ツチノコ）
    eGarbageId_88,                          // Lv.74 開いたパンドラ缶
    eGarbageId_89,                          // Lv.75 顔パック
    // 出現率0.5%
    eGarbageId_90,                          // Lv.76 殿様カツラ
    eGarbageId_91,                          // Lv.77 ラブレター
    eGarbageId_92,                          // Lv.78 反陽子爆弾
    eGarbageId_93,                          // Lv.79 ヌートリア
    eGarbageId_94,                          // Lv.80 8号のリモコン
    eGarbageId_95,                          // Lv.81 預言の書
    eGarbageId_96,                          // Lv.82 ガラスの長靴
    eGarbageId_97,                          // Lv.83 有名なキノコ
    eGarbageId_98,                          // Lv.84 スズメの涙
    eGarbageId_99,                          // Lv.85 サイボーグG
    eGarbageId_100,                         // Lv.86 偽ヒーロドリンク
    eGarbageId_101,                         // Lv.87 モノクロ写真
    eGarbageId_102,                         // Lv.88 リッププラント
    eGarbageId_103,                         // Lv.89 看板
    eGarbageId_104,                         // Lv.90 壊れたレディオ
    eGarbageId_105,                         // Lv.91 オニの金棒
    eGarbageId_106,                         // Lv.92 バクスイ
    eGarbageId_107,                         // Lv.93 通信簿
    eGarbageId_108,                         // Lv.94 デズラ装置
    eGarbageId_109,                         // Lv.95 魔女の薬
    eGarbageId_110,                         // Lv.96 招かない猫
    eGarbageId_111,                         // Lv.97 きつね火
    eGarbageId_112,                         // Lv.98 ポルターガイスト
    eGarbageId_113,                         // Lv.99 カミナリオヤジ
    eGarbageId_114,                         // Lv.100 かあちゃんのでべそ
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
    eGarbageId_126,                         // (合成)黒歴史<-eGarbageId_107 + eGarbageId_125 + eGarbageId_113
    
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

//
// PartsID
// ------------------------------
enum
{
    ePartsID_unknown = 0,
    ePartsID_dummy,
    ePartsID_dummy2,
    ePartsID_roomBackground,
    ePartsID_garbageCan,
    ePartsID_seal,
    ePartsID_poiko,
    ePartsID_serif_telephone,
    ePartsID_serif_mother,
    ePartsID_hero_drink_plush_light,
    ePartsID_text_hero_drink,
    ePartsID_text_get,
    ePartsID_text_bonus_time,
    ePartsID_text_level_up_plus_desript,
    ePartsID_poiko_face,
    ePartsID_poiko_effect,
    ePartsID_hero_drink,
    ePartsID_serif_full_garbages,
    ePartsID_textFull,
    ePartsID_hole,
    ePartsID_holeMask,
    ePartsID_screen,
    ePartsID_dummyGem,
    ePartsID_undergroundTitle,
    ePartsID_secretTrashCan,
    ePartsID_secretTV,
    ePartsID_secretRocket,
    ePartsID_secretHeart1,
    ePartsID_secretHeart2,
    ePartsID_secretHeart3,
    ePartsID_secretLamp,
    ePartsID_secretMouseHole,
    ePartsID_secretTapEffect,
    ePartsID_secretTrashCanGem,
    ePartsID_secretTVScreen,
    ePartsID_secretFlyingRocket,
    ePartsID_secretShowHeart1,
    ePartsID_secretShowHeart2,
    ePartsID_secretShowHeart3,
    ePartsID_secretLightLamp,
    ePartsID_secretMouse,
    ePartsID_auto_broom,
    ePartsID_battery_gage_frame,
    ePartsID_battery_gage_left,
    ePartsID_battery_gage_middle,
    ePartsID_battery_gage_right,
    ePartsID_dummy3,
};

//
// AnimationID
// ------------------------------
enum
{
    eAnimId_unknown = 0,
    eAnimId_Poiko_Normal,
    eAnimId_Poiko_Glanced,
    eAnimId_Poiko_Move_Set,
    eAnimId_Poiko_Move_Translate,
    eAnimId_Poiko_Move_Frame,
    eAnimId_Poiko_Sweep_Set,
    eAnimId_Poiko_Sweep_Translate1,
    eAnimId_Poiko_Sweep_Translate2,
    eAnimId_Poiko_Sweep_Translate3,
    eAnimId_Poiko_Sweep_Frame,
    eAnimId_Garbage_Move_Set,
    eAnimId_Garbage_Move_Rotate,
    eAnimId_Garbage_Move_Translate,
    eAnimId_Serif_Show_Set,
    eAnimId_Serif_Show_Appear,
    eAnimId_Serif_Show_Disappear,
    eAnimId_PoikoFaceWait,
    eAnimId_Garbage_Appear,
    eAnimId_BonusTime_Disappear,
    eAnimId_TextFull_Set,
    eAnimId_TextFull_Appear,
    eAnimId_TextFull_Disappear,
    eAnimId_Hole_Appear,
    eAnimId_Hole_HalfScale,
    eAnimId_Hole_AppearSet,
    eAnimId_Hole_Scale,
    eAnimId_Hole_ScaleSet,
    eAnimId_Hole_FallIn,
    eAnimId_Hole_FallOut,
    eAnimId_Screen_Appear,
    eAnimId_Screen_Disppear,
    eAnimId_Underground_Title_Disppear,
    eAnimId_secretTap,
    eAnimId_secretGemDown,
    eAnimId_secretGemChange,
    eAnimId_secretGemOut,
    eAnimId_secretGemSet,
    eAnimId_secret_TVScale,
    eAnimId_secret_TVKeep,
    eAnimId_secret_TVFade,
    eAnimId_secret_TVFadeSet,
    eAnimId_secret_RocketFrame,
    eAnimId_secret_RocketCurve,
    eAnimId_secret_RocketSet,
    eAnimId_secret_MouseFrame,
    eAnimId_secret_MousePath,
    eAnimId_secret_MouseSet,
    eAnimId_secret_LampStill,
    eAnimId_secret_LampFadeOut,
    eAnimId_secret_HeartFadeIn,
    eAnimId_secret_HeartFrame,
    eAnimId_secret_HeartScale,
    eAnimId_secret_HeartFadeOut,
    eAnimId_secret_HeartFadeSet,
    eAnimId_secret_HeartSet,
    eAnimId_AutoBroom_Normal,
    eAnimId_AutoBroom_Glanced,
    eAnimId_AutoBroom_Move_Set,
    eAnimId_AutoBroom_Move_Translate,
    eAnimId_AutoBroom_Move_Frame,
    eAnimId_AutoBroom_Sweep_Set,
    eAnimId_AutoBroom_Sweep_Translate1,
    eAnimId_AutoBroom_Sweep_Translate2,
    eAnimId_AutoBroom_Sweep_Translate3,
    eAnimId_AutoBroom_Sweep_Frame,
    eAnimId_BatteryGage_Move,
    eAnimId_BatteryGageLeft_Move,
    eAnimId_BatteryGageMiddle_Move,
    eAnimId_BatteryGageRight_Move,
};

//
// PartsEventID
// ------------------------------
enum
{
    ePartsEventId_unknown = 0,
    ePartsEventId_playBroomSe,
    ePartsEventId_fullGarbages,
    ePartsEventId_levelUp,
};

#endif //TEST_APP_PARAMETER_H

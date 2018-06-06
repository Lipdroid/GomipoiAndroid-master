//
//
//
#include "GarbageData.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * クローンを返す
 */
GarbageData*
GarbageData::clone()
{
    bool isFirst = this->mIsFirst;
    this->mIsFirst = false;
    return new GarbageData(mGarbageId, mRestLife, mInitLeft, mInitTop, isFirst);
}

/**
 * ライフの最大値を返す
 */
int
GarbageData::getMaxLife()
{
    return GarbageData::getMaxLife(mGarbageId);
}

/**
 * ボーナスを返す
 */
int
GarbageData::getBonus()
{
    return GarbageData::getBonus(mGarbageId);
}

/**
 * 幅を返す
 */
double
GarbageData::getWidth()
{
    return GarbageData::getGarbageWidth(mGarbageId);
}

/**
 * 高さを返す
 */
double
GarbageData::getHeight()
{
    return GarbageData::getGarbageHeight(mGarbageId);
}

void
GarbageData::changeGarbageId(int garbageId)
{
    int maxLife = getMaxLife();
    int sweepCount = maxLife - mRestLife;
    
    mGarbageId = garbageId;
    maxLife = getMaxLife();
    mRestLife = maxLife - sweepCount;
}

bool
GarbageData::isSp()
{
    return mGarbageId >= eGarbageId_127 && mGarbageId <= eGarbageId_135;
}

/**
 * GarbageDataを作成する
 */
GarbageData*
GarbageData::makeGarbageData(int garbageId)
{
    double left = (double)RandomUtils::getRandomNumber(320);
//    double top = (double)RandomUtils::getRandomNumber(278) + 190.0;
    double top = (double)RandomUtils::getRandomNumber(258) + 190.0;
    double width = GarbageData::getGarbageWidth(garbageId);
    double height = GarbageData::getGarbageHeight(garbageId);
    if (left + width > STANDARD_WIDTH)
    {
        left = STANDARD_WIDTH - width - (double)RandomUtils::getRandomNumber(10);
    }
    if (top + height > STANDARD_HEIGHT - 120.0)
    {
        top = STANDARD_HEIGHT - 120.0 - height - (double)RandomUtils::getRandomNumber(10);
    }
//    if (top + height > STANDARD_HEIGHT - 100.0)
//    {
//        top = STANDARD_HEIGHT - 100.0 - height - (double)RandomUtils::getRandomNumber(10);
//    }

    return new GarbageData(garbageId, GarbageData::getMaxLife(garbageId), left, top, true);
}

// ------------------------------
// Function
// ------------------------------
/**
 * ゴミIDに対応するWidthを返す
 */
double
GarbageData::getGarbageWidth(int garbageId)
{
    switch (garbageId)
    {
            // 42
        case eGarbageId_98:
            return 21;
            
            // 54
        case eGarbageId_124:
            return 27;
            
            // 58
        case eGarbageId_Pencil:
        case eGarbageId_SmallMedal:
        case eGarbageId_CaterpillarFungus:
            return 29;

        case eGarbageId_AppleCore:
            return 31;

            // 66
        case eGarbageId_Paper:
        case eGarbageId_LuckyStone:
            return 33;

            // 68
        case eGarbageId_SmallGarbageBag:
            return 34;

            // 72
        case eGarbageId_111:
        case eGarbageId_120:
            return 36;
            
            // 74
        case eGarbageId_ShepherdsPurse:
        case eGarbageId_79:
            return 37;

            // 76
        case eGarbageId_Spider:
        case eGarbageId_109:
            return 38;

            // 80
        case eGarbageId_TangerinePeel:
        case eGarbageId_PoisonedApple:
            return 40;

            // 82
        case eGarbageId_Crystal:
        case eGarbageId_68:
            return 41;

            // 84
        case eGarbageId_JellyfinshMushroom:
        case eGarbageId_HeartMushroom:
        case eGarbageId_Parasite:
        case eGarbageId_BambooShoot:
        case eGarbageId_121:
            return 42;

            // 86
        case eGarbageId_PandoraCan:
        case eGarbageId_Socks:
        case eGarbageId_96:
        case eGarbageId_88:
            return 43;

            // 88
        case eGarbageId_MiddleGarbageBag:
            return 44;

            // 90
        case eGarbageId_PaperAirplane:
            return 45;

            // 92
        case eGarbageId_G:
        case eGarbageId_BrokenHeart:
        case eGarbageId_EmptyBottle:
        case eGarbageId_LuckyBag:
            return 46;

            // 94
        case eGarbageId_MarsPassport:
        case eGarbageId_LittleUncle:
        case eGarbageId_LaughterBag:
        case eGarbageId_86:
        case eGarbageId_114:
            return 47;
            
            // 96
        case eGarbageId_EmptyCan:
        case eGarbageId_84:
            return 48;

            // 98
        case eGarbageId_Wig:
        case eGarbageId_90:
        case eGarbageId_95:
        case eGarbageId_108:
            return 49;

            // 100
        case eGarbageId_IronManNo3:
        case eGarbageId_71:
        case eGarbageId_94:
            return 50;

            // 102
        case eGarbageId_PetitEnvelope:
        case eGarbageId_74:
        case eGarbageId_67:
            return 51;

            // 104
        case eGarbageId_Snail:
        case eGarbageId_LastYearText:
        case eGarbageId_92:
            return 52;
            
            // 106
        case eGarbageId_116:
        case eGarbageId_122:
        case eGarbageId_131:
            return 53;

            // 108
        case eGarbageId_Dust:
        case eGarbageId_Helmet:
        case eGarbageId_91:
            return 54;

            // 110
        case eGarbageId_Nope:
        case eGarbageId_69:
        case eGarbageId_75:
        case eGarbageId_89:
        case eGarbageId_126:
            return 55;

            // 112
        case eGarbageId_Pin:
        case eGarbageId_PanaeolusPapilionaceus:
        case eGarbageId_Stone:
        case eGarbageId_82:
        case eGarbageId_101:
        case eGarbageId_102:
            return 56;

            // 114
        case eGarbageId_LargeGarbageBag:
        case eGarbageId_BananaPeel:
        case eGarbageId_AdultBook:
        case eGarbageId_HotWaterBottle:
            return 57;

            // 116
        case eGarbageId_Batch:
        case eGarbageId_78:
        case eGarbageId_70:
        case eGarbageId_104:
        case eGarbageId_118:
            return 58;

            // 118
        case eGarbageId_UnfortunatelyAnswerSheet:
        case eGarbageId_85:
            return 59;
            
            // 120
        case eGarbageId_72:
            return 60;

            // 122
        case eGarbageId_RatPis:
            return 61;

            // 124
        case eGarbageId_Record:
        case eGarbageId_66:
        case eGarbageId_80:
        case eGarbageId_107:
            return 62;

            // 126
        case eGarbageId_Bowl:
        case eGarbageId_65:
        case eGarbageId_112:
            return 63;

            // 128
        case eGarbageId_Chest:
            return 64;

            // 130
        case eGarbageId_SecondhandBook:
        case eGarbageId_125:
        case eGarbageId_127:
            return 65;

            // 132
        case eGarbageId_No1Arm:
        case eGarbageId_81:
        case eGarbageId_97:
        case eGarbageId_119:
        case eGarbageId_123:
            return 66;
            
            // 134
        case eGarbageId_FishBone:
            return 67;
            
            // 136
        case eGarbageId_73:
        case eGarbageId_100:
        case eGarbageId_115:
            return 68;

            // 140
        case eGarbageId_Haunted:
        case eGarbageId_GaoGao:
        case eGarbageId_MiraculousAnswerSheet:
        case eGarbageId_GodOfPoverty:
        case eGarbageId_77:
            return 70;

            // 142
        case eGarbageId_Scorpion:
            return 71;

            // 144
        case eGarbageId_UFO:
        case eGarbageId_130:
            return 72;
            
            // 146
        case eGarbageId_117:
            return 73;
            
            // 150
        case eGarbageId_110:
            return 75;

            // 152
        case eGarbageId_CardboardBox:
        case eGarbageId_105:
            return 76;

            // 154
        case eGarbageId_Slime:
        case eGarbageId_76:
            return 77;

            // 158
        case eGarbageId_KingG:
            return 79;

            // 160
        case eGarbageId_Cane:
        case eGarbageId_103:
            return 80;
            
            // 162
        case eGarbageId_99:
        case eGarbageId_135:
            return 81;
            
            // 164
        case eGarbageId_87:
            return 82;

            // 168
        case eGarbageId_TreeOfStrangeBeans:
            return 84;
            
            // 170
        case eGarbageId_Umbrella:
        case eGarbageId_128:
            return 85;
            
            // 180
        case eGarbageId_129:
            return 90;
            
            // 184
        case eGarbageId_113:
            return 92;
            
            // 188
        case eGarbageId_83:
            return 94;
            
            // 190
        case eGarbageId_106:
            return 95;
            
            // 192
        case eGarbageId_93:
            return 96;
            
            // 196
        case eGarbageId_132:
            return 98;
            
            // 204
        case eGarbageId_133:
            return 102;
            
            // 300
        case eGarbageId_134:
            return 150;

    }

    return 0;
}

/**
 * ゴミIDに対応するHeightを返す
 */
double
GarbageData::getGarbageHeight(int garbageId)
{
    switch (garbageId)
    {
            // 50
        case eGarbageId_G:
        case eGarbageId_66:
            return 25;

            // 52
        case eGarbageId_Pin:
        case eGarbageId_Umbrella:
            return 26;

            // 54
        case eGarbageId_EmptyCan:
            return 27;

            // 56
        case eGarbageId_TangerinePeel:
        case eGarbageId_100:
            return 28;

            // 58
        case eGarbageId_SmallMedal:
        case eGarbageId_Paper:
        case eGarbageId_114:
        case eGarbageId_122:
            return 29;

            // 60
        case eGarbageId_EmptyBottle:
        case eGarbageId_85:
            return 30;

            // 62
        case eGarbageId_Pencil:
        case eGarbageId_LuckyStone:
        case eGarbageId_Batch:
        case eGarbageId_68:
        case eGarbageId_75:
            return 31;

            // 64
        case eGarbageId_Cane:
            return 32;

            // 66
        case eGarbageId_BrokenHeart:
            return 33;

            // 68
        case eGarbageId_RatPis:
            return 34;

            // 70
        case eGarbageId_74:
        case eGarbageId_91:
            return 35;
            
            // 72
        case eGarbageId_77:
        case eGarbageId_98:
            return 36;

            // 74
        case eGarbageId_Snail:
        case eGarbageId_Stone:
        case eGarbageId_71:
        case eGarbageId_82:
        case eGarbageId_89:
        case eGarbageId_118:
            return 37;

            // 76
        case eGarbageId_Nope:
        case eGarbageId_Dust:
        case eGarbageId_Socks:
            return 38;

            // 78
        case eGarbageId_PandoraCan:
        case eGarbageId_120:
            return 39;

            // 80
        case eGarbageId_Record:
            return 40;

            // 82
        case eGarbageId_Wig:
        case eGarbageId_KingG:
        case eGarbageId_SmallGarbageBag:
        case eGarbageId_101:
            return 41;

            // 84
        case eGarbageId_Spider:
        case eGarbageId_ShepherdsPurse:
        case eGarbageId_PaperAirplane:
        case eGarbageId_FishBone:
        case eGarbageId_70:
        case eGarbageId_79:
            return 42;

            // 86
        case eGarbageId_BananaPeel:
        case eGarbageId_67:
            return 43;

            // 88
        case eGarbageId_UnfortunatelyAnswerSheet:
        case eGarbageId_JellyfinshMushroom:
        case eGarbageId_Crystal:
        case eGarbageId_PoisonedApple:
        case eGarbageId_LuckyBag:
        case eGarbageId_HotWaterBottle:
        case eGarbageId_78:
        case eGarbageId_80:
        case eGarbageId_Helmet:
        case eGarbageId_84:
        case eGarbageId_88:
        case eGarbageId_99:
            return 44;
            
            // 90
        case eGarbageId_124:
        case eGarbageId_126:
            return 45;

            // 92
        case eGarbageId_No1Arm:
        case eGarbageId_AppleCore:
            return 46;

            // 94
        case eGarbageId_MarsPassport:
        case eGarbageId_81:
            return 47;
            
            // 96
        case eGarbageId_87:
        case eGarbageId_104:
            return 48;

            // 98
        case eGarbageId_LastYearText:
        case eGarbageId_96:
        case eGarbageId_107:
            return 49;
            
            // 100
        case eGarbageId_94:
        case eGarbageId_95:
        case eGarbageId_125:
            return 50;

            // 102
        case eGarbageId_MiddleGarbageBag:
        case eGarbageId_90:
            return 51;

            // 104
        case eGarbageId_Slime:
        case eGarbageId_SecondhandBook:
        case eGarbageId_76:
            return 52;

            // 106
        case eGarbageId_Bowl:
        case eGarbageId_65:
            return 53;

            // 108
        case eGarbageId_Scorpion:
        case eGarbageId_LittleUncle:
        case eGarbageId_LaughterBag:
            return 54;

            // 110
        case eGarbageId_UFO:
        case eGarbageId_PetitEnvelope:
        case eGarbageId_105:
        case eGarbageId_69:
        case eGarbageId_123:
            return 55;

            // 112
        case eGarbageId_MiraculousAnswerSheet:
        case eGarbageId_AdultBook:
        case eGarbageId_93:
        case eGarbageId_121:
            return 56;
            
            // 114
        case eGarbageId_73:
        case eGarbageId_108:
            return 57;
            
            // 116
        case eGarbageId_72:
        case eGarbageId_92:
        case eGarbageId_109:
            return 58;

            // 118
        case eGarbageId_GaoGao:
        case eGarbageId_IronManNo3:
            return 59;

            // 120
        case eGarbageId_Haunted:
        case eGarbageId_HeartMushroom:
        case eGarbageId_103:
        case eGarbageId_117:
            return 60;

            // 122
        case eGarbageId_TreeOfStrangeBeans:
            return 61;

            // 124
        case eGarbageId_Parasite:
        case eGarbageId_CaterpillarFungus:
        case eGarbageId_Chest:
        case eGarbageId_86:
        case eGarbageId_110:
        case eGarbageId_116:
            return 62;

            // 126
        case eGarbageId_CardboardBox:
        case eGarbageId_BambooShoot:
        case eGarbageId_119:
            return 63;
            
            // 128
        case eGarbageId_111:
        case eGarbageId_133:
            return 64;
            
            // 130
        case eGarbageId_102:
            return 65;

            // 132
        case eGarbageId_GodOfPoverty:
        case eGarbageId_PanaeolusPapilionaceus:
        case eGarbageId_83:
            return 66;
            
            // 136
        case eGarbageId_115:
            return 68;

            // 138
        case eGarbageId_LargeGarbageBag:
            return 69;
            
            // 140
        case eGarbageId_106:
            return 70;
            
            // 148
        case eGarbageId_112:
            return 74;
            
            // 158
        case eGarbageId_97:
        case eGarbageId_132:
            return 79;
            
            // 164
        case eGarbageId_127:
            return 82;
            
            // 168
        case eGarbageId_113:
            return 84;
            
            // 174
        case eGarbageId_128:
            return 87;
            
            // 184
        case eGarbageId_130:
            return 92;
            
            // 188
        case eGarbageId_131:
            return 94;
            
            // 190
        case eGarbageId_129:
            return 95;
            
            // 202
        case eGarbageId_135:
            return 101;
            
            // 268
        case eGarbageId_134:
            return 134;
            
            
    }

    return 0;
}

int
GarbageData::getMaxLife(int garbageId)
{
    switch (garbageId)
    {
        case eGarbageId_Dust:                        // Lv.1 埃
        case eGarbageId_Paper:                       // Lv.2 ちり紙
        case eGarbageId_SmallGarbageBag:             // Lv.3 ゴミ袋(小)
        case eGarbageId_MiddleGarbageBag:            // Lv.4 ゴミ袋(中)
        case eGarbageId_LargeGarbageBag:             // Lv.5 ゴミ袋(大)
            return 1;

        case eGarbageId_PaperAirplane:               // Lv.6 紙飛行機
        case eGarbageId_EmptyCan:                    // Lv.7 空き缶
        case eGarbageId_TangerinePeel:               // Lv.8 みかんの皮
        case eGarbageId_G:                           // Lv.9 G
//        case eGarbageId_PaperBag:                    // Lv.10 紙袋
        case eGarbageId_Helmet:                    // Lv.10 紙カブト
            return 2;

        case eGarbageId_Nope:                        // Lv.11 スカ
        case eGarbageId_SecondhandBook:              // Lv.12 古本
        case eGarbageId_BananaPeel:                  // Lv.13 バナナの皮
        case eGarbageId_RatPis:                      // Lv.14 ネズミ小僧
        case eGarbageId_AppleCore:                   // Lv.15 リンゴの芯
        case eGarbageId_UnfortunatelyAnswerSheet:    // Lv.16 0点の回答
        case eGarbageId_EmptyBottle:                 // Lv.17 空き瓶
        case eGarbageId_Bowl:                        // Lv.18 どんぶり
        case eGarbageId_ShepherdsPurse:              // Lv.19 ペンペン草
        case eGarbageId_Pencil:                      // Lv.20 鉛筆の最後
        case eGarbageId_Socks:                       // Lv.21 片足靴下
//        case eGarbageId_Block:                       // Lv.22 ボロック
        case eGarbageId_FishBone:                    // Lv.22 魚の骨
        case eGarbageId_Stone:                       // Lv.23 石
        case eGarbageId_Record:                      // Lv.24 レコード
        case eGarbageId_LastYearText:                // Lv.25 去年の教科書
        case eGarbageId_JellyfinshMushroom:          // Lv.26 くらげきのこ
        case eGarbageId_Pin:                         // Lv.27 ピン
        case eGarbageId_Snail:                       // Lv.28 マイマイ
        case eGarbageId_BrokenHeart:                 // Lv.29 壊れたハート
        case eGarbageId_LaughterBag:                 // Lv.30 笑い袋
            return 3;

        case eGarbageId_HeartMushroom:               // Lv.31 ハートきのこ
//        case eGarbageId_OnePiece:                    // Lv.32 1ピース
        case eGarbageId_HotWaterBottle:              // Lv.32 湯たんぽ
        case eGarbageId_CardboardBox:                // Lv.33 ダン箱
        case eGarbageId_Crystal:                     // Lv.34 水晶玉
//        case eGarbageId_TelephoneCard:               // Lv.35 折れたテレカ
        case eGarbageId_Umbrella:                    // Lv.35 傘
        case eGarbageId_PanaeolusPapilionaceus:      // Lv.36 笑い茸
        case eGarbageId_IronManNo3:                  // Lv.37 鉄人3号
        case eGarbageId_SmallMedal:                  // Lv.38 ちっこいメダル
        case eGarbageId_Spider:                      // Lv.39 スパイダー
        case eGarbageId_Batch:                       // Lv.40 防衛軍バッチ
        case eGarbageId_No1Arm:                      // Lv.41 1号の腕
        case eGarbageId_PoisonedApple:               // Lv.42 毒林檎
//        case eGarbageId_Horsetail:                   // Lv.43 つくしんぼ
        case eGarbageId_BambooShoot:                 // Lv.43 筍
        case eGarbageId_Wig:                         // Lv.44 親父のカツラ
        case eGarbageId_LuckyBag:                    // Lv.45 福袋の袋
        case eGarbageId_KingG:                       // Lv.46 キングG
        case eGarbageId_PandoraCan:                  // Lv.47 パンドラの缶詰
        case eGarbageId_Cane:                        // Lv.48 魔法使いの杖
        case eGarbageId_LuckyStone:                  // Lv.49 ラッキーストーン
        case eGarbageId_PetitEnvelope:               // Lv.50 お年玉の袋
            return 4;

        case eGarbageId_Slime:                       // (合成)スライム<-eGarbageId_BananaPeel + eGarbageId_Snail + eGarbageId_Horsetail
        case eGarbageId_Scorpion:                    // (合成)サソリ<-eGarbageId_RatPis + eGarbageId_JellyfinshMushroom + eGarbageId_Spider
        case eGarbageId_UFO:                         // (合成)UFO<-eGarbageId_PaperAirplane + eGarbageId_Stone + eGarbageId_IronManNo3
        case eGarbageId_GaoGao:                      // (合成)ガオガオ<-eGarbageId_Bowl + eGarbageId_BrokenHeart + eGarbageId_No1Arm
        case eGarbageId_CaterpillarFungus:           // (合成)冬虫夏草<-eGarbageId_G + eGarbageId_Pin + eGarbageId_PanaeolusPapilionaceus
        case eGarbageId_Parasite:                    // (合成)パラサイト<-eGarbageId_ShepherdsPurse + eGarbageId_LaughterBag + eGarbageId_PoisonedApple
        case eGarbageId_MarsPassport:                // (合成)火星パスポート<-eGarbageId_UnfortunatelyAnswerSheet + eGarbageId_LastYearText + eGarbageId_Batch
        case eGarbageId_AdultBook:                   // (合成)大人の絵本<-eGarbageId_SecondhandBook + eGarbageId_Record + eGarbageId_OnePiece
        case eGarbageId_LittleUncle:                 // (合成)小さなおっさん<-eGarbageId_Slime + eGarbageId_Scorpion + eGarbageId_Wig
        case eGarbageId_Haunted:                     // (合成)おばけ<-eGarbageId_UFO + eGarbageId_GaoGao + eGarbageId_PandoraCan
        case eGarbageId_TreeOfStrangeBeans:          // (合成)不思議な豆の木<-eGarbageId_CaterpillarFungus + eGarbageId_Parasite + eGarbageId_LuckyBag
        case eGarbageId_MiraculousAnswerSheet:       // (合成)奇跡の回答<-eGarbageId_MarsPassport + eGarbageId_AdultBook + eGarbageId_LuckyStone
        case eGarbageId_GodOfPoverty:                // (合成)貧乏神<-eGarbageId_LittleUncle + eGarbageId_Haunted + eGarbageId_Cane
        case eGarbageId_Chest:                       // (合成)宝箱<-eGarbageId_TreeOfStrangeBeans + eGarbageId_MiraculousAnswerSheet + eGarbageId_PetitEnvelope
            return 5;
            
        case eGarbageId_65:                         // Lv.51 小言1
        case eGarbageId_66:                         // Lv.52 曲げたスプーン
        case eGarbageId_67:                         // Lv.53 ラヂオ体操カード
        case eGarbageId_68:                         // Lv.54 スイッチ
        case eGarbageId_69:                         // Lv.55 パンプキン
        case eGarbageId_70:                         // Lv.56 心霊写真
        case eGarbageId_71:                         // Lv.57 おねしょパンツ
        case eGarbageId_72:                         // Lv.58 火星人（タコ）
        case eGarbageId_73:                         // Lv.59 小言2
        case eGarbageId_74:                         // Lv.60 OIWAの皿
        case eGarbageId_75:                         // Lv.61 口裂け女マスク
        case eGarbageId_76:                         // Lv.62 下向き矢印
        case eGarbageId_77:                         // Lv.63 大根足
        case eGarbageId_78:                         // Lv.64 あぶりだし
        case eGarbageId_79:                         // Lv.65 ネコの小判
        case eGarbageId_80:                         // Lv.66 まっくろこげ肉
        case eGarbageId_81:                         // Lv.67 人面魚
        case eGarbageId_82:                         // Lv.68 月の石
        case eGarbageId_83:                         // Lv.69 ラフレシア
        case eGarbageId_84:                         // Lv.70 覆面マスク
        case eGarbageId_85:                         // Lv.71 真っ白い粉
        case eGarbageId_86:                         // Lv.72 おサルのこしかけ
        case eGarbageId_87:                         // Lv.73 メタボスネーク（ツチノコ）
        case eGarbageId_88:                         // Lv.74 開いたパンドラ缶
        case eGarbageId_89:                         // Lv.75 顔パック
        case eGarbageId_90:                         // Lv.76 殿様カツラ
        case eGarbageId_91:                         // Lv.77 ラブレター
        case eGarbageId_92:                         // Lv.78 反陽子爆弾
        case eGarbageId_93:                         // Lv.79 ヌートリア
        case eGarbageId_94:                         // Lv.80 8号のリモコン
        case eGarbageId_95:                         // Lv.81 預言の書
        case eGarbageId_96:                         // Lv.82 ガラスの長靴
        case eGarbageId_97:                         // Lv.83 有名なキノコ
        case eGarbageId_98:                         // Lv.84 スズメの涙
        case eGarbageId_99:                         // Lv.85 サイボーグG
        case eGarbageId_100:                        // Lv.86 偽ヒーロドリンク
        case eGarbageId_101:                        // Lv.87 モノクロ写真
        case eGarbageId_102:                        // Lv.88 リッププラント
        case eGarbageId_103:                        // Lv.89 看板
        case eGarbageId_104:                        // Lv.90 壊れたレディオ
        case eGarbageId_105:                        // Lv.91 オニの金棒
        case eGarbageId_106:                        // Lv.92 バクスイ
        case eGarbageId_107:                        // Lv.93 通信簿
        case eGarbageId_108:                        // Lv.94 でズラ装置
        case eGarbageId_109:                        // Lv.95 魔女の薬
        case eGarbageId_110:                        // Lv.96 招かない猫
        case eGarbageId_111:                        // Lv.97 きつね火
        case eGarbageId_112:                        // Lv.98 ポルターガイスト
        case eGarbageId_113:                        // Lv.99 カミナリオヤジ
        case eGarbageId_114:                        // Lv.100 かあちゃんのでべそ
        case eGarbageId_115:                        // (合成)メカガオガオ<-eGarbageId_Chest + eGarbageId_66 + eGarbageId_69
        case eGarbageId_116:                        // (合成)マンドラゴラ<-eGarbageId_GodOfPoverty + eGarbageId_70 + eGarbageId_74
        case eGarbageId_117:                        // (合成)小言3（おバカ！）<-eGarbageId_65 + eGarbageId_73 + eGarbageId_78
        case eGarbageId_118:                        // (合成)不幸の手紙<-eGarbageId_71 + eGarbageId_77 + eGarbageId_81
        case eGarbageId_119:                        // (合成)蛇足<-eGarbageId_79 + eGarbageId_117 + eGarbageId_87
        case eGarbageId_120:                        // (合成)トロルの鼻くそ<-eGarbageId_115 + eGarbageId_83 + eGarbageId_91
        case eGarbageId_121:                        // (合成)変身手鏡<-eGarbageId_81 + eGarbageId_90 + eGarbageId_95
        case eGarbageId_122:                        // (合成)メタルG<-eGarbageId_88 + eGarbageId_94 + eGarbageId_99
        case eGarbageId_123:                        // (合成)ケセランパサラン<-eGarbageId_116 + eGarbageId_121 + eGarbageId_102
        case eGarbageId_124:                        // (合成)賢者の石<-eGarbageId_120 + eGarbageId_98 + eGarbageId_105
        case eGarbageId_125:                        // (合成)玉手箱<-eGarbageId_124 + eGarbageId_108 + eGarbageId_110
        case eGarbageId_126:                        // (合成)黒歴史<-eGarbageId_107 + eGarbageId_125 + eGarbageId_113
            return 6;
            
        case eGarbageId_127:
        case eGarbageId_128:
        case eGarbageId_129:
        case eGarbageId_130:
        case eGarbageId_131:
        case eGarbageId_132:
        case eGarbageId_133:
        case eGarbageId_134:
        case eGarbageId_135:
            return 1;
            
    }
    return 1;
}

int
GarbageData::getBonus(int garbageId)
{
    switch (garbageId)
    {
        case eGarbageId_Dust:                        // Lv.1 埃
        case eGarbageId_Paper:                       // Lv.2 ちり紙
        case eGarbageId_SmallGarbageBag:             // Lv.3 ゴミ袋(小)
        case eGarbageId_MiddleGarbageBag:            // Lv.4 ゴミ袋(中)
        case eGarbageId_LargeGarbageBag:             // Lv.5 ゴミ袋(大)
            return 1;

        case eGarbageId_PaperAirplane:               // Lv.6 紙飛行機
        case eGarbageId_EmptyCan:                    // Lv.7 空き缶
        case eGarbageId_TangerinePeel:               // Lv.8 みかんの皮
        case eGarbageId_G:                           // Lv.9 G
//        case eGarbageId_PaperBag:                    // Lv.10 紙袋
        case eGarbageId_Helmet:                    // Lv.10 紙カブト
            return 3;

        case eGarbageId_Nope:                        // Lv.11 スカ
        case eGarbageId_SecondhandBook:              // Lv.12 古本
        case eGarbageId_BananaPeel:                  // Lv.13 バナナの皮
        case eGarbageId_RatPis:                      // Lv.14 ネズミ小僧
        case eGarbageId_AppleCore:                   // Lv.15 リンゴの芯
        case eGarbageId_UnfortunatelyAnswerSheet:    // Lv.16 0点の回答
        case eGarbageId_EmptyBottle:                 // Lv.17 空き瓶
        case eGarbageId_Bowl:                        // Lv.18 どんぶり
        case eGarbageId_ShepherdsPurse:              // Lv.19 ペンペン草
        case eGarbageId_Pencil:                      // Lv.20 鉛筆の最後
        case eGarbageId_Socks:                       // Lv.21 片足靴下
//        case eGarbageId_Block:                       // Lv.22 ボロック
        case eGarbageId_FishBone:                    // Lv.22 魚の骨
        case eGarbageId_Stone:                       // Lv.23 石
        case eGarbageId_Record:                      // Lv.24 レコード
        case eGarbageId_LastYearText:                // Lv.25 去年の教科書
        case eGarbageId_JellyfinshMushroom:          // Lv.26 くらげきのこ
        case eGarbageId_Pin:                         // Lv.27 ピン
        case eGarbageId_Snail:                       // Lv.28 マイマイ
        case eGarbageId_BrokenHeart:                 // Lv.29 壊れたハート
        case eGarbageId_LaughterBag:                 // Lv.30 笑い袋
            return 5;

        case eGarbageId_HeartMushroom:               // Lv.31 ハートきのこ
//        case eGarbageId_OnePiece:                    // Lv.32 1ピース
        case eGarbageId_HotWaterBottle:              // Lv.32 湯たんぽ
        case eGarbageId_CardboardBox:                // Lv.33 ダン箱
        case eGarbageId_Crystal:                     // Lv.34 水晶玉
//        case eGarbageId_TelephoneCard:               // Lv.35 折れたテレカ
        case eGarbageId_Umbrella:                    // Lv.35 傘
        case eGarbageId_PanaeolusPapilionaceus:      // Lv.36 笑い茸
        case eGarbageId_IronManNo3:                  // Lv.37 鉄人3号
        case eGarbageId_SmallMedal:                  // Lv.38 ちっこいメダル
        case eGarbageId_Spider:                      // Lv.39 スパイダー
        case eGarbageId_Batch:                       // Lv.40 防衛軍バッチ
        case eGarbageId_No1Arm:                      // Lv.41 1号の腕
        case eGarbageId_PoisonedApple:               // Lv.42 毒林檎
//        case eGarbageId_Horsetail:                   // Lv.43 つくしんぼ
        case eGarbageId_BambooShoot:                 // Lv.43 筍
        case eGarbageId_Wig:                         // Lv.44 親父のカツラ
        case eGarbageId_LuckyBag:                    // Lv.45 福袋の袋
        case eGarbageId_KingG:                       // Lv.46 キングG
        case eGarbageId_PandoraCan:                  // Lv.47 パンドラの缶詰
        case eGarbageId_Cane:                        // Lv.48 魔法使いの杖
        case eGarbageId_LuckyStone:                  // Lv.49 ラッキーストーン
        case eGarbageId_PetitEnvelope:               // Lv.50 お年玉の袋
            return 8;

        case eGarbageId_Slime:                       // (合成)スライム<-eGarbageId_BananaPeel + eGarbageId_Snail + eGarbageId_Horsetail
        case eGarbageId_Scorpion:                    // (合成)サソリ<-eGarbageId_RatPis + eGarbageId_JellyfinshMushroom + eGarbageId_Spider
        case eGarbageId_UFO:                         // (合成)UFO<-eGarbageId_PaperAirplane + eGarbageId_Stone + eGarbageId_IronManNo3
        case eGarbageId_GaoGao:                      // (合成)ガオガオ<-eGarbageId_Bowl + eGarbageId_BrokenHeart + eGarbageId_No1Arm
        case eGarbageId_CaterpillarFungus:           // (合成)冬虫夏草<-eGarbageId_G + eGarbageId_Pin + eGarbageId_PanaeolusPapilionaceus
        case eGarbageId_Parasite:                    // (合成)パラサイト<-eGarbageId_ShepherdsPurse + eGarbageId_LaughterBag + eGarbageId_PoisonedApple
        case eGarbageId_MarsPassport:                // (合成)火星パスポート<-eGarbageId_UnfortunatelyAnswerSheet + eGarbageId_LastYearText + eGarbageId_Batch
        case eGarbageId_AdultBook:                   // (合成)大人の絵本<-eGarbageId_SecondhandBook + eGarbageId_Record + eGarbageId_OnePiece
        case eGarbageId_LittleUncle:                 // (合成)小さなおっさん<-eGarbageId_Slime + eGarbageId_Scorpion + eGarbageId_Wig
        case eGarbageId_Haunted:                     // (合成)おばけ<-eGarbageId_UFO + eGarbageId_GaoGao + eGarbageId_PandoraCan
        case eGarbageId_TreeOfStrangeBeans:          // (合成)不思議な豆の木<-eGarbageId_CaterpillarFungus + eGarbageId_Parasite + eGarbageId_LuckyBag
        case eGarbageId_MiraculousAnswerSheet:       // (合成)奇跡の回答<-eGarbageId_MarsPassport + eGarbageId_AdultBook + eGarbageId_LuckyStone
        case eGarbageId_GodOfPoverty:                // (合成)貧乏神<-eGarbageId_LittleUncle + eGarbageId_Haunted + eGarbageId_Cane
        case eGarbageId_Chest:                       // (合成)宝箱<-eGarbageId_TreeOfStrangeBeans + eGarbageId_MiraculousAnswerSheet + eGarbageId_PetitEnvelope
            return 10;
            
        case eGarbageId_65:                         // Lv.51 小言1
        case eGarbageId_66:                         // Lv.52 曲げたスプーン
        case eGarbageId_67:                         // Lv.53 ラヂオ体操カード
        case eGarbageId_68:                         // Lv.54 スイッチ
        case eGarbageId_69:                         // Lv.55 パンプキン
        case eGarbageId_70:                         // Lv.56 心霊写真
        case eGarbageId_71:                         // Lv.57 おねしょパンツ
        case eGarbageId_72:                         // Lv.58 火星人（タコ）
        case eGarbageId_73:                         // Lv.59 小言2
        case eGarbageId_74:                         // Lv.60 OIWAの皿
        case eGarbageId_75:                         // Lv.61 口裂け女マスク
        case eGarbageId_76:                         // Lv.62 下向き矢印
        case eGarbageId_77:                         // Lv.63 大根足
        case eGarbageId_78:                         // Lv.64 あぶりだし
        case eGarbageId_79:                         // Lv.65 ネコの小判
        case eGarbageId_80:                         // Lv.66 まっくろこげ肉
        case eGarbageId_81:                         // Lv.67 人面魚
        case eGarbageId_82:                         // Lv.68 月の石
        case eGarbageId_83:                         // Lv.69 ラフレシア
        case eGarbageId_84:                         // Lv.70 覆面マスク
            return 10;
            
        case eGarbageId_85:                         // Lv.71 真っ白い粉
        case eGarbageId_86:                         // Lv.72 おサルのこしかけ
        case eGarbageId_87:                         // Lv.73 メタボスネーク（ツチノコ）
        case eGarbageId_88:                         // Lv.74 開いたパンドラ缶
        case eGarbageId_89:                         // Lv.75 顔パック
        case eGarbageId_90:                         // Lv.76 殿様カツラ
        case eGarbageId_91:                         // Lv.77 ラブレター
        case eGarbageId_92:                         // Lv.78 反陽子爆弾
        case eGarbageId_93:                         // Lv.79 ヌートリア
        case eGarbageId_94:                         // Lv.80 8号のリモコン
            return 13;
            
        case eGarbageId_95:                         // Lv.81 預言の書
        case eGarbageId_96:                         // Lv.82 ガラスの長靴
        case eGarbageId_97:                         // Lv.83 有名なキノコ
        case eGarbageId_98:                         // Lv.84 スズメの涙
        case eGarbageId_99:                         // Lv.85 サイボーグG
        case eGarbageId_100:                        // Lv.86 偽ヒーロドリンク
        case eGarbageId_101:                        // Lv.87 モノクロ写真
        case eGarbageId_102:                        // Lv.88 リッププラント
        case eGarbageId_103:                        // Lv.89 看板
        case eGarbageId_104:                        // Lv.90 壊れたレディオ
            return 15;
            
        case eGarbageId_105:                        // Lv.91 オニの金棒
        case eGarbageId_106:                        // Lv.92 バクスイ
        case eGarbageId_107:                        // Lv.93 通信簿
        case eGarbageId_108:                        // Lv.94 でズラ装置
        case eGarbageId_109:                        // Lv.95 魔女の薬
        case eGarbageId_110:                        // Lv.96 招かない猫
        case eGarbageId_111:                        // Lv.97 きつね火
        case eGarbageId_112:                        // Lv.98 ポルターガイスト
        case eGarbageId_113:                        // Lv.99 カミナリオヤジ
        case eGarbageId_114:                        // Lv.100 かあちゃんのでべそ
            return 18;
            
        case eGarbageId_115:                        // (合成)メカガオガオ<-eGarbageId_Chest + eGarbageId_66 + eGarbageId_69
        case eGarbageId_116:                        // (合成)マンドラゴラ<-eGarbageId_GodOfPoverty + eGarbageId_70 + eGarbageId_74
        case eGarbageId_117:                        // (合成)小言3（おバカ！）<-eGarbageId_65 + eGarbageId_73 + eGarbageId_78
        case eGarbageId_118:                        // (合成)不幸の手紙<-eGarbageId_71 + eGarbageId_77 + eGarbageId_81
        case eGarbageId_119:                        // (合成)蛇足<-eGarbageId_79 + eGarbageId_117 + eGarbageId_87
        case eGarbageId_120:                        // (合成)トロルの鼻くそ<-eGarbageId_115 + eGarbageId_83 + eGarbageId_91
        case eGarbageId_121:                        // (合成)変身手鏡<-eGarbageId_81 + eGarbageId_90 + eGarbageId_95
        case eGarbageId_122:                        // (合成)メタルG<-eGarbageId_88 + eGarbageId_94 + eGarbageId_99
        case eGarbageId_123:                        // (合成)ケセランパサラン<-eGarbageId_116 + eGarbageId_121 + eGarbageId_102
        case eGarbageId_124:                        // (合成)賢者の石<-eGarbageId_120 + eGarbageId_98 + eGarbageId_105
        case eGarbageId_125:                        // (合成)玉手箱<-eGarbageId_124 + eGarbageId_108 + eGarbageId_110
        case eGarbageId_126:                        // (合成)黒歴史<-eGarbageId_107 + eGarbageId_125 + eGarbageId_113
            return 20;
            
        case eGarbageId_127:
            return 10000;
            
        case eGarbageId_128:
            return 15000;
            
        case eGarbageId_129:
            return 20000;
            
        case eGarbageId_130:
            return 25000;
            
        case eGarbageId_131:
            return 30000;
            
        case eGarbageId_132:
            return 35000;
            
        case eGarbageId_133:
            return 40000;
            
        case eGarbageId_134:
            return 45000;
            
        case eGarbageId_135:
            return 50000;
            
    }
    return 1;
}

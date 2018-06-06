//
//
//
#include "GarbagePartsBase.h"

// ------------------------------
// Override
// ------------------------------
void
GarbagePartsBase::initParts(PartsCallback *callback)
{
    PartsBase::initParts(callback);

    if (mGarbageData != 0 && mGarbageData->isFirst())
    {
        startAnimation(eAnimId_Garbage_Appear);
    }
}

// ------------------------------
// Accesser
// ------------------------------
bool
GarbagePartsBase::isCompleted()
{
    if (mGarbageData == NULL)
    {
        return true;
    }
    return mGarbageData->isCompleted();
}

bool
GarbagePartsBase::isLastAnimation(int broomType)
{
    if (mGarbageData == NULL)
    {
        return false;
    }

    int restChangeCount = 1;
    switch (broomType)
    {
        case eBroomType_Gold:
        {
            restChangeCount = 5;
            break;
        }

        case eBroomType_Silver:
        {
            restChangeCount = 2;
            break;
        }
    }

    return (mGarbageData->getRestLife() <= restChangeCount);
}

/**
 * ゴミのBottom値を返す
 */
double
GarbagePartsBase::getBottom()
{
    return getTopOffset() + getHeight();
}

/**
 * スワイプの対象になるかを判定する
 */
void
GarbagePartsBase::checkHitSwipe(double ptX, double ptY, double screenWidth, double screenHeight)
{
    if (mGarbageData == NULL)
    {
        return;
    }
    mGarbageData->checkHit(ptX, ptY, screenWidth, screenHeight);
}

/**
 * スワイプの対象かを返す
 */
bool
GarbagePartsBase::isSwipeTarget()
{
    if (mGarbageData == NULL)
    {
        return false;
    }
    return mGarbageData->isSwipeTarget();
}

/**
 * スワイプの対象から外す
 */
void
GarbagePartsBase::resetSwipe()
{
    if (mGarbageData == NULL)
    {
        return;
    }
    mGarbageData->setSwipeTarget(false);
}

void
GarbagePartsBase::startMoveAnimation(int broomType, tCallback_onCompletedGarbageAnim onCompleted)
{
    int garbageId = 0;
    int maxLife = 1;
    int restLife = 0;
    double oldLeft = 0;
    double oldTop = 0;
    if (mGarbageData != 0)
    {
        garbageId = mGarbageData->mGarbageId;
        maxLife = mGarbageData->getMaxLife();
        restLife = mGarbageData->getRestLife();
        oldLeft = mGarbageData->getInitLeft();
        oldTop = mGarbageData->getInitTop();
    }

    if (maxLife == 0)
    {
        maxLife = 1;
    }

    int addValue = -1;
    switch (broomType)
    {
        case eBroomType_Gold:
        {
            addValue = -5;
            break;
        }

        case eBroomType_Silver:
        {
            addValue = -2;
            break;
        }
    }
    restLife += addValue;

    if (restLife < 0)
    {
        restLife = 0;
    }

    double canX = 286.5 - getWidth() / 2.0;
    double canY = 138.5 - getHeight() / 2.0;
    double newLeft = oldLeft + ((canX - oldLeft) * (double)(maxLife - restLife) / (double)maxLife);
    double newTop = oldTop + ((canY - oldTop) * (double)(maxLife - restLife) / (double)maxLife);

    AnimationBase * animation = getAnimation(eAnimId_Garbage_Move_Set);
    if (animation != 0)
    {
        MultipleAnimationSet *animationSet = (MultipleAnimationSet *) animation;
        AnimationBase * translate = animationSet->getAnimation(eAnimId_Garbage_Move_Translate);
        if (translate != NULL) {
            ((TranslateAnimation *) translate)->setPosition(0, 0, newLeft - getLeftOffset(), getTopOffset() - newTop);
        }
    }

    startAnimation(
            eAnimId_Garbage_Move_Set,
            [&, onCompleted, newLeft, newTop, garbageId, restLife](int animationId)
            {
                setOffset(newLeft, newTop);

                if (mGarbageData != 0)
                {
                    mGarbageData->setRestLife(restLife);
                }

                int bonus = 0;
                if (isCompleted())
                {
                    if (mGarbageData != 0)
                    {
                        bonus = mGarbageData->getBonus();
                    }
                    hide();
                }

                if (onCompleted != 0)
                {
                    onCompleted(bonus);
                }
            });

}

int
GarbagePartsBase::getDefaultBonus()
{
    if (mGarbageData == NULL)
    {
        return 0;
    }
    return mGarbageData->getBonus();
}

int
GarbagePartsBase::getSpriteSheet()
{
    return mSpriteSheet;
}

bool
GarbagePartsBase::isSp()
{
    if (mGarbageData == NULL)
    {
        return false;
    }
    return mGarbageData->isSp();
}

/**
 * ゴミパーツを作成する
 */
GarbagePartsBase*
GarbagePartsBase::makeGarbageParts(int garbageId)
{
    return makeGarbageParts(GarbageData::makeGarbageData(garbageId));
}

/**
 * ゴミパーツを作成する
 */
GarbagePartsBase*
GarbagePartsBase::makeGarbageParts(GarbageData* data)
{
    GarbagePartsBase *garbage = new GarbagePartsBase(data);
    int spriteSheet = 0;
    garbage->addTexture(GarbagePartsBase::makeTexture(data, &spriteSheet));
    garbage->mSpriteSheet = spriteSheet;

    // 飛んでくアニメーション
    TranslateAnimation *translateAnimation = new TranslateAnimation(eAnimId_Garbage_Move_Translate, 0, 0, 0, 0, 500);
    RotateAnimation *rotateAnimation = new RotateAnimation(eAnimId_Garbage_Move_Rotate, 0, 360, 250, true);
    rotateAnimation->setLooping(true);
    MultipleAnimationSet *animationSet = new MultipleAnimationSet(eAnimId_Garbage_Move_Set);
    animationSet->addAnimation(rotateAnimation);
    animationSet->addAnimation(translateAnimation);
    garbage->addAnimation(animationSet);

    FadeAnimation *fadeAnimation = new FadeAnimation(eAnimId_Garbage_Appear, 0, 1, 300);
    fadeAnimation->setKeepAfter(true);
    garbage->addAnimation(fadeAnimation);

    return garbage;
}

// ------------------------------
// Function
// ------------------------------
/**
 * ゴミIDに対応するTextuerを作成する
 */
PartsTexture*
GarbagePartsBase::makeTexture(GarbageData* data, int* outSpriteSheet)
{
    double spritOffsetX = 0;
    double spritOffsetY = 0;
    bool isRotate = false;
    int spriteSheet = 0;

    switch (data->mGarbageId)
    {
        //------------------------------------
        //第1弾
        //------------------------------------
            // 1,1533 - false
        case eGarbageId_Dust:                        // Lv.1 埃
            spritOffsetX = 1;
            spritOffsetY = 1533;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 181,1885 - false
        case eGarbageId_Paper:                       // Lv.2 ちり紙
            spritOffsetX = 181;
            spritOffsetY = 1885;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 267,881 - false
        case eGarbageId_SmallGarbageBag:             // Lv.3 ゴミ袋(小)
            spritOffsetX = 267;
            spritOffsetY = 881;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,1611 - true
        case eGarbageId_MiddleGarbageBag:            // Lv.4 ゴミ袋(中)
            spritOffsetX = 1;
            spritOffsetY = 1611;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 143,623 - true
        case eGarbageId_LargeGarbageBag:             // Lv.5 ゴミ袋(大)
            spritOffsetX = 143;
            spritOffsetY = 623;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 95,1781 - false
        case eGarbageId_PaperAirplane:               // Lv.6 紙飛行機
            spritOffsetX = 95;
            spritOffsetY = 1781;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 285,577 - true
        case eGarbageId_EmptyCan:                    // Lv.7 空き缶
            spritOffsetX = 285;
            spritOffsetY = 577;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 283,675 - true
        case eGarbageId_TangerinePeel:               // Lv.8 みかんの皮
            spritOffsetX = 283;
            spritOffsetY = 675;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 287,483 - true
        case eGarbageId_G:                           // Lv.9 G
            spritOffsetX = 287;
            spritOffsetY = 483;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 113,1531 - false
//        case eGarbageId_PaperBag:                    // Lv.10 紙袋
        case eGarbageId_Helmet:                    // Lv.10 紙カブト
            spritOffsetX = 113;
            spritOffsetY = 1531;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 113,1453 - false
        case eGarbageId_Nope:                        // Lv.11 スカ
            spritOffsetX = 113;
            spritOffsetY = 1453;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 135,833 - false
        case eGarbageId_SecondhandBook:              // Lv.12 古本
            spritOffsetX = 135;
            spritOffsetY = 833;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,1341 - false
        case eGarbageId_BananaPeel:                  // Lv.13 バナナの皮
            spritOffsetX = 1;
            spritOffsetY = 1341;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 269,757 - true
        case eGarbageId_RatPis:                      // Lv.14 ネズミ小僧
            spritOffsetX = 269;
            spritOffsetY = 757;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 95,1717 - true
        case eGarbageId_AppleCore:                   // Lv.15 リンゴの芯
            spritOffsetX = 95;
            spritOffsetY = 1717;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 121,1133 - false
        case eGarbageId_UnfortunatelyAnswerSheet:    // Lv.16 0点の回答
            spritOffsetX = 121;
            spritOffsetY = 1133;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 189,1739 - false
        case eGarbageId_EmptyBottle:                 // Lv.17 空き瓶
            spritOffsetX = 189;
            spritOffsetY = 1739;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 131,939 - false
        case eGarbageId_Bowl:                        // Lv.18 どんぶり
            spritOffsetX = 131;
            spritOffsetY = 939;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 263,1929 - false
        case eGarbageId_ShepherdsPurse:              // Lv.19 ペンペン草
            spritOffsetX = 263;
            spritOffsetY = 1929;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 277,1869 - true
        case eGarbageId_Pencil:                      // Lv.20 鉛筆の最後
            spritOffsetX = 277;
            spritOffsetY = 1869;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 89,1949 - false
        case eGarbageId_Socks:                       // Lv.21 片足靴下
            spritOffsetX = 89;
            spritOffsetY = 1949;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,653 - false
//        case eGarbageId_Block:                       // Lv.22 ボロック
        case eGarbageId_FishBone:                    // Lv.22 魚の骨
            spritOffsetX = 1;
            spritOffsetY = 653;
            isRotate = false;
            spriteSheet = 0;
            break;

        // 117,1377 - false
        case eGarbageId_Stone:                       // Lv.23 石
            spritOffsetX = 117;
            spritOffsetY = 1377;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 259,965 - true
        case eGarbageId_Record:                      // Lv.24 レコード
            spritOffsetX = 259;
            spritOffsetY = 965;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 241,1213 - true
        case eGarbageId_LastYearText:                // Lv.25 去年の教科書
            spritOffsetX = 241;
            spritOffsetY = 1213;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 1,1791 - true
        case eGarbageId_JellyfinshMushroom:          // Lv.26 くらげきのこ
            spritOffsetX = 1;
            spritOffsetY = 1791;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 287,369 - true
        case eGarbageId_Pin:                         // Lv.27 ピン
            spritOffsetX = 287;
            spritOffsetY = 369;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 233,1319 - false
        case eGarbageId_Snail:                       // Lv.28 マイマイ
            spritOffsetX = 233;
            spritOffsetY = 1319;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 201,1671 - false
        case eGarbageId_BrokenHeart:                 // Lv.29 壊れたハート
            spritOffsetX = 201;
            spritOffsetY = 1671;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 225,1491 - true
        case eGarbageId_LaughterBag:                 // Lv.30 笑い袋
            spritOffsetX = 225;
            spritOffsetY = 1491;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 255,1091 - false
        case eGarbageId_HeartMushroom:               // Lv.31 ハートきのこ
            spritOffsetX = 255;
            spritOffsetY = 1091;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 117,1287 - false
//        case eGarbageId_OnePiece:                    // Lv.32 1ピース
        case eGarbageId_HotWaterBottle:              // Lv.32 湯たんぽ
            spritOffsetX = 117;
            spritOffsetY = 1287;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,179 - false
        case eGarbageId_CardboardBox:                // Lv.33 ダン箱
            spritOffsetX = 1;
            spritOffsetY = 179;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 187,1801 - true
        case eGarbageId_Crystal:                     // Lv.34 水晶玉
            spritOffsetX = 187;
            spritOffsetY = 1801;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 1,1 - false
//        case eGarbageId_TelephoneCard:               // Lv.35 折れたテレカ
        case eGarbageId_Umbrella:                    // Lv.35 傘
            spritOffsetX = 1;
            spritOffsetY = 1;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,739 - true
        case eGarbageId_PanaeolusPapilionaceus:      // Lv.36 笑い茸
            spritOffsetX = 1;
            spritOffsetY = 739;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 1,1125 - true
        case eGarbageId_IronManNo3:                  // Lv.37 鉄人3号
            spritOffsetX = 1;
            spritOffsetY = 1125;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 1,1957 - false
        case eGarbageId_SmallMedal:                  // Lv.38 ちっこいメダル
            spritOffsetX = 1;
            spritOffsetY = 1957;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 177,1949 - true
        case eGarbageId_Spider:                      // Lv.39 スパイダー
            spritOffsetX = 177;
            spritOffsetY = 1949;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 121,1223 - false
        case eGarbageId_Batch:                       // Lv.40 防衛軍バッチ
            spritOffsetX = 121;
            spritOffsetY = 1223;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 135,739 - false
        case eGarbageId_No1Arm:                      // Lv.41 1号の腕
            spritOffsetX = 135;
            spritOffsetY = 739;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 91,1867 - true
        case eGarbageId_PoisonedApple:               // Lv.42 毒林檎
            spritOffsetX = 91;
            spritOffsetY = 1867;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 1,979 - true
//        case eGarbageId_Horsetail:                   // Lv.43 つくしんぼ
        case eGarbageId_BambooShoot:                 // Lv.43 筍
            spritOffsetX = 1;
            spritOffsetY = 979;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 223,1587 - false
        case eGarbageId_Wig:                         // Lv.44 親父のカツラ
            spritOffsetX = 223;
            spritOffsetY = 1587;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,1701 - false
        case eGarbageId_LuckyBag:                    // Lv.45 福袋の袋
            spritOffsetX = 1;
            spritOffsetY = 1701;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 171,67 - false
        case eGarbageId_KingG:                       // Lv.46 キングG
            spritOffsetX = 171;
            spritOffsetY = 67;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,1877 - false
        case eGarbageId_PandoraCan:                  // Lv.47 パンドラの缶詰
            spritOffsetX = 1;
            spritOffsetY = 1877;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 173,1 - false
        case eGarbageId_Cane:                        // Lv.48 魔法使いの杖
            spritOffsetX = 173;
            spritOffsetY = 1;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 277,1801 - true
        case eGarbageId_LuckyStone:                  // Lv.49 ラッキーストーン
            spritOffsetX = 277;
            spritOffsetY = 1801;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 1,1429 - true
        case eGarbageId_PetitEnvelope:               // Lv.50 お年玉の袋
            spritOffsetX = 1;
            spritOffsetY = 1429;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 171,151 - false
        case eGarbageId_Slime:                       // (合成)スライム<-eGarbageId_BananaPeel + eGarbageId_Snail + eGarbageId_Horsetail
            spritOffsetX = 171;
            spritOffsetY = 151;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,307 - false
        case eGarbageId_Scorpion:                    // (合成)サソリ<-eGarbageId_RatPis + eGarbageId_JellyfinshMushroom + eGarbageId_Spider
            spritOffsetX = 1;
            spritOffsetY = 307;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 155,257 - false
        case eGarbageId_UFO:                         // (合成)UFO<-eGarbageId_PaperAirplane + eGarbageId_Stone + eGarbageId_IronManNo3
            spritOffsetX = 155;
            spritOffsetY = 257;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 143,503 - false
        case eGarbageId_GaoGao:                      // (合成)ガオガオ<-eGarbageId_Bowl + eGarbageId_BrokenHeart + eGarbageId_No1Arm
            spritOffsetX = 143;
            spritOffsetY = 503;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,1065 - true
        case eGarbageId_CaterpillarFungus:           // (合成)冬虫夏草<-eGarbageId_G + eGarbageId_Pin + eGarbageId_PanaeolusPapilionaceus
            spritOffsetX = 1;
            spritOffsetY = 1065;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 129,1047 - true
        case eGarbageId_Parasite:                    // (合成)パラサイト<-eGarbageId_ShepherdsPurse + eGarbageId_LaughterBag + eGarbageId_PoisonedApple
            spritOffsetX = 129;
            spritOffsetY = 1047;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 105,1621 - false
        case eGarbageId_MarsPassport:                // (合成)火星パスポート<-eGarbageId_UnfortunatelyAnswerSheet + eGarbageId_LastYearText + eGarbageId_Batch
            spritOffsetX = 105;
            spritOffsetY = 1621;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,1227 - false
        case eGarbageId_AdultBook:                   // (合成)大人の絵本<-eGarbageId_SecondhandBook + eGarbageId_Record + eGarbageId_OnePiece
            spritOffsetX = 1;
            spritOffsetY = 1227;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 231,1395 - true
        case eGarbageId_LittleUncle:                 // (合成)小さなおっさん<-eGarbageId_Slime + eGarbageId_Scorpion + eGarbageId_Wig
            spritOffsetX = 231;
            spritOffsetY = 1395;
            isRotate = true;
            spriteSheet = 0;
            break;

            // 1,417 - false
        case eGarbageId_Haunted:                     // (合成)おばけ<-eGarbageId_UFO + eGarbageId_GaoGao + eGarbageId_PandoraCan
            spritOffsetX = 1;
            spritOffsetY = 417;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,55 - false
        case eGarbageId_TreeOfStrangeBeans:          // (合成)不思議な豆の木<-eGarbageId_CaterpillarFungus + eGarbageId_Parasite + eGarbageId_LuckyBag
            spritOffsetX = 1;
            spritOffsetY = 55;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,539 - false
        case eGarbageId_MiraculousAnswerSheet:       // (合成)奇跡の回答<-eGarbageId_MarsPassport + eGarbageId_AdultBook + eGarbageId_LuckyStone
            spritOffsetX = 1;
            spritOffsetY = 539;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 145,369 - false
        case eGarbageId_GodOfPoverty:                // (合成)貧乏神<-eGarbageId_LittleUncle + eGarbageId_Haunted + eGarbageId_Cane
            spritOffsetX = 145;
            spritOffsetY = 369;
            isRotate = false;
            spriteSheet = 0;
            break;

            // 1,853 - false
        case eGarbageId_Chest:                       // (合成)宝箱<-eGarbageId_TreeOfStrangeBeans + eGarbageId_MiraculousAnswerSheet + eGarbageId_PetitEnvelope
            spritOffsetX = 1;
            spritOffsetY = 853;
            isRotate = false;
            spriteSheet = 0;
            break;
            
        //------------------------------------
        //第2弾
        //------------------------------------
            // 382,658 - false
        case eGarbageId_65:                         // Lv.51 小言1
            spritOffsetX = 382;
            spritOffsetY = 658;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,0 - false
        case eGarbageId_66:                         // Lv.52 曲げたスプーン
            spritOffsetX = 0;
            spritOffsetY = 0;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 190,284 - false
        case eGarbageId_67:                         // Lv.53 ラヂオ体操カード
            spritOffsetX = 190;
            spritOffsetY = 284;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 228,58 - false
        case eGarbageId_68:                         // Lv.54 スイッチ
            spritOffsetX = 228;
            spritOffsetY = 58;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,764 - false
        case eGarbageId_69:                         // Lv.55 パンプキン
            spritOffsetX = 0;
            spritOffsetY = 764;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,284 - false
        case eGarbageId_70:                         // Lv.56 心霊写真
            spritOffsetX = 0;
            spritOffsetY = 284;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 290,128 - false
        case eGarbageId_71:                         // Lv.57 おねしょパンツ
            spritOffsetX = 290;
            spritOffsetY = 128;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 104,988 - false
        case eGarbageId_72:                         // Lv.58 火星人（タコ）
            spritOffsetX = 104;
            spritOffsetY = 988;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 276,874 - false
        case eGarbageId_73:                         // Lv.59 小言2
            spritOffsetX = 276;
            spritOffsetY = 874;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 310,58 - false
        case eGarbageId_74:                         // Lv.60 OIWAの皿
            spritOffsetX = 310;
            spritOffsetY = 58;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 118,58 - false
        case eGarbageId_75:                         // Lv.61 口裂け女マスク
            spritOffsetX = 118;
            spritOffsetY = 58;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 228,658 - false
        case eGarbageId_76:                         // Lv.62 下向き矢印
            spritOffsetX = 228;
            spritOffsetY = 658;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 108,128 - false
        case eGarbageId_77:                         // Lv.63 大根足
            spritOffsetX = 108;
            spritOffsetY = 128;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,372 - false
        case eGarbageId_78:                         // Lv.64 あぶりだし
            spritOffsetX = 0;
            spritOffsetY = 372;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 116,284 - false
        case eGarbageId_79:                         // Lv.65 ネコの小判
            spritOffsetX = 116;
            spritOffsetY = 284;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 278,372 - false
        case eGarbageId_80:                         // Lv.66 まっくろこげ肉
            spritOffsetX = 278;
            spritOffsetY = 372;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 54,462 - false
        case eGarbageId_81:                         // Lv.67 人面魚
            spritOffsetX = 54;
            spritOffsetY = 462;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 116,202 - false
        case eGarbageId_82:                         // Lv.68 月の石
            spritOffsetX = 116;
            spritOffsetY = 202;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 316,1232 - false
        case eGarbageId_83:                         // Lv.69 ラフレシア
            spritOffsetX = 316;
            spritOffsetY = 1232;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 292,284 - false
        case eGarbageId_84:                         // Lv.70 覆面マスク
            spritOffsetX = 292;
            spritOffsetY = 284;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,58 - false
        case eGarbageId_85:                         // Lv.71 真っ白い粉
            spritOffsetX = 0;
            spritOffsetY = 58;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 146,1108 - false
        case eGarbageId_86:                         // Lv.72 おサルのこしかけ
            spritOffsetX = 146;
            spritOffsetY = 1108;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 302,462 - false
        case eGarbageId_87:                         // Lv.73 メタボスネーク（ツチノコ）
            spritOffsetX = 302;
            spritOffsetY = 462;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 388,284 - false
        case eGarbageId_88:                         // Lv.74 開いたパンドラ缶
            spritOffsetX = 388;
            spritOffsetY = 284;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 390,128 - false
        case eGarbageId_89:                         // Lv.75 顔パック
            spritOffsetX = 390;
            spritOffsetY = 128;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 130,658 - false
        case eGarbageId_90:                         // Lv.76 殿様カツラ
            spritOffsetX = 130;
            spritOffsetY = 658;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,128 - false
        case eGarbageId_91:                         // Lv.77 ラブレター
            spritOffsetX = 0;
            spritOffsetY = 128;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,988 - false
        case eGarbageId_92:                         // Lv.78 反陽子爆弾
            spritOffsetX = 0;
            spritOffsetY = 988;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,874 - false
        case eGarbageId_93:                         // Lv.79 ヌートリア
            spritOffsetX = 0;
            spritOffsetY = 874;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 308,558 - false
        case eGarbageId_94:                         // Lv.80 8号のリモコン
            spritOffsetX = 308;
            spritOffsetY = 558;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 210,558 - false
        case eGarbageId_95:                         // Lv.81 預言の書
            spritOffsetX = 210;
            spritOffsetY = 558;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,558 - false
        case eGarbageId_96:                         // Lv.82 ガラスの長靴
            spritOffsetX = 0;
            spritOffsetY = 558;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,1512 - false
        case eGarbageId_97:                         // Lv.83 有名なキノコ
            spritOffsetX = 0;
            spritOffsetY = 1512;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 248,128 - false
        case eGarbageId_98:                         // Lv.84 スズメの涙
            spritOffsetX = 248;
            spritOffsetY = 128;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 116,372 - false
        case eGarbageId_99:                         // Lv.85 サイボーグG
            spritOffsetX = 116;
            spritOffsetY = 372;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 124,0 - false
        case eGarbageId_100:                        // Lv.86 偽ヒーロドリンク
            spritOffsetX = 124;
            spritOffsetY = 0;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 300,202 - false
        case eGarbageId_101:                        // Lv.87 モノクロ写真
            spritOffsetX = 300;
            spritOffsetY = 202;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 204,1232 - false
        case eGarbageId_102:                        // Lv.88 リッププラント
            spritOffsetX = 204;
            spritOffsetY = 1232;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 300,988 - false
        case eGarbageId_103:                        // Lv.89 看板
            spritOffsetX = 300;
            spritOffsetY = 988;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 186,462 - false
        case eGarbageId_104:                        // Lv.90 壊れたレディオ
            spritOffsetX = 186;
            spritOffsetY = 462;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 110,764 - false
        case eGarbageId_105:                        // Lv.91 オニの金棒
            spritOffsetX = 110;
            spritOffsetY = 764;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 136,1364 - false
        case eGarbageId_106:                        // Lv.92 バクスイ
            spritOffsetX = 136;
            spritOffsetY = 1364;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 86,558 - false
        case eGarbageId_107:                        // Lv.93 通信簿
            spritOffsetX = 86;
            spritOffsetY = 558;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 412,874 - false
        case eGarbageId_108:                        // Lv.94 でズラ装置
            spritOffsetX = 412;
            spritOffsetY = 874;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 224,988 - false
        case eGarbageId_109:                        // Lv.95 魔女の薬
            spritOffsetX = 224;
            spritOffsetY = 988;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 240,1108 - false
        case eGarbageId_110:                        // Lv.96 招かない猫
            spritOffsetX = 240;
            spritOffsetY = 1108;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 132,1232 - false
        case eGarbageId_111:                        // Lv.97 きつね火
            spritOffsetX = 132;
            spritOffsetY = 1232;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 326,1364 - false
        case eGarbageId_112:                        // Lv.98 ポルターガイスト
            spritOffsetX = 326;
            spritOffsetY = 1364;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 132,1512 - false
        case eGarbageId_113:                        // Lv.99 カミナリオヤジ
            spritOffsetX = 132;
            spritOffsetY = 1512;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 260,0 - false
        case eGarbageId_114:                        // Lv.100 かあちゃんのでべそ
            spritOffsetX = 260;
            spritOffsetY = 0;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,1364 - false
        case eGarbageId_115:                        // (合成)メカガオガオ<-eGarbageId_Chest + eGarbageId_66 + eGarbageId_69
            spritOffsetX = 0;
            spritOffsetY = 1364;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 390,1108 - false
        case eGarbageId_116:                        // (合成)マンドラゴラ<-eGarbageId_GodOfPoverty + eGarbageId_70 + eGarbageId_74
            spritOffsetX = 390;
            spritOffsetY = 1108;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,1108 - false
        case eGarbageId_117:                        // (合成)小言3（おバカ！）<-eGarbageId_65 + eGarbageId_73 + eGarbageId_78
            spritOffsetX = 0;
            spritOffsetY = 1108;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,202 - false
        case eGarbageId_118:                        // (合成)不幸の手紙<-eGarbageId_71 + eGarbageId_77 + eGarbageId_81
            spritOffsetX = 0;
            spritOffsetY = 202;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,1232 - false
        case eGarbageId_119:                        // (合成)蛇足<-eGarbageId_79 + eGarbageId_117 + eGarbageId_87
            spritOffsetX = 0;
            spritOffsetY = 1232;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 228,202 - false
        case eGarbageId_120:                        // (合成)トロルの鼻くそ<-eGarbageId_115 + eGarbageId_83 + eGarbageId_91
            spritOffsetX = 228;
            spritOffsetY = 202;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 192,874 - false
        case eGarbageId_121:                        // (合成)変身手鏡<-eGarbageId_81 + eGarbageId_90 + eGarbageId_95
            spritOffsetX = 192;
            spritOffsetY = 874;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 354,0 - false
        case eGarbageId_122:                        // (合成)メタルG<-eGarbageId_88 + eGarbageId_94 + eGarbageId_99
            spritOffsetX = 354;
            spritOffsetY = 0;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 262,764 - false
        case eGarbageId_123:                        // (合成)ケセランパサラン<-eGarbageId_116 + eGarbageId_121 + eGarbageId_102
            spritOffsetX = 262;
            spritOffsetY = 764;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,462 - false
        case eGarbageId_124:                        // (合成)賢者の石<-eGarbageId_120 + eGarbageId_98 + eGarbageId_105
            spritOffsetX = 0;
            spritOffsetY = 462;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 0,658 - false
        case eGarbageId_125:                        // (合成)玉手箱<-eGarbageId_124 + eGarbageId_108 + eGarbageId_110
            spritOffsetX = 0;
            spritOffsetY = 658;
            isRotate = false;
            spriteSheet = 1;
            break;
            
            // 402,372 - false
        case eGarbageId_126:                        // (合成)黒歴史<-eGarbageId_107 + eGarbageId_125 + eGarbageId_113
            spritOffsetX = 402;
            spritOffsetY = 372;
            isRotate = false;
            spriteSheet = 1;
            break;
            
        //------------------------------------
        //第3弾
        //------------------------------------
            // 295,199 - false
        case eGarbageId_127:
            spritOffsetX = 295;
            spritOffsetY = 199;
            isRotate = false;
            spriteSheet = 2;
            break;
            
            // 481,291 - false
        case eGarbageId_128:
            spritOffsetX = 481;
            spritOffsetY = 291;
            isRotate = false;
            spriteSheet = 2;
            break;
            
            // 431,1 - true
        case eGarbageId_129:
            spritOffsetX = 431;
            spritOffsetY = 1;
            isRotate = true;
            spriteSheet = 2;
            break;
            
            // 295,365 - true
        case eGarbageId_130:
            spritOffsetX = 295;
            spritOffsetY = 365;
            isRotate = true;
            spriteSheet = 2;
            break;
            
            // 431,183 - true
        case eGarbageId_131:
            spritOffsetX = 431;
            spritOffsetY = 183;
            isRotate = true;
            spriteSheet = 2;
            break;
            
            // 271,1 - true
        case eGarbageId_132:
            spritOffsetX = 271;
            spritOffsetY = 1;
            isRotate = true;
            spriteSheet = 2;
            break;
            
            // 1,303 - true
        case eGarbageId_133:
            spritOffsetX = 1;
            spritOffsetY = 303;
            isRotate = true;
            spriteSheet = 2;
            break;
            
            // 1,1 - true
        case eGarbageId_134:
            spritOffsetX = 1;
            spritOffsetY = 1;
            isRotate = true;
            spriteSheet = 2;
            break;
            
            // 131,303 - false
        case eGarbageId_135:
            spritOffsetX = 131;
            spritOffsetY = 303;
            isRotate = false;
            spriteSheet = 2;
            break;
    }
    
    *outSpriteSheet = spriteSheet;

    if (spriteSheet == 0) {
        return new PartsTexture(
                                "garbage.png", 340, 2030,
                                data->getWidth() * 2, data->getHeight() * 2,
                                spritOffsetX, spritOffsetY, isRotate);
    }
    else if (spriteSheet == 1) {
        return new PartsTexture(
                                "garbage2.png", 512, 1680,
                                data->getWidth() * 2, data->getHeight() * 2,
                                spritOffsetX, spritOffsetY, isRotate);
    }
    else if (spriteSheet == 2) {
        return new PartsTexture(
                                "garbage3.png", 652, 510,
                                data->getWidth() * 2, data->getHeight() * 2,
                                spritOffsetX, spritOffsetY, isRotate);
    }
    else {
        return new PartsTexture(
                                "garbage.png", 340, 2030,
                                data->getWidth() * 2, data->getHeight() * 2,
                                spritOffsetX, spritOffsetY, isRotate);
    }
}

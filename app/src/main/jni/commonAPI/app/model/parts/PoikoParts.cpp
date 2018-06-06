//
//
//
#include "PoikoParts.h"

// ------------------------------
// Override
// ------------------------------
void
PoikoParts::setTexturePosition(int position)
{
    PartsBase::setTexturePosition(getCurrentTexturePosition(position));
}

// ------------------------------
// Accesser
// ------------------------------
void
PoikoParts::setBroomType(int type)
{
    int oldState = mCurrentState;

    switch (type)
    {
        case eBroomType_Normal:
            mCurrentState = ePoiko_Status_Normal;
            break;
        case eBroomType_Silver:
            mCurrentState = ePoiko_Status_Silver;
            break;
        case eBroomType_Gold:
            mCurrentState = ePoiko_Status_Gold;
            break;
    }

    if (oldState == mCurrentState)
    {
        return;
    }

    PartsBase::setTexturePosition(getCurrentTexturePosition(ePoikoTexture_Normal1));
}

void
PoikoParts::setCharacterType(int type)
{
    mCurrentCharacter = type;
}

void
PoikoParts::onBonusTime()
{
    int oldState = mCurrentState;
    mCurrentState = ePoiko_Status_Hero;
    if (oldState == mCurrentState)
    {
        return;
    }

    PartsBase::setTexturePosition(getCurrentTexturePosition(ePoikoTexture_Normal1));
}

//void
//PoikoParts::setState(int state)
//{
//    mCurrentState = state;
//    PartsBase::setTexturePosition(getCurrentTexturePosition(ePoikoTexture_Normal1));
//}
//
//bool
//PoikoParts::isNeedPlayGlancedAnimation()
//{
//    return mCurrentState != ePoiko_Status_Hero;
//}

// ------------------------------
// Function
// ------------------------------
int
PoikoParts::getCurrentTexturePosition(int texturePosition)
{
    switch (mCurrentState)
    {
        case ePoiko_Status_Normal:
        {
            switch (texturePosition)
            {
                case ePoikoTexture_Normal1:
                    return ePoiko_Normal1 + mCurrentCharacter;

                case ePoikoTexture_Normal2:
                    return ePoiko_Normal2 + mCurrentCharacter;

                case ePoikoTexture_Move1:
                    return ePoiko_Move1 + mCurrentCharacter;

                case ePoikoTexture_Move2:
                    return ePoiko_Move2 + mCurrentCharacter;

                case ePoikoTexture_Sweep1:
                    return ePoiko_Sweep1 + mCurrentCharacter;

                case ePoikoTexture_Sweep2:
                    return ePoiko_Sweep2 + mCurrentCharacter;

                case ePoikoTexture_Sweep3:
                    return ePoiko_Sweep3 + mCurrentCharacter;

                case ePoikoTexture_Glanced:
                    return ePoiko_Glanced + mCurrentCharacter;

            }
            return ePoiko_Normal1 + mCurrentCharacter;
        }

        case ePoiko_Status_Silver:
        {
            switch (texturePosition)
            {
                case ePoikoTexture_Normal1:
                    return ePoiko_Silver_Normal1 + mCurrentCharacter;

                case ePoikoTexture_Normal2:
                    return ePoiko_Silver_Normal2 + mCurrentCharacter;

                case ePoikoTexture_Move1:
                    return ePoiko_Silver_Move1 + mCurrentCharacter;

                case ePoikoTexture_Move2:
                    return ePoiko_Silver_Move2 + mCurrentCharacter;

                case ePoikoTexture_Sweep1:
                    return ePoiko_Silver_Sweep1 + mCurrentCharacter;

                case ePoikoTexture_Sweep2:
                    return ePoiko_Silver_Sweep2 + mCurrentCharacter;

                case ePoikoTexture_Sweep3:
                    return ePoiko_Silver_Sweep3 + mCurrentCharacter;

                case ePoikoTexture_Glanced:
                    return ePoiko_Silver_Glanced + mCurrentCharacter;
            }

            return ePoiko_Silver_Normal1 + mCurrentCharacter;
        }

        case ePoiko_Status_Gold:
        {
            switch (texturePosition)
            {
                case ePoikoTexture_Normal1:
                    return ePoiko_Gold_Normal1 + mCurrentCharacter;

                case ePoikoTexture_Normal2:
                    return ePoiko_Gold_Normal2 + mCurrentCharacter;

                case ePoikoTexture_Move1:
                    return ePoiko_Gold_Move1 + mCurrentCharacter;

                case ePoikoTexture_Move2:
                    return ePoiko_Gold_Move2 + mCurrentCharacter;

                case ePoikoTexture_Sweep1:
                    return ePoiko_Gold_Sweep1 + mCurrentCharacter;

                case ePoikoTexture_Sweep2:
                    return ePoiko_Gold_Sweep2 + mCurrentCharacter;

                case ePoikoTexture_Sweep3:
                    return ePoiko_Gold_Sweep3 + mCurrentCharacter;

                case ePoikoTexture_Glanced:
                    return ePoiko_Gold_Glanced + mCurrentCharacter;
            }

            return ePoiko_Gold_Normal1 + mCurrentCharacter;
        }

        case ePoiko_Status_Hero:
        {
            switch (texturePosition)
            {
                case ePoikoTexture_Normal1:
                    return ePoiko_Hero_Normal1 + mCurrentCharacter;

                case ePoikoTexture_Normal2:
                    return ePoiko_Hero_Normal2 + mCurrentCharacter;

                case ePoikoTexture_Move1:
                    return ePoiko_Hero_Move1 + mCurrentCharacter;

                case ePoikoTexture_Move2:
                    return ePoiko_Hero_Move2 + mCurrentCharacter;

                case ePoikoTexture_Sweep1:
                    return ePoiko_Hero_Sweep1 + mCurrentCharacter;

                case ePoikoTexture_Sweep2:
                    return ePoiko_Hero_Sweep2 + mCurrentCharacter;

                case ePoikoTexture_Sweep3:
                    return ePoiko_Hero_Sweep3 + mCurrentCharacter;
            }

            return ePoiko_Hero_Normal1 + mCurrentCharacter;
        }
    }

    return ePoiko_Normal1;
}

int
PoikoParts::getRandomNumber()
{
    std::random_device rnd;     // 非決定的な乱数生成器を生成
    std::mt19937 mt(rnd());     //  メルセンヌ・ツイスタの32ビット版、引数は初期シード値
    std::uniform_int_distribution<> rand(0, 5);        // [0, 5] 範囲の一様乱数
    return rand(mt);
}

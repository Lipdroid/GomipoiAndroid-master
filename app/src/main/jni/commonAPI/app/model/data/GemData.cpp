//
//  GemData.cpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/07.
//
//

#include "GemData.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * クローンを返す
 */
GemData*
GemData::clone()
{
    bool isFirst = this->mIsFirst;
    this->mIsFirst = false;
    return new GemData(mRestLife, mInitLeft, mInitTop, isFirst);
}

/**
 * ライフの最大値を返す
 */
int
GemData::getMaxLife()
{
    return 1;
}

/**
 * ボーナスを返す
 */
int
GemData::getBonus()
{
    return 1;
}

/**
 * 幅を返す
 */
double
GemData::getWidth()
{
    return 29.0;
}

/**
 * 高さを返す
 */
double
GemData::getHeight()
{
    return 29.0;
}

/**
 * GemDataを作成する
 */
GemData*
GemData::makeGemData()
{
    double left = (double)RandomUtils::getRandomNumber(320);
    double top = (double)RandomUtils::getRandomNumber(258) + 190.0;
    double width = 29.0;
    double height = 29.0;
    
    if (left + width > STANDARD_WIDTH)
    {
        left = STANDARD_WIDTH - width - (double)RandomUtils::getRandomNumber(10);
    }
    if (top + height > STANDARD_HEIGHT - 120.0)
    {
        top = STANDARD_HEIGHT - 120.0 - height - (double)RandomUtils::getRandomNumber(10);
    }
    
    return new GemData(1, left, top, true);
}
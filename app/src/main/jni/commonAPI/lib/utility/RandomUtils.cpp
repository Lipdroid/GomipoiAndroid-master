//
//
//
#include "RandomUtils.h"

int
RandomUtils::getRandomNumber(int max)
{
    std::random_device rnd;     // 非決定的な乱数生成器を生成
    std::mt19937 mt(rnd());     //  メルセンヌ・ツイスタの32ビット版、引数は初期シード値
    std::uniform_int_distribution<> rand(0, max);        // [0, 5] 範囲の一様乱数
    return rand(mt);
}

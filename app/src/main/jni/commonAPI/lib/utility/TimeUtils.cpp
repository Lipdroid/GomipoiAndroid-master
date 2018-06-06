//
//
//
#include "TimeUtils.h"

double
TimeUtils::getTime()
{
    auto now = std::chrono::system_clock::now().time_since_epoch();
    return (double)std::chrono::duration_cast<std::chrono::milliseconds>(now).count();
}
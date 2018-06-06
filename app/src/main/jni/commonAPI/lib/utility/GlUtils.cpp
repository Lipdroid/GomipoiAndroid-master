//
//
//
#include "GlUtils.h"

/**
 * OpenGLのTexture用に最も近い2のべき乗の値を返す
 */
int
GlUtils::getTextureSize(int imageSize)
{
    if (imageSize <= 1)       return 1;
    if (imageSize <= 2)       return 2;
    if (imageSize <= 4)       return 4;
    if (imageSize <= 8)       return 8;
    if (imageSize <= 16)      return 16;
    if (imageSize <= 32)      return 32;
    if (imageSize <= 64)      return 64;
    if (imageSize <= 128)     return 128;
    if (imageSize <= 256)     return 256;
    if (imageSize <= 512)     return 512;
    if (imageSize <= 1024)    return 1024;
    if (imageSize <= 2048)    return 2048;
    if (imageSize <= 4096)    return 4096;
    if (imageSize <= 8192)    return 8192;
    if (imageSize <= 16384)   return 16384;
    if (imageSize <= 32768)   return 32768;
    if (imageSize <= 65536)   return 65536;
    return 0;
}

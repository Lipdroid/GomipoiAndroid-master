#include <stdio.h>

#include "../libpng/png.h"
#include "android_log.h"

#ifndef LIBPNGDEBUG_MACOSX
#include <android/bitmap.h>
#include <android/asset_manager.h>
#else
#define AAsset	FILE
#endif

#ifndef _PNGUTILS_H_
#define _PNGUTILS_H_

// ------------------------------
// Structure
// ------------------------------
typedef struct GameBitmapInfo
{
    int width;
    int height;
    int bitdepth;
    int channel;
    int size;
} GameBitmapInfo;

typedef struct dat_png_buffer_ {
        unsigned char *data;
        unsigned long data_len;
        unsigned long data_offset;
} dat_png_buffer;


class PngUtils
{

// ------------------------------
// Accesser
// ------------------------------
public:
    static void createPNGData(AAsset *pAsset, GameBitmapInfo *pInfo, unsigned char **ppData);
    static int createPNGDataFromPath(char *filePath, GameBitmapInfo *pInfo, unsigned char **ppData);
    static void createPNGFromDatPath(char *filePath, GameBitmapInfo *pInfo, unsigned char **ppData);

// ------------------------------
// Function
// ------------------------------
private:
    static void pngReadFileCallback(png_structp pPng, png_bytep pBuf, png_size_t len);
    static void png_memread_func(png_structp png_ptr, png_bytep buf, png_size_t size);
};

#endif // _PNGUTILS_H_

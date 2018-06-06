//
// パーツが保持するテクスチャーに関するデータ
//
#include "../../const/parameter.h"
#include "../../utility/GlUtils.h"

#ifndef TEST_PARTSTEXTURE_H
#define TEST_PARTSTEXTURE_H

class PartsTexture {

// ------------------------------
// Member
// ------------------------------
public:
    // 画像ファイル名
    std::string mFileName;

    // TextureID
    GLuint mTextureId;

    // 動的なLoad用
    bool mIsLoadAtInit;

    // Texcoord生成用
    double mLeftTopX;
    double mLeftTopY;
    double mLeftBottomX;
    double mLeftBottomY;
    double mRightTopX;
    double mRightTopY;
    double mRightBottomX;
    double mRightBottomY;

// ------------------------------
// Constructor
// ------------------------------
public:

    /**
     * Constructor
     *
     * FileSizeがSourceSize直近の2のべき乗と同じで、
     * 尚且つSpriteSheetでなく、
     * 必ず初回ロード時に読み込む場合
     */
    PartsTexture(std::string fileName,
                 double sourceWidth, double sourceHeight)
    {
        double fileWidth = GlUtils::getTextureSize(sourceWidth);
        double fileHeight = GlUtils::getTextureSize(sourceHeight);
        double spriteOffsetX = 0.0;
        double spriteOffsetY = 0.0;

        mFileName = fileName;
        mTextureId = 0;
        mIsLoadAtInit = true;

        double leftTopX = spriteOffsetX / fileWidth;
        double leftTopY = spriteOffsetY / fileHeight;
        double leftBottomX = leftTopX;
        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
        double rightTopX = leftTopX + (sourceWidth / fileWidth);
        double rightTopY = leftTopY;
        double rightBottomX = rightTopX;
        double rightBottomY = leftBottomY;

        mLeftTopX = leftTopX;
        mLeftTopY = leftTopY;
        mLeftBottomX = leftBottomX;
        mLeftBottomY = leftBottomY;
        mRightTopX = rightTopX;
        mRightTopY = rightTopY;
        mRightBottomX = rightBottomX;
        mRightBottomY = rightBottomY;
    }

    /**
     * Constructor
     *
     * FileSizeがSourceSize直近の2のべき乗と同じで、
     * 尚且つSpriteSheetでなく、
     * 初回ロード時に読み込むかどうかを設定したい場合
     */
    PartsTexture(std::string fileName,
                 double sourceWidth, double sourceHeight,
                 bool isLoadAtInit)
    {
        double fileWidth = GlUtils::getTextureSize(sourceWidth);
        double fileHeight = GlUtils::getTextureSize(sourceHeight);
        double spriteOffsetX = 0.0;
        double spriteOffsetY = 0.0;

        mFileName = fileName;
        mTextureId = 0;
        mIsLoadAtInit = isLoadAtInit;

        double leftTopX = spriteOffsetX / fileWidth;
        double leftTopY = spriteOffsetY / fileHeight;
        double leftBottomX = leftTopX;
        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
        double rightTopX = leftTopX + (sourceWidth / fileWidth);
        double rightTopY = leftTopY;
        double rightBottomX = rightTopX;
        double rightBottomY = leftBottomY;

        mLeftTopX = leftTopX;
        mLeftTopY = leftTopY;
        mLeftBottomX = leftBottomX;
        mLeftBottomY = leftBottomY;
        mRightTopX = rightTopX;
        mRightTopY = rightTopY;
        mRightBottomX = rightBottomX;
        mRightBottomY = rightBottomY;
    }

    /**
     * Constructor
     *
     * FileSizeがSourceSize直近の2のべき乗と同じで、
     * 尚且つSpriteSheetで、
     * 必ず初回ロード時に読み込む場合
     */
    PartsTexture(std::string fileName,
                 double sourceWidth, double sourceHeight,
                 double spriteOffsetX, double spriteOffsetY, bool isRotate)
    {
        mFileName = fileName;
        mTextureId = 0;

        double fileWidth = GlUtils::getTextureSize(sourceWidth);
        double fileHeight = GlUtils::getTextureSize(sourceHeight);
        mIsLoadAtInit = true;

        if (isRotate)
        {
            double tmp = sourceWidth;
            sourceWidth = sourceHeight;
            sourceHeight = tmp;
        }

        double leftTopX = spriteOffsetX / fileWidth;
        double leftTopY = spriteOffsetY / fileHeight;
        double leftBottomX = leftTopX;
        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
        double rightTopX = leftTopX + (sourceWidth / fileWidth);
        double rightTopY = leftTopY;
        double rightBottomX = rightTopX;
        double rightBottomY = leftBottomY;

        if (isRotate)
        {
            mLeftTopX = rightTopX;
            mLeftTopY = rightTopY;
            mLeftBottomX = leftTopX;
            mLeftBottomY = leftTopY;

            mRightTopX = rightBottomX;
            mRightTopY = rightBottomY;
            mRightBottomX = leftBottomX;
            mRightBottomY = leftBottomY;
            return;
        }

        mLeftTopX = leftTopX;
        mLeftTopY = leftTopY;
        mLeftBottomX = leftBottomX;
        mLeftBottomY = leftBottomY;
        mRightTopX = rightTopX;
        mRightTopY = rightTopY;
        mRightBottomX = rightBottomX;
        mRightBottomY = rightBottomY;
    }

    /**
     * Constructor
     *
     * FileSizeがSourceSize直近の2のべき乗と同じで、
     * 尚且つSpriteSheetで、
     * 初回ロード時に読み込むかどうかを設定したい場合
     */
    PartsTexture(std::string fileName,
                 double sourceWidth, double sourceHeight,
                 double spriteOffsetX, double spriteOffsetY, bool isRotate,
                 bool isLoadAtInit)
    {
        mFileName = fileName;
        mTextureId = 0;

        double fileWidth = GlUtils::getTextureSize(sourceWidth);
        double fileHeight = GlUtils::getTextureSize(sourceHeight);
        mIsLoadAtInit = true;

        if (isRotate)
        {
            double tmp = sourceWidth;
            sourceWidth = sourceHeight;
            sourceHeight = tmp;
        }

        double leftTopX = spriteOffsetX / fileWidth;
        double leftTopY = spriteOffsetY / fileHeight;
        double leftBottomX = leftTopX;
        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
        double rightTopX = leftTopX + (sourceWidth / fileWidth);
        double rightTopY = leftTopY;
        double rightBottomX = rightTopX;
        double rightBottomY = leftBottomY;

        if (isRotate)
        {
            mLeftTopX = rightTopX;
            mLeftTopY = rightTopY;
            mLeftBottomX = leftTopX;
            mLeftBottomY = leftTopY;

            mRightTopX = rightBottomX;
            mRightTopY = rightBottomY;
            mRightBottomX = leftBottomX;
            mRightBottomY = leftBottomY;
            return;
        }

        mLeftTopX = leftTopX;
        mLeftTopY = leftTopY;
        mLeftBottomX = leftBottomX;
        mLeftBottomY = leftBottomY;
        mRightTopX = rightTopX;
        mRightTopY = rightTopY;
        mRightBottomX = rightBottomX;
        mRightBottomY = rightBottomY;
    }

    // [補足]
    // 引数の関係で以下のような画像は作らないでもらいたい
    // ・FileSizeがSourceSize直近の2のべき乗と同じでなく、尚且つSpriteSheetでない
//    /**
//     * Constructor
//     *
//     * FileSizeがSourceSize直近の2のべき乗と同じでなく、
//     * 尚且つSpriteSheetでなく、
//     * 必ず初回ロード時に読み込む場合
//     */
//    PartsTexture(std::string fileName,
//                 double fileWidth, double fileHeight,
//                 double sourceWidth, double sourceHeight)
//    {
//        mFileName = fileName;
//        mTextureId = 0;
//
//        fileWidth = GlUtils::getTextureSize(fileWidth);
//        fileHeight = GlUtils::getTextureSize(fileHeight);
//        double spriteOffsetX = 0.0;
//        double spriteOffsetY = 0.0;
//        mIsLoadAtInit = true;
//
//        double leftTopX = spriteOffsetX / fileWidth;
//        double leftTopY = spriteOffsetY / fileHeight;
//        double leftBottomX = leftTopX;
//        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
//        double rightTopX = leftTopX + (sourceWidth / fileWidth);
//        double rightTopY = leftTopY;
//        double rightBottomX = rightTopX;
//        double rightBottomY = leftBottomY;
//
//        mLeftTopX = leftTopX;
//        mLeftTopY = leftTopY;
//        mLeftBottomX = leftBottomX;
//        mLeftBottomY = leftBottomY;
//        mRightTopX = rightTopX;
//        mRightTopY = rightTopY;
//        mRightBottomX = rightBottomX;
//        mRightBottomY = rightBottomY;
//    }
//
//    /**
//     * Constructor
//     *
//     * FileSizeがSourceSize直近の2のべき乗と同じでなく、
//     * 尚且つSpriteSheetでなく、
//     * 初回ロード時に読み込むかどうかを設定したい場合
//     */
//    PartsTexture(std::string fileName,
//                 double fileWidth, double fileHeight,
//                 double sourceWidth, double sourceHeight,
//                 bool isLoadAtInit)
//    {
//        mFileName = fileName;
//        mTextureId = 0;
//
//        fileWidth = GlUtils::getTextureSize(fileWidth);
//        fileHeight = GlUtils::getTextureSize(fileHeight);
//        double spriteOffsetX = 0.0;
//        double spriteOffsetY = 0.0;
//        mIsLoadAtInit = isLoadAtInit;
//
//        double leftTopX = spriteOffsetX / fileWidth;
//        double leftTopY = spriteOffsetY / fileHeight;
//        double leftBottomX = leftTopX;
//        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
//        double rightTopX = leftTopX + (sourceWidth / fileWidth);
//        double rightTopY = leftTopY;
//        double rightBottomX = rightTopX;
//        double rightBottomY = leftBottomY;
//
//        mLeftTopX = leftTopX;
//        mLeftTopY = leftTopY;
//        mLeftBottomX = leftBottomX;
//        mLeftBottomY = leftBottomY;
//        mRightTopX = rightTopX;
//        mRightTopY = rightTopY;
//        mRightBottomX = rightBottomX;
//        mRightBottomY = rightBottomY;
//    }
//
    /**
     * Constructor
     *
     * FileSizeがSourceSize直近の2のべき乗と同じでなく、
     * 尚且つSpriteSheetで、
     * 必ず初回ロード時に読み込む場合
     */
    PartsTexture(std::string fileName,
                 double fileWidth, double fileHeight,
                 double sourceWidth, double sourceHeight,
                 double spriteOffsetX, double spriteOffsetY, bool isRotate)
    {
        mFileName = fileName;
        mTextureId = 0;

        fileWidth = GlUtils::getTextureSize(fileWidth);
        fileHeight = GlUtils::getTextureSize(fileHeight);
        mIsLoadAtInit = true;

        if (isRotate)
        {
            double tmp = sourceWidth;
            sourceWidth = sourceHeight;
            sourceHeight = tmp;
        }

        double leftTopX = spriteOffsetX / fileWidth;
        double leftTopY = spriteOffsetY / fileHeight;
        double leftBottomX = leftTopX;
        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
        double rightTopX = leftTopX + (sourceWidth / fileWidth);
        double rightTopY = leftTopY;
        double rightBottomX = rightTopX;
        double rightBottomY = leftBottomY;

        if (isRotate)
        {
            mLeftTopX = rightTopX;
            mLeftTopY = rightTopY;
            mLeftBottomX = leftTopX;
            mLeftBottomY = leftTopY;

            mRightTopX = rightBottomX;
            mRightTopY = rightBottomY;
            mRightBottomX = leftBottomX;
            mRightBottomY = leftBottomY;
            return;
        }

        mLeftTopX = leftTopX;
        mLeftTopY = leftTopY;
        mLeftBottomX = leftBottomX;
        mLeftBottomY = leftBottomY;
        mRightTopX = rightTopX;
        mRightTopY = rightTopY;
        mRightBottomX = rightBottomX;
        mRightBottomY = rightBottomY;
    }

    /**
     * Constructor
     *
     * FileSizeがSourceSize直近の2のべき乗と同じでなく、
     * 尚且つSpriteSheetで、
     * 初回ロード時に読み込むかどうかを設定したい場合
     */
    PartsTexture(std::string fileName,
                 double fileWidth, double fileHeight,
                 double sourceWidth, double sourceHeight,
                 double spriteOffsetX, double spriteOffsetY, bool isRotate,
                 bool isLoadAtInit)
    {
        mFileName = fileName;
        mTextureId = 0;

        fileWidth = GlUtils::getTextureSize(fileWidth);
        fileHeight = GlUtils::getTextureSize(fileHeight);
        mIsLoadAtInit = isLoadAtInit;

        if (isRotate)
        {
            double tmp = sourceWidth;
            sourceWidth = sourceHeight;
            sourceHeight = tmp;
        }

        double leftTopX = spriteOffsetX / fileWidth;
        double leftTopY = spriteOffsetY / fileHeight;
        double leftBottomX = leftTopX;
        double leftBottomY = leftTopY + (sourceHeight / fileHeight);
        double rightTopX = leftTopX + (sourceWidth / fileWidth);
        double rightTopY = leftTopY;
        double rightBottomX = rightTopX;
        double rightBottomY = leftBottomY;

        if (isRotate)
        {
            mLeftTopX = rightTopX;
            mLeftTopY = rightTopY;
            mLeftBottomX = leftTopX;
            mLeftBottomY = leftTopY;

            mRightTopX = rightBottomX;
            mRightTopY = rightBottomY;
            mRightBottomX = leftBottomX;
            mRightBottomY = leftBottomY;
            return;
        }

        mLeftTopX = leftTopX;
        mLeftTopY = leftTopY;
        mLeftBottomX = leftBottomX;
        mLeftBottomY = leftBottomY;
        mRightTopX = rightTopX;
        mRightTopY = rightTopY;
        mRightBottomX = rightBottomX;
        mRightBottomY = rightBottomY;
    }

};

#endif //TEST_PARTSTEXTURE_H

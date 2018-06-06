//
//
//
#include "RotateAnimation.h"

// ------------------------------
// Override
// ------------------------------
void
RotateAnimation::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    if (mMaxAnimationTime == 0 || !mIsAnimating)
    {
        // isKeepAfterScaleの場合は、ここで処理
        if (mIsOncePlay && mAfterOffsetTime < 0)
        {
            switch (mAxis)
            {
                case eRotateAxis_X: {
                    flushKeepAxisX(screenWidth, screenHeight, vertexts);
                    break;
                }

                case eRotateAxis_Y: {
                    flushKeepAxisY(screenWidth, screenHeight, vertexts);
                    break;
                }

                case eRotateAxis_Z: {
                    flushKeepAxisZ(screenWidth, screenHeight, vertexts);
                    break;
                }
            }
            return;
        }

        return;
    }

    mIsOncePlay = true;

    switch (mAxis)
    {
        case eRotateAxis_X: {
            flushAxisX(screenWidth, screenHeight, vertexts);
            break;
        }

        case eRotateAxis_Y: {
            flushAxisY(screenWidth, screenHeight, vertexts);
            break;
        }

        case eRotateAxis_Z: {
            flushAxisZ(screenWidth, screenHeight, vertexts);
            break;
        }
    }

}

// ------------------------------
// Function
// ------------------------------
void
RotateAnimation::flushAxisX(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    //
    // パラメーターをセットする
    // ------------------------------
    double cs = 0.0;
    double sn = 0.0;

    //
    // 前OffsetTime時
    // ------------------------------
    if (getCurrentAnimationTime() < 0)
    {
        cs = cos(mFrom * M_PI / 180.0);
        sn = sin(mFrom * M_PI / 180.0);
    }

    //
    // 後OffsetTime時
    // ------------------------------
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        cs = cos(mTo * M_PI / 180.0);
        sn = sin(mTo * M_PI / 180.0);
    }

        //
        // アニメーション時
        // ------------------------------
    else
    {
        double theta = ((mTo - mFrom) * M_PI * getCurrentAnimationTime()) / (mMaxAnimationTime * 180.0);
        cs = cos(mFrom * M_PI / 180.0 + theta);
        sn = sin(mFrom * M_PI / 180.0 + theta);
    }

    //
    // X軸かY軸を中心に回転
    // ------------------------------
    // [解説]
    // X軸かY軸を中心に回転する際には、xもしくはyの値が固定となり、
    // screenWidthを元にして算出した比率がyに代入されることは在り得ない。
    // その為こちらでそれを考慮する必要はない。
    // X軸を中心に回転
    double rotateMatrix[3 * 3]
    = {
            1.0, 0.0, 0.0,
            0.0, cs, -sn,
            0.0, sn, cs,
    };

    int i;
    for (i = 0; i < 4; i++)
    {
        double x = vertexts[3 * i];
        double y = vertexts[3 * i + 1];
        double z = vertexts[3 * i + 2];
        vertexts[3 * i]         = x * rotateMatrix[0] + y * rotateMatrix[3] + z * rotateMatrix[6];
        vertexts[3 * i + 1]     = x * rotateMatrix[1] + y * rotateMatrix[4] + z * rotateMatrix[7];
        vertexts[3 * i + 2]     = x * rotateMatrix[2] + y * rotateMatrix[5] + z * rotateMatrix[8];
    }
}

void
RotateAnimation::flushAxisY(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    //
    // パラメーターをセットする
    // ------------------------------
    double cs = 0.0;
    double sn = 0.0;

    //
    // 前OffsetTime時
    // ------------------------------
    if (getCurrentAnimationTime() < 0)
    {
        cs = cos(mFrom * M_PI / 180.0);
        sn = sin(mFrom * M_PI / 180.0);
    }

    //
    // 後OffsetTime時
    // ------------------------------
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        cs = cos(mTo * M_PI / 180.0);
        sn = sin(mTo * M_PI / 180.0);
    }

    //
    // アニメーション時
    // ------------------------------
    else
    {
        double theta = ((mTo - mFrom) * M_PI * getCurrentAnimationTime()) / (mMaxAnimationTime * 180.0);
        cs = cos(mFrom * M_PI / 180.0 + theta);
        sn = sin(mFrom * M_PI / 180.0 + theta);
    }

    //
    // X軸かY軸を中心に回転
    // ------------------------------
    // [解説]
    // X軸かY軸を中心に回転する際には、xもしくはyの値が固定となり、
    // screenWidthを元にして算出した比率がyに代入されることは在り得ない。
    // その為こちらでそれを考慮する必要はない。
    // X軸を中心に回転
    double rotateMatrix[3 * 3]
    = {
            cs, 0.0, sn,
            0.0, 1.0, 0.0,
            -sn, 0.0, cs,
    };

    int i;
    for (i = 0; i < 4; i++)
    {
        double x = vertexts[3 * i];
        double y = vertexts[3 * i + 1];
        double z = vertexts[3 * i + 2];
        vertexts[3 * i]         = x * rotateMatrix[0] + y * rotateMatrix[3] + z * rotateMatrix[6];
        vertexts[3 * i + 1]     = x * rotateMatrix[1] + y * rotateMatrix[4] + z * rotateMatrix[7];
        vertexts[3 * i + 2]     = x * rotateMatrix[2] + y * rotateMatrix[5] + z * rotateMatrix[8];
    }
}

void
RotateAnimation::flushAxisZ(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    //
    // Z軸を中心に回転時のみ
    // ------------------------------
    // [解説]
    // 渡されるvertextsの値はscreenWidthとscreenHeightを元に算出した比率になっているが、
    // 回転をすることでxとyの値が入れ替わることが考えられる。(90°の場合等)
    // この場合、Widthに対する比率がyの値に入ることを防ぐ為に、
    // 一度比率から値に戻しておく必要があるので、その処理を行う
    vertexts[0] *= screenWidth;
    vertexts[3] *= screenWidth;
    vertexts[6] *= screenWidth;
    vertexts[9] *= screenWidth;
    vertexts[1] *= screenHeight;
    vertexts[4] *= screenHeight;
    vertexts[7] *= screenHeight;
    vertexts[10] *= screenHeight;

    //
    // パラメーターをセットする
    // ------------------------------
    double cs = 0.0;
    double sn = 0.0;

    //
    // 前OffsetTime時
    // ------------------------------
    if (getCurrentAnimationTime() < 0)
    {
        cs = cos(mFrom * M_PI / 180.0);
        sn = sin(mFrom * M_PI / 180.0);
    }

        //
        // 後OffsetTime時
        // ------------------------------
    else if (getCurrentAnimationTime() > mMaxAnimationTime)
    {
        cs = cos(mTo * M_PI / 180.0);
        sn = sin(mTo * M_PI / 180.0);
    }

        //
        // アニメーション時
        // ------------------------------
    else
    {
        double interportator = makeStraightInterpolator();
        double theta = (mTo - mFrom) * M_PI /180.0;
        cs = cos(mFrom * M_PI / 180.0 + theta * interportator);
        sn = sin(mFrom * M_PI / 180.0 + theta * interportator);
    }

    //
    // Z軸を中心に回転
    // ------------------------------
    // 反映
    // [解説]
    // さっき比率から値に戻したので、
    // ここでは再度screenWidth, screenHeight其々の比率に戻す必要がある。
    // (OpenGLの座標系は(-1, -1) -> (1, 1)である)
    int i;
    double x, y;
    for (i = 0; i < 4; i++)
    {
        x = vertexts[3 * i];
        y = vertexts[3 * i + 1];
        vertexts[3 * i]         = (x * cs - y * sn) / screenWidth;
        vertexts[3 * i + 1]     = (x * sn + y * cs) / screenHeight;
    }
}

void
RotateAnimation::flushKeepAxisX(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    //
    // パラメーターをセットする
    // ------------------------------
    double cs = cos(mTo * M_PI / 180.0);
    double sn = sin(mTo * M_PI / 180.0);

    //
    // X軸かY軸を中心に回転
    // ------------------------------
    // [解説]
    // X軸かY軸を中心に回転する際には、xもしくはyの値が固定となり、
    // screenWidthを元にして算出した比率がyに代入されることは在り得ない。
    // その為こちらでそれを考慮する必要はない。
    // X軸を中心に回転
    double rotateMatrix[3 * 3]
    = {
            1.0, 0.0, 0.0,
            0.0, cs, -sn,
            0.0, sn, cs,
    };

    int i;
    for (i = 0; i < 4; i++)
    {
        double x = vertexts[3 * i];
        double y = vertexts[3 * i + 1];
        double z = vertexts[3 * i + 2];
        vertexts[3 * i]         = x * rotateMatrix[0] + y * rotateMatrix[3] + z * rotateMatrix[6];
        vertexts[3 * i + 1]     = x * rotateMatrix[1] + y * rotateMatrix[4] + z * rotateMatrix[7];
        vertexts[3 * i + 2]     = x * rotateMatrix[2] + y * rotateMatrix[5] + z * rotateMatrix[8];
    }
}

void
RotateAnimation::flushKeepAxisY(int screenWidth, int screenHeight, GLfloat *vertexts)
{
//
    // パラメーターをセットする
    // ------------------------------
    double cs = cos(mTo * M_PI / 180.0);
    double sn = sin(mTo * M_PI / 180.0);

    //
    // X軸かY軸を中心に回転
    // ------------------------------
    // [解説]
    // X軸かY軸を中心に回転する際には、xもしくはyの値が固定となり、
    // screenWidthを元にして算出した比率がyに代入されることは在り得ない。
    // その為こちらでそれを考慮する必要はない。
    // X軸を中心に回転
    double rotateMatrix[3 * 3]
    = {
            cs, 0.0, sn,
            0.0, 1.0, 0.0,
            -sn, 0.0, cs,
    };

    int i;
    for (i = 0; i < 4; i++)
    {
        double x = vertexts[3 * i];
        double y = vertexts[3 * i + 1];
        double z = vertexts[3 * i + 2];
        vertexts[3 * i]         = x * rotateMatrix[0] + y * rotateMatrix[3] + z * rotateMatrix[6];
        vertexts[3 * i + 1]     = x * rotateMatrix[1] + y * rotateMatrix[4] + z * rotateMatrix[7];
        vertexts[3 * i + 2]     = x * rotateMatrix[2] + y * rotateMatrix[5] + z * rotateMatrix[8];
    }
}

void
RotateAnimation::flushKeepAxisZ(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    //
    // Z軸を中心に回転時のみ
    // ------------------------------
    // [解説]
    // 渡されるvertextsの値はscreenWidthとscreenHeightを元に算出した比率になっているが、
    // 回転をすることでxとyの値が入れ替わることが考えられる。(90°の場合等)
    // この場合、Widthに対する比率がyの値に入ることを防ぐ為に、
    // 一度比率から値に戻しておく必要があるので、その処理を行う
    vertexts[0] *= screenWidth;
    vertexts[3] *= screenWidth;
    vertexts[6] *= screenWidth;
    vertexts[9] *= screenWidth;
    vertexts[1] *= screenHeight;
    vertexts[4] *= screenHeight;
    vertexts[7] *= screenHeight;
    vertexts[10] *= screenHeight;

    double cs = cos(mTo * M_PI / 180.0);
    double sn = sin(mTo * M_PI / 180.0);

    //
    // Z軸を中心に回転
    // ------------------------------
    // 反映
    // [解説]
    // さっき比率から値に戻したので、
    // ここでは再度screenWidth, screenHeight其々の比率に戻す必要がある。
    // (OpenGLの座標系は(-1, -1) -> (1, 1)である)
    int i;
    double x, y;
    for (i = 0; i < 4; i++)
    {
        x = vertexts[3 * i];
        y = vertexts[3 * i + 1];
        vertexts[3 * i]         = (x * cs - y * sn) / screenWidth;
        vertexts[3 * i + 1]     = (x * sn + y * cs) / screenHeight;
    }
}
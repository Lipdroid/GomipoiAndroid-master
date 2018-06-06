//
// Object共通のパラメーター
//

#if IS_ANDROID
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#else
#include <OpenGLES/ES2/gl.h>
#include <OpenGLES/ES2/glext.h>
#endif

#include <string>
#include <vector>
#include <algorithm>
#include <functional>
#include <future>
#include <sstream>
#include <iomanip>
#include <map>

#if IS_ANDROID
#include "../utility/android_log.h"
#else
#include "../utility/ios_log.h"
#endif

#include "../utility/utility.h"
#include "../utility/RandomUtils.h"

#ifndef PARAMETER_H
#define PARAMETER_H

//
// アプリケーションのFPS
// ------------------------------
#define appFPS 60.0

//
// StandardSize
// ------------------------------
// 座標定義の基準となるサイズ(実際の描画時にアスペクト比を守りながら、スクリーンサイズを考慮して拡縮します)
#define STANDARD_WIDTH 320.0
#define STANDARD_HEIGHT 568.0
//#define STANDARD_HEIGHT 548.0
#define RATIO(width,height) (fmin(width / STANDARD_WIDTH, height / STANDARD_HEIGHT))

//
// Callback type
// ------------------------------
typedef std::function<bool()>           tCallback_isLockedEvent;
typedef std::function<bool()>           tCallback_isLoading;
typedef std::function<bool()>           tCallback_isPauseing;
typedef std::function<bool()>           tCallback_isSuspending;
typedef std::function<void(int)>        tCallback_onPartsClicked;
typedef std::function<void(int)>        tCallback_onPartsEvent;
typedef std::function<void(int)>        tCallback_onCompletedAnim;
typedef std::function<void(int)>        tCallback_onChangeTexture;
typedef std::function<void()>           tCallback_onLoadingEnd;
typedef std::function<void()>           tCallback_onPartsPressed;
typedef std::function<void()>           tCallback_onPartsPressEnd;

//
// Shader
// ------------------------------
#define NUM_OBJECT_VERTEXCOMPONENTS		3 * 4
#define NUM_OBJECT_TEXCOORDCOMPONENTS	2 * 4

enum
{
    eInterpolator_Straight = 0,
    eInterpolator_Curve,
};

#endif //PARAMETER_H

//
// Animationの基幹クラス
//
#include "AnimationBase.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * AnimationのIDを返す
 */
int
AnimationBase::getAnimationId()
{
    return mAnimationId;
}

/**
 * Animation中かを返す
 */
bool
AnimationBase::isAnimating()
{
    return mIsAnimating;
}

bool
AnimationBase::isLooping()
{
    return mIsLoopingPlay;
}

/**
 * Animationを開始する
 */
void
AnimationBase::start(tCallback_onCompletedAnim onCompletedAnimation)
{
    if (isPausing())
    {
        return;
    }

    stop();

    mIsAnimating = true;
    mCompletedFunc = onCompletedAnimation;

    mStartTime = TimeUtils::getTime();
}

/**
 * Animationを停止する
 */
void
AnimationBase::stop()
{
    if (isPausing())
    {
        return;
    }

    mIsAnimating = false;
    mStartTime = 0;
    mPauseStartTime = 0;
    mPauseTotalTime = 0;
}

/**
 * Pause処理
 */
void
AnimationBase::pause()
{
    mPauseStartTime = TimeUtils::getTime();
}

/**
 * unpause処理
 */
void
AnimationBase::pauseEnd()
{
    mPauseTotalTime += getTimeDif(mPauseStartTime);
    mPauseStartTime = 0;
}

/**
 * 描画するAlpha値を返す
 */
double
AnimationBase::getCurrentAlpha()
{
    return 1.0;
}

/**
 * アニメーションが終了していないかを確認する
 */
void
AnimationBase::checkState()
{
    if (!mIsAnimating)
    {
        return;
    }

    if (getCurrentAnimationTime() - mAfterOffsetTime > mMaxAnimationTime)
    {
        if (mIsLoopingPlay)
        {
            // ループアニメーション
            start(mCompletedFunc);
            return;
        }

        stop();

        if (mCompletedFunc != 0)
        {
            // アニメーションの終了通知
            mCompletedFunc(mAnimationId);
            mCompletedFunc = 0;
        }

        return;
    }

    return;
}

/**
 * アニメーションをVertextsに反映する
 */
void
AnimationBase::flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    // Overrideを想定
    // [注意!]
    // 複数のAnimationを仕掛ける場合には、Scale -> Translateの順番で行ってください
    // Scaleアニメーションの拡縮の中心がずれると、思い通りのアニメーションにならない可能性があります
}

/**
 * 一時停止フラグのコールバックをセットする
 */
void
AnimationBase::setIsPausingCallback(PausingCallback *callback)
{
    mPausingCallback = callback;
}

// ------------------------------
// Function
// ------------------------------
/**
 * Pause中かを返す
 */
bool
AnimationBase::isPausing()
{
    if (mPausingCallback == 0)
    {
        return false;
    }
    return mPausingCallback->isAppPausing();
}

/**
 * 経過時間を返す
 */
double
AnimationBase::getTimeDif(double startTime)
{
    return TimeUtils::getTime() - startTime;

}

/**
 * 現在のAnimationFrame(currentFrame - beforOffset)を返す
 */
double
AnimationBase::getCurrentAnimationTime()
{
    double pauseTime = mPauseTotalTime;
    if (isPausing())
    {
        pauseTime += getTimeDif(mPauseStartTime);
    }

    return getTimeDif(mStartTime) - pauseTime - mBeforeOffsetTime;
}

/**
 * 直線的なInterpolatorを作成する
 */
double
AnimationBase::makeStraightInterpolator()
{
    return getCurrentAnimationTime() / mMaxAnimationTime;
}

/**
 * 曲線的なInterpolatorを作成する
 */
double
AnimationBase::makeCurveInterpolator()
{
    double progress = makeStraightInterpolator();
    return pow(progress, 2.0); // y = x^2
}

/**
 * 変則的な変化(二次関数使用 - 変曲点1つ)のInterpolatorを作成する
 */
double
AnimationBase::makeVariableCurveInterpolator(double changePointX, double changePointY)
{
    double progress = makeStraightInterpolator();
    if (progress <= changePointX)
    {
        return changePointY * pow(progress, 2.0) / pow(changePointX, 2.0); // y = ax^2
    }
    return ((changePointY - 1.0) * pow((progress - 1.0), 2.0) / pow((changePointX - 1.0), 2.0)) + 1.0; // y = a(x-1)^2 + 1
}

/**
 * アニメーションの時間を返す
 */
double
AnimationBase::getTotalTime()
{
    return mBeforeOffsetTime + mMaxAnimationTime + mAfterOffsetTime;
}

void
AnimationBase::setKeepAfter(bool isKeep)
{
    if (mAfterOffsetTime < 0)
    {
        mAfterOffsetTime = 0;
    }

    if (isKeep)
    {
        mAfterOffsetTime = -1;
    }
}

void
AnimationBase::setBeforeOffsetTime(double offsetTime)
{
    mBeforeOffsetTime = offsetTime;
}

void
AnimationBase::setAffterOffsetTime(double offsetTime)
{
    mAfterOffsetTime = offsetTime;
}

void
AnimationBase::setLooping(bool isLoop)
{
    mIsLoopingPlay = isLoop;
}

void AnimationBase::setInterpolatorType(int type)
{
    mInterpolatorType = type;
}

/**
 * ブレンド用のパーツ. デフォルトでブレンドしないので、NULLを返す
 */
PartsBase* AnimationBase::getBlendParts()
{
    return NULL;
}

/**
 * ブレンド用のTexture ID. デフォルトでブレンドしないので、0を返す
 */
GLuint AnimationBase::getBlendTextureId()
{
    return 0;
}

/**
 * ブレンド用のxオフセット. デフォルトでブレンドしないので、0を返す
 */
GLfloat AnimationBase::getBlendOffsetX()
{
    return 0.0;
}

/**
 * ブレンド用のyオフセット. デフォルトでブレンドしないので、0を返す
 */
GLfloat AnimationBase::getBlendOffsetY()
{
    return 0.0;
}

/**
 * アニメーションで座標が変わる場合使用する。現在PathAnimationしか実装していない
 */
double AnimationBase::getCurrentLeftOffset()
{
    return 0.0;
}

/**
 * アニメーションで座標が変わる場合使用する。現在PathAnimationしか実装していない
 */
double AnimationBase::getCurrentTopOffset()
{
    return 0.0;
}

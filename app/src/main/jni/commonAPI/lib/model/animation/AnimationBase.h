//
// Animation基幹クラス
//
#include <math.h>
#include "../../const/parameter.h"
#include "../../utility/TimeUtils.h"
#include "../../callback/PausingCallback.h"

#ifndef TEST_ANIMATIONBASE_H
#define TEST_ANIMATIONBASE_H

class PartsBase;

class AnimationBase
{

// ------------------------------
// Member
// ------------------------------
protected:
    int mAnimationId;
    bool mIsAnimating;
    bool mIsLoopingPlay;
    bool mIsOncePlay;

    double mBeforeOffsetTime;
    double mMaxAnimationTime;
    double mAfterOffsetTime;

    int mInterpolatorType;

    tCallback_onCompletedAnim mCompletedFunc;
    PausingCallback *mPausingCallback;

private:
    double mStartTime;
    double mPauseStartTime;
    double mPauseTotalTime;

// ------------------------------
// Constructor/ Destructor
// ------------------------------
public:
    AnimationBase(int animationId)
    {
        mAnimationId = animationId;
        mIsAnimating = false;
        mIsLoopingPlay = false;
        mIsOncePlay = false;

        mInterpolatorType = eInterpolator_Straight;

        mStartTime = 0;
        mPauseStartTime = 0;
        mPauseTotalTime = 0;

        mMaxAnimationTime = 0;
        mBeforeOffsetTime = 0;
        mAfterOffsetTime = 0;
    }

    virtual ~AnimationBase()
    {
        mCompletedFunc = NULL;
        mPausingCallback = NULL;
    }

// ------------------------------
// Function
// ------------------------------
protected:
    bool isPausing();
    bool isLooping();
    double getCurrentAnimationTime();
    double makeStraightInterpolator();
    double makeCurveInterpolator();
    double makeVariableCurveInterpolator(double changePointX, double changePointY);
    double getTimeDif(double startTime);

// ------------------------------
// Accesser
// ------------------------------
public:
    int getAnimationId();
    bool isAnimating();
    virtual void start(tCallback_onCompletedAnim onCompletedAnimation);
    virtual void stop();
    virtual void pause();
    virtual void pauseEnd();
    virtual void checkState();
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);
    virtual double getCurrentAlpha();
    virtual void setIsPausingCallback(PausingCallback *callback);

    virtual double getTotalTime();
    void setKeepAfter(bool isKeep);
    virtual void setBeforeOffsetTime(double offsetTime);
    virtual void setAffterOffsetTime(double offsetTime);
    void setLooping(bool isLoop);
    void setInterpolatorType(int type);
    
    virtual PartsBase* getBlendParts();
    virtual GLuint getBlendTextureId();
    virtual GLfloat getBlendOffsetX();
    virtual GLfloat getBlendOffsetY();
    
    virtual double getCurrentLeftOffset();
    virtual double getCurrentTopOffset();

};


#endif //TEST_ANIMATIONBASE_H

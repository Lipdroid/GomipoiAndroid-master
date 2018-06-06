//
//
//
#include "../../../const/parameter.h"
#include "../AnimationBase.h"

#ifndef JANPOI_GENERALROTATEANIMATION_H
#define JANPOI_GENERALROTATEANIMATION_H

// ------------------------------
// Enum
// ------------------------------
enum
{
    eRotateAxis_X = 0,
    eRotateAxis_Y,
    eRotateAxis_Z,
};


class RotateAnimation : public AnimationBase
{

// ------------------------------
// Member
// ------------------------------
private:
    double mFrom;
    double mTo;
    int mAxis;

// ------------------------------
// Constructor / Destructor
// ------------------------------
public:
    RotateAnimation(int animationId, double from, double to, double duration, bool looping) : AnimationBase(animationId)
    {
        mFrom = from;
        mTo = to;
        mAxis = eRotateAxis_Z;
        mMaxAnimationTime = duration;
        mIsLoopingPlay = looping;
    }

    RotateAnimation(int animationId, int axis, double from, double to, double duration, bool looping) : AnimationBase(animationId)
    {
        mFrom = from;
        mTo = to;
        mAxis = axis;
        mMaxAnimationTime = duration;
        mIsLoopingPlay = looping;
    }

    virtual ~RotateAnimation()
    {
    }

// ------------------------------
// Override
// ------------------------------
public:
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);

// ------------------------------
// Function
// ------------------------------
private:
    void flushAxisX(int screenWidth, int screenHeight, GLfloat *vertexts);
    void flushAxisY(int screenWidth, int screenHeight, GLfloat *vertexts);
    void flushAxisZ(int screenWidth, int screenHeight, GLfloat *vertexts);
    void flushKeepAxisX(int screenWidth, int screenHeight, GLfloat *vertexts);
    void flushKeepAxisY(int screenWidth, int screenHeight, GLfloat *vertexts);
    void flushKeepAxisZ(int screenWidth, int screenHeight, GLfloat *vertexts);

};


#endif //JANPOI_GENERALROTATEANIMATION_H

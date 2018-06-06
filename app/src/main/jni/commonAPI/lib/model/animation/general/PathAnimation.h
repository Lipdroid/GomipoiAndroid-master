//
//  PathAnimation.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/15.
//
//

#ifndef PathAnimation_h
#define PathAnimation_h

#include "../../../const/parameter.h"
#include "../AnimationBase.h"

typedef struct 
{
    double x;
    double y;
    double time;
} PathData;


class PathAnimation : public AnimationBase {
    
// ------------------------------
// Member
// ------------------------------
private:
    PathData** mPath;
    int mDataLength;
    
// ------------------------------
// Constructor
// ------------------------------
public:
    PathAnimation(int animationId, double* pathX, double* pathY, double* time, int dataLenth,
                   double duration) : AnimationBase(animationId)
    {
        mMaxAnimationTime = duration;
        mInterpolatorType = eInterpolator_Straight;
        mDataLength = dataLenth;
        
        mPath = new PathData*[dataLenth];
        int i;
        for (i = 0; i < dataLenth; i++)
        {
            PathData* data = new PathData();
            data->x = pathX[i];
            data->y = pathY[i];
            data->time = time[i];
            mPath[i] = data;
        }
    }
    
    virtual ~PathAnimation()
    {
        int i;
        for (i = 0; i < mDataLength; i++)
        {
            if (mPath[i] != NULL)
                delete mPath[i];
        }
        delete mPath;
    }
    
// ------------------------------
// Override
// ------------------------------
public:
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);
    virtual double getCurrentLeftOffset();
    virtual double getCurrentTopOffset();

};

#endif /* PathAnimation_h */

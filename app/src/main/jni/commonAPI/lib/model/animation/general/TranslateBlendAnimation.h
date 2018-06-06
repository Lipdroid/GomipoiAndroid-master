//
//  TranslateBlendAnimation.h
//  Gomibako
//
//  Created by Tan Herve on 2016/09/01.
//
//

#ifndef TranslateBlendAnimation_h
#define TranslateBlendAnimation_h

#include "../../../const/parameter.h"
#include "../AnimationBase.h"
#include "../../parts/PartsBase.h"

/**
 * 穴に落ちるときのアニメーション
 */
class TranslateBlendAnimation : public AnimationBase {
    
// ------------------------------
// Member
// ------------------------------
private:
    double mFromX;
    double mToX;
    double mFromY;
    double mToY;
    
    PartsBase *mBlendParts;
    double mOriginalOffsetX;
    double mOriginalOffsetY;
    
    double mOffsetX;
    double mOffsetY;
    
// ------------------------------
// Constructor
// ------------------------------
public:
    TranslateBlendAnimation(int animationId, double fromX, double fromY, double toX, double toY, PartsBase *blendParts, double duration) : AnimationBase(animationId)
    {
        mFromX = fromX;
        mFromY = fromY;
        mToX = toX;
        mToY = toY;
        mMaxAnimationTime = duration;
        mInterpolatorType = eInterpolator_Curve;
        
        mBlendParts = blendParts;
        
        mOriginalOffsetX = 0.0;
        mOriginalOffsetY = 0.0;
        mOffsetX = 0.0;
        mOffsetY = 0.0;
    }
    
    virtual ~TranslateBlendAnimation()
    {
        mBlendParts = NULL;
    }
    
// ------------------------------
// Override
// ------------------------------
    virtual void flushAnimation(int screenWidth, int screenHeight, GLfloat *vertexts);
    virtual PartsBase* getBlendParts();
    virtual GLuint getBlendTextureId();
    virtual GLfloat getBlendOffsetX();
    virtual GLfloat getBlendOffsetY();
    
// ------------------------------
// Accesser
// ------------------------------
    void setPosition(double fromX, double fromY, double toX, double toY);
    void setOriginalOffset(double offsetX, double offsetY);
};

#endif /* TranslateBlendAnimation_h */

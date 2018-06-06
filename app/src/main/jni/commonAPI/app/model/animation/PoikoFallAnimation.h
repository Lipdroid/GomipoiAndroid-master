//
//  PoikoFallAnimation.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/01.
//
//

#ifndef PoikoFallAnimation_h
#define PoikoFallAnimation_h

#include "../../../lib/model/animation/AnimationBase.h"
#include "../../parameter/app_parameter.h"

#include "../../../lib/model/parts/PartsBase.h"
#include "../../../lib/model/animation/general/TranslateBlendAnimation.h"

class PoikoFallAnimation : public TranslateBlendAnimation
{
// ------------------------------
// Define
// ------------------------------
private:
    
// ------------------------------
// Member
// ------------------------------
private:
    
// ------------------------------
// Constructor
// ------------------------------
public:
    PoikoFallAnimation(double fromX, double fromY, double toX, double toY, PartsBase *blendParts) : TranslateBlendAnimation(eAnimId_Hole_FallIn, fromX, fromY, toX, toY, blendParts, 700)
    {
        setOriginalOffset(-20.0, 0.0);
    }
    
// ------------------------------
// Destructor
// ------------------------------
    virtual ~PoikoFallAnimation()
    {
    }

};

#endif /* PoikoFallAnimation_h */

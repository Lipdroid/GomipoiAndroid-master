//
//  PoikoFallOutAnimation.hpp
//  Gomibako
//
//  Created by Tan Herve on 2016/09/05.
//
//

#ifndef PoikoFallOutAnimation_h
#define PoikoFallOutAnimation_h

#include "../../../lib/model/animation/AnimationBase.h"
#include "../../parameter/app_parameter.h"

#include "../../../lib/model/parts/PartsBase.h"
#include "../../../lib/model/animation/general/TranslateAnimation.h"

class PoikoFallOutAnimation : public TranslateAnimation
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
    PoikoFallOutAnimation(double fromX, double fromY, double toX, double toY) : TranslateAnimation(eAnimId_Hole_FallOut, fromX, fromY, toX, toY, 1000)
    {
    }
    
    // ------------------------------
    // Destructor
    // ------------------------------
    virtual ~PoikoFallOutAnimation()
    {
    }
    
};

#endif /* PoikoFallOutAnimation_h */

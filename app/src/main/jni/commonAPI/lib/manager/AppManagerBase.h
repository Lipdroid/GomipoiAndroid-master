//
// アプリケーション管理クラス
//
#include "../const/parameter.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#if IS_ANDROID
#include <jni.h>
#include <android/bitmap.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#else
    // TODO iOS特有のincludeが必要ならここ
#endif

#include "../callback/AppManagerCallback.h"
#include "../utility/GlUtils.h"

#include "ShaderManager.h"
#include "event/ClickEventManager.h"
#include "event/TrackingEventManager.h"

#ifndef TEST_OPENGLMANAGER_H
#define TEST_OPENGLMANAGER_H

class AppManagerBase {

// ------------------------------
// Member
// ------------------------------
private:
    std::mutex mMutex;

protected:
    std::vector<PartsBase *> mPartsList;
    std::map<std::string, GLuint> mTextureMap;

    ShaderManager *mShaderManager;

    // screenSize
    double mScreenWidth;
    double mScreenHeight;

#if IS_ANDROID
    // AssetManager
    AAssetManager *gAssetManager = NULL;
#else
#endif

    // Event
    ClickEventManager *mClickManager;
    TrackingEventManager *mTrackingEventManager;

    // Callback
    AppManagerCallback *mCallback;


// ------------------------------
// Constructor
// ------------------------------
public:
    AppManagerBase()
    {
        mScreenWidth = 0;
        mScreenHeight = 0;

        mClickManager = 0;
        mTrackingEventManager = 0;
        mCallback = 0;
    }

    virtual ~AppManagerBase()
    {
        mCallback = 0;

        if (mClickManager != 0)
        {
            delete mClickManager;
            mClickManager = 0;
        }

        if (mTrackingEventManager != 0)
        {
            delete mTrackingEventManager;
            mTrackingEventManager = 0;
        }

        if (mTextureMap.size() > 0)
        {
            mTextureMap.clear();
        }

        int i;
        for (i = mPartsList.size() - 1; i >= 0; i--)
        {
            if (mPartsList[i] != 0)
            {
                mPartsList[i]->stopAnimation();
                delete mPartsList[i];
            }
            mPartsList.erase(mPartsList.begin() + i);
        }

#if IS_ANDROID
        gAssetManager = 0;
#endif

    }

// ------------------------------
// Accesser
// ------------------------------
public:
    int getScreenWidth();
    int getScreenHeight();
#if IS_ANDROID
    void setAssetManager(JNIEnv* env, jobject object);
#endif
    virtual void setCallback(AppManagerCallback *callback);
    virtual std::vector<PartsBase*> getAdditionalTexture();
    virtual void onSurfaceCreated();
    virtual void onSurfaceChanged(double width, double height);
    virtual void onDrawFrame();
    virtual void onTouchDown(float ptX, float ptY);
    virtual void onTouchMove(float ptX, float ptY);
    virtual void onTouchUp();
    virtual void pauseEnd();
    virtual void pause();
    virtual void foreground();
    virtual void background();

// ------------------------------
// Function
// ------------------------------
protected:
    PartsBase *getParts(int id);
    virtual void onDraw();
    void drawShader(PartsBase *parts, GLfloat* vertexts, GLfloat* texcoord);
};


#endif //TEST_OPENGLMANAGER_H


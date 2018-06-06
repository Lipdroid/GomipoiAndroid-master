//
// パーツの基幹クラス
//
#include "../../const/parameter.h"
#include "../../callback/PartsCallback.h"
#include "../animation/AnimationBase.h"
#include "../animation/general/FrameAnimation.h"
#include "PartsTexture.h"

#ifndef TEST_MODELBASE_H
#define TEST_MODELBASE_H

class PartsBase {

    // ------------------------------
    // Member
    // ------------------------------
public:
    std::vector<PartsTexture *> mTextureList;

protected:
    // PartsID
    int mPartsId;
    // CurrentTexturePosition
    int mCurrentTexturePosition;

    // Animation
    std::vector<AnimationBase *>mAnimationList;
    int mCurrentActiveAnimationId;

    // Rect
    // 画像のサイズが2のべき乗でないといけない端末が存在したので、
    // 画像ファイルのwidth, heightと実際の画像のwidth, heightを分けて所持する
    double mWidth;
    double mHeight;
    double mOffsetX;
    double mOffsetY;

    // Callback
    PartsCallback *mCallback;
    tCallback_onPartsPressed mOnPartsPressed;
    tCallback_onPartsPressEnd mOnPartsPressEnd;

private:
    bool mIsVisible;
    bool mIsClickable;
    bool mIsTrackable;
    bool mIsTracking;

    // ------------------------------
    // Constructor
    // ------------------------------
public:
    PartsBase(int partsId)
    {
        // 初期化
        mPartsId = partsId;
        mCurrentTexturePosition = 0;
        mCurrentActiveAnimationId = 0;

        mCallback = 0;
        mOnPartsPressed = 0;
        mOnPartsPressEnd = 0;

        mWidth = 0;
        mHeight = 0;
        mOffsetX = 0;
        mOffsetY = 0;

        setClickable(false);
        setTrackable(false);
        show();
    }

    PartsBase(int partsId, double x, double y, double width, double height)
    {
        // 初期化
        mPartsId = partsId;
        mCurrentTexturePosition = 0;
        mCurrentActiveAnimationId = 0;

        mCallback = 0;
        mOnPartsPressed = 0;
        mOnPartsPressEnd = 0;

        mOffsetX = x;
        mOffsetY = y;
        mWidth = width;
        mHeight = height;

        setClickable(false);
        setTrackable(false);
        show();
    }

    // ------------------------------
    // Destructor
    // ------------------------------
    virtual ~PartsBase()
    {
        for (PartsTexture* texture : mTextureList) {
            if (texture != 0) {
                delete texture;
            }
        }
        mTextureList.clear();

        for (AnimationBase* animation : mAnimationList) {
            if (animation != 0) {
                delete animation;
            }
        }
        mAnimationList.clear();

        mCallback = 0;
        mOnPartsPressed = 0;
        mOnPartsPressEnd = 0;

    }

    // ------------------------------
    // Accesser
    // ------------------------------
public:
    // Getter
    int getPartsId();
    GLuint getCurrentTextureId();
    std::string getPngFileName(int index);
    GLuint getTextureId(int index);
    std::vector<int> getAllTextureIds();
    void setTextureId(int index, GLuint textureId);

    virtual void setTexturePosition(int position);

    // ActionFrag
    bool isClickable();
    void setClickable(bool isClickable);
    bool isTracking();
    bool isTrackable();
    void setTrackable(bool isTrackable);
    bool isVisible();
    void show();
    void hide();

    // 初期化
    virtual void initParts(PartsCallback *callback);
    void setOffset(double left, double top);
    double getLeftOffset();
    double getTopOffset();
    void setRect(double left, double top, double width, double height);
    double getWidth();
    double getHeight();
    double getCurrentLeft();
    double getCurrentTop();
    void setOnPressCallback(tCallback_onPartsPressed onPressed, tCallback_onPartsPressEnd onPressEnd);

    // 描画関連
    virtual void getCurrentVertexts(int screenWidth, int screenHeight, GLfloat *vertexts);
    virtual void getCurrentTexcoord(GLfloat *texcoord);
    virtual double getAlpha();

    // Event
    bool isTouched(float ptX, float ptY, int screenWidth, int screenHeight);
    virtual void onPressed();
    virtual void onPressedEnd();
    virtual void onTracking();
    virtual void onTrackingEnd();

    // Textureの管理
    void addTexture(PartsTexture *texture);

    // Animation管理
    void addAnimation(AnimationBase *animation);
    AnimationBase *getCurrentAnimation();
    AnimationBase *getAnimation(int animationId);
    virtual void startAnimation(int animationId);
    virtual void startAnimation(int animationId, tCallback_onCompletedAnim callback);
    virtual void startFrameAnimation(int animationId, tCallback_onCompletedAnim callback);
    virtual void stopAnimation();
    PartsBase* getBlendParts();
    GLuint getBlendTextureId();
    GLfloat getBlendOffsetX();
    GLfloat getBlendOffsetY();

    // ------------------------------
    // Function
    // ------------------------------
protected:
    // Pause関連
    bool isPausing();

    // 描画関連
    void createDefaultVertexts(double screenWidth, double screenHeight, GLfloat *vertexts);
    void createDefaultTexcoord(GLfloat *texcoord);
    void flushOffset(double screenWidth, double screenHeight, GLfloat *vertexts);

};


#endif //TEST_MODELBASE_H

//
// パーツの基幹クラス
//

#include "PartsBase.h"

// ------------------------------
// Accesser
// ------------------------------
//
// IDやParts内で定義している情報を返す
// ------------------------------
/**
 * PartsIDを返す
 */
int
PartsBase::getPartsId()
{
    return mPartsId;
}

/**
 * 現在表示すべきTextureIDを返す
 */
GLuint
PartsBase::getCurrentTextureId()
{
    if (mCurrentTexturePosition < 0
        || mCurrentTexturePosition >= mTextureList.size()
        || mTextureList[mCurrentTexturePosition] == 0)
    {
        return 0;
    }
    return mTextureList[mCurrentTexturePosition]->mTextureId;
}

/**
 * 渡されたindexの画像ファイル名を返す
 */
std::string
PartsBase::getPngFileName(int index)
{
    if (index < 0
        || index >= mTextureList.size()
        || mTextureList[index] == 0)
    {
        return "";
    }
    return mTextureList[index]->mFileName;
}

/**
 * 渡されたindexのTextureIDを返す
 */
GLuint
PartsBase::getTextureId(int index)
{
    if (index < 0
        || index >= mTextureList.size()
        || mTextureList[index] == 0)
    {
        return 0;
    }
    return mTextureList[index]->mTextureId;
}

/**
 * TextureIDのリストを返す
 */
std::vector<int>
PartsBase::getAllTextureIds()
{
    std::vector<int> textureIds = std::vector<int>();
    
    for (PartsTexture* texture : mTextureList)
    {
        textureIds.push_back(texture->mTextureId);
    }
    return textureIds;
}

void
PartsBase::setTextureId(int index, GLuint textureId)
{
    if (index < 0
        || index >= mTextureList.size()
        || mTextureList[index] == 0)
    {
        return;
    }
    mTextureList[index]->mTextureId = textureId;
}

void
PartsBase::setTexturePosition(int position)
{
    mCurrentTexturePosition = position;
}

//
// ActionFrag
// ------------------------------
/**
 * クリックイベントを取得するかを返す
 */
bool
PartsBase::isClickable()
{
    return mIsClickable;
}

/**
 * クリックイベントを取得するかを設定する
 */
void
PartsBase::setClickable(bool isClickable)
{
    mIsClickable = isClickable;
}

/**
 * なぞるイベント中かを返す
 */
bool
PartsBase::isTracking()
{
    return mIsTracking;
}

/**
 * なぞるイベントを取得するかを返す
 */
bool
PartsBase::isTrackable()
{
    return mIsTrackable;
}

/**
 * なぞるイベントを取得するかを設定する
 */
void
PartsBase::setTrackable(bool isTrackable)
{
    mIsTrackable = isTrackable;
}

/**
 * Partsを表示するかを返す
 */
bool
PartsBase::isVisible()
{
    return mIsVisible;
}

/**
 * Partsを表示する
 */
void
PartsBase::show()
{
    mIsVisible = true;
}

/**
 * Partsを非表示にする
 */
void
PartsBase::hide()
{
    mIsVisible = false;
}

//
// 初期化
// ------------------------------
/**
 * パーツを初期化する
 */
void
PartsBase::initParts(PartsCallback *callback)
{
    mCallback = callback;

    int i;
    for (i = 0; i < mAnimationList.size(); i++)
    {
        AnimationBase *animation = mAnimationList[i];
        if (animation != 0)
        {
            animation->setIsPausingCallback(mCallback->mPausing);
        }
    }
}

/**
 * オフセットをセットする
 */
void
PartsBase::setOffset(double left, double top)
{
    mOffsetX = left;
    mOffsetY = top;
}

double
PartsBase::getLeftOffset()
{
    return mOffsetX;
}

double
PartsBase::getTopOffset()
{
    return mOffsetY;
}

/**
 * Rectをセットする
 */
void
PartsBase::setRect(double left, double top, double width, double height)
{
    mOffsetX = left;
    mOffsetY = top;
    mWidth = width;
    mHeight = height;
}

double
PartsBase::getWidth()
{
    return mWidth;
}

double
PartsBase::getHeight()
{
    return mHeight;
}

double
PartsBase::getCurrentLeft()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return getLeftOffset() + animation->getCurrentLeftOffset();
    }
    else return getLeftOffset();
}

double
PartsBase::getCurrentTop()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return getTopOffset() + animation->getCurrentTopOffset();
    }
    else return getTopOffset();
}

/**
 * Press時のコールバックをセットする
 */
void
PartsBase::setOnPressCallback(tCallback_onPartsPressed onPressed, tCallback_onPartsPressEnd onPressEnd)
{
    mOnPartsPressed = onPressed;
    mOnPartsPressEnd = onPressEnd;
}

//
// 描画関連
// ------------------------------
/**
 * 現在表示すべきVertextsを返す
 */
void
PartsBase::getCurrentVertexts(int screenWidth, int screenHeight, GLfloat *vertexts)
{
    PartsBase::createDefaultVertexts(screenWidth, screenHeight, vertexts);

    if (mAnimationList.size() > 0)
    {
        AnimationBase *animation = getCurrentAnimation();
        if (animation != 0)
        {
            animation->flushAnimation(screenWidth, screenHeight, vertexts);
        }
    }

    PartsBase::flushOffset(screenWidth, screenHeight, vertexts);
}

/**
 * 現在描画すべきTexcoordを返す
 */
void
PartsBase::getCurrentTexcoord(GLfloat *texcoord)
{
    PartsBase::createDefaultTexcoord(texcoord);

    if (mTextureList.size() <= mCurrentTexturePosition || mCurrentTexturePosition < 0)
    {
        return;
    }

    PartsTexture *texture = mTextureList[mCurrentTexturePosition];
    if (texture != 0)
    {
        texcoord[0] = texture->mLeftTopX;
        texcoord[1] = texture->mLeftTopY;
        texcoord[2] = texture->mLeftBottomX;
        texcoord[3] = texture->mLeftBottomY;
        texcoord[4] = texture->mRightTopX;
        texcoord[5] = texture->mRightTopY;
        texcoord[6] = texture->mRightBottomX;
        texcoord[7] = texture->mRightBottomY;
    }
}

/**
 * パーツのアルファ値を返す
 */
double
PartsBase::getAlpha()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getCurrentAlpha();
    }

    return 1.0;
}

//
// Event
// ------------------------------
/**
 * タッチ座標との当たり判定を行う
 */
bool
PartsBase::isTouched(float ptX, float ptY, int screenWidth, int screenHeight)
{
    double ratio = RATIO(screenWidth, screenHeight);

    // Vertextsを取得する
    GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
    getCurrentVertexts(screenWidth, screenHeight, vertexts);

    float left = vertexts[0];
    float right = vertexts[6];
    float top = vertexts[1];
    float bottom = vertexts[4];

    // 判定
    bool isIncludeX = (ptX >= left && ptX <= right);
    bool isIncludeY = (ptY >= bottom && ptY <= top);
    return (isIncludeX && isIncludeY);
}

/**
 * Partsにタッチした時に呼ばれる
 */
void
PartsBase::onPressed()
{
    if (mOnPartsPressed != 0)
    {
        mOnPartsPressed();
    }
}

/**
 * Partsのタッチが終了した時に呼ばれる
 */
void
PartsBase::onPressedEnd()
{
    if (mOnPartsPressEnd != 0)
    {
        mOnPartsPressEnd();
    }
}

/**
 * Partsをなぞっている時(最初の1回のみ)に呼ばれる
 */
void
PartsBase::onTracking()
{
    mIsTracking = true;
}

/**
 * Partsのなぞるアクションが終了した時に呼ばれる
 */
void
PartsBase::onTrackingEnd()
{
    mIsTracking = false;
}

//
// Textureの管理
// ------------------------------
void
PartsBase::addTexture(PartsTexture *texture)
{
    mTextureList.push_back(texture);
}

//
// Animationの管理
// ------------------------------
void
PartsBase::addAnimation(AnimationBase *animation)
{
    if (animation == 0)
    {
        return;
    }

    if (getAnimation(animation->getAnimationId()) != 0)
    {
        return;
    }

    mAnimationList.push_back(animation);
}

/**
 * 現在再生中のアニメーションを返す
 */
AnimationBase*
PartsBase::getCurrentAnimation()
{
    if (mAnimationList.size() == 0) {
        return 0;
    }

    int i;
    for (i = 0; i < mAnimationList.size(); i++)
    {
        if (mAnimationList[i] != 0
            && mAnimationList[i]->getAnimationId() == mCurrentActiveAnimationId)
        {
            return mAnimationList[i];
        }
    }
    return 0;
}

/**
 * アニメーションを開始する
 */
void
PartsBase::startAnimation(int animationId)
{
    if (isPausing())
    {
        return;
    }

    stopAnimation();

    mCurrentActiveAnimationId = animationId;
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->start(0);
    }
}

/**
 * アニメーションを開始する
 */
void
PartsBase::startAnimation(int animationId, tCallback_onCompletedAnim callback)
{
    if (isPausing())
    {
        return;
    }

    stopAnimation();

    mCurrentActiveAnimationId = animationId;
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->start(callback);
    }
}

void
PartsBase::startFrameAnimation(int animationId, tCallback_onCompletedAnim callback)
{
    if (isPausing())
    {
        return;
    }

    stopAnimation();

    mCurrentActiveAnimationId = animationId;
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        ((FrameAnimation *)animation)->start(
                callback,
                [&](int texturePosition)
                {
                    setTexturePosition(texturePosition);
                });
    }
}

/**
 * アニメーションを停止する
 */
void
PartsBase::stopAnimation()
{
    if (isPausing() || mAnimationList.size() == 0)
    {
        return;
    }

    mCurrentActiveAnimationId = 0;
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        animation->stop();
    }
}

/**
 * アルファブレンドのパーツ
 */
PartsBase*
PartsBase::getBlendParts()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getBlendParts();
    }
    else return NULL;
}

/**
 * アルファブレンドのTexture ID
 */
GLuint
PartsBase::getBlendTextureId()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getBlendTextureId();
    }
    else return 0;
}

/**
 * アルファブレンドのxオフセット
 */
GLfloat
PartsBase::getBlendOffsetX()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getBlendOffsetX();
    }
    else return 0;
}

/**
 * アルファブレンドのyオフセット
 */
GLfloat
PartsBase::getBlendOffsetY()
{
    AnimationBase *animation = getCurrentAnimation();
    if (animation != 0)
    {
        return animation->getBlendOffsetY();
    }
    else return 0;
}

// ------------------------------
// Function
// ------------------------------
//
// 一時停止関連
// ------------------------------
/**
 * 一時停止中かを返す
 */
bool
PartsBase::isPausing()
{
    if (mCallback == 0 || mCallback->mPausing == 0)
    {
        return false;
    }
    return mCallback->mPausing->isAppPausing();
}

//
// 描画関連
// ------------------------------
/**
 * DefaultVertextsを作成する
 */
void
PartsBase::createDefaultVertexts(double screenWidth, double screenHeight, GLfloat *vertexts)
{
    double ratio = RATIO(screenWidth, screenHeight);

    // STANDARD_WIDTH : STANDARD_HEIGHTの比率を守ったまま、
    // screenWidth, screenHeightに内接するように拡縮する
    float width = mWidth * ratio / screenWidth;
    float height = mHeight * ratio / screenHeight;

    vertexts[0] = -width;     // 左上 X
    vertexts[1] = height;     //     Y
    vertexts[2] = 0.0;        //     Z
    vertexts[3] = -width;     // 左下 X
    vertexts[4] = -height;    //     Y
    vertexts[5] = 0.0;        //     Z
    vertexts[6] = width;      // 右上 X
    vertexts[7] = height;     //     Y
    vertexts[8] = 0.0;        //     Z
    vertexts[9] = width;      // 右下 X
    vertexts[10] = -height;   //     Y
    vertexts[11] = 0.0;       //     Z
}

/**
 * VertextsにOffsetを反映する
 */
void
PartsBase::flushOffset(double screenWidth, double screenHeight, GLfloat *vertexts)
{
    double ratio = RATIO(screenWidth, screenHeight);

    // offsetは画面左上を基準にする(interfaceBuilderの値をそのまま使えるように)
    float offsetX = -(STANDARD_WIDTH - mWidth) + (mOffsetX * 2.0);
    float offsetY = (STANDARD_HEIGHT - mHeight) - (mOffsetY * 2.0);

    // STANDARD_WIDTH : STANDARD_HEIGHTの比率を守ったまま、
    // screenWidth, screenHeightに内接するように拡縮する
    vertexts[0] += offsetX * ratio / screenWidth;
    vertexts[3] += offsetX * ratio / screenWidth;
    vertexts[6] += offsetX * ratio / screenWidth;
    vertexts[9] += offsetX * ratio / screenWidth;

    vertexts[1] += offsetY * ratio / screenHeight;
    vertexts[4] += offsetY * ratio / screenHeight;
    vertexts[7] += offsetY * ratio / screenHeight;
    vertexts[10] += offsetY * ratio / screenHeight;
}

/**
 * DefaultのTexcoordを作成する
 */
void
PartsBase::createDefaultTexcoord(GLfloat *texcoord)
{
    // 左上
    texcoord[0] = 0.0f;
    texcoord[1] = 0.0f;
    // 左下
    texcoord[2] = 0.0f;
    texcoord[3] = 1.0f;
    // 右上
    texcoord[4] = 1.0f;
    texcoord[5] = 0.0f;
    // 右下
    texcoord[6] = 1.0f;
    texcoord[7] = 1.0f;
}

//
// Animationの管理
// ------------------------------
AnimationBase*
PartsBase::getAnimation(int animationId)
{
    int i;
    for (i = 0; i < mAnimationList.size(); i++)
    {
        if (mAnimationList[i] == 0)
        {
            continue;
        }

        if (mAnimationList[i]->getAnimationId() == animationId)
        {
            return mAnimationList[i];
        }
    }
    return 0;
}
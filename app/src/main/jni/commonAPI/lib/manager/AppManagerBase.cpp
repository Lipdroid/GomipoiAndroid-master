//
// アプリケーション管理クラス
//
#include "AppManagerBase.h"

#if IS_ANDROID
#else
#import <UIKit/UIKit.h>
#endif

// ------------------------------
// Accesser
// ------------------------------
/**
 * スクリーンのwidthを返す
 */
int
AppManagerBase::getScreenWidth()
{
    return mScreenWidth;
}

/**
 * スクリーンのheightを返す
 */
int
AppManagerBase::getScreenHeight()
{
    return mScreenHeight;
}

#if IS_ANDROID
/**
 * Javaから受け取った、AssetManagerをセットする
 */
void
AppManagerBase::setAssetManager(JNIEnv* env, jobject object)
{
    gAssetManager = AAssetManager_fromJava(env, object);
}
#endif

/**
 * コールバック関数を受け取る
 */
void
AppManagerBase::setCallback(AppManagerCallback *callback)
{
    mCallback = callback;

    if (mClickManager != 0 && mCallback != 0)
    {
        mClickManager->setClickCallback(mCallback->mOnPartsClicked);
    }

    // Partsの初期化処理
    int i;
    for (i = 0; i < mPartsList.size(); i++)
    {
        if (mPartsList[i] == 0)
        {
            continue;
        }

        if (mCallback != 0)
        {
            mPartsList[i]->initParts(mCallback->getPartsCallback());
        }
    }
}

/**
 * 追加テキスチャー
 */
std::vector<PartsBase*>
AppManagerBase::getAdditionalTexture()
{
    return std::vector<PartsBase*>();
}

/**
 * onSurfaceCreated時に呼ばれる
 */
void
AppManagerBase::onSurfaceCreated()
{
    mShaderManager = new ShaderManager();
    std::vector<std::string> fileList;

    // 同名ファイル判定用のMapが残っていたら、削除しておく
    if (mTextureMap.size() > 0)
    {
        mTextureMap.clear();
    }

    //
    // 読み込む画像ファイル名のリスト作成
    // ------------------------------
    int i, j;
    for(i = 0; i < mPartsList.size(); i++)
    {
        if (mPartsList[i] == 0)
        {
            continue;
        }

        for (j = 0; j < mPartsList[i]->mTextureList.size(); j++)
        {
            if (mPartsList[i]->mTextureList[j] == 0)
            {
                continue;
            }

            if (!mPartsList[i]->mTextureList[j]->mIsLoadAtInit)
            {
                continue;
            }

            if(find(fileList.begin(), fileList.end(), mPartsList[i]->getPngFileName(j)) == fileList.end())
            {
                fileList.push_back(mPartsList[i]->getPngFileName(j));
            }
        }
    }
    
    std::vector<PartsBase*> additional = getAdditionalTexture();
    for(i = 0; i < additional.size(); i++)
    {
        if (additional[i] == 0)
        {
            continue;
        }
        
        for (j = 0; j < additional[i]->mTextureList.size(); j++)
        {
            if (additional[i]->mTextureList[j] == 0)
            {
                continue;
            }
            
            if (!additional[i]->mTextureList[j]->mIsLoadAtInit)
            {
                continue;
            }
            
            if(find(fileList.begin(), fileList.end(), additional[i]->getPngFileName(j)) == fileList.end())
            {
                fileList.push_back(additional[i]->getPngFileName(j));
            }
        }
    }

    //
    // 画像読み込みを非同期に行う
    // ------------------------------
    if (mShaderManager == 0)
    {
        // 随時サスペンドチェックを行う
        // (もっと良い手はないものか。。。)
        return;
    }

    std::vector<std::thread> imageLoadThreadList;
    std::map<std::string, unsigned char*> dataMap;
    std::map<std::string, GLsizei> widthMap;
    std::map<std::string, GLsizei> heightMap;
    for (i = 0; i < fileList.size(); i++)
    {
        imageLoadThreadList.push_back(
            std::thread(
            [&](std::string key)
            {
#if IS_ANDROID
                bool result;
                unsigned char* textureData = NULL;
                GLsizei        textureSize;
                GLsizei        textureWidth;
                GLsizei        textureHeight;
                if (gAssetManager != NULL)
                {
                    if (mShaderManager->makeImage(
                            gAssetManager,
                            key,
                            (unsigned char**)&textureData,
                            &textureSize,
                            &textureWidth,
                            &textureHeight))
                    {
                        // 排他制御は忘れずに
                        std::lock_guard<std::mutex> lock(mMutex);
                        dataMap[key] = textureData;
                        widthMap[key] = textureWidth;
                        heightMap[key] = textureHeight;
                    }
                    else
                    {
                        LOGE("image loading failed:%s", key.c_str());
                    }
                }
#else
            // iOSの画像読み込み
                // TODO: エラー処理
                
                // テクスチャー画像ファイルをロード
				
                NSString *fileName = [NSString stringWithUTF8String:key.c_str()];
				
#if 0	//iOS10でimageNamedは遅くなる？
				UIImage *image = [UIImage imageNamed:fileName];
#else
				NSString *filePath = [[NSBundle mainBundle] pathForResource:[fileName stringByDeletingPathExtension] ofType:@"png"];

                UIImage *image = [UIImage imageWithContentsOfFile:filePath];
#endif
                CGImageRef textureImage = image.CGImage;
                // サイズ取得
                NSInteger textWidth = CGImageGetWidth(textureImage);
                NSInteger textHeight = CGImageGetHeight(textureImage);
                // テクスチャメモリのデータを確保
                GLubyte *textureData = (GLubyte *)calloc(1, textWidth * textHeight * 4);
                
                CGColorSpaceRef space = CGColorSpaceCreateDeviceRGB();
                
                CGContextRef context = CGBitmapContextCreate(textureData, textWidth, textHeight, 8, textWidth * 4, space, kCGImageAlphaPremultipliedLast);
                
                CGContextDrawImage(context, CGRectMake(0, 0, textWidth, textHeight), textureImage);
                
                CGContextRelease(context);
                CGColorSpaceRelease(space);
                
                std::lock_guard<std::mutex> lock(mMutex);
                dataMap[key] = textureData;
                widthMap[key] = (GLsizei)textWidth;
                heightMap[key] = (GLsizei)textHeight;
#endif

            }, fileList[i]));
    }

    for (i = 0; i < imageLoadThreadList.size(); i++)
    {
        if (imageLoadThreadList[i].joinable())
        {
            imageLoadThreadList[i].join();
        }
    }
    imageLoadThreadList.shrink_to_fit();
    imageLoadThreadList.clear();
    fileList.shrink_to_fit();

    //
    // 読み込んだ画像をTextureに登録する
    // ※メインスレッドじゃないと正常に動作しない
    // ------------------------------
    for (std::map<std::string, unsigned char *>::iterator it = dataMap.begin(); it != dataMap.end(); ++it)
    {
        if (mShaderManager != 0)
        {
            mTextureMap[it->first] = glGetUniformLocation(mShaderManager->getProgram(), "texture");
            glGenTextures(1, &(mTextureMap[it->first]));
            glBindTexture(GL_TEXTURE_2D, mTextureMap[it->first]);
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    GlUtils::getTextureSize(widthMap[it->first]),
                    GlUtils::getTextureSize(heightMap[it->first]),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    0);
            glTexSubImage2D(
                    GL_TEXTURE_2D,
                    0,
                    0,
                    0,
                    widthMap[it->first],
                    heightMap[it->first],
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    it->second);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }

        free(it->second);
        it->second = NULL;
    }
    dataMap.clear();
    widthMap.clear();
    heightMap.clear();

    //
    // TextureIDを各Objectに非同期にセットする
    // ------------------------------
    if (mShaderManager == 0)
    {
        // 随時サスペンドチェックを行う
        // (もっと良い手はないものか。。。)
        return;
    }

    std::vector<std::thread> setIdThreadList;
    for(i = 0; i < mPartsList.size(); i++)
    {
        if (mPartsList[i] == 0)
        {
            continue;
        }

        for (j = 0; j < mPartsList[i]->mTextureList.size(); j++)
        {
            if (mPartsList[i]->mTextureList[j] == 0)
            {
                continue;
            }

            setIdThreadList.push_back(
                std::thread(
                    [&](PartsTexture *texture)
                    {
                        if (texture == 0)
                        {
                            return;
                        }

                        if (mTextureMap.find(texture->mFileName) == mTextureMap.end())
                        {
                            return;
                        }

                       texture->mTextureId = mTextureMap[texture->mFileName];
                    }, mPartsList[i]->mTextureList[j]));
        }
    }
    
    for(i = 0; i < additional.size(); i++)
    {
        if (additional[i] == 0)
        {
            continue;
        }
        
        for (j = 0; j < additional[i]->mTextureList.size(); j++)
        {
            if (additional[i]->mTextureList[j] == 0)
            {
                continue;
            }
            
            setIdThreadList.push_back(
                                      std::thread(
                                                  [&](PartsTexture *texture)
                                                  {
                                                      if (texture == 0)
                                                      {
                                                          return;
                                                      }
                                                      
                                                      if (mTextureMap.find(texture->mFileName) == mTextureMap.end())
                                                      {
                                                          return;
                                                      }
                                                      
                                                      texture->mTextureId = mTextureMap[texture->mFileName];
                                                  }, additional[i]->mTextureList[j]));
        }
    }

    for (i = 0; i < setIdThreadList.size(); i++)
    {
        if (setIdThreadList[i].joinable())
        {
            setIdThreadList[i].join();
        }
    }
    setIdThreadList.shrink_to_fit();
    setIdThreadList.clear();

    //
    // ローディングの終了を通知
    // ------------------------------
    if (mCallback != 0)
    {
        mCallback->mOnLoadingEnd();
    }
}

/**
 * onSurfaceChanged時に呼ばれる
 */
void
AppManagerBase::onSurfaceChanged(double width, double height)
{
    // スクリーンサイズを保存しておく
    mScreenWidth = width;
    mScreenHeight = height;
    if (mClickManager != 0)
    {
        mClickManager->setScreenSize(width, height);
    }

    if (mTrackingEventManager != 0)
    {
        mTrackingEventManager->setScreenSize(width, height);
    }

    // ビューポートを設定する
    glViewport(0, 0, width, height);
}

/**
 * onDrawFrame時に呼ばれる
 */
void
AppManagerBase::onDrawFrame()
{
    // ローティング中は描画を行わない
    if (mCallback == 0 || mCallback->mIsLoading())
    {
        return;
    }

    // 背景色を指定して背景を描画する
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    // 背景とのブレンド方法を設定する
    glEnable(GL_BLEND);
#if IS_ANDROID
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);	// 単純なアルファブレンド
#else
    glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
#endif

    // テクスチャを描画する
    onDraw();

    glDisable(GL_BLEND);
}

/**
 * onTouchDown時に呼ばれる
 */
void
AppManagerBase::onTouchDown(float ptX, float ptY)
{
    if (mCallback == 0 || mCallback->mIsLockedEvent())
    {
        return;
    }

    // クリックアクション判定
    if (mClickManager != 0
        && mClickManager->onTouchedDown(ptX, ptY))
    {
        return;
    }
}

/**
 * onTouchMove時に呼ばれる
 */
void
AppManagerBase::onTouchMove(float ptX, float ptY)
{
    if (mCallback == 0 || mCallback->mIsLockedEvent())
    {
        return;
    }

    // クリックアクション判定
    if (mClickManager != 0
        && mClickManager->onTouchedMove(ptX, ptY))
    {
        return;
    }

    // なぞるアクション判定
    if (mTrackingEventManager != 0)
    {
        mTrackingEventManager->onTouchedMove(ptX, ptY);
    }
}

/**
 * onTouchUp時に呼ばれる
 */
void
AppManagerBase::onTouchUp()
{
    if (mCallback == 0 || mCallback->mIsLockedEvent())
    {
        return;
    }

    // クリックアクション判定
    if (mClickManager != 0
        && mClickManager->onTouchedUp())
    {
        return;
    }

    // なぞるアクション判定
    mTrackingEventManager->onTouchedUp();
}

/**
 * 一時停止解除処理
 */
void
AppManagerBase::pauseEnd()
{
    // アニメーションの一時停止を解除する
    if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->mIsSuspending())
    {
        int i;
        for (i = 0; i < mPartsList.size(); i++)
        {
            if (mPartsList[i] == 0)
            {
                continue;
            }

            AnimationBase *animation = mPartsList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->pauseEnd();
            }
        }
    }
}

/**
 * 一時停止処理
 */
void
AppManagerBase::pause()
{
    // アニメーションを一時停止する
    if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->mIsSuspending())
    {
        int i;
        for (i = 0; i < mPartsList.size(); i++)
        {
            if (mPartsList[i] == 0)
            {
                continue;
            }

            AnimationBase *animation = mPartsList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->pause();
            }
        }
    }
}

/**
 * サスペンド復帰処理
 */
void
AppManagerBase::foreground()
{
    // アニメーションの一時停止を解除する
    if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->mIsPausing())
    {
        int i;
        for (i = 0; i < mPartsList.size(); i++)
        {
            if (mPartsList[i] == 0)
            {
                continue;
            }

            AnimationBase *animation = mPartsList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->pauseEnd();
            }
        }
    }
}

/**
 * サスペンド処理
 */
void
AppManagerBase::background()
{
    // アニメーションを一時停止する
    if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->mIsPausing())
    {
        int i;
        for (i = 0; i < mPartsList.size(); i++)
        {
            if (mPartsList[i] == 0)
            {
                continue;
            }

            AnimationBase *animation = mPartsList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->pause();
            }
        }
    }

    // ShaderManagerを削除
    if (mShaderManager != 0)
    {
        delete mShaderManager;
        mShaderManager = 0;
    }
}


// ------------------------------
// Function
// ------------------------------
/**
 * PartsIDからPartsを取得する
 */
PartsBase *
AppManagerBase::getParts(int id)
{
    int i;
    for (i = 0; i < mPartsList.size(); i++)
    {
        if (mPartsList[i] == 0)
        {
            continue;
        }

        if (mPartsList[i]->getPartsId() == id)
        {
            return mPartsList[i];
        }
    }
    return 0;
}

void
AppManagerBase::onDraw()
{
    int i;
    for (i = 0; i < mPartsList.size(); i++)
    {
        if (mPartsList[i] == 0)
        {
            continue;
        }

        if (!mPartsList[i]->isVisible())
        {
            continue;
        }

        // Vertextsを作成
        GLfloat vertexts[NUM_OBJECT_VERTEXCOMPONENTS];
        mPartsList[i]->getCurrentVertexts(mScreenWidth, mScreenHeight, vertexts);

        // Texcoordを作成
        GLfloat texcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
        mPartsList[i]->getCurrentTexcoord(texcoord);

        // 描画処理
        drawShader(mPartsList[i], vertexts, texcoord);

        // 再生中のAnimationがあれば、状態を確認する
        if (mCallback != 0 && mCallback->mPausing != 0 && !mCallback->mPausing->isAppPausing())
        {
            AnimationBase *animation = mPartsList[i]->getCurrentAnimation();
            if (animation != 0)
            {
                animation->checkState();
            }
        }
    }
}

void AppManagerBase::drawShader(PartsBase *parts, GLfloat* vertexts, GLfloat* texcoord)
{
    if (mShaderManager != 0 && parts != 0 && vertexts != 0 && texcoord != 0)
    {
        if (parts->getBlendTextureId() != 0)
        {
            // Vertextsを作成
            GLfloat blendVertexts[NUM_OBJECT_VERTEXCOMPONENTS];
            PartsBase* blendParts = parts->getBlendParts();
            blendParts->getCurrentVertexts(mScreenWidth, mScreenHeight, blendVertexts);
            
            // Texcoordを作成
            GLfloat blendTexcoord[NUM_OBJECT_TEXCOORDCOMPONENTS];
            blendParts->getCurrentTexcoord(blendTexcoord);
            
            mShaderManager->onDraw(
                                   parts->getCurrentTextureId(),
                                   vertexts,
                                   texcoord,
                                   parts->getAlpha(),
                                   blendVertexts,
                                   blendTexcoord,
                                   parts->getBlendTextureId());
        }
        else
        {
            mShaderManager->onDraw(
                                   parts->getCurrentTextureId(),
                                   vertexts,
                                   texcoord,
                                   parts->getAlpha());
        }
        
    }
}

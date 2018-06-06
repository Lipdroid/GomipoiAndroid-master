//
// Shader管理クラス
//
#include "ShaderManager.h"

// ------------------------------
// Accesser
// ------------------------------
/**
 * Programを返す
 */
GLuint
ShaderManager::getProgram()
{
    return mProgram;
}

#if IS_ANDROID
/**
 * 画像を取得する
 */
bool
ShaderManager::makeImage(AAssetManager *pAssetManager, const std::string name, unsigned char **textureData,
                           int *pImageSize, int *pImageWidth, int*pImageHeight)
{
    if (!textureData)
    {
        return false;
    }

    *textureData = NULL;
    int len = name.length() + 1;
    char* fname = new char[len];
    memcpy(fname, name.c_str(), len);

    // Assetから画像を読み込む
    AAsset *pAsset = AAssetManager_open(pAssetManager, fname, AASSET_MODE_UNKNOWN);
    if (!pAsset)
    {
        return false;
    }
    GameBitmapInfo imageInfo = { 0 };
    unsigned char *pData = NULL;
    PngUtils::createPNGData (pAsset, &imageInfo, &pData);
    if (pData)
    {
        *pImageSize		=	imageInfo.size;
        *pImageWidth	=	imageInfo.width;
        *pImageHeight	=	imageInfo.height;
        *textureData	=	pData;
    }
    AAsset_close(pAsset);
    pAsset = NULL;

    return (*textureData);
}

#else

#endif
/**
 * マスクを使う場合の描画処理
 */
void
ShaderManager::renderMask(GLuint textureId, GLfloat *vertexts, GLfloat *texcoord, double alpha, 
                          GLfloat *blendVertexts, GLfloat *blendTexcoord, GLuint blendTextureId)
{
    glActiveTexture(GL_TEXTURE0);
    
    //画像の(1-アルファ)チャネルのみを描画（RGB変更なし）。画があるべきところを透明にする
    glBindTexture(GL_TEXTURE_2D, textureId);
    glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE_MINUS_SRC_COLOR);
    glUniform1i(textureId, 0);
    glVertexAttribPointer(mTexcoord, 2, GL_FLOAT, false, 0, texcoord);
    glVertexAttribPointer(mPosition, 3, GL_FLOAT, false, 0, vertexts);
#if IS_ANDROID
    glUniform4f (mRGBA, 1.0f, 1.0f, 1.0f, alpha);
#else
    glUniform4f (mRGBA, alpha, alpha, alpha, alpha);
#endif
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    
    //マスクのアルファチャネルの描画。画像とマスクが両方透明な部分だけ残す
    glBindTexture(GL_TEXTURE_2D, blendTextureId);
    glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ONE, GL_ONE);
    glBlendEquationSeparate(GL_FUNC_ADD, GL_MAX_EXT);
    glUniform1i(blendTextureId, 0);
    glVertexAttribPointer(mTexcoord, 2, GL_FLOAT, false, 0, blendTexcoord);
    glVertexAttribPointer(mPosition, 3, GL_FLOAT, false, 0, blendVertexts);
#if IS_ANDROID
    glUniform4f (mRGBA, 1.0f, 1.0f, 1.0f, alpha);
#else
    glUniform4f (mRGBA, alpha, alpha, alpha, alpha);
#endif
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    
    //透明な部分の(RGB)を黒にする
    glBindTexture(GL_TEXTURE_2D, blendTextureId);
    glBlendFuncSeparate(GL_ZERO, GL_DST_ALPHA, GL_ONE, GL_ONE);
    glBlendEquation(GL_FUNC_ADD);
    glUniform1i(blendTextureId, 0);
    glVertexAttribPointer(mTexcoord, 2, GL_FLOAT, false, 0, blendTexcoord);
    glVertexAttribPointer(mPosition, 3, GL_FLOAT, false, 0, blendVertexts);
#if IS_ANDROID
    glUniform4f (mRGBA, 1.0f, 1.0f, 1.0f, alpha);
#else
    glUniform4f (mRGBA, alpha, alpha, alpha, alpha);
#endif
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    
    //黒なところに画像のRGBを描画
    glBindTexture(GL_TEXTURE_2D, textureId);
    glBlendFuncSeparate(GL_ONE_MINUS_DST_ALPHA, GL_ONE, GL_ONE, GL_ONE);
    glBlendEquation(GL_FUNC_ADD);
    glUniform1i(textureId, 0);
    glVertexAttribPointer(mTexcoord, 2, GL_FLOAT, false, 0, texcoord);
    glVertexAttribPointer(mPosition, 3, GL_FLOAT, false, 0, vertexts);
#if IS_ANDROID
    glUniform4f (mRGBA, 1.0f, 1.0f, 1.0f, alpha);
#else
    glUniform4f (mRGBA, alpha, alpha, alpha, alpha);
#endif
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    //透明な部分をオパックに戻す
    glBindTexture(GL_TEXTURE_2D, blendTextureId);
    glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    glBlendEquation(GL_FUNC_ADD);
    glUniform1i(blendTextureId, 0);
    glVertexAttribPointer(mTexcoord, 2, GL_FLOAT, false, 0, blendTexcoord);
    glVertexAttribPointer(mPosition, 3, GL_FLOAT, false, 0, blendVertexts);
#if IS_ANDROID
    glUniform4f (mRGBA, 1.0f, 1.0f, 1.0f, alpha);
#else
    glUniform4f (mRGBA, alpha, alpha, alpha, alpha);
#endif
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    
    //OpenGLの描画パラメータを元に戻す
#if IS_ANDROID
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
#else
    glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
#endif
    glBlendEquation(GL_FUNC_ADD);
}

/**
 * Draw処理を行う
 */
void
ShaderManager::onDraw(GLuint textureId, GLfloat *vertexts, GLfloat *texcoord, double alpha)
{
    onDraw(textureId, vertexts, texcoord, alpha, NULL, NULL, 0);
}

void
ShaderManager::onDraw(GLuint textureId, GLfloat *vertexts, GLfloat *texcoord, double alpha,
            GLfloat *blendVertexts, GLfloat *blendTexcoord, GLuint blendTextureId)
{
    if (blendTextureId != 0)
    {
        renderMask(textureId, vertexts, texcoord, alpha, blendVertexts, blendTexcoord, blendTextureId);
    }
    else
    {
        if (textureId == 0)
            return;
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(textureId, 0);
        glVertexAttribPointer(mTexcoord, 2, GL_FLOAT, false, 0, texcoord);
        glVertexAttribPointer(mPosition, 3, GL_FLOAT, false, 0, vertexts);
#if IS_ANDROID
        glUniform4f (mRGBA, 1.0f, 1.0f, 1.0f, alpha);
#else
        glUniform4f (mRGBA, alpha, alpha, alpha, alpha);
#endif
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }
}

// ------------------------------
// Function
// ------------------------------
/**
 * 指定された直前の OpenGL API 操作についてエラーが発生しているかどうか検証する
 *
 * @param op 検証する直前に操作した OpenGL API 名
 */
void
ShaderManager::checkGlError(const char* op)
{
    for (GLint error = glGetError(); error; error = glGetError())
    {
        LOGE("after %s() glError (0x%x)\n", op, error);
    }
}

/**
 * 指定されたシェーダのソースコードをコンパイルする
 *
 * @param shaderType シェーダの種類
 * @param shaderCode シェーダのソースコード
 * @return シェーダハンドルまたは <code>0</code>
 */
GLuint
ShaderManager::loadShader(GLenum shaderType, const char *shaderCode)
{
    GLuint shader = glCreateShader(shaderType);
    if (shader != 0)
    {
        glShaderSource(shader, 1, &shaderCode, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled)
        {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen)
            {
                char* buf = (char*) malloc(infoLen);
                if (buf)
                {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    LOGE("Could not compile shader %d:\n%s\n",shaderType, buf);
                    free(buf);
                }
            }
            glDeleteShader(shader);
        }
    }
    return shader;
}

/**
 * Programを作成する
 */
GLuint
ShaderManager::createProgram(const char* vertexCode, const char* fragmentCode)
{
    // バーテックスシェーダをコンパイルする
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, vertexCode);
    if (!vertexShader)
    {
        LOGE("vertex missed!");
        return 0;
    }

    // フラグメントシェーダをコンパイルする
    GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, fragmentCode);
    if (!pixelShader)
    {
        LOGE("fragment missed!");
        return 0;
    }

    // プログラムを生成して、プログラムへバーテックスシェーダとフラグメントシェーダを関連付ける
    GLuint program = glCreateProgram();
    if (program)
    {
        // プログラムへバーテックスシェーダを関連付ける
        glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        // プログラムへフラグメントシェーダを関連付ける
        glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");

        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE)
        {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength)
            {
                char* buf = (char*) malloc(bufLength);
                if (buf)
                {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            LOGE("program missed!");
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}
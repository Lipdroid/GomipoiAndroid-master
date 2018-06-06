//
// Shader管理クラス
//
#include "../const/parameter.h"
#include "../const/cube_shader.h"

#if IS_ANDROID
#include <android/bitmap.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "../utility/PngUtils.h"
#else
    // TODO iOS特有のincludeがある場合はここ
#endif

#ifndef TEST_SHADERMANAGER_H
#define TEST_SHADERMANAGER_H


class ShaderManager {

// ------------------------------
// Member
// ------------------------------
private:
    GLuint mProgram;
    GLuint mPosition;
    GLuint mTexcoord;
    GLuint mRGBA;

// ------------------------------
// Constructor
// ------------------------------
public:
    ShaderManager()
    {
        mProgram = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        if (!mProgram)
        {
            LOGE("Could not create program.");
        }
        glUseProgram(mProgram);
        checkGlError("glUseProgram");

        mPosition = glGetAttribLocation(mProgram, "position");
        checkGlError("glGetAttribLocation position");
        glEnableVertexAttribArray(mPosition);

        mTexcoord = glGetAttribLocation(mProgram, "texcoord");
        checkGlError("glGetAttribLocation texcoord");
        glEnableVertexAttribArray(mTexcoord);

        mRGBA = glGetUniformLocation(mProgram, "rgba");
        checkGlError("glGetAttribLocation rgba");
    }

    virtual ~ShaderManager()
    {
        mProgram = 0;
        mPosition = 0;
        mTexcoord = 0;
        mRGBA = 0;
    }

// ------------------------------
// Accesser
// ------------------------------
public:
    GLuint getProgram();
#if IS_ANDROID
    bool makeImage(AAssetManager *pAssetManager, const std::string name, unsigned char **textureData,
                     int *pImageSize, int *pImageWidth, int*pImageHeight);
#else
#endif
    void onDraw(GLuint textureId, GLfloat *vertexts, GLfloat *texcoord, double alpha);
    void onDraw(GLuint textureId, GLfloat *vertexts, GLfloat *texcoord, double alpha,
                GLfloat *blendVertexts, GLfloat *blendTexcoord, GLuint blendTextureId);

// ------------------------------
// Function
// ------------------------------
private:
    void checkGlError(const char* op);
    void renderMask(GLuint textureId, GLfloat *vertexts, GLfloat *texcoord, double alpha,
                    GLfloat *blendVertexts, GLfloat *blendTexcoord, GLuint blendTextureId);
    GLuint loadShader(GLenum shaderType, const char *shaderCode);
    GLuint createProgram(const char* vertexCode, const char* fragmentCode);
};


#endif //TEST_SHADERMANAGER_H

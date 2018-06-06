//
// 直方体のシェーダー定義
//
#ifndef TEST_CUBE_SHADER_H
#define TEST_CUBE_SHADER_H

/**
 * ポリゴン描画用のバーテックスシェーダ (頂点シェーダ) のソースコード
 */
static const char VERTEX_SHADER[] =
        "attribute vec4 position;"
                "attribute vec2 texcoord;"
                "varying vec2 texcoordVarying;"
                "void main() {"
                "gl_Position = position;"
                "texcoordVarying = texcoord;"
                "}";

/**
 * 色描画用のピクセル/フラグメントシェーダのソースコード
 */
static const char FRAGMENT_SHADER[] =
        "precision mediump float;"
                "uniform lowp vec4 rgba;"
                "uniform sampler2D texture;"
                "varying vec2 texcoordVarying;"
                "void main() {"
                "vec4 texcolor;"
                "texcolor = texture2D(texture, texcoordVarying);"
                "gl_FragColor = texcolor * rgba;"
                "}";

#endif //TEST_CUBE_SHADER_H

package app.opengl;

import android.content.Context;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import app.application.GBApplication;
import app.jni.JniBridge;
import lib.opengl.GLRenderBase;

/**
 * OpenGL用のRendererクラス
 *
 * [注意] OpenGLES2.0を使用する場合は、各関数の引数のGL10は使用しない！
 */
public class GLRenderer extends GLRenderBase {

    // ------------------------------
    // Constructor
    // ------------------------------
    public GLRenderer(Context context) {
        super(context);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        // Nativeへ処理を移す
        getJniBridge().onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        // Nativeへ処理を移す
        getJniBridge().onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        // Nativeへ処理を移す
        getJniBridge().onDrawFrame();
    }

    @Override
    protected boolean isNeedCheckFps() {
        return false;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * JniBridgeのインスタンスを返す
     */
    private JniBridge getJniBridge() {
        return ((GBApplication) context).getJniBridge();
    }

}

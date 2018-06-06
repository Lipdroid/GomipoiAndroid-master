package lib.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lib.fps.FpsManager;
import lib.image.ImageUtils;

/**
 *
 */
public class GLRenderBase  implements GLSurfaceView.Renderer {

    // ------------------------------
    // Define
    // ------------------------------
    protected final Context context;

    // ------------------------------
    // Member
    // ------------------------------
    private FpsManager mFpsManager;

    private int mSurfaceWidth;
    private int mSurfaceHeight;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GLRenderBase(Context context) {
        this.context = context;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (isNeedCheckFps()) {
            getFpsManager().checkFPS();
        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * キャプチャ画像を返す
     */
    /*packaged*/ final Bitmap getCapture(int scale) {
        final int width = mSurfaceWidth;
        final int height = mSurfaceHeight;
        final int pixels[] = new int[width * height];
        final IntBuffer buffer = IntBuffer.wrap(pixels);
        buffer.position(0);
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
        return ImageUtils.makeSurfaceCaptureImage(pixels, width, height, Bitmap.Config.ARGB_8888, scale);
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * FPSをチェックするかを返す
     */
    protected boolean isNeedCheckFps() {
        return false;
    }

    /**
     * FpsManagerのインスタンスを返す
     */
    private FpsManager getFpsManager() {
        if (mFpsManager == null) {
            mFpsManager = new FpsManager();
        }
        return mFpsManager;
    }

}

package lib.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Kurosuke on 16/08/02.
 */
public class GLSurfaceViewBase extends GLSurfaceView {

    // ------------------------------
    // Member
    // ------------------------------
    private GLRenderBase mRenderer;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GLSurfaceViewBase(Context context) {
        super(context);
    }

    public GLSurfaceViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        if (renderer instanceof GLRenderBase) {
            mRenderer = (GLRenderBase)renderer;
        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * キャプチャ画像を取得する
     */
    public final void getCapture(final int scale, final OnGLSurfaceViewBaseListener listener) {
        if (listener == null) {
            return;
        }

        if (mRenderer == null) {
            listener.onCaptured(null);
            return;
        }

        queueEvent(new Runnable() {
            @Override
            public void run() {

                removeCallbacks(this);
                listener.onCaptured(Bitmap.createBitmap(mRenderer.getCapture(scale)));
            }
        });

    }

    // ------------------------------
    // Callback
    // ------------------------------
    public interface OnGLSurfaceViewBaseListener {
        void onCaptured(Bitmap capture);
    }

}

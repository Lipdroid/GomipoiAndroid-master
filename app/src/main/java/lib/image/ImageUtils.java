package lib.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

/**
 *
 */
public class ImageUtils {

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * SurfaceViewのpixel配列からBitmapを作成し、返す
     */
    public static Bitmap makeSurfaceCaptureImage(@NonNull int[] pixels, int width, int height, Bitmap.Config config, int scale) {
        System.gc();
        final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                0, 0, 1, 0, 0,
                0, 1, 0, 0, 0,
                1, 0, 0, 0, 0,
                0, 0, 0, 1, 0
        })));

        final Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        final Canvas canvas = new Canvas(bitmap);

        final Matrix matrix = new Matrix();
        matrix.postScale(1.0f, -1.0f);
        matrix.postTranslate(0, height);
        canvas.concat(matrix);
        canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, false, paint);

        if (scale <= 1) {
            return bitmap;
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale, false);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        System.gc();

        return scaledBitmap;
    }

    /**
     * Viewの内容をBitmap化する
     */
    public static Bitmap getViewCapture(View view, int scale) {
        view.setDrawingCacheEnabled(true);
        Bitmap cache = view.getDrawingCache();

        if(cache == null){
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);

        if (scale <= 1) {
            return bitmap;
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale, false);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        System.gc();

        return scaledBitmap;
    }

    /**
     * 画像を合成する
     */
    public static Bitmap synthesizeImages(Bitmap baseImage, Bitmap addImage) {
        int width = baseImage.getWidth();
        int height = addImage.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(baseImage, 0, 0, (Paint)null);
        canvas.drawBitmap(addImage, 0, 0, (Paint) null);
        return newBitmap;
    }

    /**
     * 画像をImageViewにセットする
     * @param bmp [Bitmap]
     * @param imageView [ImageView]
     */
    public static boolean setImageToImageView(@NonNull Bitmap bmp, @NonNull ImageView imageView) {
        // 海外性のHTCだと落ちるらしいので、チェック
        if("htc_wwe".equals(Build.BRAND) && "HTC Desire".equals(Build.MODEL) ){
            System.runFinalization();
            System.gc();
        }

        // 前に画像があったら、その参照を切ってGC対象に
        // ＊ OS4.1からsetBackgroundDrawableがdeprecate指定に。。。
        imageView.setImageDrawable(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(null);
        } else {
            imageView.setBackgroundDrawable(null);
        }

        imageView.setImageBitmap(bmp);
        return true;
    }

    /**
     * ImageViewにセットされている画像を破棄する
     * @param imageView [ImageView]
     */
    public static boolean clearImageFromImageView(ImageView imageView) {
        if (imageView == null)
            return false;

        Drawable drawable = imageView.getDrawable();
        if (drawable == null)
            return false;

        Bitmap image = ((BitmapDrawable) drawable).getBitmap();
        if (image == null) {
            return false;
        }

        // BitmapStateがBitmapを参照しているため、ImageView::setImageDrawable(null)を呼ばないと、Recycle時にクラッシュする
        imageView.setImageDrawable(null);
        if (!image.isRecycled()) {
            image.recycle();
        }

        imageView.setImageDrawable(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(null);
        } else {
            imageView.setBackgroundDrawable(null);
        }

        System.runFinalization();
        System.gc();

        return true;
    }


}

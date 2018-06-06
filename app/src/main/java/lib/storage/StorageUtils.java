package lib.storage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class StorageUtils {

    public static StorageUtilsResponse saveImage(Context context, String dirPath, String fileName, Bitmap image) {
        // ストレージの権限チェック
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return new StorageUtilsResponse(StorageUtilsResponse.PERMISSION_ERROR, null);
        }

        final File rootDir = Environment.getExternalStorageDirectory();
        File dir = null;
        try {
            dir = new File(rootDir.getCanonicalPath() + "/" + dirPath);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    dir = null;
                }
            }
        } catch (IOException ignored) {
            dir = null;
        }

        if (dir == null) {
            return new StorageUtilsResponse(StorageUtilsResponse.ERROR, "Not found directory.");
        }

        FileOutputStream out = null;
        File captureFile = new File(dir, fileName);
        try {
            out = new FileOutputStream(captureFile);
            if (image != null) {
                image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            } else {
                throw new Exception("Image is null.");
            }
        } catch (Exception e) {
            captureFile = null;
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (captureFile == null) {
            return new StorageUtilsResponse(StorageUtilsResponse.ERROR, "failed save file.");
        }

        return new StorageUtilsResponse(captureFile);
    }

}

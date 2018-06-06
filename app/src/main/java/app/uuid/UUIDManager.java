package app.uuid;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import app.application.GBApplication;

/**
 */
public class UUIDManager {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String dDirectoryName = ".data/ALC";
    private static final String dFileName = "access_device_id.txt";

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * UUIDを返す
     */
    public static String getUUID(Context context, OnUUIDManagerListener listener) {
        if (listener == null) {
            return null;
        }

        // ストレージの権限チェック
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            listener.UUIDManager_OnPermissionError();
            return null;
        }

        // 端末に保存しているUUIDがあれば、そっちを返す
        final File rootDir = Environment.getExternalStorageDirectory();

		// 必要ならDirecotyを作成
		File dir = null;
		try {
			dir = new File(rootDir.getCanonicalPath() + "/" + dDirectoryName);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
                    dir = null;
                }
			}
		} catch (IOException ignored) {
            dir = null;
		}

        // Directoryが作成できなければ、エラー
        if (dir == null) {
            // Android 2.x上がりの端末でSDカードが未挿入の場合にここにくることを確認済み
            String uuid = ((GBApplication) context).getPreferenceManager().getUuid();
            if (uuid != null) {
                return uuid;
            }

            uuid = makeUUID();
            ((GBApplication) context).getPreferenceManager().setUuid(uuid);
            return uuid;
        }

		// 読み込み
        String readText = readFile(new File(dir, dFileName));
        if (readText != null) {
            return readText;
        }

        // 端末に保存しているUUIDがなければ、新規で作成する
        String uuid = makeUUID();

        // 新規作成したら、書き込み
        if (writeFile(new File(dir, dFileName), uuid)) {
            return uuid;
        }

        listener.UUIDManager_OnStorageError();
        return null;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * UUIDを作成する
     */
    private static String makeUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * ファイルを読み込んで内容のテキストを返す
     */
    private static String readFile(File file) {
        try {
            byte[] readBuffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(readBuffer);
            fileInputStream.close();
            return new String(readBuffer, "UTF-8").replaceAll("\\u0000", "");
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * ファイルにテキストを書き込む
     */
    private static boolean writeFile(File file, String text) {
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return false;
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(text.getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();

            return true;
        } catch (IOException ignored) {
        }

        return false;
    }

}

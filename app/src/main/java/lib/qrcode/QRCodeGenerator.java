package lib.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by kazuya on 2017/09/28.
 */
public class QRCodeGenerator {


    /**
     * instant function
     * @param contents original text
     * @return 400x400 qr code bitmap
     */
    @Nullable
    public static Bitmap createQRCode(@NonNull String contents) {
        Bitmap qr = null;
        QRCodeGenerator generator = new QRCodeGenerator();
        try {
            qr = generator.createQRCode(contents, 400);
        } catch (WriterException e) {
            /* no-op */
        }

        return qr;
    }

    @Nullable
    private Bitmap createQRCode(String contents,int size) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();

        Map<EncodeHintType, Object> encodeHint = new HashMap<>();
        encodeHint.put(EncodeHintType.CHARACTER_SET, "utf-8");

        // setting error level
        //L 7%
        //M 15%
        //Q 25%
        //H 30%
        encodeHint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix qrCodeData = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, encodeHint);

        // create bitmap
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.argb(255, 255, 255, 255));
        for (int x = 0; x < qrCodeData.getWidth(); x++) {
            for (int y = 0; y < qrCodeData.getHeight(); y++) {
                if (qrCodeData.get(x, y)) {
                    //0はBlack
                    bitmap.setPixel(x, y, Color.argb(255, 0, 0, 0));
                } else {
                    //-1はWhite
                    bitmap.setPixel(x, y, Color.argb(255, 255, 255, 255));
                }
            }
        }

        return bitmap;
    }
}

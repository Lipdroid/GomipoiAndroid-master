package app.data;

import android.net.Uri;

import java.io.Serializable;

/**
 *
 */
public class SharedCaptureData implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    public String text;
    public Uri image;

    // ------------------------------
    // Constructor
    // ------------------------------
    public SharedCaptureData(String text, Uri image) {
        this.image = image;
        this.text = text;
    }
}

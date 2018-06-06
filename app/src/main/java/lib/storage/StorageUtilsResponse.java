package lib.storage;

import java.io.File;
import java.io.Serializable;

/**
 *
 */
public class StorageUtilsResponse implements Serializable {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int OK = 1;
    public static final int ERROR = 2;
    public static final int PERMISSION_ERROR = 3;

    // ------------------------------
    // Member
    // ------------------------------
    public int status;
    public String errorMessage;
    public File outputFile;

    // ------------------------------
    // Constructor
    // ------------------------------
    public StorageUtilsResponse(File outputFile) {
        this.status = OK;
        this.outputFile = outputFile;
        this.errorMessage = null;
    }

    public StorageUtilsResponse(int status, String errorMessage) {
        this.status = status;
        this.outputFile = null;
        this.errorMessage = errorMessage;
    }

    public StorageUtilsResponse(int status, File outputFile, String errorMessage) {
        this.status = status;
        this.outputFile = outputFile;
        this.errorMessage = errorMessage;
    }
}

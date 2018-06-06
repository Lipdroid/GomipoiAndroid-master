package app.data;

import java.io.Serializable;

import app.define.GarbageId;

/**
 *
 */
public class Garbage implements Serializable {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int FOUND_TYPE_CLEANING = 0;
    public static final int FOUND_TYPE_SYNTHESIS = 1;

    // ------------------------------
    // Member
    // ------------------------------
    public GarbageId garbageId;
    public int found_type;

    // ------------------------------
    // Constructor
    // ------------------------------
    public Garbage(GarbageId garbageId, int found_type) {
        this.garbageId = garbageId;
        this.found_type = found_type;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final String getJson() {
        return "{" + "\"garbage_code\" : \"" + garbageId.getCode() + "\", \"found_type\" : " + String.valueOf(found_type) + "}";
    }

}

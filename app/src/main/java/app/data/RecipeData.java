package app.data;

import com.topmission.gomipoi.R;

import java.io.Serializable;

import app.define.GarbageId;
import app.define.ItemCode;

/**
 *
 */
public class RecipeData implements Serializable {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int MATERIAL_NO_1 = 0;
    public static final int MATERIAL_NO_2 = 1;
    public static final int MATERIAL_NO_3 = 2;

    // ------------------------------
    // Member
    // ------------------------------
    public String itemCode;
    public GarbageId garbage1;
    public boolean foundGarbage1;
    public GarbageId garbage2;
    public boolean foundGarbage2;
    public GarbageId garbage3;
    public boolean foundGarbage3;

    // ------------------------------
    // Constructor
    // ------------------------------
    public RecipeData(String itemCode,
                      String garbageCode1, boolean foundGarbage1,
                      String garbageCode2, boolean foundGarbage2,
                      String garbageCode3, boolean foundGarbage3) {
        this.itemCode = itemCode;
        this.garbage1 = GarbageId.valueOfWithCode(garbageCode1);
        this.foundGarbage1 = foundGarbage1;
        this.garbage2 = GarbageId.valueOfWithCode(garbageCode2);
        this.foundGarbage2 = foundGarbage2;
        this.garbage3 = GarbageId.valueOfWithCode(garbageCode3);
        this.foundGarbage3 = foundGarbage3;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * タイトル画像IDを返す
     */
    public final int getTitleResource() {
        if (itemCode == null) {
            return 0;
        }

        switch (ItemCode.getCode(itemCode)) {
            case BookOfSecrets1:
                return R.drawable.text_secret_book_1;

            case BookOfSecrets2:
                return R.drawable.text_secret_book_2;

            case BookOfSecrets3:
                return R.drawable.text_secret_book_3;

            case BookOfSecrets4:
                return R.drawable.text_secret_book_4;

            case BookOfSecrets5:
                return R.drawable.text_secret_book_5;

            case BookOfSecrets6:
                return R.drawable.text_secret_book_6;
        }

        return 0;
    }

    /**
     * 要素の画像IDを返す
     */
    public final int getMaterialResource(int materialNo) {
        GarbageId garbage = null;
        boolean isLocked = false;
        switch (materialNo) {
            case MATERIAL_NO_1: {
                garbage = garbage1;
                isLocked = !foundGarbage1;
                break;
            }
            case MATERIAL_NO_2: {
                garbage = garbage2;
                isLocked = !foundGarbage2;
                break;
            }
            case MATERIAL_NO_3: {
                garbage = garbage3;
                isLocked = !foundGarbage3;
                break;
            }
        }

        if (isLocked) {
            return R.drawable.recipe_lock;
        }

        if (garbage == null || garbage.equals(GarbageId.UNKNOWN)) {
            return 0;
        }

        return garbage.getSynthesisResourceId();
    }
}

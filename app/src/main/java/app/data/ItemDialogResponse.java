package app.data;

import java.io.Serializable;

/**
 *
 */
public class ItemDialogResponse implements Serializable {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int MOVE_TO_SHOP = 0;
    public static final int SHOW_USE_DIALOG = 1;
    public static final int SHOW_BUY_DIALOG = 2;
    public static final int SHOW_ALREADY_FULL_MAX_LIFE_DIALOG = 3;
    public static final int GET_NEWEST_INFO = 4;
    public static final int SHOW_DETAILS_DIALOG = 5;

    // ------------------------------
    // Member
    // ------------------------------
    public int status;
    public ListItemData listItemData;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ItemDialogResponse(int status, ListItemData listItemData) {
        this.listItemData = listItemData;
        this.status = status;
    }
}

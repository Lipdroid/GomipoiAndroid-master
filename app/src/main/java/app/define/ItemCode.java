package app.define;

import com.topmission.gomipoi.R;

import java.io.Serializable;

/**
 */
public enum ItemCode implements Serializable {
    UNKNOWN(""),
    BroomNormal("item_broom_default"),
    BroomSilver("item_broom_silver"),
    BroomGold("item_broom_gold"),
    DustboxSmall("item_garbage_can_default"),
    DustboxMiddle("item_garbage_can_large"),
    DustboxBig("item_garbage_can_huge"),
    BookOfSecrets1("item_garbage_recipe_single"),
    BookOfSecrets2("item_garbage_recipe_double"),
    BookOfSecrets3("item_garbage_recipe_triple"),
    Telephone("item_clear_garbage_can"),
    Seal("item_sticker"),
    RoomSecret1("item_place_secret_1"),
    RoomSecret2("item_place_secret_2"),
    RoomSecret3("item_place_secret_3"),
    RoomSecret4("item_place_secret_4"),
    RoomSecret5("item_place_secret_5"),
    RoomSecret6("item_place_secret_6"),
    PoikoRoomKey("item_place_key_2"),
    BookOfSecrets4("item_garbage_recipe_4"),
    BookOfSecrets5("item_garbage_recipe_5"),
    BookOfSecrets6("item_garbage_recipe_6"),
    DustboxSmallPoiko("item_garbage_can_default_2"),
    DustboxMiddlePoiko("item_garbage_can_large_2"),
    DustboxBigPoiko("item_garbage_can_huge_2"),

    GARDEN_KEY("item_25"),
    GARDEN_GARBAGE_CAN("item_26"),
    Z_DRINK("item_29"),
    DROP("item_30"),
    AUTO_BRROM("item_31"),
    BATTERY("item_32"),
    POIKO("item_33"),
    OTON("item_34"),
    KOTATSU("item_35"),
    GARBAGE_CAN_XL("item_36"),
    GARBAGE_CAN_XL_ROOM("item_37"),
    ;

    private String mValue;

    ItemCode(String mValue) {
        this.mValue = mValue;
    }

    public final String getValue() {
        return mValue;
    }

    public static ItemCode getCode(String value) {
        for (ItemCode code : values()) {
            if (code.getValue().equals(value)) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public static ItemCode getCode(ItemId id) {
        switch (id) {
            case BroomNormal:
                return BroomNormal;

            case BroomSilver:
                return BroomSilver;

            case BroomGold:
                return BroomGold;

            case DustboxSmall:
                return DustboxSmall;

            case DustboxMiddle:
                return DustboxMiddle;

            case DustboxBig:
                return DustboxBig;

            case BookOfSecrets1:
                return BookOfSecrets1;

            case BookOfSecrets2:
                return BookOfSecrets2;

            case BookOfSecrets3:
                return BookOfSecrets3;

            case Seal:
                return Seal;

            case Telephone:
                return Telephone;

            case RoomSecret1:
                return RoomSecret1;

            case RoomSecret2:
                return RoomSecret2;

            case RoomSecret3:
                return RoomSecret3;

            case RoomSecret4:
                return RoomSecret4;

            case RoomSecret5:
                return RoomSecret5;

            case RoomSecret6:
                return RoomSecret6;

            case PoikoRoomKey:
                return PoikoRoomKey;

            case BookOfSecrets4:
                return BookOfSecrets4;

            case BookOfSecrets5:
                return BookOfSecrets5;

            case BookOfSecrets6:
                return BookOfSecrets6;

            case DustboxMiddlePoiko:
                return DustboxMiddlePoiko;

            case DustboxBigPoiko:
                return DustboxBigPoiko;

            case Z_DRINK:
                return Z_DRINK;

            case DROP:
                return DROP;

            case AUTO_BRROM:
                return AUTO_BRROM;

            case BATTERY:
                return BATTERY;

            case GARDEN_KEY:
                return GARDEN_KEY;

            case GARDEN_GARBAGE_CAN:
                return GARDEN_GARBAGE_CAN;

            case POIKO:
                return POIKO;

            case OTON:
                return OTON;

            case KOTATSU:
                return KOTATSU;

            case GARBAGE_CAN_XL:
                return GARBAGE_CAN_XL;

            case GARBAGE_CAN_XL_ROOM:
                return GARBAGE_CAN_XL_ROOM;
        }

        return UNKNOWN;
    }
}

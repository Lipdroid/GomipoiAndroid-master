package app.define;

import java.io.Serializable;

/**
 */
public enum ItemSetCode implements Serializable {
    UNKNOWN(""),
    BroomSilver("item_set_broom_silver"),
    BroomGold("item_set_broom_gold"),
    DustboxMiddle("item_set_garbage_can_large"),
    DustboxBig("item_set_garbage_can_huge"),
    BookOfSecrets1("item_set_garbage_recipe_single"),
    BookOfSecrets2("item_set_garbage_recipe_double"),
    BookOfSecrets3("item_set_garbage_recipe_triple"),
    Telephone("item_set_clear_garbage_can"),
    Seal("item_set_sticker"),
    RoomSecret1("item_set_place_secret_1"),
    RoomSecret2("item_set_place_secret_2"),
    RoomSecret3("item_set_place_secret_3"),
    RoomSecret4("item_set_place_secret_4"),
    RoomSecret5("item_set_place_secret_5"),
    RoomSecret6("item_set_place_secret_6"),
    PoikoRoomKey("item_set_place_key_2"),
    BookOfSecrets4("item_set_garbage_recipe_4"),
    BookOfSecrets5("item_set_garbage_recipe_5"),
    BookOfSecrets6("item_set_garbage_recipe_6"),
    DustboxMiddlePoiko("item_set_garbage_can_large_2"),
    DustboxBigPoiko("item_set_garbage_can_huge_2"),

    Z_DRINK("item_set_22"),
    DROP("item_set_23"),
    AUTO_BRROM("item_set_24"),
    BATTERY("item_set_25"),
    OTON("item_set_26"),
    KOTATSU("item_set_27"),
    GARBAGE_CAN_XL("item_set_28"),
    GARBAGE_CAN_XL_ROOM("item_set_29"),
    ;

    private String mValue;

    ItemSetCode(String mValue) {
        this.mValue = mValue;
    }

    public final String getValue() {
        return mValue;
    }

    public static ItemSetCode getCode(String value) {
        for (ItemSetCode code : values()) {
            if (code.getValue().equals(value)) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public static ItemSetCode getCode(ItemId id) {
        switch (id) {
            case BroomSilver:
                return BroomSilver;

            case BroomGold:
                return BroomGold;

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

    public ItemCode getItemCode() {
        // TODO
        return ItemCode.UNKNOWN;
    }
}

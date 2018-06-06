package app.define;

import com.topmission.gomipoi.R;

import java.io.Serializable;

import app.jni.JniBridge;

public enum ItemId implements Serializable {
    UNKNOWN             (0, 0, "", 0, 0, 0),
    BroomNormal         (1, 0, "普通のほうき", R.drawable.card_broom_normal, R.drawable.parts_broom_normal, R.drawable.parts_text_broom_normal),
    BroomSilver         (2, 20, "銀のほうき", R.drawable.card_broom_silver, R.drawable.parts_broom_silver, R.drawable.parts_text_broom_silver),
    BroomGold           (3, 40, "黄金のほうき", R.drawable.card_broom_gold, R.drawable.parts_broom_gold, R.drawable.parts_text_broom_gold),
    DustboxSmall        (4, 0, "普通のゴミ箱", R.drawable.card_dustbox_small, R.drawable.parts_dustbox_small, R.drawable.parts_text_dustbox_small),
    DustboxMiddle       (5, 30, "大きなゴミ箱", R.drawable.card_dustbox_middle, R.drawable.parts_dustbox_middle, R.drawable.parts_text_dustbox_middle),
    DustboxBig          (6, 50, "巨大なゴミ箱", R.drawable.card_dustbox_big, R.drawable.parts_dustbox_big, R.drawable.parts_text_dustbox_big),
    BookOfSecrets1      (7, 30, "合成の秘伝書★1", R.drawable.card_bookofsecrets_1, R.drawable.parts_bookofsecrets_1, R.drawable.parts_text_bookofsecrets_1),
    BookOfSecrets2      (8, 30, "合成の秘伝書★2", R.drawable.card_bookofsecrets_2, R.drawable.parts_bookofsecrets_2, R.drawable.parts_text_bookofsecrets_2),
    BookOfSecrets3      (9, 30, "合成の秘伝書★3", R.drawable.card_bookofsecrets_3, R.drawable.parts_bookofsecrets_3, R.drawable.parts_text_bookofsecrets_3),
    Telephone           (10, 5, "業者に電話", R.drawable.card_telephone, R.drawable.parts_telephone, R.drawable.parts_text_telephone),
    Seal                (11, 10, "封印ステッカー", R.drawable.card_seal, R.drawable.parts_seal, R.drawable.parts_text_seal),
    PoikoRoomKey        (12, 200, "ポイ子の部屋のカギ", R.drawable.card_key_poiko, R.drawable.parts_key_poiko, R.drawable.parts_text_key_poiko),
    DustboxSmallPoiko   (13, 0, "普通のゴミ箱（ポイ子の部屋）", R.drawable.card_dustbox_small_poiko, R.drawable.parts_dustbox_small_poiko, R.drawable.parts_text_dustbox_small_poiko),
    DustboxMiddlePoiko  (14, 30, "大きなゴミ箱（ポイ子の部屋）", R.drawable.card_dustbox_middle_poiko, R.drawable.parts_dustbox_middle_poiko, R.drawable.parts_text_dustbox_middle_poiko),
    DustboxBigPoiko     (15, 50, "巨大なゴミ箱（ポイ子の部屋）", R.drawable.card_dustbox_big_poiko, R.drawable.parts_dustbox_big_poiko, R.drawable.parts_text_dustbox_big_poiko),
    BookOfSecrets4      (16, 30, "合成の秘伝書★4", R.drawable.card_bookofsecrets_4, R.drawable.parts_bookofsecrets_4, R.drawable.parts_text_bookofsecrets_4),
    BookOfSecrets5      (17, 30, "合成の秘伝書★5", R.drawable.card_bookofsecrets_5, R.drawable.parts_bookofsecrets_5, R.drawable.parts_text_bookofsecrets_5),
    BookOfSecrets6      (18, 30, "合成の秘伝書★6", R.drawable.card_bookofsecrets_6, R.drawable.parts_bookofsecrets_6, R.drawable.parts_text_bookofsecrets_6),
    RoomSecret1         (19, 30, "お茶の間のひみつ1", R.drawable.card_secret_ochanoma_1, R.drawable.parts_secret_ochanoma_1, R.drawable.parts_text_secret_ochanoma_1),
    RoomSecret2         (20, 30, "お茶の間のひみつ2", R.drawable.card_secret_ochanoma_2, R.drawable.parts_secret_ochanoma_2, R.drawable.parts_text_secret_ochanoma_2),
    RoomSecret3         (21, 30, "お茶の間のひみつ3", R.drawable.card_secret_ochanoma_3, R.drawable.parts_secret_ochanoma_3, R.drawable.parts_text_secret_ochanoma_3),
    RoomSecret4         (22, 30, "ポイ子の部屋のひみつ1", R.drawable.card_secret_poiko_1, R.drawable.parts_secret_poiko_1, R.drawable.parts_text_secret_poiko_1),
    RoomSecret5         (23, 30, "ポイ子の部屋のひみつ2", R.drawable.card_secret_poiko_2, R.drawable.parts_secret_poiko_2, R.drawable.parts_text_secret_poiko_2),
    RoomSecret6         (24, 30, "ポイ子の部屋のひみつ3", R.drawable.card_secret_poiko_3, R.drawable.parts_secret_poiko_3, R.drawable.parts_text_secret_poiko_3),

    GARDEN_KEY          (25, 0, "庭のカギ", R.drawable.card_garden_key, R.drawable.parts_garden_key, R.drawable.parts_text_garden_key),
    GARDEN_GARBAGE_CAN  (26, 0, "普通のゴミ箱（庭）", 0, 0, 0),
    Z_DRINK             (27, 50, "Zドリンク", R.drawable.card_zdrink, R.drawable.parts_zdrink, R.drawable.parts_text_zdrink),
    DROP                (28, 50, "変身ドロップ", R.drawable.card_drop, R.drawable.parts_drop, R.drawable.parts_text_drop),
    AUTO_BRROM          (29, 100, "電動ほうき", R.drawable.card_autobrrom, R.drawable.parts_autobrrom, R.drawable.parts_text_autobrrom),
    BATTERY             (30, 200, "電池", R.drawable.card_battery, R.drawable.parts_battery, R.drawable.parts_text_battery),
    POIKO               (31, 0, "ポイ子", R.drawable.card_poiko, R.drawable.parts_poiko, R.drawable.parts_text_poiko),
    OTON                (32, 200, "おとん", R.drawable.card_oton, R.drawable.parts_oton, R.drawable.parts_text_oton),
    KOTATSU             (33, 200, "こたつ", R.drawable.card_kotatsu, R.drawable.parts_kotatsu, R.drawable.parts_text_kotatsu),
    GARBAGE_CAN_XL         (34, 100, "ゴミ箱XL", R.drawable.card_garbage_can_xl, R.drawable.parts_garbage_can_xl, R.drawable.parts_text_garbage_can_xl),
    GARBAGE_CAN_XL_ROOM    (35, 100, "ゴミ箱XL（ポイ子の部屋）", R.drawable.card_garbage_can_xl_poiko, R.drawable.parts_garbage_can_xl, R.drawable.parts_text_garbage_can_xl_poiko),
    ;

    private int mValue;
    private int mPrice;
    private String mName;
    private int mCardResourceId;
    private int mPartsResourceId;
    private int mTextResourceId;

    ItemId(int mValue, int price, String name, int cardResourceId, int partsResourceId, int textResourceId) {
        this.mValue = mValue;
        this.mPrice = price;
        this.mName = name;
        mCardResourceId = cardResourceId;
        mPartsResourceId = partsResourceId;
        mTextResourceId = textResourceId;
    }

    public final int getValue() {
        return mValue;
    }

    public static ItemId valueOf(int value) {
        for (ItemId id : values()) {
            if (id.getValue() == value) {
                return id;
            }
        }
        return UNKNOWN;
    }

    public final int getCardResourceId() {
        return mCardResourceId;
    }

    public final int getPartsResourceId() {
        return mPartsResourceId;
    }

    public final int getPartsTextResourceId() {
        return mTextResourceId;
    }

    public final int getPrice() {
        return mPrice;
    }

    public final String getName() {
        return mName;
    }

    public final boolean isHaveCountHidden() {
        return this.equals(BroomNormal)
                || this.equals(BroomSilver)
                || this.equals(BroomGold)
                || this.equals(DustboxSmall)
                || this.equals(DustboxMiddle)
                || this.equals(DustboxBig)
                || this.equals(DustboxSmallPoiko)
                || this.equals(DustboxMiddlePoiko)
                || this.equals(DustboxBigPoiko);
    }

    public final boolean isVisibleUseCheck() {
        return this.equals(BroomNormal) || this.equals(BroomSilver) || this.equals(BroomGold)
                || this.equals(DustboxSmall) || this.equals(DustboxMiddle) || this.equals(DustboxBig)
                || this.equals(DustboxSmallPoiko) || this.equals(DustboxMiddlePoiko) || this.equals(DustboxBigPoiko)
                || this.equals(POIKO) || this.equals(OTON) || this.equals(KOTATSU);
    }

    public final BroomType getBroomType() {
        switch (this) {
            case BroomNormal: {
                return BroomType.Normal;
            }

            case BroomSilver: {
                return BroomType.Silver;
            }

            case BroomGold: {
                return BroomType.Gold;
            }
        }
        return BroomType.UNKNOWN;
    }

    public final GarbageCanType getGarbageCanType() {
        switch (this) {
            case DustboxSmall:
            case DustboxSmallPoiko: {
                return GarbageCanType.Normal;
            }

            case DustboxMiddle:
            case DustboxMiddlePoiko: {
                return GarbageCanType.Big;
            }

            case DustboxBig:
            case DustboxBigPoiko: {
                return GarbageCanType.Huge;
            }

            case GARBAGE_CAN_XL:
            case GARBAGE_CAN_XL_ROOM:
                return GarbageCanType.XL;
        }
        return GarbageCanType.UNKNOWN;
    }

    public final StageType getGarbageCanStageType() {
        switch (this) {
            case DustboxSmall:
            case DustboxMiddle:
            case DustboxBig:
            case GARBAGE_CAN_XL:{
                return StageType.DefaultRoom;
            }

            case DustboxSmallPoiko:
            case DustboxMiddlePoiko:
            case DustboxBigPoiko:
            case GARBAGE_CAN_XL_ROOM:{
                return StageType.PoikoRoom;
            }

            case GARDEN_GARBAGE_CAN: {
                return StageType.Garden;
            }

            default:
                return StageType.DefaultRoom;
        }
    }

    public final boolean isNeedCheck() {
        if (!isVisibleUseCheck()) {
            return false;
        }

        switch (this) {
            case BroomNormal: {
                return JniBridge.nativeGetCurrentBroomType() == BroomType.Normal.getValue();
            }

            case BroomSilver: {
                return JniBridge.nativeGetCurrentBroomType() == BroomType.Silver.getValue();
            }

            case BroomGold: {
                return JniBridge.nativeGetCurrentBroomType() == BroomType.Gold.getValue();
            }

            case DustboxSmall: {
                return JniBridge.nativeGetCurrentGarbageCanType() == GarbageCanType.Normal.getValue();
            }

            case DustboxMiddle:
            case DustboxMiddlePoiko: {
                return JniBridge.nativeGetCurrentGarbageCanType() == GarbageCanType.Big.getValue();
            }

            case DustboxBig:
            case DustboxBigPoiko: {
                return JniBridge.nativeGetCurrentGarbageCanType() == GarbageCanType.Huge.getValue();
            }

            case GARBAGE_CAN_XL:
            case GARBAGE_CAN_XL_ROOM: {
                return JniBridge.nativeGetCurrentGarbageCanType() == GarbageCanType.XL.getValue();
            }

            case POIKO: {
                return JniBridge.nativeIsItemUsing(ItemCode.POIKO.getValue());
            }
            case OTON: {
                return JniBridge.nativeIsItemUsing(ItemCode.OTON.getValue());
            }
            case KOTATSU: {
                return JniBridge.nativeIsItemUsing(ItemCode.KOTATSU.getValue());
            }

        }
        return false;
    }

}

package app.data;

import android.content.Context;
import android.support.annotation.DrawableRes;

import com.topmission.gomipoi.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.define.BroomType;
import app.define.GarbageCanType;
import app.define.ItemCode;
import app.define.ItemId;
import app.define.ItemSetCode;
import app.define.StageType;
import app.jni.JniBridge;
import app.number.NumberUtils;

/**
 *
 */
public class ListItemData implements Serializable {

    // ------------------------------
    // Define
    // ------------------------------

    // ------------------------------
    // Member
    // ------------------------------
    public ItemId id;
    public ItemCode itemCode;
    public ItemSetCode itemSetCode;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ListItemData(ItemId id) {
        this.id = id;
        this.itemCode = ItemCode.getCode(id);
        this.itemSetCode = ItemSetCode.getCode(id);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final @DrawableRes int getCardResourceId() {
        return id.getCardResourceId();
    }

    public final int getPartsResourceId() {
        return id.getPartsResourceId();
    }

    public final int getPartsTextResourceId() {
        return id.getPartsTextResourceId();
    }

    public final String getHaveCountText() {
        return NumberUtils.getNumberFormatText(JniBridge.nativeGetItemOwnCount(itemCode.getValue()));
    }

    public final @DrawableRes int getImageForSecretPage() {
        switch (id) {
            case RoomSecret1:
                return R.drawable.secret_ochanoma_1;

            case RoomSecret2:
                return R.drawable.secret_ochanoma_2;

            case RoomSecret3:
                return R.drawable.secret_ochanoma_3;

            case RoomSecret4:
                return R.drawable.secret_poiko_1;

            case RoomSecret5:
                return R.drawable.secret_poiko_2;

            case RoomSecret6:
                return R.drawable.secret_poiko_3;

            default:
                return 0;
        }
    }

    public final int getHaveCount() {
        return JniBridge.nativeGetItemOwnCount(itemCode.getValue());
    }

    public final boolean isUsableItem(Context context) {
        return JniBridge.nativeIsItemUsing(itemCode.getValue());
    }

    public final boolean isBuyAndUseItem() {
        return (id.equals(ItemId.BroomSilver)
                || id.equals(ItemId.BroomGold)
                || id.equals(ItemId.DustboxMiddle)
                || id.equals(ItemId.DustboxBig)
                || id.equals(ItemId.GARBAGE_CAN_XL)
                || id.equals(ItemId.DustboxMiddlePoiko)
                || id.equals(ItemId.DustboxBigPoiko)
                || id.equals(ItemId.GARBAGE_CAN_XL_ROOM)
                || id.equals(ItemId.AUTO_BRROM)
        );
    }

    /**
     * 3桁区切りの価格テキストを返す
     */
    public final String getPriceText() {
        return NumberUtils.getNumberFormatText(id.getPrice());
    }

    public final int getPrice() {
        return id.getPrice();
    }

    public final boolean isVisibleUseCheck() {
        return id.isVisibleUseCheck();
    }

    public final boolean isNeedCheck() {
        return id.isNeedCheck();
    }

    public final BroomType getBroomType() {
        return id.getBroomType();
    }

    public final boolean isShowAttentionMessage() {
        return id.isVisibleUseCheck();
    }

    public final GarbageCanType getGarbageCanType() {
        return id.getGarbageCanType();
    }

    public final StageType getGarbageCanStageType() {
        return id.getGarbageCanStageType();
    }

    public final boolean isItemCanOwnMultiple() {
        return id.equals(ItemId.Telephone) || id.equals(ItemId.Seal) || id.equals(ItemId.Z_DRINK) || id.equals(ItemId.BATTERY) || id.equals(ItemId.DROP);
    }

    public final boolean alreadyOwnItemWithNonConsumable() {
        switch (id) {
            case BookOfSecrets1:
            case BookOfSecrets2:
            case BookOfSecrets3:
            case BookOfSecrets4:
            case BookOfSecrets5:
            case BookOfSecrets6:
            case RoomSecret1:
            case RoomSecret2:
            case RoomSecret3:
            case RoomSecret4:
            case RoomSecret5:
            case RoomSecret6:
            case PoikoRoomKey:
            case AUTO_BRROM:
            case OTON:
            case KOTATSU:
            case GARDEN_KEY:
                return JniBridge.nativeGetItemOwnCount(itemCode.getValue()) > 0;
            case GARBAGE_CAN_XL:
            case GARBAGE_CAN_XL_ROOM:
                return JniBridge.nativeIsItemUsing(itemCode.getValue());

            case DustboxBig:
                return JniBridge.nativeIsItemUsing(itemCode.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.GARBAGE_CAN_XL.getValue());

            case DustboxMiddle:
                return JniBridge.nativeIsItemUsing(itemCode.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.DustboxBig.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.GARBAGE_CAN_XL.getValue());

            case DustboxBigPoiko:
                return JniBridge.nativeIsItemUsing(itemCode.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.GARBAGE_CAN_XL_ROOM.getValue());

            case DustboxMiddlePoiko:
                return JniBridge.nativeIsItemUsing(itemCode.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.DustboxBigPoiko.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.GARBAGE_CAN_XL_ROOM.getValue());

            default:
                return true;
        }
    }

    public final boolean isSecretOfRoomItem() {
        switch (id) {
            case RoomSecret1:
            case RoomSecret2:
            case RoomSecret3:
            case RoomSecret4:
            case RoomSecret5:
            case RoomSecret6:
                return true;

            default:
                return false;
        }
    }

    public static List<ListItemData> getBuyList() {
        List<ListItemData> item = new ArrayList<>();
        item.add(new ListItemData(ItemId.Telephone));
        item.add(new ListItemData(ItemId.Seal));
        item.add(new ListItemData(ItemId.AUTO_BRROM));

        item.add(new ListItemData(ItemId.DustboxMiddle));
        item.add(new ListItemData(ItemId.DustboxBig));
        item.add(new ListItemData(ItemId.GARBAGE_CAN_XL));

        item.add(new ListItemData(ItemId.DustboxMiddlePoiko));
        item.add(new ListItemData(ItemId.DustboxBigPoiko));
        item.add(new ListItemData(ItemId.GARBAGE_CAN_XL_ROOM));

        item.add(new ListItemData(ItemId.PoikoRoomKey));
        item.add(new ListItemData(ItemId.BroomSilver));
        item.add(new ListItemData(ItemId.BroomGold));

        item.add(new ListItemData(ItemId.BATTERY));
        item.add(new ListItemData(ItemId.Z_DRINK));
        item.add(new ListItemData(ItemId.DROP));

        item.add(new ListItemData(ItemId.GARDEN_KEY));

        item.add(new ListItemData(ItemId.BookOfSecrets1));
        item.add(new ListItemData(ItemId.BookOfSecrets2));
        item.add(new ListItemData(ItemId.BookOfSecrets3));

        item.add(new ListItemData(ItemId.BookOfSecrets4));
        item.add(new ListItemData(ItemId.BookOfSecrets5));
        item.add(new ListItemData(ItemId.BookOfSecrets6));

        item.add(new ListItemData(ItemId.RoomSecret1));
        item.add(new ListItemData(ItemId.RoomSecret2));
        item.add(new ListItemData(ItemId.RoomSecret3));

        item.add(new ListItemData(ItemId.RoomSecret4));
        item.add(new ListItemData(ItemId.RoomSecret5));
        item.add(new ListItemData(ItemId.RoomSecret6));

        item.add(new ListItemData(ItemId.OTON));
        item.add(new ListItemData(ItemId.KOTATSU));

        return item;
    }

    public static List<ListItemData> getUseList() {
        List<ListItemData> item = new ArrayList<>();

        item.add(new ListItemData(ItemId.Telephone));
        item.add(new ListItemData(ItemId.Seal));
        item.add(new ListItemData(ItemId.Z_DRINK));

        item.add(new ListItemData(ItemId.POIKO));
        item.add(new ListItemData(ItemId.OTON));
        item.add(new ListItemData(ItemId.KOTATSU));

        item.add(new ListItemData(ItemId.BATTERY));
        item.add(new ListItemData(ItemId.DROP));

        item.add(new ListItemData(ItemId.RoomSecret1));
        item.add(new ListItemData(ItemId.RoomSecret2));
        item.add(new ListItemData(ItemId.RoomSecret3));

        item.add(new ListItemData(ItemId.RoomSecret4));
        item.add(new ListItemData(ItemId.RoomSecret5));
        item.add(new ListItemData(ItemId.RoomSecret6));

        return item;
    }
}

package app.data;

import java.io.Serializable;

import app.billing.PurchaseManager;
import app.number.NumberUtils;

/**
 * ショップのアイテムクラス
 */
public class ShopItem implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    public PurchaseManager.StoreItem storeItem;
    public int price;
    public int gemCount;
    public int skuId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ShopItem(PurchaseManager.StoreItem storeItem, int price, int gemCount, int skuId) {
        this.storeItem = storeItem;
        this.price = price;
        this.gemCount = gemCount;
        this.skuId = skuId;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * 個数のテキストを返す
     */
    public final String getGemCountText() {
        return NumberUtils.getNumberFormatText(gemCount) + "個";
    }

}

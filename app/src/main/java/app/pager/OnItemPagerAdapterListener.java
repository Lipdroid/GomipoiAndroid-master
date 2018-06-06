package app.pager;

import app.data.ListItemData;

/**
 *
 */
public interface OnItemPagerAdapterListener {

    // ------------------------------
    // Callbacks
    // ------------------------------
    void OnItemPagerAdapterListener_onClickedUseItem(ListItemData item);
    void OnItemPagerAdapterListener_onClickedBuyItem(ListItemData item);
    void OnItemPagerAdapterListener_onShowAlreadyFullMaxLife();

}

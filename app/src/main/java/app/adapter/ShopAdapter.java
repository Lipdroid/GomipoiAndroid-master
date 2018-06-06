package app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.topmission.gomipoi.R;

import java.util.List;

import app.application.GBApplication;
import app.data.ShopItem;
import app.number.NumberUtils;
import app.view.OutlineTextView;
import lib.adapter.AdapterBase;
import lib.adapter.OnAdapterListener;

/**
 * ショップ画面のリストのセルクラス
 */
public class ShopAdapter extends AdapterBase<ShopItem> {

    public ShopAdapter(Context context, List<ShopItem> objects, OnAdapterListener listener) {
        super(context, objects, listener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.adapter_shop, parent, false);
            holder = new ViewHolder();

            holder.imageViewCell = (ImageView) convertView.findViewById(R.id.imageViewCell);
            holder.outlineTextViewPrice = (OutlineTextView) convertView.findViewById(R.id.outlineTextViewPrice);
            if (holder.outlineTextViewPrice != null) {
                holder.outlineTextViewPrice.setOutlineTextAligh(OutlineTextView.ALIGN_RIGHT);
            }

            ((GBApplication)getContext()).getResizeManager().resize(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShopItem item = getItem(position);

        if (holder.imageViewCell != null) {
            holder.imageViewCell.setImageLevel(position);
        }

        if (holder.outlineTextViewPrice != null) {
            holder.outlineTextViewPrice.setText(NumberUtils.getNumberFormatText(item.price));
        }

        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageViewCell;
        public OutlineTextView outlineTextViewPrice;
    }
}

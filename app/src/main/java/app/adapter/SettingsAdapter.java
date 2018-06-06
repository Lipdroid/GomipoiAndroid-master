package app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.util.List;

import app.data.SettingsItem;
import app.define.SettingsType;
import common.adapter.GBAdapterBase;
import lib.adapter.OnAdapterListener;

/**
 * 設定画面のリストのセルクラス
 */
public class SettingsAdapter extends GBAdapterBase<SettingsItem> implements CompoundButton.OnCheckedChangeListener {

    private boolean mIsIgnore = false;

    public SettingsAdapter(Context context, List<SettingsItem> objects, OnAdapterListener listener) {
        super(context, objects, listener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.adapter_settings, null);
            holder = new ViewHolder();
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.textViewDescript = (TextView) convertView.findViewById(R.id.textViewDescript);
            holder.switchView = (Switch) convertView.findViewById(R.id.switchView);
            if (holder.switchView != null) {
                holder.switchView.setOnCheckedChangeListener(this);
            }
            convertView.setTag(holder);

            SettingsItem settingsItem = getItem(position);
            if (settingsItem != null && settingsItem.isBGM()) {
                getApp().getBgmManager().stopAndRelease();
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SettingsItem item = getItem(position);
        if (item == null) {
            return convertView;
        }

        if (holder.textViewName != null) {
            holder.textViewName.setText(item.getTitle());
        }

        if (holder.switchView != null) {
            if (!item.getType().equals(SettingsType.WITH_SWITCH)) {
                holder.switchView.setVisibility(View.GONE);
                holder.switchView.setTag(null);
            } else {
                holder.switchView.setVisibility(View.VISIBLE);
                holder.switchView.setTag(item);
                if (item.isBGM()) {
                    holder.switchView.setChecked(getApp().getPreferenceManager().isSoundEnabled());
                } else {
                    holder.switchView.setChecked(getApp().getPreferenceManager().isSeEnabled());
                }
            }
        }

        if (holder.textViewDescript != null) {
            if (!item.getType().equals(SettingsType.WITH_DESCRIPT)) {
                holder.textViewDescript.setVisibility(View.GONE);
            } else {
                holder.textViewDescript.setVisibility(View.VISIBLE);
                holder.textViewDescript.setText(item.getDescript());
            }
        }

        return convertView;
    }

    // CompoundButton.OnCheckedChangeListener
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getTag() == null || !(buttonView.getTag() instanceof SettingsItem)) {
            return;
        }

        SettingsItem item = (SettingsItem)buttonView.getTag();
        if (item.isBGM()) {
            getApp().getPreferenceManager().setSoundEnabled(isChecked);
            if (isChecked) {
                getApp().getBgmManager().playAndRetain();
            } else {
                getApp().getBgmManager().stopAndDecreaseCount();
            }
        } else {
            getApp().getPreferenceManager().setSeEnabled(isChecked);
        }

    }

    private static class ViewHolder {
        public TextView textViewName;
        public TextView textViewDescript;
        private Switch switchView;
    }
}

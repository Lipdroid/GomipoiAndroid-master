package app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.topmission.gomipoi.R;

import java.util.List;

import app.animation.button.ButtonAnimationManager;
import app.application.GBApplication;
import app.data.http.FriendsForPresentRequestResponse;
import app.define.FriendActionCode;
import lib.adapter.AdapterBase;
import lib.adapter.OnAdapterListener;

/**
 * おねだりリストのセルクラス
 */
public class FriendGimmeAdapter extends AdapterBase<FriendsForPresentRequestResponse> {

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendGimmeAdapter(Context context, List<FriendsForPresentRequestResponse> objects, OnAdapterListener listener) {
        super(context, objects, listener);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.adapter_friend_gimme, null);
            holder = new ViewHolder();

            holder.imageViewGimme = (ImageView)convertView.findViewById(R.id.imageViewGimme);
            if (holder.imageViewGimme != null) {
                new ButtonAnimationManager(holder.imageViewGimme, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dAdapterListener != null) {
                            dAdapterListener.onEvent(
                                    FriendActionCode.GIMME.getValue(),
                                    v.getTag());
                        }
                    }
                });
            }
            holder.textViewGimme = (TextView)convertView.findViewById(R.id.textViewGimme);
            holder.textViewName = (TextView)convertView.findViewById(R.id.textViewName);

            ((GBApplication)getContext()).getResizeManager().resize(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FriendsForPresentRequestResponse item = getItem(position);
        if (item != null) {
            if (holder.textViewName != null) {
                holder.textViewName.setText(item.nickname);
            }

            if (holder.imageViewGimme != null) {
                holder.imageViewGimme.setEnabled(item.canPresentRequest());
                holder.imageViewGimme.setTag(item);
            }
        }

        return convertView;
    }

    // ------------------------------
    // InnerClass
    // ------------------------------
    private static class ViewHolder {

        // ------------------------------
        // Member
        // ------------------------------
        public ImageView imageViewGimme;
        public TextView textViewGimme;
        public TextView textViewName;

    }
}

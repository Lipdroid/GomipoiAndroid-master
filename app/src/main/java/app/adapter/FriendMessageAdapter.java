package app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.util.List;

import app.animation.button.ButtonAnimationManager;
import app.application.GBApplication;
import app.data.http.FriendMessagesResponse;
import app.data.http.MessagesResponse;
import app.data.http.SystemMessagesResponse;
import app.define.FriendActionCode;
import lib.adapter.AdapterBase;
import lib.adapter.OnAdapterListener;

/**
 * メッセージダイアログのリストのセルクラス
 */
public class FriendMessageAdapter extends AdapterBase<MessagesResponse> implements View.OnClickListener {

    // ------------------------------
    // Member
    // ------------------------------
    private String mServerDate;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendMessageAdapter(Context context, String serverDate, List<MessagesResponse> objects, OnAdapterListener listener) {
        super(context, objects, listener);
        mServerDate = serverDate;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.adapter_friend_message, parent, false);
            holder = new ViewHolder();

            holder.imageViewInvite = (Button)convertView.findViewById(R.id.imageViewInvite);
            if (holder.imageViewInvite != null) {
                new ButtonAnimationManager(holder.imageViewInvite, this);
            }

            holder.textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            if (holder.textViewName != null) {
                float minHeight = ((GBApplication)getContext()).getResizeManager().calcValue(92.0f);
                holder.textViewName.setMinimumHeight((int)minHeight);
            }

            holder.textViewDate = (TextView)convertView.findViewById(R.id.textViewDate);

            ((GBApplication)getContext()).getResizeManager().resize(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MessagesResponse item = getItem(position);

        if (item != null) {
            if (item instanceof FriendMessagesResponse) {
                FriendMessagesResponse friendItem = (FriendMessagesResponse) item;

                if (holder.textViewName != null) {
                    holder.textViewName.setText(friendItem.getMessage());
                }

                if (holder.textViewDate != null) {
                    holder.textViewDate.setText(friendItem.getDateText(mServerDate));
                }

                if (holder.imageViewInvite != null) {
                    holder.imageViewInvite.setVisibility(friendItem.isInvite() ? View.VISIBLE : View.INVISIBLE);
                    holder.imageViewInvite.setTag(friendItem);
                    holder.imageViewInvite.setText(R.string.friend_message_install);
                }
            }
            else if (item instanceof SystemMessagesResponse) {
                SystemMessagesResponse systemItem = (SystemMessagesResponse) item;

                if (holder.textViewName != null) {
                    holder.textViewName.setText(systemItem.getMessage(getContext().getResources()));
                }

                if (holder.textViewDate != null) {
                    holder.textViewDate.setVisibility(View.VISIBLE);
                    holder.textViewDate.setText(systemItem.getDateText(mServerDate));
                }

                if (holder.imageViewInvite != null) {
                    holder.imageViewInvite.setVisibility(systemItem.isAddFriendBonus() ? View.VISIBLE : View.INVISIBLE);
                    holder.imageViewInvite.setTag(systemItem);
                    holder.imageViewInvite.setText(R.string.friend_message_accept);
                }
            }
        }

        return convertView;
    }

    @Override
    @Deprecated
    public void refresh(List<MessagesResponse> dataList) {
        super.refresh(dataList);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void refresh(String serverDate, List<MessagesResponse> dataList) {
        mServerDate = serverDate;
        super.refresh(dataList);
    }

    // ------------------------------
    // OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        Object tag = v.getTag();

        if (tag instanceof FriendMessagesResponse) {
            if (dAdapterListener != null) {
                dAdapterListener.onEvent(FriendActionCode.INVITE.getValue(), v.getTag());
            }
        }
        else if (tag instanceof SystemMessagesResponse) {
            if (dAdapterListener != null) {
                dAdapterListener.onEvent(FriendActionCode.RECEIVE.getValue(), v.getTag());
            }
        }

        if (tag instanceof FriendMessagesResponse) {
            if (dAdapterListener != null) {
                dAdapterListener.onEvent(FriendActionCode.DELETE.getValue(), v.getTag());
            }
        }
    }

    // ------------------------------
    // InnerClass
    // ------------------------------
    private static class ViewHolder {

        // ------------------------------
        // Member
        // ------------------------------
        public Button imageViewInvite;
        public TextView textViewName;
        public TextView textViewDate;

    }
}

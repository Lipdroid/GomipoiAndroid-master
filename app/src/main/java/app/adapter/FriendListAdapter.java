package app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.util.List;

import app.animation.button.ButtonAnimationManager;
import app.application.GBApplication;
import app.data.http.GomipoiFriendsResponse;
import app.define.FriendActionCode;
import lib.adapter.AdapterBase;
import lib.adapter.OnAdapterListener;

/**
 * フレンドリスト画面のリストのセルクラス
 */
public class FriendListAdapter extends AdapterBase<GomipoiFriendsResponse> {

    // ------------------------------
    // Constants
    // ------------------------------
    private static final int VIEW_TYPE_FRIEND = 0;
    private static final int VIEW_TYPE_SECTION = 1;

    // ------------------------------
    // Fields
    // ------------------------------
    private List<GomipoiFriendsResponse> friendList;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendListAdapter(Context context, List<GomipoiFriendsResponse> objects, OnAdapterListener listener) {
        super(context, objects, listener);

        friendList = objects;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getCount() {
        boolean hasSection = false;
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).isNeedShowInvite()) {
                hasSection = true;
                break;
            }
        }
        return friendList.size() + (hasSection ? 1 : 0);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int sectionPosition = -1;
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).isNeedShowInvite()) {
                sectionPosition = i;
                break;
            }
        }

        return position == sectionPosition ? VIEW_TYPE_SECTION : VIEW_TYPE_FRIEND;
    }

    @Override
    public GomipoiFriendsResponse getItem(int position) {
        int sectionPosition = -1;
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).isNeedShowInvite()) {
                sectionPosition = i;
                break;
            }
        }

        if (sectionPosition == -1 || position < sectionPosition)
            return super.getItem(position);
        else {
            return super.getItem(position - 1);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        if (type == VIEW_TYPE_SECTION) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.adapter_friend_list_section, parent, false);
            }

            return convertView;
        }
        else {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.adapter_friend_list, parent, false);
                holder = new ViewHolder();

                holder.buttonInvite = (Button)convertView.findViewById(R.id.imageViewInvite);
                if (holder.buttonInvite != null) {
                    new ButtonAnimationManager(holder.buttonInvite, v -> {
                        if (dAdapterListener != null) {
                            dAdapterListener.onEvent(FriendActionCode.INVITE.getValue(), v.getTag());
                        }
                    });
                }

                holder.buttonDeleteFriend = (Button) convertView.findViewById(R.id.btn_delete_friend);
                if (holder.buttonDeleteFriend != null)
                    new ButtonAnimationManager(holder.buttonDeleteFriend, v -> {
                        if (dAdapterListener != null) {
                            dAdapterListener.onEvent(FriendActionCode.DELETE.getValue(), v.getTag());
                        }
                    });

                holder.textViewLife = (TextView)convertView.findViewById(R.id.textViewLife);
                holder.textViewLifeTitle = (TextView) convertView.findViewById(R.id.textViewLifeTitle);
                holder.imageViewLife = (ImageView)convertView.findViewById(R.id.imageViewLife);
                holder.textViewName = (TextView)convertView.findViewById(R.id.textViewName);
                holder.textViewLevel = (TextView)convertView.findViewById(R.id.textViewLevel);

                ((GBApplication)getContext()).getResizeManager().resize(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GomipoiFriendsResponse item = getItem(position);
            if (item != null) {
                if (holder.textViewName != null) {
                    holder.textViewName.setText(item.nickname);
                }

                if (holder.buttonInvite != null) {
                    boolean canInvite = item.canInvite();
                    holder.buttonInvite.setVisibility(item.isNeedShowInvite() ? View.VISIBLE : View.GONE);
                    holder.buttonInvite.setEnabled(canInvite);
                    holder.buttonInvite.setTag(item);
                    holder.buttonInvite.setAlpha(canInvite ? 1.0f : 0.5f);
                }

                if (holder.buttonDeleteFriend != null) {
                    holder.buttonDeleteFriend.setVisibility(item.isNeedShowInvite() ? View.GONE : View.VISIBLE);
                    holder.buttonDeleteFriend.setTag(item);
                }

                if (holder.textViewLevel != null) {
                    holder.textViewLevel.setVisibility(item.isNeedShowInvite() ? View.INVISIBLE : View.VISIBLE);
                    holder.textViewLevel.setText(item.getLevelText(getContext().getResources()));
                }

                if (holder.textViewLife != null) {
                    holder.textViewLife.setVisibility(item.isNeedShowInvite() ? View.INVISIBLE : View.VISIBLE);
                    holder.textViewLife.setText(item.getTotalPointText());
                }

                if (holder.textViewLifeTitle != null) {
                    holder.textViewLifeTitle.setVisibility(item.isNeedShowInvite() ? View.INVISIBLE : View.VISIBLE);
                }

                if (holder.imageViewLife != null) {
                    holder.imageViewLife.setVisibility(item.isNeedShowInvite() ? View.INVISIBLE : View.VISIBLE);
                }
            }

            return convertView;
        }
    }

    // ------------------------------
    // InnerClass
    // ------------------------------
    private static class ViewHolder {
        public Button buttonInvite;
        public TextView textViewLife;
        public TextView textViewLifeTitle;
        public ImageView imageViewLife;
        public TextView textViewName;
        public TextView textViewLevel;
        // Same function.
        public Button buttonDeleteFriend; // Visible in !isNeedShowInvite
    }
}

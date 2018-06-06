package app.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.adapter.FriendListAdapter;
import app.animation.button.ButtonAnimationManager;
import app.application.GBApplication;
import app.data.http.GomipoiFriendsResponse;
import app.define.ChangeActivityCode;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.define.FriendActionCode;
import app.define.SeData;
import app.interfaces.FriendListRankListener;
import common.fragment.GBFragmentBase;
import lib.fragment.OnFragmentListener;
import lib.log.DebugLog;

/**
 * フレンドリスト画面
 */
public class FriendListFragment extends GBFragmentBase {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private Button buttonMessage;
    private Button buttonInvite;
    private View layoutSns;
    private ListView listView;
    private TextView textViewCode;
    private TextView textViewFriendCount;
    private TextView textViewBadge;

    private View backButton;
    private View readQRButton;
    private View showQRButton;
    private Button friendRankButton;

    private int mFriendBadge = -1;
    private int mSystemBadge = -1;

    private String friendNickName;
    private boolean isDialogShowing;

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        backButton = view.findViewById(R.id.back_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            backButton.setBackground(null);
        } else {
            backButton.setBackgroundDrawable(null);
        }
        if (backButton != null) {
            new ButtonAnimationManager(backButton, v -> {
                GBApplication app = getApp();
                if (app != null) {
                    app.getSeManager().playSe(SeData.YES);
                    // if (getFragmentManager() != null)
                    //     getFragmentManager().popBackStack();
                    FriendListRankListener mListener = (FriendListRankListener) getActivity();
                    mListener.replaceFragment(null);
                }
            });
        }

        buttonMessage = (Button) view.findViewById(R.id.buttonMessage);
        if (buttonMessage != null) {
            new ButtonAnimationManager(buttonMessage, v -> {
                if (isLocked()) {
                    return;
                }
                lockEvent();

                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                onShowDialog(DialogCode.FriendMessage, null);
            });
        }

        buttonInvite = (Button) view.findViewById(R.id.buttonInvite);
        if (buttonInvite != null) {
            new ButtonAnimationManager(buttonInvite, v -> {
                if (isLocked()) {
                    return;
                }
                lockEvent();

                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                onShowDialog(DialogCode.SearchFriend, null);
                unlockEvent();
            });
        }

        friendRankButton = view.findViewById(R.id.friendRank_button);
        if (friendRankButton != null) {
            new ButtonAnimationManager(friendRankButton, v -> {
                FriendListRankListener mListener = (FriendListRankListener) getActivity();
                if (mListener != null) {
                    FriendListRankFragment mFragment = FriendListRankFragment.newInstance();
                    mListener.replaceFragment(mFragment);
                }
            });
        }

        layoutSns = view.findViewById(R.id.layoutSns);
        if (layoutSns != null) {
            new ButtonAnimationManager(layoutSns, v -> {
                if (isLocked()) {
                    return;
                }
                lockEvent();

                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                onChangeActivity(ChangeActivityCode.SNS, null);
            });
        }

        listView = (ListView) view.findViewById(R.id.listView);
        if (listView != null) {
            listView.setAdapter(null);
        }

        textViewCode = (TextView)view.findViewById(R.id.textViewCode);
        if (textViewCode != null) {
            textViewCode.setText(getApp().getFriendCode());
        }

        textViewFriendCount = (TextView)view.findViewById(R.id.textViewFriendCount);

        textViewBadge = (TextView)view.findViewById(R.id.textViewBadge);
        if (textViewBadge != null) {
            textViewBadge.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = textViewBadge.getLayoutParams();
            textViewBadge.setMinimumWidth(params.height);
        }

        readQRButton = view.findViewById(R.id.readQR_button);
        if (readQRButton != null) {
            new ButtonAnimationManager(readQRButton, v -> {
                if (isLocked()) return;

                lockEvent();

                GBApplication app = getApp();
                if (app != null) {
                    app.getSeManager().playSe(SeData.YES);
                }

                onChangeActivity(ChangeActivityCode.ReadQR, null);
            });
        }

        showQRButton = view.findViewById(R.id.showQR_button);
        if (showQRButton != null) {
            new ButtonAnimationManager(showQRButton, v -> {
                if (isLocked()) return;

                lockEvent();

                GBApplication app = getApp();
                if (app != null) {
                    app.getSeManager().playSe(SeData.YES);
                    transitionToShowQRCode(app.getFriendCode());
                }
            });
        }



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // データを取得する
        getFriendListData();
        DebugLog.i("FriendListFragment - onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (backButton != null) {
            backButton.setOnClickListener(null);
        }

        if (buttonInvite != null) {
            buttonInvite.setOnClickListener(null);
        }

        if (buttonMessage != null) {
            buttonMessage.setOnClickListener(null);
        }

        if (friendRankButton != null) {
            friendRankButton.setOnClickListener(null);
        }

        if (layoutSns != null) {
            layoutSns.setOnClickListener(null);
        }

        if (listView != null) {
            listView.setAdapter(null);
        }

        if (readQRButton != null) {
            readQRButton.setOnClickListener(null);
        }

        if (showQRButton != null) {
            showQRButton.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_friend_list);
    }

    /**
     * サーバーから受け取ったメッセージのバッヂ数を反映する
     */
    public final void onReceivedMessageBadge(int count, boolean isSystem) {
        if (isSystem) {
            mSystemBadge = count;
        }
        else mFriendBadge = count;

        if (mSystemBadge >=0 && mFriendBadge >= 0 && textViewBadge != null) {
            textViewBadge.setVisibility((mFriendBadge + mSystemBadge) == 0 ? View.GONE : View.VISIBLE);
            textViewBadge.setText(String.valueOf(mFriendBadge + mSystemBadge));
        }
    }

    /**
     * サーバーから受け取ったデータをリストに反映する
     */
    public final void onReceivedFriendListData(int friendCount, int friendMax, List<GomipoiFriendsResponse> listData) {
        if (textViewFriendCount != null) {
            textViewFriendCount.setText(String.format(getString(R.string.friend_list_count_format), friendCount, friendMax));
        }

        if (listView == null) {
            DebugLog.e("NullPo");
            return;
        }

        Collections.sort(listData, new Comparator<GomipoiFriendsResponse>() {
            @Override
            public int compare(GomipoiFriendsResponse lhs, GomipoiFriendsResponse rhs) {
                if (!lhs.isNeedShowInvite() && rhs.isNeedShowInvite()) {
                    return -1;
                }
                else if (lhs.isNeedShowInvite() && !rhs.isNeedShowInvite()) {
                    return 1;
                }

                return 0;
            }
        });

        if (listView.getAdapter() == null) {
            listView.setAdapter(
                    new FriendListAdapter(
                            getApplicationContext(),
                            listData,
                            (eventCode, data) -> {
                                if (isLocked()) {
                                    return;
                                }
                                lockEvent();

                                if (getApp() != null) {
                                    getApp().getSeManager().playSe(SeData.YES);
                                }

                                switch (FriendActionCode.valueOf(eventCode)) {
                                    case GIVE: {
                                        onShowDialog(DialogCode.GiveFriend, data);
                                        break;
                                    }

                                    case GIMME: {
                                        onShowDialog(DialogCode.GimmeFriend, data);
                                        break;
                                    }

                                    case INVITE: {
                                        onShowDialog(DialogCode.InviteFriend, data);
                                        unlockEvent();
                                        break;
                                    }

                                    case DELETE: {
                                        onShowDialog(DialogCode.DeleteFriend, data);
                                        unlockEvent();
                                        break;
                                    }
                                }
                            }));
            return;
        }

        Adapter adapter = listView.getAdapter();
        if (adapter instanceof FriendListAdapter) {
            ((FriendListAdapter) adapter).refresh(listData);
            return;
        }

        DebugLog.e("NullPo");
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * サーバーにフレンドリストのデータを問い合わせる
     */
    private void getFriendListData() {
        onFragmentEvent(FragmentEventCode.GetFriendList, null);
    }

    /**
     * QRコード表示画面
     * @param friendCode friendCode
     */
    private void transitionToShowQRCode(@NonNull String friendCode) {
        OnFragmentListener listener = getFragmentListener();
        if (listener != null) {
            listener.onChangedActivity(ChangeActivityCode.ShowQR.getValue(), friendCode);
        }
    }

    public void onDeleteFriendCallback(String friendNickName) {
        onShowDialog(DialogCode.DeleteFriendSuccess, friendNickName);
    }

    public void refreshFriendList() {
        getFriendListData();
    }

    public void deleteFriend(Object data) {
        //isDialogShowing = false;
        //friendNickName = nickName;
        //GomipoiFriendsDelete gomipoiFriendsDelete = new GomipoiFriendsDelete(friendId);
        onFragmentEvent(FragmentEventCode.DeleteFriend, data);
    }
}

package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;


import com.topmission.gomipoi.R;

import java.util.List;

import app.adapter.FriendGimmeAdapter;
import app.animation.button.ButtonAnimationManager;
import app.data.friend.IconCanSelectDialogResponse;
import app.data.http.FriendsForPresentRequestResponse;
import app.define.DialogCode;
import app.define.FriendActionCode;
import app.define.SeData;
import common.dialog.GBDialogBase;
import lib.adapter.OnAdapterListener;
import lib.animation.view.OnViewAnimationListener;
import lib.animation.view.ViewAnimationManager;
import lib.log.DebugLog;

/**
 *
 */
public class IconCanSelectDialog extends GBDialogBase {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private View layoutTop;
    private Button buttonUpgrade;
    private Button buttonClean;
    private Button buttonGimme;
    private View layoutList;
    private ListView listView;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static IconCanSelectDialog newInstance(String name) {
        IconCanSelectDialog dialog = new IconCanSelectDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected boolean isPermitTouchOutside() {
        return false;
    }

    @Override
    protected boolean isPermitCancel() {
        return true;
    }

    @Override
    public int getDialogCode() {
        return DialogCode.IconCan_Select.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        LayoutInflater inflator = getInflater();
        View contentView = inflator.inflate(R.layout.dialog_iconcan_select, null);

        layoutTop = contentView.findViewById(R.id.layoutTop);
        if (layoutTop != null) {
            layoutTop.setVisibility(View.VISIBLE);
        }

        buttonUpgrade = (Button)contentView.findViewById(R.id.buttonUpgrade);
        if (buttonUpgrade != null) {
            new ButtonAnimationManager(buttonUpgrade, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    sendResult(
                            RESULT_OK,
                            new IconCanSelectDialogResponse(
                                    FriendActionCode.USE_ITEM,
                                    null));
                }
            });
        }

        buttonClean = (Button)contentView.findViewById(R.id.buttonClean);
        if (buttonClean != null) {
            new ButtonAnimationManager(buttonClean, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    sendResult(
                            RESULT_OK,
                            new IconCanSelectDialogResponse(
                                    FriendActionCode.USE_ITEM,
                                    null));
                }
            });
        }

        buttonGimme = (Button)contentView.findViewById(R.id.buttonGimme);
        if (buttonGimme != null) {
            new ButtonAnimationManager(buttonGimme, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    if (getApp() != null) {
                        getApp().getSeManager().playSe(SeData.YES);
                    }

                    next();
                }
            });
        }

        layoutList = contentView.findViewById(R.id.layoutList);
        if (layoutList != null) {
            layoutList.setVisibility(View.GONE);
        }

        listView = (ListView)contentView.findViewById(R.id.listView);
        if (listView != null) {
            listView.setEmptyView(contentView.findViewById(R.id.textViewEmpty));
            listView.setAdapter(null);
        }

        return contentView;
    }

    @Override
    public void onDestroyView() {
        if (buttonUpgrade != null) {
            buttonUpgrade.setOnClickListener(null);
        }

        if (buttonClean != null) {
            buttonClean.setOnClickListener(null);
        }

        if (buttonGimme != null) {
            buttonGimme.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected boolean onClickedBackButton() {
        boolean result = !(layoutList != null && layoutList.getVisibility() == View.VISIBLE);
        if (!result) {
            if (getApp() != null) {
                getApp().getSeManager().playSe(SeData.NO);
            }

            prev();
        }
        return result;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * リストデータを受け取った時に呼ばれる
     */
    public final void onReceivedListData(List<FriendsForPresentRequestResponse> listData) {
        if (listView == null) {
            DebugLog.e("NullPo");
            return;
        }

        if (listView.getAdapter() == null) {
            listView.setAdapter(
                    new FriendGimmeAdapter(
                            getApplicationContext(),
                            listData,
                            new OnAdapterListener() {

                                @Override
                                public void onEvent(int eventCode, Object data) {
                                    if (isEventLocked()) {
                                        return;
                                    }
                                    lockEvent();

                                    switch (FriendActionCode.valueOf(eventCode)) {
                                        case GIMME: {
                                            sendResult(
                                                    RESULT_OK,
                                                    new IconCanSelectDialogResponse(
                                                            FriendActionCode.GIMME,
                                                            (FriendsForPresentRequestResponse)data));
                                            break;
                                        }

                                        default: {
                                            unlockEvent();
                                            break;
                                        }
                                    }

                                }

                            }));
            return;
        }

        Adapter adapter = listView.getAdapter();
        if (adapter instanceof FriendGimmeAdapter) {
            ((FriendGimmeAdapter) adapter).refresh(listData);
            return;
        }

        DebugLog.e("NullPo");
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * リストのデータを取得する
     */
    private void refreshListData() {
        lockEvent();
        sendResult(
                RESULT_OK,
                new IconCanSelectDialogResponse(
                        FriendActionCode.DATA_GET,
                        null));
    }

    /**
     * リストレイヤーに移動する
     */
    private void next() {
        if (layoutTop != null) {
            ViewAnimationManager animation = new ViewAnimationManager(1, layoutTop, new OnViewAnimationListener() {
                @Override
                public void onAnimationStart(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationRepeat(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationEnd(int animationCode, Animation animation) {
                    layoutTop.setVisibility(View.GONE);
                }
            }) {
                @Override
                public Animation createViewAnimation() {
                    TranslateAnimation animation = new TranslateAnimation(0, -layoutTop.getWidth(), 0, 0);
                    animation.setDuration(200L);
                    animation.setFillBefore(true);
                    animation.setFillAfter(true);
                    return animation;
                }
            };
            animation.start();
        }

        if (layoutList != null) {
            ViewAnimationManager animation = new ViewAnimationManager(1, layoutList, new OnViewAnimationListener() {
                @Override
                public void onAnimationStart(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationRepeat(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationEnd(int animationCode, Animation animation) {
                    refreshListData();
                }
            }) {
                @Override
                public Animation createViewAnimation() {
                    TranslateAnimation animation = new TranslateAnimation(layoutList.getWidth(), 0, 0, 0);
                    animation.setDuration(200L);
                    animation.setFillBefore(true);
                    animation.setFillAfter(true);
                    return animation;
                }
            };
            animation.start();
            layoutList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * トップレイヤーに戻る
     */
    private void prev() {
        if (layoutTop != null) {
            ViewAnimationManager animation = new ViewAnimationManager(1, layoutTop, new OnViewAnimationListener() {
                @Override
                public void onAnimationStart(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationRepeat(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationEnd(int animationCode, Animation animation) {
                    unlockEvent();
                }
            }) {
                @Override
                public Animation createViewAnimation() {
                    TranslateAnimation animation = new TranslateAnimation(-layoutTop.getWidth(), 0, 0, 0);
                    animation.setDuration(200L);
                    animation.setFillBefore(true);
                    animation.setFillAfter(true);
                    return animation;
                }
            };
            animation.start();
            layoutTop.setVisibility(View.VISIBLE);
        }

        if (layoutList != null) {
            ViewAnimationManager animation = new ViewAnimationManager(1, layoutList, new OnViewAnimationListener() {
                @Override
                public void onAnimationStart(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationRepeat(int animationCode, Animation animation) {
                }

                @Override
                public void onAnimationEnd(int animationCode, Animation animation) {
                    layoutList.setVisibility(View.GONE);
                }
            }) {
                @Override
                public Animation createViewAnimation() {
                    TranslateAnimation animation = new TranslateAnimation(0, layoutList.getWidth(), 0, 0);
                    animation.setDuration(200L);
                    animation.setFillBefore(true);
                    animation.setFillAfter(true);
                    return animation;
                }
            };
            animation.start();
        }
    }
}

package app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.data.BookGarbageData;
import app.define.ChangeActivityCode;
import app.define.DebugMode;
import app.define.DialogCode;
import app.define.GarbageId;
import app.define.SeData;
import app.jni.OnGLJniBridgeListener;
import app.wheel.WheelManager;
import app.wheel.lib.number.NumberWheelView;
import common.fragment.GBFragmentBase;

/**
 * トップ画面
 */
public class TopFragment extends GBFragmentBase implements View.OnClickListener, OnGLJniBridgeListener {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static TopFragment newInstance() {
        TopFragment fragment = new TopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private Button buttonRanking;
    private Button buttonExchange;
    private Button buttonSettings;
    private Button buttonHelp;
    private Button buttonFriend;
    private Button buttonStart;
    private View layoutSns;
    private Button buttonInfo;
    private ImageView imageViewInfoNew;
    private ImageView imageViewFriendNew;

    private WheelManager mWheelManager;

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        buttonRanking = (Button) view.findViewById(R.id.buttonRanking);
        if (buttonRanking != null) {
            new ButtonAnimationManager(buttonRanking, this);
        }

        buttonExchange = (Button) view.findViewById(R.id.buttonExchange);
        if (buttonExchange != null) {
            new ButtonAnimationManager(buttonExchange, this);
        }

        buttonSettings = (Button) view.findViewById(R.id.buttonSettings);
        if (buttonSettings != null) {
            new ButtonAnimationManager(buttonSettings, this);
        }

        buttonHelp = (Button) view.findViewById(R.id.buttonHelp);
        if (buttonHelp != null) {
            new ButtonAnimationManager(buttonHelp, this);
        }

        buttonFriend = (Button) view.findViewById(R.id.buttonFriend);
        if (buttonFriend != null) {
            new ButtonAnimationManager(buttonFriend, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLocked()) {
                        return;
                    }
                    lockEvent();

                    if (getApp() != null) {
                        getApp().getSeManager().playSe(SeData.YES);
                    }

                    if (getApp().getIsGuest()) {
                        // ゲストの場合は会員登録ダイアログを表示
                        onShowDialog(DialogCode.GuestLimitedFuncFriend, null);
                    } else {
                        // 会員ユーザーの場合はフレンド画面へ遷移
                        onChangeActivity(ChangeActivityCode.Friend, null);
                    }
                }
            });
        }

        imageViewFriendNew = (ImageView) view.findViewById(R.id.imageViewFriendNew);

        buttonStart = (Button) view.findViewById(R.id.buttonStart);
        if (buttonStart != null) {
            new ButtonAnimationManager(buttonStart, this);
        }

        layoutSns = view.findViewById(R.id.layoutSns);
        if (layoutSns !=  null) {
            layoutSns.setOnClickListener(this);
        }

        buttonInfo = (Button) view.findViewById(R.id.buttonInfo);
        if (buttonInfo != null) {
            new ButtonAnimationManager(buttonInfo, this);
        }

        imageViewInfoNew = (ImageView) view.findViewById(R.id.imageViewInfoNew);
        if (imageViewInfoNew != null) {
            imageViewInfoNew.setVisibility(View.INVISIBLE);
        }

        mWheelManager = new WheelManager(getActivity().getApplicationContext());
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel1), 0);
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel10), 1);
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel100), 2);
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel1000), 3);
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel10000), 4);
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel100000), 5);
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel1000000), 6);
        mWheelManager.addWheelView((NumberWheelView) view.findViewById(R.id.wheel10000000), 7);
        mWheelManager.addSeparatorView(view.findViewById(R.id.imageViewSeparator1), 3);
        mWheelManager.addSeparatorView(view.findViewById(R.id.imageViewSeparator2), 6);
        mWheelManager.onCreate(0);

        if (DebugMode.isTestJirokichi || DebugMode.isTestRandomCharacter) {
            Toast toast = Toast.makeText(getContext(), "デモ版アプリです。", Toast.LENGTH_LONG);
            toast.show();
        }

        return view;
    }

   @Override
    public void onResume() {
        super.onResume();

        if (getApp() != null) {
            getApp().getJniBridge().setOnGLJniBridgeListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getApp() != null) {
            getApp().getJniBridge().setOnGLJniBridgeListener(null);
        }

    }

    @Override
    public void onDestroyView() {
        if (mWheelManager != null) {
            mWheelManager.onDestroy();
            mWheelManager = null;
        }

        if (buttonRanking != null) {
            buttonRanking.setOnClickListener(null);
        }

        if (buttonExchange != null) {
            buttonExchange.setOnClickListener(null);
        }

        if (buttonSettings != null) {
            buttonSettings.setOnClickListener(null);
        }

        if (buttonHelp != null) {
            buttonHelp.setOnClickListener(null);
        }

        if (buttonFriend != null) {
            buttonFriend.setOnClickListener(null);
        }

        if (buttonStart != null) {
            buttonStart.setOnClickListener(null);
        }

        if (layoutSns !=  null) {
            layoutSns.setOnClickListener(null);
        }

        if (buttonInfo != null) {
            buttonInfo.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    // ------------------------------
    // View.OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        if (isLocked()) {
            return;
        }
        lockEvent();

        if (getApp() != null) {
            getApp().getSeManager().playSe(SeData.YES);
        }

        switch (v.getId()) {
            case R.id.buttonRanking: {
                onChangeActivity(ChangeActivityCode.Ranking, null);
                break;
            }

            case R.id.buttonExchange: {
                onChangeActivity(ChangeActivityCode.Exchange, null);
                break;
            }

            case R.id.buttonSettings: {
                onChangeActivity(ChangeActivityCode.Settings, null);
                break;
            }

            case R.id.buttonHelp: {
                onChangeActivity(ChangeActivityCode.Help, null);
                break;
            }

            case R.id.buttonStart: {
                onChangeActivity(ChangeActivityCode.Game, null);
                break;
            }

            case R.id.layoutSns: {
                onChangeActivity(ChangeActivityCode.SNS, null);
                break;
            }

            case R.id.buttonInfo: {
                onChangeActivity(ChangeActivityCode.Info, null);
                break;
            }

            default: {
                unlockEvent();
                break;
            }
        }
    }

    // ------------------------------
    // OnGLJniBridgeListener
    // ------------------------------
    @Override
    public void onBrokenBroom(int broomType) {
    }

    @Override
    public void onBrokenGarbageCan() {
    }

    @Override
    public void onChangedGem(int gem) {
    }

    @Override
    public void onClearMission(int missionId) {

    }

    @Override
    public void onUndergroundGemGot(int gem) {

    }

    @Override
    public void onChangedPoint(int point) {
        if (mWheelManager != null) {
            mWheelManager.changeValue(point);
        }
    }

    @Override
    public void onChangedFullness(double fullness) {
    }

    @Override
    public void onChangedLevel(int level) {
    }

    @Override
    public void onShowPoint(int point) {
    }

    @Override
    public void onShowGem(int gem) {
    }

    @Override
    public void onShowComboBonus(int comboCount, int point) {
    }

    @Override
    public void onShowSucceededSyntheses(GarbageId garbageId) {
        onShowDialog(DialogCode.SynthesisSuccess, new BookGarbageData(garbageId));
    }

    @Override
    public void onCompletedLoading() {
    }

    @Override
    public void onRequestSaveGame(int add_point, int put_in_garbage_count, String garbages,
                                  int broom_use_count, int broom_broken) {
//        onFragmentEvent(FragmentEventCode.PostGameSave, new GomipoiGameSaveParam(add_point, put_in_garbage_count, garbages, broom_use_count, broom_broken == 1));
    }

    @Override
    public void onRequestFoundGarbage(String idListText) {
//        String[] idList = idListText.split(",");
//        GomipoiGarbageFoundParam param = new GomipoiGarbageFoundParam(GomipoiGarbageFoundParam.Method.Cleaning);
//        for (int i = 0; i < idList.length; i++) {
//            param.addGarbage(new Garbage(GarbageId.valueOfWithCode(idList[i]), Garbage.FOUND_TYPE_CLEANING));
//        }
//        onFragmentEvent(FragmentEventCode.PostGarbageFound, param);
    }

    @Override
    public void onEnterUnderground(boolean enterJirokichi) {

    }

    @Override
    public void onChangeStage(int stageId) {

    }

    @Override
    public void onRemainingBonusTime(int remainingSecond) {

    }

    @Override
    public void onFinishSaveGame() {
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_top);
    }

    public void setInfoNewVisible(boolean notificationVisible, boolean friendVisible) {
        if (imageViewInfoNew != null) {
            if (notificationVisible) {
                imageViewInfoNew.setVisibility(View.VISIBLE);
            } else {
                imageViewInfoNew.setVisibility(View.INVISIBLE);
            }
        }
        if (imageViewFriendNew != null) {
            if (friendVisible) {
                imageViewFriendNew.setVisibility(View.VISIBLE);
            } else {
                imageViewFriendNew.setVisibility(View.INVISIBLE);
            }
        }
    }
}

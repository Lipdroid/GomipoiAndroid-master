package app.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import java.util.ArrayList;

import app.animation.button.ButtonAnimationManager;
import app.data.BookGarbageData;
import app.data.ListItemData;
import app.data.SharedCaptureData;
import app.data.http.GomipoiItemUseParam;
import app.data.http.GomipoiJirokichiBonusesParams;
import app.define.ChangeActivityCode;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.define.GarbageCanType;
import app.define.GarbageId;
import app.define.ItemId;
import app.define.SeData;
import app.define.StageType;
import app.jni.JniBridge;
import app.jni.OnGLJniBridgeListener;
import app.manager.PointManager;
import app.number.NumberUtils;
import app.opengl.GLRenderer;
import app.sound.BgmManager;
import app.view.OutlineTextView;
import app.wheel.WheelManager;
import app.wheel.lib.number.NumberWheelView;
import common.fragment.GBFragmentBase;
import lib.convert.UnitUtils;
import lib.image.ImageUtils;
import lib.log.DebugLog;
import lib.opengl.GLSurfaceViewBase;
import lib.opengl.GLUtils;
import lib.storage.StorageUtils;
import lib.storage.StorageUtilsResponse;

/**
 * ゲーム画面
 */
public class GameFragment extends GBFragmentBase implements View.OnTouchListener,
        OnGLJniBridgeListener, View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final int SCALE_CAPTURE = 2;

    private static final int MISSION_ID_DEFAULT_1   = 0;
    private static final int MISSION_ID_DEFAULT_2   = 1;
    private static final int MISSION_ID_DEFAULT_3   = 2;
    private static final int MISSION_ID_POIKO_1     = 3;
    private static final int MISSION_ID_POIKO_2     = 4;
    private static final int MISSION_ID_POIKO_3     = 5;

    private static final long JIROKICHI_FADE_ANIMATION = 300l;

    // ------------------------------
    // Member
    // ------------------------------
    private GLSurfaceViewBase glView;

    private View layoutHeader;
    private View layoutFooter;

    private View viewGaugeFrame;
    private View viewGauge;
    private ImageView imageViewIconCan;
    private ImageView imageViewLevelMax;
    private ImageView imageViewCapture;

    private Button buttonItem;
    private Button buttonSynthesis;
    private Button buttonPicturePoi;
    private Button buttonPictureBook;
//    private View layoutSns;
    private ImageButton buttonMenu;

    private OutlineTextView outlineTextViewGem;
    private OutlineTextView outlineTextViewLevel;

    private View bonusTimeLayout;
    private OutlineTextView bonusTimeTextView;

    private WheelManager mWheelManager;
    private PointManager mPointManager;

    private GLRenderer mRenderer;
    private boolean mIsCreated;
    private boolean mIsUnderground;

    private OutlineTextView outlineTextViewSyncData;

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getApp() == null || getApp().isNeedRelunchar()) {
            return;
        }

        mIsCreated = true;
        getApp().getJniBridge().onParentCreated();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        glView = (GLSurfaceViewBase) view.findViewById(R.id.glView);
        if (glView != null) {
            if (GLUtils.isSupportedGLES2(getApplicationContext())) {
                glView.setEGLContextClientVersion(2);
                glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
                mRenderer = new GLRenderer(getApplicationContext());
                glView.setRenderer(mRenderer);
                glView.setOnTouchListener(this);
            }
        }

        mPointManager = new PointManager(
                getApplicationContext(),
                view.findViewById(R.id.layoutPointEffect));

        viewGaugeFrame = view.findViewById(R.id.viewGaugeFrame);
        if (viewGaugeFrame != null) {
            viewGaugeFrame.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewGaugeFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewGaugeFrame.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    onChangedFullness(JniBridge.nativeGetFullness());
                }
            });
        }

        viewGauge = view.findViewById(R.id.viewGauge);

        imageViewIconCan = (ImageView) view.findViewById(R.id.imageViewIconCan);
        if (imageViewIconCan != null) {
            new ButtonAnimationManager(imageViewIconCan, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (isLocked()) {
//                        return;
//                    }
//                    lockEvent();
//
//                    getApp().getSeManager().playSe(SeData.YES);
//
//                    onShowDialog(DialogCode.IconCan_Select, null);
                }
            });
        }

        layoutFooter = view.findViewById(R.id.layoutFooter);

        buttonItem = (Button) view.findViewById(R.id.buttonItem);
        if (buttonItem != null) {
            ButtonAnimationManager animationManager = new ButtonAnimationManager(buttonItem, this);
        }

        buttonSynthesis = (Button) view.findViewById(R.id.buttonSynthesis);
        if (buttonSynthesis != null) {
            ButtonAnimationManager animationManager = new ButtonAnimationManager(buttonSynthesis, this);
        }

        buttonPicturePoi = (Button) view.findViewById(R.id.buttonPicturePoi);
        if (buttonPicturePoi != null) {
            ButtonAnimationManager animationManager = new ButtonAnimationManager(buttonPicturePoi, this);
        }

        buttonPictureBook = (Button) view.findViewById(R.id.buttonPictureBook);
        if (buttonPictureBook != null) {
            ButtonAnimationManager animationManager = new ButtonAnimationManager(buttonPictureBook, this);
        }

        buttonMenu = (ImageButton) view.findViewById(R.id.buttonMenu);
        if (buttonMenu != null) {
            new ButtonAnimationManager(buttonMenu, this);
        }

        bonusTimeLayout = view.findViewById(R.id.bonus_time_left_layout);
        bonusTimeTextView = (OutlineTextView) view.findViewById(R.id.bonus_time_left_text);
        if (bonusTimeTextView != null) {
            bonusTimeTextView.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
        }

//        layoutSns = view.findViewById(R.id.layoutSns);
//        if (layoutSns != null) {
//            layoutSns.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isLocked()) {
//                        return;
//                    }
//                    lockEvent();
//
//                    shareCapture();
//                }
//            });
//        }

        outlineTextViewGem = (OutlineTextView) view.findViewById(R.id.outlineTextViewGem);
        if (outlineTextViewGem != null) {
            outlineTextViewGem.setOutlineTextAligh(OutlineTextView.ALIGN_RIGHT);
            outlineTextViewGem.setText(NumberUtils.getNumberFormatText(JniBridge.nativeGetGem()));
        }

        int level = JniBridge.nativeGetLevel();

        int maxLevel = JniBridge.nativeGetMaxLevel();
        imageViewLevelMax = (ImageView) view.findViewById(R.id.imageViewLevelMax);
        if (imageViewLevelMax != null) {
            imageViewLevelMax.setVisibility((level >= maxLevel) ? View.VISIBLE : View.GONE);
        }

        outlineTextViewLevel = (OutlineTextView) view.findViewById(R.id.outlineTextViewLevel);
        if (outlineTextViewLevel != null) {
            outlineTextViewLevel.setVisibility((level < maxLevel) ? View.VISIBLE : View.GONE);
            if (level < maxLevel) {
                outlineTextViewLevel.setOutlineTextAligh(OutlineTextView.ALIGN_LEFT);
                outlineTextViewLevel.setText(NumberUtils.getNumberFormatText(level));
            }
        }

        imageViewCapture = (ImageView)view.findViewById(R.id.imageViewCapture);
        if (imageViewCapture != null) {
            imageViewCapture.setVisibility(View.GONE);
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
        mWheelManager.onCreate(JniBridge.nativeGetPoint());

        // データ送信時のメッセージ
        outlineTextViewSyncData = (OutlineTextView) view.findViewById(R.id.outlineTextView_saveData);
        outlineTextViewSyncData.setOutlineTextAligh(OutlineTextView.ALIGN_LEFT);
        outlineTextViewSyncData.setOutlineTextWidth((int) UnitUtils.getPxFromDp(getApplicationContext(), 3));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (glView != null) {
            glView.setVisibility(View.VISIBLE);
        }

        lockEvent();

        if (getApp() != null) {
            getApp().getJniBridge().setOnGLJniBridgeListener(this);
        }

        getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                getMainHandler().removeCallbacks(this);
                if (glView != null) {
                    glView.onResume();
                }

                if (getApp() != null) {
                    getApp().setForegroundFirst(true);
                    getApp().getJniBridge().foreground();
                }
            }
        });

        bonusTimeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        if (glView != null) {
            glView.setVisibility(View.INVISIBLE);
        }

        if (getApp() != null) {
            getApp().getJniBridge().setOnGLJniBridgeListener(null);
            getApp().getJniBridge().background();
            getApp().getJniBridge().loadingPause();
        }

        getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                getMainHandler().removeCallbacks(this);

                if (glView != null) {
                    glView.onPause();
                }
            }
        });
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mWheelManager != null) {
            mWheelManager.onDestroy();
            mWheelManager = null;
        }

        mPointManager = null;

        if (mRenderer != null) {
            mRenderer = null;
        }

        if (glView != null) {
            glView.setOnTouchListener(null);
        }

        if (buttonItem != null) {
            buttonItem.setOnClickListener(null);
        }

        if (buttonSynthesis != null) {
            buttonSynthesis.setOnClickListener(null);
        }

        if (buttonPicturePoi != null) {
            buttonPicturePoi.setOnClickListener(null);
        }

        if (buttonPictureBook != null) {
            buttonPictureBook.setOnClickListener(null);
        }

        if (buttonMenu != null) {
            buttonMenu.setOnClickListener(null);
        }

        if (imageViewIconCan != null) {
            imageViewIconCan.setOnClickListener(null);
        }

//        if (layoutSns != null) {
//            layoutSns.setOnClickListener(null);
//        }

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (getApp() != null) {
            getApp().getJniBridge().onParentDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    // ------------------------------
    // View.OnTouchListener
    // ------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (getApp() != null) {
                    getApp().getJniBridge().onTouchDown(event.getX(), event.getY());
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (getApp() != null) {
                    getApp().getJniBridge().onTouchMove(event.getX(), event.getY());
                }
                return true;
            }
            case MotionEvent.ACTION_UP: {
                if (getApp() != null) {
                    getApp().getJniBridge().onTouchUp();
                }
                return true;
            }
        }

        return false;
    }

    // ------------------------------
    // ジロキチボーナス受け取り成功
    // ------------------------------
    public void onSucceedReceiveJirokichiBonus(int gem) {
        JniBridge.nativeAddGem(gem);
    }

    // ------------------------------
    // OnGLJniBridgeListener
    // ------------------------------
    @Override
    public void onCompletedLoading() {
        unlockEvent();

        // 最初のローディング完了なら、ゲームを開始する
        if (mIsCreated) {
            mIsCreated = false;
            restart();
        }

        if (layoutHeader != null) {
            layoutHeader.setVisibility(View.VISIBLE);
        }

        if (imageViewCapture != null) {
            if (imageViewCapture.getVisibility() == View.VISIBLE) {
                imageViewCapture.setVisibility(View.GONE);
                getApp().getJniBridge().pauseEnd();

                // Goneにしてすぐに解放しようとすると、画面がちらつくので、
                // その他の処理を待ってから解放処理を行う
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(this);
                        ImageUtils.clearImageFromImageView(imageViewCapture);
                    }
                });
            }
        }

    }

    @Override
    public void onChangedFullness(double fullness) {
        double frameWidth = 0.0;
        int marginLeft = 0;
        switch (GarbageCanType.valueOf(JniBridge.nativeGetCurrentGarbageCanType())) {
            case Normal: {
                frameWidth = getApp().getResizeManager().calcValue(75);
                marginLeft = (int)getApp().getResizeManager().calcValue(215);
                break;
            }

            case Big: {
                frameWidth = getApp().getResizeManager().calcValue(140);
                marginLeft = (int)getApp().getResizeManager().calcValue(150);
                break;
            }

            case Huge: {
                frameWidth = getApp().getResizeManager().calcValue(210);
                marginLeft = (int)getApp().getResizeManager().calcValue(80);
                break;
            }

            case XL: {
                frameWidth = getApp().getResizeManager().calcValue(210);
                marginLeft = (int)getApp().getResizeManager().calcValue(80);
                break;
            }
        }

        if (viewGaugeFrame != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)viewGaugeFrame.getLayoutParams();
            params.width = (int)frameWidth;
            params.leftMargin = marginLeft;
            viewGaugeFrame.setLayoutParams(params);
        }

        if (imageViewIconCan != null) {
            int imageLevel = 0;
            if (fullness >= 1.0) {
                imageLevel = 2;
            } else if (fullness >= 0.8) {
                imageLevel = 1;
            }
            imageViewIconCan.setImageLevel(imageLevel);
        }

        if (viewGauge != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)viewGauge.getLayoutParams();
            params.width = (int) frameWidth;
            params.leftMargin = marginLeft;
            viewGauge.setLayoutParams(params);
            ClipDrawable drawable = (ClipDrawable) viewGauge.getBackground();
            int level = (int) Math.round(10000.0 * fullness);
            if (level < 0)
                level = 0;
            else if (level > 10000) {
                level = 10000;
            }
            drawable.setLevel(level);
        }
    }

    @Override
    public void onChangedGem(int gem) {
        if (outlineTextViewGem != null) {
            outlineTextViewGem.setText(NumberUtils.getNumberFormatText(gem));
        }
    }

    @Override
    public void onClearMission(int missionId) {
        ItemId id = null;

        switch (missionId) {
            case MISSION_ID_DEFAULT_1:
                id = ItemId.RoomSecret1;
                break;

            case MISSION_ID_DEFAULT_2:
                id = ItemId.RoomSecret2;
                break;

            case MISSION_ID_DEFAULT_3:
                id = ItemId.RoomSecret3;
                break;

            case MISSION_ID_POIKO_1:
                id = ItemId.RoomSecret4;
                break;

            case MISSION_ID_POIKO_2:
                id = ItemId.RoomSecret5;
                break;

            case MISSION_ID_POIKO_3:
                id = ItemId.RoomSecret6;
                break;
        }

        if (id != null) {
            onFragmentEvent(FragmentEventCode.PostItemUse, new GomipoiItemUseParam(new ListItemData(id)));
        }
    }

    @Override
    public void onUndergroundGemGot(int gem) {
        onFragmentEvent(FragmentEventCode.Receive_JirokichiBonus, new GomipoiJirokichiBonusesParams(gem));
    }

    @Override
    public void onChangedPoint(int point) {
        if (mWheelManager != null) {
            mWheelManager.changeValue(point);
        }
    }

    @Override
    public void onChangedLevel(int level) {
        int maxLevel = JniBridge.nativeGetMaxLevel();

        if (imageViewLevelMax != null) {
            imageViewLevelMax.setVisibility((level >= maxLevel) ? View.VISIBLE : View.GONE);
        }

        if (outlineTextViewLevel != null) {
            outlineTextViewLevel.setVisibility((level < maxLevel) ? View.VISIBLE : View.GONE);
            if (level < maxLevel) {
                outlineTextViewLevel.setText(NumberUtils.getNumberFormatText(level));
            }
        }
    }

    @Override
    public void onShowPoint(int point) {
        if (mPointManager != null) {
            mPointManager.showPoint(NumberUtils.getNumberFormatText(point) + "P");
        }
    }

    @Override
    public void onShowGem(int gem) {
        if (mPointManager != null) {
            mPointManager.showPoint(NumberUtils.getNumberFormatText(gem) + "Get");
        }

        if (mIsUnderground) {
            getApp().getSeManager().playSe(SeData.GEM);
        }
    }

    @Override
    public void onShowComboBonus(int comboCount, int point) {
        if (mPointManager != null) {
            mPointManager.showPoint(NumberUtils.getNumberFormatText(comboCount) + "combo");
        }
    }

    @Override
    public void onShowSucceededSyntheses(GarbageId garbageId) {
        onShowDialog(DialogCode.SynthesisSuccess, new BookGarbageData(garbageId));
    }

    @Override
    public void onBrokenBroom(int broomType) {
        onShowDialog(DialogCode.BrokenBroom, broomType);
    }

    @Override
    public void onBrokenGarbageCan() {
        onShowDialog(DialogCode.BrokenGarbageCan, null);
    }

    @Override
    public void onRequestSaveGame(int add_point, int put_in_garbage_count, String garbages, int broom_use_count, int broom_broken) {
        // テキスト表示設定を更新する
        DataSyncTextAnimation.show(outlineTextViewSyncData);
//        onFragmentEvent(FragmentEventCode.PostGameSave, new GomipoiGameSaveParam(add_point, put_in_garbage_count, garbages, broom_use_count, broom_broken == 1));
    }

    @Override
    public void onFinishSaveGame() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                DataSyncTextAnimation.dismiss(outlineTextViewSyncData);
            }
        }, 1000);
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
        if (enterJirokichi) {
            layoutFooter.setVisibility(View.GONE);
            mIsUnderground = true;
            getApp().getBgmManager().changeBgm(BgmManager.BgmType.JIROKICHI);
        }
        else {
            layoutFooter.setVisibility(View.VISIBLE);
            mIsUnderground = false;
            getApp().getBgmManager().changeBgm(BgmManager.BgmType.NORMAL);
        }
    }

    @Override
    public void onChangeStage(int stageId) {
        if (layoutFooter != null) {
            StageType stage = StageType.valueOf(stageId);

            switch (stage) {
                case DefaultRoom:
                    layoutFooter.setBackgroundResource(R.drawable.room_footer);
                    break;

                case PoikoRoom:
                    layoutFooter.setBackgroundResource(R.drawable.room_footer_poiko);
                    break;

                case Garden:
                    layoutFooter.setBackgroundResource(R.drawable.room_footer);
                    break;
            }
        }
    }

    @Override
    public void onRemainingBonusTime(int remainingSecond) {
        if (remainingSecond < 0) {
            bonusTimeLayout.setVisibility(View.GONE);
        }
        else {
            bonusTimeLayout.setVisibility(View.VISIBLE);
            bonusTimeTextView.setText(Integer.toString(remainingSecond));
        }
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
            case R.id.buttonItem: {
                onShowDialog(DialogCode.Item, null);
                break;
            }

            case R.id.buttonSynthesis: {
                onShowDialog(DialogCode.Synthesis, null);
                break;
            }

            case R.id.buttonPicturePoi: {
                onShowDialog(DialogCode.PicturePoi, null);
                break;
            }

            case R.id.buttonPictureBook: {
                onShowDialog(DialogCode.PictureBook, null);
                break;
            }

            case R.id.buttonMenu: {
                ArrayList<StageType> availableStages = new ArrayList<>();
                availableStages.add(StageType.DefaultRoom);
                availableStages.add(StageType.PoikoRoom);
                availableStages.add(StageType.Garden);
                onShowDialog(DialogCode.GameMenu, availableStages);
                break;
            }

            default: {
                unlockEvent();
                break;
            }
        }
    }

    // ------------------------------
    // Acceser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_game);
    }

    public final void restart() {
//        getGameManager().restart();
//
//        if (layoutLiquidate != null) {
//            layoutLiquidate.setVisibility(View.GONE);
//        }
//
//        if (layoutLose != null) {
//            layoutLose.setVisibility(View.GONE);
//        }
//
//        // ライフを使用
//        if (getApp() != null && getApp().getPlayerManager() != null) {
//            getApp().getPlayerManager().useLife();
//        }
    }

    public final void onRefresh() {
        if (getApp() != null) {
            getApp().getJniBridge().setOnGLJniBridgeListener(this);
        }

        onChangedGem(JniBridge.nativeGetGem());
        onChangedFullness(JniBridge.nativeGetFullness());
        onChangedLevel(JniBridge.nativeGetLevel());
        onChangedPoint(JniBridge.nativeGetPoint());
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * キャプチャを共有する
     */
    private void shareCapture() {

        //
        // 画面キャプチャ処理
        // ------------------------------
        getApp().getJniBridge().pause();
        onFragmentEvent(FragmentEventCode.ShowCaptureWaitingDialog, null);
        System.gc();

        // OnLGSurfaceViewBaseListener内でこの処理を行うと、
        // スレッド違いでクラッシュする恐れがあるため、
        // ここでViewのキャプチャは取得しておく
        final Bitmap viewCapture = ImageUtils.getViewCapture(getView().findViewById(R.id.layoutCaptureTarget), SCALE_CAPTURE);
        if (viewCapture == null) {
            onFragmentEvent(FragmentEventCode.CloseCaptureWaitingDialog, null);

            // TODO エラー
            DebugLog.e("Viewのキャプチャの取得に失敗");
            getApp().getJniBridge().pauseEnd();
            return;
        }

        // Capture画像生成
        glView.getCapture(SCALE_CAPTURE, new GLSurfaceViewBase.OnGLSurfaceViewBaseListener() {
            @Override
            public void onCaptured(Bitmap capture) {
                if (capture == null) {
                    onFragmentEvent(FragmentEventCode.CloseCaptureWaitingDialog, null);

                    // TODO エラー
                    DebugLog.e("SurfaceViewのキャプチャの取得に失敗");
                    getApp().getJniBridge().pauseEnd();
                    return;
                }

                // 画像の合成
                final Bitmap captureImage = ImageUtils.synthesizeImages(capture, viewCapture);
                final StorageUtilsResponse response = StorageUtils.saveImage(
                        getApplicationContext(),
                        ".data/ALC/Image",
                        "IMG_CAPTURE.png",
                        captureImage);

                if (!capture.isRecycled()) {
                    capture.recycle();
                }

                if (!viewCapture.isRecycled()) {
                    viewCapture.recycle();
                }

                System.gc();

                if (response.status != StorageUtilsResponse.OK) {
                    onFragmentEvent(FragmentEventCode.CloseCaptureWaitingDialog, null);

                    // TODO エラー
                    DebugLog.e("キャプチャの保存に失敗");
                    getApp().getJniBridge().pauseEnd();
                    return;
                }

                // UIスレッドに処理を戻す
                getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        getMainHandler().removeCallbacks(this);

                        if (imageViewCapture != null) {
                            ImageUtils.setImageToImageView(captureImage, imageViewCapture);
                            imageViewCapture.setVisibility(View.VISIBLE);
                        }

                        System.gc();

                        onFragmentEvent(FragmentEventCode.CloseCaptureWaitingDialog, null);

                        onChangeActivity(
                                ChangeActivityCode.CaptureShare,
                                new SharedCaptureData(
                                        "#ゴミ箱にPOI",
                                        Uri.fromFile(response.outputFile)));

                    }
                });
            }
        });
    }

    public BgmManager.BgmType getBgmType() {
        if (mIsUnderground) {
            return BgmManager.BgmType.JIROKICHI;
        }
        else return BgmManager.BgmType.NORMAL;
    }

    private static class DataSyncTextAnimation {
        /**
         *
         * @param view view
         */
        static void show(@Nullable View view) {
            if (view == null) {
                return;
            }

            view.setVisibility(View.VISIBLE);

            final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            alphaAnimation.setDuration(500);
            view.startAnimation(alphaAnimation);
        }

        /**
         *
         * @param view view
         */
        static void dismiss(@Nullable View view) {
            if (view == null) {
                return;
            }

            view.clearAnimation();
            view.setVisibility(View.GONE);
        }
    }
}

package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.adapter.SynthesisAdapter;
import app.animation.button.ButtonAnimationManager;
import app.data.BookGarbageData;
import app.define.DialogCode;
import app.define.GarbageId;
import app.define.ItemCode;
import app.define.SeData;
import app.jni.JniBridge;
import common.dialog.GBDialogBase;
import lib.adapter.OnAdapterListener;

/**
 *
 */
public class SynthesisDialog extends GBDialogBase
        implements View.OnClickListener, ViewPager.OnPageChangeListener, OnAdapterListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private ViewPager mBookViewPager = null;// ViewPager
    private ImageButton mArrowLeftImageButton = null;// 左矢印ボタン
    private ImageButton mArrowRightImageButton = null;// 右矢印ボタン
    private ImageButton mSynthesisImageButton = null;// 合成ボタン
    private List<BookGarbageData> mSelectGarbageList = null;// 選択しているアイテム
    private ImageButton mLeftSynthesisImageButton = null;// 合成アイテムの左側
    private ImageButton mMiddelSynthesisImageButton = null;// 合成アイテムの中央
    private ImageButton mRightSynthesisImageButton = null;// 合成アイテムの右側
    private Button buttonScroll;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static SynthesisDialog newInstance(String name) {
        SynthesisDialog dialog = new SynthesisDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.Synthesis.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_synthesis, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }
        // ViewPager
        setBookViewPager(view);
        // 矢印ボタン
        setArrowButton(view);
        // 合成ボタン
        setSynthesisButton(view);
        // 合成選択アイテム
        setSelectSynthesisItem(view);

        buttonScroll = (Button) view.findViewById(R.id.buttonScroll);
        if (buttonScroll != null) {
            new ButtonAnimationManager(buttonScroll, this);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (buttonScroll != null) {
            boolean isClickable = (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets1.getValue()) > 0)
                    || (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets2.getValue()) > 0)
                    || (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets3.getValue()) > 0)
                    || (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets4.getValue()) > 0)
                    || (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets5.getValue()) > 0)
                    || (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets6.getValue()) > 0);
            buttonScroll.setEnabled(isClickable);
        }
    }

    @Override
    public void onDestroyView() {
        if (mArrowRightImageButton != null) {
            mArrowRightImageButton.setOnClickListener(null);
        }
        if (mArrowLeftImageButton != null) {
            mArrowLeftImageButton.setOnClickListener(null);
        }
        if (mSynthesisImageButton != null) {
            mSynthesisImageButton.setOnClickListener(null);
        }

        if (buttonScroll != null) {
            buttonScroll.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected boolean isPermitCancel() {
        return true;
    }

    @Override
    protected boolean isPermitTouchOutside() {
        return false;
    }

    // ------------------------------
    // View.OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        if (isEventLocked()) {
            return;
        }
        lockEvent();

        switch (v.getId()) {
            case R.id.buttonArrowLeft:
                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                // 左矢印ボタン
                mBookViewPager.arrowScroll(View.FOCUS_LEFT);
                unlockEvent();
                break;
            case R.id.buttonArrowRight:
                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                // 右矢印ボタン
                mBookViewPager.arrowScroll(View.FOCUS_RIGHT);
                unlockEvent();
                break;
            case R.id.buttonSynthesis:
                // 合成ボタン
                clickSynthesisButton();
                break;
            case R.id.buttonScroll:
                // 合成ボタン
                sendResult(RESULT_OK, new SynthesisDialogResponse(SynthesisDialogResponse.ACTION_SCROLL));
                break;
            default: {
                unlockEvent();
                break;
            }
        }
    }

    // ------------------------------
    // ViewPager.OnPageChangeListener
    // ------------------------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setShowPage(position);
    }

    @Override
    public void onPageSelected(int position) {
        if (getApp() != null) {
            getApp().getSeManager().playSe(SeData.PAGE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // ------------------------------
    // OnAdapterListener
    // ------------------------------
    @Override
    public void onEvent(int eventCode, Object data) {
        if (eventCode == DialogCode.Synthesis.getValue()) {
            if (data != null && data instanceof BookGarbageData) {
                BookGarbageData garbageData = (BookGarbageData) data;
                renewalSelectItemList(garbageData);
            }
        }
    }

    // ------------------------------
    // function
    // ------------------------------
    /**
     * ViewPagerの設定をする
     */
    private void setBookViewPager(View view) {
        // 図鑑のリストを読み込み
        List<BookGarbageData> garbageDataList = BookGarbageData.getList();

        mBookViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        SynthesisAdapter adapter = new SynthesisAdapter(getApplicationContext(), garbageDataList, this);
        mBookViewPager.setAdapter(adapter);
        mBookViewPager.addOnPageChangeListener(this);
    }

    /**
     * 矢印ボタンの設定をする
     */
    private void setArrowButton(View view) {
        mArrowLeftImageButton = (ImageButton) view.findViewById(R.id.buttonArrowLeft);
        mArrowLeftImageButton.setOnClickListener(this);

        mArrowRightImageButton = (ImageButton) view.findViewById(R.id.buttonArrowRight);
        mArrowRightImageButton.setOnClickListener(this);
    }

    /**
     * ViewPagerのページに応じた番号、矢印の状態にする
     */
    private void setShowPage(int position) {
        if (getActivity() == null || getDialog() == null) {
            return;
        }

        if (mBookViewPager == null || mBookViewPager.getAdapter() == null)
            return;

        int maxPage = mBookViewPager.getAdapter().getCount();

        if (mArrowLeftImageButton != null) {
            mArrowLeftImageButton.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        }

        if (mArrowRightImageButton != null) {
            mArrowRightImageButton.setVisibility(position == maxPage - 1 ? View.GONE : View.VISIBLE);
        }

        for (int i = 1; i <= maxPage; i++) {
            String idString = "imageViewPage" + i;
            int resourceId = getResources().getIdentifier(idString, "id", getActivity().getPackageName());
            ImageView selectItemImageView = (ImageView) getDialog().findViewById(resourceId);
            if (selectItemImageView != null) {
                selectItemImageView.setSelected((i - 1) == position);
            }
        }
    }

    /**
     * 合成ボタンの設定
     */
    private void setSynthesisButton(View view) {
        mSynthesisImageButton = (ImageButton) view.findViewById(R.id.buttonSynthesis);
        new ButtonAnimationManager(mSynthesisImageButton, this);
    }

    /**
     * 合成するアイテムの選択の設定
     */
    private void setSelectSynthesisItem(View view) {
        // 選択アイテムの初期化
        mSelectGarbageList = new ArrayList<>();

        mLeftSynthesisImageButton = (ImageButton) view.findViewById(R.id.imageButtonSynthesis1);
        mMiddelSynthesisImageButton = (ImageButton) view.findViewById(R.id.imageButtonSynthesis2);
        mRightSynthesisImageButton = (ImageButton) view.findViewById(R.id.imageButtonSynthesis3);
    }

    /**
     * 選択アイテムのリストを更新する
     */
    private void renewalSelectItemList(BookGarbageData garbageData) {
        if (garbageData == null || mSelectGarbageList == null) {
            return;
        }
        // 選択しているアイテムをクリックしたとき
        if (mSelectGarbageList.contains(garbageData)) {
            mSelectGarbageList.remove(garbageData);
        } else {
            // 選択されていないアイテムをクリックしたとき
            if (mSelectGarbageList.size() <= 2 && !garbageData.isLocked()) {
                // 選択アイテムが2個以下かつロックされていないアイテムのとき
                mSelectGarbageList.add(garbageData);
            }
        }
        // ViewPagerの選択アイテムを更新する
        if (mBookViewPager != null) {
            SynthesisAdapter adapter = (SynthesisAdapter) mBookViewPager.getAdapter();
            if (adapter != null) {
                adapter.setItemSelectList(mSelectGarbageList);
                // ViewPagerの表示を更新する
                adapter.notifyDataSetChanged();
            }
        }
        showSelectItem();
    }

    /**
     * 選択したアイテムを合成アイテム欄に表示する
     */
    private void showSelectItem() {
        if (mSelectGarbageList == null) {
            return;
        }
        int index = 0;
        for (BookGarbageData data : mSelectGarbageList) {
            GarbageId garbageId = data.getGarbageId();
            if (garbageId != null) {
                int resourceId = garbageId.getSynthesisResourceId();
                switch (index) {
                    case 0:
                        if (mLeftSynthesisImageButton != null) {
                            mLeftSynthesisImageButton.setImageResource(resourceId);
                        }
                        break;
                    case 1:
                        if (mMiddelSynthesisImageButton != null) {
                            mMiddelSynthesisImageButton.setImageResource(resourceId);
                        }
                        break;
                    case 2:
                        if (mRightSynthesisImageButton != null) {
                            mRightSynthesisImageButton.setImageResource(resourceId);
                        }
                        break;
                }
            }
            index++;
        }
        // リストの数でアイテムの表示を設定
        if (mLeftSynthesisImageButton != null) {
            mLeftSynthesisImageButton.setVisibility(index > 0 ? View.VISIBLE : View.GONE);
        }
        if (mMiddelSynthesisImageButton != null) {
            mMiddelSynthesisImageButton.setVisibility(index > 1 ? View.VISIBLE : View.GONE);
        }
        if (mRightSynthesisImageButton != null) {
            mRightSynthesisImageButton.setVisibility(index > 2 ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 合成ボタンを押したとき
     */
    private void clickSynthesisButton() {
        if (mSelectGarbageList == null) {
            return;
        }
        if (mSelectGarbageList.size() == 3) {
            // アイテムが3つ選択されているとき
            sendResult(
                    RESULT_OK,
                    new SynthesisDialogResponse(
                            SynthesisDialogResponse.ACTION_SYNTHESIS,
                            mSelectGarbageList.get(0),
                            mSelectGarbageList.get(1),
                            mSelectGarbageList.get(2)));
        } else {
            // アイテムが3つ選択されていないのでエラーダイアログを表示する
            sendResult(RESULT_NG, null);
        }
    }

    /**
     * 選択アイテムをクリアする
     */
    private void clearSelectItem() {
        mSelectGarbageList = new ArrayList<>();
        // ViewPagerの選択アイテムを更新する
        if (mBookViewPager != null) {
            SynthesisAdapter adapter = (SynthesisAdapter) mBookViewPager.getAdapter();
            if (adapter != null) {
                adapter.setItemSelectList(mSelectGarbageList);
                // ViewPagerの表示を更新する
                adapter.notifyDataSetChanged();
            }
        }
        showSelectItem();
    }

    /**
     * 合成成功を受けとる
     */
    public void receiveSynthesisSuccess() {
        clearSelectItem();
    }

    // ------------------------------
    // Inner-Class
    // ------------------------------
    public static class SynthesisDialogResponse implements Serializable {

        public static final int ACTION_SYNTHESIS = 1;
        public static final int ACTION_SCROLL = 2;

        public int action;
        public BookGarbageData data1;
        public BookGarbageData data2;
        public BookGarbageData data3;

        public SynthesisDialogResponse(int action) {
            this.action = action;
        }

        public SynthesisDialogResponse(int action, BookGarbageData data1, BookGarbageData data2, BookGarbageData data3) {
            this.action = action;
            this.data1 = data1;
            this.data2 = data2;
            this.data3 = data3;
        }
    }

}

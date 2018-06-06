package app.dialog;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import java.util.ArrayList;
import java.util.List;

import app.animation.KiraAnimationManager;
import app.animation.button.ButtonAnimationManager;
import app.data.BookGarbageData;
import app.define.AnimationId;
import app.define.DialogCode;
import app.define.GarbageId;
import app.jni.JniBridge;
import app.number.NumberUtils;
import app.view.OutlineTextView;
import common.dialog.GBDialogBase;

/**
 *
 */
public class SynthesisSuccessDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();
    private static final String ARG_KEY_DATA = "#2#" + System.currentTimeMillis();
    private static final int KIRAKIRA = 7;

    // ------------------------------
    // Member
    // ------------------------------
    private ImageButton mBackImageButton = null;
    private List<KiraAnimationManager> mAnimationList = null;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static SynthesisSuccessDialog newInstance(String name) {
        SynthesisSuccessDialog dialog = new SynthesisSuccessDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        dialog.setArguments(args);
        return dialog;
    }

    public static SynthesisSuccessDialog newInstance(String name, BookGarbageData garbagedata) {
        SynthesisSuccessDialog dialog = new SynthesisSuccessDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        args.putSerializable(ARG_KEY_DATA, garbagedata);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.SynthesisSuccess.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_synthesis_success, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }
        // 図鑑詳細パネルの設定
        setPanel(view);

        // もどるボタン
        setSynthesisButton(view);
        // キラキラのアニメーション
        setKiraAnimation(view);

        OutlineTextView outlineTextViewPoint = (OutlineTextView) view.findViewById(R.id.outlineTextViewPoint);
        if (outlineTextViewPoint != null) {
            outlineTextViewPoint.setOutlineTextAligh(OutlineTextView.ALIGN_RIGHT);
            BookGarbageData garbageData = getGarbageData();
            if (garbageData != null) {
                int point = JniBridge.nativeGetGarbageBonus(garbageData.getGarbageId().getValue());
                outlineTextViewPoint.setText(NumberUtils.getNumberFormatText(point) + "P");
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // キラキラのアニメーションを開始
        if (mAnimationList != null) {
            for (KiraAnimationManager animationManager : mAnimationList) {
                if (animationManager != null) {
                    animationManager.start();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // キラキラのアニメーションを停止
        if (mAnimationList != null) {
            for (KiraAnimationManager animationManager : mAnimationList) {
                if (animationManager != null) {
                    animationManager.stop();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mBackImageButton != null) {
            mBackImageButton.setOnClickListener(null);
        }

        // キラキラのアニメーションを削除
        if (mAnimationList != null) {
            for (KiraAnimationManager animationManager : mAnimationList) {
                if (animationManager != null) {
                    animationManager.release();
                    animationManager = null;
                }
            }
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
    // function
    // ------------------------------
    /**
     * BookGarbageDataの取得
     */
    private BookGarbageData getGarbageData() {
        return (BookGarbageData) getArguments().getSerializable(ARG_KEY_DATA);
    }

    /**
     * 詳細パネルの設定
     */
    private void setPanel(View view) {
        BookGarbageData garbageData = getGarbageData();
        if (garbageData == null) {
            return;
        }
        GarbageId garbageId = garbageData.getGarbageId();
        if (garbageId == null) {
            return;
        }
        Drawable drawable = getImageDrawable(garbageId.getDetailResourceId());
        ImageView mBackgroundImageView = (ImageView) view.findViewById(R.id.imageViewBackground);
        if (drawable != null && mBackgroundImageView != null) {
            mBackgroundImageView.setImageDrawable(drawable);
        }
    }

    /**
     * もどるボタンの設定
     */
    private void setSynthesisButton(View view) {
        mBackImageButton = (ImageButton) view.findViewById(R.id.imageButtonSynthesisBack);
        new ButtonAnimationManager(mBackImageButton, this);
    }

    /**
     * キラキラの設定
     */
    private void setKiraAnimation(View view) {
        if (getActivity() == null) {
            return;
        }
        mAnimationList = new ArrayList<>();
        // 大きいキラキラは4つ、小さいキラキラは3つ
        for (int i = 1; i <= KIRAKIRA; i++) {
            String idString = "imageViewKiraKira" + i;
            int resourceId = getResources().getIdentifier(idString, "id", getActivity().getPackageName());
            ImageView kiraImageView = (ImageView) view.findViewById(resourceId);
            KiraAnimationManager animationManager = new KiraAnimationManager(AnimationId.ALPHAANIMATION.getValue(), kiraImageView, null);
            long duration = (long) (Math.random() * 1000) + 500;
            long offset = (long) (Math.random() * 500);
            animationManager.setAnimationTimer(duration, offset);
            animationManager.createViewAnimation();
            mAnimationList.add(animationManager);
        }
    }

    /**
     * drawableをAPILevelでわけて取得する
     */
    @SuppressWarnings("deprecation")
    private Drawable getImageDrawable(int resourceId) {
        if (getActivity() == null || resourceId == 0) {
            return null;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP以上のとき
            return getActivity().getDrawable(resourceId);
        } else {
            return getActivity().getResources().getDrawable(resourceId);
        }
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
            case R.id.imageButtonSynthesisBack:
                // もどる
                if (getDialog() != null) {
                    getDialog().onBackPressed();
                }
                unlockEvent();
                break;
            default: {
                unlockEvent();
                break;
            }
        }
    }
}

package app.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.topmission.gomipoi.R;
import com.daydelight.imagefetcher.BitmapAsyncLoadHelper;
import com.daydelight.imagefetcher.ImageData;
import com.daydelight.imagefetcher.ImageFetcher;
import com.daydelight.imagefetcher.SimpleImageDataAdapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.adapter.ThumbnailAdapter;
import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import app.jni.JniBridge;
import common.activity.GBActivityBase;
import common.dialog.GBDialogBase;
import lib.log.DebugLog;

/**
 *
 */
public class PicturePoiDialog extends GBDialogBase
        implements View.OnClickListener, ThumbnailAdapter.OnThumbnailAdapterListener {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int PERMISSION_EXSTORAGE_REQUEST = 100;

    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private Button buttonSelect;
    private GridView gridViewPoi;
    private TextView textViewMessage;

    private BitmapAsyncLoadHelper mImageLoader;

    private List<ImageData> mSelectedList;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static PicturePoiDialog newInstance(String name) {
        PicturePoiDialog dialog = new PicturePoiDialog();
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
        return DialogCode.PicturePoi.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        @SuppressLint("InflateParams")
        View view = getInflater().inflate(R.layout.dialog_picture_poi, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        buttonSelect = (Button) view.findViewById(R.id.buttonSelect);
        if (buttonSelect != null) {
            new ButtonAnimationManager(buttonSelect, this);
        }

        gridViewPoi = (GridView) view.findViewById(R.id.gridViewPoi);

        textViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
        if (textViewMessage != null) {
            String currentDate = JniBridge.nativeGetCurrentDate();
            String savedDate = getApp().getPreferenceManager().getPicturePoiDate();
            int count = getApp().getPreferenceManager().getPicturePoiCount();
            if (currentDate != null && !currentDate.equals(savedDate)) {
                count = 0;
            }

            if (count >= 10) {
                textViewMessage.setText("本日のボーナスは受け取り済みです。");
            } else {
                textViewMessage.setText("ボーナス獲得まで、あと" + (GBActivityBase.GEM_EXCHANGE_COUNT - count) + "枚！");
            }
        }

        mSelectedList = new ArrayList<>();
        mImageLoader = new BitmapAsyncLoadHelper();
        loadPictures();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadPictures();
    }

    @Override
    public void onDestroyView() {
        if (buttonSelect != null) {
            buttonSelect.setOnClickListener(null);
        }

        if (mSelectedList != null) {
            mSelectedList.clear();
            mSelectedList = null;
        }

        if (mImageLoader != null) {
            if (!mImageLoader.isShutdown()) {
                mImageLoader.shutdown();
            }
            mImageLoader = null;
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

    @Override
    public void lockEvent() {
        super.lockEvent();

        ((ThumbnailAdapter)gridViewPoi.getAdapter()).lockEvent();
    }

    @Override
    public void unlockEvent() {
        super.unlockEvent();

        ((ThumbnailAdapter)gridViewPoi.getAdapter()).unlockEvent();
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
            case R.id.buttonSelect: {
                // 既に受け取り済みの処理
                if (getApp() != null && !getApp().getPreferenceManager().isEnabledReceiveBonus()) {
                    sendResult(
                            RESULT_OK,
                            new PicturePoiDialogResponse(
                                    PicturePoiDialogResponse.ACTION_NOT_BONUS_CONFIRM,
                                    0,
                                    0));
                    return;
                }

                sendResult(RESULT_OK, delete(PicturePoiDialogResponse.ACTION_BONUS_DELETE));
                break;
            }

            default: {
                unlockEvent();
                break;
            }

        }
    }

    // ------------------------------
    // ThumbnailAdapter.OnThumbnailAdapterListener
    // ------------------------------
    @Override
    public boolean ThumbnailAdapter_isSelected(ImageData item) {
        return (mSelectedList != null && item != null) && mSelectedList.contains(item);
    }

    @Override
    public void ThumbnailAdapter_onChangeSelected(ImageData item) {
        if (mSelectedList == null || item == null) {
            return;
        }

        if (mSelectedList.contains(item)) {
            mSelectedList.remove(item);
        } else {
            mSelectedList.add(item);
        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * 端末の写真一覧を読み込む
     */
    public final void loadPictures() {
        if (ActivityCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            sendResult(RESULT_CANCEL, null);
            return;
        }

        ImageFetcher.getImageData(getApplicationContext(), new ImageFetcher.OnImageDataListener() {

            @Override
            public void onImageDataGot(ArrayList<ImageData> imageDatas) {
                ThumbnailAdapter adapter;
                if (gridViewPoi.getAdapter() != null
                        && gridViewPoi.getAdapter() instanceof SimpleImageDataAdapter) {
                    adapter = (ThumbnailAdapter) gridViewPoi.getAdapter();
                    adapter.setImageDatas(imageDatas);
                } else {
                    adapter = new ThumbnailAdapter(
                            getApplicationContext(),
                            imageDatas,
                            mImageLoader);
                    adapter.setOnThumbnailAdapterListener(PicturePoiDialog.this);
                    gridViewPoi.setAdapter(adapter);
                }


            }
        });
    }

    public void deletePicture() {
        sendResult(RESULT_OK, delete(PicturePoiDialogResponse.ACTION_NOT_BONUS_DELETE));
    }

    private PicturePoiDialogResponse delete(int action) {
        if (ActivityCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            unlockEvent();
            return null;
        }

        if (mSelectedList == null || mSelectedList.size() == 0) {
            unlockEvent();
            return null;
        }

        int successCount = 0;
        int failedCount = 0;
        for (int i = mSelectedList.size() - 1; i >= 0; i--) {
            ImageData data = mSelectedList.get(i);
            String fileName = data.getFileName();
            if (fileName != null) {
                File file = new File(fileName);
                if (file.exists()) {
                    if (file.delete()) {
                        successCount += 1;
                        String[] proj = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
                        Cursor cursor = getApplicationContext().getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                proj,
                                MediaStore.Images.Media.DATA + " = ?",
                                new String[] {fileName},
                                null);
                        if(cursor != null && cursor.getCount() != 0) {
                            cursor.moveToFirst();
                            Uri deleteUri = ContentUris.appendId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon(),
                                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))).build();
                            getApplicationContext().getContentResolver().delete(deleteUri, null, null);
                            cursor.close();
                        }
                        ((ThumbnailAdapter) gridViewPoi.getAdapter()).remove(data);
                    } else {
                        failedCount += 1;
                    }
                } else {
                    failedCount += 1;
                }
            } else {
                failedCount += 1;
            }
        }
        mSelectedList.clear();
        ((ThumbnailAdapter)gridViewPoi.getAdapter()).onCompletedDelete();

        return new PicturePoiDialogResponse(action, successCount, failedCount);
    }

    // ------------------------------
    // Inner-Class
    // ------------------------------
    public static class PicturePoiDialogResponse implements Serializable {

        // ------------------------------
        // Define
        // ------------------------------
        public static final int ACTION_BONUS_DELETE = 1;
        public static final int ACTION_NOT_BONUS_DELETE = 2;
        public static final int ACTION_NOT_BONUS_CONFIRM = 3;

        // ------------------------------
        // Member
        // ------------------------------
        public int action;
        public int successCount;
        public int failedCount;

        // ------------------------------
        // Constructor
        // ------------------------------
        public PicturePoiDialogResponse(int action, int successCount, int failedCount) {
            this.action = action;
            this.successCount = successCount;
            this.failedCount = failedCount;
        }
    }
}

package app.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.topmission.gomipoi.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import lib.qrcode.QRCodeUtil;

/**
 *
 * Created by kazuya on 2017/09/28.
 */

public class QRReadActivity extends AppCompatActivity {
    public static final String RESULT_TEXT_KEY = "result";

    private static final class CustomCaptureManager extends CaptureManager {
        @NonNull
        private Activity mActivity;
        private boolean destroyed = false;
        private boolean finishWhenClosed = false;

        CustomCaptureManager(@NonNull Activity activity, DecoratedBarcodeView barcodeView) {
            super(activity, barcodeView);
            mActivity = activity;
        }

        @Override
        protected void displayFrameworkBugMessageAndExit() {
            if (mActivity.isFinishing() || this.destroyed || finishWhenClosed) {
                return;
            }

            Activity activity = mActivity;

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
            builder.setTitle(activity.getString(com.google.zxing.client.android.R.string.zxing_app_name));
            builder.setMessage(activity.getString(R.string.failed_to_start_camera));
            builder.setPositiveButton(com.google.zxing.client.android.R.string.zxing_button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            builder.show();
        }

        private void finish() {
            mActivity.finish();
        }

        @Override
        protected void closeAndFinish() {
            super.closeAndFinish();
            finishWhenClosed = true;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            destroyed = true;
        }
    }

    private CustomCaptureManager mCaptureManager;

    @Nullable
    private CompoundBarcodeView mBarcodeView;

    @Nullable
    private Disposable mDisposable;

    @Nullable
    private AlertDialog mDialog;

    @NonNull
    private PublishSubject<String> mSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_qr);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("QRコードリーダー");
        }

        CompoundBarcodeView barcodeView = (CompoundBarcodeView) findViewById(R.id.barcodeView);

        mCaptureManager = new CustomCaptureManager(this, barcodeView);
        mBarcodeView = barcodeView;

        mDisposable = toScanObservable(barcodeView)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (TextUtils.isEmpty(s)) {
                    showInvalidQRCode().subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            backPrevious();
                        }
                    });
                } else {
                    applyFriendCode(s);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCaptureManager != null) {
            mCaptureManager.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCaptureManager != null) {
            mCaptureManager.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }

        super.onDestroy();
        if (mCaptureManager != null) {
            mCaptureManager.onDestroy();
        }
    }

    private void backPrevious() {
        finish();
    }

    /**
     *
     * @param resultText resultText
     */
    private void applyFriendCode(@NonNull String resultText) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_TEXT_KEY, resultText);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    /**
     *
     */
    private Observable<String> showInvalidQRCode() {
        if (mDialog != null && mDialog.isShowing()) {
            return Observable.empty();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_message_qr_code_invalid));
        builder.setTitle(R.string.confirm);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSubject.onNext("");
            }
        });
        mDialog = builder.create();
        mDialog.show();

        return mSubject;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCaptureManager.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mCaptureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mBarcodeView != null) {
            return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     *
     * @param barcodeView barcodeView
     * @return scanObservable
     */
    private Observable<String> toScanObservable(@NonNull final CompoundBarcodeView barcodeView) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter<String> e) throws Exception {
                barcodeView.decodeSingle(new BarcodeCallback() {
                    @Override
                    public void barcodeResult(BarcodeResult result) {
                        String resultText = result.getText();
                        // validate result text
                        String friendCode = QRCodeUtil.friendCode(resultText);
                        if (friendCode != null) {
                            e.onNext(resultText);
                            e.onComplete();
                        } else {
                            e.onNext("");
                        }
                    }

                    @Override
                    public void possibleResultPoints(List<ResultPoint> resultPoints) {
                        /* no-op */
                    }
                });
            }
        });
    }
}

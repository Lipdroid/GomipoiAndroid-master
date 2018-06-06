package app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import common.fragment.GBFragmentBase;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lib.qrcode.QRCodeGenerator;
import lib.qrcode.QRCodeUtil;

/**
 * show QR code
 * Created by kazuya on 2017/10/04.
 */
public class ShowQRFragment extends GBFragmentBase {

    @Nullable
    private Disposable mDisposable;

    /**
     * new instance
     *
     * @param friendCode friendCode
     * @return ShowQRFragment instance
     */
    public static ShowQRFragment newInstance(@NonNull String friendCode) {
        ShowQRFragment fragment = new ShowQRFragment();
        Bundle arguments = new Bundle();
        arguments.putString("friendCode", friendCode);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list_qr, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View root = getView();
        if (root != null) {
            // show friend code
            showFriendCode(root);

            // show QR code
            String friendCode = getArguments().getString("friendCode");
            if (friendCode != null) {
                mDisposable = toGenerateQRCodeObservable(friendCode).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (o instanceof Bitmap) {
                            progressBar(root).setVisibility(View.INVISIBLE);
                            showQRCode(root, (Bitmap) o);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroyView();
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    /**
     * show friend code
     *
     * @param root root
     */
    private void showFriendCode(@NonNull View root) {
        String friendCode = getArguments().getString("friendCode");
        if (friendCode == null) {
            return;
        }
        TextView codeView = (TextView) root.findViewById(R.id.textViewCode);
        codeView.setText(friendCode);
    }

    /**
     * display QR code
     *
     * @param root   view
     * @param bitmap QR code bitmap
     */
    private void showQRCode(@NonNull View root, @Nullable Bitmap bitmap) {
        ImageView qrView = (ImageView) root.findViewById(R.id.imageView_qr);
        qrView.setImageBitmap(bitmap);
    }

    /**
     * create QR code
     *
     * @param friendCode friendCode
     * @return Observable
     */
    private Observable<Bitmap> toGenerateQRCodeObservable(@NonNull final String friendCode) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                e.onNext(QRCodeGenerator.createQRCode(QRCodeUtil.qrCodeString(friendCode)));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * progress
     *
     * @param root root view
     * @return progress view instance
     */
    private ProgressBar progressBar(View root) {
        return (ProgressBar) root.findViewById(R.id.progress_bar);
    }
}

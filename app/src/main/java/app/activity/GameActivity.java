package app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.topmission.gomipoi.R;

import app.data.http.UserAppJewelParam;
import app.define.ChangeActivityCode;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.dialog.ItemDialog;
import app.dialog.MessageDialog;
import app.dialog.PicturePoiDialog;
import app.fragment.GameFragment;
import app.sound.BgmManager;
import common.activity.GBActivityBase;
import lib.dialog.DialogBase;
import lib.log.DebugLog;

/**
 *
 */
public class GameActivity extends GBActivityBase {

    // ------------------------------
    // Define
    // ------------------------------
    private static final int REQUEST_BUY_GEM_FROM_ITEM = 1;

    // ------------------------------
    // Member
    // ------------------------------

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(
                GameFragment.newInstance(),
                GameFragment.getName(getApplicationContext()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_BUY_GEM_FROM_ITEM: {
                DialogBase currentDialog = getCurrentActiveDialog();
                // ItemのDialogが表示されている場合
                if (currentDialog != null && currentDialog instanceof ItemDialog) {
                    ((ItemDialog) currentDialog).onChangedGem();
                    currentDialog.unlockEvent();
                }

                Fragment fragment = getCurrentActiveFragment();
                if (fragment != null && fragment instanceof GameFragment) {
                    ((GameFragment)fragment).onRefresh();
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PicturePoiDialog.PERMISSION_EXSTORAGE_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onShowDialog(DialogCode.PicturePoi.getValue(), null);
                    return;
                }

                // 権限エラー
                onShowDialog(DialogCode.PermissionError.getValue(), null);
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.layoutFragment;
    }

    @Override
    protected int getResizeBaseViewId() {
        return R.id.layoutFragment;
    }

    // ------------------------------
    // OnFragmentListener
    // ------------------------------
    @Override
    public void onChangedActivity(int requestCode, Object data) {
        ChangeActivityCode code = ChangeActivityCode.valueOf(requestCode);
        switch (code) {
            case Shop: {
                startActivityForResult(
                        new Intent(getApplicationContext(), ShopActivity.class),
                        REQUEST_BUY_GEM_FROM_ITEM);
                break;
            }

            default: {
                super.onChangedActivity(requestCode, data);
                break;
            }
        }
    }

    @Override
    public void onShowDialog(int dialogCode, Object data) {
        switch (DialogCode.valueOf(dialogCode)) {
            case PicturePoi: {
                if (ActivityCompat.checkSelfPermission(
                        getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PicturePoiDialog.PERMISSION_EXSTORAGE_REQUEST);
                    return;
                }

                super.onShowDialog(dialogCode, data);
                break;
            }

            default: {
                super.onShowDialog(dialogCode, data);
                break;
            }
        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------

    // ------------------------------
    // Function
    // ------------------------------

    /**
     * BGMの種類。デフォルトでNORMAL
     */
    @Override
    protected BgmManager.BgmType getBgmType() {
        Fragment fragment = getCurrentActiveFragment();
        if (fragment != null && fragment instanceof GameFragment) {
            return ((GameFragment) fragment).getBgmType();
        }
        else return BgmManager.BgmType.NORMAL;
    }
}

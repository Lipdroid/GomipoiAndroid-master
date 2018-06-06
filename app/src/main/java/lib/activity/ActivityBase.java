package lib.activity;

import android.app.ActionBar;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import lib.application.ApplicationBase;

/**
 * Activity基幹クラス
 */
public class ActivityBase extends FragmentActivity {

	// ------------------------------
	// Member
	// ------------------------------
	protected Handler mHandler;
	private Handler mStartHandler;
	
	// ------------------------------
	// Override
	// ------------------------------
	@Override
	protected void onCreate(final Bundle arg0) {
		super.onCreate(arg0);
		
		// 必要であれば、アクションバーを削除する
		if (isHiddenActionBar()) {
			hiddenActiionBar();
		}
		
		// ボリュームキーの接続先をセットする
		setVolumeControlStream(getStreamType());
		
		// 必要であれば、デバイススリープを禁止する
		if (isKeepScreenOn()) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}

		mHandler = new Handler();
		mStartHandler = new Handler();
		mStartHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mStartHandler.removeCallbacks(this);
				mStartHandler = null;
				onCreateHandler(arg0);
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		unlockEvent();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mStartHandler = null;
		mHandler = null;
		super.onDestroy();
		System.runFinalization();
		System.gc();
	}

	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * HandlerでonCreateの終了を待ってから処理したいものは、ここで
	 */
	protected void onCreateHandler(Bundle bundler) {
	}
	
	/**
	 * バック処理
	 */
	protected void backScreen() {
		this.onBackPressed();
	}
	
	/**
	 * ボリュームキーを適用するストリームタイプを返す
	 */
	protected int getStreamType() {
		return AudioManager.STREAM_MUSIC;
	}
	
	/**
	 * スリープを禁止するか
	 */
	protected boolean isKeepScreenOn() {
		return true;
	}
	
	/**
	 * アクションバーを削除するか？
	 */
	protected boolean isHiddenActionBar() {
		return false;
	}
	
	//
	// イベントロック関連
	// ------------------------------
	/**
	 * ロックがかかっているかを返す
	 */
	protected boolean isLocked() {
		return ((ApplicationBase) getApplication()).isLockedEvent();
	}
	
	/**
	 * イベントロックをかける
	 */
	protected void lockEvent() {
		((ApplicationBase) getApplication()).lockEvent();
	}
	
	/**
	 * イベントロックを解除する
	 */
	protected void unlockEvent() {
		((ApplicationBase) getApplication()).unlockEvent();
	}
	
	//
	// Private
	// ------------------------------
	/**
	 * アクションバーを削除する
	 */
	private void hiddenActiionBar() {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
	}
	
}

package app.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import app.data.http.GomipoiGameSaveParam;
import app.http.ConnectionManager;

public class SaveDataServiceManager {

	// ------------------------------
	// Define
	// ------------------------------
	private final Handler dHandler = new Handler();

	// ------------------------------
	// Member
	// ------------------------------
	private OnSaveDataServiceManagerListener mManagerListener;
	private SaveDataService mService;

	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 */
	public SaveDataServiceManager() {
	}
	
	// ------------------------------
	// ServiceConnection
	// ------------------------------
	private final ServiceConnection dCompetitionServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = SaveDataService.onServiceConnected_getService(service);
			if (mService == null) {
				return;
			}

			mService.setListener(dServiceListener);
		}
	};
	
	// ------------------------------
	// Callbacks
	// ------------------------------
	private final SaveDataService.OnSaveDataServiceListener dServiceListener = new SaveDataService.OnSaveDataServiceListener() {
		@Override
		public void SaveDataService_OnShowNetworkError(ConnectionManager.RetryData retryData) {
			if (mManagerListener != null) {
				mManagerListener.onShowRetryDialog(retryData);
			}
		}

		@Override
		public void SaveDataService_OnSucceed(GomipoiGameSaveParam gameSaveParam) {
			if (mManagerListener != null) {
				mManagerListener.onSucceed(gameSaveParam);
			}
		}

		@Override
		public void SaveDataService_AlreadyMaxCapacity(GomipoiGameSaveParam newGameSaveParam) {
			if (mManagerListener != null) {
				mManagerListener.onAlreadyMaxCapacity(newGameSaveParam);
			}
		}

		@Override
		public void SaveDataService_OnStop() {
			mService = null;
		}

		@Override
		public void SaveDataService_OnAuthorizeError() {
			if (mManagerListener != null) {
				mManagerListener.onShowAuthorizeErrorDialog();
			}
		}
    };
	
	// ------------------------------
	// Accesser
	// ------------------------------
	public void setServiceManagerListener(OnSaveDataServiceManagerListener listener) {
		mManagerListener = listener;
	}

	/**
	 * ServiceConnectionを返す
	 */
	public ServiceConnection getServiceConnection() {
		return dCompetitionServiceConnection;
	}
	
	/**
	 * Serviceを返す
	 */
	public SaveDataService getService() {
		return mService;
	}
	
	/**
	 * Serviceが活動中かを返す
	 */
	public boolean isRunningService() {
		return mService != null;
	}

}

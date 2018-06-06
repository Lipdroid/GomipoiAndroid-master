package app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;

import app.data.http.GomipoiGameSaveParam;
import app.define.ConnectionCode;
import app.http.ConnectionManager;
import app.uuid.OnUUIDManagerListener;
import app.uuid.UUIDManager;

/**
 * 真剣対局の通知レシーバーを載せたServiceクラス
 */
public class SaveDataService extends Service implements OnUUIDManagerListener {
	
	// ------------------------------
	// Define
	// ------------------------------
	private final IBinder mBinder = new SaveDataServiceBinder();

	
	// ------------------------------
	// Member
	// ------------------------------
	private String mDeviceId;

	private OnSaveDataServiceListener mServiceListener;
	private ConnectionManager mConnectionManager;

	
	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public IBinder onBind(Intent arg0) {
		mDeviceId = UUIDManager.getUUID(getApplicationContext(), this);
		return mBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		onStop();
		return super.onUnbind(intent);
	}

	// ------------------------------
	// OnUUIDManagerListener
	// ------------------------------
	@Override
	public void UUIDManager_OnPermissionError() {
	}

	@Override
	public void UUIDManager_OnStorageError() {
	}

	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * BTMatchRequestMonitoringServiceのインスタンスを返す
	 */
	public static SaveDataService onServiceConnected_getService(IBinder service) {
		if (service instanceof SaveDataServiceBinder) {
			return ((SaveDataServiceBinder) service).getService();
		} else if (service instanceof SaveDataService) {
			return (SaveDataService)service;
		}
		return null;
	}
	
	/**
	 * リスナーをセットする
	 */
	public void setListener(OnSaveDataServiceListener serviceListener) {
		mServiceListener = serviceListener;
		onStart();
	}

    /**
     *
     * @param gameSaveParam gameSaveParam
     * @param deviceId deviceId
     */
    public void saveData(final GomipoiGameSaveParam gameSaveParam, final String deviceId) {
        if (mConnectionManager == null) {
            return;
        }

        mConnectionManager.gameSave(gameSaveParam, deviceId, new ConnectionManager.OnGameSaveListener() {
            @Override
            public void GameSave_Ok() {
                if (mServiceListener != null) {
                    mServiceListener.SaveDataService_OnSucceed(gameSaveParam);
                }
            }

            @Override
            public void GameSave_AlreadyMaxCapacity() {
				// TODO: 改善必要！
				// 時間差でデータベースとローカルで差が出てしまうことがあるので、
				// やむなくゴミの数を0にして、ポイント等の更新を行う
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						GomipoiGameSaveParam newParam = (GomipoiGameSaveParam) gameSaveParam.cloneParam();
						newParam.put_in_garbage_count = 0;

						if (mServiceListener != null) {
							mServiceListener.SaveDataService_AlreadyMaxCapacity(newParam);
						}
					}
				});
            }
        });
    }

    /**
     *
     * @param gameSaveParam gameSaveParam
     * @param deviceId deviceId
     */
    public void saveData(final GomipoiGameSaveParam gameSaveParam, final String deviceId, @NonNull final RequestSaveDataCallback callback) {
        if (mConnectionManager == null) {
            return;
        }

        mConnectionManager.gameSave(gameSaveParam, deviceId, new ConnectionManager.OnGameSaveListener() {
            @Override
            public void GameSave_Ok() {
                if (mServiceListener != null) {
                    mServiceListener.SaveDataService_OnSucceed(gameSaveParam);
                }

                callback.onFinish();
            }

            @Override
            public void GameSave_AlreadyMaxCapacity() {
                // TODO: 改善必要！
                // 時間差でデータベースとローカルで差が出てしまうことがあるので、
                // やむなくゴミの数を0にして、ポイント等の更新を行う
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        GomipoiGameSaveParam newParam = (GomipoiGameSaveParam) gameSaveParam.cloneParam();
                        newParam.put_in_garbage_count = 0;

                        if (mServiceListener != null) {
                            mServiceListener.SaveDataService_AlreadyMaxCapacity(newParam);
                        }

                        callback.onFinish();
                    }
                });
            }
        });
    }


    /**
	 * ゲームデータをサーバーに保存する
	 */
//	public void saveData(final GomipoiGameSaveParam gameSaveParam, final String deviceId,@NonNull final OnSaveDataServiceListener listener) {
//		if (mConnectionManager == null) {
//			return;
//		}
//
//		mConnectionManager.gameSave(gameSaveParam, deviceId, new ConnectionManager.OnGameSaveListener() {
//			@Override
//			public void GameSave_Ok() {
////                crypto.deleteStoredUserDataFile();
//
//				if (mServiceListener != null) {
//					mServiceListener.SaveDataService_OnSucceed(gameSaveParam);
//				}
//
//                listener.SaveDataService_OnSucceed(gameSaveParam);
//			}
//
//			@Override
//			public void GameSave_AlreadyMaxCapacity() {
//				// TODO: 改善必要！
//				// 時間差でデータベースとローカルで差が出てしまうことがあるので、
//				// やむなくゴミの数を0にして、ポイント等の更新を行う
//				new Handler(Looper.getMainLooper()).post(new Runnable() {
//					@Override
//					public void run() {
//						GomipoiGameSaveParam newParam = (GomipoiGameSaveParam) gameSaveParam.cloneParam();
//						newParam.put_in_garbage_count = 0;
//
//						if (mServiceListener != null) {
//							mServiceListener.SaveDataService_AlreadyMaxCapacity(newParam);
//						}
//					}
//				});
//			}
//		});
//	}

	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * 処理の開始
	 */
	protected void onStart() {
		mConnectionManager = new ConnectionManager(
				getApplicationContext(),
				new ConnectionManager.OnConnectionManagerListener() {

			@Override
			public void ConnectionManager_OnDeleteLoginDataForLogout(boolean isNeedShowDialog) {
			}

			@Override
			public void ConnectionManager_OnAuthorizedError() {
				if (mServiceListener != null) {
					mServiceListener.SaveDataService_OnAuthorizeError();
				}
			}

			@Override
			public void ConnectionManager_OnShowConnectDialog() {
			}

			@Override
			public void ConnectionManager_OnHideConnectDialog() {
			}

			@Override
			public void ConnectionManager_OnShowNetworkErrorDialog() {
				if (mServiceListener != null) {
					mServiceListener.SaveDataService_OnShowNetworkError(null);
				}
			}

			@Override
			public void ConnectionManager_OnShowGetNetworkErrorDialog(ConnectionManager.RetryData retryData) {
				if (mServiceListener != null) {
					mServiceListener.SaveDataService_OnShowNetworkError(retryData);
				}
			}

			@Override
			public void ConnectionManager_OnShowPostNetworkErrorDialog(ConnectionManager.RetryData retryData) {
				if (mServiceListener != null) {
					mServiceListener.SaveDataService_OnShowNetworkError(retryData);
				}
			}

			@Override
			public void ConnectionManager_OnError(ConnectionCode code) {
				if (mServiceListener != null) {
					mServiceListener.SaveDataService_OnShowNetworkError(null);
				}
			}
		});
		if (mDeviceId == null) {
			mDeviceId = UUIDManager.getUUID(getApplicationContext(), this);
		}
		mConnectionManager.setDeviceId(mDeviceId);
	}

	/**
	 * 処理の終了
	 */
	protected void onStop() {
		if (mConnectionManager != null) {
			mConnectionManager.onDestroy();
		}

		if (mServiceListener != null) {
			mServiceListener.SaveDataService_OnStop();
		}
		mServiceListener = null;
	}
	
	// ------------------------------
	// Callback-Interface
	// ------------------------------
	public interface OnSaveDataServiceListener {
		void SaveDataService_OnStop();
		void SaveDataService_OnShowNetworkError(ConnectionManager.RetryData retryData);
		void SaveDataService_OnSucceed(GomipoiGameSaveParam gameSaveParam);
		void SaveDataService_AlreadyMaxCapacity(GomipoiGameSaveParam newGameSaveParam);
		void SaveDataService_OnAuthorizeError();
	}

	public interface RequestSaveDataCallback {
        void onFinish();
    }

	// ------------------------------
	// Inner-Class
	// ------------------------------
	public class SaveDataServiceBinder extends Binder {
		
		// ------------------------------
		// Accesser
		// ------------------------------
		/**
		 * Serviceを返す
		 */
		public SaveDataService getService() {
			return SaveDataService.this;
		}
		
	}

}

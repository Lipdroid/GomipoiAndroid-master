package app.application;

import app.data.http.GomipoiGameSaveParam;
import app.http.ConnectionManager;

/**
 *
 */
public interface OnApplicationListener {
    void onLoadServerData();
    void onShowRetryDialog(ConnectionManager.RetryData retryData);
    void onSendSaveData(GomipoiGameSaveParam gameSaveParam);
    void onShowAuthorizeErrorDialog();
    void onAlreadyMaxCapacity(GomipoiGameSaveParam newGameSaveParam);
}

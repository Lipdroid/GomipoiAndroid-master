package app.service;

import app.data.http.GomipoiGameSaveParam;
import app.http.ConnectionManager;

/**
 * BTCompetitionMonitoringServiceManagerのコールバックインターフェース
 */
public interface OnSaveDataServiceManagerListener {
    void onShowRetryDialog(ConnectionManager.RetryData retryData);
    void onSucceed(GomipoiGameSaveParam gameSaveParam);
    void onAlreadyMaxCapacity(GomipoiGameSaveParam newGameSaveParam);
    void onShowAuthorizeErrorDialog();
}

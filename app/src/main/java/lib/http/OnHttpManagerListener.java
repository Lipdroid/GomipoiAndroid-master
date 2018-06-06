package lib.http;

/**
 */
public interface OnHttpManagerListener {

    void onStartedConnection(int connectionCode);
    void onFinishedConnection(int connectionCode, String result);
    void onErroredConnection(int connectionCode);
    void onAutorizedErroredConnection(int connectionCode);

}

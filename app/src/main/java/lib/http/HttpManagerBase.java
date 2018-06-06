package lib.http;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import lib.log.DebugLog;
import lib.network.NetworkUtils;

/**
 *
 */
public class HttpManagerBase {

    // ------------------------------
    // Define
    // ------------------------------
    public static final String ERROR_NAME_UNAUTHORIZED = "network_error_unauthorized";
    public static final int TIMEOUT_TIME = 60000;

    // ------------------------------
    // Member
    // ------------------------------
    private HashMap<Integer, AsyncTaskBase> mConnectionTaskQue;
    private OnHttpManagerListener mListener;


    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * OnHttpManagerListenerをセットする
     * @param listener [OnHttpManagerListener]
     */
    public void setOnHttpManagerListener(OnHttpManagerListener listener) {
        mListener = listener;
    }

    public void onResume() {
        for (Integer key : getConnectionTaskQue().keySet()) {
            AsyncTaskBase task = getConnectionTaskQue().get(key);
            if (task != null) {
                AsyncTaskBase newTask = task.cloneTask();
                getConnectionTaskQue().remove(key);

                newTask.execute();
                getConnectionTaskQue().put(key, newTask);
            }
        }
    }

    public void onPause() {
        for (Integer key : getConnectionTaskQue().keySet()) {
            AsyncTask task = getConnectionTaskQue().get(key);
            if (task != null) {
                task.cancel(true);
            }
        }
    }

    /**
     * Get通信を行う
     * @param connectionCode [int]
     * @param httpUrl [String]
     */
    public void startGet(int connectionCode, Context context, String httpUrl, HashMap<String, String> parameterMap, HashMap<String, Object> headerMap) {
        if (!NetworkUtils.isEnabled(context)) {
            if (mListener != null) {
                mListener.onErroredConnection(connectionCode);
            }
            return;
        }

        GetConnectionTask task = new GetConnectionTask(connectionCode, httpUrl, parameterMap, headerMap);
        task.execute();
        getConnectionTaskQue().put(connectionCode, task);
    }

    /**
     * Get通信を行う
     */
    public void startGet(int connectionCode, Context context, String protocol, String host, int port, String filePath, HashMap<String, String> parameterMap, HashMap<String, Object> headerMap) {
        if (!NetworkUtils.isEnabled(context)) {
            if (mListener != null) {
                mListener.onErroredConnection(connectionCode);
            }
            return;
        }

        GetNotDefaultConnectionTask task = new GetNotDefaultConnectionTask(connectionCode, protocol, host, port, filePath, parameterMap, headerMap);
        task.execute();
        getConnectionTaskQue().put(connectionCode, task);
    }

    /**
     * Post通信を行う
     * @param connectionCode [int]
     * @param httpUrl [String]
     * @param parameterMap [HashMap]
     */
    public void startPost(int connectionCode, Context context, String httpUrl, HashMap<String, String> parameterMap, HashMap<String, Object> headerMap) {
        if (!NetworkUtils.isEnabled(context)) {
            if (mListener != null) {
                mListener.onErroredConnection(connectionCode);
            }
            return;
        }

        PostConnectionTask task = new PostConnectionTask(connectionCode, httpUrl, parameterMap, headerMap);
        task.execute();
        getConnectionTaskQue().put(connectionCode, task);
    }

    /**
     * Post通信を行う
     */
    public void startPost(int connectionCode, Context context, String protocol, String host, int port, String filePath, HashMap<String, String> parameterMap, HashMap<String, Object> headerMap) {
        if (!NetworkUtils.isEnabled(context)) {
            if (mListener != null) {
                mListener.onErroredConnection(connectionCode);
            }
            return;
        }

        PostNotDefaultConnectionTask task = new PostNotDefaultConnectionTask(connectionCode, protocol, host, port, filePath, parameterMap, headerMap);
        task.execute();
        getConnectionTaskQue().put(connectionCode, task);
    }

    public void startPatch(int connectionCode, Context context, String protocol, String host, int port, String filePath, HashMap<String, String> parameterMap, HashMap<String, Object> headerMap) {
        if (!NetworkUtils.isEnabled(context)) {
            if (mListener != null) {
                mListener.onErroredConnection(connectionCode);
            }
            return;
        }

        PatchNotDefaultConnectionTask task = new PatchNotDefaultConnectionTask(connectionCode, protocol, host, port, filePath, parameterMap, headerMap);
        task.execute();
        getConnectionTaskQue().put(connectionCode, task);
    }

    public void startDelete(int connectionCode, Context context, String protocol, String host, int port, String filePath, HashMap<String, String> parameterMap, HashMap<String, Object> headerMap) {
        if (!NetworkUtils.isEnabled(context)) {
            if (mListener != null) {
                mListener.onErroredConnection(connectionCode);
            }
            return;
        }

        DeleteNotDefaultConnectionTask task = new DeleteNotDefaultConnectionTask(connectionCode, protocol, host, port, filePath, parameterMap, headerMap);
        task.execute();
        getConnectionTaskQue().put(connectionCode, task);
    }


    /**
     * 管理している全通信を止める
     */
    public void stopAll() {
        Object[] keyArray = getConnectionTaskQue().keySet().toArray();
        for (int i = keyArray.length - 1; i >= 0; i--) {
            stop(Integer.parseInt(keyArray[i].toString()));
        }
    }

    /**
     * 指定したConnectionCodeの通信を止める
     * @param connectionCode [int]
     */
    public void stop(int connectionCode) {
        AsyncTask task = getConnectionTaskQue().get(connectionCode);
        if (task != null) {
            task.cancel(true);
            getConnectionTaskQue().remove(connectionCode);
        }

    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * 通信の実行タスクキューを返す
     */
    protected HashMap<Integer, AsyncTaskBase> getConnectionTaskQue() {
        if (mConnectionTaskQue == null) {
            mConnectionTaskQue = new HashMap<>();
        }
        return mConnectionTaskQue;
    }

    /**
     * InputStreamからデータを取得する
     * @param is [InputStream]
     * @throws IOException
     */
    protected static String inputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }

    // ------------------------------
    // InnerClass
    // ------------------------------
    protected abstract class AsyncTaskBase extends AsyncTask<Void, Void, String> {

        // ------------------------------
        // Accesser
        // ------------------------------
        public AsyncTaskBase cloneTask() {
            return null;
        }

    }

    /**
     * Get通信を行うタスククラス
     */
    protected class GetConnectionTask extends AsyncTaskBase {

        // ------------------------------
        // Member
        // ------------------------------
        private int mKey;
        private String mUrl;
        private HashMap<String, String> mParamaterMap;
        private HashMap<String, Object> mHeaderMap;

        // ------------------------------
        // Constructor
        // ------------------------------
        public GetConnectionTask(int key, String url, HashMap<String, String> mParamaterMap, HashMap<String, Object> mHeaderMap) {
            this.mKey = key;
            this.mUrl = url;
            this.mParamaterMap = mParamaterMap;
            this.mHeaderMap = mHeaderMap;

            DebugLog.i("HttpManagerBase - GetConnectionTask Key: " + key);
            DebugLog.i("HttpManagerBase - GetConnectionTask url: " + url);
            DebugLog.i("HttpManagerBase - GetConnectionTask Param: " + mParamaterMap);
            DebugLog.i("HttpManagerBase - GetConnectionTask Header: " + mHeaderMap);
        }

        // ------------------------------
        // Override
        // ------------------------------
        @Override
        public AsyncTaskBase cloneTask() {
            return new GetConnectionTask(mKey, mUrl, mParamaterMap, mHeaderMap);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStartedConnection(mKey);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (mUrl == null) {
                return null;
            }

            HttpURLConnection connection = null;
            String result = null;
            try {
                StringBuilder paramBuilder = new StringBuilder();
                if (mParamaterMap != null) {
                    // 通信用のParameterを作成
                    int index = 0;
                    for (String key : mParamaterMap.keySet()) {
                        if (index++ != 0) {
                            paramBuilder.append("?");
                        } else {
                            paramBuilder.append("&");
                        }
                        paramBuilder.append(key);
                        paramBuilder.append("=");
                        paramBuilder.append(mParamaterMap.get(key));
                    }
                }

                URL url = new URL(mUrl + ".json" + paramBuilder.toString());
                connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(TIMEOUT_TIME);
                connection.setReadTimeout(TIMEOUT_TIME);
                if (mHeaderMap != null && mHeaderMap.size() > 0) {
                    for (String key : mHeaderMap.keySet()) {
                        connection.setRequestProperty(key, String.valueOf(mHeaderMap.get(key)));
                    }
                }
                connection.setRequestMethod("GET");
                result = inputStreamToString(connection.getInputStream());
            } catch (IOException e) {
                DebugLog.e(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("authentication")) {
                    return ERROR_NAME_UNAUTHORIZED;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                if (mListener != null) {
                    mListener.onErroredConnection(mKey);
                }
            } else {
                if (mListener != null) {
                    mListener.onFinishedConnection(mKey, s);
                }
            }
            stop(mKey);
        }

    }

    /**
     * Get通信を行うタスククラス
     */
    protected class GetNotDefaultConnectionTask extends AsyncTaskBase {

        // ------------------------------
        // Member
        // ------------------------------
        private int mKey;
        private String mProtocol;
        private String mHost;
        private int mPort;
        private String mFilePath;
        private HashMap<String, String> mParamaterMap;
        private HashMap<String, Object> mHeaderMap;

        // ------------------------------
        // Constructor
        // ------------------------------
        public GetNotDefaultConnectionTask(int key, String protocol, String host, int port, String filePath, HashMap<String, String> mParamaterMap, HashMap<String, Object> mHeaderMap) {
            this.mKey = key;
            this.mProtocol = protocol;
            this.mHost = host;
            this.mPort = port;
            this.mFilePath = filePath;
            this.mParamaterMap = mParamaterMap;
            this.mHeaderMap = mHeaderMap;

            DebugLog.i("HttpManagerBase - GetNotDefaultConnectionTask Key: " + key);
            DebugLog.i("HttpManagerBase - GetNotDefaultConnectionTask Protocol: " + protocol);
            DebugLog.i("HttpManagerBase - GetNotDefaultConnectionTask Host: " + host);
            DebugLog.i("HttpManagerBase - GetNotDefaultConnectionTask Port: " + port);
            DebugLog.i("HttpManagerBase - GetNotDefaultConnectionTask FilePath: " + filePath);
            DebugLog.i("HttpManagerBase - GetNotDefaultConnectionTask Param: " + mParamaterMap);
            DebugLog.i("HttpManagerBase - GetNotDefaultConnectionTask Header: " + mHeaderMap);
        }

        // ------------------------------
        // Override
        // ------------------------------
        @Override
        public AsyncTaskBase cloneTask() {
            return new GetNotDefaultConnectionTask(mKey, mProtocol, mHost, mPort, mFilePath, mParamaterMap, mHeaderMap);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStartedConnection(mKey);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (mProtocol == null || mHost == null || mFilePath == null) {
                return null;
            }

            HttpURLConnection connection = null;
            String result = null;
            try {
                StringBuilder paramBuilder = new StringBuilder();
                if (mParamaterMap != null && mParamaterMap.size() > 0) {
                    // 通信用のParameterを作成
                    int index = 0;
                    for (String key : mParamaterMap.keySet()) {
                        if (index++ != 0) {
                            paramBuilder.append("&");
                        } else {
                            paramBuilder.append("?");
                        }
                        paramBuilder.append(key);
                        paramBuilder.append("=");
                        paramBuilder.append(mParamaterMap.get(key));
                    }
                }

                URL url = new URL(mProtocol, mHost, mPort, mFilePath + ".json" + paramBuilder.toString());
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                if (mHeaderMap != null && mHeaderMap.size() > 0) {
                    for (String key : mHeaderMap.keySet()) {
                        connection.setRequestProperty(key, String.valueOf(mHeaderMap.get(key)));
                    }
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    return ERROR_NAME_UNAUTHORIZED;
                } else if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // 特に処理はない
                } else if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("responseCode:" + responseCode + "(" + mKey + ")");
                }

                result = inputStreamToString(connection.getInputStream());
            } catch (IOException e) {
                DebugLog.e(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("authentication")) {
                    return ERROR_NAME_UNAUTHORIZED;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                if (mListener != null) {
                    mListener.onErroredConnection(mKey);
                }
            } else if (s.equals(ERROR_NAME_UNAUTHORIZED)) {
                if (mListener != null) {
                    mListener.onAutorizedErroredConnection(mKey);
                }
            } else {
                if (mListener != null) {
                    mListener.onFinishedConnection(mKey, s);
                }
            }
            stop(mKey);
        }

    }

    /**
     * Post通信を行うタスククラス
     */
    protected class PostConnectionTask extends AsyncTaskBase {

        // ------------------------------
        // Member
        // ------------------------------
        private int mKey;
        private String mUrl;
        private HashMap<String, String> mParamaterMap;
        private HashMap<String, Object> mHeaderMap;

        // ------------------------------
        // Constructor
        // ------------------------------
        public PostConnectionTask(int key, String url, HashMap<String, String> mParamaterMap, HashMap<String, Object> mHeaderMap) {
            this.mKey = key;
            this.mUrl = url;
            this.mParamaterMap = mParamaterMap;
            this.mHeaderMap = mHeaderMap;
        }

        // ------------------------------
        // Override
        // ------------------------------
        @Override
        public AsyncTaskBase cloneTask() {
            return new PostConnectionTask(mKey, mUrl, mParamaterMap, mHeaderMap);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStartedConnection(mKey);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (mUrl == null) {
                return null;
            }

            HttpURLConnection connection = null;
            String result = null;
            try {
                URL url = new URL(mUrl + ".json");
                connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(TIMEOUT_TIME);
                connection.setReadTimeout(TIMEOUT_TIME);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                if (mHeaderMap != null && mHeaderMap.size() > 0) {
                    for (String key : mHeaderMap.keySet()) {
                        connection.setRequestProperty(key, String.valueOf(mHeaderMap.get(key)));
                    }
                }
                connection.setRequestMethod("POST");

                if (mParamaterMap != null) {
                    // 通信用のParameterを作成
                    StringBuilder paramBuilder = new StringBuilder();
                    int index = 0;
                    for (String key : mParamaterMap.keySet()) {
                        if (index++ != 0) {
                            paramBuilder.append("&");
                        }

                        paramBuilder.append(key);
                        paramBuilder.append("=");
                        paramBuilder.append(mParamaterMap.get(key));
                    }

                    // 通信用のParameterを設定
                    if (paramBuilder.length() > 0) {
                        PrintStream ps = new PrintStream(connection.getOutputStream());
                        ps.print(paramBuilder.toString());
                        ps.close();
                    }
                }

                result = inputStreamToString(connection.getInputStream());
            } catch (IOException e) {
                DebugLog.e(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("authentication")) {
                    return ERROR_NAME_UNAUTHORIZED;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                if (mListener != null) {
                    mListener.onErroredConnection(mKey);
                }
            } else {
                if (mListener != null) {
                    mListener.onFinishedConnection(mKey, s);
                }
            }
            stop(mKey);
        }

    }


    /**
     * Post通信を行うタスククラス
     */
    protected class PostNotDefaultConnectionTask extends AsyncTaskBase {

        // ------------------------------
        // Member
        // ------------------------------
        private int mKey;
        private String mProtocol;
        private String mHost;
        private int mPort;
        private String mFilePath;
        private HashMap<String, String> mParamaterMap;
        private HashMap<String, Object> mHeaderMap;

        // ------------------------------
        // Constructor
        // ------------------------------
        public PostNotDefaultConnectionTask(int key, String protocol, String host, int port, String filePath, HashMap<String, String> mParamaterMap, HashMap<String, Object> mHeaderMap) {
            this.mKey = key;
            this.mProtocol = protocol;
            this.mHost = host;
            this.mPort = port;
            this.mFilePath = filePath;
            this.mParamaterMap = mParamaterMap;
            this.mHeaderMap = mHeaderMap;

            DebugLog.i("HttpManagerBase - PostNotDefaultConnectionTask Key: " + key);
            DebugLog.i("HttpManagerBase - PostNotDefaultConnectionTask Protocol: " + protocol);
            DebugLog.i("HttpManagerBase - PostNotDefaultConnectionTask Host: " + host);
            DebugLog.i("HttpManagerBase - PostNotDefaultConnectionTask Port: " + port);
            DebugLog.i("HttpManagerBase - PostNotDefaultConnectionTask FilePath: " + filePath);
            DebugLog.i("HttpManagerBase - PostNotDefaultConnectionTask Param: " + mParamaterMap);
            DebugLog.i("HttpManagerBase - PostNotDefaultConnectionTask Header: " + mHeaderMap);
        }

        // ------------------------------
        // Override
        // ------------------------------
        @Override
        public AsyncTaskBase cloneTask() {
            return new PostNotDefaultConnectionTask(mKey, mProtocol, mHost, mPort, mFilePath, mParamaterMap, mHeaderMap);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStartedConnection(mKey);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (mProtocol == null || mHost == null || mFilePath == null) {
                return null;
            }

            HttpURLConnection connection = null;
            String result = null;
            try {
                URL url = new URL(mProtocol, mHost, mPort, mFilePath + ".json");
                connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(TIMEOUT_TIME);
                connection.setReadTimeout(TIMEOUT_TIME);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");

                if (mHeaderMap != null && mHeaderMap.size() > 0) {
                    for (String key : mHeaderMap.keySet()) {
                        connection.setRequestProperty(key, String.valueOf(mHeaderMap.get(key)));
                    }
                }

                if (mParamaterMap != null) {
                    // 通信用のParameterを作成
                    StringBuilder paramBuilder = new StringBuilder();
                    int index = 0;
                    for (String key : mParamaterMap.keySet()) {
                        if (index++ != 0) {
                            paramBuilder.append("&");
                        }

                        paramBuilder.append(key);
                        paramBuilder.append("=");
                        paramBuilder.append(mParamaterMap.get(key));
                    }

                    // 通信用のParameterを設定
                    if (paramBuilder.length() > 0) {
                        PrintStream ps = new PrintStream(connection.getOutputStream());
                        ps.print(paramBuilder.toString());
                        ps.close();
                    }
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    return ERROR_NAME_UNAUTHORIZED;
                } else if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // 特に処理はない
                } else if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("responseCode:" + responseCode + "(" + mKey + ")");
                }

                result = inputStreamToString(connection.getInputStream());
            } catch (IOException e) {
                DebugLog.e(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("authentication")) {
                    return ERROR_NAME_UNAUTHORIZED;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                if (mListener != null) {
                    mListener.onErroredConnection(mKey);
                }
            } else if (s.equals(ERROR_NAME_UNAUTHORIZED)) {
                if (mListener != null) {
                    mListener.onAutorizedErroredConnection(mKey);
                }
            } else {
                if (mListener != null) {
                    mListener.onFinishedConnection(mKey, s);
                }
            }
            stop(mKey);
        }

    }

    /**
     * Patch通信を行うタスククラス
     */
    protected class PatchNotDefaultConnectionTask extends AsyncTaskBase {

        // ------------------------------
        // Member
        // ------------------------------
        private int mKey;
        private String mProtocol;
        private String mHost;
        private int mPort;
        private String mFilePath;
        private HashMap<String, String> mParamaterMap;
        private HashMap<String, Object> mHeaderMap;

        // ------------------------------
        // Constructor
        // ------------------------------
        public PatchNotDefaultConnectionTask(int key, String protocol, String host, int port, String filePath, HashMap<String, String> mParamaterMap, HashMap<String, Object> mHeaderMap) {
            this.mKey = key;
            this.mProtocol = protocol;
            this.mHost = host;
            this.mPort = port;
            this.mFilePath = filePath;
            this.mParamaterMap = mParamaterMap;
            this.mHeaderMap = mHeaderMap;

            DebugLog.i("HttpManagerBase - PatchNotDefaultConnectionTask Key: " + key);
            DebugLog.i("HttpManagerBase - PatchNotDefaultConnectionTask Protocol: " + protocol);
            DebugLog.i("HttpManagerBase - PatchNotDefaultConnectionTask Host: " + host);
            DebugLog.i("HttpManagerBase - PatchNotDefaultConnectionTask Port: " + port);
            DebugLog.i("HttpManagerBase - PatchNotDefaultConnectionTask FilePath: " + filePath);
            DebugLog.i("HttpManagerBase - PatchNotDefaultConnectionTask Param: " + mParamaterMap);
            DebugLog.i("HttpManagerBase - PatchNotDefaultConnectionTask Header: " + mHeaderMap);
        }

        // ------------------------------
        // Override
        // ------------------------------
        @Override
        public AsyncTaskBase cloneTask() {
            return new PatchNotDefaultConnectionTask(mKey, mProtocol, mHost, mPort, mFilePath, mParamaterMap, mHeaderMap);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStartedConnection(mKey);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (mProtocol == null || mHost == null || mFilePath == null) {
                return null;
            }

            HttpURLConnection connection = null;
            String result = null;
            try {
                URL url = new URL(mProtocol, mHost, mPort, mFilePath + ".json");
                connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(TIMEOUT_TIME);
                connection.setReadTimeout(TIMEOUT_TIME);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");

                connection.setRequestProperty("X-HTTP-Method", "PATCH");
                connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                connection.setRequestProperty("X-Method-Override", "PATCH");

                if (mHeaderMap != null && mHeaderMap.size() > 0) {
                    for (String key : mHeaderMap.keySet()) {
                        connection.setRequestProperty(key, String.valueOf(mHeaderMap.get(key)));
                    }
                }

                if (mParamaterMap != null) {
                    // 通信用のParameterを作成
                    StringBuilder paramBuilder = new StringBuilder();
                    int index = 0;
                    for (String key : mParamaterMap.keySet()) {
                        if (index++ != 0) {
                            paramBuilder.append("&");
                        }

                        paramBuilder.append(key);
                        paramBuilder.append("=");
                        paramBuilder.append(mParamaterMap.get(key));
                    }

                    // 通信用のParameterを設定
                    if (paramBuilder.length() > 0) {
                        PrintStream ps = new PrintStream(connection.getOutputStream());
                        ps.print(paramBuilder.toString());
                        ps.close();
                    }
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    return ERROR_NAME_UNAUTHORIZED;
                } else if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // 特に処理はない
                } else if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("responseCode:" + responseCode + "(" + mKey + ")");
                }

                result = inputStreamToString(connection.getInputStream());
            } catch (IOException e) {
                DebugLog.e(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("authentication")) {
                    return ERROR_NAME_UNAUTHORIZED;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                if (mListener != null) {
                    mListener.onErroredConnection(mKey);
                }
            } else if (s.equals(ERROR_NAME_UNAUTHORIZED)) {
                if (mListener != null) {
                    mListener.onAutorizedErroredConnection(mKey);
                }
            } else {
                if (mListener != null) {
                    mListener.onFinishedConnection(mKey, s);
                }
            }
            stop(mKey);
        }

    }

    /**
     * Delete通信を行うタスククラス
     */
    protected class DeleteNotDefaultConnectionTask extends AsyncTaskBase {

        // ------------------------------
        // Member
        // ------------------------------
        private int mKey;
        private String mProtocol;
        private String mHost;
        private int mPort;
        private String mFilePath;
        private HashMap<String, String> mParamaterMap;
        private HashMap<String, Object> mHeaderMap;

        // ------------------------------
        // Constructor
        // ------------------------------
        public DeleteNotDefaultConnectionTask(int key, String protocol, String host, int port, String filePath, HashMap<String, String> mParamaterMap, HashMap<String, Object> mHeaderMap) {
            this.mKey = key;
            this.mProtocol = protocol;
            this.mHost = host;
            this.mPort = port;
            this.mFilePath = filePath;
            this.mParamaterMap = mParamaterMap;
            this.mHeaderMap = mHeaderMap;

            DebugLog.i("HttpManagerBase - protocol: " + protocol);
            DebugLog.i("HttpManagerBase - host: " + host);
            DebugLog.i("HttpManagerBase - port: " + port);
            DebugLog.i("HttpManagerBase - filePath: " + filePath);
            // DebugLog.i("HttpManagerBase - params: " + mParamaterMap.toString());
        }

        // ------------------------------
        // Override
        // ------------------------------
        @Override
        public AsyncTaskBase cloneTask() {
            return new DeleteNotDefaultConnectionTask(mKey, mProtocol, mHost, mPort, mFilePath, mParamaterMap, mHeaderMap);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStartedConnection(mKey);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (mProtocol == null || mHost == null || mFilePath == null) {
                return null;
            }

            HttpURLConnection connection = null;
            String result = null;
            try {
                URL url = new URL(mProtocol, mHost, mPort, mFilePath + ".json");
                connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(TIMEOUT_TIME);
                connection.setReadTimeout(TIMEOUT_TIME);
//                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("DELETE");

                if (mHeaderMap != null && mHeaderMap.size() > 0) {
                    for (String key : mHeaderMap.keySet()) {
                        connection.setRequestProperty(key, String.valueOf(mHeaderMap.get(key)));
                    }
                }

                if (mParamaterMap != null) {
                    // 通信用のParameterを作成
                    StringBuilder paramBuilder = new StringBuilder();
                    int index = 0;
                    for (String key : mParamaterMap.keySet()) {
                        if (index++ != 0) {
                            paramBuilder.append("&");
                        }

                        paramBuilder.append(key);
                        paramBuilder.append("=");
                        paramBuilder.append(mParamaterMap.get(key));
                    }

                    // 通信用のParameterを設定
                    if (paramBuilder.length() > 0) {
                        PrintStream ps = new PrintStream(connection.getOutputStream());
                        ps.print(paramBuilder.toString());
                        ps.close();
                    }
                }

                int responseCode = connection.getResponseCode();
                DebugLog.i("HttpManagerBase - responseCode: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    return ERROR_NAME_UNAUTHORIZED;
                } else if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // 特に処理はない
                } else if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("responseCode:" + responseCode + "(" + mKey + ")");
                }

                result = inputStreamToString(connection.getInputStream());
            } catch (IOException e) {
                DebugLog.e(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("authentication")) {
                    return ERROR_NAME_UNAUTHORIZED;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                if (mListener != null) {
                    mListener.onErroredConnection(mKey);
                }
            } else if (s.equals(ERROR_NAME_UNAUTHORIZED)) {
                if (mListener != null) {
                    mListener.onAutorizedErroredConnection(mKey);
                }
            } else {
                if (mListener != null) {
                    mListener.onFinishedConnection(mKey, s);
                }
            }
            stop(mKey);
        }

    }

}

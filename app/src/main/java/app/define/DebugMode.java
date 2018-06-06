package app.define;

/**
 * デバッグモードの定義クラス
 */
public class DebugMode {

    // ------------------------------
    // Define
    // ------------------------------
    public static final boolean isHaveToShowTutorial = false;    // 必ずチュートリアルを表示するか
    public static final boolean isSendTestAnalytics = false;     // Analyticsの送信先をテストにするか
    public static final boolean isStagingServer = false;         // ステージングサーバに接続するか
    public static final boolean isTestServer = true;            // テストサーバー（開発用）に接続するか
    public static final boolean isIgnoreMaintenance = false;    // メンテナンスを無視するか
    public static final boolean isIgnoreUpdate = false;         // 強制アップデートを無視するか
    public static final boolean isShowTestAd = false;            // テスト用のADを表示するか
    public static final boolean isTestJirokichi = false;            // ジロキチテスト用
    public static final boolean isTestRandomCharacter = false;       // ランダムなごみのテスト用

}

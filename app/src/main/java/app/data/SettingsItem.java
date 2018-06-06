package app.data;

import java.io.Serializable;

import app.define.SettingsType;

/**
 *
 */
public class SettingsItem implements Serializable {

    // ------------------------------
    // Define
    // ------------------------------
    public static final String TITLE_USERID = "ログインID";
    public static final String TITLE_VERSION = "アプリバージョン";
    public static final String TITLE_BGM = "BGM";
    public static final String TITLE_SE = "SE";
    public static final String TITLE_CONTACT = "お問い合わせ";
    public static final String TITLE_LOGOUT = "ログアウト";
    public static final String TITLE_USER_REGISTER = "会員登録";
    public static final String TITLE_OPEN_SOURCE_LICENSE = "Open Source License";
    // ------------------------------
    // Member
    // ------------------------------
    private SettingsType type;
    private String title;
    private String descript;

    // ------------------------------
    // Constructor
    // ------------------------------
    public SettingsItem(SettingsType type, String title, String descript) {
        this.type = type;
        this.title = title;
        this.descript = descript;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final SettingsType getType() {
        return type;
    }

    public final boolean isBGM() {
        return getTitle().equals(TITLE_BGM);
    }

    public final String getTitle() {
        return title == null ? "" : title;
    }

    public final String getDescript() {
        return descript == null ? "" : descript;
    }

    public final boolean isItemClickable() {
        return (getTitle().equals(TITLE_LOGOUT) ||
                getTitle().equals(TITLE_CONTACT) ||
                getTitle().equals(TITLE_USER_REGISTER)) ||
                getTitle().equals(TITLE_OPEN_SOURCE_LICENSE);
    }

}

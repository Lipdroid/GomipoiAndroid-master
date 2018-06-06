package app.http;

import java.util.HashMap;

import app.application.GBApplication;
import app.data.http.ExchangePremiumTicketResponse;
import app.data.http.FriendCodeUseParam;
import app.data.http.FriendInvitationsParam;
import app.data.http.FriendMessagesBadgeCountResponse;
import app.data.http.FriendMessagesResponse;
import app.data.http.FriendPresentRequestsParam;
import app.data.http.FriendPresentsParam;
import app.data.http.FriendPresentsUseParam;
import app.data.http.FriendsForPresentRequestResponse;
import app.data.http.FriendsSearchByCodeParam;
import app.data.http.FriendshipBonusParam;
import app.data.http.GomipoiBookCheckParam;
import app.data.http.GomipoiBookOwnParam;
import app.data.http.GomipoiBookReceiveBonusesParam;
import app.data.http.GomipoiFriendsRankResponse;
import app.data.http.GomipoiFriendsResponse;
import app.data.http.GomipoiGameLoadResponse;
import app.data.http.GomipoiGameMovePlaceParam;
import app.data.http.GomipoiGameSaveParam;
import app.data.http.GomipoiGarbageFoundParam;
import app.data.http.GomipoiGarbageOwnResponse;
import app.data.http.GomipoiGarbageRecipeParam;
import app.data.http.GomipoiGarbageSynthesesParam;
import app.data.http.GomipoiItemOwnResponse;
import app.data.http.GomipoiItemSetBuyParam;
import app.data.http.GomipoiItemUseParam;
import app.data.http.GomipoiJirokichiBonusesParams;
import app.data.http.NewsTopicCheckResponse;
import app.data.http.NotificationsCheckResponse;
import app.data.http.RegisterDeviceTokenParam;
import app.data.http.SystemMessagesBadgeCountResponse;
import app.data.http.SystemMessagesResponse;
import app.data.http.TopNotificationsCheckResponse;
import app.data.http.UserAppJewelParam;
import app.data.http.UserAppPointParam;
import app.data.http.UserAppSelfResponse;
import app.data.http.UserDefinitiveRegisterParam;
import app.data.http.UserGuestRegisterResponce;
import app.data.http.UserSessionsLogoutParam;
import app.data.http.UserSessionsParam;
import app.data.http.UsersParam;
import app.define.ConnectionCode;
import app.define.DebugMode;
import app.preference.PJniBridge;

/**
 *
 */
public class HttpDefine {

    // ------------------------------
    // Define
    // ------------------------------
//    public static final String base_url = DebugMode.isTestServer ? "http://dev-prize-exchange.app-daydelight.com" : "未定";

    // *************プロトコル*************//
    public static final String protocol = DebugMode.isTestServer ? "http" : "https";

    // *************ホスト*************//
    public static final String host = DebugMode.isTestServer ? "dev-prize-exchange.app-daydelight.com" :
            DebugMode.isStagingServer ? "stg-prize-exchange-api.top-mission-app.com" : "prize-exchange-api.top-mission-app.com";
    // 近藤さんサーバ
    //    public static final String host = DebugMode.isTestServer ? "192.168.100.55" : "未定";

    // *************ポート*************//
    public static final int port = DebugMode.isTestServer ? 3000 : -1;
    // 近藤さんサーバ
    //public static final int port = DebugMode.isTestServer ? 3001 : -1;

    // *************S3*************//
    public static final String syste_url = DebugMode.isTestServer ? "http://dev-prize-exchange-contents.app-daydelight.com/api/systems/gomipoi" :
            DebugMode.isStagingServer ? "http://stg-prize-exchange-contents.top-mission-app.com/api/systems/gomipoi" : "http://prize-exchange-contents.top-mission-app.com/api/systems/gomipoi";

    // ------------------------------
    // Accesser
    // ------------------------------
    public static HashMap<String, Object> makeLoginHttpHeaderMap(String uuid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("X_PRIZE_EX_ACCESS_APPLICATION_ID", PJniBridge.nativeGetApplicationId());
        map.put("X_PRIZE_EX_ACCESS_DEVICE_ID", uuid);
        map.put("X_PRIZE_EX_ACCESS_DEVICE_TYPE", 2);
        return map;
    }

    public static HashMap<String, Object> makeHttpHeaderMap(GBApplication app, String uuid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("X_PRIZE_EX_ACCESS_TOKEN", app.getAccessToken());
        map.put("X_PRIZE_EX_ACCESS_APPLICATION_ID", PJniBridge.nativeGetApplicationId());
        map.put("X_PRIZE_EX_ACCESS_DEVICE_ID", uuid);
        map.put("X_PRIZE_EX_ACCESS_DEVICE_TYPE", 2);
        return map;
    }

    public static String getFilePath(ConnectionCode code) {
        switch (code) {
            case Users:
                return UsersParam.API;

            case User_Sessions:
                return UserSessionsParam.API;

            case User_Sessions_Logout:
                return UserSessionsLogoutParam.API;

            case All_UserApps_Self:
            case UserApps_Self:
                return UserAppSelfResponse.API;

            case All_GomipoiGarbage_Own:
                return GomipoiGarbageOwnResponse.API;

            case All_GomipoiGame_Load:
            case GomipoiGame_Load:
                return GomipoiGameLoadResponse.API;

            case All_GomipoiItem_Own:
            case GomipoiItem_Own:
                return GomipoiItemOwnResponse.API;

            case Garbages_Recipe:
                return GomipoiGarbageRecipeParam.API;

            case Add_Jewel:
                return UserAppJewelParam.API;

            case Add_Point:
                return UserAppPointParam.API;

            case All_Book_Own:
            case Book_Own:
                return GomipoiBookOwnParam.API;

            case Garbage_Found:
                return GomipoiGarbageFoundParam.API;

            case Garbage_Syntheses:
                return GomipoiGarbageSynthesesParam.API;

            case Game_Save:
                return GomipoiGameSaveParam.API;

            case Jirokichi_Bonuses:
                return GomipoiJirokichiBonusesParams.API;

            case Item_Use:
                return GomipoiItemUseParam.API;

            case ItemSet_Buy:
                return GomipoiItemSetBuyParam.API;

            case Book_Check:
                return GomipoiBookCheckParam.API;

            case Book_ReceiveBonusPages:
                return GomipoiBookReceiveBonusesParam.API;

            case NewsTopic_Check:
                return NewsTopicCheckResponse.API;
//
//            case PrizeWinners_Check:
//                return PrizeWinnersCheckResponse.API;
//

            case ExchangePremiumTicket:
                return ExchangePremiumTicketResponse.API;
            case GomipoiFriends:
                return GomipoiFriendsResponse.API;

            case GomipoiFriendDelete: {
                return GomipoiFriendsResponse.API_DELETE;
            }

            case GomipoiFriendsRank:
                return GomipoiFriendsRankResponse.API;

            case Friends_SearchByCode:
                return FriendsSearchByCodeParam.API;

            case FriendCode_Use:
                return FriendCodeUseParam.API;

            case Friends_ForPresentRequest:
                return FriendsForPresentRequestResponse.API;

            case FriendInvitations:
                return FriendInvitationsParam.API;

            case FriendPresents_FriendList:
            case FriendPresents_MessageList:
                return FriendPresentsParam.API;

            case FriendPresents_Use:
                return FriendPresentsUseParam.API;

            case FriendPresentRequests_FriendList:
            case FriendPresentRequests_GimmeList:
                return FriendPresentRequestsParam.API;

            case FriendMessages:
                return FriendMessagesResponse.API;

            case SystemMessages:
                return SystemMessagesResponse.API;

            case FriendMessages_BadgeCount:
                return FriendMessagesBadgeCountResponse.API;

            case SystemMessages_BadgeCount:
                return SystemMessagesBadgeCountResponse.API;

            case FriendshipBonuses:
                return FriendshipBonusParam.API;

            case GomipoiGame_MovePlace:
                return GomipoiGameMovePlaceParam.API;

            case RegisterDeviceToken:
                return RegisterDeviceTokenParam.API;

            case Notifications_Check:
                return NotificationsCheckResponse.API;

            case TopNotifications_Check:
                return TopNotificationsCheckResponse.API;

            case User_Guest_Register:
                return UserGuestRegisterResponce.API;

            case User_Definitive_Register:
                return UserDefinitiveRegisterParam.API;

        }
        return null;
    }

}

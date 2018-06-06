package app.define;

import java.io.Serializable;

/**
 * フラグメントのイベントコード
 */
public enum FragmentEventCode implements Serializable {
    UNKNOWN(0),
    TutorialNext(1),
    OnResume(2),
    OnPause(3),
    PostUser(4),
    PostUserSessions(5),
    DeleteUserSessions(6),
    GetAllUserData(7),
    GetUserAppsSelf(8),
    PatchUserAppsJewel(9),
    PatchUserAppsPoint(10),
    GetBookOwn(11),
    PostGarbageFound(12),
    PostGarbageSyntheses(13),
//    PostGameSave(14),
    PostItemUse(15),
    PostItemSetBuy(16),
    GetItemOwn(17),
    PostBookCheck(18),
    PostReceiveBonusPages(19),
    GetNewsTopicCheck(20),
    GetGarbageRecipe(21),
    GetSystemCheck(22),
    ShowCaptureWaitingDialog(23),
    CloseCaptureWaitingDialog(24),
    GetFriendList(25),
    GetFriendSearch(26),
    RequestAddFriend(27),
    GetFriendMessage(28),
    Send_Present_FriendList(29),
    Send_Present_MessageList(30),
    Receive_Present(31),
    GetGimmeList(32),
    RequestGimme_FriendList(33),
    RequestGimme_GimmeList(34),
    PostFriendInvitations(35),
    Receive_FriendshipBonus(36),
    Game_ChangeRoom(37),
    Receive_JirokichiBonus(38),
    RegisterDeviceToken(39),
    GetNotificationCheck(40),
    PostUserGuestRegister(41),
    PostUserDefinitiveRegister(42),
    GetFriendListRank(43),
    DeleteFriend(44),
    PostPremiumTicket(45),
    OpenConnectionDialog(46),
    CloseConnectionDialog(47),
    ;

    int mValue;

    FragmentEventCode(int mValue) {
        this.mValue = mValue;
    }

    public static FragmentEventCode valueOf(int value) {
        for (FragmentEventCode code : values()) {
            if (code.getValue() == value) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public final int getValue() {
        return mValue;
    }
}

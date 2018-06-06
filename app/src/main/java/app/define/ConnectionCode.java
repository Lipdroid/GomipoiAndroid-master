package app.define;

import java.io.Serializable;

/**
 * 通信コード
 */
public enum ConnectionCode implements Serializable {
    UNKNOWN(0),
    Users(1),
    User_Sessions(2),
    User_Sessions_Logout(3),
    All_UserApps_Self(4),
    All_GomipoiGarbage_Own(5),
    All_GomipoiGame_Load(6),
    All_GomipoiItem_Own(7),
    Add_Jewel(8),
    Add_Point(9),
    Book_Own(10),
    Garbage_Found(11),
    Garbage_Syntheses(12),
    Game_Save(13),
    Item_Use(14),
    ItemSet_Buy(15),
    GomipoiItem_Own(16),
    All_Book_Own(17),
    Book_Check(18),
    Book_ReceiveBonusPages(19),
    NewsTopic_Check(20),
    UserApps_Self(21),
    GomipoiGame_Load(22),
    Garbages_Recipe(23),
    System_Check(24),
    GomipoiFriends(25),
    Friends_SearchByCode(26),
    FriendCode_Use(27),
    Friends_ForPresentRequest(28),
    FriendInvitations(29),
    FriendPresents_FriendList(30),
    FriendPresents_MessageList(31),
    FriendPresents_Use(32),
    FriendPresentRequests_FriendList(33),
    FriendPresentRequests_GimmeList(34),
    FriendMessages(35),
    FriendMessages_BadgeCount(36),
    SystemMessages_BadgeCount(37),
    FriendshipBonuses(38),
    SystemMessages(39),
    Jirokichi_Bonuses(40),
    GomipoiGame_MovePlace(41),
    RegisterDeviceToken(42),
    Notifications_Check(43),
    User_Guest_Register(44),
    User_Definitive_Register(45),
    TopNotifications_Check(46),
    GomipoiFriendsRank(47),
    GomipoiFriendDelete(48),
    ExchangePremiumTicket(49),
    ;


    int mValue;

    ConnectionCode(int mValue) {
        this.mValue = mValue;
    }

    public static ConnectionCode valueOf(int value) {
        for (ConnectionCode item : values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mValue;
    }

    public boolean isNeedRetry() {
        return (!this.equals(Users) && !this.equals(User_Sessions) && !this.equals(User_Sessions_Logout));
    }

}

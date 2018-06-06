package app.define;

import java.io.Serializable;

/**
 * ダイアログコード
 */
public enum DialogCode implements Serializable {
    UNKNOWN(0),
    ERROR(1),
    InputShortage(2),
    News(3),
    BuyGem(4),
    PictureBook(5),
    Item(6),
    Synthesis(7),
    SynthesisSuccess(8),
    PicturePoi(9),
    Scroll(10),
    SynthesisShortage(11),
//    InputOverTextLength(12),
    InputSurrogate(13),
    LogoutConfirm(14),
    Connection(15),
    UnauthorizedError(16),
    ExistSameAccountError(17),
    NotMatchUserInfo(18),
    NetworkError(19),
    NetworkErrorWithRetry(20),
    GetNetworkErrorWithRetry(21),
    ItemUse(22),
    ItemBuy(23),
    PicturePoiResultGet(24),
    PicturePoiResultNone(25),
    BrokenBroom(26),
    ItemShortage(27),
    GemShortage(28),
    PageCompleteBonus(29),
    AlreadyBought(30),
    FailedSynthesis(31),
    PictureBookDetail(32),
    SynthesisNotHaveReceipe(33),
    SynthesisAlreadyGot(34),
    GameSaveAlreadyMaxCapacity(35),
    ItemUseAlreadyEmptyCan(36),
    ItemUseAlreadyUsed(37),
    ItemUseDisabled(38),
    ItemUseNotChangeCan(39),
    ItemSetBuyAchievedMaxPossession(40),
    InputAccountOverTextLength(41),
    InputUserIdOverTextLength(42),
    InputPasswordOverTextLength(43),
    PermissionError(44),
    PicturePoiResult(45),
    StorageError(46),
    ServiceNetworkErrorWithRetry(47),
    DonwgradeError(48),
    BoughtItem(49),
    NotHavePrevItem(50),
    NotHaveScroll(51),
    DeletePictureAlert(52),
    VersionUpDialog(53),
    CaptureWaiting(54),
    SearchFriend(55),
    InviteFriend(56),
    GiveFriend(57),
    GimmeFriend(58),
    FriendMessage(59),
    SearchByCode_Invalid(60),
    CodeUse_NotMatch(61),
    CodeUse_IsSelf(62),
    CodeUse_AlreadyFriend(63),
    CodeUse_ReachedUpperLimitSelf(64),
    CodeUse_ReachedUpperLimitFriend(65),
    Present_ExceedUpperLimitSelf(66),
    Present_ExceedUpperLimitFriend(67),
    Presents_ExceedUpperLimitSelf(68),
    Presents_ExceedUpperLimitFriend(69),
    PresentsUse_Expiration(70),
    PresentsUse_NoEffect(71),
    PresentRequest_ExceedUpperLimit(72),
    PresentRequests_ExceedUpperLimit(73),
    IconCan_Select(74),
    GameMenu(75),
    ItemSetBuyNoKey(76),
    ItemUseDailyCountReached(77),
    RoomSecretDetail(78),
    ExistSameNicknameError(79),
    InvalidPasswordError(80),
    LoginGuestConfirm(81),
    GuestLimitedFuncFriend(82),
    Notice1(83),
    Notice2(84),
    UsedSameDay(85),
    ConfirmSeal(86),
    GardenKeyMessage(87),
    BrokenGarbageCan(88),
    Unimplemented(89),
    InvalidQRCode(90),
    Notice3(91),
    ItemBuyTicket(92),
    ItemBuyTicketSuccess(93),
    DeleteFriend(94),
    DeleteFriendSuccess(95),
    ;

    int mValue;

    DialogCode(int mValue) {
        this.mValue = mValue;
    }

    public static DialogCode valueOf(int value) {
        for (DialogCode item : values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mValue;
    }

}

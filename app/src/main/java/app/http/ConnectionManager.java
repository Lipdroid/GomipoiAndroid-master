package app.http;

import android.content.Context;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.application.GBApplication;
import app.data.ListItemData;
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
import app.data.http.FriendsSearchByCodeResponse;
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
import app.data.http.MessagesResponse;
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
import app.define.BroomType;
import app.define.CharacterType;
import app.define.ConnectionCode;
import app.define.DebugMode;
import app.define.GarbageCanType;
import app.define.ItemId;
import app.define.StageType;
import app.jni.JniBridge;
import app.notification.InstanceIDListenerService;
import lib.appversion.AppVersionUtils;
import lib.http.HttpManagerBase;
import lib.http.OnHttpManagerListener;
import lib.json.JsonParser;
import lib.json.JsonUtils;
import lib.log.DebugLog;
import lib.network.NetworkUtils;

/**
 *
 */
public class ConnectionManager implements OnHttpManagerListener {

    // -----------------------------------
    // Member
    // -----------------------------------
    private Context mContext;
    private HttpManagerBase mHttpManager;
    private OnConnectionManagerListener mListener;
    private String mUUID;
    private int mAllGetCompleteCount;
    private int mLoadingFriendCompleteCount;

    private UsersParam mTmpRegistParam;
    private OnUserRegistListener mRegistListener;

    private UserSessionsParam mTmpLoginParam;
    private OnUserLoginListener mLoginListener;

    private OnGetSystemCheckListener mGetSystemCheckListener;
    private OnAllGetListener mAllGetListener;
    private OnItemOwnListener mItemOwnListener;
    private OnBookOwnListener mBookOwnListener;
    private OnUserAppSelfListener mUserAppSelfListener;
    private OnGameLoadListener mGameLoadListener;
    private OnGarbageRecipeListener mGarbageRecipeListener;

    private UserAppJewelParam mTmpAddJewelParam;
    private OnAddJewelListener mAddJewelListener;

    private UserAppPointParam mTmpAddPointParam;
    private OnAddPointListener mAddPointListener;

    private GomipoiGarbageFoundParam mTmpGarbageFoundParam;
    private OnGarbageFoundListener mGarbageFoundListener;

    private GomipoiGarbageSynthesesParam mTmpGarbageSynthesesParam;
    private OnGarbageSynthesesListener mGarbageSynthesesListener;

    private GomipoiGameSaveParam mTmpGameSaveParam;
    private OnGameSaveListener mGameSaveListener;

    private GomipoiJirokichiBonusesParams mTmpJirokichiBonusesParam;
    private OnJirokichiBonusesListener mJirokichiBonusesListener;

    private GomipoiItemUseParam mTmpItemUseParam;
    private OnItemUseListener mItemUseListener;

    private GomipoiItemSetBuyParam mTmpItemSetBuyParam;
    private OnItemSetBuyListener mItemSetBuyListener;

    private GomipoiBookCheckParam mTmpBookCheckParam;
    private OnBookCheckListener mBookCheckListener;

    private GomipoiBookReceiveBonusesParam mTmpBookReceiveBonusesParam;
    private OnBookReceiveBonusesListener mBookReceiveBonusesListener;

    private OnNewsTopicCheckListener mNewsTopicCheckListener;

    // API: /user/premium_ticket
    private OnPremiumTicketListener mOnPremiumTicketListener;

    // API: /gomipoi_friends(.:format)
    private OnFriendListListener mOnFriendListListener;

    // API: /friends/id=?
    private OnFriendDeleteListener mOnFriendDeleteListener;

    // API: /week_rankings/current
    // API: /week_rankings/last
    private OnFriendListRankListener mOnFriendListRankListener;

    // API: /friends/search_by_code(.:format)
    private OnFriendsSearchByCodeListener mOnFriendsSearchByCodeListener;
    private FriendsSearchByCodeParam mFriensSearchByCodeParam;

    // API: /friend_messages(.:format)
    private OnFriendMessagesListener mOnFriendMessagesListener;
    private int mLoadingFriendMessagesCompleteCount;

    // API: /friend_code/use(.:format)
    private OnFriendCodeUseListener mOnFriendCodeUseListener;
    private FriendCodeUseParam mFriendCodeUseParam;

    // API: /friend_presents(.:format)
    private OnFriendPresentsListener mOnFriendPresentsListener_MessageList;
    private FriendPresentsParam mFriendPresentsParam_MessageList;
    private OnFriendPresentsListener mOnFriendPresentsListener_FriendList;
    private FriendPresentsParam mFriendPresentsParam_FriendList;

    // API: /friend_presents/use(.:format)
    private OnFriendPresentsUseListener mOnFriendPresentsUseListener;
    private FriendPresentsUseParam mFriendPresentsUseParam;

    // API: /friendship_bonuses/receive(.:format)
    private OnFriendshipBonusListener mOnFriendshipBonusListener;
    private FriendshipBonusParam mFriendshipBonusParam;

    // API: /friends/for_present_request(.:format)
    private OnFriendsForPresentRequestListener mOnFriendsForPresentRequestListener;

    // API: /friend_present_requests(.:format)
    private OnFriendPresentRequestsListener mOnFriendPresentRequestsListener_GimmeList;
    private OnFriendPresentRequestsListener mOnFriendPresentRequestsListener_FriendList;
    private FriendPresentRequestsParam mFriendPresentRequestsParam_GimmeList;
    private FriendPresentRequestsParam mFriendPresentRequestsParam_FriendList;

    // API: /friend_invitations(.:format)
    private OnFriendInvitationsListener mOnFriendInvitationsListener;

    // API: /friend_invitations(.:format)
    private OnGameMovePlaceListener mOnGameMovePlaceListener;

    private OnRegisterDeviceTokenListener mOnRegisterDeviceTokenListener;

    // API: /notifications/check
    private OnCheckedNotificationListner mOnCheckedNotificationListner;

    // API: /top_notifications/check
    private OnCheckedTopNotificationsListner mOnCheckedTopNotificationsListener;

    // API: /users/guest_register
    private OnUserGuestRegisteredListner mOnUserGuestRegisteredListner;

    // API: /users/definitive_register
    private UserDefinitiveRegisterParam mTempUserDefinitiveRegisterParam;
    private OnUserDefinitiveRegisterdListener mOnUserDefinitiveRegisterdListener;

    // -----------------------------------
    // Constructor
    // -----------------------------------
    /**
     * Constructor
     */
    public ConnectionManager(Context context, OnConnectionManagerListener listener) {
        mContext = context;
        mListener = listener;
    }

    // -----------------------------------
    // OnHttpManagerListener
    // -----------------------------------
    @Override
    public void onAutorizedErroredConnection(int connectionCode) {
        if (mListener != null) {
            mListener.ConnectionManager_OnAuthorizedError();
        }
        getHttpManager().stopAll();
    }

    @Override
    public void onStartedConnection(int connectionCode) {
    }

    @Override
    public void onFinishedConnection(int connectionCode, String result) {
        Object jsonData = null;
        try {
            jsonData = JsonParser.parseJson(result);
        } catch (JSONException e) {
            DebugLog.e("connectionError:" + e.getMessage());
        }

        ConnectionCode code = ConnectionCode.valueOf(connectionCode);
        Object retryParam = getRetryData(code);

        if (jsonData == null) {
            getHttpManager().stopAll();
            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
            return;
        }

        switch (code) {
            case Users: {
                if (!(jsonData instanceof HashMap)) {
                    mRegistListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = UsersParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (UsersParam.ResultCode.valueOf(resultCode)) {
                    case OK:
                        if (retryParam == null) {
                            mRegistListener = null;
                            onHideConnectionDialog();
                            onShowNetworkErrorDialog(code, null);
                            return;
                        }

                        boolean isNew = InstanceIDListenerService.isNew(mContext);
                        // 登録完了なら、そのままログイン処理に移る
                        userLogin(new UserSessionsParam(
                            UserSessionsParam.ParentActivity.LOGIN_ACTIVITY,
                                mTmpRegistParam.account,
                                mTmpRegistParam.password,
                                isNew ? InstanceIDListenerService.getDeviceToken(mContext) : null), null);
                        mTmpRegistParam = null;
                        return;

                    case EXIST_SAME_ACCOUNT: {
                        onHideConnectionDialog();
                        if (mRegistListener != null) {
                            mRegistListener.UserRegist_OnShowExistSameAccountError();
                        }
                        break;
                    }

                    case EXIST_SAME_NICKNAME:
                        onHideConnectionDialog();
                        if (mRegistListener != null) {
                            mRegistListener.UserRegist_OnShowExistSameNicknameError();
                        }
                        break;

                    case INVALID_PASSWORD:
                        onHideConnectionDialog();
                        if (mRegistListener != null) {
                            mRegistListener.UserRegist_OnShowInvalidPasswordError();
                        }
                        break;

                    default: {
                        onHideConnectionDialog();
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mRegistListener = null;
                break;
            }

            case User_Sessions: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = UserSessionsParam.ResultCode.UNKNOWN.getValue();
                String accessToken = null;
                String friendCode = null;
                String nickname = null;
                boolean isGuest = false;
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }
                if (response.get("access_token") != null) {
                    accessToken = response.get("access_token").toString();
                }
                if (response.get("friend_code") != null) {
                    friendCode = response.get("friend_code").toString();
                }
                if (response.get("nickname") != null) {
                    nickname = response.get("nickname").toString();
                }
                if (response.get("guest") != null) {
                    isGuest = response.get("guest").toString().equals("1");
                }

                onHideConnectionDialog();

                switch (UserSessionsParam.ResultCode.valueOf(resultCode)) {
                    case OK:
                        if (retryParam == null) {
                            mRegistListener = null;
                            mOnUserDefinitiveRegisterdListener = null;
                            mLoginListener = null;
                            onShowNetworkErrorDialog(code, null);
                            return;
                        }

                        if (accessToken == null || accessToken.length() == 0) {
                            mRegistListener = null;
                            mOnUserDefinitiveRegisterdListener = null;
                            mLoginListener = null;
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            return;
                        }

                        if (mContext != null) {
                            ((GBApplication) mContext).setAccessToken(accessToken);
                            ((GBApplication) mContext).setFriendCode(friendCode);
                            ((GBApplication) mContext).setNickname(nickname);
                            ((GBApplication) mContext).setIsGuest(isGuest);
                            ((GBApplication) mContext).getPreferenceManager().setUserId(mTmpLoginParam.account);
                            ((GBApplication) mContext).getPreferenceManager().setUserPassword(mTmpLoginParam.password);
                        }

                        switch (mTmpLoginParam.parentActivity) {
                            case LOGIN_ACTIVITY:
                                if (mRegistListener != null) {
                                    mRegistListener.UserRegist_Ok();
                                } else if (mLoginListener != null) {
                                    mLoginListener.UserLogin_Ok();
                                }
                                break;

                            case SETTING_ACTIVITY_GUEST_REGISTER:
                                if (mOnUserDefinitiveRegisterdListener != null) {
                                    mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_Ok_Setting();
                                }
                                break;

                            case TOP_ACTIVITY_GUEST_REGISTER:
                                if (mOnUserDefinitiveRegisterdListener != null) {
                                    mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_Ok_Top();
                                }
                                break;

                            case EXCHANGE_ACTIVITY_GUEST_REGISTER:
                                if (mOnUserDefinitiveRegisterdListener != null) {
                                    mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_Ok_Exchange();
                                }
                                break;

                            case OTHER_ACTIVITY:
                            default:
                                if (mLoginListener != null) {
                                    mLoginListener.UserLogin_AutoLoginOk();
                                } else if (mOnUserDefinitiveRegisterdListener != null) {
                                    mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_Ok_Other();
                                }
                                break;
                        }
                        break;

                    case NOT_MATCH_USER_INFO:
                        if (mRegistListener != null) {
                            mRegistListener.UserRegist_OnNotMatchUserError();
                        } else if (mLoginListener != null) {
                            mLoginListener.UserLogin_OnNotMatchUserError();
                        } else if (mOnUserDefinitiveRegisterdListener != null) {
                            mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_OnNotMatchUserError();
                        }
                        break;

                    default:
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                }

                mTmpLoginParam = null;
                mRegistListener = null;
                mOnUserDefinitiveRegisterdListener = null;
                break;
            }

            case User_Sessions_Logout: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = UserSessionsLogoutParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                onHideConnectionDialog();

                switch (UserSessionsLogoutParam.ResultCode.valueOf(resultCode)) {
                    case OK:
                        // ログアウト後の処理
                        if (mListener != null) {
                            mListener.ConnectionManager_OnDeleteLoginDataForLogout(false);
                        }
                        break;

                    default:
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                }

                break;
            }

            case All_UserApps_Self:
            case UserApps_Self:
            {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                int resultCode = UserAppSelfResponse.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (UserAppSelfResponse.ResultCode.valueOf(resultCode)) {
                    case OK:
                        JniBridge.nativeOnReceivedUserAppsSelfResponse(result);

                        if (code.equals(ConnectionCode.All_UserApps_Self)) {
                            checkCompleteAllGet();
                        } else if (mUserAppSelfListener != null) {
                            mUserAppSelfListener.UserAppSelf_Ok();
                            mUserAppSelfListener = null;
                        }

                        break;

                    default:
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                }
                break;
            }

            case All_GomipoiGarbage_Own: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiGarbageOwnResponse.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiGarbageOwnResponse.ResultCode.valueOf(resultCode)) {
                    case OK:
                        JniBridge.nativeOnReceivedGomipoiGarbageOwnResponse(result);

                        if (code.equals(ConnectionCode.All_GomipoiGarbage_Own)) {
                            checkCompleteAllGet();
                        }

                        break;

                    default:
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                }
                break;
            }

            case All_GomipoiGame_Load:
            case GomipoiGame_Load: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiGarbageOwnResponse.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiGameLoadResponse.ResultCode.valueOf(resultCode)) {
                    case OK:
                        JniBridge.nativeOnReceivedGomipoiGameLoadResponse(result);

                        if (code.equals(ConnectionCode.All_GomipoiGame_Load)) {
                            checkCompleteAllGet();
                        } else if (mGameLoadListener != null) {
                            mGameLoadListener.GameLoad_Ok();
                            mGameLoadListener = null;
                        }

                        break;

                    default:
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                }

                break;
            }

            case All_GomipoiItem_Own:
            case GomipoiItem_Own: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiItemOwnResponse.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiItemOwnResponse.ResultCode.valueOf(resultCode)) {
                    case OK:
                        JniBridge.nativeOnReceivedGomipoiItemOwnResponse(result);
                        if (code.equals(ConnectionCode.All_GomipoiItem_Own)) {
                            checkCompleteAllGet();
                        } else if (mItemOwnListener != null) {
                            onHideConnectionDialog();
                            mItemOwnListener.ItemOwn_Ok();
                            mItemOwnListener = null;
                        }
                        break;

                    default:
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                }

                break;
            }

            case Garbages_Recipe: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiGarbageRecipeParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiGarbageRecipeParam.ResultCode.valueOf(resultCode)) {
                    case OK:
                        onHideConnectionDialog();
                        if (mGarbageRecipeListener != null) {
                            mGarbageRecipeListener.GarbageRecipe_Ok(new GomipoiGarbageRecipeParam(response));
                        }
                        break;

                    default:
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                }

                break;
            }

            case Add_Jewel: {
                if (!(jsonData instanceof HashMap)) {
                    mAddJewelListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = UserAppJewelParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                onHideConnectionDialog();

                switch (UserAppJewelParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        if (mAddJewelListener != null) {
                            mAddJewelListener.AddJewel_Ok();
                        }
                        break;
                    }

                    case Shortage: {
                        if (mAddJewelListener != null) {
                            mAddJewelListener.AddJewel_OnShortageError();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpAddJewelParam = null;
                mAddJewelListener = null;
                break;
            }

            case Add_Point: {
                if (!(jsonData instanceof HashMap)) {
                    mAddPointListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = UserAppPointParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                onHideConnectionDialog();

                switch (UserAppPointParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        if (mAddPointListener != null) {
                            mAddPointListener.AddPoint_Ok();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpAddPointParam = null;
                mAddPointListener = null;
                break;
            }

            case All_Book_Own:
            case Book_Own: {
                if (!(jsonData instanceof HashMap)) {
                    mBookOwnListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiBookOwnParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiBookOwnParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        JniBridge.nativeOnReceivedGomipoiBookOwnResponse(result);

                        if (code.equals(ConnectionCode.All_Book_Own)) {
                            checkCompleteAllGet();
                        } else if (mBookOwnListener != null) {
                            onHideConnectionDialog();
                            mBookOwnListener.BookOwn_Ok();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mBookOwnListener = null;
                break;
            }

            case Garbage_Found: {
                if (!(jsonData instanceof HashMap)) {
                    mGarbageFoundListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiGarbageFoundParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiGarbageFoundParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        onHideConnectionDialog();
                        if (mGarbageFoundListener != null) {
                            mGarbageFoundListener.GarbageFound_Ok();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpGarbageFoundParam = null;
                mGarbageFoundListener = null;
                break;
            }

            case Garbage_Syntheses: {
                if (!(jsonData instanceof HashMap)) {
                    mGarbageSynthesesListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiGarbageSynthesesParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                onHideConnectionDialog();
                switch (GomipoiGarbageSynthesesParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        JniBridge.nativeOnReceivedGomipoiGarbageSynthesesResponse(result);
                        if (mGarbageSynthesesListener != null) {
                            mGarbageSynthesesListener.GarbageSyntheses_Ok();
                        }
                        break;
                    }

                    case NotHaveReceipe: {
                        onHideConnectionDialog();
                        if (mGarbageSynthesesListener != null) {
                            mGarbageSynthesesListener.GarbageSyntheses_NotHaveReceipe();
                        }
                        break;
                    }

                    case Failed: {
                        onHideConnectionDialog();
                        if (mGarbageSynthesesListener != null) {
                            mGarbageSynthesesListener.GarbageSyntheses_Failed();
                        }
                        break;
                    }

                    case AlreadySucceed: {
                        onHideConnectionDialog();
                        if (mGarbageSynthesesListener != null) {
                            mGarbageSynthesesListener.GarbageSyntheses_AlreadySucceed();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpGarbageSynthesesParam = null;
                mGarbageSynthesesListener = null;
                break;
            }

            case Game_Save: {
                if (!(jsonData instanceof HashMap)) {
                    mGameSaveListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiGameSaveParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                onHideConnectionDialog();

                switch (GomipoiGameSaveParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        JniBridge.nativeOnReceivedGomipoiGameSaveResponse(result);

                        if (mTmpGameSaveParam != null) {
                            GomipoiGarbageFoundParam garbageFoundParam = mTmpGameSaveParam.getGarbageFoundParam();
                            if (garbageFoundParam != null) {
                                garbageFound(garbageFoundParam, new OnGarbageFoundListener() {
                                    @Override
                                    public void GarbageFound_Ok() {
                                        getBookOwn(new GomipoiBookOwnParam(), new OnBookOwnListener() {
                                            @Override
                                            public void BookOwn_Ok() {
                                                if (mGameSaveListener != null) {
                                                    mGameSaveListener.GameSave_Ok();
                                                }
                                            }
                                        }, false);
                                    }
                                });
                                return;
                            }
                        }

                        if (mGameSaveListener != null) {
                            mGameSaveListener.GameSave_Ok();
                        }
                        break;
                    }

                    case AlreadyMaxCapacity: {

                        if (mGameSaveListener != null) {
                            mGameSaveListener.GameSave_AlreadyMaxCapacity();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mGameSaveListener = null;
                break;
            }

            case Jirokichi_Bonuses:
            {
                if (!(jsonData instanceof HashMap)) {
                    mJirokichiBonusesListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiJirokichiBonusesParams.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                onHideConnectionDialog();

                switch (GomipoiJirokichiBonusesParams.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        if (mJirokichiBonusesListener != null) {
                            mJirokichiBonusesListener.JirokichiBonuses_Ok();
                        }
                        break;
                    }

                    case NoBonus: {
                        if (mJirokichiBonusesListener != null) {
                            mJirokichiBonusesListener.JirokichiBonuses_NoBonus();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mJirokichiBonusesListener = null;
                break;
            }

            case Item_Use: {
                if (!(jsonData instanceof HashMap)) {
                    mItemUseListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiItemUseParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiItemUseParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        final GomipoiItemUseParam itemUseParam = (GomipoiItemUseParam)retryParam;
                        getItemOwn(new OnItemOwnListener() {
                            @Override
                            public void ItemOwn_Ok() {
                                ListItemData item = itemUseParam.itemData;
                                BroomType broomType = item.getBroomType();
                                GarbageCanType garbageCanType = item.getGarbageCanType();

                                if (!broomType.equals(BroomType.UNKNOWN)) {
                                    JniBridge.nativeChangeBroomType(broomType.getValue());
                                }
                                else if (!garbageCanType.equals(GarbageCanType.UNKNOWN)) {
                                    StageType stage = item.getGarbageCanStageType();
                                    if (stage.getValue() == JniBridge.nativeGetCurrentStage()) {
                                        JniBridge.nativeChangeGarbageCanType(garbageCanType.getValue());
                                    }
                                }
                                else if (item.id.equals(ItemId.Seal)) {
                                    JniBridge.nativeUseSeal();
                                }
                                else if (item.id.equals(ItemId.Telephone)) {
                                    JniBridge.nativeUseTelephone();
                                }
                                else if (item.id.equals(ItemId.POIKO)) {
                                    JniBridge.nativeChangeCharacter(CharacterType.POIKO.getValue());
                                }
                                else if (item.id.equals(ItemId.OTON)) {
                                    JniBridge.nativeChangeCharacter(CharacterType.OTON.getValue());
                                }
                                else if (item.id.equals(ItemId.KOTATSU)) {
                                    JniBridge.nativeChangeCharacter(CharacterType.KOTATSU.getValue());
                                }
                                else if (item.id.equals(ItemId.Z_DRINK)) {
                                    JniBridge.nativeOnUsedZDrink();
                                }
                                else if (item.id.equals(ItemId.DROP)) {
                                    JniBridge.nativeOnUsedDrop();
                                }
                                else if (item.id.equals(ItemId.AUTO_BRROM)) {
                                    JniBridge.nativeOnUsedAutoBroom();
                                }
                                else if (item.id.equals(ItemId.BATTERY)) {
                                    JniBridge.nativeOnUsedBattery();
                                }

                                if (mItemUseListener != null) {
                                    mItemUseListener.ItemUse_Ok();
                                }

                                mTmpItemUseParam = null;
                                mItemUseListener = null;
                            }
                        });
                        return;
                    }

                    case NotHave: {
                        onHideConnectionDialog();
                        if (mItemUseListener != null) {
                            mItemUseListener.ItemUse_NotHave();
                        }
                        break;
                    }

                    case Disabled: {
                        onHideConnectionDialog();
                        if (mItemUseListener != null) {
                            mItemUseListener.ItemUse_Disabled();
                        }
                        break;
                    }

                    case AlreadyEmptyCan: {
                        onHideConnectionDialog();
                        if (mItemUseListener != null) {
                            mItemUseListener.ItemUse_AlreadyEmptyCan();
                        }
                        break;
                    }

                    case NotChangeCan: {
                        onHideConnectionDialog();
                        if (mItemUseListener != null) {
                            mItemUseListener.ItemUse_NotChangeCan();
                        }
                        break;
                    }

                    case AlreadyUsed: {
                        onHideConnectionDialog();
                        if (mItemUseListener != null) {
                            mItemUseListener.ItemUse_AlreadyUsed();
                        }
                        break;
                    }

                    case DailyCountReached: {
                        onHideConnectionDialog();
                        if (mItemUseListener != null) {
                            mItemUseListener.ItemUse_DailyCountReached();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpItemUseParam = null;
                mItemUseListener = null;
                break;
            }

            case ItemSet_Buy: {
                DebugLog.i("ConnectionManager - onFinishedConnection ItemSet_Buy");
                if (!(jsonData instanceof HashMap)) {
                    mItemSetBuyListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiItemSetBuyParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiItemSetBuyParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        getItemOwn(new OnItemOwnListener() {
                            @Override
                            public void ItemOwn_Ok() {
                            if (mItemSetBuyListener != null) {
                                mItemSetBuyListener.ItemSetBuy_Ok(mTmpItemSetBuyParam.itemData.getPrice());
                            }

                            mTmpItemSetBuyParam = null;
                            mItemSetBuyListener = null;
                            }
                        });
                        return;
                    }

                    case GemShortage: {
                        onHideConnectionDialog();

                        if (mItemSetBuyListener != null) {
                            mItemSetBuyListener.ItemSetBuy_GemShortage();
                        }
                        break;
                    }

                    case AchievedMaxPossession: {
                        onHideConnectionDialog();

                        if (mItemSetBuyListener != null) {
                            mItemSetBuyListener.ItemSetBuy_AchievedMaxPossession();
                        }
                        break;
                    }

                    case NoKey: {
                        onHideConnectionDialog();

                        if (mItemSetBuyListener != null) {
                            mItemSetBuyListener.ItemSetBuy_NoKey();
                        }
                        break;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpItemSetBuyParam = null;
                mItemSetBuyListener = null;
                break;
            }

            case Book_Check: {
                if (!(jsonData instanceof HashMap)) {
                    mBookCheckListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiBookCheckParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (GomipoiBookCheckParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        getBookOwn(new GomipoiBookOwnParam(), new OnBookOwnListener() {
                            @Override
                            public void BookOwn_Ok() {
                                onHideConnectionDialog();

                                if (mBookCheckListener != null) {
                                    mBookCheckListener.BookCheck_Ok();
                                }
                            }
                        }, false);
                        return;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpBookCheckParam = null;
                mBookCheckListener = null;
                break;
            }

            case Book_ReceiveBonusPages: {
                if (!(jsonData instanceof HashMap)) {
                    mBookReceiveBonusesListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = GomipoiBookReceiveBonusesParam.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                onHideConnectionDialog();
                switch (GomipoiBookReceiveBonusesParam.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        JniBridge.nativeOnReceivedBookReceiveBonusesResponse(result);
                        getUserAppSelf(new OnUserAppSelfListener() {
                            @Override
                            public void UserAppSelf_Ok() {
                                if (mBookReceiveBonusesListener != null) {
                                    mBookReceiveBonusesListener.BookReceiveBonuses_Ok();
                                }
                            }
                        });
                        return;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mTmpBookReceiveBonusesParam = null;
                mBookReceiveBonusesListener = null;
                break;
            }

            case NewsTopic_Check: {
                if (!(jsonData instanceof HashMap)) {
                    mNewsTopicCheckListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = NewsTopicCheckResponse.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (NewsTopicCheckResponse.ResultCode.valueOf(resultCode)) {
                    case OK: {
                        NewsTopicCheckResponse newsTopicCheckResponse = new NewsTopicCheckResponse(response);
                        if (mNewsTopicCheckListener != null) {
                            mNewsTopicCheckListener.NewsTopicCheck_Ok(newsTopicCheckResponse);
                        }
                        return;
                    }

                    default: {
                        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                        break;
                    }
                }

                mNewsTopicCheckListener = null;
                break;
            }

            case System_Check: {
                if (!(jsonData instanceof HashMap)) {
                    mGetSystemCheckListener = null;
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> responseMap = (HashMap<String, Object>) jsonData;
                // メンテナンスの場合
                if (!DebugMode.isIgnoreMaintenance) {
                    if (responseMap.containsKey("maintenance")) {
                        HashMap<String, Object> maintainanceMap = (HashMap<String, Object>) responseMap.get("maintenance");
                        onHideConnectionDialog();
                        if (mGetSystemCheckListener != null) {
                            mGetSystemCheckListener.SystemCheck_ShowMaintenance(maintainanceMap);
                        }
                        mGetSystemCheckListener = null;
                        return;
                    }
                }

                // バージョン
                if (!DebugMode.isIgnoreUpdate) {
                    if (responseMap.containsKey("application")) {
                        HashMap<String, Object> applicationMap = (HashMap<String, Object>) responseMap.get("application");
                        String androidAppUrl = (String) applicationMap.get("android_app_url");
                        String androidRequiredVer = (String) applicationMap.get("android_required_ver");
//                        String androidLatestVer = (String) applicationMap.get("android_latest_ver");
                        if (AppVersionUtils.isNeedUpdate(mContext, androidRequiredVer)) {
                            onHideConnectionDialog();
                            if (mGetSystemCheckListener != null) {
                                mGetSystemCheckListener.SystemCheck_ShowVersionUp(androidAppUrl);
                            }
                            mGetSystemCheckListener = null;
                            return;
                        }
                    }
                }

                onHideConnectionDialog();
                if (mGetSystemCheckListener != null) {
                    mGetSystemCheckListener.SystemCheck_Ok();
                    mGetSystemCheckListener = null;
                }
                break;
            }

            // API: /users/premium_ticket
            case ExchangePremiumTicket: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                    int resultCode = ExchangePremiumTicketResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (ExchangePremiumTicketResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            mOnPremiumTicketListener.PremiumTicket_Ok();
                            break;
                        case FAILED:
                            mOnPremiumTicketListener.PremiumTicket_Failed();
                            break;
                    }
                    onCompletedBuyPremiumTicket();
                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }


            // API: /gomipoi_friends(.:format)
            case GomipoiFriends: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = GomipoiFriendsResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (GomipoiFriendsResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            int friendCount = 0;
                            int friendMax = 0;
                            HashMap<String, Object> selfUserMap = GomipoiFriendsResponse.getSelfUserMap(response);
                            if (selfUserMap != null) {
                                if (selfUserMap.get("friend_count") != null) {
                                    friendCount = Integer.parseInt(selfUserMap.get("friend_count").toString());
                                }

                                if (selfUserMap.get("friend_max") != null) {
                                    friendMax = Integer.parseInt(selfUserMap.get("friend_max").toString());
                                }
                            }

                            List<Object> list = (List)GomipoiFriendsResponse.getList(response);
                            List<GomipoiFriendsResponse> responseList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                GomipoiFriendsResponse responseData = new GomipoiFriendsResponse((HashMap<String, Object>)list.get(i));
                                responseList.add(responseData);
                            }

                            if (mOnFriendListListener != null) {
                                mOnFriendListListener.FriendList_OK_Friends(friendCount, friendMax, responseList);
                            }

                            onCompletedGetFriendList();

                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friends/id=?
            case GomipoiFriendDelete: {
                DebugLog.i("ConnectionManager - GomipoiFriendDelete");
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = GomipoiFriendsResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (GomipoiFriendsResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            if (mOnFriendDeleteListener != null) {
                                mOnFriendDeleteListener.FriendDeleteSuccess();
                            }
                            onFriendDeleteSuccess();
                            break;
                        case FAILED:
                            DebugLog.i("ConnectionManager - Failed to delete friend.");
                            break;
                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                    /*switch (GomipoiFriendsDelete.ResultCode.valueOf(resultCode)) {
                        case OK:
                            if (mOnFriendDeleteListener != null) {
                                mOnFriendDeleteListener.FriendDeleteSuccess();
                            }
                            onFriendDeleteSuccess();
                            break;
                        case FAILED:
                            DebugLog.i("ConnectionManager - Failed to delete friend.");
                            break;
                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }*/
                }

                break;
            }

            // API: /week_rankings/current
            // API: /week_rankings/last
            case GomipoiFriendsRank: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = GomipoiFriendsRankResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (GomipoiFriendsRankResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            String executedAt = "";
                            HashMap<String, Object> ranking = GomipoiFriendsRankResponse.getRankingDate(response);
                            if (ranking != null) {
                                if (ranking.get("executed_at") != null) {
                                    executedAt = ranking.get("executed_at").toString();
                                }
                            }

                            int selfId = 0;
                            int selfRank = 0;
                            int selfPoint = 0;
                            HashMap<String, Object> selfRankMap = GomipoiFriendsRankResponse.getSelfRank(response);
                            if (selfRankMap != null) {
                                if (selfRankMap.get("id") != null) {
                                    //selfId = Integer.parseInt(selfRankMap.get("id").toString());
                                    selfId = JsonUtils.getIntElement(selfRankMap, "id");
                                }

                                if (selfRankMap.get("ranking") != null) {
                                    //selfRank = Integer.parseInt(selfRankMap.get("ranking").toString());
                                    selfRank = JsonUtils.getIntElement(selfRankMap, "ranking");
                                }

                                if (selfRankMap.get("point") != null) {
                                    //selfPoint = Integer.parseInt(selfRankMap.get("point").toString());
                                    selfPoint = JsonUtils.getIntElement(selfRankMap, "point");
                                }
                            }

                            DebugLog.i("ConnectionManager - Response: " + response.toString());

                            List<Object> list = (List) GomipoiFriendsRankResponse.getList(response);
                            List<GomipoiFriendsRankResponse> responseList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                GomipoiFriendsRankResponse responseData = new GomipoiFriendsRankResponse((HashMap<String, Object>)list.get(i));
                                responseList.add(responseData);
                            }

                            if (mOnFriendListRankListener != null) {
                                mOnFriendListRankListener.FriendListRank_OK(executedAt, selfId, selfRank, selfPoint, responseList);
                            }

                            onCompletedGetFriendListRank();

                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_messages/badge_count(.:format)
            case FriendMessages_BadgeCount: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendMessagesBadgeCountResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendMessagesBadgeCountResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            FriendMessagesBadgeCountResponse apiResponse = new FriendMessagesBadgeCountResponse(response);

                            if (mOnFriendListListener != null) {
                                mOnFriendListListener.FriendList_OK_FriendMessageBadge(apiResponse.unreadCount);
                            }

                            onCompletedGetFriendList();
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /system_messages/badge_count(.:format)
            case SystemMessages_BadgeCount: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = SystemMessagesBadgeCountResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (SystemMessagesBadgeCountResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            SystemMessagesBadgeCountResponse apiResponse = new SystemMessagesBadgeCountResponse(response);

                            if (mOnFriendListListener != null) {
                                mOnFriendListListener.FriendList_OK_SystemMessageBadge(apiResponse.unreadCount);
                            }

                            onCompletedGetFriendList();
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friends/search_by_code(.:format)
            case Friends_SearchByCode: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendsSearchByCodeResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendsSearchByCodeResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            FriendsSearchByCodeResponse res = null;
                            if (response.get("friend") != null) {
                                res = new FriendsSearchByCodeResponse(
                                        mFriensSearchByCodeParam.friendCode,
                                        (HashMap) response.get("friend"));
                            }

                            if (mOnFriendsSearchByCodeListener != null) {
                                mOnFriendsSearchByCodeListener.FriendsSearchByCode_Ok(res);
                            }

                            mFriensSearchByCodeParam = null;

                            onHideConnectionDialog();
                            break;

                        case INVALID_CODE:
                            mFriensSearchByCodeParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendsSearchByCodeListener != null) {
                                mOnFriendsSearchByCodeListener.FriendsSearchByCode_InvalidCode();
                            }
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_invitations(.:format)
            case FriendInvitations: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendInvitationsParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendInvitationsParam.ResultCode.valueOf(resultCode)) {
                        case OK:

                            if (mOnFriendInvitationsListener != null) {
                                mOnFriendInvitationsListener.FriendInvitations_Ok();
                            }
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_code/use(.:format)
            case FriendCode_Use: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendCodeUseParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendCodeUseParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            if (mOnFriendCodeUseListener != null) {
                                mOnFriendCodeUseListener.FriendCodeUse_Ok();
                            }
                            break;

                        case NOT_MATCH_USER:
                            mFriendCodeUseParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendCodeUseListener != null) {
                                mOnFriendCodeUseListener.FriendCodeUse_NotMatchUser();
                            }
                            break;

                        case IS_SELF:
                            mFriendCodeUseParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendCodeUseListener != null) {
                                mOnFriendCodeUseListener.FriendCodeUse_IsSelf();
                            }
                            break;

                        case ALREADY_FRIEND:
                            mFriendCodeUseParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendCodeUseListener != null) {
                                mOnFriendCodeUseListener.FriendCodeUse_AlreadyFriend();
                            }
                            break;

                        case REACHED_UPPER_LIMIT_SELF:
                            mFriendCodeUseParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendCodeUseListener != null) {
                                mOnFriendCodeUseListener.FriendCodeUse_ReachedUpperLimitSelf();
                            }
                            break;

                        case REACHED_UPPER_LIMIT_FRIEND:
                            mFriendCodeUseParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendCodeUseListener != null) {
                                mOnFriendCodeUseListener.FriendCodeUse_ReachedUpperLimitFriend();
                            }
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friends/for_present_request(.:format)
            case Friends_ForPresentRequest: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendsForPresentRequestResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendsForPresentRequestResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            List<Object> list = FriendsForPresentRequestResponse.getList(response);
                            List<FriendsForPresentRequestResponse> responseList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                FriendsForPresentRequestResponse responseData = new FriendsForPresentRequestResponse((HashMap<String, Object>)list.get(i));
                                responseList.add(responseData);
                            }

                            if (mOnFriendsForPresentRequestListener != null) {
                                mOnFriendsForPresentRequestListener.FriendsForPresentRequest_Ok(responseList);
                            }
                            onHideConnectionDialog();
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_presents(.:format)_FriendList
            case FriendPresents_FriendList: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendPresentsParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendPresentsParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            mFriendPresentsParam_FriendList = null;

                            if (mOnFriendPresentsListener_FriendList != null) {
                                mOnFriendPresentsListener_FriendList.FriendPresents_Ok();
                            }
                            break;

                        case EXCEED_UPPER_LIMIT_SELF: {
                            mFriendPresentsParam_FriendList = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentsListener_FriendList != null) {
                                mOnFriendPresentsListener_FriendList.FriendPresents_ExceedUpperLimitSelf();
                            }
                            break;
                        }

                        case EXCEED_UPPER_LIMIT_FRIEND: {
                            mFriendPresentsParam_FriendList = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentsListener_FriendList != null) {
                                mOnFriendPresentsListener_FriendList.FriendPresents_ExceedUpperLimitFriend();
                            }
                            break;
                        }

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }

                break;
            }

            // API: /friend_presents(.:format)_MessageList
            case FriendPresents_MessageList: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendPresentsParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendPresentsParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            mFriendPresentsParam_MessageList = null;

                            if (mOnFriendPresentsListener_MessageList != null) {
                                mOnFriendPresentsListener_MessageList.FriendPresents_Ok();
                            }
                            break;

                        case EXCEED_UPPER_LIMIT_SELF: {
                            mFriendPresentsParam_MessageList = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentsListener_MessageList != null) {
                                mOnFriendPresentsListener_MessageList.FriendPresents_ExceedUpperLimitSelf();
                            }
                            break;
                        }

                        case EXCEED_UPPER_LIMIT_FRIEND: {
                            mFriendPresentsParam_MessageList = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentsListener_MessageList != null) {
                                mOnFriendPresentsListener_MessageList.FriendPresents_ExceedUpperLimitFriend();
                            }
                            break;
                        }

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_presents/use(.:format)
            case FriendPresents_Use: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendPresentsUseParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendPresentsUseParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            mFriendPresentsUseParam = null;

                            if (mOnFriendPresentsUseListener != null) {
                                mOnFriendPresentsUseListener.FriendPresentsUse_Ok();
                            }
                            break;

                        case EXPIRATION: {
                            mFriendPresentsUseParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentsUseListener != null) {
                                mOnFriendPresentsUseListener.FriendPresentsUse_Expiration();
                            }
                            break;
                        }

                        case NO_EFFECT: {
                            mFriendPresentsUseParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentsUseListener != null) {
                                mOnFriendPresentsUseListener.FriendPresentsUse_NoEffect();
                            }
                            break;
                        }

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_present_requests(.:format)_GimmeList
            case FriendPresentRequests_GimmeList: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendPresentRequestsParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendPresentRequestsParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            mFriendPresentRequestsParam_GimmeList = null;

                            if (mOnFriendPresentRequestsListener_GimmeList != null) {
                                mOnFriendPresentRequestsListener_GimmeList.FriendPresentRequests_Ok();
                            }
                            break;

                        case EXCEED_UPPER_LIMIT: {
                            mFriendPresentRequestsParam_GimmeList = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentRequestsListener_GimmeList != null) {
                                mOnFriendPresentRequestsListener_GimmeList.FriendPresentRequests_ExceedUpperLimit();
                            }
                            break;
                        }

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_present_requests(.:format)_FriendList
            case FriendPresentRequests_FriendList: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendPresentRequestsParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendPresentRequestsParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            mFriendPresentRequestsParam_FriendList = null;

                            if (mOnFriendPresentRequestsListener_FriendList != null) {
                                mOnFriendPresentRequestsListener_FriendList.FriendPresentRequests_Ok();
                            }
                            break;

                        case EXCEED_UPPER_LIMIT: {
                            mFriendPresentRequestsParam_FriendList = null;
                            onHideConnectionDialog();

                            if (mOnFriendPresentRequestsListener_FriendList != null) {
                                mOnFriendPresentRequestsListener_FriendList.FriendPresentRequests_ExceedUpperLimit();
                            }
                            break;
                        }

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /system_messages(.:format)
            case SystemMessages: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = SystemMessagesResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    String serverDate = null;
                    if (response.get("server_date") != null) {
                        serverDate = response.get("server_date").toString();
                    }

                    switch (SystemMessagesResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            List<Object> list = (List)SystemMessagesResponse.getList(response);
                            List<MessagesResponse> responseList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                SystemMessagesResponse responseData = new SystemMessagesResponse((HashMap<String, Object>)list.get(i));
                                responseList.add(responseData);
                            }

                            if (mOnFriendMessagesListener != null) {
                                mOnFriendMessagesListener.FriendMessages_Ok(serverDate, responseList);
                            }

                            onCompletedGetFriendMessages();

                            onHideConnectionDialog();
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friend_messages(.:format)
            case FriendMessages: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendMessagesResponse.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    String serverDate = null;
                    if (response.get("server_date") != null) {
                        serverDate = response.get("server_date").toString();
                    }

                    switch (FriendMessagesResponse.ResultCode.valueOf(resultCode)) {
                        case OK:
                            List<Object> list = (List)FriendMessagesResponse.getList(response);
                            List<MessagesResponse> responseList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                FriendMessagesResponse responseData = new FriendMessagesResponse((HashMap<String, Object>)list.get(i));
                                responseList.add(responseData);
                            }

                            if (mOnFriendMessagesListener != null) {
                                mOnFriendMessagesListener.FriendMessages_Ok(serverDate, responseList);
                            }

                            onHideConnectionDialog();
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /friendship_bonuses/receive(.:format)
            case FriendshipBonuses: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = FriendshipBonusParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (FriendshipBonusParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            mFriendshipBonusParam = null;

                            if (mOnFriendshipBonusListener != null) {
                                mOnFriendshipBonusListener.FriendshipBonus_Ok();
                            }
                            break;

                        case EXPIRATION:
                            mFriendshipBonusParam = null;
                            onHideConnectionDialog();

                            if (mOnFriendshipBonusListener != null) {
                                mOnFriendshipBonusListener.FriendshipBonus_Expiration();
                            }
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            case GomipoiGame_MovePlace: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = GomipoiGameMovePlaceParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (GomipoiGameMovePlaceParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            if (mOnGameMovePlaceListener != null) {
                                mOnGameMovePlaceListener.GameMovePlace_Ok();
                            }
                            break;

                        case AlreadyInRoom:
                            onHideConnectionDialog();

                            if (mOnGameMovePlaceListener != null) {
                                mOnGameMovePlaceListener.GameMovePlace_AlreadyInPlace();
                            }
                            break;

                        case NoKeyRoom:
                            onHideConnectionDialog();

                            if (mOnGameMovePlaceListener != null) {
                                mOnGameMovePlaceListener.GameMovePlace_NoKeyRoom();
                            }
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            case RegisterDeviceToken: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> response = (HashMap<String, Object>) jsonData;

                    int resultCode = RegisterDeviceTokenParam.ResultCode.UNKNOWN.getValue();
                    if (response.get("result_code") != null) {
                        resultCode = Integer.parseInt(response.get("result_code").toString());
                    }

                    switch (RegisterDeviceTokenParam.ResultCode.valueOf(resultCode)) {
                        case OK:
                            if (mOnRegisterDeviceTokenListener!= null) {
                                mOnRegisterDeviceTokenListener.RegisterDeviceToken_Ok();
                            }
                            break;

                        default:
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                    }

                } else {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /notifications/check
            case Notifications_Check: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> responce = (HashMap<String, Object>)jsonData;

                    int resultCode = NotificationsCheckResponse.ResultCode.UNKNOWN.getValue();
                    if (responce.get("result_code") != null) {
                        resultCode = Integer.parseInt(responce.get("result_code").toString());
                    }

                    switch (NotificationsCheckResponse.ResultCode.valueOf(resultCode)) {
                        case OK: {
                            if (mOnCheckedNotificationListner != null) {
                                onHideConnectionDialog();
                                NotificationsCheckResponse notificationsCheckResponse = new NotificationsCheckResponse(responce);
                                mOnCheckedNotificationListner.CheckNotification_Ok(notificationsCheckResponse.unread);
                                mOnCheckedNotificationListner = null;
                            }

                            break;
                        }

                        default: {
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                        }
                    }
                } else {
                  onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /top_notifications/check
            case TopNotifications_Check: {
                if (jsonData instanceof HashMap) {
                    HashMap<String, Object> responce = (HashMap<String, Object>)jsonData;

                    int resultCode = TopNotificationsCheckResponse.ResultCode.UNKNOWN.getValue();
                    if (responce.get("result_code") != null) {
                        resultCode = Integer.parseInt(responce.get("result_code").toString());
                    }

                    switch (TopNotificationsCheckResponse.ResultCode.valueOf(resultCode)) {
                        case OK: {
                            if (mOnCheckedTopNotificationsListener != null) {
                                onHideConnectionDialog();
                                TopNotificationsCheckResponse notificationsCheckResponse = new TopNotificationsCheckResponse(responce);
                                mOnCheckedTopNotificationsListener.CheckTopNotifications_Ok(notificationsCheckResponse.notification_unread, notificationsCheckResponse.friend_unread);
                                mOnCheckedTopNotificationsListener = null;
                            }

                            break;
                        }

                        default: {
                            onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                            break;
                        }
                    }
                } else {
                  onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                }
                break;
            }

            // API: /users/guest_register
            case User_Guest_Register: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> response = (HashMap<String, Object>) jsonData;
                int resultCode = UserGuestRegisterResponce.ResultCode.UNKNOWN.getValue();
                if (response.get("result_code") != null) {
                    resultCode = Integer.parseInt(response.get("result_code").toString());
                }

                switch (UserGuestRegisterResponce.ResultCode.valueOf(resultCode)) {
                    case OK:
                        if (mOnUserGuestRegisteredListner != null) {
                            UserGuestRegisterResponce regResponce = new UserGuestRegisterResponce(response);
                            mOnUserGuestRegisteredListner.UserGuestRegister_Ok(regResponce);
                        }

                        break;

                    default:
                        onShowNetworkErrorDialog(code, null);
                        break;
                }

                mOnUserGuestRegisteredListner = null;
                break;
            }

            case User_Definitive_Register: {
                if (!(jsonData instanceof HashMap)) {
                    onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
                    return;
                }

                HashMap<String, Object> responce = (HashMap<String, Object>) jsonData;
                int resultCode = UserDefinitiveRegisterParam.ResultCode.UNKNOWN.getValue();
                if (responce.get("result_code") != null) {
                    resultCode = Integer.parseInt(responce.get("result_code").toString());
                }

                switch (UserDefinitiveRegisterParam.ResultCode.valueOf(resultCode)) {
                    case OK:

                        boolean isNew = InstanceIDListenerService.isNew(mContext);
                        // 登録完了なら、そのままログイン処理に移る
                        userLogin(new UserSessionsParam(
                                mTempUserDefinitiveRegisterParam.parentActivity,
                                mTempUserDefinitiveRegisterParam.account,
                                mTempUserDefinitiveRegisterParam.password,
                                isNew ? InstanceIDListenerService.getDeviceToken(mContext) : null), null);
                        mTempUserDefinitiveRegisterParam = null;
                        return;
                    case EXIST_SAME_ACCOUNT:
                        onHideConnectionDialog();
                        if (mOnUserDefinitiveRegisterdListener != null) {
                            mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_OnShowExistSameAccountError();
                        }
                        break;
                    case EXIST_SAME_NICKNAME:
                        onHideConnectionDialog();
                        if (mOnUserDefinitiveRegisterdListener != null) {
                            mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_OnShowExistSameNicknameError();
                        }
                        break;
                    case INVALID_PASSWORD:
                        onHideConnectionDialog();
                        if (mOnUserDefinitiveRegisterdListener != null) {
                            mOnUserDefinitiveRegisterdListener.UserDefinitiveRegist_OnShowInvalidPasswordError();
                        }
                        break;
                    default:
                        onHideConnectionDialog();
                        onShowNetworkErrorDialog(code, null);
                        break;
                }

                mTempUserDefinitiveRegisterParam = null;
                mOnUserDefinitiveRegisterdListener = null;

                break;
            }

            default: {
                onHideConnectionDialog();
                onShowNetworkErrorDialog(code, null);
                break;
            }
        }

    }

    @Override
    public void onErroredConnection(int connectionCode) {
        ConnectionCode code = ConnectionCode.valueOf(connectionCode);
        Object retryParam = getRetryData(code);
        onShowNetworkErrorDialog(code, new RetryData(code, retryParam));
    }

    // -----------------------------------
    // Accesser
    // -----------------------------------
    public void setDeviceId(String deviceId) {
        mUUID = deviceId;
    }

    /**
     * onResume時に呼ぶ
     */
    public void onResume() {
        getHttpManager().onResume();
    }

    /**
     * onPause時に呼ぶ
     */
    public void onPause() {
        getHttpManager().onPause();
    }

    /**
     * onDestroy時に呼ぶ
     */
    public void onDestroy() {
        if (mHttpManager != null) {
            mHttpManager.stopAll();
            mHttpManager = null;
        }

        mContext = null;
        mListener = null;
        mTmpRegistParam = null;
        mRegistListener = null;
        mTmpLoginParam = null;
        mLoginListener = null;
        mTempUserDefinitiveRegisterParam = null;
        mOnUserDefinitiveRegisterdListener = null;
    }

    /**
     * ユーザー登録処理を行う
     */
    public void userRegist(UsersParam param, OnUserRegistListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Users;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mRegistListener = listener;
        mTmpRegistParam = param;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeLoginHttpHeaderMap(mUUID));
    }

    /**
     * ユーザーログインの処理を行う
     */
    public void userLogin(UserSessionsParam param, OnUserLoginListener listener) {
        ConnectionCode connectionCode = ConnectionCode.User_Sessions;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mLoginListener = listener;
        mTmpLoginParam = param;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeLoginHttpHeaderMap(mUUID));
    }

    /**
     * ユーザーログアウトの処理を行う
     */
    public void userLogout() {
        ConnectionCode connectionCode = ConnectionCode.User_Sessions_Logout;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        onShowConnectionDialog();

        getHttpManager().startDelete(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public void getUserAppSelf(OnUserAppSelfListener listener) {
        ConnectionCode connectionCode = ConnectionCode.UserApps_Self;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        mUserAppSelfListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public void getGameLoad(OnGameLoadListener listener) {
        ConnectionCode connectionCode = ConnectionCode.GomipoiGame_Load;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        mGameLoadListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public void getSystemCheck (OnGetSystemCheckListener listener){

        // System_Check
        ConnectionCode connectionCode = ConnectionCode.System_Check;

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        onShowConnectionDialog();

        mGetSystemCheckListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.syste_url,
                null,
                null);
    }

    /**
     * 全Get通信を一度に行う
     */
    public void getAllData(OnAllGetListener listener) {
        mAllGetCompleteCount = 0;
        mAllGetListener = listener;

        //
        // All_UserApps_Self
        // ------------------------------
        ConnectionCode connectionCode = ConnectionCode.All_UserApps_Self;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        onShowConnectionDialog();

        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));

        //
        // All_GomipoiGarbage_Own
        // ------------------------------
        connectionCode = ConnectionCode.All_GomipoiGarbage_Own;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));

        //
        // All_GomipoiGame_Load
        // ------------------------------
        connectionCode = ConnectionCode.All_GomipoiGame_Load;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));

        //
        // All_Book_Own
        // ------------------------------
        connectionCode = ConnectionCode.All_Book_Own;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));

        //
        // All_GomipoiItem_Own
        // ------------------------------
        connectionCode = ConnectionCode.All_GomipoiItem_Own;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public void reloadGame(OnAllGetListener listener) {
        mAllGetCompleteCount = 4;
        mAllGetListener = listener;

        //
        // All_GomipoiGame_Load
        // ------------------------------
        ConnectionCode connectionCode = ConnectionCode.All_GomipoiGame_Load;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        onShowConnectionDialog();

        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));

    }

    public void getItemOwn(OnItemOwnListener listener) {
        ConnectionCode connectionCode = ConnectionCode.GomipoiItem_Own;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        onShowConnectionDialog();

        mItemOwnListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public final void getGarbageRecipe(OnGarbageRecipeListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Garbages_Recipe;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        onShowConnectionDialog();

        mGarbageRecipeListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * 宝石所持数を増減する
     */
    public void addJewel(UserAppJewelParam param, OnAddJewelListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Add_Jewel;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mTmpAddJewelParam = param;
        mAddJewelListener = listener;
        getHttpManager().startPatch(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * ポイント数を増減する
     */
    public void addPoint(UserAppPointParam param, OnAddPointListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Add_Jewel;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mTmpAddPointParam = param;
        mAddPointListener = listener;
        getHttpManager().startPatch(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public void getBookOwn(GomipoiBookOwnParam param, OnBookOwnListener listener) {
        getBookOwn(param, listener, true);
    }

    /**
     * 図鑑の情報を取得する
     */
    public void getBookOwn(GomipoiBookOwnParam param, OnBookOwnListener listener,
                           boolean isShowConnectingDialog) {
        ConnectionCode connectionCode = ConnectionCode.Book_Own;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        if (isShowConnectingDialog) {
            onShowConnectionDialog();
        }

        mBookOwnListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * 新たに発見したゴミを送る
     */
    public void garbageFound(GomipoiGarbageFoundParam param, OnGarbageFoundListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Garbage_Found;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        mTmpGarbageFoundParam = param;
        mGarbageFoundListener = listener;

        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * 合成処理を送る
     */
    public void garbageSyntheses(GomipoiGarbageSynthesesParam param,
                                 OnGarbageSynthesesListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Garbage_Syntheses;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mTmpGarbageSynthesesParam = param;
        mGarbageSynthesesListener = listener;

        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * ゲーム情報を保存する
     */
    public void gameSave(GomipoiGameSaveParam param, String deviceId, OnGameSaveListener listener) {
        mUUID = deviceId;

        ConnectionCode connectionCode = ConnectionCode.Game_Save;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        mTmpGameSaveParam = param;
        mGameSaveListener = listener;


        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, deviceId));
    }

    /**
     * ゲーム情報を保存する
     */
    public void gameJirokichiBonus(GomipoiJirokichiBonusesParams param, OnJirokichiBonusesListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Jirokichi_Bonuses;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        mTmpJirokichiBonusesParam = param;
        mJirokichiBonusesListener = listener;

        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * アイテムを使用する
     */
    public void useItem(GomipoiItemUseParam param, OnItemUseListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Item_Use;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mTmpItemUseParam = param;
        mItemUseListener = listener;

        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * アイテムを使用する
     */
    public void buyItem(GomipoiItemSetBuyParam param, OnItemSetBuyListener listener) {
        ConnectionCode connectionCode = ConnectionCode.ItemSet_Buy;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mTmpItemSetBuyParam = param;
        mItemSetBuyListener = listener;

        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    /**
     * 図鑑のNewを外す通知を行う
     */
    public void bookCheck(GomipoiBookCheckParam param, OnBookCheckListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Book_Check;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mTmpBookCheckParam = param;
        mBookCheckListener = listener;

        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public void bookReceiveBonuses(GomipoiBookReceiveBonusesParam param,
                                   OnBookReceiveBonusesListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Book_ReceiveBonusPages;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mTmpBookReceiveBonusesParam = param;
        mBookReceiveBonusesListener = listener;

        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    public void newsTopicCheck(OnNewsTopicCheckListener listener) {
        ConnectionCode connectionCode = ConnectionCode.NewsTopic_Check;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        mNewsTopicCheckListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /users/premium_ticket
    public void postPremiumTicket(OnPremiumTicketListener listener) {
        if (mUUID == null) {
            onShowErrorDialog(ConnectionCode.ExchangePremiumTicket);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(ConnectionCode.ExchangePremiumTicket, null);
            return;
        }

        onShowConnectionDialog();

        mOnPremiumTicketListener = listener;

        {
            ConnectionCode connectionCode = ConnectionCode.ExchangePremiumTicket;
            getHttpManager().startPost(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode),
                    /*params.makeParam()*/ null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }
    }

    // API: /gomipoi_friends(.:format)
    public void getFriendList(OnFriendListListener listener) {
        if (mUUID == null) {
            onShowErrorDialog(ConnectionCode.GomipoiFriends);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(ConnectionCode.GomipoiFriends, null);
            return;
        }

        onShowConnectionDialog();

        mLoadingFriendCompleteCount = 0;
        mOnFriendListListener = listener;

        // API: /gomipoi_friends(.:format)
        {
            ConnectionCode connectionCode = ConnectionCode.GomipoiFriends;
            getHttpManager().startGet(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode),
                    null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }

        // API: /friend_messages/badge_count(.:format)
        {
            ConnectionCode connectionCode = ConnectionCode.FriendMessages_BadgeCount;
            getHttpManager().startGet(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode),
                    null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }

        // API: /system_messages/badge_count(.:format)
        {
            ConnectionCode connectionCode = ConnectionCode.SystemMessages_BadgeCount;
            getHttpManager().startGet(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode),
                    null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }
    }

    // API: /friends/id=?
    public void deleteFriend(OnFriendDeleteListener listener, int id) {
        DebugLog.i("ConnectionManager - deleteFriend trigger.");
        if (mUUID == null) {
            onShowErrorDialog(ConnectionCode.GomipoiFriendDelete);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(ConnectionCode.GomipoiFriendDelete, null);
            return;
        }

        onShowConnectionDialog();

        mOnFriendDeleteListener = listener;

        DebugLog.i("ConnectionManager - deleteFriend startDelete().");
        {
            ConnectionCode connectionCode = ConnectionCode.GomipoiFriendDelete;
            getHttpManager().startDelete(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode) + id,
                    /*params.makeParam()*/ null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }
    }

    // API: /week_rankings/current
    // API: /week_rankings/last
    public void getFriendListRank(OnFriendListRankListener listener) {
        if (mUUID == null) {
            onShowErrorDialog(ConnectionCode.GomipoiFriendsRank);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(ConnectionCode.GomipoiFriendsRank, null);
            return;
        }

        onShowConnectionDialog();

        mOnFriendListRankListener = listener;

        // API: /gomipoi_friends(.:format)
        {
            ConnectionCode connectionCode = ConnectionCode.GomipoiFriendsRank;
            getHttpManager().startGet(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode),
                    null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }
    }

    // API: /friends/search_by_code(.:format)
    public final void friendSearch(FriendsSearchByCodeParam param, OnFriendsSearchByCodeListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Friends_SearchByCode;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriensSearchByCodeParam = param;
        mOnFriendsSearchByCodeListener = listener;

        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_messages(.:format)
    // API: /system_messages(.:format)
    public final void getFriendMessageList(OnFriendMessagesListener listener) {
        if (mUUID == null) {
            onShowErrorDialog(ConnectionCode.FriendMessages);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(ConnectionCode.FriendMessages, null);
            return;
        }

        onShowConnectionDialog();

        mLoadingFriendMessagesCompleteCount = 0;
        mOnFriendMessagesListener = listener;

        // API: /friend_messages(.:format)
        {
            ConnectionCode connectionCode = ConnectionCode.FriendMessages;
            getHttpManager().startGet(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode),
                    null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }

        // API: /system_messages(.:format)
        {
            ConnectionCode connectionCode = ConnectionCode.SystemMessages;
            getHttpManager().startGet(
                    connectionCode.getValue(),
                    mContext,
                    HttpDefine.protocol,
                    HttpDefine.host,
                    HttpDefine.port,
                    HttpDefine.getFilePath(connectionCode),
                    null,
                    HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
        }
    }

    // API: /friend_code/use(.:format)
    public final void friendCodeUse(FriendCodeUseParam param, OnFriendCodeUseListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendCode_Use;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriendCodeUseParam = param;
        mOnFriendCodeUseListener = listener;

        getHttpManager().startPatch(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_presents(.:format)_FriendList
    public final void sendPresents_FriendList(FriendPresentsParam param, OnFriendPresentsListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendPresents_FriendList;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriendPresentsParam_FriendList = param;
        mOnFriendPresentsListener_FriendList = listener;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_presents(.:format)_MessageList
    public final void sendPresents_MessageList(FriendPresentsParam param, OnFriendPresentsListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendPresents_MessageList;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriendPresentsParam_MessageList = param;
        mOnFriendPresentsListener_MessageList = listener;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_presents/use(.:format)
    public final void sendPresents(FriendPresentsUseParam param, OnFriendPresentsUseListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendPresents_Use;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriendPresentsUseParam = param;
        mOnFriendPresentsUseListener = listener;
        getHttpManager().startPatch(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_presents/use(.:format)
    public final void sendFriendshipBonus(FriendshipBonusParam param, OnFriendshipBonusListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendshipBonuses;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriendshipBonusParam = param;
        mOnFriendshipBonusListener = listener;
        getHttpManager().startPatch(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friends/for_present_request(.:format)
    public final void getPresentRequestList(OnFriendsForPresentRequestListener listener) {
        ConnectionCode connectionCode = ConnectionCode.Friends_ForPresentRequest;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, null);
            return;
        }

        onShowConnectionDialog();

        mOnFriendsForPresentRequestListener = listener;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_present_requests(.:format)_GimmeList
    public final void requestPresent_GimmeList(FriendPresentRequestsParam param, OnFriendPresentRequestsListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendPresentRequests_GimmeList;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriendPresentRequestsParam_GimmeList = param;
        mOnFriendPresentRequestsListener_GimmeList = listener;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_present_requests(.:format)_FriendList
    public final void requestPresent_FriendList(FriendPresentRequestsParam param, OnFriendPresentRequestsListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendPresentRequests_FriendList;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mFriendPresentRequestsParam_FriendList = param;
        mOnFriendPresentRequestsListener_FriendList = listener;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /friend_invitations(.:format)
    public final void invitation(FriendInvitationsParam param, OnFriendInvitationsListener listener) {
        ConnectionCode connectionCode = ConnectionCode.FriendInvitations;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mOnFriendInvitationsListener = listener;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /gomipoi_games/move_place(.:format)
    public final void changePlace(GomipoiGameMovePlaceParam param, OnGameMovePlaceListener listener) {
        ConnectionCode connectionCode = ConnectionCode.GomipoiGame_MovePlace;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mOnGameMovePlaceListener = listener;
        getHttpManager().startPatch(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /users/register_device_token(.:format)
    public final void registerDeviceToken(RegisterDeviceTokenParam param, OnRegisterDeviceTokenListener listener) {
        ConnectionCode connectionCode = ConnectionCode.RegisterDeviceToken;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, param));
            return;
        }

        onShowConnectionDialog();

        mOnRegisterDeviceTokenListener = listener;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID));
    }

    // API: /notifications/check
    public final void checkNotification(OnCheckedNotificationListner listner) {
        ConnectionCode connectionCode = ConnectionCode.Notifications_Check;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, null));
            return;
        }

        onShowConnectionDialog();

        mOnCheckedNotificationListner = listner;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext,mUUID));
    }

    // API: /top_notifications/check
    public final void checkTopNotifications(OnCheckedTopNotificationsListner listner) {
        ConnectionCode connectionCode = ConnectionCode.TopNotifications_Check;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, null));
            return;
        }

        onShowConnectionDialog();

        mOnCheckedTopNotificationsListener = listner;
        getHttpManager().startGet(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext,mUUID));
    }

    // API: /users/guest_register
    public final void userGuestRegist(OnUserGuestRegisteredListner listner) {
        ConnectionCode connectionCode = ConnectionCode.User_Guest_Register;

        if (mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, null));
            return;
        }

        onShowConnectionDialog();

        mOnUserGuestRegisteredListner = listner;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                null,
                HttpDefine.makeLoginHttpHeaderMap(mUUID));
    }

    // API: /users/definitive_register
    public final void userDefinitiveRegist(UserDefinitiveRegisterParam param, OnUserDefinitiveRegisterdListener listener) {
        ConnectionCode connectionCode = ConnectionCode.User_Definitive_Register;

        if (param == null || mUUID == null) {
            onShowErrorDialog(connectionCode);
            return;
        }

        if (!NetworkUtils.isEnabled(mContext)) {
            onShowNetworkErrorDialog(connectionCode, new RetryData(connectionCode, null));
            return;
        }

        onShowConnectionDialog();

        mTempUserDefinitiveRegisterParam = param;
        mOnUserDefinitiveRegisterdListener = listener;
        getHttpManager().startPost(
                connectionCode.getValue(),
                mContext,
                HttpDefine.protocol,
                HttpDefine.host,
                HttpDefine.port,
                HttpDefine.getFilePath(connectionCode),
                param.makeParam(),
                HttpDefine.makeHttpHeaderMap((GBApplication) mContext, mUUID)
        );
    }

    // -----------------------------------
    // Function
    // -----------------------------------
    /**
     * HttpManagerBaseのインスタンスを返す
     */
    public HttpManagerBase getHttpManager() {
        if (mHttpManager == null) {
            mHttpManager = new HttpManagerBase();
            mHttpManager.setOnHttpManagerListener(this);
        }
        return mHttpManager;
    }

    /**
     * 保存していたデータからリトライ用のデータを返す
     */
    private Object getRetryData(ConnectionCode code) {
        Object retryData = null;
        switch (code) {
            case Users: {
                if (mTmpRegistParam != null) {
                    retryData = mTmpRegistParam.cloneParam();
                }
                break;
            }

            case User_Sessions: {
                if (mTmpLoginParam != null) {
                    retryData = mTmpLoginParam.cloneParam();
                }
                break;
            }

            case Add_Jewel: {
                if (mTmpAddJewelParam != null) {
                    retryData = mTmpAddJewelParam.cloneParam();
                }
                break;
            }

            case Add_Point: {
                if (mTmpAddPointParam != null) {
                    retryData = mTmpAddPointParam.cloneParam();
                }
                break;
            }

            case Garbage_Found: {
                if (mTmpGarbageFoundParam != null) {
                    retryData = mTmpGarbageFoundParam.cloneParam();
                }
                break;
            }

            case Garbage_Syntheses: {
                if (mTmpGarbageSynthesesParam != null) {
                    retryData = mTmpGarbageSynthesesParam.cloneParam();
                }
                break;
            }

            case Game_Save: {
                if (mTmpGameSaveParam != null) {
                    retryData = mTmpGameSaveParam.cloneParam();
                }
                break;
            }

            case Jirokichi_Bonuses: {
                if (mTmpJirokichiBonusesParam != null) {
                    retryData = mTmpJirokichiBonusesParam.cloneParam();
                }
                break;
            }

            case Item_Use: {
                if (mTmpItemUseParam != null) {
                    retryData = mTmpItemUseParam.cloneParam();
                }
                break;
            }

            case ItemSet_Buy: {
                if (mTmpItemSetBuyParam != null) {
                    retryData = mTmpItemSetBuyParam.cloneParam();
                }
                break;
            }

            case Book_Check: {
                if (mTmpBookCheckParam != null) {
                    retryData = mTmpBookCheckParam.cloneParam();
                }
                break;
            }

            case Book_ReceiveBonusPages: {
                if (mTmpBookReceiveBonusesParam != null) {
                    retryData = mTmpBookReceiveBonusesParam.cloneParam();
                }
                break;
            }

            case Friends_SearchByCode: {
                if (mFriensSearchByCodeParam != null) {
                    retryData = mFriensSearchByCodeParam.cloneParam();
                }
                break;
            }

            case FriendCode_Use: {
                if (mFriendCodeUseParam != null) {
                    retryData = mFriendCodeUseParam.cloneParam();
                }
                break;
            }

            case FriendPresents_FriendList: {
                if (mFriendPresentsParam_FriendList != null) {
                    retryData = mFriendPresentsParam_FriendList.cloneParam();
                }
                break;
            }

            case FriendPresents_MessageList: {
                if (mFriendPresentsParam_MessageList != null) {
                    retryData = mFriendPresentsParam_MessageList.cloneParam();
                }
                break;
            }

            case FriendPresents_Use: {
                if (mFriendPresentsUseParam != null) {
                    retryData = mFriendPresentsUseParam.cloneParam();
                }
                break;
            }

            case FriendPresentRequests_FriendList: {
                if (mFriendPresentRequestsParam_FriendList != null) {
                    retryData = mFriendPresentRequestsParam_FriendList.cloneParam();
                }
                break;
            }

            case FriendPresentRequests_GimmeList: {
                if (mFriendPresentRequestsParam_GimmeList != null) {
                    retryData = mFriendPresentRequestsParam_GimmeList.cloneParam();
                }
                break;
            }

            case User_Sessions_Logout:
            case All_UserApps_Self:
            case UserApps_Self:
            case All_GomipoiGarbage_Own:
            case All_GomipoiGame_Load:
            case All_GomipoiItem_Own:
            case GomipoiGame_Load:
            case GomipoiFriends:
            case Friends_ForPresentRequest:
            case FriendMessages:
            case FriendMessages_BadgeCount:
            case SystemMessages_BadgeCount:
            case FriendInvitations:
            default: {
                break;
            }
        }
        return retryData;
    }

    /**
     * 全てのGet通信が完了したかをチェックする
     */
    private void checkCompleteAllGet() {
        mAllGetCompleteCount += 1;
        if (mAllGetCompleteCount < 5) {
            return;
        }

        onHideConnectionDialog();

        if (mAllGetListener != null) {
            mAllGetListener.AllGet_Ok();
        }
        mAllGetListener = null;
    }

    private void onCompletedBuyPremiumTicket() {
        onHideConnectionDialog();

        if (mOnPremiumTicketListener != null) {

        }
        mOnPremiumTicketListener = null;
    }

    private void onCompletedGetFriendList() {
        mLoadingFriendCompleteCount += 1;
        if (mLoadingFriendCompleteCount < 3) {
            return;
        }

        onHideConnectionDialog();
        if (mOnFriendListListener != null) {
            mOnFriendListListener.FriendList_AllOk();
        }
        mOnFriendListListener = null;
    }

    private void onFriendDeleteSuccess() {
        onHideConnectionDialog();
        if (mOnFriendDeleteListener != null) {
            mOnFriendDeleteListener.FriendDeleteSuccess();
        }
        mOnFriendDeleteListener = null;
    }

    private void onCompletedGetFriendListRank() {

        onHideConnectionDialog();
        if (mOnFriendListRankListener != null) {
            mOnFriendListRankListener.FriendListRank_AllOk();
        }
        mOnFriendListRankListener = null;
    }

    private void onCompletedGetFriendMessages() {
        mLoadingFriendMessagesCompleteCount += 1;
        if (mLoadingFriendMessagesCompleteCount < 2) {
            return;
        }

        onHideConnectionDialog();
        if (mOnFriendMessagesListener != null) {
            mOnFriendMessagesListener.MessagesAll_Ok();
        }
        mOnFriendMessagesListener = null;
    }

    /**
     * NetworkErrorダイアログを表示する
     */
    private void onShowNetworkErrorDialog(ConnectionCode code, RetryData retryData) {
        onHideConnectionDialog();

        if (!code.isNeedRetry()) {
            if (retryData != null
                    && retryData instanceof RetryData
                    && ((RetryData)retryData).param instanceof UserSessionsParam
                    && !((UserSessionsParam)((RetryData)retryData).param).isCalledLoginActivity()) {
                if (mListener != null) {
                    mListener.ConnectionManager_OnShowGetNetworkErrorDialog(retryData);
                }
                return;
            }

            if (mListener != null) {
                mListener.ConnectionManager_OnShowNetworkErrorDialog();
            }
            return;
        }

        switch (code) {
            case User_Sessions:
            case All_UserApps_Self:
            case UserApps_Self:
            case All_GomipoiGarbage_Own:
            case All_GomipoiGame_Load:
            case All_GomipoiItem_Own:
            case GomipoiGame_Load:
            case GomipoiItem_Own:
            case Garbages_Recipe:
            case Book_Own:
            case All_Book_Own:
            case NewsTopic_Check:
            case System_Check:
            case  Notifications_Check:
            case  TopNotifications_Check:
            {
                if (mListener != null) {
                    mListener.ConnectionManager_OnShowGetNetworkErrorDialog(retryData);
                }
                break;
            }

            default: {
                if (mListener != null) {
                    mListener.ConnectionManager_OnShowPostNetworkErrorDialog(retryData);
                }
                break;
            }
        }
    }

    /**
     * エラーダイアログを表示する
     */
    private void onShowErrorDialog(ConnectionCode code) {
        if (mListener != null) {
            mListener.ConnectionManager_OnError(code);
        }
    }

    /**
     * 通信中ダイアログを表示する
     */
    private void onShowConnectionDialog() {
        if (mListener != null) {
            mListener.ConnectionManager_OnShowConnectDialog();
        }
    }

    /**
     * 通信中ダイアログを閉じる
     */
    private void onHideConnectionDialog() {
        if (mListener != null) {
            mListener.ConnectionManager_OnHideConnectDialog();
        }
    }

    // -----------------------------------
    // Inner-Class
    // -----------------------------------
    public static class RetryData implements Serializable {
        public ConnectionCode code;
        public Object param;

        public RetryData(ConnectionCode code, Object param) {
            this.code = code;
            this.param = param;
        }
    }

    // -----------------------------------
    // Callback
    // -----------------------------------
    public interface OnConnectionManagerListener {
        void ConnectionManager_OnDeleteLoginDataForLogout(boolean isNeedShowDialog);
        void ConnectionManager_OnAuthorizedError();
        void ConnectionManager_OnShowConnectDialog();
        void ConnectionManager_OnHideConnectDialog();
        void ConnectionManager_OnShowNetworkErrorDialog();
        void ConnectionManager_OnShowGetNetworkErrorDialog(RetryData retryData);
        void ConnectionManager_OnShowPostNetworkErrorDialog(RetryData retryData);
        void ConnectionManager_OnError(ConnectionCode code);
    }

    public interface OnUserRegistListener {
        void UserRegist_Ok();
        void UserRegist_OnShowExistSameAccountError();
        void UserRegist_OnNotMatchUserError();
        void UserRegist_OnShowExistSameNicknameError();
        void UserRegist_OnShowInvalidPasswordError();
    }

    public interface OnUserLoginListener {
        void UserLogin_Ok();
        void UserLogin_AutoLoginOk();
        void UserLogin_OnNotMatchUserError();
    }

    public interface OnGetSystemCheckListener {
        void SystemCheck_Ok();
        void SystemCheck_ShowMaintenance(HashMap<String, Object> maintenanceMap);
        void SystemCheck_ShowVersionUp(String storeUrl);
    }

    public interface OnAllGetListener {
        void AllGet_Ok();
    }

    public interface OnUserAppSelfListener {
        void UserAppSelf_Ok();
    }

    public interface OnGameLoadListener {
        void GameLoad_Ok();
    }

    public interface OnAddJewelListener {
        void AddJewel_Ok();
        void AddJewel_OnShortageError();
    }

    public interface OnAddPointListener {
        void AddPoint_Ok();
    }

    public interface OnBookOwnListener {
        void BookOwn_Ok();
    }

    public interface OnGarbageFoundListener {
        void GarbageFound_Ok();
    }

    public interface OnGarbageSynthesesListener {
        void GarbageSyntheses_Ok();
        void GarbageSyntheses_NotHaveReceipe();
        void GarbageSyntheses_Failed();
        void GarbageSyntheses_AlreadySucceed();
    }

    public interface OnGameSaveListener {
        void GameSave_Ok();
        void GameSave_AlreadyMaxCapacity();
    }

    public interface OnJirokichiBonusesListener {
        void JirokichiBonuses_Ok();
        void JirokichiBonuses_NoBonus();
    }

    public interface OnItemUseListener {
        void ItemUse_Ok();
        void ItemUse_NotHave();
        void ItemUse_AlreadyEmptyCan();
        void ItemUse_AlreadyUsed();
        void ItemUse_Disabled();
        void ItemUse_NotChangeCan();
        void ItemUse_DailyCountReached();
    }

    public interface OnItemSetBuyListener {
        void ItemSetBuy_Ok(int price);
        void ItemSetBuy_GemShortage();
        void ItemSetBuy_AchievedMaxPossession();
        void ItemSetBuy_NoKey();
    }

    public interface OnItemOwnListener {
        void ItemOwn_Ok();
    }

    public interface OnBookCheckListener {
        void BookCheck_Ok();
    }

    public interface OnBookReceiveBonusesListener {
        void BookReceiveBonuses_Ok();
    }

    public interface OnNewsTopicCheckListener {
        void NewsTopicCheck_Ok(NewsTopicCheckResponse response);
    }

    public interface OnGarbageRecipeListener {
        void GarbageRecipe_Ok(GomipoiGarbageRecipeParam response);
    }

    // API: /user/premium_ticket
    public interface OnPremiumTicketListener {
        void PremiumTicket_Ok();
        void PremiumTicket_Failed();
    }

    // API: /gomipoi_friends(.:format)
    public interface OnFriendListListener {
        void FriendList_AllOk();
        void FriendList_OK_Friends(int friendCount, int friendMaxCount, List<GomipoiFriendsResponse> listData);
        void FriendList_OK_FriendMessageBadge(int count);
        void FriendList_OK_SystemMessageBadge(int count);
    }

    // API: /friends/id=?
    public interface OnFriendDeleteListener {
        void FriendDeleteSuccess();
    }

    // API: /week_rankings/current
    // API: /week_rankings/last
    public interface OnFriendListRankListener {
        void FriendListRank_AllOk();
        void FriendListRank_OK(String executedAt, int selfId, int selfRank, int selfPoint, List<GomipoiFriendsRankResponse> listData);
    }

    // API: /friends/search_by_code(.:format)
    public interface OnFriendsSearchByCodeListener {
        void FriendsSearchByCode_Ok(FriendsSearchByCodeResponse response);
        void FriendsSearchByCode_InvalidCode();
    }

    // API: /friend_messages(.:format)
    public interface OnFriendMessagesListener {
        void FriendMessages_Ok(String serverDate, List<MessagesResponse> listData);
        void SystemMessages_Ok(String serverDate, List<MessagesResponse> listData);
        void MessagesAll_Ok();
    }

    // API: /friend_code/use(.:format)
    public interface OnFriendCodeUseListener {
        void FriendCodeUse_Ok();
        void FriendCodeUse_NotMatchUser();
        void FriendCodeUse_IsSelf();
        void FriendCodeUse_AlreadyFriend();
        void FriendCodeUse_ReachedUpperLimitSelf();
        void FriendCodeUse_ReachedUpperLimitFriend();
    }

    // API: /friend_presents(.:format)
    public interface OnFriendPresentsListener {
        void FriendPresents_Ok();
        void FriendPresents_ExceedUpperLimitSelf();
        void FriendPresents_ExceedUpperLimitFriend();
    }

    // API: /friend_presents/use(.:format)
    public interface OnFriendPresentsUseListener {
        void FriendPresentsUse_Ok();
        void FriendPresentsUse_Expiration();
        void FriendPresentsUse_NoEffect();
    }

    // API: /friendship_bonuses/receive(.:format)
    public interface OnFriendshipBonusListener {
        void FriendshipBonus_Ok();
        void FriendshipBonus_Expiration();
    }

    // API: /friends/for_present_request(.:format)
    public interface OnFriendsForPresentRequestListener {
        void FriendsForPresentRequest_Ok(List<FriendsForPresentRequestResponse> listData);
    }

    // API: /friend_present_requests(.:format)
    public interface OnFriendPresentRequestsListener {
        void FriendPresentRequests_Ok();
        void FriendPresentRequests_ExceedUpperLimit();
    }

    // API: /friend_invitations(.:format)
    public interface OnFriendInvitationsListener {
        void FriendInvitations_Ok();
    }

    // API: /gomipoi_games/move_place(.:format)
    public interface OnGameMovePlaceListener {
        void GameMovePlace_Ok();
        void GameMovePlace_AlreadyInPlace();
        void GameMovePlace_NoKeyRoom();
    }

    // API: /gomipoi_games/move_place(.:format)
    public interface OnRegisterDeviceTokenListener {
        void RegisterDeviceToken_Ok();
    }

    // API: /notifications/check
    public interface OnCheckedNotificationListner {
        void CheckNotification_Ok(boolean unread);
    }

    // API: /top_notifications/check
    public interface OnCheckedTopNotificationsListner {
        void CheckTopNotifications_Ok(boolean notification_unread, boolean friend_unread);
    }

    // API: /users/guest_register
    public interface OnUserGuestRegisteredListner {
        void UserGuestRegister_Ok(UserGuestRegisterResponce responce);
    }

    // API: /users/definitive_register
    public interface OnUserDefinitiveRegisterdListener {
        void UserDefinitiveRegist_Ok_Setting();
        void UserDefinitiveRegist_Ok_Top();
        void UserDefinitiveRegist_Ok_Other();
        void UserDefinitiveRegist_Ok_Exchange();
        void UserDefinitiveRegist_OnNotMatchUserError();
        void UserDefinitiveRegist_OnShowExistSameAccountError();
        void UserDefinitiveRegist_OnShowExistSameNicknameError();
        void UserDefinitiveRegist_OnShowInvalidPasswordError();
    }
}

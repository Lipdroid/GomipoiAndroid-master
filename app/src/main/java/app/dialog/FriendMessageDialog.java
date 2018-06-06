package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;


import com.topmission.gomipoi.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.adapter.FriendMessageAdapter;
import app.animation.button.ButtonAnimationManager;
import app.data.friend.FriendMessageDialogResponse;
import app.data.http.FriendMessagesResponse;
import app.data.http.MessagesResponse;
import app.define.DialogCode;
import app.define.FriendActionCode;
import common.dialog.GBDialogBase;
import lib.adapter.OnAdapterListener;
import lib.log.DebugLog;

/**
 * メッセージダイアログ
 */
public class FriendMessageDialog extends GBDialogBase {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private ListView listView;
    private List<MessagesResponse> messageList;
    private Button buttonLeft;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static FriendMessageDialog newInstance(String name) {
        FriendMessageDialog dialog = new FriendMessageDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected boolean isPermitTouchOutside() {
        return false;
    }

    @Override
    protected boolean isPermitCancel() {
        return true;
    }

    @Override
    public int getDialogCode() {
        return DialogCode.FriendMessage.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        LayoutInflater inflator = getInflater();
        View contentView = inflator.inflate(R.layout.dialog_friend_message, null);

        listView = (ListView)contentView.findViewById(R.id.listView);
        if (listView != null) {
            listView.setEmptyView(contentView.findViewById(R.id.textViewEmpty));
        }

        buttonLeft = (Button)contentView.findViewById(R.id.buttonLeft);
        if (buttonLeft != null) {
            new ButtonAnimationManager(buttonLeft, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    sendResult(RESULT_NG, null);
                }
            });
        }

        sendResult(
                RESULT_OK,
                new FriendMessageDialogResponse(
                        FriendActionCode.DATA_GET,
                        null));

        return contentView;
    }

    @Override
    public void onDestroyView() {
        if (listView != null) {
            listView.setAdapter(null);
        }

        if (buttonLeft != null) {
            buttonLeft.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final void onReceivedFriendMessageData(String serverDate, List<MessagesResponse> listData) {
        if (listView == null) {
            DebugLog.e("NullPo");
            return;
        }

        boolean isComplete;
        if (messageList == null) {
            isComplete = false;

            messageList = listData;
        }
        else {
            messageList.addAll(listData);

            Collections.sort(messageList, new Comparator<MessagesResponse>() {
                @Override
                public int compare(MessagesResponse lhs, MessagesResponse rhs) {
                    Date lhsDate = lhs.getDate();
                    Date rhsDate = rhs.getDate();

                    if (lhsDate != null && rhsDate != null) {
                        return lhsDate.compareTo(rhsDate);
                    }
                    else return 0;
                }
            });

            isComplete = true;
        }

        if (isComplete) {
            if (listView.getAdapter() == null) {
                listView.setAdapter(new FriendMessageAdapter(
                        getApplicationContext(),
                        serverDate,
                        messageList,
                        new OnAdapterListener() {

                            @Override
                            public void onEvent(int eventCode, Object data) {
                                if (isEventLocked()) {
                                    return;
                                }
                                lockEvent();

                                switch (FriendActionCode.valueOf(eventCode)) {

                                    case RECEIVE: {
                                        sendResult(
                                                RESULT_OK,
                                                new FriendMessageDialogResponse(
                                                        FriendActionCode.RECEIVE,
                                                        (MessagesResponse) data));
                                        break;
                                    }

                                    case INVITE: {
                                        sendResult(
                                                RESULT_OK,
                                                new FriendMessageDialogResponse(
                                                        FriendActionCode.INVITE,
                                                        (MessagesResponse) data));
                                        break;
                                    }

                                    case DELETE: {
                                        sendResult(RESULT_OK, new FriendMessageDialogResponse(FriendActionCode.DELETE,
                                                (MessagesResponse) data));
                                        break;
                                    }

                                    default: {
                                        unlockEvent();
                                        break;
                                    }
                                }

                            }

                        }));
            } else {
                FriendMessageAdapter adapter = (FriendMessageAdapter) listView.getAdapter();
                adapter.refresh(serverDate, messageList);
                adapter.notifyDataSetChanged();
            }

            messageList = null;
        }
    }

}

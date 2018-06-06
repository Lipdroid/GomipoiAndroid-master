package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.data.friend.SearchResponse;
import app.data.http.FriendsSearchByCodeResponse;
import app.define.DialogCode;
import app.define.FriendActionCode;
import common.dialog.GBDialogBase;
import lib.edittextview.EditTextUtils;

/**
 * 友人検索ダイアログ
 */
public class SearchFriendDialog extends GBDialogBase {

    // ------------------------------
    // Member
    // ------------------------------
    private EditText editTextCode;
    private View backButton;
    private View noUserView;
    private Button buttonSearch;
    private TextView textViewName;
    private Button buttonAdd;

    private FriendsSearchByCodeResponse mFriendData;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static SearchFriendDialog newInstance() {
        SearchFriendDialog dialog = new SearchFriendDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * new instance with friend code
     * @param friendCode friend code
     * @return newInstance
     */
    public static SearchFriendDialog newInstance(@Nullable String friendCode) {
        SearchFriendDialog dialog = new SearchFriendDialog();
        Bundle args = new Bundle();
        args.putString("code", friendCode);
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
        return DialogCode.SearchFriend.getValue();
    }

    @Override
    public String getDialogName() {
        return "SearchFriendDialog";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        LayoutInflater inflator = getInflater();
        View contentView = inflator.inflate(R.layout.dialog_search_friend, null);

        backButton = contentView.findViewById(R.id.back_button);
        if (backButton != null) {
            new ButtonAnimationManager(backButton, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        noUserView = contentView.findViewById(R.id.textViewNoName);
        noUserView.setVisibility(View.GONE);

        editTextCode = (EditText)contentView.findViewById(R.id.editTextCode);
        if (editTextCode != null) {
            EditTextUtils.setInputFilter(editTextCode, EditTextUtils.MODE_Alphanumeric);
            // set QR code result
            String qrCode = getArguments().getString("code");
            if (!TextUtils.isEmpty(qrCode)) {
                editTextCode.setText(qrCode);
            }
        }

        buttonSearch = (Button)contentView.findViewById(R.id.buttonSearch);
        if (buttonSearch != null) {
            new ButtonAnimationManager(buttonSearch, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EditTextUtils.isEmpty(editTextCode)) {
                        return;
                    }

                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    getSearchResult(editTextCode.getText().toString());
                }
            });
        }

        textViewName = (TextView)contentView.findViewById(R.id.textViewName);
        if (textViewName != null) {
            textViewName.setVisibility(View.GONE);
        }

        buttonAdd = (Button)contentView.findViewById(R.id.buttonAdd);
        if (buttonAdd != null) {
            new ButtonAnimationManager(buttonAdd, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    sendResult(
                            RESULT_OK,
                            new SearchResponse(
                                    FriendActionCode.APPLY,
                                    editTextCode.getText().toString(),
                                    mFriendData));
                }
            });
            buttonAdd.setVisibility(View.GONE);
        }

        return contentView;
    }

    @Override
    public void onDestroyView() {
        if (buttonSearch != null) {
            buttonSearch.setOnClickListener(null);
        }

        if (buttonAdd != null) {
            buttonAdd.setOnClickListener(null);
        }

        if (backButton != null) {
            backButton.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final void onReceivedSearchResult(FriendsSearchByCodeResponse data) {
        mFriendData = data;

        if (textViewName != null) {
            textViewName.setVisibility(data != null ? View.VISIBLE : View.GONE);
            textViewName.setText(data != null ? data.nickname : "");
        }

        if (buttonAdd != null) {
            buttonAdd.setVisibility(data != null ? View.VISIBLE : View.GONE);
        }

        if (noUserView != null) {
            noUserView.setVisibility(data != null ? View.GONE : View.VISIBLE);
        }

    }

    // ------------------------------
    // Function
    // ------------------------------
    private void getSearchResult(String searchText) {
        sendResult(RESULT_OK, new SearchResponse(searchText));
    }

}

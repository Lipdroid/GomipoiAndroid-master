package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.util.ArrayList;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import app.define.SeData;
import app.define.StageType;
import app.jni.JniBridge;
import common.dialog.GBDialogBase;

/**
 * Created by Herve on 2016/09/15.
 */
public class GameMenuDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_AVAILABLE_ROOMS = "#1#" + System.currentTimeMillis();

    public static final int RESPONSE_TOP = 0;
    public static final int RESPONSE_MAIN_ROOM = 1;
    public static final int RESPONSE_POIKO_ROOM = 2;
    public static final int RESPONSE_GARDEN = 3;

    // ------------------------------
    // Member
    // ------------------------------
    private ImageButton buttonMainRoom;
    private ImageButton buttonPoikoRoom;
    private ImageButton garden_button;
    private ImageButton buttonTop;
    private TextView levelTextView;
    private TextView restTextView;

    private ArrayList<StageType> availableRooms;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static GameMenuDialog newInstance(ArrayList<StageType> availableRooms) {
        GameMenuDialog dialog = new GameMenuDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_KEY_AVAILABLE_ROOMS, availableRooms);
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
        return DialogCode.GameMenu.getValue();
    }

    @Override
    public String getDialogName() {
        return "GameMenu";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        LayoutInflater inflator = getInflater();
        View contentView = inflator.inflate(R.layout.dialog_game_menu, null);

        availableRooms = (ArrayList<StageType>) getArguments().getSerializable(ARG_KEY_AVAILABLE_ROOMS);
        if (availableRooms == null) {
            availableRooms = new ArrayList<>();
        }

        buttonMainRoom = (ImageButton) contentView.findViewById(R.id.main_room_button);
        if (buttonMainRoom != null)
            new ButtonAnimationManager(buttonMainRoom, this);

        buttonPoikoRoom = (ImageButton) contentView.findViewById(R.id.poiko_room_button);
        if (buttonPoikoRoom != null)
            new ButtonAnimationManager(buttonPoikoRoom, this);

        garden_button = (ImageButton) contentView.findViewById(R.id.garden_button);
        if (garden_button != null)
            new ButtonAnimationManager(garden_button, this);

        buttonTop = (ImageButton) contentView.findViewById(R.id.to_top_button);
        if (buttonTop != null)
            new ButtonAnimationManager(buttonTop, this);

        buttonMainRoom.setEnabled(availableRooms.contains(StageType.DefaultRoom));
        buttonPoikoRoom.setEnabled(availableRooms.contains(StageType.PoikoRoom));
        garden_button.setEnabled(availableRooms.contains(StageType.Garden));

        // レベル
        levelTextView = (TextView) contentView.findViewById(R.id.level_textview);
        String level = Integer.toString(JniBridge.nativeGetLevel());
        levelTextView.setText(level);

        // レベルアップまでに必要な経験値
        restTextView = (TextView) contentView.findViewById(R.id.rest_textview);
        int experiencePoint = JniBridge.nativeGetExperiencePoint();
        int nextLevelRequiredPoint = JniBridge.nativeGetNextLevelRequiredPoint();
        int restPoint = 0;
        if (nextLevelRequiredPoint > experiencePoint) {
            restPoint = nextLevelRequiredPoint - experiencePoint;
        }
        String rest = Integer.toString(restPoint);
        restTextView.setText(rest);

        return contentView;
    }

    @Override
    protected boolean onClickedBackButton() {
        sendResult(RESULT_NG, null);

        return super.onClickedBackButton();
    }

    // ------------------------------
    // OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        if (isEventLocked()) {
            return;
        }
        lockEvent();

        if (getApp() != null) {
            getApp().getSeManager().playSe(SeData.YES);
        }

        switch (v.getId()) {
            case R.id.to_top_button:
                sendResult(RESULT_OK, RESPONSE_TOP);
                break;

            case R.id.main_room_button:
                sendResult(RESULT_OK, RESPONSE_MAIN_ROOM);
                break;

            case R.id.poiko_room_button:
                sendResult(RESULT_OK, RESPONSE_POIKO_ROOM);
                break;

            case R.id.garden_button:
                sendResult(RESULT_OK, RESPONSE_GARDEN);
                break;
        }
    }
}

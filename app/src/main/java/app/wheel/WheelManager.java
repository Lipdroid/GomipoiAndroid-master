package app.wheel;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import app.wheel.lib.WheelManagerBase;
import app.wheel.lib.WheelView;
import app.wheel.lib.number.NumberWheelView;

/**
 */
public class WheelManager extends WheelManagerBase {

    // ------------------------------
    // Define
    // ------------------------------
    private final int STANDARD_INTERVAL = 150;
    private final int MAX_DIGIT_VALUE = 10;

    // ------------------------------
    // Member
    // ------------------------------
    private List<View> mSeparatorList;

    private int mCurrentValue;

    // ------------------------------
    // Constructor
    // ------------------------------
    public WheelManager(Context context) {
        super(context);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onCreate(Object initData) {
        super.onCreate(initData);

        mCurrentValue = (int)initData;
        refreshSeparatorView(mCurrentValue);
    }

    @Override
    public void onDestroy() {
        if (mSeparatorList != null) {
            mSeparatorList.clear();
            mSeparatorList = null;
        }

        super.onDestroy();
    }

    @Override
    public boolean changeValue(Object newValue) {
        if (newValue == null || !(newValue instanceof Integer)) {
            return false;
        }

        int value = (int) newValue;
        return !(value < 0 || value >= (int) Math.pow(10, getWheelList().size())) && super.changeValue(newValue);
    }

    @Override
    protected void setWheelInitData(Object initData, WheelView wheelView) {
        if (wheelView == null || !(initData instanceof Integer)
                || !(wheelView instanceof NumberWheelView)) {
            return;
        }

        int currentValue = (int)initData;
        int digit = ((NumberWheelView) wheelView).getDigit();
        wheelView.setViewAdapter(new WheelAdapter(getContext(), digit));
        wheelView.setCurrentItem(getItemsToScroll(
                currentValue,
                0,
                digit));
        wheelView.setVisibility(isVisibleDigit(currentValue, digit) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void forcedChangeValue(Object object) {
        if (object == null || !(object instanceof Integer)) {
            return;
        }

        int newValue = (int)object;
        refreshSeparatorView(newValue);

        for (int i = 0; i < getWheelList().size(); i++) {
            WheelView wheelView = getWheelList().get(i);
            if (wheelView == null || !(wheelView instanceof NumberWheelView)) {
                continue;
            }

            int digit = ((NumberWheelView)wheelView).getDigit();
            wheelView.setVisibility(isVisibleDigit(newValue, digit) ? View.VISIBLE : View.INVISIBLE);

            int itemsToScroll = getItemsToScroll(
                    newValue,
                    wheelView.getCurrentItem(),
                    digit);

            if (newValue >= mCurrentValue) {
                // 増える時の移動量は必ず正の数であってほしい
                if (itemsToScroll < 0) {
                    itemsToScroll = MAX_DIGIT_VALUE + itemsToScroll;
                }

                int time = STANDARD_INTERVAL * (digit + 1);

                wheelView.scroll(
                        itemsToScroll,
                        time);
            } else {
                // 減る時の移動量は必ず負の数であってほしい
                if (itemsToScroll > 0) {
                    itemsToScroll = itemsToScroll - MAX_DIGIT_VALUE;
                }
                wheelView.scroll(
                        itemsToScroll,
                        STANDARD_INTERVAL * (getWheelList().size() - digit));
            }

        }

        mCurrentValue = newValue;
    }


    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * WheelViewを追加する
     */
    public final void addWheelView(WheelView wheelView, int digit) {
        if (wheelView == null) {
            return;
        }

        if (wheelView instanceof NumberWheelView) {
            ((NumberWheelView) wheelView).setDigit(digit);
        }

        super.addWheelView(wheelView);
    }

    /**
     * SeparatorViewを追加する
     */
    public final void addSeparatorView(View separatorView, int digit) {
        separatorView.setTag(digit);
        getSeparatorList().add(separatorView);
    }


    // ------------------------------
    // Function
    // ------------------------------
    /**
     * セパレーターのリストを返す
     */
    private List<View> getSeparatorList() {
        if (mSeparatorList == null) {
            mSeparatorList = new ArrayList<>();
        }
        return mSeparatorList;
    }

    /**
     * 各桁の移動量を求める
     */
    private int getItemsToScroll(int newValue, int oldDigitValue, int digit) {
        // 新しい桁の値を算出する
        int newDigitValue = newValue;
        if (digit > 0) {
            double divValue = Math.pow(MAX_DIGIT_VALUE, digit);
            newDigitValue = (int)(newValue / divValue);
        }
        newDigitValue = newDigitValue % MAX_DIGIT_VALUE;

        // 現在の桁の値との差を算出する
        return newDigitValue - oldDigitValue;
    }

    /**
     * 表示する桁かを返す
     */
    private boolean isVisibleDigit(int value, int digit) {
        if (digit == 0) {
            return true;
        }

        int digitValue = (int)(Math.pow(MAX_DIGIT_VALUE, digit));
        return (digitValue <= value);
    }

    /**
     * セパレーターの状態を更新する
     */
    private void refreshSeparatorView(int value) {
        for (int i = 0; i < getSeparatorList().size(); i++) {
            View separator = getSeparatorList().get(i);
            if (separator == null) {
                continue;
            }

            int digit = (int)separator.getTag();
            separator.setVisibility(isVisibleDigit(value, digit) ? View.VISIBLE : View.INVISIBLE);
        }

    }

}

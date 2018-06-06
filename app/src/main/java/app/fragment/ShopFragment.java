package app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.topmission.gomipoi.R;

import java.util.ArrayList;
import java.util.List;

import app.adapter.ShopAdapter;
import app.billing.PurchaseManager;
import app.data.BookGarbageData;
import app.data.Garbage;
import app.data.ShopItem;
import app.data.http.GomipoiGameSaveParam;
import app.data.http.GomipoiGarbageFoundParam;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.define.GarbageId;
import app.jni.JniBridge;
import app.jni.OnGLJniBridgeListener;
import app.number.NumberUtils;
import app.view.OutlineTextView;
import common.fragment.GBFragmentBase;

/**
 * ショップ画面
 */
public class ShopFragment extends GBFragmentBase implements AdapterView.OnItemClickListener, OnGLJniBridgeListener {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private ListView listView;
    private OutlineTextView outlineTextViewGem;

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, null);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        List<ShopItem> dataList = new ArrayList<>();
        dataList.add(new ShopItem(PurchaseManager.StoreItem.jewel_10, 120, 10, 1));
        dataList.add(new ShopItem(PurchaseManager.StoreItem.jewel_50, 600, 50, 2));
        dataList.add(new ShopItem(PurchaseManager.StoreItem.jewel_100, 1080, 100, 3));
        dataList.add(new ShopItem(PurchaseManager.StoreItem.jewel_300, 2900, 300, 4));
        dataList.add(new ShopItem(PurchaseManager.StoreItem.jewel_500, 4700, 500, 5));
        dataList.add(new ShopItem(PurchaseManager.StoreItem.jewel_1000, 8800, 1000, 6));

        listView = (ListView) view.findViewById(R.id.listView);
        if (listView != null) {
            listView.setAdapter(new ShopAdapter(getApplicationContext(), dataList, null));
            listView.setOnItemClickListener(this);
        }

        outlineTextViewGem = (OutlineTextView) view.findViewById(R.id.outlineTextViewGem);
        if (outlineTextViewGem != null) {
            outlineTextViewGem.setOutlineTextAligh(OutlineTextView.ALIGN_RIGHT);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getApp().getJniBridge().setOnGLJniBridgeListener(this);
        onChangedGem(JniBridge.nativeGetGem());
    }

    @Override
    public void onPause() {
        getApp().getJniBridge().setOnGLJniBridgeListener(null);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (listView != null) {
            listView.setAdapter(null);
            listView.setOnItemClickListener(null);
        }
        super.onDestroyView();
    }

    // ------------------------------
    // AdapterView.OnItemClickListener
    // ------------------------------
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isLocked()) {
            return;
        }
        lockEvent();

        ShopItem item = (ShopItem)parent.getItemAtPosition(position);
        if (item == null) {
            onShowDialog(DialogCode.ERROR, null);
            return;
        }

        onShowDialog(DialogCode.BuyGem, item);
    }

    // ------------------------------
    // OnGLJniBridgeListener
    // ------------------------------
    @Override
    public void onBrokenBroom(int broomType) {
    }

    @Override
    public void onBrokenGarbageCan() {
    }

    @Override
    public void onChangedGem(int gem) {
        if (outlineTextViewGem != null) {
            outlineTextViewGem.setText(NumberUtils.getNumberFormatText(JniBridge.nativeGetGem()));
        }
    }

    @Override
    public void onClearMission(int missionId) {

    }

    @Override
    public void onUndergroundGemGot(int gem) {

    }

    @Override
    public void onChangedPoint(int point) {
    }

    @Override
    public void onChangedFullness(double fullness) {
    }

    @Override
    public void onChangedLevel(int level) {
    }

    @Override
    public void onCompletedLoading() {
    }

    @Override
    public void onShowPoint(int point) {
    }

    @Override
    public void onShowGem(int gem) {
    }

    @Override
    public void onShowComboBonus(int comboCount, int point) {
    }

    @Override
    public void onShowSucceededSyntheses(GarbageId garbageId) {
        onShowDialog(DialogCode.SynthesisSuccess, new BookGarbageData(garbageId));
    }

    @Override
    public void onRequestSaveGame(int add_point, int put_in_garbage_count, String garbages, int broom_use_count, int broom_broken) {
//        onFragmentEvent(FragmentEventCode.PostGameSave, new GomipoiGameSaveParam(add_point, put_in_garbage_count, garbages, broom_use_count, broom_broken == 1));
    }

    @Override
    public void onRequestFoundGarbage(String idListText) {
//        String[] idList = idListText.split(",");
//        GomipoiGarbageFoundParam param = new GomipoiGarbageFoundParam(GomipoiGarbageFoundParam.Method.Cleaning);
//        for (int i = 0; i < idList.length; i++) {
//            param.addGarbage(new Garbage(GarbageId.valueOfWithCode(idList[i]), Garbage.FOUND_TYPE_CLEANING));
//        }
//        onFragmentEvent(FragmentEventCode.PostGarbageFound, param);
    }

    @Override
    public void onEnterUnderground(boolean enterJirokichi) {
    }

    @Override
    public void onChangeStage(int stageId) {
    }

    @Override
    public void onRemainingBonusTime(int remainingSecond) {

    }

    @Override
    public void onFinishSaveGame() {
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_shop);
    }

}

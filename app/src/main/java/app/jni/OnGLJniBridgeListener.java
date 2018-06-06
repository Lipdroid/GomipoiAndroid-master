package app.jni;

import app.define.GarbageId;
import lib.opengl.jni.OnGLJniBridgeListenerBase;

/**
 * JniBridgeのコールバックインターフェース
 */
public interface OnGLJniBridgeListener extends OnGLJniBridgeListenerBase {
    void onChangedGem(int gem);
    void onClearMission(int missionId);
    void onUndergroundGemGot(int gem);
    void onChangedPoint(int point);
    void onChangedFullness(double fullness);
    void onChangedLevel(int level);
    void onBrokenBroom(int broomType);
    void onBrokenGarbageCan();
    void onShowPoint(int point);
    void onShowGem(int gem);
    void onShowComboBonus(int comboCount, int point);
    void onShowSucceededSyntheses(GarbageId garbageId);
    void onRequestSaveGame(int add_point, int put_in_garbage_count, String garbages, int broom_use_count, int broom_broken);
    void onFinishSaveGame();
    void onRequestFoundGarbage(String idListText);
    void onEnterUnderground(boolean enterJirokichi);
    void onChangeStage(int stageId);
    void onRemainingBonusTime(int remainingSecond);
}

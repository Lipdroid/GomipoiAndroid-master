package app.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.define.GarbageId;
import app.jni.JniBridge;

public class BookGarbageData implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    private GarbageId garbageId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public BookGarbageData(GarbageId garbageId) {
        this.garbageId = garbageId;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * リストでNEWが必要かどうか
     */
    public boolean isNew() {
        return garbageId.isNew();
    }

    /**
     * 画像がアンロックされているかどうか
     */
    public boolean isLocked() {
        return garbageId.isLocked();
    }

    /**
     * 画像が合成でできたレアゴミかどうか
     */
    public boolean isRare() {
        return garbageId.isRare();
    }

    /**
     * Codeを取得
     */
    public String getCode() {
        return garbageId.getCode();
    }

    /**
     * GarbageIdを取得
     */
    public GarbageId getGarbageId() {
        return garbageId;
    }

    /**
     * BookGarbageDataのリストを取得する
     */
    public static List<BookGarbageData> getList() {
        List<BookGarbageData> garbageList = new ArrayList<>();
        int[] bookGarbages = JniBridge.nativeGetBookGarbages();
        for (int i = 0; i < bookGarbages.length; i++) {
            int garbageId = bookGarbages[i];
            BookGarbageData data = new BookGarbageData(GarbageId.valueOf(garbageId));
            garbageList.add(data);
        }
        return garbageList;
    }

    public static List<BookGarbageData> getSpList() {
        List<BookGarbageData> garbageList = new ArrayList<>();
        garbageList.add(new BookGarbageData(GarbageId.Garbage127));
        garbageList.add(new BookGarbageData(GarbageId.Garbage128));
        garbageList.add(new BookGarbageData(GarbageId.Garbage129));
        garbageList.add(new BookGarbageData(GarbageId.Garbage130));
        garbageList.add(new BookGarbageData(GarbageId.Garbage131));
        garbageList.add(new BookGarbageData(GarbageId.Garbage132));
        garbageList.add(new BookGarbageData(GarbageId.Garbage133));
        garbageList.add(new BookGarbageData(GarbageId.Garbage134));
        garbageList.add(new BookGarbageData(GarbageId.Garbage135));
        return garbageList;
    }

    public boolean isSp() {
        return getGarbageId().equals(GarbageId.Garbage127)
                || getGarbageId().equals(GarbageId.Garbage128)
                || getGarbageId().equals(GarbageId.Garbage129)
                || getGarbageId().equals(GarbageId.Garbage130)
                || getGarbageId().equals(GarbageId.Garbage131)
                || getGarbageId().equals(GarbageId.Garbage132)
                || getGarbageId().equals(GarbageId.Garbage133)
                || getGarbageId().equals(GarbageId.Garbage134)
                || getGarbageId().equals(GarbageId.Garbage135)
                ;
    }
}

package common.adapter;

import android.content.Context;

import java.util.List;

import app.application.GBApplication;
import lib.adapter.AdapterBase;
import lib.adapter.OnAdapterListener;

/**
 */
public class GBAdapterBase<T> extends AdapterBase<T> {

    // ------------------------------
    // Constructor
    // ------------------------------
    public GBAdapterBase(Context context, List<T> objects, OnAdapterListener listener) {
        super(context, objects, listener);
    }

    // ------------------------------
    // Function
    // ------------------------------
    protected GBApplication getApp() {
        if (getContext() == null) {
            return null;
        }
        return (GBApplication)getContext();
    }

}

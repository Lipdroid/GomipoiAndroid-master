package app.wheel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import app.application.GBApplication;
import app.wheel.lib.NumericWheelAdapter;

/**
 */
public class WheelAdapter extends NumericWheelAdapter {

    // ------------------------------
    // Define
    // ------------------------------
    private static final int MAX = 9;
    private static final int MIN = 0;
    private final Context context;

    // ------------------------------
    // Member
    // ------------------------------
    private int mDigit;

    // ------------------------------
    // Constructor
    // ------------------------------
    public WheelAdapter(Context context, int digit) {
        super(context, MIN, MAX);
        this.context = context;
        this.mDigit = digit;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.adapter_point_wheel, null);

            holder = new ViewHolder();
            holder.imageViewNumber = (ImageView) convertView.findViewById(R.id.imageViewNumber);

            convertView.setTag(holder);

            ((GBApplication)context).getResizeManager().resize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder == null) {
            return convertView;
        }

        if (holder.imageViewNumber != null) {
            holder.imageViewNumber.setImageLevel(Integer.parseInt(getItemText(index).toString()));
        }

        return convertView;
    }

    @Override
    public CharSequence getItemText(int index) {
        return super.getItemText(index);
    }

    // ------------------------------
    // InnerClass
    // ------------------------------
    private static class ViewHolder {

        public ImageView imageViewNumber;

    }
}

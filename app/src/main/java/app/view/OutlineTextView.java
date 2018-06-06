package app.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lib.convert.UnitUtils;

/**
 *
 */
public class OutlineTextView extends TextView {

    // ------------------------------
    // Define
    // ------------------------------
    protected final Paint dOutlinePaint = new Paint();
    protected final Paint dTextPaint = new Paint();

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;

    // ------------------------------
    // Member
    // ------------------------------
    protected int mOutlineColor = Color.BLACK;
    protected int mOutlineWidth = 8;
    protected int mAlign = ALIGN_LEFT;

    // ------------------------------
    // Constructor
    // ------------------------------
    public OutlineTextView(Context context) {
        super(context);
        mOutlineWidth = floor(UnitUtils.getPxFromDp(context, 5));
        this.requestLayout();
        this.invalidate();
    }

    public OutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOutlineWidth = floor(UnitUtils.getPxFromDp(context, 5));
        this.requestLayout();
        this.invalidate();
    }

    public OutlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOutlineWidth = floor(UnitUtils.getPxFromDp(context, 5));
        this.requestLayout();
        this.invalidate();
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        this.requestLayout();
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getText() == null || getText().length() == 0) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        dOutlinePaint.setAntiAlias(true);
        dOutlinePaint.setColor(mOutlineColor);
        dOutlinePaint.setStyle(Paint.Style.STROKE);
        dOutlinePaint.setStrokeWidth(mOutlineWidth);
        dOutlinePaint.setTextSize(getTextSize());

        dTextPaint.setAntiAlias(true);
        dTextPaint.setColor(getCurrentTextColor());
        dTextPaint.setStyle(Paint.Style.FILL);
        dTextPaint.setTextSize(getTextSize());

        int maxLine = 1;
        List<String> lineText = separateLineText(getText().toString(), maxLine, viewWidth);

        double maxWidth = 0;
        for (int i = 0; i < lineText.size(); i++) {
            double lineWidth = getTextWidth(lineText.get(i));
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
        }

        for (int i = 0; i < lineText.size(); i++) {
            int line = i + 1;

            double offsetX = getDrawLeft();
            double lineWidth = getTextWidth(lineText.get(i));

            if (mAlign == ALIGN_CENTER) {
                offsetX = (((double)getWidth() - maxWidth) + (maxWidth - lineWidth)) / 2.0;
            } else if (mAlign == ALIGN_RIGHT) {
                offsetX = ((double)getWidth() - getDrawLeft() - maxWidth) - (maxWidth - lineWidth);
            }

            //ふちの部分を描画
            canvas.drawText(lineText.get(i), floor(offsetX), getDrawTop(lineText.size(), line), dOutlinePaint);

            //実際のテキストを描画
            canvas.drawText(lineText.get(i), floor(offsetX), getDrawTop(lineText.size(), line), dTextPaint);

        }
        lineText.clear();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final void setOutlineTextColor(int color) {
        mOutlineColor = color;
    }

    public final void setOutlineTextWidth(int width) {
        mOutlineWidth = width;
    }

    public final void setOutlineTextAligh(int align) {
        mAlign = align;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * 描画文字列を各行分ごとに分割する
     */
    protected List<String> separateLineText(String text, int maxLine, int maxWidth) {
        List<String> result = new ArrayList<>();

        if (getTextWidth(text) <= maxWidth) {
            // 1行で収まれば、
            result.add(text);
            return result;
        }

        int startIndex = 0;
        int endIndex = 0;
        String addText = "";
        for (int i = 0; i < maxLine; i++) {
            if (i == maxLine - 1) {
                addText = "…";
            }

            while (true) {
                endIndex += 1;
                if (endIndex >= text.length() + 1) {
                    result.add(text.substring(startIndex, text.length()));
                    startIndex = endIndex - 1;
                    break;
                }

                if (mAlign == ALIGN_RIGHT && addText.length() > 0) {
                    if (getTextWidth(text.substring(startIndex, endIndex) + addText) > maxWidth - getDrawLeft()) {
                        result.add(addText + text.substring(text.length() - (endIndex - 1 - startIndex), text.length()));
                        startIndex = endIndex - 1;
                        break;
                    }
                } else {
                    if (getTextWidth(text.substring(startIndex, endIndex) + addText) > maxWidth - getDrawLeft()) {
                        result.add(text.substring(startIndex, endIndex - 1) + addText);
                        startIndex = endIndex - 1;
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 文字列描画のOffset(Left)を返す
     * ※ これを設定しないとOutlineが切れる。。。
     */
    protected float getDrawLeft() {
        return (float)mOutlineWidth / 2.0f;
    }

    /**
     * 文字列描画のOffset(Top)を返す
     */
    protected float getDrawTop(int maxLine, int line) {
        float textSize = getTextSize();
//        float margin = (getHeight() - maxLine * textSize) / (maxLine + 1);
        float margin = 0;
        return (line * margin + (line - 1) * textSize) + textSize;
    }

    /**
     * 文字列の幅を返す
     */
    protected int getTextWidth(String text) {
        return (int)dTextPaint.measureText(text);
    }

    /**
     * 小数の切り上げ処理
     */
    protected int floor(double value) {
        return value > 0 ? (int) value : (int) (value - 0.99999999999999D);
    }
}

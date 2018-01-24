package com.uninew.car.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.uninew.car.R;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class LineDividerTextView extends TextView {
    public static final String TAG = LineDividerTextView.class.getSimpleName();
    private Rect mRect;

    private Drawable lineDivider;
    private int lineDividerHeight;

    public LineDividerTextView(Context context) {
        this(context, null);
    }

    public LineDividerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineDividerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineDividerTextView, defStyleAttr, 0);
        lineDivider = array.getDrawable(R.styleable.LineDividerTextView_line_divider);
        lineDividerHeight = array.getDimensionPixelSize(R.styleable.LineDividerTextView_line_divider_height, 0);
        array.recycle();
        init();
    }

    private void init() {
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = getLineCount();

        for (int i = 0; i < count; i++) {
            // last line not draw
            if (i == count - 1) {
                break;
            }
            getLineBounds(i, mRect);

            lineDivider.setBounds(
                    mRect.left,
                    (int) (mRect.bottom - getLineSpacingExtra() / 2 - lineDividerHeight / 2),
                    mRect.right,
                    (int) (mRect.bottom - getLineSpacingExtra() / 2 + lineDividerHeight / 2));
            lineDivider.draw(canvas);
        }

    }

    /**
     *
     * @return 算出最后一行多出的行间距的高
     */
    public int calculateExtraSpace() {
        int result = 0;
        int lastLineIndex = getLineCount() - 1;

        if (lastLineIndex >= 0) {
            Layout layout = getLayout();
            int baseline = getLineBounds(lastLineIndex, mRect);
            if (getMeasuredHeight() == getLayout().getHeight()) {
                result = mRect.bottom - (baseline + layout.getPaint().getFontMetricsInt().descent);
            }

        }
        Log.i(TAG, "extra space:" + result);
        return result;
    }
}

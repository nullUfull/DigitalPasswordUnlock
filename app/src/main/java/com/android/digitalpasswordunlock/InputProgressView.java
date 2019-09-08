package com.android.digitalpasswordunlock;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class InputProgressView extends LinearLayout {
    private int mMaxProgress = 4;
    private int mCurProgress = 0;

    public InputProgressView(Context context) {
        this(context, null);
    }

    public InputProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        for (int i = 0; i < mMaxProgress; i++) {
            DigitView digitView = new DigitView(context);
            digitView.setRadius(16);
            digitView.setEnabled(false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i<mMaxProgress-1) {
                layoutParams.rightMargin = 48;
            }
            addView(digitView,layoutParams);
        }
    }

    public void advance() {
        mCurProgress++;
        if (mCurProgress > mMaxProgress) {
            mCurProgress = 1;
        }
        for (int i = 0; i < mCurProgress; i++) {
            DigitView digitView = (DigitView) getChildAt(i);
            if (digitView != null) {
                digitView.setSelected(true);
            }
        }
    }

    public void clear() {
        mCurProgress = 0;
        for (int i = 0; i < mMaxProgress; i++) {
            DigitView digitView = (DigitView) getChildAt(i);
            if (digitView != null) {
                digitView.setSelected(false);
            }
        }
    }
}

package com.android.digitalpasswordunlock;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

public class DigitView extends android.support.v7.widget.AppCompatTextView {

    private int mRadius = 90;
    private int mFontSize = 18;
    private GradientDrawable mSelectDrawable;
    private GradientDrawable mUnSelectDrawable;

    public DigitView(Context context) {
        this(context, null);
    }

    public DigitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        setWidth(radius*2);
        setHeight(radius*2);
    }

    public void setFontSize(int size) {
        this.mFontSize = size;
        setTextSize(size);
    }

    private void initView() {
        setClickable(true);
        mUnSelectDrawable = getBackground(Color.argb(40, 0, 0, 0));
        mSelectDrawable = getBackground(Color.argb(70, 0, 0, 0));
        setBackground(mUnSelectDrawable);
        int width = getWidth();
        int height = getHeight();
        if (width != 0 && height != 0) {
            if (width < height) {
                mRadius = width/2;
                setHeight(width);
            } else {
                mRadius = height/2;
                setWidth(height);
            }
        }else {
            setWidth(mRadius*2);
            setHeight(mRadius*2);
        }
        setTextSize(mFontSize);
        setGravity(Gravity.CENTER);
    }

    private GradientDrawable getBackground(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(mRadius);
        return gradientDrawable;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (!isEnabled() || !isClickable()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackground(mUnSelectDrawable);
                break;
            case MotionEvent.ACTION_DOWN:
                setBackground(mSelectDrawable);
                break;
        }
        return true;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setBackground(selected?mSelectDrawable:mUnSelectDrawable);
    }
}

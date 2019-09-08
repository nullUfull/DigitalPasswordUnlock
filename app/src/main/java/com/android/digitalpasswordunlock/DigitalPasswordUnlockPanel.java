package com.android.digitalpasswordunlock;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.widget.GridLayout.spec;

public class DigitalPasswordUnlockPanel extends RelativeLayout {

    //数字字体大小
    private int mFontSize;
    private int mTextColor;

    private StringBuilder mInputPassword;
    private String mPassword;
    private int mPasswordLength = 4;

    private TextView mTips;
    private ImageView mDeleteIcon;
    private InputProgressView mInputProgressView;
    private OnVerifyListener onVerifyListener;

    public void setOnVerifyListener(OnVerifyListener onVerifyListener) {
        this.onVerifyListener = onVerifyListener;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public DigitalPasswordUnlockPanel(Context context) {
        this(context, null);
    }

    public DigitalPasswordUnlockPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalPasswordUnlockPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInputPassword = new StringBuilder();
        initView(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(final Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DigitalPasswordUnlockPanel);
        mTextColor = typedArray.getColor(R.styleable.DigitalPasswordUnlockPanel_itemTextColor,Color.argb(180, 255, 255, 255));
        mFontSize = typedArray.getInteger(R.styleable.DigitalPasswordUnlockPanel_itemTextSize, 32);

        typedArray.recycle();
        //提示
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTips = new TextView(context);
        mTips.setText("请输入密码");
        mTips.setTextSize(20);
        mTips.setTextColor(Color.BLACK);
        mTips.setId(View.generateViewId());
        layoutParams.addRule(CENTER_HORIZONTAL);
        addView(mTips, layoutParams);
        //顶部密码输入显示
        LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(BELOW, mTips.getId());
        layoutParams1.addRule(CENTER_HORIZONTAL);
        layoutParams1.topMargin = 48;
        layoutParams1.bottomMargin = 48;
        mInputProgressView = new InputProgressView(context);
        mInputProgressView.setId(View.generateViewId());
        addView(mInputProgressView, layoutParams1);
        //删除icon
        mDeleteIcon = new ImageView(context);
        mDeleteIcon.setImageResource(R.drawable.ic_close);
        mDeleteIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(48, 48);
        layoutParams2.addRule(END_OF, mInputProgressView.getId());
        layoutParams2.addRule(BELOW, mTips.getId());
        layoutParams2.addRule(ALIGN_BASELINE, mInputProgressView.getId());
        layoutParams2.leftMargin = 32;
        layoutParams2.topMargin = 40;
        mDeleteIcon.setVisibility(INVISIBLE);
        addView(mDeleteIcon, layoutParams2);
        //数字键盘
        GridLayout numContainer = new GridLayout(context);
        numContainer.setColumnCount(3);
        numContainer.setRowCount(4);
        for (int i = 1; i <= 12; i++) {
            final DigitView tvNum = new DigitView(context);
            tvNum.setText(String.valueOf(i));
            tvNum.setTextSize(mFontSize);
            tvNum.setTextColor(mTextColor);
            tvNum.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteIcon.setVisibility(VISIBLE);
                    mInputProgressView.advance();
                    mInputPassword.append(tvNum.getText());
                    //验证密码
                    if (mInputPassword.toString().length() >= mPasswordLength) {
                        verifyPassword();
                    }
                }
            });
            GridLayout.LayoutParams itemLayoutParams = new GridLayout.LayoutParams();
            itemLayoutParams.columnSpec = spec((i - 1) % 3, numContainer.getAlignmentMode(), 1);
            itemLayoutParams.rowSpec = spec((i - 1) / 3, numContainer.getAlignmentMode(), 1);
            itemLayoutParams.setGravity(Gravity.CENTER);
            tvNum.setLayoutParams(itemLayoutParams);
            //10:左下 12：右下
            if (i >= 10) {
                if (i == 11) {
                    tvNum.setText("0");
                } else {
                    tvNum.setVisibility(INVISIBLE);
                }
            }
            numContainer.addView(tvNum);
        }
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gridParams.addRule(BELOW, mInputProgressView.getId());
        addView(numContainer, gridParams);
    }

    private void verifyPassword() {
        if (TextUtils.isEmpty(mInputPassword.toString())) {
            return;
        }
        //MD5加密
        String password = MD5Utils.stringToMD5(mInputPassword.toString());
        if (!TextUtils.isEmpty(mPassword) && mPassword.equals(password)) {
            if (onVerifyListener != null) {
                mTips.setText("密码正确");
                onVerifyListener.onSucceed();
            }
        } else {
            if (onVerifyListener != null) {
                onVerifyListener.onFailed();
                mTips.setText("密码错误");
                startVerifyFailedAnimation();
            }
        }
        clear();
    }

    private void startVerifyFailedAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTips, "translationX", 0, -10)
                .setDuration(50);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mTips, "translationX", 0, 10)
                .setDuration(50);
        animatorSet.playSequentially(objectAnimator, objectAnimator1);
        animatorSet.start();
    }

    private void clear() {
        mInputPassword = new StringBuilder();
        mInputProgressView.clear();
        mDeleteIcon.setVisibility(INVISIBLE);
    }

    public interface OnVerifyListener {
        void onSucceed();

        void onFailed();
    }
}

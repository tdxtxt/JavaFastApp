package com.baselib.ui.weight.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;


/**
 * @作者： tangdx
 * @创建时间： 2018\5\21 0021
 * @功能描述： 倒计时功能的自定义按钮-使用与短信验证码倒计时
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
@SuppressLint("AppCompatCustomView")
public class CountDownButton extends Button {
    /**
     * 默认时间间隔1000ms
     */
    private static final long DEFAULT_INTERVAL = 1000;
    /**
     * 默认时长60s
     */
    private static final long DEFAULT_COUNT = 60 * 1000;
    /**
     * 默认倒计时文字格式(显示秒数)
     */
    private static final String DEFAULT_COUNT_FORMAT = "%d";
    /**
     * 倒计时结束后按钮显示的文本
     */
    private String mCdFinishText;
    /**
     * 倒计时时长，单位为毫秒
     */
    private long mCount;
    /**
     * 时间间隔，单位为毫秒
     */
    private long mInterval;
    /**
     * 全局可用,默认全局不可用
     */
    private boolean mGlobalEnable;
    /**
     * 倒计时文字格式
     */
    private String mCountDownFormat = DEFAULT_COUNT_FORMAT;
    /**
     * 倒计时是否可用
     */
    private boolean mEnableCountDown = true;
    /**
     * 点击事件监听器
     */
    private OnClickListener onClickListener;

    /**
     * 倒计时
     */
    private CountDownTimer mCountDownTimer;

    /**
     * 是否正在执行倒计时
     */
    private boolean isCountDownNow;

    private CheckListener mCheckListener;

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        // 获取自定义属性值
        setAllCaps(false);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownButton);
        mCountDownFormat = "%ds重新获取验证码";//typedArray.getString(R.styleable.CountDownButton_countDownFormat);
        mCdFinishText = "获取验证码";
//        if (typedArray.hasValue(R.styleable.CountDownButton_countDown)) {
//            mCount = (int) typedArray.getFloat(R.styleable.CountDownButton_countDown, DEFAULT_COUNT);
//        }
        mCount = DEFAULT_COUNT;
        mInterval = DEFAULT_INTERVAL;//(int) typedArray.getFloat(R.styleable.CountDownButton_countDownInterval, DEFAULT_INTERVAL);
        mEnableCountDown = (mCount > mInterval)/* && typedArray.getBoolean(R.styleable.CountDownButton_enableCountDown, true)*/;
//        typedArray.recycle();
        // 初始化倒计时Timer
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(mCount, mInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setText(String.format(mCountDownFormat, millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    isCountDownNow = false;
                    setEnabled(true);
                    setText(TextUtils.isEmpty(mCdFinishText) ? getText().toString() : mCdFinishText);
                }
            };
        }
//        setBackgroundResource(R.mipmap.countdown_botton_enable);
    }


    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
//        super.setOnClickListener(onClickListener);
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if(mCheckListener != null && !isCountDownNow()){
                    if(!mCheckListener.doCheck()){
                        return super.onTouchEvent(event);
                    }
                }
//                Rect rect = new Rect();
//                this.getGlobalVisibleRect(rect);
                if (onClickListener != null/* && rect.contains((int) event.getRawX(), (int) event.getRawY())*/) {
                    onClickListener.onClick(this);
                }
                if (mEnableCountDown/* && rect.contains((int) event.getRawX(), (int) event.getRawY())*/) {
                    // 设置按钮不可点击
                    setEnabled(false);
                    // 开始倒计时
                    mCountDownTimer.start();
                    isCountDownNow = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setEnableCountDown(boolean enableCountDown) {
        this.mEnableCountDown = (mCount > mInterval) && enableCountDown;
    }

    public void setCountDownFormat(String countDownFormat) {
        this.mCountDownFormat = countDownFormat;
    }

    public void setCount(long count) {
        this.mCount = count;
    }

    public void setInterval(long interval) {
        mInterval = interval;
    }

    public void setGlobalEnable(boolean globalEnable){
        mGlobalEnable = globalEnable;
    }
    /**
     * 是否正在执行倒计时
     *
     * @return 倒计时期间返回true否则返回false
     */
    public boolean isCountDownNow() {
        return isCountDownNow;
    }

    /**
     * 设置倒计时数据
     *
     * @param count           时长
     * @param interval        间隔
     * @param countDownFormat 文字格式
     */
    public void setCountDown(long count, long interval, String countDownFormat) {
        this.mCount = count;
        this.mCountDownFormat = countDownFormat;
        this.mInterval = interval;
        setEnableCountDown(true);
    }

    public void setCDFinishText(String cdFinishText) {
        this.mCdFinishText = cdFinishText;
    }

    /**
     * 移除倒计时
     */
    public void removeCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        isCountDownNow = false;
        setText(TextUtils.isEmpty(mCdFinishText) ? getText().toString() : mCdFinishText);
        setEnabled(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isCountDownNow()) {
            return;
        }
        super.setEnabled(enabled);
        setClickable(enabled);
        setTextColor(Color.parseColor(enabled ? "#2D97FA" : "#8f8f8f"));
//        setBackgroundResource(enabled ? R.drawable.fastlib_biground_rectangle_bule : R.drawable.fastlib_biground_rectangle_gray);
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCountDown();
        super.onDetachedFromWindow();
    }

    public void setCheckListener(CheckListener checkListener) {
        this.mCheckListener = checkListener;
    }

    public interface CheckListener{
        //用于校验本控件是否可用，在点击时候触发，false表示不进入点击事件倒计时
        boolean doCheck();//返回false表示不进入点击事件
    }
}

package com.baselib.ui.weight.edit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.baselib.R;
import com.baselib.helper.DrawableHelper;

import java.lang.reflect.Field;

public class DeleteEditText extends AppCompatEditText {
    private Drawable mRightDrawable;
    //控件是否获得焦点标志位
    boolean mIsHasFocus;

    //构造方法1
    public DeleteEditText(Context context) {
        super(context);
        init(context);
    }

    //构造方法2
    public DeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //构造方法3
    public DeleteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //本方法获取控件上下左右四个方位插入的图片
        Drawable drawables[] = this.getCompoundDrawables();
        mRightDrawable = drawables[2];
        if(mRightDrawable == null) mRightDrawable = DrawableHelper.getDrawable(R.drawable.baselib_delete);
        //添加文本改变监听
        this.addTextChangedListener(new TextWatcherImpl());
        //添加触摸改变监听
        this.setOnFocusChangeListener(new OnFocusChangeImpl());
        //初始设置所有右边图片不可见
        setClearDrawableVisible(false);

        /**
         * 初始化光标（颜色 & 粗细）
         */
        // 原理：通过 反射机制 动态设置光标
        // 1. 获取资源ID
        try {

            // 2. 通过反射 获取光标属性
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            // 3. 传入资源ID
            f.set(this, R.drawable.baselib_cursor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class OnFocusChangeImpl implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            mIsHasFocus = hasFocus;
            if (mIsHasFocus) {
                //如果获取焦点并且判断输入内容不为空则显示删除图标
                boolean isNoNull = getText().toString().length() >= 1;
                setClearDrawableVisible(isNoNull);
            } else {
                //否则隐藏删除图标
                setClearDrawableVisible(false);
            }
        }
    }

    //本方法控制右边图片的显示与否
    private void setClearDrawableVisible(boolean isNoNull) {
        Drawable rightDrawable;
        if (isNoNull) {
            rightDrawable = mRightDrawable;
        } else {
            rightDrawable = null;
        }
        // 使用代码设置该控件left, top, right, and bottom处的图标
        setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0],
                getCompoundDrawables()[1], rightDrawable,
                getCompoundDrawables()[3]);
    }

    private class TextWatcherImpl implements TextWatcher {
        //内容输入后
        @Override
        public void afterTextChanged(Editable s) {
            boolean isNoNull = getText().toString().length() >= 1;
            setClearDrawableVisible(isNoNull);
        }

        //内容输入前
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        //内容输入中
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //抬手指事件
            case MotionEvent.ACTION_UP:
                //删除图片右侧到EditText控件最左侧距离
                int length1 = getWidth() - getPaddingRight();
                //删除图片左侧到EditText控件最左侧距离
                int length2 = getWidth() - getTotalPaddingRight();
                //判断点击位置是否在图片上
                boolean isClean = (event.getX() > length2)
                        && (event.getX() < length1);
                if (isClean) {
                    setText("");
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}

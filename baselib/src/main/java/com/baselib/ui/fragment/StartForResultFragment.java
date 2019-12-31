package com.baselib.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.baselib.ui.activity.callback.StartForResultListener;

/**
 * 用于startActivityForResult
 */
public class StartForResultFragment extends Fragment {
    private StartForResultListener mListener;

    public void setListener(StartForResultListener listener){
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//避免旋转时fragment销毁重建
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            if(mListener != null) mListener.onCancel();
        }else{
            if(mListener != null) mListener.onActivityResult(requestCode,resultCode,data);
        }
        mListener = null;
    }
}

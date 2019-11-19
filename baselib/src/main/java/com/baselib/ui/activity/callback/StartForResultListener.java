package com.baselib.ui.activity.callback;

import android.content.Intent;

public abstract class StartForResultListener {
    /**
     * StartActivityForResult回调，同{@link android.app.Activity#onActivityResult(int, int, Intent)}
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);
    public void onCancel(){}
}

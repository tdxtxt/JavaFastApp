package com.baselib.ui.dialog.callback;

import android.app.Dialog;
import android.view.View;

public class MenuDialogCallBack {
    public String menuText;
    public MenuDialogCallBack(String menuText){
        this.menuText = menuText;
    }
    public void onClick(View customView, Dialog dialog) {}
}

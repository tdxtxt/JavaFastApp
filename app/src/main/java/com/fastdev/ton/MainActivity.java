package com.fastdev.ton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.baselib.helper.ImageLoadHelper;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.iv_test)
    ImageView ivTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTest = findViewById(R.id.iv_test);
        ImageLoadHelper.displayImage(ivTest,"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1415088147,1132690104&fm=26&gp=0.jpg");
    }
}

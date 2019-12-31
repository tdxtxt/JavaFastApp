package com.fastdev.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselib.helper.DeviceHelper;
import com.baselib.helper.FileHelper;
import com.baselib.helper.FragmentHelper;
import com.baselib.helper.HashMapParams;
import com.baselib.helper.ImageLoadHelper;
import com.baselib.helper.ToastHelper;
import com.baselib.ui.activity.BaseActivity;
import com.baselib.ui.activity.CommToolBarActivity;
import com.baselib.ui.activity.callback.StartForResultListener;
import com.baselib.ui.fragment.BaseFragment;
import com.fastdev.helper.compress.CompressHelper;
import com.fastdev.ton.R;
import com.fastdev.ui.ForResultFragment;
import com.fastdev.ui.weight.UploadPhotoView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends CommToolBarActivity {

    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;

    @BindView(R.id.uploadview)
    UploadPhotoView uploadPhotoView;
    String filePath;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUi() {
        getTitleBar().setStatusBarColor(R.color.color_orange);
        initPermission();
        uploadPhotoView.setRequestCode(100,101).setChangListener(new UploadPhotoView.ChangListener() {
            @Override
            public void changPic(String path) {
                filePath = path;
                tvContent1.setText("源文件大小:" + FileHelper.getFileSize(path));
                tvContent2.setText("压缩之后文件大小:");
            }
        });
        initPermission();
    }

    @OnClick(R.id.btn_show)
    public void clickView(View view){
        switch (view.getId()){
            case R.id.btn_show:
//                UpdateBuilder.create().check();
//                ImageLoadHelper.displayImage(ivTest,"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1415088147,1132690104&fm=26&gp=0.jpg");
//                startActivityForResult(ForResultActivity.class, new HashMapParams().add("pamas", "3333"), new StartForResultListener() {
//                    @Override
//                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//                        ToastHelper.showToast(data.getStringExtra("tex") == null ? "没有获取到返回结果" :
//                                data.getStringExtra("tex"));
//                    }
//                });
                File newFile = new CompressHelper.Builder(this)
                        .setQuality(95)    //默认压缩质量为80
                        .setFileName("storeHeaderCopy") // 文件名称
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                        .setDestinationDirectoryPath(FileHelper.getImageDir(this).getAbsolutePath())//路径
                        .build()
                        .compressToFile(new File(filePath));
                tvContent2.setText("压缩之后文件大小:" + FileHelper.getFileSize(newFile));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadPhotoView.onActivityResult(requestCode,resultCode,data);
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CAMERA
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

        if(!toApplyList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //有读写权限
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        switch (requestCode){
            case 123:
                for (int i = 0; i < permissions.length; i++){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        if(permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE){
                            //授权请求被允许
//                            ToastHelper.showToast("读写权限授权允许");
                        }else{
                            //授权请求被拒绝
//                            ToastHelper.showToast("读写权限授权拒绝，请自行到设置中打开。");
                        }

                        if(permissions[i] == Manifest.permission.READ_PHONE_STATE){
                            //授权请求被允许
//                            ToastHelper.showToast("电话权限授权允许");
                        }else{
                            //授权请求被拒绝
//                            ToastHelper.showToast("电话权限授权拒绝");
                        }

                        if(permissions[i] == Manifest.permission.CAMERA){
                            //授权请求被允许
//                            ToastHelper.showToast("相机权限授权允许");
                        }else{
                            //授权请求被拒绝
//                            ToastHelper.showToast("相机权限授权拒绝");
                        }
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}

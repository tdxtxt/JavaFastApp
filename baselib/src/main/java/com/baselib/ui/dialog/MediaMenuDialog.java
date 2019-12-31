package com.baselib.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.baselib.R;
import com.baselib.helper.FileHelper;
import com.baselib.helper.ToastHelper;
import com.baselib.ui.dialog.popup.BasePopup;
import java.io.File;

public class MediaMenuDialog extends BasePopup<MediaMenuDialog> {
    public int ALBUM_REQUEST_CODE = 0x123;
    public int CAMERA_REQUEST_CODE = 0x122;
    public int VIDEO_REQUEST_CODE  = 0x121;

    private View btnAlbum;
    private View btnCamera;
    private View btnVideo;

    private Activity context;
    public MediaMenuDialog(Activity context){
        this.context = context;
        setContext(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.baselib_popup_media_menu, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.AnimBottom).setFocusAndOutsideEnable(true).setBackgroundDimEnable(true).setDimValue(0.5f);
    }

    @Override
    protected void initViews(View view, MediaMenuDialog popup) {
        clickView(view.findViewById(R.id.btn_cancel));
        clickView(btnAlbum = view.findViewById(R.id.btn_album));
        clickView(btnCamera = view.findViewById(R.id.btn_camera));
        clickView(btnVideo = view.findViewById(R.id.btn_video));
    }

    private void clickView(View view){
        if(view == null) return;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId();
                if (id == R.id.btn_cancel) {
                    dismiss();
                } else if (id == R.id.btn_album) {
                    openSystemAlbum(ALBUM_REQUEST_CODE);
                    dismiss();
                } else if (id == R.id.btn_camera) {
                    openSystemCamera(CAMERA_REQUEST_CODE);
                    dismiss();
                } else if (id == R.id.btn_video) {
                    openSystemVideo(VIDEO_REQUEST_CODE);
                    dismiss();
                }
            }
        });
    }

    private void openSystemAlbum(int requestCode){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intentToPickPic, requestCode);
    }
    private void openSystemCamera(int requestCode){
        if(isCameraCanUse()){
            Intent takephoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takephoto.putExtra("return-data", false);
            takephoto.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(getCameraOutPath()));
            takephoto.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            takephoto.putExtra("noFaceDetection", true);
            context.startActivityForResult(takephoto, requestCode);
        }else{
            ToastHelper.showToast("相机权限已被禁用，请在三峡付应用管理中启用相机权限。");
        }
    }

    private File getCameraOutPath() {
        File file = new File(FileHelper.getImageDir(context), System.currentTimeMillis() + ".jpg");
        if(outPutListener != null) outPutListener.changeFile(file.getAbsolutePath());
        return file;
    }
    private void openSystemVideo(int requestCode){
        if (isCameraCanUse()) {

        }else {
            ToastHelper.showToast("相机权限已被禁用，请在三峡付应用管理中启用相机权限。");
        }
    }

    public boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    public void showPhoto(){
        showAtLocation(context.getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        if(btnAlbum != null) btnAlbum.setVisibility(View.VISIBLE);
        if(btnCamera != null) btnCamera.setVisibility(View.VISIBLE);
        if(btnVideo != null) btnVideo.setVisibility(View.GONE);
    }

    public MediaMenuDialog setRequestCode(int albumCode,int cameraCode){
        this.ALBUM_REQUEST_CODE = albumCode;
        this.CAMERA_REQUEST_CODE = cameraCode;
        return this;
    }

    private OutPutListener outPutListener;
    public MediaMenuDialog setOutPutListener(OutPutListener outPutListener){
        this.outPutListener = outPutListener;
        return this;
    }
    public interface OutPutListener{
        void changeFile(String path);
    }
}

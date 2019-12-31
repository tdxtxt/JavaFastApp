package com.fastdev.ui.weight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.baselib.helper.ImageLoadHelper;
import com.fastdev.ton.R;
import com.fastdev.ui.dialog.MediaMenuDialog;

public class UploadPhotoView extends FrameLayout {
    Activity context;
    String filePath;
    String remoteUrl;

    ImageView ivPic;
    View viewAfter;
    TextView tvBtnAddphoto;

    public int ALBUM_REQUEST_CODE = 0x123;
    public int CAMERA_REQUEST_CODE = 0x122;

    public UploadPhotoView(Context context) {
        super(context);
        initView(context);
    }

    public UploadPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public UploadPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        View root = View.inflate(context, R.layout.include_phone_upload_view,null);
        if (!isInEditMode()) {
            this.context = (Activity) context;
            ivPic = root.findViewById(R.id.iv_pic);
            tvBtnAddphoto = root.findViewById(R.id.tvBtn_addphoto);
            viewAfter = root.findViewById(R.id.view_after);
            clickView(root.findViewById(R.id.tvBtn_reupload));
            clickView(root.findViewById(R.id.tvBtn_preview));
            clickView(root.findViewById(R.id.tvBtn_addphoto));
        }
        addView(root);
    }

    private void clickView(View view){
        if(view == null) return;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tvBtn_addphoto:
                        new MediaMenuDialog(context).setOutPutListener(new MediaMenuDialog.OutPutListener() {
                            @Override
                            public void changeFile(String path) {
                                filePath = path;
                            }
                        }).setRequestCode(ALBUM_REQUEST_CODE,CAMERA_REQUEST_CODE).showPhoto();
                        break;
                    case R.id.tvBtn_reupload:
                        new MediaMenuDialog(context).setOutPutListener(new MediaMenuDialog.OutPutListener() {
                            @Override
                            public void changeFile(String path) {
                                filePath = path;
                            }
                        }).setRequestCode(ALBUM_REQUEST_CODE,CAMERA_REQUEST_CODE).showPhoto();
                        break;
                    case R.id.tvBtn_preview:
                        break;
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;
        if(requestCode == CAMERA_REQUEST_CODE){
            ImageLoadHelper.displayImage(ivPic,filePath);
            viewAfter.setVisibility(VISIBLE);

            if(listener != null) listener.changPic(filePath);
        }else if(requestCode == ALBUM_REQUEST_CODE){
            if(data .getData() == null) return;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
            ImageLoadHelper.displayImage(ivPic,filePath);
            viewAfter.setVisibility(VISIBLE);

            if(listener != null) listener.changPic(filePath);
        }
    }

    private ChangListener listener;
    public UploadPhotoView setChangListener(ChangListener listener) {
        this.listener = listener;
        return this;
    }
    public UploadPhotoView setRequestCode(int albumCode,int cameraCode){
        this.ALBUM_REQUEST_CODE = albumCode;
        this.CAMERA_REQUEST_CODE = cameraCode;
        return this;
    }

    public interface ChangListener{
         void changPic(String path);
    }
}

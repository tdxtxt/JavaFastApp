package com.fastdev.helper;

import android.content.Context;
import android.text.TextUtils;

import com.baselib.helper.ToastHelper;
import com.fastdev.bean.share.ShareEntity;
import com.fastdev.ton.R;
import com.mob.MobSDK;
import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @作者： ton
 * @创建时间： 2018\9\18 0018
 * @功能描述： 请参考： 【http://wiki.mob.com/完整集成文档（gradle）/】【http://wiki.mob.com/不同平台分享内容的详细说明/】
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ShareHelper {
    public static void initSDK(Context context){
        MobSDK.init(context);
    }

    /**
     * 仅分享图片
     */
    public static void share(Context context,String imagePath){
        share(context,new ShareEntity().setImagePath(imagePath));
    }
    public static void share(final Context context, final ShareEntity entity){
        if(context == null) return;
        if(entity == null) return;
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(entity.title);
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(entity.url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(entity.content);
        // setImageData分享drawable只有微信可以
//        oks.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("http://www.postalong.com/img/public/headlogo.png");//确保SDcard下面存在此张图片
        if(!TextUtils.isEmpty(entity.remoteImage)){
            oks.setImageUrl(entity.remoteImage);
        }else if(!TextUtils.isEmpty(entity.localImage)){
            oks.setImagePath(entity.localImage);
        }else{
            oks.setImageUrl("http://www.postalong.com/img/public/headlogo.png");
        }
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(entity.url);
        // 启动分享GUI
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams shareParams) {
                if (Twitter.NAME.equals(platform.getName())) {
                    //推特不支持分享链接的，想分享的话 只能写到setText里面
                    shareParams.setText(entity.toString());
                }else if(ShortMessage.NAME.equals(platform.getName())){
                    if(TextUtils.isEmpty(entity.localImage)){
                        //短信只需要setText，其他为null，否则就是彩信了
                        shareParams.setTitleUrl(null);
                        shareParams.setImageUrl(null);
                        shareParams.setImagePath(null);
                        shareParams.setComment(null);
                        shareParams.setText(entity.toString());
                    }
                }else if(Wechat.NAME.equals(platform.getName())){
                    shareParams.setTitle(entity.title);
                    shareParams.setText(entity.content);
                    shareParams.setUrl(entity.url);
                }
            }
        });
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastHelper.showToast(R.string.ssdk_oks_share_completed);
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastHelper.showToast(R.string.ssdk_oks_share_failed);
                throwable.printStackTrace();
            }
            @Override
            public void onCancel(Platform platform, int i) {
                ToastHelper.showToast(R.string.ssdk_oks_share_canceled);
            }
        });
        oks.show(context);
    }
    public static void share(Context context,String title,String content,String image,String url){
        share(context,new ShareEntity().setTitle(title).setContent(content).setImage(image).setUrl(url));
    }
}

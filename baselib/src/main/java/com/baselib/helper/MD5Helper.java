package com.baselib.helper;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * @作者： ton
 * @创建时间： 2018\5\22 0022
 * @功能描述： md5加密
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class MD5Helper {
    private static String oldDataMap = "1234567890abcdef";
    private static String newDataMap = "d0f59a6348b71c2e";
    private static String stringToMD5(String data){
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(data.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    public static String getNormalMd5(String data){
        return stringToMD5(data);
    }
    public static String getNewMd5(String data){
        StringBuffer sb = new StringBuffer();
        try {
            String md5 = stringToMD5(data);
            char[] md5_chars = md5.toCharArray();
            for (char c : md5_chars) {
                if(oldDataMap.contains(Character.toString(c))){
                    char newChar = newDataMap.charAt(oldDataMap.indexOf(Character.toString(c)));
                    sb.append(newChar);
                }else{
                    sb.append(c);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
   /* public static Flowable<String> getNormalMd5ByFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return Flowable.just("");
        }
        return Flowable.just(new File(path))
                .map(new Function<File, String>() {
                    @Override
                    public String apply(File file) {
                        if (file != null && file.exists()) {
                            FileInputStream fis = null;
                            try{
                                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                                fis = new FileInputStream(file);
                                MappedByteBuffer byteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                                messageDigest.update(byteBuffer);
                                BigInteger bigInt = new BigInteger(1, messageDigest.digest());
                                String md5 = bigInt.toString(16);
                                while(md5.length() < 32){
                                    md5 = "0" + md5;
                                }
                                return md5;
                            }catch (Exception e){e.printStackTrace();}finally {
                                if (fis != null) {
                                    try {
                                        fis.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        return "";
                    }
                });
    }*/
}

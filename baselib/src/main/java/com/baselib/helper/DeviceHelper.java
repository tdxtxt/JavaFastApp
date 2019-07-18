package com.baselib.helper;

import android.os.Build;
import android.text.TextUtils;

import java.util.UUID;

public class DeviceHelper {
    public static String DEVICE_ID;
    /**
     * 参考：https://github.com/giantray/stackoverflow-java-top-qa/blob/master/contents/is-there-a-unique-android-device-id.md
     * 1.不需要特定权限.
     * 2.在99.5% Android装置（包括root过的）上，即API => 9，保证唯一性.
     * 3.重装app之后仍能取得相同唯一值.
     */
    public static String getDeviceId(){
        if(!TextUtils.isEmpty(DEVICE_ID)) return DEVICE_ID;
        String m_szDevIDShort = "35" +
                (Build.BOARD.length() % 10) + //主板型号
                (Build.BRAND.length() % 10) + //产品品牌
                (Build.CPU_ABI.length() % 10) +
                (Build.DEVICE.length() % 10) + //设备型号
                (Build.MANUFACTURER.length() % 10) + //产品制造商
                (Build.MODEL.length() % 10) +
                (Build.PRODUCT.length() % 10); //产品型号
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();//硬件序列号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}

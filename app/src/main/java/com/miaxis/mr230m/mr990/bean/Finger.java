package com.miaxis.mr230m.mr990.bean;

import android.text.TextUtils;



public class Finger {

    public long id;
    /**
     * 用户ID
     */
    public String UserId;//用户ID
    public int Position;//手指位置
    public long fingerImageId;//指纹图片ID
    public byte[] FingerFeature;
    public long create_time;//创建时间
    public long update_time;//修改时间

    public Finger() {
        this.create_time = System.currentTimeMillis();
    }

    public boolean isIllegal() {
        return TextUtils.isEmpty(UserId) ||
                fingerImageId <= 0 ||
                Position < 0 || Position > 9
                || FingerFeature == null || FingerFeature.length <= 0;
    }

    public static boolean isIllegal(Finger finger) {
        return finger == null || finger.isIllegal();
    }

    @Override
    public String toString() {
        return "Finger{" +
                "id=" + id +
                ", UserId='" + UserId + '\'' +
                ", Position='" + Position + '\'' +
                ", fingerImageId='" + fingerImageId + '\'' +
                ", FingerFeature=" + (FingerFeature == null ? null : FingerFeature.length) +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                '}';
    }

}

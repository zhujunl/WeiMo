package com.miaxis.weomosdk.entity;

/**
 * @ClassName: AuthEntity
 * @Author: cheng.peng
 * @Date: 2022/5/24 18:04
 */
public class AuthEntity {
    private String deviceChannelNumber;
    private String appKey;
    private String appSecret;
    private String IMEI;
    private String mac;

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getDeviceChannelNumber() {
        return deviceChannelNumber;
    }

    public void setDeviceChannelNumber(String deviceChannelNumber) {
        this.deviceChannelNumber = deviceChannelNumber;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}

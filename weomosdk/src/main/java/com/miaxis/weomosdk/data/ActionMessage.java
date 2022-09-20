package com.miaxis.weomosdk.data;

import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.utils.MmkvHelper;

/**
 * @ClassName: ActionMessage
 * @Author: cheng.peng
 * @Date: 2022/5/26 19:20
 * simpleMessage->ChannelMessage->ActionMessage->real entity
 * 包装者模式构造
 */
public class ActionMessage<T> {
    private String clientId;
    private String deviceChannelNumber;
    private String actionId;
    private Integer value;
    private String reason;
    private String domainId;
    private String mac;
    private String imei;
    private String longitude;
    private String latitude;
    private T data;

    public ActionMessage(T data) {
        this.data = data;
        this.clientId= Constant.CLIENT_ID;
        this.deviceChannelNumber= Constant.DEVICE_CHANNEL;
       this.imei= MmkvHelper.getInstance().getObject("IMEI",String.class);
        this.mac= MmkvHelper.getInstance().getObject("MAC",String.class);
        this.latitude= MmkvHelper.getInstance().getObject("LAT",String.class);
        this.longitude= MmkvHelper.getInstance().getObject("LON",String.class);
    }

    public ActionMessage() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDeviceChannelNumber() {
        return deviceChannelNumber;
    }

    public void setDeviceChannelNumber(String deviceChannelNumber) {
        this.deviceChannelNumber = deviceChannelNumber;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}

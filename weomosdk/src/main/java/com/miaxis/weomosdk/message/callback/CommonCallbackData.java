package com.miaxis.weomosdk.message.callback;

/**
 * @ClassName: CommonCallbackData
 * @Author: cheng.peng
 * @Date: 2022/5/24 19:27
 * 设备通用回调,通道回调
 */
public class CommonCallbackData {
    private String clientId;
    private String deviceChannelNumber;
    private String actionId;
    private Integer value;
    private String reason;
    private String domainId;

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
}

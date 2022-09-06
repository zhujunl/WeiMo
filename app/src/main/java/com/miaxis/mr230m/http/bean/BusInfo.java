package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/9/5 9:28
 * @des
 * @updateAuthor
 * @updateDes
 */
public class BusInfo {
    private String receiveId;
    private String deviceId;
    private String deviceBaseStationInfo;

    public BusInfo(String receiveId, String deviceId, String deviceBaseStationInfo) {
        this.receiveId = receiveId;
        this.deviceId = deviceId;
        this.deviceBaseStationInfo = deviceBaseStationInfo;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceBaseStationInfo() {
        return deviceBaseStationInfo;
    }

    public void setDeviceBaseStationInfo(String deviceBaseStationInfo) {
        this.deviceBaseStationInfo = deviceBaseStationInfo;
    }

    @Override
    public String toString() {
        return "BusInfo{" +
                "receiveId='" + receiveId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceBaseStationInfo='" + deviceBaseStationInfo + '\'' +
                '}';
    }
}

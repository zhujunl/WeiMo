package com.miaxis.weomosdk.entity;

/**
 * @ClassName: HeartBeatEntity
 * @Author: cheng.peng
 * @Date: 2022/5/26 18:34
 * 心跳包数据
 */
public class HeartBeatEntity {
    private String deviceChannelNumber;
    private Long timestamp;


    public static HeartBeatEntity obtain(String channelNumber,long stamp) {
        HeartBeatEntity entity=new HeartBeatEntity();
        entity.deviceChannelNumber=channelNumber;
        entity.timestamp=stamp;
        return entity;
    }

    public String getDeviceChannelNumber() {
        return deviceChannelNumber;
    }

    public void setDeviceChannelNumber(String deviceChannelNumber) {
        this.deviceChannelNumber = deviceChannelNumber;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

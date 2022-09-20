package com.miaxis.weomosdk.entity;

import java.util.List;

/**
 * @ClassName: InitEntity
 * @Author: cheng.peng
 * @Date: 2022/5/25 10:06
 */
public class InitEntity {
    private List<ChannelListDTO> channelList;
    private String algEngineIp;
    private String deviceName;
    private String deviceNumber;
    private String modelId;

    public List<ChannelListDTO> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelListDTO> channelList) {
        this.channelList = channelList;
    }

    public String getAlgEngineIp() {
        return algEngineIp;
    }

    public void setAlgEngineIp(String algEngineIp) {
        this.algEngineIp = algEngineIp;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public static class ChannelListDTO {
        private String channelNumber;
        private String appKey;
        private String appSecret;
        private String channelName;

        public String getChannelNumber() {
            return channelNumber;
        }

        public void setChannelNumber(String channelNumber) {
            this.channelNumber = channelNumber;
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

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }
    }
}

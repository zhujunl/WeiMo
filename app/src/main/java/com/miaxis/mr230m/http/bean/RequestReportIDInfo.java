package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/6/17 16:13
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RequestReportIDInfo {
    private String appId;
    private String sn;
    private Data data;

    public static class Data{
        private String framedata;
        private String samsigncert;

        public Data(String framedata, String samsigncert) {
            this.framedata = framedata;
            this.samsigncert = samsigncert;
        }

        public String getFramedata() {
            return framedata;
        }

        public void setFramedata(String framedata) {
            this.framedata = framedata;
        }

        public String getSamsigncert() {
            return samsigncert;
        }

        public void setSamsigncert(String samsigncert) {
            this.samsigncert = samsigncert;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "framedata='" + framedata + '\'' +
                    ", samsigncert='" + samsigncert + '\'' +
                    '}';
        }
    }

    public RequestReportIDInfo(String appId, String sn, Data data) {
        this.appId = appId;
        this.sn = sn;
        this.data = data;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RequestReportIDInfo{" +
                "appId='" + appId + '\'' +
                ", sn='" + sn + '\'' +
                ", data=" + data +
                '}';
    }
}

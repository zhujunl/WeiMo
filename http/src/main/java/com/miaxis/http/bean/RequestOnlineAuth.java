package com.miaxis.http.bean;

/**
 * @author ZJL
 * @date 2022/6/17 16:13
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RequestOnlineAuth {
    private String appId;
    private String sn;
    private Data data;

    public static class Data{
        private String authreq;

        public String getAuthreq() {
            return authreq;
        }

        public void setAuthreq(String authreq) {
            this.authreq = authreq;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "authreq='" + authreq + '\'' +
                    '}';
        }
    }

    public RequestOnlineAuth(String appId, String sn, Data data) {
        this.appId = appId;
        this.sn = sn;
        this.data = data;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String sppId) {
        this.appId = sppId;
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
        return "RequestOnlineAuth{" +
                "appId='" + appId + '\'' +
                ", sn='" + sn + '\'' +
                ", data=" + data +
                '}';
    }
}

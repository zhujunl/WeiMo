package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/6/17 16:12
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RequestActiveInfo {
    private String appId;
    private String sn;

    public RequestActiveInfo(String appId, String sn) {
        this.appId = appId;
        this.sn = sn;
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

    @Override
    public String toString() {
        return "RequestActiveInfo{" +
                "appId='" + appId + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }
}

package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/9/5 9:25
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RequestJKM {
    private String appId;
    private String sn;
    private String strgyId;
    private DataJkm data;
    private BusInfo busInfo;

    public RequestJKM(String appId, String sn, String strgyId, DataJkm data, BusInfo busInfo) {
        this.appId = appId;
        this.sn = sn;
        this.strgyId = strgyId;
        this.data = data;
        this.busInfo = busInfo;
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

    public String getStrgyId() {
        return strgyId;
    }

    public void setStrgyId(String strgyId) {
        this.strgyId = strgyId;
    }

    public DataJkm getData() {
        return data;
    }

    public void setData(DataJkm data) {
        this.data = data;
    }

    public BusInfo getBusInfo() {
        return busInfo;
    }

    public void setBusInfo(BusInfo busInfo) {
        this.busInfo = busInfo;
    }

    @Override
    public String toString() {
        return "RequestJKM{" +
                "appId='" + appId + '\'' +
                ", sn='" + sn + '\'' +
                ", strgyId='" + strgyId + '\'' +
                ", data=" + data +
                ", busInfo=" + busInfo +
                '}';
    }
}

package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/9/5 9:25
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ResponseJKM {
    private String sn;
    private String ret;
    private String desc;
    private String startTime;
    private String endTime;
    private String data;
    private String platId;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPlatId() {
        return platId;
    }

    public void setPlatId(String platId) {
        this.platId = platId;
    }

    @Override
    public String toString() {
        return "ResponseJKM{" +
                "sn='" + sn + '\'' +
                ", ret='" + ret + '\'' +
                ", desc='" + desc + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", data='" + data + '\'' +
                ", platId='" + platId + '\'' +
                '}';
    }
}

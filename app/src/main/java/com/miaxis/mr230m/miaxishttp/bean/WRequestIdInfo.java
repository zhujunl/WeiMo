package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/6 16:22
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WRequestIdInfo {
    private String cid;
    private String mdeviceid;
    private String samid;
    private String businessSerialNum;
    private String framedata;
    private String samsigncert;

    public WRequestIdInfo(String cid, String mdeviceid, String samid, String businessSerialNum, String framedata, String samsigncert) {
        this.cid = cid;
        this.mdeviceid = mdeviceid;
        this.samid = samid;
        this.businessSerialNum = businessSerialNum;
        this.framedata = framedata;
        this.samsigncert = samsigncert;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMdeviceid() {
        return mdeviceid;
    }

    public void setMdeviceid(String mdeviceid) {
        this.mdeviceid = mdeviceid;
    }

    public String getSamid() {
        return samid;
    }

    public void setSamid(String samid) {
        this.samid = samid;
    }

    public String getBusinessSerialNum() {
        return businessSerialNum;
    }

    public void setBusinessSerialNum(String businessSerialNum) {
        this.businessSerialNum = businessSerialNum;
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
        return "WRequestIdInfo{" +
                "cid='" + cid + '\'' +
                ", mdeviceid='" + mdeviceid + '\'' +
                ", samid='" + samid + '\'' +
                ", businessSerialNum='" + businessSerialNum + '\'' +
                ", framedata='" + framedata + '\'' +
                ", samsigncert='" + samsigncert + '\'' +
                '}';
    }
}

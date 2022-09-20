package com.miaxis.weomosdk.entity;

/**
 * @ClassName: RequestPersonInfoBean
 * @Author: cheng.peng
 * @Date: 2022/9/14 17:07
 */
public class RequestPersonInfoBean {
    private String cid;
    private String mdeviceid;
    private String samid;
    private String businessSerialNum;
    private String framedata;
    private String samsigncert;

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
}

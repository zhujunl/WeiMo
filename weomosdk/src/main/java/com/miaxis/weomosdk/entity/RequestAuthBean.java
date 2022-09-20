package com.miaxis.weomosdk.entity;

/**
 * @ClassName: RequestAuthBean
 * @Author: cheng.peng
 * @Date: 2022/9/14 17:04
 */
public class RequestAuthBean {

    private String cid;
    private String mdeviceid;
    private String samid;
    private String businessSerialNum;
    private String authreq;

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

    public String getAuthreq() {
        return authreq;
    }

    public void setAuthreq(String authreq) {
        this.authreq = authreq;
    }
}

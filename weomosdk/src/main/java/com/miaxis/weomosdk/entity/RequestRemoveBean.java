package com.miaxis.weomosdk.entity;

/**
 * @ClassName: RequestRemoveBean
 * @Author: cheng.peng
 * @Date: 2022/9/14 16:23
 * 微模块业务解除激活
 */
public class RequestRemoveBean {

    private String cid;
    private String mdeviceid;
    private String samid;
    private String businessSerialNum;

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
}

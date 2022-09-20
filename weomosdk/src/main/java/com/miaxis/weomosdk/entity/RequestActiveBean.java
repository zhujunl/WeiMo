package com.miaxis.weomosdk.entity;

/**
 * @ClassName: RequestActiveBean
 * @Author: cheng.peng
 * @Date: 2022/9/14 16:21
 * 微模请求参数
 */
public class RequestActiveBean {
    private String cid;
    private String samid;
    private String businessSerialNum;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/6 16:22
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WRequestDeactiveInfo {
    private String cid;
    private String mdeviceid;
    private String samid;
    private String businessSerialNum;

    public WRequestDeactiveInfo(String cid, String mdeviceid, String samid, String businessSerialNum) {
        this.cid = cid;
        this.mdeviceid = mdeviceid;
        this.samid = samid;
        this.businessSerialNum = businessSerialNum;
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

    @Override
    public String toString() {
        return "WRequestDeactiveInfo{" +
                "cid='" + cid + '\'' +
                ", mdeviceid='" + mdeviceid + '\'' +
                ", samid='" + samid + '\'' +
                ", businessSerialNum='" + businessSerialNum + '\'' +
                '}';
    }
}

package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/6 16:22
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WRequestActiveinfo {
    private String cid;
    private String samid;
    private String businessSerialNum;

    public WRequestActiveinfo(String cid ,String samid, String businessSerialNum) {
        this.cid = cid;
        this.samid = samid;
        this.businessSerialNum = businessSerialNum;
    }

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

    @Override
    public String toString() {
        return "WRequestActiveinfo{" +
                "cid='" + cid + '\'' +
                ", samid='" + samid + '\'' +
                ", businessSerialNum='" + businessSerialNum + '\'' +
                '}';
    }
}

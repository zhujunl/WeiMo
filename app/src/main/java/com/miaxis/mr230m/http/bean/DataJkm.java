package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/9/5 9:27
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DataJkm {
    private String framedata;
    private String samsigncert;

    public DataJkm(String framedata, String samsigncert) {
        this.framedata = framedata;
        this.samsigncert = samsigncert;
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
        return "DataJkm{" +
                "framedata='" + framedata + '\'' +
                ", samsigncert='" + samsigncert + '\'' +
                '}';
    }
}

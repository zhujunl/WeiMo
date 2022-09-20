package com.miaxis.weomosdk.entity;

/**
 * @ClassName: ResponsePersonInfoBean
 * @Author: cheng.peng
 * @Date: 2022/9/14 17:07
 */
public class ResponsePersonInfoBean {

    private String idtype;
    private String chinesename;
    private String englishname;
    private String idnumber;
    private String idvalidtime;
    private String idexpiretime;

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getChinesename() {
        return chinesename;
    }

    public void setChinesename(String chinesename) {
        this.chinesename = chinesename;
    }

    public String getEnglishname() {
        return englishname;
    }

    public void setEnglishname(String englishname) {
        this.englishname = englishname;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getIdvalidtime() {
        return idvalidtime;
    }

    public void setIdvalidtime(String idvalidtime) {
        this.idvalidtime = idvalidtime;
    }

    public String getIdexpiretime() {
        return idexpiretime;
    }

    public void setIdexpiretime(String idexpiretime) {
        this.idexpiretime = idexpiretime;
    }

    @Override
    public String toString() {
        return "ResponsePersonInfoBean{" +
                "idtype='" + idtype + '\'' +
                ", chinesename='" + chinesename + '\'' +
                ", englishname='" + englishname + '\'' +
                ", idnumber='" + idnumber + '\'' +
                ", idvalidtime='" + idvalidtime + '\'' +
                ", idexpiretime='" + idexpiretime + '\'' +
                '}';
    }
}

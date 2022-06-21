package com.miaxis.http.bean;

/**
 * @author ZJL
 * @date 2022/6/17 16:13
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ResponseReportIDInfo {
    private String sn;
    private String ret;
    private String desc;
    private Data data;

    private class Data{
        private String idtype;
        private String chinesename;
        private String englishname;
        private String idnumber;
        private String idvalidtime;
        private String idexpiretime;

        public Data(String idtype, String chinesename, String englishname, String idnumber, String idvalidtime, String idexpiretime) {
            this.idtype = idtype;
            this.chinesename = chinesename;
            this.englishname = englishname;
            this.idnumber = idnumber;
            this.idvalidtime = idvalidtime;
            this.idexpiretime = idexpiretime;
        }

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
            return "Data{" +
                    "idtype='" + idtype + '\'' +
                    ", chinesename='" + chinesename + '\'' +
                    ", englishname='" + englishname + '\'' +
                    ", idnumber='" + idnumber + '\'' +
                    ", idvalidtime='" + idvalidtime + '\'' +
                    ", idexpiretime='" + idexpiretime + '\'' +
                    '}';
        }
    }

    public ResponseReportIDInfo(String sn, String ret, String desc, Data data) {
        this.sn = sn;
        this.ret = ret;
        this.desc = desc;
        this.data = data;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseReportIDInfo{" +
                "sn='" + sn + '\'' +
                ", ret='" + ret + '\'' +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }
}

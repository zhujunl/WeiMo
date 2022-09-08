package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/6 16:22
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WResponseIdInfo {
    private int code;
    private String msg;
    private Data data;

    public class Data{
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WResponseIdInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

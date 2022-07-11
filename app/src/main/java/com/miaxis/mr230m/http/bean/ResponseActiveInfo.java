package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/6/17 16:13
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ResponseActiveInfo {
    private String sn;
    private String ret;
    private String desc;
    private Data data;



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

    public class Data{
        private String activeinfo;

        public String getActiveinfo() {
            return activeinfo;
        }

        public void setActiveinfo(String activeinfo) {
            this.activeinfo = activeinfo;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "activeinfo='" + activeinfo + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "ResponseActiveInfo{" +
                "sn='" + sn + '\'' +
                ", ret='" + ret + '\'' +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }
}

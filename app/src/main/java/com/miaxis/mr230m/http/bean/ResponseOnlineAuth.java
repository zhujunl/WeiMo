package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/6/17 16:13
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ResponseOnlineAuth {
    private String sn;
    private String ret;
    private String desc;
    private Data data;

    public class Data{
        private String authresp;

        public Data(String authresp) {
            this.authresp = authresp;
        }

        public String getAuthresp() {
            return authresp;
        }

        public void setAuthresp(String authresp) {
            this.authresp = authresp;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "authresp='" + authresp + '\'' +
                    '}';
        }
    }

    public ResponseOnlineAuth(String sn, String ret, String desc, Data data) {
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
        return "ResponseOnlineAuth{" +
                "sn='" + sn + '\'' +
                ", ret='" + ret + '\'' +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }
}

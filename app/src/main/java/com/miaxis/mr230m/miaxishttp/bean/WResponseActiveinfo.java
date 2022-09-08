package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/6 16:22
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WResponseActiveinfo {
    private int code;
    private String msg;
    private Data data;

    public class Data{
        private String activeinfo;
        private String mdeviceid;

        public String getActiveinfo() {
            return activeinfo;
        }

        public void setActiveinfo(String activeinfo) {
            this.activeinfo = activeinfo;
        }

        public String getMdeviceid() {
            return mdeviceid;
        }

        public void setMdeviceid(String mdeviceid) {
            this.mdeviceid = mdeviceid;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "activeinfo='" + activeinfo + '\'' +
                    ", mdeviceid='" + mdeviceid + '\'' +
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
        return "WResponseActiveinfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

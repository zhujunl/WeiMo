package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/6 16:22
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WResponseOnlineauthInfo {
    private int code;
    private String msg;
    private Data data;

    public class Data{
        private String authresp;

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
        return "WResponseOnlineauthInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

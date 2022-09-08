package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/7 9:25
 * @des
 * @updateAuthor
 * @updateDes
 */
public class TokenResonse {
    private int code;
    private String msg;
    private Data data;

    public class Data{
        private String access_token;
        private int expires_in;

        @Override
        public String toString() {
            return "Data{" +
                    "access_token='" + access_token + '\'' +
                    ", expires_in='" + expires_in + '\'' +
                    '}';
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
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
        return "TokenResonse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

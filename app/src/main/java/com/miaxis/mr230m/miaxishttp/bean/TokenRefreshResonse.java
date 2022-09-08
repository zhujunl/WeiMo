package com.miaxis.mr230m.miaxishttp.bean;

/**
 * @author ZJL
 * @date 2022/9/7 9:25
 * @des
 * @updateAuthor
 * @updateDes
 */
public class TokenRefreshResonse {
    private int code;
    private String msg;

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

    @Override
    public String toString() {
        return "TokenRefreshResonse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

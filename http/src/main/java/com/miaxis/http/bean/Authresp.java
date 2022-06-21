package com.miaxis.http.bean;

/**
 * @author ZJL
 * @date 2022/6/18 10:55
 * @des
 * @updateAuthor
 * @updateDes
 */
public class Authresp {
    private String authresp;

    public Authresp(String authresp) {
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
        return "Authresp{" +
                "authresp='" + authresp + '\'' +
                '}';
    }
}

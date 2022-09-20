package com.miaxis.weomosdk.api;

/**
 * @ClassName: ZZResponse
 * @Author: cheng.peng
 * @Date: 2022/6/16 16:36
 */
public class ZZResponse {
    public int code;
    public String reason;
    public String msg;
    public String deviceToken;


    public ZZResponse() {
    }

    public ZZResponse(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }
}

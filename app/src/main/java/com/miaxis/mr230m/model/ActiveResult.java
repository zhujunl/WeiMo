package com.miaxis.mr230m.model;

/**
 * @author ZJL
 * @date 2022/9/13 15:32
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ActiveResult {
    private String msg;
    private int re;

    public ActiveResult(String msg, int re) {
        this.msg = msg;
        this.re = re;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRe() {
        return re;
    }

    public void setRe(int re) {
        this.re = re;
    }


}

package com.miaxis.weomosdk.constant;

/**
 * @ClassName: CommandTypeEnum
 * @Author: cheng.peng
 * @Date: 2022/5/21 17:42
 */
/**
 * 操作类型枚举
 * @author midplt
 */
public enum CommandTypeEnum {
    /**
     * 验证
     */
    AUTH(1,"AUTH"),
    /**
     * ping
     */
    PING(2,"PING"),
    /**
     * pong
     */
    PONG(3,"PONG"),
    /**
     * 上报数据（设备到协议层）
     */
    UPLOAD_DATA(4,"UPLOAD_DATA"),
    /**
     * 推送数据（协议层到设备）
     */
    PUSH_DATA(5,"PUSH_DATA"),
    /**
     * 验证返回
     */
    AUTH_BACK(11,"AUTH_BACK"),

    /**
     * 上报数据回调（协议层到设备）
     */
    UPLOAD_DATA_BACK(14,"UPLOAD_DATA_BACK"),

    /**
     * 推送数据回调（设备到协议层）
     */
    PUSH_DATA_BACK(15,"PUSH_DATA_BACK"),
    ;

    private int number;

    private String cmd;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    CommandTypeEnum(int number) {
        this.number = number;
    }

    CommandTypeEnum(String cmd) {
        this.cmd = cmd;
    }


    CommandTypeEnum(int number, String cmd) {
        this.number=number;
        this.cmd = cmd;
    }
}

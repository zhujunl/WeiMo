package com.miaxis.weomosdk.command;

/**
 * @ClassName: CommandParse
 * @Author: cheng.peng
 * @Date: 2022/5/23 17:06
 * 下发字段解析类
 */
public abstract class CommandParse {
    private String TAG=CommandParse.class.getSimpleName();
    private final  long createTime;

    public CommandParse() {
        this.createTime = System.currentTimeMillis();
    }

    /**
     * 获取命令类型
     * @return
     */
    public abstract String getCommand();

    public abstract String  getClientId();

    public abstract String getDeviceChannelNumber();

    public  abstract String getActionId();

    public abstract  String getDomainId();

    public abstract String getData();

    public abstract  int getValue();


    public abstract String getKey();



}

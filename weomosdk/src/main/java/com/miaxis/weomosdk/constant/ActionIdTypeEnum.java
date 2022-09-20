package com.miaxis.weomosdk.constant;

/**
 * @ClassName: ActionIdTypeEnum
 * @Author: cheng.peng
 * @Date: 2022/5/21 17:41
 */
/**
 * 操作领域枚举
 *
 * @author midplt
 */
public enum ActionIdTypeEnum {
    OPEN("1001", "打开关闭"),
    OFTENOPEN("1002", "常开常闭"),
    LINE("1003", "开机关机"),

    DEL("2001", "删除某个通道某个人员授权数据"),
    CHECK("2002", "复核某个设备通道下某个人员某个特征值某个权限计划的授权任务"),
    DELPLAN("2003", ""),
    DOWN("2004", "下发授权任务"),
    REAUTH("2005", "重发某个设备通道下某个人员某个特征值某个权限计划的授权任务"),
    CLEARALL("2006", "清空某个设备所有通道的人员权限信息，等待重新下发"),
    CHECKALL("2007", "复核某个设备通道下所有人员的权限信息"),

    GETPARAM("3001", "设备参数"),
    //    GETABILITY("3002", "业务能力集合"), //废弃
    UPDATEPARAM("3003", "修改设备参数"),
    UPDATEABILITY("3004", "修改业务能力集合"),
    INITCHANNEL("3005", "初始化设备通道信息"),
    UPGRADECHANNEL("3006", "升级设备通道信息"),
    STATUS("3007", "在离线状态"),
    DELETE_DEVICE("3008", "删除设备"),

    UPGRADE("4001", "升级当前版本"),
    ROLLBACK("4002", "回滚上一个版本"),
    RESEND("4003", "重发升级当前版本"),
    GET_THIRD_APP("4004", "获取第三方app列表"),

    USERDOWN("5001", "下发某设备通道下某个人员基础信息"),
    USERDEL("5002", "删除某设备通道下某个人员基础信息"),

    UPLOAD_EVENT("6001", "上报事件"),
    ;
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ActionIdTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActionIdTypeEnum getByCode(String code) {
        ActionIdTypeEnum[] idTypeEnums = ActionIdTypeEnum.values();
        for (ActionIdTypeEnum idTypeEnum : idTypeEnums) {
            if (idTypeEnum.getCode().equals(code)) {
                return idTypeEnum;
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        ActionIdTypeEnum[] idTypeEnums = ActionIdTypeEnum.values();
        for (ActionIdTypeEnum idTypeEnum : idTypeEnums) {
            if (idTypeEnum.name().equals(name)) {
                return idTypeEnum.getCode();
            }
        }
        return null;
    }

    public static ActionIdTypeEnum getByName(String name) {
        ActionIdTypeEnum[] idTypeEnums = ActionIdTypeEnum.values();
        for (ActionIdTypeEnum idTypeEnum : idTypeEnums) {
            if (idTypeEnum.name().equals(name)) {
                return idTypeEnum;
            }
        }
        return null;
    }
}


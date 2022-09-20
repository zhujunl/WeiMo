package com.miaxis.weomosdk.constant;

/**
 * @ClassName: DomainIdTypeEnum
 * @Author: cheng.peng
 * @Date: 2022/5/21 17:40
 */
public enum DomainIdTypeEnum {
    CTRL("1000", "控制"),
    AUTH("2000", "授权"),
    BASE("3000", "基础信息"),
    PROGRAM("4000", "程序相关"),
    USER("5000", "人员信息"),
    ALARM("6000", "报警信息"),
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

    DomainIdTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DomainIdTypeEnum getByCode(String code) {
        DomainIdTypeEnum[] idTypeEnums = DomainIdTypeEnum.values();
        for (DomainIdTypeEnum idTypeEnum : idTypeEnums) {
            if (idTypeEnum.getCode().equals(code)) {
                return idTypeEnum;
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        DomainIdTypeEnum[] idTypeEnums = DomainIdTypeEnum.values();
        for (DomainIdTypeEnum idTypeEnum : idTypeEnums) {
            if (idTypeEnum.name().equals(name)) {
                return idTypeEnum.getCode();
            }
        }
        return null;
    }

    public static DomainIdTypeEnum getByName(String name) {
        DomainIdTypeEnum[] idTypeEnums = DomainIdTypeEnum.values();
        for (DomainIdTypeEnum idTypeEnum : idTypeEnums) {
            if (idTypeEnum.name().equals(name)) {
                return idTypeEnum;
            }
        }
        return null;
    }
}

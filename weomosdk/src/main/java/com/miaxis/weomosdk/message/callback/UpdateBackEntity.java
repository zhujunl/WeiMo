package com.miaxis.weomosdk.message.callback;

/**
 * @ClassName: UpdateBackEntity
 * @Author: cheng.peng
 * @Date: 2022/5/26 13:42
 * 设备升级回调
 */
public class UpdateBackEntity {
    private String detailPlanNumber;
    private String resendFlag;

    public String getDetailPlanNumber() {
        return detailPlanNumber;
    }

    public void setDetailPlanNumber(String detailPlanNumber) {
        this.detailPlanNumber = detailPlanNumber;
    }

    public String getResendFlag() {
        return resendFlag;
    }

    public void setResendFlag(String resendFlag) {
        this.resendFlag = resendFlag;
    }
}

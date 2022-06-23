package org.zz;

/**
 * @author Admin
 * @version $Rev$
 * @des ${}
 * @updateAuthor $Author$
 * @updateDes ${}
 */
public class mr990Driver {
    static {
        System.loadLibrary("mr990Driver");
    }

    // 功能：开关背光灯控制
    // 参数：lightState		-- 1 开灯   0 关灯
    // 返回：0-成功，其它-失败
    public static native int backLightControl(int lightState);

    // 功能：开关红光灯控制
    // 参数：lightState		-- 1 开灯   0 关灯
    // 返回：0-成功，其它-失败
    public static native int redLightControl(int lightState);

    // 功能：开关绿光灯控制
    // 参数：lightState		-- 1 开灯   0 关灯
    // 返回：0-成功，其它-失败
    public static native int greenLightControl(int lightState);

    // 功能：开关蓝光灯控制
    // 参数：lightState		-- 1 开灯   0 关灯
    // 返回：0-成功，其它-失败
    public static native int blueLightControl(int lightState);

    // 功能：USB控制
    // 参数：usbState		-- 1 打开usb与电脑通讯  0 关闭usb与电脑通讯
    // 返回：0-成功，其它-失败
    public static native int usbControl(int usbState);

    // 功能：网口控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static native int ethernetControl(int State);

    // 功能：继电器控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static native int relayControl(int State);

    // 功能：指纹二代证电源控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static native int IDFPControl(int State);

    // 功能：vcc5v电源控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static native int vcc5Control(int State);

    // 功能：摄像头电源控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static native int camControl(int State);

    // 功能：获取USB状态
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static native int getUSBState(byte[] buff, int buffLen);


    // 功能：开关灯控制
    // 参数：lightState		-- 1 开灯   0 关灯
    //      lightType       -- 0 背光灯  1 红灯 2 绿灯 3 蓝灯
    // 返回：0-成功，其它-失败
    public int zzLightControl(int lightState, int lightType) {
        int lRV = -1;
        if (lightType == 0) {
            lRV = backLightControl(lightState);
        } else if (lightType == 1) {
            lRV = redLightControl(lightState);
        } else if (lightType == 2) {
            lRV = greenLightControl(lightState);
        } else if (lightType == 3) {
            lRV = blueLightControl(lightState);
        } else {
            lRV = -5;
        }
        return lRV;
    }

    // 功能：USB控制
    // 参数：State		-- 1 开   0 关
    // 返回：0-成功，其它-失败
    public int zzUSBControl(int State) {
        return usbControl(State);
    }

    // 功能：网口控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public int zzEthernetControl(int State) {
        int lRV = -1;
        lRV = usbControl(0);
        if (lRV != 0) {
            return lRV;
        }
        return ethernetControl(State);
    }


    // 功能：继电器控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public int zzRelayControl(int State) {
        return relayControl(State);
    }


    // 功能：指纹二代证电源控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static int zzIDFPControl(int State) {
        int lRV = -1;
        lRV = usbControl(0);
        if (lRV != 0) {
            return lRV;
        }
        return IDFPControl(State);
    }

    // 功能：摄像头电源控制
    // 参数：State		-- 1 打开  0 关闭
    // 返回：0-成功，其它-失败
    public static int zzCamControl(int State) {
        int lRV = -1;
        lRV = usbControl(0);
        if (lRV != 0) {
            return lRV;
        }
        return camControl(State);
    }

}

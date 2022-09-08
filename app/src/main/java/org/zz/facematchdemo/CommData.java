package org.zz.facematchdemo;

public class CommData {

    private static final String tag = "MIAXIS_CommData";

    //摄像头读写标识
    private static boolean m_bCamerasRW = false;

    public static void setCamerasRW(boolean value) {
        m_bCamerasRW = value;
    }

    public static boolean getCamerasRW() {
        return m_bCamerasRW;
    }

    //人脸跟踪操作标识
    private static boolean m_bFaceTrackOP = false;

    public static boolean getFaceTrackOP() {
        return m_bFaceTrackOP;
    }

    public static void setFaceTrackOP(boolean value) {
        m_bFaceTrackOP = value;
    }

    //人脸识别操作标识
    private static boolean m_bFaceRecogOP = false;

    public static boolean getFaceRecogOP() {
        return m_bFaceRecogOP;
    }

    public static void setFaceRecogOP(boolean value) {
        m_bFaceRecogOP = value;
    }

    //人脸信息读写标识
    private static boolean m_bFaceInfoRW = false;

    public static boolean getFaceInfoRW() {
        return m_bFaceInfoRW;
    }

    public static void setFaceInfoRW(boolean value) {
        m_bFaceInfoRW = value;
    }
}

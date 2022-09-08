package com.miaxis.mr230m.mr990.camera;

/**
 * @author Tank
 * @date 2020/8/10 17:21
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MXCameraErrorCode {


    private static final int BaseCode = -100;
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAIL = BaseCode - 1;
    public static final int CODE_FAIL_NO_CAMERA = BaseCode - 2;
    public static final int CODE_FAIL_CAMERA_COUNTS_LESS = BaseCode - 3;
    public static final int CODE_FAIL_CAMERA_OPEN = BaseCode - 4;
    public static final int CODE_FAIL_PARAMETERS = BaseCode - 5;

    public static final int CODE_FAIL_CAMERA_ID = BaseCode - 6;
    public static final int CODE_FAIL_CAMERA_ID_NOT_FOUND = BaseCode - 7;

    public static final int CODE_FAIL_CAMERA_ORIENTATION = BaseCode - 8;


    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAIL = "fail";
    public static final String MSG_FAIL_NO_CAMERA = "NO CAMERA";
    public static final String MSG_FAIL_CAMERA_COUNTS_LESS = "Counts of camera is less than 2";
    public static final String MSG_FAIL_CAMERA_OPEN = "Camera open error";
    public static final String MSG_FAIL_PARAMETERS = "PARAMETERS  error";

    public static final String MSG_FAIL_CAMERA_ORIENTATION =  "ORIENTATION  error";

}

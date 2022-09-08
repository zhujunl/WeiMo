package com.miaxis.mr230m.mr990.camera;

import android.hardware.Camera;

import com.miaxis.mr230m.mr990.response.ZZResponse;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Admin
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class CameraHelper {

    private final CopyOnWriteArrayList<MXCamera> mMXCameras = new CopyOnWriteArrayList<>();

    private CameraHelper() {
    }

    private static class CameraHelperHolder {
        static CameraHelper mCameraHelper = new CameraHelper();
    }

    public static CameraHelper getInstance() {
        return CameraHelperHolder.mCameraHelper;
    }

    public ZZResponse<?> init(int minCameraNumber) {
        free();
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras <= 0) {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_NO_CAMERA, MXCameraErrorCode.MSG_FAIL_NO_CAMERA);
        }
        if (numberOfCameras < minCameraNumber) {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_COUNTS_LESS, MXCameraErrorCode.MSG_FAIL_CAMERA_COUNTS_LESS + ":" + numberOfCameras);
        }
        return ZZResponse.CreateSuccess();
    }

    public void free() {
        stop();
        this.mMXCameras.clear();
    }

    public ZZResponse<MXCamera> createOrFindMXCamera(CameraConfig cameraConfig) {
        if (cameraConfig == null) {
            return ZZResponse.CreateFail(-90, "config error");
        }
        ZZResponse<MXCamera> mxCameraZZResponse = find(cameraConfig.CameraId);
        if (ZZResponse.isSuccess(mxCameraZZResponse)) {
            return mxCameraZZResponse;
        }
        MXCamera mxCamera = new MXCamera();
        int init = mxCamera.init();
        if (init == 0) {
            ZZResponse<MXCamera> open = mxCamera.open(cameraConfig.CameraId, cameraConfig.width, cameraConfig.height);
            if (ZZResponse.isSuccess(open)) {
                int setOrientation = mxCamera.setOrientation(cameraConfig.previewOrientation);
                if (setOrientation == 0) {
                    addMXCamera(mxCamera);
                } else {
                    return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_ORIENTATION, MXCameraErrorCode.MSG_FAIL_CAMERA_ORIENTATION);
                }
            }
            return open;
        } else {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_NO_CAMERA, MXCameraErrorCode.MSG_FAIL_NO_CAMERA);
        }
    }

    public ZZResponse<MXCamera> find(CameraConfig cameraConfig) {
        if (cameraConfig == null) {
            return ZZResponse.CreateFail(-90, "config error");
        }
        return find(cameraConfig.CameraId);
    }

    private ZZResponse<MXCamera> find(int cameraId) {
        if (cameraId < 0) {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_ID, null);
        }
        for (MXCamera mxCamera : this.mMXCameras) {
            if (mxCamera.getCameraId() == cameraId) {
                return ZZResponse.CreateSuccess(mxCamera);
            }
        }
        return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_ID_NOT_FOUND, null);
    }

    private void addMXCamera(MXCamera mxCamera) {
        if (mxCamera == null) {
            return;
        }
        this.mMXCameras.add(mxCamera);
    }

    public int resume() {
        for (MXCamera mxCamera : this.mMXCameras) {
            if (mxCamera != null) {
                mxCamera.resume();
            }
        }
        return 0;
    }

    public int pause() {
        for (MXCamera mxCamera : this.mMXCameras) {
            if (mxCamera != null) {
                mxCamera.pause();
            }
        }
        return 0;
    }

    public int stop() {
        for (MXCamera mxCamera : this.mMXCameras) {
            if (mxCamera != null) {
                mxCamera.stop();
            }
        }
        return 0;
    }

}

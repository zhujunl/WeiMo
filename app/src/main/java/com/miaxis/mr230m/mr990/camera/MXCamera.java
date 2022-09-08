package com.miaxis.mr230m.mr990.camera;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import com.miaxis.mr230m.mr990.response.ZZResponse;


/**
 * @author Tank
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class MXCamera implements Camera.AutoFocusCallback, Camera.PreviewCallback {

    private static final String TAG = "MXCamera";
    private int width = 640;
    private int height = 480;
    private boolean isPreview = false;
    private CameraPreviewCallback mCameraPreviewCallback;
    private Camera mCamera;
    private int mCameraId;
    private int orientation;
    private static final int errorCounts = 3;

    protected MXCamera() {
    }

    public int init() {
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras <= 0) {
            return -1;
        }
        return 0;
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    protected ZZResponse<MXCamera> open(int cameraId, int width, int height) {
        if (this.mCamera != null) {
            return ZZResponse.CreateFail(-2, "already open");
        }
        //        if (cameraId >= Camera.getNumberOfCameras()) {
        //            return -4;
        //        }
        for (int i = 0; i < errorCounts; i++) {
            try {
                this.mCameraId = cameraId;
                this.mCamera = Camera.open(cameraId);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "" + e);
                SystemClock.sleep(500);
            }
        }
        if (this.mCamera == null) {
            return ZZResponse.CreateFail(-1, "open camera failed");
        }

        try {
            Camera.Parameters parameters = this.mCamera.getParameters();
            parameters.setPreviewSize(width, height);
            parameters.setPictureSize(width, height);
            this.mCamera.setParameters(parameters);
            this.width = width;
            this.height = height;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (this.mCamera != null) {
                    this.mCamera.release();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return ZZResponse.CreateFail(-3, "set camera parameters failed," + e.getMessage());
        }
        this.buffer = new byte[((this.width * this.height) * ImageFormat.getBitsPerPixel(ImageFormat.NV21)) / 8];
        return ZZResponse.CreateSuccess(this);
    }

    public int setOrientation(int orientation) {
        if (this.mCamera == null) {
            return -1;
        }
        this.mCamera.setDisplayOrientation(orientation);
        this.orientation = orientation;
        return 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private byte[] buffer;

    public int setPreviewCallback(CameraPreviewCallback cameraPreviewCallback) {
        if (this.mCamera == null) {
            return -1;
        }
        this.mCamera.setPreviewCallbackWithBuffer(this);
        this.mCameraPreviewCallback = cameraPreviewCallback;
        return 0;
    }

    public int takePicture(Camera.PictureCallback jpeg) {
        if (this.mCamera == null) {
            return -1;
        }

        if (!this.isPreview) {
            return -2;
        }
        this.mCamera.takePicture(() -> {
        }, (data, camera) -> {
        }, jpeg);
        return 0;
    }

    public int setNextFrameEnable() {
        if (this.mCamera == null) {
            return -1;
        }
        this.mCamera.addCallbackBuffer(this.buffer);
        return 0;
    }

    public byte[] getCurrentFrame() {
        if (this.mCamera == null || !this.isPreview) {
            return null;
        }
        return this.buffer;
    }


    public int start(SurfaceHolder holder) {
        if (this.mCamera == null) {
            return -1;
        }
        try {
            this.mCamera.setPreviewDisplay(holder);
            this.mCamera.startPreview();
            this.isPreview = true;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public int startTexture(SurfaceTexture holder) {
        if (this.mCamera == null) {
            return -1;
        }
        try {
            this.mCamera.setPreviewTexture(holder);
            this.mCamera.startPreview();
            this.isPreview = true;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public int resume() {
        if (this.mCamera == null) {
            return -1;
        }
        if (this.isPreview) {
            try {
                Log.e(TAG, "resume: " + mCameraId);
                this.mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "resume: " + e);
            }
        }
        return 0;
    }

    public int pause() {
        if (this.mCamera == null) {
            return -1;
        }
        try {
            Log.e(TAG, "pause: " + mCameraId);
            this.mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "pause: " + e);
        }
        return 0;
    }

    public int stop() {
        if (this.mCamera == null) {
            return -1;
        }
        try {
            this.mCamera.stopPreview();
            this.mCamera.setPreviewCallback(null);
            this.mCamera.setPreviewCallbackWithBuffer(null);
            this.mCamera.release();
            this.mCamera = null;
            this.isPreview = false;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.e(TAG, "onPreviewFrame" );
        if (mCameraPreviewCallback != null) {
            mCameraPreviewCallback.onPreview(this, new MXFrame(data, this.width, this.height, this.orientation));
        }
    }

//    /**
//     * 保存视频帧数据
//     * 视频帧Buffer为 setNextFrameEnable() 后一帧数据
//     *
//     * @param savePath 保存文件路径
//     * @return true:保存成功    false:失败
//     */
//    public boolean saveFrameImage(String savePath, int rotate) {
//        try {
//            byte[] nv21toJPEG = MXUtils.NV21toJPEG(this.buffer, this.width, this.height, 90);
//            if (nv21toJPEG == null) {
//                return false;
//            }
//            Bitmap bitmap = BitmapFactory.decodeByteArray(nv21toJPEG, 0, nv21toJPEG.length);
//            Bitmap rotateBitmap = MXUtils.rotate(bitmap, rotate);
//            return BitmapUtils.saveBitmap(rotateBitmap, savePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    //设置焦距
    public void setZoom(int zoom) {
        if (this.mCamera == null) {
            return;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        boolean zoomSupported = parameters.isZoomSupported();
        if (zoomSupported) {
            int maxZoom = parameters.getMaxZoom();
            if (zoom <= 1) {
                zoom = 1;
            }
            if (zoom >= maxZoom) {
                zoom = maxZoom;
            }
            parameters.setZoom(zoom);
            this.mCamera.setParameters(parameters);
        }
    }

    public int getMaxZoom() {
        if (this.mCamera == null) {
            return -1;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        return parameters.getMaxZoom();
    }

}

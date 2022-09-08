package com.miaxis.mr230m.viewmodel;

import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.miaxis.mr230m.mr990.camera.CameraConfig;
import com.miaxis.mr230m.mr990.camera.CameraHelper;
import com.miaxis.mr230m.mr990.camera.CameraPreviewCallback;
import com.miaxis.mr230m.mr990.camera.MXCamera;
import com.miaxis.mr230m.mr990.camera.MXFrame;
import com.miaxis.mr230m.mr990.response.ZZResponse;
import com.miaxis.mr230m.util.ListUtils;

import org.zz.api.MXFace;
import org.zz.api.MXFaceIdAPI;
import org.zz.api.MXImageToolsAPI;
import org.zz.api.MXResult;
import org.zz.api.MxImage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class PreviewViewModel extends ViewModel implements CameraPreviewCallback {

    private static final String TAG = "PreviewViewModel";
    private Handler mHandler = new Handler();

    /**
     * 人脸框
     */
    MutableLiveData<RectF> faceRect = new MutableLiveData<>();
    /**
     * 摄像头是否可用
     */
    MutableLiveData<ZZResponse<?>> IsCameraEnable_Rgb = new MutableLiveData<>();
    MutableLiveData<ZZResponse<?>> IsCameraEnable_Nir = new MutableLiveData<>();

    /**
     * 近红外预览区域
     */
    AtomicReference<SurfaceHolder> SurfaceHolder_Nir = new AtomicReference<>();
    /**
     * Nir视频帧是否正在处理
     */
    AtomicBoolean IsNirFrameProcessing = new AtomicBoolean(false);
    /**
     * 是否启用近红外帧
     */
    AtomicBoolean IsNirEnable = new AtomicBoolean(true);


    /**
     * 人脸帧数据缓存
     */
    AtomicReference<Map.Entry<MxImage, MXFace>> CurrentMxImage_Rgb = new AtomicReference<>();
    AtomicReference<Map.Entry<MxImage, MXFace>> CurrentMxImage_Nir = new AtomicReference<>();

    //MutableLiveData<Boolean> HaveFace = new MutableLiveData<>(false);

    MutableLiveData<Boolean> StartCountdown = new MutableLiveData<>(true);



    public PreviewViewModel() {
    }


    /**
     * 处理可见光视频帧数据
     */
    private synchronized void Process_Rgb(MXFrame frame) {
        Log.e(TAG, "Process_Rgb====" );
        if (!MXFrame.isBufferEmpty(frame) && MXFrame.isSizeLegal(frame)) {
            MXResult<byte[]> mxResult = MXImageToolsAPI.getInstance().YUV2RGB(frame.buffer, frame.width, frame.height);//MR90 10ms
            if (MXResult.isSuccess(mxResult)) {
                MXResult<MxImage> imageZoom = MXImageToolsAPI.getInstance().ImageZoom(new MxImage(frame.width, frame.height, mxResult.getData()),
                        frame.width / 2, frame.height / 2);
                if (MXResult.isSuccess(imageZoom)) {
                    MXResult<MxImage> imageRotate = MXImageToolsAPI.getInstance().ImageRotate(
                            imageZoom.getData(),
                            CameraConfig.Camera_RGB.bufferOrientation);
                    if (MXResult.isSuccess(imageRotate)) {
                        MxImage mxImage = imageRotate.getData();
                        MXResult<List<MXFace>> detectFace = MXFaceIdAPI.getInstance().mxDetectFace(
                                mxImage.buffer, mxImage.width, mxImage.height);//MR90 40--100ms
                        if (MXResult.isSuccess(detectFace)) {
                            List<RectF> list = new ArrayList<>();
                            List<MXFace> data = detectFace.getData();
                            for (MXFace mxFace : data) {
                                RectF faceRectF = mxFace.getFaceRectF();
                                faceRectF.left = faceRectF.left * 2;
                                faceRectF.top = faceRectF.top * 2;
                                faceRectF.right = faceRectF.right * 2;
                                faceRectF.bottom = faceRectF.bottom * 2;
                                list.add(faceRectF);
                            }
                            if (!ListUtils.isNullOrEmpty(data)) {
                                this.StartCountdown.postValue(true);
                            }
                            boolean b = this.CurrentMxImage_Rgb.compareAndSet(null, new AbstractMap.SimpleEntry<>(mxImage, MXFaceIdAPI.getInstance().getMaxFace(data)));

                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 开启可见光视频帧
     */
    private void startRgbFrame() {
        ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createOrFindMXCamera(CameraConfig.Camera_RGB);
        if (ZZResponse.isSuccess(mxCamera)) {
            int enable = mxCamera.getData().setNextFrameEnable();
            this.IsCameraEnable_Rgb.setValue(ZZResponse.CreateSuccess());
        } else {
            this.StartCountdown.postValue(false);
            this.IsCameraEnable_Rgb.setValue(ZZResponse.CreateFail(mxCamera.getCode(), mxCamera.getMsg()));
        }
    }

    /**
     * 开启近红外视频帧
     * 主线程
     */
    private void startNirFrame() {
        if (!this.IsNirFrameProcessing.get()) {
            ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createOrFindMXCamera(CameraConfig.Camera_NIR);
            if (ZZResponse.isSuccess(mxCamera)) {
                int enable = mxCamera.getData().setNextFrameEnable();
                this.IsNirFrameProcessing.set(true);
                this.IsCameraEnable_Nir.setValue(ZZResponse.CreateSuccess());
            } else {
                this.IsNirEnable.set(true);
                this.StartCountdown.postValue(false);
                this.IsCameraEnable_Nir.setValue(ZZResponse.CreateFail(mxCamera.getCode(), mxCamera.getMsg()));
            }
        }
    }

    /**
     * 处理近红外视频帧数据
     */
    private void Process_Nir(MXFrame frame) {

    }

    /**
     * 开启可见光预览
     */
    public void showRgbCameraPreview(SurfaceTexture surface) {
        ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createOrFindMXCamera(CameraConfig.Camera_RGB);
        if (ZZResponse.isSuccess(mxCamera)) {
            MXCamera camera = mxCamera.getData();
            int startTexture = camera.startTexture(surface);
            if (startTexture == 0) {
                mxCamera.getData().setPreviewCallback(PreviewViewModel.this);
                startRgbFrame();
            } else {
                Log.e(TAG, "Preview failed");
                return;
            }
        }
    }

    /**
     * 开启近红外预览
     */
    private void startNirPreview() {

    }

    private String lastUserID;
    private long lastTime;

    /**
     * 处理活体和比对
     */
    private void processLiveAndMatch() {

    }

    @Override
    public void onPreview(MXCamera camera, MXFrame frame) {
        if (camera.getCameraId() == CameraConfig.Camera_RGB.CameraId) {
            Log.e(TAG, "Camera_RGB" );
            this.Process_Rgb(frame);
        } else {
            Log.e(TAG, "Process_Nir" );
            this.Process_Nir(frame);
        }
    }



    public void resume() {
        this.CurrentMxImage_Rgb.set(null);
        this.CurrentMxImage_Nir.set(null);
        CameraHelper.getInstance().resume();
    }

    public void pause() {
        CameraHelper.getInstance().pause();
    }

    public void destroy() {
        this.StartCountdown.postValue(false);
        CameraHelper.getInstance().stop();
        CameraHelper.getInstance().free();
    }

}
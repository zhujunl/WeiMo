package com.miaxis.mr230m.viewmodel;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.miaxis.mr230m.mr990.camera.CameraConfig;
import com.miaxis.mr230m.mr990.camera.CameraHelper;
import com.miaxis.mr230m.mr990.camera.MXCamera;
import com.miaxis.mr230m.mr990.camera.MXFrame;
import com.miaxis.mr230m.mr990.response.ZZResponse;

import org.zz.api.MXFace;
import org.zz.api.MxImage;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class PreviewViewModel extends ViewModel{

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
    private synchronized void Process_Rgb(byte[] faceData) {
//        MXResult<byte[]> mxResult = MXImageToolsAPI.getInstance().YUV2RGB(faceData, 640,480);//MR90 10ms
//        if (MXResult.isSuccess(mxResult)) {
//            MXResult<MxImage> imageZoom = MXImageToolsAPI.getInstance().ImageZoom(new MxImage(640, 480, mxResult.getData()),
//                    640 / 2, 480/ 2);
//            if (MXResult.isSuccess(imageZoom)){
//                MXResult<MxImage> imageRotate = MXImageToolsAPI.getInstance().ImageRotate(
//                        imageZoom.getData(),
//                        CameraConfig.Camera_RGB.bufferOrientation);
//                if (MXResult.isSuccess(imageRotate)) {
//                    MxImage mxImage = imageRotate.getData();
//                    MXResult<List<MXFace>> detectFace = MXFaceIdAPI.getInstance().mxDetectFace(
//                            mxImage.buffer, mxImage.width, mxImage.height);//MR90 40--100ms
//                    if (MXResult.isSuccess(detectFace)) {
//                        List<RectF> list = new ArrayList<>();
//                        List<MXFace> data = detectFace.getData();
//                        for (MXFace mxFace : data) {
//                            RectF faceRectF = mxFace.getFaceRectF();
//                            faceRectF.left=faceRectF.left*2;
//                            faceRectF.top=faceRectF.top*2;
//                            faceRectF.right=faceRectF.right*2;
//                            faceRectF.bottom=faceRectF.bottom*2;
//                            list.add(faceRectF);
//                        }
//                        MXFace maxFace = MXFaceIdAPI.getInstance().getMaxFace(data);
//                        MXResult<byte[]> featureExtract = MXFaceIdAPI.getInstance().mxFeatureExtract(mxImage.buffer, mxImage.width, mxImage.height, maxFace);
//                        if (!MXResult.isSuccess(featureExtract)) {
//                            Log.e(TAG, "!MXResult.isSuccess(featureExtract)" );
//                            return;
//                        }
//                        MXResult<Float> result = MXFaceIdAPI.getInstance().mxFeatureMatch(featureExtract.getData(),null);
//                    }
//                }
//            }
//        }


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
    Bitmap faceBitmap;

    Camera camera;

    public void setFaceBitmap(Bitmap faceBitmap) {
        this.faceBitmap = faceBitmap;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(faceBitmap.getByteCount());
        faceBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        MxImage i=new MxImage(faceBitmap.getWidth(), faceBitmap.getHeight(), outputStream.toByteArray());
    }

    /**
     * 开启可见光预览
     */
    public void showRgbCameraPreview(SurfaceTexture surface) {
//        ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createOrFindMXCamera(CameraConfig.Camera_RGB);
//        if (ZZResponse.isSuccess(mxCamera)) {
//            MXCamera camera = mxCamera.getData();
//            int startTexture = camera.startTexture(surface);
//            if (startTexture == 0) {
//                mxCamera.getData().setPreviewCallback(new CameraPreviewCallback() {
//                    @Override
//                    public void onPreview(MXCamera camera, MXFrame frame) {
//
//                    }
//                });
//                startRgbFrame();
//            } else {
//                Log.e(TAG, "Preview failed");
//                return;
//            }
//        }
        try {
            for (int i = 0; i < 3; i++) {
                camera=Camera.open(0);
                if (camera!=null){
                    break;
                }
            }
            Camera.Parameters parameters = this.camera.getParameters();
            parameters.setPreviewSize(640,480);
            parameters.setPictureSize(640,480);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(270);
            camera.setPreviewTexture(surface);
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Process_Rgb(data);
                }
            });
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRgbCameraPreview(){
      try {
          if (camera != null) {
              camera.setPreviewCallback(null);
              camera.stopPreview();
              camera.release();
              camera = null;
          }
      } catch (Exception e) {
          e.printStackTrace();
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
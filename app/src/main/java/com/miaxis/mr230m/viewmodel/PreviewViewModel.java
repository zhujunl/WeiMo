package com.miaxis.mr230m.viewmodel;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import com.miaxis.mr230m.App;
import com.miaxis.mr230m.event.SingleLiveEvent;
import com.miaxis.mr230m.model.PhotoFaceFeature;
import com.miaxis.mr230m.model.Result;
import com.miaxis.mr230m.mr990.camera.CameraConfig;
import com.miaxis.mr230m.mr990.camera.CameraHelper;

import org.zz.api.MXFace;
import org.zz.api.MXFaceIdAPI;
import org.zz.api.MXFaceInfoEx;
import org.zz.api.MXImageToolsAPI;
import org.zz.api.MXResult;
import org.zz.api.MxImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class PreviewViewModel extends ViewModel{

    private static final String TAG = "PreviewViewModel";

    //MutableLiveData<Boolean> HaveFace = new MutableLiveData<>(false);

    MutableLiveData<Boolean> StartCountdown = new MutableLiveData<>(true);

    public SingleLiveEvent<Result> VerifyResult=new SingleLiveEvent<>();



    public PreviewViewModel() {
    }

    /**
     * 处理可见光视频帧数据
     */
    private synchronized void Process_Rgb(byte[] faceData) {
        if (PhotoFaceFeatureMutableLiveData.getValue()==null){
            VerifyResult.setValue(new Result("照片未检测到人脸",false));
            return;
        }
        App.getInstance().threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MXResult<byte[]> mxResult = MXImageToolsAPI.getInstance().YUV2RGB(faceData, 640,480);//MR90 10ms
                if (MXResult.isSuccess(mxResult)) {
                    MXResult<MxImage> imageZoom = MXImageToolsAPI.getInstance().ImageZoom(new MxImage(640, 480, mxResult.getData()),
                            640 / 2, 480/ 2);
                    if (MXResult.isSuccess(imageZoom)){
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
                                    faceRectF.left=faceRectF.left*2;
                                    faceRectF.top=faceRectF.top*2;
                                    faceRectF.right=faceRectF.right*2;
                                    faceRectF.bottom=faceRectF.bottom*2;
                                    list.add(faceRectF);
                                }
                                MXFace maxFace = MXFaceIdAPI.getInstance().getMaxFace(data);
                                MXResult<byte[]> featureExtract = MXFaceIdAPI.getInstance().mxFeatureExtract(mxImage.buffer, mxImage.width, mxImage.height, maxFace);
                                if (!MXResult.isSuccess(featureExtract)) {
                                    Log.e(TAG, "!MXResult.isSuccess(featureExtract)" );
                                    return;
                                }
                                MXResult<Float> result = MXFaceIdAPI.getInstance().mxFeatureMatch(featureExtract.getData(),PhotoFaceFeatureMutableLiveData.getValue().getFaceFeature());
                                if (result.getData()>0.7F){
                                    Log.d(TAG, "核验成功==" +result.getData() );
                                    VerifyResult.postValue(new Result(String.valueOf(result.getData()),true));
                                }
                            }
                        }
                    }
                }
            }
        });



    }

   public MutableLiveData<PhotoFaceFeature> PhotoFaceFeatureMutableLiveData=new MutableLiveData<>();

    public void setCardFaceFeature(Bitmap bitmap) {
        String message = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] rgbData = imageFileDecode(outputStream.toByteArray(), bitmap.getWidth(), bitmap.getHeight());
        if (rgbData == null) {
            message = "图片转码失败";
            PhotoFaceFeatureMutableLiveData.setValue(new PhotoFaceFeature(message));;
        }
        int[] pFaceNum = new int[]{0};
        MXFaceInfoEx[] pFaceBuffer = makeFaceContainer(50);
        boolean result = faceDetect(rgbData, bitmap.getWidth(), bitmap.getHeight(), pFaceNum, pFaceBuffer);
        if (result && pFaceNum[0] > 0) {
            MXFaceInfoEx mxFaceInfoEx = sortMXFaceInfoEx(pFaceBuffer);
            byte[] faceFeature = extractFeature(rgbData, bitmap.getWidth(), bitmap.getHeight(), mxFaceInfoEx);
            if (faceFeature != null) {
                PhotoFaceFeatureMutableLiveData.setValue(new PhotoFaceFeature(faceFeature, "提取成功"));
                return;
            } else {
                message = "提取特征失败";
            }
        } else {
            message = "未检测到人脸";
        }
        PhotoFaceFeatureMutableLiveData.setValue(new PhotoFaceFeature(message));;
    }

    public byte[] imageFileDecode(byte[] data, int width, int height) {
        byte[] rgbData = new byte[width * height * 3];
        int[] oX = new int[1];
        int[] oY = new int[1];
        int result = MXImageToolsAPI.getInstance().getMxImageTool().ImageDecode(data, data.length, rgbData, oX, oY);
        if (result > 0) {
            return rgbData;
        }
        return null;
    }

    private MXFaceInfoEx[] makeFaceContainer(int size) {
        MXFaceInfoEx[] pFaceBuffer = new MXFaceInfoEx[size];
        for (int i = 0; i < size; i++) {
            pFaceBuffer[i] = new MXFaceInfoEx();
        }
        return pFaceBuffer;
    }

    private boolean faceDetect(byte[] rgbData, int width, int height, int[] faceNum, MXFaceInfoEx[] faceBuffer) {
            int result = MXFaceIdAPI.getInstance().mxDetectFace(rgbData, width, height, faceNum, faceBuffer);
            return result == 0 && faceNum[0] > 0;
    }

    private MXFaceInfoEx sortMXFaceInfoEx(MXFaceInfoEx[] mxFaceInfoExList) {
        MXFaceInfoEx maxMXFaceInfoEx = mxFaceInfoExList[0];
        for (MXFaceInfoEx mxFaceInfoEx : mxFaceInfoExList) {
            if (mxFaceInfoEx.width > maxMXFaceInfoEx.width) {
                maxMXFaceInfoEx = mxFaceInfoEx;
            }
        }
        return maxMXFaceInfoEx;
    }

    private byte[] extractFeature(byte[] pImage, int width, int height, MXFaceInfoEx faceInfo) {
            byte[] feature = new byte[MXFaceIdAPI.getInstance().mxGetFeatureSize()];
            int result = MXFaceIdAPI.getInstance().mxFeatureExtract(pImage, width, height, 1, new MXFaceInfoEx[]{faceInfo}, feature);
            return result == 0 ? feature : null;
    }

    Camera camera;

    /**
     * 开启可见光预览
     */
    public void showRgbCameraPreview(SurfaceTexture surface) {
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
            camera.setPreviewCallback((data, camera) -> Process_Rgb(data));
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


    public void destroy() {
        this.StartCountdown.postValue(false);
        CameraHelper.getInstance().stop();
        CameraHelper.getInstance().free();
    }

}
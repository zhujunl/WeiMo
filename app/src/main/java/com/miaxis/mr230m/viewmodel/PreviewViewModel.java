package com.miaxis.mr230m.viewmodel;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

import com.miaxis.mr230m.model.PhotoFaceFeature;
import com.miaxis.mr230m.mr990.camera.CameraConfig;
import com.miaxis.mr230m.mr990.camera.CameraHelper;

import org.zz.api.MXFace;
import org.zz.api.MXFaceIdAPI;
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
    private Handler mHandler = new Handler();

    //MutableLiveData<Boolean> HaveFace = new MutableLiveData<>(false);

    MutableLiveData<Boolean> StartCountdown = new MutableLiveData<>(true);



    public PreviewViewModel() {
    }


    /**
     * 处理可见光视频帧数据
     */
    private synchronized void Process_Rgb(byte[] faceData) {
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
                        MXResult<Float> result = MXFaceIdAPI.getInstance().mxFeatureMatch(featureExtract.getData(),mPhotoFaceFeature.getFaceFeature());
                    }
                }
            }
        }


    }


    PhotoFaceFeature mPhotoFaceFeature;

    public void getCardFaceFeatureByBitmapPosting(Bitmap bitmap) {
        String message = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] rgbData = imageFileDecode(outputStream.toByteArray(), bitmap.getWidth(), bitmap.getHeight());
        if (rgbData == null) {
            message = "图片转码失败";
            mPhotoFaceFeature= new PhotoFaceFeature(message);
        }
        MXResult<MxImage> imageZoom = MXImageToolsAPI.getInstance().ImageZoom(new MxImage( bitmap.getWidth(),  bitmap.getHeight(), rgbData),
                bitmap.getWidth()/2,  bitmap.getHeight()/2);
        if (MXResult.isSuccess(imageZoom)) {
            MxImage mxImage = imageZoom.getData();
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
                if (MXResult.isSuccess(featureExtract)) {
                    mPhotoFaceFeature=new PhotoFaceFeature(featureExtract.getData(), "提取成功");
                }
                Log.e(TAG, "!MXResult.isSuccess(featureExtract)" );
                message = "照片提取特征失败";
                mPhotoFaceFeature= new PhotoFaceFeature(message);
            }
            message = "照片未检测到人脸";
            mPhotoFaceFeature= new PhotoFaceFeature(message);
        }
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


    public void destroy() {
        this.StartCountdown.postValue(false);
        CameraHelper.getInstance().stop();
        CameraHelper.getInstance().free();
    }

}
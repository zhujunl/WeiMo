package org.zz.facematchdemo;

import android.app.Activity;

public class LiveDetectActivity extends Activity{

//    private static final String TAG = "MIAXIS";
//    private static boolean DEBUG = false;
//
//    private static int PIXEL_WIDTH  = 320;//640 //320
//    private static int PIXEL_HEIGHT = 240;//480 //240
//
//    private int m_infraredCamIndex = 0; //红外摄像头序号
//    private int m_visibleCamIndex  = 1; //可见光摄像头序号
//
//    private SurfaceView m_infraredSurfaceview     = null;      // SurfaceView对象：(视图组件)视频显示
//    private SurfaceHolder m_infraredSurfaceHolder = null;      // SurfaceHolder对象：(抽象接口)SurfaceView支持类
//    private Camera m_infraredCamera = null;                   // Camera对象，相机预览
//    private byte[] m_infraredData   = null;
//
//    private SurfaceView m_visibleSurfaceview     = null;     // SurfaceView对象：(视图组件)视频显示
//    private SurfaceHolder m_visibleSurfaceHolder = null;     // SurfaceHolder对象：(抽象接口)SurfaceView支持类
//    private Camera m_visibleCamera  = null;                    // Camera对象，相机预览
//    private byte[] m_visibleData  = null;
//
//    private boolean m_bProcessImage = false;
//
//    private static final int SHOW_MSG                   = 0;   // 显示信息
//    private static final int SHOW_INFRARED_CAM_IMG_MSG  = 10;  // 显示红外摄像头图像
//    private static final int SHOW_VISIBLE_ICAM_MG_MSG   = 11;  // 显示可见光摄像头图像
//
//    Bitmap m_infraredCamBitmap    = null;
//    Bitmap m_visibleCamBitmap     = null;
//
//    MXFaceAPI m_faceAlgApi = null;
//    mxImageTool m_imgTool= null;
//    int m_iScore = 80;
//    int m_iNirLiveDetect = 1;
//    int m_iCamAngle = 0;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
//        setContentView(R.layout.activity_live_detect);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
//
//        Intent intent=getIntent();
//        Bundle bundle=intent.getExtras();
//        m_iCamAngle  = bundle.getInt("CamAngle");
//        m_visibleCamIndex  = bundle.getInt("CamIndex");
//        if(m_visibleCamIndex == 0)
//            m_infraredCamIndex = 1;
//        else
//            m_infraredCamIndex = 0;
//
//        m_faceAlgApi   = MainActivity.GetFaceAPI();
//        m_imgTool      = MainActivity.GetImageTool();
//
//        m_infraredSurfaceview   = (SurfaceView) findViewById(R.id.surfaceview_infrared);
//        m_infraredSurfaceHolder = m_infraredSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
//        m_infraredSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                Timber.e(TAG, "infrared surfaceCreated");
//                openInfraredCamera();
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//                Timber.e(TAG, "infrared surfaceDestroyed");
//                m_bProcessImage = false;
//                MySleep(100);
//                closeInfraredCamera();
//            }
//        });
//
//        m_visibleSurfaceview   = (SurfaceView) findViewById(R.id.surfaceview_visible);
//        m_visibleSurfaceHolder = m_visibleSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
//        m_visibleSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
//
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                Timber.e(TAG, "visible surfaceCreated");
//                openVisibleCamera();
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//                Timber.e(TAG, "visible surfaceDestroyed");
//                m_bProcessImage = false;
//                MySleep(100);
//                closeVisibleCamera();
//            }
//        });
//
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(this::camerathread);
//    }
//
//    public void camerathread() {
//        m_bProcessImage = true;
//        while(m_bProcessImage)
//        {
//            if (m_infraredData != null  && m_visibleData != null ) {
//                processImage();
//            }
//            MySleep(50);
//        }
//    }
//
//    public void MySleep(int iTimeout)
//    {
//        long duration = -1;
//        Calendar time1,time2;
//        time1 = Calendar.getInstance();
//        while(duration<=iTimeout){
//            time2 = Calendar.getInstance();
//            duration = time2.getTimeInMillis() - time1.getTimeInMillis();
//        }
//    }
//
//    // 主界面按返回键弹出确认退出对话框
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 按下键盘上返回按钮
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Timber.e(TAG, "onKeyDown KEYCODE_BACK");
//            finish();
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
//
//    public int openInfraredCamera()
//    {
//        try {
//            if (m_infraredCamera != null) {
//                Timber.e(TAG, "openInfraredCamera -> closeInfraredCamera");
//                closeInfraredCamera();
//            }
//            m_infraredCamera = Camera.open(m_infraredCamIndex);
//            Camera.Parameters parameters = m_infraredCamera.getParameters();
//            /* 指定preview的屏幕大小 */
//            parameters.setPreviewSize(PIXEL_WIDTH, PIXEL_HEIGHT);
//            /* 设置图片分辨率大小 */
//            parameters.setPictureSize(PIXEL_WIDTH, PIXEL_HEIGHT);
//            m_infraredCamera.setParameters(parameters);
//            try {
//                /* setPreviewDisplay唯一的参数为SurfaceHolder */
//                m_infraredCamera.setPreviewDisplay(m_infraredSurfaceHolder);
//                m_infraredCamera.setPreviewCallback(infraredPreviewCallback);
//                m_infraredCamera.startPreview();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//                return -2;
//            }
//        } catch (Exception e) {
//            Timber.w(TAG, "Open Camera Error ! ");
//            return -1;
//        }
//        return 0;
//    }
//
//    public void closeInfraredCamera() {
//        try {
//            if (m_infraredCamera != null) {
//                m_infraredCamera.setPreviewCallback(null) ;
//                m_infraredCamera.stopPreview();
//                m_infraredCamera.release();
//                m_infraredCamera = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int openVisibleCamera()
//    {
//        try {
//            if (m_visibleCamera != null) {
//                Timber.e(TAG, "openVisibleCamera -> closeVisibleCamera");
//                closeVisibleCamera();;
//            }
//            m_visibleCamera = Camera.open(m_visibleCamIndex);
//            Camera.Parameters parameters = m_visibleCamera.getParameters();
//            /* 指定preview的屏幕大小 */
//            parameters.setPreviewSize(PIXEL_WIDTH, PIXEL_HEIGHT);
//            /* 设置图片分辨率大小 */
//            parameters.setPictureSize(PIXEL_WIDTH, PIXEL_HEIGHT);
//            m_visibleCamera.setParameters(parameters);
//            try {
//                /* setPreviewDisplay唯一的参数为SurfaceHolder */
//                m_visibleCamera.setPreviewDisplay(m_visibleSurfaceHolder);
//                m_visibleCamera.setPreviewCallback(visiblePreviewCallback);
//                m_visibleCamera.startPreview();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//                return -2;
//            }
//        } catch (Exception e) {
//            Timber.w(TAG, "Open Camera Error ! ");
//            return -1;
//        }
//        return 0;
//    }
//
//    public void closeVisibleCamera() {
//        try {
//            if (m_visibleCamera != null) {
//                m_visibleCamera.setPreviewCallback(null) ;
//                m_visibleCamera.stopPreview();
//                m_visibleCamera.release();
//                m_visibleCamera = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Camera.PreviewCallback infraredPreviewCallback = (data, camera) -> {
//        //Timber.e(TAG, "infraredPreviewCallback");
//        this.m_infraredData = data;
//    };
//
//    private Camera.PreviewCallback visiblePreviewCallback = (data, camera) -> {
//        //Timber.e(TAG, "visiblePreviewCallback");
//        this.m_visibleData = data;
//    };
//
//    public void processImage(){
//        //检测人脸信息读写标识
//        if (CommData.getFaceInfoRW() == true)
//        {
//            Timber.e(TAG,"人脸信息读写中");
//            return;
//        }
//        CommData.setCamerasRW(true);
//
//        //数据格式转换
//        int iRGBWidth  = PIXEL_WIDTH;
//        int iRGBHeight = PIXEL_HEIGHT;
//        int[] iDstWidth  = new int[1];
//        int[] iDstHeight = new int[1];
//        byte[] pInfraredImage = new byte[iRGBWidth*iRGBHeight*3];
//        m_imgTool.YUV2RGB(m_infraredData, iRGBWidth, iRGBHeight, pInfraredImage);
//        byte[] pVisibleImage = new byte[iRGBWidth*iRGBHeight*3];
//        m_imgTool.YUV2RGB(m_visibleData, iRGBWidth, iRGBHeight, pVisibleImage);
//        if(m_iCamAngle == 1)//沿x-轴翻转
//        {
//            m_imgTool.ImageFlip(pInfraredImage, iRGBWidth, iRGBHeight,0, pInfraredImage);
//            m_imgTool.ImageFlip(pVisibleImage, iRGBWidth, iRGBHeight,0, pVisibleImage);
//        }
//        else if(m_iCamAngle == 2) //沿y-轴翻转
//        {
//            m_imgTool.ImageFlip(pInfraredImage, iRGBWidth, iRGBHeight,1, pInfraredImage);
//            m_imgTool.ImageFlip(pVisibleImage, iRGBWidth, iRGBHeight,1, pVisibleImage);
//        }
//        else  if(m_iCamAngle == 3) //x、y轴同时翻转
//        {
//            m_imgTool.ImageFlip(pInfraredImage, iRGBWidth, iRGBHeight,-1, pInfraredImage);
//            m_imgTool.ImageFlip(pVisibleImage, iRGBWidth, iRGBHeight,-1, pVisibleImage);
//        }
//        else  if(m_iCamAngle == 4) //顺时针90角度旋转
//        {
//            m_imgTool.ImageRotate(pInfraredImage, iRGBWidth, iRGBHeight,90, pInfraredImage,iDstWidth,iDstHeight);
//            m_imgTool.ImageRotate(pVisibleImage, iRGBWidth, iRGBHeight,90, pVisibleImage,iDstWidth,iDstHeight);
//            iRGBWidth  = iDstWidth[0];
//            iRGBHeight = iDstHeight[0];
//        }
//        else  if(m_iCamAngle == 5) //逆时针90角度旋转
//        {
//            m_imgTool.ImageFlip(pInfraredImage, iRGBWidth, iRGBHeight,1, pInfraredImage);
//            m_imgTool.ImageRotate(pInfraredImage, iRGBWidth, iRGBHeight,90, pInfraredImage,iDstWidth,iDstHeight);
//            m_imgTool.ImageFlip(pVisibleImage, iRGBWidth, iRGBHeight,1, pVisibleImage);
//            m_imgTool.ImageRotate(pVisibleImage, iRGBWidth, iRGBHeight,90, pVisibleImage,iDstWidth,iDstHeight);
//            iRGBWidth  = iDstWidth[0];
//            iRGBHeight = iDstHeight[0];
//        }
//
//        if(DEBUG) {
//            // 刚刚拍照的文件名
//            String fileName =new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";
//            String dir = FileDataUtils.getSDCardPath() + "/Img/";
//            FileDataUtils.AddDirectory(dir);
//            m_imgTool.ImageSave(dir + fileName,pInfraredImage,iRGBWidth,iRGBHeight,(int)3);
//        }
//
//        int[] pFaceNum = new int[1];
//        pFaceNum[0] = MXFaceInfoEx.iMaxFaceNum;
//        MXFaceInfoEx[] pFaceInfo = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
//        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
//            pFaceInfo[i] = new MXFaceInfoEx();
//        }
//
//        int[] pInfraredFaceNum = new int[1];
//        pInfraredFaceNum[0] = MXFaceInfoEx.iMaxFaceNum;
//        MXFaceInfoEx[] pInfraredFaceInfo = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
//        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
//            pInfraredFaceInfo[i] = new MXFaceInfoEx();
//        }
//
////        //可见光摄像头图像用于人脸识别，红外摄像头图像用于活体检测
////        //先进行人脸检测并判断人脸图像质量，符合要求，再进行红外图像人脸检测并判断活体情况，符合要求，则对可见光图像提取特征并用于识别
////        int nRet = m_faceAlgApi.mxDetectFace(pVisibleImage, iRGBWidth, iRGBHeight,pFaceNum,pFaceInfo);
////        if (nRet == 0 && pFaceNum[0] > 0) {
////            //图像质量分数评价
////            m_faceAlgApi.mxFaceQuality(pVisibleImage, iRGBWidth, iRGBHeight, pFaceNum[0], pFaceInfo);
////            if(pFaceInfo[0].quality > 50)//最大人脸的质量大于阈值
////            {
////                //红外摄像头图像用于活体检测
////                nRet = m_faceAlgApi.mxDetectFace(pInfraredImage, iRGBWidth, iRGBHeight,pInfraredFaceNum,pInfraredFaceInfo);
////                if (nRet == 0 && pFaceNum[0] > 0) {
////                    m_faceAlgApi.mxNIRLivenessDetect(pInfraredImage, iRGBWidth, iRGBHeight, pInfraredFaceNum[0], pInfraredFaceInfo);
////                }
////            }
////            //标注检测结果
////            for (int i=0;i<pFaceNum[0];i++) {
////                //人脸框
////                int[] iRect = new int[4];
////                iRect[0] = pFaceInfo[i].x;
////                iRect[1] = pFaceInfo[i].y;
////                iRect[2] = pFaceInfo[i].width;
////                iRect[3] = pFaceInfo[i].height;
////                m_imgTool.DrawRect(pVisibleImage, iRGBWidth, iRGBHeight,iRect,0);
////                //图像质量分数
////                //m_imgTool.DrawText(pVisibleImage, iRGBWidth, iRGBHeight, iRect[0]+10, iRect[1]+30,"Quality:"+pFaceInfo[i].quality,0);
////                //口罩分数
////                if(pInfraredFaceInfo[i].liveness>m_iScore){
////                    m_imgTool.DrawText(pVisibleImage, iRGBWidth, iRGBHeight, iRect[0]+10, iRect[1]+30,"Live",1);
////                }else{
////                    m_imgTool.DrawText(pVisibleImage, iRGBWidth, iRGBHeight, iRect[0]+10, iRect[1]+30,"Attack",0);
////                }
////            }
////        }
//
//        String strInfo = "";
//        int iPointColor = 2;
//        //双目活体检测
//        int nLiveResult = m_faceAlgApi.mxDetectLive(pVisibleImage,pInfraredImage, iRGBWidth, iRGBHeight,pFaceNum,pFaceInfo,pInfraredFaceNum,pInfraredFaceInfo);
//        switch (nLiveResult)
//        {
//            case MXErrorCode.ERR_FACE_LIV_IS_LIVE:
//                strInfo = "活体";
//                iPointColor = 1;
//                break;
//            case MXErrorCode.ERR_FACE_LIV_IS_UNLIVE:
//                strInfo = "非活体";
//                iPointColor = 0;
//                break;
//            case MXErrorCode.ERR_FACE_LIV_VIS_NO_FACE:
//                //strInfo = "可见光输入没有人脸";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_NIS_NO_FACE:
//                strInfo = "近红外输入没有人脸";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_SKIN_FAILED:
//                strInfo = "人脸肤色检测未通过";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_DIST_TOO_CLOSE:
//                strInfo = "请离远一点";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_DIST_TOO_FAR:
//                strInfo = "请离近一点";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_POSE_DET_FAIL:
//                strInfo = "请正对摄像头";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_FACE_CLARITY_DET_FAIL:
//                strInfo = "模糊";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_VIS_EYE_CLOSE:
//                strInfo = "请勿闭眼";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_VIS_MOUTH_OPEN:
//                strInfo = "请勿张嘴";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_VIS_BRIGHTNESS_EXC:
//                strInfo = "过曝";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_VIS_BRIGHTNESS_INS:
//                strInfo = "欠曝";
//                break;
//            case MXErrorCode.ERR_FACE_LIV_VIS_OCCLUSION:
//                strInfo = "遮挡";
//                break;
//            default:
//                strInfo = "其他";
//                break;
//        }
//        SendMsg(SHOW_MSG, strInfo);
//
//        m_imgTool.DrawText(pVisibleImage, iRGBWidth, iRGBHeight, 10, 50, "RGB", 0);
//        m_imgTool.DrawText(pInfraredImage, iRGBWidth, iRGBHeight, 10, 50, "NIR", 0);
//        //标注检测结果
//        for (int i=0;i<pFaceNum[0];i++) {
//            //人脸框
//            int[] iRect = new int[4];
//            iRect[0] = pFaceInfo[i].x;
//            iRect[1] = pFaceInfo[i].y;
//            iRect[2] = pFaceInfo[i].width;
//            iRect[3] = pFaceInfo[i].height;
//            m_imgTool.DrawRect(pVisibleImage, iRGBWidth, iRGBHeight,iRect,iPointColor);
//        }
//
//        for (int i=0;i<pInfraredFaceNum[0];i++) {
//            //人脸框
//            int[] iRect = new int[4];
//            iRect[0] = pInfraredFaceInfo[i].x;
//            iRect[1] = pInfraredFaceInfo[i].y;
//            iRect[2] = pInfraredFaceInfo[i].width;
//            iRect[3] = pInfraredFaceInfo[i].height;
//            m_imgTool.DrawRect(pInfraredImage, iRGBWidth, iRGBHeight,iRect,iPointColor);
//        }
//
//        //显示图像1
//        byte[] pFileDataBuf1 = new byte[iRGBWidth * iRGBHeight * 3+54];
//        int[] iFileDataLen1 = new int[1];
//        m_imgTool.ImageEncode(pInfraredImage, iRGBWidth,iRGBHeight, ".bmp", pFileDataBuf1,iFileDataLen1);
//        m_infraredCamBitmap = BitmapFactory.decodeByteArray(pFileDataBuf1, 0, iFileDataLen1[0]);
//        SendMsg(SHOW_INFRARED_CAM_IMG_MSG, "");
//
//        //显示图像2
//        byte[] pFileDataBuf2 = new byte[iRGBWidth * iRGBHeight * 3+54];
//        int[] iFileDataLen2 = new int[1];
//        m_imgTool.ImageEncode(pVisibleImage, iRGBWidth,iRGBHeight, ".bmp", pFileDataBuf2,iFileDataLen2);
//        m_visibleCamBitmap = BitmapFactory.decodeByteArray(pFileDataBuf2, 0, iFileDataLen2[0]);
//        SendMsg(SHOW_VISIBLE_ICAM_MG_MSG, "");
//
//        CommData.setCamerasRW(false);
//    }
//
//    public void ShowMsgDlg(String msg){
//        new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Message")
//                .setMessage(msg)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //finish();//Exit Activity
//                    }
//                }).create().show();
//    }
//
//    public void SendMsg(final int what, final String msg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (what) {
//                    case SHOW_MSG: {
//                        TextView text_tip = (TextView) findViewById(R.id.text_tip);
//                        text_tip.setText(msg);
//                        break;
//                    }
//                    case SHOW_VISIBLE_ICAM_MG_MSG: {
//                        if (m_visibleCamBitmap!= null) {
//                            ImageView image_show = (ImageView) findViewById(R.id.image_cam_visible);
//                            image_show.setImageBitmap(m_visibleCamBitmap);
//                        }
//                        break;
//                    }
//                    case SHOW_INFRARED_CAM_IMG_MSG: {
//                        if (m_infraredCamBitmap != null) {
//                            ImageView image_show = (ImageView) findViewById(R.id.image_cam_infrared);
//                            image_show.setImageBitmap(m_infraredCamBitmap);
//                        }
//                        break;
//                    }
//                }
//            }
//        });
//    }
}

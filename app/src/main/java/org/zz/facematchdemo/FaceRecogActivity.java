package org.zz.facematchdemo;

import android.app.Activity;

public class FaceRecogActivity extends Activity  {

//    private static final String TAG = "MIAXIS";
//    private static boolean DEBUG = false;
//
//    private static int PIXEL_WIDTH  = 320;//640 //320
//    private static int PIXEL_HEIGHT = 240;//480 //240
//
//    private SurfaceView m_Surfaceview = null;     // SurfaceView对象：(视图组件)视频显示
//    private SurfaceHolder m_SurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
//    private Camera m_Camera = null;               // Camera对象，相机预览
//    private int m_iCamAngle = 0;
//    private int m_iCamIndex = 0;
//
//    private static final int SHOW_CAM_IMG_MSG       = 16;// 显示摄像头图像
//    private static final int PROGRESS_DLG_SHOW_MSG  = 5; // 进度对话框显示
//    private static final int PROGRESS_DLG_HIDE_MSG  = 6; // 进度对话框隐藏
//    Bitmap m_camBitmap     = null;
//
//    MXFaceAPI m_faceAlgApi = null;
//    mxImageTool m_imgTool= null;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
//        setContentView(R.layout.activity_face_recog);
//        /* 以SurfaceView作为相机Preview之用mSurfaceView显示相机,mSurfaceView02显示脸框  */
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
//
//        m_Surfaceview   = (SurfaceView) findViewById(R.id.surfaceview);
//        m_SurfaceHolder = m_Surfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
//        m_SurfaceHolder.addCallback(this);          // SurfaceHolder加入回调接口
//
//        m_faceAlgApi   = MainActivity.GetFaceAPI();
//        m_imgTool      = MainActivity.GetImageTool();
//        Intent intent=getIntent();
//        Bundle bundle=intent.getExtras();
//        m_iCamAngle = bundle.getInt("CamAngle");
//        m_iCamIndex  = bundle.getInt("CamIndex");
//    }
//
//    // 主界面按返回键弹出确认退出对话框
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 按下键盘上返回按钮
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//           // Timber.e(TAG, "onKeyDown KEYCODE_BACK");
//            finish();
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
//
//    public int openCamera(int iCamIndex)
//    {
//        try {
//            if (m_Camera != null) {
//                Timber.e(TAG, "openCamera -> closeCamera");
//                closeCamera();
//            }
//            m_Camera = Camera.open(iCamIndex);
//            Camera.Parameters parameters = m_Camera.getParameters();
//            /* 指定preview的屏幕大小 */
//            parameters.setPreviewSize(PIXEL_WIDTH, PIXEL_HEIGHT);
//            /* 设置图片分辨率大小 */
//            parameters.setPictureSize(PIXEL_WIDTH, PIXEL_HEIGHT);
//            m_Camera.setParameters(parameters);
//            try {
//                /* setPreviewDisplay唯一的参数为SurfaceHolder */
//                m_Camera.setPreviewDisplay(m_SurfaceHolder);
//                m_Camera.setPreviewCallback(this);
//                m_Camera.startPreview();
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
//    public void closeCamera() {
//        try {
//            if (m_Camera != null) {
//                m_Camera.setPreviewCallback(null) ;
//                m_Camera.stopPreview();
//                m_Camera.release();
//                m_Camera = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        Timber.e(TAG, "surfaceCreated");
//        openCamera(m_iCamIndex);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        Timber.e(TAG, "surfaceChanged");
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        Timber.e("MIAXIS", "surfaceDestroyed");
//        closeCamera();
//    }
//
//    @Override
//    public void onPreviewFrame(byte[] data, Camera camera) {
//       // Timber.e(TAG, "onPreviewFrame");
//        //Timber.e(TAG,"mDisplayOrientation="+mDisplayOrientation);
//
//        //检测人脸信息读写标识
//        if (CommData.getFaceInfoRW() == true)
//        {
//            Timber.e(TAG,"人脸信息读写中");
//            return;
//        }
//        //数据格式转换
//        CommData.setCamerasRW(true);
//
//        int iRGBWidth  = PIXEL_WIDTH;
//        int iRGBHeight = PIXEL_HEIGHT;
//        byte[] pRGBImage = new byte[iRGBWidth*iRGBHeight*3];
//        m_imgTool.YUV2RGB(data, iRGBWidth, iRGBHeight, pRGBImage);
//        int[] iDstWidth  = new int[1];
//        int[] iDstHeight = new int[1];
//        if(m_iCamAngle == 1)//沿x-轴翻转
//        {
//            m_imgTool.ImageFlip(pRGBImage, iRGBWidth, iRGBHeight,0, pRGBImage);
//        }
//        else if(m_iCamAngle == 2) //沿y-轴翻转
//        {
//            m_imgTool.ImageFlip(pRGBImage, iRGBWidth, iRGBHeight,1, pRGBImage);
//        }
//        else  if(m_iCamAngle == 3) //x、y轴同时翻转
//        {
//            m_imgTool.ImageFlip(pRGBImage, iRGBWidth, iRGBHeight,-1, pRGBImage);
//        }
//        else  if(m_iCamAngle == 4) //顺时针90角度旋转
//        {
//            m_imgTool.ImageRotate(pRGBImage, iRGBWidth, iRGBHeight,90, pRGBImage,iDstWidth,iDstHeight);
//            iRGBWidth  = iDstWidth[0];
//            iRGBHeight = iDstHeight[0];
//        }
//        else  if(m_iCamAngle == 5) //逆时针90角度旋转
//        {
//            m_imgTool.ImageFlip(pRGBImage, iRGBWidth, iRGBHeight,1, pRGBImage);
//            m_imgTool.ImageRotate(pRGBImage, iRGBWidth, iRGBHeight,90, pRGBImage,iDstWidth,iDstHeight);
//            iRGBWidth  = iDstWidth[0];
//            iRGBHeight = iDstHeight[0];
//        }
//
//        if(DEBUG) {
//            // 刚刚拍照的文件名
//            String fileName =new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";
//            String dir = FileDataUtils.getSDCardPath() + "/Img/";
//            FileDataUtils.AddDirectory(dir);
//            m_imgTool.ImageSave(dir + fileName,pRGBImage,iRGBWidth,iRGBHeight,(int)3);
//        }
//        //人脸检测
//        int[] pFaceNum = new int[1];
//        pFaceNum[0] = MXFaceInfoEx.iMaxFaceNum;
//        MXFaceInfoEx[] pFaceInfo = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
//        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
//            pFaceInfo[i] = new MXFaceInfoEx();
//        }
//        int nRet = m_faceAlgApi.mxDetectFace(pRGBImage, iRGBWidth, iRGBHeight,pFaceNum,pFaceInfo);
//        if (nRet == 0 && pFaceNum[0] > 0) {
//            //已有注册数据，进行人脸搜索
//            if (DataManager.m_iPersonNum > 0) {
//                nRet = m_faceAlgApi.mxSearchAllFace(DataManager.m_pFaceFeaList, DataManager.m_pFaceFeaMaskList, DataManager.m_iPersonNum,
//                        Config.matchThreshold, Config.maskMatchThreshold, Config.qualityThreshold,
//                        pRGBImage, iRGBWidth, iRGBHeight, pFaceNum[0],pFaceInfo);
//                Timber.e(TAG, "mxSearchAllFace,nRet：" +nRet);
//            }
//            //标注检测结果
//            for (int i=0;i<pFaceNum[0];i++) {
//                //人脸框
//                int[] iRect = new int[4];
//                iRect[0] = pFaceInfo[i].x;
//                iRect[1] = pFaceInfo[i].y;
//                iRect[2] = pFaceInfo[i].width;
//                iRect[3] = pFaceInfo[i].height;
//                m_imgTool.DrawRect(pRGBImage, iRGBWidth, iRGBHeight,iRect,0);
//                if (pFaceInfo[i].reCog>0) {
//                    m_imgTool.DrawRect(pRGBImage, iRGBWidth, iRGBHeight, iRect, 1);
//                    Date date = new Date(System.currentTimeMillis());
//                    String strUserId = DataManager.m_strUserIdList.get(pFaceInfo[i].reCogId);
//                    Timber.e(TAG, "搜索到：" + strUserId + "，得分：" + pFaceInfo[i].reCogScore);
//                    m_imgTool.DrawText(pRGBImage, iRGBWidth, iRGBHeight, iRect[0]+10, iRect[1]+30,strUserId,1);
//                }else{
//                    m_imgTool.DrawRect(pRGBImage, iRGBWidth, iRGBHeight,iRect,0);
//                }
//            }
//        }
//        //显示
//        byte[] pFileDataBuf = new byte[iRGBWidth * iRGBHeight * 3+54];
//        int[] iFileDataLen = new int[1];
//        m_imgTool.ImageEncode(pRGBImage, iRGBWidth,iRGBHeight, ".bmp", pFileDataBuf,iFileDataLen);
//        m_camBitmap = BitmapFactory.decodeByteArray(pFileDataBuf, 0, iFileDataLen[0]);
//        SendMsg(SHOW_CAM_IMG_MSG, "");
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
//                    case SHOW_CAM_IMG_MSG: {
//                        if (m_camBitmap != null) {
//                            ImageView image_show = (ImageView) findViewById(R.id.image_cam);
//                            image_show.setImageBitmap(m_camBitmap);
//                        }
//                        break;
//                    }
//                    case PROGRESS_DLG_SHOW_MSG: {
//                        ShowProgressDlg("提示信息",""+msg);
//                        break;
//                    }
//                    case PROGRESS_DLG_HIDE_MSG: {
//                        CancelProgressDlg();
//                        break;
//                    }
//                }
//            }
//        });
//    }
//
//    /* 显示辅助 */
//    private ProgressDialog m_progressDlg = null;
//    private void ShowProgressDlg(String strTitle, String strMsg) {
//        m_progressDlg = ProgressDialog.show(this, strTitle,
//                strMsg, true);
//    }
//
//    private void CancelProgressDlg() {
//        m_progressDlg.cancel();
//    }
}

package org.zz.facematchdemo;

import android.app.Activity;

public class FaceEnrollActivity extends Activity{

//    private static final String TAG = "MIAXIS";
//    private static boolean DEBUG = false;
//
//    private static int PIXEL_WIDTH  = 320;//640 //320
//    private static int PIXEL_HEIGHT = 240;//480 //240
//
//    private SurfaceView   m_Surfaceview = null;     // SurfaceView对象：(视图组件)视频显示
//    private SurfaceHolder m_SurfaceHolder = null;   // SurfaceHolder对象：(抽象接口)SurfaceView支持类
//    private Camera m_Camera = null;                 // Camera对象，相机预览
//    int m_iCamIndex = 0;
//    int m_iCamAngle = 0;
//
//    private static final int SHOW_MSG          		= 0; // 显示信息
//    private static final int SHOW_CAM_IMG_MSG       = 16;// 显示摄像头图像
//    private static final int SHOW_CAPTURE_IMG_MSG   = 17;// 显示采集图像
//    Bitmap m_camBitmap     = null;
//
//    Bitmap m_captureBitmap = null;
//    private boolean m_bCapture = false;
//    private byte[]  m_pCaptureImage = new byte[PIXEL_WIDTH*PIXEL_HEIGHT*3];
//    private int m_iImageWidth       = PIXEL_WIDTH;
//    private int m_iImageHeight      = PIXEL_HEIGHT;
//
//    MXFaceAPI m_faceAlgApi = null;
//    mxImageTool m_imgTool= null;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
//        setContentView(R.layout.activity_face_enroll);
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
//            Log.e(TAG, "onKeyDown KEYCODE_BACK");
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
//                Log.e(TAG, "openCamera -> closeCamera");
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
//            Log.w(TAG, "Open Camera Error ! ");
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
//        Log.e(TAG, "surfaceCreated");
//        openCamera(m_iCamIndex);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        Log.e(TAG, "surfaceChanged");
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        Log.e("MIAXIS", "surfaceDestroyed");
//        closeCamera();
//    }
//
//    @Override
//    public void onPreviewFrame(byte[] data, Camera camera) {
//        //Log.e(TAG, "onPreviewFrame");
//        //Log.e(TAG,"mDisplayOrientation="+mDisplayOrientation);
//
//        //检测人脸信息读写标识
//        if (CommData.getFaceInfoRW() == true)
//        {
//            Log.e(TAG,"人脸信息读写中");
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
//
//        //人脸检测
//        int[] pFaceNum = new int[1];
//        pFaceNum[0] = MXFaceInfoEx.iMaxFaceNum;
//        MXFaceInfoEx[] pFaceInfo = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
//        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
//            pFaceInfo[i] = new MXFaceInfoEx();
//        }
//        int nRet = m_faceAlgApi.mxDetectFace(pRGBImage, iRGBWidth, iRGBHeight,pFaceNum,pFaceInfo);
//        if (nRet == 0 && pFaceNum[0] > 0) {
//            if(pFaceNum[0]==1){
//                //图像质量分数评价
//                m_faceAlgApi.mxFaceQuality4Reg(pRGBImage, iRGBWidth, iRGBHeight, pFaceNum[0], pFaceInfo);
//                if(pFaceInfo[0].quality<Config.quality4RegThreshold ){
//                    SendMsg(SHOW_MSG,getString(R.string.str_tip)+": "+getString(R.string.str_facequality)
//                            +" ["+pFaceInfo[0].quality+"] < "+getString(R.string.str_threshold)+" ["+Config.quality4RegThreshold+"]");
//                }else{
//                    //采集
//                    if (m_bCapture == false) {
//                        SendMsg(SHOW_MSG,getString(R.string.str_capture_qualified_image));
//                        m_bCapture = true;
//                        m_iImageWidth  = iRGBWidth;
//                        m_iImageHeight = iRGBHeight;
//                        System.arraycopy(pRGBImage,0,m_pCaptureImage,0,m_iImageWidth*m_iImageHeight*3);
//                        //显示
//                        byte[] pCaptureFileDataBuf = new byte[m_iImageWidth * m_iImageHeight * 3+54];
//                        int[]  iCaptureFileDataLen = new int[1];
//                        m_imgTool.ImageEncode(m_pCaptureImage, m_iImageWidth,m_iImageHeight, ".bmp", pCaptureFileDataBuf,iCaptureFileDataLen);
//                        m_captureBitmap = BitmapFactory.decodeByteArray(pCaptureFileDataBuf, 0, iCaptureFileDataLen[0]);
//                        SendMsg(SHOW_CAPTURE_IMG_MSG, "");
//                    }
//                    else {
//                        SendMsg(SHOW_MSG,getString(R.string.str_face_capture_enroll));
//                    }
//                }
//            }else{
//                SendMsg(SHOW_MSG,getString(R.string.str_multiple_faces_detect));
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
//                //图像质量分数
//                m_imgTool.DrawText(pRGBImage, iRGBWidth, iRGBHeight, iRect[0]+10, iRect[1]+30,""+pFaceInfo[i].quality,0);
//            }
//        }else {
//                SendMsg(SHOW_MSG,getString(R.string.str_no_face_detect));
//        }
//        //显示
//        byte[] pFileDataBuf = new byte[iRGBWidth * iRGBHeight * 3+54];
//        int[] iFileDataLen = new int[1];
//        m_imgTool.ImageEncode(pRGBImage, iRGBWidth,iRGBHeight, ".bmp", pFileDataBuf,iFileDataLen);
//        m_camBitmap = BitmapFactory.decodeByteArray(pFileDataBuf, 0, iFileDataLen[0]);
//        SendMsg(SHOW_CAM_IMG_MSG, "");
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
//                    case SHOW_CAM_IMG_MSG: {
//                        if (m_camBitmap != null) {
//                            ImageView image_show = (ImageView) findViewById(R.id.image_cam);
//                            image_show.setImageBitmap(m_camBitmap);
//                        }
//                        break;
//                    }
//                    case SHOW_CAPTURE_IMG_MSG: {
//                        if (m_captureBitmap != null) {
//                            ImageView image_capture = (ImageView) findViewById(R.id.image_capture);
//                            image_capture.setImageBitmap(m_captureBitmap);
//                        }
//                        break;
//                    }
//                }
//            }
//        });
//    }
//
//    public void OnClickFaceCapture(View view) {
//        m_bCapture = false;
//    }
//
//    public void OnClickFaceEnroll(View view) {
//        if (m_bCapture == false) {
//            ShowMsgDlg(getString(R.string.str_no_capture_qualified_image));
//            return;
//        }
//        EditText editUserId = (EditText)findViewById(R.id.editUserId);
//        String strUserId = editUserId.getText().toString();
//        strUserId = strUserId.trim(); //去掉空格影响
//        if (strUserId.isEmpty()) {
//            ShowMsgDlg(getString(R.string.str_userid_empty));
//            return;
//        }
//        if (DataManager.IsExistUserId(strUserId)==true) {
//            ShowMsgDlg(getString(R.string.str_userid_exist));
//            return;
//        }
//        int nRet = DataManager.FaceEnroll(m_pCaptureImage,m_iImageWidth,m_iImageHeight,strUserId);
//        if(nRet != 0) {
//            ShowMsgDlg(getString(R.string.btn_face_enroll)+getString(R.string.str_error)+",nRet="+nRet);
//        }else {
//            Intent intent = new Intent();
//            intent.putExtra("UserId", strUserId);
//            setResult(Activity.RESULT_OK, intent);
//            finish();
//        }
//    }
}

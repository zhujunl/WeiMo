package org.zz.facematchdemo;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private static final int SHOW_MSG = 0; // 显示信息
//    private static final int INIT_SUCCESS_MSG = 1; // 初始化成功
//    private static final int INIT_FAILED_MSG = 2; // 初始化失败
//    private static final int BTN_ENABLE_MSG = 3; // 按钮有效
//    private static final int BTN_DISABLE_MSG = 4; // 按钮无效
//    private static final int PROGRESS_DLG_SHOW_MSG = 5; // 进度对话框显示
//    private static final int PROGRESS_DLG_HIDE_MSG = 6; // 进度对话框隐藏
//
//    private static final int SELECT_PIC_FOR_ENROLL = 12; // 选择图像用于注册
//    private static final int SELECT_PIC_FOR_MATCH = 13; // 选择图像用于验证
//    private static final int CAM_FACE_ENROLL_MSG = 14; // 摄像头人脸注册
//    //MXFaceAPI m_faceAlgApi  = null;
//    //mxImageTool m_imgTool  = null;
//    static MXFaceAPI m_faceAlgApi = null;
//    static mxImageTool m_imgTool = null;
//
//    public static MXFaceAPI GetFaceAPI() {
//        return m_faceAlgApi;
//    }
//
//    public static mxImageTool GetImageTool() {
//        return m_imgTool;
//    }
//
//    public static boolean m_bInit = false;
//    private String m_strSDCardPath;
//    private ExecutorService executor = Executors.newSingleThreadExecutor();
//
//    private int m_iCamIndex = 0;
//    private int m_iCamAngle = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //将系统自带的标题栏隐藏掉
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
//
//        m_faceAlgApi = new MXFaceAPI();
//        m_imgTool = new mxImageTool();
//
//        m_strSDCardPath = FileDataUtils.getSDCardPath();
//
//        EnableButton(false);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // 拆开请求只会显示第一个
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.READ_PHONE_STATE,
//                }, 1);
//            }
//        }
//
//        //第一步：获取下拉列表
//        Spinner spinnerIndex = (Spinner) findViewById(R.id.spinnerCamIndex);
//        //第二步：为下拉列表定义一个适配器
//        ArrayAdapter<CharSequence> adapterIndex = ArrayAdapter.createFromResource(this, R.array.camIndex, android.R.layout.simple_spinner_item);
//        //第三步：设置下拉列表下拉时的菜单样式
//        adapterIndex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //第四步：将适配器添加到下拉列表上
//        spinnerIndex.setAdapter(adapterIndex);
//        //第五步：添加监听器，为下拉列表设置事件的响应
//        spinnerIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            //parent就是父控件spinner
//            //view就是spinner内填充的textview,id=@android:id/text1
//            //position是值所在数组的位置
//            //id是值所在行的位置，一般来说与positin一致
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //SendMsg(SHOW_MSG,"Index position="+position+",id="+id);
//                m_iCamIndex = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        //设置默认值
//        spinnerIndex.setSelection(0);
//
//        //第一步：获取下拉列表
//        Spinner spinnerAngle = (Spinner) findViewById(R.id.spinnerCamAngle);
//        //第二步：为下拉列表定义一个适配器
//        ArrayAdapter<CharSequence> adapterAngle = ArrayAdapter.createFromResource(this, R.array.camAngle, android.R.layout.simple_spinner_item);
//        //第三步：设置下拉列表下拉时的菜单样式
//        adapterAngle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //第四步：将适配器添加到下拉列表上
//        spinnerAngle.setAdapter(adapterAngle);
//        //第五步：添加监听器，为下拉列表设置事件的响应
//        spinnerAngle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            //parent就是父控件spinner
//            //view就是spinner内填充的textview,id=@android:id/text1
//            //position是值所在数组的位置
//            //id是值所在行的位置，一般来说与positin一致
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //SendMsg(SHOW_MSG,"Angle position="+position+",id="+id);
//                m_iCamAngle = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        //设置默认值
//        spinnerAngle.setSelection(4);
//    }
//
//    protected void onDestory() {
//        m_faceAlgApi.mxFreeAlg();
//    }
//
//    private void EnableButton(Boolean bFlag) {
//        Button btn;
//        btn = (Button) findViewById(R.id.btn_free);
//        btn.setEnabled(bFlag);
//        btn = (Button) findViewById(R.id.btn_face_enroll);
//        btn.setEnabled(bFlag);
//        btn = (Button) findViewById(R.id.btn_face_match);
//        btn.setEnabled(bFlag);
//        btn = (Button) findViewById(R.id.btn_cam_face_enroll);
//        btn.setEnabled(bFlag);
//        btn = (Button) findViewById(R.id.btn_cam_face_match);
//        btn.setEnabled(bFlag);
//        btn = (Button) findViewById(R.id.btn_liveDetect);
//        btn.setEnabled(bFlag);
//    }
//
//    public void SendMsg(final int what, final String msg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (what) {
//                    case SHOW_MSG: {
//                        showMsg(msg);
//                        break;
//                    }
//                    case PROGRESS_DLG_SHOW_MSG: {
//                        ShowProgressDlg("Message", "" + msg);
//                        break;
//                    }
//                    case PROGRESS_DLG_HIDE_MSG: {
//                        CancelProgressDlg();
//                        break;
//                    }
//                    case BTN_ENABLE_MSG: {
//                        EnableButton(true);
//                        break;
//                    }
//                    case BTN_DISABLE_MSG: {
//                        EnableButton(false);
//                        break;
//                    }
//                    case INIT_SUCCESS_MSG: {
//                        EnableButton(true);
//                        Button btn = (Button) findViewById(R.id.btn_init);
//                        btn.setEnabled(false);
//                        break;
//                    }
//                    case INIT_FAILED_MSG: {
//                        EnableButton(false);
//                        Button btn = (Button) findViewById(R.id.btn_init);
//                        btn.setEnabled(true);
//                        break;
//                    }
//                }
//            }
//        });
//    }
//
//    public static void scrollToBottom(final View scroll, final View inner) {
//        Handler mHandler = new Handler();
//        mHandler.post(new Runnable() {
//            public void run() {
//                if (scroll == null || inner == null) {
//                    return;
//                }
//                int offset = inner.getMeasuredHeight() - scroll.getHeight();
//                if (offset < 0) {
//                    offset = 0;
//                }
//                scroll.scrollTo(0, offset);
//            }
//        });
//    }
//
//    public void showMsg(String msg) {
//        long currentTime = System.currentTimeMillis();
//        //String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
//        String timeNow = new SimpleDateFormat("HH:mm:ss").format(currentTime);
//
//        EditText edit_show_msg = (EditText) findViewById(R.id.edit_show_msg);
//        String strShowMsg = edit_show_msg.getText().toString();
//
//        edit_show_msg.setText(strShowMsg + "[" + timeNow + "]:" + msg + "\r\n");
//
//        // ScrollView移动到最底部
//        ScrollView scrollView_show_msg = (ScrollView) findViewById(R.id.scrollView_show_msg);
//        scrollToBottom(scrollView_show_msg, edit_show_msg);
//    }
//
//    /* 显示辅助 */
//    private ProgressDialog m_progressDlg = null;
//
//    private void ShowProgressDlg(String strTitle, String strMsg) {
//        m_progressDlg = ProgressDialog.show(this, strTitle,
//                strMsg, true);
//    }
//
//    private void CancelProgressDlg() {
//        m_progressDlg.cancel();
//    }
//
//    public String getLicense(Context context, String dstPath) {
//        if (dstPath == null || dstPath == "") {
//            return null;
//        }
//        byte[] szLicenseData = FileDataUtils.ReadData(dstPath);
//        if (szLicenseData == null) {
//            return null;
//        }
//        String lic = new String(szLicenseData);
//        return lic;
//    }
//
//    public void OnClickInitAlg(View view) {
//        executor.execute(this::InitAlg);
//    }
//
//    public void InitAlg() {
//        final Context context = getApplicationContext();
//        SendMsg(SHOW_MSG, getString(R.string.str_load_alg_engine));
//        SendMsg(PROGRESS_DLG_SHOW_MSG, getString(R.string.str_load_alg_engine));
//        String license = getLicense(context, Config.LicensePath);
//        //        if (license == null) {
//        //            SendMsg(SHOW_MSG,getString(R.string.str_no_license_file));
//        //            SendMsg(INIT_FAILED_MSG,"");
//        //            SendMsg(PROGRESS_DLG_HIDE_MSG, "");
//        //            return;
//        //        }
//        int nRet = m_faceAlgApi.mxInitAlg(context, null, license);
//        if (nRet != 0) {
//            String strInfo = getString(R.string.str_error);
//            SendMsg(SHOW_MSG, strInfo + ",nRet=" + nRet);
//            SendMsg(INIT_FAILED_MSG, "");
//        } else {
//            m_bInit = true;
//            //SendMsg(SHOW_MSG,getString(R.string.str_success));
//            SendMsg(INIT_SUCCESS_MSG, "");
//            FileDataUtils.AddDirectory(Config.RootDbPath);
//            FileDataUtils.AddDirectory(Config.ImgDbPath);
//            FileDataUtils.AddDirectory(Config.TzDbPath);
//            FileDataUtils.AddDirectory(Config.TzMaskDbPath);
//        }
//        SendMsg(PROGRESS_DLG_HIDE_MSG, "");
//    }
//
//    public void OnClickFreeAlg(View view) {
//        m_bInit = false;
//        m_faceAlgApi.mxFreeAlg();
//        SendMsg(SHOW_MSG, getString(R.string.str_free_alg_engine));
//        SendMsg(SHOW_MSG, "===========================");
//        SendMsg(INIT_FAILED_MSG, "");
//    }
//
//    private void startImgIntent(int requestCode) {
//        Intent intent;
//        if (Build.VERSION.SDK_INT < 19) {
//            intent = new Intent();
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//            startActivityForResult(Intent.createChooser(intent, "Select"),
//                    requestCode);
//        } else {
//            intent = new Intent(Intent.ACTION_PICK,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            startActivityForResult(Intent.createChooser(intent, "Select"),
//                    requestCode);
//        }
//    }
//
//    public void OnClickFaceEnroll(View view) {
//        if (m_bInit == false) {
//            SendMsg(SHOW_MSG, getString(R.string.str_no_init));
//            return;
//        }
//        startImgIntent(SELECT_PIC_FOR_ENROLL);
//    }
//
//    public void OnClickFaceMatch(View view) {
//        if (m_bInit == false) {
//            SendMsg(SHOW_MSG, getString(R.string.str_no_init));
//            return;
//        }
//        startImgIntent(SELECT_PIC_FOR_MATCH);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("MIAXIS", "resultCode=" + resultCode + ",requestCode="
//                + requestCode);
//        if (resultCode == RESULT_OK) {
//            if (data == null)
//                return;
//            switch (requestCode) {
//                case SELECT_PIC_FOR_ENROLL: {
//                    int[] iMask = new int[1];
//                    Uri tempurl = data.getData();             // 获取选择图片的URI
//                    Context context = getApplicationContext();// 由URL获取绝对路径
//                    String strPathImgFile = UriUnit.getPath(context, tempurl);
//                    SendMsg(SHOW_MSG, "filePath=" + strPathImgFile);
//                    Log.e("MIAXIS", "filePath=" + strPathImgFile);
//                    FaceFeatureExtract(SELECT_PIC_FOR_ENROLL, strPathImgFile, iMask);
//                    break;
//                }
//                case SELECT_PIC_FOR_MATCH: {
//                    int[] iMask = new int[1];
//                    Uri tempurl = data.getData();             // 获取选择图片的URI
//                    Context context = getApplicationContext();// 由URL获取绝对路径
//                    String strPathImgFile = UriUnit.getPath(context, tempurl);
//                    SendMsg(SHOW_MSG, "filePath=" + strPathImgFile);
//                    Log.e("MIAXIS", "filePath=" + strPathImgFile);
//                    if (FaceFeatureExtract(SELECT_PIC_FOR_MATCH, strPathImgFile, iMask) != 0) {
//                        break;
//                    }
//                    int iFeatureSize = m_faceAlgApi.mxGetFeatureSize();
//                    //读取注册特征
//                    String strTzFile = m_strSDCardPath + File.separator + Config.strTzFile1;
//                    long iFileLen = FileDataUtils.getFileSizes(strTzFile);
//                    if (iFileLen != iFeatureSize) {
//                        SendMsg(SHOW_MSG, getString(R.string.str_error));
//                        break;
//                    }
//                    byte[] faceFea = FileDataUtils.ReadData(strTzFile);
//
//                    strTzFile = m_strSDCardPath + File.separator + Config.strTzFile1_mask;
//                    iFileLen = FileDataUtils.getFileSizes(strTzFile);
//                    if (iFileLen != iFeatureSize) {
//                        SendMsg(SHOW_MSG, getString(R.string.str_error));
//                        break;
//                    }
//                    byte[] MaskfaceFea = FileDataUtils.ReadData(strTzFile);
//
//                    //读取比对特征
//                    if (iMask[0] == 0) {
//                        strTzFile = m_strSDCardPath + File.separator + Config.strTzFile2;
//                    } else {
//                        strTzFile = m_strSDCardPath + File.separator + Config.strTzFile2_mask;
//                    }
//                    iFileLen = FileDataUtils.getFileSizes(strTzFile);
//                    if (iFileLen < iFeatureSize) {
//                        SendMsg(SHOW_MSG, getString(R.string.str_error));
//                        break;
//                    }
//                    byte[] TestfaceFea = FileDataUtils.ReadData(strTzFile);
//
//                    float[] fScore = new float[1];
//
//                    if (iMask[0] == 0) {
//                        m_faceAlgApi.mxFeatureMatch(faceFea, TestfaceFea, fScore);
//                    } else {
//                        m_faceAlgApi.mxMaskFeatureMatch(MaskfaceFea, TestfaceFea, fScore);
//                    }
//
//                    String strScore = new DecimalFormat("0.000").format(fScore[0]);
//                    String strInfo1 = getString(R.string.str_match_score);
//                    SendMsg(SHOW_MSG, strInfo1 + ":[" + strScore + "]");
//
//                    SendMsg(SHOW_MSG, "===========================");
//                    break;
//                }
//                case CAM_FACE_ENROLL_MSG: {
//                    Bundle bundle = data.getExtras();
//                    String strUserId = bundle.getString("UserId");
//                    SendMsg(SHOW_MSG, getString(R.string.str_userid) + " [" + strUserId + "] " + getString(R.string.btn_face_enroll) + getString(R.string.str_success));
//                    break;
//                }
//            }
//        }
//    }
//
//    // 人脸特征提取
//    public int FaceFeatureExtract(int what, String strPathImgFile, int[] iMask) {
//        String strInfo;
//        int nRet = 0;
//        Calendar time1, time2;
//        long bt_time;
//        int[] oX = new int[1];
//        int[] oY = new int[1];
//        // 获取图像大小
//        nRet = m_imgTool.ImageLoad(strPathImgFile, 3, null, oX, oY);
//        if (nRet != 1) {
//            strInfo = getString(R.string.str_error);
//            SendMsg(SHOW_MSG, strInfo + ",nRet=" + nRet);
//            return -1;
//        }
//        // 得到图像大小后
//        byte[] pRGBBuff = new byte[oX[0] * oY[0] * 3];
//        nRet = m_imgTool.ImageLoad(strPathImgFile, 3, pRGBBuff, oX, oY);
//        if (nRet != 1) {
//            strInfo = getString(R.string.str_error);
//            SendMsg(SHOW_MSG, strInfo + ",nRet=" + nRet);
//            return -2;
//        }
//        //人脸检测
//        int iWidth = oX[0];
//        int iHeight = oY[0];
//        int[] pFaceNum = new int[1];
//        pFaceNum[0] = MXFaceInfoEx.iMaxFaceNum;
//        MXFaceInfoEx[] pFaceInfo = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
//        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
//            pFaceInfo[i] = new MXFaceInfoEx();
//        }
//        time1 = Calendar.getInstance();
//        nRet = m_faceAlgApi.mxDetectFace(pRGBBuff, iWidth, iHeight, pFaceNum, pFaceInfo);
//        if (nRet != 0) {
//            strInfo = getString(R.string.str_error);
//            SendMsg(SHOW_MSG, strInfo + ",nRet=" + nRet);
//            return -3;
//        }
//        time2 = Calendar.getInstance();
//        bt_time = time2.getTimeInMillis() - time1.getTimeInMillis();
//
//        strInfo = getString(R.string.str_face_num);
//        SendMsg(SHOW_MSG, strInfo + ":" + pFaceNum[0]);
//        strInfo = getString(R.string.str_facedetect) + " " + getString(R.string.str_runtime);
//        SendMsg(SHOW_MSG, strInfo + ":[" + bt_time + "]ms");
//
//        //人脸图像质量评价
//        time1 = Calendar.getInstance();
//        m_faceAlgApi.mxFaceQuality(pRGBBuff, iWidth, iHeight, pFaceNum[0], pFaceInfo);
//        time2 = Calendar.getInstance();
//        bt_time = time2.getTimeInMillis() - time1.getTimeInMillis();
//        strInfo = getString(R.string.str_facequality) + " " + getString(R.string.str_runtime);
//        SendMsg(SHOW_MSG, strInfo + ":[" + bt_time + "]ms");
//
//        //口罩检测
//        m_faceAlgApi.mxMaskDetect(pRGBBuff, iWidth, iHeight, pFaceNum[0], pFaceInfo);
//
//        //特征提取
//        int iFeatureSize = m_faceAlgApi.mxGetFeatureSize();
//        byte[] pFeatureBuf = new byte[iFeatureSize * pFaceNum[0]];
//        time1 = Calendar.getInstance();
//        nRet = m_faceAlgApi.mxFeatureExtract(pRGBBuff, iWidth, iHeight, pFaceNum[0], pFaceInfo, pFeatureBuf);
//        if (nRet != 0) {
//            strInfo = getString(R.string.str_error);
//            SendMsg(SHOW_MSG, strInfo + ",nRet=" + nRet);
//            return -4;
//        }
//        time2 = Calendar.getInstance();
//        bt_time = time2.getTimeInMillis() - time1.getTimeInMillis();
//        strInfo = getString(R.string.str_featureextract) + " " + getString(R.string.str_runtime);
//        SendMsg(SHOW_MSG, strInfo + ":[" + bt_time + "]ms");
//
//        //保存特征
//        //        String strFileName;
//        //        if(what==SELECT_PIC_FOR_ENROLL){
//        //            strFileName = m_strSDCardPath + File.separator + Config.strTzFile1;
//        //        }else{
//        //            strFileName = m_strSDCardPath + File.separator + Config.strTzFile2;
//        //        }
//        //        FileDataUtils.saveData(strFileName, pFeatureBuf, iFeatureSize * pFaceNum[0]);
//
//        //保存最大人脸特征
//        int maxIndex = 0;
//        int maxEyeDistance = 0;
//        for (int i = 0; i < pFaceNum[0]; i++) {
//            if (pFaceInfo[i].eyeDistance > maxEyeDistance) {
//                maxEyeDistance = pFaceInfo[i].eyeDistance;
//                maxIndex = i;
//            }
//        }
//        //标准算法
//        String strFileName;
//        if (what == SELECT_PIC_FOR_ENROLL) {
//            strFileName = m_strSDCardPath + File.separator + Config.strTzFile1;
//        } else {
//            strFileName = m_strSDCardPath + File.separator + Config.strTzFile2;
//        }
//        byte[] pMaxFaceFeatureBuf = new byte[iFeatureSize];
//        System.arraycopy(pFeatureBuf, maxIndex * iFeatureSize, pMaxFaceFeatureBuf, 0, iFeatureSize);
//        FileDataUtils.saveData(strFileName, pMaxFaceFeatureBuf, iFeatureSize);
//
//        //特征提取（戴口罩算法）
//        byte[] pMaskFeatureBuf = new byte[iFeatureSize * pFaceNum[0]];
//        time1 = Calendar.getInstance();
//        nRet = m_faceAlgApi.mxMaskFeatureExtract(pRGBBuff, iWidth, iHeight, pFaceNum[0], pFaceInfo, pMaskFeatureBuf);
//        if (what == SELECT_PIC_FOR_ENROLL) {
//            strFileName = m_strSDCardPath + File.separator + Config.strTzFile1_mask;
//        } else {
//            strFileName = m_strSDCardPath + File.separator + Config.strTzFile2_mask;
//        }
//        byte[] pMaxMaskFaceFeatureBuf = new byte[iFeatureSize];
//        System.arraycopy(pMaskFeatureBuf, maxIndex * iFeatureSize, pMaxMaskFaceFeatureBuf, 0, iFeatureSize);
//        FileDataUtils.saveData(strFileName, pMaxMaskFaceFeatureBuf, iFeatureSize);
//
//        if (pFaceInfo[maxIndex].mask > 40) {
//            iMask[0] = 1;
//        } else {
//            iMask[0] = 0;
//        }
//
//        //标注检测结果
//        for (int i = 0; i < pFaceNum[0]; i++) {
//            //人脸框
//            int[] iRect = new int[4];
//            iRect[0] = pFaceInfo[i].x;
//            iRect[1] = pFaceInfo[i].y;
//            iRect[2] = pFaceInfo[i].width;
//            iRect[3] = pFaceInfo[i].height;
//            m_imgTool.DrawRect(pRGBBuff, iWidth, iHeight, iRect, 0);
//            //关键点
//            //            int[] iPointPos = new int[10];
//            //            for(int j=0;j<5;j++)
//            //            {
//            //                iPointPos[2*j]   = pFaceInfo[i].keypt_x[j];
//            //                iPointPos[2*j+1] = pFaceInfo[i].keypt_y[j];
//            //            }
//            //            m_imgTool.DrawPoint(pRGBBuff, iWidth, iHeight,iPointPos,5,0);
//            //            //图像质量分数
//            //            m_imgTool.DrawText(pRGBBuff, iWidth, iHeight, iRect[0]+20, iRect[1]+30,"Score:"+pFaceInfo[i].quality,0);
//            //            //口罩
//            //            if(pFaceInfo[i].mask>40){
//            //                m_imgTool.DrawText(pRGBBuff, iWidth, iHeight, iRect[0]+20, iRect[1]+60,"Mask",2);
//            //            }else{
//            //                m_imgTool.DrawText(pRGBBuff, iWidth, iHeight, iRect[0]+20, iRect[1]+60,"No",0);
//            //            }
//        }
//        //保存检测结果图像
//        if (what == SELECT_PIC_FOR_ENROLL) {
//            strFileName = m_strSDCardPath + File.separator + Config.strImgFile1;
//        } else {
//            strFileName = m_strSDCardPath + File.separator + Config.strImgFile2;
//        }
//        m_imgTool.ImageSave(strFileName, pRGBBuff, iWidth, iHeight, 3);
//
//        //缩放显示
//        int iDstImgWidth = 128;
//        int iDstImgHeight = (int) (iHeight * iDstImgWidth / iWidth);
//        byte[] pDstRGBBuff = new byte[iDstImgWidth * iDstImgHeight * 3];
//        m_imgTool.Zoom(pRGBBuff, iWidth, iHeight, 3, iDstImgWidth, iDstImgHeight, pDstRGBBuff);
//        byte[] pFileDataBuf = new byte[iDstImgWidth * iDstImgHeight * 3 + 54];
//        int[] iFileDataLen = new int[1];
//        nRet = m_imgTool.ImageEncode(pDstRGBBuff, iDstImgWidth, iDstImgHeight, ".bmp", pFileDataBuf, iFileDataLen);
//        if (nRet != 1) {
//            strInfo = getString(R.string.str_error);
//            SendMsg(SHOW_MSG, strInfo + ",nRet=" + nRet);
//            return -5;
//        }
//        Bitmap bm = BitmapFactory.decodeByteArray(pFileDataBuf, 0, iFileDataLen[0]);
//        if (bm != null) {
//            ImageView image_show;
//            if (what == SELECT_PIC_FOR_ENROLL)
//                image_show = (ImageView) findViewById(R.id.image_show1);
//            else
//                image_show = (ImageView) findViewById(R.id.image_show2);
//            image_show.setImageBitmap(bm);
//        }
//        return 0;
//    }
//
//    public void OnClickCamFaceEnroll(View view) {
//        if (m_bInit == false) {
//            SendMsg(SHOW_MSG, getString(R.string.str_no_init));
//            return;
//        }
//        Intent intent = new Intent(this, FaceEnrollActivity.class);
//        intent.putExtra("CamAngle", m_iCamAngle);
//        intent.putExtra("CamIndex", m_iCamIndex);
//        startActivityForResult(intent, CAM_FACE_ENROLL_MSG);
//    }
//
//    public void OnClickCamFaceMatch(View view) {
//        Log.e("miaxis", "加载人脸库，开启动态人脸识别");
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(this::loadDb4FaceRecogThread);
//    }
//
//    /**
//     * 加载人脸库，开启动态人脸识别
//     */
//    public void loadDb4FaceRecogThread() {
//        // 加载人脸库
//        SendMsg(PROGRESS_DLG_SHOW_MSG, getString(R.string.str_loading_dataset));
//        DataManager.LoadTzDb();
//        //Log.e(TAG, "注册人员总数："+DataManager.m_iPersonNum);
//        SendMsg(SHOW_MSG, getString(R.string.str_enroll_personnum) + ": " + DataManager.m_iPersonNum);
//        MySleep(500);
//        SendMsg(PROGRESS_DLG_HIDE_MSG, "");
//        // 开启动态人脸识别
//        Intent intent = new Intent(this, FaceRecogActivity.class);
//        intent.putExtra("CamAngle", m_iCamAngle);
//        intent.putExtra("CamIndex", m_iCamIndex);
//        startActivity(intent);
//    }
//
//    /**
//     * 延时
//     */
//    public static void MySleep(int iTimeout) {
//        long duration = -1;
//        Calendar time1, time2;
//        time1 = Calendar.getInstance();
//        while (duration <= iTimeout) {
//            time2 = Calendar.getInstance();
//            duration = time2.getTimeInMillis() - time1.getTimeInMillis();
//        }
//    }
//
//    public void OnClickLiveDetect(View view) {
//        if (m_bInit == false) {
//            SendMsg(SHOW_MSG, getString(R.string.str_no_init));
//            return;
//        }
//        Intent intent = new Intent(this, LiveDetectActivity.class);
//        intent.putExtra("CamAngle", m_iCamAngle);
//        intent.putExtra("CamIndex", m_iCamIndex);
//        startActivity(intent);
//
//    }
//
//    public void OnClickAlgVersion(View view) {
//        SendMsg(SHOW_MSG, m_faceAlgApi.mxAlgVersion());
//    }
}

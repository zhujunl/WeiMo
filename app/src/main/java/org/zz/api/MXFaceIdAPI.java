
package org.zz.api;

import android.content.Context;

import com.miaxis.mr230m.util.ArrayUtils;
import com.miaxis.mr230m.util.ListUtils;

import org.zz.jni.JustouchFaceApi;

import java.util.ArrayList;
import java.util.List;


public class MXFaceIdAPI {

    private static final String TAG = "MXFaceIdAPI";
    private boolean m_bInit = false;
    private final JustouchFaceApi mJustouchFaceApi = new JustouchFaceApi();
    private int[] FaceData_Rgb;
    private MXFaceInfoEx[] FaceInfo_Rgb;

    private int[] FaceData_Nir;
    private MXFaceInfoEx[] FaceInfo_Nir;
    public final int FaceQuality = 70;
    public final int FaceLive = 65;
    public final int FaceMinWidth = 50;
    public final float FaceMatch = 0.73001F;


    private MXFaceIdAPI() {
    }

    public static MXFaceIdAPI getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final MXFaceIdAPI instance = new MXFaceIdAPI();
    }

    /*
      ================================ 静态内部类单例 ================================
     */

    /**
     * @param
     * @return algorithm version
     * @author chen.gs
     * @category algorithm version
     */
    public MXResult<String> mxAlgVersion() {
        return MXResult.CreateSuccess(this.mJustouchFaceApi.getAlgVersion());
    }

    /**
     * @param context -  input，context handle
     *                szModelPath    -  input，model path
     *                szLicense      -  input，authorization code
     * @return 0-success, others-failure
     * @author chen.gs
     * @category initialization algorithm
     */
    public MXResult<?> mxInitAlg(Context context, String szModelPath, String szLicense) {
        int nRet = this.mJustouchFaceApi.initAlg(context, szModelPath, szLicense);
        if (nRet != 0) {
            return MXResult.CreateFail(nRet, null);
        }
        this.FaceData_Rgb = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        this.FaceInfo_Rgb = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
            this.FaceInfo_Rgb[i] = new MXFaceInfoEx();
        }
        this.FaceData_Nir = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        this.FaceInfo_Nir = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
            this.FaceInfo_Nir[i] = new MXFaceInfoEx();
        }
        this.m_bInit = true;
        return MXResult.CreateSuccess();
    }

    /**
     * 获取最大人脸
     */
    public MXFace getMaxFace(List<MXFace> list) {
        if (ListUtils.isNullOrEmpty(list)) {
            return null;
        }
        MXFace temp = null;
        for (MXFace face : list) {
            temp = temp == null ? face : (face != null && (temp.getFaceRect().width()
                    * temp.getFaceRect().height() > face.getFaceRect().width()
                    * face.getFaceRect().height()) ? temp : face);
        }
        return temp;
    }


    /**
     * @param
     * @return 0-success, others-failure
     * @author chen.gs
     * @category release algorithm
     */
    public void mxFreeAlg() {
        if (this.m_bInit) {
            this.mJustouchFaceApi.freeAlg();
        }
        this.FaceData_Rgb = null;
        this.FaceInfo_Rgb = null;
        this.FaceData_Nir = null;
        this.FaceInfo_Nir = null;
        this.m_bInit = false;
    }

    /**
     * @param pImage - input, BGR image data
     *               nWidth    - input, image width
     *               nHeight   - input, image height
     *               pFaceNum  - input/output，number of faces
     *               pFaceInfo - output, face information, see MXFaceInfoEx structure
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face detection for still image detection
     */
    public synchronized MXResult<List<MXFace>> mxDetectFace(byte[] pImage, int nWidth, int nHeight) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        int[] pFaceNum = new int[1];
        int nRet = this.mJustouchFaceApi.detectFace(pImage, nWidth, nHeight, pFaceNum, this.FaceData_Rgb);
        if (nRet != 0) {
            pFaceNum[0] = 0;
            return MXResult.CreateFail(nRet, Msg_Illegal_Detect_Face_Error);
        }
        MXFaceInfoEx.Ints2MXFaceInfoExs(pFaceNum[0], this.FaceData_Rgb, this.FaceInfo_Rgb);
        List<MXFace> infoList = new ArrayList<>();
        for (int i = 0; i < pFaceNum[0]; i++) {
            MXFaceInfoEx mxFaceInfoEx = this.FaceInfo_Rgb[i];
            if (mxFaceInfoEx.width >= this.FaceMinWidth) {
                int[] info = new int[MXFaceInfoEx.SIZE];
                System.arraycopy(this.FaceData_Rgb, MXFaceInfoEx.SIZE * i, info, 0, MXFaceInfoEx.SIZE);
                infoList.add(new MXFace(info, mxFaceInfoEx));
            }
        }
        return infoList.isEmpty() ? MXResult.CreateFail(Code_Illegal_NO_Face, Msg_Illegal_NO_Face) : MXResult.CreateSuccess(infoList);
    }

    public int mxDetectFace(byte[] pImage, int nWidth, int nHeight,
                            int[] pFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (m_bInit!=true){
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        int nRet = this.mJustouchFaceApi.detectFace(pImage, nWidth, nHeight,pFaceNum,bInfo);
        if(nRet!=0){
            pFaceNum[0] = 0;
            return nRet;
        }
        MXFaceInfoEx.Int2MXFaceInfoEx(pFaceNum[0],bInfo,pFaceInfo);
        return 0;
    }



    public static int Code_Illegal_Face_Init = -200;
    public static String Msg_Illegal_Face_Init = "人脸算法初始化失败";
    public static int Code_Illegal_NO_Face = -201;
    public static String Msg_Illegal_NO_Face = "未检测到人脸";
    public static int Code_Illegal_Detect_Face_Error = -202;
    public static String Msg_Illegal_Detect_Face_Error = "人脸检测失败";

    public MXResult<List<MXFace>> mxDetectFaceNir(byte[] pImage, int nWidth, int nHeight) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        int[] pFaceNum = new int[1];
        int nRet = this.mJustouchFaceApi.detectFace(pImage, nWidth, nHeight, pFaceNum, this.FaceData_Nir);
        if (nRet != 0) {
            pFaceNum[0] = 0;
            return MXResult.CreateFail(nRet, "人脸检测失败");
        }
        MXFaceInfoEx.Ints2MXFaceInfoExs(pFaceNum[0], this.FaceData_Nir, this.FaceInfo_Nir);
        List<MXFace> infoList = new ArrayList<>();
        for (int i = 0; i < pFaceNum[0]; i++) {
            MXFaceInfoEx mxFaceInfoEx = this.FaceInfo_Nir[i];
            if (mxFaceInfoEx.width >= this.FaceMinWidth) {
                int[] info = new int[MXFaceInfoEx.SIZE];
                System.arraycopy(this.FaceData_Nir, MXFaceInfoEx.SIZE * i, info, 0, MXFaceInfoEx.SIZE);
                infoList.add(new MXFace(info, mxFaceInfoEx));
            }
        }
        return infoList.isEmpty() ? MXResult.CreateFail(nRet, "没有人脸") : MXResult.CreateSuccess(infoList);
    }

    /**
     * @param pImage - input, RGB image data
     *               nWidth       - input, image width
     *               nHeight      - input, image height
     *               nFaceNum     - input, number of faces
     *               pFaceInfo    - input, face information, see MXFaceInfoEx structure
     *               pFaceFeature - output, face features, feature length * number of faces
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face feature extraction
     */
    public MXResult<byte[]> mxFeatureExtract(byte[] pImage, int nWidth, int nHeight, MXFace mxFace) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        byte[] feature = new byte[mxGetFeatureSize()];
        int nRet = mJustouchFaceApi.featureExtract(pImage, nWidth, nHeight, 1, mxFace.getFaceData(), feature);
        if (nRet != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_FACE_EXTRACT, "人脸特征提取失败");
        }
        return MXResult.CreateSuccess(feature);
    }

    public int mxFeatureExtract(byte[] pImage, int nWidth, int nHeight,
                                int nFaceNum, MXFaceInfoEx[] pFaceInfo, byte[] pFaceFeature)
    {
        if (m_bInit!=true){
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoEx2Int(nFaceNum,bInfo,pFaceInfo);
        int nRet = mJustouchFaceApi.featureExtract(pImage, nWidth, nHeight,nFaceNum, bInfo,pFaceFeature);
        if(nRet!=0){
            return MXErrorCode.ERR_FACE_EXTRACT;
        }
        return nRet;
    }

    /**
     * @param pFaceFeatureA - input, face feature A
     *                      pFaceFeatureB - input, face feature B
     *                      fScore  - Output, similarity measure(0 ~ 1.0), the bigger the more similar, recommended threshold: 0.76
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face feature match
     */
    public MXResult<Float> mxFeatureMatch(byte[] pFaceFeatureA, byte[] pFaceFeatureB) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        int featureSize = mxGetFeatureSize();
        if (!ArrayUtils.isLength(pFaceFeatureA, featureSize) || !ArrayUtils.isLength(pFaceFeatureB, featureSize)) {
            return MXResult.CreateFail(MXErrorCode.ERR_FEATURE_EMPTY, "特征长度非法");
        }
        float[] fScore = new float[1];
        int match = mJustouchFaceApi.featureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
        if (match != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_FACE_MATCH, "人脸比对失败");
        }
        return MXResult.CreateSuccess(fScore[0]);
    }

    /**
     * @param pImage - input, RGB image width
     *               nWidth        - input, image width
     *               nHeight       - input, image height
     *               nFaceNum    	- input, number of faces
     *               pFaceInfo     - input/output, obtained through quality attribute of MXFaceInfoEx structure, recommended threshold: 50
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face image quality evaluation based on face detection results
     */
    public MXResult<Integer> mxFaceQuality(byte[] pImage, int nWidth, int nHeight, MXFace mxFace) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        int nRet = mJustouchFaceApi.faceQuality(pImage, nWidth, nHeight, 1, mxFace.getFaceData());
        if (nRet != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_QUALITY, "图像质量检测失败");
        }
        MXFaceInfoEx.Int2MXFaceInfoEx(mxFace.getFaceData(), mxFace.getFaceInfo());
        return MXResult.CreateSuccess(mxFace.getFaceInfo().quality);
    }

    /**
     * @param pImage - input, near infrared face image
     *               nWidth  	    - input, image width
     *               nHeight    	- input, image height
     *               nFaceNum    	- input, number of faces
     *               pFaceInfo 	- input/output, obtained through liveness attribute of MXFaceInfoEx structure, recommended threshold: 80
     * @return 0-success, others-failure
     * @author chen.gs
     * @category live detection of near infrared face image (the best effect of the camera of the specified model)
     */
    public MXResult<Integer> mxNIRLiveDetect(byte[] pImage, int nWidth, int nHeight, MXFace mxFace) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        int nRet = mJustouchFaceApi.nirLivenessDetect(pImage, nWidth, nHeight, 1, mxFace.getFaceData());
        if (nRet != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_FACE_LIV, "活体检测失败");
        }
        MXFaceInfoEx.Int2MXFaceInfoEx(mxFace.getFaceData(), mxFace.getFaceInfo());
        return MXResult.CreateSuccess(mxFace.getFaceInfo().liveness);
    }

    /**
     * @return 10000-活体，10001-假体，其他-图像质量不满足
     * @author chen.gs
     * @category 双目摄像头活体检测
     * @paramp pRgbImage        - 输入，可见光摄像头的图像数据
     * pNirImage		- 输入，近红外摄像头的图像数据
     * ImgWidth		- 输入，图像宽度
     * nImgHeight		- 输入，图像高度
     * pRgbFaceNum		- 输出，可见光摄像头图像的人脸检出数
     * pRgbFaceInfo	- 输出，可见光摄像头图像的人脸信息
     * pNirFaceNum		- 输出，近红外摄像头图像的人脸检出数
     * pNirFaceInfo	- 输出，近红外摄像头图像的人脸信息
     */
    public MXResult<List<MXFace>> detectLive(byte[] pRgbImage, int nImgWidth, int nImgHeight, byte[] pNirImage) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pRgbImage, nImgWidth, nImgHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        parameter = checkParameter(pNirImage, nImgWidth, nImgHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        int[] faceNumber_rgb = new int[1];
        int[] faceNumber_nir = new int[1];
        int nRet = this.mJustouchFaceApi.detectLive(pRgbImage, pNirImage, nImgWidth, nImgHeight, faceNumber_rgb, this.FaceData_Rgb, faceNumber_nir, this.FaceData_Rgb);
        return liveResultFormat(nRet, faceNumber_rgb, faceNumber_nir);
    }

    public MXResult<?> mxRGBLiveDetect(byte[] pImage, int nWidth, int nHeight, MXFace mxFace) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        MXResult<Integer> rgbResult = this.mxFaceQuality(pImage, nWidth, nHeight, mxFace);
        if (!MXResult.isSuccess(rgbResult)) {
            return MXResult.CreateFail(rgbResult.getCode(), rgbResult.getMsg());
        }
        //2.可见光质量评价
        if (rgbResult.getData() < this.FaceQuality) {
            //return MXResult.CreateFail(-70, "人脸质量过低，值:" + rgbResult.getData());
            return MXResult.CreateFail(-70, "人脸质量过低");
        }
        //2.1 大姿态
        MXFaceInfoEx rgbFaceInfo = mxFace.getFaceInfo();
        if (rgbFaceInfo.pitch > 20 || rgbFaceInfo.roll > 20 || rgbFaceInfo.yaw > 20) {
            return MXResult.CreateFail(-71, "姿态过大");
        }
        //2.2 距离
        if (rgbFaceInfo.eyeDistance < 15) {
            return MXResult.CreateFail(-72, "请不要离镜头太远");
        }
        if (rgbFaceInfo.eyeDistance > 150) {
            return MXResult.CreateFail(-73, "请不要离镜头太近");
        }
        //2.3 眼睛遮挡
        int max_eye_occlusion = 0;
        int occlusion_lefteye = rgbFaceInfo.keypt_x[112];
        int occlusion_righteye = rgbFaceInfo.keypt_x[113];
        max_eye_occlusion = occlusion_lefteye;
        if (max_eye_occlusion < occlusion_righteye) {
            max_eye_occlusion = occlusion_righteye;
        }
        if (max_eye_occlusion > 60) {
            return MXResult.CreateFail(-74, "请勿遮挡");
        }
        //2.5 肤色
        int skin = rgbFaceInfo.keypt_y[113];
        if (skin < 36) {
            return MXResult.CreateFail(-75, "肤色检测不通过");
        }
        return MXResult.CreateSuccess();
    }

    /**
     * @param pImage - input, RGB image width
     *               nWidth        - input, image width
     *               nHeight       - input, image height
     *               nFaceNum    	- input, number of faces
     *               pFaceInfo 	- input/output, obtained through mask attribute of MXFaceInfoEx structure, recommended threshold: 40
     * @return 0-success, others-failure
     * @author chen.gs
     * @category Detect whether a face is wearing a mask
     */
    public MXResult<Integer> mxMaskDetect(byte[] pImage, int nWidth, int nHeight, MXFace mxFace) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        int nRet = mJustouchFaceApi.maskDetect(pImage, nWidth, nHeight, 1, mxFace.getFaceData());
        if (nRet != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_FACE_MASK, "口罩检测失败");
        }
        MXFaceInfoEx.Int2MXFaceInfoEx(mxFace.getFaceData(), mxFace.getFaceInfo());
        return MXResult.CreateSuccess(mxFace.getFaceInfo().mask);
    }

    /**
     * @param pImage - input, RGB image data
     *               nWidth       - input, image width
     *               nHeight      - input, image height
     *               nFaceNum     - input, number of faces
     *               pFaceInfo    - input, face information, see MXFaceInfoEx structure
     *               pFaceFeature - output, face features, feature length * number of faces
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face feature extraction (mask algorithm)
     */
    public MXResult<byte[]> mxMaskFeatureExtract(byte[] pImage, int nWidth, int nHeight, MXFace mxFace) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        byte[] feature = new byte[mxGetFeatureSize()];
        int nRet = mJustouchFaceApi.maskFeatureExtract(pImage, nWidth, nHeight, 1, mxFace.getFaceData(), feature);
        if (nRet != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_FACE_MASK_EXTRACT, "人脸特征提取失败(带口罩)");
        }
        return MXResult.CreateSuccess(feature);
    }

    /**
     * @param pImage - input, RGB image data
     *               nWidth       - input, image width
     *               nHeight      - input, image height
     *               nFaceNum     - input, number of faces
     *               pFaceInfo    - input, face information, see MXFaceInfoEx structure
     *               pFaceFeature - output, face features, feature length * number of faces
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face feature extraction for registration(mask algorithm)
     */
    public MXResult<byte[]> mxMaskFeatureExtract4Reg(byte[] pImage, int nWidth, int nHeight, MXFace mxFace) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        MXResult<?> parameter = checkParameter(pImage, nWidth, nHeight);
        if (!MXResult.isSuccess(parameter)) {
            return MXResult.CreateFail(parameter);
        }
        byte[] feature = new byte[mxGetFeatureSize()];
        int nRet = mJustouchFaceApi.maskFeatureExtract4Reg(pImage, nWidth, nHeight, 1, mxFace.getFaceData(), feature);
        if (nRet != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_FACE_MASK_REG, "提取人脸特征失败(带口罩)");
        }
        return MXResult.CreateSuccess(feature);
    }

    /**
     * @param pFaceFeatureA - input, face feature A
     *                      pFaceFeatureB - input, face feature B
     *                      fScore  - Output, similarity measure(0 ~ 1.0), the bigger the more similar, recommended threshold: 0.73
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face feature match (mask algorithm)
     */
    public MXResult<Float> mxMaskFeatureMatch(byte[] pFaceFeatureA, byte[] pFaceFeatureB) {
        if (!this.m_bInit) {
            return MXResult.CreateFail(MXErrorCode.ERR_NO_INIT, "未初始化");
        }
        float[] fScore = new float[1];
        int match = mJustouchFaceApi.maskFeatureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
        if (match != 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_FACE_MASK_MATCH, "特征(带口罩)匹配失败");
        }
        return MXResult.CreateSuccess(fScore[0]);
    }

    /**
     * @param
     * @return face feature length
     * @author chen.gs
     * @category get face feature length
     */
    public int mxGetFeatureSize() {
        int iFeaLen = 0;
        if (m_bInit) {
            iFeaLen = mJustouchFaceApi.getFeatureSize();
        }
        return iFeaLen;
    }

    /**
     * 校验参数是否合法
     */
    private MXResult<?> checkParameter(byte[] pImage, int nWidth, int nHeight) {
        if (pImage == null || pImage.length == 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_BUFFER_EMPTY, "数据为空");
        }
        if (nWidth <= 0 || nHeight <= 0) {
            return MXResult.CreateFail(MXErrorCode.ERR_IMAGE_SIZE_ILLEGAL, "图像尺寸不合法");
        }
        return MXResult.CreateSuccess();
    }


    /**
     * 活体检测返回值格式化
     */
    private MXResult<List<MXFace>> liveResultFormat(int result, int[] faceNumber_rgb, int[] faceNumber_nir) {
        if (ArrayUtils.isNullOrEmpty(faceNumber_rgb) || ArrayUtils.isNullOrEmpty(faceNumber_nir)) {
            return MXResult.CreateFail(-1, "参数错误");
        }
        //活体检测返回值
        //public static final int ERR_FACE_LIV_IS_LIVE = 10000;    //活体
        //public static final int ERR_FACE_LIV_IS_UNLIVE = 10001;    //非活体
        //public static final int ERR_FACE_LIV_VIS_NO_FACE = 10002;    //可见光输入没有人脸
        //public static final int ERR_FACE_LIV_NIS_NO_FACE = 10003;    //近红外输入没有人脸
        //public static final int ERR_FACE_LIV_SKIN_FAILED = 10004;    //人脸肤色检测未通过
        //public static final int ERR_FACE_LIV_DIST_TOO_CLOSE = 10005;    //请离远一点
        //public static final int ERR_FACE_LIV_DIST_TOO_FAR = 10006;    //请离近一点
        //public static final int ERR_FACE_LIV_POSE_DET_FAIL = 10007;    //请正对摄像头
        //public static final int ERR_FACE_LIV_FACE_CLARITY_DET_FAIL = 10008;    //模糊
        //public static final int ERR_FACE_LIV_VIS_EYE_CLOSE = 10009;    //请勿闭眼
        //public static final int ERR_FACE_LIV_VIS_MOUTH_OPEN = 10010;    //请勿张嘴
        //public static final int ERR_FACE_LIV_VIS_BRIGHTNESS_EXC = 10011;    //过曝
        //public static final int ERR_FACE_LIV_VIS_BRIGHTNESS_INS = 10012;   //欠曝
        //public static final int ERR_FACE_LIV_VIS_OCCLUSION = 10013;   //遮挡
        switch (result) {
            case 10000:
                if (faceNumber_rgb[0] > 0) {
                    MXFaceInfoEx.Ints2MXFaceInfoExs(faceNumber_rgb[0], this.FaceData_Rgb, this.FaceInfo_Rgb);
                }
                if (faceNumber_nir[0] > 0) {
                    MXFaceInfoEx.Ints2MXFaceInfoExs(faceNumber_nir[0], this.FaceData_Nir, this.FaceInfo_Nir);
                }
                List<MXFace> infoList = new ArrayList<>();
                for (int i = 0; i < faceNumber_rgb[0]; i++) {
                    int[] info = new int[MXFaceInfoEx.SIZE];
                    System.arraycopy(this.FaceData_Rgb, MXFaceInfoEx.SIZE * i, info, 0, MXFaceInfoEx.SIZE);
                    infoList.add(new MXFace(info, this.FaceInfo_Rgb[i]));
                }
                return MXResult.CreateSuccess(infoList);
            case 10001:
                return MXResult.CreateFail(result, "活体检测不通过");
            case 10002:
                return MXResult.CreateFail(result, "可见光输入没有人脸");
            case 10003:
                return MXResult.CreateFail(result, "近红外输入没有人脸");
            case 10004:
                return MXResult.CreateFail(result, "人脸肤色检测未通过");
            case 10005:
                return MXResult.CreateFail(result, "请离远一点");
            case 10006:
                return MXResult.CreateFail(result, "请离近一点");
            case 10007:
                return MXResult.CreateFail(result, "请正对摄像头");
            case 10008:
                return MXResult.CreateFail(result, "图像模糊");
            case 10009:
                return MXResult.CreateFail(result, "请勿闭眼");
            case 10010:
                return MXResult.CreateFail(result, "请勿张嘴");
            case 10011:
                return MXResult.CreateFail(result, "过曝");
            case 10012:
                return MXResult.CreateFail(result, "欠曝");
            case 10013:
                return MXResult.CreateFail(result, "遮挡");
            default:
                return MXResult.CreateFail(result, "未知错误");
        }
    }

}

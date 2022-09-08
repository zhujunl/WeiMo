
package org.zz.api;

import android.content.Context;

import org.zz.jni.JustouchFaceApi;

public class MXFaceAPI {

    private boolean m_bInit = false;
    private final JustouchFaceApi m_dllFaceApi = new JustouchFaceApi();

    /**
     * @param
     * @return algorithm version
     * @author chen.gs
     * @category algorithm version
     */
    public String mxAlgVersion() {
        return m_dllFaceApi.getAlgVersion();
    }

    /**
     * @param context -  input，context handle
     *                szModelPath    -  input，model path
     *                szLicense      -  input，authorization code
     * @return 0-success, others-failure
     * @author chen.gs
     * @category initialization algorithm
     */
    public int mxInitAlg(Context context, String szModelPath, String szLicense) {
        int nRet = 0;
        nRet = m_dllFaceApi.initAlg(context, szModelPath, szLicense);
        if (nRet != 0) {
            return nRet;
        }
        m_bInit = true;
        return 0;
    }


    /**
     * @param
     * @return 0-success, others-failure
     * @author chen.gs
     * @category release algorithm
     */
    public int mxFreeAlg() {
        if (m_bInit) {
            m_dllFaceApi.freeAlg();
            m_bInit = false;
        }
        return 0;
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
    public int mxDetectFace(byte[] pImage, int nWidth, int nHeight,
                            int[] pFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        int nRet = m_dllFaceApi.detectFace(pImage, nWidth, nHeight, pFaceNum, bInfo);
        if (nRet != 0) {
            pFaceNum[0] = 0;
            return nRet;
        }
        MXFaceInfoEx.Ints2MXFaceInfoExs(pFaceNum[0], bInfo, pFaceInfo);
        return 0;
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
    public int mxDetectLive(byte[] pRgbImage, byte[] pNirImage, int nWidth, int nHeight,
                            int[] pRgbFaceNum, MXFaceInfoEx[] pRgbFaceInfo, int[] pNirFaceNum, MXFaceInfoEx[] pNirFaceInfo) {

        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bRgbInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        int[] bNirInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        int nRet = m_dllFaceApi.detectLive(pRgbImage, pNirImage, nWidth, nHeight, pRgbFaceNum, bRgbInfo, pNirFaceNum, bNirInfo);
        if (pRgbFaceNum[0] > 0) {
            MXFaceInfoEx.Ints2MXFaceInfoExs(pRgbFaceNum[0], bRgbInfo, pRgbFaceInfo);
        }
        if (pNirFaceNum[0] > 0) {
            MXFaceInfoEx.Ints2MXFaceInfoExs(pNirFaceNum[0], bNirInfo, pNirFaceInfo);
        }
        return nRet;
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
            iFeaLen = m_dllFaceApi.getFeatureSize();
        }
        return iFeaLen;
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
    public int mxFeatureExtract(byte[] pImage, int nWidth, int nHeight,
                                int nFaceNum, MXFaceInfoEx[] pFaceInfo, byte[] pFaceFeature) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.featureExtract(pImage, nWidth, nHeight, nFaceNum, bInfo, pFaceFeature);
        if (nRet != 0) {
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
    public int mxFeatureMatch(byte[] pFaceFeatureA, byte[] pFaceFeatureB, float[] fScore) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        return m_dllFaceApi.featureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
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
    public int mxFaceQuality(byte[] pImage, int nWidth, int nHeight,
                             int nFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.faceQuality(pImage, nWidth, nHeight, nFaceNum, bInfo);
        if (nRet != 0) {
            return nRet;
        }
        MXFaceInfoEx.Ints2MXFaceInfoExs(nFaceNum, bInfo, pFaceInfo);
        return nRet;
    }

    /**
     * @param pImage - input, RGB image width
     *               nWidth        - input, image width
     *               nHeight       - input, image height
     *               nFaceNum    	- input, number of faces
     *               pFaceInfo     - input/output, obtained through quality attribute of MXFaceInfoEx structure, recommended threshold: 90
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face image quality evaluation based on face detection results for face image registration。
     */
    public int mxFaceQuality4Reg(byte[] pImage, int nWidth, int nHeight,
                                 int nFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        for (int i = 0; i < nFaceNum; i++) {
            int[] bInfoTmp = new int[MXFaceInfoEx.SIZE];
            System.arraycopy(bInfo, i * MXFaceInfoEx.SIZE, bInfoTmp, 0, MXFaceInfoEx.SIZE);
            m_dllFaceApi.faceQuality4Reg(pImage, nWidth, nHeight, bInfoTmp);
            pFaceInfo[i].quality = bInfoTmp[8 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        }
        return 0;
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
    public int mxNIRLivenessDetect(byte[] pImage, int nWidth, int nHeight, int nFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }

        int[] bInfo = new int[MXFaceInfoEx.SIZE * nFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.nirLivenessDetect(pImage, nWidth, nHeight, nFaceNum, bInfo);
        MXFaceInfoEx.Ints2MXFaceInfoExs(nFaceNum, bInfo, pFaceInfo);
        return nRet;
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
    public int mxMaskDetect(byte[] pImage, int nWidth, int nHeight,
                            int nFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * nFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.maskDetect(pImage, nWidth, nHeight, nFaceNum, bInfo);
        MXFaceInfoEx.Ints2MXFaceInfoExs(nFaceNum, bInfo, pFaceInfo);
        return nRet;
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
    public int mxMaskFeatureExtract(byte[] pImage, int nWidth, int nHeight,
                                    int nFaceNum, MXFaceInfoEx[] pFaceInfo, byte[] pFaceFeature) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.maskFeatureExtract(pImage, nWidth, nHeight, nFaceNum, bInfo, pFaceFeature);
        if (nRet != 0) {
            return MXErrorCode.ERR_FACE_EXTRACT;
        }
        return nRet;
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
    public int mxMaskFeatureExtract4Reg(byte[] pImage, int nWidth, int nHeight,
                                        int nFaceNum, MXFaceInfoEx[] pFaceInfo, byte[] pFaceFeature) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.maskFeatureExtract4Reg(pImage, nWidth, nHeight, nFaceNum, bInfo, pFaceFeature);
        if (nRet != 0) {
            return MXErrorCode.ERR_FACE_EXTRACT;
        }
        return nRet;
    }

    /**
     * @param pFaceFeatureA - input, face feature A
     *                      pFaceFeatureB - input, face feature B
     *                      fScore  - Output, similarity measure(0 ~ 1.0), the bigger the more similar, recommended threshold: 0.73
     * @return 0-success, others-failure
     * @author chen.gs
     * @category face feature match (mask algorithm)
     */
    public int mxMaskFeatureMatch(byte[] pFaceFeatureA, byte[] pFaceFeatureB, float[] fScore) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        return m_dllFaceApi.maskFeatureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
    }


    /**
     * @param pFaceFeaList - input, face template set (template 1 + template 2 +... + template n)
     *                     iFaceNum       	- input, number of face templates (less than 5000 recommended)
     *                     iScoreThreshold   - input, threshold  (recommendation 76)
     *                     pFaceFea       	- input, face features
     *                     pFaceInfo  	    - output. obtained through recog/recogid/recogscore attribute of MXFaceInfoEx structure
     * @return 0-success, others-failure
     * @author chen.gs
     * @category According to the set of input face feature and face template, find the serial number of matching face feature
     */
    public int mxSearchFeature(byte[] pFaceFeaList, int iPictureNum, int iScoreThreshold, byte[] pFaceFea, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE];
        MXFaceInfoEx.MXFaceInfoExs2Ints(1, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.searchFeature(pFaceFeaList, iPictureNum, iScoreThreshold, pFaceFea, bInfo);
        if (nRet != 0) {
            return MXErrorCode.ERR_FACE_SEARCH;
        }
        MXFaceInfoEx.Ints2MXFaceInfoExs(1, bInfo, pFaceInfo);
        return nRet;
    }

    /**
     * @param pFaceFeaList - input, face template set(template 1 + template 2 + ... + template N)
     *                     iPersonNum    	- input, the total number of people in face template set
     *                     iMatchThreshold  	- input, match threshold (recommended 76)
     *                     iQualityThreshold	- input, quality threshold (recommended 50)
     *                     pImage		  	- input, RGB image data
     *                     nWidth        	- input, image width
     *                     nHeight       	- input, image height
     *                     nFaceNum      	- input/output, number of faces, must be global variables
     *                     pFaceInfo     	- output, face information, see MXFaceInfoEx structure, must be global variables
     *                     obtained through reCog/reCogId/reCogScore attribute of MXFaceInfoEx structure
     * @return 0-success, others-failure
     * @author chen.gs
     * @category According to the input face information and the set of face templates, find the serial number that matches the facial features
     * (the same person can contain one picture)
     * @note： Use with mxDetectFace
     */
    public int mxSearchFace(byte[] pFaceFeaList, int iPersonNum,
                            int iMatchThreshold, int iQualityThreshold,
                            byte[] pImage, int nWidth, int nHeight, int nFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(nFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.searchFace(pFaceFeaList, iPersonNum, iMatchThreshold, iQualityThreshold,
                pImage, nWidth, nHeight, nFaceNum, bInfo);
        if (nRet != 0) {
            return nRet;
        }
        MXFaceInfoEx.Ints2MXFaceInfoExs(nFaceNum, bInfo, pFaceInfo);
        return nRet;
    }

    /**
     * @param pFaceFeaList - input, face template set(template 1 + template 2 + ... + template N)
     *                     pMaskFaceFeaList    - input, (mask algorithm) face template set(template 1 + template 2 + ... + template N)
     *                     iPersonNum    	  - input, the total number of people in face template set
     *                     iMatchThreshold  	  - input, match threshold (recommended 76)
     *                     iMaskMatchThreshold - input, mask algorithm match threshold (recommended 73)
     *                     iQualityThreshold   - input, quality threshold (recommended 50)
     *                     pImage		  	  - input, RGB image data
     *                     nWidth        	  - input, image width
     *                     nHeight    	      - input, image height
     *                     nFaceNum      	  - input/output, number of faces, must be global variables
     *                     pFaceInfo     	  - output, face information, see MXFaceInfoEx structure, must be global variables
     *                     obtained through reCog/reCogId/reCogScore attribute of MXFaceInfoEx structure
     * @return 0-success, others-failure
     * @author chen.gs
     * @category According to the input face information and the set of face templates, find the serial number that matches the facial features
     * (the same person can contain one picture)
     * @note： Use with mxDetectFace
     */
    public int mxSearchAllFace(byte[] pFaceFeaList, byte[] pMaskFaceFeaList, int iPersonNum,
                               int iMatchThreshold, int iMaskMatchThreshold, int iQualityThreshold,
                               byte[] pImage, int nImgWidth, int nImgHeight, int iFaceNum, MXFaceInfoEx[] pFaceInfo) {
        if (!m_bInit) {
            return MXErrorCode.ERR_NO_INIT;
        }
        int[] bInfo = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];
        MXFaceInfoEx.MXFaceInfoExs2Ints(iFaceNum, bInfo, pFaceInfo);
        int nRet = m_dllFaceApi.searchAllFace(pFaceFeaList, pMaskFaceFeaList, iPersonNum, iMatchThreshold, iMaskMatchThreshold, iQualityThreshold,
                pImage, nImgWidth, nImgHeight, iFaceNum, bInfo);
        if (nRet != 0) {
            return nRet;
        }
        MXFaceInfoEx.Ints2MXFaceInfoExs(iFaceNum, bInfo, pFaceInfo);
        return nRet;
    }
}

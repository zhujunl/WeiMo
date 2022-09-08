package org.zz.facematchdemo;

public class DataManager {

//    private static final String tag = "MIAXIS_DataManager";
//
//    static MXFaceAPI m_faceAlgApi = MainActivity.GetFaceAPI();
//    ;
//    static mxImageTool m_imgTool = MainActivity.GetImageTool();
//    ;
//
//    public static List<String> m_strUserIdList = new ArrayList<String>();
//    public static byte[] m_pFaceFeaList;          //人脸模板集合(模板1+模板2+...+模板N)
//    public static byte[] m_pFaceFeaMaskList;      //人脸模板（戴口罩算法）集合(模板1+模板2+...+模板N)
//    //public static int m_iPictureNum = 0;          //库中图片总数
//    // public static int[] m_pFaceNumList;           //顺序记录模板库中每个人的模板图片数(建议<=10)
//    public static int m_iPersonNum = 0;           //库中总人数
//
//    public static int getDbPersonNum() {
//        if (FileDataUtils.isExist(Config.TzDbPath) == false) {
//            return 0;
//        }
//        //2 遍历 图像 文件
//        List<String> strSubPathList = new ArrayList<String>();
//        FileDataUtils.GetSubFolders(Config.TzDbPath, strSubPathList);
//        return strSubPathList.size();
//    }
//
//    public static int LoadTzDb() {
//
//        if (getDbPersonNum() == 0) {
//            //如果特征库不存在，则从图像库生成
//            if (FileDataUtils.isExist(Config.ImgDbPath) == false) {
//                //如果图像库也不存在
//                return 0;
//            }
//            ImgDb2TzDb();
//        }
//
//        int nRet = 0;
//        int iFeatureSize = m_faceAlgApi.mxGetFeatureSize();
//
//        //2 遍历 图像 文件
//        List<String> strSubPathList = new ArrayList<String>();
//        FileDataUtils.GetSubFolders(Config.TzDbPath, strSubPathList);
//        int nPersonNum = strSubPathList.size();
//        //Timber.e(tag,"nPersonNum = "+ nPersonNum);
//        if (nPersonNum <= 0) {
//            return 0;
//        }
//
//        m_strUserIdList.clear();
//        m_pFaceFeaList = new byte[2 * iFeatureSize * nPersonNum];
//        m_pFaceFeaMaskList = new byte[2 * iFeatureSize * nPersonNum];
//        m_iPersonNum = 0;
//
//        for (int i = 0; i < nPersonNum; i++) {
//            String strSubPath = strSubPathList.get(i);
//            String strUserId = strSubPath.substring(Config.TzDbPath.length());
//            //Timber.e(tag,strSubPath);
//            //Timber.e(tag,"====UserId:"+strUserId);
//            List<String> strTzFileList = new ArrayList<String>();
//            FileDataUtils.GetSubFiles(strSubPath, strTzFileList);
//            int nTzNum = strTzFileList.size();
//            //Timber.e(tag,"nImageNum = "+ nImageNum);
//            int iPerTzNum = 0;
//            for (int j = 0; j < nTzNum; j++) {
//                String strTzFile = strTzFileList.get(j);
//                String strTzName = strTzFile.substring(strSubPath.length() + 1);
//                Timber.e(tag, "=======FeaturePath:" + strTzFile);
//                //Timber.e(tag,"====TzName:"+strTzName);
//                //读取特征
//                byte[] pFeatureData = FileDataUtils.ReadData(strTzFile);
//                if (iFeatureSize != pFeatureData.length) {
//                    Timber.e(tag, strTzFile + ",特征文件大小不合法,length=" + pFeatureData.length);
//                    continue;
//                }
//                //保存数据
//                System.arraycopy(pFeatureData, 0, m_pFaceFeaList, m_iPersonNum * iFeatureSize, iFeatureSize);
//
//                //读取特征(戴口罩)
//                String strMaskFeaturePath = Config.TzMaskDbPath + strUserId + "/" + strTzName;
//                //Timber.e(tag,"====TzName:"+strTzName);
//                Timber.e(tag, "=======MaskFeaturePath:" + strMaskFeaturePath);
//                byte[] pMaskFeatureData = FileDataUtils.ReadData(strMaskFeaturePath);
//                if (iFeatureSize != pMaskFeatureData.length) {
//                    Timber.e(tag, strTzFile + ",特征文件大小不合法,length=" + pMaskFeatureData.length);
//                    continue;
//                }
//                //保存数据(戴口罩)
//                System.arraycopy(pMaskFeatureData, 0, m_pFaceFeaMaskList, m_iPersonNum * iFeatureSize, iFeatureSize);
//
//                iPerTzNum++;
//                m_iPersonNum++;
//                if (iPerTzNum >= 1) {
//                    break;
//                }
//            }
//            if (iPerTzNum > 0) {
//                //String strUserId = strSubPath.substring(Config.TzDbPath.length());
//                m_strUserIdList.add(strUserId);
//                //Timber.e(tag,"strUserId="+strUserId);
//                Timber.e(tag, strUserId + ",特征个数=" + iPerTzNum);
//            }
//        }
//        Timber.e(tag, "库中总人数=" + m_iPersonNum);
//        return m_iPersonNum;
//    }
//
//    public static String GetDbImgPath(String strUsrId) {
//        String strUserPath = Config.ImgDbPath + "/" + strUsrId;
//        List<String> strImageFileList = new ArrayList<String>();
//        FileDataUtils.GetSubFiles(strUserPath, strImageFileList);
//        if (strImageFileList.size() > 0) {
//            return strImageFileList.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    public static int ImgDb2TzDb() {
//        Timber.e(tag, "=========ImgDb2TzDb========");
//        int nRet = 0;
//        int[] iWidth = new int[1];
//        int[] iHeight = new int[1];
//        byte[] pImgBuf = null;
//        int[] pFaceNum = new int[1];
//        pFaceNum[0] = MXFaceInfoEx.iMaxFaceNum;
//        MXFaceInfoEx[] pFaceInfo = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
//        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
//            pFaceInfo[i] = new MXFaceInfoEx();
//        }
//        int iFeatureSize = m_faceAlgApi.mxGetFeatureSize();
//        byte[] pFeatureData = new byte[iFeatureSize];
//        byte[] pMaskFeatureData = new byte[iFeatureSize];
//
//        //2 遍历 图像 文件
//        List<String> strSubPathList = new ArrayList<String>();
//        FileDataUtils.GetSubFolders(Config.ImgDbPath, strSubPathList);
//        int nPersonNum = strSubPathList.size();
//        Timber.e(tag, "nPersonNum = " + nPersonNum);
//        if (nPersonNum <= 0) {
//            return 0;
//        }
//        for (int i = 0; i < nPersonNum; i++) {
//            float percent = (float) (i + 1) / nPersonNum * 100;
//            Timber.e(tag, "完成 [" + (int) percent + "%]");
//            String strSubPath = strSubPathList.get(i);
//            String strUserId = strSubPath.substring(Config.ImgDbPath.length());
//            Timber.e(tag, "strSubPath=" + strSubPath);
//            Timber.e(tag, "strUserId=" + strUserId);
//            List<String> strImageFileList = new ArrayList<String>();
//            FileDataUtils.GetSubFiles(strSubPath, strImageFileList);
//            int nImageNum = strImageFileList.size();
//            //Timber.e(tag,"nImageNum = "+ nImageNum);
//            for (int j = 0; j < nImageNum; j++) {
//                String strImageFile = strImageFileList.get(j);
//                String strImageName = strImageFile.substring(strSubPath.length() + 1);
//                Timber.e(tag, "strImageFile=" + strImageFile);
//                Timber.e(tag, "strImageName=" + strImageName);
//                //读取图像
//                nRet = m_imgTool.ImageLoad(strImageFile, 3, null, iWidth, iHeight);
//                if (nRet != 1) {
//                    Timber.e(tag, strImageFile + ",读取失败,nRet=" + nRet);
//                    continue;
//                }
//                pImgBuf = new byte[iWidth[0] * iHeight[0] * 3];
//                m_imgTool.ImageLoad(strImageFile, 3, pImgBuf, iWidth, iHeight);
//                //人脸检测
//                nRet = m_faceAlgApi.mxDetectFace(pImgBuf, iWidth[0], iHeight[0], pFaceNum, pFaceInfo);
//                if (nRet != 0) {
//                    Timber.e(tag, strImageFile + ",人脸检测失败,nRet=" + nRet);
//                    continue;
//                }
//                if (pFaceNum[0] != 1) {
//                    Timber.e(tag, strImageFile + ",人脸个数不合法，FaceNum=" + pFaceNum[0]);
//                    continue;
//                }
//                //质量评价
//                //                AlgEngine.m_faceAlgApi.mxFaceQuality4Reg(pImgBuf, iWidth[0], iHeight[0],pFaceNum[0],pFaceInfo);
//                //                if (pFaceInfo[0].quality<Config.quality4RegThreshold) { //
//                //                    Timber.e(tag,strImageFile+",注册" +
//                //                            "人脸质量分数过低，quality="+pFaceInfo[0].quality);
//                //                    continue;
//                //                }
//                //提取特征
//                nRet = m_faceAlgApi.mxFeatureExtract(pImgBuf, iWidth[0], iHeight[0], pFaceNum[0], pFaceInfo, pFeatureData);
//                if (nRet != 0) {
//                    Timber.e(tag, strImageFile + ",提取特征失败,nRet=" + nRet);
//                    continue;
//                }
//                //保存特征
//                FileDataUtils.AddDirectory(Config.TzDbPath);
//                String strSavePath = Config.TzDbPath + "/" + strUserId;
//                FileDataUtils.AddDirectory(strSavePath);
//                String strSaveFile = strSavePath + "/" + strImageName + ".dat";
//                FileDataUtils.saveData(strSaveFile, pFeatureData);
//
//                //提取特征（戴口罩）
//                nRet = m_faceAlgApi.mxMaskFeatureExtract4Reg(pImgBuf, iWidth[0], iHeight[0], pFaceNum[0], pFaceInfo, pMaskFeatureData);
//                if (nRet != 0) {
//                    Timber.e(tag, strImageFile + ",提取特征（戴口罩）失败,nRet=" + nRet);
//                    continue;
//                }
//                //保存特征（戴口罩）
//                FileDataUtils.AddDirectory(Config.TzMaskDbPath);
//                strSavePath = Config.TzMaskDbPath + "/" + strUserId;
//                FileDataUtils.AddDirectory(strSavePath);
//                strSaveFile = strSavePath + "/" + strImageName + ".dat";
//                FileDataUtils.saveData(strSaveFile, pMaskFeatureData);
//            }
//        }
//        Timber.e(tag, "=================================");
//        return 0;
//    }
//
//    public static boolean IsExistUserId(String strUserId) {
//        //遍历 图像 文件
//        List<String> strSubPathList = new ArrayList<String>();
//        FileDataUtils.GetSubFolders(Config.ImgDbPath, strSubPathList);
//        int nPersonNum = strSubPathList.size();
//        if (nPersonNum <= 0) {
//            return false;
//        }
//        for (int i = 0; i < nPersonNum; i++) {
//            String strSubPath = strSubPathList.get(i);
//            String strUserIdTmp = strSubPath.substring(Config.ImgDbPath.length());
//            if (strUserIdTmp.compareToIgnoreCase(strUserId) == 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static int FaceEnroll(String strPathImgFile, String strUserId) {
//        int nRet = 0;
//        int[] oX = new int[1];
//        int[] oY = new int[1];
//
//        //读取图像
//        nRet = m_imgTool.ImageLoad(strPathImgFile, 3, null, oX, oY);
//        if (nRet != 1) {
//            //SendMsg(SHOW_MSG, "装载图像失败,nRet=" + nRet);
//            return MXErrorCode.ERR_READ_IMAGE;
//        }
//        byte[] pRGBBuff = new byte[oX[0] * oY[0] * 3];
//        nRet = m_imgTool.ImageLoad(strPathImgFile, 3, pRGBBuff, oX, oY);
//        if (nRet != 1) {
//            //SendMsg(SHOW_MSG, "装载图像失败,nRet=" + nRet);
//            return MXErrorCode.ERR_READ_IMAGE;
//        }
//
//        return FaceEnroll(pRGBBuff, oX[0], oY[0], strUserId);
//    }
//
//    public static int FaceEnroll(byte[] pRGBBuff, int iWidth, int iHeight, String strUserId) {
//        //人脸检测
//        int[] pFaceNum = new int[1];
//        pFaceNum[0] = MXFaceInfoEx.iMaxFaceNum;
//        MXFaceInfoEx[] pFaceInfo = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
//        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
//            pFaceInfo[i] = new MXFaceInfoEx();
//        }
//        int nRet = m_faceAlgApi.mxDetectFace(pRGBBuff, iWidth, iHeight, pFaceNum, pFaceInfo);
//        if (nRet != 0) {
//            //SendMsg(SHOW_MSG, "人脸检测失败,nRet=" + nRet);
//            return MXErrorCode.ERR_FACE_DETECT;
//        }
//        if (pFaceNum[0] < 1) {
//            return MXErrorCode.ERR_FACE_DETECT;
//        }
//        if (pFaceNum[0] > 1) {
//            return MXErrorCode.ERR_MULTIPLE_FACES;
//        }
//
//        //注册人脸图像质量评价
//        m_faceAlgApi.mxFaceQuality4Reg(pRGBBuff, iWidth, iHeight, pFaceNum[0], pFaceInfo);
//        Timber.e("miaxis", "pFaceInfo[0].quality=" + pFaceInfo[0].quality);
//        if (pFaceInfo[0].quality < Config.quality4RegThreshold) {
//            return MXErrorCode.ERR_FACE_QUALITY;
//        }
//
//        //标准算法特征提取
//        int iFeatureSize = m_faceAlgApi.mxGetFeatureSize();
//        byte[] pFeatureBuf = new byte[iFeatureSize * pFaceNum[0]];
//        nRet = m_faceAlgApi.mxFeatureExtract(pRGBBuff, iWidth, iHeight, pFaceNum[0], pFaceInfo, pFeatureBuf);
//        if (nRet != 0) {
//            //SendMsg(SHOW_MSG, "特征提取失败,nRet=" + nRet);
//            return MXErrorCode.ERR_TZ_CHECK;
//        }
//        //戴口罩算法特征提取
//        byte[] pMaskFeatureBuf = new byte[iFeatureSize * pFaceNum[0]];
//        m_faceAlgApi.mxMaskFeatureExtract4Reg(pRGBBuff, iWidth, iHeight, pFaceNum[0], pFaceInfo, pMaskFeatureBuf);
//
//        //创建数据库根目录
//        FileDataUtils.AddDirectory(Config.RootDbPath);
//        //保存注册图像
//        FileDataUtils.AddDirectory(Config.ImgDbPath);
//        String strSavePath = Config.ImgDbPath + File.separator + strUserId;
//        FileDataUtils.AddDirectory(strSavePath);
//        String strSaveFile = strSavePath + File.separator + strUserId + ".jpg";
//        m_imgTool.ImageSave(strSaveFile, pRGBBuff, iWidth, iHeight, 3);
//        //保存标准算法特征
//        FileDataUtils.AddDirectory(Config.TzDbPath);
//        strSavePath = Config.TzDbPath + File.separator + strUserId;
//        FileDataUtils.AddDirectory(strSavePath);
//        strSaveFile = strSavePath + File.separator + strUserId + ".dat";
//        FileDataUtils.saveData(strSaveFile, pFeatureBuf, iFeatureSize * pFaceNum[0]);
//        //保存戴口罩算法特征
//        FileDataUtils.AddDirectory(Config.TzMaskDbPath);
//        strSavePath = Config.TzMaskDbPath + File.separator + strUserId;
//        FileDataUtils.AddDirectory(strSavePath);
//        strSaveFile = strSavePath + File.separator + strUserId + ".dat";
//        FileDataUtils.saveData(strSaveFile, pMaskFeatureBuf, iFeatureSize * pFaceNum[0]);
//        return 0;
//    }
}

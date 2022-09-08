
package org.zz.jni;

public class JustouchFaceApi {

    //加载动态库
    static {
        System.loadLibrary("JustouchFaceAPI");
    }

    /**
     * @author   chen.gs
     * @category 获取算法版本
     * @param
     * @return   算法版本
     * */
    public native String getAlgVersion();

    /**
     * @author   chen.gs
     * @category 初始化算法（
     * @param    context        - 输入，上下文句柄
     * 			 szModelPath    - 输入，模型路径
     * 			 szLicense      - 输入，授权码
     * @return   0-成功，其他-失败
     * */
    public native int initAlg(Object context,String path,String license);

    /**
     * @author   chen.gs
     * @category 释放算法
     * @param
     * @return   0-成功，其他-失败
     * */
    public native int freeAlg();

    /**
     * @author   chen.gs
     * @category 获取人脸特征长度
     * @param
     * @return   人脸特征长度
     * */
    public native int getFeatureSize();

    /**
     * @author   chen.gs
     * @category 人脸检测,用于静态图像检测
     * @param    pImage    - 输入，RGB图像数据
     * 			 nWidth    - 输入，图像宽度
     * 			 nHeight   - 输入，图像高度
     * 			 pFaceNum  - 输入/输出，人脸数,内存分配大小 new int[262*100],结构详见MXFaceInfoEx
     * 			 pFaceInfo - 输出，人脸信息
     * @return   0-成功，其他-失败
     * */
    public native int detectFace(byte[] pImage, int nWidth, int nHeight,
                                    int[] pFaceNum, int[] pFaceInfo);

    /**
     *  @author     chen.gs
     *  @category   双目摄像头活体检测
     *  @paramp     pRgbImage		- 输入，可见光摄像头的图像数据
     *              pNirImage		- 输入，近红外摄像头的图像数据
     *              ImgWidth		- 输入，图像宽度
     *              nImgHeight		- 输入，图像高度
     *              pRgbFaceNum		- 输出，可见光摄像头图像的人脸检出数
     *              pRgbFaceInfo	- 输出，可见光摄像头图像的人脸信息
     *              pNirFaceNum		- 输出，近红外摄像头图像的人脸检出数
     *              pNirFaceInfo	- 输出，近红外摄像头图像的人脸信息
     *  @return     10000-活体，10001-假体，其他-图像质量不满足
     */
    public native int detectLive(byte[] pRgbImage, byte[] pNirImage, int nImgWidth, int nImgHeight,
                            int[] pRgbFaceNum,int[] pRgbFaceInfo,int[] pNirFaceNum, int[] pNirFaceInfo);

    /**
     * @author   chen.gs
     * @category 人脸特征提取
     * @param    pImage       - 输入，RGB图像数据
     * 			 nWidth       - 输入，图像宽度
     * 			 nHeight      - 输入，图像高度
     * 			 nFaceNum     - 输入，人脸个数
     * 			 pFaceInfo    - 输入，人脸信息
     * 			 pFaceFeature - 输出，人脸特征，特征长度*人脸个数
     * @return   0-成功，其他-失败
     * */
    public native int featureExtract(byte[] pImage, int nWidth, int nHeight,
                                      int pFaceNum, int[] pFaceInfo, byte[] pFaceFea);

    /**
     * @author   chen.gs
     * @category 人脸特征比对
     * @param    pFaceFeatureA - 输入，人脸特征A
     * 			 pFaceFeatureB - 输入，人脸特征B
     * 			 fScore    - 输出，相似性度量值，0~1.0 ，越大越相似。
     * @return   0-成功，其他-失败
     * */
    public native int featureMatch(byte[] pFaceFeatureA,byte[] pFaceFeatureB, float[] fScore);

    /**
     * @author   chen.gs
     * @category 根据人脸检测结果，进行人脸图像质量评价，用于过滤参与人脸比对识别的图像
     * @param     pImage     	- 输入，RGB图像数据
     * 			  nImgWidth     - 输入，图像宽度
     * 			  nImgHeight    - 输入，图像高度
     * 			  nFaceNum    	- 输入，人脸数
     * 			  pFaceInfo     - 输入/输出，人脸信息，质量分数通过MXFaceInfoEx结构体quality属性获取
     * @return   0-成功，其他-失败
     * */
    public native int faceQuality(byte[] pImage, int nWidth, int nHeight,
                                     int pFaceNum, int[] pFaceInfo);

    /**
     * @author   chen.gs
     * @category 根据人脸检测结果，进行人脸图像质量评价，用于人脸图像注册。
     * @note     建议采用高清USB摄像头采集，质量分大于90分，用于注册
     * @param     pImage     	- 输入，RGB图像数据
     * 			  nImgWidth     - 输入，图像宽度
     * 			  nImgHeight    - 输入，图像高度
     * 			  pFaceInfo 	- 输入，人脸信息
     * @return    质量分数
     * */
    public native int faceQuality4Reg(byte[] pImage, int nWidth, int nHeight,
                                         int[] pFaceInfo);
    /**
     * @author   chen.gs
     * @category  近红外活体检测（配合指定型号双目摄像头）
     * @param     pImage        - 输入，近红外图像数据
     * 			  nImgWidth  	- 输入，图像宽度
     * 			  nImgHeight  	- 输入，图像高度
     * 			  nFaceNum    	- 输入，人脸数
     * 			  pFaceInfo 	- 输入/输出，人脸信息，活体分数通过MXFaceInfoEx结构体liveness属性获取
     * @return   0-成功，其他-失败
     * */
    public native int nirLivenessDetect(byte[] pImage, int nImgWidth, int nImgHeight, int iFaceNum,int[] pFaceInfo);

    /**
     * @author   chen.gs
     * @category  检测人脸是否戴口罩
     * @param     pImage        - 输入，近红外图像数据
     * 			  nImgWidth  	- 输入，图像宽度
     * 			  nImgHeight  	- 输入，图像高度
     * 			  nFaceNum    	- 输入，人脸数
     * 			  pFaceInfo 	- 输入/输出，人脸信息，活体分数通过MXFaceInfoEx结构体mask属性获取
     * @return   0-成功，其他-失败
     * */
    public native int maskDetect(byte[] pImage, int nWidth, int nHeight,
                                 int iFaceNum, int[] pFaceInfo);

    /**
     * @author   chen.gs
     * @category 人脸特征提取,用于比对（戴口罩算法）
     * @param    pImage       - 输入，RGB图像数据
     * 			 nWidth       - 输入，图像宽度
     * 			 nHeight      - 输入，图像高度
     * 			 nFaceNum     - 输入，人脸个数
     * 			 pFaceInfo    - 输入，人脸信息
     * 			 pFaceFeature - 输出，人脸特征，特征长度*人脸个数
     * @return   0-成功，其他-失败
     * */
    public native int maskFeatureExtract(byte[] pImage, int nWidth, int nHeight,
                                     int pFaceNum, int[] pFaceInfo, byte[] pFaceFea);

    /**
     * @author   chen.gs
     * @category 人脸特征提取,用于注册（戴口罩算法）
     * @param    pImage       - 输入，RGB图像数据
     * 			 nWidth       - 输入，图像宽度
     * 			 nHeight      - 输入，图像高度
     * 			 nFaceNum     - 输入，人脸个数
     * 			 pFaceInfo    - 输入，人脸信息
     * 			 pFaceFeature - 输出，人脸特征，特征长度*人脸个数
     * @return   0-成功，其他-失败
     * */
    public native int maskFeatureExtract4Reg(byte[] pImage, int nWidth, int nHeight,
                                         int pFaceNum, int[] pFaceInfo, byte[] pFaceFea);

    /**
     * @author   chen.gs
     * @category 人脸特征比对（戴口罩算法）
     * @param    pFaceFeatureA - 输入，人脸特征A
     * 			 pFaceFeatureB - 输入，人脸特征B
     * 			 fScore    - 输出，相似性度量值，0~1.0 ，越大越相似。
     * @return   0-成功，其他-失败
     * */
    public native int maskFeatureMatch(byte[] pFaceFeatureA,byte[] pFaceFeatureB, float[] fScore);

    /**
     * @author   chen.gs
     * @category 根据输入人脸特征与人脸模板集合，查找匹配人脸特征的序号
     * @param     pFaceFeaList  	- 输入，人脸模板集合(模板1+模板2+...+模板N)
     * 			  iFaceNum       	- 输入，人脸模板个数（建议小于5000）
     * 			  score         	- 输入，比对分通过阈值（建议76）
     * 			  pFaceFea       	- 输入，人脸特征
     * 			  pFaceInfo  	    - 输入，输入/输出，识别结果通过MXFaceInfoEx结构体的reCog/reCogId/reCogScore属性获取
     * @return   0-成功，其他-失败
     * */
    public native int searchFeature(byte[] pFaceFeaList, int iPictureNum, int iScoreThreshold, byte[] pFaceFea, int[] pFaceInfo);

    /**
     * @author   chen.gs
     * @category 根据输入人脸信息与人脸模板集合，查找匹配人脸特征的序号(同一人含有一张图片)
     * @note：   配合detectFace一起使用
     * @param     pFaceFeaList  	- 输入，人脸模板集合(模板1+模板2+...+模板N)
     * 			  iPersonNum    	- 输入，库中总人数
     * 			  iMatchThreshold  	- 输入，比对分通过阈值（建议76）
     * 			  iMatchThreshold  	- 输入，图片质量评价阈值（建议50）
     * 			  pImage		  	- 输入，RGB图像数据
     * 			  nImgWidth     	- 输入，图像宽度
     * 			  nImgHeight    	- 输入，图像高度
     * 			  nFaceNum      	- 输入，人脸数，须申请为全局变量
     * 			  pFaceInfo     	- 输入/输出，人脸信息，须申请为全局变量
     * 										识别结果通过MXFaceInfoEx结构体的reCog/reCogId/reCogScore属性获取
     * @return   0-成功，其他-失败
     * */
    public native int searchFace(byte[] pFaceFeaList, int iPersonNum,
                                 int iMatchThreshold, int iQualityThreshold,
                                 byte[] pImage, int nImgWidth, int nImgHeight, int iFaceNum,int[] pFaceInfo);
    /**
     * @author   chen.gs
     * @category 根据输入人脸信息与标准算法/戴口罩算法人脸模板集合，查找匹配人脸特征的序号(同一人含有一张图片)
     * @note：   配合detectFace一起使用
     * @param     pFaceFeaList  		- 输入，人脸模板集合(模板1+模板2+...+模板N)
     *            pMaskFaceFeaList		- 输入，戴口罩人脸模板集合(模板1+模板2+...+模板N)
     * 			  iPersonNum    		- 输入，库中总人数
     * 			  iMatchThreshold  		- 输入，比对分通过阈值（建议76）
     * 			  iMaskMatchThreshold 	- 输入，戴口罩人脸的比对分通过阈值（建议73）
     * 			  iMatchThreshold  		- 输入，图片质量评价阈值（建议50）
     * 			  pImage		  		- 输入，RGB图像数据
     * 			  nImgWidth     		- 输入，图像宽度
     * 			  nImgHeight    		- 输入，图像高度
     * 			  nFaceNum      		- 输入，人脸数，须申请为全局变量
     * 			  pFaceInfo     		- 输入/输出，人脸信息，须申请为全局变量
     * 											识别结果通过MXFaceInfoEx结构体的reCog/reCogId/reCogScore属性获取
     * @return   0-成功，其他-失败
     * */
    public native int searchAllFace(byte[] pFaceFeaList, byte[] pMaskFaceFeaList,int iPersonNum,
                                    int iMatchThreshold, int iMaskMatchThreshold, int iQualityThreshold,
                                    byte[] pImage, int nImgWidth, int nImgHeight, int iFaceNum,int[] pFaceInfo);
}


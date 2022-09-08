package org.zz.api;

public class MXErrorCode {
    public static final int ERR_OK = 0;//成功
    public static final int ERR_CHECK = 1;//校验失败

    public static final int ERR_INIT = 11;//初始化失败
    public static final int ERR_NO_INIT = 12;//未初始化
    public static final int ERR_FACE_DETECT = 13;//人脸检测失败
    public static final int ERR_FACE_LANDMARK = 14;//关键点定位失败
    public static final int ERR_MULTIPLE_FACES = 15;//多张人脸
    public static final int ERR_FACE_DEWARE = 16;//去网纹失败
    public static final int ERR_TZ_CHECK = 17;//特征格式校验失败
    public static final int ERR_FACE_EXTRACT = 18;//人脸特征提取失败
    public static final int ERR_FACE_MATCH = 19;//人脸比对失败
    public static final int ERR_NO_MODEL_FILE = 20;//模型文件不存在
    public static final int ERR_HASH_INVALID = 21;//Hash不合法
    public static final int ERR_MEMORY_OUT = 22;//内存越界
    public static final int ERR_OUT_FACES = 23;//人脸个数超过额定值（100） 或者 授权算法类型不一致
    public static final int ERR_NO_DETECT_MODEL = 24;//检测模型不存在
    public static final int ERR_NO_QUALITY_MODEL = 25;//质量评价模型不存在
    public static final int ERR_NO_RECOG_MODEL = 26;//识别模型不存在
    public static final int ERR_FACE_SIZE = 27;//待检测人脸图像小于100x100
    public static final int ERR_FACE_SEARCH = 27;//人脸搜索失败

    public static final int ERR_IMAGE_DECODE = 31;//图像解析失败
    public static final int ERR_FACE_QUALITY = 32;//图像质量不达标
    public static final int ERR_QUALITY = 33;//图像质量检测失败

    public static final int ERR_READ_IMAGE = 41;//读取图像失败
    public static final int ERR_FACE_LIV = 42;//活体检测失败
    public static final int ERR_FACE_MASK = 43;//口罩检测失败
    public static final int ERR_FACE_MASK_EXTRACT = 44;//人脸特征提取失败(口罩)
    public static final int ERR_FACE_MASK_REG = 45;//提取人脸特征失败(口罩)
    public static final int ERR_FACE_MASK_MATCH = 46;//特征(带口罩)匹配失败


    public static final int ERR_EXPIRED = 100;//授权过期或无效
    public static final int ERR_INVALID = 101;//授权文件非法
    public static final int ERR_LICENSE_FILE = 102;//授权文件找不到
    public static final int ERR_KEY_INVALID = 103;//授权UKey无效


    //活体检测返回值
    public static final int ERR_FACE_LIV_IS_LIVE = 10000;    //活体
    public static final int ERR_FACE_LIV_IS_UNLIVE = 10001;    //非活体
    public static final int ERR_FACE_LIV_VIS_NO_FACE = 10002;    //可见光输入没有人脸
    public static final int ERR_FACE_LIV_NIS_NO_FACE = 10003;    //近红外输入没有人脸
    public static final int ERR_FACE_LIV_SKIN_FAILED = 10004;    //人脸肤色检测未通过
    public static final int ERR_FACE_LIV_DIST_TOO_CLOSE = 10005;    //请离远一点
    public static final int ERR_FACE_LIV_DIST_TOO_FAR = 10006;    //请离近一点
    public static final int ERR_FACE_LIV_POSE_DET_FAIL = 10007;    //请正对摄像头
    public static final int ERR_FACE_LIV_FACE_CLARITY_DET_FAIL = 10008;    //模糊
    public static final int ERR_FACE_LIV_VIS_EYE_CLOSE = 10009;    //请勿闭眼
    public static final int ERR_FACE_LIV_VIS_MOUTH_OPEN = 10010;    //请勿张嘴
    public static final int ERR_FACE_LIV_VIS_BRIGHTNESS_EXC = 10011;    //过曝
    public static final int ERR_FACE_LIV_VIS_BRIGHTNESS_INS = 10012;   //欠曝
    public static final int ERR_FACE_LIV_VIS_OCCLUSION = 10013;   //遮挡


    public static final int ERR_BUFFER_EMPTY = 1001;//数据为空
    public static final int ERR_IMAGE_SIZE_ILLEGAL = 1002;//图像尺寸不合法
    public static final int ERR_FEATURE_EMPTY = 1003;//特征为空
    public static final int ERR_FEATURE_LENGTH = 1004;//特征长度非法


}

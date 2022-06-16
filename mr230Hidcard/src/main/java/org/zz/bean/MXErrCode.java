package org.zz.bean;

public class MXErrCode {
	// 返回值 Error Code	
	public static final int ERR_NOFINGER   		= 1;   //无手指
	public static final int ERR_OK            	= 0;
	public static final int ERR_NO_DEV		  	= -100;   //无设备
	public static final int ERR_NO_PERMISION  	= -101;   //无访问权限
	public static final int ERR_NO_CONTEXT    	= -102;   //无Context
	
	public static final int ERR_OPEN			= -1;   //打开失败
	public static final int ERR_CANCEL			= -2;	//取消
	public static final int ERR_TIMEOUT			= -3;	//超时
	public static final int ERR_READIMAGE	  	= -4;	//读取图像失败
	public static final int ERR_UPIMAGE			= -5;	//上传图像失败
	
	public static final int ERR_IMAGE_BLANK	    = -9;	//图像为空
	
	public static final int ERR_IOSEND			= -10;	//IO通信发送数据包失败
	public static final int ERR_IORECV			= -11;	//IO通信接收数据包失败
	public static final int ERR_HEAD_FLAG		= -12;	//包标识错误
	public static final int ERR_END_FLAG		= -13;	//包标识错误
	public static final int ERR_CRC		  		= -14;	//数据校验错误
	public static final int ERR_DATA_LEN		= -15;  //数据长度有误
	public static final int ERR_IMG_WDITH		= -16;  //图像宽度有误
	public static final int ERR_IMG_HEIGHT		= -17;  //图像高度有误
	public static final int ERR_MEMORY_OVER 	= -18;	//内存溢出
	public static final int ERR_GET_LOSS_PACK 	= -19;	//获取丢包数据失败
	public static final int ERR_LOSS_PACK 	    = -20;	//丢包数据失败
	public static final int ERR_NO_INFO  	    = -21;	//设备没有内容返回
}





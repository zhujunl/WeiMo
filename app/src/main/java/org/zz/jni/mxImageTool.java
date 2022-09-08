package org.zz.jni;

public class mxImageTool {

	static {
		System.loadLibrary("JustouchFaceAPI");
	}

	public mxImageTool() {
	}

	/**
	 * @author   chen.gs
	 * @category 获取算法信息
	 * @param    szVersion - 输出，算法信息
	 * @return
	 * */
	public native void ImageToolVersion(byte[] szVersion);

	/**
	 * @author   chen.gs
	 * @category 图像文件加载到内存
	 * @param    szLoadFilePath		- 输入	图像路径
	 *           iChannels			- 输入  图像通道数，1-加载为灰度图像，3-加载为RGB图像
	 *            pImgBuf			- 输出	外部图像缓冲区,用于接收图像数据，如果为NULL，则不接收
	 *           oImgWidth			- 输出	图像宽度
	 *           oImgHeight			- 输出	图像高度
	 * @return   1-成功，其他-失败
	 * */
	public native int ImageLoad(String szLoadFilePath, int iChannels,
								byte[] pImgBuf, int[] oImgWidth, int[] oImgHeight);

	/**
	 * @author   chen.gs
	 * @category 保存图像数据
	 * @param    szSaveFilePath		- 输入	保存图像路径
	 *            pImgBuf			- 输入	图像缓冲区
	 *            iImgWidth			- 输入	图像宽度
	 *           iImgHeight			- 输入	图像高度
	 *           iChannels          - 输入  图像通道
	 * @return   1-成功，其他-失败
	 * */
	public native int ImageSave(String szSaveFilePath,
								byte[] pImgBuf, int iImgWidth, int iImgHeight, int iChannels);

	/**
	 * @author   chen.gs
	 * @category YUV数据转换为RGB数据(Android摄像头获取的数据为YUV格式)
	 * @param    pYUVImage	- 输入	YUV图像数据
	 *           iImgWidth	- 输入	图像宽度
	 *           iImgHeight	- 输入	图像高度
	 *           pRGBImage	- 输出	RGB图像数据
	 * @return   1-成功，其他-失败
	 * */
	public native void YUV2RGB(byte[] pYUVImage, int iImgWidth, int iImgHeight,
							   byte[] pRGBImage);

	/**
	 * @author   chen.gs
	 * @category RGB图像数据转换为灰度图像数据
	 * @param    pRGBImage	- 输入	RGB图像数据
	 *           iImgWidth	- 输入	图像宽度
	 *           iImgHeight	- 输入	图像高度
	 *           pGrayImage	- 输出	灰度图像数据
	 * @return   1-成功，其他-失败
	 * */
	public native void RGB2GRAY(byte[] pRGBImage, int iImgWidth, int iImgHeight,byte[]pGrayImage);

	/**
	 * @author   chen.gs
	 * @category 对输入图像根据输入的目标宽度进行按比例缩放。
	 * @param    pImgBuf  		- 输入	图像缓冲区
	 *           iImgWidth		- 输入	图像宽度
	 *           iImgHeight		- 输入	图像高度
	 *           iChannels      - 输入  图像通道
	 *           iDstImgWidth   - 输入  目标图像宽度
	 *           iDstImgHeight	- 输入  目标图像高度
	 *            pDstImgBuf  	- 输出  目标图像缓冲区
	 * @return   1-成功，其他-失败
	 * */
	public native int Zoom(byte[] pImgBuf, int iImgWidth, int iImgHeight, int iChannels,
						   int iDstImgWidth,int iDstImgHeight, byte[] pDstImgBuf);

	/**
	 * @author   chen.gs
	 * @category 在输入的RGB图像上根据输入的Rect绘制矩形框
	 * @param    pRgbImgBuf  		- 输入	RGB图像缓冲区
	 *           iImgWidth		- 输入	图像宽度
	 *           iImgHeight		- 输入	图像高度
	 *            iRect			- 输入	Rect[0]	=x;
	 * 								    Rect[1]	=y;
	 * 									Rect[2]	=width;
	 * 									Rect[3]	=height;
	 *           iPointColor    - 输入  点颜色，0:R,1:G,2:B
	 * @return   1-成功，其他-失败
	 * */
	public native int DrawRect(byte[] pRgbImgBuf, int iImgWidth, int iImgHeight, int[] iRect,int iPointColor);

	/**
	 * @author   chen.gs
	 * @category 在输入的RGB图像上根据输入的点坐标绘制点
	 * @param    pRgbImgBuf  		- 输入	RGB图像缓冲区
	 *           iImgWidth		- 输入	图像宽度
	 *           iImgHeight		- 输入	图像高度
	 *           iPointPos   	- 输入	点坐标序列（x1,y1,x2,y2,...）
	 * 			 iPointNum		- 输入  点个数
	 *           iPointColor    - 输入  点颜色，0:R,1:G,2:B
	 * @return   1-成功，其他-失败
	 * */
	public native int DrawPoint(byte[] pRgbImgBuf, int iImgWidth, int iImgHeight,
								int[] iPointPos, int iPointNum,int iPointColor);

	/**
	 * @author   chen.gs
	 * @category 在输入的RGB图像上根据输入的点坐标绘制点序号
	 * @param    pRgbImgBuf  		- 输入	RGB图像缓冲区
	 *           iImgWidth		- 输入	图像宽度
	 *           iImgHeight		- 输入	图像高度
	 *           iPointX		- 输入	指定位置的X坐标
	 * 			 iPointY		- 输入  指定位置的Y坐标
	 * 			 szText			- 输入  显示文字
	 *           iPointColor    - 输入  点颜色，0:R,1:G,2:B
	 * @return   1-成功，其他-失败
	 * */
	public native int DrawText(byte[] pRgbImgBuf, int iImgWidth, int iImgHeight,
							   int iPointX, int iPointY, String szText,int iPointColor);

	/**
	 * @author   chen.gs
	 * @category 在输入的RGB图像上根据输入的Rect,进行裁剪
	 * @param    pRgbImgBuf  		- 输入	RGB图像缓冲区
	 *           iImgWidth		- 输入	图像宽度
	 *           iImgHeight		- 输入	图像高度
	 *           iRect			- 输入	Rect[0]	=x;
	 * 	 								Rect[1]	=y;
	 * 	 								Rect[2]	=width;
	 * 	 								Rect[3]	=height;
	 * 			 pDstImgBuf  	- 输出	RGB图像缓冲区
	 * 			 iDstWidth		- 输出	图像宽度
	 *         	 iDstHeight		- 输出	图像高度
	 * @return   1-成功，其他-失败
	 * */
	public native int ImageCutRect(byte[] pRgbImgBuf, int iImgWidth, int iImgHeight, int[] iRect,
								   byte[] pDstImgBuf, int[] iDstWidth, int[] iDstHeight);

	/**
	 * @author   chen.gs
	 * @category 图像文件数据转换为RGB24内存数据
	 * @param    pFileDataBuf	- 输入	图像文件数据
	 *           iFileDataLen	- 输入 	图像文件数据长度
	 *           pRGB24Buf		- 输出	RGB24内存数据
	 *           iWidth			- 输出	图像宽度
	 * 	 		 iHeight		- 输出	图像高度
	 * @return   1-成功，其他-失败
	 * */
	public native int ImageDecode(byte[] pFileDataBuf, int iFileDataLen,
								  byte[] pRGB24Buf, int[] iWidth, int[] iHeight);

	/**
	 * @author   chen.gs
	 * @category RGB24内存数据转换为图像文件数据
	 * @param    pRGB24Buf		- 输入	RGB24内存数据
	 *           iWidth			- 输入	图像宽度
	 *           iHeight		- 输入	图像高度
	 * 	 		 szTpye         - 输入  文件后缀,比如.jpg;.bmp
	 * 	 		 pFileDataBuf	- 输出	图像文件数据
	 * 	 		 iFileDataLen	- 输出  图像文件数据长度
	 * @return   1-成功，其他-失败
	 * */
	public native int ImageEncode(byte[] pRGB24Buf, int iWidth, int iHeight,
								  String szTpye, byte[] pFileDataBuf, int[] iFileDataLen);

	/**
	 * @author   chen.gs
	 * @category 输入的RGB图像，进行顺时针90/180/270角度旋转
	 * @param    pRgbImgBuf  		- 输入	RGB图像缓冲区
	 *           iImgWidth			- 输入	图像宽度
	 *           iImgHeight			- 输入	图像高度
	 * 	 		 iAngle             - 输入，角度90/180/270
	 * 	 		 pDstImgBuf  		- 输出	RGB图像缓冲区
	 * 	 		 iDstWidth			- 输出	图像宽度
	 * 	 		 iDstHeight			- 输出	图像高度
	 * @return   1-成功，其他-失败
	 * */
	public native int ImageRotate(byte[] pRgbImgBuf, int iImgWidth, int iImgHeight, int iAngle,
								  byte[] pDstImgBuf, int[] iDstWidth, int[] iDstHeight);

	/**
	 * @author   chen.gs
	 * @category 输入的RGB图像，进行镜像
	 * @param    pRgbImgBuf  		- 输入	RGB图像缓冲区
	 *           iImgWidth			- 输入	图像宽度
	 *           iImgHeight			- 输入	图像高度
	 * 	 		 iIndex              - 输入， >0: 沿y-轴翻转, 0: 沿x-轴翻转, <0: x、y轴同时翻转
	 * 	 		 pDstImgBuf  		- 输出	RGB图像缓冲区
	 * @return   1-成功，其他-失败
	 * */
	public native int ImageFlip(byte[] pRgbImgBuf, int iImgWidth, int iImgHeight, int iIndex,
								byte[] pDstImgBuf);
}

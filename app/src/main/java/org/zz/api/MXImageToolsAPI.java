
package org.zz.api;


import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.miaxis.mr230m.util.StringUtils;

import org.zz.jni.mxImageTool;

public class MXImageToolsAPI {

    final mxImageTool mMxImageTool = new mxImageTool();

    public static MXImageToolsAPI getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final MXImageToolsAPI instance = new MXImageToolsAPI();
    }

    /*
      ================================ 静态内部类单例 ================================
     */

    /**
     * @param pYUVImage - 输入	YUV图像数据
     *                  iImgWidth	- 输入	图像宽度
     *                  iImgHeight	- 输入	图像高度
     *                  pRGBImage	- 输出	RGB图像数据
     * @return 1-成功，其他-失败
     * @author chen.gs
     * @category YUV数据转换为RGB数据(Android摄像头获取的数据为YUV格式)
     */
    public MXResult<byte[]> YUV2RGB(byte[] pYUVImage, int iImgWidth, int iImgHeight) {
        if (pYUVImage == null || pYUVImage.length == 0 || iImgWidth <= 0 || iImgHeight <= 0) {
            return MXResult.CreateFail(-1, "参数不合法");
        }
        byte[] pRGBImage = new byte[iImgWidth * iImgHeight * 3];
        this.mMxImageTool.YUV2RGB(pYUVImage, iImgWidth, iImgHeight, pRGBImage);
        return MXResult.CreateSuccess(pRGBImage);
    }

    /**
     * @param szSaveFilePath - 输入	保存图像路径
     *                       pImgBuf			- 输入	图像缓冲区
     *                       iImgWidth			- 输入	图像宽度
     *                       iImgHeight			- 输入	图像高度
     *                       iChannels          - 输入  图像通道
     * @return 1-成功，其他-失败
     * @author chen.gs
     * @category 保存图像数据
     */
//    public MXResult<?> ImageSave(String szSaveFilePath,
//                                 byte[] pImgBuf, int iImgWidth, int iImgHeight, int iChannels) {
//        if (TextUtils.isEmpty(szSaveFilePath) || pImgBuf == null ||
//                pImgBuf.length == 0 || iImgWidth <= 0 || iImgHeight <= 0 ||
//                iChannels <= 0 || iChannels > 4) {
//            return MXResult.CreateFail(-1, "图片保存参数不合法");
//        }
//        if (!FileUtils.initFile(szSaveFilePath)) {
//            return MXResult.CreateFail(-2, "创建文件失败");
//        }
//        int imageSave = this.mMxImageTool.ImageSave(szSaveFilePath, pImgBuf, iImgWidth, iImgHeight, iChannels);
//        if (imageSave != 1) {
//            return MXResult.CreateFail(-2, "图片保存操作失败");
//        }
//        return MXResult.CreateSuccess();
//    }

    /**
     * @param input - 输入	RGB图像缓冲区
     *              iImgWidth			- 输入	图像宽度
     *              iImgHeight			- 输入	图像高度
     *              iAngle             - 输入，角度90/180/270
     *              pDstImgBuf  		- 输出	RGB图像缓冲区
     *              iDstWidth			- 输出	图像宽度
     *              iDstHeight			- 输出	图像高度
     * @return 1-成功，其他-失败
     * @author chen.gs
     * @category 输入的RGB图像，进行顺时针90/180/270角度旋转
     */
    public MXResult<MxImage> ImageRotate(MxImage input, int iAngle) {
        if (input == null || input.isBufferEmpty() ||
                !input.isSizeLegal() ||
                !(iAngle == 90 || iAngle == 180 || iAngle == 270)) {
            return MXResult.CreateFail(-1, "图片旋转参数不合法");
        }
        byte[] out = new byte[input.width * input.height * 3];
        int[] width = new int[1];
        int[] height = new int[1];
        int imageRotate = this.mMxImageTool.ImageRotate(input.buffer, input.width, input.height,
                iAngle, out, width, height);
        if (imageRotate != 1) {
            return MXResult.CreateFail(-2, "图片旋转操作失败");
        }
        return MXResult.CreateSuccess(new MxImage(width[0], height[0], out));
    }

    public MXResult<MxImage> ImageZoom(MxImage input, int room_width,int room_height) {
        if (input == null || input.isBufferEmpty() ||
                !input.isSizeLegal() ||
                (room_width<=0||room_height<=0)) {
            return MXResult.CreateFail(-1, "图片缩放参数不合法");
        }
        byte[] out = new byte[room_width * room_height * 3];
        int imageZoom = this.mMxImageTool.Zoom(input.buffer, input.width, input.height,
                3, room_width, room_height, out);
        if (imageZoom != 1) {
            return MXResult.CreateFail(-2, "图片缩放操作失败");
        }
        return MXResult.CreateSuccess(new MxImage(room_width, room_height, out));
    }


    /**
     * @param input - 输入	RGB图像缓冲区
     *              iImgWidth		- 输入	图像宽度
     *              iImgHeight		- 输入	图像高度
     *              iRect			- 输入	Rect[0]	=x;
     *              Rect[1]	=y;
     *              Rect[2]	=width;
     *              Rect[3]	=height;
     *              pDstImgBuf  	- 输出	RGB图像缓冲区
     *              iDstWidth		- 输出	图像宽度
     *              iDstHeight		- 输出	图像高度
     * @return 1-成功，其他-失败
     * @author chen.gs
     * @category 在输入的RGB图像上根据输入的Rect, 进行裁剪
     */
    public MXResult<MxImage> ImageCutRect(MxImage input, Rect rect) {
        if (input == null || input.isBufferEmpty() ||
                !input.isSizeLegal() || rect == null || rect.isEmpty() ||
                rect.top <= 0 || rect.left <= 0 || rect.right <= 0 || rect.bottom <= 0) {
            return MXResult.CreateFail(-1, "图片裁剪参数不合法");
        }
        int[] rects = new int[]{rect.left, rect.top, rect.width(), rect.height()};
        byte[] out = new byte[input.width * input.height * 3];
        int[] width = new int[1];
        int[] height = new int[1];
        int imageCutRect = this.mMxImageTool.ImageCutRect(input.buffer, input.width, input.height,
                rects, out, width, height);
        if (imageCutRect != 1) {
            return MXResult.CreateFail(-2, "图片裁剪操作失败");
        }
        return MXResult.CreateSuccess(new MxImage(width[0], height[0], out));
    }


    /**
     * @param imagePath - 输入	图像路径
     *                  iChannels			- 输入  图像通道数，1-加载为灰度图像，3-加载为RGB图像
     *                  pImgBuf			- 输出	外部图像缓冲区,用于接收图像数据，如果为NULL，则不接收
     *                  oImgWidth			- 输出	图像宽度
     *                  oImgHeight			- 输出	图像高度
     * @return 1-成功，其他-失败
     * @author chen.gs
     * @category 图像文件加载到内存
     */
    public MXResult<MxImage> ImageLoad(String imagePath, int channel) {
        if (StringUtils.isNullOrEmpty(imagePath) || channel <= 0) {
            return MXResult.CreateFail(-1, "加载图片参数不合法," + imagePath);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            return MXResult.CreateFail(-1, "图片加载失败," + imagePath);
        }
        byte[] out = new byte[options.outWidth * options.outHeight * channel];
        int[] width = new int[1];
        int[] height = new int[1];
        int imageLoad = this.mMxImageTool.ImageLoad(imagePath, channel, out, width, height);
        if (imageLoad != 1) {
            return MXResult.CreateFail(-2, "加载图片操作失败," + imagePath);
        }
        return MXResult.CreateSuccess(new MxImage(width[0], height[0], out));
    }

    public mxImageTool getMxImageTool() {
        return mMxImageTool;
    }

}

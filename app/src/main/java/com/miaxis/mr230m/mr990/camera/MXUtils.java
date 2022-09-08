package com.miaxis.mr230m.mr990.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author Tank
 * @date 2021/7/23 3:31 下午
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MXUtils {

    public static Bitmap rotate(Bitmap bitmap, int rotate) {
        Matrix matrix = new Matrix();
        matrix.preRotate(rotate);
        return crop(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), matrix);
    }

    public static Bitmap crop(Bitmap bitmap, Rect rect) {
        return crop(bitmap, rect, new Matrix());
    }

    public static Bitmap crop(Bitmap bitmap, Rect rect, Matrix matrix) {
        if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
            return null;
        }
        if (rect == null || rect.left < 0 || rect.top < 0
                || (rect.left + rect.width() > bitmap.getWidth())
                || (rect.top + rect.height() > bitmap.getHeight())) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height(), matrix, true);
    }

    public static byte[] NV21toRgba(byte[] nv21, int width, int height, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Rect rect = new Rect(0, 0, width, height);
            boolean compressToJpeg = compressToJpeg(nv21, width, height, quality, rect, byteArrayOutputStream);
            if (compressToJpeg) {
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
                int bytes = bitmap.getByteCount();
                ByteBuffer buffer = ByteBuffer.allocate(bytes);
                bitmap.copyPixelsToBuffer(buffer);
                return buffer.array();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(byteArrayOutputStream);
        }
        return null;
    }

    public static byte[] NV21toJPEG(byte[] nv21, int width, int height, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Rect rect = new Rect(0, 0, width, height);
            boolean compressToJpeg = compressToJpeg(nv21, width, height, quality, rect, byteArrayOutputStream);
            if (compressToJpeg) {
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(byteArrayOutputStream);
        }
        return null;
    }

    public static File NV21toJPEGFile(String savePath, byte[] nv21, int width, int height, int quality) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    boolean mkdirs = parentFile.mkdirs();
                }
            }
            Rect rect = new Rect(0, 0, width, height);
            fileOutputStream = new FileOutputStream(file);
            boolean compressToJpeg = compressToJpeg(nv21, width, height, quality, rect, fileOutputStream);
            if (compressToJpeg) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(fileOutputStream);
        }
        return null;
    }

    public static boolean compressToJpeg(byte[] nv21, int width, int height, int quality, Rect rect, OutputStream outputStream) {
        if (nv21 == null || nv21.length == 0 || width <= 0 || height <= 0 || quality <= 0) {
            return false;
        }
        if (rect == null || rect.left < 0 || rect.top < 0 || (rect.left + rect.width() > width) || (rect.top + rect.height() > height)) {
            return false;
        }
        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        return yuv.compressToJpeg(new Rect(0, 0, width, height), quality, outputStream);
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

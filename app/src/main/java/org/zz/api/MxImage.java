package org.zz.api;

/**
 * @author Tank
 * @date 2021/8/21 6:20 下午
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MxImage {

    public int width;
    public int height;
    public byte[] buffer;

    public MxImage(int width, int height, byte[] buffer) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    public boolean isBufferEmpty() {
        return buffer == null || buffer.length == 0;
    }

    public boolean isSizeLegal() {
        return this.width > 0 && this.height > 0;
    }

    public static boolean isBufferEmpty(MXFrame frame) {
        return frame == null || frame.isBufferEmpty();
    }

    public static boolean isSizeLegal(MXFrame frame) {
        return frame != null && frame.isSizeLegal();
    }

    @Override
    public String toString() {
        return "MxImage{" +
                "width=" + width +
                ", height=" + height +
                ", buffer=" + (buffer == null ? null : buffer.length) +
                '}';
    }
}

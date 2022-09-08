package org.zz.api;

/**
 * @author Tank
 * @version $
 * @des 视频帧数据
 * @updateAuthor $
 * @updateDes
 */
public class MXFrame {

    public int width;
    public int height;
    public byte[] buffer;
    public int orientation;

    public MXFrame(byte[] buffer, int width, int height, int orientation) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
        this.orientation = orientation;
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
        return "MXFrame{" +
                "width=" + width +
                ", height=" + height +
                ", orientation=" + orientation +
                ", buffer=" + (buffer == null ? null : buffer.length) +
                '}';
    }
}

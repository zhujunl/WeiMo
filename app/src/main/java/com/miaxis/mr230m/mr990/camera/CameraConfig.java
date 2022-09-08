package com.miaxis.mr230m.mr990.camera;

/**
 * @author Tank
 * @date 2021/5/16 13:19
 * @des
 * @updateAuthor
 * @updateDes
 */
public enum CameraConfig {

    Camera_RGB(0, 640, 480, 270, 90, true),
    Camera_NIR(1, 640, 480, 270, 90, false);

    public int CameraId;
    public int width;
    public int height;
    public int previewOrientation;
    public int bufferOrientation;
    public boolean mirror;

    CameraConfig(int cameraId, int width, int height, int previewOrientation, int bufferOrientation, boolean mirror) {
        this.CameraId = cameraId;
        this.width = width;
        this.height = height;
        this.previewOrientation = previewOrientation;
        this.bufferOrientation = bufferOrientation;
        this.mirror = mirror;
    }

    @Override
    public String toString() {
        return "CameraConfig{" +
                "CameraId=" + CameraId +
                ", width=" + width +
                ", height=" + height +
                ", previewOrientation=" + previewOrientation +
                ", bufferOrientation=" + bufferOrientation +
                '}';
    }

}

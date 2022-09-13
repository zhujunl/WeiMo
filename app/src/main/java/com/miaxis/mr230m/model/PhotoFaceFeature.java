package com.miaxis.mr230m.model;

import java.util.Arrays;

public class PhotoFaceFeature {

    private byte[] faceFeature;
    private String message;

    public PhotoFaceFeature(String message) {
        this.message = message;
    }

    public PhotoFaceFeature(byte[] faceFeature, String message) {
        this.faceFeature = faceFeature;
        this.message = message;
    }

    public byte[] getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(byte[] faceFeature) {
        this.faceFeature = faceFeature;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PhotoFaceFeature{" +
                "faceFeature=" + Arrays.toString(faceFeature) +
                ", message='" + message + '\'' +
                '}';
    }
}

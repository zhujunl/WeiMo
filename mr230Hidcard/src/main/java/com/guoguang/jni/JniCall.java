package com.guoguang.jni;

public class JniCall
{
    static {
        System.loadLibrary("dewlt2-jni");
    }
    
    private static native int wlt2bmp(final byte[] p0, final byte[] p1, final int p2);
    
    public static int Huaxu_Wlt2Bmp(final byte[] wlt, final byte[] bmp, final int bmpSave) {
        return wlt2bmp(wlt, bmp, bmpSave);
    }
}

package org.zz.facematchdemo;

public class Config {
    public static final String LicensePath = "/sdcard/miaxis/FaceId_ST/st_lic.txt";

    public static final String strImgFile1 = "face_1.jpg";
    public static final String strImgFile2 = "face_2.jpg";
    public static final String strTzFile1 = "face_tz_1.dat";
    public static final String strTzFile2 = "face_tz_2.dat";

    public static final String strTzFile1_mask = "face_tz_1_mask.dat";
    public static final String strTzFile2_mask = "face_tz_2_mask.dat";

    public static final String RootDbPath = "/sdcard/miaxis_db/";
    public static final String ImgDbPath = "/sdcard/miaxis_db/enroll_img/";
    public static final String TzDbPath = "/sdcard/miaxis_db/enroll_tz/";
    public static final String TzMaskDbPath = "/sdcard/miaxis_db/enroll_tz_mask/";

    public static int maxSearchNum = 10000;
    public static int matchThreshold = 76;
    public static int maskMatchThreshold = 73;
    public static int qualityThreshold = 30;
    public static int quality4RegThreshold = 90;
}

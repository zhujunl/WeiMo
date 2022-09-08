package org.zz.api;

public class MXFaceInfoEx {
    public static final int iMaxFaceNum = 100;
    public static final int MAX_KEY_POINT_NUM = 120;
    public static final int SIZE = (22 + 2 * MAX_KEY_POINT_NUM);
    //face rect人脸框
    public int x;        // Upper left x-coordinate
    public int y;        // Upper left y-coordinate
    public int width;    // Face width
    public int height;   // Face height

    //face_point关键点
    public int keypt_num;                                    // Number of key points
    public int[] keypt_x = new int[MAX_KEY_POINT_NUM];      // Key x-coordinate
    public int[] keypt_y = new int[MAX_KEY_POINT_NUM];      // Key y-coordinate

    // 人脸属性
    public int age;
    public int gender;
    public int expression;

    //人脸质量分
    public int quality;     // 总分，0~100之间，越大则人脸质量越好.

    public int eyeDistance;    // 瞳距
    public int liveness;        // 活体，0~100之间
    public int detected;        // 1: 检测到的人脸,.0：跟踪到的人脸
    // 注： 跟踪到的仅ID和人脸框数据有效
    public int trackId;            // 人脸ID（ID<0表示没有进入跟踪）

    public int idmax;              //获取交并比最大的人脸下标
    public int reCog;              //判断该人脸是否被识别-识别标识
    public int reCogId;
    public int reCogScore;

    public int mask;              //口罩分数0~100，建议大于40认为是有口罩
    public int stranger;          //陌生人标识位

    // head_pose头部姿态
    public int pitch;         // 抬头、低头,范围-90到90，越大表示越抬头
    public int yaw;           // 左右转头
    public int roll;          // 平面内偏头


    public static void Int2MXFaceInfoEx(int[] iFaceInfo, MXFaceInfoEx pMXFaceInfoEx) {
        pMXFaceInfoEx.x = iFaceInfo[0];
        pMXFaceInfoEx.y = iFaceInfo[1];
        pMXFaceInfoEx.width = iFaceInfo[2];
        pMXFaceInfoEx.height = iFaceInfo[3];
        pMXFaceInfoEx.keypt_num = iFaceInfo[4];
        for (int j = 0; j < MXFaceInfoEx.MAX_KEY_POINT_NUM; j++) {
            pMXFaceInfoEx.keypt_x[j] = iFaceInfo[5 + j];
            pMXFaceInfoEx.keypt_y[j] = iFaceInfo[5 + j + MXFaceInfoEx.MAX_KEY_POINT_NUM];
        }
        pMXFaceInfoEx.age = iFaceInfo[5 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.gender = iFaceInfo[6 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.expression = iFaceInfo[7 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.quality = iFaceInfo[8 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.eyeDistance = iFaceInfo[9 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.liveness = iFaceInfo[10 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.detected = iFaceInfo[11 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.trackId = iFaceInfo[12 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.idmax = iFaceInfo[13 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.reCog = iFaceInfo[14 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.reCogId = iFaceInfo[15 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.reCogScore = iFaceInfo[16 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.mask = iFaceInfo[17 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.stranger = iFaceInfo[18 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.pitch = iFaceInfo[19 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.yaw = iFaceInfo[20 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        pMXFaceInfoEx.roll = iFaceInfo[21 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
    }

    public static int Ints2MXFaceInfoExs(int iFaceNum, int[] iFaceInfos, MXFaceInfoEx[] pMXFaceInfoExs) {
        for (int i = 0; i < iFaceNum; i++) {
            pMXFaceInfoExs[i].x = iFaceInfos[i * MXFaceInfoEx.SIZE];
            pMXFaceInfoExs[i].y = iFaceInfos[i * MXFaceInfoEx.SIZE + 1];
            pMXFaceInfoExs[i].width = iFaceInfos[i * MXFaceInfoEx.SIZE + 2];
            pMXFaceInfoExs[i].height = iFaceInfos[i * MXFaceInfoEx.SIZE + 3];
            pMXFaceInfoExs[i].keypt_num = iFaceInfos[i * MXFaceInfoEx.SIZE + 4];
            for (int j = 0; j < MXFaceInfoEx.MAX_KEY_POINT_NUM; j++) {
                pMXFaceInfoExs[i].keypt_x[j] = iFaceInfos[i * MXFaceInfoEx.SIZE + 5 + j];
                pMXFaceInfoExs[i].keypt_y[j] = iFaceInfos[i * MXFaceInfoEx.SIZE + 5 + j + MXFaceInfoEx.MAX_KEY_POINT_NUM];
            }
            pMXFaceInfoExs[i].age = iFaceInfos[i * MXFaceInfoEx.SIZE + 5 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].gender = iFaceInfos[i * MXFaceInfoEx.SIZE + 6 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].expression = iFaceInfos[i * MXFaceInfoEx.SIZE + 7 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].quality = iFaceInfos[i * MXFaceInfoEx.SIZE + 8 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].eyeDistance = iFaceInfos[i * MXFaceInfoEx.SIZE + 9 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].liveness = iFaceInfos[i * MXFaceInfoEx.SIZE + 10 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].detected = iFaceInfos[i * MXFaceInfoEx.SIZE + 11 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].trackId = iFaceInfos[i * MXFaceInfoEx.SIZE + 12 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].idmax = iFaceInfos[i * MXFaceInfoEx.SIZE + 13 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].reCog = iFaceInfos[i * MXFaceInfoEx.SIZE + 14 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].reCogId = iFaceInfos[i * MXFaceInfoEx.SIZE + 15 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].reCogScore = iFaceInfos[i * MXFaceInfoEx.SIZE + 16 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].mask = iFaceInfos[i * MXFaceInfoEx.SIZE + 17 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].stranger = iFaceInfos[i * MXFaceInfoEx.SIZE + 18 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].pitch = iFaceInfos[i * MXFaceInfoEx.SIZE + 19 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].yaw = iFaceInfos[i * MXFaceInfoEx.SIZE + 20 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
            pMXFaceInfoExs[i].roll = iFaceInfos[i * MXFaceInfoEx.SIZE + 21 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM];
        }
        return 0;
    }

    public static int MXFaceInfoExs2Ints(int iFaceNum, int[] iFaceInfos, MXFaceInfoEx[] pMXFaceInfoExs) {
        for (int i = 0; i < iFaceNum; i++) {
            iFaceInfos[i * MXFaceInfoEx.SIZE] = pMXFaceInfoExs[i].x;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 1] = pMXFaceInfoExs[i].y;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 2] = pMXFaceInfoExs[i].width;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 3] = pMXFaceInfoExs[i].height;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 4] = pMXFaceInfoExs[i].keypt_num;
            for (int j = 0; j < MXFaceInfoEx.MAX_KEY_POINT_NUM; j++) {
                iFaceInfos[i * MXFaceInfoEx.SIZE + 5 + j] = pMXFaceInfoExs[i].keypt_x[j];
                iFaceInfos[i * MXFaceInfoEx.SIZE + 5 + j + MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].keypt_y[j];
            }
            iFaceInfos[i * MXFaceInfoEx.SIZE + 5 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].age;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 6 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].gender;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 7 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].expression;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 8 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].quality;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 9 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].eyeDistance;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 10 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].liveness;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 11 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].detected;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 12 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].trackId;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 13 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].idmax;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 14 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].reCog;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 15 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].reCogId;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 16 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].reCogScore;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 17 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].mask;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 18 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].stranger;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 19 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].pitch;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 20 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].yaw;
            iFaceInfos[i * MXFaceInfoEx.SIZE + 21 + 2 * MXFaceInfoEx.MAX_KEY_POINT_NUM] = pMXFaceInfoExs[i].roll;
        }
        return 0;
    }

    public static int Copy(int iFaceNum, MXFaceInfoEx[] pMXFaceInfoExs, MXFaceInfoEx[] pMXFaceInfoExDsts) {
        for (int i = 0; i < iFaceNum; i++) {
            pMXFaceInfoExDsts[i].x = pMXFaceInfoExs[i].x;
            pMXFaceInfoExDsts[i].y = pMXFaceInfoExs[i].y;
            pMXFaceInfoExDsts[i].width = pMXFaceInfoExs[i].width;
            pMXFaceInfoExDsts[i].height = pMXFaceInfoExs[i].height;
            pMXFaceInfoExDsts[i].keypt_num = pMXFaceInfoExs[i].keypt_num;
            for (int j = 0; j < pMXFaceInfoExs[i].keypt_num; j++) {
                pMXFaceInfoExDsts[i].keypt_x[j] = pMXFaceInfoExs[i].keypt_x[j];
                pMXFaceInfoExDsts[i].keypt_y[j] = pMXFaceInfoExs[i].keypt_y[j];
            }
            for (int j = 0; j < 2; j++) {
                pMXFaceInfoExDsts[i].keypt_x[j + 5] = pMXFaceInfoExs[i].keypt_x[j + 5];
                pMXFaceInfoExDsts[i].keypt_y[j + 5] = pMXFaceInfoExs[i].keypt_y[j + 5];
            }
            pMXFaceInfoExDsts[i].age = pMXFaceInfoExs[i].age;
            pMXFaceInfoExDsts[i].gender = pMXFaceInfoExs[i].gender;
            pMXFaceInfoExDsts[i].expression = pMXFaceInfoExs[i].expression;
            pMXFaceInfoExDsts[i].quality = pMXFaceInfoExs[i].quality;
            pMXFaceInfoExDsts[i].eyeDistance = pMXFaceInfoExs[i].eyeDistance;
            pMXFaceInfoExDsts[i].liveness = pMXFaceInfoExs[i].liveness;
            pMXFaceInfoExDsts[i].detected = pMXFaceInfoExs[i].detected;
            pMXFaceInfoExDsts[i].trackId = pMXFaceInfoExs[i].trackId;
            pMXFaceInfoExDsts[i].idmax = pMXFaceInfoExs[i].idmax;
            pMXFaceInfoExDsts[i].reCog = pMXFaceInfoExs[i].reCog;
            pMXFaceInfoExDsts[i].reCogId = pMXFaceInfoExs[i].reCogId;
            pMXFaceInfoExDsts[i].reCogScore = pMXFaceInfoExs[i].reCogScore;
            pMXFaceInfoExDsts[i].mask = pMXFaceInfoExs[i].mask;
            pMXFaceInfoExDsts[i].stranger = pMXFaceInfoExs[i].stranger;
            pMXFaceInfoExDsts[i].pitch = pMXFaceInfoExs[i].pitch;
            pMXFaceInfoExDsts[i].yaw = pMXFaceInfoExs[i].yaw;
            pMXFaceInfoExDsts[i].roll = pMXFaceInfoExs[i].roll;
        }
        return 0;
    }

    public static MXFaceInfoEx Copy(MXFaceInfoEx pMXFaceInfoExs) {
        MXFaceInfoEx out = new MXFaceInfoEx();
        out.x = pMXFaceInfoExs.x;
        out.y = pMXFaceInfoExs.y;
        out.width = pMXFaceInfoExs.width;
        out.height = pMXFaceInfoExs.height;
        out.keypt_num = pMXFaceInfoExs.keypt_num;
        //for (int j = 0; j < pMXFaceInfoExs.keypt_num; j++) {
        //    out.keypt_x[j] = pMXFaceInfoExs.keypt_x[j];
        //    out.keypt_y[j] = pMXFaceInfoExs.keypt_y[j];
        //}
        System.arraycopy(pMXFaceInfoExs.keypt_x, 0, out.keypt_x, 0, pMXFaceInfoExs.keypt_num);
        System.arraycopy(pMXFaceInfoExs.keypt_y, 0, out.keypt_y, 0, pMXFaceInfoExs.keypt_num);
        //for (int j = 0; j < 2; j++) {
        //    out.keypt_x[j + 5] = pMXFaceInfoExs.keypt_x[j + 5];
        //    out.keypt_y[j + 5] = pMXFaceInfoExs.keypt_y[j + 5];
        //}
        out.age = pMXFaceInfoExs.age;
        out.gender = pMXFaceInfoExs.gender;
        out.expression = pMXFaceInfoExs.expression;
        out.quality = pMXFaceInfoExs.quality;
        out.eyeDistance = pMXFaceInfoExs.eyeDistance;
        out.liveness = pMXFaceInfoExs.liveness;
        out.detected = pMXFaceInfoExs.detected;
        out.trackId = pMXFaceInfoExs.trackId;
        out.idmax = pMXFaceInfoExs.idmax;
        out.reCog = pMXFaceInfoExs.reCog;
        out.reCogId = pMXFaceInfoExs.reCogId;
        out.reCogScore = pMXFaceInfoExs.reCogScore;
        out.mask = pMXFaceInfoExs.mask;
        out.stranger = pMXFaceInfoExs.stranger;
        out.pitch = pMXFaceInfoExs.pitch;
        out.yaw = pMXFaceInfoExs.yaw;
        out.roll = pMXFaceInfoExs.roll;
        return out;
    }

    @Override
    public String toString() {
        return "MXFaceInfoEx{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", keypt_num=" + keypt_num +
                ", keypt_x=" + (keypt_x == null ? null : keypt_x.length) +
                ", keypt_y=" + (keypt_y == null ? null : keypt_y.length) +
                ", age=" + age +
                ", gender=" + gender +
                ", expression=" + expression +
                ", quality=" + quality +
                ", eyeDistance=" + eyeDistance +
                ", liveness=" + liveness +
                ", detected=" + detected +
                ", trackId=" + trackId +
                ", idmax=" + idmax +
                ", reCog=" + reCog +
                ", reCogId=" + reCogId +
                ", reCogScore=" + reCogScore +
                ", mask=" + mask +
                ", stranger=" + stranger +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", roll=" + roll +
                '}';
    }
}

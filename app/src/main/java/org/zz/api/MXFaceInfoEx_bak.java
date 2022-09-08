package org.zz.api;

public class MXFaceInfoEx_bak {
	public static final int iMaxFaceNum        = 100;
	public static final int MAX_KEY_POINT_NUM  = 110;
	public static final int SIZE = (42+2*MAX_KEY_POINT_NUM);
	//face rect人脸框
	public int x;        // 左上角x坐标
	public int y;        // 坐上角y坐标
	public int width;    // 人脸宽
	public int height;   // 人脸高

	//face_point关键点
	public int keypt_num;						            // 关键点个数                               // 关键点得分
	public int[] keypt_x = new int[MAX_KEY_POINT_NUM];      // 关键点x坐标
	public int[] keypt_y = new int[MAX_KEY_POINT_NUM];      // 关键点y坐标

	// 人脸质量分
	public int quality;        //注册：>=90,  比对/识别：>=60
	// 人脸属性
	public int probability;    //人脸置信度
	public int completeness;   //人脸完整性,	 =0 提示：把脸移入框内
	public int eyeDistance;    //瞳距，		<30 提示：请靠近摄像头
	public int illumination;   //光照，		<50 提示：脸部过暗，>200 提示：脸部过亮
	public int blur;           //模糊，		>30 提示：图像模糊
	public int[] occlusion = new int[10];   //遮挡,         >=10,提示：对应区域有遮挡
	//0-total，1-lefteye，2-righteye，3-nose，4-mouth，
	//5-leftcheck，6-rightcheck，7-chin
	public int expression;     //表情
	public int skin;           //肤色
	public int makeup;         //浓妆
	public int leye_status;    //左眼睁闭程度
	public int reye_status;    //右眼睁闭程度
	public int mouth_status;   //嘴巴张闭程度
	public int glasses;        //眼镜
	public int mask;           //口罩
	public int liveness;       //活体
	public int age;            //年龄
	public int gender;         //性别
	// head_pose头部姿态
	public int pitch;         // 抬头、低头， >20-提示：请正对摄像头
	public int yaw;           // 左右转头，   >20-提示：请正对摄像头
	public int roll;          // 平面内偏头， >20-提示：请正对摄像头

	// 人脸ID
	public int detected;       //1-检测到的人脸,0-跟踪到的人脸
	public int trackId;        //人脸ID（ID<0表示没有进入跟踪）
	public int idmax;          //获取交并比最大的人脸下标
	public int reCog;          //判断该人脸是否被识别-识别标识
	public int reCogId;        //数据库中的识别ID
	public int reCogScore;     //数据库中识别ID的分数
	public int stranger;       //陌生人标识位

	public static void Int2MXFaceInfoEx(int[] iFaceInfo, MXFaceInfoEx_bak pMXFaceInfoEx) {
		pMXFaceInfoEx.x         = iFaceInfo[ MXFaceInfoEx_bak.SIZE];
		pMXFaceInfoEx.y         = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 1];
		pMXFaceInfoEx.width     = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 2];
		pMXFaceInfoEx.height    = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 3];
		pMXFaceInfoEx.keypt_num = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 4];
		for (int j = 0; j < MAX_KEY_POINT_NUM; j++)
		{
			pMXFaceInfoEx.keypt_x[j] = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 5 + j];
			pMXFaceInfoEx.keypt_y[j] = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 5 + j + MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		}
		pMXFaceInfoEx.quality      = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 5 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.probability  = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 6 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.completeness = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 7 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.eyeDistance  = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 8 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.illumination = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 9 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.blur         = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 10 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		for (int k = 0; k < 10; k++)
		{
			pMXFaceInfoEx.occlusion[k] = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 11 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM + k];
		}
		pMXFaceInfoEx.expression   = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 21 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.skin         = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 22 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.makeup       = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 23 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.leye_status  = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 24 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.reye_status  = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 25 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.mouth_status = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 26 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.glasses      = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 27 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.mask         = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 28 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.liveness     = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 29 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.age          = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 30 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.gender       = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 31 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.pitch        = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 32 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.yaw          = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 33 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.roll         = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 34 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.detected     = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 35 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.trackId      = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 36 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.idmax        = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 37 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.reCog        = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 38 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.reCogId      = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 39 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.reCogScore   = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 40 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		pMXFaceInfoEx.stranger     = iFaceInfo[ MXFaceInfoEx_bak.SIZE + 41 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
	}

	public static int Ints2MXFaceInfoExs(int iFaceNum, int[] iFaceInfos, MXFaceInfoEx_bak[] pMXFaceInfoExs) {
		for (int i = 0; i < iFaceNum; i++) {
			pMXFaceInfoExs[i].x         = iFaceInfos[i * MXFaceInfoEx_bak.SIZE];
			pMXFaceInfoExs[i].y         = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 1];
			pMXFaceInfoExs[i].width     = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 2];
			pMXFaceInfoExs[i].height    = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 3];
			pMXFaceInfoExs[i].keypt_num = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 4];
			for (int j = 0; j < MAX_KEY_POINT_NUM; j++)
			{
				pMXFaceInfoExs[i].keypt_x[j] = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 5 + j];
				pMXFaceInfoExs[i].keypt_y[j] = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 5 + j + MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			}
			pMXFaceInfoExs[i].quality      = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 5 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].probability  = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 6 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].completeness = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 7 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].eyeDistance  = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 8 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].illumination = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 9 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].blur         = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 10 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			for (int k = 0; k < 10; k++)
			{
				pMXFaceInfoExs[i].occlusion[k] = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 11 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM + k];
			}
			pMXFaceInfoExs[i].expression   = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 21 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].skin         = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 22 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].makeup       = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 23 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].leye_status  = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 24 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].reye_status  = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 25 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].mouth_status = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 26 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].glasses      = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 27 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].mask         = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 28 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].liveness     = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 29 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].age          = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 30 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].gender       = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 31 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].pitch        = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 32 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].yaw          = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 33 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].roll         = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 34 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].detected     = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 35 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].trackId      = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 36 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].idmax        = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 37 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].reCog        = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 38 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].reCogId      = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 39 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].reCogScore   = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 40 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoExs[i].stranger     = iFaceInfos[i * MXFaceInfoEx_bak.SIZE + 41 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		}
		return 0;
	}
	public static int Int2MXFaceInfoEx(int iFaceNum, int[] iFaceInfo, MXFaceInfoEx_bak[] pMXFaceInfoEx) {
		for (int i = 0; i < iFaceNum; i++) {
			pMXFaceInfoEx[i].x         = iFaceInfo[i * MXFaceInfoEx_bak.SIZE];
			pMXFaceInfoEx[i].y         = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 1];
			pMXFaceInfoEx[i].width     = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 2];
			pMXFaceInfoEx[i].height    = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 3];
			pMXFaceInfoEx[i].keypt_num = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 4];
			for (int j = 0; j < MAX_KEY_POINT_NUM; j++)
			{
				pMXFaceInfoEx[i].keypt_x[j] = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 5 + j];
				pMXFaceInfoEx[i].keypt_y[j] = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 5 + j + MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			}
			pMXFaceInfoEx[i].quality      = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 5 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].probability  = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 6 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].completeness = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 7 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].eyeDistance  = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 8 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].illumination = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 9 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].blur         = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 10 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			for (int k = 0; k < 10; k++)
			{
				pMXFaceInfoEx[i].occlusion[k] = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 11 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM + k];
			}
			pMXFaceInfoEx[i].expression   = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 21 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].skin         = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 22 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].makeup       = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 23 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].leye_status  = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 24 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].reye_status  = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 25 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].mouth_status = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 26 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].glasses      = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 27 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].mask         = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 28 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].liveness     = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 29 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].age          = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 30 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].gender       = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 31 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].pitch        = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 32 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].yaw          = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 33 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].roll         = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 34 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].detected     = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 35 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].trackId      = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 36 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].idmax        = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 37 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].reCog        = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 38 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].reCogId      = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 39 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].reCogScore   = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 40 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
			pMXFaceInfoEx[i].stranger     = iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 41 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM];
		}
		return 0;
	}

	public static int MXFaceInfoEx2Int(int iFaceNum, int[] iFaceInfo, MXFaceInfoEx_bak[] pMXFaceInfoEx) {
		for (int i = 0; i < iFaceNum; i++) {
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE]     = pMXFaceInfoEx[i].x;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 1] = pMXFaceInfoEx[i].y;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 2] = pMXFaceInfoEx[i].width;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 3] = pMXFaceInfoEx[i].height;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 4] = pMXFaceInfoEx[i].keypt_num;
			for (int j = 0; j < MAX_KEY_POINT_NUM; j++)
			{
				iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 5 + j] = pMXFaceInfoEx[i].keypt_x[j];
				iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 5 + j + MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].keypt_y[j];
			}

			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 5 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM]  = pMXFaceInfoEx[i].quality;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 6 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM]  = pMXFaceInfoEx[i].probability;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 7 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM]  = pMXFaceInfoEx[i].completeness;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 8 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM]  = pMXFaceInfoEx[i].eyeDistance;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 9 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM]  = pMXFaceInfoEx[i].illumination;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 10 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].blur;
			for (int k = 0; k < 10; k++)
			{
				iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 11 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM + k] = pMXFaceInfoEx[i].occlusion[k];
			}
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 21 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].expression;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 22 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].skin;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 23 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].makeup;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 24 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].leye_status;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 25 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].reye_status;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 26 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].mouth_status;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 27 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].glasses;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 28 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].mask;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 29 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].liveness;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 30 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].age;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 31 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].gender;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 32 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].pitch;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 33 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].yaw;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 34 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].roll;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 35 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].detected;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 36 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].trackId;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 37 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].idmax;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 38 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].reCog;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 39 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].reCogId;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 40 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].reCogScore;
			iFaceInfo[i * MXFaceInfoEx_bak.SIZE + 41 + 2 * MXFaceInfoEx_bak.MAX_KEY_POINT_NUM] = pMXFaceInfoEx[i].stranger;
		}
		return 0;
	}

	public static int Copy(int iFaceNum, MXFaceInfoEx_bak[] pMXFaceInfoEx, MXFaceInfoEx_bak[] pMXFaceInfoExDst) {
		for (int i = 0; i < iFaceNum; i++) {
			pMXFaceInfoExDst[i].x         = pMXFaceInfoEx[i].x;
			pMXFaceInfoExDst[i].y         = pMXFaceInfoEx[i].y;
			pMXFaceInfoExDst[i].width     = pMXFaceInfoEx[i].width ;
			pMXFaceInfoExDst[i].height    = pMXFaceInfoEx[i].height;
			pMXFaceInfoExDst[i].keypt_num = pMXFaceInfoEx[i].keypt_num;
			for (int j = 0; j < MAX_KEY_POINT_NUM; j++)
			{
				pMXFaceInfoExDst[i].keypt_x[j] = pMXFaceInfoEx[i].keypt_x[j];
				pMXFaceInfoExDst[i].keypt_y[j] = pMXFaceInfoEx[i].keypt_y[j];
			}
			pMXFaceInfoExDst[i].quality      = pMXFaceInfoEx[i].quality;
			pMXFaceInfoExDst[i].probability  = pMXFaceInfoEx[i].probability;
			pMXFaceInfoExDst[i].completeness = pMXFaceInfoEx[i].completeness;
			pMXFaceInfoExDst[i].eyeDistance  = pMXFaceInfoEx[i].eyeDistance;
			pMXFaceInfoExDst[i].illumination = pMXFaceInfoEx[i].illumination;
			pMXFaceInfoExDst[i].blur         = pMXFaceInfoEx[i].blur;
			System.arraycopy(pMXFaceInfoEx[i].occlusion, 0, pMXFaceInfoExDst[i].occlusion, 0, 10);
			pMXFaceInfoExDst[i].expression   = pMXFaceInfoEx[i].expression;
			pMXFaceInfoExDst[i].skin         = pMXFaceInfoEx[i].skin;
			pMXFaceInfoExDst[i].makeup       = pMXFaceInfoEx[i].makeup;
			pMXFaceInfoExDst[i].leye_status  = pMXFaceInfoEx[i].leye_status;
			pMXFaceInfoExDst[i].reye_status  = pMXFaceInfoEx[i].reye_status;
			pMXFaceInfoExDst[i].mouth_status = pMXFaceInfoEx[i].mouth_status;
			pMXFaceInfoExDst[i].glasses      = pMXFaceInfoEx[i].glasses;
			pMXFaceInfoExDst[i].mask         = pMXFaceInfoEx[i].mask;
			pMXFaceInfoExDst[i].liveness     = pMXFaceInfoEx[i].liveness;
			pMXFaceInfoExDst[i].age          = pMXFaceInfoEx[i].age;
			pMXFaceInfoExDst[i].gender       = pMXFaceInfoEx[i].gender;
			pMXFaceInfoExDst[i].pitch        = pMXFaceInfoEx[i].pitch;
			pMXFaceInfoExDst[i].yaw          = pMXFaceInfoEx[i].yaw ;
			pMXFaceInfoExDst[i].roll         = pMXFaceInfoEx[i].roll;
			pMXFaceInfoExDst[i].detected     = pMXFaceInfoEx[i].detected;
			pMXFaceInfoExDst[i].trackId      = pMXFaceInfoEx[i].trackId;
			pMXFaceInfoExDst[i].idmax        = pMXFaceInfoEx[i].idmax;
			pMXFaceInfoExDst[i].reCog        = pMXFaceInfoEx[i].reCog ;
			pMXFaceInfoExDst[i].reCogId      = pMXFaceInfoEx[i].reCogId;
			pMXFaceInfoExDst[i].reCogScore   = pMXFaceInfoEx[i].reCogScore;
			pMXFaceInfoExDst[i].stranger     = pMXFaceInfoEx[i].stranger;
		}
		return 0;
	}

	public static MXFaceInfoEx_bak Copy(MXFaceInfoEx_bak pMXFaceInfoExs) {
		MXFaceInfoEx_bak out = new MXFaceInfoEx_bak();
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
		out.probability = pMXFaceInfoExs.probability;
		out.completeness = pMXFaceInfoExs.completeness;
		out.illumination = pMXFaceInfoExs.illumination;
		out.blur = pMXFaceInfoExs.blur;
		out.occlusion  = pMXFaceInfoExs.occlusion;
		out.skin  = pMXFaceInfoExs.skin;
		out.makeup  = pMXFaceInfoExs.makeup;
		out.leye_status  = pMXFaceInfoExs.leye_status;
		out.reye_status  = pMXFaceInfoExs.reye_status;
		out.mouth_status  = pMXFaceInfoExs.mouth_status;
		out.glasses  = pMXFaceInfoExs.glasses;
		return out;
	}

}

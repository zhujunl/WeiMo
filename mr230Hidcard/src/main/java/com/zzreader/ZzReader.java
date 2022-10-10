package com.zzreader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.guoguang.jni.JniCall;

import java.math.BigInteger;

public class ZzReader
{
    public static byte CMD_IDCARD_COMMAND;
    public static short CMD_ANTCTL_CONTROL;
    public static short CMD_READIDVER_CONTROL;
    public static short CMD_READIDMSG_CONTROL;
    public static short CMD_GETSAMID_CONTROL;
    public static short CMD_FindCARD_CONTROL;
    public static short CMD_SELECTCARD_CONTROL;
    public static short CMD_READMSG_CONTROL;
    public static short CMD_READFULLMSG_CONTROL;
    public static short CMD_BOOT;
    public static short CMD_ATR;
    public static short CMD_SN;
    public static short CMD_ChipSN;
    public static short CMD_APDU;
    public static short CMD_APPLY_AUTHORIZATION;
    public static short CMD_AUTHORIZATION;
    public static short CMD_CHECK;
    public static short CMD_SIGN;
    public static short CMD_INTERNT_READCARD;
    public static short CMD_INTERNT_READCARD_FACEFINGER;
    public static short CMD_INTERNT_READCARD_JKM;
    public static short CMD_LOWPOWER;
    public static short CMD_ACTIVEINFO;
    public static short CMD_ACT_RELIVE;
    public static short CMD_READCARD_MULTIPLE;

    private static int IMAGE_X;
    private static int IMAGE_Y;
    private static int IMAGE_SIZE;
    private static byte CMD_GET_IMAGE;
    private static byte CMD_READ_VERSION;
    private static byte CMD_GET_HALF_IMG;
    private static final int mPhotoWidth = 102;
    private static final int mPhotoWidthBytes = 308;
    private static final int mPhotoHeight = 126;
    private static final int mPhotoSize = 38862;
    public static final byte[] BSENDBUF = { 0x32,0x30, 0x32,0x30,0x32,0x30, 0x32,0x30,0x32,0x30, 0x32,0x30,0x32,0x30, 0x32,0x30};
    private UsbBase m_usbBase;
    private Handler m_fHandler;
    private final String TAG="IdCardDriver";

    static {
        ZzReader.CMD_IDCARD_COMMAND = (byte) 0xB1;
        ZzReader.CMD_ANTCTL_CONTROL = -1519;
        ZzReader.CMD_READIDVER_CONTROL = (short) 0xFAF0;
        ZzReader.CMD_READIDMSG_CONTROL = (short) 0xFA92;
        ZzReader.CMD_GETSAMID_CONTROL = 0x12FF;
        ZzReader.CMD_FindCARD_CONTROL = 0x2001;
        ZzReader.CMD_SELECTCARD_CONTROL = 0x2002;
        ZzReader.CMD_READMSG_CONTROL = 0x3001;
        ZzReader.CMD_READFULLMSG_CONTROL = 0x301B;
        ZzReader.CMD_BOOT=0x11FF;
        ZzReader.CMD_ATR= (short) 0xFA90;
        ZzReader.CMD_SN= (short) 0xFAF2;
        ZzReader.CMD_ChipSN= (short) 0xFAF3;
        ZzReader.CMD_APDU= (short) 0xFA91;
        ZzReader.CMD_APPLY_AUTHORIZATION= (short) 0xA10D;
        ZzReader.CMD_AUTHORIZATION= (short) 0xA10E;
        ZzReader.CMD_CHECK= (short) 0xA10C;
        ZzReader.CMD_SIGN=0x12FE;
        ZzReader.CMD_INTERNT_READCARD=0x3020;
        ZzReader.CMD_INTERNT_READCARD_FACEFINGER=0x3021;
        ZzReader.CMD_INTERNT_READCARD_JKM=0x3020;
        ZzReader.CMD_LOWPOWER=0x6201;
        ZzReader.CMD_ACTIVEINFO= (short) 0xA10A;
        ZzReader.CMD_ACT_RELIVE= (short) 0xA10B;
        ZzReader.CMD_READCARD_MULTIPLE=(short) 0xFA82;

        ZzReader.IMAGE_X = 256;
        ZzReader.IMAGE_Y = 360;
        ZzReader.IMAGE_SIZE = ZzReader.IMAGE_X * ZzReader.IMAGE_Y;
        ZzReader.CMD_GET_IMAGE = 10;
        ZzReader.CMD_READ_VERSION = 13;
        ZzReader.CMD_GET_HALF_IMG = 20;
    }

    public void mxSetTraceLevel(final int iTraceLevel) {
        if (iTraceLevel != 0) {
            ConStant.DEBUG = true;
        }
        else {
            ConStant.DEBUG = false;
        }
    }

    public void SendMsg(final String obj) {
        if (ConStant.DEBUG) {
            final Message message = new Message();
            message.what = ConStant.SHOW_MSG;
            message.obj = obj;
            message.arg1 = 0;
            if (this.m_fHandler != null) {
                this.m_fHandler.sendMessage(message);
            }
        }
    }

    public ZzReader(final Context context) {
        this.m_fHandler = null;
        this.m_usbBase = new UsbBase(context);
    }

    public ZzReader(final Context context, final Handler bioHandler) {
        this.m_fHandler = null;
        this.m_fHandler = bioHandler;
        this.m_usbBase = new UsbBase(context, bioHandler);
    }

    public String mxGetJarVersion() {
        final String strVersion = "MIAXIS IdCard Driver V1.0.8.20200623";
        return strVersion;
    }

    public int mxGetDevNum() {
        return this.m_usbBase.getDevNum(ConStant.VID, ConStant.PID);
    }

    public int mxGetDevVersion(final byte[] bVersion) {
        int nRet = ConStant.ERRCODE_SUCCESS;
        final int[] wRecvLength = { 56 };
        nRet = this.ExeCommand(ZzReader.CMD_READ_VERSION, null, 0, 100, bVersion, wRecvLength, ConStant.CMD_TIMEOUT);
        return nRet;
    }

    /**
     * 连接读卡器
     * @return 0成功其他失败
     * */
    public synchronized int connectReaderZ(String reader){
        int iRet = ConStant.ERRCODE_SUCCESS;
        for (int i = 0; i < 3; i++) {
            iRet = this.m_usbBase.openDev(ConStant.VID, ConStant.PID);
            if (iRet==0){
                break;
            }
            SystemClock.sleep(100);
        }
        return iRet;
    }

    /**
     * 断开读卡器连接
     * @return 0成功其他失败
     * */
    public synchronized int disconnectZ (){
        int iRet = ConStant.ERRCODE_SUCCESS;
        this.m_usbBase.closeDev();
        return iRet;
    }

    /**
     * 获取读卡器固件版本
     * @param ver 输出固件版本
     * @return 0成功，其他失败
     * */
    public synchronized int getVersionZ(StringBuffer ver) {
        byte[] bVersion=new byte[ConStant.DATA_BUFFER_SIZE];
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.GetVersion(bVersion);
        if (iRet != 144) {
            return iRet;
        }
        String str=zzStringTrans.byteToStr(bVersion);
        ver.append(str);
        return ConStant.ERRCODE_SUCCESS;
    }

    /**
     * 获取读卡器序列号
     * @param sn  输出读卡器序列号
     * @return 0成功，其他失败
     * */
    public synchronized int getBoardSNZ(StringBuffer sn){
        byte[] bSn=new byte[ConStant.DATA_BUFFER_SIZE];
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.GetBoardSN(bSn);
        if (iRet != 144) {
            return iRet;
        }
        String str=zzStringTrans.byteToStr(bSn);
        sn.append(str);
        return ConStant.ERRCODE_SUCCESS;
    }

    /**
     * 获取安全芯片序列号
     * @param snbuf 输出芯片序列号
     * @return 0成功，其他失败
     * */
    public synchronized int getChipSNZ(byte[] snbuf){

        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.GetChipSN(snbuf);
        if (iRet != 144) {
            return iRet;
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    /**
     *  获取卡片ATR（找卡）
     * @return Atr 返回应答数据
     * */
    public synchronized byte[] getAtrZ(){
        byte[] Atr = new byte[64];
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.GetAtr(Atr);
        if (iRet != 144) {
            return null;
        }
        return Atr;
    }

    /**
     * 切回boot态
     * @return 0成功，其他失败
     * */
    public synchronized int firmwareUpdateZ(){
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.Boot();
        if (iRet != 144) {
            return iRet;
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    public synchronized int mxReadCardId(final byte[] bCardId) {
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.GetIdCardNo(bCardId);
        if (iRet != 144) {
            return iRet;
        }
        iRet = this.AntControl(0);
        if (iRet != 144) {
            return iRet;
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    public String SAMIDToNum(final byte[] SAMID) {
        final byte[] szIDtemp = new byte[256];
        final byte[] temp1 = new byte[2];
        final byte[] temp2 = new byte[2];
        final byte[] temp3 = new byte[4];
        final byte[] temp4 = new byte[4];
        final byte[] temp5 = new byte[4];
        int offsize = 0;
        for (int i = 0; i < 2; ++i) {
            temp1[i] = SAMID[offsize + i];
        }
        offsize += 2;
        for (int i = 0; i < 2; ++i) {
            temp2[i] = SAMID[offsize + i];
        }
        offsize += 2;
        for (int i = 0; i < 4; ++i) {
            temp3[i] = SAMID[offsize + i];
        }
        offsize += 4;
        for (int i = 0; i < 4; ++i) {
            temp4[i] = SAMID[offsize + i];
        }
        offsize += 4;
        for (int i = 0; i < 4; ++i) {
            temp5[i] = SAMID[offsize + i];
        }
        final short sTemp1 = MXDataCode.byteArrayToShort(temp1, 0);
        final short sTemp2 = MXDataCode.byteArrayToShort(temp2, 0);
        final BigInteger sTemp3 = MXDataCode.byteArrayToBigInteger(temp3);
        final BigInteger sTemp4 = MXDataCode.byteArrayToBigInteger(temp4);
        final BigInteger sTemp5 = MXDataCode.byteArrayToBigInteger(temp5);
        return String.format("%02d%02d%08d%010d", sTemp1, sTemp2, sTemp3, sTemp4);
    }

    /**
     * 获取微模块SAMID
     * @param samid 输出SAMID
     * @return 0成功，其他失败
     * */
    public synchronized int getSAMIDZ(byte[] samid) {
        final int iRet = this.GetSAMID(samid);
        if (iRet != 144) {
            return ConStant.ERRORCODE_SAMID;
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    public synchronized int mxReadCardInfo(final byte[] bCardInfo) {
        this.SendMsg("========================");
        this.SendMsg("mxReadCardInfo");
        if (bCardInfo.length < 1280) {
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        int iRet = ConStant.ERRCODE_SUCCESS;
        final byte[] ucCHMsg = new byte[256];
        final byte[] ucPHMsg = new byte[1024];
        final byte[] pucManaInfo = new byte[256];
        final int[] uiCHMsgLen = { 0 };
        final int[] uiPHMsgLen = { 0 };
        final byte[] bmp = new byte[38862];
        this.SendMsg("GetSAMIDZ");
        iRet = this.GetSAMID(pucManaInfo);
        if (iRet != 144) {
            this.AntControl(0);
            return iRet;
        }
        this.SendMsg("StartFindIDCard");
        iRet = this.StartFindIDCard(pucManaInfo);
        if (iRet != 159) {
            iRet = this.StartFindIDCard(pucManaInfo);
        }
        this.SendMsg("SelectIDCard");
        iRet = this.SelectIDCard(pucManaInfo);
        if (iRet != 144) {
            return iRet;
        }
        this.SendMsg("ReadBaseMsgUnicode");
        iRet = this.ReadBaseMsgUnicode(ucCHMsg, uiCHMsgLen, ucPHMsg, uiPHMsgLen);
        if (iRet != 144) {
            this.AntControl(0);
            return iRet;
        }
        for (int i = 0; i < uiCHMsgLen[0]; ++i) {
            bCardInfo[i] = ucCHMsg[i];
        }
        for (int i = 0; i < uiPHMsgLen[0]; ++i) {
            bCardInfo[i + 256] = ucPHMsg[i];
        }
        this.SendMsg("AntControl(0)");
        this.AntControl(0);
        this.SendMsg("========================");
        return ConStant.ERRCODE_SUCCESS;
    }

    /**
     * 读取身份证数据
     * @param baseinf     [OUT]基本信息,空间应不小于256字节
     * @param basesize    [OUT]基本信息,空间应不小于256字节
     * @param photo       [OUT]照片数据,空间应不小于1024字节
     * @param photosize   [OUT]照片数据长度
     * @param fpimg       [OUT]指纹数据,空间应不小于1024字节
     * @param fpsize      [OUT]指纹数据长度
     * @return 0成功，其他失败
     * */
    public synchronized int readIDCardMsgZ(byte[] baseinf, int[] basesize, byte[] photo, int[] photosize, byte[] fpimg, int[] fpsize){
        this.SendMsg("========================");
        this.SendMsg("mxReadCardFullInfo");
        if ((baseinf.length+photo.length+fpimg.length) < 2304) {
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        int iRet = ConStant.ERRCODE_SUCCESS;
        final byte[] pucManaInfo = new byte[256];
        this.AntControl(0);
        this.SendMsg("GetSAMID");
        this.SendMsg("StartFindIDCard");
        iRet = this.StartFindIDCard(pucManaInfo);
        if (iRet != 159) {
            iRet = this.StartFindIDCard(pucManaInfo);
            if (iRet==128){
                return ConStant.ERRORCODE_NOCARD;
            }
            if (iRet != 159) {
                Log.d(TAG, "StartFindIDCard:"+iRet);
                return ConStant.ERRCODE_READCARD;
            }
        }
        this.SendMsg("SelectIDCard");
        iRet = this.SelectIDCard(pucManaInfo);
        if (iRet != 144) {
            this.SendMsg("SelectIDCard iRet=" + iRet);
            Log.d(TAG, "SelectIDCard:" + iRet);
            return ConStant.ERRCODE_READCARD;
        }
        this.SendMsg("ReadFullMsgUnicode");
        iRet = this.ReadFullMsgUnicode(baseinf, basesize, photo, photosize, fpimg, fpsize);
        if (iRet != 144) {
            Log.d(TAG, "ReadFullMsgUnicode:"+iRet);
            this.SendMsg("ReadBaseMsgUnicode,iRet=" + iRet);
            this.AntControl(0);
            return iRet;
        }
        this.SendMsg("AntControl(0)");
        this.AntControl(0);
        this.SendMsg("========================");
        if (fpsize[0] == 0) {
            return ConStant.ERRCODE_SUCCESS_1;
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    /**
     * 读取身份证基本数据
     * @return 0成功，其他失败
     * */
    public synchronized CardResult readIDCardBaseMsgZ(int[] count){
        this.SendMsg("========================");
        this.SendMsg("mxReadCardFullInfo");
        int iRet = ConStant.ERRCODE_SUCCESS;
        final byte[] pucManaInfo = new byte[256];
        this.AntControl(0);
        this.SendMsg("StartFindIDCard");
        iRet = this.StartFindIDCard(pucManaInfo);
        if (iRet != 159) {
            iRet = this.StartFindIDCard(pucManaInfo);
            if (iRet==128){
                return new CardResult(iRet,null);
            }
            if (iRet != 159) {
                return new CardResult(iRet,null);
            }
        }
        this.SendMsg("SelectIDCard");
        iRet = this.SelectIDCard(pucManaInfo);
        if (iRet != 144) {
            this.SendMsg("SelectIDCard iRet=" + iRet);
            return new CardResult(iRet,null);
        }
        this.SendMsg("ReadFullMsgUnicode");
        CardResult re = this.ReadBaseMsgUnicode(count);
        if (re.re!=0) {
            this.SendMsg("ReadBaseMsgUnicode,iRet=" + iRet);
            this.AntControl(0);
            return re;
        }
        this.SendMsg("AntControl(0)");
        this.AntControl(0);
        this.SendMsg("========================");
        return re;
    }

    /** 一个指令完成寻卡读卡*/
    public synchronized CardResult readIdCardMsg(int[] count){
        this.SendMsg("========================");
        int iRet = ConStant.ERRCODE_SUCCESS;
        CardResult re = this.ReadMsgUnicode(count);
        if (re.re!=0) {
            this.SendMsg("ReadBaseMsgUnicode,iRet=" + iRet);
            this.AntControl(0);
            return re;
        }
        this.SendMsg("========================");
        return re;
    }



    public synchronized CardResult readIDCardMsgZ( byte[] photo, byte[] fpimg,int[] count){
        this.SendMsg("========================");
        this.SendMsg("mxReadCardForJkm");
        int iRet = ConStant.ERRCODE_SUCCESS;
        final byte[] pucManaInfo = new byte[256];
        this.SendMsg("StartFindIDCard");
        iRet = this.StartFindIDCard(pucManaInfo);
        if (iRet != 159) {
            iRet = this.StartFindIDCard(pucManaInfo);
            if (iRet != 159) {
                return new CardResult(iRet,null);
            }
        }
        this.SendMsg("SelectIDCard");
        iRet = this.SelectIDCard(pucManaInfo);
        if (iRet != 144) {
            this.SendMsg("SelectIDCard iRet=" + iRet);
            return new CardResult(iRet,null);
        }
        this.SendMsg("ReadFullMsgUnicode");
        CardResult re = this.ReadFullForFullMsgUnicode(photo,fpimg,count);
        if (re.re!=0) {
            this.SendMsg("ReadBaseMsgUnicode,iRet=" + iRet);
            this.AntControl(0);
            return re;
        }
        this.SendMsg("AntControl(0)");
        this.AntControl(0);
        this.SendMsg("========================");
        return re;
    }

    /**
     * APDU指令传输
     * @param apducmd [IN]apdu指令数据
     * @return apdu的响应数据
     * */
    public synchronized byte[] transceive(byte[] apducmd){
        byte[] out=new byte[512];
        byte[] Atr=new byte[64];
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.GetAtr(Atr);
        if (iRet != 144) {
            return null;
        }
        iRet=APDU(apducmd,out);
        return out;
    }

    /**
     * SAM透传指令
     * @param cmd   [IN]指令数据
     * @return 响应数据
     * */
    public synchronized CardResult samCommandZ(byte[] cmd){
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(cmd, bSendBuf, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        int a=oRecvDataBuffer[5];
        int b=oRecvDataBuffer[6];
        if (a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        byte[] out=new byte[a*256+b+7];
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, out.length);

        return new CardResult(lRV,out);
    }

    public CardResult samCommandZ(byte[] cmd,byte[] bSendBuf){
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] out=new byte[ConStant.CMD_BUFSIZE];
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        lRV = this.SendIDCardPack(cmd, bSendBuf, bSendBuf.length, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, oRecvDataBuffer.length);

        return new CardResult(lRV,out);
    }

    /**
     * SAM+身份证透传指令
     * @param cmd   [IN]指令数据
     * @return 响应数据
     * */
    public synchronized byte[] samCardCommandZ(byte[] cmd){
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] out=new byte[512];
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(cmd, bSendBuf, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return null;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return null;
        }
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, oRecvDataBuffer.length);

        return out;
    }

    /**
     * 低功耗
     * @return 0成功，其他失败
     * */
    public synchronized int lowPower(){
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] out=new byte[ConStant.CMD_BUFSIZE];
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(CMD_LOWPOWER, bSendBuf, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, oRecvDataBuffer.length);

        return ConStant.ERRCODE_SUCCESS;
    }

    /**健康码*/
    public byte[] mxReadCardForJkm() {
        this.SendMsg("========================");
        this.SendMsg("mxReadCardForJkm");
        int iRet = ConStant.ERRCODE_SUCCESS;
        final byte[] ucCHMsg = new byte[256];
        final byte[] ucPHMsg = new byte[1024];
        final byte[] ucFPMsg = new byte[1024];
        final byte[] pucManaInfo = new byte[256];
        final int[] uiCHMsgLen = { 0 };
        final int[] uiPHMsgLen = { 0 };
        final int[] uiFPMsgLen = { 0 };
        final byte[] bmp = new byte[38862];
        this.SendMsg("GetSAMID");
        iRet = this.GetSAMID(pucManaInfo);
        if (iRet != 144) {
            this.AntControl(0);
            return null;
        }
        this.SendMsg("StartFindIDCard");
        iRet = this.StartFindIDCard(pucManaInfo);
        if (iRet != 159) {
            iRet = this.StartFindIDCard(pucManaInfo);
            if (iRet != 159) {
                return null;
            }
        }
        this.SendMsg("SelectIDCard");
        iRet = this.SelectIDCard(pucManaInfo);
        if (iRet != 144) {
            this.SendMsg("SelectIDCard iRet=" + iRet);
            return null;
        }
        this.SendMsg("ReadFullMsgUnicode");
        byte[] i = this.ReadFullForJkmMsgUnicode();
        if (i == null) {
            this.SendMsg("ReadBaseMsgUnicode,iRet=" + iRet);
            this.AntControl(0);
            return null;
        }
        this.SendMsg("AntControl(0)");
        this.AntControl(0);
        this.SendMsg("========================");
        return i;
    }

    public int mxReadCardFullInfo(final byte[] bCardFullInfo) {
        this.SendMsg("========================");
        this.SendMsg("mxReadCardFullInfo");
        if (bCardFullInfo.length < 2304) {
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        int iRet = ConStant.ERRCODE_SUCCESS;
        final byte[] ucCHMsg = new byte[256];
        final byte[] ucPHMsg = new byte[1024];
        final byte[] ucFPMsg = new byte[1024];
        final byte[] pucManaInfo = new byte[256];
        final int[] uiCHMsgLen = { 0 };
        final int[] uiPHMsgLen = { 0 };
        final int[] uiFPMsgLen = { 0 };
        final byte[] bmp = new byte[38862];
        this.SendMsg("GetSAMID");
        iRet = this.GetSAMID(pucManaInfo);
        if (iRet != 144) {
            this.AntControl(0);
            return iRet;
        }
        this.SendMsg("StartFindIDCard");
        iRet = this.StartFindIDCard(pucManaInfo);
        if (iRet != 159) {
            iRet = this.StartFindIDCard(pucManaInfo);
            if (iRet != 159) {
                return iRet;
            }
        }
        this.SendMsg("SelectIDCard");
        iRet = this.SelectIDCard(pucManaInfo);
        if (iRet != 144) {
            this.SendMsg("SelectIDCard iRet=" + iRet);
            return iRet;
        }
        this.SendMsg("ReadFullMsgUnicode");
        iRet = this.ReadFullMsgUnicode(ucCHMsg, uiCHMsgLen, ucPHMsg, uiPHMsgLen, ucFPMsg, uiFPMsgLen);
        if (iRet != 144) {
            this.SendMsg("ReadBaseMsgUnicode,iRet=" + iRet);
            this.AntControl(0);
            return ConStant.ERRCODE_ID_CARD_READ;
        }
        for (int i = 0; i < uiCHMsgLen[0]; ++i) {
            bCardFullInfo[i] = ucCHMsg[i];
        }
        for (int i = 0; i < uiPHMsgLen[0]; ++i) {
            bCardFullInfo[i + 256] = ucPHMsg[i];
        }
        for (int i = 0; i < uiFPMsgLen[0]; ++i) {
            bCardFullInfo[i + 256 + 1024] = ucFPMsg[i];
        }
        this.SendMsg("AntControl(0)");
        this.AntControl(0);
        this.SendMsg("========================");
        if (uiFPMsgLen[0] == 0) {
            return ConStant.ERRCODE_SUCCESS_1;
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    int GetVersion(final byte[] bVersion) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_READIDVER_CONTROL, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            bVersion[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    int APDU(byte[] apducmd,byte[] out){
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_APDU, apducmd, apducmd.length, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, oRecvDataBuffer.length);
//        for (int i = 0; i < oPackDataBuffer.length; ++i) {
//            oPackDataBuffer[i] = 0;
//        }
//        oPackLen[0] = oPackDataBuffer.length;
//        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
//        if (lRV != ConStant.ERRCODE_SUCCESS) {
//            return lRV;
//        }
//        for (int i = 0; i < oPackLen[0]; ++i) {
//            out[i] = oPackDataBuffer[i];
//        }
        return result[0];
    }

    int GetBoardSN(final byte[] bSn) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_SN, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            bSn[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    int GetChipSN(byte[] snbuf){
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_ChipSN, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            snbuf[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    int GetAtr(final byte[] Atr){
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_ATR, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            Atr[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    int Boot(){
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_BOOT, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        return result[0];
    }

    int GetIdCardNo(final byte[] bVersion) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_READIDMSG_CONTROL, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            bVersion[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    int GetSAMID(final byte[] bVersion) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_GETSAMID_CONTROL, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            bVersion[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    int StartFindIDCard(final byte[] bVersion) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_FindCARD_CONTROL, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            bVersion[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    int SelectIDCard(final byte[] bVersion) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        this.SendMsg("SendIDCardPack");
        lRV = this.SendIDCardPack(ZzReader.CMD_SELECTCARD_CONTROL, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("SendIDCardPack lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("IDCardAPDU");
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("IDCardAPDU lRV=" + lRV);
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        this.SendMsg("RecvIDCardPack");
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvIDCardPack lRV=" + lRV);
            return lRV;
        }
        if (result[0] != 144) {
            this.SendMsg("RecvIDCardPack result[0]=" + result[0]);
            return result[0];
        }
        for (int i = 0; i < oPackLen[0]; ++i) {
            bVersion[i] = oPackDataBuffer[i];
        }
        return result[0];
    }

    CardResult ReadBaseMsgUnicode(int[] count) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_INTERNT_READCARD, BSENDBUF, BSENDBUF.length, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        this.SendMsg("RecvIDCardPack");
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvIDCardPack lRV=" + lRV);
            return new CardResult(lRV,null);
        }
        if (result[0] != 144) {
            this.SendMsg("RecvIDCardPack result[0]=" + result[0]);
            return new CardResult(result[0] ,null);
        }
        int a=oPackDataBuffer[87];
        int b=oPackDataBuffer[88];
        if(a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        int len=a*256+b;
        byte[] data=new byte[len+152];
        count[0]=oPackDataBuffer[0];
        if (count[0]<0){
            count[0]+=256;
        }
        System.arraycopy(oPackDataBuffer,1,data,0,data.length);
        return new CardResult(lRV,data);
    }

    CardResult ReadMsgUnicode(int[] count) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_READCARD_MULTIPLE, BSENDBUF, BSENDBUF.length, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        this.SendMsg("RecvIDCardPack");
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvIDCardPack lRV=" + lRV);
            return new CardResult(lRV,null);
        }
        if (result[0] != 144) {
            this.SendMsg("RecvIDCardPack result[0]=" + result[0]);
            return new CardResult(result[0] ,null);
        }
        int a=oPackDataBuffer[87];
        int b=oPackDataBuffer[88];
        if(a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        count[0]=oPackDataBuffer[0];
        if (count[0]<0){
            count[0]+=256;
        }
        int len=a*256+b;
        byte[] data=new byte[len+152];
        System.arraycopy(oPackDataBuffer,1,data,0,data.length);
        return new CardResult(lRV,data);
    }


    int ReadBaseMsgUnicode(final byte[] pucCHMsg, final int[] puiCHMsgLen, final byte[] PucPHMsg, final int[] puiPHMsgLen) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_READMSG_CONTROL, null, 0, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        if (result[0] != 144) {
            return result[0];
        }
        if (oPackLen[0] != 1295) {
            return ConStant.ERRCODE_CRC;
        }
        System.arraycopy(oPackDataBuffer, 4, pucCHMsg, 0, 256);
        puiCHMsgLen[0] = 256;
        for (int i = 0; i < 1024; ++i) {
            PucPHMsg[i] = oPackDataBuffer[i + 4 + 256];
        }
        puiPHMsgLen[0] = 1024;
        return result[0];
    }

    int ReadFullMsgUnicode(final byte[] pucCHMsg, final int[] puiCHMsgLen, final byte[] PucPHMsg, final int[] puiPHMsgLen, final byte[] PucFPMsg, final int[] puiFPMsgLen) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_INTERNT_READCARD_FACEFINGER, BSENDBUF, BSENDBUF.length, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        this.SendMsg("RecvIDCardPack");
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvIDCardPack lRV=" + lRV);
            return lRV;
        }
        if (result[0] != 144) {
            this.SendMsg("RecvIDCardPack result[0]=" + result[0]);
            return result[0];
        }
        puiCHMsgLen[0]=32;
        System.arraycopy(oPackDataBuffer,39,pucCHMsg,0,puiCHMsgLen[0]);
        puiPHMsgLen[0]=1024;
        puiFPMsgLen[0]=1024;
        int a=oPackDataBuffer[87];
        int b=oPackDataBuffer[88];
        if(a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        int len=a*256+b;
        System.arraycopy(oPackDataBuffer,89+len+32+64+4,PucPHMsg,0,puiPHMsgLen[0]);
        System.arraycopy(oPackDataBuffer,89+len+32+64+4+1024,PucFPMsg,0,puiFPMsgLen[0]);
        return result[0];
    }

    byte[] ReadFullForJkmMsgUnicode() {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_INTERNT_READCARD_FACEFINGER, BSENDBUF, BSENDBUF.length, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return null;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return null;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        this.SendMsg("RecvIDCardPack");
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvIDCardPack lRV=" + lRV);
            return null;
        }
        if (result[0] != 144) {
            this.SendMsg("RecvIDCardPack result[0]=" + result[0]);
            return null;
        }
        int a=oPackDataBuffer[87];
        int b=oPackDataBuffer[88];
        if(a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        int len=a*256+b;
        byte[] data=new byte[len+184];
        System.arraycopy(oPackDataBuffer,1,data,0,data.length);
        return data;
    }

    CardResult ReadFullForFullMsgUnicode(byte[] photo,byte[] finger,int[] count) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        lRV = this.SendIDCardPack(ZzReader.CMD_INTERNT_READCARD_FACEFINGER, BSENDBUF, BSENDBUF.length, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return new CardResult(lRV,null);
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        this.SendMsg("RecvIDCardPack");
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvIDCardPack lRV=" + lRV);
            return new CardResult(lRV,null);
        }
        if (result[0] != 144) {
            this.SendMsg("RecvIDCardPack result[0]=" + result[0]);
            return new CardResult(result[0] ,null);
        }
        int a=oPackDataBuffer[87];
        int b=oPackDataBuffer[88];
        if(a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        int len=a*256+b;
        byte[] data=new byte[len+184];
        count[0]=oPackDataBuffer[0];
        if (count[0]<0){
            count[0]+=256;
        }
        System.arraycopy(oPackDataBuffer,1,data,0,data.length);
        System.arraycopy(oPackDataBuffer,89+len+32+64+4,photo,0,photo.length);
        System.arraycopy(oPackDataBuffer,89+len+32+64+4+1024,finger,0,finger.length);
        return new CardResult(lRV,data);
    }

    int AntControl(final int dAntState) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oPackLen = { oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] oRecvLen = { oRecvDataBuffer.length };
        final int[] result = { 0 };
        final byte[] bSendBuf = { (byte)dAntState };
        lRV = this.SendIDCardPack(ZzReader.CMD_ANTCTL_CONTROL, bSendBuf, 1, oPackDataBuffer, oPackLen);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        lRV = this.IDCardAPDU(oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 500);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        for (int i = 0; i < oPackDataBuffer.length; ++i) {
            oPackDataBuffer[i] = 0;
        }
        oPackLen[0] = oPackDataBuffer.length;
        lRV = this.RecvIDCardPack(oRecvDataBuffer, oRecvLen[0], oPackDataBuffer, oPackLen, result);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        return result[0];
    }

    int SendIDCardPack(final short IDCardCommandIDAndIDCardparam, final byte[] SendDataBuffer, final int SendLen, final byte[] oPackDataBuffer, final int[] oPackLen) {
        final byte[] tempBufferData = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        int i = 0;
        int offsize = 0;
        byte AddCheck = 0;
        short len = 0;
        final byte[] FlagStart = new byte[5];
        final byte[] dtemp = new byte[2];
        FlagStart[0] = -86;
        FlagStart[2] = (FlagStart[1] = -86);
        FlagStart[3] = -106;
        FlagStart[4] = 105;
        dtemp[1] = (dtemp[0] = 0);
        if (SendLen > ConStant.DATA_BUFFER_SIZE_MIN - 10 || SendLen < 0) {
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        for (i = 0; i < FlagStart.length; ++i) {
            tempBufferData[offsize + i] = FlagStart[i];
        }
        offsize += FlagStart.length;
        len = (short)(2 + SendLen + 1);
        dtemp[0] = (byte)((byte)(len / 256) & 0xFF);
        dtemp[1] = (byte)((byte)len & 0xFF);
        for (i = 0; i < dtemp.length; ++i) {
            tempBufferData[offsize + i] = dtemp[i];
        }
        offsize += dtemp.length;
        for (i = 0; i < dtemp.length; ++i) {
            dtemp[i] = 0;
        }
        dtemp[0] = (byte)((byte)(IDCardCommandIDAndIDCardparam >> 8) & 0xFF);
        dtemp[1] = (byte)((byte)IDCardCommandIDAndIDCardparam & 0xFF);
        for (i = 0; i < dtemp.length; ++i) {
            tempBufferData[offsize + i] = dtemp[i];
        }
        offsize += dtemp.length;
        if (SendLen > 0 && SendLen < ConStant.DATA_BUFFER_SIZE_MIN - 10) {
            for (i = 0; i < SendLen; ++i) {
                tempBufferData[offsize + i] = SendDataBuffer[i];
            }
            offsize += SendLen;
        }
        for (i = 0; i < len + 2; ++i) {
            AddCheck ^= tempBufferData[i + 5];
        }
        tempBufferData[offsize] = AddCheck;
        ++offsize;
        if (oPackLen[0] < offsize) {
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        oPackLen[0] = (short)offsize;
        for (i = 0; i < offsize; ++i) {
            oPackDataBuffer[i] = tempBufferData[i];
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    int SendIDCardPack(final byte[] IDCardCommandIDAndIDCardparam, final byte[] SendDataBuffer, final int SendLen, final byte[] oPackDataBuffer, final int[] oPackLen) {
        final byte[] tempBufferData = new byte[oPackLen[0]];
        int i = 0;
        int offsize = 0;
        byte AddCheck = 0;
        short len = 0;
        final byte[] FlagStart = new byte[5];
        final byte[] dtemp = new byte[2];
        FlagStart[0] = -86;
        FlagStart[2] = (FlagStart[1] = -86);
        FlagStart[3] = -106;
        FlagStart[4] = 105;
        for (i = 0; i < FlagStart.length; ++i) {
            tempBufferData[offsize + i] = FlagStart[i];
        }
        offsize += FlagStart.length;
        len = (short)(IDCardCommandIDAndIDCardparam.length + SendLen + 1);
        dtemp[0] = (byte)((byte)(len / 256) & 0xFF);
        dtemp[1] = (byte)((byte)len & 0xFF);
        for (i = 0; i < dtemp.length; ++i) {
            tempBufferData[offsize + i] = dtemp[i];
        }
        offsize += dtemp.length;
        for (i = 0; i < dtemp.length; ++i) {
            dtemp[i] = 0;
        }
        for (i = 0; i < IDCardCommandIDAndIDCardparam.length; ++i) {
            tempBufferData[offsize + i] = IDCardCommandIDAndIDCardparam[i];
        }
        offsize += IDCardCommandIDAndIDCardparam.length;
        if (SendLen > 0 ) {
            for (i = 0; i < SendLen; ++i) {
                tempBufferData[offsize + i] = SendDataBuffer[i];
            }
            offsize += SendLen;
        }
        for (i = 0; i < len + 2; ++i) {
            AddCheck ^= tempBufferData[i + 5];
        }
        tempBufferData[offsize] = AddCheck;
        ++offsize;
        if (oPackLen[0] < offsize) {
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        oPackLen[0] = (short)offsize;
        for (i = 0; i < offsize; ++i) {
            oPackDataBuffer[i] = tempBufferData[i];
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    int RecvIDCardPack(final byte[] RecvDataBuffer, final int RecvLen, final byte[] oPackDataBuffer, final int[] oPackLen, final int[] oResult) {
        final byte[] tempBufferData = new byte[ConStant.CMD_BUFSIZE];
        int offsize = 0;
        short len = 0;
        byte dresult = -1;
        byte recvCheck = 0;
        byte currentCheck = 0;
        final byte[] FlagStart = new byte[5];
        final byte[] dtemp = new byte[2];
        final byte[] Reser = new byte[2];
        FlagStart[0] = -86;
        FlagStart[2] = (FlagStart[1] = -86);
        FlagStart[3] = -106;
        FlagStart[4] = 105;
        dtemp[1] = (dtemp[0] = 0);
        Reser[1] = (Reser[0] = 0);
        for (int i = 0; i < FlagStart.length; ++i) {
            if (RecvDataBuffer[i] != FlagStart[i]) {
                return ConStant.ERRCODE_CRC;
            }
        }
        offsize += 5;
        len = (short)(256 * RecvDataBuffer[offsize] + RecvDataBuffer[offsize + 1]);
        offsize += 2;
        Reser[0] = RecvDataBuffer[offsize];
        Reser[1] = RecvDataBuffer[offsize + 1];
        for (int i = 0; i < Reser.length; ++i) {
            if (Reser[i] != 0) {
                return ConStant.ERRCODE_CRC;
            }
        }
        offsize += 2;
        dresult = RecvDataBuffer[offsize];
        ++offsize;
        if (len > 4) {
            System.arraycopy(RecvDataBuffer, offsize + 0, tempBufferData, 0, len - 4);
            offsize = offsize + len - 4;
        }
        recvCheck = RecvDataBuffer[offsize];
        for (int i = 0; i < len + 2 - 1; ++i) {
            currentCheck ^= RecvDataBuffer[i + 5];
        }
        ++offsize;
        if (currentCheck != recvCheck) {
            return ConStant.ERRCODE_CRC;
        }
        if (oPackDataBuffer != null && oPackLen[0] > len - 4) {
            oPackLen[0] = (short)offsize;
            if (len - 4 >= 0) System.arraycopy(tempBufferData, 0, oPackDataBuffer, 0, len - 4);
            if ((oResult[0] = dresult) < 0) {
                oResult[0] = dresult + 256;
            }
            return ConStant.ERRCODE_SUCCESS;
        }
        return ConStant.ERRCODE_MEMORY_OVER;
    }

    int IDCardAPDU(final byte[] lpSendData, final int wSendLength, final int iSendTime, final byte[] lpRecvData, final int[] io_wRecvLength, final int iRecvTime) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        lRV = this.ExeCommand(ZzReader.CMD_IDCARD_COMMAND, lpSendData, wSendLength, iSendTime, lpRecvData, io_wRecvLength, iRecvTime);
        return lRV;
    }

    int ExeCommand(final byte nCommandID, final byte[] lpSendData, final int wSendLength, final int iSendTime, final byte[] lpRecvData, final int[] io_wRecvLength, final int iRecvTime) {
        final int iMaxRecvLen = io_wRecvLength[0];
        this.SendMsg("nCommandID:" + nCommandID);
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.m_usbBase.openDev(ConStant.VID, ConStant.PID);
        if (iRet != 0) {
            SystemClock.sleep(500);
            iRet = this.m_usbBase.openDev(ConStant.VID, ConStant.PID);
            if (iRet != 0){
                return iRet;
            }
        }
//        if (this.m_usbBase.getM_connection()==null){
//            return ConStant.ERRCODE_NODEVICE;
//        }
        final byte[] DataBuffer = new byte[ConStant.CMD_DATA_BUF_SIZE];
        do {
            SystemClock.sleep(10);
            iRet = this.m_usbBase.recvData(DataBuffer, DataBuffer.length, 5);
        } while (iRet == 0);
        iRet = this.sendPacket(nCommandID, lpSendData, wSendLength);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final byte[] bRecv = new byte[3012];
        iRet = this.recvPacket(bResult, bRecv, io_wRecvLength, ConStant.CMD_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        int len = 0;
        int a=bRecv[5];
        int b=bRecv[6];
        if (a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        len = a* 256 + b+ 7;
        byte[] bRecvBuf = new byte[len];
        if (len >= 0) System.arraycopy(bRecv, 0, bRecvBuf, 0, len);
        this.SendMsg("len=" + len);
        int packsize = len / ConStant.REVC_BUFFER_SIZE_MIN;
        if (len % ConStant.REVC_BUFFER_SIZE_MIN != 0) {
            ++packsize;
        }
        this.SendMsg("packsize=" + packsize);
        final byte[] outBuffer = new byte[ConStant.CMD_BUFSIZE];
        int realsize = 0;
        this.SendMsg("io_wRecvLength[0]=" + io_wRecvLength[0]);
        if (io_wRecvLength[0] >= 2) {
            System.arraycopy(bRecvBuf, 0, outBuffer, 0 + realsize, io_wRecvLength[0]);
            realsize = realsize + io_wRecvLength[0];
        }
        else {
            realsize = realsize;
        }
        this.SendMsg("realsize=" + realsize);
        this.SendMsg("====realsize=" + realsize);
        this.SendMsg("====iMaxRecvLen=" + iMaxRecvLen);
//        if (realsize > iMaxRecvLen) {
//            this.m_usbBase.closeDev();
//            return ConStant.ERRCODE_MEMORY_OVER;
//        }
        if (realsize >= 2) {
            System.arraycopy(outBuffer, 0, lpRecvData, 0, realsize);
            io_wRecvLength[0] = realsize;
        }
        this.m_usbBase.closeDev();
        return ConStant.ERRCODE_SUCCESS;
    }

    private int sendPacket(final byte bCmd, final byte[] bSendBuf, final int iDataLen) {
        int iRet = -1;
        int offsize = 0;
        short iCheckSum = 0;
        final byte[] DataBuffer = new byte[iDataLen];
        int a=bSendBuf[5];
        int b=bSendBuf[6];
        if (a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        int len=a*256+b+7;
        int pack=0;
        pack=len/ConStant.DATA_BUFFER_SIZE;
        if (pack%ConStant.DATA_BUFFER_SIZE!=0){
            pack++;
        }
        if (iDataLen > 1) {
            for (int i = 0; i < iDataLen; ++i) {
                DataBuffer[offsize++] = bSendBuf[i];
            }
        }
        iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
        if (iRet < 0) {
            return iRet;
        }
        //        if (iDataLen > 1&&iDataLen<56) {
        //            for (int i = 0; i < iDataLen; ++i) {
        //                DataBuffer[offsize++] = bSendBuf[i];
        //            }
        //            iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
        //            if (iRet < 0) {
        //                return ConStant.ERRCODE_TRANS;
        //            }
        //        }else if (iDataLen>56){
        //            for (int i = 0; i < 56; ++i) {
        //                DataBuffer[offsize++] = bSendBuf[i];
        //            }
        //            iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
        //            if (iRet < 0) {
        //                return ConStant.ERRCODE_TRANS;
        //            }
        //            for (int j=1;j<pack;j++){
        //                System.arraycopy(bSendBuf,  56 +(64*(j-1)) , DataBuffer, 0, DataBuffer.length);
        //                iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
        //                if (iRet < 0) {
        //                    return ConStant.ERRCODE_TRANS;
        //                }
        //            }
        //        }
        return 0;
    }

//    private int sendPacket(final byte bCmd, final byte[] bSendBuf, final int iDataLen) {
//        int iRet = -1;
//        int offsize = 0;
//        short iCheckSum = 0;
//        final byte[] DataBuffer = new byte[64];
//        int a=bSendBuf[5];
//        int b=bSendBuf[6];
//        if (a<0){
//            a+=256;
//        }
//        if (b<0){
//            b+=256;
//        }
//        int len=a*256+b+7;
//        int pack=0;
//        pack=len/ConStant.DATA_BUFFER_SIZE;
//        if (pack%ConStant.DATA_BUFFER_SIZE!=0){
//            pack++;
//        }
//        if (iDataLen > 1&&iDataLen<64) {
//            for (int i = 0; i < iDataLen; ++i) {
//                DataBuffer[offsize++] = bSendBuf[i];
//            }
//            iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
//            if (iRet < 0) {
//                return ConStant.ERRCODE_TRANS;
//            }
//        }else if (iDataLen>64){
//            for (int i = 0; i < 64; ++i) {
//                DataBuffer[offsize++] = bSendBuf[i];
//            }
//            iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
//            if (iRet < 0) {
//                return ConStant.ERRCODE_TRANS;
//            }
//            for (int j=1;j<pack;j++){
//                System.arraycopy(bSendBuf,  64 +(64*(j-1)) , DataBuffer, 0, DataBuffer.length);
//                iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
//                if (iRet < 0) {
//                    return ConStant.ERRCODE_TRANS;
//                }
//            }
//        }
//        return 0;
//    }

    private int recvPacket(final byte[] bResult, final byte[] bRecvBuf, final int[] iRecvLen, final int iTimeOut) {
        int iRet = -1;
        int offsize = 0;
        int iDataLen = 0;
        int a = 0;
        int b = 0;
        final byte[] SRN = new byte[2];
        final byte[] STYLE=new byte[3];
        final byte[] CHECK=new byte[1];
        final byte[] DataBuffer = new byte[bRecvBuf.length];
        SystemClock.sleep(10);
        iRet = this.m_usbBase.recvData(DataBuffer, DataBuffer.length, iTimeOut);
        if (iRet < 0) {
            return ConStant.ERRCODE_TRANS;
        }
        if (DataBuffer[offsize++] != ConStant.CMD_RET_FLAG) {
            return ConStant.ERRCODE_CRC;
        }
        SRN[0] = DataBuffer[offsize++];
        SRN[1] = DataBuffer[offsize++];

        a = DataBuffer[6];
        if (a < 0) {
            a += 256;
        }
        b = DataBuffer[5];
        if (b < 0) {
            b += 256;
        }
        iDataLen = b * 256 + a;
        if(iDataLen!=iRet-7){
            return ConStant.ERRCODE_CRC;
        }
        bResult[0] = DataBuffer[offsize++];
        iRecvLen[0] = iRet;

        for(int i = 0; i <  iRet; ++i){
            bRecvBuf[i] = DataBuffer[i];
        }
        int che=DataBuffer[5];
        for (int i=6;i<iRet-1;++i){
            che=che^DataBuffer[i];
        }
        STYLE[0]=DataBuffer[7];
        STYLE[1]=DataBuffer[8];
        STYLE[2]=DataBuffer[9];
        CHECK[0]=DataBuffer[iRet-1];
        if (che!=CHECK[0]){
            return ConStant.ERRCODE_CRC;
        }
        return ConStant.ERRCODE_SUCCESS;
    }

    public int Wlt2Bmp(final byte[] wlt, final byte[] bmp) {
        if (bmp.length < 38862) {
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        JniCall.Huaxu_Wlt2Bmp(wlt, bmp, 0);
        return 0;
    }

    public int Base64Encode(final byte[] pInput, final int inputLen, final byte[] pOutput, final int outputbufsize) {
        return zzJavaBase64.JavaBase64Encode(pInput, inputLen, pOutput, outputbufsize);
    }
}

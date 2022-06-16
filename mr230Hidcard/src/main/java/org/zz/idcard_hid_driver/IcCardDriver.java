package org.zz.idcard_hid_driver;

import android.os.*;
import android.content.*;
import java.util.*;

public class IcCardDriver
{
    private UsbBase m_usbBase;
    private Handler m_fHandler;
    public static byte CMD_ICCARD_COMMAND;
    public static byte CMD_IDCARD_COMMAND;
    public static byte CMD_ID64CARD_COMMAND;
    public static byte CONTACT_CARD;
    public static byte CONTACT_LESS_CARD;
    public static byte CONTACT_64CARD;
    public static short CMD_IC_CARD_ACTIVE;
    public static short CMD_IC_APDU;
    public static short CMD_U_IC_CARD_AVTIVE;
    public static short CMD_U_IC_CARD_S_VERIFY;
    public static short CMD_U_IC_CARD_S_READ;
    public static short CMD_U_IC_CARD_S_WRITE;
    
    static {
        IcCardDriver.CMD_ICCARD_COMMAND = -80;
        IcCardDriver.CMD_IDCARD_COMMAND = -79;
        IcCardDriver.CMD_ID64CARD_COMMAND = -78;
        IcCardDriver.CONTACT_CARD = 0;
        IcCardDriver.CONTACT_LESS_CARD = 1;
        IcCardDriver.CONTACT_64CARD = 2;
        IcCardDriver.CMD_IC_CARD_ACTIVE = 12836;
        IcCardDriver.CMD_IC_APDU = 12838;
        IcCardDriver.CMD_U_IC_CARD_AVTIVE = 12865;
        IcCardDriver.CMD_U_IC_CARD_S_VERIFY = 12866;
        IcCardDriver.CMD_U_IC_CARD_S_READ = 12867;
        IcCardDriver.CMD_U_IC_CARD_S_WRITE = 12868;
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
    
    public IcCardDriver(final Context context) {
        this.m_fHandler = null;
        this.m_usbBase = new UsbBase(context);
    }
    
    public IcCardDriver(final Context context, final Handler bioHandler) {
        this.m_fHandler = null;
        this.m_fHandler = bioHandler;
        this.m_usbBase = new UsbBase(context, bioHandler);
    }
    
    public String mxGetJarVersion() {
        final String strVersion = "MIAXIS IcCard Driver V1.0.4.20170427";
        return strVersion;
    }
    
    public int ContactLessCardPowerOn(final short delaytime, final byte[] ATR, final short[] ATRLen) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oPackLen = { (short)oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[256];
        final short[] oRecvLen = { (short)oRecvDataBuffer.length };
        final byte[] SendBufferData = new byte[ConStant.DATA_BUFFER_SIZE_MIN - 7];
        short SendLen = 0;
        final short[] Status = { 0 };
        int offsize = 0;
        final int flag = 1;
        final byte[] dtemp = { (byte)((byte)(delaytime / 256) & 0xFF), (byte)((byte)delaytime & 0xFF) };
        SendBufferData[0] = dtemp[1];
        SendBufferData[1] = dtemp[0];
        offsize += 2;
        SendLen = (short)offsize;
        this.SendMsg("=============================");
        this.SendMsg("SendBufferData:" + zzStringTrans.hex2str(SendBufferData));
        this.SendMsg("SendLen:" + SendLen);
        lRV = this.SendICCardPack(IcCardDriver.CMD_IC_CARD_ACTIVE, SendBufferData, SendLen, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("SendICCardPack failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        lRV = this.zzICCardAPDU(IcCardDriver.CONTACT_LESS_CARD, oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 100);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("zzICCardAPDU failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oRecvDataBuffer:" + zzStringTrans.hex2str(oRecvDataBuffer));
        this.SendMsg("oRecvLen:" + oRecvLen[0]);
        lRV = this.RecvICCardPack(oRecvDataBuffer, oRecvLen[0], Status, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvICCardPack failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        if (Status[0] != 0) {
            this.SendMsg("Status[0]=" + Status[0]);
            return Status[0];
        }
        final byte[] tmp = new byte[oPackLen[0]];
        for (int j = 0; j < tmp.length; ++j) {
            tmp[j] = oPackDataBuffer[j];
        }
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(tmp));
        this.SendMsg("ATRLen[0]=" + ATRLen[0]);
        this.SendMsg("oPackLen[0]=" + oPackLen[0]);
        if (ATR != null && ATRLen[0] >= oPackLen[0]) {
            for (int i = 0; i < oPackLen[0]; ++i) {
                ATR[i] = oPackDataBuffer[i];
            }
            ATRLen[0] = oPackLen[0];
            this.SendMsg("ATR:" + zzStringTrans.hex2str(ATR));
            return ConStant.ERRCODE_SUCCESS;
        }
        ATRLen[0] = 0;
        return ConStant.ERRCODE_MEMORY_OVER;
    }
    
    public int CardAPDU(final byte CardNo, final byte[] SendBuffer, final short SendBufferLen, final byte[] RevcBuffer, final short[] RecvBufferLen, final int flag, final int recvtimeout) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[256];
        final short[] oPackLen = { (short)oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[256];
        final short[] oRecvLen = { (short)oRecvDataBuffer.length };
        final byte[] SendBufferData = new byte[ConStant.DATA_BUFFER_SIZE_MIN - 7];
        short SendLen = 0;
        final short[] Status = { 0 };
        int offsize = 0;
        final byte CardNoTmp = -1;
        SendBufferData[offsize] = CardNo;
        ++offsize;
        for (int i = 0; i < SendBufferLen; ++i) {
            SendBufferData[offsize + i] = SendBuffer[i];
        }
        offsize += SendBufferLen;
        SendLen = (short)offsize;
        this.SendMsg("=============================");
        this.SendMsg("SendBufferData:" + zzStringTrans.hex2str(SendBufferData));
        this.SendMsg("SendLen:" + SendLen);
        lRV = this.SendICCardPack(IcCardDriver.CMD_IC_APDU, SendBufferData, SendLen, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("SendICCardPack failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        if (CardNoTmp == CardNo) {
            lRV = this.zzICCardAPDU(IcCardDriver.CONTACT_LESS_CARD, oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 100);
        }
        else {
            lRV = this.zzICCardAPDU(IcCardDriver.CONTACT_CARD, oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 100);
        }
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("zzICCardAPDU failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oRecvDataBuffer:" + zzStringTrans.hex2str(oRecvDataBuffer));
        this.SendMsg("oRecvLen:" + oRecvLen[0]);
        lRV = this.RecvICCardPack(oRecvDataBuffer, oRecvLen[0], Status, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvICCardPack failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        if (Status[0] != 0) {
            this.SendMsg("Status[0]=" + Status[0]);
            return Status[0];
        }
        final byte[] tmp = new byte[oPackLen[0]];
        for (int j = 0; j < tmp.length; ++j) {
            tmp[j] = oPackDataBuffer[j];
        }
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(tmp));
        this.SendMsg("ATRLen[0]=" + RecvBufferLen[0]);
        this.SendMsg("oPackLen[0]=" + oPackLen[0]);
        if (RevcBuffer != null && RecvBufferLen[0] >= oPackLen[0]) {
            for (int k = 0; k < oPackLen[0]; ++k) {
                RevcBuffer[k] = oPackDataBuffer[k];
            }
            RecvBufferLen[0] = oPackLen[0];
            this.SendMsg("RevcBuffer:" + zzStringTrans.hex2str(RevcBuffer));
            return ConStant.ERRCODE_SUCCESS;
        }
        RecvBufferLen[0] = 0;
        return ConStant.ERRCODE_MEMORY_OVER;
    }
    
    public int ContactLessStorageCardActive(final short delaytime, final byte[] ATR, final short[] ATRLen) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oPackLen = { (short)oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oRecvLen = { (short)oRecvDataBuffer.length };
        final byte[] SendBufferData = new byte[ConStant.DATA_BUFFER_SIZE_MIN - 7];
        short SendLen = 0;
        final short[] Status = { 0 };
        int offsize = 0;
        final int flag = 1;
        final byte[] dtemp = { (byte)((byte)(delaytime / 256) & 0xFF), (byte)((byte)delaytime & 0xFF) };
        SendBufferData[0] = dtemp[0];
        SendBufferData[1] = dtemp[1];
        offsize += 2;
        SendLen = (short)offsize;
        this.SendMsg("=============================");
        this.SendMsg("SendBufferData:" + zzStringTrans.hex2str(SendBufferData));
        this.SendMsg("SendLen:" + SendLen);
        lRV = this.SendICCardPack(IcCardDriver.CMD_U_IC_CARD_AVTIVE, SendBufferData, SendLen, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("SendICCardPack failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        lRV = this.zzICCardAPDU(IcCardDriver.CONTACT_LESS_CARD, oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 100);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("zzICCardAPDU failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oRecvDataBuffer:" + zzStringTrans.hex2str(oRecvDataBuffer));
        this.SendMsg("oRecvLen:" + oRecvLen[0]);
        lRV = this.RecvICCardPack(oRecvDataBuffer, oRecvLen[0], Status, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            this.SendMsg("RecvICCardPack failed,lRV=" + lRV);
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        if (Status[0] != 0) {
            this.SendMsg("Status[0]=" + Status[0]);
            return Status[0];
        }
        final byte[] tmp = new byte[oPackLen[0]];
        for (int j = 0; j < tmp.length; ++j) {
            tmp[j] = oPackDataBuffer[j];
        }
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(tmp));
        this.SendMsg("ATRLen[0]=" + ATRLen[0]);
        this.SendMsg("oPackLen[0]=" + oPackLen[0]);
        if (ATR != null && ATRLen[0] >= oPackLen[0]) {
            for (int i = 0; i < oPackLen[0]; ++i) {
                ATR[i] = oPackDataBuffer[i];
            }
            ATRLen[0] = oPackLen[0];
            this.SendMsg("ATR:" + zzStringTrans.hex2str(ATR));
            return ConStant.ERRCODE_SUCCESS;
        }
        ATRLen[0] = 0;
        return ConStant.ERRCODE_MEMORY_OVER;
    }
    
    public int ContactLessCardVerify(final byte sectorNum, final byte pintype, final byte[] pin) {
        int i = 0;
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oPackLen = { (short)oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oRecvLen = { (short)oRecvDataBuffer.length };
        final byte[] SendBufferData = new byte[ConStant.DATA_BUFFER_SIZE_MIN - 7];
        short SendLen = 0;
        final short[] Status = { 0 };
        int offsize = 0;
        final int flag = 1;
        SendBufferData[offsize] = sectorNum;
        ++offsize;
        SendBufferData[offsize] = pintype;
        ++offsize;
        for (i = 0; i < 6; ++i) {
            SendBufferData[offsize + i] = pin[i];
        }
        offsize += 6;
        SendLen = (short)offsize;
        this.SendMsg("=============================");
        this.SendMsg("SendBufferData:" + zzStringTrans.hex2str(SendBufferData));
        this.SendMsg("SendLen:" + SendLen);
        lRV = this.SendICCardPack(IcCardDriver.CMD_U_IC_CARD_S_VERIFY, SendBufferData, SendLen, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        lRV = this.zzICCardAPDU(IcCardDriver.CONTACT_LESS_CARD, oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 100);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oRecvDataBuffer:" + zzStringTrans.hex2str(oRecvDataBuffer));
        this.SendMsg("oRecvLen:" + oRecvLen[0]);
        lRV = this.RecvICCardPack(oRecvDataBuffer, oRecvLen[0], Status, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        if (Status[0] != 0) {
            return Status[0];
        }
        return ConStant.ERRCODE_SUCCESS;
    }
    
    public int ContactLessCardReadBlock(final byte blockNum, final byte[] block) {
        int i = 0;
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oPackLen = { (short)oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oRecvLen = { (short)oRecvDataBuffer.length };
        final byte[] SendBufferData = new byte[ConStant.DATA_BUFFER_SIZE_MIN - 7];
        short SendLen = 0;
        final short[] Status = { 0 };
        int offsize = 0;
        final int flag = 1;
        SendBufferData[offsize] = blockNum;
        SendLen = (short)(++offsize);
        this.SendMsg("=============================");
        this.SendMsg("SendBufferData:" + zzStringTrans.hex2str(SendBufferData));
        this.SendMsg("SendLen:" + SendLen);
        lRV = this.SendICCardPack(IcCardDriver.CMD_U_IC_CARD_S_READ, SendBufferData, SendLen, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        lRV = this.zzICCardAPDU(IcCardDriver.CONTACT_LESS_CARD, oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 100);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oRecvDataBuffer:" + zzStringTrans.hex2str(oRecvDataBuffer));
        this.SendMsg("oRecvLen:" + oRecvLen[0]);
        lRV = this.RecvICCardPack(oRecvDataBuffer, oRecvLen[0], Status, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        if (Status[0] != 0) {
            return Status[0];
        }
        if (block != null && oPackLen[0] <= 16) {
            for (i = 0; i < oPackLen[0]; ++i) {
                block[i] = oPackDataBuffer[i];
            }
        }
        return ConStant.ERRCODE_SUCCESS;
    }
    
    public int ContactLessCardWriteBlock(final byte blockNum, final byte[] block) {
        int i = 0;
        int lRV = ConStant.ERRCODE_SUCCESS;
        final byte[] oPackDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final short[] oPackLen = { (short)oPackDataBuffer.length };
        final byte[] oRecvDataBuffer = new byte[1024];
        final short[] oRecvLen = { (short)oRecvDataBuffer.length };
        final byte[] SendBufferData = new byte[ConStant.DATA_BUFFER_SIZE_MIN - 7];
        short SendLen = 0;
        final short[] Status = { 0 };
        int offsize = 0;
        final int flag = 1;
        SendBufferData[offsize] = blockNum;
        ++offsize;
        for (i = 0; i < 16; ++i) {
            SendBufferData[offsize + i] = block[i];
        }
        offsize += 16;
        SendLen = (short)offsize;
        this.SendMsg("=============================");
        this.SendMsg("SendBufferData:" + zzStringTrans.hex2str(SendBufferData));
        this.SendMsg("SendLen:" + SendLen);
        lRV = this.SendICCardPack(IcCardDriver.CMD_U_IC_CARD_S_WRITE, SendBufferData, SendLen, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        lRV = this.zzICCardAPDU(IcCardDriver.CONTACT_LESS_CARD, oPackDataBuffer, oPackLen[0], 100, oRecvDataBuffer, oRecvLen, 100);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oRecvDataBuffer:" + zzStringTrans.hex2str(oRecvDataBuffer));
        this.SendMsg("oRecvLen:" + oRecvLen[0]);
        lRV = this.RecvICCardPack(oRecvDataBuffer, oRecvLen[0], Status, oPackDataBuffer, oPackLen, flag);
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return lRV;
        }
        this.SendMsg("=============================");
        this.SendMsg("oPackDataBuffer:" + zzStringTrans.hex2str(oPackDataBuffer));
        this.SendMsg("oPackLen:" + oPackLen[0]);
        if (Status[0] != 0) {
            return Status[0];
        }
        return ConStant.ERRCODE_SUCCESS;
    }
    
    void EncData(final byte[] lpRawData, final int nRawLen, final byte[] lpEncData) {
        int i;
        int aaa;
        for (i = 0, i = 0; i < nRawLen; ++i) {
            aaa = JUnsigned(lpRawData[i]);
            lpEncData[2 * i] = (byte)((aaa >> 4) + 48);
            lpEncData[2 * i + 1] = (byte)((aaa & 0xF) + 48);
        }
        lpEncData[2 * nRawLen] = 0;
    }
    
    void DecData(final byte[] lpEncData, final int nRawLen, final byte[] lpRawData) {
        int i;
        for (i = 0, i = 0; i < nRawLen; ++i) {
            lpRawData[i] = (byte)((lpEncData[2 * i] - 48 << 4) + (lpEncData[2 * i + 1] - 48));
        }
    }
    
    int SendICCardPack(final short CommandID, final byte[] SendDataBuffer, short SendLen, final byte[] oPackDataBuffer, final short[] oPackLen, final int flag) {
        final byte[] bodyBufferData = new byte[ConStant.CMD_BUFSIZE * 2];
        final byte[] tempBufferData = new byte[ConStant.CMD_BUFSIZE * 2];
        final byte[] DataEncode = new byte[ConStant.CMD_BUFSIZE * 2];
        final byte packstx = 2;
        final byte packetx = 3;
        byte AddCheck = 0;
        final byte[] dtemp = new byte[2];
        int offsize = 0;
        tempBufferData[offsize] = packstx;
        ++offsize;
        dtemp[1] = (dtemp[0] = 0);
        SendLen += 2;
        dtemp[0] = (byte)((byte)(SendLen / 256) & 0xFF);
        dtemp[1] = (byte)((byte)SendLen & 0xFF);
        for (int i = 0; i < dtemp.length; ++i) {
            tempBufferData[offsize + i] = dtemp[i];
        }
        offsize += dtemp.length;
        dtemp[1] = (dtemp[0] = 0);
        dtemp[0] = (byte)((byte)(CommandID / 256) & 0xFF);
        dtemp[1] = (byte)((byte)CommandID & 0xFF);
        for (int i = 0; i < dtemp.length; ++i) {
            tempBufferData[offsize + i] = dtemp[i];
        }
        offsize += dtemp.length;
        for (int i = 0; i < SendLen - 2; ++i) {
            tempBufferData[offsize + i] = SendDataBuffer[i];
        }
        offsize = offsize + SendLen - 2;
        for (int i = 0; i < SendLen; ++i) {
            AddCheck ^= tempBufferData[i + 3];
        }
        tempBufferData[offsize] = AddCheck;
        ++offsize;
        tempBufferData[offsize] = packetx;
        ++offsize;
        if (flag == 1) {
            final int bodylen = offsize - 2;
            for (int i = 0; i < bodylen; ++i) {
                bodyBufferData[i] = tempBufferData[i + 1];
            }
            this.EncData(bodyBufferData, bodylen, DataEncode);
            offsize = 0;
            tempBufferData[offsize] = packstx;
            ++offsize;
            for (int i = 0; i < bodylen * 2; ++i) {
                tempBufferData[offsize + i] = DataEncode[i];
            }
            offsize += bodylen * 2;
            tempBufferData[offsize] = packetx;
            ++offsize;
        }
        if (oPackDataBuffer != null && oPackLen[0] >= offsize) {
            for (int i = 0; i < offsize; ++i) {
                oPackDataBuffer[i] = tempBufferData[i];
            }
            oPackLen[0] = (short)offsize;
            return ConStant.ERRCODE_SUCCESS;
        }
        return ConStant.ERRCODE_MEMORY_OVER;
    }
    
    int RecvICCardPack(final byte[] RecvDataBuffer, final short RecvLen, final short[] Status, final byte[] oPackDataBuffer, final short[] oPackLen, final int flag) {
        int i = 0;
        final byte[] bodyBufferData = new byte[ConStant.CMD_BUFSIZE];
        final byte[] tempBufferData = new byte[ConStant.CMD_BUFSIZE];
        final byte[] DecDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        byte packstx = 0;
        byte packetx = 0;
        byte recvCheck = 0;
        byte currentCheck = 0;
        final byte[] dtemp = new byte[2];
        int offsize = 0;
        short len = 0;
        final int bodylen = RecvLen - 2;
        packstx = RecvDataBuffer[0];
        if (packstx != 2) {
            return ConStant.ERRCODE_CRC;
        }
        ++offsize;
        packetx = RecvDataBuffer[RecvLen - 1];
        if (packetx != 3) {
            return ConStant.ERRCODE_CRC;
        }
        for (i = 0; i < bodylen; ++i) {
            bodyBufferData[i] = RecvDataBuffer[i + offsize];
        }
        if (flag == 1) {
            final byte[] tmp = new byte[bodylen];
            for (int j = 0; j < tmp.length; ++j) {
                tmp[j] = bodyBufferData[j];
            }
            this.SendMsg("bodyBufferData:" + zzStringTrans.hex2str(tmp));
            this.DecData(bodyBufferData, bodylen, DecDataBuffer);
            for (i = 0; i < bodylen; ++i) {
                bodyBufferData[i] = 0;
            }
            for (i = 0; i < bodylen / 2; ++i) {
                bodyBufferData[i] = DecDataBuffer[i];
            }
            for (int j = 0; j < tmp.length; ++j) {
                tmp[j] = bodyBufferData[j];
            }
            this.SendMsg("DecDataBuffer:" + zzStringTrans.hex2str(tmp));
        }
        offsize = 0;
        dtemp[0] = bodyBufferData[1];
        dtemp[1] = bodyBufferData[0];
        offsize += 2;
        int a = dtemp[0];
        if (a < 0) {
            a += 256;
        }
        int b = dtemp[1];
        if (b < 0) {
            b += 256;
        }
        len = (short)(b * 256 + a);
        this.SendMsg("len:" + len);
        for (i = 0; i < len; ++i) {
            tempBufferData[i] = bodyBufferData[i + offsize];
        }
        offsize += len;
        recvCheck = bodyBufferData[offsize];
        ++offsize;
        for (i = 0; i < len; ++i) {
            currentCheck ^= tempBufferData[i];
        }
        if (currentCheck != recvCheck) {
            return ConStant.ERRCODE_CRC;
        }
        dtemp[0] = tempBufferData[1];
        dtemp[1] = tempBufferData[0];
        a = dtemp[0];
        if (a < 0) {
            a += 256;
        }
        b = dtemp[1];
        if (b < 0) {
            b += 256;
        }
        Status[0] = (short)(b * 256 + a);
        this.SendMsg("Status[0]:" + Status[0]);
        for (i = 0; i < len - 2; ++i) {
            oPackDataBuffer[i] = tempBufferData[i + 2];
        }
        oPackLen[0] = (short)(len - 2);
        return ConStant.ERRCODE_SUCCESS;
    }
    
    int zzICCardAPDU(final byte cardtype, final byte[] lpSendData, final short wSendLength, final int iSendTime, final byte[] lpRecvData, final short[] io_wRecvLength, final int iRecvTime) {
        int ret = ConStant.ERRCODE_SUCCESS;
        byte nCommandID = IcCardDriver.CMD_ICCARD_COMMAND;
        if (cardtype == IcCardDriver.CONTACT_LESS_CARD) {
            nCommandID = IcCardDriver.CMD_IDCARD_COMMAND;
        }
        else if (cardtype == IcCardDriver.CONTACT_CARD) {
            nCommandID = IcCardDriver.CMD_ICCARD_COMMAND;
        }
        else {
            if (cardtype != IcCardDriver.CONTACT_64CARD) {
                return ConStant.ERRCODE_CRC;
            }
            nCommandID = IcCardDriver.CMD_ID64CARD_COMMAND;
        }
        ret = this.ExeCommand(nCommandID, lpSendData, wSendLength, iSendTime, lpRecvData, io_wRecvLength, iRecvTime);
        return ret;
    }
    
    int ExeCommand(final byte nCommandID, final byte[] lpSendData, final short wSendLength, final int iSendTime, final byte[] lpRecvData, final short[] io_wRecvLength, int iRecvTime) {
        final byte[] outBuffer = new byte[1024];
        final byte[] buf = new byte[ConStant.DATA_BUFFER_SIZE + 1];
        final byte[] nRetCode = { 0 };
        final short[] wRecvLen = { (short)buf.length };
        int realsize = 0;
        final int packsize = ConStant.DATA_BUFFER_SIZE_MIN;
        int iRet = ConStant.ERRCODE_SUCCESS;
        iRet = this.m_usbBase.openDev(ConStant.VID, ConStant.PID);
        if (iRet != 0) {
            this.SendMsg("openDev failed,iRet=" + iRet);
            return ConStant.ERRCODE_NODEVICE;
        }
        int packnum = wSendLength / packsize;
        final int lastpacksize = wSendLength % packsize;
        if (lastpacksize != 0) {
            ++packnum;
        }
        for (int i = 0; i < packnum; ++i) {
            for (int j = 0; j < buf.length; ++j) {
                buf[j] = 0;
            }
            if (i == packnum - 1) {
                for (int j = 0; j < lastpacksize; ++j) {
                    buf[j] = lpSendData[j + i * packsize];
                }
                iRet = this.sendPacket(nCommandID, buf, lastpacksize);
            }
            else {
                for (int j = 0; j < packsize; ++j) {
                    buf[j] = lpSendData[j + i * packsize];
                }
                iRet = this.sendPacket(nCommandID, buf, packsize);
            }
            if (iRet != 0) {
                this.SendMsg("sendPacket failed,iRet=" + iRet);
                this.m_usbBase.closeDev();
                return ConStant.ERRCODE_IOSEND;
            }
        }
        long duration = -1L;
        int timeout = 5000;
        final Calendar time1 = Calendar.getInstance();
        if (iRecvTime < 2000) {
            iRecvTime = 2000;
        }
        timeout = iRecvTime;
        while (duration < timeout) {
            iRet = this.recvPacket(nRetCode, buf, wRecvLen, iRecvTime);
            if (iRet == ConStant.ERRCODE_SUCCESS) {
                iRecvTime = 50;
            }
            if (iRet != ConStant.ERRCODE_TIMEOUT) {
                this.SendMsg("===wRecvLen:" + wRecvLen[0]);
                if (wRecvLen[0] > 0) {
                    final byte[] tmp = new byte[wRecvLen[0]];
                    for (int k = 0; k < tmp.length; ++k) {
                        tmp[k] = buf[k];
                    }
                    this.SendMsg("tmp:" + zzStringTrans.hex2str(tmp));
                }
                this.SendMsg("buf:" + zzStringTrans.hex2str(buf));
                if (iRet == ConStant.ERRCODE_SUCCESS) {
                    if (wRecvLen[0] >= 2) {
                        if (realsize + wRecvLen[0] - 2 > 1024) {
                            this.m_usbBase.closeDev();
                            return ConStant.ERRCODE_MEMORY_OVER;
                        }
                        for (int l = 0; l < wRecvLen[0] - 2; ++l) {
                            outBuffer[l + realsize] = buf[l + 2];
                        }
                        realsize = realsize + wRecvLen[0] - 2;
                    }
                    else {
                        realsize = realsize;
                    }
                    final Calendar time2 = Calendar.getInstance();
                    duration = time2.getTimeInMillis() - time1.getTimeInMillis();
                    continue;
                }
                if (wRecvLen[0] <= 0) {
                    this.m_usbBase.closeDev();
                    return ConStant.ERRCODE_CRC;
                }
            }
            this.SendMsg("io_wRecvLength[0]:" + io_wRecvLength[0]);
            this.SendMsg("realsize:" + realsize);
            if (io_wRecvLength[0] > 0 && realsize <= io_wRecvLength[0]) {
                if (realsize > 0) {
                    for (int l = 0; l < realsize; ++l) {
                        lpRecvData[l] = outBuffer[l];
                    }
                }
                io_wRecvLength[0] = (short)realsize;
                this.m_usbBase.closeDev();
                return ConStant.ERRCODE_SUCCESS;
            }
            this.m_usbBase.closeDev();
            return ConStant.ERRCODE_MEMORY_OVER;
        }
        this.m_usbBase.closeDev();
        return ConStant.ERRCODE_TIMEOUT;
    }
    
    private int sendPacket(final byte bCmd, final byte[] bSendBuf, final int iDataLen) {
        int iRet = -1;
        int offsize = 0;
        short iCheckSum = 0;
        final byte[] DataBuffer = new byte[ConStant.CMD_DATA_BUF_SIZE];
        DataBuffer[offsize++] = ConStant.CMD_REQ_FLAG;
        DataBuffer[offsize++] = 0;
        DataBuffer[offsize++] = 0;
        DataBuffer[offsize++] = (byte)(iDataLen + 1 & 0xFF);
        DataBuffer[offsize++] = (byte)(iDataLen + 1 >> 8);
        DataBuffer[offsize++] = bCmd;
        if (iDataLen > 1) {
            for (int i = 0; i < iDataLen; ++i) {
                DataBuffer[offsize++] = bSendBuf[i];
            }
        }
        for (int j = 3; j < offsize; ++j) {
            short tmp = DataBuffer[j];
            if (tmp < 0) {
                tmp += 256;
            }
            iCheckSum += tmp;
        }
        if (iCheckSum < 0) {
            iCheckSum += 256;
        }
        DataBuffer[offsize++] = (byte)(iCheckSum & 0xFF);
        DataBuffer[offsize++] = (byte)((byte)(iCheckSum >> 8) & 0xFF);
        iRet = this.m_usbBase.sendData(DataBuffer, DataBuffer.length, ConStant.CMD_TIMEOUT);
        if (iRet < 0) {
            return -1;
        }
        return 0;
    }
    
    private int recvPacket(final byte[] bResult, final byte[] bRecvBuf, final short[] iRecvLen, final int iTimeOut) {
        int iRet = -1;
        int offsize = 0;
        int iDataLen = 0;
        int a = 0;
        int b = 0;
        final byte[] DataBuffer = new byte[ConStant.CMD_DATA_BUF_SIZE];
        final byte[] SRN = new byte[2];
        short recvCheckSum = 0;
        short currentCheckSum = 0;
        iRet = this.m_usbBase.recvData(DataBuffer, DataBuffer.length, iTimeOut);
        if (iRet < 0) {
            return iRet;
        }
        if (DataBuffer[offsize++] != ConStant.CMD_RET_FLAG) {
            return ConStant.ERRCODE_CRC;
        }
        SRN[0] = DataBuffer[offsize++];
        SRN[1] = DataBuffer[offsize++];
        a = DataBuffer[offsize++];
        if (a < 0) {
            a += 256;
        }
        b = DataBuffer[offsize++];
        if (b < 0) {
            b += 256;
        }
        iDataLen = b * 256 + a;
        if (iDataLen > ConStant.CMD_DATA_BUF_SIZE - 5) {
            return ConStant.ERRCODE_CRC;
        }
        bResult[0] = DataBuffer[offsize];
        if (iDataLen - 1 > 0) {
            for (int i = 1; i < iDataLen; ++i) {
                bRecvBuf[i - 1] = DataBuffer[offsize + i];
            }
        }
        iRecvLen[0] = (short)(iDataLen - 1);
        offsize += iDataLen;
        for (int i = 3; i < offsize; ++i) {
            a = DataBuffer[i];
            if (a < 0) {
                a += 256;
            }
            currentCheckSum += (short)a;
        }
        a = DataBuffer[offsize++];
        if (a < 0) {
            a += 256;
        }
        b = DataBuffer[offsize++];
        if (b < 0) {
            b += 256;
        }
        recvCheckSum = (short)(b * 256 + a);
        if (currentCheckSum != recvCheckSum) {
            return ConStant.ERRCODE_CRC;
        }
        return ConStant.ERRCODE_SUCCESS;
    }
    
    public static int JUnsigned(final int x) {
        if (x >= 0) {
            return x;
        }
        return x + 256;
    }
}

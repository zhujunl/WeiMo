package android.serialport.api;

import android.serialport.MXDataCode;
import android.util.Log;

import java.math.BigInteger;

/**
 * @author ZJL
 * @date 2022/6/22 13:14
 * @des
 * @updateAuthor
 * @updateDes
 */
public class SerialPortHelper {
    private final String TAG="SerialPortHelper";
    public static final short CMD_SERIAL_VERSION=0x0003;
    public static final short CMD_SERIAL_BOARDSN=0x008;
    public static final short CMD_SERIAL_ATR=0x0001;
    public static final short CMD_SERIAL_CHIPSN=0x0006;
    public static final short CMD_SERIAL_READ=0x0002;

    public static final byte CMD_SERIAL= (byte) 0xAF;
    public static final byte CMD_SERIAL2= (byte) 0xA3;
    public static final byte CMD_SERIAL3= (byte) 0xA1;

    public static final short W_SAMID=0x12FF;
    public static final short W_FindCARD_CONTROL = 0x2001;
    public static final short W_SELECTCARD_CONTROL = 0x2002;
    public static final short W_READ=0x301B;
    public static final short W_INTERNET_READ=0x3021;
    public static final short W_APDU= (short) 0xFA91;
    public static final short W_CHECK= (short) 0xA10C;
    public static final short W_SIGN=0x12FE;

    public static final short W_APPLY_AUTHORIZATION= (short) 0xA10D;

    public static final byte[] BSENDBUF = { 0x32,0x30, 0x32,0x30,0x32,0x30, 0x32,0x30,0x32,0x30, 0x32,0x30,0x32,0x30, 0x32,0x30};

    public final int sendSize=8;
    public final int recvSize=512;

    private SerialPortManager mSerialPortManager;

    public SerialPortHelper() {
        mSerialPortManager = new SerialPortManager();
    }

    /**
     * 获取版本
     * @param ver   [OUT]固件版本
     * @return 0 = 成功 , 其他 = 失败
     * */
    public int getVersion(StringBuffer ver){
        byte[] send=new byte[sendSize];
        byte[] recv=new byte[recvSize];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        SendSerialPack(CMD_SERIAL,CMD_SERIAL_VERSION,send,null,0);
        int size = mSerialPortManager.sendSerialPort(send, recv);
        if (size<0){
            return size;
        }
        byte[] out =new byte[size];
        int re = RecvSerialPack(recv, size, out);
        if (re!=0){
            return re;
        }
        String str= zzStringTrans.byteToStr(out);
        ver.append(str);
        close();
        return ConStant.ERRCODE_SUCCESS;
    }

    /**
     * 获取卡片ATR（找卡）
     * @return 卡片ATR数据
     * */

    public byte[] getAtr(){
        int re=ConStant.ERRCODE_SUCCESS;
        byte[] send=new byte[sendSize];
        byte[] recv=new byte[recvSize];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return null;
        }
        re=SendSerialPack(CMD_SERIAL3, CMD_SERIAL_ATR, send, null, 0);
        int size = mSerialPortManager.sendSerialPort(send, recv);
        byte[] out =new byte[size];
        if (size<0){
            return null;
        }
        re = RecvSerialPack(recv, size, out);
        if (re!=0){
            return null;
        }
        if (out[2]!=-112){
            return null;
        }
        byte[] real=new byte[out.length-3];
        System.arraycopy(out,3,real,0,real.length);
        close();
        return real;
    }

    /**
     * 获取读卡器序列号
     * @param sn  [OUT]板卡序列号
     * @return 0 = 成功 , 其他 = 失败
     * */
    public int getBoardSN(StringBuffer sn){
        int re=ConStant.ERRCODE_SUCCESS;
        byte[] send=new byte[sendSize];
        byte[] recv=new byte[recvSize];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        re= SendSerialPack(CMD_SERIAL,CMD_SERIAL_BOARDSN,send,null,0);
        int size = mSerialPortManager.sendSerialPort(send, recv);
        byte[] out=new byte[recv.length];
        if (size<0){
            return size;
        }
        re = RecvSerialPack(recv, size, out);
        if (re!=0){
            return re;
        }
        String str= zzStringTrans.byteToStr(out);
        sn.append(str);
        close();
        return ConStant.ERRCODE_SUCCESS;
    }

    /**
     * 获取安全芯片序列号
     * @param snbuf    [OUT]安全芯片序列号
     * @return 0 = 成功 , 其他 = 失败；
     * */
    public int getChipSN(byte[] snbuf){
        int re=ConStant.ERRCODE_SUCCESS;
        byte[] send=new byte[sendSize];
        byte[] recv=new byte[recvSize];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        re=SendSerialPack(CMD_SERIAL, CMD_SERIAL_CHIPSN, send, null, 0);
        byte[] b=new byte[]{
                0x5a , (byte) 0xaf,0x00 ,0x06 ,0x00 ,0x00 , (byte) 0xf3, (byte) 0xfd
        };
        int size = mSerialPortManager.sendSerialPort(b, recv);
        if (size<0){
            return size;
        }
        re = RecvSerialPack(recv, size, snbuf);
        close();
        return re;
    }

    /**
     * 获取微模块SAMID
     * @param samid    [OUT]SAMID
     * @return 0 = 成功 , 其他 = 失败；
     * */
    public int getSAMID(byte[] samid){
        int re=ConStant.ERRCODE_SUCCESS;
        byte[] w=new byte[2];
        byte[] send=new byte[sendSize+w.length];
        byte[] recv=new byte[recvSize];
        w[0]=(byte)((byte)(W_SAMID >> 8) & 0xFF);
        w[1]=(byte)((byte)W_SAMID & 0xFF);
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        re=SendSerialPack(CMD_SERIAL2, CMD_SERIAL_ATR, send, w, 2);
        int size = mSerialPortManager.sendSerialPort(send, recv);
        byte[] out =new byte[samid.length+3];
        if (size<0){
            return size;
        }
        re = RecvSerialPack(recv, size, out);
        if (out[2]!=-112){
            return -1;
        }
        System.arraycopy(out,3,samid,0,samid.length);
        close();
        return re;
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
    public int readIDCardMsg(byte[] baseinf, int[] basesize, byte[] photo, int[] photosize, byte[] fpimg, int[] fpsize){
        int iRet=ConStant.ERRCODE_SUCCESS;
        final byte[] pucManaInfo = new byte[256];
        iRet = this.getSAMID(pucManaInfo);
        iRet = this.StartFindIDCard(pucManaInfo);
        if (iRet!=-97){
            iRet = this.StartFindIDCard(pucManaInfo);
            if (iRet!=-97){
                return iRet;
            }
        }
        iRet = this.SelectIDCard(pucManaInfo);
        if (iRet != -112) {
            return iRet;
        }
        iRet = ReadFullMsgUnicode(baseinf, basesize, photo, photosize, fpimg, fpsize);
        return iRet;
    }

    /**
     * SAM透传指令
     * @param cmd   [IN]指令数据
     * @return 响应数据
     * */
    public byte[] samCommand(byte[] cmd){
        int re=ConStant.ERRCODE_SUCCESS;
        byte[] send=new byte[cmd.length+sendSize];
        byte[] recv=new byte[2048];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return null;
        }
        re=SendSerialPack(CMD_SERIAL2, CMD_SERIAL_ATR, send, cmd, cmd.length);
        int size = mSerialPortManager.sendSerialPort(send, recv,0);
        if (size<0){
            return null;
        }
//        re = WRecvSerialPack(recv, size, samid);
        close();
        return recv;
    }

    public byte[] samCommand(byte[] cmd,byte[] bSendBuf){
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] out=new byte[ConStant.CMD_BUFSIZE];
        final byte[] oPackDataBuffer = new byte[cmd.length+bSendBuf.length+sendSize];
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return null;
        }
        byte[] w=new byte[cmd.length+bSendBuf.length];
        System.arraycopy(cmd,0,w,0,cmd.length);
        System.arraycopy(bSendBuf,0,w,cmd.length,bSendBuf.length);
        lRV=SendSerialPack(CMD_SERIAL2, CMD_SERIAL_ATR, oPackDataBuffer, w, w.length);
        int size = mSerialPortManager.sendSerialPort(oPackDataBuffer, oRecvDataBuffer,0);
        if (size<0){
            return null;
        }
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return null;
        }
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, oRecvDataBuffer.length);
        close();
        return out;
    }

    /**
     * SAM+身份证透传指令
     * @param cmd   [IN]指令数据
     * @return 响应数据
     * */
    public byte[] samCardCommand(byte[] cmd){
        int re=ConStant.ERRCODE_SUCCESS;
        byte[] send=new byte[sendSize+cmd.length];
        byte[] recv=new byte[recvSize];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return null;
        }
        re=SendSerialPack(CMD_SERIAL2, CMD_SERIAL_READ, send, cmd, cmd.length);
        int size = mSerialPortManager.sendSerialPort(send, recv);
        if (size<0){
            return null;
        }
        //        re = WRecvSerialPack(recv, size, samid);
        close();
        return recv;
    }

    public byte[] samCardCommand(byte[] cmd,byte[] bSendBuf){
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] out=new byte[ConStant.CMD_BUFSIZE];
        byte[] oPackDataBuffer=new byte[sendSize+cmd.length];
        final byte[] oRecvDataBuffer = new byte[ConStant.CMD_BUFSIZE];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return null;
        }
        byte[] w=new byte[cmd.length+bSendBuf.length];
        System.arraycopy(cmd,0,w,0,cmd.length);
        System.arraycopy(bSendBuf,0,w,cmd.length,bSendBuf.length);
        lRV=SendSerialPack(CMD_SERIAL2, CMD_SERIAL_READ, oPackDataBuffer, w, w.length);
        int size = mSerialPortManager.sendSerialPort(oPackDataBuffer, oRecvDataBuffer);
        if (size<0){
            return null;
        }
        if (lRV != ConStant.ERRCODE_SUCCESS) {
            return null;
        }
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, oRecvDataBuffer.length);

        return out;
    }

    /**
     * APDU指令传输
     * @param apducmd [IN]apdu指令数据
     * @return apdu的响应数据
     * */
    public byte[] transceive(byte[] apducmd){
        byte[] out=new byte[512];
        int iRet = ConStant.ERRCODE_SUCCESS;
        byte[] Atr = this.getAtr();
        if (Atr==null){
            return Atr;
        }
        iRet=APDU(apducmd,out);
        return out;
    }

    /**
     * 切回boot态
     * @return 0成功，其他失败
     * */
    public int firmwareUpdate(){
        int re=ConStant.ERRCODE_SUCCESS;
        byte[] w=new byte[2];
        w[0]=(byte)((byte)(W_SAMID >> 8) & 0xFF);
        w[1]=(byte)((byte)W_SAMID & 0xFF);
        byte[] send=new byte[sendSize+w.length];
        byte[] recv=new byte[recvSize];
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        re=SendSerialPack(CMD_SERIAL2, CMD_SERIAL_VERSION, send, w, 2);
        int size = mSerialPortManager.sendSerialPort(send, recv);
        if (size<0){
            return size;
        }
        close();
        return re;
    }

    public void close(){
        mSerialPortManager.closeSerialPort();
    }

    int APDU(byte[] apducmd,byte[] out){
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] oPackDataBuffer=new byte[sendSize+apducmd.length];
        final byte[] oRecvDataBuffer = new byte[ConStant.DATA_BUFFER_SIZE_MIN];
        final int[] result = { 0 };
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        byte[] w=new byte[2+apducmd.length];
        w[0]=(byte)((byte)(W_APDU >> 8) & 0xFF);
        w[1]=(byte)((byte)W_APDU & 0xFF);
        System.arraycopy(apducmd,0,w,0,apducmd.length);
        lRV=SendSerialPack(CMD_SERIAL2, CMD_SERIAL_ATR, oPackDataBuffer, w, w.length);
        int size = mSerialPortManager.sendSerialPort(oPackDataBuffer, oRecvDataBuffer);
        if (size<0){
            return size;
        }
        if (oRecvDataBuffer.length >= 0)
            System.arraycopy(oRecvDataBuffer, 0, out, 0, oRecvDataBuffer.length);
        close();
        return result[0];
    }

    int StartFindIDCard(final byte[] pucManaInfo) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] w=new byte[2];
        final byte[] oPackDataBuffer = new byte[sendSize+w.length];
        final byte[] oRecvDataBuffer = new byte[recvSize];
        byte[] realbyte=new byte[oRecvDataBuffer.length];
        w[0]=(byte)((byte)(W_FindCARD_CONTROL >> 8) & 0xFF);
        w[1]=(byte)((byte)W_FindCARD_CONTROL & 0xFF);
        lRV = SendSerialPack(CMD_SERIAL2, CMD_SERIAL_READ, oPackDataBuffer, w, w.length);
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        int size = mSerialPortManager.sendSerialPort(oPackDataBuffer, oRecvDataBuffer);
        if (size<0){
            return size;
        }
        lRV = RecvSerialPack(oRecvDataBuffer, size, realbyte);
        close();
        return realbyte[2];
    }

    int SelectIDCard(final byte[] pucManaInfo){
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] w=new byte[2];
        final byte[] oPackDataBuffer = new byte[sendSize+w.length];
        final byte[] oRecvDataBuffer = new byte[recvSize];
        byte[] realbyte=new byte[oRecvDataBuffer.length];
        w[0]=(byte)((byte)(W_SELECTCARD_CONTROL >> 8) & 0xFF);
        w[1]=(byte)((byte)W_SELECTCARD_CONTROL & 0xFF);
        lRV = SendSerialPack(CMD_SERIAL2, CMD_SERIAL_READ, oPackDataBuffer, w, w.length);
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        int size = mSerialPortManager.sendSerialPort(oPackDataBuffer, oRecvDataBuffer);
        if (size<0){
            return size;
        }
        lRV = RecvSerialPack(oRecvDataBuffer, size, realbyte);
        close();
        return realbyte[2];
    }

    int ReadFullMsgUnicode(final byte[] pucCHMsg, final int[] puiCHMsgLen, final byte[] PucPHMsg, final int[] puiPHMsgLen, final byte[] PucFPMsg, final int[] puiFPMsgLen) {
        int lRV = ConStant.ERRCODE_SUCCESS;
        byte[] w=new byte[18];
        final byte[] oPackDataBuffer = new byte[sendSize+w.length];
        final byte[] oRecvDataBuffer = new byte[3096];
        byte[] realbyte=new byte[oRecvDataBuffer.length];
        w[0]=(byte)((byte)(W_INTERNET_READ >> 8) & 0xFF);
        w[1]=(byte)((byte)W_INTERNET_READ & 0xFF);
        System.arraycopy(BSENDBUF,0,w,2,BSENDBUF.length);
        lRV = SendSerialPack(CMD_SERIAL2, CMD_SERIAL_READ, oPackDataBuffer, w, w.length);
        SerialPort serialPort = mSerialPortManager.openSerialPort();
        if (serialPort==null){
            return ConStant.ERRCODE_DEVICE;
        }
        int size = mSerialPortManager.sendSerialPort(oPackDataBuffer, oRecvDataBuffer,0);
        Log.e(TAG, "readcard===" + size);
        if (size<0){
            return size;
        }
        lRV = RecvSerialPack(oRecvDataBuffer, size, realbyte);
        if (realbyte[2]!=-112){
            return realbyte[2];
        }
        Log.e(TAG, "real;" +zzStringTrans.hex2str(realbyte) );
        byte[] real=new byte[realbyte.length-3];
        System.arraycopy(realbyte,3,real,0,real.length);
        puiCHMsgLen[0]=32;
        System.arraycopy(real,39,pucCHMsg,0,puiCHMsgLen[0]);
        puiPHMsgLen[0]=1024;
        puiFPMsgLen[0]=1024;
        int a=real[87];
        int b=real[88];
        if(a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        int len=a*256+b;
        System.arraycopy(real,89+len+32+64+4,PucPHMsg,0,puiPHMsgLen[0]);
        System.arraycopy(real,89+len+32+64+4+1024,PucFPMsg,0,PucFPMsg[0]);
        close();
        return lRV;
    }


    private int SendSerialPack(byte cmd,short cmdParameter,byte[] send,byte[] sendData,int sendDataWidth){
        byte[] dataBuffer=new byte[send.length];
        int offsize=0;
        byte XOR=0;
        dataBuffer[offsize++]=0x5A;
        dataBuffer[offsize++]=cmd;
        dataBuffer[offsize++] = (byte)((byte)(cmdParameter >> 8) & 0xFF);
        dataBuffer[offsize++]= (byte)((byte)cmdParameter & 0xFF);
        dataBuffer[offsize++] = (byte)((byte)(sendDataWidth >> 8) & 0xFF);
        dataBuffer[offsize++]= (byte)((byte)sendDataWidth & 0xFF);
        for(int i=0; i<6; i++) {
            XOR ^= dataBuffer[i];
        }
        dataBuffer[offsize++]=XOR;
        if (sendDataWidth>0){
            for (int i = 0; i < sendDataWidth; i++) {
                dataBuffer[offsize+i]=sendData[i];
            }
            offsize+=sendDataWidth;
        }
        XOR=0;
        for(int i=0; i<(7+sendDataWidth); i++) {
            XOR += dataBuffer[i];
        }
        XOR = (byte) ~XOR;
        dataBuffer[offsize]=XOR;
        System.arraycopy(dataBuffer,0,send,0,dataBuffer.length);
        return ConStant.ERRCODE_SUCCESS;
    }

    private int RecvSerialPack(byte[] recv,int recvWidth,byte[] out){
        byte XOR=0;
        int offsize=0;
        int pack_len=0;
        for(int i=0; i<6; i++) {
            XOR ^= recv[i];
            offsize++;
        }
        if (XOR!=recv[offsize++]){
            return ConStant.ERRORCODE_XOR;
        }
        XOR=0;
        int a=recv[4];
        int b=recv[5];
        if (a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        pack_len=a*256+b;
        for(int i=0; i<(7+pack_len); i++)
        {
            XOR += recv[i];
        }
        XOR = (byte) ~XOR;
        if (XOR!=recv[recvWidth-1]){
            return ConStant.ERRORCODE_SUM;
        }
        System.arraycopy(recv,offsize,out,0,pack_len);
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
        return String.format("%02d%02d%08d%010d%010d", sTemp1, sTemp2, sTemp3, sTemp4, sTemp5);
    }

}

package org.zz.idcard_hid_driver;

public class ConStant
{
    public static boolean DEBUG;
    public static int VID;
    public static int PID;
    public static int SHOW_MSG;
    public static int DATA_BUFFER_SIZE;
    public static int DATA_BUFFER_SIZE_MIN;
    public static int REVC_BUFFER_SIZE_MIN;
    public static int CMD_BUFSIZE;
    public static int CMD_DATA_BUF_SIZE;
    public static int CMD_TIMEOUT;
    public static byte CMD_REQ_FLAG;
    public static byte CMD_RET_FLAG;
    public static int ERRCODE_SUCCESS_1;
    public static int ERRCODE_SUCCESS;
    public static int ERRCODE_NODEVICE;
    public static int ERRCODE_INDEXERR;
    public static int ERRCODE_TIMEOUT;
    public static int ERRCODE_CANCEL;
    public static int ERRCODE_DEVBUSY;
    public static int ERRCODE_UPIMAGE;
    public static int ERRCODE_DEVLIST;
    public static int ERRCODE_IOSEND;
    public static int ERRCODE_IORECV;
    public static int ERRCODE_HANDLE_NULL;
    public static int ERRCODE_CRC;
    public static int ERRCODE_MEMORY_OVER;
    public static int ERRCODE_ID_CARD_FIND;
    public static int ERRCODE_ID_CARD_READ;
    public static int ERRCODE_ANTENNA_ON;
    public static int ERRCODE_TCPIOSEND;
    public static int ERRCODE_TCPIORECV;
    public static int ERRCODE_TCPSERVER_INI;
    public static int ERRCODE_TCP_STATE;
    public static int ERRCODE_SAVE_IDBMP;
    public static int ERRCODE_READ_ID_MODULE;
    public static int ERRCODE_RESET_ID;
    public static int ERRCODE_ID_MSG;
    public static int ERRCODE_ANTENNA_OFF;

    public static int ERRCODE_TRANS;
    public static int ERRCODE_READCARD;
    public static int ERRCODE_DEVICE;
    public static int ERRORCODE_NOCARD;
    public static int ERRORCODE_CMD;
    public static int ERRORCODE_APDU;
    public static int ERRORCODE_SAMID;

    static {
        ConStant.DEBUG = false;
        ConStant.VID = 4292;
        ConStant.PID = 7;
        ConStant.SHOW_MSG = 255;
        ConStant.DATA_BUFFER_SIZE = 64;
        ConStant.DATA_BUFFER_SIZE_MIN = 512;
        ConStant.REVC_BUFFER_SIZE_MIN = 512;
        ConStant.CMD_BUFSIZE = 8200;
        ConStant.CMD_DATA_BUF_SIZE = 64;
        ConStant.CMD_TIMEOUT = 1000;
        ConStant.CMD_REQ_FLAG = (byte) 0x88;
        ConStant.CMD_RET_FLAG = -86;
        ConStant.ERRCODE_SUCCESS_1 = 1;
        ConStant.ERRCODE_SUCCESS = 0;
        ConStant.ERRCODE_INDEXERR = -2;
        ConStant.ERRCODE_TIMEOUT = -3;
        ConStant.ERRCODE_CANCEL = -4;
        ConStant.ERRCODE_DEVBUSY = -5;
        ConStant.ERRCODE_UPIMAGE = -6;
        ConStant.ERRCODE_DEVLIST = -7;
        ConStant.ERRCODE_IOSEND = -8;
        ConStant.ERRCODE_IORECV = -9;
        ConStant.ERRCODE_HANDLE_NULL = -10;
        ConStant.ERRCODE_CRC = -11;
        ConStant.ERRCODE_MEMORY_OVER = -12;
        ConStant.ERRCODE_ID_CARD_FIND = -13;
        ConStant.ERRCODE_ID_CARD_READ = -14;
        ConStant.ERRCODE_ANTENNA_ON = -15;
        ConStant.ERRCODE_TCPIOSEND = -16;
        ConStant.ERRCODE_TCPIORECV = -17;
        ConStant.ERRCODE_TCPSERVER_INI = -18;
        ConStant.ERRCODE_TCP_STATE = -19;
        ConStant.ERRCODE_SAVE_IDBMP = -20;
        ConStant.ERRCODE_READ_ID_MODULE = -21;
        ConStant.ERRCODE_RESET_ID = -22;
        ConStant.ERRCODE_ID_MSG = -23;
        ConStant.ERRCODE_ANTENNA_OFF = -24;
        ConStant.ERRCODE_NODEVICE = -33;

        ConStant.ERRCODE_TRANS=0x8102;
        ConStant.ERRCODE_READCARD=0x8E02;
        ConStant.ERRCODE_DEVICE=0x8101;
        ConStant. ERRORCODE_NOCARD=0x8D01;
        ConStant.ERRORCODE_CMD=0x8203;
        ConStant.ERRORCODE_APDU=0x8D02;
        ConStant. ERRORCODE_SAMID=0x8E01;

    }
}

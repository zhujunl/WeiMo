package com.miaxis.mr230m;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.miaxis.http.bean.RequestActiveInfo;
import com.miaxis.http.bean.RequestOnlineAuth;
import com.miaxis.http.bean.ResponseActiveInfo;
import com.miaxis.http.net.MyRetrofit;

import org.zz.Mr990SwitchStateProxy;
import org.zz.bean.IDCardRecord;
import org.zz.bean.IdCardParser;
import org.zz.idcard_hid_driver.ConStant;
import org.zz.idcard_hid_driver.IdCardDriver;
import org.zz.idcard_hid_driver.MXDataCode;
import org.zz.idcard_hid_driver.zzStringTrans;
import org.zz.mr990Driver;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Response;

import static org.zz.idcard_hid_driver.ConStant.DATA_BUFFER_SIZE;
import static org.zz.idcard_hid_driver.ConStant.ERRCODE_SUCCESS;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    IdCardDriver idCardDriver;
    private static final int FINGER_DATA_SIZE = 512;
    private boolean openFlag=false;
    private SerialPortHelper mSerialPortHelper;
    private boolean isUsb=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.activity_main);
        TextView tittle=findViewById(R.id.text_title);
        tittle.setText(BuildConfig.VERSION_NAME);
    }

    /**
     * 提示信息
     */
    private void ShowMessage(final String strMsg, Boolean bAdd) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText edit_show_msg = (EditText) findViewById(R.id.edit_show_msg);
                String str=strMsg;
                if (!TextUtils.isEmpty(strMsg))
                    Log.e(TAG, strMsg );
                if (bAdd) {
                    String strShowMsg = edit_show_msg.getText().toString();
                    str = strShowMsg + str;
                }
                edit_show_msg.setText(str + "\r\n");
            }
        });
//        ScrollView scrollView_show_msg = (ScrollView) findViewById(R.id.scrollView_show_msg);
//        scrollToBottom(scrollView_show_msg, edit_show_msg);
    }

    public static void scrollToBottom(final View scroll, final View inner) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }


    /**
     * usb
     * */
    public void OnClickUsbConnect(View view){
        if (isUsb){
            idCardDriver=new IdCardDriver(this);
            UsbConnect();
        }else {
            mSerialPortHelper=new SerialPortHelper();
            SerialConnect();
        }
    }

    public void OnClickUsbDisconnect(View view){
        if (isUsb){
            UsbDisconnect();
        }else {
            SerialDisconnect();
        }
    }

    public void OnClickCardFullInfo(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbReadIDCardMsg();
        }else {
            SerialReadIDCardMsg();
        }

    }

    /**
     * 中国
     * */
    public IDCardRecord IdCardParser(byte[] bCardInfo){
        IDCardRecord idCardRecord = new IDCardRecord();
        idCardRecord.setName(IdCardParser.getName(bCardInfo));
        if(IdCardParser.getGender(bCardInfo).equals("1")){
            idCardRecord.setSex("男");
        }else {
            idCardRecord.setSex("女");
        }
        idCardRecord.setNation(TextUtils.isEmpty(IdCardParser.getNation(bCardInfo).trim())?"":IdCardParser.FOLK[Integer.parseInt(IdCardParser.getNation(bCardInfo))-1]);
        idCardRecord.setBirthday(IdCardParser.getBirthday(bCardInfo));
        idCardRecord.setAddress(IdCardParser.getAddress(bCardInfo));
        idCardRecord.setCardNumber(IdCardParser.getCardNum(bCardInfo));
        idCardRecord.setIssuingAuthority(IdCardParser.getIssuingAuthority(bCardInfo));
        idCardRecord.setValidateStart(IdCardParser.getStartTime(bCardInfo));
        idCardRecord.setValidateEnd(IdCardParser.getEndTime(bCardInfo));
        idCardRecord.setPassNumber(IdCardParser.getPassNum(bCardInfo));
        idCardRecord.setIssueCount(IdCardParser.getIssueNum(bCardInfo));
        idCardRecord.setCardType(getCardType(bCardInfo));
        idCardRecord.setCardBitmap(IdCardParser.getFaceBit(bCardInfo));
        return idCardRecord;
    }

    /**
     * 外国人
     * */
    public IDCardRecord IdGreenCardOarser(byte[] bCardInfo ){
        IDCardRecord idCardRecord = new IDCardRecord();
        idCardRecord.setName(IdCardParser.getEnglishName(bCardInfo));
        if(IdCardParser.getEnglishGender(bCardInfo).equals("1")){
            idCardRecord.setSex("男");
        }else {
            idCardRecord.setSex("女");
        }
        idCardRecord.setCardNumber(IdCardParser.getCardNum(bCardInfo));
        idCardRecord.setNation(IdCardParser.getNationality(bCardInfo));
        idCardRecord.setChineseName(IdCardParser.getChineseName(bCardInfo));
        idCardRecord.setValidateStart(IdCardParser.getStartTime(bCardInfo));
        idCardRecord.setValidateEnd(IdCardParser.getEndTime(bCardInfo));
        idCardRecord.setBirthday(IdCardParser.getEnglishBir(bCardInfo));
        idCardRecord.setVersion(IdCardParser.getVersion(bCardInfo));
        idCardRecord.setIssuingAuthority(IdCardParser.getAcceptMatter(bCardInfo));
        idCardRecord.setCardType(IdCardParser.getCardType(bCardInfo));
        idCardRecord.setCardBitmap(IdCardParser.getFaceBit(bCardInfo));
        return idCardRecord;
    }

    private String getCardType(byte[] idCardData){
        if(TextUtils.isEmpty(IdCardParser.getCardType(idCardData))){
            if(TextUtils.isEmpty(IdCardParser.getPassNum(idCardData))&&!TextUtils.isEmpty(IdCardParser.getNation(idCardData))){
                return "";
            }else {
                return "J";
            }
        }else {
            return IdCardParser.getCardType(idCardData);
        }
    }

    public void OnClickDevVersion(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbGetVersion();
        }else {
            SerialGetVersion();
        }
    }

    public void OnClickSAMId(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbGetSAMID();
        }else {
            SerialGetSAMID();
        }
    }

    public void OnClickBoot(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbFirmwareUpdate();
        }else {
            SerialFirmwareUpdate();
        }

    }

    public void OnClickUsbgetAtr(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbGetAtr();
        }else {
            SerialGetAtr();
        }

    }

    public void OnClickSN(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbGetBoardSN();
        }else {
            SerialGetBoardSN();
        }
    }

    public void OnClickChipSN(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbGetChipSN();
        }else {
            SerialGetChipSN();
        }
    }

    public void OnClickAPDU(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbTransceive();
        }else {
            SerialTransceive();
        }
    }

    public void OnClickSamCommand(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbSamCommand();
        }else {
            SerialSamCommand();
        }

    }

    public void OnClickSamCardCommand(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        if (isUsb){
            UsbSamCardCommand();
        }else {
            SerialSamCardCommand();
        }
    }

    public void OnClickAu(View view){
        if (isUsb){
            OnlineAuth();
        }else {
            OnlineAuthSerial();
        }
    }


    /**
     * USB
     * */

    void UsbConnect(){
        int connect = idCardDriver.connect();
        if (connect== ERRCODE_SUCCESS) {
            openFlag=true;
            ShowMessage("读卡器连接成功", false);
        }
        else {
            openFlag=false;
            ShowMessage("读卡器连接失败，错误码："+connect, false);
        }
    }

    void UsbDisconnect(){
        int disconnect = idCardDriver.disconnect();
        if (disconnect== ERRCODE_SUCCESS){
            openFlag=false;
            ShowMessage("读卡器断开成功", false);
        }else {

        }
    }

    void UsbGetAtr(){
    
        byte[] nRet = idCardDriver.getAtr();
        if (nRet!=null){
            ShowMessage("获取卡片成功，"+new String(nRet), false);
        }else {
            ShowMessage("获取卡片失败,", false);
        }
    }

    void UsbGetVersion(){

        StringBuffer ver=new StringBuffer();
        int nRet = idCardDriver.getVersion(ver);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取读卡器固件版本成功，版本号"+ver, false);
        }else {
            ShowMessage("获取读卡器固件版本失败，错误码："+nRet, false);
        }
    }

    void UsbGetBoardSN(){

        StringBuffer sn=new StringBuffer();
        int nRet = idCardDriver.getBoardSN(sn);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取获取读卡器序列号成功，SN:"+sn, false);
        }else {
            ShowMessage("获取获取读卡器序列号失败，错误码："+nRet, false);
        }
    }

    void UsbGetChipSN(){

        byte[] snbuf=new byte[DATA_BUFFER_SIZE];
        int nRet = idCardDriver.getChipSN(snbuf);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取获取芯片序列号成功，SchipSN:"+zzStringTrans.byteToStr(snbuf), false);
        }else {
            ShowMessage("获取获取芯片序列号失败，错误码："+nRet, false);
        }
    }

    void UsbGetSAMID(){

        byte[] samid=new byte[256];
        int nRet=idCardDriver.getSAMID(samid);
        String strSAMID=idCardDriver.SAMIDToNum(samid);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取SAMID成功，SAMID:"+strSAMID, false);
        }else {
            ShowMessage("获取SAMID失败，错误码："+nRet, false);
        }
    }

    void UsbReadIDCardMsg(){
        ReadCard();
//        byte[] bCardFullInfo = new byte[256 + 1024 + 1024];
//        byte[] baseinf=new byte[256];
//        byte[] photo=new byte[1024];
//        byte[] fpimg=new byte[1024];
//        int[] basesize = { 0 };
//        int[] photosize = { 0 };
//        int[] fpsize = { 0 };
//        IDCardRecord idCardRecord=new IDCardRecord();
//        String type = null;
//        try {
//            int re = idCardDriver.readIDCardMsg(baseinf, basesize,photo,photosize,fpimg,fpsize);
//            if (re == 1 || re == 0) {
//                ImageView img=findViewById(R.id.image_idcard);
//                Bitmap faceBit = IdCardParser.getBitmap(photo);
//                img.setImageBitmap(faceBit);
//                idCardRecord.setCardBitmap(faceBit);
//                if (re == 0) {
//                    byte[] bFingerData0 = new byte[FINGER_DATA_SIZE];
//                    byte[] bFingerData1 = new byte[FINGER_DATA_SIZE];
//                    int iLen = 256 + 1024;
//                    try {
//                        System.arraycopy(bCardFullInfo, iLen, bFingerData0, 0, bFingerData0.length);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    iLen += 512;
//                    try {
//                        System.arraycopy(bCardFullInfo, iLen, bFingerData1, 0, bFingerData1.length);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    try {
//                        idCardRecord.setFingerprintPosition0(fingerPositionCovert(bFingerData0[5]));
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    try {
//                        idCardRecord.setFingerprint0(Base64.encodeToString(bFingerData0, Base64.NO_WRAP));
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    try {
//                        idCardRecord.setFingerprintPosition1(fingerPositionCovert(bFingerData1[5]));
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    try {
//                        idCardRecord.setFingerprint1(Base64.encodeToString(bFingerData1, Base64.NO_WRAP));
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//                ShowMessage("读卡成功",false);
//            }else {
//                ShowMessage("读卡失败，错误码"+ re + (re== ConStant.ERRORCODE_NOCARD ?"  无卡":""),false);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    void UsbSamCommand(){

        try {
            EditText edit=findViewById(R.id.edit_SAM);
            String trim = edit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ShowMessage("指令未输入",false);
                return;
            }
            //        byte[] cmd=trim.getBytes();
            byte[] cmd= MXDataCode.shortToByteArray(Short.parseShort(trim));
            byte[] bytes = idCardDriver.samCommand(cmd);

            ShowMessage("SAM透传指令："+zzStringTrans.hex2str(cmd), false);
            ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(bytes), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
        }
    }

    void UsbSamCardCommand(){

        try {
            EditText edit=findViewById(R.id.edit_SAM_CARD);
            String trim = edit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ShowMessage("指令未输入",false);
                return;
            }
            byte[] cmd= MXDataCode.shortToByteArray(Short.parseShort(trim));
            byte[] bytes = idCardDriver.samCardCommand(cmd);

            ShowMessage("SAM+身份证透传指令："+zzStringTrans.hex2str(cmd), false);
            ShowMessage("SAM+身份证透传指令返回："+zzStringTrans.hex2str(bytes), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
        }
    }

    void UsbTransceive(){

        try {
            EditText edit=findViewById(R.id.edit_APDU);
            String trim = edit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ShowMessage("指令未输入",false);
                return;
            }
            byte[] apducmd=trim.getBytes();
            //            byte[] apducmd=new byte[]{0x00, (byte) 0x84,0x00,0x00,0x08};
            byte[] transceiveBuffer = idCardDriver.transceive(apducmd);
            if (transceiveBuffer==null){
                ShowMessage("APDU指令传输失败，错误码：  "+ConStant.ERRORCODE_APDU,false);
                return;
            }
            ShowMessage("APDU指令数据："+zzStringTrans.hex2str(apducmd), false);
            ShowMessage("APDU返回："+zzStringTrans.hex2str(transceiveBuffer), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码：  "+ConStant.ERRORCODE_APDU,false);
        }
    }

    void UsbFirmwareUpdate(){

        int nRet = idCardDriver.firmwareUpdate();
        if (nRet== ERRCODE_SUCCESS){
            ShowMessage("切换BOOT成功", false);
        }else {
            ShowMessage("切换BOOT失败,错误码:"+nRet, false);
        }
    }

    /**
    *USB----在线授权
    * */
    public void OnlineAuth(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] cmd=MXDataCode.shortToByteArray(IdCardDriver.CMD_APPLY_AUTHORIZATION);
                    byte[] bytes = idCardDriver.samCommand(cmd);
                    String author=AnalysisTran(bytes);
                    Log.e(TAG, "author==" +author );
                    RequestOnlineAuth.Data data = new RequestOnlineAuth.Data();
                    data.setAuthreq(author.trim().replace("\n",""));
                    RequestOnlineAuth requestOnlineAuth=new RequestOnlineAuth("" ,"zjzz", data);
                    Log.e(TAG, "requestOnlineAuth:" +requestOnlineAuth.toString() );
                    Response<ResponseActiveInfo> execute = MyRetrofit.getApiService("http://192.168.6.78:8080")
                            .RequestOnlineAuth(requestOnlineAuth).execute();
                    if (execute.code()==200&&execute.body().getRet().equals("1")){
                        Log.e(TAG, "ex==" + execute.body().toString());
                        String authresp = execute.body().getData().getAuthresp();
                        byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
                        byte[] aut=MXDataCode.shortToByteArray(IdCardDriver.CMD_AUTHORIZATION);
                        Log.e(TAG, "authresp_base:" + zzStringTrans.hex2str(authresp_base));
                        byte[] realBytes = idCardDriver.samCommand(aut,authresp_base);
                        ShowMessage("授权结果："+(realBytes[9]==-112?"成功":"失败"), false);
//                        ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(realBytes), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
                }
            }
        }).start();
    }

    /**
     * 获取签名证书
     * */
    public String  getSign(){

        byte[] check= MXDataCode.shortToByteArray(IdCardDriver.CMD_CHECK);
        byte[] check_bytes = idCardDriver.samCommand(check);
        if (check_bytes[10]==0x01){
            byte[] sign=MXDataCode.shortToByteArray(IdCardDriver.CMD_SIGN);
            byte[] sign_byte=idCardDriver.samCommand(sign);
            return  AnalysisTran(sign_byte);
        }else{

        }
        return null;
    }

    public void AnalysisCard(byte[] baseinf,String sign){
        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    byte[] pcSAMID=new byte[22];
                    byte[] pucRin=new byte[16];
                    byte[] pucT=new byte[32];
                    byte[] pucShortcode=new byte[16];
                    byte[] pucChkDataLenH=new byte[1];
                    byte[] pucChkDatalenL=new byte[1];
                    byte[] pucSign=new byte[64];
                    byte[] pucP=new byte[1024];
                    byte[] pucF=new byte[1024];
                    System.arraycopy(baseinf, 11, pcSAMID, 0, pcSAMID.length);
                    Log.e(TAG, "pcSAMID:" + zzStringTrans.hex2str(pcSAMID));
                    System.arraycopy(baseinf, 33, pucRin, 0, pucRin.length);
                    Log.e(TAG, "pucRin:" + zzStringTrans.hex2str(pucRin));
                    System.arraycopy(baseinf, 49, pucT, 0, pucT.length);
                    Log.e(TAG, "pucT:" + zzStringTrans.hex2str(pucT));
                    System.arraycopy(baseinf, 81, pucShortcode, 0, pucShortcode.length);
                    Log.e(TAG, "pucShortcode:" + zzStringTrans.hex2str(pucShortcode));
                    System.arraycopy(baseinf, 97, pucChkDataLenH, 0, pucChkDataLenH.length);
                    Log.e(TAG, "pucChkDataLenH:" + zzStringTrans.hex2str(pucChkDataLenH));
                    System.arraycopy(baseinf, 98, pucChkDatalenL, 0, pucChkDatalenL.length);
                    Log.e(TAG, "pucChkDatalenL:" + zzStringTrans.hex2str(pucChkDatalenL));
                    int a = pucChkDataLenH[0];
                    int b=pucChkDatalenL[0];
                    if (a<0){
                        a+=256;
                    }
                    if (b<0){
                        b+=256;
                    }
                    byte[] pucChkData=new byte[a*256+b];
                    Log.e(TAG, "pucChkData.length:" +pucChkData.length );
                    System.arraycopy(baseinf, 99, pucChkData, 0, pucChkData.length);
                    Log.e(TAG, "pucChkData:" + zzStringTrans.hex2str(pucChkData));
                    System.arraycopy(baseinf, 99+32+pucChkData.length, pucSign, 0, pucSign.length);
                    Log.e(TAG, "pucSign:" + zzStringTrans.hex2str(pucSign));
                    if (baseinf.length>(99+32+pucChkData.length+pucSign.length+4)) {
                        System.arraycopy(baseinf, 99 + 32 + pucChkData.length + pucSign.length + 4, pucP, 0, pucP.length);
                        Log.e(TAG, "pucP:" + zzStringTrans.hex2str(pucP));
                    }
                    if (baseinf.length>(99+32+pucChkData.length+pucSign.length+4+1024)) {
                        System.arraycopy(baseinf, 99 + 32 + pucChkData.length + pucSign.length + 4 + 1024, pucF, 0, pucF.length);
                        Log.e(TAG, "pucF:" + zzStringTrans.hex2str(pucF));
                    }


//                    byte[] framebytes=new byte[pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length+pucChkDataLenH.length+pucChkDatalenL.length+pucSign.length+pucChkData.length];
//                    System.arraycopy(pcSAMID,0,framebytes,0,pcSAMID.length);
//                    System.arraycopy(pucRin,0,framebytes,pcSAMID.length,pucRin.length);
//                    System.arraycopy(pucT,0,framebytes,pcSAMID.length+pucRin.length,pucT.length);
//                    System.arraycopy(pucShortcode,0,framebytes,pcSAMID.length+pucRin.length+pucT.length,pucShortcode.length);
//                    framebytes[pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length]=(byte)(pucChkData.length >> 8 & 0xFF);
//                    framebytes[pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length+1]=(byte)(pucChkData.length & 0xFF);
//                    System.arraycopy(pucChkData,0,framebytes,pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length+2,pucChkData.length);
//                    System.arraycopy(pucSign,0,framebytes,pcSAMID.length+pucRin.length+pucT.length+pucChkData.length+pucShortcode.length+2,pucSign.length);
//                    Log.e(TAG, "framebytes:" + zzStringTrans.hex2str(framebytes));
//
//                    String framedata = jdkBase64Encode(framebytes);

//                    RequestReportIDInfo.Data data=new RequestReportIDInfo.Data(framedata.trim().replace("\n",""),sign.trim().replace("\n",""));
//                    RequestReportIDInfo requestReportIDInfo = new RequestReportIDInfo("","zjzz",data);
//                    Log.e(TAG, "requestReportIDInfo:" +requestReportIDInfo.toString() );
//                    Response<ResponseActiveInfo> execute = null;
//                    try {
//                        execute = MyRetrofit.getApiService("http://192.168.6.78:8080").RequestIDInfo(requestReportIDInfo).execute();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Log.e(TAG, "execute:" +execute.toString() );
//                }
//            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 激活
     * */
    public void ActiveInfo(){
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  Response<ResponseActiveInfo> execute = MyRetrofit.getApiService("http://192.168.6.78:8080").RequestActiveInfo(new RequestActiveInfo("", "11")).execute();
                  Log.e(TAG, "execute.toString():" + execute.toString());
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }).start();
    }

    /**
     * 联网读卡
    * */
    public void ReadCard(){
        idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_CHECK));//0XA10C
        idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_FindCARD_CONTROL));//0X2001
        idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_SELECTCARD_CONTROL));//0X2002
        byte[] bytes = idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_INTERNT_READCARD_FACEFINGER),IdCardDriver.BSENDBUF);//0X3020
//        String sign = getSign();
        AnalysisCard(bytes,null);
        //        idCardDriver.readIDCardMsg(baseinf, basesize,photo,photosize,fpimg,fpsize);
    }

    public String AnalysisTran(byte[] tran){
        Log.e(TAG, "tran:" + zzStringTrans.hex2str(tran));
        short len=(short)(256 * tran[5] + tran[6]);
        byte[] out=new byte[len-4];
        Log.e(TAG, "len====" +len );
        System.arraycopy(tran,10,out,0,out.length);
        Log.e(TAG, "out===" +zzStringTrans.hex2str(out) );
        return jdkBase64Encode(out);
    }


    private static String jdkBase64Encode(byte[] bytes) {
        String result = Base64.encodeToString(bytes,Base64.DEFAULT);
        return result;
    }

    private static byte[] jdkBase64Decode(byte[] bytes) {
        return Base64.decode(bytes,Base64.DEFAULT);
    }


    /**
     * SrtialPort
     * */


    public void OnClickPort(View view){
        int i = mr990Driver.zzIDFPControl(Mr990SwitchStateProxy.OPEN);//mr990上电
        Log.e(TAG, "i====" +i );


    }

    void SerialConnect(){
        openFlag=true;
    }

    void SerialDisconnect(){
        openFlag=false;
        mSerialPortHelper.close();
    }

    void SerialGetAtr(){
        byte[] nRet = mSerialPortHelper.getAtr();
        if (nRet!=null){
            Log.e(TAG, "nRet:" + zzStringTrans.hex2str(nRet));
            ShowMessage("获取卡片成功，"+new String(nRet), false);
        }else {
            ShowMessage("获取卡片失败,", false);
        }
    }

    void SerialGetVersion(){
        StringBuffer ver=new StringBuffer();
       int nRet= mSerialPortHelper.getVersion(ver);
        Log.e(TAG, "ver:" +ver.toString());
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取读卡器固件版本成功，版本号"+ver, false);
        }else {
            ShowMessage("获取读卡器固件版本失败，错误码："+nRet, false);
        }

    }

    void SerialGetBoardSN(){
        StringBuffer sn=new StringBuffer();
        int nRet = mSerialPortHelper.getBoardSN(sn);
        Log.e(TAG, "ver:" +sn.toString());
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取获取读卡器序列号成功，SN:"+sn, false);
        }else {
            ShowMessage("获取获取读卡器序列号失败，错误码："+nRet, false);
        }

    }

    void SerialGetChipSN(){
        byte[] snbuf=new byte[64];
        int nRet=mSerialPortHelper.getChipSN(snbuf);
        Log.e(TAG, "ver:" +zzStringTrans.hex2str(snbuf));
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取获取芯片序列号成功，SchipSN:"+zzStringTrans.byteToStr(snbuf), false);
        }else {
            ShowMessage("获取获取芯片序列号失败，错误码："+nRet, false);
        }

    }

    void SerialGetSAMID(){
        byte[] samid=new byte[64];
        int nRet=mSerialPortHelper.getSAMID(samid);
        if (nRet== ERRCODE_SUCCESS) {
            String strSAMID=mSerialPortHelper.SAMIDToNum(samid);
            ShowMessage("获取SAMID成功，SAMID:"+strSAMID, false);
        }else {
            ShowMessage("获取SAMID失败，错误码："+nRet, false);
        }
    }

    void SerialReadIDCardMsg(){
//        mSerialPortHelper.readIDCardMsg();
        SReadCard();
    }

    void SerialSamCommand(){
        try {
            EditText edit=findViewById(R.id.edit_SAM);
            String trim = edit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ShowMessage("指令未输入",false);
                return;
            }
            byte[] cmd= MXDataCode.shortToByteArray(Short.parseShort(trim));
            byte[] bytes = mSerialPortHelper.samCommand(cmd);

            ShowMessage("SAM透传指令："+zzStringTrans.hex2str(cmd), false);
            ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(bytes), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
        }
    }

    void SerialSamCardCommand(){
        try {
            EditText edit=findViewById(R.id.edit_SAM_CARD);
            String trim = edit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ShowMessage("指令未输入",false);
                return;
            }
            byte[] cmd= MXDataCode.shortToByteArray(Short.parseShort(trim));
            byte[] bytes = mSerialPortHelper.samCardCommand(cmd);

            ShowMessage("SAM+身份证透传指令："+zzStringTrans.hex2str(cmd), false);
            ShowMessage("SAM+身份证透传指令返回："+zzStringTrans.hex2str(bytes), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
        }
    }

    void SerialTransceive(){
        try {
            EditText edit=findViewById(R.id.edit_APDU);
            String trim = edit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ShowMessage("指令未输入",false);
                return;
            }
            byte[] apducmd=trim.getBytes();
            //            byte[] apducmd=new byte[]{0x00, (byte) 0x84,0x00,0x00,0x08};
            byte[] transceiveBuffer = mSerialPortHelper.transceive(apducmd);
            if (transceiveBuffer==null){
                ShowMessage("APDU指令传输失败，错误码：  "+ConStant.ERRORCODE_APDU,false);
                return;
            }
            ShowMessage("APDU指令数据："+zzStringTrans.hex2str(apducmd), false);
            ShowMessage("APDU返回："+zzStringTrans.hex2str(transceiveBuffer), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码：  "+ConStant.ERRORCODE_APDU,false);
        }
    }

    void SerialFirmwareUpdate(){
        int nRet = mSerialPortHelper.firmwareUpdate();
        if (nRet== ERRCODE_SUCCESS){
            ShowMessage("切换BOOT成功", false);
        }else {
            ShowMessage("切换BOOT失败,错误码:"+nRet, false);
        }
    }

    /**
     * 联网读卡
     * */
    public void SReadCard(){
        mSerialPortHelper.samCommand(MXDataCode.shortToByteArray(mSerialPortHelper.W_SAMID));//0XA10C
        mSerialPortHelper.samCommand(MXDataCode.shortToByteArray(mSerialPortHelper.W_FindCARD_CONTROL));//0X2001
        mSerialPortHelper.samCommand(MXDataCode.shortToByteArray(mSerialPortHelper.W_SELECTCARD_CONTROL));//0X2002
        byte[] bytes = mSerialPortHelper.samCommand(MXDataCode.shortToByteArray(mSerialPortHelper.W_INTERNET_READ),mSerialPortHelper.BSENDBUF);//0X3020
        //        String sign = getSign();
        AnalysisCard(bytes,null);
        //        idCardDriver.readIDCardMsg(baseinf, basesize,photo,photosize,fpimg,fpsize);
    }


    /**
     *SERIAL----在线授权
     * */
    public void OnlineAuthSerial(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] cmd=MXDataCode.shortToByteArray(IdCardDriver.CMD_APPLY_AUTHORIZATION);
                    byte[] bytes = mSerialPortHelper.samCommand(cmd);
                    String author=AnalysisTranSerial(bytes);
                    RequestOnlineAuth.Data data = new RequestOnlineAuth.Data();
                    data.setAuthreq(author.trim().replace("\n",""));
                    RequestOnlineAuth requestOnlineAuth=new RequestOnlineAuth("" ,"zjzz", data);
                    Response<ResponseActiveInfo> execute = MyRetrofit.getApiService("http://192.168.6.78:8080")
                            .RequestOnlineAuth(requestOnlineAuth).execute();
                    if (execute.code()==200&&execute.body().getRet().equals("1")){
                        Log.e(TAG, "ex==" + execute.body().toString());
                        String authresp = execute.body().getData().getAuthresp();
                        byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
                        byte[] aut=MXDataCode.shortToByteArray(IdCardDriver.CMD_AUTHORIZATION);
                        byte[] realBytes = mSerialPortHelper.samCommand(aut,authresp_base);
                        ShowMessage("授权结果："+(realBytes[9]==-112?"成功":"失败"), false);
//                        ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(realBytes), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
                }
            }
        }).start();
    }


    public String AnalysisTranSerial(byte[] tran){
        Log.e(TAG, "tran:" + zzStringTrans.hex2str(tran));
        int len=getLen(tran[4],tran[5]);
        byte[] out=new byte[len-3];
        System.arraycopy(tran,10,out,0,out.length);
        return jdkBase64Encode(out);
    }

    public int getLen(byte h,byte l){
        int len=0;
        int a=h;
        int b=l;
        if(a<0){
            a+=256;
        }
        if ((b < 0)) {
            b+=256;
        }
        len=a*256+b;
        return len;
    }

}

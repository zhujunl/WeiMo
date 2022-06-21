package com.miaxis.mr230m;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaxis.http.bean.RequestActiveInfo;
import com.miaxis.http.bean.RequestOnlineAuth;
import com.miaxis.http.bean.RequestReportIDInfo;
import com.miaxis.http.bean.ResponseActiveInfo;
import com.miaxis.http.net.MyRetrofit;

import org.zz.bean.IDCardRecord;
import org.zz.bean.IdCardParser;
import org.zz.idcard_hid_driver.ConStant;
import org.zz.idcard_hid_driver.IdCardDriver;
import org.zz.idcard_hid_driver.MXDataCode;
import org.zz.idcard_hid_driver.zzStringTrans;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Response;

import static org.zz.bean.IdCardParser.fingerPositionCovert;
import static org.zz.idcard_hid_driver.ConStant.DATA_BUFFER_SIZE;
import static org.zz.idcard_hid_driver.ConStant.ERRCODE_SUCCESS;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    IdCardDriver idCardDriver;
    private static final int FINGER_DATA_SIZE = 512;
    private boolean openFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.activity_main);
        TextView tittle=findViewById(R.id.text_title);
        tittle.setText(BuildConfig.VERSION_NAME);

        idCardDriver=new IdCardDriver(this);
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
    
    public void OnClickUsbConnect(View view){
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

    public void OnClickUsbDisconnect(View view){
        int disconnect = idCardDriver.disconnect();
        if (disconnect== ERRCODE_SUCCESS){
            openFlag=false;
            ShowMessage("读卡器断开成功", false);
        }else {

        }
    }

    public void OnClickCardFullInfo(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        byte[] bCardFullInfo = new byte[256 + 1024 + 1024];
        byte[] baseinf=new byte[256];
        byte[] photo=new byte[1024];
        byte[] fpimg=new byte[1024];
        int[] basesize = { 0 };
        int[] photosize = { 0 };
        int[] fpsize = { 0 };
        IDCardRecord idCardRecord=new IDCardRecord();
        String type = null;
        try {
            int re = idCardDriver.readIDCardMsg(baseinf, basesize,photo,photosize,fpimg,fpsize);
            if (re == 1 || re == 0) {
                ImageView img=findViewById(R.id.image_idcard);
                Bitmap faceBit = IdCardParser.getBitmap(photo);
                img.setImageBitmap(faceBit);
                idCardRecord.setCardBitmap(faceBit);
                if (re == 0) {
                    byte[] bFingerData0 = new byte[FINGER_DATA_SIZE];
                    byte[] bFingerData1 = new byte[FINGER_DATA_SIZE];
                    int iLen = 256 + 1024;
                    try {
                        System.arraycopy(bCardFullInfo, iLen, bFingerData0, 0, bFingerData0.length);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    iLen += 512;
                    try {
                        System.arraycopy(bCardFullInfo, iLen, bFingerData1, 0, bFingerData1.length);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        idCardRecord.setFingerprintPosition0(fingerPositionCovert(bFingerData0[5]));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        idCardRecord.setFingerprint0(Base64.encodeToString(bFingerData0, Base64.NO_WRAP));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        idCardRecord.setFingerprintPosition1(fingerPositionCovert(bFingerData1[5]));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        idCardRecord.setFingerprint1(Base64.encodeToString(bFingerData1, Base64.NO_WRAP));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                ShowMessage("读卡成功",false);
            }else {
                ShowMessage("读卡失败，错误码"+ re + (re== ConStant.ERRORCODE_NOCARD ?"  无卡":""),false);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        StringBuffer ver=new StringBuffer();
        int nRet = idCardDriver.getVersion(ver);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取读卡器固件版本成功，版本号"+ver, false);
        }else {
            ShowMessage("获取读卡器固件版本失败，错误码："+nRet, false);
        }
    }

    public void OnClickSAMId(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        byte[] samid=new byte[256];
        int nRet=idCardDriver.getSAMID(samid);
        String strSAMID=idCardDriver.SAMIDToNum(samid);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取SAMID成功，SAMID:"+strSAMID, false);
        }else {
            ShowMessage("获取SAMID失败，错误码："+nRet, false);
        }
    }

    public void OnClickBoot(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        int nRet = idCardDriver.firmwareUpdate();
        if (nRet== ERRCODE_SUCCESS){
            ShowMessage("切换BOOT成功", false);
        }else {
            ShowMessage("切换BOOT失败,错误码:"+nRet, false);
        }

    }

    public void OnClickUsbgetAtr(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        byte[] Atr = new byte[64];
        byte[] nRet = idCardDriver.getAtr(Atr);
        if (nRet!=null){
            ShowMessage("获取卡片成功，"+new String(nRet), false);
        }else {
            ShowMessage("获取卡片失败,", false);
        }

    }

    public void OnClickSN(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        StringBuffer sn=new StringBuffer();
        int nRet = idCardDriver.getBoardSN(sn);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取获取读卡器序列号成功，SN:"+sn, false);
        }else {
            ShowMessage("获取获取读卡器序列号失败，错误码："+nRet, false);
        }
    }

    public void OnClickChipSN(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        byte[] snbuf=new byte[DATA_BUFFER_SIZE];
        int nRet = idCardDriver.getChipSN(snbuf);
        if (nRet== ERRCODE_SUCCESS) {
            ShowMessage("获取获取芯片序列号成功，SchipSN:"+zzStringTrans.byteToStr(snbuf), false);
        }else {
            ShowMessage("获取获取芯片序列号失败，错误码："+nRet, false);
        }
    }

    public void OnClickAPDU(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
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

    public void OnClickSamCommand(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
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

    public void OnClickSamCardCommand(View view){
//        if (!openFlag){
//            ShowMessage("读卡器未连接",false);
//            return;
//        }
//        try {
//            EditText edit=findViewById(R.id.edit_SAM_CARD);
//            String trim = edit.getText().toString().trim();
//            if (TextUtils.isEmpty(trim)){
//                ShowMessage("指令未输入",false);
//                return;
//            }
//            byte[] cmd= MXDataCode.shortToByteArray(Short.parseShort(trim));
//            byte[] bytes = idCardDriver.samCardCommand(cmd);
//
//            ShowMessage("SAM+身份证透传指令："+zzStringTrans.hex2str(cmd), false);
//            ShowMessage("SAM+身份证透传指令返回："+zzStringTrans.hex2str(bytes), true);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
//        }
        try {
            ReadCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnClickAu(View view){
        OnlineAuth();
    }

    /**
    *在线授权
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
                        byte[] realBytes = idCardDriver.samCommand(aut,authresp_base);
                        ShowMessage("授权结果："+(realBytes[9]==-112?"成功":"失败"), false);
                        ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(realBytes), true);
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

    public void AnalysisCard(byte[] baseinf,int[] basesize,String sign){
       new Thread(new Runnable() {
           @Override
           public void run() {
//               byte[] pcSAMID=new byte[22];
//               byte[] pucRin=new byte[16];
//               byte[] pucT=new byte[32];
//               //        byte[] pucShortcode=new byte[];
//               byte[] pucChkDataLenH=new byte[1];
//               byte[] pucChkDatalenL=new byte[1];
//               byte[] pucChkData=new byte[112];
//               byte[] pucSign=new byte[64];
//               System.arraycopy(baseinf,0,pcSAMID,0,pcSAMID.length);
//               System.arraycopy(baseinf,pcSAMID.length,pucRin,0,pucRin.length);
//               System.arraycopy(baseinf,pcSAMID.length+pucRin.length,pucT,0,pucT.length);
//               pucChkDataLenH[0]=(byte)(pucChkData.length >> 8 & 0xFF);
//               pucChkDatalenL[0]=(byte)(pucChkData.length & 0xFF);
//               System.arraycopy(baseinf,pcSAMID.length+pucRin.length+pucT.length,pucChkData,0,pucChkData.length);
//               System.arraycopy(baseinf,pcSAMID.length+pucRin.length+pucT.length+32+pucChkData.length,pucSign,0,pucSign.length);

               byte[] pcSAMID=new byte[]{0x30, 0x35, 0x32, 0x33, 0x32, 0x30, 0x31, 0x39, 0x30, 0x39, 0x32, 0x36, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x32, 0x36, 0x34, 0x34};
               byte[] pucRin=new byte[]{0x32, 0x30, 0x32, 0x30, 0x32, 0x30, 0x32, 0x30, 0x32, 0x30, 0x32, 0x30, 0x32, 0x30, 0x32, 0x30};
               byte[] pucT=new byte[]{32, 0x00, 0x30, 0x00, 0x30, 0x00, 0x36, 0x00, 0x30, 0x00, 0x32, 0x00, 0x31, 0x00, 0x39, 0x00, 0x32, 0x00, 0x30, 0x00, 0x32, 0x00, 0x36, 0x00, 0x30, 0x00, 0x32, 0x00, 0x31, 0x00, 0x39, 0x00};
               byte[] pucShortcode=new byte[]{
//                       0x32,0x30 ,0x31 ,0x35 ,0x35 ,0x35 ,0x33 ,0x35 ,0x21 , (byte) 0xAD, (byte) 0x96,0x3D , (byte) 0xF5, (byte) 0x97,0x13 ,0x57
                       0x32,0x30 ,0x31 ,0x36 ,0x30 ,0x31 ,0x32 ,0x37 ,0x70 ,0x0C , (byte) 0xD2,0x3C , (byte) 0x82,0x71 , (byte) 0xB5,0x0C
               };
               byte[] pucChkDataLenH=new byte[]{0x00};
               byte[] pucChkDatalenL=new byte[]{0x70};
               byte[] pucChkData=new byte[]{
                       0x01, 0x01, 0x01, 0x01, 0x62, 0x34, (byte) 0xe9, 0x29, 0x42, 0x5b,
                       (byte) 0xbc, 0x39, (byte) 0xac, (byte) 0xbe, 0x33, 0x5e, (byte) 0x88,(byte)  0xe0, (byte) 0xa9, (byte) 0x9b,
                       (byte) 0xb4, (byte) 0x9f,(byte)  0xab,(byte)  0xa1, (byte) 0xa4, 0x57, (byte) 0x9e, 0x4e, (byte) 0xac,(byte)  0xcc,
                       (byte) 0xf4, (byte) 0xf7, 0x75, (byte) 0xfc, 0x1d, 0x55, 0x00, (byte) 0xcf,(byte)  0xa8, (byte) 0xf7,
                       0x14, 0x26, 0x50, (byte) 0xc2, 0x10, (byte) 0xf8, 0x24, 0x6a, (byte) 0xb8, 0x2a,
                       (byte) 0xa8, 0x09, (byte) 0x98, (byte) 0x9d, (byte) 0xe5, (byte) 0xd6,(byte)  0xa5, (byte) 0xf9, 0x69, 0x6c,
                       0x0a, 0x5c, (byte) 0x96, 0x01, (byte) 0x94,(byte)  0x85, (byte) 0x87, (byte) 0xf8, 0x00, (byte) 0xd7,
                       (byte) 0xdc, (byte) 0xe4, 0x08, 0x11, (byte) 0xb9, 0x73, (byte) 0xf3, 0x58, 0x2f, 0x63,
                       0x37, 0x23, 0x7d, 0x22, 0x6c, 0x00,(byte)  0x94, (byte) 0xdb, 0x73, 0x56,
                       (byte) 0xe4, 0x31, 0x0d, 0x73, (byte) 0x9f, (byte) 0x89,(byte)  0xfe, 0x55, 0x73, 0x6b,
                       (byte) 0x8d, (byte) 0xa0, 0x6d, (byte) 0xaa, (byte) 0xe1, (byte) 0x9d, (byte) 0xb0, 0x35, 0x56, 0x48,
                       0x35, (byte) 0xcf
               };
               byte[] pucSign=new byte[]{
                      0x06, (byte)0xc4, 0x3e, (byte)0x97, 0x43, (byte)0x88, (byte)0xdb, 0x47, 0x70, (byte)0xc8, 0x6e,
                      0x72,(byte) 0xb3, 0x6d, (byte)0xc8, 0x56,(byte) 0x9d, 0x79, (byte)0xb7, 0x7f, 0x52,(byte) 0x9d,
                       (byte)0xbf, (byte)0xb2, 0x57, (byte)0xbd, (byte)0x92, 0x24, (byte)0xb2,(byte) 0x84, 0x64, 0x68,(byte) 0x14,
                       (byte)0xc4,(byte) 0xde, 0x21, (byte)0xa5,(byte) 0xe1, 0x15, (byte)0xa0, (byte)0xb3,(byte) 0xef, 0x6d, (byte)0x87,
                       0x02,(byte) 0xac,(byte) 0xe4, (byte)0xcb,(byte) 0x9b,(byte) 0xbc, 0x6f, (byte)0x8f, 0x64, 0x09,(byte) 0xfb,
                       (byte)0x92, (byte)0xa5,(byte) 0x90, (byte)0x91,(byte) 0xed,(byte) 0xcf, 0x6b, 0x52, 0x7d
               };




               Log.e(TAG, "pcSAMID:" +zzStringTrans.hex2str(pcSAMID) );
               Log.e(TAG, "pucRin:" +zzStringTrans.hex2str(pucRin) );
               Log.e(TAG, "pucT:" +zzStringTrans.hex2str(pucT) );
               Log.e(TAG, "pucChkDataLenH:" +zzStringTrans.hex2str(pucChkDataLenH) );
               Log.e(TAG, "pucChkDatalenL:" +zzStringTrans.hex2str(pucChkDatalenL) );
               Log.e(TAG, "pucChkData:" +zzStringTrans.hex2str(pucChkData) );
               Log.e(TAG, "pucSign:" +zzStringTrans.hex2str(pucSign) );
               byte[] framebytes=new byte[248+pucShortcode.length];
               System.arraycopy(pcSAMID,0,framebytes,0,pcSAMID.length);
               System.arraycopy(pucRin,0,framebytes,pcSAMID.length,pucRin.length);
               System.arraycopy(pucT,0,framebytes,pcSAMID.length+pucRin.length,pucT.length);
               System.arraycopy(pucShortcode,0,framebytes,pcSAMID.length+pucRin.length+pucT.length,pucShortcode.length);
               framebytes[pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length]=(byte)(pucChkData.length >> 8 & 0xFF);
               framebytes[pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length+1]=(byte)(pucChkData.length & 0xFF);
               System.arraycopy(pucChkData,0,framebytes,pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length+2,pucChkData.length);
               System.arraycopy(pucSign,0,framebytes,pcSAMID.length+pucRin.length+pucT.length+pucChkData.length+pucShortcode.length+2,pucSign.length);
               Log.e(TAG, "framebytes:" + zzStringTrans.hex2str(framebytes));

               String framedata = jdkBase64Encode(framebytes);

               RequestReportIDInfo.Data data=new RequestReportIDInfo.Data(framedata.trim().replace("\n",""),sign.trim().replace("\n",""));
               RequestReportIDInfo requestReportIDInfo = new RequestReportIDInfo("","zjzz",data);
               Log.e(TAG, "requestReportIDInfo:" +requestReportIDInfo.toString() );
               try {
                   Response<ResponseActiveInfo> execute = MyRetrofit.getApiService("http://192.168.6.78:8080").RequestIDInfo(requestReportIDInfo).execute();
                   Log.e(TAG, "execute:" +execute.toString() );
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }).start();
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

    public void ReadCard(){
        idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_CHECK));//0XA10C
        idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_FindCARD_CONTROL));//0X2001
        idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_SELECTCARD_CONTROL));//0X2002
        idCardDriver.samCommand(MXDataCode.shortToByteArray(IdCardDriver.CMD_INTERNT_READCARD));//0X3020
//        idCardDriver.readIDCardMsg(baseinf, basesize,photo,photosize,fpimg,fpsize);
    }

    public String AnalysisTran(byte[] tran){
        Log.e(TAG, "tran:" + zzStringTrans.hex2str(tran));
        short len=(short)(256 * tran[5] + tran[6]);
        byte[] out=new byte[len-4];
        System.arraycopy(tran,10,out,0,out.length);
        return jdkBase64Encode(out);
    }


    private static String jdkBase64Encode(byte[] bytes) {
        String result = Base64.encodeToString(bytes,Base64.DEFAULT);
        return result;
    }

    private static byte[] jdkBase64Decode(byte[] bytes) {
        return Base64.decode(bytes,Base64.DEFAULT);
    }

}

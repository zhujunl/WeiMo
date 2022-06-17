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

import org.zz.bean.IDCardRecord;
import org.zz.bean.IdCardParser;
import org.zz.idcard_hid_driver.ConStant;
import org.zz.idcard_hid_driver.IdCardDriver;
import org.zz.idcard_hid_driver.zzStringTrans;

import androidx.appcompat.app.AppCompatActivity;

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
    private void ShowMessage(String strMsg, Boolean bAdd) {
        EditText edit_show_msg = (EditText) findViewById(R.id.edit_show_msg);
        if (!TextUtils.isEmpty(strMsg))
            Log.e(TAG, strMsg );
        if (bAdd) {
            String strShowMsg = edit_show_msg.getText().toString();
            strMsg = strShowMsg + strMsg;
        }
        edit_show_msg.setText(strMsg + "\r\n");
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
        }else if(connect==-1000){
            idCardDriver.connect();
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
            ShowMessage("寻卡成功，有卡", false);
        }else {
            ShowMessage("寻卡失败，无卡", false);
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
            byte[] transceiveBuffer = idCardDriver.transceive(apducmd);
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
            byte[] cmd=intToByte(Integer.parseInt(trim));
            byte[] bytes = idCardDriver.samCommand(cmd);

            ShowMessage("SAM透传指令："+zzStringTrans.hex2str(cmd), false);
            ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(bytes), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
        }

    }

    public void OnClickSamCardCommand(View view){
        if (!openFlag){
            ShowMessage("读卡器未连接",false);
            return;
        }
        try {
            EditText edit=findViewById(R.id.edit_SAM_CARD);
            String trim = edit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ShowMessage("指令未输入",false);
                return;
            }
            byte[] cmd=intToByte(Integer.parseInt(trim));
            byte[] bytes = idCardDriver.samCardCommand(cmd);

            ShowMessage("SAM+身份证透传指令："+zzStringTrans.hex2str(cmd), false);
            ShowMessage("SAM+身份证透传指令返回："+zzStringTrans.hex2str(bytes), true);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
        }
    }

    public void restartUsb(){
        idCardDriver.disconnect();
        int connect = idCardDriver.connect();
        if (connect==-1000){
            idCardDriver.connect();
        }
    }

    public byte[] intToByte(int n){
        byte[] b=new byte[2];
        for (int i=0;i<b.length;i++){
            b[i]= (byte) (n>>(8-i*8));
        }
        return b;
    }

}

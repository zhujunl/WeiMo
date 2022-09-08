package com.miaxis.mr230m.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.serialport.MXDataCode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.miaxis.mr230m.event.SingleLiveEvent;
import com.miaxis.mr230m.http.bean.BusInfo;
import com.miaxis.mr230m.http.bean.DataJkm;
import com.miaxis.mr230m.http.bean.RequestJKM;
import com.miaxis.mr230m.http.bean.RequestReportIDInfo;
import com.miaxis.mr230m.http.bean.ResponseJKM;
import com.miaxis.mr230m.http.bean.ResponseReportIDInfo;
import com.miaxis.mr230m.http.net.MyCallback;
import com.miaxis.mr230m.http.net.MyRetrofit;
import com.miaxis.mr230m.miaxishttp.bean.TokenResonse;
import com.miaxis.mr230m.miaxishttp.bean.WRequestActiveinfo;
import com.miaxis.mr230m.miaxishttp.bean.WRequestDeactiveInfo;
import com.miaxis.mr230m.miaxishttp.bean.WRequestIdInfo;
import com.miaxis.mr230m.miaxishttp.bean.WRequestOnlineauthInfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseActiveinfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseDeactiveInfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseIdInfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseOnlineauthInfo;
import com.miaxis.mr230m.miaxishttp.net.MiaxisRetrofit;
import com.miaxis.mr230m.model.Result;
import com.miaxis.mr230m.util.mkUtil;
import com.zzreader.ConStant;
import com.zzreader.ZzReader;
import com.zzreader.zzStringTrans;

import org.zz.bean.IDCardRecord;
import org.zz.bean.IdCardParser;

import java.io.IOException;

import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zzreader.ConStant.ERRCODE_SUCCESS;
import static org.zz.bean.IdCardParser.fingerPositionCovert;

/**
 * @author ZJL
 * @date 2022/9/6 14:35
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DemoViewModel extends ViewModel {

    public SingleLiveEvent<Result> resultLiveData=new SingleLiveEvent<>();
    public SingleLiveEvent<IDCardRecord> IDCardLiveData=new SingleLiveEvent<>();


    ZzReader idCardDriver;
    boolean openFlag=false;
    final int FINGER_DATA_SIZE=512;
    String TAG="DemoViewModel";

    private void ShowMessage(String msg, boolean b) {
        Result result=new Result(msg,b);
        resultLiveData.postValue(result);
    }

    /**
     * USB
     * */

    public void UsbConnect(Context context){
        if (idCardDriver==null){
            idCardDriver=new ZzReader(context);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int connect = idCardDriver.connectReaderZ("USB");
                if (connect== ConStant.ERRCODE_SUCCESS) {
                    openFlag=true;
                    ShowMessage("读卡器连接成功", false);
                } else {
                    openFlag=false;
                    ShowMessage("读卡器连接失败，错误码："+connect, false);
                }
            }
        }).start();
    }

    public void UsbDisconnect(){
        if (openFlag){
            int disconnect = idCardDriver.disconnectZ ();
            if (disconnect== ConStant.ERRCODE_SUCCESS){
                openFlag=false;
                ShowMessage("读卡器断开成功", false);
            }else {

            }
        }
    }

    public void UsbGetAtr(){
        byte[] nRet = idCardDriver.getAtrZ();
        if (nRet!=null){
            ShowMessage("获取卡片成功，"+ zzStringTrans.hex2str(nRet), false);
        }else {
            ShowMessage("获取卡片失败", false);
        }
    }

    public void UsbGetVersion(){
        StringBuffer ver=new StringBuffer();
        int nRet = idCardDriver.getVersionZ(ver);
        if (nRet== ConStant.ERRCODE_SUCCESS) {
            ShowMessage("获取读卡器固件版本成功，版本号"+ver, false);
        }else {
            ShowMessage("获取读卡器固件版本失败，错误码："+nRet, false);
        }
    }

    public void UsbGetBoardSN(){

        StringBuffer sn=new StringBuffer();
        int nRet = idCardDriver.getBoardSNZ(sn);
        if (nRet== ConStant.ERRCODE_SUCCESS) {
            ShowMessage("获取获取读卡器序列号成功，SN:"+sn, false);
        }else {
            ShowMessage("获取获取读卡器序列号失败，错误码："+nRet, false);
        }
    }

    public void UsbGetChipSN(){

        byte[] snbuf=new byte[ConStant.DATA_BUFFER_SIZE];
        int nRet = idCardDriver.getChipSNZ(snbuf);
        if (nRet== ConStant.ERRCODE_SUCCESS) {
            ShowMessage("获取获取芯片序列号成功，SchipSN:"+ zzStringTrans.byteToStr(snbuf), false);
        }else {
            ShowMessage("获取获取芯片序列号失败，错误码："+nRet, false);
        }
    }

    public String  UsbGetSAMID(){
        byte[] samid=new byte[256];
        int nRet=idCardDriver.getSAMIDZ(samid);
        String strSAMID =idCardDriver.SAMIDToNum(samid);
        if (nRet==ConStant.ERRCODE_SUCCESS) {
//            ShowMessage("获取SAMID成功，SAMID:"+strSAMID, false);
            return strSAMID;
        }else {
//            ShowMessage("获取SAMID失败，错误码："+nRet, false);
            return null;
        }
    }

    public void UsbReadIDCardMsgVerify(){
        new Thread(() -> {
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
                int re = idCardDriver.readIDCardMsgZ(baseinf, basesize,photo,photosize,fpimg,fpsize);
                if (re == 1 || re == 0) {
                    byte[] start=new byte[16];
                    byte[] end=new byte[16];
                    System.arraycopy(baseinf,0,start,0,start.length);
                    System.arraycopy(baseinf,16,end,0,end.length);
                    idCardRecord.setValidateStart(IdCardParser.unicode2String(start));
                    Log.e(TAG, "start=" + zzStringTrans.hex2str(start));
                    idCardRecord.setValidateEnd(IdCardParser.unicode2String(end));
                    Log.e(TAG, "end=" + zzStringTrans.hex2str(end));
                    Bitmap faceBit = IdCardParser.getBitmap(photo);
                    idCardRecord.setCardBitmap(faceBit);
                    if (re == 0) {
                        byte[] bFingerData0 = new byte[FINGER_DATA_SIZE];
                        byte[] bFingerData1 = new byte[FINGER_DATA_SIZE];
                        try {
                            System.arraycopy(fpimg, 0, bFingerData0, 0, bFingerData0.length);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try {
                            System.arraycopy(fpimg, 512, bFingerData1, 0, bFingerData1.length);
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
                    IDCardLiveData.postValue(idCardRecord);
                    ShowMessage("读卡成功",false);
                }else {
                    ShowMessage("读卡失败，错误码"+ re + (re== ConStant.ERRORCODE_NOCARD ?"  无卡":""),false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("读卡失败,"+e.getMessage(),false);
            }
        }).start();
    }

    public void UsbReadIDCardMsg(String token,String ip){
        new Thread(()->{
            try {
                String samid=UsbGetSAMID();
                if (samid==null){
                    ShowMessage("解除激活失败,没有SAMID", false);
                    return;
                }
                String cid=mkUtil.getInstance().decodeString("cid","95d0047549ef4300b6448fa18e87b268");
                String mdeviceid=mkUtil.getInstance().decodeString("mdeviceid",null);
                if (mdeviceid==null){
                    ShowMessage("解除激活失败,未激活", false);
                    return;
                }
                byte[] photo=new byte[1024];
                byte[] fpimg=new byte[1024];
                byte[] start=new byte[16];
                byte[] end=new byte[16];
                byte[] jkm= idCardDriver.readIDCardMsgZ(photo,fpimg);
                Bitmap faceBit = IdCardParser.getBitmap(photo);
                IDCardRecord idCardRecord=new IDCardRecord();
                System.arraycopy(jkm,38,start,0,start.length);
                System.arraycopy(jkm,54,end,0,end.length);
                idCardRecord.setValidateStart(IdCardParser.unicode2String(start));
                idCardRecord.setValidateEnd(IdCardParser.unicode2String(end));
                idCardRecord.setCardBitmap(faceBit);
                byte[] bFingerData0 = new byte[FINGER_DATA_SIZE];
                byte[] bFingerData1 = new byte[FINGER_DATA_SIZE];
                System.arraycopy(fpimg, 0, bFingerData0, 0, bFingerData0.length);
                System.arraycopy(fpimg, 512, bFingerData1, 0, bFingerData1.length);
                idCardRecord.setFingerprintPosition0(fingerPositionCovert(bFingerData0[5]));
                idCardRecord.setFingerprint0(Base64.encodeToString(bFingerData0, Base64.NO_WRAP));
                idCardRecord.setFingerprintPosition1(fingerPositionCovert(bFingerData1[5]));
                idCardRecord.setFingerprint1(Base64.encodeToString(bFingerData1, Base64.NO_WRAP));

                IDCardLiveData.postValue(idCardRecord);
                String framedata = jdkBase64Encode(jkm);
                String samsigncert=getSign();
                Log.e(TAG, "wRequestIdInfo  samsigncert==" + samsigncert);
                WRequestIdInfo wRequestIdInfo=new WRequestIdInfo(cid,mdeviceid,samid,encodeBusiness(cid,samid,"idinfo"),framedata,samsigncert);
                Log.e(TAG, "wRequestIdInfo==" + wRequestIdInfo);
                Response<WResponseIdInfo> execute = MiaxisRetrofit.getApiService(token, ip).IdInfo(wRequestIdInfo).execute();
                Log.e(TAG, "execute=" + execute.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void UsbFirmwareUpdate(){

        int nRet = idCardDriver.firmwareUpdateZ();
        if (nRet== ERRCODE_SUCCESS){
            ShowMessage("切换BOOT成功", false);
        }else {
            ShowMessage("切换BOOT失败,错误码:"+nRet, false);
        }
    }

    public void UsbLowPower(){
        idCardDriver.lowPower();
    }

    /**
     *USB----在线授权
     * */
    public void OnlineAuth(String token,String ip){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    byte[] cmd= MXDataCode.shortToByteArray(ZzReader.CMD_APPLY_AUTHORIZATION);
//                    byte[] bytes = idCardDriver.samCommandZ(cmd);
//                    String author=AnalysisTran(bytes).trim().replace("\n","");
//                    RequestOnlineAuth.Data data = new RequestOnlineAuth.Data();
//                    data.setAuthreq(author);
//                    RequestOnlineAuth requestOnlineAuth=new RequestOnlineAuth("" ,"zjzz", data);
//                    Log.e(TAG, "requestOnlineAuth:" +requestOnlineAuth.toString() );
//                    Response<ResponseOnlineAuth> execute = MyRetrofit.getApiService(ip)
//                            .RequestOnlineAuth(requestOnlineAuth).execute();
//                    if (execute.code()==200&&execute.body().getRet().equals("1")){
//                        Log.e(TAG, "ex==" + execute.body().toString());
//                        String authresp = execute.body().getData().getAuthresp();
//                        byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
//                        byte[] aut= MXDataCode.shortToByteArray(ZzReader.CMD_AUTHORIZATION);
//                        Log.e(TAG, "authresp_base:" + zzStringTrans.hex2str(authresp_base));
//                        byte[] realBytes = idCardDriver.samCommandZ(aut,authresp_base);
//                        ShowMessage("授权结果："+(realBytes[9]==-112?"成功":"失败"), false);
//                        //                        ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(realBytes), true);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ShowMessage(e.getMessage()+"   错误码： "+ConStant.ERRORCODE_CMD,false);
//                }
//            }
//        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String samid=UsbGetSAMID();
                    if (samid==null){
                        ShowMessage("解除激活失败,没有SAMID", false);
                        return;
                    }
                    String mdeviceid=mkUtil.getInstance().decodeString("mdeviceid",null);
                    if (mdeviceid==null){
                        ShowMessage("解除激活失败,未激活", false);
                        return;
                    }
                    String cid=mkUtil.getInstance().decodeString("cid","95d0047549ef4300b6448fa18e87b268");
                    byte[] cmd= MXDataCode.shortToByteArray(ZzReader.CMD_APPLY_AUTHORIZATION);
                    byte[] bytes = idCardDriver.samCommandZ(cmd);
                    String author=AnalysisTran(bytes).trim().replace("\n","");
                    WRequestOnlineauthInfo wRequestOnlineauthInfo=new WRequestOnlineauthInfo(cid,mdeviceid,samid,encodeBusiness(cid,samid,"onlineauthinfo"),author);
                    Response<WResponseOnlineauthInfo> execute = MiaxisRetrofit.getApiService(token, ip).OnlineauthInfo(wRequestOnlineauthInfo).execute();
                    String authresp = execute.body().getData().getAuthresp();
                    byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
                    byte[] aut= MXDataCode.shortToByteArray(ZzReader.CMD_AUTHORIZATION);
                    byte[] realBytes = idCardDriver.samCommandZ(aut,authresp_base);
                    ShowMessage("授权结果："+(realBytes[9]==-112?"成功":"失败"), false);
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
        byte[] check= MXDataCode.shortToByteArray(ZzReader.CMD_CHECK);
        byte[] check_bytes = idCardDriver.samCommandZ(check);
        if (check_bytes==null){
            ShowMessage("未激活", false);
            return "";
        }
        if (check_bytes[10]==0x01){
            byte[] sign= MXDataCode.shortToByteArray(ZzReader.CMD_SIGN);
            byte[] sign_byte=idCardDriver.samCommandZ(sign);
            return  AnalysisTran(sign_byte);
        }
        return null;
    }

    public void AnalysisCard(byte[] baseinf,String sign,String ip){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] pcSAMID=new byte[22];
                    byte[] pucRin=new byte[16];
                    byte[] pucT=new byte[32];
                    byte[] pucShortcode=new byte[16];
                    byte[] pucChkDataLenH=new byte[1];
                    byte[] pucChkDatalenL=new byte[1];
                    byte[] pucSign=new byte[64];
                    byte[] pucP=new byte[1024];
                    byte[] pucF=new byte[1024];
                    //            System.arraycopy(baseinf, 11, pcSAMID, 0, pcSAMID.length);
                    //            Log.e(TAG, "pcSAMID:" + zzStringTrans.hex2str(pcSAMID));
                    //            System.arraycopy(baseinf, 33, pucRin, 0, pucRin.length);
                    //            Log.e(TAG, "pucRin:" + zzStringTrans.hex2str(pucRin));
                    //            System.arraycopy(baseinf, 49, pucT, 0, pucT.length);
                    //            Log.e(TAG, "pucT:" + zzStringTrans.hex2str(pucT));
                    //            System.arraycopy(baseinf, 81, pucShortcode, 0, pucShortcode.length);
                    //            Log.e(TAG, "pucShortcode:" + zzStringTrans.hex2str(pucShortcode));
                    //            System.arraycopy(baseinf, 97, pucChkDataLenH, 0, pucChkDataLenH.length);
                    //            Log.e(TAG, "pucChkDataLenH:" + zzStringTrans.hex2str(pucChkDataLenH));
                    //            System.arraycopy(baseinf, 98, pucChkDatalenL, 0, pucChkDatalenL.length);
                    //            Log.e(TAG, "pucChkDatalenL:" + zzStringTrans.hex2str(pucChkDatalenL));
                    int a =baseinf[97];
                    int b=baseinf[98];
                    if (a<0){
                        a+=256;
                    }
                    if (b<0){
                        b+=256;
                    }
                    int count =a*256+b;
                    //            byte[] pucChkData=new byte[count];
                    //            Log.e(TAG, "pucChkData.length:" +pucChkData.length );
                    //            System.arraycopy(baseinf, 99, pucChkData, 0, pucChkData.length);
                    //            Log.e(TAG, "pucChkData:" + zzStringTrans.hex2str(pucChkData));
                    //            System.arraycopy(baseinf, 99+32+pucChkData.length, pucSign, 0, pucSign.length);
                    //            Log.e(TAG, "pucSign:" + zzStringTrans.hex2str(pucSign));
                    //            if (baseinf.length>(99+32+pucChkData.length+pucSign.length+4)) {
                    //                System.arraycopy(baseinf, 99 + 32 + pucChkData.length + pucSign.length + 4, pucP, 0, pucP.length);
                    //                Log.e(TAG, "pucP:" + zzStringTrans.hex2str(pucP));
                    //                ImageView img=findViewById(R.id.image_idcard);
                    //                Bitmap faceBit = IdCardParser.getBitmap(pucP);
                    //                img.setImageBitmap(faceBit);
                    //            }
                    //            if (baseinf.length>(99+32+pucChkData.length+pucSign.length+4+1024)) {
                    //                System.arraycopy(baseinf, 99 + 32 + pucChkData.length + pucSign.length + 4 + 1024, pucF, 0, pucF.length);
                    //                Log.e(TAG, "pucF:" + zzStringTrans.hex2str(pucF));
                    //            }


                    byte[] framebytes=new byte[152+count];
                    //                System.arraycopy(pcSAMID,0,framebytes,0,pcSAMID.length);
                    //                System.arraycopy(pucRin,0,framebytes,pcSAMID.length,pucRin.length);
                    //                System.arraycopy(pucT,0,framebytes,pcSAMID.length+pucRin.length,pucT.length);
                    //                System.arraycopy(pucShortcode,0,framebytes,pcSAMID.length+pucRin.length+pucT.length,pucShortcode.length);
                    //                framebytes[pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length]=(byte)(pucChkData.length >> 8 & 0xFF);
                    //                framebytes[pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length+1]=(byte)(pucChkData.length & 0xFF);
                    //                System.arraycopy(pucChkData,0,framebytes,pcSAMID.length+pucRin.length+pucT.length+pucShortcode.length+2,pucChkData.length);
                    //                System.arraycopy(pucSign,0,framebytes,pcSAMID.length+pucRin.length+pucT.length+pucChkData.length+pucShortcode.length+2,pucSign.length);
                    //                Log.e(TAG, "framebytes:" + zzStringTrans.hex2str(framebytes));
                    System.arraycopy(baseinf,11,framebytes,0,88);
                    System.arraycopy(baseinf,99,framebytes,88,count);
                    System.arraycopy(baseinf,99+count,framebytes,88+count,64);

                    String framedata = jdkBase64Encode(framebytes);

                    RequestReportIDInfo.Data data=new RequestReportIDInfo.Data(framedata.trim().replace("\n",""),sign.trim().replace("\n",""));
                    RequestReportIDInfo requestReportIDInfo = new RequestReportIDInfo("","zjzz",data);
                    Log.e(TAG, "requestReportIDInfo:" +requestReportIDInfo.toString() );
                    Response<ResponseReportIDInfo> execute = null;
                    try {
                        execute = MyRetrofit.getApiService(ip).RequestIDInfo(requestReportIDInfo).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "IOException:" +e.getMessage() );
                    }
                    Log.e(TAG, "execute:" +execute.toString() );
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception:" +e.getMessage() );
        }
    }

    public void UsbJkm(String ip){
        new Thread(() -> {
            try {
                byte[] jkm= idCardDriver.mxReadCardForJkm();
                String framedata = jdkBase64Encode(jkm);
                String samsigncert=jdkBase64Encode(getSign().getBytes());
                DataJkm dataJkm=new DataJkm(framedata.trim().replace("\n",""),samsigncert.trim().replace("\n",""));
                BusInfo busInfo=new BusInfo("11","222","3333");
                RequestJKM requestJKM=new RequestJKM("","zjzz","A,1,2",dataJkm,busInfo);
                Log.e(TAG, "requestJKM==" +requestJKM.toString() );
                MyRetrofit.getApiService(ip).RequestJKM(requestJKM).enqueue(new Callback<ResponseJKM>() {
                    @Override
                    public void onResponse(Call<ResponseJKM> call, Response<ResponseJKM> response) {
                        Gson gson=new Gson();
                        String s = gson.toJson(response);
                        Log.e(TAG, "RequestJKM成功:" + s);
                    }

                    @Override
                    public void onFailure(Call<ResponseJKM> call, Throwable t) {
                        Log.e(TAG, "RequestJKM失败：" + t.getMessage());
                    }
                });

                ShowMessage("读卡成功",false);
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("读卡失败,"+e.getMessage(),false);
            }

        }).start();
    }

    /**
     * 激活
     * */
    public void ActiveInfo(String token,String ip){
        new Thread(() -> {
            try {
                String samid=UsbGetSAMID();
                if (samid==null){
                    ShowMessage("解除激活失败,没有SAMID", false);
                    return;
                }
                String cid=mkUtil.getInstance().decodeString("cid","95d0047549ef4300b6448fa18e87b268");
                WRequestActiveinfo entity=new WRequestActiveinfo(cid,samid,encodeBusiness(cid,samid,"activeinfo"));
                Response<WResponseActiveinfo> execute = MiaxisRetrofit.getApiService(token, ip).Activeinfo(entity).execute();
                WResponseActiveinfo body = execute.body();
                if (body.getCode()==200){
                    mkUtil.getInstance().encode("mdeviceid",body.getData().getMdeviceid());
                    String activeinfo = body.getData().getActiveinfo();
                    byte[] bytes = jdkBase64Decode(activeinfo.getBytes());
                    byte[] bytes1 = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_ACTIVEINFO), bytes);
                    ShowMessage("激活成功", false);
                }else {
                    ShowMessage("激活失败,错误码："+body.getCode()+"错误信息:"+body.getMsg(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("激活失败"+e.getMessage(), false);
            }
        }).start();
    }

    public void ActRel(String token,String ip){
        new Thread(() -> {
         try {
             String samid=UsbGetSAMID();
             if (samid==null){
                 ShowMessage("解除激活失败,没有SAMID", false);
                 return;
             }
             String mdeviceid=mkUtil.getInstance().decodeString("mdeviceid",null);
             if (mdeviceid==null){
                 ShowMessage("解除激活失败,未激活", false);
                 return;
             }
             String cid=mkUtil.getInstance().decodeString("cid","95d0047549ef4300b6448fa18e87b268");
             WRequestDeactiveInfo wRequestDeactiveInfo=new WRequestDeactiveInfo(cid,mdeviceid,samid,encodeBusiness(cid,samid,"deactiveinfo"));
             Response<WResponseDeactiveInfo> execute = MiaxisRetrofit.getApiService(token, ip).DeactiveInfo(wRequestDeactiveInfo).execute();
             Log.e(TAG, "execute:" + execute);
         } catch (Exception e) {
             e.printStackTrace();
             ShowMessage("解除激活失败"+e.getMessage(), false);
         }
        }).start();
    }

    /**激活状态*/
    public void ActiveState(){
        new Thread(() -> {
            try {
                byte[] bytes = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_CHECK));
                switch (bytes[10]) {
                    case 2:
                        ShowMessage("解除激活", false);
                        break;
                    case 1:
                        ShowMessage("已经激活", false);
                        break;
                    default:
                        ShowMessage("从未激活", false);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("查询失败"+e.getMessage(), false);
            }
        }).start();
    }

    /**
     * 联网读卡
     * */
    public void ReadCard(){
        idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_CHECK));//0XA10C
        idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_FindCARD_CONTROL));//0X2001
        idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_SELECTCARD_CONTROL));//0X2002
        byte[] bytes = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_INTERNT_READCARD_FACEFINGER), ZzReader.BSENDBUF);//0X3020
    }

    public String AnalysisTran(byte[] tran){
        int a=tran[5];
        int b=tran[6];
        if (a<0){
            a+=256;
        }
        if (b<0){
            b+=256;
        }
        short len=(short)(256 * a + b);
        byte[] out=new byte[len-4];
        System.arraycopy(tran,10,out,0,out.length);
        Log.e(TAG, "tran==" + zzStringTrans.hex2str(tran));
        Log.e(TAG, "out==" + zzStringTrans.hex2str(out));
        return jdkBase64Encode(out);
    }

    public String encodeBusiness(String cid,String samid,String in){
        long time=System.currentTimeMillis();
        String businessSerialNum =cid+samid+in+time;
        return jdkBase64Encode(businessSerialNum.getBytes());
    }

    private static String jdkBase64Encode(byte[] bytes) {
        String result = Base64.encodeToString(bytes,Base64.DEFAULT);
        return result;
    }

    private static byte[] jdkBase64Decode(byte[] bytes) {
        return Base64.decode(bytes,Base64.DEFAULT);
    }

    public void getToken(){
        String ip=mkUtil.getInstance().decodeString("weiIp","");
        String cid=mkUtil.getInstance().decodeString("cid","95d0047549ef4300b6448fa18e87b268");
        if (TextUtils.isEmpty(ip)){
            return;
        }
        MiaxisRetrofit.TokenService(ip).getToken(cid).enqueue(new MyCallback<TokenResonse>() {
            @Override
            public void onSuccess(TokenResonse tokenResonse) {
                if (tokenResonse.getCode()==200){
                    mkUtil.getInstance().encode("token",tokenResonse.getData().getAccess_token());
                    mkUtil.getInstance().encode("tokenTime",tokenResonse.getData().getExpires_in());
                }else {
                    Log.e(TAG, "失败：" +tokenResonse.getMsg() );
                }
            }

            @Override
            public void onFailed(Throwable t) {
                Log.e(TAG, "错误:" +t.getMessage() );
            }
        });
    }
}

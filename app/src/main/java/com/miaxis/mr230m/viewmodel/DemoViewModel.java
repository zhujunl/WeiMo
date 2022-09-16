package com.miaxis.mr230m.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.serialport.MXDataCode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.miaxis.mr230m.App;
import com.miaxis.mr230m.event.SingleLiveEvent;
import com.miaxis.mr230m.http.bean.BusInfo;
import com.miaxis.mr230m.http.bean.DataJkm;
import com.miaxis.mr230m.http.bean.RequestJKM;
import com.miaxis.mr230m.http.bean.ResponseJKM;
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

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Response;

import static org.zz.bean.IdCardParser.fingerPositionCovert;

/**
 * @author ZJL
 * @date 2022/9/6 14:35
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DemoViewModel extends ViewModel {

    public SingleLiveEvent<Result> resultLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<IDCardRecord> IDCardLiveData = new SingleLiveEvent<>();
    public MutableLiveData<Result> ActiveInfoResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnect = new MutableLiveData<>();


    ZzReader idCardDriver;
    boolean openFlag = false;
    final int FINGER_DATA_SIZE = 512;
    String TAG = "DemoViewModel";

    private void ShowMessage(String msg, boolean b) {
        Result result = new Result(msg, b);
        resultLiveData.postValue(result);
    }

    /**
     * USB
     */

    public void UsbConnect(Context context) {
        if (idCardDriver == null) {
            idCardDriver = new ZzReader(context);
        }
        App.getInstance().threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int connect = idCardDriver.connectReaderZ("USB");
                if (connect == ConStant.ERRCODE_SUCCESS) {
                    openFlag = true;
                    ShowMessage("读卡器连接成功", false);
                    isConnect.postValue(true);
                } else {
                    openFlag = false;
                    ShowMessage("读卡器连接失败，错误码：" + connect, false);
                    isConnect.postValue(false);
                }
            }
        });
    }

    public String UsbGetSAMID() {
        byte[] samid = new byte[256];
        int nRet = idCardDriver.getSAMIDZ(samid);
        String strSAMID = idCardDriver.SAMIDToNum(samid);
        if (nRet == ConStant.ERRCODE_SUCCESS) {
            //            ShowMessage("获取SAMID成功，SAMID:"+strSAMID, false);
            return strSAMID;
        } else {
            //            ShowMessage("获取SAMID失败，错误码："+nRet, false);
            return null;
        }
    }

    public void UsbReadIDCardMsgVerify() {
        
        App.getInstance().threadExecutor.execute(() -> {
            byte[] bCardFullInfo = new byte[256 + 1024 + 1024];
            byte[] baseinf = new byte[256];
            byte[] photo = new byte[1024];
            byte[] fpimg = new byte[1024];
            int[] basesize = {0};
            int[] photosize = {0};
            int[] fpsize = {0};
            IDCardRecord idCardRecord = new IDCardRecord();
            String type = null;
            try {
                int re = idCardDriver.readIDCardMsgZ(baseinf, basesize, photo, photosize, fpimg, fpsize);
                if (re == 1 || re == 0) {
                    byte[] start = new byte[16];
                    byte[] end = new byte[16];
                    System.arraycopy(baseinf, 0, start, 0, start.length);
                    System.arraycopy(baseinf, 16, end, 0, end.length);
                    idCardRecord.setValidateStart(IdCardParser.unicode2String(start));
                    idCardRecord.setValidateEnd(IdCardParser.unicode2String(end));
                    Bitmap faceBit = IdCardParser.getBitmap(photo);
                    idCardRecord.setCardBitmap(faceBit);
                    byte[] bFingerData0 = new byte[FINGER_DATA_SIZE];
                    byte[] bFingerData1 = new byte[FINGER_DATA_SIZE];
                    System.arraycopy(fpimg, 0, bFingerData0, 0, bFingerData0.length);
                    System.arraycopy(fpimg, 512, bFingerData1, 0, bFingerData1.length);
                    idCardRecord.setFingerprintPosition0(fingerPositionCovert(bFingerData0[5]));
                    idCardRecord.setFingerprint0(bFingerData0);
                    idCardRecord.setFingerprintPosition1(fingerPositionCovert(bFingerData1[5]));
                    idCardRecord.setFingerprint1(bFingerData1);
                    IDCardLiveData.postValue(idCardRecord);
                    ShowMessage("读卡成功", false);
                } else {
                    ShowMessage("读卡失败，错误码" + re + (re == ConStant.ERRORCODE_NOCARD ? "  无卡" : ""), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("读卡失败," + e.getMessage(), false);
            }
        });
    }

    public void UsbReadIDCardMsg( String ip) {
        App.getInstance().threadExecutor.execute(() -> {
            try {
                String samid = UsbGetSAMID();
                if (samid == null) {
                    ShowMessage("解除激活失败,没有SAMID", false);
                    return;
                }
                String cid = mkUtil.getInstance().decodeString("cid", "226062ee9fba4316ac0357f86de259a3");
                String mdeviceid = mkUtil.getInstance().decodeString("mdeviceid", null);
                if (mdeviceid == null) {
                    ShowMessage("解除激活失败,未激活", false);
                    return;
                }
                byte[] photo = new byte[1024];
                byte[] fpimg = new byte[1024];
                byte[] start = new byte[16];
                byte[] end = new byte[16];
                byte[] jkm = idCardDriver.readIDCardMsgZ(photo, fpimg);
                if (jkm == null) {
                    ShowMessage("读卡失败，请重新读取", false);
                    return;
                }
                Bitmap faceBit = IdCardParser.getBitmap(photo);
                IDCardRecord idCardRecord = new IDCardRecord();
                System.arraycopy(jkm, 38, start, 0, start.length);
                System.arraycopy(jkm, 54, end, 0, end.length);
                idCardRecord.setValidateStart(IdCardParser.unicode2String(start));
                idCardRecord.setValidateEnd(IdCardParser.unicode2String(end));
                idCardRecord.setCardBitmap(faceBit);
                byte[] bFingerData0 = new byte[FINGER_DATA_SIZE];
                byte[] bFingerData1 = new byte[FINGER_DATA_SIZE];
                System.arraycopy(fpimg, 0, bFingerData0, 0, bFingerData0.length);
                System.arraycopy(fpimg, 512, bFingerData1, 0, bFingerData1.length);
                idCardRecord.setFingerprintPosition0(fingerPositionCovert(bFingerData0[5]));
                idCardRecord.setFingerprint0(bFingerData0);
                idCardRecord.setFingerprintPosition1(fingerPositionCovert(bFingerData1[5]));
                idCardRecord.setFingerprint1(bFingerData1);


                String framedata = jdkBase64Encode(jkm);
                String samsigncert = getSign();
                WRequestIdInfo wRequestIdInfo = new WRequestIdInfo(cid, mdeviceid, samid, encodeBusiness(cid, samid, "idinfo"), framedata, samsigncert);
                Response<WResponseIdInfo> execute = MiaxisRetrofit.getApiService( ip).IdInfo(wRequestIdInfo).execute();
                WResponseIdInfo body = execute.body();
                if (body.getCode() == 200) {
                    String idtype = IdCardParser.unicode2String(jdkBase64Decode(body.getData().getIdtype().getBytes()));
                    String name = IdCardParser.unicode2String(jdkBase64Decode(idtype.equals("I") ? body.getData().getEnglishname().getBytes() : body.getData().getChinesename().getBytes()));
                    ;
                    String idnumber = IdCardParser.unicode2String(jdkBase64Decode(body.getData().getIdnumber().getBytes()));
                    idCardRecord.setName(name);
                    idCardRecord.setCardNumber(idnumber);
                    IDCardLiveData.postValue(idCardRecord);

                } else {
                    ShowMessage("读卡请求失败," + body.getMsg(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("读卡失败," + e.getMessage(), false);
            }
        });
    }

    /**
     * USB----在线授权
     */
    public void OnlineAuth( String ip) {
        App.getInstance().threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String samid = UsbGetSAMID();
                    if (samid == null) {
                        ShowMessage("解除激活失败,没有SAMID", false);
                        return;
                    }
                    String mdeviceid = mkUtil.getInstance().decodeString("mdeviceid", null);
                    if (mdeviceid == null) {
                        ShowMessage("解除激活失败,未激活", false);
                        return;
                    }
                    String cid = mkUtil.getInstance().decodeString("cid", "226062ee9fba4316ac0357f86de259a3");
                    byte[] cmd = MXDataCode.shortToByteArray(ZzReader.CMD_APPLY_AUTHORIZATION);
                    byte[] bytes = idCardDriver.samCommandZ(cmd);
                    String author = AnalysisTran(bytes).trim().replace("\n", "");
                    WRequestOnlineauthInfo wRequestOnlineauthInfo = new WRequestOnlineauthInfo(cid, mdeviceid, samid, encodeBusiness(cid, samid, "onlineauthinfo"), author);
                    Response<WResponseOnlineauthInfo> execute = MiaxisRetrofit.getApiService( ip).OnlineauthInfo(wRequestOnlineauthInfo).execute();
                    String authresp = execute.body().getData().getAuthresp();
                    byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
                    byte[] aut = MXDataCode.shortToByteArray(ZzReader.CMD_AUTHORIZATION);
                    byte[] realBytes = idCardDriver.samCommandZ(aut, authresp_base);
                    ShowMessage("授权结果：" + (realBytes[9] == -112 ? "成功" : "失败"), false);
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowMessage(e.getMessage() + "   错误码： " + ConStant.ERRORCODE_CMD, false);
                }
            }
        });
    }

//    public void OnlineAuth(String ip) {
//        App.getInstance().threadExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    byte[] cmd = MXDataCode.shortToByteArray(ZzReader.CMD_APPLY_AUTHORIZATION);
//                    byte[] bytes = idCardDriver.samCommandZ(cmd);
//                    String author = AnalysisTran(bytes);
//                    Log.e(TAG, "author==" + author);
//                    RequestOnlineAuth.Data data = new RequestOnlineAuth.Data();
//                    data.setAuthreq(author.trim().replace("\n", ""));
//                    RequestOnlineAuth requestOnlineAuth = new RequestOnlineAuth("", "zjzz", data);
//                    Log.e(TAG, "requestOnlineAuth:" + requestOnlineAuth.toString());
//                    Response<ResponseOnlineAuth> execute = MyRetrofit.getApiService(ip)
//                            .RequestOnlineAuth(requestOnlineAuth).execute();
//                    if (execute.code() == 200 && execute.body().getRet().equals("1")) {
//                        Log.e(TAG, "ex==" + execute.body().toString());
//                        String authresp = execute.body().getData().getAuthresp();
//                        byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
//                        byte[] aut = MXDataCode.shortToByteArray(ZzReader.CMD_AUTHORIZATION);
//                        Log.e(TAG, "authresp_base:" + zzStringTrans.hex2str(authresp_base));
//                        byte[] realBytes = idCardDriver.samCommandZ(aut, authresp_base);
//                        ShowMessage("授权结果：" + (realBytes[9] == -112 ? "成功" : "失败"), false);
//                        //                        ShowMessage("SAM透传指令返回："+zzStringTrans.hex2str(realBytes), true);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ShowMessage(e.getMessage() + "   错误码： " + ConStant.ERRORCODE_CMD, false);
//                }
//            }
//        });
//    }


    /**
     * 获取签名证书
     */
    public String getSign() {
        byte[] check = MXDataCode.shortToByteArray(ZzReader.CMD_CHECK);
        byte[] check_bytes = idCardDriver.samCommandZ(check);
        if (check_bytes == null) {
            ShowMessage("未激活", false);
            return "";
        }
        if (check_bytes[10] == 0x01) {
            byte[] sign = MXDataCode.shortToByteArray(ZzReader.CMD_SIGN);
            byte[] sign_byte = idCardDriver.samCommandZ(sign);
            Log.e(TAG, "UsbJkm_sign=" + zzStringTrans.hex2str(sign_byte));
            return AnalysisTran(sign_byte);
        }
        return null;
    }

    public void UsbJkm(String ip) {
        App.getInstance().threadExecutor.execute(() -> {
            try {
                byte[] photo = new byte[1024];
                byte[] fpimg = new byte[1024];
                byte[] jkm = idCardDriver.readIDCardMsgZ(photo, fpimg);
                Log.e(TAG, "UsbJkm_jkm=" + zzStringTrans.hex2str(jkm));
                String framedata = jdkBase64Encode(jkm);
                String samsigncert = getSign();
                DataJkm dataJkm = new DataJkm(framedata, samsigncert);
                BusInfo busInfo = new BusInfo("11", "222", "3333");
                RequestJKM requestJKM = new RequestJKM("", "zjzz", "A,1,2", dataJkm, busInfo);
                Log.e(TAG, "requestJKM==" + requestJKM.toString());
                Response<ResponseJKM> execute = MyRetrofit.getApiService(ip).RequestJKM(requestJKM).execute();
                Log.e(TAG, "execute==" + execute);
                ShowMessage("读卡成功", false);
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("读卡失败," + e.getMessage(), false);
            }

        });
    }

    /**
     * 激活
     */
    public void ActiveInfo( String ip) {
        App.getInstance().threadExecutor.execute(() -> {
            try {
                String samid = UsbGetSAMID();
                if (samid == null) {
                    ShowMessage("解除激活失败,没有SAMID", false);
                    return;
                }
                String cid = mkUtil.getInstance().decodeString("cid", "226062ee9fba4316ac0357f86de259a3");
                WRequestActiveinfo entity = new WRequestActiveinfo(cid, samid, encodeBusiness(cid, samid, "activeinfo"));
                Response<WResponseActiveinfo> execute = MiaxisRetrofit.getApiService( ip).Activeinfo(entity).execute();
                WResponseActiveinfo body = execute.body();
                if (body.getCode() == 200) {
                    mkUtil.getInstance().encode("mdeviceid", body.getData().getMdeviceid());
                    String activeinfo = body.getData().getActiveinfo();
                    byte[] bytes = jdkBase64Decode(activeinfo.getBytes());
                    byte[] bytes1 = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_ACTIVEINFO), bytes);
                    if ((int) bytes1[9] == -112) {
                        ShowMessage("激活成功", false);
                    } else {
                        ShowMessage("激活失败" + (int) bytes1[9], false);
                    }

                } else {
                    ShowMessage("激活失败,错误码：" + body.getCode() + "错误信息:" + body.getMsg(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("激活失败" + e.getMessage(), false);
            }
        });
    }

//    public void ActiveInfo(String ip) {
//        App.getInstance().threadExecutor.execute(() -> {
//            try {
//                Response<ResponseActiveInfo> execute = MyRetrofit.getApiService(ip)
//                        .RequestActiveInfo(new RequestActiveInfo("", "11")).execute();
//                if (execute.body().getRet().equals("1")) {
//                    String activeinfo = execute.body().getData().getActiveinfo();
//                    byte[] bytes = jdkBase64Decode(activeinfo.getBytes());
//                    byte[] bytes1 = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_ACTIVEINFO), bytes);
//                    if ((int) bytes1[9] == -112) {
//                        ShowMessage("激活成功", false);
//                    } else {
//                        ShowMessage("激活失败" + (int) bytes1[9], false);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                ShowMessage("激活失败" + e.getMessage(), false);
//            }
//        });
//    }

    public void ActRel( String ip) {
        App.getInstance().threadExecutor.execute(() -> {
            try {
                String samid = UsbGetSAMID();
                if (samid == null) {
                    ShowMessage("解除激活失败,没有SAMID", false);
                    return;
                }
                String mdeviceid = mkUtil.getInstance().decodeString("mdeviceid", null);
                if (mdeviceid == null) {
                    ShowMessage("解除激活失败,未激活", false);
                    return;
                }
                String cid = mkUtil.getInstance().decodeString("cid", "226062ee9fba4316ac0357f86de259a3");
                WRequestDeactiveInfo wRequestDeactiveInfo = new WRequestDeactiveInfo(cid, mdeviceid, samid, encodeBusiness(cid, samid, "deactiveinfo"));
                Response<WResponseDeactiveInfo> execute = MiaxisRetrofit.getApiService( ip).DeactiveInfo(wRequestDeactiveInfo).execute();
                WResponseDeactiveInfo body = execute.body();
                String deactiveinfo = body.getData().getDeactiveinfo();
                byte[] bytes = jdkBase64Decode(deactiveinfo.getBytes());
                byte[] bytes1 = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_ACT_RELIVE), bytes);
                if ((int) bytes1[9] == -112) {
                    ShowMessage("解除激活成功", false);
                } else {
                    ShowMessage("解除激活失败" + (int) bytes1[9], false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage("解除激活失败" + e.getMessage(), false);
            }
        });
    }

//    public void ActRel(String ip) {
//        App.getInstance().threadExecutor.execute(() -> {
//            byte[] samid = new byte[256];
//            int nRet = idCardDriver.getSAMIDZ(samid);
//            String strSAMID = idCardDriver.SAMIDToNum(samid);
//            RequestDeActiveInfo.Data data = new RequestDeActiveInfo.Data();
//            data.setSamid_ascii(strSAMID);
//            if (nRet == ERRCODE_SUCCESS) {
//                RequestDeActiveInfo entity = new RequestDeActiveInfo("", "zjzz", data);
//                try {
//                    Response<ResponseDeActiveInfo> execute = MyRetrofit.getApiService(ip).RequestDeActiveInfo(entity).execute();
//                    if (execute.code() == 200) {
//                        ResponseDeActiveInfo body = execute.body();
//                        String deactiveinfo = body.getData().getDeactiveinfo();
//                        byte[] bytes = jdkBase64Decode(deactiveinfo.getBytes());
//                        byte[] bytes1 = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_ACT_RELIVE), bytes);
//                        if (bytes1 != null && (int) bytes1[9] == -112) {
//                            ShowMessage("解除激活成功", false);
//                        } else {
//                            ShowMessage("解除激活失败" + (int) bytes1[9], false);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    ShowMessage("解除激活失败" + e.getMessage(), false);
//                }
//            } else {
//                ShowMessage("解除激活失败:" + nRet, false);
//            }
//        });
//    }

    /**
     * 激活状态
     */
    public void ActiveState() {
        App.getInstance().threadExecutor.execute(() -> {
            try {
                byte[] bytes = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_CHECK));
                if (bytes == null) {
                    ShowMessage("查询失败", false);
                    return;
                }
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
                ShowMessage("查询失败" + e.getMessage(), false);
            }
        });
    }


    public String AnalysisTran(byte[] tran) {
        int a = tran[5];
        int b = tran[6];
        if (a < 0) {
            a += 256;
        }
        if (b < 0) {
            b += 256;
        }
        short len = (short) (256 * a + b);
        byte[] out = new byte[len - 4];
        System.arraycopy(tran, 10, out, 0, out.length);
        return jdkBase64Encode(out);
    }

    public String encodeBusiness(String cid, String samid, String in) {
        long time = System.currentTimeMillis();
        String businessSerialNum = cid + samid + in + time;
        return jdkBase64Encode(businessSerialNum.getBytes());
    }

    private static String jdkBase64Encode(byte[] bytes) {
        String result = Base64.encodeToString(bytes, Base64.DEFAULT);
        return result.replace("\n", "");
    }

    public static byte[] jdkBase64Decode(byte[] bytes) {
        return Base64.decode(bytes, Base64.DEFAULT);
    }

    public void getToken() {
        try {
            String ip = mkUtil.getInstance().decodeString("weiIp", "https://183.129.171.153:8080");
            String cid = mkUtil.getInstance().decodeString("cid", "226062ee9fba4316ac0357f86de259a3");
            if (TextUtils.isEmpty(ip)) {
                return;
            }
            MiaxisRetrofit.TokenService(ip).getToken(cid).enqueue(new MyCallback<TokenResonse>() {
                @Override
                public void onSuccess(TokenResonse tokenResonse) {
                    if (tokenResonse.getCode() == 200) {
                        mkUtil.getInstance().encode("token", tokenResonse.getData().getAccess_token());
                        mkUtil.getInstance().encode("tokenTime", tokenResonse.getData().getExpires_in());
                    } else {
                        Log.e(TAG, "失败：" + tokenResonse.getMsg());
                    }
                }

                @Override
                public void onFailed(Throwable t) {
                    Log.e(TAG, "错误:" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ActiveInfoAuto(String ip) {
        App.getInstance().threadExecutor.execute(() -> {
            try {
                byte[] CMD_CHECK = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_CHECK));
                if (CMD_CHECK == null) {
                    Log.e(TAG, "ActiveInfoAuto检查激活状态失败");
                    ActiveInfoResult.postValue(new Result("微模查激活状态失败", false));
                    return;
                }
                String samid = UsbGetSAMID();
                String cid = mkUtil.getInstance().decodeString("cid", "226062ee9fba4316ac0357f86de259a3");
                WRequestActiveinfo entity = new WRequestActiveinfo(cid, samid, encodeBusiness(cid, samid, "activeinfo"));
//                String token = mkUtil.getInstance().decodeString("token", "");
                Response<WResponseActiveinfo> execute = MiaxisRetrofit.getApiService( ip).Activeinfo(entity).execute();
                WResponseActiveinfo body = execute.body();
                if (body.getCode() == 200) {
                    String mdeviceid = body.getData().getMdeviceid();
                    mkUtil.getInstance().encode("mdeviceid", mdeviceid);
                    String activeinfo = body.getData().getActiveinfo();
                    byte[] bytes = jdkBase64Decode(activeinfo.getBytes());
                    byte[] bytes1 = idCardDriver.samCommandZ(MXDataCode.shortToByteArray(ZzReader.CMD_ACTIVEINFO), bytes);
                    if ((int) bytes1[9] == -112) {
                        byte[] cmd = MXDataCode.shortToByteArray(ZzReader.CMD_APPLY_AUTHORIZATION);
                        byte[] autnor = idCardDriver.samCommandZ(cmd);
                        String author = AnalysisTran(autnor).trim().replace("\n", "");
                        WRequestOnlineauthInfo wRequestOnlineauthInfo = new WRequestOnlineauthInfo(cid, mdeviceid, samid, encodeBusiness(cid, samid, "onlineauthinfo"), author);
                        Response<WResponseOnlineauthInfo> executeOnlinea = MiaxisRetrofit.getApiService( ip).OnlineauthInfo(wRequestOnlineauthInfo).execute();
                        if (executeOnlinea.body().getCode() == 200) {
                            String authresp = executeOnlinea.body().getData().getAuthresp();
                            byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
                            byte[] aut = MXDataCode.shortToByteArray(ZzReader.CMD_AUTHORIZATION);
                            byte[] realBytes = idCardDriver.samCommandZ(aut, authresp_base);
                            ActiveInfoResult.postValue(new Result(realBytes[9] == -112 ? "微模自动授权成功" : "微模自动授权失败" + String.valueOf((int) realBytes[9]), realBytes[9] == -112));
                        } else {
                            ActiveInfoResult.postValue(new Result("微模自动授权失败," + execute.body().getMsg(), false));
                        }

                    }
                } else {
                    ActiveInfoResult.postValue(new Result("微模自动激活失败," + body.getMsg(), false));
                }
//                if (CMD_CHECK[10] == 1) {
//                    String samid = UsbGetSAMID();
//                    String mdeviceid = mkUtil.getInstance().decodeString("mdeviceid", null);
//                    String cid = mkUtil.getInstance().decodeString("cid", "226062ee9fba4316ac0357f86de259a3");
//                    byte[] cmd = MXDataCode.shortToByteArray(ZzReader.CMD_APPLY_AUTHORIZATION);
//                    byte[] bytes = idCardDriver.samCommandZ(cmd);
//                    String author = AnalysisTran(bytes).trim().replace("\n", "");
//                    WRequestOnlineauthInfo wRequestOnlineauthInfo = new WRequestOnlineauthInfo(cid, mdeviceid, samid, encodeBusiness(cid, samid, "onlineauthinfo"), author);
//                    String token = mkUtil.getInstance().decodeString("token", "");
//                    Response<WResponseOnlineauthInfo> execute = MiaxisRetrofit.getApiService( ip).OnlineauthInfo(wRequestOnlineauthInfo).execute();
//                    if (execute.body().getCode() == 200) {
//                        String authresp = execute.body().getData().getAuthresp();
//                        byte[] authresp_base = jdkBase64Decode(authresp.getBytes());
//                        byte[] aut = MXDataCode.shortToByteArray(ZzReader.CMD_AUTHORIZATION);
//                        byte[] realBytes = idCardDriver.samCommandZ(aut, authresp_base);
//                        ActiveInfoResult.postValue(new Result(realBytes[9] == -112 ? "微模自动授权成功" : "微模自动授权失败" + String.valueOf((int) realBytes[9]), realBytes[9] == -112));
//                    } else {
//                        ActiveInfoResult.postValue(new Result("微模自动授权失败," + execute.body().getMsg(), false));
//                    }
//                } else {
//
//                }
            } catch (Exception e) {
                e.printStackTrace();
                ActiveInfoResult.postValue(new Result("微模自动授权失败," + e.getMessage(), false));
            }
        });

    }
}

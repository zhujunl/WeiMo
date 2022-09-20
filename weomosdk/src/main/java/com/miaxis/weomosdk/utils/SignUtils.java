package com.miaxis.weomosdk.utils;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName: SignUtils
 * @Author: cheng.peng
 * @Date: 2022/5/21 17:43
 */
public class SignUtils {
    private static String TAG=SignUtils.class.getSimpleName();

    /**
     * 生成签名请求头需要的信息
     * @param method 请求方法 POST/GET
     * @param path 请求url，例如：/api/device/auth 不携带？和参数
     * @param appSecret appSecret
     * @param appKey appkey
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Map<String, String> createSignHeaders(String method, String path, String appSecret, String appKey) {
        Long currentTime = System.currentTimeMillis();
        String signature = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            signature = signature(method, path, currentTime, appSecret, appKey);
        }
        Map<String, String> signMap = new HashMap<>();
        //签名
        signMap.put("X-Ca-Signature", signature);
        //时间戳，精确到毫秒
        signMap.put("X-Ca-Timestamp", currentTime.toString());
        return signMap;
    }

    /**
     * HTTP协议加签生成签名
     * @param method 请求方法 POST/GET
     * @param path 请求url，例如：/api/device/auth 不携带？和参数
     * @param currentTime 当前时间戳，精确到毫秒
     * @param appSecret appSecret
     * @param appKey appkey
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String signature(String method, String path, Long currentTime, String appSecret, String appKey) {
        String content =
                method + "\n"
                        + path + "\n"
                        + currentTime + "\n";
        SecretKeySpec secretKeySpec = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getEncoder().encodeToString(bytes);
            return String.format("midplt@@@@%s:%s", appKey, signature);
        } catch (Exception e) {
            Log.d(TAG,"e "+e.toString());
            return null;
        }
    }

    /**
     * TCP协议加签生成签名
     * @param cmd 方法
     * @param currentTime 时间戳，精确到毫秒
     * @param appSecret appSecret
     * @param appKey appkey
     * @return
     */
    public static String tcpSignature(String cmd, String clientId, Long currentTime, String appSecret, String appKey) {
        if (cmd==null||clientId==null||currentTime==null||appSecret==null||appKey==null){
            Log.d(TAG,"======生成签名为空");
            return "";
        }
        String content =
                cmd + "\n"
                        + clientId + "\n"
                        + currentTime + "\n";
        SecretKeySpec secretKeySpec = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
            String signature = Base64Utils.encode(bytes);

            return String.format("midplt@@@@%s:%s", appKey, signature);
        } catch (Exception e) {
            Log.d(TAG,"======TCP加签名报错e:{}"+e);
            return null;
        }
    }
}

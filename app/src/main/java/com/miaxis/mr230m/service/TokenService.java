package com.miaxis.mr230m.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.miaxis.mr230m.http.net.MyCallback;
import com.miaxis.mr230m.miaxishttp.bean.TokenRefreshResonse;
import com.miaxis.mr230m.miaxishttp.net.MiaxisRetrofit;
import com.miaxis.mr230m.util.mkUtil;

import androidx.annotation.Nullable;

public class TokenService extends Service {
    int TIME_INTERVAL = 0; // 这是5s
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    public static final String TEST_ACTION ="REFRESHTOKEN";
    String TAG="TokenService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "开启" );
        IntentFilter intentFilter = new IntentFilter(TEST_ACTION);
        registerReceiver(receiver, intentFilter);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(TEST_ACTION);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        int tokenTime = 8;
        TIME_INTERVAL= tokenTime*60*1000;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0低电量模式需要使用该方法触发定时任务
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4以上 需要使用该方法精确执行时间
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        } else {//4。4一下 使用老方法
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), TIME_INTERVAL, pendingIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TEST_ACTION.equals(action)) {
                if (TIME_INTERVAL>0){
                    Log.d(TAG, "刷新" );
                    refresh();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TIME_INTERVAL, pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TIME_INTERVAL, pendingIntent);
                    }
                }
            }
        }
    };

    private void refresh(){
//        String token = mkUtil.getInstance().decodeString("token", "");
        String ip= mkUtil.getInstance().decodeString("weiIp", "");
        if (TextUtils.isEmpty(ip)){
            return;
        }
        MiaxisRetrofit.getApiService(ip).RefreshToken().enqueue(new MyCallback<TokenRefreshResonse>() {
            @Override
            public void onSuccess(TokenRefreshResonse tokenRefreshResonse) {
                if (tokenRefreshResonse.getCode()==200){
                    Log.d(TAG, "refresh成功:");
                }else {
                    Log.e(TAG, "refresh失败:"+tokenRefreshResonse.getMsg());
                }
            }

            @Override
            public void onFailed(Throwable t) {
                Log.e(TAG, "refresh错误:"+t.getMessage());
            }
        });
    }

}
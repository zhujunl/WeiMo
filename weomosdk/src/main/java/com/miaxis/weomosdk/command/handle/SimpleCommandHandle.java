package com.miaxis.weomosdk.command.handle;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.miaxis.weomosdk.command.CommandParse;


/**
 * @ClassName: SimpleCommandHandle
 * @Author: cheng.peng
 * @Date: 2022/5/26 18:55
 */
public  abstract class SimpleCommandHandle<T>  extends  AbsCommandHandle<T>{

    public abstract  boolean onCustomCallBack();

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void intentHandle(CommandParse skillParse) {
        intentHandle(skillParse.getClientId(),skillParse.getData());
        if(onCustomCallBack()){  //是否要自己处理消息
            return;
        }
        onCommonCallback();
    }

    public abstract void intentHandle(String clientId, String data);


}

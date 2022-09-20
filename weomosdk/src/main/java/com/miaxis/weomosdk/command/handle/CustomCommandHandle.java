package com.miaxis.weomosdk.command.handle;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.miaxis.weomosdk.command.CommandParse;


/**
 * @ClassName: CustomCommandHandle
 * @Author: cheng.peng
 * @Date: 2022/5/23 18:38
 * 业务命令处理
 */
public abstract class CustomCommandHandle extends AbsCommandHandle{

    public abstract  boolean onCustomCallBack();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void intentHandle(CommandParse skillParse) {
        intentHandle(skillParse.getData());
        if(onCustomCallBack()){  //是否要自己处理消息
            return;
        }
        onCommonCallback();
    }

    public abstract void intentHandle( String data);



}

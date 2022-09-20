package com.miaxis.weomosdk.command.handle;


import com.miaxis.weomosdk.command.CommandParse;

/**
 * @ClassName: IIntentHandle
 * @Author: cheng.peng
 * @Date: 2022/5/23 18:07
 */
public interface IIntentHandle<T> {
    void intentHandle(CommandParse skillParse);

    T getData();
}

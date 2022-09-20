package com.miaxis.weomosdk.message.sender;

import com.easysocket.EasySocket;
import com.easysocket.entity.basemsg.SuperCallbackSender;
import com.google.gson.Gson;

import java.nio.ByteBuffer;

/**
 * @ClassName: PictureCallbackSender
 * @Author: cheng.peng
 * @Date: 2022/5/27 16:00
 */
public class PictureCallbackSender extends SuperCallbackSender {

    @Override
    public byte[] pack() {
        byte[] body = (new Gson().toJson(this)+"\n").getBytes();
        // 如果没有设置消息协议，则直接发送消息
        if (EasySocket.getInstance().getDefOptions().getMessageProtocol() == null) {
            return body;
        }
        ByteBuffer bb = ByteBuffer.allocate(body.length + 4);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }
}

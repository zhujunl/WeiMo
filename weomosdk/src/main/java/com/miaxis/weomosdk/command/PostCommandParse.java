package com.miaxis.weomosdk.command;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: PostCommandParse
 * @Author: cheng.peng
 * @Date: 2022/5/23 17:11
 * 云南邮政命令解析类
 */
public class PostCommandParse extends CommandParse {
    private String TAG=PostCommandParse.class.getSimpleName();

    private JSONObject root;
    private String clientId;
    private String command;
    private String deviceChannelNumber;
    private String actionId="";
    private String domainId="";
    private String data;
    private int  value;

    @Override
    public String getKey() {
        return getCommand()+getDomainId()+getActionId();
    }

    @Override
    public String getCommand() {
        if(command!=null) return command;
        if(root==null) return "";
        if (root.has("cmd")) {
            try {
                command = root.getString("cmd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  command;
    }

    @Override
    public String getClientId() {
        if(clientId!=null) return clientId;
        if(root==null) return "";
        if (root.has("clientId")) {
            try {
                clientId = root.getString("clientId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  clientId;
    }


    @Override
    public String getDeviceChannelNumber() {
        if(root==null) return "";
        if(deviceChannelNumber!=null) return deviceChannelNumber;
        if(root.has("data")){
            try {
                JSONObject data = root.getJSONObject("data");
                if(data.has("deviceChannelNumber")){
                    deviceChannelNumber=data.getString("deviceChannelNumber");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return deviceChannelNumber;
    }

    @Override
    public String getActionId() {
        if(!TextUtils.isEmpty(actionId)) return actionId;
     //   if(actionId!=null) return actionId;
        if(root==null) return "";
        if(root.has("data")){
            try {
                JSONObject data = root.getJSONObject("data");
                if(data.has("actionId")){
                    actionId=data.getString("actionId");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return actionId;
    }

    @Override
    public String getDomainId() {
        if(!TextUtils.isEmpty(domainId)) return domainId;
     //   if(domainId!=null) return domainId;
        if(root==null) return "";
        if(root.has("data")){
            try {
                JSONObject data = root.getJSONObject("data");
                if(data.has("domainId")){
                    domainId=data.getString("domainId");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return domainId;
    }

    @Override
    public String getData() {
        if(data!=null) return data;
        if(root==null) return "";
        if(root.has("data")){
            try {
                JSONObject data1 = root.getJSONObject("data");
                if(data1.has("data")){
                    data=data1.getString("data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    @Override
    public int getValue() {
        if(root==null) return value;
        if(root.has("data")){
            try {
                JSONObject data = root.getJSONObject("data");
                if(data.has("value")){
                    value=data.getInt("value");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public void parse(String data) {
        try {
            root = new JSONObject(data);
            if(root.has("cmd")){
                command=root.getString("cmd");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

package com.miaxis.mr230m.model;

/**
 * @author ZJL
 * @date 2022/9/6 16:54
 * @des
 * @updateAuthor
 * @updateDes
 */
public class Result {
   private String msg;
   private boolean flag;
   private long netTime;
   private long deviceTime;

   public Result(String msg, boolean flag, long netTime, long deviceTime) {
      this.msg = msg;
      this.flag = flag;
      this.netTime = netTime;
      this.deviceTime = deviceTime;
   }

   public String getMsg() {
      return msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public boolean isFlag() {
      return flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public long getNetTime() {
      return netTime;
   }

   public void setNetTime(long netTime) {
      this.netTime = netTime;
   }

   public long getDeviceTime() {
      return deviceTime;
   }

   public void setDeviceTime(long deviceTime) {
      this.deviceTime = deviceTime;
   }

   @Override
   public String toString() {
      return "Result{" +
              "msg='" + msg + '\'' +
              ", flag=" + flag +
              ", netTime=" + netTime +
              ", deviceTime=" + deviceTime +
              '}';
   }
}

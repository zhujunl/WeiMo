package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/7/13 9:40
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ResponseDeActiveInfo {
   private String sn;
   private String ret;
   private String desc;
   private Data data;

   public class Data{
      private String deactiveinfo;

      public String getDeactiveinfo() {
         return deactiveinfo;
      }

      public void setDeactiveinfo(String deactiveinfo) {
         this.deactiveinfo = deactiveinfo;
      }

      @Override
      public String toString() {
         return "Data{" +
                 "deactiveinfo='" + deactiveinfo + '\'' +
                 '}';
      }
   }

   public String getSn() {
      return sn;
   }

   public void setSn(String sn) {
      this.sn = sn;
   }

   public String getRet() {
      return ret;
   }

   public void setRet(String ret) {
      this.ret = ret;
   }

   public String getDesc() {
      return desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public Data getData() {
      return data;
   }

   public void setData(Data data) {
      this.data = data;
   }

   @Override
   public String toString() {
      return "ResponseDeActiveInfo{" +
              "sn='" + sn + '\'' +
              ", ret='" + ret + '\'' +
              ", desc='" + desc + '\'' +
              ", data=" + data +
              '}';
   }
}

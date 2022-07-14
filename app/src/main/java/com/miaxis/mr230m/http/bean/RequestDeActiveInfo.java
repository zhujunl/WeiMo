package com.miaxis.mr230m.http.bean;

/**
 * @author ZJL
 * @date 2022/7/13 9:38
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RequestDeActiveInfo {
   private String appId;
   private String sn;
   private Data data;

   public RequestDeActiveInfo(String appId, String sn, Data data) {
      this.appId = appId;
      this.sn = sn;
      this.data = data;
   }

   public static class Data{
      private String samid_ascii;

      public String getSamid_ascii() {
         return samid_ascii;
      }

      public void setSamid_ascii(String samid_ascii) {
         this.samid_ascii = samid_ascii;
      }

      @Override
      public String toString() {
         return "Data{" +
                 "samid_ascii='" + samid_ascii + '\'' +
                 '}';
      }
   }

   public String getAppId() {
      return appId;
   }

   public void setAppId(String appId) {
      this.appId = appId;
   }

   public String getSn() {
      return sn;
   }

   public void setSn(String sn) {
      this.sn = sn;
   }

   public Data getData() {
      return data;
   }

   public void setData(Data data) {
      this.data = data;
   }

   @Override
   public String toString() {
      return "RequestDeActiveInfo{" +
              "appId='" + appId + '\'' +
              ", sn='" + sn + '\'' +
              ", data=" + data +
              '}';
   }
}

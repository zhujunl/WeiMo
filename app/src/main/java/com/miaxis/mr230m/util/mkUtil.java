package com.miaxis.mr230m.util;

import android.content.Context;

import com.tencent.mmkv.MMKV;

/**
 * @author ZJL
 * @date 2022/9/7 9:33
 * @des
 * @updateAuthor
 * @updateDes
 */
public class mkUtil {
   static  MMKV kv =null;

   public static MMKV getInstance(){
      if (kv==null){
         kv=MMKV.defaultMMKV();
      }
      return kv;
   }

   public static void init(Context context){
      MMKV.initialize(context);
   }
}

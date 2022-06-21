package com.miaxis.http.net;

import com.miaxis.http.bean.RequestActiveInfo;
import com.miaxis.http.bean.RequestOnlineAuth;
import com.miaxis.http.bean.RequestReportIDInfo;
import com.miaxis.http.bean.ResponseActiveInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author ZJL
 * @date 2022/6/17 16:57
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface Api {
    @POST("/decryptservice/activeinfo")
    Call<ResponseActiveInfo> RequestActiveInfo(@Body RequestActiveInfo entity);

    @POST("/decryptservice/onlineauth")
    Call<ResponseActiveInfo> RequestOnlineAuth(@Body RequestOnlineAuth entity);

    @POST("/decryptservice/idinfo")
    Call<ResponseActiveInfo> RequestIDInfo(@Body RequestReportIDInfo entity);
}

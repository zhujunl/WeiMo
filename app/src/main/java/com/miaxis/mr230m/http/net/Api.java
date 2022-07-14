package com.miaxis.mr230m.http.net;

import com.miaxis.mr230m.http.bean.RequestActiveInfo;
import com.miaxis.mr230m.http.bean.RequestDeActiveInfo;
import com.miaxis.mr230m.http.bean.RequestOnlineAuth;
import com.miaxis.mr230m.http.bean.RequestReportIDInfo;
import com.miaxis.mr230m.http.bean.ResponseActiveInfo;
import com.miaxis.mr230m.http.bean.ResponseDeActiveInfo;
import com.miaxis.mr230m.http.bean.ResponseOnlineAuth;
import com.miaxis.mr230m.http.bean.ResponseReportIDInfo;

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
    Call<ResponseOnlineAuth> RequestOnlineAuth(@Body RequestOnlineAuth entity);

    @POST("/decryptservice/idinfo")
    Call<ResponseReportIDInfo> RequestIDInfo(@Body RequestReportIDInfo entity);

    @POST("/decryptservice/deactiveinfo")
    Call<ResponseDeActiveInfo> RequestDeActiveInfo(@Body RequestDeActiveInfo entity);
}

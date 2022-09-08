package com.miaxis.mr230m.miaxishttp.net;

import com.miaxis.mr230m.miaxishttp.bean.TokenRefreshResonse;
import com.miaxis.mr230m.miaxishttp.bean.TokenResonse;
import com.miaxis.mr230m.miaxishttp.bean.WRequestActiveinfo;
import com.miaxis.mr230m.miaxishttp.bean.WRequestDeactiveInfo;
import com.miaxis.mr230m.miaxishttp.bean.WRequestIdInfo;
import com.miaxis.mr230m.miaxishttp.bean.WRequestOnlineauthInfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseActiveinfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseDeactiveInfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseIdInfo;
import com.miaxis.mr230m.miaxishttp.bean.WResponseOnlineauthInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author ZJL
 * @date 2022/9/6 16:20
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface WeiAPi {
    @POST("oms/oms/micromodule/activeinfo")
    Call<WResponseActiveinfo> Activeinfo(@Body WRequestActiveinfo entity);
    @POST("oms/oms/micromodule/deactiveinfo")
    Call<WResponseDeactiveInfo> DeactiveInfo(@Body WRequestDeactiveInfo entity);
    @POST("oms/oms/micromodule/onlineauthinfo")
    Call<WResponseOnlineauthInfo> OnlineauthInfo(@Body WRequestOnlineauthInfo entity);
    @POST("oms/oms/micromodule/idinfo")
    Call<WResponseIdInfo> IdInfo(@Body WRequestIdInfo entity);


    @POST("oms/oms/login")
    Call<TokenResonse> getToken(@Query("cid") String cid);
    @POST("oms/oms/refresh")
    Call<TokenRefreshResonse> RefreshToken();


}

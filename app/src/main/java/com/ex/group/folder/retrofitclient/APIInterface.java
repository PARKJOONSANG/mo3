package com.ex.group.folder.retrofitclient;

import com.ex.group.folder.retrofitclient.pojo.RequestInitInfo;
import com.ex.group.folder.retrofitclient.pojo.RequestLogin;
import com.ex.group.folder.retrofitclient.pojo.RequestMailCount;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

//Retrofit의 가장 큰 특징은 플러그인 방식이다.

//  https://shiny-corolla.tistory.com/48  글을 참고하면 도움이 된다.

public  interface APIInterface {
    @GET("images/fuurestudio-university-logo.png")
    Call<ResponseBody>downloadFile();

    @FormUrlEncoded
    @POST("retrieveLoginInfo.do")  //Login Activity로그인 인증
    Call<RequestLogin> request_user_info(@Field("userId") String userId, @Field("pwd") String pwd,@Field("platformCd") String platformCd,@Field("mdn") String mdn);

    @FormUrlEncoded
    @POST("retrieveUnreadCount.do")  //Login Activity로그인 인증
    Call<RequestMailCount> request_unread_mail_count(@Field("userId") String userId, @Field("platformCd") String platformCd);


    @FormUrlEncoded
    @POST("retrieveInitInfo.do")  // MainActivity 초기정보 조회
    Call<RequestInitInfo> request_initial_info(@Field("userId")String userId, @Field("userType")String userType, @Field("platformCd")String platformCd,
                                               @Field("deviceType")String deviceType);


}

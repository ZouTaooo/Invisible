package com.example.invisible.Service;

import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Bottle;
import com.example.invisible.Bean.Token;
import com.example.invisible.Bean.Trace;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    String BASE_URL = "http://118.24.32.220:8001/";

    //注册
    @FormUrlEncoded
    @POST("api/regist/")
    Observable<Basebean<Token>> register(@Field("username") String username,
                                         @Field("password") String password,
                                         @Field("user_mobile") String user_mobile,
                                         @Field("email") String email);

    //登录
    @FormUrlEncoded
    @POST("api/login/")
    Observable<Basebean<Token>> login(@Field("mobile") String mobile,
                                      @Field("password") String password);

    //判断是否被注册过
    @FormUrlEncoded
    @POST("api/is_registed/")
    Observable<Basebean<Token>> isRegister(@Field("mobile") String mobile);

    //发送短信验证码
    @FormUrlEncoded
    @POST("api/sendms/")
    Observable<Basebean> sendMessage(@Field("mobile") String mobile);

    //验证短信验证码
    @FormUrlEncoded
    @POST("api/receive_sms/")
    Observable<Basebean> testMessage(@Field("mobile") String mobile,
                                     @Field("code") String code);

    //修改密码
    @FormUrlEncoded
    @POST("api/modify_password/")
    Observable<Basebean> modifyPassword(@Field("mobile") String mobile,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("api/get_trace/")
    Observable<Basebean<List<Trace>>> getTrace(@Field("time") String time,
                                               @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/trace/")
    Observable<Basebean> putTrace(@Field("time") String time,
                                  @Field("content") String content,
                                  @Field("type") String type,
                                  @Header("Authorization") String token);

    @GET("api/bottle/")
    Observable<Basebean<Bottle>> getBottle(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/bottle/")
    Observable<Basebean> sendBottle(@Header("Authorization") String token,
                                            @Field("content") String content);

    @FormUrlEncoded
    @POST("api/re_bottle/")
    Observable<Basebean> replyBottle(@Header("Authorization") String token,
                                     @Field("re_content") String reply,
                                     @Field("pre_content") String content,
                                     @Field("to_user") String userId);

    @GET("api/get_rebottle/")
    Observable<Basebean> getHistoryBottle(@Header("Authorization") String token);
}
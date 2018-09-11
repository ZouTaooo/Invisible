package com.example.invisible.Service;

import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Token;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

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
}
package com.example.invisible.Service;

import com.example.invisible.Bean.AnotherPhone;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Bottle;
import com.example.invisible.Bean.HistoryBottles;
import com.example.invisible.Bean.Score;
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

    //获取日迹
    @FormUrlEncoded
    @POST("api/get_trace/")
    Observable<Basebean<List<Trace>>> getTrace(@Field("time") String time,
                                               @Header("Authorization") String token);

    //上传日迹
    @FormUrlEncoded
    @POST("api/trace/")
    Observable<Basebean> putTrace(@Field("time") String time,
                                  @Field("content") String content,
                                  @Field("type") String type,
                                  @Header("Authorization") String token);

    //获取一个漂流瓶
    @GET("api/bottle/")
    Observable<Basebean<Bottle>> getBottle(@Header("Authorization") String token);

    //丢一个漂流瓶
    @FormUrlEncoded
    @POST("api/bottle/")
    Observable<Basebean> sendBottle(@Header("Authorization") String token,
                                    @Field("content") String content,
                                    @Field("date") String date);

    //回复漂流瓶
    @FormUrlEncoded
    @POST("api/re_bottle/")
    Observable<Basebean> replyBottle(@Header("Authorization") String token,
                                     @Field("re_content") String reply,
                                     @Field("pre_content") String content,
                                     @Field("to_user") String userId,
                                     @Field("date") String anything);

    //获取被回复的漂流瓶
    @GET("api/get_rebottle/")
    Observable<Basebean<List<HistoryBottles.HistoryItem>>> getHistoryBottle(@Header("Authorization") String token);

    //聊天匹配
    @FormUrlEncoded
    @POST("api/match/")
    Observable<Basebean<AnotherPhone>> match(@Header("Authorization") String token,
                                             @Field("role") String role);

    //取消匹配
    @POST("api/break_match/")
    Observable<Basebean> break_match(@Header("Authorization") String token);

    //评价
    @FormUrlEncoded
    @POST("api/evalute/")
    Observable<Basebean> evalute(@Header("Authorization") String token,
                                 @Field("add") String add,
                                 @Field("user") String username);

    //获取好感度
    @GET("api/get_score/")
    Observable<Basebean<Score>> get_score(@Header("Authorization") String token);

}
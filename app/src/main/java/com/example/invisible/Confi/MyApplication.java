package com.example.invisible.Confi;

import android.app.Activity;
import android.app.Application;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application{
    private List<Activity> list;

    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<>();
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    //添加单个Activity
    public void addActivity(Activity activity){
        if (!list.contains(activity)) {
            list.add(activity);
        }
    }

    //移除单个Activity
    public void removeActivity(Activity activity) {
        if (list.contains(activity)) {
            list.remove(activity);
        }
    }
    //销毁所有Activity
    public void exitApplication(){
        for (Activity activity:list){
            activity.finish();
        }
    }
}


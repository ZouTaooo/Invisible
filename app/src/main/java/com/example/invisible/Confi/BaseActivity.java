package com.example.invisible.Confi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.invisible.R;

public class BaseActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private MyApplication mApplication;
    private BaseActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mApplication == null) {
            mApplication = (MyApplication)getApplication();
        }
        mContext = this;
        mApplication.addActivity(mContext);
    }


    public void setUpToolbar(Toolbar mToolbar,String title,boolean isBack) {
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isBack);
            actionBar.setTitle(title);
        }
    }



    public void setUpStatusBar(View view,String color) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        view.setBackgroundColor(Color.parseColor(color));
    }
    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /*全屏模式延伸*/
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplication.removeActivity(mContext);
    }

    public void removeAllActivity(){
        mApplication.exitApplication();
    }

    //Toast
    public void T(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    //添加缓存
    public void putS(String key,String value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(key, value);
        editor.apply();
    }

    //获取缓存
    public String getS(String key){
        return getPreference().getString(key, null);
    }

    //获取SharedPreferences对象
    public SharedPreferences getPreference() {
        if (preferences==null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return preferences;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

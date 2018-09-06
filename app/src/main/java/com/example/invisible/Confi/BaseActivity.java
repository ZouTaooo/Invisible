package com.example.invisible.Confi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
    public void getS(String key){
        getPreference().getString(key, null);
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

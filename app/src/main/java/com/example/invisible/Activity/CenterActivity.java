package com.example.invisible.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.invisible.Adapter.MyFragmentPagerAdapter;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Confi.MyApplication;
import com.example.invisible.Fragment.BottleFragment;
import com.example.invisible.Fragment.ChatFragment;
import com.example.invisible.Fragment.DiaryFragment;
import com.example.invisible.R;

import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private ViewPager mViewpager;
    /**
     * 日迹
     */
    private TextView mDiary;
    /**
     * 聊天
     */
    private TextView mChat;
    /**
     * 漂流瓶
     */
    private TextView mBottle;

    private NavigationView mNavigationView;

    private DrawerLayout mDrawLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        //setTranslucent(this);
        initView();

    }

    private void initView() {
        mDiary = (TextView) findViewById(R.id.diary);
        mDiary.setOnClickListener(this);
        mChat = (TextView) findViewById(R.id.chat);
        mChat.setOnClickListener(this);
        mBottle = (TextView) findViewById(R.id.bottle);
        mBottle.setOnClickListener(this);
        mDrawLayout = findViewById(R.id.drawer_layout);
        setUpTitleBar();
        setUpViewPager();
        setUpNav();

    }

    private void setUpViewPager() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        List<Fragment> list = new ArrayList<>();
        list.add(new ChatFragment());
        list.add(new BottleFragment());
        list.add(new DiaryFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        mViewpager.setAdapter(adapter);
    }

    private void setUpNav() {
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modify_password:
                        break;
                    case R.id.modify_nickname:
                        break;
                    case R.id.delete_share_preference:
                        //TODO clear share preferences
                        break;
                    case R.id.settings:
                        break;
                    case R.id.about:
                        break;
                    case R.id.feedback:
                        break;
                    case R.id.exit:
                        startActivity(new Intent(CenterActivity.this,LoginActivity.class));
                        removeAllActivity();
                        break;
                    default:
                        break;
                }
                mDrawLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void setUpTitleBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("聊天");
        mToolbar.setNavigationIcon(R.drawable.ic_reorder_white_36dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    //如果DrawLayout被打开则关闭DrawLayout
    @Override
    public void onBackPressed() {
        if (mDrawLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 使状态栏透明
     * <p>
     * <p>
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */

    public static void setTranslucent(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // 设置状态栏透明

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // 设置根布局的参数

            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);

            rootView.setFitsSystemWindows(true);

            rootView.setClipToPadding(true);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.diary:
                break;
            case R.id.chat:
                break;
            case R.id.bottle:
                break;
        }
    }
}

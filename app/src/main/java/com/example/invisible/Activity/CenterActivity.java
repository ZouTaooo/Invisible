package com.example.invisible.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.invisible.Adapter.MyFragmentPagerAdapter;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Fragment.ChatFragment;
import com.example.invisible.Fragment.ListenFragment;
import com.example.invisible.Fragment.TalkFragment;
import com.example.invisible.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CenterActivity extends BaseActivity implements View.OnClickListener {

    private static final String FRAGMENT_ONE_COLOR = "#242C3B";
    private static final String FRAGMENT_TWO_COLOR = "#2784E6";
    private static final String FRAGMENT_ZERO_COLOR = "#EE2B29";

    private Map<Integer, String> colorMap;

    private Map<Integer, TextView> textViewMap;

    private int pre_position = -1;

    private int now_position = 1;

    private Toolbar mToolbar;

    private ViewPager mViewpager;
    /**
     * 倾诉
     */
    private TextView mTalk;
    /**
     * 聊天
     */
    private TextView mChat;
    /**
     * 聆听
     */
    private TextView mListen;

    private NavigationView mNavigationView;

    private DrawerLayout mDrawLayout;

    private View statusBar;

    private LinearLayout mHeader;


    private ImageView mNav_pic;

    private static final String TAG = "CenterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        fullScreen(this);
        initView();
        initMap();
    }

    private void initMap() {
        colorMap = new HashMap();
        colorMap.put(0, FRAGMENT_ZERO_COLOR);
        colorMap.put(1, FRAGMENT_ONE_COLOR);
        colorMap.put(2, FRAGMENT_TWO_COLOR);
        textViewMap = new HashMap<>();
        textViewMap.put(0, mTalk);
        textViewMap.put(1, mChat);
        textViewMap.put(2, mListen);
    }

    private void initView() {
        mHeader = findViewById(R.id.center_header);
        mTalk = (TextView) findViewById(R.id.talk);
        mTalk.setOnClickListener(this);
        mChat = (TextView) findViewById(R.id.chat);
        mChat.setTextSize(22);
        mChat.setOnClickListener(this);
        mListen = (TextView) findViewById(R.id.listen);
        mListen.setOnClickListener(this);
        mDrawLayout = findViewById(R.id.drawer_layout);
        setUpStatusBar();
        setUpTitleBar();
        setUpViewPager();
        setUpNav();
    }

    private void getBingPic() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://guolin.tech/api/bing_pic")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String url = response.body().string();
                putS("bing_pic", url);
                putS("pic_time", "");
                Log.e(TAG, "onResponse: " + url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(CenterActivity.this).load(url).into(mNav_pic);
                    }
                });
            }
        });
    }

    private void setUpStatusBar() {
        statusBar = findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        statusBar.setBackgroundColor(Color.parseColor(FRAGMENT_ONE_COLOR));
    }

    private void setUpViewPager() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        List<Fragment> list = new ArrayList<>();
        list.add(new TalkFragment());
        list.add(new ChatFragment());
        list.add(new ListenFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        mViewpager.setAdapter(adapter);
        mViewpager.setCurrentItem(1);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("vp", "滑动中=====position:" + position + "   positionOffset:" + positionOffset + "   positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                pre_position = now_position;
                now_position = position;
                BackgroundColorAnimation(pre_position, now_position);
                textViewMap.get(now_position).setTextSize(22);
                textViewMap.get(pre_position).setTextSize(20);
                Log.e("vp", "显示页改变=====postion:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        Log.e(TAG, "onPageScrollStateChanged: prepositon: " + pre_position + "     nowposition: " + now_position);
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        Log.e("vp", "状态改变=====SCROLL_STATE_DRAGGING==滑动状态");
                        pre_position = now_position;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        Log.e("vp", "状态改变=====SCROLL_STATE_SETTLING==滑翔状态");
                        break;
                }
            }
        });
    }

    private void setUpNav() {
        mNavigationView = findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        mNav_pic = headerView.findViewById(R.id.nav_pic);
        String url = getS("bing_pic");
        if (url != null) {
            Glide.with(CenterActivity.this)
                    .load(url)
                    .into(mNav_pic);
        } else {
            getBingPic();
        }
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
                        startActivity(new Intent(CenterActivity.this, LoginActivity.class));
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
        mToolbar.setNavigationOnClickListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        //menu item click event
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.diary:
                        startActivity(new Intent(CenterActivity.this, DiaryActivity.class));
                        break;
                    case R.id.bottle:
                        startActivity(new Intent(CenterActivity.this, BottlesActivity.class));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /*左上角导航键点击事件*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    /*如果DrawLayout被打开则关闭DrawLayout*/
    @Override
    public void onBackPressed() {
        if (mDrawLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        Log.e(TAG, "getStatusBarHeight: " + result);
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.talk:
                changePager(0);
                break;
            case R.id.chat:
                changePager(1);
                break;
            case R.id.listen:
                changePager(2);
                break;
        }
    }

    private void changePager(int position) {
        mViewpager.setCurrentItem(position);
    }

    /*加载右上角菜单栏*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.center_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*通过反射为菜单item加icon*/
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //hasToolbar 是用来判断该布局是否包含toolbar。
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /*为上半部分和状态栏加渐变动画*/
    private void BackgroundColorAnimation(int pre_position, int now_position) {
        ValueAnimator headerAnim = ObjectAnimator.ofInt(mHeader, "backgroundColor", Color.parseColor(colorMap.get(pre_position)), Color.parseColor(colorMap.get(now_position)));
        headerAnim.setDuration(500);
        headerAnim.setEvaluator(new ArgbEvaluator());
        headerAnim.start();
        ValueAnimator statusAnim = ObjectAnimator.ofInt(statusBar, "backgroundColor", Color.parseColor(colorMap.get(pre_position)), Color.parseColor(colorMap.get(now_position)));
        statusAnim.setDuration(500);
        statusAnim.setEvaluator(new ArgbEvaluator());
        statusAnim.start();
    }
}

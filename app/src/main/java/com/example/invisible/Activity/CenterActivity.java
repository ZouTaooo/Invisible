package com.example.invisible.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.invisible.Adapter.MyFragmentPagerAdapter;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Score;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.Fragment.ChatFragment;
import com.example.invisible.Fragment.ListenFragment;
import com.example.invisible.Fragment.TalkFragment;
import com.example.invisible.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    private TextView mNickname;

    private TextView mUserNum;

    private View statusBar;

    private LinearLayout mHeader;

    private ImageView mHead;

    private int headPics[] = {R.drawable.head1_1, R.drawable.head1_2, R.drawable.head1_3,
            R.drawable.head1_4, R.drawable.head1_5, R.drawable.head1_6, R.drawable.head1_7, R.drawable.head1_8};


    private static final String TAG = "CenterActivity";

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        fullScreen(this);
        initView();
        initMap();
    }

    private void getScore() {
        RetrofitFactory.getInstance().get_score("Token " + getS("token"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean<Score>>() {
                    @Override
                    public void accept(Basebean<Score> basebean) throws Exception {
                        if (basebean.getStatus() == 1) {
                            String score = basebean.getBody().getScore();
                            Log.e(TAG, "accept: score:" + score);
                            mUserNum.setText(score);
                        } else {
                            Toast.makeText(CenterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(CenterActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
        mNickname = findViewById(R.id.nickname);
        name = getS("name");
        mNickname.setText(name);
        mUserNum = findViewById(R.id.user_num);
        statusBar = findViewById(R.id.statusBarView);
        setUpStatusBar(statusBar, FRAGMENT_ONE_COLOR);
        toolbarSettings();
        setUpViewPager();
        mHead = findViewById(R.id.head_pic);
        Glide.with(CenterActivity.this).load(headPics[new Random().nextInt(8)]).into(mHead);
    }


    private void toolbarSettings() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(this);
        setUpToolbar(mToolbar, "", false);
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
                    case R.id.exit:
                        startActivity(new Intent(CenterActivity.this, LoginActivity.class));
                        finish();
                        break;
                    case R.id.delete_share_preference:
                        Toast.makeText(CenterActivity.this, "清理成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        getScore();
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

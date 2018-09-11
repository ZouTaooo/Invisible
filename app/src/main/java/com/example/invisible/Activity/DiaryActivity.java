package com.example.invisible.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.baoyachi.stepview.VerticalStepView;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DiaryActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackgroundImage;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private VerticalStepView mStepView;
    private FloatingActionButton mFab;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        initView();
    }

    private void initView() {
        mBackgroundImage = (ImageView) findViewById(R.id.background_image);
        setUpToolbar();
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mCollapsingToolbar.setTitle(year + "年" + (month + 1) + "月" + day + "日");
        setUpStepView();
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
    }

    private void setUpToolbar() {
        calendar = Calendar.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.change:
                        new DatePickerDialog(DiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mCollapsingToolbar.setTitle(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();

                }
                return false;
            }
        });
    }

    private void setUpStepView() {
        mStepView = (VerticalStepView) findViewById(R.id.step_view);
        List<String> list = new ArrayList<>();
        list.add("我今天心情很不错\n                               9:20  日迹");
        list.add("我今天心情很不错\n                               9:20  日迹");
        list.add("我今天心情很不错\n                               9:30  日迹");
        list.add("今日共3条日迹哦");
        mStepView.setStepsViewIndicatorComplectingPosition(list.size() - 1)//设置完成的步数
                .reverseDraw(false)//default is true
                .setStepViewTexts(list)//总步骤
                .setLinePaddingProportion(3f)//设置indicator线与线间距的比例系数
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getApplicationContext(), R.color.uncompleted_text_color))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.color_black))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.color_black))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.color_black))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.icons8_circle_96))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.icons8_circle_96))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.icons8_circle_96));//设置StepsViewIndicator AttentionIcon
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.fab:
                break;
        }
    }
}

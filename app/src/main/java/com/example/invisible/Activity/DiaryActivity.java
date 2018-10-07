package com.example.invisible.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Trace;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.FlowViewVertical;
import com.example.invisible.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiaryActivity extends BaseActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private ImageView mBackgroundImage;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private FlowViewVertical mFlowView;
    private FloatingActionButton mFab;
    private Calendar calendar;
    private TextView mWeek;
    private int year;
    private int month;
    private int day;
    private String token;
    private String date;
    private Realm realm;
    private List<String> stringList;
    private boolean isGetTraceFromServerSuccess;

    private static final String TAG = "DiaryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        isGetTraceFromServerSuccess = false;
        stringList = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;//要+1
        day = calendar.get(Calendar.DAY_OF_MONTH);
        token = getS("token");
        date = "" + year + convertInt(month) + convertInt(day);//20180920
        initView();
        getTrace(token, date);
    }

    private void getTrace(String token, String date) {
        Log.e(TAG, "getTrace: 从数据库获取数据");
        RealmResults<Trace> traces = getTraceFromDatabase(date);
        if (traces.size() == 0 && !isGetTraceFromServerSuccess) {
            Log.e(TAG, "getTrace: 从服务器获取日迹数据");
            getTraceFromServer(token, date);
        } else {
            Log.e(TAG, "getTrace: 更新UI");
            stringList.clear();
            for (Trace trace : traces) {
                stringList.add(handleTrace(trace));
            }
            for (String text : stringList) {
                Log.e(TAG, "getTrace: " + text);
            }
            updateStepView();
            isGetTraceFromServerSuccess = false;
        }
    }

    private RealmResults<Trace> getTraceFromDatabase(String date) {
        return realm.where(Trace.class)
                .contains("date", date).findAll().sort("date");
    }

    private void updateStepView() {
        Log.e(TAG, "updateStepView: 重新设置数据  共" + stringList.size() + "条数据");
        String[] date = new String[100];
        for (int i = 0; i < stringList.size(); i++) {
            date[i] = stringList.get(i);
        }
        if (stringList.size() == 0) {
            date = null;
            Log.e(TAG, "updateStepView: date = null");
        }
        mFlowView.setProgress(stringList.size(), stringList.size(), date, null);
        //TODO if no date add background remind add trace
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
                View view = LayoutInflater.from(this).inflate(R.layout.layout_edit_text, null);
                final EditText editText = view.findViewById(R.id.edit_content);
                final TextView textView = view.findViewById(R.id.char_num);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        textView.setText(s.toString().length() + "字");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                new AlertDialog.Builder(this).setTitle("日迹")
                        .setView(view)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String type = "日迹";
                                String content = editText.getText().toString();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);
                                //time 201809201559 具体到分钟
                                String time = convertInt(year) + convertInt(month) + convertInt(day) + convertInt(hour) + convertInt(minute);
                                Log.e(TAG, "onClick: 上传的time为" + time);
                                putTrace(time, content, type, token);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    //将小于10的数字转化为0+num
    private String convertInt(int num) {
        if (num < 10) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void getTraceFromServer(final String token, String time) {
        RetrofitFactory.getInstance().getTrace(time, "Token " + token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean<List<Trace>>>() {
                    @Override
                    public void accept(Basebean<List<Trace>> listBasebean) throws Exception {
                        List<Trace> traceList = listBasebean.getBody();
                        if (traceList != null) {
                            for (Trace trace : traceList) {
                                addToDatabase(trace);
                            }
                        }
                        isGetTraceFromServerSuccess = true;
                        getTrace(token, date);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        T(throwable.getMessage());
                    }
                });
    }

    @NonNull
    private String handleTrace(Trace trace) {
        String date = trace.getDate();
        int hour = Integer.parseInt(date.substring(8, 10));
        int minute = Integer.parseInt(date.substring(10, 12));
        String content = trace.getContent();
        String type = trace.getType();
        String var1;
        if (hour >= 12) {
            var1 = "下午";
        } else {
            var1 = "上午";
        }
        return content +
                "\n                               -" + var1 + hour + "时" + minute + "分," + type;
    }

    //添加一条日迹记录到数据库
    private void addToDatabase(final Trace trace) {
        realm.beginTransaction();
        Trace trace1 = realm.createObject(Trace.class);
        Log.e(TAG, "execute: 插入的date为" + trace.date);
        trace1.date = trace.date;
        trace1.type = trace.type;
        trace1.content = trace.content;
        realm.commitTransaction();
    }

    //上传日迹并保存到数据库中 根据是否为当前页面判断是否更新页面
    private void putTrace(final String time, final String content,
                          final String type, final String token) {
        RetrofitFactory.getInstance().putTrace(time, content, type, "Token " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        final Trace trace = new Trace();
                        trace.setContent(content);
                        trace.setType(type);
                        trace.setDate(time);
                        addToDatabase(trace);
                        //time精确到分钟 date精确到日期
                        if (time.substring(0, 8).equals(date)) {
                            //如果在当天的页面添加日迹 则会进行更新
                            getTrace(token, date);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    //依据年月日获取是星期几
    String getWeek(int y, int m, int d) {
        if (m == 1 || m == 2) {//一二月换算
            m += 12;
            y--;
        }
        int week = (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400 + 1) % 7;
        String weekNum = "";
        switch (week) {
            case 1:
                weekNum = "一";
                break;
            case 2:
                weekNum = "二";
                break;
            case 3:
                weekNum = "三";
                break;
            case 4:
                weekNum = "四";
                break;
            case 5:
                weekNum = "五";
                break;
            case 6:
                weekNum = "六";
                break;
            case 0:
                weekNum = "天";
                break;
        }
        return "星期" + weekNum;//其中1~7表示周一到周日
    }

    //获取Bing的每日一图
    private void getBingPic() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://guolin.tech/api/bing_pic")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + "获取失败");
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
                        Glide.with(DiaryActivity.this).load(url).into(mBackgroundImage);
                    }
                });
            }
        });
    }

    private void initView() {
        mBackgroundImage = (ImageView) findViewById(R.id.background_image);
        String url = getS("bing_pic");
        if (url != null && date.equals(getS("pic_time"))) {
            Glide.with(DiaryActivity.this).load(url).into(mBackgroundImage);
        } else {
            getBingPic();
        }
        setUpToolbar();
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(year + "年" + month + "月" + day + "日");
        mWeek = findViewById(R.id.week);
        mWeek.setText(getWeek(year, month, day));
        mFlowView = findViewById(R.id.FlowViewVertical);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change:
                //日期选择器
                new DatePickerDialog(DiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCollapsingToolbar.setTitle(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                        mWeek.setText(getWeek(year, month + 1, dayOfMonth));
                        date = "" + year + convertInt(month + 1) + convertInt(dayOfMonth);
                        getTrace(token, date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
        return false;
    }
}

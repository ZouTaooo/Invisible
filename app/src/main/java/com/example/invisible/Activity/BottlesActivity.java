package com.example.invisible.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.invisible.Adapter.HistoryBottlesRecyclerViewAdapter;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Bottle;
import com.example.invisible.Bean.HistoryBottles;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BottlesActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BottlesActivity";

    private String token;

    private Toolbar mToolbar;
    /**
     * 扔一个漂流瓶
     */
    private TextView mTitle11;
    /**
     * 输入你的想法,把他丢到海洋中吧
     */
    private TextView mContent11;
    private TextView mAddBottle;
    private ImageButton mBack11;
    private EditText mBottleContent;
    /**
     * 丢出
     */
    private TextView mPush;
    /**
     * 捡一个漂流瓶
     */
    private TextView mTitle21;
    /**
     * 快从这片海洋里拾起一个漂流瓶吧
     */
    private TextView mContent21;
    private TextView mGetBottle;
    private ImageButton mBack21;
    /**
     * 漂流瓶内容
     */
    private TextView mContent22;
    /**
     * 回复
     */
    private TextView mReply;
    private ImageButton mBack22;
    /**
     * 快从这片海洋里拾起一个漂流瓶吧
     */
    private EditText mReplyContent;
    /**
     * 发送
     */
    private TextView mSend;
    private RecyclerView mBottlesRecyclerView;
    private LinearLayout mContentLayout;

    private View statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottles);
        token = getS("token");
        Log.e(TAG, "onCreate: " + token);
        fullScreen(this);
        initView();
    }

    private void initView() {
        statusView = findViewById(R.id.statusBarView);
        setUpStatusBar(statusView, "#242C3B");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar(mToolbar, "", true);
        mTitle11 = (TextView) findViewById(R.id.title1_1);
        mContent11 = (TextView) findViewById(R.id.content1_1);
        mAddBottle = findViewById(R.id.add_bottle);
        mAddBottle.setOnClickListener(this);
        mBack11 = (ImageButton) findViewById(R.id.back1_1);
        mBack11.setOnClickListener(this);
        mBottleContent = (EditText) findViewById(R.id.bottle_content);
        mPush = (TextView) findViewById(R.id.push);
        mPush.setOnClickListener(this);
        mTitle21 = (TextView) findViewById(R.id.title2_1);
        mContent21 = (TextView) findViewById(R.id.content2_1);
        mGetBottle = findViewById(R.id.get_bottle);
        mGetBottle.setOnClickListener(this);
        mBack21 = (ImageButton) findViewById(R.id.back2_1);
        mBack21.setOnClickListener(this);
        mContent22 = (TextView) findViewById(R.id.content2_2);
        mReply = (TextView) findViewById(R.id.reply);
        mReply.setOnClickListener(this);
        mBack22 = (ImageButton) findViewById(R.id.back2_2);
        mBack22.setOnClickListener(this);
        mReplyContent = (EditText) findViewById(R.id.reply_content);
        mSend = (TextView) findViewById(R.id.send);
        mSend.setOnClickListener(this);
        setUpRecyclerView();
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
    }

    private void setUpRecyclerView() {
        mBottlesRecyclerView = (RecyclerView) findViewById(R.id.bottles_recyclerView);
        getHistoryBottle();
    }

    private void getHistoryBottle() {
        RetrofitFactory.getInstance().getHistoryBottle("Token " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basebean<List<HistoryBottles.HistoryItem>>>() {
                    @Override
                    public void accept(Basebean<List<HistoryBottles.HistoryItem>> listBasebean) throws Exception {
                        List<HistoryBottles.HistoryItem> historyItems;
                        if (listBasebean.getBody() != null) {
                            historyItems = listBasebean.getBody();
                        } else {
                            historyItems = new ArrayList<>();
                        }
                        HistoryBottlesRecyclerViewAdapter adapter = new HistoryBottlesRecyclerViewAdapter(BottlesActivity.this, historyItems);
                        mBottlesRecyclerView.setLayoutManager(new LinearLayoutManager(BottlesActivity.this));
                        mBottlesRecyclerView.setAdapter(adapter);
                    }
                });
    }

    private void setVisibility1_1(int visibility) {
        mTitle11.setVisibility(visibility);
        mContent11.setVisibility(visibility);
        mAddBottle.setVisibility(visibility);
    }

    private void setVisibility1_2(int visibility) {
        mBack11.setVisibility(visibility);
        mBottleContent.setVisibility(visibility);
        mPush.setVisibility(visibility);
    }

    private void setVisibility2_1(int visibility) {
        mTitle21.setVisibility(visibility);
        mContent21.setVisibility(visibility);
        mGetBottle.setVisibility(visibility);
    }

    private void setVisibility2_2(int visibility) {
        mBack21.setVisibility(visibility);
        mContent22.setVisibility(visibility);
        mReply.setVisibility(visibility);
    }

    private void setVisibility2_3(int visibility) {
        mBack22.setVisibility(visibility);
        mReplyContent.setVisibility(visibility);
        mSend.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.add_bottle:
                setVisibility1_1(View.GONE);
                setVisibility1_2(View.VISIBLE);
                break;
            case R.id.back1_1:
                setVisibility1_2(View.GONE);
                setVisibility1_1(View.VISIBLE);
                break;
            case R.id.push:
                String content = mBottleContent.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    pushBottle(content);
                }
                setVisibility1_1(View.VISIBLE);
                setVisibility1_2(View.GONE);
                mBottleContent.setText("");
                break;
            case R.id.get_bottle:
                setVisibility2_1(View.GONE);
                getBottle();
                setVisibility2_2(View.VISIBLE);
                break;
            case R.id.back2_1:
                setVisibility2_1(View.VISIBLE);
                setVisibility2_2(View.GONE);
                break;
            case R.id.reply:
                setVisibility2_2(View.GONE);
                setVisibility2_3(View.VISIBLE);
                break;
            case R.id.back2_2:
                setVisibility2_2(View.VISIBLE);
                setVisibility2_3(View.GONE);
                break;
            case R.id.send:
                String reply = mReplyContent.getText().toString();
                sendReply(reply);
                setVisibility2_1(View.VISIBLE);
                setVisibility2_3(View.GONE);
                mContent22.setText("");
                mReplyContent.setText("");
                break;
        }
    }

    private void sendReply(String reply) {
        RetrofitFactory.getInstance()
                .replyBottle("Token " + token, reply, getS("reply"), getS("userId"), "anything")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        Log.e(TAG, "accept:send " + basebean.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept:send " + throwable.getMessage());
                    }
                });
    }

    private void getBottle() {
        RetrofitFactory.getInstance().getBottle("Token " + token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basebean<Bottle>>() {
                    @Override
                    public void accept(Basebean<Bottle> bottleBasebean) throws Exception {
                        Log.e(TAG, "accept:getBottle " + bottleBasebean.getBody().getContent());
                        mContent22.setText(bottleBasebean.getBody().getContent());
                        putS("reply", bottleBasebean.getBody().getContent());
                        putS("userId", bottleBasebean.getBody().getFrom());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept:getBottle " + throwable.getMessage());
                    }
                });
    }

    private void pushBottle(String content) {
        RetrofitFactory.getInstance().sendBottle("Token " + token, content, "anything")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        Log.e(TAG, "accept:push " + basebean.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept:push " + throwable.getMessage());
                    }
                });
    }
}

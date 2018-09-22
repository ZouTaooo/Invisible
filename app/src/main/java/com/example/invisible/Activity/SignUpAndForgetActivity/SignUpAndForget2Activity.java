package com.example.invisible.Activity.SignUpAndForgetActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.invisible.Bean.Basebean;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.R;
import com.jkb.vcedittext.VerificationCodeEditText;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignUpAndForget2Activity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    /**
     * 验证码已发送到177 1135 6411
     */
    private TextView mMsg;
    /**
     * 35s后重发送
     */
    private TextView mReSend;
    private VerificationCodeEditText mCode;
    private Button mNext;
    private String phone;

    final static private int PHONE_REGISTER = 0;

    final static private int FORGET_PASSWORD = 1;
    private int type;
    private static final String TAG = "SignUpAndForget2Activit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_and_forget2);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        phone = intent.getStringExtra("phone");
        initView();
        sendMessage();
        Log.e(TAG, "onCreate: start" );
        clockButton(60);
    }

    private void sendMessage() {
        RetrofitFactory.getInstance().sendMessage(phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        Log.e(TAG, "accept: " + basebean.getMsg());
                    }
                });
    }

    private void initView() {
        setUpToolbar();
        setUpMsg();
        setUpButton();
        mReSend = (TextView) findViewById(R.id.reSend);
        mReSend.setOnClickListener(this);
        mCode = (VerificationCodeEditText) findViewById(R.id.code);
    }

    private void setUpButton() {
        mNext = (Button) findViewById(R.id.base_btn);
        mNext.setText("下一步");
        mNext.setOnClickListener(this);
    }

    private void setUpMsg() {
        mMsg = (TextView) findViewById(R.id.msg);
        mMsg.setText("验证码已发送到" + phone.substring(0, 3) + " " + phone.substring(3, 7) + " " + phone.substring(7, 11));
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (type == PHONE_REGISTER) {
            actionBar.setTitle("手机注册");
        } else if (type == FORGET_PASSWORD) {
            actionBar.setTitle("忘记密码");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.reSend:
                //TODO 重发
                sendMessage();
                Log.e(TAG, "onClick: 重发");
                clockButton(60);
                break;
            case R.id.base_btn:
                testMessage();
//                Intent i = new Intent(SignUpAndForget2Activity.this, SignUpAndForget3Activity.class);
//                i.putExtra("type", type);
//                i.putExtra("phone", phone);
//                startActivity(i);
                break;
        }
    }

    private void testMessage() {
        RetrofitFactory.getInstance().testMessage(phone, mCode.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        Log.e(TAG, "accept: " + basebean.getMsg());
                        if (basebean.getStatus() == 1) {
                            Intent i = new Intent(SignUpAndForget2Activity.this, SignUpAndForget3Activity.class);
                            i.putExtra("type", type);
                            i.putExtra("phone", phone);
                            startActivity(i);
                        } else {
                            T(basebean.getMsg());
                        }
                    }
                });
    }

    /**
     * 重新发送按钮倒计时
     *
     * @param second
     */
    private void clockButton(final int second) {
        //用来在执行子线程中的FlowableOnSubscribe的subscribe方法之前执行
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                mReSend.setClickable(false);
                mReSend.setTextColor(Color.parseColor("#8A000000"));
            }
        };

        //返回主线程执行
        FlowableSubscriber subscriber = new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(second + 1);//需要接收,1秒一条,还有最后一个onComplete
            }

            @Override
            public void onNext(Integer i) {
                mReSend.setText(i+"s后重新发送");
            }

            @Override
            public void onError(Throwable t) {
                Log.e("FlowableSubscriber", "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d("FlowableSubscriber", "onComplete: ");
                mReSend.setText("重新发送");
                mReSend.setTextColor(Color.parseColor("#F23826"));
                mReSend.setClickable(true);
            }
        };

        //在子线程中执行
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                int ss = second;//倒计时ss秒
                while (ss > 0) {
                    e.onNext(ss);
                    ss--;
                    Thread.sleep(1000);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);//BUFFER就是把RxJava中默认的只能存128个事件的缓存池换成一个大的缓存池，支持存很多很多的数据。

        flowable.subscribeOn(Schedulers.io())
                .doOnSubscribe(consumer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

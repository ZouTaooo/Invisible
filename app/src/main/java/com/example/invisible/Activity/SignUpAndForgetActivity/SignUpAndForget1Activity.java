package com.example.invisible.Activity.SignUpAndForgetActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.invisible.Bean.Basebean;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.R;

import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignUpAndForget1Activity extends BaseActivity implements View.OnClickListener {

    /**
     * 请输入手机号
     */
    private EditText mAccount;
    private Button mNext;
    final static private int PHONE_REGISTER = 0;

    final static private int FORGET_PASSWORD = 1;
    private int type;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_and_forget1);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        initView();
    }

    private void initView() {
        setUpToolbar();
        setUpButton();
        mAccount = (EditText) findViewById(R.id.account);
    }



    private void setUpButton() {
        mNext = (Button) findViewById(R.id.base_btn);
        mNext.setText("下一步");
        mNext.setOnClickListener(this);
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            case R.id.base_btn:
                String account = mAccount.getText().toString();
                String pattern = "[0-9]*";
                if (Pattern.matches(pattern, account) && account.length() == 11) {
                    isRegister();
                } else {
                    T("手机号错误");
                }
//                Intent i = new Intent(SignUpAndForget1Activity.this, SignUpAndForget2Activity.class);
//                i.putExtra("type", type);
//                i.putExtra("phone", mAccount.getText().toString());
//                startActivity(i);
//                break;
        }
    }

    private void isRegister() {
        RetrofitFactory.getInstance().isRegister(mAccount.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        if (type == PHONE_REGISTER) {
                            startActivity(basebean, 1, PHONE_REGISTER, "手机号已被注册");
                        } else if (type == FORGET_PASSWORD) {
                            startActivity(basebean, 0, FORGET_PASSWORD, "手机号未被注册");
                        }
                    }
                });
    }

    private void startActivity(Basebean basebean, int status, int type, String msg) {
        if (basebean.getStatus() == status) {
            Intent i = new Intent(SignUpAndForget1Activity.this, SignUpAndForget2Activity.class);
            i.putExtra("type", type);
            i.putExtra("phone", mAccount.getText().toString());
            startActivity(i);
        } else {
            T(msg);
        }
    }
}

package com.example.invisible.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.invisible.Activity.SignUpAndForgetActivity.SignUpAndForget1Activity;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Token;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.R;

import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    /**
     * Phone Number
     */
    private EditText mAccount;
    /**
     * Password
     */
    private EditText mPassword;
    /**
     * 登录
     */
    private Button mLogin;
    /**
     * 快速注册
     */
    private TextView mSignUp;
    /**
     * 忘记密码
     */
    private TextView mForgetPassword;

    private String phone;
    private String password;

    final static private int PHONE_REGISTER = 0;

    final static private int FORGET_PASSWORD = 1;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        password = i.getStringExtra("password");
        initView();
    }

    private void initView() {
        setUpEditText();
        setUpButton();
        mSignUp = (TextView) findViewById(R.id.signUp);
        mSignUp.setOnClickListener(this);
        mForgetPassword = (TextView) findViewById(R.id.forgetPassword);
        mForgetPassword.setOnClickListener(this);
    }

    private void setUpButton() {
        mLogin = (Button) findViewById(R.id.base_btn);
        mLogin.setOnClickListener(this);
        mLogin.setText("登录");
    }

    private void setUpEditText() {
        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            mAccount.setText(phone);
            mPassword.setText(password);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            default:
                break;
            case R.id.base_btn:
                String account = mAccount.getText().toString();
                String password = mPassword.getText().toString();
                String pattern = "[0-9]*";
                if (Pattern.matches(pattern, account) && account.length() == 11) {
                    if (!TextUtils.isEmpty(password))
                        login();
                    else T("密码为空");
                } else {
                    T("手机号错误");
                }
                break;
            case R.id.signUp:
                intent = new Intent(this, SignUpAndForget1Activity.class);
                intent.putExtra("type", PHONE_REGISTER);
                startActivity(intent);
                break;
            case R.id.forgetPassword:
                intent = new Intent(this, SignUpAndForget1Activity.class);
                intent.putExtra("type", FORGET_PASSWORD);
                startActivity(intent);
                break;
        }
    }

    private void login() {
        RetrofitFactory.getInstance().login(mAccount.getText().toString(), mPassword.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean<Token>>() {
                    @Override
                    public void accept(Basebean<Token> tokenBasebean) throws Exception {
                        if (tokenBasebean.getStatus() == 1) {
                            putS("token", tokenBasebean.getBody().getToken());
                            Log.e(TAG, "accept: token" + tokenBasebean.getBody().getToken());
                            startActivity(new Intent(LoginActivity.this,CenterActivity.class));
                        } else {
                            T(tokenBasebean.getMsg());
                        }
                        Log.e(TAG, "accept: " + tokenBasebean.getMsg());
                    }
                });
    }
}

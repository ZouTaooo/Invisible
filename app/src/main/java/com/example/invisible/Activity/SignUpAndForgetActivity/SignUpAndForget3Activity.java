package com.example.invisible.Activity.SignUpAndForgetActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.invisible.Activity.LoginActivity;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Token;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Confi.MyApplication;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignUpAndForget3Activity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    /**
     * 请输入密码
     */
    private EditText mPassword1;
    /**
     * 再次输入密码
     */
    private EditText mPassword2;
    private int type;
    private String phone;
    /**
     * Nickname
     */
    private EditText mNickname;
    /**
     * Email
     */
    private TextView mTextNickname;
    private TextView mTextEmail;
    private EditText mEmail;
    private Button mBaseBtn;

    final static private int PHONE_REGISTER = 0;

    final static private int FORGET_PASSWORD = 1;

    private static final String TAG = "SignUpAndForget3Activit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_and_forget3);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        phone = intent.getStringExtra("phone");
        initView();
        Log.e(TAG, "onCreate: type" + type);
    }

    private void initView() {
        mBaseBtn = (Button) findViewById(R.id.base_btn);
        mPassword1 = (EditText) findViewById(R.id.password1);
        mPassword2 = (EditText) findViewById(R.id.password2);
        mBaseBtn.setOnClickListener(this);
        setUpToolbar();
        setUpVisibility();
    }

    private void setUpVisibility() {
        mTextEmail = findViewById(R.id.text_email);
        mTextNickname = findViewById(R.id.text_nickname);
        mNickname = (EditText) findViewById(R.id.nickname);
        mEmail = (EditText) findViewById(R.id.email);
        if (type == FORGET_PASSWORD) {
            mTextNickname.setVisibility(View.GONE);
            mTextEmail.setVisibility(View.GONE);
            mNickname.setVisibility(View.GONE);
            mEmail.setVisibility(View.GONE);
        }
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (type == PHONE_REGISTER) {
            actionBar.setTitle("手机注册");
            mBaseBtn.setText("注册");
        } else if (type == FORGET_PASSWORD) {
            actionBar.setTitle("忘记密码");
            mBaseBtn.setText("修改");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.base_btn:
                String nickname = mNickname.getText().toString();
                String password1 = mPassword1.getText().toString();
                String password2 = mPassword2.getText().toString();
                String email = mEmail.getText().toString();
                if (type == PHONE_REGISTER) {
                    registerAccount(nickname, password1, password2, email);
                } else if (type == FORGET_PASSWORD) {
                    modifyPassword(password1, password2);
                }
                break;
        }
    }

    private void modifyPassword(String password1, String password2) {
        if (password1.equals(password2)) {
            RetrofitFactory.getInstance().modifyPassword(phone, password1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            new Consumer<Basebean>() {
                                @Override
                                public void accept(Basebean basebean) throws Exception {
                                    if (basebean.getStatus() == 1) {
                                        showDialog("修改成功!");
                                    } else {
                                        T(basebean.getMsg());
                                    }
                                }
                            });
        }
    }

    private void registerAccount(String nickname, String password1, String password2, String email) {
        if (!TextUtils.isEmpty(nickname)
                && !TextUtils.isEmpty(password1)
                && password1.equals(password2)
                && !TextUtils.isEmpty(email)) {
            register(nickname, password1, email, phone);
        } else {
            T("信息有误 请检查");
        }
    }

    private void register(String nickname, String password1, String email, String phone) {
        RetrofitFactory.getInstance().register(nickname, password1, phone, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean<Token>>() {
                    @Override
                    public void accept(Basebean<Token> tokenBasebean) throws Exception {
                        Log.e(TAG, "accept: " + tokenBasebean.getMsg());
                        if (tokenBasebean.getStatus() == 1) {
                            showDialog("欢迎来到Invisible!");
                        } else {
                            T("注册失败");
                        }
                    }
                });
    }

    private void showDialog(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpAndForget3Activity.this)
                .setTitle("Invisible")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(SignUpAndForget3Activity.this, LoginActivity.class);
                        putS("login_phone", phone);
                        putS("login_password", mPassword1.getText().toString());
                        startActivity(i);
                        removeAllActivity();
                    }
                });
        dialog.show();
    }
}

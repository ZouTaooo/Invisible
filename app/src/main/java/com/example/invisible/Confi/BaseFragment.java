package com.example.invisible.Confi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.invisible.Activity.ChatActivity;
import com.example.invisible.Bean.AnotherPhone;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Factory.RetrofitFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BaseFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "BaseFragment";
    private ProgressDialog progressDialog;

    //获取SharedPreferences对象
    public SharedPreferences getPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences;
    }

    //获取缓存
    public String getS(String key) {
        return getPreference().getString(key, null);
    }


    public void match(final String role) {
        showProgressDialog();
        RetrofitFactory.getInstance().match("Token " + getS("token"), role)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Basebean<AnotherPhone>>() {
                    @Override
                    public void accept(Basebean<AnotherPhone> basebean) throws Exception {
                        Log.e(TAG, "accept: accept" + basebean.getMsg());
                        Log.e(TAG, "accept: accept" + basebean.getStatus());
                        if (basebean.getStatus() == 1) {
                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("other_mobile", basebean.getBody().getAnothermobile());
                            i.putExtra("name", basebean.getBody().getName());
                            i.putExtra("role", role);
                            startActivity(i);
                        } else {
                            Toast.makeText(getActivity(), "匹配失败", Toast.LENGTH_SHORT).show();
                        }
                        progressDialogDismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "匹配失败", Toast.LENGTH_SHORT).show();
                        progressDialogDismiss();
                        Log.e(TAG, "accept: error error" + throwable.getMessage());
                    }
                });
    }

    private void progressDialogDismiss() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage("匹配ing...");
        progressDialog.setTitle("请稍等");
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage("取消中...");
                //TODO progressDialog dismiss
                break_match();
                progressDialogDismiss();
            }
        });
        progressDialog.show();
    }

    private void break_match() {
        RetrofitFactory.getInstance().break_match("Token " + getS("token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        Log.e(TAG, "accept: break_match" + basebean.getMsg());
                        //TODO depend on msg
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: break_match" + throwable.getMessage());
                    }
                });
    }
}

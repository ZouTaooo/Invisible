package com.example.invisible.Activity;

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
import android.widget.ListView;
import android.widget.Toast;

import com.example.invisible.Adapter.ChatListviewAdapter;
import com.example.invisible.Bean.Basebean;
import com.example.invisible.Bean.Message;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.Factory.RetrofitFactory;
import com.example.invisible.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private List<com.example.invisible.Bean.Message> messageList;

    private View mStatusBarView;

    private Toolbar mToolbar;
    //    private RecyclerView mChatRecyclerView;
    private ListView mListView;

    private EditText mContentEdit;
    /**
     * 发送
     */
    private Button mSend;

    private EMMessageListener msgListener;

    private static final String TAG = "ChatActivity";

//  private ChatRecyclerViewAdapter adapter;

    private ChatListviewAdapter adapter;

    private String user_phone;

    private String user_name;

    private String role;

    private boolean isLeave;

    private boolean isNeedEva;

    private int headPics[] = {R.drawable.head1_1, R.drawable.head1_2, R.drawable.head1_3,
            R.drawable.head1_4, R.drawable.head1_5, R.drawable.head1_6, R.drawable.head1_7, R.drawable.head1_8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        fullScreen(this);
        messageList = new ArrayList<>();
        isLeave = false;
        isNeedEva = false;
        Intent i = getIntent();
        user_phone = i.getStringExtra("other_mobile");
        user_name = i.getStringExtra("name");
        role = i.getStringExtra("role");
        switch (role) {
            case "chat": {
                role = "匿名聊天";
                isNeedEva = false;
                break;
            }
            case "teller": {
                role = "倾诉者";
                isNeedEva = true;
                break;
            }
            case "listener": {
                role = "聆听者";
                isNeedEva = true;
                break;
            }
            default:
                break;
        }
        initView();
        Log.e(TAG, "onCreate: 2");
        initEM();
    }

    private void initEM() {
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                Log.e(TAG, "onMessageReceived: 3");
                //收到消息
                for (EMMessage message : messages) {
                    Log.e(TAG, "onMessageReceived: " + message.toString());
                    Message msg = new Message();
                    String content = message.getBody().toString();
                    content = content.substring(5, content.length() - 1);
                    if (content.equals("///thekeyofexit///")) {
                        Log.e(TAG, "onMessageReceived: 出现");
                        isLeave = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "run: T another people leave");
                                Toast.makeText(ChatActivity.this, "对方已离开聊天", Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (isNeedEva) {
                            Log.e(TAG, "onMessageReceived: showdialog");
                            showEvaluteDialog();
                        } else {
                            Log.e(TAG, "onMessageReceived: finish");
                            finish();
                        }
                    } else {
                        msg.setContent(content);
                        msg.setLayout_flag(0);
                        msg.setTime(message.localTime() + "");
                        Log.e(TAG, "onMessageReceived: severTime" + message.getMsgTime());
                        Log.e(TAG, "onMessageReceived: localTime" + message.localTime());
                        messageList.add(msg);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run: " + messageList.size());
                        refreshListView();
                    }
                });
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void initView() {
//        mStatusBarView = (View) findViewById(R.id.statusBarView);
//        setUpStatusBar(mStatusBarView, "#242C3B");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar(mToolbar, role, true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNeedEva) {
                    showEvaluteDialog();
                } else {
                    tellOtherLeave();
                    finish();
                }
            }
        });
//        setUpRecycler();
        mListView = findViewById(R.id.chat_listView);

        adapter = new ChatListviewAdapter(this, R.layout.layout_chat_recyclerview_item, messageList, new Random().nextInt(8), new Random().nextInt(8));
        mListView.setAdapter(adapter);
        mContentEdit = (EditText) findViewById(R.id.content_edit);
        mSend = (Button) findViewById(R.id.send);
        mSend.setOnClickListener(this);
    }

    private void showEvaluteDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("Invisible")
                        .setMessage("离开前给您的聊天对象一个评价吧！")
                        .setCancelable(false)
                        .setPositiveButton("好评", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exit(dialog, "1");
                            }
                        })
                        .setNegativeButton("差评", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exit(dialog, "0");
                            }
                        });
                dialog.show();
            }
        });
    }

    private void exit(DialogInterface dialog, String add) {
        dialog.dismiss();
        tellOtherLeave();
        evalute(add);
        finish();
    }

    private void tellOtherLeave() {
        if (!isLeave) {
            Log.e(TAG, "exit: send");
            sendMessage("///thekeyofexit///");
        }
    }

    private void evalute(String add) {
        RetrofitFactory.getInstance().evalute("Token " + getS("token"), add, user_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Basebean>() {
                    @Override
                    public void accept(Basebean basebean) throws Exception {
                        if (basebean.getStatus() == 1) {
                            T("已收到您的评价");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: evalute error" + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (isNeedEva) {
            showEvaluteDialog();
        } else {
            tellOtherLeave();
            finish();
        }
    }

    //    private void setUpRecycler() {
//        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recyclerView);
//        adapter = new ChatRecyclerViewAdapter(ChatActivity.this, messageList);
//        adapter.setHasStableIds(true);
//        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mChatRecyclerView.setAdapter(adapter);
//        mChatRecyclerView.setItemAnimator(null);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.send:
                if (TextUtils.isEmpty(mContentEdit.getText().toString())) {
                    T("输入不能为空...");
                } else {
                    sendMessage(mContentEdit.getText().toString());
                    Message msg = new Message();
                    msg.setContent(mContentEdit.getText().toString());
                    msg.setLayout_flag(1);
                    msg.setTime("time");
                    messageList.add(msg);
                    Log.e(TAG, "onClick: " + messageList.size());
                    refreshListView();
                    mContentEdit.setText("");
                }
                break;
        }
    }

    private void sendMessage(String msg) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(msg, user_phone);
//如果是群聊，设置chattype，默认是单聊
//                if (chatType == CHATTYPE_GROUP)
//                    message.setChatType(ChatType.GroupChat);
//发送消息
        Log.e(TAG, "onClick: send");
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private void refreshListView() {
        adapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getBottom());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}

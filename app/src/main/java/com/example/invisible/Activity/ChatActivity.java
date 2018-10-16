package com.example.invisible.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.invisible.Adapter.ChatListviewAdapter;
import com.example.invisible.Adapter.ChatRecyclerViewAdapter;
import com.example.invisible.Bean.Message;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

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

//    private ChatRecyclerViewAdapter adapter;

    private ChatListviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fullScreen(this);
        messageList = new ArrayList<>();
        initView();
        Log.e(TAG, "onCreate: 2");
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                Log.e(TAG, "onMessageReceived: 3");
                //收到消息
                for (EMMessage message : messages) {
                    Log.e(TAG, "onMessageReceived: " + message.toString());
                    Message msg = new Message();
                    String content = message.getBody().toString();
                    content.substring(4, content.length() - 1);
                    msg.setContent(content);
                    msg.setLayout_flag(0);
                    msg.setTime(message.localTime() + "");
                    Log.e(TAG, "onMessageReceived: severTime" + message.getMsgTime());
                    Log.e(TAG, "onMessageReceived: localTime" + message.localTime());
                    messageList.add(msg);
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
        mStatusBarView = (View) findViewById(R.id.statusBarView);
        setUpStatusBar(mStatusBarView, "#242C3B");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar(mToolbar, "", true);
//        setUpRecycler();
        mListView = findViewById(R.id.chat_listView);
         adapter = new ChatListviewAdapter(this, R.layout.layout_chat_recyclerview_item, messageList);
        mListView.setAdapter(adapter);
        mContentEdit = (EditText) findViewById(R.id.content_edit);
        mSend = (Button) findViewById(R.id.send);
        mSend.setOnClickListener(this);
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
                String user;
                if (getS("user").equals("17711356411")) {
                    Log.e(TAG, "onClick: to 13888888888");
                    user = "13888888888";
                } else {
                    Log.e(TAG, "onClick: to 17711356411");
                    user = "17711356411";
                }
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(mContentEdit.getText().toString(), user);
//如果是群聊，设置chattype，默认是单聊
//                if (chatType == CHATTYPE_GROUP)
//                    message.setChatType(ChatType.GroupChat);
//发送消息
                Log.e(TAG, "onClick: send");
                EMClient.getInstance().chatManager().sendMessage(message);
                Message msg = new Message();
                msg.setContent(mContentEdit.getText().toString());
                msg.setLayout_flag(1);
                msg.setTime("time");
                messageList.add(msg);
                Log.e(TAG, "onClick: " + messageList.size());
                refreshListView();
                mContentEdit.setText("");
                break;
        }
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

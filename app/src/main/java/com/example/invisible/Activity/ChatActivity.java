package com.example.invisible.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.invisible.Adapter.ChatRecyclerViewAdapter;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.R;

import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private List<com.example.invisible.Bean.Message> messageList;
    private View mStatusBarView;
    private Toolbar mToolbar;
    private RecyclerView mChatRecyclerView;
    private EditText mContentEdit;
    /**
     * 发送
     */
    private Button mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fullScreen(this);
        initView();
    }

    private void initView() {
        mStatusBarView = (View) findViewById(R.id.statusBarView);
        setUpStatusBar(mStatusBarView, "#242C3B");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar(mToolbar, "", true);
        setUpRecycler();
        mContentEdit = (EditText) findViewById(R.id.content_edit);
        mSend = (Button) findViewById(R.id.send);
        mSend.setOnClickListener(this);
    }

    private void setUpRecycler() {
        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recyclerView);
        ChatRecyclerViewAdapter adapter = new ChatRecyclerViewAdapter(ChatActivity.this, messageList);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.send:
                break;
        }
    }
}

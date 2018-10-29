package com.example.invisible.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.invisible.Activity.ChatActivity;
import com.example.invisible.Confi.BaseFragment;
import com.example.invisible.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ChatFragment";
    private Button mChat;

    public ChatFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        mChat = (Button) view.findViewById(R.id.chat);
        mChat.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.chat:
                Log.e(TAG, "onClick: 1");
                match("chat");
                break;
        }
    }
}

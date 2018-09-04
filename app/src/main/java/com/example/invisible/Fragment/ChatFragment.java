package com.example.invisible.Fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.invisible.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private LinearLayout mChat;
    private LinearLayout mTalk;
    private LinearLayout mListen;

    public ChatFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mChat = view.findViewById(R.id.chat);
        mChat.setBackgroundColor(Color.parseColor("#4DD0E1"));
        mTalk = view.findViewById(R.id.talk);
        mTalk.setBackgroundColor(Color.parseColor("#AAAAAA"));
        mListen = view.findViewById(R.id.listen);
        mListen.setBackgroundColor(Color.parseColor("#02D7F2"));
        return view;
    }

}

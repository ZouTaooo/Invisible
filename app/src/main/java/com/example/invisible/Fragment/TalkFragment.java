package com.example.invisible.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class TalkFragment extends BaseFragment implements View.OnClickListener {

    private Button mTalk;

    public TalkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_talk, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTalk = view.findViewById(R.id.talk);
        mTalk.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.talk:
                match("teller");
        }
    }
}

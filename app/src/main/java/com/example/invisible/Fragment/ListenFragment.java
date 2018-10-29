package com.example.invisible.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.invisible.Confi.BaseFragment;
import com.example.invisible.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListenFragment extends BaseFragment {


    private Button mListen;

    public ListenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_listen, container, false);
        mListen = view.findViewById(R.id.listen);
        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                match("listener");
            }
        });
        return view;
    }

}

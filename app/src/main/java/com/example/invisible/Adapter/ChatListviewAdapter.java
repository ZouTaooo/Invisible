package com.example.invisible.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.invisible.Bean.Message;
import com.example.invisible.R;

import java.util.List;

public class ChatListviewAdapter extends ArrayAdapter<Message> {


    private int resourceId;

    public ChatListviewAdapter(Context context, int resourceId,
                               List<Message> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        EditText leftContent = view.findViewById(R.id.left_content);
        EditText rightContent = view.findViewById(R.id.right_content);

        if (message.getLayout_flag() == 0) {
            rightContent.setVisibility(View.GONE);
            leftContent.setText(message.getContent());
        } else {
            leftContent.setVisibility(View.GONE);
            rightContent.setText(message.getContent());
        }

        return view;
    }
}

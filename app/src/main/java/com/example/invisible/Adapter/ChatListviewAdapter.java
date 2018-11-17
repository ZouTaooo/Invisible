package com.example.invisible.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.invisible.Bean.Message;
import com.example.invisible.R;

import java.util.List;

public class ChatListviewAdapter extends ArrayAdapter<Message> {

    private int headPics[] = {R.drawable.head1_1, R.drawable.head1_2, R.drawable.head1_3,
            R.drawable.head1_4, R.drawable.head1_5, R.drawable.head1_6, R.drawable.head1_7, R.drawable.head1_8};

    private int resourceId;

    private int leftImgNum;

    private int rightImgNum;

    public ChatListviewAdapter(Context context, int resourceId,
                               List<Message> objects, int leftImg, int rightImg) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        this.leftImgNum = leftImg;
        this.rightImgNum = rightImg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        LinearLayout leftLayout = view.findViewById(R.id.left_layout);
        LinearLayout rightLayout = view.findViewById(R.id.right_layout);
        ImageView leftImg = view.findViewById(R.id.left_head);
        ImageView rightImg = view.findViewById(R.id.right_head);
        Glide.with(getContext()).load(headPics[leftImgNum]).into(leftImg);
        Glide.with(getContext()).load(headPics[rightImgNum]).into(rightImg);
        EditText leftContent = view.findViewById(R.id.left_content);
        EditText rightContent = view.findViewById(R.id.right_content);

        if (message.getLayout_flag() == 0) {
            rightLayout.setVisibility(View.GONE);
            leftContent.setText(message.getContent());
        } else {
            leftLayout.setVisibility(View.GONE);
            rightContent.setText(message.getContent());
        }
        return view;
    }
}

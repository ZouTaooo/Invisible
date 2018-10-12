package com.example.invisible.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.invisible.Bean.Message;
import com.example.invisible.R;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Message> messageList;

    public ChatRecyclerViewAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.getLayout_flag() == 0) {
            holder.rightTime.setVisibility(View.GONE);
            holder.rightContent.setVisibility(View.GONE);
            holder.leftContent.setText(message.getContent());
            holder.leftTime.setText(message.getTime());
        } else {
            holder.leftTime.setVisibility(View.GONE);
            holder.leftContent.setVisibility(View.GONE);
            holder.rightContent.setText(message.getContent());
            holder.rightTime.setText(message.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView leftContent;
        private TextView leftTime;
        private TextView rightContent;
        private TextView rightTime;

        public ViewHolder(View itemView) {
            super(itemView);
            leftContent = itemView.findViewById(R.id.left_content);
            leftTime = itemView.findViewById(R.id.left_time);
            rightContent = itemView.findViewById(R.id.right_content);
            rightTime = itemView.findViewById(R.id.right_time);
        }
    }

}

package com.example.invisible.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.invisible.Bean.HistoryBottles;
import com.example.invisible.R;

import java.util.List;

public class HistoryBottlesRecyclerViewAdapter extends RecyclerView.Adapter<HistoryBottlesRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<HistoryBottles.HistoryItem> historyItems;

    public HistoryBottlesRecyclerViewAdapter(Context context, List<HistoryBottles.HistoryItem> historyItems) {
        this.context = context;
        this.historyItems = historyItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_history_bottle_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText("内容: " + historyItems.get(position).getPre_content());
        holder.reUser.setText("回复者: " + historyItems.get(position).getRe_user());
        holder.reContent.setText("回复: " + historyItems.get(position).getRe_content());
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView reContent;
        TextView reUser;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            reContent = itemView.findViewById(R.id.reply_content);
            reUser = itemView.findViewById(R.id.re_user);
        }
    }
}

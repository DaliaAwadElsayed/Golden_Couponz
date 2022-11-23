package com.goldencouponz.room;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<SavedData> taskList;

    public TasksAdapter(Context mCtx, List<SavedData> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.old_search_item, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        SavedData t = taskList.get(position);
        holder.textId.setText(t.getWord());
        holder.id.setText(t.getStoreId()+"");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {

        TextView textId, id;

        public TasksViewHolder(View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.textId);
            id = itemView.findViewById(R.id.storeId);

        }

    }
}
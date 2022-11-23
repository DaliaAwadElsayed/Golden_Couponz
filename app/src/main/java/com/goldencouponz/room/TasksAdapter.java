package com.goldencouponz.room;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.goldencouponz.viewModles.aboutgolden.SearchViewModel;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<SavedData> taskList;
    SearchViewModel.Listener listener;

    public TasksAdapter(Context mCtx, List<SavedData> taskList, SearchViewModel.Listener listener) {
        this.mCtx = mCtx;
        this.taskList = taskList;
        this.listener = listener;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.old_search_item, parent, false);
        return new TasksViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        SavedData t = taskList.get(position);
        holder.textId.setText(t.getWord());
        holder.id.setText(t.getStoreId() + "");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {
        SearchViewModel.Listener listener;
        TextView textId, id;

        public TasksViewHolder(View itemView, SearchViewModel.Listener listener) {
            super(itemView);
            this.listener=listener;
            textId = itemView.findViewById(R.id.textId);
            id = itemView.findViewById(R.id.storeId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskList.get(getAdapterPosition()).getType().equals("store")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("storeId", taskList.get(getAdapterPosition()).getStoreId());
                        bundle.putString("type", "no");
                        Navigation.findNavController(v).navigate(R.id.storeDetailsFragment, bundle);
                    } else {
                        listener.clickProduct(1, getAdapterPosition(), String.valueOf(taskList.get(getAdapterPosition()).getStoreId()), "");
                    }
                }
            });
        }

    }
}
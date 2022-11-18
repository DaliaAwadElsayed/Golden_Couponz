package com.goldencouponz.adapters.home;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;

import java.util.ArrayList;

public class ChosenBrandAdapter extends RecyclerView.Adapter<ChosenBrandAdapter.ViewHolder> {
    private ArrayList<Integer> localIdsSet;
    private ArrayList<String> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.textId);
            imageView = view.findViewById(R.id.deleteId);

        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ChosenBrandAdapter(ArrayList<String> dataSet, ArrayList<Integer> idSet) {
        localDataSet = dataSet;
        localIdsSet = idSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.filter_store_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localDataSet.remove(position);
                localIdsSet.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}

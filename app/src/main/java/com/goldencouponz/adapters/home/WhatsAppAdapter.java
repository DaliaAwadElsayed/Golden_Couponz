package com.goldencouponz.adapters.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.WhatsappIdBinding;
import com.goldencouponz.models.wrapper.Message;
import com.goldencouponz.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppAdapter extends RecyclerView.Adapter<WhatsAppAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Message> store = new ArrayList<>();
    Context context;

    public WhatsAppAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(WhatsappIdBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<Message> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private WhatsappIdBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull WhatsappIdBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Message store) {
            storeGrideItemBinding.detailsId.setText(Utility.fixNullString(String.valueOf(store.getDetails())));
            storeGrideItemBinding.titleId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            Picasso.get().load(store.getIcon()).into(storeGrideItemBinding.iconId);
            storeGrideItemBinding.itemId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendNoActive(store.getDetails(), store.getPhone());
                }
            });

        }

        private void sendNoActive(String message, String number) {
            try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.whatsapp");
                String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + message;
                sendIntent.setData(Uri.parse(url));
                context.startActivity(sendIntent);

            } catch (Exception e) {
                Log.i("EXCEPTIONn", e.toString());
                Toast.makeText(context, "WhatsApp Not Install", Toast.LENGTH_SHORT).show();
            }


        }

    }
}


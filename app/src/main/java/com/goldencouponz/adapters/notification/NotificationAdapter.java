package com.goldencouponz.adapters.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.NotificationItemBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.appsetting.Notification;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Notification> store = new ArrayList<>();
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private NotificationAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View viewItem, String type, String id);

    }

    public void setOnItemClickListener(NotificationAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    public NotificationAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(NotificationItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<Notification> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private NotificationItemBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull NotificationItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Notification store) {
            if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
                storeGrideItemBinding.storeNameId.setText(Utility.fixNullString(String.valueOf(store.getData().getTitleAr())));
                storeGrideItemBinding.detailsId.setText(Utility.fixNullString(String.valueOf(store.getData().getDetailsAr())));
            } else {
                storeGrideItemBinding.storeNameId.setText(Utility.fixNullString(String.valueOf(store.getData().getTitleEn())));
                storeGrideItemBinding.detailsId.setText(Utility.fixNullString(String.valueOf(store.getData().getDetailsEn())));
            }
            if (store.getReadAt() != null) {
                storeGrideItemBinding.showId.setVisibility(View.GONE);
                storeGrideItemBinding.readLinearId.setVisibility(View.VISIBLE);
                storeGrideItemBinding.readId.setText(Utility.fixNullString(String.valueOf(store.getReadAt())));
            } else {
                storeGrideItemBinding.showId.setVisibility(View.VISIBLE);
                storeGrideItemBinding.readLinearId.setVisibility(View.GONE);
            }
            storeGrideItemBinding.notificationItemId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (store.getData().getGroupType().equals("coupon")) {
                        listener.onItemClick(storeGrideItemBinding.getRoot(), "coupon", store.getData().getId());
                    } else {
                        listener.onItemClick(storeGrideItemBinding.getRoot(), "product", store.getData().getId());
                    }
                    if (store.getReadAt() == null) {
                        readNotification(getAdapterPosition());
                    }
                }
            });


        }

        private void readNotification(int position) {
            apiInterface.readNotification("Bearer" + GoldenSharedPreference.getToken(context), store.get(position).getId()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200 && response.body() != null) {
                            storeGrideItemBinding.showId.setVisibility(View.GONE);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
        }



    }
}


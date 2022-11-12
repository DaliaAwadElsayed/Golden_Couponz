package com.goldencouponz.viewModles.favourite;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.CopyCouponDialogBinding;
import com.e.goldencouponz.databinding.FragmentFavouriteBinding;
import com.e.goldencouponz.databinding.SecondCopyCouponDialogBinding;
import com.goldencouponz.adapters.favourite.CopounzFavAdapter;
import com.goldencouponz.adapters.favourite.StoresFavAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteViewModel extends ViewModel {
    FragmentFavouriteBinding fragmentFavouriteBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CopounzFavAdapter copounzFavAdapter;
    StoresFavAdapter storesFavAdapter;
    BottomSheetDialog showCopyCouponDialog;
    BottomSheetDialog showSecondCopyCouponDialog;
    CopyCouponDialogBinding copyCouponDialogBinding;
    SecondCopyCouponDialogBinding secondCopyCouponDialogBinding;
    String youTube = "https://www.youtube.com/@goldencouponz";
    String snapChat = "https://www.snapchat.com/add/goldencouponz?share_id=9XqCMUv88XE&locale=ar-EG*/";
    String instagram = "https://www.instagram.com/golden.couponz/";
    String facebook = "https://www.facebook.com/goldencouponzz/";
    private ClipboardManager myClipboard;
    private ClipData myClip;

    public void init(FragmentFavouriteBinding fragmentFavouriteBinding, CopyCouponDialogBinding copyCouponDialogBinding,
                     SecondCopyCouponDialogBinding secondCopyCouponDialogBinding, Context context) {
        this.context = context;
        this.fragmentFavouriteBinding = fragmentFavouriteBinding;
        this.secondCopyCouponDialogBinding = secondCopyCouponDialogBinding;
        this.copyCouponDialogBinding = copyCouponDialogBinding;
        showCopyCouponDialog = new BottomSheetDialog(context);
        showSecondCopyCouponDialog = new BottomSheetDialog(context);
        tabLayout();
        if (GoldenSharedPreference.isLoggedIn(context)) {
            getFavouriteCopounz("Bearer" + GoldenSharedPreference.getToken(context),
                    GoldenNoLoginSharedPreference.getUserLanguage(context));
        } else {
            pleaseLogin();
        }

    }

    private void tabLayout() {
        fragmentFavouriteBinding.tabLayoutId.addTab(fragmentFavouriteBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.coupons_label)));
        fragmentFavouriteBinding.tabLayoutId.addTab(fragmentFavouriteBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.stores_label)));
        fragmentFavouriteBinding.tabLayoutId.setTabGravity(TabLayout.GRAVITY_FILL);
        fragmentFavouriteBinding.tabLayoutId.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        getFavouriteCopounz("Bearer" + GoldenSharedPreference.getToken(context),
                                GoldenNoLoginSharedPreference.getUserLanguage(context));
                    } else {
                        pleaseLogin();
                    }
                } else if (tab.getPosition() == 1) {
                    fragmentFavouriteBinding.storesLinearId.setVisibility(View.VISIBLE);
                    fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        getFavouriteStores("Bearer" + GoldenSharedPreference.getToken(context),
                                GoldenNoLoginSharedPreference.getUserLanguage(context));
                    } else {
                        pleaseLogin();
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    Listener listener = new Listener() {
        @Override
        public void click(int click, int position) {
            Log.i("CLICKS", click + "?");
            if (click == 3) {
                getFavouriteCopounz("Bearer" + GoldenSharedPreference.getToken(context),
                        GoldenNoLoginSharedPreference.getUserLanguage(context));
            }
            if (click == 1) {
                getFavouriteStores("Bearer" + GoldenSharedPreference.getToken(context),
                        GoldenNoLoginSharedPreference.getUserLanguage(context));
            }
            // 5 for copy coupon
            if (click == 5) {
                getSingleCoupon("Bearer" + GoldenSharedPreference.getToken(context),
                        GoldenNoLoginSharedPreference.getUserLanguage(context), position);
            }


        }

        @Override
        public void clickShare(String coupon, String store, String url) {
            if (coupon.equals("no")) {
                shareOfferVia(store, url);
            } else {
                shareVia(coupon, store, url);
            }
        }

        @Override
        public void click(int i) {

        }
    };

    private void shareOfferVia(String store, String url) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = store
                + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
    }

    public interface Listener {
        void click(int click, int position);

        void clickShare(String coupon, String store, String url);

        void click(int i);
    }

    private void getSingleCoupon(String token, String lang, int position) {
        showCopyCouponDialog();
        apiInterface.getFavouriteCopounz(token, lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getCoupons().isEmpty()) {
                            Picasso.get().load(response.body().getCoupons().get(position).getStore().getFile()).into(copyCouponDialogBinding.copyCouponImgId);
                            Picasso.get().load(response.body().getCoupons().get(position).getStore().getFile()).into(secondCopyCouponDialogBinding.copyCouponImg2Id);
                            copyCouponDialogBinding.couponValueId.setText(response.body().getCoupons().get(position).getCoupon());
                            secondCopyCouponDialogBinding.couponValue2Id.setText(response.body().getCoupons().get(position).getCoupon());
                            copyCouponDialogBinding.copyId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showSecondCopyCouponDialog();
                                }
                            });
                            secondCopyCouponDialogBinding.copy2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    copyFun(response.body().getCoupons().get(position).getId(), response.body().getCoupons().get(position).getStore().getStoreLink());
                                }
                            });
                            if (response.body().getCoupons().get(position).getVideoFile() == null)
                            {
                                copyCouponDialogBinding.goToYoutubeId.setVisibility(View.GONE);
                                secondCopyCouponDialogBinding.goToYoutube2Id.setVisibility(View.GONE);

                            }
                            else {
                                copyCouponDialogBinding.goToYoutubeId.setVisibility(View.VISIBLE);
                                secondCopyCouponDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                            }
                            copyCouponDialogBinding.goToYoutubeId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getCoupons().get(position).getVideoFile());
                                }
                            });
                            secondCopyCouponDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getCoupons().get(position).getVideoFile());
                                }
                            });

                            secondCopyCouponDialogBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("WHATSAPP", "?" + response.body().getCoupons().get(position).getWhatsapp());
                                    sendNoActive(secondCopyCouponDialogBinding.couponValue2Id.getText().toString(),
                                            response.body().getCoupons().get(position).getStore().getTitle(), response.body().getCoupons().get(position).getWhatsapp()
                                    );
                                }
                            });
                            secondCopyCouponDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    shareVia(response.body().getCoupons().get(position).getCoupon(),
                                            response.body().getCoupons().get(position).getTitle(), response.body().getCoupons().get(position).getCouponLink());
                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendNoActive(String code, String store, String phone) {
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + context.getResources().getString(R.string.this_coupon) + "\"" + code + "\"" +
                    context.getResources().getString(R.string.not_vaild) + "\"" + store + "\"";
            sendIntent.setData(Uri.parse(url));
            context.startActivity(sendIntent);

        } catch (Exception e) {
            Log.i("EXCEPTIONn", e.toString());
            Toast.makeText(context, "WhatsApp Not Install", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareVia(String coupon, String store, String url) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getResources().getString(R.string.share_msg_part1) + "\"" + coupon + "\"" +
                context.getResources().getString(R.string.share_msg_part2) + "\"" + store +
                "\"" + context.getResources().getString(R.string.click_link) + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
    }

    private void copyFun(int coupon, String url) {
        apiInterface.copyCoupon(coupon).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                        String text;
                        text = secondCopyCouponDialogBinding.couponValue2Id.getText().toString();
                        myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(context, Html.fromHtml("<font><b>" + context.getResources().getString(R.string.code_copy) + "</b></font>"), Toast.LENGTH_SHORT).show();
                        openUrl(url);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void getFavouriteCopounz(String token, String lang) {
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.VISIBLE);
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getFavouriteCopounz(token, lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getCoupons().isEmpty()) {
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                            copounzFavAdapter = new CopounzFavAdapter(context, listener);
                            copounzFavAdapter.setCoupons(response.body().getCoupons());
                            fragmentFavouriteBinding.couponzRecyclerId.setAdapter(copounzFavAdapter);
                        } else {
                            noFavCopounzYet(context.getResources().getString(R.string.nothing_coupons_added));
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFavouriteStores(String token, String lang) {
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.VISIBLE);
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getFavouriteStores(token, lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getStores().isEmpty()) {
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                            storesFavAdapter = new StoresFavAdapter(context, listener);
                            storesFavAdapter.setStores(response.body().getStores());
                            fragmentFavouriteBinding.storesRecyclerId.setAdapter(storesFavAdapter);
                        } else {
                            noFavCopounzYet(context.getResources().getString(R.string.nothing_offers_added));
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSecondCopyCouponDialog() {
        final View view3 = secondCopyCouponDialogBinding.getRoot();
        showSecondCopyCouponDialog.setContentView(view3);
        showSecondCopyCouponDialog.setCancelable(true);
        showSecondCopyCouponDialog.show();
        secondCopyCouponDialogBinding.cancelCopyCoupon2Id.setOnClickListener(v1 -> showSecondCopyCouponDialog.cancel());
        secondCopyCouponDialogBinding.yesActiveId.setOnClickListener(v2 -> rateDialog());
        social2();
    }

    private void social() {
        copyCouponDialogBinding.fbId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        copyCouponDialogBinding.snapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        copyCouponDialogBinding.instagramId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        copyCouponDialogBinding.youtubeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });
    }

    private void social2() {
        secondCopyCouponDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        secondCopyCouponDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        secondCopyCouponDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        secondCopyCouponDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

    }

    private void openUrl(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void rateDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.rate_dialog);
        TextView yes, no;
        yes = dialog.findViewById(R.id.yesId);
        no = dialog.findViewById(R.id.noId);
        dialog.show();
        yes.setOnClickListener(v -> supportUs());
        no.setOnClickListener(v -> dialog.dismiss());
    }

    private void showCopyCouponDialog() {
        final View view1 = copyCouponDialogBinding.getRoot();
        showCopyCouponDialog.setContentView(view1);
        showCopyCouponDialog.setCancelable(true);
        showCopyCouponDialog.show();
        copyCouponDialogBinding.cancelCopyCouponId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showCopyCouponDialog.dismiss();

            }
        });
        social();
    }

    private void supportUs() {
        //https://play.google.com/store/apps/details?id=com.codesroots.goldencoupon
        Uri uri = Uri.parse("market://details?id=" + "com.codesroots.goldencoupon");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void noFavCopounzYet(String text) {
        fragmentFavouriteBinding.noLoginLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.noCopounzLinearId.setVisibility(View.VISIBLE);
        fragmentFavouriteBinding.couponOrStore.setText(text);
    }

    private void pleaseLogin() {
        fragmentFavouriteBinding.noLoginLinearId.setVisibility(View.VISIBLE);
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.noCopounzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.signInId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.loginFragment);
            }
        });
    }


}
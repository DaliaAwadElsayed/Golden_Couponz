package com.goldencouponz.viewModles.aboutgolden;

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
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.NotificationFragmentBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.SecondCopyCouponDialogBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.goldencouponz.adapters.notification.NotificationAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {
    NotificationFragmentBinding notificationFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    NotificationAdapter notificationAdapter;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    CopyCouponDialogBinding copyCouponDialogBinding;
    SecondCopyCouponDialogBinding secondCopyCouponDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;
    BottomSheetDialog showCopyCouponDialog;
    BottomSheetDialog showProductDetailsDialog;
    BottomSheetDialog secondShowProductDetailsDialog;
    BottomSheetDialog showSecondCopyCouponDialog;
    BottomSheetDialog showNoCouponDialog;
    String youTube = "https://www.youtube.com/@goldencouponz";
    String snapChat = "https://www.snapchat.com/add/goldencouponz?share_id=9XqCMUv88XE&locale=ar-EG*/";
    String instagram = "https://www.instagram.com/golden.couponz/";
    String facebook = "https://www.facebook.com/goldencouponzz/";

    public void init(NotificationFragmentBinding notificationFragmentBinding, CopyCouponDialogBinding copyCouponDialogBinding,
                     SecondCopyCouponDialogBinding secondCopyCouponDialogBinding,
                     ProductDetailsDialogBinding productDetailsDialogBinding,
                     SecondProductDetailsDialogBinding secondProductDetailsDialogBinding,
                     NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding, Context context) {
        this.context = context;
        this.notificationFragmentBinding = notificationFragmentBinding;
        this.copyCouponDialogBinding = copyCouponDialogBinding;
        this.secondCopyCouponDialogBinding = secondCopyCouponDialogBinding;
        this.secondProductDetailsDialogBinding = secondProductDetailsDialogBinding;
        this.productDetailsDialogBinding = productDetailsDialogBinding;
        this.noCouponProductDetailsDialogBinding = noCouponProductDetailsDialogBinding;
        showCopyCouponDialog = new BottomSheetDialog(context);
        showSecondCopyCouponDialog = new BottomSheetDialog(context);
        showNoCouponDialog = new BottomSheetDialog(context);
        secondShowProductDetailsDialog = new BottomSheetDialog(context);
        showProductDetailsDialog = new BottomSheetDialog(context);
        notificationFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.homeFragment);
            }
        });
        notification();
    }

    private void notification() {
        notificationFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.userGetNotification("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getNotifications().isEmpty()) {
                            notificationFragmentBinding.progress.setVisibility(View.GONE);
                            notificationAdapter = new NotificationAdapter(context);
                            notificationFragmentBinding.storesLinearId.setVisibility(View.VISIBLE);
                            notificationAdapter.setStores(response.body().getNotifications());
                            notificationFragmentBinding.noNotificationId.setVisibility(View.GONE);
                            notificationFragmentBinding.storesRecyclerId.setAdapter(notificationAdapter);
                            notificationAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, String type, String id) {
                                    if (type.equals("coupon")) {
                                        //getSingleCoupon(GoldenSharedPreference.getToken(context),GoldenNoLoginSharedPreference.getUserLanguage(context), Integer.parseInt(id));
                                    }
                                }
                            });
                        } else {
                            notificationFragmentBinding.storesLinearId.setVisibility(View.GONE);
                            notificationFragmentBinding.progress.setVisibility(View.GONE);
                            notificationFragmentBinding.noNotificationId.setVisibility(View.VISIBLE);

                        }
                    } else {
                        notificationFragmentBinding.storesRecyclerId.setVisibility(View.GONE);
                        notificationFragmentBinding.noNotificationId.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                        notificationFragmentBinding.progress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                notificationFragmentBinding.progress.setVisibility(View.GONE);
                notificationFragmentBinding.storesRecyclerId.setVisibility(View.GONE);
                notificationFragmentBinding.noNotificationId.setVisibility(View.GONE);

            }
        });
    }

    private void showProductDetailsDialog() {
        final View view4 = productDetailsDialogBinding.getRoot();
        showProductDetailsDialog.setContentView(view4);
        showProductDetailsDialog.setCancelable(true);
        showProductDetailsDialog.show();
        productDetailsDialogBinding.cancelLogId.setOnClickListener(v1 -> showProductDetailsDialog.cancel());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            productDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            productDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social3();

    }

    private void showSecondProductDetailsDialog() {
        final View view5 = secondProductDetailsDialogBinding.getRoot();
        secondShowProductDetailsDialog.setContentView(view5);
        secondShowProductDetailsDialog.setCancelable(true);
        secondShowProductDetailsDialog.show();
        secondProductDetailsDialogBinding.cancelLogId.setOnClickListener(v8 -> secondShowProductDetailsDialog.cancel());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social4();
        secondProductDetailsDialogBinding.yesActiveId.setOnClickListener(v7 -> rateDialog());

    }

    private void showNoCouponProductDetailsDialog() {
        final View view5 = noCouponProductDetailsDialogBinding.getRoot();
        showNoCouponDialog.setContentView(view5);
        showNoCouponDialog.setCancelable(true);
        showNoCouponDialog.show();
        noCouponProductDetailsDialogBinding.cancelLogId.setOnClickListener(v8 -> showNoCouponDialog.cancel());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            noCouponProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            noCouponProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social5();
        noCouponProductDetailsDialogBinding.rateNwId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportUs();
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

    private void social3() {
        productDetailsDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        productDetailsDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        productDetailsDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        productDetailsDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

    }

    private void social5() {
        noCouponProductDetailsDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        noCouponProductDetailsDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        noCouponProductDetailsDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        noCouponProductDetailsDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

    }

    private void social4() {
        secondProductDetailsDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        secondProductDetailsDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        secondProductDetailsDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        secondProductDetailsDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

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

    private void openUrl(String url) {
        Log.i("URL", "?" + url);
        if (url != null) {
            if (url.contains("http://") || url.contains("https://")) {
                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Wrong Url", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Missing Url", Toast.LENGTH_SHORT).show();
        }
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

    private void shareOfferVia(String store, String url) {
        Intent sharingIntent = new Intent(Intent.ACTION_SENDTO);
        sharingIntent.setType("text/plain");
        String shareBody = store
                + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
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
 /*   private void getSingleCoupons(String token, int position) {
        showCopyCouponDialog();
        String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
        String fcmToken = "";
        int countryId = GoldenNoLoginSharedPreference.getUserCountryId(context);
        int id = storeId;
        Log.i("COUNTRYID", countryId + "?");
        apiInterface.getSingleStore(token, lang, fcmToken, countryId, String.valueOf(id)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getStore().getStore_coupons().isEmpty()) {
                            CopounzAdapter categoriesAdapter = new CopounzAdapter(context, listener);
                            categoriesAdapter.setStores(response.body().getStore().getStore_coupons());
                            storeDetailsFragmentBinding.recyclerId.setAdapter(categoriesAdapter);
                            //coupon click
                            Picasso.get().load(response.body().getStore().getFile()).into(copyCouponDialogBinding.copyCouponImgId);
                            Picasso.get().load(response.body().getStore().getFile()).into(secondCopyCouponDialogBinding.copyCouponImg2Id);
                            copyCouponDialogBinding.couponValueId.setText(response.body().getStore().getStore_coupons().get(position).getCoupon());
                            secondCopyCouponDialogBinding.couponValue2Id.setText(response.body().getStore().getStore_coupons().get(position).getCoupon());
                            copyCouponDialogBinding.copyId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showSecondCopyCouponDialog();
                                }
                            });
                            secondCopyCouponDialogBinding.copy2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    copyFun(response.body().getStore().getStore_coupons().get(position).getId(), response.body().getStore().getStore_coupons().get(position).getCouponLink());
                                }
                            });
                            copyCouponDialogBinding.goToYoutubeId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getStore().getStore_coupons().get(position).getVideoFile());
                                }
                            });
                            secondCopyCouponDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getStore().getStore_coupons().get(position).getVideoFile());
                                }
                            });
                            secondCopyCouponDialogBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendNoActive(secondCopyCouponDialogBinding.couponValue2Id.getText().toString(),
                                            response.body().getStore().getTitle(),response.body().getStore().getStore_coupons().get(position).getWhatsapp()
                                    );
                                }
                            });
                            secondCopyCouponDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shareVia(response.body().getStore().getStore_coupons().get(position).getCoupon(),
                                            response.body().getStore().getTitle(), response.body().getStore().getStore_coupons().get(position).getCouponLink());
                                }
                            });

                        }

                    }
                } else {
                    storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getSingleProducts(String deviceToken, int country, String lang, String storeId, String catId, String subCatId,int position) {
        Log.i("POSITIONOUT",position+"?");
        showProductDetailsDialog();
        apiInterface.getStoreProducts(deviceToken, country, lang, storeId, catId, subCatId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getProducts().getData().isEmpty()) {
                            //product dialog
                            productDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getProducts().getData().get(position).getProductCountry().getDiscountPrice()));
                            productDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            productDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getProductCountry().getDiscount())));
                            productDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getProductCountry().getPrice())));
                            productDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getDetails())));
                            Picasso.get().load(response.body().getProducts().getData().get(position).getFile()).into(productDetailsDialogBinding.productId);
                            productDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                            productDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                            Picasso.get().load(response.body().getProducts().getData().get(position).getStore().getFile()).into(productDetailsDialogBinding.storeImgId);
                            productDetailsDialogBinding.couponValueId.setText(Utility.fixNullString(response.body().getProducts().getData().get(position).getCoupon()));
                            productDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shareVia(response.body().getProducts().getData().get(position).getCoupon(), response.body().getProducts().getData().get(position).getStore().getTitle()
                                            , response.body().getProducts().getData().get(position).getStore().getStoreLink());
                                }
                            });

                            //nocoupon//
                            noCouponProductDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getProducts().getData().get(position).getProductCountry().getDiscountPrice()));
                            noCouponProductDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            noCouponProductDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getProductCountry().getDiscount())));
                            noCouponProductDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getProductCountry().getPrice())));
                            noCouponProductDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getDetails())));
                            Picasso.get().load(response.body().getProducts().getData().get(position).getFile()).into(noCouponProductDetailsDialogBinding.productId);
                            noCouponProductDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                            noCouponProductDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                            Picasso.get().load(response.body().getProducts().getData().get(position).getStore().getFile()).into(noCouponProductDetailsDialogBinding.storeImgId);
                            noCouponProductDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shareOfferVia(response.body().getProducts().getData().get(position).getStore().getTitle()
                                            , response.body().getProducts().getData().get(position).getStore().getStoreLink());
                                }
                            });
                            noCouponProductDetailsDialogBinding.couponValue2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getProducts().getData().get(position).getProductLink());
                                    noCouponProductDetailsDialogBinding.recomendId.setVisibility(View.VISIBLE);
                                }
                            });


                            secondProductDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getProducts().getData().get(position).getProductCountry().getDiscountPrice()));
                            secondProductDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            secondProductDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getProductCountry().getDiscount())));
                            secondProductDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getProductCountry().getPrice())));
                            secondProductDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getProducts().getData().get(position).getDetails())));
                            Picasso.get().load(response.body().getProducts().getData().get(position).getFile()).into(secondProductDetailsDialogBinding.productId);
                            secondProductDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                            secondProductDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                            Picasso.get().load(response.body().getProducts().getData().get(position).getStore().getFile()).into(secondProductDetailsDialogBinding.storeImgId);
                            secondProductDetailsDialogBinding.couponValueId.setText(Utility.fixNullString(response.body().getProducts().getData().get(position).getCoupon()));
                            secondProductDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shareVia(response.body().getProducts().getData().get(position).getCoupon(), response.body().getProducts().getData().get(position).getStore().getTitle()
                                            , response.body().getProducts().getData().get(position).getStore().getStoreLink());
                                }
                            });
                            productDetailsDialogBinding.couponValue2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showSecondProductDetailsDialog();
                                }
                            });
                            secondProductDetailsDialogBinding.copy2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(context, R.string.code_copy, Toast.LENGTH_SHORT).show();
                                    myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                    String text;
                                    text = secondProductDetailsDialogBinding.couponValueId.getText().toString();
                                    myClip = ClipData.newPlainText("text", text);
                                    myClipboard.setPrimaryClip(myClip);
                                    openUrl(response.body().getProducts().getData().get(position).getProductLink());
                                }
                            });
                            productDetailsDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getProducts().getData().get(position).getVideoLink());
                                }
                            });
                            secondProductDetailsDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getProducts().getData().get(position).getVideoLink());
                                }
                            });
                            secondProductDetailsDialogBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendNoActive(secondProductDetailsDialogBinding.couponValueId.getText().toString(),
                                            response.body().getProducts().getData().get(position).getStore().getTitle(), response.body().getProducts().getData().get(position).getWhatsapp()
                                    );
                                }
                            });
                            ////////////////
                        } else {
                        }
                    } else {
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }
*/


}
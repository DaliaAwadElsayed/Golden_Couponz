package com.goldencouponz.viewModles.home;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ArrangeDialogBinding;
import com.e.goldencouponz.databinding.BrandListDialogBinding;
import com.e.goldencouponz.databinding.FiltterDialogBinding;
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductsFragmentBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.StoreListDialogBinding;
import com.goldencouponz.adapters.home.BrandAdapter;
import com.goldencouponz.adapters.home.ChosenBrandAdapter;
import com.goldencouponz.adapters.home.ProductCatAdapter;
import com.goldencouponz.adapters.home.StoresFilterAdapter;
import com.goldencouponz.adapters.stores.ProductAdapter;
import com.goldencouponz.adapters.stores.ProductsSubCategoriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends ViewModel {
    ProductsFragmentBinding productsFragmentBinding;
    Context context;
    BottomSheetDialog showProductDetailsDialog;
    BottomSheetDialog secondShowProductDetailsDialog;
    BottomSheetDialog showNoCouponDialog;
    BottomSheetDialog arrangeDialog;
    BottomSheetDialog brandListDialog;
    BottomSheetDialog filtterDialog;
    BottomSheetDialog storeListDialog;
    BrandListDialogBinding brandListDialogBinding;
    FiltterDialogBinding filtterDialogBinding;
    StoreListDialogBinding storeListDialogBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    ArrangeDialogBinding arrangeDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    String youTube = "https://www.youtube.com/@goldencouponz";
    String snapChat = "https://www.snapchat.com/add/goldencouponz?share_id=9XqCMUv88XE&locale=ar-EG*/";
    String instagram = "https://www.insshowNoCouponProductDetailsDialogtagram.com/golden.couponz/";
    String facebook = "https://www.facebook.com/goldencouponzz/";
    ProductCatAdapter categoriesAdapter;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    int position;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    private void openUrl(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void social() {
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

    private void social3() {
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

    private void social2() {
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

    public void init(ProductsFragmentBinding productsFragmentBinding, ProductDetailsDialogBinding productDetailsDialogBinding,
                     SecondProductDetailsDialogBinding secondProductDetailsDialogBinding,
                     NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding,
                     ArrangeDialogBinding arrangeDialogBinding,
                     BrandListDialogBinding brandListDialogBinding,
                     FiltterDialogBinding filtterDialogBinding,
                     StoreListDialogBinding storeListDialogBinding,
                     Context context) {
        this.context = context;
        this.arrangeDialogBinding = arrangeDialogBinding;
        this.productsFragmentBinding = productsFragmentBinding;
        this.productDetailsDialogBinding = productDetailsDialogBinding;
        this.filtterDialogBinding = filtterDialogBinding;
        this.brandListDialogBinding = brandListDialogBinding;
        this.storeListDialogBinding = storeListDialogBinding;
        this.secondProductDetailsDialogBinding = secondProductDetailsDialogBinding;
        this.noCouponProductDetailsDialogBinding = noCouponProductDetailsDialogBinding;
        secondShowProductDetailsDialog = new BottomSheetDialog(context);
        showProductDetailsDialog = new BottomSheetDialog(context);
        showNoCouponDialog = new BottomSheetDialog(context);
        storeListDialog = new BottomSheetDialog(context);
        brandListDialog = new BottomSheetDialog(context);
        filtterDialog = new BottomSheetDialog(context);
        arrangeDialog = new BottomSheetDialog(context);
        productsFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
        productsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
        productsFragmentBinding.allId.setBackgroundTintList(null);
        getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), "", "", "", "", "");
        categories(GoldenNoLoginSharedPreference.getUserLanguage(context));
        productsFragmentBinding.allId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsFragmentBinding.subCategoryRecyclerView.setVisibility(View.GONE);
                productsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                productsFragmentBinding.allId.setBackgroundTintList(null);
                getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), "", "", "", "", "");
                categories(GoldenNoLoginSharedPreference.getUserLanguage(context));

            }

        });
        productsFragmentBinding.arrangeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArrangeDialog();
            }
        });
        productsFragmentBinding.filtterId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFillterDialog();
            }
        });

    }

    private void showArrangeDialog() {
        final View view6 = arrangeDialogBinding.getRoot();
        arrangeDialog.setContentView(view6);
        arrangeDialog.setCancelable(true);
        arrangeDialog.show();
        arrangeDialogBinding.cancelProfileId.setOnClickListener(v8 -> arrangeDialog.cancel());
        arrangeDialogBinding.lowToHighId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                        "", "", "", "1", "");
                arrangeDialog.dismiss();
            }
        });
        arrangeDialogBinding.highToLowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                        "", "", "", "", "1");
                arrangeDialog.dismiss();
            }
        });
    }

    private void subCategory(int parentId, String lang) {
        productsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
        productsFragmentBinding.subCategoryRecyclerView.setVisibility(View.VISIBLE);
        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.w("TAG", token + "?");

                        apiInterface.getCategories(lang, token, parentId).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getCategories().isEmpty()) {
                                            productsFragmentBinding.progress.setVisibility(View.GONE);
                                            ProductsSubCategoriesAdapter categoriesAdapter = new ProductsSubCategoriesAdapter(context);
                                            productsFragmentBinding.subCategoryRecyclerView.setAdapter(categoriesAdapter);
                                            categoriesAdapter.setCategories(response.body().getCategories());
                                            categoriesAdapter.setOnItemClickListener(new ProductsSubCategoriesAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View viewItem, int position, int categoryId, int subCatId) {
                                                    getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                                                            "", String.valueOf(categoryId), String.valueOf(subCatId), "", "");
                                                }
                                            });
                                        } else {
                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                            productsFragmentBinding.progress.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                productsFragmentBinding.progress.setVisibility(View.GONE);

                            }
                        });
                    }
                });
    }

    private void getProducts(String id, int country, String lang, String storeId, String catId, String subCatId, String asc, String desc) {
        Log.i("POSITIONOUT", position + "?");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.w("TAG", token + "?");

                        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
                        apiInterface.getStoreProducts(id, token, country, lang, storeId, catId, subCatId, asc, desc).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        productsFragmentBinding.progress.setVisibility(View.GONE);
                                        if (!response.body().getProducts().getData().isEmpty()) {
                                            ProductAdapter storeProduct = new ProductAdapter(context, listener);
                                            storeProduct.setStores(response.body().getProducts().getData());
                                            productsFragmentBinding.idRVUsers.setAdapter(storeProduct);
                                            productsFragmentBinding.idRVUsers.setVisibility(View.VISIBLE);

                                        } else {
                                            productsFragmentBinding.idRVUsers.setVisibility(View.GONE);
                                            productsFragmentBinding.progress.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                        productsFragmentBinding.progress.setVisibility(View.GONE);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.i("onFailure", t.toString());
                                productsFragmentBinding.progress.setVisibility(View.GONE);
                                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void getSingleProducts(int country, String lang, String id) {
        Log.i("POSITIONOUT", position + "?");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.w("TAG", token + "?");

                        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
                        apiInterface.getStoreProducts(id, token, country, lang, "", "", "", "", "").enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        productsFragmentBinding.progress.setVisibility(View.GONE);

                                        //product dialog
                                        productDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getSingleProduct().getProductCountry().getDiscountPrice()) + "");
                                        productDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        productDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getDiscount())) + "");
                                        productDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getPrice())) + "");
                                        productDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getDetails())));
                                        Picasso.get().load(response.body().getSingleProduct().getFile()).into(productDetailsDialogBinding.productId);
                                        productDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                                        productDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                                        Picasso.get().load(response.body().getSingleProduct().getStore().getFile()).into(productDetailsDialogBinding.storeImgId);
                                        productDetailsDialogBinding.couponValueId.setText(Utility.fixNullString(response.body().getSingleProduct().getCoupon()));
                                        productDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                shareVia(response.body().getSingleProduct().getCoupon(), response.body().getSingleProduct().getStore().getTitle()
                                                        , response.body().getSingleProduct().getStore().getStoreLink());
                                            }
                                        });

                                        //nocoupon//
                                        noCouponProductDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getSingleProduct().getProductCountry().getDiscountPrice()));
                                        noCouponProductDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        noCouponProductDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getDiscount())));
                                        noCouponProductDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getPrice())));
                                        noCouponProductDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getDetails())));
                                        Picasso.get().load(response.body().getSingleProduct().getFile()).into(noCouponProductDetailsDialogBinding.productId);
                                        noCouponProductDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                                        noCouponProductDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                                        Picasso.get().load(response.body().getSingleProduct().getStore().getFile()).into(noCouponProductDetailsDialogBinding.storeImgId);
                                        noCouponProductDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                shareOfferVia(response.body().getSingleProduct().getStore().getTitle()
                                                        , response.body().getSingleProduct().getStore().getStoreLink());
                                            }
                                        });
                                        noCouponProductDetailsDialogBinding.couponValue2Id.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openUrl(response.body().getSingleProduct().getProductLink());
                                                noCouponProductDetailsDialogBinding.recomendId.setVisibility(View.VISIBLE);
                                            }
                                        });


                                        secondProductDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getSingleProduct().getProductCountry().getDiscountPrice()));
                                        secondProductDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        secondProductDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getDiscount())));
                                        secondProductDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getPrice())));
                                        secondProductDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getDetails())));
                                        Picasso.get().load(response.body().getSingleProduct().getFile()).into(secondProductDetailsDialogBinding.productId);
                                        secondProductDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                                        secondProductDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                                        Picasso.get().load(response.body().getSingleProduct().getStore().getFile()).into(secondProductDetailsDialogBinding.storeImgId);
                                        secondProductDetailsDialogBinding.couponValueId.setText(Utility.fixNullString(response.body().getSingleProduct().getCoupon()));
                                        secondProductDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                shareVia(response.body().getSingleProduct().getCoupon(), response.body().getSingleProduct().getStore().getTitle()
                                                        , response.body().getSingleProduct().getStore().getStoreLink());
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
                                                openUrl(response.body().getSingleProduct().getProductLink());
                                            }
                                        });
                                        if (response.body().getSingleProduct().getVideoLink() == null) {
                                            productDetailsDialogBinding.goToYoutube2Id.setVisibility(View.GONE);
                                            secondProductDetailsDialogBinding.goToYoutube2Id.setVisibility(View.GONE);

                                        } else {
                                            secondProductDetailsDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                                            productDetailsDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                                        }
                                        productDetailsDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openUrl(response.body().getSingleProduct().getVideoLink());
                                            }
                                        });
                                        secondProductDetailsDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openUrl(response.body().getSingleProduct().getVideoLink());
                                            }
                                        });
                                        secondProductDetailsDialogBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                sendNoActive(secondProductDetailsDialogBinding.couponValueId.getText().toString(),
                                                        response.body().getSingleProduct().getStore().getTitle(), response.body().getSingleProduct().getWhatsapp()
                                                );
                                            }
                                        });
                                        ////////////////
                                    } else {
                                        productsFragmentBinding.idRVUsers.setVisibility(View.GONE);
                                        productsFragmentBinding.progress.setVisibility(View.GONE);
                                    }
                                } else {
                                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                    productsFragmentBinding.progress.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.i("onFailure", t.toString());
                                productsFragmentBinding.progress.setVisibility(View.GONE);
                                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
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

    private void categories(String lang) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.w("TAG", token + "?");

                        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
                        apiInterface.getCategories(lang, token, 0).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getCategories().isEmpty()) {
                                            productsFragmentBinding.progress.setVisibility(View.GONE);
                                            categoriesAdapter = new ProductCatAdapter(context);
                                            productsFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
                                            categoriesAdapter.setCategories(response.body().getCategories());
                                            categoriesAdapter.setOnItemClickListener(new ProductCatAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View viewItem, int position, int categoryId) {
                                                    productsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
//                                    homeFragmentBinding.allId.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.category_bk)));
                                                    subCategory(categoryId, GoldenNoLoginSharedPreference.getUserLanguage(context));
                                                    getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                                                            "", String.valueOf(categoryId), "", "", "");

                                                }
                                            });

                                        } else {
                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                            productsFragmentBinding.progress.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.i("onFailure", t.toString());
                                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                                productsFragmentBinding.progress.setVisibility(View.GONE);
                            }
                        });
                    }

                });
    }

    private void storesList(String token, String lang) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String device = task.getResult();
                        Log.w("TAG", token + "?");
                        apiInterface.getStore(token, lang, device, 0).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getStores().isEmpty()) {
                                            StoresFilterAdapter storesListAdapter = new StoresFilterAdapter(context);
                                            storesListAdapter.setStores(response.body().getStores());
                                            storeListDialogBinding.tradeRecyclerView.setAdapter(storesListAdapter);
                                            storesListAdapter.setOnItemClickListener(new StoresFilterAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View viewItem, int position, int id, String store) {
                                                    filtterDialogBinding.storeLinearId.setVisibility(View.VISIBLE);
                                                    storeListDialog.dismiss();
                                                    filtterDialogBinding.textId.setText(store);
                                                    filtterDialogBinding.storeChosenId.setText(id + "");
                                                }
                                            });
                                        } else {
                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void brandList() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String device = task.getResult();
                        apiInterface.getBrands(GoldenNoLoginSharedPreference.getUserLanguage(context), device, GoldenNoLoginSharedPreference.getUserCountryId(context)).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getBrands().isEmpty()) {
                                            BrandAdapter storesListAdapter = new BrandAdapter(context);
                                            storesListAdapter.setStores(response.body().getBrands());
                                            brandListDialogBinding.tradeRecyclerView.setAdapter(storesListAdapter);
                                            storesListAdapter.setOnItemClickListener(new BrandAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View viewItem, int position, ArrayList<Integer> selectedBrandIdItem, ArrayList<String> selectedBrandNamesItem) {
                                                    Log.i("LISTS", selectedBrandNamesItem.toString());
                                                    //ADD TO LIST OF CHOSEN BRAND
                                                    ChosenBrandAdapter storesListAdapter = new ChosenBrandAdapter(selectedBrandNamesItem);
                                                    filtterDialogBinding.tradeRecyclerView.setAdapter(storesListAdapter);

                                                }
                                            });
                                        } else {
                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void showFillterDialog() {
        final View view5 = filtterDialogBinding.getRoot();
        filtterDialog.setContentView(view5);
        filtterDialog.setCancelable(true);
        filtterDialog.show();
        filtterDialogBinding.resetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO delete tradeListId and storeId
                filtterDialogBinding.storeLinearId.setVisibility(View.INVISIBLE);
                filtterDialogBinding.tradeRecyclerView.setVisibility(View.INVISIBLE);
            }
        });
        filtterDialogBinding.deleteId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtterDialogBinding.storeLinearId.setVisibility(View.INVISIBLE);
            }
        });
        filtterDialogBinding.doneId.setOnClickListener(v -> filtterDialog.dismiss());
        filtterDialogBinding.storeId.setOnClickListener(v -> showStoreListDialog());
        filtterDialogBinding.brandId.setOnClickListener(v -> showBrandListDialog());

    }

    private void showStoreListDialog() {
        final View view5 = storeListDialogBinding.getRoot();
        storeListDialog.setContentView(view5);
        storeListDialog.setCancelable(true);
        storeListDialog.show();
        storesList("", GoldenNoLoginSharedPreference.getUserLanguage(context));
        storeListDialogBinding.cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeListDialog.dismiss();
            }
        });


    }

    private void showBrandListDialog() {
        final View view5 = brandListDialogBinding.getRoot();
        brandListDialog.setContentView(view5);
        brandListDialog.setCancelable(true);
        brandListDialog.show();
        brandList();
        brandListDialogBinding.closeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandListDialog.dismiss();
            }
        });


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
        social();
        noCouponProductDetailsDialogBinding.rateNwId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportUs();
            }
        });
    }

    public interface Listener {
        void click(int click, int position, String id, String c);

        void clickShare(String coupon, String store, String url);
    }

    ProductsViewModel.Listener listener = new ProductsViewModel.Listener() {
        @Override
        public void click(int click, int p, String id, String coupon) {
            Log.i("POSITIONIN", p + "?");
            position = p;
            getSingleProducts(GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                    id);

            if (click == 4) {
                //openProductDialog
                //check coupon
                Log.i("COO", coupon + "?");
                if (coupon == null || coupon.equals("null") || coupon.isEmpty()) {
                    showNoCouponProductDetailsDialog();
                } else {
                    showProductDetailsDialog();
                }
            }

        }


        //String coupon, String store, String url
        public void clickShare(String coupon, String store, String url) {
            shareVia(coupon, store, url);
        }

    };

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
        social2();

    }

    private void showSecondProductDetailsDialog() {
        final View view5 = secondProductDetailsDialogBinding.getRoot();
        secondShowProductDetailsDialog.setContentView(view5);
        secondShowProductDetailsDialog.setCancelable(true);
        secondShowProductDetailsDialog.show();
        secondProductDetailsDialogBinding.cancelLogId.setOnClickListener(v8 -> secondShowProductDetailsDialog.dismiss());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social3();
        secondProductDetailsDialogBinding.yesActiveId.setOnClickListener(v7 -> rateDialog());

    }

    private void shareOfferVia(String store, String url) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = store
                + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
    }
}
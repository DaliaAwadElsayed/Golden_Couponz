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

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductsFragmentBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.goldencouponz.adapters.home.ProductCatAdapter;
import com.goldencouponz.adapters.stores.ProductAdapter;
import com.goldencouponz.adapters.stores.ProductsSubCategoriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends ViewModel {
    ProductsFragmentBinding productsFragmentBinding;
    Context context;
    BottomSheetDialog showProductDetailsDialog;
    BottomSheetDialog secondShowProductDetailsDialog;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    BottomSheetDialog showNoCouponDialog;
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
                     Context context) {
        this.context = context;
        this.productsFragmentBinding = productsFragmentBinding;
        this.productDetailsDialogBinding = productDetailsDialogBinding;
        this.secondProductDetailsDialogBinding = secondProductDetailsDialogBinding;
        this.noCouponProductDetailsDialogBinding = noCouponProductDetailsDialogBinding;
        secondShowProductDetailsDialog = new BottomSheetDialog(context);
        showProductDetailsDialog = new BottomSheetDialog(context);
        showNoCouponDialog = new BottomSheetDialog(context);
        productsFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
        productsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
        productsFragmentBinding.allId.setBackgroundTintList(null);
        getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), "", "", "");
        categories(GoldenNoLoginSharedPreference.getUserLanguage(context));
        productsFragmentBinding.allId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsFragmentBinding.subCategoryRecyclerView.setVisibility(View.GONE);
                productsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                productsFragmentBinding.allId.setBackgroundTintList(null);
                getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), "", "", "");
                categories(GoldenNoLoginSharedPreference.getUserLanguage(context));

            }

        });

    }

    private void subCategory(int parentId, String lang) {
        productsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
        productsFragmentBinding.subCategoryRecyclerView.setVisibility(View.VISIBLE);
        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCategories(lang, "deviceToken", parentId).enqueue(new Callback<ApiResponse>() {
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
                                            "", String.valueOf(categoryId), String.valueOf(subCatId));
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

    private void getProducts(String deviceToken, int country, String lang, String storeId, String catId, String subCatId) {
        Log.i("POSITIONOUT",position+"?");
        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getStoreProducts(deviceToken, country, lang, storeId, catId, subCatId).enqueue(new Callback<ApiResponse>() {
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
    private void getSingleProducts(String deviceToken, int country, String lang, String storeId, String catId, String subCatId,int position) {
        Log.i("POSITIONOUT",position+"?");
        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getStoreProducts(deviceToken, country, lang, storeId, catId, subCatId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        productsFragmentBinding.progress.setVisibility(View.GONE);
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
                                    openUrl(response.body().getProducts().getData().get(position).getVideoLink());

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
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                productsFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void shareVia(String coupon, String store, String url) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getResources().getString(R.string.share_msg_part1) + "(" + coupon + ")" +
                context.getResources().getString(R.string.share_msg_part2) + "(" + store +
                ")" + context.getResources().getString(R.string.click_link) + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
    }

    private void categories(String lang) {
        productsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCategories(lang, "deviceToken", 0).enqueue(new Callback<ApiResponse>() {
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
                                    subCategory(categoryId, GoldenNoLoginSharedPreference.getSelectedLanguageValue(context));
                                    getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                                            "", String.valueOf(categoryId), "");

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
        void click(int click, int position, String c);

        void clickShare(String coupon, String store, String url);
    }

    ProductsViewModel.Listener listener = new ProductsViewModel.Listener() {
        @Override
        public void click(int click, int p, String coupon) {
            Log.i("POSITIONIN",p+"?");
            position=p;
            getSingleProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                    "", "", "",position);

            if (click == 4) {
                //openProductDialog
                //check coupon
                Log.i("COO",coupon+"?");
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
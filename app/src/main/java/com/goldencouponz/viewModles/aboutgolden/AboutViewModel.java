package com.goldencouponz.viewModles.aboutgolden;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.AboutFragmentBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.appsetting.AboutApp;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutViewModel extends ViewModel {
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    AboutFragmentBinding aboutFragmentBinding;
    String type;

    public void init(Context context, AboutFragmentBinding aboutFragmentBinding, String type) {
        this.aboutFragmentBinding = aboutFragmentBinding;
        this.context = context;
        this.type = type;
        aboutFragmentBinding.backId.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.profileFragment));
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            getGeneralViews("ar", type);
            title();
        } else {
            getGeneralViews("en", type);
            title();
        }
    }

    private void title() {
        if (type.equals("about")) {
            aboutFragmentBinding.titleId.setText(R.string.who_we_are);
        } else if (type.equals("privacy")) {
            aboutFragmentBinding.titleId.setText(R.string.privacy_policy);
        } else if (type.equals("terms")) {
            aboutFragmentBinding.titleId.setText(R.string.terms_conditions);
        }
    }

    private void getGeneralViews(String lang, String type) {
        apiInterface.generalViews(lang).enqueue(new Callback<AboutApp>() {
            @Override
            public void onResponse(Call<AboutApp> call, Response<AboutApp> response) {
                if (response.code() == 200) {
                    Log.i("DETAILSLANGUAGE", lang);
                    aboutFragmentBinding.detailsId.setVisibility(View.VISIBLE);
                    aboutFragmentBinding.noInternetConId.setVisibility(View.GONE);
                    aboutFragmentBinding.progress.setVisibility(View.GONE);
                    if (lang.equals("en")) {
                        if (type.equals("about")) {
                            aboutFragmentBinding.logoId.setVisibility(View.VISIBLE);
                            aboutFragmentBinding.detailsId.setText(Html.fromHtml(response.body().getTopics().get(2).getDetailsEn()));
                        } else if (type.equals("privacy")) {
                            aboutFragmentBinding.logoId.setVisibility(View.GONE);
                            aboutFragmentBinding.detailsId.setText(Html.fromHtml(response.body().getTopics().get(1).getDetailsEn()));
                        } else if (type.equals("terms")) {
                            aboutFragmentBinding.logoId.setVisibility(View.GONE);
                            aboutFragmentBinding.detailsId.setText(Html.fromHtml(response.body().getTopics().get(0).getDetailsEn()));
                        }
                    } else {
                        if (type.equals("about")) {
                            aboutFragmentBinding.logoId.setVisibility(View.VISIBLE);
                            aboutFragmentBinding.detailsId.setText(Html.fromHtml(response.body().getTopics().get(2).getDetailsAr()));
                        } else if (type.equals("privacy")) {
                            aboutFragmentBinding.logoId.setVisibility(View.GONE);
                            aboutFragmentBinding.detailsId.setText(Html.fromHtml(response.body().getTopics().get(1).getDetailsAr()));
                        } else if (type.equals("terms")) {
                            aboutFragmentBinding.logoId.setVisibility(View.GONE);
                            aboutFragmentBinding.detailsId.setText(Html.fromHtml(response.body().getTopics().get(0).getDetailsAr()));
                        }
                    }
                } else {
                    aboutFragmentBinding.logoId.setVisibility(View.GONE);
                    aboutFragmentBinding.progress.setVisibility(View.GONE);
                    aboutFragmentBinding.detailsId.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AboutApp> call, Throwable t) {
                Log.i("DETAILSLANGUAGE", t.toString());
                aboutFragmentBinding.logoId.setVisibility(View.GONE);
                aboutFragmentBinding.detailsId.setVisibility(View.GONE);
                aboutFragmentBinding.noInternetConId.setVisibility(View.VISIBLE);
                aboutFragmentBinding.progress.setVisibility(View.GONE);

            }
        });
    }
}
package com.goldencouponz.viewModles.onboarding;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.FirstSkipFragmentBinding;
import com.goldencouponz.adapters.onBoarding.OnBoardingAdapter;
import com.rd.animation.type.AnimationType;

import java.util.Timer;
import java.util.TimerTask;

public class FirstSkipViewModel extends ViewModel implements ViewPager.OnPageChangeListener {
    FirstSkipFragmentBinding firstSkipFragmentBinding;
    Context context;
    OnBoardingAdapter onBoardingAdapter;
    Timer timer;
    int interfaceSize;
    int currentPage = 0;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    public void init(FirstSkipFragmentBinding firstSkipFragmentBinding, Context context) {
        this.context = context;
        this.firstSkipFragmentBinding = firstSkipFragmentBinding;
        firstSkipFragmentBinding.skipId.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.notificationAlertFragment));
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int selection = firstSkipFragmentBinding.slider.getCurrentItem();
                if (selection == 0) {
                    firstSkipFragmentBinding.nextId.setText(R.string.next_label);
                } else if (selection == 1) {
                    firstSkipFragmentBinding.nextId.setText(R.string.next_label);
                } else if (selection == 2) {
                    firstSkipFragmentBinding.nextId.setText(R.string.lets_start);
                }
                firstSkipFragmentBinding.slider.setCurrentItem(currentPage++, true);

            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);

            }

        }, DELAY_MS, PERIOD_MS);
        firstSkipFragmentBinding.nextId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                handler.removeCallbacks(Update);
                int selection = firstSkipFragmentBinding.slider.getCurrentItem();
                if (selection == 0) {
                    firstSkipFragmentBinding.nextId.setText(R.string.next_label);
                    firstSkipFragmentBinding.slider.setCurrentItem(1);
                } else if (selection == 1) {
                    firstSkipFragmentBinding.nextId.setText(R.string.lets_start);
                    firstSkipFragmentBinding.slider.setCurrentItem(2);
                } else if (selection == 2) {
                    Navigation.findNavController(v).navigate(R.id.notificationAlertFragment);
                }

            }
        });
        firstSkipFragmentBinding.slider.setAdapter(onBoardingAdapter);
        firstSkipFragmentBinding.slider.addOnPageChangeListener(this);
        firstSkipFragmentBinding.pageIndicatorView.setAnimationType(AnimationType.SWAP);
        int imageArray[] = {R.drawable.ic_first_skip, R.drawable.ic_second_skip, R.drawable.ic_third_skip};
        String firstStringArray[] = {context.getResources().getString(R.string.stores_label), context.getResources().getString(R.string.products_label), context.getResources().getString(R.string.alerts_label)};
        String secondStringArray[] = {context.getResources().getString(R.string.best_store_deals), context.getResources().getString(R.string.disconts_products), context.getResources().getString(R.string.follow_offer)};
        String thirdStringArray[] = {context.getResources().getString(R.string.get_exclusive_coupon_and_offer), context.getResources().getString(R.string.find_the_product_and_discount), context.getResources().getString(R.string.be_ready_for_alerts)};
        OnBoardingAdapter adapter = new OnBoardingAdapter(context, imageArray, firstStringArray, secondStringArray, thirdStringArray);
        firstSkipFragmentBinding.slider.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        firstSkipFragmentBinding.pageIndicatorView.setSelection(position);
        int selection = firstSkipFragmentBinding.slider.getCurrentItem();
        if (selection == 0) {
            firstSkipFragmentBinding.nextId.setText(R.string.next_label);
        } else if (selection == 1) {
            firstSkipFragmentBinding.nextId.setText(R.string.next_label);
        } else if (selection == 2) {
            firstSkipFragmentBinding.nextId.setText(R.string.lets_start);
        }
    }

}
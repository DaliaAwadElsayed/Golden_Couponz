package com.goldencouponz.adapters.onBoarding;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;

public class OnBoardingAdapter extends PagerAdapter {
    private int imageArray[];
    private String firstStringArray[];
    private String secondStringArray[];
    private String thirdStringArray[];
    Context activity;

    public OnBoardingAdapter(Context act, int[] imgArray, String[] firstStringArra, String[] secondStringArra,
                             String[] thirdStringArra) {
        imageArray = imgArray;
        firstStringArray = firstStringArra;
        secondStringArray = secondStringArra;
        thirdStringArray = thirdStringArra;
        activity = act;
    }

    public int getCount() {
        return 3;
    }

    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext
                ().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.slides_layout, null);
        ImageView imageView = layout.findViewById(R.id.slider_image);
        imageView.setImageResource(imageArray[position]);
        TextView fText = layout.findViewById(R.id.firstTextId);
        TextView sText = layout.findViewById(R.id.secondTextId);
        TextView tThird = layout.findViewById(R.id.thirdTextId);
        fText.setText(firstStringArray[position]);
        sText.setText(secondStringArray[position]);
        tThird.setText(thirdStringArray[position]);
        ((ViewPager) collection).addView(layout, 0);
        return layout;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}




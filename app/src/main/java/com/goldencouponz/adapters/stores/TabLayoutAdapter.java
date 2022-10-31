package com.goldencouponz.adapters.stores;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.goldencouponz.fragments.stores.CopounzFragment;
import com.goldencouponz.fragments.stores.ProudctsFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabLayoutAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CopounzFragment copounzFragment = new CopounzFragment();
                return copounzFragment;
            case 1:
                ProudctsFragment proudctsFragment = new ProudctsFragment();
                return proudctsFragment;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
package com.goldencouponz.fragments.stores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.R;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.stores.CopounzViewModel;

public class CopounzFragment extends Fragment {

    private CopounzViewModel mViewModel;

    public static CopounzFragment newInstance() {
        return new CopounzFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.copounz_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CopounzViewModel.class);
        ((MainActivity) getActivity()).hideBottomMenu();
        // TODO: Use the ViewModel
    }

}
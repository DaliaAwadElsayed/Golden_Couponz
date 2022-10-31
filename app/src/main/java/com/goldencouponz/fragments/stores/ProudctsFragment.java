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
import com.goldencouponz.viewModles.stores.ProudctsViewModel;

public class ProudctsFragment extends Fragment {

    private ProudctsViewModel mViewModel;

    public static ProudctsFragment newInstance() {
        return new ProudctsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.proudcts_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProudctsViewModel.class);
        ((MainActivity) getActivity()).hideBottomMenu();
        // TODO: Use the ViewModel
    }

}
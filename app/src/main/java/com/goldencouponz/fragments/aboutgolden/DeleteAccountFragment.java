package com.goldencouponz.fragments.aboutgolden;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.DeleteAccountFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.aboutgolden.DeleteAccountViewModel;

public class DeleteAccountFragment extends Fragment {

    private DeleteAccountViewModel mViewModel;
    DeleteAccountFragmentBinding deleteAccountFragmentBinding;

    public static DeleteAccountFragment newInstance() {
        return new DeleteAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        deleteAccountFragmentBinding = DeleteAccountFragmentBinding.inflate(inflater, container, false);
        return deleteAccountFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeleteAccountViewModel.class);
        String part = (Build.VERSION.RELEASE);
        String[] parts = part.split("\\.");
        int release = Integer.parseInt(parts[0]); // 004
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {
            ((MainActivity) getActivity()).hideBottomMenu();
        } else {
            ((MainActivity) getActivity()).hideBottomMenu2();

        }
        mViewModel.init(deleteAccountFragmentBinding, getContext());
    }

}
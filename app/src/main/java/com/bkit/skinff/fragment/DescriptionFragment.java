package com.bkit.skinff.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bkit.skinff.databinding.FragmentDescriptionBinding;


public class DescriptionFragment extends Fragment {

    private FragmentDescriptionBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDescriptionBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }
}
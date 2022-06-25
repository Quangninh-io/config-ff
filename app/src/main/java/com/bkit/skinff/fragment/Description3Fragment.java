package com.bkit.skinff.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bkit.skinff.databinding.FragmentDescription3Binding;

public class Description3Fragment extends Fragment {
    private FragmentDescription3Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDescription3Binding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }
}

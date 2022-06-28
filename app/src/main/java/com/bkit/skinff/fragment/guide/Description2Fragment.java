package com.bkit.skinff.fragment.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bkit.skinff.databinding.FragmentDescription2Binding;

public class Description2Fragment extends Fragment {
    private FragmentDescription2Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDescription2Binding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }
}

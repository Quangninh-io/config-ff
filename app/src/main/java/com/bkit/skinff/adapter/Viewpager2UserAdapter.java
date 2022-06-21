package com.bkit.skinff.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bkit.skinff.fragment.Description2Fragment;
import com.bkit.skinff.fragment.Description3Fragment;
import com.bkit.skinff.fragment.DescriptionFragment;
import com.bkit.skinff.fragment.WelcomeFragment;

public class Viewpager2UserAdapter extends FragmentStateAdapter {


    public Viewpager2UserAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WelcomeFragment();
            case 1:
                return new DescriptionFragment();
            case 2:
                return new Description2Fragment();
            case 3:
                return new Description3Fragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

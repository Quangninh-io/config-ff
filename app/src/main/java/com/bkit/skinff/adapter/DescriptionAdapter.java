package com.bkit.skinff.adapter;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bkit.skinff.fragment.guide.Description2Fragment;
import com.bkit.skinff.fragment.guide.Description3Fragment;
import com.bkit.skinff.fragment.guide.WelcomeFragment;
// use for target when open app, user will tap desc about app and guide
// include 4 fragments
public class DescriptionAdapter extends FragmentStateAdapter {


    public DescriptionAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 30) {
            switch (position) {
                case 0:
                    return new WelcomeFragment();
                case 1:
                    return new Description2Fragment();
            }
        }else{
            switch (position) {
                case 0:
                    return new WelcomeFragment();
                case 1:
                    return new Description2Fragment();
                case 2:
                    return new Description3Fragment();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 30) {
            return 2;
        }
        return 3;
    }
}

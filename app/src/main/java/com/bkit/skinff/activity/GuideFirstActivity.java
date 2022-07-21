package com.bkit.skinff.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bkit.skinff.R;
import com.bkit.skinff.databinding.ActivityGuideFirstBinding;

public class GuideFirstActivity extends AppCompatActivity {

    ActivityGuideFirstBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivClose.setOnClickListener(v->{
            startActivity(new Intent(this,UserMainActivity.class));
        });
    }
}
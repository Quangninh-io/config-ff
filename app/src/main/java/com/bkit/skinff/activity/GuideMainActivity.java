package com.bkit.skinff.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bkit.skinff.R;
import com.bkit.skinff.utilities.SetLanguage;

public class GuideMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ImageView imageView = findViewById(R.id.iv_back_guide);
        imageView.setOnClickListener(v->{
            onBackPressed();
        });

    }
}
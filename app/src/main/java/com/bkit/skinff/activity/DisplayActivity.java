package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.PUT_BUNDLE;
import static com.bkit.skinff.utilities.Constants.PUT_INTENT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bkit.skinff.databinding.ActivityDisplayBinding;
import com.bkit.skinff.listener.ReceiveDataFromFirebase;
import com.bkit.skinff.model.FileData;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {

    private ActivityDisplayBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(PUT_INTENT);
        List<FileData> list = (List<FileData>) bundle.getSerializable(PUT_BUNDLE);
        Log.d("list",list.size()+"");

    }

}
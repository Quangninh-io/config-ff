package com.bkit.skinff.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bkit.skinff.R;
import com.bkit.skinff.databinding.ActivityResponseBinding;
import com.bkit.skinff.databinding.ActivityUserBinding;
import com.bkit.skinff.utilities.SetLanguage;

public class ResponseActivity extends AppCompatActivity {
    private ActivityResponseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetLanguage.getInstance().configLanguage(this);
        super.onCreate(savedInstanceState);
        binding = ActivityResponseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMain();
    }
    String message ="";

    private void initMain() {
        binding.ivBackGuide.setOnClickListener(v->{onBackPressed();});
        binding.ivSend.setOnClickListener(v->{
            handleSendResponse();
        });


       binding.fbErrorApp.setOnClickListener(v->{
           message = binding.etMessage.getText().toString().trim();
           message +=binding.fbErrorApp.getText().toString().trim();
           binding.etMessage.setText(message);
       });

        binding.fbErrorOutfitMax.setOnClickListener(v->{
            message = binding.etMessage.getText().toString().trim();
            message +=binding.fbErrorOutfitMax.getText().toString().trim();
            binding.etMessage.setText(message);
        });

        binding.fbErrorOutfit.setOnClickListener(v->{
            message = binding.etMessage.getText().toString().trim();
            message +=binding.fbErrorOutfit.getText().toString().trim();
            binding.etMessage.setText(message);
        });

        binding.fbErrorGun.setOnClickListener(v->{
            message = binding.etMessage.getText().toString().trim();
            message +=binding.fbErrorGun.getText().toString().trim();
            binding.etMessage.setText(message);
        });
        binding.fbErrorGunMax.setOnClickListener(v->{
            message = binding.etMessage.getText().toString().trim();
            message +=binding.fbErrorGunMax.getText().toString().trim();
            binding.etMessage.setText(message);
        });
    }

    private void handleSendResponse() {
        String[] emailReceive = {"quangninh312456@gmail.com"};
        String subject =getResources().getString(R.string.a_feedback);
        message = binding.etMessage.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,emailReceive);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose Account"));
    }

    @Override
    public void onBackPressed() {
        message="";
        super.onBackPressed();
    }
}
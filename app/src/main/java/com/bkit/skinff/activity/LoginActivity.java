package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_ADMIN;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_PASSWORD;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bkit.skinff.R;
import com.bkit.skinff.databinding.ActivityAdminBinding;
import com.bkit.skinff.databinding.ActivityLoginBinding;
import com.bkit.skinff.model.FileData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMain();
    }

    private void initMain() {
        binding.btLogin.setOnClickListener(v -> {
            // check password
            checkPassword();

        });
    }

    private void checkPassword() {
        db.collection(COLLECTION_ADMIN)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = String.valueOf(document.getData().get(KEY_NAME));
                            String password = String.valueOf(document.getData().get(KEY_PASSWORD));
                            Log.d("fdjakiad",name+""+password);
                            if (binding.etName.getText().toString().trim().equals(name) &&
                                    binding.etPas.getText().toString().trim().equals(password)
                            ) {
                                startActivity(new Intent(getApplication(), AdminActivity.class));
                            } else {
                                Toast.makeText(getApplication(), "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }
}
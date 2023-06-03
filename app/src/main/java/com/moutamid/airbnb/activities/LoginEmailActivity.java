package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.airbnb.MainActivity;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityLoginEmailBinding;
import com.moutamid.airbnb.R;

public class LoginEmailActivity extends AppCompatActivity {
    ActivityLoginEmailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.continueBtn.setOnClickListener(v -> {
            Constants.showDialog();
            Constants.auth().signInWithEmailAndPassword(
                    binding.email.getText().toString(),
                    binding.password.getText().toString()
            ).addOnSuccessListener(authResult -> {
                Constants.dismissDialog();
                startActivity(new Intent(LoginEmailActivity.this, MainActivity.class));
                finish();
            }).addOnFailureListener(e -> {
                Constants.dismissDialog();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.airbnb.R;
import com.moutamid.airbnb.databinding.ActivityMySpaceBinding;

public class MySpaceActivity extends AppCompatActivity {
    ActivityMySpaceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMySpaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}
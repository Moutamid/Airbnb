package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.airbnb.R;
import com.moutamid.airbnb.databinding.ActivityHostSpaceBinding;

public class HostSpaceActivity extends AppCompatActivity {
    ActivityHostSpaceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostSpaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}
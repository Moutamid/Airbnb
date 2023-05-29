package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.airbnb.R;
import com.moutamid.airbnb.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.airbnb.R;
import com.moutamid.airbnb.databinding.ActivityFlightBookingBinding;

public class FlightBookingActivity extends AppCompatActivity {
    ActivityFlightBookingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
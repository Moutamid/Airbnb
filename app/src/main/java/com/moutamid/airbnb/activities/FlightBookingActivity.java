package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityFlightBookingBinding;

import org.checkerframework.checker.units.qual.A;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FlightBookingActivity extends AppCompatActivity {
    ActivityFlightBookingBinding binding;
    final Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Stash.put(Constants.TRIP, "oneway");

        binding.back.setOnClickListener(v -> {
            finish();
        });

        binding.onewayTrip.setOnClickListener(v -> {
            Stash.put(Constants.TRIP, "oneway");
            binding.roundTrip.setCardBackgroundColor(getResources().getColor(R.color.dark));
            binding.onewayTrip.setCardBackgroundColor(getResources().getColor(R.color.blue));

            binding.returingDate.setVisibility(View.GONE);

        });

        DatePickerDialog.OnDateSetListener departure = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        binding.departureDate.getEditText().setText(Constants.getDepartureFormat(new Date().getTime()));

        DatePickerDialog.OnDateSetListener leaving = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLeaving();
        };

        binding.roundTrip.setOnClickListener(v -> {
            Stash.put(Constants.TRIP, "round");
            binding.roundTrip.setCardBackgroundColor(getResources().getColor(R.color.blue));
            binding.onewayTrip.setCardBackgroundColor(getResources().getColor(R.color.dark));

            binding.returingDate.setVisibility(View.VISIBLE);

        });

        binding.search.setOnClickListener(v -> {
            if (valid()){
                Stash.put(Constants.Leaving, binding.leavingFrom.getEditText().getText().toString().toUpperCase(Locale.ROOT));
                Stash.put(Constants.GOING, binding.goingTo.getEditText().getText().toString().toUpperCase(Locale.ROOT));

                startActivity(new Intent(this, FlightsResultActivity.class));
            }
        });

        binding.departureDate.getEditText().setOnClickListener(v -> {
            new DatePickerDialog(FlightBookingActivity.this, departure, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        binding.returingDate.getEditText().setOnClickListener(v -> {
            new DatePickerDialog(FlightBookingActivity.this, leaving, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.apiKey.setOnClickListener(v -> {
            changeAPI();
        });

    }

    private void changeAPI() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.api_key_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        TextView instructions = dialog.findViewById(R.id.instructions);
        TextInputLayout apiKey = dialog.findViewById(R.id.apiKey);
        Button save = dialog.findViewById(R.id.save);

        instructions.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flightapi.io/"))));

        save.setOnClickListener(v -> {
            if (apiKey.getEditText().getText().toString().isEmpty()){
                Toast.makeText(this, "API key is empty", Toast.LENGTH_SHORT).show();
            } else {
                Stash.put(Constants.API_KEY, apiKey.getEditText().getText().toString().trim());
                dialog.dismiss();
            }
        });
    }

    private void updateLabel() {
        Stash.put(Constants.departureDate, calendar.getTime().getTime());
        binding.departureDate.getEditText().setText(Constants.getDepartureFormat(calendar.getTime().getTime()));
    }

    private void updateLeaving() {
        Stash.put(Constants.returingDate, calendar.getTime().getTime());
        binding.returingDate.getEditText().setText(Constants.getDepartureFormat(calendar.getTime().getTime()));
    }

    private boolean valid() {

        if (binding.leavingFrom.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill complete data", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.goingTo.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill complete data", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
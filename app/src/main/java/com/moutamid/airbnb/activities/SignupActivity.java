package com.moutamid.airbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.moutamid.airbnb.MainActivity;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivitySignupBinding;
import com.moutamid.airbnb.models.UserModel;

import java.util.Calendar;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    final Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        binding.dob.setOnClickListener(v -> {
            new DatePickerDialog(SignupActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.continueBtn.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getText().toString(),
                        binding.password.getText().toString()
                ).addOnSuccessListener(authResult -> {
                    UserModel userModel = new UserModel(
                            authResult.getUser().getUid(),
                            (binding.firstName.getText().toString() + " " + binding.lastName.getText().toString()),
                            Constants.getFormatedDate(calendar.getTime().getTime()),
                            binding.email.getText().toString(),
                            binding.password.getText().toString(), "", ""
                    );
                    Constants.databaseReference().child(Constants.USER).child(authResult.getUser().getUid())
                            .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Constants.dismissDialog();
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Constants.dismissDialog();
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private boolean valid() {
        if (binding.firstName.getText().toString().isEmpty()){
            binding.firstName.setError("First name is Required");
            return false;
        }
        if (binding.lastName.getText().toString().isEmpty()){
            binding.lastName.setError("Last name is Required");
            return false;
        }
        if (binding.email.getText().toString().isEmpty()){
            binding.email.setError("Email is Required");
            return false;
        }
        if (binding.password.getText().toString().isEmpty()){
            binding.password.setError("Password is Required");
            return false;
        }
        if (binding.dob.getText().toString().isEmpty()){
            Toast.makeText(this, "Please add your Date of birth", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.dob.getText().toString().isEmpty()){
            int current_year = Integer.parseInt(Constants.getFormatedYear(new Date().getTime()));
            int selected_year = Integer.parseInt(Constants.getFormatedYear(calendar.getTime().getTime()));
            if (current_year - selected_year < 18){
                Toast.makeText(this, "You must be above 18 years old", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        return true;
    }

    private void updateLabel() {
        binding.dob.setText(Constants.getFormatedDate(calendar.getTime().getTime()));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
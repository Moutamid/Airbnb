package com.moutamid.airbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityProfileBinding;
import com.moutamid.airbnb.models.UserModel;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);

        binding.back.setOnClickListener(v -> {
            finish();
        });

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            binding.name.setText(userModel.getName());
                            binding.email.setText(userModel.getEmail());
                            if (userModel.getDob().isEmpty()){
                                binding.dob.setText("Edit your profile to add you DOB");
                            } else {
                                binding.dob.setText(userModel.getDob());
                            }
                            if (userModel.getPhone().isEmpty()){
                                binding.phone.setText("Edit your profile to add you Phone Number");
                            } else {
                                binding.phone.setText(userModel.getPhone());
                            }
                            Glide.with(ProfileActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profileIcon);
                        }
                        Constants.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
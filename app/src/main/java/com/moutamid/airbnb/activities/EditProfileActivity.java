package com.moutamid.airbnb.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityEditProfileBinding;
import com.moutamid.airbnb.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {
    private static final int IMAGEPICKER = 1;
    ActivityEditProfileBinding binding;
    UserModel userModel;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edit.setOnClickListener(v -> {
            showDialog();
        });

        Constants.initDialog(this);
        Constants.showDialog();

        binding.save.setOnClickListener(v -> {
            saveData();
        });

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                userModel = snapshot.getValue(UserModel.class);
                                Glide.with(EditProfileActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                                String[] name = userModel.getName().split(" ");
                                binding.fName.getEditText().setText(name[0]);
                                binding.lName.getEditText().setText(name[1]);
                                binding.phone.getEditText().setText(userModel.getPhone());
                                if (!userModel.getDob().isEmpty()){
                                    String[] date = userModel.getDob().split("/");
                                    binding.day.getEditText().setText(date[0]);
                                    binding.month.getEditText().setText(date[1]);
                                    binding.year.getEditText().setText(date[2]);
                                }
                                binding.email.getEditText().setText(userModel.getEmail());
                            }
                            Constants.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(EditProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void saveData() {
        Constants.showDialog();
        if (imageURI != null){
            saveImage();
        } else {
            save(userModel.getImage());
        }
    }

    private void save(String image) {
        UserModel model = new UserModel(
                userModel.getID(), userModel.getName(),
                (binding.day.getEditText().getText().toString().trim() + "/" + binding.month.getEditText().getText().toString().trim() + "/" +  binding.year.getEditText().getText().toString().trim()),
                userModel.getEmail(), userModel.getPassword(),
                binding.phone.getEditText().getText().toString(),
                image
        );

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .setValue(model).addOnSuccessListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void saveImage() {
        String dd = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date().getTime());
        Constants.storageReference(Constants.auth().getCurrentUser().getUid()).child("logo").putFile(imageURI)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        save(uri.toString());
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bs_get_image);

        Button openCamera = dialog.findViewById(R.id.camera);
        Button openGallery = dialog.findViewById(R.id.gallery);
        Button cancel = dialog.findViewById(R.id.close);

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        openGallery.setOnClickListener(v -> {
            getImageFromGallery();
            dialog.cancel();
        });

        openCamera.setOnClickListener(v -> {
            getImageFromCamera();
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void getImageFromCamera() {
        ImagePicker.with(this)
                .cameraOnly()
                .cropSquare()
                .compress(512)
                .maxResultSize(1080, 1080)
                .start(IMAGEPICKER);
        Constants.showDialog();
    }

    private void getImageFromGallery() {
        ImagePicker.with(this)
                .galleryOnly()
                .compress(512)
                .cropSquare()
                .maxResultSize(1080, 1080)
                .start(IMAGEPICKER);
        Constants.showDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IMAGEPICKER){
            if (resultCode == RESULT_OK && data != null && data.getData() != null){
                imageURI = data.getData();
                Glide.with(EditProfileActivity.this).load(imageURI).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Constants.dismissDialog();
                        Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Constants.dismissDialog();
                        return false;
                    }
                }).placeholder(R.drawable.profile_icon).into(binding.profile);
            }
        }
    }
}
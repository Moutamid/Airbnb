package com.moutamid.airbnb.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.AddImageAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityHostSpaceBinding;
import com.moutamid.airbnb.models.SpaceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class HostSpaceActivity extends AppCompatActivity {
    ActivityHostSpaceBinding binding;
    final int limit = 6;
    ArrayList<Uri> imagesList;
    final Calendar calendar = Calendar.getInstance();
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PICK_FROM_CAMERA = 2;

    long startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostSpaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        imagesList = new ArrayList<>();

        binding.back.setOnClickListener(v -> finish());

        DatePickerDialog.OnDateSetListener startDate = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelStartDate();
        };

        DatePickerDialog.OnDateSetListener endDate = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelEndDate();
        };

        binding.images.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.images.setHasFixedSize(true);

        binding.addImage.setOnClickListener(v -> {
            if (imagesList.size() >= 6){
                Toast.makeText(this, "You can only add 6 images", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

        binding.addMore.setOnClickListener(v -> {
            if (imagesList.size() >= 6){
                Toast.makeText(this, "You can only add 6 images", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

        binding.startDate.setOnClickListener(v -> {
            new DatePickerDialog(HostSpaceActivity.this, startDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.endDate.setOnClickListener(v -> {
            new DatePickerDialog(HostSpaceActivity.this, endDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.host.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                uploadData();
            }
        });

        binding.guests.setOnClickListener(v -> {
            Stash.put("key", 0);
            showPicker();
        });
        binding.beds.setOnClickListener(v -> {
            Stash.put("key", 1);
            showPicker();
        });
        binding.baths.setOnClickListener(v -> {
            Stash.put("key", 2);
            showPicker();
        });

    }

    private void showPicker() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.number_picker);

        AtomicInteger key = new AtomicInteger(Stash.getInt("key"));

        NumberPicker picker = dialog.findViewById(R.id.picker);
        Button select = dialog.findViewById(R.id.select);
        Button cancel = dialog.findViewById(R.id.close);

        picker.setMaxValue(100);
        picker.setMinValue(1);
        picker.setValue(1);

        cancel.setOnClickListener(v -> dialog.dismiss());

        select.setOnClickListener(v -> {
            if (key.get() == 0){
                binding.guests.setText(""+picker.getValue());
            }
            if (key.get() == 1){
                binding.beds.setText(""+picker.getValue());
            }
            if (key.get() == 2){
                binding.baths.setText(""+picker.getValue());
            }
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    private void updateLabelEndDate() {
        endTime = calendar.getTime().getTime();
        binding.endDate.setText(Constants.getDate(calendar.getTime().getTime()));
    }

    private void updateLabelStartDate() {
        startTime = calendar.getTime().getTime();
        binding.startDate.setText(Constants.getDate(calendar.getTime().getTime()));
    }

    private boolean valid() {
        if (imagesList.size() == 0){
            Toast.makeText(this, "Please Add at least 1 Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.title.getText().toString().isEmpty()){
            binding.title.setError("Require Field");
            return false;
        }
        if (binding.desc.getText().toString().isEmpty()){
            binding.desc.setError("Require Field");
            return false;
        }
        if (binding.city.getText().toString().isEmpty()){
            binding.city.setError("Require Field");
            return false;
        }
        if (binding.country.getText().toString().isEmpty()){
            binding.country.setError("Require Field");
            return false;
        }
        if (binding.price.getText().toString().isEmpty()){
            binding.price.setError("Require Field");
            return false;
        }
        if (binding.startDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Date is Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.endDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Date is Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.beds.getText().toString().isEmpty()){
            Toast.makeText(this, "Number of beds is Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.baths.getText().toString().isEmpty()){
            Toast.makeText(this, "Number of baths is Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.guests.getText().toString().isEmpty()){
            Toast.makeText(this, "Number of guests is Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadData() {
        String ID = UUID.randomUUID().toString();
        String currentDate = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date());
        for (int count =0; count<imagesList.size(); count++){
            int finalCount = count;
            Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                    .child(currentDate+"_image"+count)
                    .putFile(imagesList.get(count))
                    .addOnSuccessListener(taskSnapshot -> {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                            Map<String, String> map = new HashMap<>();
                            map.put("image", uri.toString());
                            if (finalCount == 0){
                                uploadProduct(ID, uri.toString());
                            }
                            Constants.databaseReference().child(Constants.ProductImages)
                                    .child(Constants.auth().getCurrentUser().getUid()).child(ID)
                                    .push().setValue(map).addOnSuccessListener(unused -> {
                                        if (finalCount == imagesList.size()-1) {
                                            Constants.dismissDialog();
                                            Toast.makeText(this, "Space hosted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void uploadProduct(String id, String link) {
        SpaceModel model = new SpaceModel(
                id, Constants.auth().getCurrentUser().getUid(),
                binding.title.getText().toString(), link,
                binding.desc.getText().toString(), binding.city.getText().toString(),binding.country.getText().toString(),
                Double.parseDouble(binding.price.getText().toString()), new Date().getTime(), Long.parseLong(binding.guests.getText().toString()),
                Long.parseLong(binding.beds.getText().toString()), Long.parseLong(binding.baths.getText().toString()),
                binding.startDate.getText().toString(), binding.endDate.getText().toString(),
                startTime, endTime,
                0,0,0,0,0,0
        );

        Constants.databaseReference().child(Constants.SPACE).child(Constants.auth().getCurrentUser().getUid())
                .child(id).setValue(model)
                .addOnSuccessListener(unused -> {})
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

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
                .compress(512)
                .maxResultSize(1080, 1080)
                .start(PICK_FROM_CAMERA);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_FROM_GALLERY) {
                try{
                    if (resultCode == RESULT_OK && data != null && data.getClipData() != null) {
                        binding.addImage.setVisibility(View.GONE);
                        binding.imageLay.setVisibility(View.VISIBLE);
                        int currentImage = 0;

                        while (currentImage < data.getClipData().getItemCount()) {
                            if (currentImage < limit){
                                imagesList.add(data.getClipData().getItemAt(currentImage).getUri());
                            }
                            currentImage++;
                        }

                        AddImageAdapter adapter = new AddImageAdapter(HostSpaceActivity.this, imagesList, binding.addImage, binding.imageLay);
                        binding.images.setAdapter(adapter);

                    } else {
                        Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                    }
                }  catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    binding.addImage.setVisibility(View.GONE);
                    binding.imageLay.setVisibility(View.VISIBLE);

                    //CarImagesModel model = new CarImagesModel(data.getData());
                    imagesList.add(data.getData());

                    AddImageAdapter adapter = new AddImageAdapter(HostSpaceActivity.this, imagesList, binding.addImage, binding.imageLay);
                    binding.images.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
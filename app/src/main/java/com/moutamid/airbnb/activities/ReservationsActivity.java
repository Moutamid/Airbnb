package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.ReservationAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityReservationsBinding;
import com.moutamid.airbnb.models.ReservationModel;

import java.util.ArrayList;

public class ReservationsActivity extends AppCompatActivity {
    ActivityReservationsBinding binding;
    ArrayList<ReservationModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReservationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> {
            finish();
        });

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

        list = new ArrayList<>();

        Constants.initDialog(this);
        Constants.showDialog();

        getData();

    }

    private void getData() {
        Constants.databaseReference().child(Constants.Reservations).child(Constants.INCOMING).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReservationModel model = snapshot.getValue(ReservationModel.class);
                            list.add(model);
                        }
                    }
                    if (list.size() > 0) {
                        binding.recyler.setVisibility(View.VISIBLE);
                        binding.noLayout.setVisibility(View.GONE);
                    } else {
                        binding.recyler.setVisibility(View.GONE);
                        binding.noLayout.setVisibility(View.VISIBLE);
                    }
                    Constants.dismissDialog();
                    ReservationAdapter adapter = new ReservationAdapter(binding.getRoot().getContext(), list);
                    binding.recyler.setAdapter(adapter);

                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
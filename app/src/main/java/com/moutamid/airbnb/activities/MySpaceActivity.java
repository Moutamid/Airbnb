package com.moutamid.airbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.ExploreAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityMySpaceBinding;
import com.moutamid.airbnb.models.SpaceModel;

import java.util.ArrayList;

public class MySpaceActivity extends AppCompatActivity {
    ActivityMySpaceBinding binding;
    ArrayList<SpaceModel> spaceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMySpaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

        binding.back.setOnClickListener(v -> finish());

        spaceList = new ArrayList<>();

        Constants.initDialog(this);
        Constants.showDialog();
        getData();

    }

    private void getData() {
        Constants.databaseReference().child(Constants.SPACE).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            spaceList.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                SpaceModel spaceModel = snapshot1.getValue(SpaceModel.class);
                                spaceList.add(spaceModel);
                            }
                        }
                        Constants.dismissDialog();

                        ExploreAdapter adapter = new ExploreAdapter(MySpaceActivity.this, spaceList);
                        binding.recyler.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(MySpaceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
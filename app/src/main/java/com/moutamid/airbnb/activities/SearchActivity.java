package com.moutamid.airbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.ExploreAdapter;
import com.moutamid.airbnb.adapters.SearchAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivitySearchBinding;
import com.moutamid.airbnb.models.SpaceModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    SearchAdapter adapter;
    ArrayList<SpaceModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        Constants.initDialog(this);
        Constants.showDialog();

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

        binding.back.setOnClickListener(v -> finish());

        binding.search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        getData();

    }

    private void getData() {
        Constants.databaseReference().child(Constants.SPACE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                    SpaceModel spaceModel = snapshot1.getValue(SpaceModel.class);
                                    list.add(spaceModel);
                                }
                            }
                        }
                        Constants.dismissDialog();

                        adapter = new SearchAdapter(SearchActivity.this, list);
                        binding.recyler.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
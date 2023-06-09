package com.moutamid.airbnb.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.activities.SearchActivity;
import com.moutamid.airbnb.adapters.ExploreAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.FragmentExploreBinding;
import com.moutamid.airbnb.models.SpaceModel;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {
    FragmentExploreBinding binding;
    ArrayList<SpaceModel> spaceList;
    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(getLayoutInflater(), container, false);

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(false);

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.auth().getCurrentUser().getUid());

        spaceList = new ArrayList<>();

        binding.search.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SearchActivity.class));
        });

        Constants.initDialog(requireContext());
        Constants.showDialog();
        getData();

        return binding.getRoot();
    }

    private void getData() {
        Constants.databaseReference().child(Constants.SPACE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            spaceList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                    SpaceModel spaceModel = snapshot1.getValue(SpaceModel.class);
                                    spaceList.add(spaceModel);
                                }
                            }
                        }
                        Constants.dismissDialog();

                        ExploreAdapter adapter = new ExploreAdapter(binding.getRoot().getContext(), spaceList);
                        binding.recycler.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
package com.moutamid.airbnb.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.ReservationAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.FragmentTipsBinding;
import com.moutamid.airbnb.models.ReservationModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TipsFragment extends Fragment {
    FragmentTipsBinding binding;
    ArrayList<ReservationModel> list;

    public TipsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTipsBinding.inflate(getLayoutInflater(), container, false);

        binding.recyler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyler.setHasFixedSize(false);

        list = new ArrayList<>();

        Constants.initDialog(requireContext());
        Constants.showDialog();

        getData();

        return binding.getRoot();
    }

    private void getData() {
        Constants.databaseReference().child(Constants.Reservations).child(Constants.auth().getCurrentUser().getUid())
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
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
package com.moutamid.airbnb.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.SplashScreenActivity;
import com.moutamid.airbnb.activities.FlightBookingActivity;
import com.moutamid.airbnb.activities.HostSpaceActivity;
import com.moutamid.airbnb.activities.MySpaceActivity;
import com.moutamid.airbnb.activities.ProfileActivity;
import com.moutamid.airbnb.activities.ReservationsActivity;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.FragmentProfileBinding;
import com.moutamid.airbnb.models.UserModel;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);

        Constants.initDialog(requireContext());
        Constants.showDialog();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        binding.reservation.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ReservationsActivity.class));
        });

        binding.flight.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), FlightBookingActivity.class));
        });

        binding.logout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", ((dialog, which) -> {
                        Constants.showDialog();
                        if (mGoogleSignInClient != null){
                            mGoogleSignInClient.signOut().addOnSuccessListener(unused -> {
                                Constants.dismissDialog();
                                Constants.auth().signOut();
                                startActivity(new Intent(requireContext(), SplashScreenActivity.class));
                                requireActivity().finish();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            new Handler().postDelayed(() -> {
                                Constants.dismissDialog();
                                Constants.auth().signOut();
                                startActivity(new Intent(requireContext(), SplashScreenActivity.class));
                                requireActivity().finish();
                            }, 1000);
                        }
                        dialog.dismiss();
                    })).setNegativeButton("Cancle", ((dialog, which) -> {
                        dialog.dismiss();
                    }))
                    .show();
        });

        binding.profileCard.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ProfileActivity.class));
        });

        binding.host.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), HostSpaceActivity.class));
        });
        binding.mySpace.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MySpaceActivity.class));
        });

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            binding.name.setText(userModel.getName());
                            binding.email.setText(userModel.getEmail());
                            Glide.with(requireContext()).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profileIcon);
                        }
                        Constants.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return binding.getRoot();
    }
}
package com.moutamid.airbnb.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.ExploreAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.FragmentWishlistBinding;
import com.moutamid.airbnb.models.SpaceModel;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {
    FragmentWishlistBinding binding;
    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishlistBinding.inflate(getLayoutInflater(), container, false);

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(false);

        ArrayList<SpaceModel> favrtList = Stash.getArrayList(Constants.WISHLIST, SpaceModel.class);
        ExploreAdapter adapter = new ExploreAdapter(requireContext(), favrtList);
        binding.recycler.setAdapter(adapter);

        return binding.getRoot();
    }
}
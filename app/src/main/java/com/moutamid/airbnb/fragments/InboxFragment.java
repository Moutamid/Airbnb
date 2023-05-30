package com.moutamid.airbnb.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.ChatListAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.FragmentInboxBinding;
import com.moutamid.airbnb.models.ChatListModel;
import com.moutamid.airbnb.models.ChatModel;

import java.util.ArrayList;


public class InboxFragment extends Fragment {
    FragmentInboxBinding binding;
    ArrayList<ChatListModel> list;
    public InboxFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInboxBinding.inflate(getLayoutInflater(), container, false);

        Constants.initDialog(requireContext());
        Constants.showDialog();

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(false);

        list = new ArrayList<>();

        getData();

        return binding.getRoot();
    }

    private void getData() {
        Constants.databaseReference().child(Constants.CHAT_LIST).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                ChatListModel model = dataSnapshot.getValue(ChatListModel.class);
                                list.add(model);
                            }

                            Log.d("listSize", "ee : "+ snapshot.getChildrenCount());

                        }
                        Constants.dismissDialog();
                        ChatListAdapter adapter = new ChatListAdapter(binding.getRoot().getContext(), list);
                        binding.recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(binding.getRoot().getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
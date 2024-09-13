package com.moutamid.airbnb.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;

import com.fxn.stash.Stash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.moutamid.airbnb.adapters.ChatAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityChatScreenBinding;
import com.moutamid.airbnb.models.ChatModel;
import com.moutamid.airbnb.models.UserModel;
import com.moutamid.airbnb.notifications.FCMNotificationHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatScreenActivity extends AppCompatActivity {
    ActivityChatScreenBinding binding;
    ArrayList<ChatModel> list;

    UserModel loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = Stash.getString("userName");
        String image = Stash.getString("imageLink");
        binding.chatName.setText(name);

        list = new ArrayList<>();

        binding.back.setOnClickListener(v -> {
            finish();
        });

        String ID = Stash.getString("userID");

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        loginUser = dataSnapshot.getValue(UserModel.class);
                    }
                });

        getChat(ID);

        binding.send.setOnClickListener(v -> {
            if (!binding.message.getText().toString().isEmpty()){
                long date = new Date().getTime();
                ChatModel conversation = new ChatModel(
                        binding.message.getText().toString(),
                        Constants.auth().getCurrentUser().getUid(),
                        ID,
                        loginUser.getImage(),
                        date,
                        loginUser.getName()
                );
                Constants.databaseReference().child(Constants.CHAT).child(Constants.auth().getCurrentUser().getUid())
                        .child(ID)
                        .push()
                        .setValue(conversation)
                        .addOnSuccessListener(unused -> {
                            reciver(ID, date);
                        }).addOnFailureListener(e -> {

                        });
            }
        });

    }

    private void reciver(String ID, long date) {
        ChatModel conversation = new ChatModel(
                binding.message.getText().toString(),
                Constants.auth().getCurrentUser().getUid(),
                ID,
                loginUser.getImage(),
                date,
                loginUser.getName()
        );
        Constants.databaseReference().child(Constants.CHAT).child(ID)
                .child(Constants.auth().getCurrentUser().getUid())
                .push()
                .setValue(conversation)
                .addOnSuccessListener(unused -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("message", binding.message.getText().toString());
                    map.put("timeStamp", date);
                    binding.message.setText("");
                    Constants.databaseReference().child(Constants.CHAT_LIST).child(ID).child(Constants.auth().getCurrentUser().getUid()).updateChildren(map)
                            .addOnSuccessListener(unused1 -> {
                                Constants.databaseReference().child(Constants.CHAT_LIST).child(Constants.auth().getCurrentUser().getUid()).child(ID).updateChildren(map)
                                        .addOnSuccessListener(unused4 -> {
                                            new FCMNotificationHelper(ChatScreenActivity.this).sendNotification(
                                                    ID,loginUser.getName(),
                                                    map.get("message").toString()
                                            );
                                            /*new FcmNotificationsSender(
                                                    "/topics/" + ID,  loginUser.getName(),
                                                    map.get("message").toString(),   getApplicationContext(),
                                                    ChatScreenActivity.this).SendNotifications();*/
                                        });
                            });
                }).addOnFailureListener(e -> {

                });
    }

    private void getChat(String id) {
        Constants.databaseReference().child(Constants.CHAT).child(Constants.auth().getCurrentUser().getUid())
                .child(id)
                .addChildEventListener(new ChildEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){
                            ChatModel conversation = snapshot.getValue(ChatModel.class);
                            list.add(conversation);
                            list.sort(Comparator.comparing(ChatModel::getTimestamps));
                            ChatAdapter adapter = new ChatAdapter(ChatScreenActivity.this, list);
                            binding.recyler.setAdapter(adapter);
                            binding.recyler.scrollToPosition(list.size()-1);
                            adapter.notifyItemInserted(list.size()-1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
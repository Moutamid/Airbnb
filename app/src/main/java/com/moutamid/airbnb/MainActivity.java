package com.moutamid.airbnb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityMainBinding;
import com.moutamid.airbnb.fragments.ExploreFragment;
import com.moutamid.airbnb.fragments.InboxFragment;
import com.moutamid.airbnb.fragments.ProfileFragment;
import com.moutamid.airbnb.fragments.TipsFragment;
import com.moutamid.airbnb.fragments.WishlistFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.dark)));
        binding.bottomNav.setItemActiveIndicatorShapeAppearance(new ShapeAppearanceModel().withCornerSize(50).toBuilder().build());
        binding.bottomNav.setItemActiveIndicatorHeight(100);
        binding.bottomNav.setItemActiveIndicatorWidth(100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            ) {
                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS);
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.auth().getCurrentUser().getUid())
                .addOnSuccessListener(unused -> {
                    // Toast.makeText(this, "Subscribed", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });


        Constants.databaseReference().child("serverKey").get().addOnSuccessListener(dataSnapshot -> {
            String key = dataSnapshot.getValue().toString();
            Stash.put(Constants.KEY, key);
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ExploreFragment()).commit();
        binding.bottomNav.setSelectedItemId(R.id.nav_explore);

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_explore) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ExploreFragment()).commit();
            } else if (itemId == R.id.nav_wishlist) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new WishlistFragment()).commit();
            } else if (itemId == R.id.nav_tips) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new TipsFragment()).commit();
            } else if (itemId == R.id.nav_inbox) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new InboxFragment()).commit();
            } else if (itemId == R.id.nav_profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).commit();
            }
            return true;
        });

    }
}
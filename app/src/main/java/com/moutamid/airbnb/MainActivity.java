package com.moutamid.airbnb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.google.android.material.shape.ShapeAppearanceModel;
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
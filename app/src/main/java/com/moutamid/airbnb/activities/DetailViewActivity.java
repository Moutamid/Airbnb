package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityDetailViewBinding;
import com.moutamid.airbnb.models.SpaceModel;
import com.moutamid.airbnb.models.UserModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class DetailViewActivity extends AppCompatActivity {
    ActivityDetailViewBinding binding;
    UserModel userModel;
    ArrayList<String> images;
    boolean isFavrt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);
        Constants.showDialog();
        images = new ArrayList<>();

        binding.back.setOnClickListener(v -> {
            finish();
        });

        SpaceModel model = (SpaceModel) Stash.getObject(Constants.MODEL, SpaceModel.class);
        ArrayList<SpaceModel> wish = Stash.getArrayList(Constants.WISHLIST, SpaceModel.class);

        for (SpaceModel fvrtModel : wish){
            if (fvrtModel.getID().equals(model.getID())){
                binding.wishlist.setImageResource(R.drawable.heart_fill);
                isFavrt = true;
            }
        }

        binding.wishlist.setOnClickListener(v -> {
            ArrayList<SpaceModel> favrtList = Stash.getArrayList(Constants.WISHLIST, SpaceModel.class);
            if (isFavrt){
                for (int i = 0; i < favrtList.size(); i++) {
                    if (favrtList.get(i).getID().equals(model.getID())) {
                        Toast.makeText(DetailViewActivity.this, "removed", Toast.LENGTH_SHORT).show();
                        favrtList.remove(i);
                    }
                }
                binding.wishlist.setImageResource(R.drawable.heart_off);
                isFavrt = false;
                Stash.put(Constants.WISHLIST, favrtList);
            } else {
                Toast.makeText(DetailViewActivity.this, "added", Toast.LENGTH_SHORT).show();
                favrtList.add(model);
                Stash.put(Constants.WISHLIST, favrtList);
                binding.wishlist.setImageResource(R.drawable.heart_fill);
                isFavrt = true;
            }
        });

        Constants.databaseReference().child(Constants.USER).child(model.getUserID())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        userModel = dataSnapshot.getValue(UserModel.class);
                        getData(model);
                    }
                });

    }

    private void getData(SpaceModel model) {
        Constants.databaseReference().child(Constants.ProductImages).child(model.getUserID()).child(model.getID())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            images.add(snapshot.child("image").getValue(String.class));
                        }

                        binding.title.setText(model.getName());
                        binding.desc.setText(model.getDescription());
                        double average = 0F;
                        try {
                            average = ((5.0F * model.getStar5()) + (4.0F * model.getStar4()) + (3.0F * model.getStar3()) + (2.0F * model.getStar2()) + model.getStar1())
                                    / (model.getStar1() + model.getStar2() + model.getStar3() + model.getStar4() + model.getStar5());
                        } catch (Exception e) {
                        }

                        binding.rating.setText("" + average + "(" + model.getRatingCount() + " reviews)");
                        binding.date.setText(model.getStartDate() + " - " + model.getEndDate());
                        binding.location.setText(model.getCity() + ", " + model.getCountry());
                        binding.price.setText("$" + model.getPrice());
                        binding.guest.setText(model.getGuestNum() + " guests");
                        binding.bed.setText(model.getBedNum() + " bed");
                        binding.bath.setText(model.getBatNum() + " bath");

                        binding.userName.setText("Hosted by " + userModel.getName());
                        Glide.with(DetailViewActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profileImage);

                        SliderAdapter adapter = new SliderAdapter(images);
                        binding.imageSlider.setSliderAdapter(adapter);
                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
        private ArrayList<String> mSliderItems;

        public SliderAdapter(ArrayList<String> mSliderItems) {
            this.mSliderItems = mSliderItems;
        }

        @Override
        public SliderAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, parent, false);
            return new SliderAdapter.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapter.SliderAdapterVH viewHolder, final int position) {

            String sliderItem = mSliderItems.get(position);

            Glide.with(viewHolder.itemView)
                    .load(sliderItem)
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);

        }

        @Override
        public int getCount() {
            return mSliderItems.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
            ImageView imageViewBackground;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            }
        }

    }

}
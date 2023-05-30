package com.moutamid.airbnb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.adapters.RatingAdapter;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityDetailViewBinding;
import com.moutamid.airbnb.models.ChatListModel;
import com.moutamid.airbnb.models.ChatModel;
import com.moutamid.airbnb.models.RatingModel;
import com.moutamid.airbnb.models.SpaceModel;
import com.moutamid.airbnb.models.UserModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DetailViewActivity extends AppCompatActivity {
    ActivityDetailViewBinding binding;
    UserModel userModel, loginUser;
    ArrayList<String> images;
    boolean isFavrt = false;
    ArrayList<RatingModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);
        Constants.showDialog();

        images = new ArrayList<>();
        list = new ArrayList<>();

        binding.recylerReview.setLayoutManager(new LinearLayoutManager(this));
        binding.recylerReview.setHasFixedSize(false);

        binding.back.setOnClickListener(v -> {
            finish();
        });

        SpaceModel model = (SpaceModel) Stash.getObject(Constants.MODEL, SpaceModel.class);
        ArrayList<SpaceModel> wish = Stash.getArrayList(Constants.WISHLIST, SpaceModel.class);

        binding.review.setOnClickListener(v -> {
            addReview(model, userModel);
        });

        binding.chatBtn.setOnClickListener(v -> {
            Constants.showDialog();
            chatWithHoster(model);
        });

        getRating(model);

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

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        loginUser = dataSnapshot.getValue(UserModel.class);
                    }
                });

    }

    private void chatWithHoster(SpaceModel model) {
        long date = new Date().getTime();
        ChatModel conversation = new ChatModel(
                "Hey There!",
                Constants.auth().getCurrentUser().getUid(),
                model.getUserID(),
                loginUser.getImage(),
                date,
                loginUser.getName()
        );
        Constants.databaseReference().child(Constants.CHAT).child(Constants.auth().getCurrentUser().getUid())
                .child(model.getUserID())
                .push()
                .setValue(conversation)
                .addOnSuccessListener(unused -> {
                    sellerSide(model, date);
                }).addOnFailureListener(e -> {

                });
    }

    private void sellerSide(SpaceModel model, long date) {
        ChatModel conversation = new ChatModel(
                "Hey There!",
                Constants.auth().getCurrentUser().getUid(),
                model.getUserID(),
                userModel.getImage(),
                date,
                userModel.getName()
        );
        Constants.databaseReference().child(Constants.CHAT).child(model.getUserID())
                .child(Constants.auth().getCurrentUser().getUid())
                .push()
                .setValue(conversation)
                .addOnSuccessListener(unused -> {

                    ChatListModel chatListModel = new ChatListModel(
                            model.getUserID(), userModel.getImage(), userModel.getName(), "Hey There!", date
                    );

                    Constants.databaseReference().child(Constants.CHAT_LIST).child(Constants.auth().getCurrentUser().getUid())
                            .child(userModel.getID()).setValue(chatListModel)
                            .addOnSuccessListener(unused1 -> {
                                ChatListModel chatList = new ChatListModel(
                                        Constants.auth().getCurrentUser().getUid(), loginUser.getImage(), loginUser.getName(), "Hey There!", date
                                );

                                Constants.databaseReference().child(Constants.CHAT_LIST).child(userModel.getID())
                                        .child(Constants.auth().getCurrentUser().getUid()).setValue(chatList)
                                        .addOnSuccessListener(unused2 -> {
                                                Constants.dismissDialog();
                                                Stash.put("userID", model.getUserID());
                                                Stash.put("userName", userModel.getName());
                                                startActivity(new Intent(DetailViewActivity.this, ChatScreenActivity.class));
                                        });
                            });

                }).addOnFailureListener(e -> {

                });
    }

    private void getRating(SpaceModel model) {
        Constants.databaseReference().child(Constants.Rating).child(model.getID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                RatingModel model1 = dataSnapshot.getValue(RatingModel.class);
                                list.add(model1);
                            }
                        }

                        if (list.size() > 0){
                            binding.noReviewLay.setVisibility(View.GONE);
                            binding.recylerReview.setVisibility(View.VISIBLE);
                        } else {
                            binding.noReviewLay.setVisibility(View.VISIBLE);
                            binding.recylerReview.setVisibility(View.GONE);
                        }

                        RatingAdapter adapter = new RatingAdapter(DetailViewActivity.this, list);
                        binding.recylerReview.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailViewActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addReview(SpaceModel model, UserModel userModel) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_review);
        dialog.setCancelable(true);

        AtomicInteger starCount = new AtomicInteger();
        starCount.set(0);

        EditText message = dialog.findViewById(R.id.message);
        Button cancel = dialog.findViewById(R.id.cancelBtn);
        Button add = dialog.findViewById(R.id.addBtn);

        ImageView star1, star2, star3, star4, star5;

        star1 = dialog.findViewById(R.id.star1);
        star2 = dialog.findViewById(R.id.star2);
        star3 = dialog.findViewById(R.id.star3);
        star4 = dialog.findViewById(R.id.star4);
        star5 = dialog.findViewById(R.id.star5);

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        add.setOnClickListener(v -> {
            Map<String, Object> rating = new HashMap<>();
            if (starCount.get() == 0 || message.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Add A Review First", Toast.LENGTH_SHORT).show();
            } else {
                Constants.showDialog();
                dialog.dismiss();
                RatingModel ratingModel = new RatingModel(
                        userModel.getImage(), userModel.getName(), message.getText().toString(),
                        starCount.get(), new Date().getTime()
                );

                if (starCount.get() == 5) {
                    rating.put("star5", (model.getStar5() + 1));
                } else if (starCount.get() == 4) {
                    rating.put("star4", (model.getStar4() + 1));
                } else if (starCount.get() == 3) {
                    rating.put("star3", (model.getStar3() + 1));
                } else if (starCount.get() == 2) {
                    rating.put("star2", (model.getStar2() + 1));
                } else if (starCount.get() == 1) {
                    rating.put("star1", (model.getStar1() + 1));
                }
                rating.put("ratingCount", (model.getRatingCount() + 1));

                Constants.databaseReference().child(Constants.SPACE).child(model.getUserID())
                        .child(model.getID()).updateChildren(rating)
                        .addOnSuccessListener(unused -> {
                            Constants.databaseReference().child(Constants.Rating).child(model.getID()).push()
                                    .setValue(ratingModel).addOnSuccessListener(unused1 -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(DetailViewActivity.this, "Thanks for your rating", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(e -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(DetailViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(DetailViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        star1.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(1);
        });
        star2.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(2);
        });
        star3.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(3);
        });
        star4.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(4);
        });
        star5.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            starCount.set(5);
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

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
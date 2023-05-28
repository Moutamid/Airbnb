package com.moutamid.airbnb.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.airbnb.R;

import java.util.ArrayList;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ImageViewHolder>  {
    Context context;
    ArrayList<Uri> ImagesList;
    MaterialCardView addPhotoLayout, addPhotoLayoutRecycler;

    private final int limit = 6;

    public AddImageAdapter(Context context, ArrayList<Uri> ImagesList) {
        this.context = context;
        this.ImagesList = ImagesList;
    }

    public AddImageAdapter(Context context, ArrayList<Uri> ImagesList, MaterialCardView addPhotoLayout, MaterialCardView addPhotoLayoutRecycler) {
        this.context = context;
        this.ImagesList = ImagesList;
        this.addPhotoLayout = addPhotoLayout;
        this.addPhotoLayoutRecycler = addPhotoLayoutRecycler;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageURI(ImagesList.get(position));
        holder.itemView.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bs_image_menu);

            Button removeImg = dialog.findViewById(R.id.btnRemoveImage);
            Button cancel = dialog.findViewById(R.id.close);

            cancel.setOnClickListener(can -> {
                dialog.cancel();
            });

            removeImg.setOnClickListener(remove -> {
                try{
                    if(ImagesList.size() == 1){
                        ImagesList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        dialog.cancel();
                        if (ImagesList.isEmpty()) {
                            addPhotoLayoutRecycler.setVisibility(View.GONE);
                            addPhotoLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    ImagesList.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    dialog.cancel();
                } catch (Exception e){

                }

            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        });
    }

    @Override
    public int getItemCount() {
        if (ImagesList.size() > limit){
            return limit;
        }
        return ImagesList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageView);
        }

    }
}

package com.moutamid.airbnb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.models.RatingModel;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingVH> {

    Context context;
    ArrayList<RatingModel> list;

    public RatingAdapter(Context context, ArrayList<RatingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RatingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RatingVH(LayoutInflater.from(context).inflate(R.layout.rating_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RatingVH holder, int position) {
        RatingModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.name.setText(model.getName());
        holder.date.setText(Constants.getFormatedDate(model.getTimeStamp()));
        holder.desc.setText(model.getDescription());

        Glide.with(context).load(model.getImage()).placeholder(R.drawable.profile_icon).into(holder.image);

         if (model.getStarCount() == 1) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (model.getStarCount() == 2) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (model.getStarCount() == 3) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (model.getStarCount() == 4) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (model.getStarCount() == 5) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RatingVH extends RecyclerView.ViewHolder{

        ImageView image, star1, star2, star3, star4, star5;
        TextView name, desc, date;

        public RatingVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }

}

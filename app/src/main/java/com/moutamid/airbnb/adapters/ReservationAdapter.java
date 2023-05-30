package com.moutamid.airbnb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.activities.DetailViewActivity;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.models.ReservationModel;
import com.moutamid.airbnb.models.SpaceModel;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationVH> {
    Context context;
    ArrayList<ReservationModel> list;

    public ReservationAdapter(Context context, ArrayList<ReservationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReservationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReservationVH(LayoutInflater.from(context).inflate(R.layout.hotel_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationVH holder, int position) {
        SpaceModel model = list.get(holder.getAbsoluteAdapterPosition()).getSpaceModel();
        ReservationModel reservationModel = list.get(holder.getAbsoluteAdapterPosition());

        holder.night.setVisibility(View.GONE);
        holder.price.setText("$" + reservationModel.getPrice());
        holder.title.setText(model.getName());
        holder.date.setText(reservationModel.getStartDate() + " - " + reservationModel.getEndDate());
        holder.location.setText(model.getCity() + ", " + model.getCountry());

        double average = 0F;
        try{
            average = ((5.0F * model.getStar5()) + (4.0F * model.getStar4()) + (3.0F * model.getStar3()) + (2.0F * model.getStar2()) + model.getStar1())
                    / (model.getStar1() + model.getStar2() + model.getStar3() + model.getStar4() + model.getStar5());
        } catch (Exception e) {}

        holder.rating.setText(""+average);

        ArrayList<SpaceModel> wish = Stash.getArrayList(Constants.WISHLIST, SpaceModel.class);

        for (SpaceModel fvrtModel : wish){
            if (fvrtModel.getID().equals(model.getID())){
                holder.wishList.setImageResource(R.drawable.heart_fill);
                holder.isFavrt = true;
            }
        }

        holder.wishList.setOnClickListener(v -> {
            ArrayList<SpaceModel> favrtList = Stash.getArrayList(Constants.WISHLIST, SpaceModel.class);
            if (holder.isFavrt){
                for (int i = 0; i < favrtList.size(); i++) {
                    if (favrtList.get(i).getID().equals(model.getID())) {
                        Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
                        favrtList.remove(i);
                    }
                }
                holder.wishList.setImageResource(R.drawable.heart_off);
                holder.isFavrt = false;
                Stash.put(Constants.WISHLIST, favrtList);
            } else {
                Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
                favrtList.add(model);
                Stash.put(Constants.WISHLIST, favrtList);
                holder.wishList.setImageResource(R.drawable.heart_fill);
                holder.isFavrt = true;
            }
        });

        Glide.with(context).load(model.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.MODEL, model);
            context.startActivity(new Intent(context, DetailViewActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReservationVH extends RecyclerView.ViewHolder{
        ImageView image, wishList;
        TextView title, date, rating, price, location, night;
        boolean isFavrt;
        public ReservationVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            night = itemView.findViewById(R.id.night);
            wishList = itemView.findViewById(R.id.wishList);

            isFavrt = false;

        }
    }

}

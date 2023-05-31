package com.moutamid.airbnb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.airbnb.R;
import com.moutamid.airbnb.models.FlightsModel;

import java.util.ArrayList;

public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.FlightsVH> {
    Context context;
    ArrayList<FlightsModel> list;

    public FlightsAdapter(Context context, ArrayList<FlightsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FlightsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FlightsVH(LayoutInflater.from(context).inflate(R.layout.flight_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FlightsVH holder, int position) {
            FlightsModel model = list.get(holder.getAbsoluteAdapterPosition());

            holder.airline.setText(model.getAirlines());
            holder.price.setText("$" + model.getPrice());
            holder.dest.setText(model.getDepartureAirportCode() + "-" + model.getArrivalAirportCode());
            holder.direct.setText(model.getStopoverCode());
            holder.duration.setText(model.getDuration());
            holder.time.setText(model.getDepartureTime() +" - " + model.getArrivalTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FlightsVH extends RecyclerView.ViewHolder{
        TextView airline, price, dest, time, direct, duration;
        public FlightsVH(@NonNull View itemView) {
            super(itemView);

            airline = itemView.findViewById(R.id.airline);
            price = itemView.findViewById(R.id.price);
            dest = itemView.findViewById(R.id.dest);
            time = itemView.findViewById(R.id.time);
            direct = itemView.findViewById(R.id.direct);
            duration = itemView.findViewById(R.id.duration);

        }
    }

}

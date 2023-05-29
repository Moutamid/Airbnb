package com.moutamid.airbnb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.airbnb.R;
import com.moutamid.airbnb.models.SpaceModel;

import java.util.ArrayList;
import java.util.Collection;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVH> implements Filterable {

    Context context;
    ArrayList<SpaceModel> names;
    ArrayList<SpaceModel> listAll;

    public SearchAdapter(Context context, ArrayList<SpaceModel> names) {
        this.context = context;
        this.names = names;
        this.listAll = new ArrayList<>(names);
    }

    @NonNull
    @Override
    public SearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchVH(LayoutInflater.from(context).inflate(R.layout.search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVH holder, int position) {
        SpaceModel name = names.get(holder.getAbsoluteAdapterPosition());

        holder.name.setText(name.getName());

        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return Math.min(names.size(), 10);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<SpaceModel> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterList.addAll(listAll);
            } else {
                for (SpaceModel listModel : listAll){
                    if (listModel.getCountry().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                         listModel.getCity().toLowerCase().contains(charSequence.toString().toLowerCase())
                    ){
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        //run on Ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            names.clear();
            names.addAll((Collection<? extends SpaceModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class SearchVH extends RecyclerView.ViewHolder{
        TextView name;
        public SearchVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}

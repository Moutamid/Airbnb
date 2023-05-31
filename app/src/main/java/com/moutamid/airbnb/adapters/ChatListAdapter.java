package com.moutamid.airbnb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.activities.ChatScreenActivity;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.models.ChatListModel;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListVH> {
    Context context;
    ArrayList<ChatListModel> list;

    public ChatListAdapter(Context context, ArrayList<ChatListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListVH(LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListVH holder, int position) {
        ChatListModel model = list.get(holder.getAbsoluteAdapterPosition());
        Glide.with(context).load(model.getImage()).placeholder(R.drawable.profile_icon).into(holder.imageView);
        holder.name.setText(model.getName());
        holder.message.setText(model.getMessage());

        String today = Constants.getFormatedDate(new Date().getTime());
        String message = Constants.getFormatedDate(model.getTimeStamp());
        String time = Constants.getFormatedTime(new Date().getTime());
        String ttt = Constants.getFormatedTime(model.getTimeStamp());

        if(time.equals(ttt)) {
            holder.date.setText("Now");
        } else if (today.equals(message)){
            holder.date.setText("Today");
        } else {
            holder.date.setText(Constants.getDate(model.getTimeStamp()));
        }

        holder.itemView.setOnClickListener(v -> {
            Stash.put("userID", model.getID());
            Stash.put("userName", model.getName());
            context.startActivity(new Intent(context, ChatScreenActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatListVH extends RecyclerView.ViewHolder{
        CircleImageView imageView; TextView name, message, date;
        public ChatListVH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }
    }

}

package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.R;
import java.util.List;

public class ProfileItemAdapter extends RecyclerView.Adapter<ProfileItemAdapter.ProfileItemViewHolder> {
    private ProfileItemViewHolder profileItemViewHolder;
    private String[] items;
    private Context context;

    public ProfileItemAdapter(Context context, String... items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileItemAdapter.ProfileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_item, parent, false);

        profileItemViewHolder = new ProfileItemViewHolder(view);
        return profileItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileItemAdapter.ProfileItemViewHolder holder, int position) {
        holder.name.setText(items[position]);
        switch (position){
            case 0:
                holder.profile_context.setText("Full Name");
                break;
            case 1:
                holder.profile_context.setText("Username");
                holder.profile_icon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_verified_user_24));
                break;
            case 2:
                holder.profile_context.setText("Email");
                holder.profile_icon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_alternate_email_24));
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ProfileItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView profile_icon;
        private ImageButton profile_action;
        private TextView profile_context;

        public ProfileItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.profile_name);
            profile_icon = itemView.findViewById(R.id.profile_icon);
            profile_action = itemView.findViewById(R.id.profile_action);
            profile_context = itemView.findViewById(R.id.profile_context);
        }
    }
}
package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import org.jetbrains.annotations.NotNull;

public class MiscellaneousAdapter extends RecyclerView.Adapter<MiscellaneousAdapter.MiscellaneousViewHolder> {

    private final Context context;
    private final String[] misc = {"History", "Downloads", "Your videos", "Liked Videos"};

    public MiscellaneousAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MiscellaneousViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_miscellaneous_item, parent, false);
        return new MiscellaneousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MiscellaneousViewHolder holder, int position) {
        holder.misc_text_view.setText(misc[position]);
        switch (position){
            case 0:
                holder.misc_icon.setBackground(context.getDrawable(R.drawable.ic_baseline_history_24));
                break;
            case 1:
                holder.misc_icon.setBackground(context.getDrawable(R.drawable.ic_baseline_cloud_download_24));
                break;
            case 2:
                holder.misc_icon.setBackground(context.getDrawable(R.drawable.ic_baseline_live_tv_24));
                break;
            case 3:
                holder.misc_icon.setBackground(context.getDrawable(R.drawable.ic_baseline_thumb_up_24));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return misc.length;
    }

    protected static class MiscellaneousViewHolder extends RecyclerView.ViewHolder{

        private final TextView misc_text_view;
        private final ImageView misc_icon;
        public MiscellaneousViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            misc_text_view = itemView.findViewById(R.id.miscellaneous_text);
            misc_icon = itemView.findViewById(R.id.miscellaneous_icon);
        }
    }
}

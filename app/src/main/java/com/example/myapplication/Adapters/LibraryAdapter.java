package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Factory.Factories.RecentAdapterFactory;
import com.example.myapplication.Factory.FactoryGenerator;
import com.example.myapplication.Factory.TYPE;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;
import com.example.myapplication.Utils.HorizontalItemDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private static final int RECENTS_VIEW = 0;
    private static final int MISC_VIEW = 1;
    MainActivityContext mainActivityContext;

    public LibraryAdapter(Context context, MainActivityContext mainActivityContext){
        this.context = context;
        this.mainActivityContext = mainActivityContext;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case RECENTS_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_view, parent, false);
                return new RecentsViewHolder(view);
            case MISC_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_miscellaneous_view, parent, false);
                return new MiscViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        switch (position){
            case 0:
                RecentsViewHolder recentsViewHolder = (RecentsViewHolder) holder;
                RecentsAdapter adapter = (Objects.requireNonNull(FactoryGenerator.getFactory(TYPE.RECENTS))).getRecentAdapter(new ItemCallBack(), mainActivityContext);
                recentsViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recentsViewHolder.recyclerView.addItemDecoration(new HorizontalItemDecoration());
                recentsViewHolder.recyclerView.setAdapter(adapter);
                break;
            case 1:
                MiscViewHolder miscViewHolder = (MiscViewHolder) holder;
                MiscellaneousAdapter miscAdapter = (Objects.requireNonNull(FactoryGenerator.getFactory(TYPE.MISC))).getMiscAdapter(context);
                miscViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                miscViewHolder.recyclerView.setAdapter(miscAdapter);
                miscAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return RECENTS_VIEW;
            case 1:
                return MISC_VIEW;
            default:
                return super.getItemViewType(position);
        }
    }

    public static class RecentsViewHolder extends RecyclerView.ViewHolder {

        private TextView recents_holder_header;
        private RecyclerView recyclerView;

        public RecentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recents_recycler_view);
        }
    }

    public static class MiscViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        public MiscViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.miscellaneous_recycler_view);
        }
    }

    private static class ItemCallBack extends DiffUtil.ItemCallback<MovieModel>{

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull MovieModel oldItem, @NonNull @NotNull MovieModel newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull MovieModel oldItem, @NonNull @NotNull MovieModel newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}

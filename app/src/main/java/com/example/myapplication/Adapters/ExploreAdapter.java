package com.example.myapplication.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.R;
import com.example.myapplication.Utils.HorizontalItemDecoration;
import com.example.myapplication.Utils.VideosItemCallback;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder> {

    private final Context context;
    private final Map<String, List<VideoModel>> data;
    private final List<String> keys;
    private final MainActivityContext mainActivityContext;
    private final VideosItemCallback videosItemCallback = VideosItemCallback.newInstance();
    private static final Map<String, Parcelable> recycler_states = new HashMap<>();

    public ExploreAdapter(Map<String, List<VideoModel>> data, Context context, MainActivityContext mainActivityContext) {
        this.data = data;
        this.context = context;
        keys = new ArrayList<>(data.keySet());
        this.mainActivityContext = mainActivityContext;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull @NotNull ExploreViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) holder.recycler_view.getLayoutManager();
        if (linearLayoutManager != null) {
            recycler_states.put(holder.recycler_header.getText().toString(), linearLayoutManager.onSaveInstanceState());
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull @NotNull ExploreViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) holder.recycler_view.getLayoutManager();
        if (linearLayoutManager != null) {
            linearLayoutManager.onRestoreInstanceState(recycler_states.get(holder.recycler_header.getText().toString()));
        }
    }

    @NonNull
    @NotNull
    @Override
    public ExploreViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_recycler_item, parent, false);
        RecyclerView recyclerView = view.findViewById(R.id.explore_recycler_item_recycler_view);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        HorizontalItemDecoration horizontalItemDecoration = new HorizontalItemDecoration();
        recyclerView.addItemDecoration(horizontalItemDecoration);
        return new ExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ExploreViewHolder holder, int position) {
        holder.recycler_header.setText(keys.get(position));

        ExploreItemAdapter adapter = new ExploreItemAdapter(videosItemCallback, context, mainActivityContext);
        adapter.submitList(data.get(keys.get(position)));
        holder.recycler_view.setAdapter(adapter);
    }

    @Override
    public void onViewRecycled(@NonNull @NotNull ExploreViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ExploreViewHolder extends RecyclerView.ViewHolder {

        private TextView recycler_header;
        private RecyclerView recycler_view;

        public ExploreViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            recycler_header = itemView.findViewById(R.id.explore_recycler_item_text_view);
            recycler_view = itemView.findViewById(R.id.explore_recycler_item_recycler_view);
        }
    }
}

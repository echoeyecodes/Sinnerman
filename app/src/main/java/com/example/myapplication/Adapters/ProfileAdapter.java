package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.Utils.CustomItemDecoration;
import com.example.myapplication.Utils.IntegerToDp;
import org.jetbrains.annotations.NotNull;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private static final int PROFILE_LAYOUT = 0;
    private static final int USER_BIO_LAYOUT = 1;

    public ProfileAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case PROFILE_LAYOUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_bio, parent, false);
                return new ProfileHeaderViewHolder(view);
            case USER_BIO_LAYOUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_bio_recycler_view, parent, false);
                return new BioProfileViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        if(position == 1){
            ProfileItemAdapter profileAdapter = new ProfileItemAdapter(context, "Oluwafemi Obajuluwa", "echoeyecodes", "femiobajuluwa@gmail.com");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            CustomItemDecoration customItemDecoration = new CustomItemDecoration(IntegerToDp.intToDp(15));
            ((BioProfileViewHolder) holder).recyclerView.addItemDecoration(customItemDecoration);
            ((BioProfileViewHolder) holder).recyclerView.setLayoutManager(linearLayoutManager);
            ((BioProfileViewHolder) holder).recyclerView.setAdapter(profileAdapter);
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return PROFILE_LAYOUT;
            default:
                return USER_BIO_LAYOUT;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    protected static class BioProfileViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        public BioProfileViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.user_bio_recycler_view);
        }
    }

    protected static class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        public ProfileHeaderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(2);
        }
    }
}

package com.example.myapplication.BottomNavigationFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapters.LibraryAdapter;
import com.example.myapplication.Adapters.NotificationsAdapter;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.NotificationModel;
import com.example.myapplication.R;
import com.example.myapplication.RootBottomFragment;
import com.example.myapplication.Utils.CustomItemDecoration;
import com.example.myapplication.Utils.IntegerToDp;
import org.jetbrains.annotations.NotNull;


public class LibraryFragment extends RootBottomFragment {
    private RecyclerView recyclerView;
    private static LibraryFragment libraryFragment;
    public String TAG = "LIBRARY_FRAGMENT";

    public LibraryFragment() {
        TAG = "LIBRARY_FRAGMENT";
        // Required empty public constructor
    }

    public static LibraryFragment newInstance() {
        if(libraryFragment == null){
            libraryFragment = new LibraryFragment();
        }
        return libraryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mainActivityContext = (MainActivityContext) context;
        if(!(mainActivityContext instanceof MainActivity)){
            try {
                throw new Exception("You need to implement Toggle Full Screen");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fragment_notifications_recycler_view);
        final NotificationItemCallback notificationItemCallback = new NotificationItemCallback();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        NotificationsAdapter adapter = new NotificationsAdapter(notificationItemCallback, mainActivityContext);
        recyclerView.addItemDecoration(new CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(15)));
        recyclerView.setAdapter(adapter);
    }

    private static class NotificationItemCallback extends DiffUtil.ItemCallback<NotificationModel>{

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull NotificationModel oldItem, @NonNull @NotNull NotificationModel newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull NotificationModel oldItem, @NonNull @NotNull NotificationModel newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivityContext.setActiveBottomViewFragment(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        libraryFragment = null;
    }
}
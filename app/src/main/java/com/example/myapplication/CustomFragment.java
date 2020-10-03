package com.example.myapplication;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.Interface.MainActivityContext;
import org.jetbrains.annotations.NotNull;

public class CustomFragment extends Fragment {
    public MainActivityContext mainActivityContext;

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

}

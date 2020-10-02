package com.example.myapplication.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.Adapters.CommentsAdapter;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     CommentsDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class CommentsDialogFragment extends BottomSheetDialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = view.findViewById(R.id.comments_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
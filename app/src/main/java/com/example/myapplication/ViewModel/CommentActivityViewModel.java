package com.example.myapplication.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myapplication.Room.Dao.CommentDao;
import com.example.myapplication.Room.Entities.Comment;
import com.example.myapplication.Room.PersistenceDatabase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentActivityViewModel extends AndroidViewModel {
    private PersistenceDatabase persistenceDatabase;
    private CommentDao commentDao;

    public CommentActivityViewModel(@NonNull @NotNull Application application) {
        super(application);
        persistenceDatabase = PersistenceDatabase.getInstance(application);
        commentDao = persistenceDatabase.commentDao();
    }

    public LiveData<List<Comment>> getOwnComments(){
        return commentDao.getComments();
    }

}

package com.unitfactory.notesaplication.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;
    private ExecutorService executorService;

    public NoteRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDataBase mDb = NoteRoomDataBase.getDatabaseInstance(application);
        mNoteDao = mDb.noteDao();
        mAllNotes = mNoteDao.getAllNotes();

    }


    public LiveData<Note> getNoteById(final int id)
    {
        Future<LiveData<Note>> future = executorService.submit(new Callable<LiveData<Note>>() {
            @Override
            public LiveData<Note> call() throws Exception {
                return mNoteDao.getById(id);
            }
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }


    public void insert(final Note note)
    {

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mNoteDao.insert(note);
            }
        });
    }


    public void deleteAll()  {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteAll();
            }
        });
    }


    public void deleteNote(final Note note)
    {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteNote(note);
            }
        });
    }

    public void shutDown()
    {
        executorService.shutdown();
    }


    public void updateNote(final Note note) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mNoteDao.update(note);
            }
        });
    }
}

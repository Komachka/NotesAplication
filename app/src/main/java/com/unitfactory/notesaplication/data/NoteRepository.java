package com.unitfactory.notesaplication.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.unitfactory.notesaplication.Constants.ASCENDING;
import static com.unitfactory.notesaplication.Constants.DESCENDING;

public class NoteRepository {
    private NoteDao mNoteDao;
    //private LiveData<List<Note>> mAllNotes;

    private MediatorLiveData<List<Note>> allNotes = new MediatorLiveData<List<Note>>();

    private LiveData<List<Note>> notesAscending;
    private LiveData<List<Note>> notesDescending;

    private int currentOrder = ASCENDING;
    private ExecutorService executorService;

    public NoteRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDataBase mDb = NoteRoomDataBase.getDatabaseInstance(application);
        mNoteDao = mDb.noteDao();
        notesAscending = mNoteDao.allNotesAscending();
        notesDescending = mNoteDao.allNotesDescending();

        allNotes.addSource(notesAscending, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (currentOrder == ASCENDING) {
                    if (notes != null)
                        allNotes.setValue(notes);
                }
            }
        });

        allNotes.addSource(notesDescending, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (currentOrder == DESCENDING) {
                    if (notes != null)
                        allNotes.setValue(notes);
                }
            }
        });
        rearrange(ASCENDING);
    }


    public void  rearrange(int order)
    {
        if (order == ASCENDING)
        {
            allNotes.setValue(notesAscending.getValue());
        }
        else if (order == DESCENDING)
        {
            allNotes.setValue(notesDescending.getValue());
        }
        currentOrder = order;
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




    public MediatorLiveData<List<Note>> getAllNotes() {
        return allNotes;
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

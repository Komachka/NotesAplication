package com.unitfactory.notesaplication.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.unitfactory.notesaplication.model.Note;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class NoteRepository {
    private NoteDao mNoteDao;
    private ExecutorService executorService;

    public NoteRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDataBase mDb = NoteRoomDataBase.getDatabaseInstance(application);
        mNoteDao = mDb.noteDao();
    }

    public LiveData<Note> getNoteById(final int id) {
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

    public void insert(final Note note) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mNoteDao.insert(note);
            }
        });
    }


    public void deleteAll() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteAll();
            }
        });
    }


    public void deleteNote(final Note note) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteNote(note);
            }
        });
    }

    public void shutDown() {
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

    public NoteDataSourceFactory getNoteDataSourceFactory(String filter, int order) {
        return new NoteDataSourceFactory(mNoteDao, filter, order);
    }


    public LiveData<PagedList<Note>> createPageList(NoteDataSourceFactory dataSourceFactory, PagedList.Config config) {
        return new LivePagedListBuilder<>(dataSourceFactory, config)
                .build();
    }
}

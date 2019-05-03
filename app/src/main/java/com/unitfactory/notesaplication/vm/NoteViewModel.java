package com.unitfactory.notesaplication.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import com.unitfactory.notesaplication.database.NoteDataSourceFactory;
import com.unitfactory.notesaplication.database.NoteRepository;
import com.unitfactory.notesaplication.model.Note;

import static com.unitfactory.notesaplication.utils.Constants.ASCENDING;
import static com.unitfactory.notesaplication.utils.Constants.FULL_QUERY_TEXT;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;
    private LiveData<PagedList<Note>> concertList;
    private DataSource<Integer, Note> mostRecentDataSource;
    private String filter = FULL_QUERY_TEXT;
    private int currentOrder = ASCENDING;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        init();
    }

    private void invalidateDataSource() {
        if (mostRecentDataSource != null)
            mostRecentDataSource.invalidate();
    }

    private void init() {
        NoteDataSourceFactory dataSourceFactory = mRepository.getNoteDataSourceFactory(filter, currentOrder);
        mostRecentDataSource = dataSourceFactory.create();
        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                .setInitialLoadSizeHint(1)
                .setPageSize(2).build();
        concertList = mRepository.createPageList(dataSourceFactory, config);
    }

    public LiveData<Note> getNoteById(int id) {
        return mRepository.getNoteById(id);
    }


    public LiveData<String> getNoteTextById(int id) {
        return Transformations.map(mRepository.getNoteById(id), new Function<Note, String>() {
            @Override
            public String apply(Note note) {
                return note.getNote();
            }
        });
    }

    public void setFilter(String filter) {
        this.filter = filter;
        recreateDataSource();
    }

    public LiveData<PagedList<Note>> getAllNotes() {
        return concertList;
    }

    public void rearrange(int order) {
        currentOrder = order;
        recreateDataSource();
    }


    public void insert(Note note) {
        mRepository.insert(note);
    }

    public void deleteAll() {
        mRepository.deleteAll();
        recreateDataSource();
    }

    public void deleteNote(Note note) {
        mRepository.deleteNote(note);
        recreateDataSource();
    }

    public void shutDown() {
        mRepository.shutDown();
    }

    public void updateNote(Note note) {
        mRepository.updateNote(note);
    }

    private void recreateDataSource()
    {
        invalidateDataSource();
        init();
    }

}

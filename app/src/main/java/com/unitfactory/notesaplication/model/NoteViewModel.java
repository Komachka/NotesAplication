package com.unitfactory.notesaplication.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.unitfactory.notesaplication.data.Note;
import com.unitfactory.notesaplication.data.NoteDataSource;
import com.unitfactory.notesaplication.data.NoteDataSourceFactory;
import com.unitfactory.notesaplication.data.NoteRepository;

import java.util.List;

import static com.unitfactory.notesaplication.Constants.ASCENDING;
import static com.unitfactory.notesaplication.Constants.DESCENDING;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;
    /*private MediatorLiveData<List<Note>> allNotes = new MediatorLiveData<>();
    private LiveData<List<Note>> asc;
    private LiveData<List<Note>> desc;*/


    private LiveData<PagedList<Note>> concertList;
    private DataSource<Integer,Note> mostRecentDataSource;
    private String filter = "_";



    private int currentOrder = ASCENDING;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
       init();
    }

    public void invalidateDataSource() {
        if (mostRecentDataSource != null)
            mostRecentDataSource.invalidate();
    }

    private void init()
    {
        NoteDataSourceFactory dataSourceFactory = mRepository.getNoteDataSourceFactory(filter, currentOrder);
        mostRecentDataSource = dataSourceFactory.create();

        /*PagedList.Config myPagingConfig = new PagedList.Config.Builder()
                .setPageSize(1)
                .setInitialLoadSizeHint(5)
                .setEnablePlaceholders(true)
                .setPrefetchDistance(1)
                .build();
        concertList = new LivePagedListBuilder<>(dataSourceFactory, myPagingConfig)
                .build();*/




        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                .setInitialLoadSizeHint(1)
                .setPageSize(1).build();

        //create LiveData object using LivePagedListBuilder which takes
        //data source factory and page config as params
        concertList = new LivePagedListBuilder<>(dataSourceFactory, config).build();




    }

    public LiveData<Note> getNoteById(int id)
    {
        return mRepository.getNoteById(id);
    }


    public LiveData<String> getNoteTextById(int id)
    {
        return Transformations.map(mRepository.getNoteById(id), new Function<Note, String>() {
            @Override
            public String apply(Note note) {
                return note.getNote();
            }
        });
    }


    public void setFilter(String filter) {
        invalidateDataSource();
        this.filter = filter;
        init();
    }

    public LiveData<PagedList<Note>> getAllNotes()
    {
        return concertList;
    }

    public void  rearrange(int order)
    {
        invalidateDataSource();
        currentOrder = order;
        init();
    }


    public void insert(Note note) {mRepository.insert(note);}

    public void deleteAll() {mRepository.deleteAll();}

    public void deleteNote(Note note) {mRepository.deleteNote(note);}

    public void shutDown() { mRepository.shutDown(); }

    public void updateNote(Note note) {
        mRepository.updateNote(note);
    }

}

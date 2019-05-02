package com.unitfactory.notesaplication.database;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.unitfactory.notesaplication.model.Note;

public class NoteDataSourceFactory extends DataSource.Factory<Integer, Note> {

    private NoteDataSource latestSource;
    private NoteDao dao;
    private String filter;
    private int order;
    private MutableLiveData<NoteDataSource> sourceLiveData = new MutableLiveData<>();

    public NoteDataSourceFactory(NoteDao dao, String filter, int order) {
        this.dao = dao;
        this.filter = filter;
        this.order = order;
    }

    @NonNull
    @Override
    public DataSource<Integer, Note> create() {
        latestSource = new NoteDataSource(dao, order, filter);
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }
}

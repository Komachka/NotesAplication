package com.unitfactory.notesaplication.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class NoteDataSourceFactory extends DataSource.Factory<Integer, Note> {

    //private NoteDataSource latestSource;
    private com.unitfactory.notesaplication.data.DataSource latestSource;
    private NoteDao dao;
    private String filter;
    private int order;
    private MutableLiveData<com.unitfactory.notesaplication.data.DataSource> sourceLiveData = new MutableLiveData<>();

    public NoteDataSourceFactory(NoteDao dao, String filter, int order) {
        this.dao = dao;
        this.filter = filter;
        this.order = order;
    }

    @NonNull
    @Override
    public DataSource<Integer, Note> create() {
        //latestSource = new NoteDataSource(dao, filter, order);
        latestSource = new com.unitfactory.notesaplication.data.DataSource(dao, order, filter);
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }
}

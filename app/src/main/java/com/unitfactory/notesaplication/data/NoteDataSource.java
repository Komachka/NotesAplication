package com.unitfactory.notesaplication.data;
import android.util.Log;

import androidx.annotation.NonNull;
import 	androidx.paging.PositionalDataSource;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

import static com.unitfactory.notesaplication.Constants.ASCENDING;

public class NoteDataSource extends PositionalDataSource<Note> {
    private static final String TAG = NoteDataSource.class.getSimpleName();
    private NoteDao dao;
    private String currentOrder;
    private String filter;

    public NoteDataSource(NoteDao dao, String filter, int order) {
        this.dao = dao;
        if (order == ASCENDING) {
            currentOrder = "ASC";
        }
        else {
            currentOrder = "DESC";
        }

        this.filter = filter;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Note> callback) {
        int startPosition = params.requestedStartPosition;
        Log.d(TAG,"load initial start" + startPosition + " size " + params.requestedLoadSize + " page size " + params.pageSize);
        List<Note> notes = loadRangeInternal(startPosition, params.requestedLoadSize);
        Log.d(TAG,"notes " + notes.size());
        callback.onResult(notes, 0, notes.size());
    }

    private List<Note> loadRangeInternal(int position, int loadSize) {
        //String query = "SELECT * FROM note_table  WHERE note LIKE '%"+filter+"%' LIMIT "+loadSize+" OFFSET "+position+" ORDER BY date "+currentOrder;
        String query = "SELECT * FROM note_table  WHERE note LIKE '%"+filter+"%' ORDER BY date "+currentOrder+" LIMIT "+loadSize+" OFFSET "+position;
        //String query = "SELECT * FROM note_table  WHERE note LIKE '%"+filter+"%' ORDER BY date "+currentOrder;//+" LIMIT "+loadSize+" OFFSET "+position;
        return dao.allNotesWithFilterWithOrderAndPaging(new SimpleSQLiteQuery(query));

    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Note> callback) {
        List<Note> notes = loadRangeInternal(params.startPosition, params.loadSize);
        Log.d(TAG,"loadRange startPosition " + params.startPosition + " loadSize  " + params.loadSize);
        Log.d(TAG,"notes " + notes.size());
        callback.onResult(notes);
    }

    public String getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(String currentOrder) {
        this.currentOrder = currentOrder;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}

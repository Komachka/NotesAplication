package com.unitfactory.notesaplication.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.unitfactory.notesaplication.model.Note;

import java.util.List;

import static com.unitfactory.notesaplication.utils.Constants.ASCENDING;

public class NoteDataSource extends PageKeyedDataSource<Integer, Note> {
    private static final String TAG = NoteDataSource.class.getSimpleName();
    private NoteDao dao;
    private String currentOrder;
    private String filter;

    public NoteDataSource(NoteDao dao, int order, String filter) {
        this.dao = dao;
        currentOrder = (order == ASCENDING) ? "ASC" : "DESC";
        this.filter = filter;
    }

    private List<Note> loadRangeInternal(int position, int loadSize) {
        String query = "SELECT * FROM note_table  WHERE note LIKE '%" + filter + "%' ORDER BY date " + currentOrder + " LIMIT " + loadSize + " OFFSET " + position;
        return dao.allNotesWithFilterWithOrderAndPaging(new SimpleSQLiteQuery(query));
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Note> callback) {
        int startPosition = 0;
        List<Note> notes = loadRangeInternal(startPosition, params.requestedLoadSize);
        callback.onResult(notes, null,
                notes.size() + 1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Note> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Note> callback) {
        List<Note> notes = loadRangeInternal(params.key, params.requestedLoadSize);
        Log.d(TAG, "loadAfter params.key" + params.key + " params.requestedLoadSize  " + params.requestedLoadSize);
        Log.d(TAG, "notes.size() " + notes.size());
        int nextKey = params.key + notes.size();
        callback.onResult(notes, nextKey);
    }
}

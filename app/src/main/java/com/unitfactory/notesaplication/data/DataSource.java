package com.unitfactory.notesaplication.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

import static com.unitfactory.notesaplication.Constants.ASCENDING;

public class DataSource extends PageKeyedDataSource<Integer, Note> {
    private static final String TAG = NoteDataSource.class.getSimpleName();
    private NoteDao dao;
    private String currentOrder;
    private String filter;

    public DataSource(NoteDao dao, int order, String filter) {
        this.dao = dao;
        if (order == ASCENDING) {
            currentOrder = "ASC";
        }
        else {
            currentOrder = "DESC";
        }

        this.filter = filter;
    }

    private List<Note> loadRangeInternal(int position, int loadSize) {
        //String query = "SELECT * FROM note_table  WHERE note LIKE '%"+filter+"%' LIMIT "+loadSize+" OFFSET "+position+" ORDER BY date "+currentOrder;
        String query = "SELECT * FROM note_table  WHERE note LIKE '%"+filter+"%' ORDER BY date "+currentOrder+" LIMIT "+loadSize+" OFFSET "+position;
        //String query = "SELECT * FROM note_table  WHERE note LIKE '%"+filter+"%' ORDER BY date "+currentOrder;//+" LIMIT "+loadSize+" OFFSET "+position;
        return dao.allNotesWithFilterWithOrderAndPaging(new SimpleSQLiteQuery(query));

    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Note> callback) {
        int startPosition = 0;
        List<Note> notes = loadRangeInternal(startPosition, params.requestedLoadSize);
        int noOfTryies = 0;
        while(notes.size() == 0){
            notes = loadRangeInternal(startPosition, params.requestedLoadSize);
            noOfTryies++;
            if(noOfTryies == 6){
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {  }
        }

        callback.onResult(notes,null,
                notes.size()+1);


    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Note> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Note> callback) {
        List<Note> notes = loadRangeInternal(params.key, params.requestedLoadSize);
        Log.d(TAG,"loadRange startPosition " + params.key + " loadSize  " + params.requestedLoadSize);
        Log.d(TAG,"notes " + notes.size());
        int nextKey = params.key+notes.size();
        callback.onResult(notes, nextKey);
    }
}

package com.unitfactory.notesaplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

/*    @Query("SELECT * from note_table ORDER BY date ASC")
    LiveData<List<Note>> getNotesAscending();*/

    @Query("SELECT * from note_table WHERE id = :id")
    LiveData<Note> getById(int id);

    @Delete
    void deleteNote(Note note);

    @Update
    void update(Note note);

    @Query("SELECT * from note_table ORDER BY date ASC")
    LiveData<List<Note>> allNotesAscending();

    @Query("SELECT * from note_table ORDER BY date DESC")
    LiveData<List<Note>> allNotesDescending();

    @Query("SELECT * from note_table WHERE note LIKE :pattern ORDER BY date ASC")
    LiveData<List<Note>> setQueryFilterAsc(String pattern);

    @Query("SELECT * from note_table WHERE note LIKE :pattern ORDER BY date DESC")
    LiveData<List<Note>> setQueryFilterDesc(String pattern);

}

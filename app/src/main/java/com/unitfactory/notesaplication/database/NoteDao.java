package com.unitfactory.notesaplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.unitfactory.notesaplication.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("SELECT * from note_table WHERE id = :id")
    LiveData<Note> getById(int id);

    @Delete
    void deleteNote(Note note);

    @Update
    void update(Note note);

    @RawQuery(observedEntities = Note.class)
    List<Note> allNotesWithFilterWithOrderAndPaging(SupportSQLiteQuery query);
}

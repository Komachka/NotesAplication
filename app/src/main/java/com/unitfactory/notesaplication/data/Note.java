package com.unitfactory.notesaplication.data;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;


@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int Id;

    @NonNull
    @ColumnInfo(name = "note")
    private String mNote;


    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "creating_date")
    private Date creatingDate;


    public Note(@NonNull String note) {
        this.mNote = note;
        this.creatingDate = new Date();
    }

    @Ignore //is annotated using @Ignore, because Room expects only one constructor by default in an entity class.
    public Note(int id, @NonNull String note)
    {
        this.Id = id;
        this.mNote = note;
        this.creatingDate = new Date();

    }


    public String getNote() {
        return mNote;
    }

    public int getId() {
        return Id;
    }


}

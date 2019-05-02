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
    @ColumnInfo(name = "date")
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

    public void setId(int id) {
        Id = id;
    }

    public void setmNote(@NonNull String mNote) {
        this.mNote = mNote;
    }

    public void setCreatingDate(Date creatingDate) {
        this.creatingDate = creatingDate;
    }

    @NonNull
    public String getmNote() {
        return mNote;
    }

    public Date getCreatingDate() {
        return creatingDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Note note = (Note) obj;
        return note.getId() == this.getId() &&
                note.getNote() == note.getNote() &&
                note.getCreatingDate() == note.getCreatingDate();
    }
}

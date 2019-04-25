package com.unitfactory.notesaplication.data;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey
    @NonNull

    @ColumnInfo(name = "id")
    private int Id;

    @NonNull
    @ColumnInfo(name = "note")
    private String mNote;

    public Note(@NonNull String note) {
        this.mNote = note;
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
}

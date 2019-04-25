package com.unitfactory.notesaplication.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


//TODO read more about this class
@Database(entities = {Note.class}, version = 1)
public abstract class NoteRoomDataBase extends RoomDatabase {

    private static NoteRoomDataBase instance;
    public abstract NoteDao noteDao();

    public static NoteRoomDataBase getDatabaseInstance(final Context context)
    {
        if (instance == null)
        {
            synchronized (NoteRoomDataBase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDataBase.class, "note_database")
                            .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object. Migration is not part of this practical.
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return instance;
    }



    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            System.out.println("Do smth in background");
        }
    };

}


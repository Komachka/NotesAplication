package com.unitfactory.notesaplication.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        NoteRoomDataBase mDb = NoteRoomDataBase.getDatabaseInstance(application);
        mNoteDao = mDb.noteDao(); // TODO how can I call abstract method?????
        mAllNotes = mNoteDao.getAllNotes();

    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }


    public void insert(Note note)
    {
        new InsertAsyncTask(mNoteDao).execute(note);
    }



    private static class InsertAsyncTask extends AsyncTask<Note, Void, Void>
    {

        private NoteDao mAsyncTaskDao;

        public InsertAsyncTask(NoteDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }


    public void deleteAll()  {
        new deleteAllWordsAsyncTask(mNoteDao).execute();
    }

    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void deleteNote(Note note)
    {
        new deleteNoteAsyncTask(mNoteDao).execute(note);
    }

    private static class deleteNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NoteDao mAsyncTaskDao;

        public deleteNoteAsyncTask(NoteDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncTaskDao.deleteNote(notes[0]);
            return null;
        }
    }



}

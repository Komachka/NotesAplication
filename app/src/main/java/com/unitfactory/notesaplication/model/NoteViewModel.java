package com.unitfactory.notesaplication.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.unitfactory.notesaplication.data.Note;
import com.unitfactory.notesaplication.data.NoteRepository;

import java.util.Collections;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;
    private LiveData<List<Note>> mAllNotes;// = new MutableLiveData<>();

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<Note> getNoteById(int id)
    {
        return mRepository.getNoteById(id);
    }



  /*  public void notesReverse()
    {
        List<Note> notes = mAllNotes.getValue();
        Collections.reverse(notes);
        //mAllNotes.postValue(notes);
    }*/

    public LiveData<String> getNoteTextById(int id)
    {
        return Transformations.map(mRepository.getNoteById(id), new Function<Note, String>() {
            @Override
            public String apply(Note note) {
                return note.getNote();
            }
        });
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(Note note) {mRepository.insert(note);}

    public void deleteAll() {mRepository.deleteAll();}

    public void deleteNote(Note note) {mRepository.deleteNote(note);}

    public void shutDown() { mRepository.shutDown(); }

    public void updateNote(Note note) {
        mRepository.updateNote(note);
    }
}

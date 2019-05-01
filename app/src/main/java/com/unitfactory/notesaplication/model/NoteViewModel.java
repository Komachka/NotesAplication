package com.unitfactory.notesaplication.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.unitfactory.notesaplication.data.Note;
import com.unitfactory.notesaplication.data.NoteRepository;

import java.util.ArrayList;
import java.util.List;

import static com.unitfactory.notesaplication.Constants.ASCENDING;
import static com.unitfactory.notesaplication.Constants.DESCENDING;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;
    private MediatorLiveData<List<Note>> allNotes = new MediatorLiveData<>();
    private LiveData<List<Note>> asc;
    private LiveData<List<Note>> desc;

    private int currentOrder = ASCENDING;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);

    }


    public LiveData<Note> getNoteById(int id)
    {
        return mRepository.getNoteById(id);
    }


    public LiveData<String> getNoteTextById(int id)
    {
        return Transformations.map(mRepository.getNoteById(id), new Function<Note, String>() {
            @Override
            public String apply(Note note) {
                return note.getNote();
            }
        });
    }



    public MutableLiveData<List<Note>> getAllNotes(String pattern)
    {
            asc = mRepository.getNotesAscending(pattern);
            desc = mRepository.getNotesDescending(pattern);
            allNotes.addSource(asc, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    if (currentOrder == ASCENDING) {
                        if (notes != null)
                            allNotes.setValue(notes);
                    }
                }
            });
            allNotes.addSource(desc, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    if (currentOrder == DESCENDING) {
                        if (notes != null)
                            allNotes.setValue(notes);
                    }
                }
            });
            rearrange(ASCENDING);


        return allNotes;
    }

    public void  rearrange(int order)
    {
        if (order == ASCENDING)
        {
            allNotes.setValue(asc.getValue());
        }
        else if (order == DESCENDING)
        {

            allNotes.setValue(desc.getValue());
        }
        currentOrder = order;
    }




    public void insert(Note note) {mRepository.insert(note);}

    public void deleteAll() {mRepository.deleteAll();}

    public void deleteNote(Note note) {mRepository.deleteNote(note);}

    public void shutDown() { mRepository.shutDown(); }

    public void updateNote(Note note) {
        mRepository.updateNote(note);
    }
}

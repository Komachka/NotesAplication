package com.unitfactory.notesaplication.view;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.unitfactory.notesaplication.Constants;
import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.data.Note;
import com.unitfactory.notesaplication.model.NoteViewModel;

import java.util.List;


public class NoteDetailsActivity extends AppCompatActivity {


    private EditText mEditWordView;
    private NoteViewModel mNoteViewModel;
    private int noteId;
    private Note currentNote = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mEditWordView = findViewById(R.id.edit_word);

        noteId =  getIntent().getIntExtra(Constants.NOTE_EXTRAS_KEY, -1);

        if (noteId != -1)
        {
            mNoteViewModel.getNoteById(noteId).observe(this, new Observer<Note>() {
                @Override
                public void onChanged(Note n) {
                    currentNote = n;
                    mEditWordView.setText(n.getNote());
                }
            });
        }

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String note = mEditWordView.getText().toString();
                    if (currentNote != null)
                    {
                        currentNote.setmNote(note);
                        mNoteViewModel.updateNote(currentNote);
                    }
                    else
                    {
                        mNoteViewModel.insert(new Note(note));
                    }
                    replyIntent.putExtra(Constants.EXTRA_REPLY_NOTE, note);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}

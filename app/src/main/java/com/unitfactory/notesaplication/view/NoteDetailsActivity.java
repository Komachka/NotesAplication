package com.unitfactory.notesaplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.unitfactory.notesaplication.utils.Constants;
import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.model.Note;
import com.unitfactory.notesaplication.vm.NoteViewModel;

import static com.unitfactory.notesaplication.utils.Constants.RESULT_DELETED;


public class NoteDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditWordView;
    private NoteViewModel mNoteViewModel;
    private Note currentNote = null;
    private ImageButton delBut;
    private ImageButton shareBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mEditWordView = findViewById(R.id.edit_word);
        delBut = findViewById(R.id.delete_but);
        delBut.setOnClickListener(this);
        shareBut = findViewById(R.id.share_but);
        shareBut.setOnClickListener(this);
        int noteId = getIntent().getIntExtra(Constants.NOTE_EXTRAS_KEY, -1);
        if (noteId != -1) {
            mNoteViewModel.getNoteById(noteId).observe(this, new Observer<Note>() {
                @Override
                public void onChanged(Note n) {
                    if (n != null)
                    {
                        currentNote = n;
                        mEditWordView.setText(n.getNote());
                    }
                }
            });
        }
        ;
    }

    @Override
    public void onBackPressed() {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditWordView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String note = mEditWordView.getText().toString();
            if (currentNote != null) {
                currentNote.setNote(note);
                mNoteViewModel.updateNote(currentNote);
            } else {
                mNoteViewModel.insert(new Note(note));
            }
            replyIntent.putExtra(Constants.EXTRA_REPLY_NOTE, note);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share_but)
        {
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                Toast.makeText(this, R.string.empty_not_share, Toast.LENGTH_LONG).show();
                return;
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mEditWordView.getText() + "\nSending from " + getApplicationContext().getResources().getString(R.string.app_name));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }
        if (v.getId() == R.id.delete_but)
        {
            if (currentNote != null) {
                mNoteViewModel.deleteNote(currentNote);
            }
            Intent replyIntent = new Intent();
            setResult(RESULT_DELETED, replyIntent);
            finish();
        }
    }
}

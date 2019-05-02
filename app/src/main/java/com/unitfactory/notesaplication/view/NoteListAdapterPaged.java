package com.unitfactory.notesaplication.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.unitfactory.notesaplication.utils.Constants;
import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.model.Note;

import static com.unitfactory.notesaplication.database.DateConverter.getFormattedDate;
import static com.unitfactory.notesaplication.database.DateConverter.getFormattedTime;

public class NoteListAdapterPaged extends PagedListAdapter<Note, NoteViewHolder> {


    protected NoteListAdapterPaged() {
        super(DIFF_CALLBACK);
    }
    public static DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        if (note != null) {
            holder.bindTO(note);
        }
    }

    public Note getNoteOnPosition(int position) {
        return getItem(position);
    }
}


class NoteViewHolder extends RecyclerView.ViewHolder {
    private final TextView mNoteItemText;
    private final TextView mNoteItemDate;
    private final TextView mNoteItemTime;

    NoteViewHolder(View view) {
        super(view);
        mNoteItemText = view.findViewById(R.id.textView);
        mNoteItemDate = view.findViewById(R.id.textViewDate);
        mNoteItemTime = view.findViewById(R.id.textViewTime);
    }

    void bindTO(final Note currentNote) {
        if (currentNote != null) {
            mNoteItemText.setText(currentNote.getNote().replace("\n", " "));
            mNoteItemDate.setText(getFormattedDate(currentNote.getCreatingDate()));
            mNoteItemTime.setText(getFormattedTime(currentNote.getCreatingDate()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), NoteDetailsActivity.class);
                    intent.putExtra(Constants.NOTE_EXTRAS_KEY, currentNote.getId());
                    ((Activity) itemView.getContext()).startActivityForResult(intent, Constants.UPDATED_NOTE_ACTIVITY_REQUEST_CODE);
                }
            });
        } else {
            mNoteItemText.setText("Waiting...");
        }
    }
}

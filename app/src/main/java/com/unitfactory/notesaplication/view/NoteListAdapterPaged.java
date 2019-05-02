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

import com.unitfactory.notesaplication.Constants;
import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.data.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class NoteListAdapterPaged extends PagedListAdapter<Note, NoteViewHolder> {


    protected NoteListAdapterPaged() {
        super(DIFF_CALLBACK);
    }

    //DiffUtil is used to find out whether two object in the list are same or not
    public static DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return false;
                    //return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return false;
                    //return oldItem.equals(newItem);
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
        if(note != null) {
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

    public NoteViewHolder(View view) {
        super(view);
        mNoteItemText = view.findViewById(R.id.textView);
        mNoteItemDate = view.findViewById(R.id.textViewDate);
        mNoteItemTime = view.findViewById(R.id.textViewTime);
    }

    public void bindTO(final Note currentNote){
        if (currentNote != null)
        {
            mNoteItemText.setText(currentNote.getNote());

            //TODO move from here to date converter for example
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kyiv"));
            cal.setTime(currentNote.getCreatingDate());

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");


            mNoteItemDate.setText(format1.format(cal.getTime()));
            mNoteItemTime.setText(format2.format(cal.getTime()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), NoteDetailsActivity.class);
                    intent.putExtra(Constants.NOTE_EXTRAS_KEY, currentNote.getId());
                    ((Activity)itemView.getContext()).startActivityForResult(intent, Constants.UPDATED_NOTE_ACTIVITY_REQUEST_CODE);
                }
            });
        }
        else
        {
            mNoteItemText.setText("Waiting...");
        }
    }
}

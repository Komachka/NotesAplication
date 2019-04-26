package com.unitfactory.notesaplication.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unitfactory.notesaplication.Constants;
import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.data.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class NoteListAdapter  extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private List<Note> mNoteList;
    private final LayoutInflater mInflater;
    private final Context context;

    public NoteListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (mNoteList != null)
        {
            final Note currentNote = mNoteList.get(position);
            holder.mNoteItemText.setText(currentNote.getNote());

            //TODO move from here to date converter for example
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kyiv"));
            cal.setTime(currentNote.getCreatingDate());

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");


            holder.mNoteItemDate.setText(format1.format(cal.getTime()));
            holder.mNoteItemTime.setText(format2.format(cal.getTime()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), NoteDetailsActivity.class);
                    intent.putExtra(Constants.NOTE_EXTRAS_KEY, currentNote.getId());
                    ((Activity)context).startActivityForResult(intent, Constants.UPDATED_NOTE_ACTIVITY_REQUEST_CODE);
                }
            });
        }
        else
        {
            holder.mNoteItemText.setText("No Word");
        }
    }


    /**
     * Note: The getItemCount() method needs to account gracefully for the possibility that the data is not yet ready and mWords is still null.
     * In a more sophisticated app, you could display placeholder data or something else that would be meaningful to the user.
     */

    @Override
    public int getItemCount() {
        if (mNoteList != null)
        {
            return mNoteList.size();
        }
        return 0;
    }


    void setNotes(List<Note> notes){
        mNoteList = notes;
        notifyDataSetChanged();
    }


    public Note getNoteOnPosition(int pos)
    {
        return mNoteList.get(pos);
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNoteItemText;
        private final TextView mNoteItemDate;
        private final TextView mNoteItemTime;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            mNoteItemText = itemView.findViewById(R.id.textView);
            mNoteItemDate = itemView.findViewById(R.id.textViewDate);
            mNoteItemTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}

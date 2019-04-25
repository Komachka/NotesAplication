package com.unitfactory.notesaplication.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.data.Note;

import java.util.List;

public class NoteListAdapter  extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private List<Note> mNoteList;
    private final LayoutInflater mInflater;

    public NoteListAdapter(Context context) {
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
            Note currentNote = mNoteList.get(position);
            holder.mNoteItemView.setText(currentNote.getNote());
        }
        else
        {
            holder.mNoteItemView.setText("No Word");
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

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNoteItemView;
        
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            mNoteItemView = itemView.findViewById(R.id.textView);
        }
    }
}

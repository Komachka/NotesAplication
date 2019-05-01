package com.unitfactory.notesaplication.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unitfactory.notesaplication.Constants;
import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.data.Note;
import com.unitfactory.notesaplication.model.NoteViewModel;


import java.util.List;


//TODO https://google-developer-training.github.io/android-developer-advanced-course-practicals/unit-6-working-with-architecture-components/lesson-14-room,-livedata,-viewmodel/14-1-a-room-livedata-viewmodel/14-1-a-room-livedata-viewmodel.html#task8intro
public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();

    private NoteViewModel mNoteViewModel;
    NoteListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                startActivityForResult(intent, Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        Log.d(TAG, "on create before call");
        handleSearch("");
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new NoteListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        //mNoteViewModel.invalidate();


        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Note myNote = mAdapter.getNoteOnPosition(position);
                        Toast.makeText(MainActivity.this, "Deleting " +
                                myNote.getNote(), Toast.LENGTH_LONG).show();

                        // Delete the word
                        mNoteViewModel.deleteNote(myNote);
                    }
                });

        helper.attachToRecyclerView(mRecyclerView);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Toast.makeText(
                    getApplicationContext(),
                    data.getStringExtra(Constants.EXTRA_REPLY_NOTE) + " saved",
                    Toast.LENGTH_LONG).show();

        }

        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }



      @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                 (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit "+query);
                handleSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange "+newText);
                if (TextUtils.isEmpty(newText))
                    handleSearch("");

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            // Add a toast just for confirmation
            Toast.makeText(this, "Clearing the data...",
                    Toast.LENGTH_SHORT).show();

            // Delete the existing data
            mNoteViewModel.deleteAll();
            return true;
        }

        if (id==R.id.ascending)
        {
            mNoteViewModel.rearrange(Constants.ASCENDING);
            return true;
        }

        if (id==R.id.descending)
        {
            mNoteViewModel.rearrange(Constants.DESCENDING);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNoteViewModel.shutDown();
    }

    private void handleSearch(String query) {
        String pattern =  "_";
        if (!TextUtils.isEmpty(query))
            pattern = query;
        LiveData<List<Note>> tmp = mNoteViewModel.getAllNotes(pattern);
        if (tmp!=null) {
            tmp.observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    mAdapter.setNotes(notes);
                }
            });
        }
        else {
            Toast.makeText(this, "tmp == null", Toast.LENGTH_LONG).show();
        }
    }
}

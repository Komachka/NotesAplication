package com.unitfactory.notesaplication.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unitfactory.notesaplication.utils.Constants;
import com.unitfactory.notesaplication.R;
import com.unitfactory.notesaplication.model.Note;
import com.unitfactory.notesaplication.vm.NoteViewModel;

import static com.unitfactory.notesaplication.utils.Constants.FULL_QUERY_TEXT;

public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();
    private NoteViewModel mNoteViewModel;
    private NoteListAdapterPaged mAdapter;

    private Observer<PagedList<Note>> observer = new Observer<PagedList<Note>>() {
        @Override
        public void onChanged(PagedList<Note> notes) {
            mAdapter.submitList(notes);
        }
    };

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
        final RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new NoteListAdapterPaged();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, observer);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
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
                mNoteViewModel.deleteNote(myNote);
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE ||
                requestCode == Constants.UPDATED_NOTE_ACTIVITY_REQUEST_CODE &&
                        resultCode == RESULT_OK) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.note_save,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
            Toast.makeText(this, R.string.action_clear_data,
                    Toast.LENGTH_SHORT).show();
            mNoteViewModel.deleteAll();
            return true;
        }

        if (id == R.id.ascending) {
            mNoteViewModel.rearrange(Constants.ASCENDING);
            mNoteViewModel.getAllNotes().observe(this, observer);
            return true;
        }

        if (id == R.id.descending) {
            mNoteViewModel.rearrange(Constants.DESCENDING);
            mNoteViewModel.getAllNotes().observe(this, observer);
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
        String pattern = FULL_QUERY_TEXT;
        if (!TextUtils.isEmpty(query))
            pattern = query;
        mNoteViewModel.setFilter(pattern);
        mNoteViewModel.getAllNotes().observe(this, observer);
        mAdapter.submitList(mNoteViewModel.getAllNotes().getValue()); // does not update adapter without it
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleSearch("");
    }
}

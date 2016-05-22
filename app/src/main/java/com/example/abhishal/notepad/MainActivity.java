package com.example.abhishal.notepad;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/*
Took reference from http://wing-linux.sourceforge.net/guide/tutorials/notepad/notepad-ex1.html
 */

public class MainActivity extends ListActivity {

    private static final int ACTIVITY_CREATE=0;
    private DBAdapter mDBHelper;
    public static final int INSERT_ID = Menu.FIRST;
    private static final int ACTIVITY_EDIT= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDBHelper = new DBAdapter(this);
        mDBHelper.open();
        fillData();
        Button dd = (Button) findViewById(R.id.button);
        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatNote();
            }
        });
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()){
            case INSERT_ID:
                creatNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void creatNote(){
//        String noteName = "Note" + mNoteNumber++;
//        mDBHelper.createNote(noteName, "");
//        fillData();
        Intent i = new Intent(this, NoteDetails.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    private void fillData(){

        Cursor c = mDBHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { DBAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this , R.layout.notes_row, c, from, to);
        setListAdapter(notes);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(this, NoteDetails.class);
        i.putExtra(DBAdapter.KEY_ROWID, id);

        startActivityForResult(i, ACTIVITY_EDIT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       fillData();
    }
}

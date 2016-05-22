package com.example.abhishal.notepad;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by abhishal on 5/9/16.
 */
public class NoteDetails extends Activity {
    EditText mTitleText, mBodyText;
    private Long mRowId;
    private DBAdapter mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        mDBHelper = new DBAdapter(this);
        mDBHelper.open();
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        Button confirmButton = (Button) findViewById(R.id.confirm);
        mRowId = savedInstanceState != null? savedInstanceState.getLong(DBAdapter.KEY_ROWID):null;

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(DBAdapter.KEY_ROWID): null;
        }

        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    private void populateFields(){
        if (mRowId!=null){
            Cursor note = mDBHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(note.getColumnIndexOrThrow(DBAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(note.getColumnIndexOrThrow(DBAdapter.KEY_BODY)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DBAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    private void saveState(){
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null){
            long id = mDBHelper.createNote(title, body);
            if (id > 0){
                mRowId = id;
            }else {
                mDBHelper.updateNote(mRowId, title, body);
            }
        }

    }

}

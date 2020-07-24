package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class EditActivity extends AppCompatActivity {

    private final static String noteIDKey = "CURRENT_NOTE_ID";
    private final static String noteIDListKey = "NOTE_IDS";

    private Note note;
    private EditText noteTextInput;
    private EditText noteTitleInput;

    private Bundle state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTextInput = (EditText) findViewById(R.id.noteText);
        noteTitleInput = (EditText) findViewById(R.id.noteTitle);

        Intent intent = getIntent();
        String noteId = intent.getStringExtra(noteIDKey);

        if (savedInstanceState != null) {
            note = (Note) savedInstanceState.getParcelable(noteId);
            noteTextInput.setText(note.getText());
            noteTitleInput.setText(note.getTitle());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = state;
        saveNote(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstance){
        state = savedInstance;
        super.onRestoreInstanceState(state);
    }

    public void saveAndExit(View view){
        saveNote(state);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveNote(Bundle outState) {
        System.out.println("Saving Note");
        if (note!=null){
            note.setText(noteTextInput.getText().toString());
            note.setTitle(noteTitleInput.getText().toString());
            outState.putParcelable(note.getId(), (Parcelable) note);

        } else if (noteTextInput.getText().length()!=0 || noteTitleInput.getText().length()!=0) {
            note = new Note(noteTitleInput.getText().toString(),noteTextInput.getText().toString());
            outState.putParcelable(note.getId(), (Parcelable) note);
            ArrayList<String> noteIDList = outState.getStringArrayList(noteIDListKey);
            noteIDList.add(note.getId());
            outState.putStringArrayList(noteIDListKey,noteIDList);
        }
    }

}

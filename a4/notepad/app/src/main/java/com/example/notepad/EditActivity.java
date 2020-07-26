package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class EditActivity extends AppCompatActivity {

    private final static String noteIDKey = "CURRENT_NOTE_ID";
    private final static String noteIDListKey = "NOTE_IDS";

    private Note note;
    private EditText noteTextInput;
    private EditText noteTitleInput;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTextInput = (EditText) findViewById(R.id.noteText);
        noteTitleInput = (EditText) findViewById(R.id.noteTitle);

        Intent intent = getIntent();
        String noteId = intent.getStringExtra(noteIDKey);
        if (noteId != null) {
            loadNote(noteId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveNote();
        super.onSaveInstanceState(outState);
    }

    public void saveAndExit(View view){
        saveNote();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveNote() {
        if (note!=null){
            note.setText(noteTextInput.getText().toString());
            note.setTitle(noteTitleInput.getText().toString());
            storeNote();
        } else if (noteTextInput.getText().length()!=0 || noteTitleInput.getText().length()!=0) {
            note = new Note(noteTitleInput.getText().toString(),noteTextInput.getText().toString());
            storeNote();
            storeNoteIds();
        }
    }

    private void storeNote(){
        try {
            File file = new File(this.getFilesDir(), "/" + note.getId());
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(note);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadNote(String noteId){
        try {
            File file = new File(this.getFilesDir(), "/" + noteId);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fileInputStream);
            note = (Note) oi.readObject();
            noteTitleInput.setText(note.getTitle());
            noteTextInput.setText(note.getText());
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeNoteIds(){
        try {
            File file = new File(this.getFilesDir(), "/noteIds");
            if (!file.isFile()){
                initializeNoteIdStore();
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fileInputStream);
            ArrayList<String> noteIds = (ArrayList<String>) oi.readObject();

            noteIds.add(0,note.getId());

            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(noteIds);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeNoteIdStore(){
        try {
            File file = new File(this.getFilesDir(), "/noteIds");
            ArrayList<String> noteIds = new ArrayList<>();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(noteIds);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

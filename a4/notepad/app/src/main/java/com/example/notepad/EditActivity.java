package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.ArrayList;


public class EditActivity extends AppCompatActivity {

    private final static String noteIDKey = "CURRENT_NOTE_ID";

    private Note note;
    private EditText noteTextInput;
    private EditText noteTitleInput;

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
    protected void onDestroy() {
        saveNote();
        super.onDestroy();
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
            updateNotePreview();
        } else if (noteTextInput.getText().length()!=0 || noteTitleInput.getText().length()!=0) {
            note = new Note(noteTitleInput.getText().toString(),noteTextInput.getText().toString());
            storeNote();
            addNewNotePreview();
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

    private void updateNotePreview(){
        try {
            File file = new File(this.getFilesDir(), "/notePreviews");
            if (!file.isFile()){
                initializeNotePreviewStore();
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fileInputStream);
            ArrayList<Note> notePreviews = (ArrayList<Note>) oi.readObject();

            System.out.println(notePreviews);
            for (int i = 0; i<notePreviews.size(); i++){
                Note notePreview = notePreviews.get(i);
                if(notePreview.getId().equals(note.getId())) {
                    notePreview.setText(getStub(note.getText()));
                    notePreview.setTitle(note.getTitle());
                    break;
                }
            }
            System.out.println(notePreviews);

            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(notePreviews);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addNewNotePreview(){
        try {
            File file = new File(this.getFilesDir(), "/notePreviews");
            if (!file.isFile()){
                initializeNotePreviewStore();
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fileInputStream);
            ArrayList<Note> notePreviews = (ArrayList<Note>) oi.readObject();

            Note newNotePreview = new Note(note.getTitle(),getStub(note.getText()));
            newNotePreview.setId(note.getId());

            notePreviews.add(0,newNotePreview);

            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(notePreviews);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeNotePreviewStore(){
        try {
            File file = new File(this.getFilesDir(), "/notePreviews");
            ArrayList<Note> notePreviews = new ArrayList<>();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(notePreviews);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getStub(String s) {
        if (s.length() > 15) {
            return s.substring(0,15);
        }
        return s;
    }
}

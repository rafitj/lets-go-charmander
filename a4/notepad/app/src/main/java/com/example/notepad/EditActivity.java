package com.example.notepad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.ArrayList;


public class EditActivity extends AppCompatActivity {

    private final static String noteIDKey = "CURRENT_NOTE";

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
        Note notePreview = (Note) intent.getSerializableExtra(noteIDKey);
        if (notePreview != null) {
            note = notePreview;
            AsyncTaskExample asyncTask=new AsyncTaskExample();
            asyncTask.execute(note.getId());
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

    private Note loadNote(String noteId){
        try {
            File file = new File(this.getFilesDir(), "/" + noteId);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fileInputStream);
            note = (Note) oi.readObject();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return note;
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
        if (s.length() > 50) {
            return s.substring(0,50);
        }
        return s;
    }

    //    Async Task Stuff
    private class AsyncTaskExample extends AsyncTask<String, String, Note> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            noteTitleInput.setText(note.getTitle());
            noteTextInput.setText(note.getText());
        }
        @Override
        protected Note doInBackground(String... noteIds) {
          return loadNote(noteIds[0]);
        }
        @Override
        protected void onPostExecute(Note loadedNote) {
            super.onPostExecute(loadedNote);
            note = loadedNote;
            noteTitleInput.setText(loadedNote.getTitle());
            noteTextInput.setText(loadedNote.getText());
        }
    }
}

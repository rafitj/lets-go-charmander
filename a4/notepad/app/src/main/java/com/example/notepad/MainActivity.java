package com.example.notepad;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final static String noteIDListKey = "NOTE_IDS";
    private final static String noteIDKey = "CURRENT_NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            ArrayList<String> noteIDs = savedInstanceState.getStringArrayList(noteIDListKey);
            for (String id: noteIDs) {
                System.out.println((Note) savedInstanceState.getParcelable(id));
            }
        }
    }

    public void createNewNote(View view){
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void viewNote(View view){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(noteIDKey,"note2");
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("Called Here");
//        outState.putParcelable(notesDataKey, (Parcelable) notesData);
        super.onSaveInstanceState(outState);
    }


}

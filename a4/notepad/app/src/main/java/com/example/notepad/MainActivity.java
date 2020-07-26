package com.example.notepad;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File(this.getFilesDir(), "/noteIds");

        recyclerView = (RecyclerView) findViewById(R.id.noteListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> noteIds = new ArrayList<>();
        if (file.isFile()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fileInputStream);
                noteIds = (ArrayList<String>) oi.readObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        mAdapter = new NoteListAdapter(noteIds);
        recyclerView.setAdapter(mAdapter);
    }

    public void createNewNote(View view){
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

}

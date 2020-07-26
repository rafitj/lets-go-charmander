package com.example.notepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.util.ArrayList;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {
    private ArrayList<String> noteIds;
    private final static String noteIDKey = "CURRENT_NOTE_ID";

    public NoteListAdapter(ArrayList<String> noteIds) {
        this.noteIds = noteIds;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noteListTitle;
        public TextView noteListContent;
        public LinearLayout parentLayout;
        public FloatingActionButton deleteNoteFab;
        public MyViewHolder(View itemView) {
            super(itemView);
            noteListTitle = itemView.findViewById(R.id.noteListTitle);
            noteListContent = itemView.findViewById(R.id.noteListContent);
            deleteNoteFab = itemView.findViewById(R.id.deleteNoteFab);
            parentLayout = itemView.findViewById(R.id.noteListItemLayout);
        }
    }

    @NonNull
    @Override
    public NoteListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.noteListTitle.setText(noteIds.get(position).substring(0,10));
        holder.noteListContent.setText("Test");
        holder.deleteNoteFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteNote(view.getContext(),position);
                System.out.println("Delete Note");
            }
        });
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewNote(view.getContext(),noteIds.get(position));
            }
        });
    }

    private void deleteNote(Context context, int position) {
        File noteFile = new File(context.getFilesDir(),"/"+noteIds.get(position));
        if (noteFile.isFile()) {
            noteFile.delete();
        }

        try {
            File noteIdsFile = new File(context.getFilesDir(),"/noteIds");
            noteIds.remove(position);
            FileOutputStream fileOut = new FileOutputStream(noteIdsFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(noteIds);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.notifyItemRemoved(position);
    }

    private void viewNote(Context context, String noteId){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(noteIDKey,noteId);
        ((Activity) context).startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return noteIds.size();
    }
}

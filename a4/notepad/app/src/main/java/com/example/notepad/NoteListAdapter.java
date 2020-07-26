package com.example.notepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.util.ArrayList;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {
    private ArrayList<Note> notePreviews;
    private final static String noteIDKey = "CURRENT_NOTE";

    public NoteListAdapter(ArrayList<Note> notePreviews) {
        this.notePreviews = notePreviews;
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
        holder.noteListTitle.setText(notePreviews.get(position).getPreviewTitle());
        holder.noteListContent.setText(notePreviews.get(position).getPreviewContent());
        holder.deleteNoteFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteNote(view.getContext(),position);
            }
        });
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewNote(view.getContext(),notePreviews.get(position));
            }
        });
    }

    private void deleteNote(Context context, int position) {
        File noteFile = new File(context.getFilesDir(),"/"+notePreviews.get(position).getId());
        if (noteFile.isFile()) {
            noteFile.delete();
        }

        try {
            File noteIdsFile = new File(context.getFilesDir(),"/notePreviews");
            notePreviews.remove(position);
            FileOutputStream fileOut = new FileOutputStream(noteIdsFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(notePreviews);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.notifyItemRemoved(position);
    }

    private void viewNote(Context context, Note notePreview){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(noteIDKey,notePreview);
        ((Activity) context).startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return notePreviews.size();
    }
}

package com.example.notepad;

import java.io.Serializable;
import java.util.UUID;

public class Note implements Serializable {
    private String title;
    private String text;
    private String id;

    Note(String title, String text){
        this.text = text;
        this.title = title;
        this.id = "note"+UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPreviewTitle() {
        if (title.length() != 0) {
            return title;
        }
        if (text.length() > 10) {
            return text.substring(0,10) + "...";
        }
        return text;
    }

    public String getPreviewContent() {
        if (text.length() > 15) {
            return text.substring(0,15) + "...";
        }
        return text;
    }

    @Override
    public String toString() {
        return new StringBuffer(" ID: ").append(this.id).append(" Title : ").append(this.title).append(" Text : ").append(this.text).toString();
    }
}

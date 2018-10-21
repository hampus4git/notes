package com.example.documentApp.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Note {

    @JsonProperty
    private int id;

    @JsonProperty
    private String title;

    @JsonProperty
    private String content;

    @JsonProperty
    private String user;



    public Note(){
        super();
    }

    public Note(int id, String title, String content, String user){
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

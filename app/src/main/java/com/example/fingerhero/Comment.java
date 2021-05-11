package com.example.fingerhero;

public class Comment {
    private String date;
    private String user;
    private String mention;
    private String text;

    public Comment() {}

    public Comment(String date, String user, String mention, String text) {
        this.date = date;
        this.user = user;
        this.mention = mention;
        this.text= text;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getMention() {
        return mention;
    }

    public String getText() {
        return text;
    }
}

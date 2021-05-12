package com.example.fingerhero;

import java.util.ArrayList;
import java.util.List;

public class Article {
    private String date; // 시간 YY:MM:DD
    private String url; //URL
    private String tag; // 태그
    private int favor; // 좋야요

    private List<Comment> comments = new ArrayList<>();

    public Article() {}

    public Article(String date, String url, String tag, int favor) {
        this.url = url;
        this.tag = tag;
        this.date = date;
        this.favor = favor;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public int getFavor() {
        return favor;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}

package com.maciej_switalski.projekt;

public class Entry {
    String category;
    private long id;
    private String title;
    private String content;
    private String date;

    public Entry(String title, String content, String date, String category) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.category = category;
    }

    public Entry(long id, String title, String content, String date, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

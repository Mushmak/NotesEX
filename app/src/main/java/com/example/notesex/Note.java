package com.example.notesex;
/*
    Note Template
 */
public class Note {
    private long id;
    private String title, content, date, time;
    private byte[] image;
    private int imageCount = 0;
    private int imageIndex = 0;

//Constructors for new Note
    Note() {}

    //No ID
    Note(String title, String content, String date, String time, byte[] image) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.image = image;
    }

    //with ID;
    Note(long id, String title, String content, String date, String time, byte[] image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.image = image;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte[] getImage() { return image;}

    public void setImage(byte[] image) { this.image = image; }

    public void setImageCount(int imageCount) {this.imageCount = imageCount;}

    public int getImageCount() {return imageCount;}

    public void setImageIndex(int imageIndex) {this.imageIndex = imageIndex;}

    public int getImageIndex() {return imageIndex;}
}

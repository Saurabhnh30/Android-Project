package com.example.reminderapp.models;

import java.util.List;

public class Notes
{

    public static final String NOTE_ID = "noteId";
    public static final String NOTE_TITLE = "noteTitle";
    public static final String NOTE_DETAILS = "noteDetails";

    private String noteTitle;
    private String noteDetails;
    private String noteId;
//    private List<String> imagesList;
//    private String aboutImage;
    private String userId;

    public Notes(){}

//    public String getAboutImage() {
//        return aboutImage;
//    }
//
//    public void setAboutImage(String aboutImage) {
//        this.aboutImage = aboutImage;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDetails() {
        return noteDetails;
    }

    public void setNoteDetails(String noteDetails) {
        this.noteDetails = noteDetails;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

//    public List<String> getImagesList() {
//        return imagesList;
//    }
//
//    public void setImagesList(List<String> imagesList) {
//        this.imagesList = imagesList;
//    }
}

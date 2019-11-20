package com.example.reminderapp.models;

public class Documents {

    public static final String DOC_IMAGE = "docImage";
    public static final String DOC_TITLE = "docTitle";
    public static final String DOC_ID = "docId";

    private String docImage;
    private String docTitle;
    private String docId;
    private String userId;

    public String getDocImage() {
        return docImage;
    }

    public void setDocImage(String docImage) {
        this.docImage = docImage;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package com.example.reminderapp.models;

public class Encrypts {

    public static final String TITLE = "title";
    public static final String ENCRYPT_ID = "id";

    private String title;
    private String userId;
    private String encryptId;

    public Encrypts() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String _getEncryptId() {
        return encryptId;
    }

    public void _setEncryptId(String encryptId) {
        this.encryptId = encryptId;
    }
}

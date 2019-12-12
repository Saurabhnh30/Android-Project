package com.example.reminderapp.helpers;


public class CollectionNames {

    private String notes = "notes";
    private String usersCollection = "users";
    private String documentCollection = "documents";

    public static final String USERS = "users";
    public static final String ENCRYPT = "encrypt";
    public static final String CONTACTS = "contacts";



    public CollectionNames(){}

    public String getNotes() {
        return notes;
    }

    public String getUsersCollection() {
        return usersCollection;
    }

    public String getDocumentCollection() {
        return documentCollection;
    }
}

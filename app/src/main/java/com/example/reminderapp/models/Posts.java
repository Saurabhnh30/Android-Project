package com.example.reminderapp.models;

public class Posts
{

    private String Posttitle;
    private String Postdetails;
    private String Posdid;

    public Posts(){}

    public Posts(String PostT,String PostD, String PostID)
    {
        this.Posttitle = PostT;
        this.Postdetails= PostD;
        this.Posdid = PostID;
    }

    public String getPosttitle() {
        return Posttitle;
    }

    public String getPostdetails() {
        return Postdetails;
    }

    public String getPosdid() {
        return Posdid;
    }
}

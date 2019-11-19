package com.example.reminderapp.models;

import java.util.List;

public class Posts
{

    public static final String IMAGE_LIST = "imagesLIst";
    public static final String ABOUT_IMAGE = "aboutImage";
    public static final String USER_ID = "userId";
    public static final String POST_ID = "postId";
    public static final String POST_TITLE = "posttitle";
    public static final String POST_DETAILS = "postdetails";

    private String Posttitle;
    private String Postdetails;
    private String Posdid;
    private List<String> imagesList;
    private String aboutImage;
    private String userId;

    public Posts(){}

    public Posts(String PostT,String PostD, String PostID)
    {
        this.Posttitle = PostT;
        this.Postdetails= PostD;
        this.Posdid = PostID;
    }



    public String getAboutImage() {
        return aboutImage;
    }

    public void setAboutImage(String aboutImage) {
        this.aboutImage = aboutImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPosttitle() {
        return Posttitle;
    }

    public void setPosttitle(String posttitle) {
        Posttitle = posttitle;
    }

    public String getPostdetails() {
        return Postdetails;
    }

    public void setPostdetails(String postdetails) {
        Postdetails = postdetails;
    }

    public String getPosdid() {
        return Posdid;
    }

    public void setPosdid(String posdid) {
        Posdid = posdid;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }
}

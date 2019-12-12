package com.example.reminderapp.models;

public class Contacts {

    public static final String CONTACT_EMAIL = "contactEmail";
    public static final String CONTACT_AVATAR = "contactAvatar";
    public static final String CONTACT_PHONE = "contactPhone";
    public static final String CONTACT_FIRSTNAME = "contactFirstname";
    public static final String CONTACT_SIRNAME = "contactSirname";
    public static final String CONTACT_COMPANY = "contactCompany";
    public static final String CONTACT_ID = "contactId";

    private String contactId;
    private String contactEmail;
    private String contactAvatar;
    private String contactPhone;
    private String contactFirstname;
    private String contactCompany;
    private String contactSirname;
    private String userId;

    public Contacts() {}

    public Contacts(String cemail, String cfname, String csirname, String cphone, String cavatar, String company, String uid) {
        this.contactEmail = cemail;
        this.contactFirstname = cfname;
        this.contactSirname = csirname;
        this.contactPhone = cphone;
        this.contactAvatar = cavatar;
        this.contactCompany = company;
        this.userId = uid;
    }

    public String _getContactId() {
        return contactId;
    }

    public void _setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactFirstname() {
        return contactFirstname;
    }

    public void setContactFirstname(String contactFirstname) {
        this.contactFirstname = contactFirstname;
    }

    public String getContactCompany() {
        return contactCompany;
    }

    public void setContactCompany(String contactCompany) {
        this.contactCompany = contactCompany;
    }

    public String getContactSirname() {
        return contactSirname;
    }

    public void setContactSirname(String contactSirname) {
        this.contactSirname = contactSirname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package com.example.reminderapp.models;

public class Users {

    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String PIN = "loginPin";
    public static final String PASSWORD = "password";


    private String username;
    private String password;
    private String loginPin;
    private String email;
    private String phone;


    public Users() {}


    public Users(
            String email,
            String password,
            String username,
            String pin,
            String phone
    ) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.loginPin = pin;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginPin() {
        return loginPin;
    }

    public void setLoginPin(String loginPin) {
        this.loginPin = loginPin;
    }
}

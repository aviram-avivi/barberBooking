package com.example.barberbooking;

abstract public class User {
    private String userId;
    private String email;
    private String name;
    private String phone;

    public User(String userId, String email, String name, String phone) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public User() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

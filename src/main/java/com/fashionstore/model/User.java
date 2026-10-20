
package com.fashionstore.model;

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private String address;
    private String role = "CUSTOMER";

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(int userId, String fullName, String email,
                String phone, String password,
                String gender, String address) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.role = "CUSTOMER";
    }

    public User(int userId, String fullName, String email,
                String phone, String password,
                String gender, String address, String role) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.role = role != null ? role : "CUSTOMER";
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role != null ? role : "CUSTOMER";
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
}

package com.example.sean.ratapp.model;

import java.io.Serializable;


/**
 * Created by jfahe on 9/22/2017.
 */

@SuppressWarnings("ALL")
public class User implements Serializable {
    private final String _userName; //string of user id
    private final String _userPassword; //string of user password
    private boolean active = true; //status of user...false = banned
    private String banStatus; //reason for being banned if banned
    public  boolean isAdmin;

    public User(String name, String password) {
        _userName = name;
        _userPassword = password;
    }

    User() {
        _userPassword = "pass";
        _userName = "user";
        isAdmin = false;
    }


    public void logIn() {
    }

    public String getPassword() {
        return _userPassword;
    }

    public String toString() {
        return _userName;
    }

    public boolean isAdmin() {
        return false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }

    public static User getUserByID(String un) {
        return UserManager._user_hash_map.get(un);
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}



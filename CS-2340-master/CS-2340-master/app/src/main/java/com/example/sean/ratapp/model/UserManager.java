package com.example.sean.ratapp.model;

import java.util.HashMap;
/**
 * Created by Sean on 10/1/2017.
 */

@SuppressWarnings("ALL")
public class UserManager {
    public static final HashMap<String, User> _user_hash_map = new HashMap<>(10);
    public static final HashMap<String, User> _admin_hash_map = new HashMap<>(10);
    /**
     *
     * @param username the name inputted by a user
     * @param password the password inputted by the user
     * @return false if a username with the username already exists, else a new user is added to the system
     */
    public static boolean addUser(String username, String password) {
        if (_user_hash_map.containsKey(username)) {
            return false;
        }

        _user_hash_map.put(username, new User(username, password));
        return true;
    }

    /**
     *
     * @param username the name inputted by a user
     * @param password the password inputted by the user
     * @return false if an admin with the username already exists, else a new admin is added to the system
     */
    public static boolean addAdmin(String username, String password) {
        if (_admin_hash_map.containsKey(username)) {
            return false;
        }

        _admin_hash_map.put(username, new Admin(username, password));
        _admin_hash_map.get(username).setAdmin(true);
        return true;
    }

    /**
     *
     @param username the username of the user logging in
     * @param password the password the user believes is right
     * @return true if there is a user with the given username and the given password is correct
     */
    public static boolean loginUser(String username, String password) {

        if (_user_hash_map.containsKey(username) && password.equals(_user_hash_map.get(username).getPassword())) {
                return true;
            }


        return false;
    }

    public static boolean loginAdmin(String username, String password) {

        if (_admin_hash_map.containsKey(username) && password.equals(_admin_hash_map.get(username).getPassword())) {
                return true;
            }

        return false;
    }




    /**
     * getter for user object
     * @param username username instance data of user
     * @return User associated with the username passed in
     */
    public static User getUser(String username) {
        return _user_hash_map.get(username);
    }

    public static HashMap<String, User> get_user_hash_map() {
        return _user_hash_map;
    }

    /**
     * clears user hash map, used for testing
     */
    public static void clear() {
        _user_hash_map.clear();
    }


}

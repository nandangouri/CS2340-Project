package com.example.sean.ratapp;

import com.example.sean.ratapp.model.UserManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Created by chuwh on 11/13/2017.
 */



public class TestAddAdmin {

    @Test
    public void testValidUN(){

        //checks if testing UN and PW are in the map
        assertFalse(UserManager.get_user_hash_map().containsKey("TESTING123"));
        assertFalse(UserManager.get_user_hash_map().containsValue("thisisatest"));

        //adds to the map
        assertTrue(UserManager.addAdmin("TESTING123", "thisisatest"));
        //returns false if the UN and PW are in the map
        assertFalse(UserManager.addAdmin("TESTING123", "thisisatest"));
        assertTrue(UserManager.get_user_hash_map().containsKey("TESTING123"));

        //returns map to pre-test condition
        UserManager.get_user_hash_map().remove("TESTING123");
        assertFalse(UserManager.get_user_hash_map().containsKey("TESTING123"));
        assertFalse(UserManager.get_user_hash_map().containsValue("thisisatest"));


    }

}

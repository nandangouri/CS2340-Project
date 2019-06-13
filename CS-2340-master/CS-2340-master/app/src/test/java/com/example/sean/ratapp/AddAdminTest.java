package com.example.sean.ratapp;

import org.junit.Test;

import static com.example.sean.ratapp.model.UserManager.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by nandanj.gouri on 11/12/17.
 */

public class AddAdminTest {
    String blank = "";
    String specialcharacters = "!@#$%^&*()[]{}";
    String numbers = "12345678910";
    String space = " ";
    String characters = "abcdefghijklmnopqrstuvwxz";

    @Test
    public void checkUsernameblank() {
        assertFalse(addAdmin(blank, characters));
        assertFalse(addAdmin(blank, space));
        assertFalse(addAdmin(blank, blank));
        assertFalse(addAdmin(blank, numbers));
        assertFalse(addAdmin(blank, specialcharacters));
    }
    @Test
    public void checkUsernamespecialcharacters() {
        assertFalse(addAdmin(specialcharacters, characters));
    }

    @Test
    public void checkUsernamespace() {
        assertFalse(addAdmin(space, characters));
    }
    @Test
    public void checkUsernamespec() {
        assertFalse(addAdmin(specialcharacters, characters));
    }
    @Test
    public void checkPasswordblank() {
        assertFalse(addAdmin(characters, blank));
    }

    @Test
    public void checkPasswordspace() {
        assertFalse(addAdmin(specialcharacters, space));
    }

}

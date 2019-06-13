package com.example.sean.ratapp.model;

/**
 * Created by jfahe on 12/4/2017.
 */

public class Locked {
    private static boolean locked = false;

    public static void lock() {
        locked = true;
    }

    public static boolean getLocked() {
        return locked;
    }
}

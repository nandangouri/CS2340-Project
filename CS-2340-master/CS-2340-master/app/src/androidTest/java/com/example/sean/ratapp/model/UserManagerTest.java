package com.example.sean.ratapp.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jfahe on 11/12/2017.
 */
public class UserManagerTest {
    @Before
    public void setUp() throws Exception {
        UserManager.addUser("user", "pass");
        UserManager.addUser("test", "test");
    }

    @After
    public void reset() throws Exception {
        UserManager.clear();
    }

    @Test
    public void addUser() throws Exception {
        Assert.assertTrue(UserManager.addUser("joe", "pass"));
        Assert.assertTrue(UserManager.addUser("joe2", "fahey"));
        Assert.assertFalse(UserManager.addUser("test", "test"));
        Assert.assertFalse(UserManager.addUser("user", "pass"));
        Assert.assertFalse(UserManager.addUser("joe", "joe"));
        Assert.assertFalse(UserManager.addUser("joe2", "fahey"));
        Assert.assertEquals("pass", UserManager._user_hash_map.get("joe").getPassword());
        Assert.assertEquals("fahey", UserManager._user_hash_map.get("joe2").getPassword());
        Assert.assertEquals("pass", UserManager._user_hash_map.get("user").getPassword());
        Assert.assertEquals("test", UserManager._user_hash_map.get("test").getPassword());
    }
}
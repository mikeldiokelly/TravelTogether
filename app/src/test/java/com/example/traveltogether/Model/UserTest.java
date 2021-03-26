package com.example.traveltogether.Model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class UserTest extends TestCase {
    private User testUser;
    private String testId = "uid123456";

    @Before
    public void setUp(){
        testUser = new User();
    }
    @Test
    public void testId() {
        testUser.setId((testId));
        assertEquals(testUser.getId(),testId);
    }


}
package com.example.traveltogether.Model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class ChatTest extends TestCase {
    private Chat testChat;

    String testMsg = "testMsg";
    String testReceiver = "testReceiverUid";
    String testSender = "testSenderUid";

    @Before
    public void setUp() throws Exception {
        testChat = new Chat();
    }
    @Test
    public void testMessage() {
        testChat.setMessage((testMsg));
        assertEquals(testChat.getMessage(),testMsg);
    }
    @Test
    public void testReceiver() {
        testChat.setReceiver((testReceiver));
        assertEquals(testChat.getReceiver(),testReceiver);
    }
    @Test
    public void testSender() {
        testChat.setSender((testSender));
        assertEquals(testChat.getSender(),testSender);
    }
}
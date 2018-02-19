package com.example.safwat.carstation.Model;

/**
 * Created by safwat on 10/07/17.
 */

public class Message {
    private String  title;
    private String  messageContent;

    public Message(String title, String messageContent) {
        this.title = title;
        this.messageContent = messageContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}

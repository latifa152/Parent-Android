package com.example.parentproject.Model;

public class MessageModel {
    String Message;
    String Name;

    public MessageModel() {
    }

    public MessageModel(String message, String name) {
        Message = message;
        Name = name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

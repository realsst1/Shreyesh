package com.example.shreyesh;

public class Message {

    private String type;
    private String from;
    private String text;
    private String timestamp;

    public Message(String type, String from, String text, String timestamp) {
        this.type = type;
        this.from = from;
        this.text = text;
        this.timestamp = timestamp;
    }


    public Message() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

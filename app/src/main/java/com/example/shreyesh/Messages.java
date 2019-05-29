package com.example.shreyesh;

import android.util.Log;

public class Messages {

    private String type;
    private String from;
    private String message;
    private Long time;

    public Messages(String type, String from, String message, Long time) {
        this.type = type;
        this.from = from;
        this.message = message;
        this.time = time;
    }


    public Messages() {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}

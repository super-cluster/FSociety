package com.anurag.fsociety;

public class Messages {
    String message,name,time,sUid;

    public Messages() {
    }

    public Messages(String message, String name, String time, String sUid) {
        this.message = message;
        this.name = name;
        this.time = time;
        this.sUid = sUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }
}

package com.anurag.fsociety;

public class HelpModel {
    String message,title;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HelpModel(String message, String title) {
        this.message = message;
        this.title = title;
    }
}

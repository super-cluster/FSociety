package com.anurag.fsociety;

public class User {
    String email,name,uId,status,typing;

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public User(String email, String name, String uId,String status,String typing) {
        this.email = email;
        this.name = name;
        this.uId = uId;
        this.status=status;
        this.typing=typing;
    }

    public User() {
    }

    public String getTyping() {
        return typing;
    }

    public void setTyping(String typing) {
        this.typing = typing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.visma.meetingAPI.models;

import java.util.List;

public class Person {
    private String id;
    private String name;
    // private String password; TODO create authentication and authorization
    private List<Meeting> meetings;

    @Override
    public String toString() {
        return this.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // TODO create authentication and authorization

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}

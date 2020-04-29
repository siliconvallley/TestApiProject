package com.dh.testproject.databinding;

import androidx.databinding.ObservableInt;

public class UserDataBinding {
    private String name;
    private String lastName;
    private ObservableInt likes;

    public UserDataBinding() {
    }

    public UserDataBinding(String name, String lastName, ObservableInt likes) {
        this.name = name;
        this.lastName = lastName;
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ObservableInt getLikes() {
        return likes;
    }

    public void setLikes(ObservableInt likes) {
        this.likes = likes;
    }
}

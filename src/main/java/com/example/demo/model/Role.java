package com.example.demo.model;

public class Role {
    private int id;
    private String name;

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Override toString() to display the role name in the ComboBox
    @Override
    public String toString() {
        return name;
    }
}

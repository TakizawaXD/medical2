package com.example.demo;

public class Patient {
    private int id;
    private String name;
    private String lastName;
    private String dni;
    private String date;
    private String address;
    private String phone;
    private String gender;

    public Patient(String name, String lastName, String dni, String date, String address, String phone, String gender) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.date = date;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDni() {
        return dni;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }
}

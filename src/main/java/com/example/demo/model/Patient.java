package com.example.demo.model;

public class Patient {
    private int id;
    private String name;
    private String lastName;
    private String cedula;
    private String email;
    private String date;
    private int age;
    private String address;
    private String phone;
    private String gender;
    private String bloodType;
    private String allergies;
    private String medicalHistory;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String insuranceProvider;
    private String policyNumber;

    public Patient(String name, String lastName, String cedula, String email, String date, int age, String address, String phone, String gender,
                   String bloodType, String allergies, String medicalHistory, String emergencyContactName,
                   String emergencyContactPhone, String insuranceProvider, String policyNumber) {
        this.name = name;
        this.lastName = lastName;
        this.cedula = cedula;
        this.email = email;
        this.date = date;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.medicalHistory = medicalHistory;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.insuranceProvider = insuranceProvider;
        this.policyNumber = policyNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    @Override
    public String toString() {
        return name + " " + lastName;
    }
}

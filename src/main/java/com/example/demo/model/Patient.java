
package com.example.demo.model;

import java.util.List;

public class Patient {
    private String id;
    private String name;
    private String document;
    private String medicalHistory;
    private List<String> allergies;
    private List<String> previousDiagnoses;
    private List<String> previousMedications;

    // Getters and setters

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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getPreviousDiagnoses() {
        return previousDiagnoses;
    }

    public void setPreviousDiagnoses(List<String> previousDiagnoses) {
        this.previousDiagnoses = previousDiagnoses;
    }

    public List<String> getPreviousMedications() {
        return previousMedications;
    }

    public void setPreviousMedications(List<String> previousMedications) {
        this.previousMedications = previousMedications;
    }
}

package com.example.demo;

import java.time.LocalDate;

public class Encounter {
    private int id;
    private int patientId;
    private LocalDate encounterDate;
    private String reasonForVisit;
    private String diagnosis;
    private String doctorNotes;
    private String treatmentPlan;

    // Constructor for creating a new encounter
    public Encounter(int patientId, LocalDate encounterDate, String reasonForVisit, String diagnosis, String doctorNotes, String treatmentPlan) {
        this.patientId = patientId;
        this.encounterDate = encounterDate;
        this.reasonForVisit = reasonForVisit;
        this.diagnosis = diagnosis;
        this.doctorNotes = doctorNotes;
        this.treatmentPlan = treatmentPlan;
    }

    // Constructor for loading an encounter from the database
    public Encounter(int id, int patientId, LocalDate encounterDate, String reasonForVisit, String diagnosis, String doctorNotes, String treatmentPlan) {
        this.id = id;
        this.patientId = patientId;
        this.encounterDate = encounterDate;
        this.reasonForVisit = reasonForVisit;
        this.diagnosis = diagnosis;
        this.doctorNotes = doctorNotes;
        this.treatmentPlan = treatmentPlan;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public LocalDate getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(LocalDate encounterDate) {
        this.encounterDate = encounterDate;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }
}

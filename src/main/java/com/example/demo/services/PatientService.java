
package com.example.demo.services;

import com.example.demo.model.Patient;
import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    Patient getPatientById(String id);
    Patient updatePatient(String id, Patient patient);
    void deletePatient(String id);
    List<Patient> getAllPatients();
}

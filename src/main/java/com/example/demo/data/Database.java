package com.example.demo.data;

import com.example.demo.model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connect() {
        String url = "jdbc:sqlite:src/main/resources/database/database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createInitialTables() {
        createRoleTable();
        createUserTable();
        createPatientTable();
        createEncounterTable();
        createAppointmentTable();
        populateRoles();
        insertInitialData();
    }

    private void createRoleTable() {
        String sql = "CREATE TABLE IF NOT EXISTS roles (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL UNIQUE\n"
                + ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating roles table: " + e.getMessage());
        }
    }

    private void populateRoles() {
        String[] roles = {"Administrador", "Médico", "Recepcionista", "Enfermero/a", "Técnico de Laboratorio", "Farmacéutico/a", "Personal de Facturación"};
        String sql = "INSERT OR IGNORE INTO roles(name) VALUES(?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String roleName : roles) {
                pstmt.setString(1, roleName);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error populating roles: " + e.getMessage());
        }
    }

    private void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY KEY,\n"
                + " username text NOT NULL UNIQUE,\n"
                + " password text NOT NULL,\n"
                + " role_id integer,\n"
                + " FOREIGN KEY (role_id) REFERENCES roles (id)\n"
                + ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating users table: " + e.getMessage());
        }
    }

    private void createPatientTable() {
        String sql = "CREATE TABLE IF NOT EXISTS patients (\n"
                + " id INTEGER PRIMARY KEY,\n"
                + " name TEXT NOT NULL,\n"
                + " lastName TEXT NOT NULL,\n"
                + " cedula TEXT NOT NULL UNIQUE,\n"
                + " email TEXT,\n"
                + " date TEXT,\n"
                + " age INTEGER,\n"
                + " address TEXT,\n"
                + " phone TEXT,\n"
                + " gender TEXT,\n"
                + " bloodType TEXT,\n"
                + " allergies TEXT,\n"
                + " medicalHistory TEXT,\n"
                + " emergencyContactName TEXT,\n"
                + " emergencyContactPhone TEXT,\n"
                + " insuranceProvider TEXT,\n"
                + " policyNumber TEXT\n"
                + ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating patients table: " + e.getMessage());
        }
    }

    private void createEncounterTable() {
        String sql = "CREATE TABLE IF NOT EXISTS encounters (\n"
                + " id integer PRIMARY KEY,\n"
                + " patient_id integer NOT NULL,\n"
                + " encounter_date text NOT NULL,\n"
                + " reason_for_visit text,\n"
                + " diagnosis text,\n"
                + " doctor_notes text,\n"
                + " treatment_plan text,\n"
                + " FOREIGN KEY (patient_id) REFERENCES patients (id)\n"
                + ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating encounters table: " + e.getMessage());
        }
    }

    private void createAppointmentTable() {
        String sql = "CREATE TABLE IF NOT EXISTS appointments (\n"
                + " id integer PRIMARY KEY,\n"
                + " patient_id integer NOT NULL,\n"
                + " doctor_id integer NOT NULL,\n"
                + " appointment_date_time text NOT NULL,\n"
                + " reason text,\n"
                + " FOREIGN KEY (patient_id) REFERENCES patients (id),\n"
                + " FOREIGN KEY (doctor_id) REFERENCES users (id)\n"
                + ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating appointments table: " + e.getMessage());
        }
    }

    public void insertUser(User user) {
        String sql = "INSERT INTO users(username, password, role_id) VALUES(?,?,?)";
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setInt(3, user.getRole().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    public void deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    public void deletePatient(String cedula) {
        String sql = "DELETE FROM patients WHERE cedula = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
        }
    }

    public User getUser(String username, String password) {
        String sql = "SELECT u.id, u.username, u.password, r.id as role_id, r.name as role_name FROM users u "
                + "JOIN roles r ON u.role_id = r.id "
                + "WHERE u.username = ?";
        User user = null;
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    user = new User(rs.getInt("id"), rs.getString("username"), hashedPassword, role);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting user: " + e.getMessage());
        }
        return user;
    }

    public List<Role> getRoles() {
        String sql = "SELECT id, name FROM roles";
        List<Role> roles = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roles.add(new Role(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.out.println("Error getting roles: " + e.getMessage());
        }
        return roles;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT u.id, u.username, u.password, r.id as role_id, r.name as role_name FROM users u "
                + "JOIN roles r ON u.role_id = r.id";
        List<User> users = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), role);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }

    public List<Patient> getAllPatients() {
        String sql = "SELECT * FROM patients";
        List<Patient> patients = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getString("cedula"),
                        rs.getString("email"),
                        rs.getString("date"),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("bloodType"),
                        rs.getString("allergies"),
                        rs.getString("medicalHistory"),
                        rs.getString("emergencyContactName"),
                        rs.getString("emergencyContactPhone"),
                        rs.getString("insuranceProvider"),
                        rs.getString("policyNumber")
                );
                patient.setId(rs.getInt("id"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all patients: " + e.getMessage());
        }
        return patients;
    }

    public void insert(Patient patient) {
        String sql = "INSERT INTO patients(name, lastName, cedula, email, date, age, address, phone, gender, bloodType, allergies, medicalHistory, emergencyContactName, emergencyContactPhone, insuranceProvider, policyNumber) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getCedula());
            pstmt.setString(4, patient.getEmail());
            pstmt.setString(5, patient.getDate());
            pstmt.setInt(6, patient.getAge());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getPhone());
            pstmt.setString(9, patient.getGender());
            pstmt.setString(10, patient.getBloodType());
            pstmt.setString(11, patient.getAllergies());
            pstmt.setString(12, patient.getMedicalHistory());
            pstmt.setString(13, patient.getEmergencyContactName());
            pstmt.setString(14, patient.getEmergencyContactPhone());
            pstmt.setString(15, patient.getInsuranceProvider());
            pstmt.setString(16, patient.getPolicyNumber());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                patient.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertEncounter(Encounter encounter) {
        String sql = "INSERT INTO encounters(patient_id, encounter_date, reason_for_visit, diagnosis, doctor_notes, treatment_plan) VALUES(?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, encounter.getPatientId());
            pstmt.setString(2, encounter.getEncounterDate().toString());
            pstmt.setString(3, encounter.getReasonForVisit());
            pstmt.setString(4, encounter.getDiagnosis());
            pstmt.setString(5, encounter.getDoctorNotes());
            pstmt.setString(6, encounter.getTreatmentPlan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting encounter: " + e.getMessage());
        }
    }

    public List<Encounter> getEncountersForPatient(int patientId) {
        String sql = "SELECT * FROM encounters WHERE patient_id = ? ORDER BY encounter_date DESC";
        List<Encounter> encounters = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Encounter encounter = new Encounter(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        LocalDate.parse(rs.getString("encounter_date")),
                        rs.getString("reason_for_visit"),
                        rs.getString("diagnosis"),
                        rs.getString("doctor_notes"),
                        rs.getString("treatment_plan")
                );
                encounters.add(encounter);
            }
        } catch (SQLException e) {
            System.out.println("Error getting encounters: " + e.getMessage());
        }
        return encounters;
    }

    public void updateUser(User user, boolean passwordChanged) {
        String sql;
        if (passwordChanged) {
            sql = "UPDATE users SET password = ?, role_id = ? WHERE username = ?";
        } else {
            sql = "UPDATE users SET role_id = ? WHERE username = ?";
        }

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (passwordChanged) {
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                pstmt.setString(1, hashedPassword);
                pstmt.setInt(2, user.getRole().getId());
                pstmt.setString(3, user.getUsername());
            } else {
                pstmt.setInt(1, user.getRole().getId());
                pstmt.setString(2, user.getUsername());
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name = ?, lastName = ?, cedula = ?, email = ?, date = ?, age = ?, address = ?, phone = ?, gender = ?, "
                + "bloodType = ?, allergies = ?, medicalHistory = ?, emergencyContactName = ?, emergencyContactPhone = ?, "
                + "insuranceProvider = ?, policyNumber = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getCedula());
            pstmt.setString(4, patient.getEmail());
            pstmt.setString(5, patient.getDate());
            pstmt.setInt(6, patient.getAge());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getPhone());
            pstmt.setString(9, patient.getGender());
            pstmt.setString(10, patient.getBloodType());
            pstmt.setString(11, patient.getAllergies());
            pstmt.setString(12, patient.getMedicalHistory());
            pstmt.setString(13, patient.getEmergencyContactName());
            pstmt.setString(14, patient.getEmergencyContactPhone());
            pstmt.setString(15, patient.getInsuranceProvider());
            pstmt.setString(16, patient.getPolicyNumber());
            pstmt.setInt(17, patient.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }

    public void insertAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date_time, reason) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatient().getId());
            pstmt.setInt(2, appointment.getDoctor().getId());
            pstmt.setString(3, appointment.getAppointmentDateTime().toString());
            pstmt.setString(4, appointment.getReason());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting appointment: " + e.getMessage());
        }
    }

    public void updateAppointment(Appointment appointment) {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date_time = ?, reason = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatient().getId());
            pstmt.setInt(2, appointment.getDoctor().getId());
            pstmt.setString(3, appointment.getAppointmentDateTime().toString());
            pstmt.setString(4, appointment.getReason());
            pstmt.setInt(5, appointment.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }

    public void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }
    }

    public List<Appointment> getAppointmentsForDate(LocalDate date) {
        String sql = "SELECT a.id, a.appointment_date_time, a.reason, " +
                "p.id as patient_id, p.name as patient_name, p.lastName as patient_lastName, p.cedula as patient_cedula, p.email as patient_email, p.date as patient_date, p.age as patient_age, p.address as patient_address, p.phone as patient_phone, p.gender as patient_gender, " +
                "p.bloodType as patient_bloodType, p.allergies as patient_allergies, p.medicalHistory as patient_medicalHistory, p.emergencyContactName as patient_emergencyContactName, p.emergencyContactPhone as patient_emergencyContactPhone, p.insuranceProvider as patient_insuranceProvider, p.policyNumber as patient_policyNumber, " +
                "u.id as doctor_id, u.username as doctor_username, u.password as doctor_password, r.id as role_id, r.name as role_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN users u ON a.doctor_id = u.id " +
                "JOIN roles r ON u.role_id = r.id " +
                "WHERE date(a.appointment_date_time) = ?";

        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getString("patient_name"),
                        rs.getString("patient_lastName"),
                        rs.getString("patient_cedula"),
                        rs.getString("patient_email"),
                        rs.getString("patient_date"),
                        rs.getInt("patient_age"),
                        rs.getString("patient_address"),
                        rs.getString("patient_phone"),
                        rs.getString("patient_gender"),
                        rs.getString("patient_bloodType"),
                        rs.getString("patient_allergies"),
                        rs.getString("patient_medicalHistory"),
                        rs.getString("patient_emergencyContactName"),
                        rs.getString("patient_emergencyContactPhone"),
                        rs.getString("patient_insuranceProvider"),
                        rs.getString("patient_policyNumber")
                );
                patient.setId(rs.getInt("patient_id"));

                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                User doctor = new User(rs.getInt("doctor_id"), rs.getString("doctor_username"), rs.getString("doctor_password"), role);

                Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        patient,
                        doctor,
                        LocalDateTime.parse(rs.getString("appointment_date_time")),
                        rs.getString("reason")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("Error getting appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    public void insertInitialData() {
        String sql = "INSERT OR IGNORE INTO patients (cedula, name, lastName, age, gender, address, bloodType, medicalHistory, policyNumber, insuranceProvider, phone, email, date, emergencyContactName, emergencyContactPhone, allergies) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String[][] patientData = {
                {"1001", "Luis Alberto", "Rodríguez", "45", "M", "Bogotá", "O+", "Dolor abdominal crónico", "K58.9", "Dieta FODMAP, Espasmolíticos", "Estable", "luis.rodriguez@example.com", "1979-05-20", "Ana Rodríguez", "3101234567", "Ninguna"},
                {"1002", "María del Carmen", "Sánchez", "62", "F", "Medellín", "A-", "Hipertensión no controlada", "I10", "Losartán 50mg, control de sodio", "En Monitoreo", "maria.sanchez@example.com", "1962-11-10", "Juan Sánchez", "3111234567", "AINEs"},
                {"1003", "José Antonio", "Martínez", "78", "M", "Cali", "B+", "Insuficiencia Cardíaca Congestiva", "I50.9", "Diuréticos, Betabloqueadores", "Crítico", "jose.martinez@example.com", "1946-01-30", "Marta Martínez", "3121234567", "Penicilina"},
                {"1004", "Luz Marina", "Gómez", "29", "F", "Barranquilla", "AB+", "Migraña con aura frecuente", "G43.1", "Sumatriptán, Propranolol", "Estable", "luz.gomez@example.com", "1995-03-15", "Carlos Gómez", "3131234567", "Ninguna"},
                {"1005", "Carlos Andrés", "García", "19", "M", "Cartagena", "O-", "Fractura de Tibia y Peroné", "S82.9", "Cirugía, Fisioterapia", "Recuperación", "carlos.garcia@example.com", "2005-08-25", "Isabel García", "3141234567", "Sulfas"}
            };

            for (String[] data : patientData) {
                pstmt.setString(1, data[0]);
                pstmt.setString(2, data[1]);
                pstmt.setString(3, data[2]);
                pstmt.setInt(4, Integer.parseInt(data[3]));
                pstmt.setString(5, data[4]);
                pstmt.setString(6, data[5]);
                pstmt.setString(7, data[6]);
                pstmt.setString(8, data[7]);
                pstmt.setString(9, data[8]);
                pstmt.setString(10, data[9]);
                pstmt.setString(11, data[10]);
                pstmt.setString(12, data[11]);
                pstmt.setString(13, data[12]);
                pstmt.setString(14, data[13]);
                pstmt.setString(15, data[14]);
                pstmt.setString(16, data[15]);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error inserting initial patient data: " + e.getMessage());
        }
    }
}

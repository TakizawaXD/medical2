package com.example.demo.data;

import com.example.demo.model.Encounter;
import com.example.demo.model.Patient;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
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
        populateRoles();
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
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " lastName text NOT NULL,\n"
                + " dni text NOT NULL,\n"
                + " date text NOT NULL,\n"
                + " address text NOT NULL,\n"
                + " phone text NOT NULL,\n"
                + " gender text NOT NULL\n"
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
    
    public void deletePatient(String dni) {
        String sql = "DELETE FROM patients WHERE dni = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
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
                    user = new User(rs.getString("username"), hashedPassword, role);
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
                User user = new User(rs.getString("username"), rs.getString("password"), role);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }

    public void insert(Patient patient) {
        String sql = "INSERT INTO patients(name, lastName, dni, date, address, phone, gender) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getDni());
            pstmt.setString(4, patient.getDate());
            pstmt.setString(5, patient.getAddress());
            pstmt.setString(6, patient.getPhone());
            pstmt.setString(7, patient.getGender());
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
}

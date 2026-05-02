package com.airtribe.meditrack.util;


import com.airtribe.meditrack.entity.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    private static DateUtil DateUtil;

    private CSVUtil() {
    }

    // ===== PATIENTS =====

    public static void savePatients(List<Patient> patients, String filePath) {
        ensureDirectoryExists(filePath);
        // try-with-resources — BufferedWriter auto-closed
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,name,age,gender,phone,email,address,bloodGroup,emergencyContact,medicalHistory");
            writer.newLine();
            for (Patient p : patients) {
                String history = String.join("|", p.getMedicalHistory());
                writer.write(String.join(",",
                        p.getId(), p.getName(), String.valueOf(p.getAge()),
                        p.getGender(), p.getPhone(), p.getEmail(),
                        p.getAddress(), p.getBloodGroup(), p.getEmergencyContact(),
                        history));
                writer.newLine();
            }
            System.out.println("[CSVUtil] Saved " + patients.size() + " patients to " + filePath);
        } catch (IOException e) {
            System.err.println("[CSVUtil] Error saving patients: " + e.getMessage());
        }
    }

    public static List<Patient> loadPatients(String filePath) {
        List<Patient> patients = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[CSVUtil] No patient file found at " + filePath);
            return patients;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                } // skip header
                String[] parts = line.split(",", -1);
                if (parts.length < 9) continue;
                Patient p = new Patient(parts[0], parts[1], Integer.parseInt(parts[2].trim()),
                        parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]);
                if (parts.length > 9 && !parts[9].isEmpty()) {
                    for (String hist : parts[9].split("\\|")) {
                        p.addMedicalHistory(hist);
                    }
                }
                patients.add(p);
            }
            System.out.println("[CSVUtil] Loaded " + patients.size() + " patients from " + filePath);
        } catch (IOException | NumberFormatException e) {
            System.err.println("[CSVUtil] Error loading patients: " + e.getMessage());
        }
        return patients;
    }

    // ===== DOCTORS =====

    public static void saveDoctors(List<Doctor> doctors, String filePath) {
        ensureDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,name,age,gender,phone,email,address,specialization,consultationFee,experienceYears,available");
            writer.newLine();
            for (Doctor d : doctors) {
                writer.write(String.join(",",
                        d.getId(), d.getName(), String.valueOf(d.getAge()),
                        d.getGender(), d.getPhone(), d.getEmail(), d.getAddress(),
                        d.getSpecialization().name(),
                        String.valueOf(d.getConsultationFee()),
                        String.valueOf(d.getExperienceYears()),
                        String.valueOf(d.isAvailable())));
                writer.newLine();
            }
            System.out.println("[CSVUtil] Saved " + doctors.size() + " doctors to " + filePath);
        } catch (IOException e) {
            System.err.println("[CSVUtil] Error saving doctors: " + e.getMessage());
        }
    }

    public static List<Doctor> loadDoctors(String filePath) {
        List<Doctor> doctors = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[CSVUtil] No doctor file found at " + filePath);
            return doctors;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 11) continue;
                Specialization spec = Specialization.valueOf(parts[7].trim());
                Doctor d = new Doctor(parts[0], parts[1], Integer.parseInt(parts[2].trim()),
                        parts[3], parts[4], parts[5], parts[6],
                        spec, Double.parseDouble(parts[8].trim()),
                        Integer.parseInt(parts[9].trim()));
                d.setAvailable(Boolean.parseBoolean(parts[10].trim()));
                doctors.add(d);
            }
            System.out.println("[CSVUtil] Loaded " + doctors.size() + " doctors from " + filePath);
        } catch (IOException | NumberFormatException e) {
            System.err.println("[CSVUtil] Error loading doctors: " + e.getMessage());
        }
        return doctors;
    }

    // ===== APPOINTMENTS =====

    public static void saveAppointments(List<Appointment> appointments, String filePath) {
        ensureDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("appointmentId,patientId,doctorId,dateTime,status,reason,notes");
            writer.newLine();
            for (Appointment a : appointments) {
                writer.write(String.join(",",
                        a.getAppointmentId(), a.getPatientId(), a.getDoctorId(),
                        DateUtil.formatForCsv(a.getAppointmentDateTime()),
                        a.getStatus().name(), a.getReason(),
                        a.getNotes().replace(",", ";")));
                writer.newLine();
            }
            System.out.println("[CSVUtil] Saved " + appointments.size() + " appointments to " + filePath);
        } catch (IOException e) {
            System.err.println("[CSVUtil] Error saving appointments: " + e.getMessage());
        }
    }

    public static List<Appointment> loadAppointments(String filePath) {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[CSVUtil] No appointment file found at " + filePath);
            return appointments;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;
                LocalDateTime dt = DateUtil.parse(parts[3].trim());
                Appointment a = new Appointment(parts[0], parts[1], parts[2], dt, parts[5]);
                a.setStatus(AppointmentStatus.valueOf(parts[4].trim()));
                if (parts.length > 6) a.setNotes(parts[6]);
                appointments.add(a);
            }
            System.out.println("[CSVUtil] Loaded " + appointments.size() + " appointments from " + filePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("[CSVUtil] Error loading appointments: " + e.getMessage());
        }
        return appointments;
    }

    private static void ensureDirectoryExists(String filePath) {
        File f = new File(filePath);
        if (f.getParentFile() != null) {
            f.getParentFile().mkdirs();
        }
    }
}
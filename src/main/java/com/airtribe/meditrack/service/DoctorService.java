package com.airtribe.meditrack.service;

import java.util.stream.Collectors;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.Interface.Searchable;
import com.airtribe.meditrack.util.*;

import java.util.*;

public class DoctorService {

    private final DataStore<Doctor> doctorStore;
    private final IdGenerator idGenerator;

    public DoctorService() {
        this.doctorStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
        seedSampleDoctors();
    }

    // ===== CREATE =====
    public Doctor addDoctor(String name, int age, String gender, String phone,
                            String email, String address, Specialization specialization,
                            double consultationFee, int experienceYears) {
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validatePhone(phone);
        Validator.validateEmail(email);
        Validator.validateFee(consultationFee);
        Validator.validateExperience(experienceYears);

        String id = idGenerator.generateDoctorId();
        Doctor doctor = new Doctor(id, name, age, gender, phone, email, address,
                specialization, consultationFee, experienceYears);
        doctorStore.add(id, doctor);
        System.out.println("Doctor added: " + doctor);
        return doctor;
    }

    // ===== READ =====
    public Doctor getDoctorById(String id) {
        Doctor d = doctorStore.getById(id);
        if (d == null) throw new InvalidDataException("doctorId", "No doctor found with ID: " + id);
        return d;
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    // ===== UPDATE =====
    public void updateDoctor(String id, String phone, String email, double consultationFee) {
        Doctor d = getDoctorById(id);
        Validator.validatePhone(phone);
        Validator.validateEmail(email);
        Validator.validateFee(consultationFee);
        d.setPhone(phone);
        d.setEmail(email);
        d.setConsultationFee(consultationFee);
        System.out.println("Doctor updated: " + id);
    }

    // ===== DELETE =====
    public boolean removeDoctor(String id) {
        boolean removed = doctorStore.remove(id);
        if (removed) {
            Doctor.decrementTotalDoctors();
            System.out.println("Doctor removed: " + id);
        } else {
            System.out.println("Doctor not found: " + id);
        }
        return removed;
    }

    // ===== SEARCH (Polymorphism — overloading) =====
    /** Search by keyword (name, specialization, id) */
    public List<Doctor> searchDoctor(String keyword) {
        return doctorStore.getAll().stream()
                .filter(d -> d.matchesSearch(keyword))
                .collect(Collectors.toList());
    }

    /** Search by specialization */
    public List<Doctor> searchDoctor(Specialization specialization) {
        return doctorStore.getAll().stream()
                .filter(d -> d.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    /** Search by minimum experience */
    public List<Doctor> searchDoctor(int minExperienceYears) {
        return doctorStore.getAll().stream()
                .filter(d -> d.getExperienceYears() >= minExperienceYears)
                .collect(Collectors.toList());
    }

    // ===== STREAMS & LAMBDAS (Bonus D) =====
    /** Get available doctors sorted by experience descending. */
    public List<Doctor> getAvailableDoctors() {
        return doctorStore.getAll().stream()
                .filter(Doctor::isAvailable)
                .sorted(Comparator.comparingInt(Doctor::getExperienceYears).reversed())
                .collect(Collectors.toList());
    }

    /** Average consultation fee across all doctors. */
    public double getAverageConsultationFee() {
        return doctorStore.getAll().stream()
                .mapToDouble(Doctor::getConsultationFee)
                .average()
                .orElse(0.0);
    }

    /** Doctors grouped by specialization. */
    public Map<Specialization, List<Doctor>> getDoctorsBySpecialization() {
        return doctorStore.getAll().stream()
                .collect(Collectors.groupingBy(Doctor::getSpecialization));
    }

    /** Analytics: appointment count per doctor. */
    public Map<String, Integer> getAppointmentsPerDoctor() {
        return doctorStore.getAll().stream()
                .collect(Collectors.toMap(
                        d -> d.getName() + " (" + d.getId() + ")",
                        d -> d.getAppointmentIds().size()
                ));
    }

    public DataStore<Doctor> getDoctorStore() { return doctorStore; }

    public void loadDoctors(List<Doctor> doctors) {
        doctorStore.clear();
        for (Doctor d : doctors) {
            doctorStore.add(d.getId(), d);
        }
    }

    // ===== SEED DATA =====
    private void seedSampleDoctors() {
        Doctor d1 = new Doctor(idGenerator.generateDoctorId(), "Arjun Mehta", 45, "Male",
                "9876543210", "arjun.mehta@hospital.com", "Mumbai",
                Specialization.CARDIOLOGY, 1500.0, 20);
        Doctor d2 = new Doctor(idGenerator.generateDoctorId(), "Priya Sharma", 38, "Female",
                "9876543211", "priya.sharma@hospital.com", "Delhi",
                Specialization.DERMATOLOGY, 900.0, 12);
        Doctor d3 = new Doctor(idGenerator.generateDoctorId(), "Ravi Kumar", 52, "Male",
                "9876543212", "ravi.kumar@hospital.com", "Bangalore",
                Specialization.NEUROLOGY, 1800.0, 25);
        Doctor d4 = new Doctor(idGenerator.generateDoctorId(), "Sunita Patel", 41, "Female",
                "9876543213", "sunita.patel@hospital.com", "Hyderabad",
                Specialization.PEDIATRICS, 800.0, 15);
        Doctor d5 = new Doctor(idGenerator.generateDoctorId(), "Vikram Singh", 35, "Male",
                "9876543214", "vikram.singh@hospital.com", "Chennai",
                Specialization.GENERAL_MEDICINE, 600.0, 8);
        doctorStore.add(d1.getId(), d1);
        doctorStore.add(d2.getId(), d2);
        doctorStore.add(d3.getId(), d3);
        doctorStore.add(d4.getId(), d4);
        doctorStore.add(d5.getId(), d5);
        System.out.println("[DoctorService] 5 sample doctors loaded.");
    }
}

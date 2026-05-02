package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import java.util.List;
import java.util.stream.Collectors;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.List;

public class PatientService {

    private final DataStore<Patient> patientStore;
    private final IdGenerator idGenerator;

    public PatientService() {
        this.patientStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
        seedSamplePatients();
    }

    // ===== CREATE =====
    public Patient addPatient(String name, int age, String gender, String phone,
                              String email, String address, String bloodGroup, String emergencyContact) {
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validatePhone(phone);
        Validator.validateEmail(email);

        String id = idGenerator.generatePatientId();
        Patient patient = new Patient(id, name, age, gender, phone, email, address, bloodGroup, emergencyContact);
        patientStore.add(id, patient);
        System.out.println("Patient registered: " + patient);
        return patient;
    }

    // ===== READ =====
    public Patient getPatientById(String id) {
        Patient p = patientStore.getById(id);
        if (p == null) throw new InvalidDataException("patientId", "No patient found with ID: " + id);
        return p;
    }

    public List<Patient> getAllPatients() {
        return patientStore.getAll();
    }

    // ===== UPDATE =====
    public void updatePatient(String id, String phone, String email, String address) {
        Patient p = getPatientById(id);
        Validator.validatePhone(phone);
        Validator.validateEmail(email);
        p.setPhone(phone);
        p.setEmail(email);
        p.setAddress(address);
        System.out.println("Patient updated: " + id);
    }

    public void addMedicalHistory(String patientId, String condition) {
        Patient p = getPatientById(patientId);
        p.addMedicalHistory(condition);
        System.out.println("Medical history updated for patient: " + patientId);
    }

    // ===== DELETE =====
    public boolean removePatient(String id) {
        boolean removed = patientStore.remove(id);
        if (removed) {
            Patient.decrementTotalPatients();
            System.out.println("Patient removed: " + id);
        }
        return removed;
    }

    // ===== SEARCH (overloaded — polymorphism) =====
    public List<Patient> searchPatient(String keyword) {
        return patientStore.getAll().stream()
                .filter(p -> p.matchesSearch(keyword))
                .collect(Collectors.toList());
    }

    /** Search by exact age */
    public List<Patient> searchPatient(int age) {
        return patientStore.getAll().stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }

    /** Search by age range */
    public List<Patient> searchPatient(int minAge, int maxAge) {
        return patientStore.getAll().stream()
                .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    // ===== Deep vs Shallow Copy Demo =====
    public void demonstrateCopy(String patientId) {
        Patient original = getPatientById(patientId);
        original.addMedicalHistory("Hypertension");

        Patient deepCopy = original.clone();
        Patient shallowCopy = original.shallowCopy();

        System.out.println("\n--- Deep vs Shallow Copy Demo ---");
        System.out.println("Original medical history: " + original.getMedicalHistory());

        // Modifying deep copy — does NOT affect original
        deepCopy.addMedicalHistory("Diabetes (deep copy only)");
        // Modifying shallow copy — DOES affect original (shared reference)
        shallowCopy.addMedicalHistory("Shared condition (affects original too!)");

        System.out.println("After deep copy modification:    " + original.getMedicalHistory());
        System.out.println("Deep copy's history:             " + deepCopy.getMedicalHistory());
        System.out.println("Shallow copy shares same list:   " + (original.getMedicalHistory() == shallowCopy.getMedicalHistory()));
        System.out.println("---------------------------------");
    }

    public DataStore<Patient> getPatientStore() { return patientStore; }

    public void loadPatients(List<Patient> patients) {
        patientStore.clear();
        for (Patient p : patients) {
            patientStore.add(p.getId(), p);
        }
    }

    private void seedSamplePatients() {
        Patient p1 = new Patient(idGenerator.generatePatientId(), "Rahul Gupta", 32, "Male",
                "9123456789", "rahul.gupta@email.com", "Pune", "B+", "9123456700");
        Patient p2 = new Patient(idGenerator.generatePatientId(), "Anjali Singh", 28, "Female",
                "9123456790", "anjali.singh@email.com", "Delhi", "O+", "9123456701");
        Patient p3 = new Patient(idGenerator.generatePatientId(), "Mohan Lal", 65, "Male",
                "9123456791", "mohan.lal@email.com", "Jaipur", "A-", "9123456702");
        p1.addMedicalHistory("Asthma");
        p2.addMedicalHistory("Diabetes Type 2");
        p3.addMedicalHistory("Arthritis");
        patientStore.add(p1.getId(), p1);
        patientStore.add(p2.getId(), p2);
        patientStore.add(p3.getId(), p3);
        System.out.println("[PatientService] 3 sample patients loaded.");
    }
}

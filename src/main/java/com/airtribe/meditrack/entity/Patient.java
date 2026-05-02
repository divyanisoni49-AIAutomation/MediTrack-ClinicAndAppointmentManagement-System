package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.Interface.Searchable;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Person implements Searchable, Cloneable {

    private static final long serialVersionUID = 1L;

    private static int totalPatients = 0;

    static {
        System.out.println("[Patient] Patient class initialized.");
        totalPatients = 0;
    }

    private String bloodGroup;
    private List<String> medicalHistory;   // Mutable list — needs deep copy
    private List<String> appointmentIds;
    private String emergencyContact;

    /**
     * Full constructor.
     */
    public Patient(String id, String name, int age, String gender, String phone,
                   String email, String address, String bloodGroup, String emergencyContact) {
        super(id, name, age, gender, phone, email, address);
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.medicalHistory = new ArrayList<>();
        this.appointmentIds = new ArrayList<>();
        totalPatients++;
    }

    // Overloaded constructor — no address / emergency contact
    public Patient(String id, String name, int age, String gender, String phone, String email, String bloodGroup) {
        this(id, name, age, gender, phone, email, "N/A", bloodGroup, "N/A");
    }

    @Override
    public String getEntityType() { return "Patient"; }

    @Override
    public void displayInfo() {
        System.out.println("------ Patient Info ------");
        System.out.printf("ID               : %s%n", getId());
        System.out.printf("Name             : %s%n", getName());
        System.out.printf("Age              : %d%n", getAge());
        System.out.printf("Gender           : %s%n", getGender());
        System.out.printf("Phone            : %s%n", getPhone());
        System.out.printf("Email            : %s%n", getEmail());
        System.out.printf("Blood Group      : %s%n", bloodGroup);
        System.out.printf("Emergency Contact: %s%n", emergencyContact);
        System.out.printf("Medical History  : %s%n", medicalHistory.isEmpty() ? "None" : String.join(", ", medicalHistory));
        System.out.println("--------------------------");
    }

    @Override
    public boolean matchesSearch(String keyword) {
        String kw = keyword.toLowerCase();
        return getName().toLowerCase().contains(kw)
                || getId().toLowerCase().contains(kw)
                || bloodGroup.toLowerCase().contains(kw);
    }

    // ===== Deep Copy via Cloneable =====
    /**
     * Deep clone — copies all mutable fields by value,
     * not just references (shallow copy would share lists).
     */
    @Override
    public Patient clone() {
        try {
            Patient cloned = (Patient) super.clone(); // Shallow copy from Object
            // Deep copy mutable lists
            cloned.medicalHistory = new ArrayList<>(this.medicalHistory);
            cloned.appointmentIds = new ArrayList<>(this.appointmentIds);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrates shallow copy — both share the same list reference.
     */
    public Patient shallowCopy() {
        Patient copy = new Patient(this.getId(), this.getName(), this.getAge(),
                this.getGender(), this.getPhone(), this.getEmail(),
                this.getAddress(), this.bloodGroup, this.emergencyContact);
        // SHALLOW: copy.medicalHistory points to SAME list object
        copy.medicalHistory = this.medicalHistory;
        copy.appointmentIds = this.appointmentIds;
        return copy;
    }

    // ===== Getters & Setters =====
    public String getBloodGroup() { return bloodGroup; }
    public List<String> getMedicalHistory() { return medicalHistory; }
    public List<String> getAppointmentIds() { return appointmentIds; }
    public String getEmergencyContact() { return emergencyContact; }

    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public void addMedicalHistory(String condition) { this.medicalHistory.add(condition); }
    public void addAppointmentId(String id) { this.appointmentIds.add(id); }

    public static int getTotalPatients() { return totalPatients; }
    public static void decrementTotalPatients() { if (totalPatients > 0) totalPatients--; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Patient)) return false;
        Patient other = (Patient) obj;
        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s | Age: %d | Blood: %s | Phone: %s",
                getName(), getAge(), bloodGroup, getPhone());
    }

    @Override
    public boolean matches(String query) {
        return false;
    }

    public void printSearchResult() {
        System.out.println("Patient found: " + this);
    }
}

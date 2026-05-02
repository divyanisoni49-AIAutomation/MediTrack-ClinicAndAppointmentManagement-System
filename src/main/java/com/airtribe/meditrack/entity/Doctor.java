package com.airtribe.meditrack.entity;
import com.airtribe.meditrack.Interface.Searchable;

import java.util.ArrayList;
import java.util.List;


public class Doctor extends Person implements Searchable{

    private static final long serialVersionUID = 1L;

    // Static counter — shared across ALL Doctor instances
    private static int totalDoctors = 0;

    // Static block — executes once when Doctor class is loaded
    static {
        System.out.println("[Doctor] Doctor class initialized.");
        totalDoctors = 0;
    }

    private Specialization specialization;
    private double consultationFee;
    private int experienceYears;
    private boolean available;
    private List<String> appointmentIds; // IDs of appointments for this doctor

    /**
     * Full constructor.
     */
    public Doctor(String id, String name, int age, String gender, String phone,
                  String email, String address, Specialization specialization,
                  double consultationFee, int experienceYears) {
        super(id, name, age, gender, phone, email, address);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.experienceYears = experienceYears;
        this.available = true;
        this.appointmentIds = new ArrayList<>();
        totalDoctors++;
    }

    // Overloaded constructor — uses specialization default fee
    public Doctor(String id, String name, int age, String gender, String phone,
                  String email, Specialization specialization, int experienceYears) {
        this(id, name, age, gender, phone, email, "N/A",
                specialization, specialization.getConsultationFee(), experienceYears);
    }

    // ===== Abstract method implementations =====
    @Override
    public String getEntityType() { return "Doctor"; }

    @Override
    public void displayInfo() {
        System.out.println("------ Doctor Info ------");
        System.out.printf("ID            : %s%n", getId());
        System.out.printf("Name          : Dr. %s%n", getName());
        System.out.printf("Age           : %d%n", getAge());
        System.out.printf("Gender        : %s%n", getGender());
        System.out.printf("Phone         : %s%n", getPhone());
        System.out.printf("Email         : %s%n", getEmail());
        System.out.printf("Specialization: %s%n", specialization.getDisplayName());
        System.out.printf("Consult Fee   : Rs. %.2f%n", consultationFee);
        System.out.printf("Experience    : %d years%n", experienceYears);
        System.out.printf("Available     : %s%n", available ? "Yes" : "No");
        System.out.println("-------------------------");
    }

    // ===== Searchable interface =====
    @Override
    public boolean matchesSearch(String keyword) {
        String kw = keyword.toLowerCase();
        return getName().toLowerCase().contains(kw)
                || specialization.getDisplayName().toLowerCase().contains(kw)
                || getId().toLowerCase().contains(kw);
    }

    // ===== Getters & Setters =====
    public Specialization getSpecialization() { return specialization; }
    public double getConsultationFee() { return consultationFee; }
    public int getExperienceYears() { return experienceYears; }
    public boolean isAvailable() { return available; }
    public List<String> getAppointmentIds() { return appointmentIds; }

    public void setSpecialization(Specialization specialization) { this.specialization = specialization; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public void setAvailable(boolean available) { this.available = available; }

    public void addAppointmentId(String appointmentId) {
        this.appointmentIds.add(appointmentId);
    }

    public static int getTotalDoctors() { return totalDoctors; }
    public static void decrementTotalDoctors() { if (totalDoctors > 0) totalDoctors--; }

    @Override
    public String toString() {
        return String.format("Dr. %s | %s | Fee: Rs.%.2f | Exp: %d yrs | Available: %s",
                getName(), specialization.getDisplayName(), consultationFee, experienceYears, available ? "Yes" : "No");
    }

    @Override
    public boolean matches(String query) {
        return false;
    }

    public void printSearchResult() {
        System.out.printf("Doctor: %s | Specialization: %s | Fee: Rs. %.2f | Experience: %d years%n",
                getName(), specialization.getDisplayName(), consultationFee, experienceYears);
    }
}

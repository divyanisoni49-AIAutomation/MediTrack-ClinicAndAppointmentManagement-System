package com.airtribe.meditrack.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment  implements Serializable, Cloneable{

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String reason;
    private String notes;

    public Appointment(String appointmentId, String patientId, String doctorId,
                       LocalDateTime appointmentDateTime, String reason) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = AppointmentStatus.PENDING;
        this.notes = "";
    }

    // ===== Deep Clone =====
    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone();
            // LocalDateTime is immutable — no deep copy needed
            // String is immutable — no deep copy needed
            // AppointmentStatus (enum) is immutable
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed: " + e.getMessage());
        }
    }

    // ===== Display =====
    public void displayInfo() {
        System.out.println("------ Appointment Info ------");
        System.out.printf("Appointment ID : %s%n", appointmentId);
        System.out.printf("Patient ID     : %s%n", patientId);
        System.out.printf("Doctor ID      : %s%n", doctorId);
        System.out.printf("Date & Time    : %s%n", appointmentDateTime.format(FORMATTER));
        System.out.printf("Status         : %s%n", status.getDisplayName());
        System.out.printf("Reason         : %s%n", reason);
        System.out.printf("Notes          : %s%n", notes.isEmpty() ? "None" : notes);
        System.out.println("------------------------------");
    }

    // ===== Getters & Setters =====
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public AppointmentStatus getStatus() { return status; }
    public String getReason() { return reason; }
    public String getNotes() { return notes; }

    public void setAppointmentDateTime(LocalDateTime dt) { this.appointmentDateTime = dt; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setReason(String reason) { this.reason = reason; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Appointment)) return false;
        return this.appointmentId.equals(((Appointment) obj).appointmentId);
    }

    @Override
    public int hashCode() { return appointmentId.hashCode(); }

    @Override
    public String toString() {
        return String.format("[%s] Patient:%s | Doctor:%s | %s | Status:%s",
                appointmentId, patientId, doctorId,
                appointmentDateTime.format(FORMATTER), status.getDisplayName());
    }

    public Object getId() {
        return appointmentId;
    }

    public Object getDateTime() {
        return appointmentDateTime;
    }
}

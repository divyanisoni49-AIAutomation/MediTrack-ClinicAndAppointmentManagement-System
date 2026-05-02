package com.airtribe.meditrack.exception;

public class AppointmentNotFoundException extends Exception {

    private final String appointmentId;

    public AppointmentNotFoundException(String appointmentId) {
        super("Appointment not found with ID: " + appointmentId);
        this.appointmentId = appointmentId;
    }

    // Exception chaining — wraps another exception
    public AppointmentNotFoundException(String appointmentId, Throwable cause) {
        super("Appointment not found with ID: " + appointmentId, cause);
        this.appointmentId = appointmentId;
    }

    public String getAppointmentId() { return appointmentId; }
}

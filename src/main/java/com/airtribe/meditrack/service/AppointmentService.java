package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.observer.AppointmentEventPublisher;
import com.airtribe.meditrack.observer.ConsoleNotificationObserver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;



public class AppointmentService {
    private final DataStore<Appointment> appointmentStore;
    private final IdGenerator idGenerator;
    private final AppointmentEventPublisher eventPublisher;

    public AppointmentService() {
        this.appointmentStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
        this.eventPublisher = AppointmentEventPublisher.getInstance();
        // Register observer (Observer Pattern)
        eventPublisher.subscribe(new ConsoleNotificationObserver());
    }

    // ===== CREATE =====
    public Appointment bookAppointment(Patient patient, Doctor doctor,
                                       LocalDateTime dateTime, String reason)
            throws AppointmentNotFoundException {

        if (!doctor.isAvailable()) {
            throw new InvalidDataException("doctor", "Doctor " + doctor.getName() + " is not available.");
        }
        if (!DateUtil.isFuture(dateTime)) {
            throw new InvalidDataException("dateTime", "Appointment must be in the future.");
        }

        String id = idGenerator.generateAppointmentId();
        Appointment appointment = new Appointment(id, patient.getId(), doctor.getId(), dateTime, reason);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        appointmentStore.add(id, appointment);
        patient.addAppointmentId(id);
        doctor.addAppointmentId(id);

        // Notify observers
        eventPublisher.publish(appointment, "BOOKED");
        System.out.println("Appointment booked: " + appointment);
        return appointment;
    }

    // ===== READ =====
    public Appointment getAppointmentById(String id) throws AppointmentNotFoundException {
        Appointment a = appointmentStore.getById(id);
        if (a == null) throw new AppointmentNotFoundException(id);
        return a;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentStore.getAll().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentStore.getAll().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    // ===== CANCEL =====
    public void cancelAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment a = getAppointmentById(appointmentId);
        if (a.getStatus() == AppointmentStatus.CANCELLED) {
            System.out.println("Appointment is already cancelled.");
            return;
        }
        a.setStatus(AppointmentStatus.CANCELLED);
        eventPublisher.publish(a, "CANCELLED");
        System.out.println("Appointment cancelled: " + appointmentId);
    }

    // ===== COMPLETE =====
    public void completeAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment a = getAppointmentById(appointmentId);
        a.setStatus(AppointmentStatus.COMPLETED);
        eventPublisher.publish(a, "COMPLETED");
        System.out.println("Appointment marked completed: " + appointmentId);
    }

    // ===== STREAMS Analytics =====
    public long countByStatus(AppointmentStatus status) {
        return appointmentStore.getAll().stream()
                .filter(a -> a.getStatus() == status)
                .count();
    }

    public DataStore<Appointment> getAppointmentStore() { return appointmentStore; }

    public void loadAppointments(List<Appointment> appointments) {
        appointmentStore.clear();
        for (Appointment a : appointments) {
            appointmentStore.add(a.getAppointmentId(), a);
        }
    }
}

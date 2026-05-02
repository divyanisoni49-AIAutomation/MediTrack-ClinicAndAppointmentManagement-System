package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;

import java.util.concurrent.atomic.AtomicInteger;


public class IdGenerator {

    // ========== EAGER SINGLETON ==========
    private static final IdGenerator INSTANCE = new IdGenerator(); // Created at class load

    // Thread-safe counters using AtomicInteger (intro to concurrency)
    private final AtomicInteger patientCounter;
    private final AtomicInteger doctorCounter;
    private final AtomicInteger appointmentCounter;
    private final AtomicInteger billCounter;

    // Static block — runs when class is loaded
    static {
        System.out.println("[IdGenerator] Singleton instance initialized (eager).");
    }

    private IdGenerator() {
        patientCounter = new AtomicInteger(1000);
        doctorCounter = new AtomicInteger(2000);
        appointmentCounter = new AtomicInteger(3000);
        billCounter = new AtomicInteger(4000);
    }

    /** Get the single instance (eager singleton). */
    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    // ========== LAZY SINGLETON (for demonstration) ==========
    private static volatile IdGenerator lazyInstance;

    /** Lazy singleton with double-checked locking. */
    public static IdGenerator getLazyInstance() {
        if (lazyInstance == null) {
            synchronized (IdGenerator.class) {
                if (lazyInstance == null) {
                    lazyInstance = new IdGenerator();
                    System.out.println("[IdGenerator] Lazy singleton created.");
                }
            }
        }
        return lazyInstance;
    }

    // ========== ID Generation Methods ==========

    public String generatePatientId() {
        return Constants.PATIENT_ID_PREFIX + patientCounter.getAndIncrement();
    }

    public String generateDoctorId() {
        return Constants.DOCTOR_ID_PREFIX + doctorCounter.getAndIncrement();
    }

    public String generateAppointmentId() {
        return Constants.APPOINTMENT_ID_PREFIX + appointmentCounter.getAndIncrement();
    }

    public String generateBillId() {
        return Constants.BILL_ID_PREFIX + billCounter.getAndIncrement();
    }

    // For seeding counters when loading from file
    public void setPatientCounter(int value) { patientCounter.set(value); }
    public void setDoctorCounter(int value) { doctorCounter.set(value); }
    public void setAppointmentCounter(int value) { appointmentCounter.set(value); }
    public void setBillCounter(int value) { billCounter.set(value); }
}

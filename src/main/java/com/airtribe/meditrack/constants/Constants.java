package com.airtribe.meditrack.constants;

public class Constants {

    private Constants() {}

    // Tax & Billing
    public static final double TAX_RATE = 0.18; // 18% GST
    public static final double CONSULTATION_BASE_FEE = 500.0;
    public static final double SPECIALIST_SURCHARGE = 300.0;

    // File Paths
    public static final String PATIENTS_FILE = "data/patients.csv";
    public static final String DOCTORS_FILE = "data/doctors.csv";
    public static final String APPOINTMENTS_FILE = "data/appointments.csv";

    // ID Prefixes
    public static final String PATIENT_ID_PREFIX = "PAT";
    public static final String DOCTOR_ID_PREFIX = "DOC";
    public static final String APPOINTMENT_ID_PREFIX = "APT";
    public static final String BILL_ID_PREFIX = "BILL";

    // Validation
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 130;
    public static final String PHONE_REGEX = "^[6-9]\\d{9}$";
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";

    // App Info
    public static final String APP_NAME = "MediTrack";
    public static final String APP_VERSION = "1.0.0";
    public static final String SEPARATOR = "=".repeat(60);
    public static final String LINE = "-".repeat(60);
}

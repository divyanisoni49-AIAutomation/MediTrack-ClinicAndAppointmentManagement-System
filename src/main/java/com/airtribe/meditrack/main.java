package com.airtribe.meditrack;

import com.airtribe.meditrack.Test.TestRunner;
import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.*;
import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.util.*;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Scanner;

public class main {

    // Services
    private static PatientService patientService;
    private static DoctorService doctorService;
    private static AppointmentService appointmentService;
    private static BillingService billingService;

    private static Scanner scanner;

    // Static initializer — application-wide setup
    static {
        System.out.println("=".repeat(60));
        System.out.println("   Welcome to " + Constants.APP_NAME + " v" + Constants.APP_VERSION);
        System.out.println("   Hospital Management System");
        System.out.println("=".repeat(60));
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        // Initialize services
        patientService = new PatientService();
        doctorService = new DoctorService();
        appointmentService = new AppointmentService();
        billingService = new BillingService();

        // Command-line argument handling
        for (String arg : args) {
            if (arg.equals("--test")) {

                TestRunner.runAllTests();
                return;
            }
            if (arg.equals("--loadData")) {
                loadDataFromFiles();
            }
        }

        showMainMenu();
    }

    // ===== MAIN MENU =====
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n" + Constants.SEPARATOR);
            System.out.println("                   MAIN MENU");
            System.out.println(Constants.SEPARATOR);
            System.out.println("  1. Patient Management");
            System.out.println("  2. Doctor Management");
            System.out.println("  3. Appointment Management");
            System.out.println("  4. Billing");
            System.out.println("  5. Search");
            System.out.println("  6. Analytics (Streams & Lambdas)");
            System.out.println("  7. AI Doctor Recommendation");
            System.out.println("  8. Deep vs Shallow Copy Demo");
            System.out.println("  9. Save Data to CSV");
            System.out.println("  10. Run Tests");
            System.out.println("  0. Exit");
            System.out.println(Constants.SEPARATOR);
            System.out.print("Choose option: ");

            int choice = readInt();
            switch (choice) {
                case 1: patientMenu(); break;
                case 2: doctorMenu(); break;
                case 3: appointmentMenu(); break;
                case 4: billingMenu(); break;
                case 5: searchMenu(); break;
                case 6: analyticsMenu(); break;
                case 7: aiRecommendationMenu(); break;
                case 8: demonstrateCopyMenu(); break;
                case 9: saveAllData(); break;
                case 10: TestRunner.runAllTests(); break;
                case 0:
                    System.out.println("Thank you for using MediTrack. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ===== PATIENT MENU =====
    private static void patientMenu() {
        System.out.println("\n--- Patient Management ---");
        System.out.println("1. Add Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. View Patient by ID");
        System.out.println("4. Update Patient");
        System.out.println("5. Add Medical History");
        System.out.println("6. Remove Patient");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        int choice = readInt();
        try {
            switch (choice) {
                case 1: addPatient(); break;
                case 2: viewAllPatients(); break;
                case 3:
                    System.out.print("Enter Patient ID: ");
                    String pid = scanner.nextLine().trim();
                    patientService.getPatientById(pid).displayInfo();
                    break;
                case 4: updatePatient(); break;
                case 5: addMedicalHistory(); break;
                case 6:
                    System.out.print("Enter Patient ID to remove: ");
                    patientService.removePatient(scanner.nextLine().trim());
                    break;
                case 0: return;
            }
        } catch (InvalidDataException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void addPatient() {
        System.out.println("\n-- Add New Patient --");
        System.out.print("Name: "); String name = scanner.nextLine().trim();
        System.out.print("Age: "); int age = readInt();
        System.out.print("Gender (Male/Female/Other): "); String gender = scanner.nextLine().trim();
        System.out.print("Phone (10 digits): "); String phone = scanner.nextLine().trim();
        System.out.print("Email: "); String email = scanner.nextLine().trim();
        System.out.print("Address: "); String address = scanner.nextLine().trim();
        System.out.print("Blood Group (A+/A-/B+/B-/O+/O-/AB+/AB-): "); String blood = scanner.nextLine().trim();
        System.out.print("Emergency Contact: "); String emergency = scanner.nextLine().trim();
        try {
            Patient p = patientService.addPatient(name, age, gender, phone, email, address, blood, emergency);
            System.out.println("Patient registered with ID: " + p.getId());
        } catch (InvalidDataException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        System.out.println("\n-- All Patients (" + patients.size() + ") --");
        patients.forEach(p -> System.out.println("  " + p));
    }

    private static void updatePatient() {
        System.out.print("Patient ID to update: "); String id = scanner.nextLine().trim();
        System.out.print("New Phone: "); String phone = scanner.nextLine().trim();
        System.out.print("New Email: "); String email = scanner.nextLine().trim();
        System.out.print("New Address: "); String address = scanner.nextLine().trim();
        try {
            patientService.updatePatient(id, phone, email, address);
        } catch (InvalidDataException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void addMedicalHistory() {
        System.out.print("Patient ID: "); String id = scanner.nextLine().trim();
        System.out.print("Medical Condition: "); String condition = scanner.nextLine().trim();
        patientService.addMedicalHistory(id, condition);
    }

    // ===== DOCTOR MENU =====
    private static void doctorMenu() {
        System.out.println("\n--- Doctor Management ---");
        System.out.println("1. Add Doctor");
        System.out.println("2. View All Doctors");
        System.out.println("3. View Doctor by ID");
        System.out.println("4. Update Doctor");
        System.out.println("5. Remove Doctor");
        System.out.println("6. View Available Doctors");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        int choice = readInt();
        try {
            switch (choice) {
                case 1: addDoctor(); break;
                case 2:
                    doctorService.getAllDoctors().forEach(d -> System.out.println("  " + d));
                    break;
                case 3:
                    System.out.print("Enter Doctor ID: ");
                    doctorService.getDoctorById(scanner.nextLine().trim()).displayInfo();
                    break;
                case 4: updateDoctor(); break;
                case 5:
                    System.out.print("Doctor ID to remove: ");
                    doctorService.removeDoctor(scanner.nextLine().trim());
                    break;
                case 6:
                    doctorService.getAvailableDoctors().forEach(d -> System.out.println("  " + d));
                    break;
                case 0: return;
            }
        } catch (InvalidDataException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void addDoctor() {
        System.out.println("\n-- Add New Doctor --");
        System.out.print("Name: "); String name = scanner.nextLine().trim();
        System.out.print("Age: "); int age = readInt();
        System.out.print("Gender: "); String gender = scanner.nextLine().trim();
        System.out.print("Phone: "); String phone = scanner.nextLine().trim();
        System.out.print("Email: "); String email = scanner.nextLine().trim();
        System.out.print("Address: "); String address = scanner.nextLine().trim();

        System.out.println("Specializations:");
        Specialization[] specs = Specialization.values();
        for (int i = 0; i < specs.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, specs[i].getDisplayName());
        }
        System.out.print("Choose specialization (1-" + specs.length + "): ");
        int specChoice = readInt() - 1;
        if (specChoice < 0 || specChoice >= specs.length) { System.out.println("Invalid."); return; }
        Specialization spec = specs[specChoice];

        System.out.print("Consultation Fee (Rs.): "); double fee = readDouble();
        System.out.print("Years of Experience: "); int exp = readInt();
        try {
            Doctor d = doctorService.addDoctor(name, age, gender, phone, email, address, spec, fee, exp);
            System.out.println("Doctor added with ID: " + d.getId());
        } catch (InvalidDataException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private static void updateDoctor() {
        System.out.print("Doctor ID: "); String id = scanner.nextLine().trim();
        System.out.print("New Phone: "); String phone = scanner.nextLine().trim();
        System.out.print("New Email: "); String email = scanner.nextLine().trim();
        System.out.print("New Consultation Fee: "); double fee = readDouble();
        try {
            doctorService.updateDoctor(id, phone, email, fee);
        } catch (InvalidDataException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    // ===== APPOINTMENT MENU =====
    private static void appointmentMenu() {
        System.out.println("\n--- Appointment Management ---");
        System.out.println("1. Book Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. View Appointments by Patient");
        System.out.println("4. Cancel Appointment");
        System.out.println("5. Complete Appointment");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        int choice = readInt();
        try {
            switch (choice) {
                case 1: bookAppointment(); break;
                case 2:
                    appointmentService.getAllAppointments().forEach(a -> System.out.println("  " + a));
                    break;
                case 3:
                    System.out.print("Patient ID: ");
                    String pid = scanner.nextLine().trim();
                    appointmentService.getAppointmentsByPatient(pid).forEach(a -> a.displayInfo());
                    break;
                case 4:
                    System.out.print("Appointment ID to cancel: ");
                    appointmentService.cancelAppointment(scanner.nextLine().trim());
                    break;
                case 5:
                    System.out.print("Appointment ID to complete: ");
                    appointmentService.completeAppointment(scanner.nextLine().trim());
                    break;
                case 0: return;
            }
        } catch (AppointmentNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("[Validation] " + e.getMessage());
        }
    }

    private static void bookAppointment() {
        System.out.print("Patient ID: "); String pid = scanner.nextLine().trim();
        System.out.print("Doctor ID: "); String did = scanner.nextLine().trim();
        System.out.print("Date & Time (yyyy-MM-dd HH:mm): "); String dtStr = scanner.nextLine().trim();
        System.out.print("Reason: "); String reason = scanner.nextLine().trim();
        try {
            Patient p = patientService.getPatientById(pid);
            Doctor d = doctorService.getDoctorById(did);
            LocalDateTime dt = DateUtil.parse(dtStr);
            Appointment apt = appointmentService.bookAppointment(p, d, dt, reason);
            System.out.println("Booked! Appointment ID: " + apt.getAppointmentId());
        } catch (InvalidDataException | AppointmentNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[Date Error] " + e.getMessage());
        }
    }

    // ===== BILLING MENU =====
    private static void billingMenu() {
        System.out.println("\n--- Billing ---");
        System.out.println("1. Generate Bill for Appointment");
        System.out.println("2. Pay Bill");
        System.out.println("3. View Bills for Patient");
        System.out.println("4. View Bill Summary (Immutable BillSummary)");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        int choice = readInt();
        try {
            switch (choice) {
                case 1: generateBill(); break;
                case 2:
                    System.out.print("Bill ID: "); billingService.payBill(scanner.nextLine().trim());
                    break;
                case 3:
                    System.out.print("Patient ID: ");
                    billingService.getBillsForPatient(scanner.nextLine().trim()).forEach(Bill::displayBill);
                    break;
                case 4:
                    System.out.print("Patient ID: ");
                    String pid = scanner.nextLine().trim();
                    Patient patient = patientService.getPatientById(pid);
                    billingService.generateSummary(patient).display();
                    break;
                case 0: return;
            }
        } catch (InvalidDataException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void generateBill() {
        System.out.print("Appointment ID: "); String aptId = scanner.nextLine().trim();
        System.out.print("Emergency billing? (y/n): "); boolean emergency = scanner.nextLine().trim().equalsIgnoreCase("y");
        try {
            Appointment apt = appointmentService.getAppointmentById(aptId);
            Patient p = patientService.getPatientById(apt.getPatientId());
            Doctor d = doctorService.getDoctorById(apt.getDoctorId());
            Bill bill = billingService.generateBill(p, d, apt, emergency);
            bill.displayBill();
            bill.printPaymentInfo(); // Payable interface default method
        } catch (AppointmentNotFoundException | InvalidDataException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    // ===== SEARCH MENU =====
    private static void searchMenu() {
        System.out.println("\n--- Search ---");
        System.out.println("1. Search Patients (by name/ID/blood group)");
        System.out.println("2. Search Patients by Age");
        System.out.println("3. Search Doctors (by keyword)");
        System.out.println("4. Search Doctors by Specialization");
        System.out.println("5. Search Doctors by Min Experience");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        int choice = readInt();
        switch (choice) {
            case 1:
                System.out.print("Keyword: ");
                patientService.searchPatient(scanner.nextLine().trim())
                        .forEach(p -> { System.out.println("  " + p); p.printSearchResult(); });
                break;
            case 2:
                System.out.print("Age: ");
                patientService.searchPatient(readInt()).forEach(p -> System.out.println("  " + p));
                break;
            case 3:
                System.out.print("Keyword: ");
                doctorService.searchDoctor(scanner.nextLine().trim())
                        .forEach(d -> { System.out.println("  " + d); d.printSearchResult(); });
                break;
            case 4:
                System.out.println("Specializations: ");
                Specialization[] specs = Specialization.values();
                for (int i = 0; i < specs.length; i++) System.out.printf("  %d. %s%n", i+1, specs[i].getDisplayName());
                System.out.print("Choose: ");
                int si = readInt() - 1;
                if (si >= 0 && si < specs.length)
                    doctorService.searchDoctor(specs[si]).forEach(d -> System.out.println("  " + d));
                break;
            case 5:
                System.out.print("Min Experience (years): ");
                doctorService.searchDoctor(readInt()).forEach(d -> System.out.println("  " + d));
                break;
            case 0: return;
        }
    }

    // ===== ANALYTICS =====
    private static void analyticsMenu() {
        System.out.println("\n--- Analytics (Java Streams & Lambdas) ---");
        System.out.printf("Total Patients    : %d%n", patientService.getAllPatients().size());
        System.out.printf("Total Doctors     : %d%n", doctorService.getAllDoctors().size());
        System.out.printf("Total Appointments: %d%n", appointmentService.getAllAppointments().size());
        System.out.printf("Average Doctor Fee: Rs. %.2f%n", doctorService.getAverageConsultationFee());
        System.out.printf("Confirmed Apts    : %d%n", appointmentService.countByStatus(AppointmentStatus.CONFIRMED));
        System.out.printf("Cancelled Apts    : %d%n", appointmentService.countByStatus(AppointmentStatus.CANCELLED));
        System.out.printf("Completed Apts    : %d%n", appointmentService.countByStatus(AppointmentStatus.COMPLETED));

        System.out.println("\nDoctors by Specialization:");
        doctorService.getDoctorsBySpecialization().forEach((spec, docs) ->
                System.out.printf("  %-25s: %d doctor(s)%n", spec.getDisplayName(), docs.size()));

        System.out.println("\nAppointments per Doctor:");
        doctorService.getAppointmentsPerDoctor().forEach((doc, count) ->
                System.out.printf("  %-40s: %d appointment(s)%n", doc, count));
    }

    // ===== AI MENU =====
    private static void aiRecommendationMenu() {
        System.out.println("\n--- AI Doctor Recommendation ---");
        AIHelper.printKnownSymptoms();
        System.out.print("\nDescribe your symptoms: ");
        String symptoms = scanner.nextLine().trim();
        List<Doctor> recommended = AIHelper.recommendDoctors(symptoms, doctorService.getAllDoctors());
        if (recommended.isEmpty()) {
            System.out.println("No available doctors found for your symptoms.");
        } else {
            System.out.println("Recommended doctors:");
            recommended.forEach(d -> d.displayInfo());
        }
    }

    // ===== COPY DEMO =====
    private static void demonstrateCopyMenu() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) { System.out.println("No patients to demo."); return; }
        patientService.demonstrateCopy(patients.get(0).getId());
    }

    // ===== FILE I/O =====
    private static void saveAllData() {
        CSVUtil.savePatients(patientService.getAllPatients(), Constants.PATIENTS_FILE);
        CSVUtil.saveDoctors(doctorService.getAllDoctors(), Constants.DOCTORS_FILE);
        CSVUtil.saveAppointments(appointmentService.getAllAppointments(), Constants.APPOINTMENTS_FILE);
        System.out.println("All data saved to CSV files.");
    }

    private static void loadDataFromFiles() {
        System.out.println("[Main] Loading data from CSV files...");
        List<Patient> patients = CSVUtil.loadPatients(Constants.PATIENTS_FILE);
        if (!patients.isEmpty()) patientService.loadPatients(patients);

        List<Doctor> doctors = CSVUtil.loadDoctors(Constants.DOCTORS_FILE);
        if (!doctors.isEmpty()) doctorService.loadDoctors(doctors);

        List<Appointment> appointments = CSVUtil.loadAppointments(Constants.APPOINTMENTS_FILE);
        if (!appointments.isEmpty()) appointmentService.loadAppointments(appointments);
    }

    // ===== Helpers =====
    private static int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}

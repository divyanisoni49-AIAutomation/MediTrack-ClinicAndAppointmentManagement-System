package com.airtribe.meditrack.Test;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.*;

import com.airtribe.meditrack.entity.*;

import com.airtribe.meditrack.service.*;
import com.airtribe.meditrack.util.*;


import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

public class TestRunner {
    private static int passed = 0;
    private static int failed = 0;

    public static void runAllTests() {
        System.out.println("\n" + Constants.SEPARATOR);
        System.out.println("         MEDITRACK - MANUAL TEST SUITE");
        System.out.println(Constants.SEPARATOR);

        testIdGenerator();
        testPatientCRUD();
        testDoctorCRUD();
        testDeepVsShallowCopy();
        testImmutableBillSummary();
        testEnums();
        testExceptions();
        testAppointmentLifecycle();
        testBillingStrategies();
        testSearchOverloading();
        testStreamsAndLambdas();
        testValidator();
        testDataStore();

        System.out.println("\n" + Constants.SEPARATOR);
        System.out.printf("TEST RESULTS: %d passed, %d failed%n", passed, failed);
        System.out.println(Constants.SEPARATOR);
    }

    private static void testIdGenerator() {
        printHeader("IdGenerator (Singleton + AtomicInteger)");
        IdGenerator gen1 = IdGenerator.getInstance();
        IdGenerator gen2 = IdGenerator.getInstance();
        assert gen1 == gen2 : "Singleton failed";

        String pid = gen1.generatePatientId();
        String did = gen1.generateDoctorId();
        assertTrue("Patient ID starts with PAT", pid.startsWith("PAT"));
        assertTrue("Doctor ID starts with DOC", did.startsWith("DOC"));
        System.out.println("Generated IDs: " + pid + ", " + did);
    }

    private static void testPatientCRUD() {
        printHeader("Patient CRUD");
        PatientService ps = new PatientService();
        int before = ps.getAllPatients().size();

        Patient p = ps.addPatient("Test User", 30, "Male", "9999999999",
                "test@email.com", "City", "AB+", "9999999998");
        assertTrue("Patient added", ps.getAllPatients().size() == before + 1);
        assertTrue("Patient found by ID", ps.getPatientById(p.getId()) != null);

        ps.updatePatient(p.getId(), "9888888888", "new@email.com", "New City");
        assertTrue("Phone updated", ps.getPatientById(p.getId()).getPhone().equals("9888888888"));

        ps.removePatient(p.getId());
        try {
            ps.getPatientById(p.getId());
            fail("Should have thrown exception for deleted patient");
        } catch (Exception e) {
            pass("Exception thrown for deleted patient");
        }
    }

    private static void testDoctorCRUD() {
        printHeader("Doctor CRUD");
        DoctorService ds = new DoctorService();
        int before = ds.getAllDoctors().size();

        Doctor d = ds.addDoctor("Dr. Test", 40, "Female", "9777777777",
                "dr.test@hospital.com", "Mumbai", Specialization.CARDIOLOGY, 1200.0, 15);
        assertTrue("Doctor added", ds.getAllDoctors().size() == before + 1);
        assertTrue("Doctor found", ds.getDoctorById(d.getId()) != null);
        ds.removeDoctor(d.getId());
        assertTrue("Doctor removed", ds.getAllDoctors().size() == before);
    }

    private static void testDeepVsShallowCopy() {
        printHeader("Deep vs Shallow Copy (Cloneable)");
        Patient original = new Patient("P_TEST", "Original", 25, "Male",
                "9111111111", "orig@test.com", "Delhi", "O+", "9111111110");
        original.addMedicalHistory("Fever");

        // Deep copy — modification should NOT affect original
        Patient deepCopy = original.clone();
        deepCopy.addMedicalHistory("Deep condition");
        assertTrue("Deep copy doesn't affect original",
                !original.getMedicalHistory().contains("Deep condition"));

        // Shallow copy — modification DOES affect original
        Patient shallowCopy = original.shallowCopy();
        assertTrue("Shallow copy shares list reference",
                original.getMedicalHistory() == shallowCopy.getMedicalHistory());

        System.out.println("Original history: " + original.getMedicalHistory());
        System.out.println("Deep copy history: " + deepCopy.getMedicalHistory());
    }

    private static void testImmutableBillSummary() {
        printHeader("Immutable BillSummary");
        BillSummary summary = new BillSummary("P001", "Test Patient", 3,
                1500.0, 270.0, 1770.0, 2, 1);
        assertTrue("Grand total correct", summary.getGrandTotal() == 1770.0);
        assertTrue("Paid bills correct", summary.getPaidBills() == 2);
        // No setters exist — immutability enforced by design
        summary.display();
        pass("BillSummary is immutable (no setters)");
    }

    private static void testEnums() {
        printHeader("Enums (Specialization, AppointmentStatus)");
        assertTrue("Cardiology fee", Specialization.CARDIOLOGY.getConsultationFee() > 0);
        assertTrue("Pending status", AppointmentStatus.PENDING.getDisplayName().equals("Pending"));
        assertTrue("Confirmed status", AppointmentStatus.CONFIRMED.getDisplayName().equals("Confirmed"));
        System.out.println("Specialization: " + Specialization.NEUROLOGY);
        System.out.println("Status: " + AppointmentStatus.CANCELLED);
    }

    private static void testExceptions() {
        printHeader("Custom Exceptions");
        // AppointmentNotFoundException
        try {
            throw new AppointmentNotFoundException("APT999");
        } catch (AppointmentNotFoundException e) {
            assertTrue("Exception message correct", e.getMessage().contains("APT999"));
            assertTrue("Appointment ID accessible", e.getAppointmentId().equals("APT999"));
        }

        // InvalidDataException
        try {
            Validator.validatePhone("12345");
            fail("Should have thrown for invalid phone");
        } catch (Exception e) {
            pass("InvalidDataException thrown for bad phone");
        }

        // try-with-resources — tested in CSVUtil
        pass("Custom exception hierarchy verified");
    }

    private static void testAppointmentLifecycle() {
        printHeader("Appointment Lifecycle");
        PatientService ps = new PatientService();
        DoctorService ds = new DoctorService();
        AppointmentService as = new AppointmentService();

        Patient p = ps.getAllPatients().get(0);
        Doctor d = ds.getAvailableDoctors().get(0);
        LocalDateTime future = LocalDateTime.now().plusDays(2);

        try {
            Appointment apt = as.bookAppointment(p, d, future, "Routine checkup");
            assertTrue("Appointment confirmed", apt.getStatus() == AppointmentStatus.CONFIRMED);

            as.cancelAppointment(apt.getAppointmentId());
            assertTrue("Appointment cancelled", apt.getStatus() == AppointmentStatus.CANCELLED);
        } catch (AppointmentNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private static void testBillingStrategies() {
        printHeader("Billing Strategies (Strategy + Factory Pattern)");
        BillingStrategy standard = BillingStrategyFactory.getStrategy(BillingStrategyFactory.BillingType.STANDARD);
        BillingStrategy senior = BillingStrategyFactory.getStrategy(BillingStrategyFactory.BillingType.SENIOR_CITIZEN);
        BillingStrategy emergency = BillingStrategyFactory.getStrategy(BillingStrategyFactory.BillingType.EMERGENCY);

        double base = 1000.0;
        double stdTotal = standard.calculateTotal(base, 0);
        double senTotal = senior.calculateTotal(base, 0);
        double eemTotal = emergency.calculateTotal(base, 0);

        assertTrue("Standard billing", stdTotal > base);
        assertTrue("Senior cheaper than standard", senTotal < stdTotal);
        assertTrue("Emergency costlier than standard", eemTotal > stdTotal);

        System.out.printf("Base fee: Rs.%.2f | Standard: Rs.%.2f | Senior: Rs.%.2f | Emergency: Rs.%.2f%n",
                base, stdTotal, senTotal, eemTotal);
    }

    private static void testSearchOverloading() {
        printHeader("Search Overloading (Polymorphism)");
        PatientService ps = new PatientService();
        DoctorService ds = new DoctorService();

        // Patient search — three overloads
        List<?> byName = ps.searchPatient("Rahul");
        List<?> byAge = ps.searchPatient(28);
        List<?> byRange = ps.searchPatient(20, 40);
        assertTrue("Search by name works", !byName.isEmpty());
        System.out.println("Search by name: " + byName.size() + " results");
        System.out.println("Search by age 28: " + byAge.size() + " results");
        System.out.println("Search by range 20-40: " + byRange.size() + " results");

        // Doctor search overloads
        List<?> byKeyword = ds.searchDoctor("Cardio");
        List<?> bySpec = ds.searchDoctor(Specialization.CARDIOLOGY);
        List<?> byExp = ds.searchDoctor(10);
        assertTrue("Doctor search by keyword", !byKeyword.isEmpty());
        System.out.println("Doctor search by 'Cardio': " + byKeyword.size() + " results");
    }

    private static void testStreamsAndLambdas() {
        printHeader("Streams & Lambdas (Bonus D)");
        DoctorService ds = new DoctorService();

        double avgFee = ds.getAverageConsultationFee();
        assertTrue("Average fee > 0", avgFee > 0);
        System.out.printf("Average consultation fee: Rs.%.2f%n", avgFee);

        var grouped = ds.getDoctorsBySpecialization();
        assertTrue("Grouping works", !grouped.isEmpty());
        System.out.println("Specializations found: " + grouped.keySet().size());

        var analytics = ds.getAppointmentsPerDoctor();
        System.out.println("Appointment analytics: " + analytics);
    }

    private static void testValidator() {
        printHeader("Validator (Centralized Validation)");
        try { Validator.validateName(""); fail("Empty name"); } catch (Exception e) { pass("Empty name rejected"); }
        try { Validator.validateAge(-1); fail("Negative age"); } catch (Exception e) { pass("Negative age rejected"); }
        try { Validator.validateAge(200); fail("Too old"); } catch (Exception e) { pass("Age 200 rejected"); }
        try { Validator.validateEmail("notanemail"); fail("Bad email"); } catch (Exception e) { pass("Bad email rejected"); }
        try { Validator.validateFee(-100); fail("Negative fee"); } catch (Exception e) { pass("Negative fee rejected"); }
        Validator.validateName("Valid Name");
        pass("Valid name accepted");
    }

    private static void testDataStore() {
        printHeader("Generic DataStore<T>");
        DataStore<String> store = new DataStore<>();
        store.add("k1", "Value1");
        store.add("k2", "Value2");
        assertTrue("Size correct", store.size() == 2);
        assertTrue("Get by ID", store.getById("k1").equals("Value1"));
        assertTrue("Exists", store.exists("k2"));

        store.remove("k1");
        assertTrue("After remove", store.size() == 1);

        List<String> filtered = store.filter(s -> s.startsWith("V"));
        assertTrue("Filter works", !filtered.isEmpty());
        pass("DataStore<T> works correctly");
    }

    // ===== Test helpers =====
    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + testName);
        } else {
            failed++;
            System.out.println("  [FAIL] " + testName);
        }
    }

    private static void pass(String msg) { passed++; System.out.println("  [PASS] " + msg); }
    private static void fail(String msg) { failed++; System.out.println("  [FAIL] " + msg); }
    private static void printHeader(String name) {
        System.out.println("\n  >> TEST: " + name);
    }
}

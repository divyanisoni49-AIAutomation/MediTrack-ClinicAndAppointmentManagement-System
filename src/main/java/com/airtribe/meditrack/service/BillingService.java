package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillingService {

    private final List<Bill> bills;
    private final IdGenerator idGenerator;

    public BillingService() {
        this.bills = new ArrayList<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    /**
     * Generate a bill for a given appointment.
     * Uses BillingStrategyFactory to select the right strategy.
     */
    public Bill generateBill(Patient patient, Doctor doctor, Appointment appointment, boolean isEmergency) {
        BillingStrategy strategy = BillingStrategyFactory.getStrategy(patient.getAge(), isEmergency);
        System.out.println("[Billing] Using strategy: " + strategy.getStrategyName());

        double additionalCharges = 0.0; // Can be extended for tests, procedures, etc.
        double total = strategy.calculateTotal(doctor.getConsultationFee(), additionalCharges);

        // Create Bill object
        String billId = idGenerator.generateBillId();
        Bill bill = new Bill(billId, patient.getId(), appointment.getAppointmentId(),
                doctor.getConsultationFee(), additionalCharges);
        bills.add(bill);
        return bill;
    }

    /** Pay a bill by its ID. */
    public boolean payBill(String billId) {
        for (Bill bill : bills) {
            if (bill.getBillId().equals(billId)) {
                return bill.processPayment();
            }
        }
        System.out.println("Bill not found: " + billId);
        return false;
    }

    /** Get all bills for a patient. */
    public List<Bill> getBillsForPatient(String patientId) {
        return bills.stream()
                .filter(b -> b.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    /**
     * Generate an immutable BillSummary for a patient.
     * Demonstrates immutable class usage.
     */
    public BillSummary generateSummary(Patient patient) {
        List<Bill> patientBills = getBillsForPatient(patient.getId());
        double totalFees = patientBills.stream().mapToDouble(Bill::getConsultationFee).sum();
        double totalTax = patientBills.stream().mapToDouble(Bill::getTaxAmount).sum();
        double grandTotal = patientBills.stream().mapToDouble(Bill::getTotalAmount).sum();
        long paid = patientBills.stream().filter(Bill::isPaid).count();
        long unpaid = patientBills.stream().filter(b -> !b.isPaid()).count();

        return new BillSummary(patient.getId(), patient.getName(),
                patientBills.size(), totalFees, totalTax, grandTotal,
                (int) paid, (int) unpaid);
    }

    public List<Bill> getAllBills() { return bills; }
}

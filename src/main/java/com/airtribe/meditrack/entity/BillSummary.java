package com.airtribe.meditrack.entity;

public class BillSummary {
    private String billId;
    private String patientId;
    private String appointmentId;
    private double amount;
    private String billingDate;



    public BillSummary(String id, String name, int size, double totalFees, double totalTax, double grandTotal, int paid, int unpaid) {
    }

    public String getBillId() { return billId; }
    public String getPatientId() { return patientId; }
    public String getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public String getBillingDate() { return billingDate; }

    public void display() {
        System.out.println("========== BILL SUMMARY ==========");
        System.out.printf("Bill ID          : %s%n", billId);
        System.out.printf("Patient ID       : %s%n", patientId);
        System.out.printf("Appointment ID   : %s%n", appointmentId);
        System.out.printf("Amount           : Rs. %.2f%n", amount);
        System.out.printf("Billing Date     : %s%n", billingDate);
        System.out.println("=================================");
    }

    public double getGrandTotal() {
        return amount; // Placeholder for actual grand total calculation
    }

    public int getPaidBills() {
        return 0; // Placeholder for actual paid bills count
    }
}

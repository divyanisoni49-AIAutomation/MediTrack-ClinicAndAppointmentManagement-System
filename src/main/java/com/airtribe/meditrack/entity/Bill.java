package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.Interface.Payable;
import com.airtribe.meditrack.constants.Constants;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill implements Serializable, Payable {


    private static final long serialVersionUID = 1L;

    private String billId;
    private String patientId;
    private String appointmentId;
    private double consultationFee;
    private double additionalCharges;
    private double taxAmount;
    private double totalAmount;
    private boolean paid;
    private LocalDateTime billDate;

    public Bill(String billId, String patientId, String appointmentId, double consultationFee, double additionalCharges) {
        this.billId = billId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.consultationFee = consultationFee;
        this.additionalCharges = additionalCharges;
        this.taxAmount = (consultationFee + additionalCharges) * Constants.TAX_RATE;
        this.totalAmount = consultationFee + additionalCharges + taxAmount;
        this.paid = false;
        this.billDate = LocalDateTime.now();
    }

    @Override
    public double calculateTotal() {
        return totalAmount;
    }

    @Override
    public boolean processPayment() {
        this.paid = true;
        System.out.println("Payment of Rs. " + String.format("%.2f", totalAmount) + " processed for Bill: " + billId);
        return true;
    }

    @Override
    public String getPaymentStatus() {
        return paid ? "PAID" : "UNPAID";
    }

    public void displayBill() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("========== BILL ==========");
        System.out.printf("Bill ID          : %s%n", billId);
        System.out.printf("Patient ID       : %s%n", patientId);
        System.out.printf("Appointment ID   : %s%n", appointmentId);
        System.out.printf("Date             : %s%n", billDate.format(fmt));
        System.out.println("--------------------------");
        System.out.printf("Consultation Fee : Rs. %.2f%n", consultationFee);
        System.out.printf("Additional Charges: Rs. %.2f%n", additionalCharges);
        System.out.printf("Tax (%.0f%%)       : Rs. %.2f%n", Constants.TAX_RATE * 100, taxAmount);
        System.out.println("--------------------------");
        System.out.printf("TOTAL            : Rs. %.2f%n", totalAmount);
        System.out.printf("Status           : %s%n", getPaymentStatus());
        System.out.println("==========================");
    }

    // Getters
    public String getBillId() { return billId; }
    public String getPatientId() { return patientId; }
    public String getAppointmentId() { return appointmentId; }
    public double getConsultationFee() { return consultationFee; }
    public double getAdditionalCharges() { return additionalCharges; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotalAmount() { return totalAmount; }
    public boolean isPaid() { return paid; }
    public LocalDateTime getBillDate() { return billDate; }

    @Override
    public String toString() {
        return String.format("Bill[%s] Patient:%s | Total:Rs.%.2f | %s",
                billId, patientId, totalAmount, getPaymentStatus());
    }

    @Override
    public double calculatePayment() {
        return 0;
    }

    public void printPaymentInfo() {
        System.out.println("Payment Info for Bill: " + billId);
            System.out.println("Total Amount: Rs. " + String.format("%.2f", totalAmount));
            System.out.println("Payment Status: " + getPaymentStatus());
    }
}

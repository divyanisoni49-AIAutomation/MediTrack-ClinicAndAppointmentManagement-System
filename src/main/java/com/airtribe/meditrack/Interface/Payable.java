package com.airtribe.meditrack.Interface;

public interface Payable {
    double calculateTotal();

    boolean processPayment();

    String getPaymentStatus();

    double  calculatePayment();

}

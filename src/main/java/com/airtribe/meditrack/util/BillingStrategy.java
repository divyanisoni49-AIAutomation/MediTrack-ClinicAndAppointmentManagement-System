package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;

public interface BillingStrategy {
    double calculateTotal(double baseFee, double additionalCharges);
    String getStrategyName();
}

class StandardBillingStrategy implements BillingStrategy {
    @Override
    public double calculateTotal(double baseFee, double additionalCharges) {
        double subtotal = baseFee + additionalCharges;
        return subtotal + subtotal * Constants.TAX_RATE;
    }

    @Override
    public String getStrategyName() { return "Standard Billing (18% GST)"; }
}

class SeniorCitizenBillingStrategy implements BillingStrategy {
    private static final double DISCOUNT = 0.20; // 20% discount

    @Override
    public double calculateTotal(double baseFee, double additionalCharges) {
        double subtotal = (baseFee + additionalCharges) * (1 - DISCOUNT);
        return subtotal + subtotal * Constants.TAX_RATE;
    }

    @Override
    public String getStrategyName() { return "Senior Citizen Billing (20% discount + 18% GST)"; }
}

class EmergencyBillingStrategy implements BillingStrategy {
    private static final double SURCHARGE = 0.50; // 50% emergency surcharge

    @Override
    public double calculateTotal(double baseFee, double additionalCharges) {
        double subtotal = (baseFee + additionalCharges) * (1 + SURCHARGE);
        return subtotal + subtotal * Constants.TAX_RATE;
    }

    @Override
    public String getStrategyName() { return "Emergency Billing (50% surcharge + 18% GST)"; }
}

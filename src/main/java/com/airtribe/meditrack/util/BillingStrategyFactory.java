package com.airtribe.meditrack.util;

public class BillingStrategyFactory {

    private BillingStrategyFactory() {}

    public enum BillingType {
        STANDARD, SENIOR_CITIZEN, EMERGENCY
    }


    public static BillingStrategy getStrategy(BillingType type) {
        switch (type) {
            case SENIOR_CITIZEN:
                return new SeniorCitizenBillingStrategy();
            case EMERGENCY:
                return new EmergencyBillingStrategy();
            case STANDARD:
            default:
                return new StandardBillingStrategy();
        }
    }

    public static BillingStrategy getStrategy(int patientAge, boolean isEmergency) {
        if (isEmergency) return new EmergencyBillingStrategy();
        if (patientAge >= 60) return new SeniorCitizenBillingStrategy();
        return new StandardBillingStrategy();
    }
}

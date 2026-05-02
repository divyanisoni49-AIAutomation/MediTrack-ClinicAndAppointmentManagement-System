package com.airtribe.meditrack.entity;

public enum Specialization {
    CARDIOLOGY("Cardiology"),
    DERMATOLOGY("Dermatology"),
    NEUROLOGY("Neurology"),
    PEDIATRICS("Pediatrics"),
    ORTHOPEDICS("Orthopedics"),
    PSYCHIATRY("Psychiatry"),
    GYNECOLOGY("Gynecology"),
    ONCOLOGY("Oncology"),
    ENDOCRINOLOGY("Endocrinology"),
    GASTROENTEROLOGY("Gastroenterology"),
    GENERAL_MEDICINE("General Medicine"),
    OPHTHALMOLOGY("Ophthalmology"),
    ENT("ENT");


    private final String displayName;

    Specialization(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }

    public double getConsultationFee() {
        switch (this) {
            case CARDIOLOGY: return 200.0;
            case DERMATOLOGY: return 150.0;
            case NEUROLOGY: return 250.0;
            case PEDIATRICS: return 120.0;
            case ORTHOPEDICS: return 180.0;
            case PSYCHIATRY: return 220.0;
            case GYNECOLOGY: return 160.0;
            case ONCOLOGY: return 300.0;
            case ENDOCRINOLOGY: return 170.0;
            case GASTROENTEROLOGY: return 190.0;
            default: return 100.0; // General fee
        }
    }
}

package com.airtribe.meditrack.util;

import java.util.HashMap;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;

import java.util.*;
import java.util.stream.Collectors;

public class AIHelper {
    private AIHelper() {}


    private static final Map<String, Specialization> SYMPTOM_MAP = new HashMap<>();

    static {
        // Heart-related
        SYMPTOM_MAP.put("chest pain", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("palpitations", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("shortness of breath", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("high blood pressure", Specialization.CARDIOLOGY);

        // Skin
        SYMPTOM_MAP.put("rash", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("acne", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("skin allergy", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("itching", Specialization.DERMATOLOGY);

        // Neuro
        SYMPTOM_MAP.put("headache", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("migraine", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("dizziness", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("seizure", Specialization.NEUROLOGY);

        // Bone
        SYMPTOM_MAP.put("joint pain", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("back pain", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("fracture", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("knee pain", Specialization.ORTHOPEDICS);

        // Pediatrics
        SYMPTOM_MAP.put("child fever", Specialization.PEDIATRICS);
        SYMPTOM_MAP.put("vaccination", Specialization.PEDIATRICS);

        // General
        SYMPTOM_MAP.put("fever", Specialization.GENERAL_MEDICINE);
        SYMPTOM_MAP.put("cold", Specialization.GENERAL_MEDICINE);
        SYMPTOM_MAP.put("cough", Specialization.GENERAL_MEDICINE);
        SYMPTOM_MAP.put("fatigue", Specialization.GENERAL_MEDICINE);

        // ENT
        SYMPTOM_MAP.put("ear pain", Specialization.ENT);
        SYMPTOM_MAP.put("sore throat", Specialization.ENT);
        SYMPTOM_MAP.put("hearing loss", Specialization.ENT);

        // Eyes
        SYMPTOM_MAP.put("blurred vision", Specialization.OPHTHALMOLOGY);
        SYMPTOM_MAP.put("eye pain", Specialization.OPHTHALMOLOGY);
        SYMPTOM_MAP.put("red eye", Specialization.OPHTHALMOLOGY);

        // Mental health
        SYMPTOM_MAP.put("anxiety", Specialization.PSYCHIATRY);
        SYMPTOM_MAP.put("depression", Specialization.PSYCHIATRY);
        SYMPTOM_MAP.put("insomnia", Specialization.PSYCHIATRY);
    }

    /**
     * Recommends a specialization based on symptom keywords.
     */
    public static Specialization recommendSpecialization(String symptomDescription) {
        String lower = symptomDescription.toLowerCase();
        for (Map.Entry<String, Specialization> entry : SYMPTOM_MAP.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return Specialization.GENERAL_MEDICINE; // Default
    }

    /**
     * Recommends matching doctors from a list based on symptoms.
     */
    public static List<Doctor> recommendDoctors(String symptoms, List<Doctor> allDoctors) {
        Specialization recommended = recommendSpecialization(symptoms);
        System.out.println("[AI] Based on symptoms, recommending: " + recommended.getDisplayName());

        List<Doctor> matched = allDoctors.stream()
                .filter(d -> d.getSpecialization() == recommended && d.isAvailable())
                .sorted(Comparator.comparingInt(Doctor::getExperienceYears).reversed())
                .collect(Collectors.toList());

        if (matched.isEmpty()) {
            System.out.println("[AI] No exact match. Showing General Medicine doctors.");
            return allDoctors.stream()
                    .filter(d -> d.getSpecialization() == Specialization.GENERAL_MEDICINE && d.isAvailable())
                    .collect(Collectors.toList());
        }
        return matched;
    }

    /** Print all known symptoms */
    public static void printKnownSymptoms() {
        System.out.println("Known symptoms: " + String.join(", ", SYMPTOM_MAP.keySet()));
    }
}

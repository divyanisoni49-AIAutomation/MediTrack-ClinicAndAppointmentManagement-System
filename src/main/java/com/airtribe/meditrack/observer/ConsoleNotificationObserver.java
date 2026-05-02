package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.AppointmentObserver;

public class ConsoleNotificationObserver implements AppointmentObserver {

     public void notify(String message) {
         System.out.println("[Notification] " + message);
     }

    @Override
    public void onAppointmentEvent(Appointment appointment, String eventType) {
        String message = String.format("Appointment %s: ID=%s, PatientID=%s, DoctorID=%s, DateTime=%s",
                eventType, appointment.getId(), appointment.getPatientId(),
                appointment.getDoctorId(), appointment.getDateTime());
        notify(message);

    }
}

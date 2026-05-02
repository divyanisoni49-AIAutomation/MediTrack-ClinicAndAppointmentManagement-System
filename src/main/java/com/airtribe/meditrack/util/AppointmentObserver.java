package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Appointment;

public interface AppointmentObserver {
    void onAppointmentEvent(Appointment appointment, String eventType);
}

package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.AppointmentObserver;

import java.util.ArrayList;
import java.util.List;

public class AppointmentEventPublisher {
        private static AppointmentEventPublisher instance;
        private final List<AppointmentObserver> observers;

        private AppointmentEventPublisher() {
            observers = new ArrayList<>();
        }

        public static synchronized AppointmentEventPublisher getInstance() {
            if (instance == null) {
                instance = new AppointmentEventPublisher();
            }
            return instance;
        }

        public void registerObserver(AppointmentObserver observer) {
            observers.add(observer);
        }

        public void unregisterObserver(AppointmentObserver observer) {
            observers.remove(observer);
        }

        public void notifyObservers(Appointment appointment, String eventType) {
            for (AppointmentObserver observer : observers) {
                observer.onAppointmentEvent(appointment, eventType);
            }
        }

    public void subscribe(ConsoleNotificationObserver consoleNotificationObserver) {
        registerObserver(consoleNotificationObserver);
    }

    private void registerObserver(ConsoleNotificationObserver consoleNotificationObserver) {
        observers.add(consoleNotificationObserver);
    }

    public void publish(Appointment appointment, String booked) {
        notifyObservers(appointment, booked);
    }
}

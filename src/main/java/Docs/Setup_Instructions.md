MediTrack/
└── src/main/java/com/airtribe/meditrack/
├── Main.java                    ← Entry point, menu UI
├── constants/
│   └── Constants.java           ← App-wide constants
├── entity/
│   ├── MedicalEntity.java       ← Abstract base class
│   ├── Person.java              ← Abstract Person
│   ├── Doctor.java              ← Extends Person
│   ├── Patient.java             ← Extends Person, Cloneable
│   ├── Appointment.java         ← Cloneable
│   ├── Bill.java                ← Implements Payable
│   ├── BillSummary.java         ← Immutable class
│   ├── Specialization.java      ← Enum
│   └── AppointmentStatus.java   ← Enum
├── service/
│   ├── DoctorService.java
│   ├── PatientService.java
│   ├── AppointmentService.java
│   └── BillingService.java
├── util/
│   ├── DataStore.java           ← Generic storage
│   ├── IdGenerator.java         ← Singleton pattern
│   ├── Validator.java           ← Centralized validation
│   ├── DateUtil.java
│   ├── CSVUtil.java             ← File I/O
│   ├── BillingStrategy.java     ← Strategy pattern
│   ├── BillingStrategyFactory.java ← Factory pattern
│   ├── AppointmentObserver.java ← Observer pattern
│   └── AIHelper.java            ← Rule-based AI
├── exception/
│   ├── AppointmentNotFoundException.java
│   └── InvalidDataException.java
├── interfaces/
│   ├── Searchable.java
│   └── Payable.java
└── test/
└── TestRunner.java
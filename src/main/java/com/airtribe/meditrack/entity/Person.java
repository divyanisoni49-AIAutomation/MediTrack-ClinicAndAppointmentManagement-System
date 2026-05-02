package com.airtribe.meditrack.entity;

public abstract class Person extends MedicalEntity{
    private static final long serialVersionUID = 1L;

    // Encapsulated fields — private with getters/setters
    private int age;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String name;

    // Constructor with chaining via super
    public Person(String id, String name, int age, String gender, String phone, String email, String address) {
        super(id, name); // Call MedicalEntity constructor
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Overloaded constructor — no address
    public Person(String id, String name, int age, String gender, String phone, String email) {
        this(id, name, age, gender, phone, email, "N/A");
    }

    // Getters
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    // Setters — validation handled by Validator in service layer
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }

    // ===== Abstract method implementations =====
    public abstract String getEntityType();

    public abstract void displayInfo();

    // ===== Searchable interface =====
    public abstract boolean matchesSearch(String keyword);

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Age: %d | Gender: %s | Phone: %s | Email: %s",
                getId(), getName(), getAge(), getGender(), getPhone(), getEmail());
    }
}

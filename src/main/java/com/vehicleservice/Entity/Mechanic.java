package com.vehicleservice.model;

public class Mechanic extends Person {
    private int mechanicId;
    private Specialization specialization;
    private Availability availability;

    public Mechanic() {
        super();
        this.availability = Availability.AVAILABLE;
    }

    public Mechanic(int mechanicId, String name, String phone, Specialization specialization, Availability availability) {
        super(name, phone);
        this.mechanicId = mechanicId;
        this.specialization = specialization;
        this.availability = availability != null ? availability : Availability.AVAILABLE;
    }

    public Mechanic(String name, String phone, Specialization specialization) {
        super(name, phone);
        this.specialization = specialization;
        this.availability = Availability.AVAILABLE;
    }

    public int getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                "mechanicId=" + mechanicId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", specialization=" + specialization +
                ", availability=" + availability +
                '}';
    }
}

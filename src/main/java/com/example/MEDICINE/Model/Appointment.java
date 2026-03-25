package com.example.MEDICINE.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    public enum Status {
        CONFIRMED, CANCELLED, COMPLETED, PENDING, RESCHEDULED
    }

    public enum Type {
        CONSULTATION, FOLLOW_UP, EMERGENCY, ROUTINE_CHECKUP, TEST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appointmentID;

    private String patientName;
    private String doctorName;

    private String doctorEmail;
    private String patientEmail;

    private String doctorContactNumber;
    private String patientContactNumber;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private Status appointmentStatus = Status.PENDING;

    @Enumerated(EnumType.STRING)
    private Type appointmentType;

    // ✅ FIX: Integer instead of String
    private Integer appointmentDuration;

    private String appointmentReason;
    private String location;
    private String specialty;

    private String meetingLink;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ AUTO TIMESTAMP
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ✅ ALL GETTERS & SETTERS

    public Long getId() { return id; }

    public String getAppointmentID() { return appointmentID; }
    public void setAppointmentID(String appointmentID) { this.appointmentID = appointmentID; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorEmail() { return doctorEmail; }
    public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getDoctorContactNumber() { return doctorContactNumber; }
    public void setDoctorContactNumber(String doctorContactNumber) { this.doctorContactNumber = doctorContactNumber; }

    public String getPatientContactNumber() { return patientContactNumber; }
    public void setPatientContactNumber(String patientContactNumber) { this.patientContactNumber = patientContactNumber; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public Status getAppointmentStatus() { return appointmentStatus; }
    public void setAppointmentStatus(Status appointmentStatus) { this.appointmentStatus = appointmentStatus; }

    public Type getAppointmentType() { return appointmentType; }
    public void setAppointmentType(Type appointmentType) { this.appointmentType = appointmentType; }

    public Integer getAppointmentDuration() { return appointmentDuration; }
    public void setAppointmentDuration(Integer appointmentDuration) { this.appointmentDuration = appointmentDuration; }

    public String getAppointmentReason() { return appointmentReason; }
    public void setAppointmentReason(String appointmentReason) { this.appointmentReason = appointmentReason; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
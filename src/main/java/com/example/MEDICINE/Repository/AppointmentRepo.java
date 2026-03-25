package com.example.MEDICINE.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.MEDICINE.Model.Appointment;
@Repository
public interface AppointmentRepo  extends JpaRepository<Appointment,Long>
{
@Query("SELECT MAX(CAST(SUBSTRING(a.appointmentID, 5) AS int)) FROM Appointment a")
Optional<Integer> findMaxAppointmentIdNumber();

Optional<Appointment> findByAppointmentID(String id);

boolean existsByDoctorNameAndAppointmentDateAndAppointmentTime(String doctorName, LocalDate appointmentDate,
        LocalTime appointmentTime);

boolean existsByDoctorEmailAndAppointmentDateAndAppointmentTime(String doctorEmail, LocalDate appointmentDate,
        LocalTime appointmentTime);


}

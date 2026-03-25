package com.example.MEDICINE.Service;

import com.example.MEDICINE.Model.Appointment;
import com.example.MEDICINE.Model.Appointment.Status;
import com.example.MEDICINE.Repository.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepository;

    public AppointmentRepo getAppointmentRepo() {
        return appointmentRepository;
    }

    @Autowired
    private EmailService emailService;

    // ✅ 1. Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // ✅ 2. Save appointment (WITH VALIDATION 🔥)
    public Appointment saveAppointment(Appointment appointment) {

        // 🔴 Double booking check
        boolean exists = appointmentRepository
                .existsByDoctorNameAndAppointmentDateAndAppointmentTime(
                        appointment.getDoctorName(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime());

        if (exists) {
            throw new RuntimeException("❌ Doctor already booked at this time!");
        }

        // 🟡 Default status
        appointment.setAppointmentStatus(Status.PENDING);

        return appointmentRepository.save(appointment);
    }

    // ✅ 3. Get appointment by DB ID
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // ✅ 4. Update full appointment
    public Optional<Appointment> updateAppointment(String appointmentID, Appointment updatedAppointment) {

        return appointmentRepository.findByAppointmentID(appointmentID)
                .map(existing -> {

                    // 🔥 IMPORTANT: keep same appointmentID
                    updatedAppointment.setAppointmentID(appointmentID);

                    return appointmentRepository.save(updatedAppointment);
                });
    }

    // ✅ 5. Delete appointment
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    // ✅ 6. Partial update (PATCH)
    public Optional<Appointment> updateAppointmentFields(String appointmentID, Map<String, Object> updates) {

        return appointmentRepository.findByAppointmentID(appointmentID)
                .map(appointment -> {

                    updates.forEach((key, value) -> {

                        switch (key) {

                            case "appointmentStatus" -> {
                                try {
                                    Status status = Status.valueOf(value.toString().toUpperCase());
                                    appointment.setAppointmentStatus(status);
                                } catch (Exception e) {
                                    throw new RuntimeException("Invalid Status: " + value);
                                }
                            }

                            case "appointmentDate" ->
                                appointment.setAppointmentDate(LocalDate.parse(value.toString()));

                            case "appointmentTime" ->
                                appointment.setAppointmentTime(LocalTime.parse(value.toString()));

                            case "location" ->
                                appointment.setLocation(value.toString());

                            case "appointmentReason" ->
                                appointment.setAppointmentReason(value.toString());
                        }
                    });

                    return appointmentRepository.save(appointment);
                });
    }

    // ✅ 7. 🔥 APPROVE APPOINTMENT + JITSI + EMAIL
    public Optional<Appointment> approveAppointment(String appointmentID) {

        return appointmentRepository.findByAppointmentID(appointmentID)
                .map(appointment -> {

                    // 🟢 1. Status update
                    appointment.setAppointmentStatus(Status.CONFIRMED);

                    // 🎥 2. Create Jitsi Room
                    String roomName = "MEDI-" + appointment.getAppointmentID();

                    // 🔗 3. Jitsi link (CORRECT)
                    String meetingLink = "https://meet.jit.si/" + roomName;

                    appointment.setMeetingLink(meetingLink);

                    appointmentRepository.save(appointment);

                    // 📩 4. Email content
                    String emailBody = "✅ Appointment Confirmed\n\n"
                            + "Doctor: " + appointment.getDoctorName() + "\n"
                            + "Patient: " + appointment.getPatientName() + "\n"
                            + "Date: " + appointment.getAppointmentDate() + "\n"
                            + "Time: " + appointment.getAppointmentTime() + "\n\n"
                            + "🎥 Join Meeting:\n" + meetingLink;

                    // 📧 5. Send email to doctor
                    emailService.sendEmail(
                            appointment.getDoctorEmail(),
                            "Appointment Confirmed",
                            emailBody);

                    // 📧 6. Send email to patient
                    emailService.sendEmail(
                            appointment.getPatientEmail(),
                            "Appointment Confirmed",
                            emailBody);

                    return appointment;
                });
    }
}
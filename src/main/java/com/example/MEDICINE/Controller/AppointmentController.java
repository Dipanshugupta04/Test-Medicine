package com.example.MEDICINE.Controller;

import com.example.MEDICINE.Model.Appointment;
import com.example.MEDICINE.Repository.AppointmentRepo;
import com.example.MEDICINE.Service.AppointmentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = { "http://127.0.0.1:5502", "http://localhost:5502" })
public class AppointmentController {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private AppointmentService appointmentService;

    private final AtomicLong counter = new AtomicLong(1000);

    // Initialize counter
    @PostConstruct
    public void initCounter() {
        int maxId = appointmentRepo.findMaxAppointmentIdNumber().orElse(1000);
        counter.set(maxId);
    }

    // Create new appointment
    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        String appointmentID = "APT-" + counter.incrementAndGet();
        appointment.setAppointmentID(appointmentID);
        return appointmentService.saveAppointment(appointment);
    }

    // Get all appointments
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Get appointment by ID (DB ID)
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get by appointmentID (custom ID)
    @GetMapping("/appointment/{appointmentID}")
    public ResponseEntity<Appointment> getAppointmentByAppointmentID(@PathVariable String appointmentID) {
        return appointmentService.getAppointmentRepo().findByAppointmentID(appointmentID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update full appointment (by appointmentID)
    @PutMapping("/appointment/{appointmentID}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable String appointmentID,
            @RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(appointmentID, appointment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    // Partial update
    @PatchMapping("/{id}")
    public ResponseEntity<Appointment> updatePartialAppointment(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        return appointmentService.updateAppointmentFields(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Approve appointment → generate link + send email
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Appointment> approveAppointment(@PathVariable String id) {
        return appointmentService.approveAppointment(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
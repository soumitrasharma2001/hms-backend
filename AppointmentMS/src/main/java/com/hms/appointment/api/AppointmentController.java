package com.hms.appointment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.appointment.dto.AppointmentDTO;
import com.hms.appointment.dto.AppointmentDetails;
import com.hms.appointment.dto.MedicineDTO;
import com.hms.appointment.dto.MonthlyVisitDTO;
import com.hms.appointment.dto.PatientCountDTO;
import com.hms.appointment.dto.PatientDTO;
import com.hms.appointment.dto.ReasonCountDTO;
import com.hms.appointment.dto.Status;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.service.AppointmentService;
import com.hms.appointment.service.PrescriptionService;

@RestController
@RequestMapping("/appointment")
@Validated
public class AppointmentController {
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private PrescriptionService prescriptionService;
	@PostMapping("/schedule")
	public ResponseEntity<AppointmentDTO> scheduleAppointment(@RequestBody AppointmentDTO appointmentDTO) throws HmsException{
		return new ResponseEntity<>(appointmentService.scheduleAppointment(appointmentDTO),HttpStatus.CREATED);
	}
	
	@PutMapping("/cancel/{appointmentId}")
	public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) throws HmsException{
		appointmentService.cancelAppointment(appointmentId);
		return new ResponseEntity<>("Appointment cancelled",HttpStatus.OK);
	}
	
	@GetMapping("/get/{appointmentId}")
	public ResponseEntity<AppointmentDTO> getAppointmentDetails(@PathVariable Long appointmentId) throws HmsException{
		return new ResponseEntity<>(appointmentService.getAppointmentDetails(appointmentId),HttpStatus.OK);
	}
	
	@GetMapping("/get/details/{appointmentId}")
	public ResponseEntity<AppointmentDetails> getAppointmentDetailsWithName(@PathVariable Long appointmentId) throws HmsException{
		return new ResponseEntity<>(appointmentService.getAppointmentDetailsWithName(appointmentId),HttpStatus.OK);
	}
	
	@GetMapping("/getAllByPatient/{patientId}")
	public ResponseEntity<List<AppointmentDetails>> getAllAppointmentsByPatientId(@PathVariable Long patientId) throws HmsException{
		return ResponseEntity.ok(appointmentService.getAllAppointmentsByPatientId(patientId));
	}
	
	@GetMapping("/getAllByDoctor/{doctorId}")
	public ResponseEntity<List<AppointmentDetails>> getAllAppointmentsByDoctorId(@PathVariable Long doctorId) throws HmsException{
		return ResponseEntity.ok(appointmentService.getAllAppointmentsByDoctorId(doctorId));
	}
	
	@PutMapping("/update")
	public ResponseEntity<AppointmentDTO> update(@RequestBody AppointmentDTO details){
		details.setStatus(Status.SCHEDULED);
		return ResponseEntity.ok(appointmentService.update(details));
	}
	
	@GetMapping("/countByPatient/{patientId}")
	public ResponseEntity<List<MonthlyVisitDTO>> countByPatient(@PathVariable Long patientId ) throws HmsException{
		return ResponseEntity.ok(appointmentService.getAppointmentCountByPatient(patientId));
	}
	
	@GetMapping("/countByDoctor/{doctorId}")
	public ResponseEntity<List<MonthlyVisitDTO>> countByDoctor(@PathVariable Long doctorId ) throws HmsException{
		return ResponseEntity.ok(appointmentService.getAppointmentCountByDoctor(doctorId));
	}
	
	@GetMapping("/visitCount")
	public ResponseEntity<List<MonthlyVisitDTO>> getVisitCount() throws HmsException{
		return ResponseEntity.ok(appointmentService.getAppointmentCount());
	}
	
	@GetMapping("/reasonCountByPatient/{patientId}")
	public ResponseEntity<List<ReasonCountDTO>> reasonCountByPatient(@PathVariable Long patientId ) throws HmsException{
		return ResponseEntity.ok(appointmentService.getReasonCountByPatient(patientId));
	}
	
	@GetMapping("/reasonCountByDoctor/{doctorId}")
	public ResponseEntity<List<ReasonCountDTO>> reasonCountByDoctor(@PathVariable Long doctorId ) throws HmsException{
		return ResponseEntity.ok(appointmentService.getReasonCountByDoctor(doctorId));
	}
	
	@GetMapping("/reasonCount")
	public ResponseEntity<List<ReasonCountDTO>> reasonCount() throws HmsException{
		return ResponseEntity.ok(appointmentService.getReasonCount());
	}
	
	@GetMapping("/getMedicinesByPatientId/{patientId}")
	public ResponseEntity<List<MedicineDTO>> getMedicinesByPatientId(@PathVariable Long patientId) throws HmsException{
		return ResponseEntity.ok(prescriptionService.getMedicinesByPatientId(patientId));
	}
	
	@GetMapping("/getPatientsByDoctorId/{doctorId}")
	public ResponseEntity<List<PatientDTO>> getPatientsByDoctorId(@PathVariable Long doctorId) throws HmsException{
		return ResponseEntity.ok(appointmentService.getPatientsByDoctorId(doctorId));
	}
	
	@GetMapping("/getTodaysAppointments")
	public ResponseEntity<List<AppointmentDetails>> getTodaysAppointments() throws HmsException{
		return ResponseEntity.ok(appointmentService.getTodaysAppointments());
	}
	
	@GetMapping("/getTodaysAppointmentsOfDoctor/{doctorId}")
	public ResponseEntity<List<AppointmentDetails>> getTodaysAppointmentsOfDoctor(@PathVariable Long doctorId) throws HmsException{
		return ResponseEntity.ok(appointmentService.getTodaysAppointmentsOfDoctor(doctorId));
	}
	
	@GetMapping("/getTodaysAppointmentsOfPatient/{patientId}")
	public ResponseEntity<List<AppointmentDetails>> getTodaysAppointmentsOfPatient(@PathVariable Long patientId) throws HmsException{
		return ResponseEntity.ok(appointmentService.getTodaysAppointmentsOfPatient(patientId));
	}
	
	@GetMapping("/getPatientCountsByDoctorId/{doctorId}")
	public ResponseEntity<List<PatientCountDTO>> getPatientCountsByDoctorId(@PathVariable Long doctorId) throws HmsException{
		return ResponseEntity.ok(appointmentService.getPatientCountsByDoctorId(doctorId));
	}
	
	
	
}

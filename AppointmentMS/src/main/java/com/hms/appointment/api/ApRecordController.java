package com.hms.appointment.api;

import java.util.List;

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

import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.dto.MedicineDTO;
import com.hms.appointment.dto.PrescriptionDetails;
import com.hms.appointment.dto.RecordDetails;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.service.ApRecordService;
import com.hms.appointment.service.MedicineService;
import com.hms.appointment.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointment/report")
@Validated
@RequiredArgsConstructor
public class ApRecordController {
	private final ApRecordService apRecordService;
	private final PrescriptionService prescriptionService;
	private final MedicineService medicineService;
	
	@PostMapping("/create")
	public ResponseEntity<Long> createAppointmentReport(@RequestBody ApRecordDTO apRecordDTO) throws HmsException {
		return new ResponseEntity<>(apRecordService.createdApRecord(apRecordDTO),HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateAppointmentReport(@RequestBody ApRecordDTO apRecordDTO) throws HmsException{
		apRecordService.updateApRecord(apRecordDTO);
		return ResponseEntity.ok("Appointment Report Updated");
	}
	
	@GetMapping("/getRecordByAppointmentId/{appointmentId}")
	public ResponseEntity<ApRecordDTO> getRecordByAppointmentId(@PathVariable Long appointmentId) throws HmsException{
		return ResponseEntity.ok(apRecordService.getApRecordByAppointmentId(appointmentId));
	}
	
	@GetMapping("/getById/{recordId}")
	public ResponseEntity<ApRecordDTO> getRecordById(@PathVariable Long recordId) throws HmsException{
		return ResponseEntity.ok(apRecordService.getApRecordById(recordId));
	}
	
	@GetMapping("/getRecordDetailsByAppointmentId/{appointmentId}")
	public ResponseEntity<ApRecordDTO> getRecordDetailsByAppointmentId(@PathVariable Long appointmentId) throws HmsException{
		return ResponseEntity.ok(apRecordService.getApRecordDetailsByAppointmentId(appointmentId));
	}
	
	@GetMapping("/getRecordsByPatientId/{patientId}")
	public ResponseEntity<List<RecordDetails>> getRecordsByPatientId(@PathVariable Long patientId) throws HmsException{
		return ResponseEntity.ok(apRecordService.getRecordDetailsByPatientId(patientId));
	}
	
	@GetMapping("/isRecordExists/{appointmentId}")
	public ResponseEntity<Boolean> isRecordExists(@PathVariable Long appointmentId) throws HmsException{
		return ResponseEntity.ok(apRecordService.isRecordExists(appointmentId));
	}
	
	@GetMapping("/getPrescriptionsByPatientId/{patientId}")
	public ResponseEntity<List<PrescriptionDetails>> getPrescriptionsByPatientId(@PathVariable Long patientId) throws HmsException{
		return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatientId(patientId));
	}
	
	@GetMapping("/getPrescriptions")
	public ResponseEntity<List<PrescriptionDetails>> getAllPrescriptions() throws HmsException{
		return ResponseEntity.ok(prescriptionService.getPrescriptions());
	}
	
	@GetMapping("/getMedicinesByPrescriptionId/{prescriptionId}")
	public ResponseEntity<List<MedicineDTO>> getMedicinesByPrescriptionId(@PathVariable Long prescriptionId) throws HmsException{
		return ResponseEntity.ok(medicineService.getAllMedicinesByPrescriptionId(prescriptionId));
	}
	
	
	
}

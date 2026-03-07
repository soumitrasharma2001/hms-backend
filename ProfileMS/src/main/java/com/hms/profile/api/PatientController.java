package com.hms.profile.api;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hms.profile.dto.PatientDTO;
import com.hms.profile.dto.UserDropdown;
import com.hms.profile.exception.HmsException;
import com.hms.profile.service.PatientService;

@RestController
@RequestMapping("/profile/patient")
@Validated
public class PatientController {
	@Autowired
	private PatientService patientService;
	
	@PostMapping("/add")
	public ResponseEntity<Long> addPatient(@RequestBody PatientDTO patientDTO) throws HmsException{
		return new ResponseEntity<>(patientService.addPatient(patientDTO),HttpStatus.CREATED);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) throws HmsException {
		return new ResponseEntity<>(patientService.getPatientById(id),HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientDTO patientDTO) throws HmsException{
		return new ResponseEntity<>(patientService.update(patientDTO),HttpStatus.OK);
	}
	
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> patientExists(@PathVariable Long id) throws HmsException{
		return new ResponseEntity<>(patientService.patientExists(id),HttpStatus.OK);
	}
	
	@GetMapping("/getPatientsById")
	public ResponseEntity<List<UserDropdown>> getDropdowns(@RequestParam List<Long> ids) throws HmsException{
		return ResponseEntity.ok(patientService.getDropdowns(ids));
	}
	
	@GetMapping("/getAllPatients")
	public ResponseEntity<List<PatientDTO>> getAllPatients(){
		return ResponseEntity.ok(patientService.getAllPatients());
	}
	
	@PutMapping("/setProfilePicture/{profileId}/{profilePictureId}")
	public Boolean setProfilePicture(@PathVariable Long profileId,@PathVariable Long profilePictureId) throws HmsException{
		return patientService.setProfilePicture(profileId, profilePictureId);
	}
	
	@GetMapping("/getProfilePictureId/{profileId}")
	public Long getProfilePictureId(@PathVariable Long profileId) throws HmsException {
		return patientService.getProfilePictureId(profileId);
	}
}

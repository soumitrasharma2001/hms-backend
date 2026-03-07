package com.hms.profile.api;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropdown;
import com.hms.profile.exception.HmsException;
import com.hms.profile.service.DoctorService;

@RestController
@RequestMapping("/profile/doctor")
public class DoctorController {
	@Autowired
	private DoctorService doctorService;
	
	@PostMapping("/add")
	public ResponseEntity<Long> addDoctor(@RequestBody DoctorDTO doctorDTO) throws HmsException{
		return new ResponseEntity<>(doctorService.addDoctor(doctorDTO),HttpStatus.CREATED);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) throws HmsException{
		return new ResponseEntity<>(doctorService.getDoctorById(id),HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<DoctorDTO> update(@RequestBody DoctorDTO doctorDTO) throws HmsException{
		return new ResponseEntity<>(doctorService.update(doctorDTO),HttpStatus.OK);
	}
	
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> doctorExists(@PathVariable Long id) throws HmsException{
		return new ResponseEntity<>(doctorService.doctorExists(id),HttpStatus.OK);
	}
	
	@GetMapping("/dropdowns")
	public ResponseEntity<List<DoctorDropdown>> getDoctorDropdowns() throws HmsException{
		return ResponseEntity.ok(doctorService.getDoctorDropdonws());
	}
	
	@GetMapping("/getDoctorsById")
	public ResponseEntity<List<DoctorDropdown>> getDoctorsById(@RequestParam List<Long> ids) throws HmsException{
		return ResponseEntity.ok(doctorService.getDoctorsById(ids));
	}
	
	@GetMapping("/getAllDoctors")
	public ResponseEntity<List<DoctorDTO>> getAllDoctors(){
		return ResponseEntity.ok(doctorService.getAllDoctors());
	}
	
	@PutMapping("/setProfilePicture/{profileId}/{profilePictureId}")
	public Boolean setProfilePicture(@PathVariable Long profileId, @PathVariable Long profilePictureId) throws HmsException{
		return doctorService.setProfilePicture(profileId, profilePictureId);
	}
	
	@GetMapping("/getProfilePictureId/{profileId}")
	public Long getProfilePictureId(@PathVariable Long profileId) throws HmsException {
		return doctorService.getProfilePictureId(profileId);
	}
}

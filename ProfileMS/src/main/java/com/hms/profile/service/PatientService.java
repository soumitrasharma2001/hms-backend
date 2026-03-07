package com.hms.profile.service;


import java.util.List;



import com.hms.profile.dto.PatientDTO;
import com.hms.profile.dto.UserDropdown;
import com.hms.profile.exception.HmsException;

public interface PatientService {
	public Long addPatient(PatientDTO patientDTO) throws HmsException;
	public PatientDTO getPatientById(Long id) throws HmsException;
	public PatientDTO update(PatientDTO patientDTO) throws HmsException;
	public Boolean patientExists(Long id) throws HmsException;
	public List<UserDropdown> getDropdowns(List<Long> ids);
	public List<PatientDTO> getAllPatients();
	public Boolean setProfilePicture(Long profileId, Long profilePictureId) throws HmsException;
	public Long getProfilePictureId(Long profileId) throws HmsException;
	
}

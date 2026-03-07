package com.hms.profile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hms.profile.dto.PatientDTO;
import com.hms.profile.dto.UserDropdown;
import com.hms.profile.entity.Patient;
import com.hms.profile.exception.HmsException;
import com.hms.profile.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService{
	
	@Autowired
	private PatientRepository patientRepo;
	
	@Override
	public Long addPatient(PatientDTO patientDTO) throws HmsException {
		try {
			return patientRepo.save(patientDTO.toEntity()).getId();
		}catch(DataIntegrityViolationException e) {
			throw new HmsException("PATIENT_ALREADY_EXISTS");
		}
	}

	@Override
	public PatientDTO getPatientById(Long id) throws HmsException {
		return patientRepo.findById(id).orElseThrow(()->new HmsException("PATIENT_NOT_FOUND")).toDTO();
	}

	@Override
	public PatientDTO update(PatientDTO patientDTO) throws HmsException{
		patientRepo.findById(patientDTO.getId()).orElseThrow(()->new HmsException("PATIENT_NOT_FOUND"));
		return patientRepo.save(patientDTO.toEntity()).toDTO();
	}

	@Override
	public Boolean patientExists(Long id) throws HmsException {
		return patientRepo.existsById(id);
	}

	@Override
	public List<UserDropdown> getDropdowns(List<Long> ids) {
		return patientRepo.findPatientDropdownsByIds(ids);
	}

	@Override
	public List<PatientDTO> getAllPatients() {
		return ((List<Patient>) patientRepo.findAll()).stream()
				.map(Patient::toDTO).toList();
	}

	@Override
	public Boolean setProfilePicture(Long profileId, Long profilePictureId) throws HmsException {
		Patient patient=patientRepo.findById(profileId)
				.orElseThrow(()->new HmsException("PATIENT_NOT_FOUND"));
		patient.setProfilePictureId(profilePictureId);
		patient=patientRepo.save(patient);
		return patient!=null;
	}

	@Override
	public Long getProfilePictureId(Long profileId) throws HmsException{
		return patientRepo.findById(profileId)
				.orElseThrow(()->new HmsException("PROFILE_NOT_FOUND")).getProfilePictureId();
	}

}

package com.hms.profile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropdown;
import com.hms.profile.entity.Doctor;
import com.hms.profile.exception.HmsException;
import com.hms.profile.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService{
	
	@Autowired
	private DoctorRepository doctorRepo;
	
	@Override
	public Long addDoctor(DoctorDTO doctorDTO) throws HmsException {
		try {
			return doctorRepo.save(doctorDTO.toEntity()).getId();
		}catch(DataIntegrityViolationException e) {
			throw new HmsException("DOCTOR_ALREADY_EXISTS");
		}
	}

	@Override
	public DoctorDTO getDoctorById(Long id) throws HmsException {
		return doctorRepo.findById(id).orElseThrow(()->new HmsException("DOCTOR_NOT_FOUND")).toDTO();
	}

	@Override
	public DoctorDTO update(DoctorDTO doctorDTO) throws HmsException {
		doctorRepo.findById(doctorDTO.getId()).orElseThrow(()->new HmsException("DOCTOR_NOT_FOUND"));
		return doctorRepo.save(doctorDTO.toEntity()).toDTO();
	}

	@Override
	public Boolean doctorExists(Long id) throws HmsException {
		return doctorRepo.existsById(id);
	}

	@Override
	public List<DoctorDropdown> getDoctorDropdonws() throws HmsException {
		return doctorRepo.findAllDoctorDropdowns();
	}

	@Override
	public List<DoctorDropdown> getDoctorsById(List<Long> ids) {
		return doctorRepo.findAllDoctorDropdownsByIds(ids);
	}

	@Override
	public List<DoctorDTO> getAllDoctors() {
		return ((List<Doctor>) doctorRepo.findAll()).stream().map(Doctor::toDTO).toList();
	}

	@Override
	public Boolean setProfilePicture(Long profileId,Long profilePictureId) throws HmsException{
		Doctor doctor=doctorRepo.findById(profileId)
				.orElseThrow(()->new HmsException("DOCTOR_NOT_FOUND"));
		doctor.setProfilePictureId(profilePictureId);
		doctor=doctorRepo.save(doctor);
		return doctor!=null;
	}

	@Override
	public Long getProfilePictureId(Long profileId) throws HmsException {
		return doctorRepo.findById(profileId)
				.orElseThrow(()->new HmsException("PROFILE_NOT_FOUND")).getProfilePictureId();
	}

}

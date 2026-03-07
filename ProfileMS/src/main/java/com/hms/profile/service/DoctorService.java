package com.hms.profile.service;

import java.util.List;



import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropdown;
import com.hms.profile.exception.HmsException;


public interface DoctorService {
	public Long addDoctor(DoctorDTO doctorDTO) throws HmsException;
	public DoctorDTO getDoctorById(Long id) throws HmsException;
	public DoctorDTO update(DoctorDTO doctorDTO) throws HmsException;
	public Boolean doctorExists(Long id) throws HmsException;
	public List<DoctorDropdown> getDoctorDropdonws() throws HmsException;
	public List<DoctorDropdown> getDoctorsById(List<Long> ids);
	public List<DoctorDTO> getAllDoctors();
	public Boolean setProfilePicture(Long profileId, Long profilePictureId) throws HmsException;
	public Long getProfilePictureId(Long profileId) throws HmsException;
}

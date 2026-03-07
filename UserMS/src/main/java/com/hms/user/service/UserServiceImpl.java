package com.hms.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hms.user.clients.ProfileClient;
import com.hms.user.dto.MonthlyRoleCountDTO;
import com.hms.user.dto.RegistrationCountDTO;
import com.hms.user.dto.Roles;
import com.hms.user.dto.UserDTO;
import com.hms.user.entity.User;
import com.hms.user.exception.HmsException;
import com.hms.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Autowired
	private ProfileClient profileClient;
	
	@Override
	public void registerUser(UserDTO userDTO) throws HmsException {
		if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
			throw new HmsException("USER_ALREADY_EXISTS");
		}
		userDTO.setPassword(pwdEncoder.encode(userDTO.getPassword()));
		if(userDTO.getRole().equals(Roles.DOCTOR))
			userDTO.setProfileId(profileClient.addDoctorProfile(userDTO));
		else if(userDTO.getRole().equals(Roles.PATIENT))
			userDTO.setProfileId(profileClient.addPatientProfile(userDTO));
		userRepository.save(userDTO.toEntity());
	}
	@Override
	public UserDTO loginUser(UserDTO userDTO) throws HmsException {
		User user= userRepository.findByEmail(userDTO.getEmail())
				.orElseThrow(()->new HmsException("USER_NOT_FOUND"));
		if(!pwdEncoder.matches(userDTO.getPassword(), user.getPassword())) {
			throw new HmsException("INVALID_CREDENTIALS");
		}
		user.setPassword(null);
		return user.toDTO();
		
	}

	@Override
	public UserDTO getUserById(Long id) throws HmsException {
		return userRepository.findById(id)
		.orElseThrow(()->new HmsException("USER_NOT_FOUND")).toDTO();
	}

	@Override
	public void updateUser(UserDTO userDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserDTO getUser(String email) throws HmsException {
		return userRepository.findByEmail(email)
				.orElseThrow(()->new HmsException("USER_NOT_FOUND")).toDTO();
	}
	@Override
	public Long getProfilePictureId() throws HmsException {
		ServletRequestAttributes attrs=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attrs.getRequest();
		Long profileId=Long.parseLong(request.getHeader("X-Profile-Id"));
		String role=request.getHeader("X-Role");
		Long profilePictureId=null;
		if(role.equals("DOCTOR"))
			profilePictureId=profileClient.getDoctorProfilePictureId(profileId);
		else if(role.equals("PATIENT"))
			profilePictureId=profileClient.getPatientProfilePictureId(profileId);
		return profilePictureId;
	}
	@Override
	public RegistrationCountDTO getMonthlyRegistrationCounts() {
		List<MonthlyRoleCountDTO> doctorCounts=userRepository
				.countRegistrationsByRoleGroupedByMonth(Roles.DOCTOR);
		List<MonthlyRoleCountDTO> patientCounts=userRepository
				.countRegistrationsByRoleGroupedByMonth(Roles.PATIENT);
		return new RegistrationCountDTO(doctorCounts,patientCounts);
	}

}

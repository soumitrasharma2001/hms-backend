package com.hms.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.AppointmentDTO;
import com.hms.appointment.dto.AppointmentDetails;
import com.hms.appointment.dto.DoctorDTO;
import com.hms.appointment.dto.MonthlyVisitDTO;
import com.hms.appointment.dto.PatientCountDTO;
import com.hms.appointment.dto.PatientDTO;
import com.hms.appointment.dto.ReasonCountDTO;
import com.hms.appointment.dto.Status;
import com.hms.appointment.entity.Appointment;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService{

	@Autowired
	private AppointmentRepository appointmentRepo;
	
	@Autowired
	private ProfileClient profileClient;
	
	@Override
	public AppointmentDTO scheduleAppointment(AppointmentDTO appointmentDTO) throws HmsException {
		try {
			Boolean doctorExists=profileClient.doctorExists(appointmentDTO.getDoctorId());
			if(doctorExists==null||!doctorExists)
				throw new HmsException("DOCTOR_NOT_FOUND");
			Boolean patientExists=profileClient.patientExists(appointmentDTO.getPatientId());
			if(patientExists==null||!patientExists)
				throw new HmsException("PATIENT_NOT_FOUND");
			appointmentDTO.setStatus(Status.SCHEDULED);
			return appointmentRepo.save(appointmentDTO.toEntity()).toDTO();
		}catch(DataIntegrityViolationException e) {
			throw new HmsException("APPOINTMENT_ALREADY_EXISTS");
		}
	}

	@Override
	public void cancelAppointment(Long appointmentId) throws HmsException {
		Appointment appointment=appointmentRepo.findById(appointmentId)
				.orElseThrow(()->new HmsException("APPOINTMENT_NOT_FOUND"));
		if(appointment.getStatus().equals(Status.CANCELLED))
			throw new HmsException("APPOINTMENT_ALREADY_CANCELLED");
		appointment.setStatus(Status.CANCELLED);
		appointmentRepo.save(appointment);
	}

	@Override
	public void completeAppointment(Long appointmentId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rescheduleAppointment(Long appointmentId, String newDateTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AppointmentDTO getAppointmentDetails(Long appointmentId) throws HmsException {
		return appointmentRepo.findById(appointmentId)
				.orElseThrow(()->new HmsException("APPOINTMENT_NOT_FOUND")).toDTO();
		
	}

	@Override
	public AppointmentDetails getAppointmentDetailsWithName(Long appointmentId) throws HmsException {
		AppointmentDTO appointmentDTO=appointmentRepo.findById(appointmentId)
		.orElseThrow(()->new HmsException("APPOINTMENT_NOT_FOUND")).toDTO();
		DoctorDTO doctorDTO=profileClient.getDoctorById(appointmentDTO.getDoctorId());
		PatientDTO patientDTO=profileClient.getPatientById(appointmentDTO.getPatientId());
		return new AppointmentDetails(
				appointmentDTO.getId(),
				appointmentDTO.getPatientId(),
				patientDTO.getName(),
				patientDTO.getEmail(),
				patientDTO.getPhoneNo(),
				appointmentDTO.getDoctorId(),
				doctorDTO.getName(),
				doctorDTO.getEmail(),
				doctorDTO.getPhoneNo(),
				appointmentDTO.getAppointmentTime(),
				appointmentDTO.getStatus(),
				appointmentDTO.getReason(),
				appointmentDTO.getNotes()
		);
	}

	@Override
	public List<AppointmentDetails> getAllAppointmentsByPatientId(Long appointmentId) throws HmsException {
		return appointmentRepo.findAllByPatientId(appointmentId).stream()
				.map(appointment->{
					appointment
					.setDoctorName(profileClient.getDoctorById(appointment.getDoctorId()).getName());
					return appointment;
				}).toList();
	}

	@Override
	public AppointmentDTO update(AppointmentDTO details) {
		return appointmentRepo.save(details.toEntity()).toDTO();
	}

	@Override
	public List<AppointmentDetails> getAllAppointmentsByDoctorId(Long doctorId) throws HmsException {
		return appointmentRepo.findAllByDoctorId(doctorId).stream().
				map(appointment->{
					appointment
					.setPatientName(profileClient.getPatientById(appointment.getPatientId()).getName());
					return appointment;
				}).toList();
	}

	@Override
	public List<MonthlyVisitDTO> getAppointmentCountByPatient(Long patientId) throws HmsException {
		return appointmentRepo.countCurrentYearVisitsByPatient(patientId);
	}

	@Override
	public List<ReasonCountDTO> getReasonCountByPatient(Long patientId) throws HmsException {
		return appointmentRepo.countReasonsByPatientId(patientId);
	}

	@Override
	public List<MonthlyVisitDTO> getAppointmentCountByDoctor(Long doctorId) throws HmsException {
		return appointmentRepo.countCurrentYearVisitsByDoctor(doctorId);
	}

	@Override
	public List<MonthlyVisitDTO> getAppointmentCount() throws HmsException {
		return appointmentRepo.countCurrentYearVisits();
	}

	@Override
	public List<ReasonCountDTO> getReasonCountByDoctor(Long doctorId) {
		return appointmentRepo.countReasonsByDoctorId(doctorId);
	}

	@Override
	public List<PatientDTO> getPatientsByDoctorId(Long doctorId) {
		return null;
	}

	@Override
	public List<ReasonCountDTO> getReasonCount() {
		return appointmentRepo.countReasons();
	}

	@Override
	public List<AppointmentDetails> getTodaysAppointments() {
		LocalDate today=LocalDate.now();
		LocalDateTime startOfDay=today.atStartOfDay();
		LocalDateTime endOfDay=today.atTime(LocalTime.MAX);
		return appointmentRepo.findByAppointmentTimeBetween(startOfDay, endOfDay).stream()
				.map(appointment->{
					DoctorDTO doctorDTO=profileClient.getDoctorById(appointment.getDoctorId());
					PatientDTO patientDTO=profileClient.getPatientById(appointment.getPatientId());
					return new AppointmentDetails(appointment.getId(),appointment.getPatientId(),
							patientDTO.getName(),patientDTO.getEmail(),patientDTO.getPhoneNo(),
							appointment.getDoctorId(),doctorDTO.getName(),doctorDTO.getEmail(),doctorDTO.getPhoneNo(),appointment.getAppointmentTime(),
							appointment.getStatus(),appointment.getReason(),appointment.getNotes());
				}).toList();
	}

	@Override
	public List<AppointmentDetails> getTodaysAppointmentsOfDoctor(Long doctorId) {
		LocalDate today=LocalDate.now();
		LocalDateTime startOfDay=today.atStartOfDay();
		LocalDateTime endOfDay=today.atTime(LocalTime.MAX);
		return appointmentRepo.findByAppointmentTimeBetweenAndDoctorId(startOfDay, endOfDay,doctorId).stream()
				.map(appointment->{
					DoctorDTO doctorDTO=profileClient.getDoctorById(appointment.getDoctorId());
					PatientDTO patientDTO=profileClient.getPatientById(appointment.getPatientId());
					return new AppointmentDetails(appointment.getId(),appointment.getPatientId(),
							patientDTO.getName(),patientDTO.getEmail(),patientDTO.getPhoneNo(),
							appointment.getDoctorId(),doctorDTO.getName(),doctorDTO.getEmail(),doctorDTO.getPhoneNo(),appointment.getAppointmentTime(),
							appointment.getStatus(),appointment.getReason(),appointment.getNotes());
				}).toList();
	}

	@Override
	public List<AppointmentDetails> getTodaysAppointmentsOfPatient(Long patientId) {
		LocalDate today=LocalDate.now();
		LocalDateTime startOfDay=today.atStartOfDay();
		LocalDateTime endOfDay=today.atTime(LocalTime.MAX);
		return appointmentRepo.findByAppointmentTimeBetweenAndPatientId(startOfDay, endOfDay,patientId).stream()
				.map(appointment->{
					DoctorDTO doctorDTO=profileClient.getDoctorById(appointment.getDoctorId());
					PatientDTO patientDTO=profileClient.getPatientById(appointment.getPatientId());
					return new AppointmentDetails(appointment.getId(),appointment.getPatientId(),
							patientDTO.getName(),patientDTO.getEmail(),patientDTO.getPhoneNo(),
							appointment.getDoctorId(),doctorDTO.getName(),doctorDTO.getEmail(),doctorDTO.getPhoneNo(),appointment.getAppointmentTime(),
							appointment.getStatus(),appointment.getReason(),appointment.getNotes());
				}).toList();
	}

	@Override
	public List<PatientCountDTO> getPatientCountsByDoctorId(Long doctorId) {
		return appointmentRepo.countCurrentYearPatientsByDoctor(doctorId);
	}

}

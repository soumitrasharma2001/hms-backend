package com.hms.appointment.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.hms.appointment.dto.AppointmentDTO;
import com.hms.appointment.dto.AppointmentDetails;
import com.hms.appointment.dto.MonthlyVisitDTO;
import com.hms.appointment.dto.PatientCountDTO;
import com.hms.appointment.dto.PatientDTO;
import com.hms.appointment.dto.ReasonCountDTO;
import com.hms.appointment.exception.HmsException;

@Service
public interface AppointmentService {
	AppointmentDTO scheduleAppointment(AppointmentDTO appointmentDTO) throws HmsException;
	void cancelAppointment(Long appointmentId) throws HmsException;
	void completeAppointment(Long appointmentId);
	void rescheduleAppointment(Long appointmentId,String newDateTime);
	AppointmentDTO getAppointmentDetails(Long appointmentId) throws HmsException;
	AppointmentDetails getAppointmentDetailsWithName(Long appointmentId) throws HmsException;
	List<AppointmentDetails> getAllAppointmentsByPatientId(Long appointmentId) throws HmsException;
	AppointmentDTO update(AppointmentDTO details);
	List<AppointmentDetails> getAllAppointmentsByDoctorId(Long doctorId) throws HmsException;
	List<MonthlyVisitDTO> getAppointmentCountByPatient(Long patientId) throws HmsException;
	List<MonthlyVisitDTO> getAppointmentCountByDoctor(Long doctorId) throws HmsException;
	List<MonthlyVisitDTO> getAppointmentCount() throws HmsException;
	List<ReasonCountDTO> getReasonCountByPatient(Long patientId) throws HmsException;
	List<ReasonCountDTO> getReasonCountByDoctor(Long doctorId);
	List<PatientDTO> getPatientsByDoctorId(Long doctorId);
	List<ReasonCountDTO> getReasonCount();
	List<AppointmentDetails> getTodaysAppointments();
	List<AppointmentDetails> getTodaysAppointmentsOfDoctor(Long doctorId);
	List<AppointmentDetails> getTodaysAppointmentsOfPatient(Long patientId);
	List<PatientCountDTO> getPatientCountsByDoctorId(Long doctorId);
}

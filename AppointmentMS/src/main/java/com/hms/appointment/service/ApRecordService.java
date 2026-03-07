package com.hms.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.dto.RecordDetails;
import com.hms.appointment.exception.HmsException;

@Service
public interface ApRecordService {
	public Long createdApRecord(ApRecordDTO request) throws HmsException;
	public void updateApRecord(ApRecordDTO request) throws HmsException;
	public ApRecordDTO getApRecordByAppointmentId(Long appointmentId) throws HmsException;
	public ApRecordDTO getApRecordById(Long recordId) throws HmsException;
	public ApRecordDTO getApRecordDetailsByAppointmentId(Long appointmentId) throws HmsException;
	public List<RecordDetails> getRecordDetailsByPatientId(Long patientId) throws HmsException;
	public Boolean isRecordExists(Long appointmentId) throws HmsException;
}

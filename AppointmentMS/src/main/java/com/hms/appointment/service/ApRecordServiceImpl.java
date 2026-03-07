package com.hms.appointment.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.ApRecordDTO;
import com.hms.appointment.dto.DoctorName;
import com.hms.appointment.dto.RecordDetails;
import com.hms.appointment.entity.ApRecord;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.ApRecordRepository;
import com.hms.appointment.utility.StringListConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApRecordServiceImpl implements ApRecordService{

	private final ApRecordRepository apRecordRepo;
	private final PrescriptionService prescriptionService;
	private final ProfileClient profileClient;
	
	@Override
	public Long createdApRecord(ApRecordDTO request) throws HmsException {
		Optional<ApRecord> existingRecord=apRecordRepo.findByAppointment_Id(request.getAppointmentId());
		if(existingRecord.isPresent()) 
			throw new HmsException("APPOINTMENT_RECORD_ALREADY_EXISTS");
		request.setCreatedAt(LocalDateTime.now());
		Long id= apRecordRepo.save(request.toEntity()).getId();
		if(request.getPrescription()!=null) {
			request.getPrescription().setAppointmentId(request.getAppointmentId());
			prescriptionService.savePrescription(request.getPrescription());
		}
		return id;
	}

	@Override
	public void updateApRecord(ApRecordDTO request) throws HmsException {
		ApRecord existingRecord=apRecordRepo.findById(request.getId()).orElseThrow(
				()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND"));
		
		existingRecord.setNotes(request.getNotes());
		existingRecord.setDiagnosis(request.getDiagnosis());
		existingRecord.setTests(StringListConverter.convertListToString(request.getTests()));
		existingRecord.setSymptoms(StringListConverter.convertListToString(request.getSymptoms()));
		existingRecord.setReferral(request.getReferral());;
		existingRecord.setFollowUpDate(request.getFollowUpDate());
		
		apRecordRepo.save(existingRecord);
	}

	@Override
	public ApRecordDTO getApRecordByAppointmentId(Long appointmentId) throws HmsException {
		return apRecordRepo.findByAppointment_Id(appointmentId)
				.orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND")).toDTO();
	}

	@Override
	public ApRecordDTO getApRecordById(Long recordId) throws HmsException {
		return apRecordRepo.findById(recordId)
				.orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND")).toDTO();
	}

	@Override
	public ApRecordDTO getApRecordDetailsByAppointmentId(Long appointmentId) throws HmsException {
		ApRecordDTO apRecord= apRecordRepo.findByAppointment_Id(appointmentId)
				.orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND")).toDTO();
		apRecord.setPrescription(prescriptionService.getPrescriptionByAppointmentId(appointmentId));
		return apRecord;
		
	}

	@Override
	public List<RecordDetails> getRecordDetailsByPatientId(Long patientId) throws HmsException {
		List<ApRecord> records = apRecordRepo.findByPatientId(patientId);
		List<RecordDetails> recordDetails = records.stream()
				.map(ApRecord::toRecordDetails).toList();
		List<Long> doctorIds = recordDetails.stream()
				.map(RecordDetails::getDoctorId).distinct().toList();
		List<DoctorName> doctorNames = profileClient.getDoctorsById(doctorIds);
		Map<Long, String> doctorMap = doctorNames.stream()
				.collect(Collectors.toMap(DoctorName::getId, DoctorName::getName));
		recordDetails.forEach(rd->{
			String doctorName=doctorMap.get(rd.getDoctorId());
			if(doctorName!=null)
				rd.setDoctorName(doctorName);
			else
				rd.setDoctorName("Unknown Doctor");
		});
		return recordDetails;
	
	}

	@Override
	public Boolean isRecordExists(Long appointmentId) throws HmsException {
		return apRecordRepo.existsByAppointment_Id(appointmentId);
 	}

}

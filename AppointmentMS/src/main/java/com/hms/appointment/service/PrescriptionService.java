package com.hms.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.appointment.dto.MedicineDTO;
import com.hms.appointment.dto.PrescriptionDTO;
import com.hms.appointment.dto.PrescriptionDetails;
import com.hms.appointment.exception.HmsException;

@Service
public interface PrescriptionService {
	public Long savePrescription(PrescriptionDTO request);
	public PrescriptionDTO getPrescriptionByAppointmentId(Long appointmentId) throws HmsException;
	public PrescriptionDTO getPrescriptionById(Long prescriptionId) throws HmsException;
	public List<PrescriptionDetails> getPrescriptionsByPatientId(Long patientId) throws HmsException;
	public List<PrescriptionDetails> getPrescriptions() throws HmsException;
	public List<MedicineDTO> getMedicinesByPatientId(Long patientId) throws HmsException;
}

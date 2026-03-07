package com.hms.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.appointment.dto.MedicineDTO;

@Service
public interface MedicineService {
	 public Long saveMedicine(MedicineDTO request);
	 public List<MedicineDTO> saveAllMedicines(List<MedicineDTO> requestList);
	 public List<MedicineDTO> getAllMedicinesByPrescriptionId(Long prescriptionId);
	 public List<MedicineDTO> getAllMedicinesByPrescriptionIds(List<Long> prescriptionIds);
}

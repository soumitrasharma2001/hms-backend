package com.hms.pharmacy.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.exception.HmsException;

@Service
public interface MedicineInventoryService {
	List<MedicineInventoryDTO> getAllMedicines();
	MedicineInventoryDTO getMedicineById(Long id) throws HmsException;
	MedicineInventoryDTO addMedicine(MedicineInventoryDTO medicine) throws HmsException;
	MedicineInventoryDTO updateMedicine(MedicineInventoryDTO medicine) throws HmsException;
	void deleteMedicine(Long id) throws HmsException;
	void deleteExpiredMedicines() throws HmsException;
	String sellStock(Long medicineId,Integer quantity) throws HmsException;
	
}

package com.hms.pharmacy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.MedicineDTO;
import com.hms.pharmacy.exception.HmsException;


@Service
public interface MedicineService {
	public Long addMedicine(MedicineDTO medicineDTO) throws HmsException;
	public MedicineDTO getMedicineById(Long id) throws HmsException;
	public void updateMedicine(MedicineDTO medicineDTO) throws HmsException;
	public List<MedicineDTO> getAllMedicines() throws HmsException;
	public Integer getStockCountById(Long id) throws HmsException;
	public Integer addStock(Long id,Integer quantity) throws HmsException;
	public Integer removeStock(Long id,Integer quantity) throws HmsException;
}

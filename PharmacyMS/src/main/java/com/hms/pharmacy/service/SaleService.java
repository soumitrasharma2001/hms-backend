package com.hms.pharmacy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.dto.SaleRequest;
import com.hms.pharmacy.exception.HmsException;

@Service
public interface SaleService {
	Long createSale(SaleRequest saleDTO) throws HmsException;
	void updateSale(SaleDTO saleDTO) throws HmsException;
	SaleDTO getSale(Long id) throws HmsException;
	SaleDTO getSaleByPrescriptionId(Long prescriptionId) throws HmsException;
	List<SaleDTO> getSaleItems() throws HmsException;
}

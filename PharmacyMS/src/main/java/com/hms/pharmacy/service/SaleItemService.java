package com.hms.pharmacy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.exception.HmsException;

@Service
public interface SaleItemService {
	Long createSaleItem(SaleItemDTO saleItemDTO) throws HmsException;
	void createMultipleSaleItem(Long saleId,Long medicineId,List<SaleItemDTO> saleItemDTOs) throws HmsException;
	void updateSaleItem(SaleItemDTO saleItemDTO) throws HmsException;
	List<SaleItemDTO> getSaleItemsBySaleId(Long saleId) throws HmsException;
	SaleItemDTO getSaleItem(Long id) throws HmsException;
	void createSaleItems(Long saleId,List<SaleItemDTO> saleItemDTOs) throws HmsException;
}

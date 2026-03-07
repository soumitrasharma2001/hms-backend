package com.hms.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.dto.SaleRequest;
import com.hms.pharmacy.entity.Sale;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.SaleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService{
	private final SaleRepository saleRepo;
	private final SaleItemService siService;
	private final MedicineInventoryService miService;
	
	@Override
	@Transactional
	public Long createSale(SaleRequest saleDTO) throws HmsException {
		if(saleDTO.getPrescriptionId()!=null&& saleRepo.existsByPrescriptionId(saleDTO.getPrescriptionId()))
			throw new HmsException("SALE_ALREADY_EXISTS");
		for(SaleItemDTO saleItem:saleDTO.getSaleItems()) {
			saleItem.setBatchNo(
				miService.sellStock(saleItem.getMedicineId(),saleItem.getQuantity())
			);
		}
		Sale sale=new Sale(null,saleDTO.getPrescriptionId(),saleDTO.getBuyerName(),saleDTO.getBuyerContact(),LocalDateTime.now(),saleDTO.getTotalAmount());
		sale=saleRepo.save(sale);
		siService.createSaleItems(sale.getId(), saleDTO.getSaleItems());
		return sale.getId();
	}

	@Override
	public void updateSale(SaleDTO saleDTO) throws HmsException {
		Sale existingSale=saleRepo.findById(saleDTO.getId()).orElseThrow(()->new HmsException("SALE_NOT_FOUND"));
		existingSale.setSaleDate(saleDTO.getSaleDate());
		existingSale.setTotalAmount(saleDTO.getTotalAmount());
		saleRepo.save(existingSale);
	}

	@Override
	public SaleDTO getSale(Long id) throws HmsException {
		return saleRepo.findById(id).orElseThrow(()->new HmsException("SALE_NOT_FOUND")).toDTO();
	}

	@Override
	public SaleDTO getSaleByPrescriptionId(Long prescriptionId) throws HmsException {
		return saleRepo.findById(prescriptionId).orElseThrow(()->new HmsException("SALE_NOT_FOUND")).toDTO();
	}

	@Override
	public List<SaleDTO> getSaleItems() throws HmsException {
		return ((List<Sale>)saleRepo.findAll()).stream().map(Sale::toDTO).toList();
	}
}

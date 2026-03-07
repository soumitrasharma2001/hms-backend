package com.hms.pharmacy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.entity.SaleItem;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.SaleItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleItemServiceImpl implements SaleItemService{
	private final SaleItemRepository saleItemRepo;
	

	@Override
	public Long createSaleItem(SaleItemDTO saleItemDTO) throws HmsException {
		return saleItemRepo.save(saleItemDTO.toEntity()).getId();
	}

	@Override
	public void createMultipleSaleItem(Long saleId, Long medicineId, List<SaleItemDTO> saleItemDTOs)
			throws HmsException {
		saleItemDTOs.stream().map(saleItem->{
			saleItem.setMedicineId(medicineId);
			saleItem.setSaleId(saleId);
			return saleItem.toEntity();
		}).forEach(saleItemRepo::save);
	}

	@Override
	public void updateSaleItem(SaleItemDTO saleItemDTO) throws HmsException {
		SaleItem existingItem=saleItemRepo.findById(saleItemDTO.getId())
				.orElseThrow(()->new HmsException("SALE_ITEM_NOT_FOUND"));
		existingItem.setQuantity(saleItemDTO.getQuantity());
		existingItem.setUnitPrice(saleItemDTO.getUnitPrice());
		saleItemRepo.save(existingItem);
	}

	@Override
	public List<SaleItemDTO> getSaleItemsBySaleId(Long saleId) throws HmsException {
		return saleItemRepo.findBySaleId(saleId).stream().map(SaleItem::toDTO).toList();
	}

	@Override
	public SaleItemDTO getSaleItem(Long id) throws HmsException {
		return saleItemRepo.findById(id).orElseThrow(()->new HmsException("SALE_ITEM_NOT_FOUND")).toDTO();
	}

	@Override
	public void createSaleItems(Long saleId, List<SaleItemDTO> saleItemDTOs) throws HmsException {
		saleItemDTOs.stream().map((saleItem)->{
			saleItem.setSaleId(saleId);
			return saleItem.toEntity();
		}).forEach(saleItemRepo::save);;
	}
}

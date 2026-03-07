package com.hms.pharmacy.service;

import java.time.LocalDate;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.entity.MedicineInventory;
import com.hms.pharmacy.entity.StockStatus;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.MedicineInventoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineInventoryServiceImpl implements MedicineInventoryService{
	private final MedicineInventoryRepository medicineInventoryRepo;
	private final MedicineService medicineService;
	
	@Override
	public List<MedicineInventoryDTO> getAllMedicines() {
		return ((List<MedicineInventory>) medicineInventoryRepo.findAll()).stream()
				.map(MedicineInventory::toDTO).toList();
	}

	@Override
	public MedicineInventoryDTO getMedicineById(Long id) throws HmsException {
		return medicineInventoryRepo.findById(id).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND")).toDTO();
	}

	@Override
	public MedicineInventoryDTO addMedicine(MedicineInventoryDTO medicine) throws HmsException {
		medicine.setAddedDate(LocalDate.now());
		medicineService.addStock(medicine.getMedicineId(), medicine.getQuantity());
		medicine.setStatus(StockStatus.ACTIVE);
		medicine.setInitialQuantity(medicine.getQuantity());
		return medicineInventoryRepo.save(medicine.toEntity()).toDTO();
	}

	@Override
	public MedicineInventoryDTO updateMedicine(MedicineInventoryDTO medicine) throws HmsException {
		MedicineInventory existingInventory=medicineInventoryRepo.findById(medicine.getId())
				.orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
		existingInventory.setExpiryDate(medicine.getExpiryDate());
		if(existingInventory.getQuantity()<medicine.getQuantity())
			medicineService.addStock(medicine.getMedicineId(), 
					medicine.getQuantity()-existingInventory.getQuantity());
		else if(existingInventory.getQuantity()>medicine.getQuantity())
			medicineService.removeStock(medicine.getMedicineId(), 
					existingInventory.getQuantity()-medicine.getQuantity());
		existingInventory.setQuantity(medicine.getQuantity());
		existingInventory.setInitialQuantity(medicine.getQuantity());
		existingInventory.setBatchNo(medicine.getBatchNo());
		return medicineInventoryRepo.save(existingInventory).toDTO();
	}

	@Override
	public void deleteMedicine(Long id) throws HmsException {
		medicineInventoryRepo.deleteById(id);
	}
	
	private void markExpired(List<MedicineInventory> inventories) {
		for(MedicineInventory inventory:inventories) {
			inventory.setStatus(StockStatus.EXPIRED);
		}
		medicineInventoryRepo.saveAll(inventories);
	}
	@Override
	@Scheduled(cron="0 25 16 * * ?")
	public void deleteExpiredMedicines() throws HmsException {
		List<MedicineInventory> expiredMedicines=medicineInventoryRepo
				.findByExpiryDateBefore(LocalDate.now());
		for(MedicineInventory medicine:expiredMedicines) {
			medicineService.removeStock(medicine.getMedicine().getId(), medicine.getQuantity());
		}
		this.markExpired(expiredMedicines);
		System.out.println("Expired medicines deleted..."+expiredMedicines);
	}

	@Override
	@Transactional
	public String sellStock(Long medicineId, Integer quantity) throws HmsException {

	    if (quantity <= 0) {
	        throw new HmsException("INVALID_QUANTITY");
	    }

	    List<MedicineInventory> inventories =
	            medicineInventoryRepo
	                .findByMedicineIdAndExpiryDateAfterAndQuantityGreaterThanAndStatusNotOrderByExpiryDateAsc(
	                        medicineId, LocalDate.now(), 0,StockStatus.EXPIRED
	                );

	    if (inventories.isEmpty()) {
	        throw new HmsException("OUT_OF_STOCK");
	    }

	    // 🔹 PHASE 1: availability check (NO DB WRITES)
	    int totalAvailable = inventories.stream()
	            .mapToInt(MedicineInventory::getQuantity)
	            .sum();

	    if (totalAvailable < quantity) {
	        throw new HmsException(
	                "INSUFFICIENT_STOCK: Requested " + quantity +
	                ", Available " + totalAvailable
	        );
	    }

	    // 🔹 PHASE 2: FIFO selling (SAFE to write)
	    int remainingQuantity = quantity;
	    StringBuilder batchDetails = new StringBuilder();

	    for (MedicineInventory inventory : inventories) {

	        if (remainingQuantity == 0) break;

	        int available = inventory.getQuantity();
	        int sold = Math.min(available, remainingQuantity);

	        inventory.setQuantity(available - sold);
	        medicineService.removeStock(medicineId, sold);

	        batchDetails.append(
	                String.format("Batch %s: %d units\n",
	                        inventory.getBatchNo(), sold)
	        );
	        remainingQuantity -= sold;
	        medicineInventoryRepo.save(inventory);
	    }
	    return batchDetails.toString();
	}


}

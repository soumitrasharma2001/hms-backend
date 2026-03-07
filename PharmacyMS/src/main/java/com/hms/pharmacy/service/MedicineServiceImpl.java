package com.hms.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.MedicineDTO;
import com.hms.pharmacy.entity.Medicine;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService{
	private final MedicineRepository medicineRepo;

	@Override
	public Long addMedicine(MedicineDTO medicineDTO) throws HmsException {
		Optional<Medicine> optional=medicineRepo
				.findByNameIgnoreCaseAndDosageIgnoreCase(medicineDTO.getName(), medicineDTO.getDosage());
		if(optional.isPresent())
			throw new HmsException("MEDICINE_ALREADY_EXISTS");
		medicineDTO.setStock(0);
		medicineDTO.setCreatedAt(LocalDateTime.now());
		return medicineRepo.save(medicineDTO.toEntity()).getId();
	}

	@Override
	public MedicineDTO getMedicineById(Long id) throws HmsException {
		return medicineRepo.findById(id).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"))
				.toDTO();
	}

	@Override
	public void updateMedicine(MedicineDTO medicineDTO) throws HmsException {
		Medicine existingMedicine=medicineRepo.findById(medicineDTO.getId())
				.orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
		if(!(medicineDTO.getName().equalsIgnoreCase(existingMedicine.getName())
				&& medicineDTO.getDosage().equalsIgnoreCase(existingMedicine.getDosage()))) {
			Optional<Medicine> op=medicineRepo
					.findByNameIgnoreCaseAndDosageIgnoreCase(medicineDTO.getName(), medicineDTO.getDosage());
			if(op.isPresent())
				throw new HmsException("MEDICINE_ALREADY_EXISTS");
		}
		existingMedicine.setName(medicineDTO.getName());
		existingMedicine.setDosage(medicineDTO.getDosage());
		existingMedicine.setCategory(medicineDTO.getCategory());
		existingMedicine.setType(medicineDTO.getType());
		existingMedicine.setManufacturer(medicineDTO.getManufacturer());
		existingMedicine.setUnitPrice(medicineDTO.getUnitPrice());
		medicineRepo.save(existingMedicine);
	}

	@Override
	public List<MedicineDTO> getAllMedicines() throws HmsException {
		return ((List<Medicine>)medicineRepo.findAll()).stream()
				.map(Medicine::toDTO).toList();
	}

	@Override
	public Integer getStockCountById(Long id) throws HmsException {
		return medicineRepo.findStockById(id).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
	}

	@Override
	public Integer addStock(Long id, Integer quantity) throws HmsException {
		Medicine medicine=medicineRepo.findById(id)
				.orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
		medicine.setStock(medicine.getStock()!=null?medicine.getStock()+quantity:quantity);
		medicineRepo.save(medicine);
		return medicine.getStock();
	}

	public Integer removeStock(Long id, Integer quantity) throws HmsException {
	    if (quantity == null || quantity <= 0) {
	        throw new HmsException("INVALID_QUANTITY");
	    }
	    Medicine medicine = medicineRepo.findById(id)
	            .orElseThrow(() -> new HmsException("MEDICINE_NOT_FOUND"));
	    int currentStock = medicine.getStock() == null ? 0 : medicine.getStock();
	    if (currentStock < quantity) {
	        throw new HmsException(
	                "INSUFFICIENT_STOCK: Available " + currentStock
	        );
	    }
	    medicine.setStock(currentStock - quantity);
	    return medicine.getStock();
	}
}

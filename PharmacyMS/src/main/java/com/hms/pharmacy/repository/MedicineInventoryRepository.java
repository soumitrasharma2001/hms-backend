package com.hms.pharmacy.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.hms.pharmacy.entity.MedicineInventory;
import com.hms.pharmacy.entity.StockStatus;

@Repository
public interface MedicineInventoryRepository extends CrudRepository<MedicineInventory, Long>{

	List<MedicineInventory> findByExpiryDateBefore(LocalDate now);
	List<MedicineInventory> 
	findByMedicineIdAndExpiryDateAfterAndQuantityGreaterThanAndStatusNotOrderByExpiryDateAsc(
			Long medicineId,
			LocalDate date,Integer quantity,StockStatus status);

}

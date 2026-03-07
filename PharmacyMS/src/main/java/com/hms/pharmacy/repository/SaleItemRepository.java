package com.hms.pharmacy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.hms.pharmacy.entity.SaleItem;

@Repository
public interface SaleItemRepository extends CrudRepository<SaleItem, Long>{
	List<SaleItem> findBySaleId(Long saleId);
	List<SaleItem> findByMedicineId(Long medicineId);
}

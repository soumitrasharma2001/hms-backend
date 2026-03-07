package com.hms.appointment.repository;

import java.util.List;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hms.appointment.entity.Medicine;

@Repository
public interface MedicineRepository extends CrudRepository<Medicine, Long>{
	List<Medicine> findAllByPrescription_Id(Long prescriptionId);
	List<Medicine> findAllByPrescription_IdIn(List<Long> prescriptionId);
}

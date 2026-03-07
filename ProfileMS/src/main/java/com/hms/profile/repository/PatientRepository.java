package com.hms.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hms.profile.dto.UserDropdown;
import com.hms.profile.entity.Patient;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

	Optional<Patient> findByEmail(String email);
	Optional<Patient> findByAadharNo(String aadharNo);
	@Query("SELECT p.id AS id, p.name AS name FROM Patient p WHERE p.id in ?1")
	List<UserDropdown> findPatientDropdownsByIds(List<Long> ids);
	
}

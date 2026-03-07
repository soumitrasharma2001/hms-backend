package com.hms.pharmacy.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.pharmacy.dto.MedicineDTO;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.service.MedicineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pharmacy/medicine")
@Validated
@RequiredArgsConstructor
public class MedicineController {
	private final MedicineService medicineService;
	
	@PostMapping("/add")
	public ResponseEntity<Long> add(@RequestBody MedicineDTO medicineDTO) throws HmsException{
		return ResponseEntity.status(HttpStatus.CREATED).body(medicineService.addMedicine(medicineDTO));
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<MedicineDTO> getByMedicineById(@PathVariable Long id) throws HmsException{
		return ResponseEntity.ok(medicineService.getMedicineById(id));
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> getByMedicineById(@RequestBody MedicineDTO medicineDTO) throws HmsException{
		medicineService.updateMedicine(medicineDTO);
		return ResponseEntity.ok("Medicine Updated");
	}
	
	@GetMapping("/getAllMedicines")
	public ResponseEntity<List<MedicineDTO>> getAllMedicines() throws HmsException{
		return ResponseEntity.ok(medicineService.getAllMedicines());
	}
}
